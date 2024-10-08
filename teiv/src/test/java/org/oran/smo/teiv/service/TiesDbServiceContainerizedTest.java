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
package org.oran.smo.teiv.service;

import static org.oran.smo.teiv.ingestion.DeadlockRetryPolicy.POSTGRES_DEADLOCK_ERROR_CODE;
import static org.oran.smo.teiv.utils.TiesConstants.QUOTED_STRING;
import static org.oran.smo.teiv.utils.TiesConstants.TIES_DATA_SCHEMA;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;
import org.jooq.util.xml.jaxb.Column;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.oran.smo.teiv.db.TestPostgresqlContainer;
import org.oran.smo.teiv.exception.TiesException;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.utils.JooqTypeConverter;
import org.oran.smo.teiv.utils.TiesConstants;
import org.oran.smo.teiv.utils.schema.Geography;

@Configuration
@SpringBootTest
class TiesDbServiceContainerizedTest {
    public static TestPostgresqlContainer postgreSQLContainer = TestPostgresqlContainer.getInstance();

    @Autowired
    private TiesDbService tiesDbService;

    @Autowired
    private TiesDbOperations tiesDbOperations;

    @Autowired
    @Qualifier("writeDataDslContext")
    private DSLContext dslContext;

    @Value("${database.retry-policies.deadlock.retry-attempts}")
    private int maxRetryAttemptsForDeadlock;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.read.jdbc-url", () -> postgreSQLContainer.getJdbcUrl());
        registry.add("spring.datasource.read.username", () -> postgreSQLContainer.getUsername());
        registry.add("spring.datasource.read.password", () -> postgreSQLContainer.getPassword());

