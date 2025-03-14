/*
 *  ============LICENSE_START=======================================================
 *  Copyright (C) 2024 Ericsson
 *  Modifications Copyright (C) 2024-2025 OpenInfra Foundation Europe
 *  ================================================================================
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  SPDX-License-Identifier: Apache-2.0
 *  ============LICENSE_END=========================================================
 */
package org.oran.smo.teiv.pgsqlgenerator.schema;

import static org.oran.smo.teiv.pgsqlgenerator.Constants.ALTER;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.oran.smo.teiv.pgsqlgenerator.Column;
import org.oran.smo.teiv.pgsqlgenerator.IndexType;
import org.oran.smo.teiv.pgsqlgenerator.PostgresConstraint;
import org.oran.smo.teiv.pgsqlgenerator.ForeignKeyConstraint;
import org.oran.smo.teiv.pgsqlgenerator.NotNullConstraint;
import org.oran.smo.teiv.pgsqlgenerator.PgSchemaGeneratorException;
import org.oran.smo.teiv.pgsqlgenerator.PostgresIndex;
import org.oran.smo.teiv.pgsqlgenerator.PrimaryKeyConstraint;
import org.oran.smo.teiv.pgsqlgenerator.Table;
import org.oran.smo.teiv.pgsqlgenerator.UniqueConstraint;
import org.oran.smo.teiv.pgsqlgenerator.Relationship;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Extracts entities and Columns from a baseline schema chart file. The extracted data is stored as a list of entities
 * containing their respective
 * Columns.
 */
@Slf4j
@UtilityClass
public class SchemaParser {

