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
package org.oran.smo.teiv.pgsqlgenerator.schema.consumerdata;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.oran.smo.teiv.pgsqlgenerator.Entity;
import org.oran.smo.teiv.pgsqlgenerator.Module;
import org.oran.smo.teiv.pgsqlgenerator.PgSchemaGeneratorException;
import org.oran.smo.teiv.pgsqlgenerator.Relationship;
import org.oran.smo.teiv.pgsqlgenerator.schema.BackwardCompatibilityChecker;
import org.oran.smo.teiv.pgsqlgenerator.schema.SchemaGenerator;
import org.springframework.util.ResourceUtils;

@Component
@Slf4j
@RequiredArgsConstructor
public class ConsumerDataSchemaGenerator extends SchemaGenerator {
    private final BackwardCompatibilityChecker backwardCompatibilityChecker;

    @Value("${schema.consumer-data.baseline}")
    private String baselineConsumerDataSchema;
    @Value("${schema.consumer-data.output}")
    private String outputConsumerDataSchema;
    @Value("${schema.consumer-data.skeleton}")
    private String skeletonConsumerDataSchema;

    @Override
    protected void prepareSchema() {
        try {
            final File skeletonFile = ResourceUtils.getFile("classpath:" + skeletonConsumerDataSchema);
            backwardCompatibilityChecker.checkForNBCChangesInConsumerDataSchema(baselineConsumerDataSchema, skeletonFile
                    .getAbsolutePath());

            final Path destinationPath = Paths.get(outputConsumerDataSchema);
            Files.copy(skeletonFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            this.schema = destinationPath.toFile();
        } catch (IOException exception) {
            throw PgSchemaGeneratorException.prepareBaselineException("ties.consumer-data", exception);
        }
    }

    @Override
    protected void setSqlStatements(List<Module> modules, List<Entity> entities, List<Relationship> relationships) {
        //DO-NOTHING
    }
}
