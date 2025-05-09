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
package org.oran.smo.teiv.pgsqlgenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.oran.smo.teiv.pgsqlgenerator.graphgenerator.EntityGraphGenerator;
import org.oran.smo.teiv.pgsqlgenerator.graphgenerator.EntityGraphGeneratorUml;
import org.oran.smo.teiv.pgsqlgenerator.graphgenerator.RelationshipGraphGeneratorUml;
import org.oran.smo.teiv.pgsqlgenerator.schema.consumerdata.ConsumerDataSchemaGenerator;
import org.oran.smo.teiv.pgsqlgenerator.schema.data.DataSchemaGenerator;
import org.oran.smo.teiv.pgsqlgenerator.schema.groups.GroupsSchemaGenerator;
import org.oran.smo.teiv.pgsqlgenerator.schema.model.ModelSchemaGenerator;
import org.oran.smo.teiv.pgsqlgenerator.graphgenerator.RelationshipGraphGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ResourceUtils;

@RequiredArgsConstructor
@Component
public class Processor {
    private final YangParser yangParser;
    private final YangModelProcessor yangModelProcessor;
    private final DataSchemaGenerator dataSchemaGenerator;
    private final ModelSchemaGenerator modelSchemaGenerator;
    private final ConsumerDataSchemaGenerator consumerDataSchemaGenerator;
    private final GroupsSchemaGenerator groupSchemaGenerator;
    private final RelationshipGraphGenerator relationshipGraphGenerator;
    private final RelationshipGraphGeneratorUml relationshipGraphGeneratorUml;
    private final EntityGraphGenerator entityGraphGenerator;
    private final EntityGraphGeneratorUml entityGraphGeneratorUml;
    @Value("${yang-model.source}")
    private String yangModelDirectory;
    @Value("${yang-model.external.source}")
    private String yangModelExternalDirectory;

    @PostConstruct
    void process() throws IOException {
        List<File> pathToImplementing;
        File externalYangFiles = new File(yangModelExternalDirectory);
        if (externalYangFiles.exists() && externalYangFiles.isDirectory()) {
            pathToImplementing = Collections.singletonList(ResourceUtils.getFile(yangModelExternalDirectory));
        } else {
            pathToImplementing = FileHelper.getYangFilesFromResource(yangModelDirectory);
        }

        // Yang model validation should be added here

        // retrieve info from yang parser
        List<Module> modules = yangParser.returnAllModuleReferences();

        // retrieve info from the model
        List<Entity> entitiesFromModelService = yangModelProcessor.getEntitiesAndAttributesFromYang(pathToImplementing);
        List<Module> moduleReferences = storeRelatedModuleRefsFromIncludedModules(entitiesFromModelService, modules);
        List<Relationship> relationshipsFromModelService = yangModelProcessor.getRelationshipsFromYang(pathToImplementing);

        relationshipGraphGenerator.generate(relationshipsFromModelService, entitiesFromModelService);
        relationshipGraphGeneratorUml.generate(relationshipsFromModelService, entitiesFromModelService);
        entityGraphGenerator.generate(entitiesFromModelService);
        entityGraphGeneratorUml.generate(entitiesFromModelService);
        dataSchemaGenerator.generate(moduleReferences, entitiesFromModelService, relationshipsFromModelService);
        modelSchemaGenerator.generate(moduleReferences, entitiesFromModelService, relationshipsFromModelService);
        consumerDataSchemaGenerator.generate(moduleReferences, entitiesFromModelService, relationshipsFromModelService);
        groupSchemaGenerator.generate(moduleReferences, entitiesFromModelService, relationshipsFromModelService);
    }

    /**
     * Included modules stores all the imported modules in a yang module
     * This function extracts and stores only the related module names from provided entities.
     *
     * @param entities
     *     The list of entities.
     * @param moduleRefFromYangParser
     *     The list of ModuleReferences obtained from the YANG parser.
     * @return The list of ModuleReferences.
     */
    public static List<Module> storeRelatedModuleRefsFromIncludedModules(List<Entity> entities,
            List<Module> moduleRefFromYangParser) {

        List<String> moduleRefForAllEntities = entities.stream().map(Entity::getModuleReferenceName).toList();
        for (Module module : moduleRefFromYangParser) {
            List<String> includedModules = new ArrayList<>(module.getIncludedModules());
            includedModules.removeIf(modelRef -> !moduleRefForAllEntities.contains(modelRef));
            module.setIncludedModules(includedModules);
        }
        moduleRefFromYangParser.sort(Comparator.comparing(Module::getName));
        return moduleRefFromYangParser;
    }
}
