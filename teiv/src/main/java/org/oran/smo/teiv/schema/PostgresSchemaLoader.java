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

import static org.oran.smo.teiv.schema.BidiDbNameMapper.getModelledName;
import static org.oran.smo.teiv.utils.TeivConstants.TEIV_DOMAIN;
import static org.oran.smo.teiv.utils.TeivConstants.TEIV_DATA_SCHEMA;
import static org.oran.smo.teiv.utils.TeivConstants.TEIV_MODEL;
import static org.jooq.impl.DSL.field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.Record3;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.exposure.spi.Module;

@Slf4j
@Component
public class PostgresSchemaLoader extends SchemaLoader {
    private final DSLContext readWriteDataDslContext;
    private final ObjectMapper objectMapper;

    public PostgresSchemaLoader(DSLContext readWriteDataDslContext, ObjectMapper objectMapper) {
        this.readWriteDataDslContext = readWriteDataDslContext;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void loadBidiDbNameMapper() {
        log.debug("Start loading bidirectional DB name mapper");
        SelectJoinStep<Record> records = readWriteDataDslContext.select().from(String.format(TEIV_MODEL, "hash_info"));
        Map<String, String> hash = new HashMap<>();
        Map<String, String> reverseHash = new HashMap<>();
        records.forEach(entry -> {
            hash.put((String) entry.get("name"), (String) entry.get("hashedValue"));
            reverseHash.put((String) entry.get("hashedValue"), (String) entry.get("name"));
        });
        BidiDbNameMapper.initialize(hash, reverseHash);
        log.debug("BidiDBNameMapper initialized successfully");
    }

    @Override
    public void loadModules() throws SchemaLoaderException {
        log.debug("Start loading modules");
        SelectConditionStep<Record> moduleRecords = runMethodSafe(() -> readWriteDataDslContext.select().from(String.format(
                TEIV_MODEL, "module_reference")).where(field("name").isNotNull()));
        Map<String, Module> moduleMap = new HashMap<>();
        for (Record moduleRecord : moduleRecords) {
            JSONB includedModules = (JSONB) moduleRecord.get("includedModules");
            try {
                List<String> modules = objectMapper.readValue(includedModules.data(), List.class);
                Module module = Module.builder().name((String) moduleRecord.get("name")).namespace((String) moduleRecord
                        .get("namespace")).domain((String) moduleRecord.get("domain")).includedModuleNames(modules).content(
                                (String) moduleRecord.get("content")).build();
                moduleMap.put(module.getName(), module);
            } catch (IOException e) {
                log.error("Exception occurred while retrieving included modules.", e);
                throw new SchemaLoaderException("Unable to load modules please check the logs for more details.", e);
            }
        }
        //root domain includes all the available domains.
        moduleMap.put(TEIV_DOMAIN, Module.builder().name(TEIV_DOMAIN).namespace(TEIV_DOMAIN).domain(TEIV_DOMAIN)
                .includedModuleNames(moduleMap.keySet()).build());
        SchemaRegistry.initializeModules(moduleMap);
        log.debug("Modules initialized successfully");
    }

    @Override
    public void loadEntityTypes() throws SchemaLoaderException {
        log.debug("Start loading entities");
        List<EntityType> entityTypes = new ArrayList<>();
        final String tableName = "table_name";
        final String columnName = "column_name";
        SelectConditionStep<Record3<Object, Object, Object>> tableDetails = runMethodSafe(() -> readWriteDataDslContext
                .select(field(tableName), field(columnName), field("udt_name")).from("information_schema.columns").where(
                        field("table_schema").equal(TEIV_DATA_SCHEMA)));

        SelectJoinStep<Record> entityInfoRecords = runMethodSafe(() -> readWriteDataDslContext.select().from(String.format(
                TEIV_MODEL, "entity_info")));

        for (Record entityInfoRecord : entityInfoRecords) {
            String name = (String) entityInfoRecord.get("name");
            final String storedAt = (String) entityInfoRecord.get("storedAt");
            JSONB attributeNames = (JSONB) entityInfoRecord.get("attributeNames");
            List<String> attributeNamesList = new ArrayList<>();
            try {
                attributeNamesList = objectMapper.readValue(attributeNames.data(), List.class);
            } catch (IOException e) {
                log.error("Exception occurred while retrieving attribute name", e);
                throw new SchemaLoaderException("Unable to load entities please check the logs for more details.", e);
            }

            //load attributes
            Map<String, DataType> fields = new HashMap<>();
            tableDetails.stream().filter(record3 -> storedAt.equals(getModelledName((String) record3.get(tableName))))
                    .forEach(record3 -> {
                        String colName = getModelledName((String) record3.get(columnName));
                        fields.put(colName, DataType.fromDbDataType((String) record3.get("udt_name")));
                    });

            final EntityType entityType = EntityType.builder().name(name).tableName(storedAt).fields(fields).module(
                    SchemaRegistry.getModuleByName((String) entityInfoRecord.get("moduleReferenceName"))).attributeNames(
                            attributeNamesList).build();
            entityTypes.add(entityType);
        }

        SchemaRegistry.initializeEntityTypes(entityTypes);
        log.debug("Entities initialized successfully");
    }

    @Override
    public void loadRelationTypes() throws SchemaLoaderException {
        log.debug("Start loading relations");
        List<RelationType> relationTypes = new ArrayList<>();
        SelectJoinStep<Record> relationInfoResult = runMethodSafe(() -> readWriteDataDslContext.select().from(String.format(
                TEIV_MODEL, "relationship_info")));
        for (Record entry : relationInfoResult) {
            //build associations
            Association aSideAssociation = Association.builder().name(((String) entry.get("aSideAssociationName")))
                    .minCardinality((long) (entry.get("aSideMinCardinality"))).maxCardinality((long) (entry.get(
                            "aSideMaxCardinality"))).build();
            Association bSideAssociation = Association.builder().name(((String) entry.get("bSideAssociationName")))
                    .minCardinality((long) (entry.get("bSideMinCardinality"))).maxCardinality((long) (entry.get(
                            "bSideMaxCardinality"))).build();

            final EntityType aSide;
            final EntityType bSide;
            try {
                aSide = SchemaRegistry.getEntityTypeByModuleAndName((String) entry.get("aSideModule"), (String) entry.get(
                        "aSideMOType"));
                bSide = SchemaRegistry.getEntityTypeByModuleAndName((String) entry.get("bSideModule"), (String) entry.get(
                        "bSideMOType"));
            } catch (SchemaRegistryException e) {
                log.error("Error while getting aside / bSide entity type.", e);
                throw new SchemaLoaderException(e.getMessage(), e.getCause());
            }

            RelationType relationType = RelationType.builder().name((String) entry.get("name")).aSideAssociation(
                    aSideAssociation).aSide(aSide).bSideAssociation(bSideAssociation).bSide(bSide)
                    .relationshipStorageLocation(RelationshipDataLocation.valueOf((String) entry.get(
                            "relationshipDataLocation"))).connectsSameEntity((Boolean) (entry.get("connectSameEntity")))
                    .tableName((String) entry.get("storedAt")).module(SchemaRegistry.getModuleByName((String) entry.get(
                            "moduleReferenceName"))).attributeNames(List.of()).build();
            relationTypes.add(relationType);
        }

        //load registry
        SchemaRegistry.initializeRelationTypes(relationTypes);
        log.debug("Relations initialized successfully");
    }

    private <T> T runMethodSafe(Supplier<T> supp) {
        try {
            return supp.get();
        } catch (TeivException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Sql exception during query execution", ex);
            throw TeivException.serverSQLException();
        }
    }
}
