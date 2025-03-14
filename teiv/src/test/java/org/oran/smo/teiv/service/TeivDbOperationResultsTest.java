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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.oran.smo.teiv.service.models.OperationResult.ENTITY_CATEGORY;
import static org.oran.smo.teiv.service.models.OperationResult.RELATIONSHIP_CATEGORY;
import static org.oran.smo.teiv.utils.TeivConstants.TEIV_DATA_SCHEMA;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.sql.DataSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.oran.smo.teiv.schema.SchemaRegistryException;
import org.oran.smo.teiv.service.models.OperationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;

import org.oran.smo.teiv.db.TestPostgresqlContainer;
import org.oran.smo.teiv.exception.InvalidFieldInYangDataException;
import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.ingestion.DeadlockRetryPolicy;
import org.oran.smo.teiv.ingestion.validation.IngestionOperationValidatorFactory;
import org.oran.smo.teiv.ingestion.validation.MaximumCardinalityViolationException;
import org.oran.smo.teiv.schema.EntityType;
import org.oran.smo.teiv.schema.BidiDbNameMapper;

import org.oran.smo.teiv.schema.PostgresSchemaLoader;
import org.oran.smo.teiv.schema.RelationType;
import org.oran.smo.teiv.schema.SchemaLoaderException;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.service.cloudevent.CloudEventParser;
import org.oran.smo.teiv.service.cloudevent.data.ParsedCloudEventData;
import org.oran.smo.teiv.service.cloudevent.data.Relationship;
import org.oran.smo.teiv.startup.SchemaHandler;
import org.oran.smo.teiv.utils.CloudEventTestUtil;
import org.oran.smo.teiv.utils.JooqTypeConverter;

@Configuration
@SpringBootTest
@ActiveProfiles({ "test", "ingestion" })
class TeivDbOperationResultsTest {
    private static TestPostgresqlContainer postgresqlContainer = TestPostgresqlContainer.getInstance();
    private static TeivDbService teivDbService;
    private static TeivDbOperations teivDbOperations;
    private static DSLContext dslContext;
    private static String VALIDATE_MANY_TO_ONE_DIR = "src/test/resources/cloudeventdata/validation/many-to-one/";
    private static String VALIDATE_ONE_TO_MANY_DIR = "src/test/resources/cloudeventdata/validation/one-to-many/";
    private static String VALIDATE_ONE_TO_ONE_DIR = "src/test/resources/cloudeventdata/validation/one-to-one/";
    private static EntityType entityTypeMe;
    private static EntityType entityTypeORU;
    private static EntityType entityTypeAntennMod;
    private static EntityType entityTypeAntennModLongName;
    private static EntityType entityTypeAntennCap;
    private static RelationType relTypeMeManagesORU;
    private static RelationType relTypeantennServesAntennCap;
    private static RelationType relTypeAntennModRealisedByAntennaModLongName;

    @Autowired
    CloudEventParser cloudEventParser;
    @MockBean
    private SchemaHandler schemaHandler;

    @BeforeAll
    public static void beforeAll(@Autowired DeadlockRetryPolicy deadlockRetryPolicy) throws UnsupportedOperationException,
            SchemaLoaderException {
        String url = postgresqlContainer.getJdbcUrl();
        DataSource ds = DataSourceBuilder.create().url(url).username("test").password("test").build();
        dslContext = DSL.using(ds, SQLDialect.POSTGRES);
        teivDbService = new TeivDbService(dslContext, dslContext, deadlockRetryPolicy);
        teivDbOperations = new TeivDbOperations(teivDbService, new IngestionOperationValidatorFactory(),
                new RelationshipMergeValidator(), new TeivMetadataResolver());
        TestPostgresqlContainer.loadIngestionTestData();
        PostgresSchemaLoader postgresSchemaLoader = new PostgresSchemaLoader(dslContext, new ObjectMapper());
        postgresSchemaLoader.loadSchemaRegistry();
    }

    @BeforeEach
    public void deleteAllAndMakeTypes() {
        TestPostgresqlContainer.truncateSchemas(List.of(TEIV_DATA_SCHEMA), dslContext);
        try {
            entityTypeMe = SchemaRegistry.getEntityTypeByModuleAndName("o-ran-smo-teiv-oam", "ManagedElement");
            entityTypeORU = SchemaRegistry.getEntityTypeByModuleAndName("o-ran-smo-teiv-ran", "ORUFunction");
            entityTypeAntennMod = SchemaRegistry.getEntityTypeByModuleAndName("o-ran-smo-teiv-equipment", "AntennaModule");
            entityTypeAntennModLongName = SchemaRegistry.getEntityTypeByModuleAndName("o-ran-smo-teiv-equipment",
                    "AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            entityTypeAntennCap = SchemaRegistry.getEntityTypeByModuleAndName("o-ran-smo-teiv-ran", "AntennaCapability");
            relTypeMeManagesORU = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-rel-oam-ran",
                    "MANAGEDELEMENT_MANAGES_ORUFUNCTION");
            relTypeantennServesAntennCap = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-rel-equipment-ran",
                    "ANTENNAMODULE_SERVES_ANTENNACAPABILITY");
            relTypeAntennModRealisedByAntennaModLongName = SchemaRegistry.getRelationTypeByModuleAndName(
                    "o-ran-smo-teiv-equipment", "ANTENNAMODULEEEEEEEEEEEE_REALISED_BY_ANTENNAMODULEEEEEEEEEEEEEEE");
        } catch (SchemaRegistryException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testMergeEntityRelationship() {
        CloudEvent cloudEvent = CloudEventTestUtil.getCloudEventFromJsonFile(
                "src/test/resources/cloudeventdata/end-to-end/ce-create-one-to-many.json");
        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
        List<OperationResult> mergeResult = assertDoesNotThrow(() -> teivDbOperations
                .executeEntityAndRelationshipMergeOperations(parsedCloudEventData, "dmi-plugin:nm-1"));

        assertEquals(3, mergeResult.size());
        assertEquals("ManagedElement_1", mergeResult.get(0).getId());
        assertEquals("ManagedElement", mergeResult.get(0).getType());
        assertEquals(Map.of(), mergeResult.get(0).getAttributes());

        assertEquals("ORUFunction_1", mergeResult.get(1).getId());
        assertEquals("ORUFunction", mergeResult.get(1).getType());
        assertEquals(Map.of(), mergeResult.get(1).getAttributes());

        assertEquals("relation_1", mergeResult.get(2).getId());
        assertEquals("MANAGEDELEMENT_MANAGES_ORUFUNCTION", mergeResult.get(2).getType());
        assertEquals("ManagedElement_1", mergeResult.get(2).getASide());
        assertEquals("ORUFunction_1", mergeResult.get(2).getBSide());

    }

    @Test
    void testDeleteEntityById() throws SchemaRegistryException {
        Map<String, Object> managedElementEntity = new HashMap<>();
        managedElementEntity.put("id", "managed_element_entity_id1");
        teivDbOperations.merge(dslContext, entityTypeMe, managedElementEntity);

        // Delete operation - expected to succeed
        List<OperationResult> deleteResultMatch = teivDbOperations.deleteEntity(dslContext, SchemaRegistry
                .getEntityTypeByModuleAndName("o-ran-smo-teiv-oam", "ManagedElement"), "managed_element_entity_id1");

        assertFalse(deleteResultMatch.isEmpty(), "Delete operation should return a non-empty list");
        assertTrue(deleteResultMatch.contains(OperationResult.builder().id("managed_element_entity_id1").type(
                "ManagedElement").category(ENTITY_CATEGORY).build()),
                "The list should contain the delete operation result with id: 'managed_element_entity_id1'");

        // Delete operation with the same EIID - expected to fail
        List<OperationResult> deleteResultNoMatch = teivDbOperations.deleteEntity(dslContext, SchemaRegistry
                .getEntityTypeByModuleAndName("o-ran-smo-teiv-oam", "ManagedElement"), "managed_element_entity_id1");
        assertTrue(deleteResultNoMatch.isEmpty(),
                "Delete operation should return an empty list for already deleted/non existing ID");
    }

    @Test
    void testDeleteOneToOneByRelationId() throws SchemaRegistryException {
        Map<String, Object> managedElementEntity = new HashMap<>();
        managedElementEntity.put("id", "me-id1");
        EntityType entityTypeMeLongName = SchemaRegistry.getEntityTypeByModuleAndName("o-ran-smo-teiv-oam",
                "ManagedElementtttttttttttttttttttttttttttttttttttttttttttttttttt");
        teivDbOperations.merge(dslContext, entityTypeMeLongName, managedElementEntity);

        Map<String, Object> nrCellDuEntity = new HashMap<>();
        nrCellDuEntity.put("id", "nrcelldu-id1");
        nrCellDuEntity.put("020335B0F627C169E24167748C38FE756FB34AE2", 1);
        EntityType entityTypeNrCellDuLongName = SchemaRegistry.getEntityTypeByModuleAndName("o-ran-smo-teiv-ran",
                "NRCellDUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");
        teivDbOperations.merge(dslContext, entityTypeNrCellDuLongName, nrCellDuEntity);

        Map<String, Object> meToNrcellduRelation = new HashMap<>();
        meToNrcellduRelation.put("id", "me-id1");
        meToNrcellduRelation.put("REL_FK_used-nrCellDu", "nrcelldu-id1");
        meToNrcellduRelation.put("REL_ID_ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU", "eiid1");
        meToNrcellduRelation.put("REL_CD_1F61FA6DDAECE90540F9880F2A98037B1530A5A4", JooqTypeConverter.toJsonb(List.of(
                "fdn1", "cmHandleId1")));
        teivDbOperations.merge(dslContext, entityTypeMeLongName, meToNrcellduRelation);

        // Delete operation for aSide - expected to succeed
        Optional<OperationResult> deleteASideResultMatch = teivDbOperations.deleteRelationFromEntityTableByRelationId(
                dslContext, "eiid1", SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-rel-oam-ran",
                        "ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU"));

        assertTrue(deleteASideResultMatch.isPresent(), "Delete operation should return a present Optional");
        assertEquals(OperationResult.builder().id("eiid1").type("ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU")
                .category(RELATIONSHIP_CATEGORY).build(), deleteASideResultMatch.get(),
                "The delete operation result should be present for: 'eiid1'");

        // Delete operation with the same EIID - expected to fail
        Optional<OperationResult> deleteResultNoMatch = teivDbOperations.deleteRelationFromEntityTableByRelationId(
                dslContext, "eiid1", SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-rel-oam-ran",
                        "ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU"));
        assertTrue(deleteResultNoMatch.isEmpty(),
                "Delete operation should return an empty list for already deleted/non existing ID");

