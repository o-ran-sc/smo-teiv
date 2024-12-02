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
package org.oran.smo.teiv.pgsqlgenerator.schema.groups;

import org.oran.smo.teiv.pgsqlgenerator.Entity;
import org.oran.smo.teiv.pgsqlgenerator.FileHelper;
import org.oran.smo.teiv.pgsqlgenerator.Module;
import org.oran.smo.teiv.pgsqlgenerator.PgSchemaGeneratorException;
import org.oran.smo.teiv.pgsqlgenerator.Relationship;
import org.oran.smo.teiv.pgsqlgenerator.schema.BackwardCompatibilityChecker;
import org.oran.smo.teiv.pgsqlgenerator.schema.SchemaGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class GroupsSchemaGenerator extends SchemaGenerator {
    private final BackwardCompatibilityChecker backwardCompatibilityChecker;

    @Value("${schema.groups.baseline}")
    private String baselineGroupSchema;
    @Value("${schema.groups.output}")
    private String outputGroupSchema;
    @Value("${schema.groups.skeleton}")
    private String skeletonGroupSchema;

    @Override
    protected void prepareSchema() {
        try {
            Resource skeletonResource = new ClassPathResource(skeletonGroupSchema);
            final Path destinationPath = Paths.get(outputGroupSchema);
            backwardCompatibilityChecker.checkForNBCChangesInConsumerAndGroupsSchema(baselineGroupSchema, destinationPath
                    .toFile().getAbsolutePath());
            FileHelper.copyResourceToFile(skeletonResource, destinationPath.toFile());
            this.schema = destinationPath.toFile();
        } catch (IOException exception) {
            throw PgSchemaGeneratorException.prepareBaselineException("ties.group", exception);
        }
    }

    @Override
    protected void setSqlStatements(List<Module> modules, List<Entity> entities, List<Relationship> relationships) {
        //DO-NOTHING
    }
}
