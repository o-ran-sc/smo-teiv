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
import static org.oran.smo.teiv.utils.TeivConstants.QUOTED_STRING;
import static org.oran.smo.teiv.utils.TeivConstants.TEIV_DATA_SCHEMA;
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
import org.oran.smo.teiv.schema.EntityType;
import org.oran.smo.teiv.schema.RelationType;
import org.oran.smo.teiv.schema.SchemaRegistryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.oran.smo.teiv.db.TestPostgresqlContainer;
import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.utils.JooqTypeConverter;
import org.oran.smo.teiv.utils.TeivConstants;
import org.oran.smo.teiv.utils.schema.Geography;

@Configuration
@SpringBootTest
class TeivDbServiceContainerizedTest {
    private static TestPostgresqlContainer postgreSQLContainer = TestPostgresqlContainer.getInstance();
    private static EntityType entityTypeMe;
    private static EntityType entityTypeODU;
    private static EntityType entityTypeSector;
    private static EntityType entityTypeAntennMod;
    private static EntityType entityTypeAntennCap;
    private static RelationType relTypeantennServesAntennCap;
    private static RelationType relTypeMeManagesODU;

    @Autowired
    private TeivDbService teivDbService;

    @Autowired
    private TeivDbOperations teivDbOperations;

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
    public void deleteAllAndMakeTypes() {
        TestPostgresqlContainer.truncateSchemas(List.of(TEIV_DATA_SCHEMA), dslContext);
        try {
            entityTypeMe = SchemaRegistry.getEntityTypeByModuleAndName("o-ran-smo-teiv-oam", "ManagedElement");
            entityTypeODU = SchemaRegistry.getEntityTypeByModuleAndName("o-ran-smo-teiv-ran", "ODUFunction");
            entityTypeSector = SchemaRegistry.getEntityTypeByModuleAndName("o-ran-smo-teiv-ran", "Sector");
            entityTypeAntennMod = SchemaRegistry.getEntityTypeByModuleAndName("o-ran-smo-teiv-equipment", "AntennaModule");
            entityTypeAntennCap = SchemaRegistry.getEntityTypeByModuleAndName("o-ran-smo-teiv-ran", "AntennaCapability");
            relTypeMeManagesODU = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-rel-oam-ran",
                    "MANAGEDELEMENT_MANAGES_ODUFUNCTION");
            relTypeantennServesAntennCap = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-rel-equipment-ran",
                    "ANTENNAMODULE_SERVES_ANTENNACAPABILITY");
        } catch (SchemaRegistryException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testMergeManagedElement() throws SchemaRegistryException {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("id", "id1");
        map1.put("CD_sourceIds", JSONB.jsonb("[\"sourceId1\",\"sourceId2\"]"));
        map1.put("metadata", Map.of("reliabilityIndicator", "OK"));
        teivDbOperations.merge(dslContext, entityTypeMe, map1);
        Result<Record> rows = selectAllRowsFromTable(dslContext, "teiv_data.\"o-ran-smo-teiv-oam_ManagedElement\"");
        assertEquals("{\"reliabilityIndicator\":\"OK\"}", rows.get(0).get("metadata").toString());
        map1.remove("metadata");
        for (Entry<String, Object> e : map1.entrySet()) {
            assertEquals(e.getValue(), rows.get(0).get(e.getKey()));
        }

        Map<String, Object> map2 = new HashMap<>();
        map2.put("id", "id1");
        map2.put("CD_sourceIds", JSONB.jsonb("[\"sourceId3\",\"sourceId4\"]"));
        map2.put("metadata", new HashMap<>());
        teivDbOperations.merge(dslContext, entityTypeMe, map2);
        Result<Record> rows2 = selectAllRowsFromTable(dslContext, "teiv_data.\"o-ran-smo-teiv-oam_ManagedElement\"");
        assertEquals("{\"reliabilityIndicator\":\"OK\"}", rows2.get(0).get("metadata").toString());
        map2.remove("metadata");
        for (Entry<String, Object> e : map2.entrySet()) {
            assertEquals(e.getValue(), rows2.get(0).get(e.getKey()));
        }
    }

    @Test
    void testMergeSector() throws IOException, SchemaRegistryException {
        Map<String, Object> mapWithGeoLocation = new HashMap<>();
        mapWithGeoLocation.put("id", "id1");
        mapWithGeoLocation.put("sectorId", 7);
        mapWithGeoLocation.put("geo-location", new Geography("{\"longitude\": 19.040236,\"latitude\": 47.497913}"));
        mapWithGeoLocation.put("azimuth", 7.3);
        teivDbOperations.merge(dslContext, entityTypeSector, mapWithGeoLocation);

        Map<String, Object> mapWithGeoLocationAndHeight = new HashMap<>();
        mapWithGeoLocationAndHeight.put("id", "id2");
        mapWithGeoLocationAndHeight.put("sectorId", 8);
        mapWithGeoLocationAndHeight.put("geo-location", new Geography(
                "{\"longitude\": 19.040236,\"latitude\": 47.497913,\"height\": 111.1}"));
        mapWithGeoLocationAndHeight.put("azimuth", 8.3);
        teivDbOperations.merge(dslContext, entityTypeSector, mapWithGeoLocationAndHeight);
        Result<?> rows = dslContext.select(field("id"), field("\"sectorId\"").as("sectorId"), field(
                "ST_AsText(\"geo-location\")"), field("azimuth")).from(table("teiv_data.\"o-ran-smo-teiv-ran_Sector\""))
                .fetch();
        assertEquals("id1", rows.get(0).get("id"));
        assertEquals(7L, rows.get(0).get("sectorId"));
        assertEquals("POINT(19.040236 47.497913)", rows.get(0).get("ST_AsText(\"geo-location\")"));
        assertEquals(0, new BigDecimal("7.3").compareTo((BigDecimal) rows.get(0).get("azimuth")));

        assertEquals("id2", rows.get(1).get("id"));
        assertEquals(8L, rows.get(1).get("sectorId"));
        assertEquals("POINT Z (19.040236 47.497913 111.1)", rows.get(1).get("ST_AsText(\"geo-location\")"));
        assertEquals(0, new BigDecimal("8.3").compareTo((BigDecimal) rows.get(1).get("azimuth")));
    }

    @Test
    void testDeleteFromManagedElement() throws SchemaRegistryException {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("id", "id1");
        map1.put("CD_sourceIds", JSONB.jsonb("[\"sourceId1\",\"sourceId2\"]"));
        teivDbOperations.merge(dslContext, entityTypeMe, map1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("id", "id2");
        map2.put("CD_sourceIds", JSONB.jsonb("[\"sourceId3\",\"sourceId4\"]"));
        teivDbOperations.merge(dslContext, entityTypeMe, map2);

        Result<Record> row1 = selectAllRowsFromTable(dslContext, "teiv_data.\"o-ran-smo-teiv-oam_ManagedElement\"");
        assertEquals(2, row1.size());

        teivDbOperations.deleteEntity(dslContext, SchemaRegistry.getEntityTypeByModuleAndName("o-ran-smo-teiv-oam",
                "ManagedElement"), "id1");

        Result<Record> row2 = selectAllRowsFromTable(dslContext, "teiv_data.\"o-ran-smo-teiv-oam_ManagedElement\"");
        assertEquals(1, row2.size());
    }

    @Test
    void testCascadeOnDelete() throws SchemaRegistryException {
        List<Consumer<DSLContext>> dbOperations = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("id", "id1");
        map1.put("positionWithinSector", "center");
        map1.put("antennaBeamWidth", JSONB.jsonb("[2,4,5]"));
        teivDbOperations.merge(dslContext, entityTypeAntennMod, map1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("id", "id1");
        map2.put("eUtranFqBands", JSONB.jsonb("[\"eUtranFqBands1\",\"eUtranFqBands2\"]"));
        teivDbOperations.merge(dslContext, entityTypeAntennCap, map2);

        Map<String, Object> map3 = new HashMap<>();
        map3.put("id", "id1");
        map3.put("aSide_AntennaModule", "id1");
        map3.put("bSide_AntennaCapability", "id1");
        teivDbOperations.merge(dslContext, relTypeantennServesAntennCap, map3);

        Result<Record> row1 = selectAllRowsFromTable(dslContext, "teiv_data.\"o-ran-smo-teiv-equipment_AntennaModule\"");
        assertEquals(1, row1.size());
        Result<Record> row2 = selectAllRowsFromTable(dslContext, "teiv_data.\"o-ran-smo-teiv-ran_AntennaCapability\"");
        assertEquals(1, row2.size());
        Result<Record> row3 = selectAllRowsFromTable(dslContext, "teiv_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\"");
        assertEquals(1, row3.size());

        dbOperations.add(dslContext -> {
            try {
                teivDbOperations.deleteEntity(dslContext, SchemaRegistry.getEntityTypeByModuleAndName(
                        "o-ran-smo-teiv-equipment", "AntennaModule"), "id1");
            } catch (SchemaRegistryException e) {
                throw new RuntimeException(e);
            }
        });

        assertDoesNotThrow(() -> teivDbService.execute(dbOperations));

        Result<Record> row4 = selectAllRowsFromTable(dslContext, "teiv_data.\"o-ran-smo-teiv-equipment_AntennaModule\"");
        assertEquals(0, row4.size());
        Result<Record> row5 = selectAllRowsFromTable(dslContext, "teiv_data.\"o-ran-smo-teiv-ran_AntennaCapability\"");
        assertEquals(1, row5.size());
        Result<Record> row6 = selectAllRowsFromTable(dslContext, "teiv_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\"");
        assertEquals(0, row6.size());

    }

    @Test
    void testTriggerOnDelete() throws SchemaRegistryException {
        List<Consumer<DSLContext>> dbOperations = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("id", "id1");
        map1.put("gNBId", 1);
        map1.put("dUpLMNId", JSONB.jsonb("{\"mcc\":\"dUpLMNId1\"}"));
        teivDbOperations.merge(dslContext, entityTypeODU, map1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("id", "id1");
        map2.put("CD_sourceIds", JSONB.jsonb("[\"sourceId1\",\"sourceId2\"]"));
        teivDbOperations.merge(dslContext, entityTypeMe, map2);

        Map<String, Object> map3 = new HashMap<>();
        map3.put("id", "id1");
        map3.put("REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION", "relId");
        map3.put("REL_FK_managed-by-managedElement", "id1");
        teivDbOperations.merge(dslContext, entityTypeODU, map3);

        Result<Record> row2 = selectAllRowsFromTable(dslContext, "teiv_data.\"o-ran-smo-teiv-oam_ManagedElement\"");
        assertEquals(1, row2.size());
        Result<Record> row1 = selectAllRowsFromTable(dslContext, "teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\"");
        assertEquals(1, row1.size());

        dbOperations.add(dslContext -> {
            try {
                teivDbOperations.deleteEntity(dslContext, SchemaRegistry.getEntityTypeByModuleAndName("o-ran-smo-teiv-oam",
                        "ManagedElement"), "id1");
            } catch (SchemaRegistryException e) {
                throw new RuntimeException(e);
            }
        });

        assertDoesNotThrow(() -> teivDbService.execute(dbOperations));

        Result<Record> oduFuctionRecords = selectAllRowsFromTable(dslContext,
                "teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\"");
        assertEquals("id1", oduFuctionRecords.get(0).get("id"));
        assertEquals(1L, oduFuctionRecords.get(0).get("gNBId"));
        assertNull(oduFuctionRecords.get(0).get("REL_FK_managed-by-managedElement"));
        assertNull(oduFuctionRecords.get(0).get("REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION"));
        Result<Record> managedElementRecords = selectAllRowsFromTable(dslContext,
                "teiv_data.\"o-ran-smo-teiv-oam_ManagedElement\"");
        assertEquals(0, managedElementRecords.size());
    }

    @Test
    void testMergeOneToManyRelationship() throws SchemaRegistryException {
        Map<String, Object> oduFuctionEntity1 = new HashMap<>();
        oduFuctionEntity1.put("id", "id1");
        oduFuctionEntity1.put("gNBId", 1);
        teivDbOperations.merge(dslContext, entityTypeODU, oduFuctionEntity1);

        Map<String, Object> managedElementEntity1 = new HashMap<>();
        managedElementEntity1.put("id", "id1");
        managedElementEntity1.put("CD_sourceIds", JSONB.jsonb("{\"name\":\"Hellmann1\"}"));
        teivDbOperations.merge(dslContext, entityTypeMe, managedElementEntity1);

        Map<String, Object> rel = new HashMap<>();
        rel.put("id", "id1");
        rel.put("REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION", "rel_id1");
        rel.put("REL_FK_managed-by-managedElement", "id1");
        teivDbOperations.merge(dslContext, relTypeMeManagesODU, rel);

        Result<Record> rowsBeforeMerge = selectAllRowsFromTable(dslContext, "teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\"");
        assertEquals("id1", rowsBeforeMerge.get(0).get("id"));
        assertEquals(1L, rowsBeforeMerge.get(0).get("gNBId"));
        assertEquals("id1", rowsBeforeMerge.get(0).get("REL_FK_managed-by-managedElement"));
        assertEquals("rel_id1", rowsBeforeMerge.get(0).get("REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION"));

        Map<String, Object> oduFuctionEntity2 = new HashMap<>();
        oduFuctionEntity2.put("id", "id2");
        oduFuctionEntity2.put("gNBId", 2);
        teivDbOperations.merge(dslContext, entityTypeODU, oduFuctionEntity2);

        Map<String, Object> modifiedRel = new HashMap<>();
        modifiedRel.put("id", "id2");
        modifiedRel.put("REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION", "rel_id2");
        modifiedRel.put("REL_FK_managed-by-managedElement", "id1");
        teivDbOperations.merge(dslContext, relTypeMeManagesODU, modifiedRel);

        Result<?> rowsAfterMerge = selectAllRowsFromTable(dslContext, "teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\"");

        assertEquals("id1", rowsBeforeMerge.get(0).get("id"));
        assertEquals(1L, rowsBeforeMerge.get(0).get("gNBId"));
        assertEquals("id1", rowsBeforeMerge.get(0).get("REL_FK_managed-by-managedElement"));
        assertEquals("rel_id1", rowsBeforeMerge.get(0).get("REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION"));
        assertEquals("id2", rowsAfterMerge.get(1).get("id"));
        assertEquals(2L, rowsAfterMerge.get(1).get("gNBId"));
        assertEquals("id1", rowsAfterMerge.get(1).get("REL_FK_managed-by-managedElement"));
        assertEquals("rel_id2", rowsAfterMerge.get(1).get("REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION"));
    }

    @Test
    void testDeleteOneToManyRelationship() throws SchemaRegistryException {
        Map<String, Object> managedElementEntity = new HashMap<>();
        managedElementEntity.put("id", "me-id1");
        teivDbOperations.merge(dslContext, entityTypeMe, managedElementEntity);

        Map<String, Object> oduFuctionEntity = new HashMap<>();
        oduFuctionEntity.put("id", "odu-id1");
        oduFuctionEntity.put("gNBId", 1);
        teivDbOperations.merge(dslContext, entityTypeODU, oduFuctionEntity);

        Map<String, Object> meToOduFuncRelation = new HashMap<>();
        meToOduFuncRelation.put("id", "odu-id1");
        meToOduFuncRelation.put("REL_FK_managed-by-managedElement", "me-id1");
        meToOduFuncRelation.put("REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION", "eiid1");
        meToOduFuncRelation.put("REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ODUFUNCTION", JooqTypeConverter.toJsonb(List.of(
                "fdn1", "cmHandleId1")));
        teivDbOperations.merge(dslContext, entityTypeODU, meToOduFuncRelation);

        teivDbOperations.deleteRelationFromEntityTableByRelationId(dslContext, "eiid1", SchemaRegistry
                .getRelationTypeByModuleAndName("o-ran-smo-teiv-rel-oam-ran", "MANAGEDELEMENT_MANAGES_ODUFUNCTION"));

        Result<Record> rows = selectAllRowsFromTable(dslContext, "teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\"");
        assertEquals("odu-id1", rows.get(0).get("id"));
        assertEquals(1L, rows.get(0).get("gNBId"));
        assertNull(rows.get(0).get("REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION"));
        assertEquals(JooqTypeConverter.toJsonb(List.of()), rows.get(0).get(
                "REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ODUFUNCTION"));
    }

    @Test
    void testTransactionalMergeSucceeds() throws SchemaRegistryException {
        List<Consumer<DSLContext>> dbOperations = new ArrayList<>();
        Map<String, Object> managedElementEntity = new HashMap<>();
        managedElementEntity.put("id", "me-id1");
        dbOperations.add(wrDSLContext -> teivDbOperations.merge(wrDSLContext, entityTypeMe, managedElementEntity));

        Map<String, Object> oduFuctionEntity = new HashMap<>();
        oduFuctionEntity.put("id", "odu-id1");
        oduFuctionEntity.put("gNBId", 1);
        dbOperations.add(wrDSLContext -> teivDbOperations.merge(wrDSLContext, entityTypeODU, oduFuctionEntity));

        Map<String, Object> meToOduFuncRelation = new HashMap<>();
        meToOduFuncRelation.put("id", "odu-id1");
        meToOduFuncRelation.put("REL_FK_managed-by-managedElement", "me-id1");
        meToOduFuncRelation.put("REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION", "eiid1");
        meToOduFuncRelation.put("REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ODUFUNCTION", JooqTypeConverter.toJsonb(List.of(
                "fdn1", "cmHandleId1")));
        dbOperations.add(wrDSLContext -> teivDbOperations.merge(wrDSLContext, entityTypeODU, meToOduFuncRelation));

        assertDoesNotThrow(() -> teivDbService.execute(dbOperations));

        Result<Record> rowsFromOduFuctionTable = selectAllRowsFromTable(dslContext,
                "teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\"");
        assertEquals(1, rowsFromOduFuctionTable.size());
        assertEquals("me-id1", rowsFromOduFuctionTable.get(0).get("REL_FK_managed-by-managedElement"));
        assertEquals("eiid1", rowsFromOduFuctionTable.get(0).get("REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION"));
        assertEquals(JooqTypeConverter.toJsonb(List.of("fdn1", "cmHandleId1")), rowsFromOduFuctionTable.get(0).get(
                "REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ODUFUNCTION"));

        Result<Record> rowsFromManagedElement = selectAllRowsFromTable(dslContext,
                "teiv_data.\"o-ran-smo-teiv-oam_ManagedElement\"");
        assertEquals(1, rowsFromManagedElement.size());
    }

    @Test
    void testTransactionalMergeRollsBackAfterRelationshipError() throws SchemaRegistryException {
        List<Consumer<DSLContext>> dbOperations = new ArrayList<>();
        Map<String, Object> managedElementEntity = new HashMap<>();
        managedElementEntity.put("id", "me-id1");
        dbOperations.add(wrDSLContext -> teivDbOperations.merge(wrDSLContext, entityTypeMe, managedElementEntity));

        Map<String, Object> oduFuctionEntity = new HashMap<>();
        oduFuctionEntity.put("id", "odu-id1");
        oduFuctionEntity.put("gNBId", 1);
        dbOperations.add(wrDSLContext -> teivDbOperations.merge(wrDSLContext, entityTypeODU, oduFuctionEntity));

        // Create a faulty relationship map to trigger the rollback
        Map<String, Object> faultyCloudNativeSystemRelationship = new HashMap<>();
        faultyCloudNativeSystemRelationship.put("id", "odu_id1");
        faultyCloudNativeSystemRelationship.put("REL_MANAGEDELEMENT_MANAGES_ODUFUNCTION_BAAAD", "managed_element_id1");
        faultyCloudNativeSystemRelationship.put("REL_MANAGEDELEMENT_MANAGES_ODUFUNCTION_EIID", "eiid1");
        dbOperations.add(wrDSLContext -> teivDbOperations.merge(wrDSLContext, entityTypeODU,
                faultyCloudNativeSystemRelationship));

        assertThrows(TeivException.class, () -> teivDbService.execute(dbOperations));

        Result<Record> rowsFromManagedElementTable = selectAllRowsFromTable(dslContext,
                "teiv_data.\"o-ran-smo-teiv-oam_ManagedElement\"");
        assertEquals(0, rowsFromManagedElementTable.size());

        Result<Record> rowsFromCloudNativeSystem = selectAllRowsFromTable(dslContext,
                "teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\"");
        assertEquals(0, rowsFromCloudNativeSystem.size());
    }

    @Test
    void testTransactionalDeleteSucceeds() throws SchemaRegistryException {
        List<Consumer<DSLContext>> dbOperations = new ArrayList<>();
        Map<String, Object> managedElementEntity = new HashMap<>();
        managedElementEntity.put("id", "me-id1");
        teivDbOperations.merge(dslContext, entityTypeMe, managedElementEntity);

        Map<String, Object> oduFuctionEntity = new HashMap<>();
        oduFuctionEntity.put("id", "odu-id1");
        oduFuctionEntity.put("gNBId", 1);
        teivDbOperations.merge(dslContext, entityTypeODU, oduFuctionEntity);

        Map<String, Object> meToOduFuncRelation = new HashMap<>();
        meToOduFuncRelation.put("id", "odu-id1");
        meToOduFuncRelation.put("REL_FK_managed-by-managedElement", "me-id1");
        meToOduFuncRelation.put("REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION", "eiid1");
        meToOduFuncRelation.put("REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ODUFUNCTION", JooqTypeConverter.toJsonb(List.of(
                "fdn1", "cmHandleId1")));
        teivDbOperations.merge(dslContext, entityTypeODU, meToOduFuncRelation);

        Result<Record> rowsFromOduFuctionBeforeDelete = selectAllRowsFromTable(dslContext,
                "teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\"");
        assertEquals(1, rowsFromOduFuctionBeforeDelete.size());
        assertEquals("me-id1", rowsFromOduFuctionBeforeDelete.get(0).get("REL_FK_managed-by-managedElement"));
        assertEquals("eiid1", rowsFromOduFuctionBeforeDelete.get(0).get("REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION"));
        assertEquals(JooqTypeConverter.toJsonb(List.of("fdn1", "cmHandleId1")), rowsFromOduFuctionBeforeDelete.get(0).get(
                "REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ODUFUNCTION"));

        Result<Record> rowsFromManagedElementBeforeDelete = selectAllRowsFromTable(dslContext,
                "teiv_data.\"o-ran-smo-teiv-oam_ManagedElement\"");
        assertEquals(1, rowsFromManagedElementBeforeDelete.size());

        dbOperations.add(wrDSLContext -> {
            try {
                teivDbOperations.deleteEntity(wrDSLContext, SchemaRegistry.getEntityTypeByModuleAndName(
                        "o-ran-smo-teiv-ran", "ODUFunction"), "odu-id1");
            } catch (SchemaRegistryException e) {
                e.printStackTrace();
            }
        });

        dbOperations.add(wrDSLContext -> {
            try {
                teivDbOperations.deleteEntity(wrDSLContext, SchemaRegistry.getEntityTypeByModuleAndName(
                        "o-ran-smo-teiv-oam", "ManagedElement"), "me-id1");
            } catch (SchemaRegistryException e) {
                e.printStackTrace();
            }
        });

        dbOperations.add(wrDSLContext -> {
            try {
                teivDbOperations.deleteRelationFromEntityTableByRelationId(wrDSLContext, "eiid1", SchemaRegistry
                        .getRelationTypeByModuleAndName("o-ran-smo-teiv-rel-oam-ran",
                                "MANAGEDELEMENT_MANAGES_ODUFUNCTION"));
            } catch (SchemaRegistryException e) {
                e.printStackTrace();
            }
        });

        assertDoesNotThrow(() -> teivDbService.execute(dbOperations));

        Result<Record> rowsFromManagedElementTable = selectAllRowsFromTable(dslContext,
                "teiv_data.\"o-ran-smo-teiv-oam_ManagedElement\"");
        assertEquals(0, rowsFromManagedElementTable.size());

        Result<Record> rowsOduFuction = selectAllRowsFromTable(dslContext, "teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\"");
        assertEquals(0, rowsOduFuction.size());
    }

    @Test
    void testTransactionalDeleteRollbackAfterRelationshipError() throws SchemaRegistryException {
        List<Consumer<DSLContext>> dbOperations = new ArrayList<>();
        Map<String, Object> managedElementEntity = new HashMap<>();
        managedElementEntity.put("id", "me-id1");
        teivDbOperations.merge(dslContext, entityTypeMe, managedElementEntity);

        Map<String, Object> oduFuctionEntity = new HashMap<>();
        oduFuctionEntity.put("id", "odu-id1");
        oduFuctionEntity.put("gNBId", 1);
        teivDbOperations.merge(dslContext, entityTypeODU, oduFuctionEntity);

        Map<String, Object> meToOduFuncRelation = new HashMap<>();
        meToOduFuncRelation.put("id", "odu-id1");
        meToOduFuncRelation.put("REL_FK_managed-by-managedElement", "me-id1");
        meToOduFuncRelation.put("REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION", "eiid1");
        meToOduFuncRelation.put("REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ODUFUNCTION", JooqTypeConverter.toJsonb(List.of(
                "fdn1", "cmHandleId1")));
        teivDbOperations.merge(dslContext, entityTypeODU, meToOduFuncRelation);

        Result<Record> rowsFromManagedElementBeforeDelete = selectAllRowsFromTable(dslContext,
                "teiv_data.\"o-ran-smo-teiv-oam_ManagedElement\"");
        assertEquals(1, rowsFromManagedElementBeforeDelete.size());

        Result<Record> rowsFromOduFuctionBeforeDelete = selectAllRowsFromTable(dslContext,
                "teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\"");
        assertEquals(1, rowsFromOduFuctionBeforeDelete.size());
        assertEquals(1, rowsFromOduFuctionBeforeDelete.size());
        assertEquals("me-id1", rowsFromOduFuctionBeforeDelete.get(0).get("REL_FK_managed-by-managedElement"));
        assertEquals("eiid1", rowsFromOduFuctionBeforeDelete.get(0).get("REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION"));
        assertEquals(JooqTypeConverter.toJsonb(List.of("fdn1", "cmHandleId1")), rowsFromOduFuctionBeforeDelete.get(0).get(
                "REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ODUFUNCTION"));

        dbOperations.add(wrDSLContext -> {
            try {
                teivDbOperations.deleteEntity(wrDSLContext, SchemaRegistry.getEntityTypeByModuleAndName(
                        "o-ran-smo-teiv-oam", "ManagedElement"), "me-id1");
            } catch (SchemaRegistryException e) {
                e.printStackTrace();
            }
        });
        dbOperations.add(wrDSLContext -> {
            try {
                teivDbOperations.deleteEntity(wrDSLContext, SchemaRegistry.getEntityTypeByModuleAndName(
                        "o-ran-smo-teiv-ran", "ODUFunction"), "odu-id1");
            } catch (SchemaRegistryException e) {
                e.printStackTrace();
            }
        });

        // Add a faulty relationship delete to trigger the rollback
        dbOperations.add(wrDSLContext -> {
            teivDbOperations.deleteRelationFromEntityTableByRelationId(wrDSLContext, "eiid1", null);
        });

        assertThrows(TeivException.class, () -> teivDbService.execute(dbOperations));

        Result<Record> rowsFromManagedElement = selectAllRowsFromTable(dslContext,
                "teiv_data.\"o-ran-smo-teiv-oam_ManagedElement\"");
        assertEquals(1, rowsFromManagedElement.size());

        Result<Record> rowsFromOduFuction = selectAllRowsFromTable(dslContext,
                "teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\"");
        assertEquals(1, rowsFromOduFuction.size());
        assertEquals(1, rowsFromOduFuction.size());
        assertEquals("me-id1", rowsFromOduFuction.get(0).get("REL_FK_managed-by-managedElement"));
        assertEquals("eiid1", rowsFromOduFuction.get(0).get("REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION"));
        assertEquals(JooqTypeConverter.toJsonb(List.of("fdn1", "cmHandleId1")), rowsFromOduFuction.get(0).get(
                "REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ODUFUNCTION"));
    }

    @Test
    void testDeadlockRetry() throws InterruptedException, SchemaRegistryException {
        List<Consumer<DSLContext>> dbOperations1 = new ArrayList<>();
        List<Consumer<DSLContext>> dbOperations2 = new ArrayList<>();

        Map<String, Object> managedElementEntity1 = new HashMap<>();
        managedElementEntity1.put("id", "id1");

        Map<String, Object> managedElementEntity2 = new HashMap<>();
        managedElementEntity2.put("id", "id2");

        Map<String, Object> managedElementEntity3 = new HashMap<>();
        managedElementEntity3.put("id", "id3");

        Map<String, Object> managedElementEntity4 = new HashMap<>();
        managedElementEntity4.put("id", "id4");

        final CountDownLatch firstTransactionCompletedMergeEntity1 = new CountDownLatch(1);
        final CountDownLatch secondTransactionCompletedMergeEntity2 = new CountDownLatch(1);
        dbOperations1.add(dslContext -> {
            teivDbOperations.merge(dslContext, entityTypeMe, managedElementEntity1);
            firstTransactionCompletedMergeEntity1.countDown();
            try {
                secondTransactionCompletedMergeEntity2.await();
                teivDbOperations.merge(dslContext, entityTypeMe, managedElementEntity2);
                teivDbOperations.merge(dslContext, entityTypeMe, managedElementEntity3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        // Try to add the same rows in another transaction in another order.
        dbOperations2.add(dslContext -> {
            try {
                teivDbOperations.merge(dslContext, entityTypeMe, managedElementEntity2);
                secondTransactionCompletedMergeEntity2.countDown();
                firstTransactionCompletedMergeEntity1.await();
                teivDbOperations.merge(dslContext, entityTypeMe, managedElementEntity1);
                teivDbOperations.merge(dslContext, entityTypeMe, managedElementEntity4);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread t1 = new Thread(() -> teivDbService.execute(dbOperations1));
        Thread t2 = new Thread(() -> teivDbService.execute(dbOperations2));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        Result<Record> rowsFromManagedElement = selectAllRowsFromTable(dslContext,
                "teiv_data.\"o-ran-smo-teiv-oam_ManagedElement\"");
        assertEquals(4, rowsFromManagedElement.size());
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
        assertThrows(TeivException.class, () -> teivDbService.execute(dbOperations));
        assertEquals(maxRetryAttemptsForDeadlock, attempts.get());
    }

    /**
     * The out of the box binding for geography is available in the commercial jOOQ
     * distribution only. Because of this,
     * a select * from tableName; query fails if the table has a column with
     * geography type. Even if the value in that
     * column is null.
     *
     * @param readDataDslContext
     * @param tableName
     *     For example: teiv_data."AntennaModule"
     * @return the fetched rows. Values of geography type are represented as String
     *     values.
     */
    public static Result<Record> selectAllRowsFromTable(DSLContext readDataDslContext, final String tableName) {
        String unqualifiedName = tableName.split("\\.")[1].split("\"")[1];
        Table<?> table = readDataDslContext.meta().getTables().stream().filter(t -> t.getName().equals(unqualifiedName))
                .toList().get(0);
        List<Column> columns = readDataDslContext.meta(table).informationSchema().getColumns();
        List<Field<?>> columnsToSelect = columns.stream().map(c -> {
            if ("geography".equals(c.getDataType())) {
                return field(String.format(TeivConstants.ST_TO_STRING, String.format(QUOTED_STRING, c.getColumnName())));
            } else if ("jsonb".equals(c.getDataType())) {
                return field(name(c.getColumnName()), JSONB.class);
            } else {
                return field(name(c.getColumnName()));
            }
        }).toList();
        return readDataDslContext.select(columnsToSelect).from(table(tableName)).fetch();
    }
}