        Result<Record> nrcellduRows = TeivDbServiceContainerizedTest.selectAllRowsFromTable(dslContext,
                "teiv_data.\"84E676149362F50C55FE1E004B98D4891916BBF3\"");
        Result<Record> managedElementRows = TeivDbServiceContainerizedTest.selectAllRowsFromTable(dslContext,
                "teiv_data.\"28C9A375E800E82308EBE7DA2932EF2C0AF13C38\"");
        assertEquals("me-id1", managedElementRows.get(0).get("id"));
        assertEquals("nrcelldu-id1", nrcellduRows.get(0).get("id"));
        assertNull(managedElementRows.get(0).get("REL_FK_used-nrCellDu"));
        assertNull(managedElementRows.get(0).get("REL_ID_ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU"));
        assertEquals(JooqTypeConverter.toJsonb(List.of()), managedElementRows.get(0).get(
                "REL_CD_1F61FA6DDAECE90540F9880F2A98037B1530A5A4"));
    }

    @Test
    void testDeleteOneToManyByManySideEntityId() throws SchemaRegistryException {
        Map<String, Object> managedElementEntity = new HashMap<>();
        managedElementEntity.put("id", "me-id1");
        teivDbOperations.merge(dslContext, entityTypeMe, managedElementEntity);

        Map<String, Object> oruFunctionEntity = new HashMap<>();
        oruFunctionEntity.put("id", "oru-id1");
        oruFunctionEntity.put("oruId", 1);
        teivDbOperations.merge(dslContext, entityTypeORU, oruFunctionEntity);

        Map<String, Object> meToORUFuncRelation = new HashMap<>();
        meToORUFuncRelation.put("id", "oru-id1");
        meToORUFuncRelation.put("REL_FK_managed-by-managedElement", "me-id1");
        meToORUFuncRelation.put("REL_ID_MANAGEDELEMENT_MANAGES_ORUFUNCTION", "eiid1");
        meToORUFuncRelation.put("REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ORUFUNCTION", JooqTypeConverter.toJsonb(List.of(
                "fdn1", "cmHandleId1")));
        teivDbOperations.merge(dslContext, relTypeMeManagesORU, meToORUFuncRelation);

        // Delete operation with existing relationship
        List<OperationResult> deleteResultMatch = teivDbOperations.deleteRelationshipByManySideEntityId(dslContext,
                "oru-id1", "id", SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-rel-oam-ran",
                        "MANAGEDELEMENT_MANAGES_ORUFUNCTION"));

        assertFalse(deleteResultMatch.isEmpty(), "Delete operation should return a non-empty list");
        assertTrue(deleteResultMatch.contains(OperationResult.builder().id("eiid1").type(
                "MANAGEDELEMENT_MANAGES_ORUFUNCTION").category(RELATIONSHIP_CATEGORY).build()),
                "The list should contain the delete operation result with id: 'eiid1'");

        // Delete operation with the same entity ID - expected to return an empty list
        List<OperationResult> deleteResultNoMatch = teivDbOperations.deleteRelationshipByManySideEntityId(dslContext,
                "oru-id1", "id", SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-rel-oam-ran",
                        "MANAGEDELEMENT_MANAGES_ORUFUNCTION"));

        assertTrue(deleteResultNoMatch.isEmpty(),
                "Delete operation should return an empty list for already deleted/non existing ID");
    }

    @Test
    void testDeleteOneToManyByOneSideEntityId() throws SchemaRegistryException {
        Map<String, Object> managedElementEntity = new HashMap<>();
        managedElementEntity.put("id", "me-id1");
        teivDbOperations.merge(dslContext, entityTypeMe, managedElementEntity);

        Map<String, Object> meToORUFuncRelation1 = new HashMap<>();
        meToORUFuncRelation1.put("id", "oru-id1");
        meToORUFuncRelation1.put("REL_FK_managed-by-managedElement", "me-id1");
        meToORUFuncRelation1.put("REL_ID_MANAGEDELEMENT_MANAGES_ORUFUNCTION", "eiid1");
        meToORUFuncRelation1.put("REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ORUFUNCTION", JooqTypeConverter.toJsonb(List.of(
                "fdn1", "cmHandleId1")));
        teivDbOperations.merge(dslContext, relTypeMeManagesORU, meToORUFuncRelation1);
        Map<String, Object> meToORUFuncRelation2 = new HashMap<>();
        meToORUFuncRelation2.put("id", "oru-id2");
        meToORUFuncRelation2.put("REL_FK_managed-by-managedElement", "me-id1");
        meToORUFuncRelation2.put("REL_ID_MANAGEDELEMENT_MANAGES_ORUFUNCTION", "eiid2");
        meToORUFuncRelation2.put("REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ORUFUNCTION", JooqTypeConverter.toJsonb(List.of(
                "fdn1", "cmHandleId1")));
        teivDbOperations.merge(dslContext, relTypeMeManagesORU, meToORUFuncRelation2);
        Map<String, Object> meToORUFuncRelation3 = new HashMap<>();
        meToORUFuncRelation3.put("id", "oru-id3");
        meToORUFuncRelation3.put("REL_FK_managed-by-managedElement", "me-id1");
        meToORUFuncRelation3.put("REL_ID_MANAGEDELEMENT_MANAGES_ORUFUNCTION", "eiid3");
        meToORUFuncRelation3.put("REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ORUFUNCTION", JooqTypeConverter.toJsonb(List.of(
                "fdn1", "cmHandleId1")));
        teivDbOperations.merge(dslContext, relTypeMeManagesORU, meToORUFuncRelation3);

        // Delete operation for me-id1
        List<OperationResult> deleteResultMatch = teivDbOperations.deleteEntity(dslContext, SchemaRegistry
                .getEntityTypeByModuleAndName("o-ran-smo-teiv-oam", "ManagedElement"), "me-id1");
        assertFalse(deleteResultMatch.isEmpty(), "Delete operation should return a non-empty list");

        // Check if all expected IDs are present in the deletion result
        assertEquals(4, deleteResultMatch.size(), "Delete operation should match expected size");
        assertTrue(deleteResultMatch.contains(OperationResult.builder().id("me-id1").type("ManagedElement").category(
                ENTITY_CATEGORY).build()), "The list should contain the delete operation result with id: 'me-id1'");

        assertTrue(deleteResultMatch.contains(OperationResult.builder().id("eiid1").type(
                "MANAGEDELEMENT_MANAGES_ORUFUNCTION").category(RELATIONSHIP_CATEGORY).build()),
                "The list should contain the delete operation result with id: 'eiid1'");
        assertTrue(deleteResultMatch.contains(OperationResult.builder().id("eiid2").type(
                "MANAGEDELEMENT_MANAGES_ORUFUNCTION").category(RELATIONSHIP_CATEGORY).build()),
                "The list should contain the delete operation result with id: 'eiid2'");
        assertTrue(deleteResultMatch.contains(OperationResult.builder().id("eiid3").type(
                "MANAGEDELEMENT_MANAGES_ORUFUNCTION").category(RELATIONSHIP_CATEGORY).build()),
                "The list should contain the delete operation result with id: 'eiid3'");

        // Verify all related entities have their relationships deleted
        Result<Record> rows = TeivDbServiceContainerizedTest.selectAllRowsFromTable(dslContext,
                "teiv_data.\"o-ran-smo-teiv-ran_ORUFunction\"");
        assertEquals(3, rows.size());
        for (Record row : rows) {
            assertNull(row.get("REL_FK_managed-by-managedElement"),
                    "REL_MANAGEDELEMENT_MANAGES_ORUFUNCTION should be null");
            assertNull(row.get("REL_ID_MANAGEDELEMENT_MANAGES_ORUFUNCTION"),
                    "REL_MANAGEDELEMENT_MANAGES_ORUFUNCTION_EIID should be null");
        }
    }

    @Test
    void testDeleteManyToManyByRelationshipId() throws SchemaRegistryException {
        Map<String, Object> antennaModule1 = new HashMap<>();
        antennaModule1.put("id", "antennamodule_id1");
        antennaModule1.put("positionWithinSector", "center");

        Map<String, Object> antennaCapability1 = new HashMap<>();
        antennaCapability1.put("id", "antennacapability_id1");
        antennaCapability1.put("geranFqBands", JSONB.jsonb("{\"name\":\"geranFqBands1\"}"));

        Map<String, Object> antennaCapability2 = new HashMap<>();
        antennaCapability2.put("id", "antennacapability_id2");
        antennaCapability2.put("geranFqBands", JSONB.jsonb("{\"name\":\"geranFqBands2\"}"));

        Map<String, Object> rel1 = new HashMap<>();
        rel1.put("id", "rel_id1");
        rel1.put("aSide_AntennaModule", "antennamodule_id1");
        rel1.put("bSide_AntennaCapability", "antennacapability_id1");

        Map<String, Object> rel2 = new HashMap<>();
        rel2.put("id", "rel_id2");
        rel2.put("aSide_AntennaModule", "antennamodule_id1");
        rel2.put("bSide_AntennaCapability", "antennacapability_id2");

        teivDbOperations.merge(dslContext, entityTypeAntennMod, antennaModule1);
        teivDbOperations.merge(dslContext, entityTypeAntennCap, antennaCapability1);
        teivDbOperations.merge(dslContext, entityTypeAntennCap, antennaCapability2);
        teivDbOperations.merge(dslContext, relTypeantennServesAntennCap, rel1);
        teivDbOperations.merge(dslContext, relTypeantennServesAntennCap, rel2);

        Result<Record> row1 = TeivDbServiceContainerizedTest.selectAllRowsFromTable(dslContext,
                "teiv_data.\"o-ran-smo-teiv-equipment_AntennaModule\"");
        assertEquals(1, row1.size());
        Result<Record> row2 = TeivDbServiceContainerizedTest.selectAllRowsFromTable(dslContext,
                "teiv_data.\"o-ran-smo-teiv-ran_AntennaCapability\"");
        assertEquals(2, row2.size());
        Result<Record> row3 = TeivDbServiceContainerizedTest.selectAllRowsFromTable(dslContext,
                "teiv_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\"");
        assertEquals(2, row3.size());

        RelationType relType = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-rel-equipment-ran",
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY");

        // Test deletion of a relationship by ID (expected success)
        Optional<OperationResult> deleteResultMatch = teivDbOperations.deleteManyToManyRelationByRelationId(dslContext,
                relType, "rel_id1");
        assertTrue(deleteResultMatch.isPresent(), "Delete operation should return a present Optional");
        assertEquals(OperationResult.builder().id("rel_id1").type("ANTENNAMODULE_SERVES_ANTENNACAPABILITY").category(
                RELATIONSHIP_CATEGORY).build(), deleteResultMatch.get(), "Deleted relationship ID should match 'rel_id1'");

        // Test deletion of the same relationship ID again (expected failure)
        Optional<OperationResult> deleteResultNoMatch = teivDbOperations.deleteManyToManyRelationByRelationId(dslContext,
                relType, "rel_id1");
        assertTrue(deleteResultNoMatch.isEmpty(), "Delete operation should not return a present Optional");
    }

    @Test
    void testDeleteManyToManyByEntityId() throws SchemaRegistryException {
        Map<String, Object> antennaModule1 = new HashMap<>();
        antennaModule1.put("id", "antennamodule_id1");
        antennaModule1.put("positionWithinSector", "center");

        Map<String, Object> antennaCapability1 = new HashMap<>();
        antennaCapability1.put("id", "antennacapability_id1");
        antennaCapability1.put("geranFqBands", JSONB.jsonb("{\"name\":\"geranFqBands1\"}"));

        Map<String, Object> antennaCapability2 = new HashMap<>();
        antennaCapability2.put("id", "antennacapability_id2");
        antennaCapability2.put("geranFqBands", JSONB.jsonb("{\"name\":\"geranFqBands2\"}"));

        Map<String, Object> rel1 = new HashMap<>();
        rel1.put("id", "rel_id1");
        rel1.put("aSide_AntennaModule", "antennamodule_id1");
        rel1.put("bSide_AntennaCapability", "antennacapability_id1");

        Map<String, Object> rel2 = new HashMap<>();
        rel2.put("id", "rel_id2");
        rel2.put("aSide_AntennaModule", "antennamodule_id1");
        rel2.put("bSide_AntennaCapability", "antennacapability_id2");

        teivDbOperations.merge(dslContext, entityTypeAntennMod, antennaModule1);
        teivDbOperations.merge(dslContext, entityTypeAntennCap, antennaCapability1);
        teivDbOperations.merge(dslContext, entityTypeAntennCap, antennaCapability2);
        teivDbOperations.merge(dslContext, relTypeantennServesAntennCap, rel1);
        teivDbOperations.merge(dslContext, relTypeantennServesAntennCap, rel2);

        assertEquals(1, TeivDbServiceContainerizedTest.selectAllRowsFromTable(dslContext,
                "teiv_data.\"o-ran-smo-teiv-equipment_AntennaModule\"").size(), "Expected one AntennaModule record");
        assertEquals(2, TeivDbServiceContainerizedTest.selectAllRowsFromTable(dslContext,
                "teiv_data.\"o-ran-smo-teiv-ran_AntennaCapability\"").size(), "Expected two AntennaCapability records");
        assertEquals(2, TeivDbServiceContainerizedTest.selectAllRowsFromTable(dslContext,
                "teiv_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\"").size(),
                "Expected two ANTENNAMODULE_SERVES_ANTENNACAPABILITY relations");

        RelationType relType = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-rel-equipment-ran",
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY");

        // Test deletion of relations by entity ID (expected to delete two relations)
        List<OperationResult> deleteResultMatch = teivDbOperations.deleteManyToManyRelationByEntityId(dslContext, relType,
                "antennamodule_id1", "aSide_AntennaModule", "bSide_AntennaCapability");
        assertEquals(2, deleteResultMatch.size(), "Expected two relations to be deleted");
        assertTrue(deleteResultMatch.contains(OperationResult.builder().id("rel_id1").type(
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY").category(RELATIONSHIP_CATEGORY).build()),
                "The list should contain the delete operation result with id: 'rel_id1'");

        assertTrue(deleteResultMatch.contains(OperationResult.builder().id("rel_id2").type(
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY").category(RELATIONSHIP_CATEGORY).build()),
                "The list should contain the delete operation result with id: 'rel_id2'");

        // Test deletion of relations by the same entity ID again (expected to find no
        // relations to delete)
        List<OperationResult> deleteResultNoMatch = teivDbOperations.deleteManyToManyRelationByEntityId(dslContext, relType,
                "antennamodule_id1", "aSide_AntennaModule", "bSide_AntennaCapability");
        assertTrue(deleteResultNoMatch.isEmpty(),
                "Delete operation should return an empty list for already deleted/non existing ID");
    }

    @Test
    void testDeleteRelConnectingSameEntityByRelationshipId() throws SchemaRegistryException {
        Map<String, Object> antennaModule1 = new HashMap<>();
        antennaModule1.put("id", "module_id1");
        antennaModule1.put("mechanicalAntennaTilt", 400);
        antennaModule1.put("antennaModelNumber", "['123-abc']");
        antennaModule1.put("totalTilt", 10);
        antennaModule1.put("mechanicalAntennaBearing", 123);
        antennaModule1.put("positionWithinSector", "['123', '456', '789']");
        antennaModule1.put("electricalAntennaTilt", 1);

        Map<String, Object> antennaModule2 = new HashMap<>();
        antennaModule2.put("id", "module_id2");
        antennaModule2.put("mechanicalAntennaTilt", 401);
        antennaModule2.put("antennaModelNumber", "['456-abc']");
        antennaModule2.put("totalTilt", 11);
        antennaModule2.put("mechanicalAntennaBearing", 456);
        antennaModule2.put("positionWithinSector", "['123', '456', '789']");
        antennaModule2.put("electricalAntennaTilt", 2);

        Map<String, Object> rel1 = new HashMap<>();
        rel1.put("id", "rel_id1");
        rel1.put("aSide_2A2D3374BF907674FA1905478E30ACB8882DC03C", "module_id1");
        rel1.put("bSide_EE6DD4A2CFD743779BBCBFC18FC296EF6D72EB1E", "module_id2");

        Map<String, Object> rel2 = new HashMap<>();
        rel2.put("id", "rel_id2");
        rel2.put("aSide_2A2D3374BF907674FA1905478E30ACB8882DC03C", "module_id2");
        rel2.put("bSide_EE6DD4A2CFD743779BBCBFC18FC296EF6D72EB1E", "module_id1");

        teivDbOperations.merge(dslContext, entityTypeAntennModLongName, antennaModule1);
        teivDbOperations.merge(dslContext, entityTypeAntennModLongName, antennaModule2);
        teivDbOperations.merge(dslContext, relTypeAntennModRealisedByAntennaModLongName, rel1);
        teivDbOperations.merge(dslContext, relTypeAntennModRealisedByAntennaModLongName, rel2);

        Result<Record> row1 = TeivDbServiceContainerizedTest.selectAllRowsFromTable(dslContext,
                "teiv_data.\"53017288F3FE983848689A3DD21D48D298CCD23E\"");
        assertEquals(2, row1.size());
        Result<Record> row2 = TeivDbServiceContainerizedTest.selectAllRowsFromTable(dslContext,
                "teiv_data.\"53089669D370B15C78B7E8376D434921D1C94240\"");
        assertEquals(2, row2.size());

        RelationType antennaRelType1 = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-equipment",
                "ANTENNAMODULEEEEEEEEEEEE_REALISED_BY_ANTENNAMODULEEEEEEEEEEEEEEE");

        // Test deletion of a relationship by ID (expected success)
        Optional<OperationResult> deleteResultMatch = teivDbOperations.deleteManyToManyRelationByRelationId(dslContext,
                antennaRelType1, "rel_id1");

        assertTrue(deleteResultMatch.isPresent(), "Delete operation should return a present Optional");
        assertEquals(OperationResult.builder().id("rel_id1").type(
                "ANTENNAMODULEEEEEEEEEEEE_REALISED_BY_ANTENNAMODULEEEEEEEEEEEEEEE").category(RELATIONSHIP_CATEGORY).build(),
                deleteResultMatch.get(), "Deleted relationship ID should match 'rel_id1'");

        // Test deletion of the same relationship ID again (expected failure)
        Optional<OperationResult> deleteResultNoMatch = teivDbOperations.deleteManyToManyRelationByRelationId(dslContext,
                antennaRelType1, "rel_id1");
        assertTrue(deleteResultNoMatch.isEmpty(), "Delete operation should not return a present Optional");

    }

    @Test
    void testDeleteRelConnectingSameEntityByEntityId() throws SchemaRegistryException {
        Map<String, Object> antennaModule1 = new HashMap<>();
        antennaModule1.put("id", "module_id1");
        antennaModule1.put("mechanicalAntennaTilt", 400);
        antennaModule1.put("antennaModelNumber", "['123-abc']");
        antennaModule1.put("totalTilt", 10);
        antennaModule1.put("mechanicalAntennaBearing", 123);
        antennaModule1.put("positionWithinSector", "['123', '456', '789']");
        antennaModule1.put("electricalAntennaTilt", 1);

        Map<String, Object> antennaModule2 = new HashMap<>();
        antennaModule2.put("id", "module_id2");
        antennaModule2.put("mechanicalAntennaTilt", 401);
        antennaModule2.put("antennaModelNumber", "['456-abc']");
        antennaModule2.put("totalTilt", 11);
        antennaModule2.put("mechanicalAntennaBearing", 456);
        antennaModule2.put("positionWithinSector", "['123', '456', '789']");
        antennaModule2.put("electricalAntennaTilt", 2);

        Map<String, Object> rel1 = new HashMap<>();
        rel1.put("id", "rel_id1");
        rel1.put("aSide_2A2D3374BF907674FA1905478E30ACB8882DC03C", "module_id1");
        rel1.put("bSide_EE6DD4A2CFD743779BBCBFC18FC296EF6D72EB1E", "module_id2");

        Map<String, Object> rel2 = new HashMap<>();
        rel2.put("id", "rel_id2");
        rel2.put("aSide_2A2D3374BF907674FA1905478E30ACB8882DC03C", "module_id2");
        rel2.put("bSide_EE6DD4A2CFD743779BBCBFC18FC296EF6D72EB1E", "module_id1");

        teivDbOperations.merge(dslContext, entityTypeAntennModLongName, antennaModule1);
        teivDbOperations.merge(dslContext, entityTypeAntennModLongName, antennaModule2);
        teivDbOperations.merge(dslContext, relTypeAntennModRealisedByAntennaModLongName, rel1);
        teivDbOperations.merge(dslContext, relTypeAntennModRealisedByAntennaModLongName, rel2);

        assertEquals(2, TeivDbServiceContainerizedTest.selectAllRowsFromTable(dslContext,
                "teiv_data.\"53017288F3FE983848689A3DD21D48D298CCD23E\"").size(), "Expected two AntennaModule records");
        assertEquals(2, TeivDbServiceContainerizedTest.selectAllRowsFromTable(dslContext,
                "teiv_data.\"53089669D370B15C78B7E8376D434921D1C94240\"").size(),
                "Expected two ANTENNAMODULE_REALISED_BY_ANTENNAMODULE relations");

        RelationType relType = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-equipment",
                "ANTENNAMODULEEEEEEEEEEEE_REALISED_BY_ANTENNAMODULEEEEEEEEEEEEEEE");

        // Test deletion of relations by entity ID (expected to delete two relations)
        List<OperationResult> deleteResultMatch = teivDbOperations.deleteManyToManyRelationByEntityId(dslContext, relType,
                "module_id1", "aSide_2A2D3374BF907674FA1905478E30ACB8882DC03C",
                "bSide_EE6DD4A2CFD743779BBCBFC18FC296EF6D72EB1E");
        assertEquals(2, deleteResultMatch.size(), "Expected two relations to be deleted");
        assertTrue(deleteResultMatch.contains(OperationResult.builder().id("rel_id1").type(
                "ANTENNAMODULEEEEEEEEEEEE_REALISED_BY_ANTENNAMODULEEEEEEEEEEEEEEE").category(RELATIONSHIP_CATEGORY)
                .build()), "The list should contain the delete operation result with id: 'rel_id1'");

        assertTrue(deleteResultMatch.contains(OperationResult.builder().id("rel_id2").type(
                "ANTENNAMODULEEEEEEEEEEEE_REALISED_BY_ANTENNAMODULEEEEEEEEEEEEEEE").category(RELATIONSHIP_CATEGORY)
                .build()), "The list should contain the delete operation result with id: 'rel_id2'");

        // Test deletion of relations by the same entity ID again (expected to find no
        // relations to delete)
        List<OperationResult> deleteResultNoMatch = teivDbOperations.deleteManyToManyRelationByEntityId(dslContext, relType,
                "module_id1", "aSide_2A2D3374BF907674FA1905478E30ACB8882DC03C",
                "bSide_EE6DD4A2CFD743779BBCBFC18FC296EF6D72EB1E");
        assertTrue(deleteResultNoMatch.isEmpty(),
                "Delete operation should return an empty list for already deleted/non existing ID");

    }

    @Test
    void testMergeEntityRelationshipWithLongNames() throws InvalidFieldInYangDataException {
        CloudEvent cloudEvent = CloudEventTestUtil.getCloudEventFromJsonFile(
                "src/test/resources/cloudeventdata/end-to-end/ce-merge-long-names.json");

        // Merge entities and relationship
        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
        List<OperationResult> mergeResult = teivDbOperations.executeEntityAndRelationshipMergeOperations(
                parsedCloudEventData, "dmi-plugin:nm-1");
        assertEquals(25, mergeResult.size());
    }

    @Test
    void testDeleteASideEntityWithLongNames() throws InvalidFieldInYangDataException, SchemaRegistryException {
        CloudEvent cloudEvent = CloudEventTestUtil.getCloudEventFromJsonFile(
                "src/test/resources/cloudeventdata/end-to-end/ce-merge-long-names.json");

        // Merge topology data
        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
        List<OperationResult> mergeResult = teivDbOperations.executeEntityAndRelationshipMergeOperations(
                parsedCloudEventData, "dmi-plugin:nm-1");
        assertEquals(25, mergeResult.size());

        // Entity with One_To_One relationship
        List<OperationResult> deleteEntityResult1 = teivDbOperations.deleteEntity(dslContext, SchemaRegistry
                .getEntityTypeByModuleAndName("o-ran-smo-teiv-oam",
                        "ManagedElementtttttttttttttttttttttttttttttttttttttttttttttttttt"), "ManagedElement_3");
        assertEquals(2, deleteEntityResult1.size());

        // Entity with One_To_Many relationship
        List<OperationResult> deleteEntityResult2 = teivDbOperations.deleteEntity(dslContext, SchemaRegistry
                .getEntityTypeByModuleAndName("o-ran-smo-teiv-ran",
                        "ODUFunctionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn"), "ODUFunction_1");
        assertEquals(2, deleteEntityResult2.size());

        // Entity and Many_To_One relationship
        List<OperationResult> deleteEntityResult3 = teivDbOperations.deleteEntity(dslContext, SchemaRegistry
                .getEntityTypeByModuleAndName("o-ran-smo-teiv-ran",
                        "LTESectorCarrierrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr"), "LTESectorCarrier_id1");
        assertEquals(2, deleteEntityResult3.size());

        // Entity with Many_To_Many relationship
        List<OperationResult> deleteEntityResult4 = teivDbOperations.deleteEntity(dslContext, SchemaRegistry
                .getEntityTypeByModuleAndName("o-ran-smo-teiv-equipment",
                        "AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"), "AntennaModule_7");
        assertEquals(2, deleteEntityResult4.size());

        // Entity with One_To_Many relationship ConnectingSameEntity
        List<OperationResult> deleteEntityResult5 = teivDbOperations.deleteEntity(dslContext, SchemaRegistry
                .getEntityTypeByModuleAndName("o-ran-smo-teiv-equipment",
                        "AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"), "AntennaModule_5");
        assertEquals(2, deleteEntityResult5.size());

        // Entity with One_To_One relationship ConnectingSameEntity
        List<OperationResult> deleteEntityResult6 = teivDbOperations.deleteEntity(dslContext, SchemaRegistry
                .getEntityTypeByModuleAndName("o-ran-smo-teiv-equipment",
                        "AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"), "AntennaModule_1");
        assertEquals(2, deleteEntityResult6.size());
    }

    @Test
    void testDeleteBSideEntityWithLongNames() throws InvalidFieldInYangDataException, SchemaRegistryException {
        CloudEvent cloudEvent = CloudEventTestUtil.getCloudEventFromJsonFile(
                "src/test/resources/cloudeventdata/end-to-end/ce-merge-long-names.json");

        // Merge topology data
        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
        List<OperationResult> mergeResult = teivDbOperations.executeEntityAndRelationshipMergeOperations(
                parsedCloudEventData, "dmi-plugin:nm-1");
        assertEquals(25, mergeResult.size());

        // Entity with One_To_One relationship
        List<OperationResult> deleteEntityResult1 = teivDbOperations.deleteEntity(dslContext, SchemaRegistry
                .getEntityTypeByModuleAndName("o-ran-smo-teiv-ran",
                        "NRCellDUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU"), "NRCellDU_2");
        assertEquals(2, deleteEntityResult1.size());

        // Entity with One_To_Many relationship
        List<OperationResult> deleteEntityResult2 = teivDbOperations.deleteEntity(dslContext, SchemaRegistry
                .getEntityTypeByModuleAndName("o-ran-smo-teiv-ran",
                        "NRCellDUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU"), "NRCellDU_1");
        assertEquals(2, deleteEntityResult2.size());

        // Entity with Many_To_One relationship
        List<OperationResult> deleteEntityResult3 = teivDbOperations.deleteEntity(dslContext, SchemaRegistry
                .getEntityTypeByModuleAndName("o-ran-smo-teiv-ran",
                        "AntennaCapabilityyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy"), "AntennaCapability_id2");
        assertEquals(2, deleteEntityResult3.size());

        // Entity with Many_To_Many relationship
        List<OperationResult> deleteEntityResult4 = teivDbOperations.deleteEntity(dslContext, SchemaRegistry
                .getEntityTypeByModuleAndName("o-ran-smo-teiv-ran",
                        "AntennaCapabilityyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy"), "AntennaCapability_id1");
        assertEquals(2, deleteEntityResult4.size());

        // Entity with One_To_Many relationship ConnectingSameEntity
        List<OperationResult> deleteEntityResult5 = teivDbOperations.deleteEntity(dslContext, SchemaRegistry
                .getEntityTypeByModuleAndName("o-ran-smo-teiv-equipment",
                        "AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"), "AntennaModule_6");
        assertEquals(2, deleteEntityResult5.size());

        // Entity with One_To_One relationship ConnectingSameEntity
        List<OperationResult> deleteEntityResult6 = teivDbOperations.deleteEntity(dslContext, SchemaRegistry
                .getEntityTypeByModuleAndName("o-ran-smo-teiv-equipment",
                        "AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"), "AntennaModule_2");
        assertEquals(2, deleteEntityResult6.size());

        // Again delete AntennaCapability(id=AntennaCapability_id1) should
        // return empty result list
        List<OperationResult> deleteEntityResult7 = teivDbOperations.deleteEntity(dslContext, SchemaRegistry
                .getEntityTypeByModuleAndName("o-ran-smo-teiv-ran",
                        "AntennaCapabilityyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy"), "AntennaCapability_id1");
        assertTrue(deleteEntityResult7.isEmpty(),
                "Delete operation should return an empty list for already deleted/non existing ID");
    }

    @Test
    void testDeleteRelationshipWithLongNames() throws InvalidFieldInYangDataException, SchemaRegistryException {
        CloudEvent cloudEvent = CloudEventTestUtil.getCloudEventFromJsonFile(
                "src/test/resources/cloudeventdata/end-to-end/ce-merge-long-names.json");

        // Merge topology data
        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
        List<OperationResult> mergeResult = teivDbOperations.executeEntityAndRelationshipMergeOperations(
                parsedCloudEventData, "dmi-plugin:nm-1");
        assertEquals(25, mergeResult.size());

        // One_To_One Relationship
        Relationship oneToOneRelationship = new Relationship("o-ran-smo-teiv-rel-oam-ran",
                "ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU", "ManagedElement_USES_NRCELLDU_relation_1",
                "ManagedElement_3", "NRCellDU_2", List.of());
        RelationType oneToOneRelationType = SchemaRegistry.getRelationTypeByModuleAndName(oneToOneRelationship.getModule(),
                oneToOneRelationship.getType());
        Optional<OperationResult> deleteOneToOneRelationshipResult = teivDbOperations
                .deleteRelationFromEntityTableByRelationId(dslContext, oneToOneRelationship.getId(), oneToOneRelationType);
        assertTrue(deleteOneToOneRelationshipResult.isPresent(), "Delete operation should return a present Optional");

        // One_To_Many Relationship
        Relationship oneToManyRelationship = new Relationship("o-ran-smo-teiv-rel-oam-ran",
                "MANAGEDELEMENTTTTTTTTTTTTTTT_MANAGES_ODUFUNCTIONNNNNNNNNNNNNNN",
                "MANAGEDELEMENT_MANAGES_ODUFUNCTION_relation_1", "ManagedElement_1", "ODUFunction_1", List.of());
        RelationType oneToManyRelationType = SchemaRegistry.getRelationTypeByModuleAndName(oneToManyRelationship
                .getModule(), oneToManyRelationship.getType());
        Optional<OperationResult> deleteOneToManyRelationshipResult = teivDbOperations
                .deleteRelationFromEntityTableByRelationId(dslContext, oneToManyRelationship.getId(),
                        oneToManyRelationType);
        assertTrue(deleteOneToManyRelationshipResult.isPresent(), "Delete operation should return a present Optional");

        // Many_To_One Relationship
        Relationship manyToOneRelationship = new Relationship("o-ran-smo-teiv-ran",
                "LTESECTORCARRIERRRRRRRRRRRRRRRRRRRRR_USES_ANTENNACAPABILITYYYYYYYYYYYYYYY",
                "LTESECTORCARRIER_USES_ANTENNACAPABILITY_relation_1", "LTESectorCarrier_id1", "AntennaCapability_id2", List
                        .of());
        RelationType manyToOneRelationType = SchemaRegistry.getRelationTypeByModuleAndName(manyToOneRelationship
                .getModule(), manyToOneRelationship.getType());
        Optional<OperationResult> deleteManyToOneRelationshipResult = teivDbOperations
                .deleteRelationFromEntityTableByRelationId(dslContext, manyToOneRelationship.getId(),
                        manyToOneRelationType);
        assertTrue(deleteManyToOneRelationshipResult.isPresent(), "Delete operation should return a present Optional");

        // Many_To_Many Relationship
        Relationship manyToManyRelationship = new Relationship("o-ran-smo-teiv-rel-equipment-ran",
                "ANTENNAMODULEEEEEEEEEEEEEEEEEEEE_SERVES_ANTENNACAPABILITYYYYYYYYYYYYYYYYYY",
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY_relation_1", "AntennaModule_7", "AntennaCapability_id1", List.of());
        RelationType manyToManyRelationType = SchemaRegistry.getRelationTypeByModuleAndName(manyToManyRelationship
                .getModule(), manyToManyRelationship.getType());
        Optional<OperationResult> deleteManyToManyRelationshipResult = teivDbOperations
                .deleteManyToManyRelationByRelationId(dslContext, manyToManyRelationType, manyToManyRelationship.getId());
        assertTrue(deleteManyToManyRelationshipResult.isPresent(), "Delete operation should return a present Optional");

        // One_To_One Relationship ConnectingSameEntity
        Relationship connectingSameEntityOneToOneRelationship = new Relationship("o-ran-smo-teiv-equipment",
                "ANTENNAMODULEEEEEEEEEEEE_REALISED_BY_ANTENNAMODULEEEEEEEEEEEEEEE",
                "ANTENNAMODULE_REALISED_BY_ANTENNAMODULE_relation_1", "AntennaModule_1", "AntennaModule_2", List.of());
        RelationType connectingSameEntityType = SchemaRegistry.getRelationTypeByModuleAndName(
                connectingSameEntityOneToOneRelationship.getModule(), connectingSameEntityOneToOneRelationship.getType());
        Optional<OperationResult> deleteConnectingSameEntityOneToOneRelationshipResult = teivDbOperations
                .deleteManyToManyRelationByRelationId(dslContext, connectingSameEntityType,
                        connectingSameEntityOneToOneRelationship.getId());
        assertTrue(deleteConnectingSameEntityOneToOneRelationshipResult.isPresent(),
                "Delete operation should return a present Optional");

        // One_To_Many Relationship ConnectingSameEntity
        Relationship connectingSameEntityOneToManyRelationship = new Relationship("o-ran-smo-teiv-equipment",
                "ANTENNAMODULEEEEEEEEEEEE_DEPLOYED_ON_ANTENNAMODULEEEEEEEEEEEEEEE",
                "ANTENNAMODULE_DEPLOYED_ON_ANTENNAMODULE_relation_1", "AntennaModule_5", "AntennaModule_6", List.of());
        connectingSameEntityType = SchemaRegistry.getRelationTypeByModuleAndName(connectingSameEntityOneToManyRelationship
                .getModule(), connectingSameEntityOneToManyRelationship.getType());
        Optional<OperationResult> deleteConnectingSameEntityOneToManyRelationshipResult = teivDbOperations
                .deleteManyToManyRelationByRelationId(dslContext, connectingSameEntityType,
                        connectingSameEntityOneToManyRelationship.getId());
        assertTrue(deleteConnectingSameEntityOneToManyRelationshipResult.isPresent(),
                "Delete operation should return a present Optional");
    }

    @Test
    void testSelectByCmHandleFormSourceIds() throws SchemaRegistryException {
        Map<String, Object> antennaModule1 = new HashMap<>();
        antennaModule1.put("id", "module_id1");
        antennaModule1.put("CD_sourceIds", JSONB.jsonb(
                "[\"urn:3gpp:dn:fdn\"," + "\"urn:cmHandle:395221E080CCF0FD1924103B15873814\"]"));

        Map<String, Object> antennaModule2 = new HashMap<>();
        antennaModule2.put("id", "module_id2");
        antennaModule2.put("CD_sourceIds", JSONB.jsonb(
                "[\"urn:3gpp:dn:fdn\"," + "\"urn:cmHandle:395221E080CCF0FD1924103B15873815\"]"));

        teivDbOperations.merge(dslContext, entityTypeAntennMod, antennaModule1);
        teivDbOperations.merge(dslContext, entityTypeAntennMod, antennaModule2);

        List<String> ids = teivDbOperations.selectByCmHandleFormSourceIds(dslContext,
                "teiv_data.\"o-ran-smo-teiv-equipment_AntennaModule\"", "395221E080CCF0FD1924103B15873814");
        assertEquals(List.of("module_id1"), ids);
    }

    @Test
    void testMergeManyToManyRelationshipWithExistingId_SidesNotUpdatable() throws InvalidFieldInYangDataException {
        CloudEvent cloudEvent = CloudEventTestUtil.getCloudEventFromJsonFile(
                "src/test/resources/cloudeventdata/end-to-end/ce-create-many-to-many.json");
        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
        List<OperationResult> mergeResult = teivDbOperations.executeEntityAndRelationshipMergeOperations(
                parsedCloudEventData, "dmi-plugin:nm-1");
        assertEquals(6, mergeResult.size());

        Relationship manyToManyRelationship = new Relationship("o-ran-smo-teiv-rel-equipment-ran",
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY", "relation_2", "AntennaModule_1", "AntennaCapability_1",
                new ArrayList<>());
        final ParsedCloudEventData finalParsedCloudEventData = new ParsedCloudEventData(new ArrayList<>(), List.of(
                manyToManyRelationship));
        assertThrows(TeivException.class, () -> teivDbOperations.executeEntityAndRelationshipMergeOperations(
                finalParsedCloudEventData, "dmi-plugin:nm-1"));
    }

    @Test
    void testMergeManyToManyRelationshipWithExistingId_SidesSameAsUpdatables() throws InvalidFieldInYangDataException {
        CloudEvent cloudEvent = CloudEventTestUtil.getCloudEventFromJsonFile(
                "src/test/resources/cloudeventdata/end-to-end/ce-create-many-to-many.json");
        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
        List<OperationResult> mergeResult = teivDbOperations.executeEntityAndRelationshipMergeOperations(
                parsedCloudEventData, "dmi-plugin:nm-1");
        assertEquals(6, mergeResult.size());

        Relationship manyToManyRelationship = new Relationship("o-ran-smo-teiv-rel-equipment-ran",
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY", "relation_2", "AntennaModule_2", "AntennaCapability_2",
                new ArrayList<>());
        final ParsedCloudEventData finalParsedCloudEventData = new ParsedCloudEventData(new ArrayList<>(), List.of(
                manyToManyRelationship));
        List<OperationResult> result = teivDbOperations.executeEntityAndRelationshipMergeOperations(
                finalParsedCloudEventData, "dmi-plugin:nm-1");

        assertEquals(1, result.size());
    }

    @Test
    void testMergeManyToManyWithNonExistingEntities() throws InvalidFieldInYangDataException {
        CloudEvent cloudEvent = CloudEventTestUtil.getCloudEventFromJsonFile(
                "src/test/resources/cloudeventdata/end-to-end/ce-create-many-to-many.json");
        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
        List<OperationResult> mergeResult = teivDbOperations.executeEntityAndRelationshipMergeOperations(
                parsedCloudEventData, "dmi-plugin:nm-1");
        assertEquals(6, mergeResult.size());

        Relationship manyToManyRelationship = new Relationship("o-ran-smo-teiv-rel-equipment-ran",
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY", "relation_5", "AntennaModule_5", "AntennaCapability_5",
                new ArrayList<>());
        parsedCloudEventData = new ParsedCloudEventData(new ArrayList<>(), List.of(manyToManyRelationship));
        List<OperationResult> result = teivDbOperations.executeEntityAndRelationshipMergeOperations(parsedCloudEventData,
                "dmi-plugin:nm-1");

        assertEquals(3, result.size());
    }

    @Test
    void testMergeManyToManyWithOneExistingEntity() throws InvalidFieldInYangDataException {
        CloudEvent cloudEvent = CloudEventTestUtil.getCloudEventFromJsonFile(
                "src/test/resources/cloudeventdata/end-to-end/ce-create-many-to-many.json");
        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
        List<OperationResult> mergeResult = teivDbOperations.executeEntityAndRelationshipMergeOperations(
                parsedCloudEventData, "dmi-plugin:nm-1");
        assertEquals(6, mergeResult.size());

        Relationship manyToManyRelationship = new Relationship("o-ran-smo-teiv-rel-equipment-ran",
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY", "relation_5", "AntennaModule_2", "AntennaCapability_5",
                new ArrayList<>());
        parsedCloudEventData = new ParsedCloudEventData(new ArrayList<>(), List.of(manyToManyRelationship));
        List<OperationResult> result = teivDbOperations.executeEntityAndRelationshipMergeOperations(parsedCloudEventData,
                "dmi-plugin:nm-1");

        assertEquals(2, result.size());

    }

    @Test // Both endpoints exist, and a new relationship ID is received.
    void testMergeWithNewRelationshipId() throws MaximumCardinalityViolationException, InvalidFieldInYangDataException {
        List<OperationResult> manyToOneResult = mergeSingleTestEvent(
                VALIDATE_MANY_TO_ONE_DIR + "ce-create-many-to-one.json");
        List<OperationResult> oneToManyResult = mergeSingleTestEvent(
                VALIDATE_ONE_TO_MANY_DIR + "ce-create-one-to-many.json");
        List<OperationResult> oneToOneResult = mergeSingleTestEvent(VALIDATE_ONE_TO_ONE_DIR + "ce-create-one-to-one.json");

        assertEquals(3, manyToOneResult.size());
        assertEquals(3, oneToManyResult.size());
        assertEquals(3, oneToOneResult.size());

        assertDbContainsOperationResults(manyToOneResult);
        assertDbContainsOperationResults(oneToManyResult);
        assertDbContainsOperationResults(oneToOneResult);

    }

    @Test
    void testMergeWithAttributeNull() throws MaximumCardinalityViolationException, InvalidFieldInYangDataException {
        List<OperationResult> manyToOneCreateResult = mergeSingleTestEvent(
                VALIDATE_MANY_TO_ONE_DIR + "ce-create-many-to-one9.json");

        assertEquals("ODU_1", manyToOneCreateResult.get(0).getId());
        assertEquals("ODU_1", manyToOneCreateResult.get(0).getId());
        assertEquals("ODUFunction", manyToOneCreateResult.get(0).getType());
        Map<String, Object> ORUAttributes = manyToOneCreateResult.get(0).getAttributes();
        assertTrue(ORUAttributes.containsKey("dUpLMNId"));
        assertEquals(JSONB.jsonb("{\"mcc\":\"209\",\"mnc\":\"751\"}"), ORUAttributes.get("dUpLMNId"));

        assertEquals("NRSectorCarrier_1", manyToOneCreateResult.get(1).getId());
        assertEquals("NRSectorCarrier", manyToOneCreateResult.get(1).getType());
        Map<String, Object> lteSectorAttributes = manyToOneCreateResult.get(1).getAttributes();
        assertTrue(lteSectorAttributes.containsKey("arfcnDL"));
        assertEquals(64L, lteSectorAttributes.get("arfcnDL"));

        assertEquals("Relation_ManyToOne_1", manyToOneCreateResult.get(2).getId());
        assertEquals("ODUFUNCTION_PROVIDES_NRSECTORCARRIER", manyToOneCreateResult.get(2).getType());
        assertEquals("ODU_1", manyToOneCreateResult.get(2).getASide());
        assertEquals("NRSectorCarrier_1", manyToOneCreateResult.get(2).getBSide());

        CloudEvent cloudEvent = CloudEventTestUtil.getCloudEventFromJsonFile(
                VALIDATE_MANY_TO_ONE_DIR + "ce-merge-many-to-one-null-attribute.json");

        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
        List<OperationResult> mergeResult = assertDoesNotThrow(() -> teivDbOperations
                .executeEntityAndRelationshipMergeOperations(parsedCloudEventData, "dmi-plugin:nm-1"));

        assertEquals(3, mergeResult.size());
        assertEquals("ODU_1", mergeResult.get(0).getId());
        assertEquals("ODUFunction", mergeResult.get(0).getType());
        ORUAttributes = mergeResult.get(0).getAttributes();
        assertTrue(ORUAttributes.containsKey("dUpLMNId"));
        assertNull(ORUAttributes.get("dUpLMNId"));

        assertEquals("NRSectorCarrier_1", mergeResult.get(1).getId());
        assertEquals("NRSectorCarrier", mergeResult.get(1).getType());
        lteSectorAttributes = mergeResult.get(1).getAttributes();
        assertTrue(lteSectorAttributes.containsKey("arfcnDL"));
        assertNull(lteSectorAttributes.get("arfcnDL"));
    }

    @Test // Existing but free endpoints and an existing relationship ID is received.
    void testMergeWithExistingFreeEndpoints() throws MaximumCardinalityViolationException, InvalidFieldInYangDataException {
        List<OperationResult> manyToOneResult = mergeSingleTestEvent(
                VALIDATE_MANY_TO_ONE_DIR + "ce-create-many-to-one.json");
        List<OperationResult> oneToManyResult = mergeSingleTestEvent(
                VALIDATE_ONE_TO_MANY_DIR + "ce-create-one-to-many.json");
        List<OperationResult> oneToOneResult = mergeSingleTestEvent(VALIDATE_ONE_TO_ONE_DIR + "ce-create-one-to-one.json");

        assertEquals(3, manyToOneResult.size());
        assertEquals(3, oneToManyResult.size());
        assertEquals(3, oneToOneResult.size());

        assertDbContainsOperationResults(manyToOneResult);
        assertDbContainsOperationResults(oneToManyResult);
        assertDbContainsOperationResults(oneToOneResult);

        assertThrows(TeivException.class, () -> mergeSingleTestEvent(
                VALIDATE_MANY_TO_ONE_DIR + "ce-create-many-to-one2.json"));
        assertThrows(TeivException.class, () -> mergeSingleTestEvent(
                VALIDATE_ONE_TO_MANY_DIR + "ce-create-one-to-many2.json"));
        assertThrows(TeivException.class, () -> mergeSingleTestEvent(
                VALIDATE_ONE_TO_ONE_DIR + "ce-create-one-to-one2.json"));
    }

    @Test // Used "many" side endpoint with a new relationship ID.
    void testMergeWithUsedManySideAndNewRelationshipId() throws MaximumCardinalityViolationException,
            InvalidFieldInYangDataException {
        List<OperationResult> manyToOneResult = mergeSingleTestEvent(
                VALIDATE_MANY_TO_ONE_DIR + "ce-create-many-to-one.json");
        List<OperationResult> oneToManyResult = mergeSingleTestEvent(
                VALIDATE_ONE_TO_MANY_DIR + "ce-create-one-to-many.json");
        List<OperationResult> oneToOneResult = mergeSingleTestEvent(VALIDATE_ONE_TO_ONE_DIR + "ce-create-one-to-one.json");

        assertEquals(3, manyToOneResult.size());
        assertEquals(3, oneToManyResult.size());
        assertEquals(3, oneToOneResult.size());

        assertDbContainsOperationResults(manyToOneResult);
        assertDbContainsOperationResults(oneToManyResult);
        assertDbContainsOperationResults(oneToOneResult);

        assertThrows(MaximumCardinalityViolationException.class, () -> mergeSingleTestEvent(
                VALIDATE_MANY_TO_ONE_DIR + "ce-create-many-to-one3.json"));
        assertThrows(MaximumCardinalityViolationException.class, () -> mergeSingleTestEvent(
                VALIDATE_ONE_TO_MANY_DIR + "ce-create-one-to-many3.json"));
        assertThrows(MaximumCardinalityViolationException.class, () -> mergeSingleTestEvent(
                VALIDATE_ONE_TO_ONE_DIR + "ce-create-one-to-one3.json"));
    }

    @Test // Used "many" side endpoint with an existing relationship ID.
    void testMergeWithUsedManySideAndExistingRelationshipId() throws MaximumCardinalityViolationException,
            InvalidFieldInYangDataException {
        List<OperationResult> manyToOneResult = mergeSingleTestEvent(
                VALIDATE_MANY_TO_ONE_DIR + "ce-create-many-to-one.json");
        List<OperationResult> oneToManyResult = mergeSingleTestEvent(
                VALIDATE_ONE_TO_MANY_DIR + "ce-create-one-to-many.json");
        List<OperationResult> oneToOneResult = mergeSingleTestEvent(VALIDATE_ONE_TO_ONE_DIR + "ce-create-one-to-one.json");

        assertEquals(3, manyToOneResult.size());
        assertEquals(3, oneToManyResult.size());
        assertEquals(3, oneToOneResult.size());

        assertDbContainsOperationResults(manyToOneResult);
        assertDbContainsOperationResults(oneToManyResult);
        assertDbContainsOperationResults(oneToOneResult);

        assertThrows(TeivException.class, () -> mergeSingleTestEvent(
                VALIDATE_MANY_TO_ONE_DIR + "ce-create-many-to-one4.json"));
        assertThrows(TeivException.class, () -> mergeSingleTestEvent(
                VALIDATE_ONE_TO_MANY_DIR + "ce-create-one-to-many4.json"));
        assertThrows(TeivException.class, () -> mergeSingleTestEvent(
                VALIDATE_ONE_TO_ONE_DIR + "ce-create-one-to-one4.json"));
    }

    @Test // Missing "one" side endpoint with a new relationship ID.
    void testMergeWithMissingOneSideAndNewRelationshipId() throws MaximumCardinalityViolationException,
            InvalidFieldInYangDataException {
        List<OperationResult> manyToOneResult = mergeSingleTestEvent(
                VALIDATE_MANY_TO_ONE_DIR + "ce-create-many-to-one5.json");
        List<OperationResult> oneToManyResult = mergeSingleTestEvent(
                VALIDATE_ONE_TO_MANY_DIR + "ce-create-one-to-many5.json");
        List<OperationResult> oneToOneResult = mergeSingleTestEvent(VALIDATE_ONE_TO_ONE_DIR + "ce-create-one-to-one5.json");

        assertEquals(3, manyToOneResult.size());
        assertEquals(3, oneToManyResult.size());
        assertEquals(3, oneToOneResult.size());

        assertDbContainsOperationResults(manyToOneResult);
        assertDbContainsOperationResults(oneToManyResult);
        assertDbContainsOperationResults(oneToOneResult);
    }

    @Test // Missing "many" side endpoint with a new relationship ID.
    void testMergeWithMissingManySideAndNewRelationshipId() throws MaximumCardinalityViolationException,
            InvalidFieldInYangDataException {
        List<OperationResult> manyToOneResult = mergeSingleTestEvent(
                VALIDATE_MANY_TO_ONE_DIR + "ce-create-many-to-one6.json");
        List<OperationResult> oneToManyResult = mergeSingleTestEvent(
                VALIDATE_ONE_TO_MANY_DIR + "ce-create-one-to-many6.json");
        List<OperationResult> oneToOneResult = mergeSingleTestEvent(VALIDATE_ONE_TO_ONE_DIR + "ce-create-one-to-one6.json");

        assertEquals(3, manyToOneResult.size());
        assertEquals(3, oneToManyResult.size());
        assertEquals(3, oneToOneResult.size());

        assertDbContainsOperationResults(manyToOneResult);
        assertDbContainsOperationResults(oneToManyResult);
        assertDbContainsOperationResults(oneToOneResult);
    }

    @Test // Missing both "one" and "many" side endpoints with a new relationship ID.
    void testMergeWithMissingEndpointsAndNewRelationshipId() throws MaximumCardinalityViolationException,
            InvalidFieldInYangDataException {
        List<OperationResult> manyToOneResult = mergeSingleTestEvent(
                VALIDATE_MANY_TO_ONE_DIR + "ce-create-many-to-one7.json");
        List<OperationResult> oneToManyResult = mergeSingleTestEvent(
                VALIDATE_ONE_TO_MANY_DIR + "ce-create-one-to-many7.json");
        List<OperationResult> oneToOneResult = mergeSingleTestEvent(VALIDATE_ONE_TO_ONE_DIR + "ce-create-one-to-one7.json");

        assertEquals(3, manyToOneResult.size());
        assertEquals(3, oneToManyResult.size());
        assertEquals(3, oneToOneResult.size());

        assertDbContainsOperationResults(manyToOneResult);
        assertDbContainsOperationResults(oneToManyResult);
        assertDbContainsOperationResults(oneToOneResult);
    }

    @Test // Missing "one" side endpoint with an existing relationship ID.
    void testMergeWithMissingOneSideAndExistingRelationshipId() throws MaximumCardinalityViolationException,
            InvalidFieldInYangDataException {
        List<OperationResult> manyToOneResult = mergeSingleTestEvent(
                VALIDATE_MANY_TO_ONE_DIR + "ce-create-many-to-one.json");
        List<OperationResult> oneToManyResult = mergeSingleTestEvent(
                VALIDATE_ONE_TO_MANY_DIR + "ce-create-one-to-many.json");
        List<OperationResult> oneToOneResult = mergeSingleTestEvent(VALIDATE_ONE_TO_ONE_DIR + "ce-create-one-to-one.json");

        assertEquals(3, manyToOneResult.size());
        assertEquals(3, oneToManyResult.size());
        assertEquals(3, oneToOneResult.size());

        assertDbContainsOperationResults(manyToOneResult);
        assertDbContainsOperationResults(oneToManyResult);
        assertDbContainsOperationResults(oneToOneResult);

        assertThrows(TeivException.class, () -> mergeSingleTestEvent(
                VALIDATE_MANY_TO_ONE_DIR + "ce-create-many-to-one8.json"));
        assertThrows(TeivException.class, () -> mergeSingleTestEvent(
                VALIDATE_ONE_TO_MANY_DIR + "ce-create-one-to-many8.json"));
        assertThrows(TeivException.class, () -> mergeSingleTestEvent(
                VALIDATE_ONE_TO_ONE_DIR + "ce-create-one-to-one8.json"));
    }

    @Test // Missing "many" side endpoint with an existing relationship ID.
    void testMergeWithMissingManySideAndExistingRelationshipIdInCaseOfGeoLocation()
            throws MaximumCardinalityViolationException, InvalidFieldInYangDataException {
        List<OperationResult> manyToOneResult = mergeSingleTestEvent(
                VALIDATE_MANY_TO_ONE_DIR + "ce-create-many-to-one-geolocation.json");

        assertEquals(3, manyToOneResult.size());
        assertDbContainsOperationResults(manyToOneResult);

        assertThrows(MaximumCardinalityViolationException.class, () -> mergeSingleTestEvent(
                VALIDATE_MANY_TO_ONE_DIR + "ce-create-many-to-one-geolocation2.json"));
    }

    @Test
    void testRelationRelatedMethodsWhenRelationshipIsStoredInSeparateTable() throws SchemaRegistryException {

        Relationship manyToManyRelationship = new Relationship("o-ran-smo-teiv-rel-equipment-ran",
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY", "relation_4", "ANTENNAMODULE_1", "ANTENNACAPABILITY_4",
                new ArrayList<>());

        assertNull(manyToManyRelationship.getStoringSideEntityId());
        assertNull(manyToManyRelationship.getNotStoringSideEntityId());

        RelationType manyToManyRelationType = SchemaRegistry.getRelationTypeByModuleAndName(manyToManyRelationship
                .getModule(), manyToManyRelationship.getType());

        assertNull(manyToManyRelationType.getNotStoringSideTableName());
        assertNull(manyToManyRelationType.getNotStoringSideEntityIdColumnNameInStoringSideTable());
        assertNull(manyToManyRelationType.getStoringSideEntityType());
        assertNull(manyToManyRelationType.getNotStoringSideEntityType());

    }

    @Test
    void testRelationRelatedMethodsWhenRelationshipIsStoredOnBSide() throws SchemaRegistryException {

        Relationship relationship = new Relationship("o-ran-smo-teiv-ran", "ODUFUNCTION_PROVIDES_NRCELLDU", "relation_2",
                "ODUFunction_1", "NRCellDU_5", new ArrayList<>());

        assertEquals("NRCellDU_5", relationship.getStoringSideEntityId());
        assertEquals("ODUFunction_1", relationship.getNotStoringSideEntityId());

        RelationType relationType = SchemaRegistry.getRelationTypeByModuleAndName(relationship.getModule(), relationship
                .getType());

        assertEquals("teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\"", relationType.getNotStoringSideTableName());
        assertEquals("REL_FK_provided-by-oduFunction", relationType
                .getNotStoringSideEntityIdColumnNameInStoringSideTable());
        assertEquals("NRCellDU", relationType.getStoringSideEntityType().getName());
        assertEquals("ODUFunction", relationType.getNotStoringSideEntityType().getName());

    }

    // testing exceptions loadModules() in PostgresSchemaLoader.java for Sonarqube code coverage
    @Test
    void testLoadModulesThrowsException() throws JsonProcessingException {
        ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        PostgresSchemaLoader postgresSchemaLoader = new PostgresSchemaLoader(dslContext, objectMapper);

        Mockito.when(objectMapper.readValue(anyString(), eq(List.class))).thenThrow(JsonProcessingException.class);

        assertThrows(SchemaLoaderException.class, postgresSchemaLoader::loadModules);
    }

    // testing exceptions in loadEntityTypes() in PostgresSchemaLoader.java for Sonarqube code coverage
    @Test
    void testLoadEntityTypesThrowsException() throws JsonProcessingException {
        ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        PostgresSchemaLoader postgresSchemaLoader = new PostgresSchemaLoader(dslContext, objectMapper);

        Mockito.when(objectMapper.readValue(anyString(), eq(List.class))).thenThrow(JsonProcessingException.class);

        assertThrows(SchemaLoaderException.class, postgresSchemaLoader::loadEntityTypes);
    }

    private List<OperationResult> mergeSingleTestEvent(String path) throws MaximumCardinalityViolationException,
            InvalidFieldInYangDataException {
        CloudEvent cloudEvent = CloudEventTestUtil.getCloudEventFromJsonFile(path);
        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
        return teivDbOperations.executeEntityAndRelationshipMergeOperations(parsedCloudEventData, "dmi-plugin:nm-1");
    }

    private void assertDbContainsOperationResults(List<OperationResult> results) {
        for (OperationResult result : results) {
            if (result.isRelationship()) {
                RelationType relationType = SchemaRegistry.getRelationTypeByName(result.getType());
                tableContainsId(BidiDbNameMapper.getDbName(relationType.getTableName()), relationType.getIdColumnName(),
                        result);
            } else {
                final EntityType entityType = SchemaRegistry.getEntityTypeByName(result.getType());
                tableContainsId(BidiDbNameMapper.getDbName(entityType.getTableName()), "id", result);
            }
        }
    }

    private void tableContainsId(String tableName, String idColumn, OperationResult result) {
        Result<Record> dbResults = TeivDbServiceContainerizedTest.selectAllRowsFromTable(dslContext, tableName);
        final boolean contains = dbResults.stream().map(row -> row.get(idColumn)).filter(Objects::nonNull).map(
                Object::toString).anyMatch(result.getId()::equals);
        assertTrue(contains);
    }
}