        registry.add("spring.datasource.write.jdbc-url", () -> postgreSQLContainer.getJdbcUrl());
        registry.add("spring.datasource.write.username", () -> postgreSQLContainer.getUsername());
        registry.add("spring.datasource.write.password", () -> postgreSQLContainer.getPassword());
    }

    @BeforeEach
    public void deleteAll() {
        dslContext.meta().filterSchemas(s -> s.getName().equals(TIES_DATA_SCHEMA)).getTables().forEach(t -> dslContext
                .truncate(t).cascade().execute());
    }

    @Test
    void testMergeManagedElement() {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("id", "id1");
        map1.put("fdn", "fdn1");
        map1.put("cmId", JSONB.jsonb("{\"name\":\"Hellmann1\"}"));
        tiesDbOperations.merge(dslContext, "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"", map1);

        Result<Record> rows = selectAllRowsFromTable(dslContext, "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"");
        for (Entry<String, Object> e : map1.entrySet()) {
            assertEquals(e.getValue(), rows.get(0).get(e.getKey()));
        }

        Map<String, Object> map2 = new HashMap<>();
        map2.put("id", "id1");
        map2.put("fdn", "fdn2");
        map2.put("cmId", JSONB.jsonb("{\"name\":\"Hellmann2\"}"));
        tiesDbOperations.merge(dslContext, "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"", map2);

        Result<Record> rows2 = selectAllRowsFromTable(dslContext, "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"");
        for (Entry<String, Object> e : map2.entrySet()) {
            assertEquals(e.getValue(), rows2.get(0).get(e.getKey()));
        }
    }

    @Test
    void testMergeSector() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("id", "id1");
        map.put("sectorId", 7);
        map.put("geo-location", new Geography("{\"latitude\": 47.497913,\"longitude\": 19.040236}"));
        map.put("azimuth", 7.3);
        tiesDbOperations.merge(dslContext, "ties_data.\"22174a23af5d5a96143c83ddfa78654df0acb697\"", map);

        Result<?> rows = dslContext.select(field("id"), field("\"sectorId\"").as("sectorId"), field(
                "ST_AsText(\"geo-location\")"), field("azimuth")).from(table(
                        "ties_data.\"22174a23af5d5a96143c83ddfa78654df0acb697\"")).fetch();

        assertEquals("id1", rows.get(0).get("id"));
        assertEquals(7L, rows.get(0).get("sectorId"));

        assertEquals("POINT(47.497913 19.040236)", rows.get(0).get("ST_AsText(\"geo-location\")"));
        assertEquals(0, new BigDecimal("7.3").compareTo((BigDecimal) rows.get(0).get("azimuth")));
    }

    @Test
    void testDeleteFromManagedElement() {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("id", "id1");
        map1.put("fdn", "fdn1");
        map1.put("cmId", JSONB.jsonb("{\"name\":\"Hellmann1\"}"));
        tiesDbOperations.merge(dslContext, "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"", map1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("id", "id2");
        map2.put("fdn", "fdn2");
        map2.put("cmId", JSONB.jsonb("{\"name\":\"Hellmann2\"}"));
        tiesDbOperations.merge(dslContext, "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"", map2);

        Result<Record> row1 = selectAllRowsFromTable(dslContext, "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"");
        assertEquals(2, row1.size());

        tiesDbOperations.deleteEntity(dslContext, SchemaRegistry.getEntityTypeByName("ManagedElement"), "id1");

        Result<Record> row2 = selectAllRowsFromTable(dslContext, "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"");
        assertEquals(1, row2.size());
    }

    @Test
    void testCascadeOnDelete() {
        List<Consumer<DSLContext>> dbOperations = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("id", "id1");
        map1.put("fdn", "fdn1");
        map1.put("gNBCUName", "gNBCUName");
        map1.put("gNBId", 1);
        map1.put("gNBIdLength", 1);
        map1.put("pLMNId", JSONB.jsonb("{\"name\":\"pLMNId1\"}"));
        map1.put("cmId", JSONB.jsonb("{\"name\":\"cmId1\"}"));
        tiesDbOperations.merge(dslContext, "ties_data.\"c4a425179d3089b5288fdf059079d0ea26977f0f\"", map1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("id", "id1");
        map2.put("name", "CloudNativeApplication");
        tiesDbOperations.merge(dslContext, "ties_data.\"e01fcb87ad2c34ce66c34420255e25aaca270e5e\"", map2);

        Map<String, Object> map3 = new HashMap<>();
        map3.put("id", "id1");
        map3.put("aSide_GNBCUCPFunction", "id1");
        map3.put("bSide_CloudNativeApplication", "id1");
        tiesDbOperations.merge(dslContext, "ties_data.\"7cd7062ea24531b2f48e4f2fdc51eaf0b82f88c7\"", map3);

        Result<Record> row1 = selectAllRowsFromTable(dslContext, "ties_data.\"c4a425179d3089b5288fdf059079d0ea26977f0f\"");
        assertEquals(1, row1.size());
        Result<Record> row2 = selectAllRowsFromTable(dslContext, "ties_data.\"e01fcb87ad2c34ce66c34420255e25aaca270e5e\"");
        assertEquals(1, row2.size());
        Result<Record> row3 = selectAllRowsFromTable(dslContext, "ties_data.\"7cd7062ea24531b2f48e4f2fdc51eaf0b82f88c7\"");
        assertEquals(1, row3.size());

        dbOperations.add(dslContext -> tiesDbOperations.deleteEntity(dslContext, SchemaRegistry.getEntityTypeByName(
                "GNBCUCPFunction"), "id1"));

        assertDoesNotThrow(() -> tiesDbService.execute(dbOperations));

        Result<Record> row4 = selectAllRowsFromTable(dslContext, "ties_data.\"c4a425179d3089b5288fdf059079d0ea26977f0f\"");
        assertEquals(0, row4.size());
        Result<Record> row5 = selectAllRowsFromTable(dslContext, "ties_data.\"e01fcb87ad2c34ce66c34420255e25aaca270e5e\"");
        assertEquals(1, row5.size());
        Result<Record> row6 = selectAllRowsFromTable(dslContext, "ties_data.\"7cd7062ea24531b2f48e4f2fdc51eaf0b82f88c7\"");
        assertEquals(0, row6.size());

    }

    @Test
    void testTriggerOnDelete() {
        List<Consumer<DSLContext>> dbOperations = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("id", "id1");
        map1.put("fdn", "fdn1");
        map1.put("cmId", JSONB.jsonb("{\"name\":\"Hellmann1\"}"));
        tiesDbOperations.merge(dslContext, "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"", map1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("id", "id1");
        map2.put("name", "CloudNativeSystem");
        tiesDbOperations.merge(dslContext, "ties_data.\"163276fa439cdfccabb80f7acacb6fa638e8d314\"", map2);

        Map<String, Object> map3 = new HashMap<>();
        map3.put("id", "id1");
        map3.put("REL_ID_MANAGEDELEMENT_DEPLOYED_AS_CLOUDNATIVESYSTEM", "relId");
        map3.put("REL_FK_deployed-as-cloudNativeSystem", "id1");
        tiesDbOperations.merge(dslContext, "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"", map3);

        Result<Record> row2 = selectAllRowsFromTable(dslContext, "ties_data.\"163276fa439cdfccabb80f7acacb6fa638e8d314\"");
        assertEquals(1, row2.size());
        Result<Record> row1 = selectAllRowsFromTable(dslContext, "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"");
        assertEquals(1, row1.size());

        dbOperations.add(dslContext -> tiesDbOperations.deleteEntity(dslContext, SchemaRegistry.getEntityTypeByName(
                "CloudNativeSystem"), "id1"));

        assertDoesNotThrow(() -> tiesDbService.execute(dbOperations));

        Result<Record> meRecords = selectAllRowsFromTable(dslContext,
                "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"");
        assertEquals("id1", meRecords.get(0).get("id"));
        assertEquals("fdn1", meRecords.get(0).get("fdn"));
        assertNull(meRecords.get(0).get("REL_FK_deployed-as-cloudNativeSystem"));
        assertNull(meRecords.get(0).get("REL_ID_MANAGEDELEMENT_DEPLOYED_AS_CLOUDNATIVESYSTEM"));
        Result<Record> cnsRecords = selectAllRowsFromTable(dslContext,
                "ties_data.\"163276fa439cdfccabb80f7acacb6fa638e8d314\"");
        assertEquals(0, cnsRecords.size());
    }

    @Test
    void testMergeOneToManyRelationship() {
        Map<String, Object> managedElementEntity1 = new HashMap<>();
        managedElementEntity1.put("id", "managedelement_id1");
        managedElementEntity1.put("fdn", "managedelement_fdn1");
        managedElementEntity1.put("cmId", JSONB.jsonb("{\"name\":\"Hellmann1\"}"));
        tiesDbOperations.merge(dslContext, "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"", managedElementEntity1);

        Map<String, Object> cloudNativeAppEntity = new HashMap<>();
        cloudNativeAppEntity.put("id", "cna-1");
        cloudNativeAppEntity.put("name", "CloudNativeApp1");
        tiesDbOperations.merge(dslContext, "ties_data.\"e01fcb87ad2c34ce66c34420255e25aaca270e5e\"", cloudNativeAppEntity);

        Map<String, Object> meToCnaRelationship = new HashMap<>();
        meToCnaRelationship.put("id", "cna-1");
        meToCnaRelationship.put("REL_FK_realised-managedElement", "managedelement_id1");
        meToCnaRelationship.put("REL_ID_MANAGEDELEMENT_REALISED_BY_CLOUDNATIVEAPPLICATION", "rel_id1");
        tiesDbOperations.merge(dslContext, "ties_data.\"e01fcb87ad2c34ce66c34420255e25aaca270e5e\"", meToCnaRelationship);

        Result<?> rowsBeforeMerge = dslContext.select(field("id"), field("name"), field(
                "\"REL_FK_realised-managedElement\"").as("REL_FK_realised-managedElement"), field(
                        "\"REL_ID_MANAGEDELEMENT_REALISED_BY_CLOUDNATIVEAPPLICATION\"").as(
                                "REL_ID_MANAGEDELEMENT_REALISED_BY_CLOUDNATIVEAPPLICATION")).from(table(
                                        "ties_data.\"e01fcb87ad2c34ce66c34420255e25aaca270e5e\"")).fetch();
        assertEquals("cna-1", rowsBeforeMerge.get(0).get("id"));
        assertEquals("CloudNativeApp1", rowsBeforeMerge.get(0).get("name"));
        assertEquals("managedelement_id1", rowsBeforeMerge.get(0).get("REL_FK_realised-managedElement"));
        assertEquals("rel_id1", rowsBeforeMerge.get(0).get("REL_ID_MANAGEDELEMENT_REALISED_BY_CLOUDNATIVEAPPLICATION"));

        Map<String, Object> cloudNativeAppEntity2 = new HashMap<>();
        cloudNativeAppEntity2.put("id", "cna-2");
        cloudNativeAppEntity2.put("name", "CloudNativeApp2");
        tiesDbOperations.merge(dslContext, "ties_data.\"e01fcb87ad2c34ce66c34420255e25aaca270e5e\"", cloudNativeAppEntity2);

        Map<String, Object> modifiedMeToCnaRelationship = new HashMap<>();
        modifiedMeToCnaRelationship.put("id", "cna-2");
        modifiedMeToCnaRelationship.put("REL_FK_realised-managedElement", "managedelement_id1");
        modifiedMeToCnaRelationship.put("REL_ID_MANAGEDELEMENT_REALISED_BY_CLOUDNATIVEAPPLICATION", "rel_id2");
        tiesDbOperations.merge(dslContext, "ties_data.\"e01fcb87ad2c34ce66c34420255e25aaca270e5e\"",
                modifiedMeToCnaRelationship);

        Result<?> rowsAfterMerge = dslContext.select(field("id"), field("name"), field("\"REL_FK_realised-managedElement\"")
                .as("REL_FK_realised-managedElement"), field("\"REL_ID_MANAGEDELEMENT_REALISED_BY_CLOUDNATIVEAPPLICATION\"")
                        .as("REL_ID_MANAGEDELEMENT_REALISED_BY_CLOUDNATIVEAPPLICATION")).from(table(
                                "ties_data.\"e01fcb87ad2c34ce66c34420255e25aaca270e5e\"")).fetch();

        assertEquals("cna-1", rowsAfterMerge.get(0).get("id"));
        assertEquals("CloudNativeApp1", rowsAfterMerge.get(0).get("name"));
        assertEquals("managedelement_id1", rowsAfterMerge.get(1).get("REL_FK_realised-managedElement"));
        assertEquals("rel_id1", rowsAfterMerge.get(0).get("REL_ID_MANAGEDELEMENT_REALISED_BY_CLOUDNATIVEAPPLICATION"));
        assertEquals("cna-2", rowsAfterMerge.get(1).get("id"));
        assertEquals("CloudNativeApp2", rowsAfterMerge.get(1).get("name"));
        assertEquals("managedelement_id1", rowsAfterMerge.get(1).get("REL_FK_realised-managedElement"));
        assertEquals("rel_id2", rowsAfterMerge.get(1).get("REL_ID_MANAGEDELEMENT_REALISED_BY_CLOUDNATIVEAPPLICATION"));
    }

    @Test
    void testDeleteOneToManyRelationship() {
        Map<String, Object> managedElementEntity = new HashMap<>();
        managedElementEntity.put("id", "me-id1");
        managedElementEntity.put("fdn", "fdn1");
        managedElementEntity.put("cmId", JSONB.jsonb("{\"name\":\"Hellmann1\"}"));
        tiesDbOperations.merge(dslContext, "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"", managedElementEntity);

        Map<String, Object> enodeBFunctionEntity = new HashMap<>();
        enodeBFunctionEntity.put("id", "enodeb-id1");
        enodeBFunctionEntity.put("eNBId", 1L);
        tiesDbOperations.merge(dslContext, "ties_data.\"o-ran-smo-teiv-ran-logical_ENodeBFunction\"", enodeBFunctionEntity);

        Map<String, Object> meToEnodeBFuncRelation = new HashMap<>();
        meToEnodeBFuncRelation.put("id", "enodeb-id1");
        meToEnodeBFuncRelation.put("REL_FK_managed-by-managedElement", "me-id1");
        meToEnodeBFuncRelation.put("REL_ID_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION", "eiid1");
        meToEnodeBFuncRelation.put("REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION", JooqTypeConverter.toJsonb(List
                .of("fdn1", "cmHandleId1")));
        tiesDbOperations.merge(dslContext, "ties_data.\"o-ran-smo-teiv-ran-logical_ENodeBFunction\"",
                meToEnodeBFuncRelation);

        tiesDbOperations.deleteRelationFromEntityTableByRelationId(dslContext, "eiid1", SchemaRegistry
                .getRelationTypeByName("MANAGEDELEMENT_MANAGES_ENODEBFUNCTION"));

        Result<Record> rows = selectAllRowsFromTable(dslContext, "ties_data.\"o-ran-smo-teiv-ran-logical_ENodeBFunction\"");
        assertEquals("enodeb-id1", rows.get(0).get("id"));
        assertEquals(1L, rows.get(0).get("eNBId"));
        //assertNull(rows.get(0).get("REL_FK_managed-by-managedElement"));
        assertNull(rows.get(0).get("REL_ID_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION"));
        assertEquals(JooqTypeConverter.toJsonb(List.of()), rows.get(0).get(
                "REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION"));
    }

    @Test
    void testTransactionalMergeSucceeds() {
        List<Consumer<DSLContext>> dbOperations = new ArrayList<>();
        Map<String, Object> cloudNativeSystemEntity = new HashMap<>();
        cloudNativeSystemEntity.put("id", "cloudnative_id1");
        cloudNativeSystemEntity.put("name", "CloudNativeSystem");
        dbOperations.add(wrDSLContext -> tiesDbOperations.merge(wrDSLContext,
                "ties_data.\"163276fa439cdfccabb80f7acacb6fa638e8d314\"", cloudNativeSystemEntity));

        Map<String, Object> managedElementEntity = new HashMap<>();
        managedElementEntity.put("id", "managed_element_id1");
        managedElementEntity.put("fdn", "fdn1");
        managedElementEntity.put("cmId", JSONB.jsonb("{\"name\":\"Hellmann1\"}"));
        dbOperations.add(wrDSLContext -> tiesDbOperations.merge(wrDSLContext,
                "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"", managedElementEntity));

        Map<String, Object> meTocnsRelationship = new HashMap<>();
        meTocnsRelationship.put("id", "managed_element_id1");
        meTocnsRelationship.put("REL_FK_deployed-as-cloudNativeSystem", "cloudnative_id1");
        meTocnsRelationship.put("REL_ID_MANAGEDELEMENT_DEPLOYED_AS_CLOUDNATIVESYSTEM", "eiid1");
        meTocnsRelationship.put("REL_CD_sourceIds_MANAGEDELEMENT_DEPLOYED_AS_CLOUDNATIVESYSTEM", JooqTypeConverter.toJsonb(
                List.of("fdn1", "cmHandleId1")));
        dbOperations.add(wrDSLContext -> tiesDbOperations.merge(wrDSLContext,
                "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"", meTocnsRelationship));

        assertDoesNotThrow(() -> tiesDbService.execute(dbOperations));

        Result<Record> rowsFromManagedElementTable = selectAllRowsFromTable(dslContext,
                "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"");
        assertEquals(1, rowsFromManagedElementTable.size());
        assertEquals("cloudnative_id1", rowsFromManagedElementTable.get(0).get("REL_FK_deployed-as-cloudNativeSystem"));
        assertEquals("eiid1", rowsFromManagedElementTable.get(0).get(
                "REL_ID_MANAGEDELEMENT_DEPLOYED_AS_CLOUDNATIVESYSTEM"));
        assertEquals(JooqTypeConverter.toJsonb(List.of("fdn1", "cmHandleId1")), rowsFromManagedElementTable.get(0).get(
                "REL_CD_sourceIds_MANAGEDELEMENT_DEPLOYED_AS_CLOUDNATIVESYSTEM"));

        Result<Record> rowsFromCloudNativeSystem = selectAllRowsFromTable(dslContext,
                "ties_data.\"163276fa439cdfccabb80f7acacb6fa638e8d314\"");
        assertEquals(1, rowsFromCloudNativeSystem.size());
    }

    @Test
    void testTransactionalMergeRollsBackAfterRelationshipError() {
        List<Consumer<DSLContext>> dbOperations = new ArrayList<>();
        Map<String, Object> managedElementEntity = new HashMap<>();
        managedElementEntity.put("id", "managed_element_id1");
        managedElementEntity.put("fdn", "fdn1");
        managedElementEntity.put("cmId", JSONB.jsonb("{\"name\":\"Hellmann1\"}"));
        dbOperations.add(wrDSLContext -> tiesDbOperations.merge(wrDSLContext,
                "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"", managedElementEntity));

        Map<String, Object> cloudNativeSystemEntity = new HashMap<>();
        cloudNativeSystemEntity.put("id", "cloudnative_id1");
        cloudNativeSystemEntity.put("name", "CloudNativeSystem");

        dbOperations.add(wrDSLContext -> tiesDbOperations.merge(wrDSLContext,
                "ties_data.\"163276fa439cdfccabb80f7acacb6fa638e8d314\"", cloudNativeSystemEntity));

        // Create a faulty relationship map to trigger the rollback
        Map<String, Object> faultyCloudNativeSystemRelationship = new HashMap<>();
        faultyCloudNativeSystemRelationship.put("id", "cloudnative_id1");
        faultyCloudNativeSystemRelationship.put("REL_MANAGEDELEMENT_DEPLOYED_AS_CLOUDNATIVESYSTEM_BAAAD",
                "managed_element_id1");
        faultyCloudNativeSystemRelationship.put("REL_MANAGEDELEMENT_DEPLOYED_AS_CLOUDNATIVESYSTEM_EIID", "eiid1");
        dbOperations.add(wrDSLContext -> tiesDbOperations.merge(wrDSLContext,
                "ties_data.\"163276fa439cdfccabb80f7acacb6fa638e8d314\"", faultyCloudNativeSystemRelationship));

        assertThrows(TiesException.class, () -> tiesDbService.execute(dbOperations));

        Result<Record> rowsFromManagedElementTable = selectAllRowsFromTable(dslContext,
                "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"");
        assertEquals(0, rowsFromManagedElementTable.size());

        Result<Record> rowsFromCloudNativeSystem = selectAllRowsFromTable(dslContext,
                "ties_data.\"163276fa439cdfccabb80f7acacb6fa638e8d314\"");
        assertEquals(0, rowsFromCloudNativeSystem.size());
    }

    @Test
    void testTransactionalDeleteSucceeds() {
        List<Consumer<DSLContext>> dbOperations = new ArrayList<>();
        Map<String, Object> cloudNativeSystemEntity = new HashMap<>();
        cloudNativeSystemEntity.put("id", "cloudnative_id1");
        cloudNativeSystemEntity.put("name", "CloudNativeSystem");
        tiesDbOperations.merge(dslContext, "ties_data.\"163276fa439cdfccabb80f7acacb6fa638e8d314\"",
                cloudNativeSystemEntity);

        Map<String, Object> managedElementEntity = new HashMap<>();
        managedElementEntity.put("id", "managed_element_id1");
        managedElementEntity.put("fdn", "fdn1");
        managedElementEntity.put("cmId", JSONB.jsonb("{\"name\":\"Hellmann1\"}"));
        tiesDbOperations.merge(dslContext, "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"", managedElementEntity);

        Map<String, Object> meToCnsRelationship = new HashMap<>();
        meToCnsRelationship.put("id", "managed_element_id1");
        meToCnsRelationship.put("REL_FK_deployed-as-cloudNativeSystem", "cloudnative_id1");
        meToCnsRelationship.put("REL_ID_MANAGEDELEMENT_DEPLOYED_AS_CLOUDNATIVESYSTEM", "eiid1");
        meToCnsRelationship.put("REL_CD_sourceIds_MANAGEDELEMENT_DEPLOYED_AS_CLOUDNATIVESYSTEM", JooqTypeConverter.toJsonb(
                List.of("fdn1", "cmHandleId1")));
        tiesDbOperations.merge(dslContext, "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"", meToCnsRelationship);

        Result<Record> rowsFromCloudNativeSystemBeforeDelete = selectAllRowsFromTable(dslContext,
                "ties_data.\"163276fa439cdfccabb80f7acacb6fa638e8d314\"");
        assertEquals(1, rowsFromCloudNativeSystemBeforeDelete.size());

        Result<Record> rowsFromManagedElementBeforeDelete = selectAllRowsFromTable(dslContext,
                "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"");
        assertEquals(1, rowsFromManagedElementBeforeDelete.size());
        assertEquals("cloudnative_id1", rowsFromManagedElementBeforeDelete.get(0).get(
                "REL_FK_deployed-as-cloudNativeSystem"));
        assertEquals("eiid1", rowsFromManagedElementBeforeDelete.get(0).get(
                "REL_ID_MANAGEDELEMENT_DEPLOYED_AS_CLOUDNATIVESYSTEM"));
        assertEquals(JooqTypeConverter.toJsonb(List.of("fdn1", "cmHandleId1")), rowsFromManagedElementBeforeDelete.get(0)
                .get("REL_CD_sourceIds_MANAGEDELEMENT_DEPLOYED_AS_CLOUDNATIVESYSTEM"));

        dbOperations.add(wrDSLContext -> tiesDbOperations.deleteEntity(wrDSLContext, SchemaRegistry.getEntityTypeByName(
                "ManagedElement"), "managed_element_id1"));

        dbOperations.add(wrDSLContext -> {
            tiesDbOperations.deleteEntity(wrDSLContext, SchemaRegistry.getEntityTypeByName("CloudNativeSystem"),
                    "cloudnative_id1");
        });

        dbOperations.add(wrDSLContext -> tiesDbOperations.deleteRelationFromEntityTableByRelationId(wrDSLContext,
                "managed_element_id1", SchemaRegistry.getRelationTypeByName(
                        "MANAGEDELEMENT_DEPLOYED_AS_CLOUDNATIVESYSTEM")));

        assertDoesNotThrow(() -> tiesDbService.execute(dbOperations));

        Result<Record> rowsFromManagedElementTable = selectAllRowsFromTable(dslContext,
                "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"");
        assertEquals(0, rowsFromManagedElementTable.size());

        Result<Record> rowsFromCloudNativeSystem = selectAllRowsFromTable(dslContext,
                "ties_data.\"163276fa439cdfccabb80f7acacb6fa638e8d314\"");
        assertEquals(0, rowsFromCloudNativeSystem.size());
    }

    @Test
    void testTransactionalDeleteRollbackAfterRelationshipError() {
        List<Consumer<DSLContext>> dbOperations = new ArrayList<>();
        Map<String, Object> cloudNativeSystemEntity = new HashMap<>();
        cloudNativeSystemEntity.put("id", "cloudnative_id1");
        cloudNativeSystemEntity.put("name", "CloudNativeSystem");
        tiesDbOperations.merge(dslContext, "ties_data.\"163276fa439cdfccabb80f7acacb6fa638e8d314\"",
                cloudNativeSystemEntity);

        Map<String, Object> managedElementEntity = new HashMap<>();
        managedElementEntity.put("id", "managed_element_id1");
        managedElementEntity.put("fdn", "fdn1");
        managedElementEntity.put("cmId", JSONB.jsonb("{\"name\":\"Hellmann1\"}"));
        tiesDbOperations.merge(dslContext, "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"", managedElementEntity);

        Map<String, Object> cloudNativeSystemRelationship = new HashMap<>();
        cloudNativeSystemRelationship.put("id", "managed_element_id1");
        cloudNativeSystemRelationship.put("REL_FK_deployed-as-cloudNativeSystem", "cloudnative_id1");
        cloudNativeSystemRelationship.put("REL_ID_MANAGEDELEMENT_DEPLOYED_AS_CLOUDNATIVESYSTEM", "eiid1");
        cloudNativeSystemRelationship.put("REL_CD_sourceIds_MANAGEDELEMENT_DEPLOYED_AS_CLOUDNATIVESYSTEM", JooqTypeConverter
                .toJsonb(List.of("fdn1", "cmHandleId1")));
        tiesDbOperations.merge(dslContext, "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"",
                cloudNativeSystemRelationship);

        Result<Record> rowsFromCloudNativeSystemBeforeDelete = selectAllRowsFromTable(dslContext,
                "ties_data.\"163276fa439cdfccabb80f7acacb6fa638e8d314\"");
        assertEquals(1, rowsFromCloudNativeSystemBeforeDelete.size());

        Result<Record> rowsFromManagedElementBeforeDelete = selectAllRowsFromTable(dslContext,
                "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"");
        assertEquals(1, rowsFromManagedElementBeforeDelete.size());
        assertEquals("cloudnative_id1", rowsFromManagedElementBeforeDelete.get(0).get(
                "REL_FK_deployed-as-cloudNativeSystem"));
        assertEquals("eiid1", rowsFromManagedElementBeforeDelete.get(0).get(
                "REL_ID_MANAGEDELEMENT_DEPLOYED_AS_CLOUDNATIVESYSTEM"));
        assertEquals(JooqTypeConverter.toJsonb(List.of("fdn1", "cmHandleId1")), rowsFromManagedElementBeforeDelete.get(0)
                .get("REL_CD_sourceIds_MANAGEDELEMENT_DEPLOYED_AS_CLOUDNATIVESYSTEM"));

        dbOperations.add(wrDSLContext -> tiesDbOperations.deleteEntity(wrDSLContext, SchemaRegistry.getEntityTypeByName(
                "ManagedElement"), "managed_element_id1"));
        dbOperations.add(wrDSLContext -> tiesDbOperations.deleteEntity(wrDSLContext, SchemaRegistry.getEntityTypeByName(
                "CloudNativeSystem"), "cloudnative_id1"));

        // Add a faulty relationship delete to trigger the rollback
        dbOperations.add(wrDSLContext -> tiesDbOperations.deleteRelationFromEntityTableByRelationId(wrDSLContext, "eiid1",
                SchemaRegistry.getRelationTypeByName("rel_managedelement_deployed_as_cloudnativesystem_eiid")));

        assertThrows(TiesException.class, () -> tiesDbService.execute(dbOperations));

        Result<Record> rowsFromCloudNativeSystem = selectAllRowsFromTable(dslContext,
                "ties_data.\"163276fa439cdfccabb80f7acacb6fa638e8d314\"");
        assertEquals(1, rowsFromCloudNativeSystem.size());
        Result<Record> rowsFromManagedElementTable = selectAllRowsFromTable(dslContext,
                "ties_data.\"o-ran-smo-teiv-ran-oam_ManagedElement\"");
        assertEquals(1, rowsFromManagedElementTable.size());
        assertEquals("cloudnative_id1", rowsFromManagedElementTable.get(0).get("REL_FK_deployed-as-cloudNativeSystem"));
        assertEquals("eiid1", rowsFromManagedElementTable.get(0).get(
                "REL_ID_MANAGEDELEMENT_DEPLOYED_AS_CLOUDNATIVESYSTEM"));
        assertEquals(JooqTypeConverter.toJsonb(List.of("fdn1", "cmHandleId1")), rowsFromManagedElementTable.get(0).get(
                "REL_CD_sourceIds_MANAGEDELEMENT_DEPLOYED_AS_CLOUDNATIVESYSTEM"));
    }

    @Test
    void testDeadlockRetry() throws InterruptedException {
        List<Consumer<DSLContext>> dbOperations1 = new ArrayList<>();
        List<Consumer<DSLContext>> dbOperations2 = new ArrayList<>();

        Map<String, Object> cloudNativeSystemEntity1 = new HashMap<>();
        cloudNativeSystemEntity1.put("id", "id1");
        cloudNativeSystemEntity1.put("name", "CloudNativeSystem");

        Map<String, Object> cloudNativeSystemEntity2 = new HashMap<>();
        cloudNativeSystemEntity2.put("id", "id2");
        cloudNativeSystemEntity2.put("name", "CloudNativeSystem");

        Map<String, Object> cloudNativeSystemEntity3 = new HashMap<>();
        cloudNativeSystemEntity3.put("id", "id3");
        cloudNativeSystemEntity3.put("name", "CloudNativeSystem");

        Map<String, Object> cloudNativeSystemEntity4 = new HashMap<>();
        cloudNativeSystemEntity4.put("id", "id4");
        cloudNativeSystemEntity4.put("name", "CloudNativeSystem");

        final CountDownLatch firstTransactionCompletedMergeEntity1 = new CountDownLatch(1);
        final CountDownLatch secondTransactionCompletedMergeEntity2 = new CountDownLatch(1);
        dbOperations1.add(dslContext -> {
            tiesDbOperations.merge(dslContext, "ties_data.\"163276fa439cdfccabb80f7acacb6fa638e8d314\"",
                    cloudNativeSystemEntity1);
            firstTransactionCompletedMergeEntity1.countDown();
            try {
                secondTransactionCompletedMergeEntity2.await();
                tiesDbOperations.merge(dslContext, "ties_data.\"163276fa439cdfccabb80f7acacb6fa638e8d314\"",
                        cloudNativeSystemEntity2);
                tiesDbOperations.merge(dslContext, "ties_data.\"163276fa439cdfccabb80f7acacb6fa638e8d314\"",
                        cloudNativeSystemEntity3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        //Try to add the same rows in another transaction in another order.
        dbOperations2.add(dslContext -> {
            try {
                tiesDbOperations.merge(dslContext, "ties_data.\"163276fa439cdfccabb80f7acacb6fa638e8d314\"",
                        cloudNativeSystemEntity2);
                secondTransactionCompletedMergeEntity2.countDown();
                firstTransactionCompletedMergeEntity1.await();
                tiesDbOperations.merge(dslContext, "ties_data.\"163276fa439cdfccabb80f7acacb6fa638e8d314\"",
                        cloudNativeSystemEntity1);
                tiesDbOperations.merge(dslContext, "ties_data.\"163276fa439cdfccabb80f7acacb6fa638e8d314\"",
                        cloudNativeSystemEntity4);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread t1 = new Thread(() -> tiesDbService.execute(dbOperations1));
        Thread t2 = new Thread(() -> tiesDbService.execute(dbOperations2));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        Result<Record> rowsFromCloudNativeSystem = selectAllRowsFromTable(dslContext,
                "ties_data.\"163276fa439cdfccabb80f7acacb6fa638e8d314\"");
        assertEquals(4, rowsFromCloudNativeSystem.size());
    }

    @Test
    void testDeadlockRetryMaxAttemptsReached() {
        final AtomicInteger attempts = new AtomicInteger();
        List<Consumer<DSLContext>> dbOperations = new ArrayList<>();
        dbOperations.add(dslContext -> {
            attempts.getAndIncrement();
            throw new DataAccessException("A deadlock occurred in the db", new SQLException("details",
                    POSTGRES_DEADLOCK_ERROR_CODE));
        });
        assertThrows(TiesException.class, () -> tiesDbService.execute(dbOperations));
        assertEquals(maxRetryAttemptsForDeadlock, attempts.get());
    }

    /**
     * The out of the box binding for geography is available in the commercial jOOQ distribution only. Because of this,
     * a select * from tableName; query fails if the table has a column with geography type. Even if the value in that
     * column is null.
     *
     * @param readDataDslContext
     * @param tableName
     *     For example: ties_data."AntennaModule"
     * @return the fetched rows. Values of geography type are represented as String values.
     */
    public static Result<Record> selectAllRowsFromTable(DSLContext readDataDslContext, final String tableName) {
        String unqualifiedName = tableName.split("\\.")[1].split("\"")[1];
        Table<?> table = readDataDslContext.meta().getTables().stream().filter(t -> t.getName().equals(unqualifiedName))
                .toList().get(0);
        List<Column> columns = readDataDslContext.meta(table).informationSchema().getColumns();
        List<Field<?>> columnsToSelect = columns.stream().map(c -> {
            if ("geography".equals(c.getDataType())) {
                return field(String.format(TiesConstants.ST_TO_STRING, String.format(QUOTED_STRING, c.getColumnName())));
            } else if ("jsonb".equals(c.getDataType())) {
                return field(name(c.getColumnName()), JSONB.class);
            } else {
                return field(name(c.getColumnName()));
            }
        }).toList();
        return readDataDslContext.select(columnsToSelect).from(table(tableName)).fetch();
    }
}
