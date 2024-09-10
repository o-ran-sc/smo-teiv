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
package org.oran.smo.teiv.pgsqlgenerator.schema.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.oran.smo.teiv.pgsqlgenerator.HashInfoEntity;
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
import org.oran.smo.teiv.pgsqlgenerator.schema.SchemaGenerator;
import org.oran.smo.teiv.pgsqlgenerator.schema.Table;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ModelSchemaGenerator extends SchemaGenerator {
    @Value("${schema.model.output}")
    private String modelOutputFileName;
    @Value("${schema.model.skeleton}")
    private String skeletonModelSchemaFileClassPath;
    @Value("${schema.model.baseline}")
    private String baselineModelSchemaFileClassPath;
    @Value("${schema.model.temp-baseline}")
    private String tempBaselineModelSchemaFileClassPath;
    @Value("${green-field-installation}")
    private boolean isGreenFieldInstallation;

    private final HashInfoDataGenerator hashInfoDataGenerator;
    private final BackwardCompatibilityChecker backwardCompatibilityChecker;

    @Override
    protected void prepareSchema() {
        try {
            Resource skeletonResource = new ClassPathResource(skeletonModelSchemaFileClassPath);
            File newGeneratedModelFile = new File(modelOutputFileName);
            FileHelper.copyResourceToFile(skeletonResource, newGeneratedModelFile);
            if (!isGreenFieldInstallation) {
                Resource baselineResource = new ClassPathResource(baselineModelSchemaFileClassPath);
                File modelBaseline = new File(tempBaselineModelSchemaFileClassPath);
                FileHelper.copyResourceToFile(baselineResource, modelBaseline);
            }
            this.schema = newGeneratedModelFile;
        } catch (IOException exception) {
            throw PgSchemaGeneratorException.prepareBaselineException("ties.model", exception);
        }
    }

    @Override
    protected void setSqlStatements(List<Module> modules, List<Entity> entities, List<Relationship> relationships) {
        if (!isGreenFieldInstallation) {
            // Get relationships from baseline sql
            List<Relationship> relFromBaselineSql = SchemaParser.extractFromModelBaseline(
                    tempBaselineModelSchemaFileClassPath);
            // Check for NBCs
            backwardCompatibilityChecker.checkForNBCChangesInModel(relFromBaselineSql, relationships);
        }
        StringBuilder tiesModelSql = new StringBuilder();

        List<HashInfoEntity> hashInfoList = new ArrayList<>(hashInfoDataGenerator.getHashInfoRowsList().stream().toList());
        hashInfoList.sort(Comparator.comparing(HashInfoEntity::getName));

        tiesModelSql.append(prepareCopyStatement(hashInfoList));
        tiesModelSql.append(prepareCopyStatement(modules));
        tiesModelSql.append(prepareCopyStatement(entities));
        tiesModelSql.append(prepareCopyStatement(relationships));
        tiesModelSql.append(";\n").append("\nCOMMIT;");
        this.sqlStatements = tiesModelSql.toString();
    }

    private StringBuilder prepareCopyStatement(List<? extends Table> table) {
        StringBuilder copyStatement = new StringBuilder();
        if (!table.isEmpty()) {
            copyStatement.append("COPY ties_model.").append(table.get(0).getTableName()).append(table.get(0)
                    .getColumnsForCopyStatement()).append(" FROM stdin;\n");
            table.forEach(table1 -> copyStatement.append(table1.getRecordForCopyStatement()));
            copyStatement.append("\\.\n\n");
        }
        return copyStatement;
    }
}
