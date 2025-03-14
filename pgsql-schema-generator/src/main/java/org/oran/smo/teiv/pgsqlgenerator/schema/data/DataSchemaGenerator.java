/*
 *  ============LICENSE_START=======================================================
 *  Copyright (C) 2024 Ericsson
 *  Modifications Copyright (C) 2024 OpenInfra Foundation Europe
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
package org.oran.smo.teiv.pgsqlgenerator.schema.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.oran.smo.teiv.pgsqlgenerator.schema.BackwardCompatibilityChecker;
import org.oran.smo.teiv.pgsqlgenerator.FileHelper;
import org.oran.smo.teiv.pgsqlgenerator.schema.SchemaParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import org.oran.smo.teiv.pgsqlgenerator.Entity;
import org.oran.smo.teiv.pgsqlgenerator.Module;
import org.oran.smo.teiv.pgsqlgenerator.PgSchemaGeneratorException;
import org.oran.smo.teiv.pgsqlgenerator.Relationship;
import org.oran.smo.teiv.pgsqlgenerator.Table;
import org.oran.smo.teiv.pgsqlgenerator.schema.SchemaGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataSchemaGenerator extends SchemaGenerator {
    @Value("${schema.data.baseline}")
    private String baselineDataSchema;
    @Value("${schema.data.output}")
    private String dataSchemaFileName;
    @Value("${schema.data.skeleton}")
    private String skeletonDataSchema;
    @Value("${green-field-installation}")
    private boolean isGreenFieldInstallation;
    @Value("${custom-query-execution}")
    private boolean isCustomQueryExecutionEnabled;
    @Value("${schema.data.custom-sql-script}")
    private String customSqlScripts;

    private final ModelComparator modelComparator;
    private final DataSchemaHelper dataSchemaHelper;
    private final TableBuilder tableBuilder;
    private final BackwardCompatibilityChecker backwardCompatibilityChecker;

    @Override
    protected void prepareSchema() {
        try {
            Resource baselineResource = new ClassPathResource(baselineDataSchema);
            File newDataSchema = new File(dataSchemaFileName);
            if (isGreenFieldInstallation || !baselineResource.exists()) {
                log.info("Baseline DOES NOT EXISTS!!");
                Resource skeletonResource = new ClassPathResource(skeletonDataSchema);
                FileHelper.copyResourceToFile(skeletonResource, newDataSchema);
            } else {
                log.info("Baseline EXISTS!!");
                Path sourcePath = baselineResource.getFile().toPath();
                List<String> stmts = Files.readAllLines(sourcePath);
                List<String> analyzeAndCommitExcludedStmts = new ArrayList<>();
                for (String line : stmts) {
                    if (line.startsWith("ANALYZE") || line.equals("COMMIT;")) {
                        break;
                    }
                    analyzeAndCommitExcludedStmts.add(line);
                }

                Files.write(Paths.get(dataSchemaFileName), analyzeAndCommitExcludedStmts, StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING);
            }
            this.schema = newDataSchema;
        } catch (IOException exception) {
            throw PgSchemaGeneratorException.prepareBaselineException("teiv.data", exception);
        }
    }

    @Override
    protected void setSqlStatements(List<Module> modules, List<Entity> entities, List<Relationship> relationships) {
        // Create table from entities and relationships
        List<Table> tablesFromModelSvc = tableBuilder.getTables(entities, relationships);
        List<Table> tablesForNbcCheck = new ArrayList<>();
        try {
            Resource skeletonResource = new ClassPathResource(skeletonDataSchema);
            File tmpSkeletonFile = new File("target/tmpSkeletonData.sql");
            FileHelper.copyResourceToFile(skeletonResource, tmpSkeletonFile);
            List<Table> tablesFromSkeleton = SchemaParser.extractDataFromBaseline(tmpSkeletonFile.getAbsolutePath());
            tablesForNbcCheck.addAll(tablesFromSkeleton);
        } catch (IOException exception) {
            throw PgSchemaGeneratorException.prepareBaselineException("teiv.data", exception);
        }
        // Get tables from baseline sql
        List<Table> tablesFromBaselineSql = isGreenFieldInstallation ?
                List.of() :
                SchemaParser.extractDataFromBaseline(baselineDataSchema);
        // Check for NBCs
        tablesForNbcCheck.addAll(tablesFromModelSvc);
        backwardCompatibilityChecker.checkForNBCChangesInData(tablesFromBaselineSql, tablesForNbcCheck);
        // Compare and store differences in tables from baseline sql with tables from model service
        Map<String, List<Table>> differences = modelComparator.identifyDifferencesInBaselineAndGenerated(tablesFromModelSvc,
                tablesFromBaselineSql);
        // Generate schema from differences
        StringBuilder generatedSchema = dataSchemaHelper.generateSchemaFromDifferences(differences);
        //add custome sql query if cutom flag is enabled and greenfield is disabled
        if (isCustomQueryExecutionEnabled && !isGreenFieldInstallation) {
            generatedSchema.append(appendCustomQueries());
            generatedSchema.append("\n");
        }
        generatedSchema.append(generateAnalyzeTableStatement(tablesFromModelSvc));
        generatedSchema.append("COMMIT;\n");
        this.sqlStatements = generatedSchema.toString();
    }

    private StringBuilder appendCustomQueries() {
        StringBuilder customSqlQueries = new StringBuilder();
        File customSqlFile = new File(customSqlScripts);
        try {
            Path customQueryFilePath = customSqlFile.toPath();
            try (Stream<String> lines = Files.lines(customQueryFilePath)) {
                lines.forEach(line -> {
                    customSqlQueries.append(line).append("\n");
                });
            }
        } catch (IOException exception) {
            throw PgSchemaGeneratorException.readCustomSqlFileException("teiv.data", exception);
        }
        return customSqlQueries;
    }

    private StringBuilder generateAnalyzeTableStatement(List<Table> tablesFromModelSvc) {
        StringBuilder analyzeTableStmt = new StringBuilder();
        tablesFromModelSvc.forEach(table -> analyzeTableStmt.append(String.format("ANALYZE teiv_data.\"%s\";%n%n", table
                .getName())));
        return analyzeTableStmt;
    }

}