    /**
     * Extracts data from model.sql baseline schema chart file.
     *
     * @return List of identified entities with their respective Columns
     */
    public static List<Relationship> extractFromModelBaseline(String filePath) {
        List<Relationship> identifiedRelationships = new ArrayList<>();
        File baseline = new File(filePath);

        if (baseline.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(baseline, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith("COPY teiv_model.relationship_info") && !line.startsWith("\\.")) {
                        line = br.readLine();
                        List<String> relData = Arrays.asList(line.replace("\"", "").split("\\s+"));
                        identifiedRelationships.add(Relationship.builder().name(relData.get(0)).aSideAssociationName(relData
                                .get(1)).aSideMOType(relData.get(2)).aSideMinCardinality(Long.parseLong(relData.get(4)))
                                .aSideMaxCardinality(Long.parseLong(relData.get(5))).bSideAssociationName(relData.get(6))
                                .bSideMOType(relData.get(7)).bSideMinCardinality(Long.parseLong(relData.get(9)))
                                .bSideMaxCardinality(Long.parseLong(relData.get(10))).associationKind(relData.get(11))
                                .connectSameEntity(Boolean.parseBoolean(relData.get(12))).relationshipDataLocation(relData
                                        .get(13)).moduleReferenceName(relData.get(15)).build());
                    }
                }
            } catch (IOException exception) {
                throw PgSchemaGeneratorException.readBaselineException("teiv.data", exception);
            }
        }
        return identifiedRelationships;
    }

    /**
     * Extracts data from data.sql baseline schema chart file.
     *
     * @return List of identified entities with their respective Columns
     */
    public static List<Table> extractDataFromBaseline(String filePath) {
        List<Table> identifiedTables = new ArrayList<>();
        File baseline = new File(filePath);

        if (baseline.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(baseline, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.contains("ALTER TABLE ONLY") && line.contains("SET DEFAULT")) {
                        extractDefaultValueFromBaseline(line, identifiedTables);
                    } else if ((line.contains("CREATE TABLE") || line.contains("ALTER TABLE")) && !line.startsWith("'")) {
                        extractTableColumns(line, identifiedTables, br);
                    } else if (line.contains("SELECT") && line.contains("teiv_data.create_constraint_if_not_exists")) {
                        extractConstraints(identifiedTables, br);
                    } else if (line.startsWith("CREATE INDEX IF NOT EXISTS")) {
                        extractIndex(line, identifiedTables);
                    }
                }
            } catch (IOException exception) {
                throw PgSchemaGeneratorException.readBaselineException("teiv.data", exception);
            }
        }
        return identifiedTables;
    }

    private static void extractConstraints(List<org.oran.smo.teiv.pgsqlgenerator.Table> identifiedTables, BufferedReader br)
            throws IOException {

        String tableToAddConstraintTo = br.readLine().trim().replace(",", "").replace("'", "");
        String constraintName = br.readLine().trim().replace(",", "").replace("'", "");

        String alterStatement = br.readLine();
        String columnToAddForeignKeyTo = StringUtils.substringBetween(alterStatement, "(", ")").replace("\"", "");

        identifiedTables.stream().filter(table -> table.getName().equals(tableToAddConstraintTo)).findFirst().flatMap(
                table -> table.getColumns().stream().filter(column -> column.getName().equals(columnToAddForeignKeyTo))
                        .findFirst()).ifPresent(column -> {
                            Collection<PostgresConstraint> postgresConstraintCollection = new ArrayList<>();
                            if (column.getPostgresConstraints() != null) {
                                postgresConstraintCollection.addAll(column.getPostgresConstraints());
                            }
                            if (alterStatement.contains("UNIQUE")) {
                                postgresConstraintCollection.add(UniqueConstraint.builder().constraintName(constraintName)
                                        .tableName(tableToAddConstraintTo).columnToAddConstraintTo(columnToAddForeignKeyTo)
                                        .build());
                            } else if (alterStatement.contains("PRIMARY KEY")) {
                                postgresConstraintCollection.add(PrimaryKeyConstraint.builder().constraintName(
                                        constraintName).tableName(tableToAddConstraintTo).columnToAddConstraintTo(
                                                columnToAddForeignKeyTo).build());
                            } else if (alterStatement.contains("NOT NULL")) {
                                postgresConstraintCollection.add(NotNullConstraint.builder().constraintName(constraintName)
                                        .tableName(tableToAddConstraintTo).columnToAddConstraintTo(columnToAddForeignKeyTo)
                                        .build());
                            } else if (alterStatement.contains("FOREIGN KEY")) {
                                String substringFromLastTeivData = alterStatement.substring(StringUtils.lastIndexOf(
                                        alterStatement, "teiv_data.\""));
                                String referenceTable = StringUtils.substringBetween(substringFromLastTeivData, "\"", "\"");
                                String referenceTableColumn = alterStatement.substring(StringUtils.lastIndexOf(
                                        alterStatement, "(") + 1, StringUtils.lastIndexOf(alterStatement, ")")).replace(
                                                "\"", "").trim();
                                postgresConstraintCollection.add(ForeignKeyConstraint.builder().constraintName(
                                        constraintName).tableName(tableToAddConstraintTo).columnToAddConstraintTo(
                                                columnToAddForeignKeyTo).referencedTable(referenceTable).referencedColumn(
                                                        referenceTableColumn).build());
                            }
                            column.setPostgresConstraints(postgresConstraintCollection);
                        });

    }

    private static void extractDefaultValueFromBaseline(String line,
            List<org.oran.smo.teiv.pgsqlgenerator.Table> identifiedTables) {
        String[] valuesInQuotes = StringUtils.substringsBetween(line, "\"", "\"");

        identifiedTables.forEach(table -> {
            if (table.getName().equals(valuesInQuotes[0])) {
                table.getColumns().forEach(column -> {
                    if (column.getName().equals(valuesInQuotes[1])) {
                        String[] defaultValueFromBaseline = StringUtils.substringsBetween(line, "'", "'");
                        column.setDefaultValue(defaultValueFromBaseline[0]);
                    }
                });
            }
        });
    }

    private static void extractTableColumns(String line, List<org.oran.smo.teiv.pgsqlgenerator.Table> identifiedTables,
            BufferedReader br) throws IOException {
        String[] tableName = line.split("\"");
        List<Column> identifiedColumns = new ArrayList<>();

        if (line.contains("ADD COLUMN")) {
            extractColumnsFromAlterStatements(line, identifiedTables);
        } else {
            extractColumnsFromCreateTable(tableName[1], identifiedTables, br, identifiedColumns);
        }
    }

    private static void extractColumnsFromAlterStatements(String line,
            List<org.oran.smo.teiv.pgsqlgenerator.Table> identifiedTables) {
        line = line.replace(";", "");
        for (String str : line.split("ADD COLUMN")) {
            str = str.trim();
            String[] arr = StringUtils.substringsBetween(line, "\"", "\"");
            final String finalStr = str;
            identifiedTables.forEach(table -> {
                if (table.getName().equals(arr[0]) && !finalStr.contains(ALTER)) {
                    table.getColumns().add(Column.builder().name(arr[1]).dataType(finalStr.substring(finalStr.lastIndexOf(
                            " ") + 1)).build());
                }
            });
        }
    }

    private static void extractColumnsFromCreateTable(String entityName,
            List<org.oran.smo.teiv.pgsqlgenerator.Table> identifiedTables, BufferedReader br,
            List<Column> identifiedColumns) throws IOException {
        String line = br.readLine();
        while (!line.contains(");")) {
            line = line.replace(",", "");
            List<String> column = Arrays.asList(line.split("\\s+"));
            String columnName = column.get(1).replace("\"", "");
            String dataType = column.get(2).replace("\"", "");
            // constraints are added later
            identifiedColumns.add(Column.builder().name(columnName).dataType(dataType).build());
            line = br.readLine();
        }
        identifiedTables.add(Table.builder().name(entityName).columns(identifiedColumns).build());
    }

    private static void extractIndex(String line, List<org.oran.smo.teiv.pgsqlgenerator.Table> identifiedTables)
            throws IOException {
        String[] valuesInQuotes = StringUtils.substringsBetween(line, "\"", "\"");
        String indexName = valuesInQuotes[0];
        String tableToAddIndexTo = valuesInQuotes[1];
        String columnToAddIndexTo = valuesInQuotes[2];

        List<PostgresIndex> postgresIndexList = new ArrayList<>();

        identifiedTables.stream().filter(table -> table.getName().equals(tableToAddIndexTo)).findFirst().flatMap(
                table -> table.getColumns().stream().filter(column -> column.getName().equals(columnToAddIndexTo))
                        .findFirst()).ifPresent(column -> {
                            if (!column.getPostgresIndexList().isEmpty()) {
                                postgresIndexList.addAll(column.getPostgresIndexList());
                            }
                            String createIndexStmt = line.replace(indexName, "%s").replace(tableToAddIndexTo, "%s").replace(
                                    columnToAddIndexTo, "%s");
                            for (IndexType indexType : IndexType.values()) {
                                if (indexType.getCreateIndexStmt().equals(createIndexStmt)) {
                                    postgresIndexList.add(PostgresIndex.builder().tableNameToAddIndexTo(tableToAddIndexTo)
                                            .columnNameToAddIndexTo(columnToAddIndexTo).indexName(indexName).indexType(
                                                    indexType).build());
                                }
                            }
                            column.setPostgresIndexList(postgresIndexList);
                        });
    }

}
