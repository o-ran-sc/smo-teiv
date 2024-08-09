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
package org.oran.smo.teiv.schema;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.oran.smo.teiv.exposure.spi.Module;

import static org.oran.smo.teiv.schema.DataType.BIGINT;
import static org.oran.smo.teiv.schema.DataType.CONTAINER;
import static org.oran.smo.teiv.schema.DataType.DECIMAL;
import static org.oran.smo.teiv.schema.DataType.GEOGRAPHIC;
import static org.oran.smo.teiv.schema.DataType.INTEGER;
import static org.oran.smo.teiv.schema.DataType.PRIMITIVE;
import static org.oran.smo.teiv.utils.TiesConstants.TEIV_DOMAIN;

public class MockSchemaLoader extends SchemaLoader {

    @Override
    protected void loadBidiDbNameMapper() {
        Map<String, String> hashedNames = new HashMap<>();
        Map<String, String> reverseHashedNames = new HashMap<>();

        List<List<String>> hashInfo = extractModelInfoFromSqlFile("hash_info");
        hashInfo.forEach(l -> {
            hashedNames.put(l.get(0), l.get(1));
            reverseHashedNames.put(l.get(1), l.get(0));
        });

        BidiDbNameMapper.initialize(hashedNames, reverseHashedNames);
    }

    @Override
    protected void loadModules() {
        List<Module> modules = new ArrayList<>();

        List<List<String>> moduleReference = extractModelInfoFromSqlFile("module_reference");
        moduleReference.forEach(l -> {
            String domain = l.get(2);
            if (domain.equals("\\N")) {
                domain = null;
            }

            String name = l.get(0);
            String namespace = l.get(1);
            String content = l.get(5);
            final Module.ModuleBuilder moduleBuilder = Module.builder().name(name).namespace(namespace).domain(domain)
                    .content(content);
            final String includedModuleNames = l.get(3).replaceAll("[\\[\\]\"\\s]", "");
            if (!includedModuleNames.isEmpty()) {
                moduleBuilder.includedModuleNames(List.of(includedModuleNames.split(","))).build();
            }
            modules.add(moduleBuilder.build());
        });

        Map<String, Module> moduleMap = new HashMap<>();
        modules.forEach(module -> moduleMap.put(module.getName(), module));
        moduleMap.put(TEIV_DOMAIN, Module.builder().name(TEIV_DOMAIN).namespace(TEIV_DOMAIN).domain(TEIV_DOMAIN)
                .includedModuleNames(moduleMap.keySet()).build());
        SchemaRegistry.initializeModules(moduleMap);
    }

    @Override
    protected void loadEntityTypes() {
        List<EntityType> entityTypes = new ArrayList<>();

        List<List<String>> entityInfo = extractModelInfoFromSqlFile("entity_info");
        entityInfo.forEach(l -> {
            String storedAt = l.get(0);
            String name = l.get(1);
            String moduleReferenceName = l.get(2);
            entityTypes.add(EntityType.builder().name(name).tableName(storedAt).module(SchemaRegistry.getModuleByName(
                    moduleReferenceName)).fields(extractDataFieldsFromSqlFile(storedAt)).build());
        });

        SchemaRegistry.initializeEntityTypes(entityTypes);
    }

    @Override
    protected void loadRelationTypes() {
        List<RelationType> relationTypes = new ArrayList<>();
        List<List<String>> relationshipInfo = extractModelInfoFromSqlFile("relationship_info");
        relationshipInfo.forEach(l -> {
            String name = l.get(0);
            String aSideAssociationName = l.get(1);
            String aSideMOType = l.get(2);
            long aSideMinCardinality = Long.parseLong(l.get(4));
            long aSideMaxCardinality = Long.parseLong(l.get(5));
            String bSideAssociationName = l.get(6);
            String bSideMOType = l.get(7);
            long bSideMinCardinality = Long.parseLong(l.get(9));
            long bSideMaxCardinality = Long.parseLong(l.get(10));
            RelationshipDataLocation relationshipDataLocation = RelationshipDataLocation.valueOf(l.get(13));
            boolean connectSameEntity = Boolean.parseBoolean(l.get(12));
            String storedAt = l.get(14);
            String moduleReferenceName = l.get(15);

            relationTypes.add(RelationType.builder().name(name).aSideAssociation(getRelationshipAssociation(
                    aSideAssociationName, aSideMinCardinality, aSideMaxCardinality)).aSide(SchemaRegistry
                            .getEntityTypeByName(aSideMOType)).bSideAssociation(getRelationshipAssociation(
                                    bSideAssociationName, bSideMinCardinality, bSideMaxCardinality)).bSide(SchemaRegistry
                                            .getEntityTypeByName(bSideMOType)).connectsSameEntity(connectSameEntity)
                    .relationshipStorageLocation(relationshipDataLocation).tableName(storedAt).module(SchemaRegistry
                            .getModuleByName(moduleReferenceName)).build());
        });

        SchemaRegistry.initializeRelationTypes(relationTypes);
    }

    private Association getRelationshipAssociation(String associationName, long minCardinality, long maxCardinality) {
        return Association.builder().name((associationName)).minCardinality(minCardinality).maxCardinality(maxCardinality)
                .build();
    }

    private List<List<String>> extractModelInfoFromSqlFile(String tableName) {
        List<List<String>> modelInfo = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(
                    "src/test/resources/pgsqlschema/01_init-oran-smo-teiv-model-v1.sql"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("COPY ties_model." + tableName)) {
                    while (!(line = reader.readLine()).startsWith("\\.")) {
                        List<String> l = List.of(line.split("\t"));
                        modelInfo.add(l);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return modelInfo;
    }

    private Map<String, DataType> extractDataFieldsFromSqlFile(String tableName) {
        Map<String, DataType> dataFields = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(
                    "src/test/resources/pgsqlschema/00_init-oran-smo-teiv-data-v1.sql"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("CREATE TABLE IF NOT EXISTS ties_data.\"" + tableName + "\"")) {
                    while (!(line = reader.readLine()).startsWith(");")) {
                        List<String> l = List.of(line.trim().replace("\t\t\t", "\t").replaceAll("[\",]", "").split("\t"));
                        String fieldName = l.get(0);
                        String dataType = l.get(1);
                        switch (dataType) {
                            case "TEXT" -> dataFields.put(fieldName, PRIMITIVE);
                            case "INTEGER" -> dataFields.put(fieldName, INTEGER);
                            case "BIGINT" -> dataFields.put(fieldName, BIGINT);
                            case "DECIMAL" -> dataFields.put(fieldName, DECIMAL);
                            case "jsonb" -> dataFields.put(fieldName, CONTAINER);
                            case "geography" -> dataFields.put(fieldName, GEOGRAPHIC);
                        }
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dataFields;
    }
}
