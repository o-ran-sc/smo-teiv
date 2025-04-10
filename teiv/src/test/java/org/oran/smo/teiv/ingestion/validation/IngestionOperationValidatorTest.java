/*
 *  ============LICENSE_START=======================================================
 *  Copyright (C) 2024 Ericsson
 *  Modifications Copyright (C) 2024-2025 OpenInfra Foundation Europe
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
package org.oran.smo.teiv.ingestion.validation;

import static org.oran.smo.teiv.utils.TeivConstants.TEIV_DATA_SCHEMA;
import static org.jooq.impl.DSL.table;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.oran.smo.teiv.db.TestPostgresqlContainer;
import org.oran.smo.teiv.exception.InvalidFieldInYangDataException;
import org.oran.smo.teiv.ingestion.validation.IngestionOperationValidator.MAXIMUM_CARDINALITY_CASE;
import org.oran.smo.teiv.schema.Association;
import org.oran.smo.teiv.schema.PostgresSchemaLoader;
import org.oran.smo.teiv.schema.RelationType;
import org.oran.smo.teiv.schema.RelationshipDataLocation;
import org.oran.smo.teiv.schema.SchemaLoaderException;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.service.TeivDbOperations;
import org.oran.smo.teiv.service.cloudevent.data.Entity;
import org.oran.smo.teiv.service.cloudevent.data.ParsedCloudEventData;
import org.oran.smo.teiv.service.cloudevent.data.Relationship;
import org.oran.smo.teiv.startup.SchemaHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

@Configuration
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@SpringBootTest
@ActiveProfiles({ "test", "ingestion" })
class IngestionOperationValidatorTest {
    private static TestPostgresqlContainer postgresqlContainer = TestPostgresqlContainer.getInstance();

    @Autowired
    private TeivDbOperations teivDbOperations;

    @SpyBean
    private IngestionOperationValidatorFactory validatorFactory;

    @Autowired
    private DSLContext writeDataDslContext;

    @MockBean
    private SchemaHandler schemaHandler;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.read.jdbc-url", () -> postgresqlContainer.getJdbcUrl());
        registry.add("spring.datasource.read.username", () -> postgresqlContainer.getUsername());
        registry.add("spring.datasource.read.password", () -> postgresqlContainer.getPassword());

        registry.add("spring.datasource.write.jdbc-url", () -> postgresqlContainer.getJdbcUrl());
        registry.add("spring.datasource.write.username", () -> postgresqlContainer.getUsername());
        registry.add("spring.datasource.write.password", () -> postgresqlContainer.getPassword());
    }

    private TeivDbServiceForValidation spiedDbServiceForValidation;

    @BeforeAll
    static void beforeAll() {
        TestPostgresqlContainer.loadIngestionTestData();
        TestPostgresqlContainer.loadValidationTestData();
    }

    @PostConstruct
    public void postConstruct() throws UnsupportedOperationException, SchemaLoaderException {
        PostgresSchemaLoader postgresSchemaLoader = new PostgresSchemaLoader(writeDataDslContext, new ObjectMapper());
        postgresSchemaLoader.loadSchemaRegistry();
        when(validatorFactory.createValidator(any())).thenAnswer(i -> {
            spiedDbServiceForValidation = spy(new TeivDbServiceForValidation((DSLContext) i.getArguments()[0]));
            return new IngestionOperationValidator(spiedDbServiceForValidation);
        });
    }

    @BeforeEach
    public void deleteAll() {
        TestPostgresqlContainer.truncateSchemas(List.of(TEIV_DATA_SCHEMA), writeDataDslContext);
        if (spiedDbServiceForValidation != null) {
            reset(spiedDbServiceForValidation);
        }
    }

    @Test
    void maximumCardinalityViolationOneToOne_aSideMax() throws InvalidFieldInYangDataException {
        //ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU is a 0..1 to 1 relationship
        List<Entity> entities = generateEntities(MAXIMUM_CARDINALITY_CASE.ONE_ONE);
        List<Relationship> relationships = new ArrayList<>();
        relationships.add(new Relationship("o-ran-smo-teiv-rel-oam-ran",
                "ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU", "rel_1", "ManagedElement_1", "NRCellDU_1", List
                        .of()));
        relationships.add(new Relationship("o-ran-smo-teiv-rel-oam-ran",
                "ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU", "rel_2", "ManagedElement_2", "NRCellDU_1", List
                        .of()));
        ParsedCloudEventData parsedCloudEventData = new ParsedCloudEventData(entities, relationships);
        //It's expected to fail, because the NRCellDU_1 entity would be connected to 2 ManagedElement instances
        assertThrows(MaximumCardinalityViolationException.class, () -> teivDbOperations
                .executeEntityAndRelationshipMergeOperations(parsedCloudEventData, "testSource"));

        //The whole transaction is rolled back. Neither the entities nor the relationships are persisted.
        assertEmptyTable("teiv_data.\"28C9A375E800E82308EBE7DA2932EF2C0AF13C38\"");
        assertEmptyTable("teiv_data.\"84E676149362F50C55FE1E004B98D4891916BBF3\"");

        //Remove the extra relationship that caused the cardinality violation. Successfully insert the others.
        Relationship redundantRelationship = relationships.remove(1);
        ParsedCloudEventData parsedCloudEventData2 = new ParsedCloudEventData(entities, relationships);
        assertEquals(entities.size() + relationships.size(), teivDbOperations.executeEntityAndRelationshipMergeOperations(
                parsedCloudEventData2, "testSource").size());
        verify(spiedDbServiceForValidation).acquireEntityInstanceExclusiveLock(
                "teiv_data.\"84E676149362F50C55FE1E004B98D4891916BBF3\"", "NRCellDU_1");

        //Try to insert an extra relationship. It's expected to fail, because the NRCellDU_1 entity already has the maximum number of relationships.
        ParsedCloudEventData parsedCloudEventData3 = new ParsedCloudEventData(List.of(), List.of(redundantRelationship));
        assertThrows(MaximumCardinalityViolationException.class, () -> teivDbOperations
                .executeEntityAndRelationshipMergeOperations(parsedCloudEventData3, "testSource"));
    }

    @Test
    void maximumCardinalityViolationOneToConstRelationship() throws InvalidFieldInYangDataException {
        // TESTENTITYA_USES_TESTENTITYB is a 0..1 to 0..2 relationship
        List<Entity> entities = generateEntities(MAXIMUM_CARDINALITY_CASE.ONE_CONST);
        List<Relationship> relationships = new ArrayList<>();
        relationships.add(new Relationship("o-ran-smo-teiv-ran_", "TESTENTITYA_USES_TESTENTITYB", "rel_1", "TestEntityA_1",
                "TestEntityB_1", List.of()));
        relationships.add(new Relationship("o-ran-smo-teiv-ran", "TESTENTITYA_USES_TESTENTITYB", "rel_2", "TestEntityA_1",
                "TestEntityB_2", List.of()));
        relationships.add(new Relationship("o-ran-smo-teiv-ran", "TESTENTITYA_USES_TESTENTITYB", "rel_3", "TestEntityA_1",
                "TestEntityB_3", List.of()));
        ParsedCloudEventData parsedCloudEventData = new ParsedCloudEventData(entities, relationships);
        //It's expected to fail, because the TestEntityA_1 entity would be connected to 3 TestEntityB instances
        assertThrows(MaximumCardinalityViolationException.class, () -> teivDbOperations
                .executeEntityAndRelationshipMergeOperations(parsedCloudEventData, "testSource"));
        verify(spiedDbServiceForValidation, times(1)).acquireEntityInstanceExclusiveLock(
                "teiv_data.\"o-ran-smo-teiv-ran_TestEntityA\"", "TestEntityA_1");

        //The whole transaction is rolled back. Neither the entities nor the relationships are persisted.
        assertEmptyTable("teiv_data.\"o-ran-smo-teiv-ran_TestEntityA\"");
        assertEmptyTable("teiv_data.\"o-ran-smo-teiv-ran_TestEntityB\"");

        //Remove the extra relationship that caused the cardinality violation. Successfully insert the others.
        Relationship redundantRelationship = relationships.remove(2);
        ParsedCloudEventData parsedCloudEventData2 = new ParsedCloudEventData(entities, relationships);
        assertEquals(entities.size() + relationships.size(), teivDbOperations.executeEntityAndRelationshipMergeOperations(
                parsedCloudEventData2, "testSource").size());
        verify(spiedDbServiceForValidation, times(2)).acquireEntityInstanceExclusiveLock(
                "teiv_data.\"o-ran-smo-teiv-ran_TestEntityA\"", "TestEntityA_1");
        verify(spiedDbServiceForValidation, times(2)).executeValidationQuery(any(), any(), any(), any(Long.class));
        verifyNoMoreInteractions(spiedDbServiceForValidation);

        //Try to insert an extra relationship. It's expected to fail, because the CloudNativeSystem_1 entity already has the maximum number of relationships.
        ParsedCloudEventData parsedCloudEventData3 = new ParsedCloudEventData(List.of(), List.of(redundantRelationship));
        assertThrows(MaximumCardinalityViolationException.class, () -> teivDbOperations
                .executeEntityAndRelationshipMergeOperations(parsedCloudEventData3, "testSource"));
        verify(spiedDbServiceForValidation, times(1)).acquireEntityInstanceExclusiveLock(
                "teiv_data.\"o-ran-smo-teiv-ran_TestEntityA\"", "TestEntityA_1");
    }

    @Test
    void maximumCardinalityViolationConstToConstRelationship() {
        // TESTENTITYA_PROVIDES_TESTENTITYB is a 0..2 to 0..3 relationship
        List<Entity> entities = generateEntities(MAXIMUM_CARDINALITY_CASE.CONST_CONST);
        List<Relationship> relationships = new ArrayList<>();
        relationships.add(new Relationship("o-ran-smo-teiv-ran", "TESTENTITYA_PROVIDES_TESTENTITYB", "rel_1",
                "TestEntityA_1", "TestEntityB_1", List.of()));
        relationships.add(new Relationship("o-ran-smo-teiv-ran", "TESTENTITYA_PROVIDES_TESTENTITYB", "rel_2",
                "TestEntityA_1", "TestEntityB_2", List.of()));
        relationships.add(new Relationship("o-ran-smo-teiv-ran", "TESTENTITYA_PROVIDES_TESTENTITYB", "rel_3",
                "TestEntityA_1", "TestEntityB_3", List.of()));
        relationships.add(new Relationship("o-ran-smo-teiv-ran", "TESTENTITYA_PROVIDES_TESTENTITYB", "rel_4",
                "TestEntityA_1", "TestEntityB_4", List.of()));
        ParsedCloudEventData parsedCloudEventData = new ParsedCloudEventData(entities, relationships);
        //It's expected to fail, because the TestEntityA_1 entity would be connected to 4 TestEntityB instances
        assertThrows(MaximumCardinalityViolationException.class, () -> teivDbOperations
                .executeEntityAndRelationshipMergeOperations(parsedCloudEventData, "testSource"));
        verify(spiedDbServiceForValidation, times(1)).acquireEntityInstanceExclusiveLock(
                "teiv_data.\"o-ran-smo-teiv-ran_TestEntityB\"", "TestEntityB_1");
        verify(spiedDbServiceForValidation, times(1)).acquireEntityInstanceExclusiveLock(
                "teiv_data.\"o-ran-smo-teiv-ran_TestEntityA\"", "TestEntityA_1");

        //The whole transaction is rolled back. Neither the entities nor the relationships are persisted.
        assertEmptyTable("teiv_data.\"o-ran-smo-teiv-ran_TestEntityA\"");
        assertEmptyTable("teiv_data.\"o-ran-smo-teiv-ran_TestEntityB\"");
        assertEmptyTable("teiv_data.\"o-ran-smo-teiv-ran_TESTENTITYA_PROVIDES_TESTENTITYB\"");

        //Test the other side's cardinality as well
        relationships = new ArrayList<>();
        relationships.add(new Relationship("o-ran-smo-teiv-ran", "TESTENTITYA_PROVIDES_TESTENTITYB", "rel_1",
                "TestEntityA_1", "TestEntityB_1", List.of()));
        relationships.add(new Relationship("o-ran-smo-teiv-ran", "TESTENTITYA_PROVIDES_TESTENTITYB", "rel_2",
                "TestEntityA_2", "TestEntityB_1", List.of()));
        relationships.add(new Relationship("o-ran-smo-teiv-ran", "TESTENTITYA_PROVIDES_TESTENTITYB", "rel_3",
                "TestEntityA_3", "TestEntityB_1", List.of()));

        ParsedCloudEventData parsedCloudEventData2 = new ParsedCloudEventData(entities, relationships);
        //It's expected to fail, because the TestEntityB_1 entity would be connected to 3 TestEntityA instances
        assertThrows(MaximumCardinalityViolationException.class, () -> teivDbOperations
                .executeEntityAndRelationshipMergeOperations(parsedCloudEventData2, "testSource"));
        verify(spiedDbServiceForValidation, times(1)).acquireEntityInstanceExclusiveLock(
                "teiv_data.\"o-ran-smo-teiv-ran_TestEntityB\"", "TestEntityB_1");
        assertEmptyTable("teiv_data.\"o-ran-smo-teiv-ran_TestEntityA\"");
        assertEmptyTable("teiv_data.\"o-ran-smo-teiv-ran_TestEntityB\"");
        assertEmptyTable("teiv_data.\"o-ran-smo-teiv-ran_TESTENTITYA_PROVIDES_TESTENTITYB\"");
    }

    @Test
    void maximumCardinalityViolationConstToInfiniteRelationship() throws InvalidFieldInYangDataException {
        // TESTENTITYA_GROUPS_TESTENTITYB is a 0..2 to 0..n relationship
        List<Entity> entities = generateEntities(MAXIMUM_CARDINALITY_CASE.CONST_INFINITE);
        List<Relationship> relationships = new ArrayList<>();
        relationships.add(new Relationship("o-ran-smo-teiv-ran", "TESTENTITYA_GROUPS_TESTENTITYB", "rel_1", "TestEntityA_1",
                "TestEntityB_1", List.of()));
        relationships.add(new Relationship("o-ran-smo-teiv-ran", "TESTENTITYA_GROUPS_TESTENTITYB", "rel_2", "TestEntityA_2",
                "TestEntityB_1", List.of()));
        relationships.add(new Relationship("o-ran-smo-teiv-ran", "TESTENTITYA_GROUPS_TESTENTITYB", "rel_3", "TestEntityA_3",
                "TestEntityB_1", List.of()));
        ParsedCloudEventData parsedCloudEventData = new ParsedCloudEventData(entities, relationships);
        //It's expected to fail, because the TestEntityB_1 entity would be connected to 3 TestEntityB instances
        assertThrows(MaximumCardinalityViolationException.class, () -> teivDbOperations
                .executeEntityAndRelationshipMergeOperations(parsedCloudEventData, "testSource"));
        verify(spiedDbServiceForValidation, times(1)).acquireEntityInstanceExclusiveLock(
                "teiv_data.\"o-ran-smo-teiv-ran_TestEntityB\"", "TestEntityB_1");

        //The whole transaction is rolled back. Neither the entities nor the relationships are persisted.
        assertEmptyTable("teiv_data.\"o-ran-smo-teiv-ran_TestEntityA\"");
        assertEmptyTable("teiv_data.\"o-ran-smo-teiv-ran_TestEntityB\"");
        assertEmptyTable("teiv_data.\"o-ran-smo-teiv-ran_TESTENTITYA_GROUPS_TESTENTITYB\"");

        //Test the other side's cardinality
        entities.add(new Entity("o-ran-smo-teiv-ran", "TestEntityB", "TestEntityB_2", Map.of(), List.of()));
        entities.add(new Entity("o-ran-smo-teiv-ran", "TestEntityB", "TestEntityB_3", Map.of(), List.of()));
        relationships = new ArrayList<>();
        relationships.add(new Relationship("o-ran-smo-teiv-ran", "TESTENTITYA_GROUPS_TESTENTITYB", "rel_1", "TestEntityA_1",
                "TestEntityB_1", List.of()));
        relationships.add(new Relationship("o-ran-smo-teiv-ran", "TESTENTITYA_GROUPS_TESTENTITYB", "rel_2", "TestEntityA_1",
                "TestEntityB_2", List.of()));
        relationships.add(new Relationship("o-ran-smo-teiv-ran", "TESTENTITYA_GROUPS_TESTENTITYB", "rel_3", "TestEntityA_1",
                "TestEntityB_3", List.of()));
        ParsedCloudEventData parsedCloudEventData2 = new ParsedCloudEventData(entities, relationships);
        assertEquals(entities.size() + relationships.size(), teivDbOperations.executeEntityAndRelationshipMergeOperations(
                parsedCloudEventData2, "testSource").size());
        verify(spiedDbServiceForValidation, times(1)).acquireEntityInstanceExclusiveLock(
                "teiv_data.\"o-ran-smo-teiv-ran_TestEntityB\"", "TestEntityB_1");
        verify(spiedDbServiceForValidation, times(1)).acquireEntityInstanceExclusiveLock(
                "teiv_data.\"o-ran-smo-teiv-ran_TestEntityB\"", "TestEntityB_2");
        verify(spiedDbServiceForValidation, times(1)).acquireEntityInstanceExclusiveLock(
                "teiv_data.\"o-ran-smo-teiv-ran_TestEntityB\"", "TestEntityB_3");
        verify(spiedDbServiceForValidation, times(3)).executeValidationQuery(any(), any(), any(), any(Long.class));
        verifyNoMoreInteractions(spiedDbServiceForValidation);
    }

    @ParameterizedTest
    @CsvSource({ "5, 5, A_SIDE", "3, 3, B_SIDE" })
    void unsupportedStorageLocation(int aSideMax, int bSideMax, RelationshipDataLocation location) {
        try (MockedStatic<SchemaRegistry> mockedSchemaRegistry = Mockito.mockStatic(SchemaRegistry.class)) {
            Relationship relationship = new Relationship("", "relation_type", "id", "a", "b", List.of());
            RelationType relationType = RelationType.builder().aSideAssociation(Association.builder().maxCardinality(
                    aSideMax).build()).bSideAssociation(Association.builder().maxCardinality(bSideMax).build())
                    .relationshipStorageLocation(location).build();
            mockedSchemaRegistry.when(() -> SchemaRegistry.getRelationTypeByName("relation_type")).thenReturn(relationType);
            ParsedCloudEventData parsedCloudEventData = new ParsedCloudEventData(List.of(), List.of(relationship));
            assertThrows(UnsupportedOperationException.class, () -> validatorFactory.createValidator(writeDataDslContext)
                    .validate(parsedCloudEventData));
        }
    }

    @Test
    void determineMaxCardinalityCase() {
        assertThrows(IllegalArgumentException.class, () -> IngestionOperationValidator.determineMaxCardinalityCase(0, 0));
        assertThrows(IllegalArgumentException.class, () -> IngestionOperationValidator.determineMaxCardinalityCase(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> IngestionOperationValidator.determineMaxCardinalityCase(0, -1));
        assertEquals(MAXIMUM_CARDINALITY_CASE.ONE_ONE, IngestionOperationValidator.determineMaxCardinalityCase(1, 1));
        assertEquals(MAXIMUM_CARDINALITY_CASE.ONE_CONST, IngestionOperationValidator.determineMaxCardinalityCase(1, 2));
        assertEquals(MAXIMUM_CARDINALITY_CASE.ONE_INFINITE, IngestionOperationValidator.determineMaxCardinalityCase(1,
                Long.MAX_VALUE));
        assertEquals(MAXIMUM_CARDINALITY_CASE.CONST_ONE, IngestionOperationValidator.determineMaxCardinalityCase(5, 1));
        assertEquals(MAXIMUM_CARDINALITY_CASE.CONST_CONST, IngestionOperationValidator.determineMaxCardinalityCase(3, 4));
        assertEquals(MAXIMUM_CARDINALITY_CASE.CONST_INFINITE, IngestionOperationValidator.determineMaxCardinalityCase(4,
                Long.MAX_VALUE));
        assertEquals(MAXIMUM_CARDINALITY_CASE.INFINITE_ONE, IngestionOperationValidator.determineMaxCardinalityCase(
                Long.MAX_VALUE, 1));
        assertEquals(MAXIMUM_CARDINALITY_CASE.INFINITE_CONST, IngestionOperationValidator.determineMaxCardinalityCase(
                Long.MAX_VALUE, 2));
        assertEquals(MAXIMUM_CARDINALITY_CASE.INFINITE_INFINITE, IngestionOperationValidator.determineMaxCardinalityCase(
                Long.MAX_VALUE, Long.MAX_VALUE));
    }

    @Test
    void testMaxCardinalityIsLong() {
        assertFalse(new TeivDbServiceForValidation(null).executeValidationQuery("", "", "", Long.MAX_VALUE));
    }

    void assertEmptyTable(String tableName) {
        Result<Record> rows = writeDataDslContext.selectFrom(table(tableName)).fetch();
        assertEquals(0, rows.size());
    }

    private List<Entity> generateEntities(MAXIMUM_CARDINALITY_CASE cardinalityCase) {
        List<Entity> entities = new ArrayList<>();
        switch (cardinalityCase) {
            case ONE_ONE:
                entities.add(new Entity("o-ran-smo-teiv-rel-oam-ran",
                        "ManagedElementtttttttttttttttttttttttttttttttttttttttttttttttttt", "ManagedElement_1", Map.of(),
                        List.of()));
                entities.add(new Entity("o-ran-smo-teiv-rel-oam-ran",
                        "ManagedElementtttttttttttttttttttttttttttttttttttttttttttttttttt", "ManagedElement_2", Map.of(),
                        List.of()));
                entities.add(new Entity("o-ran-smo-teiv-ran",
                        "NRCellDUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU", "NRCellDU_1", Map.of(), List
                                .of()));
                entities.add(new Entity("o-ran-smo-teiv-ran",
                        "NRCellDUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU", "NRCellDU_2", Map.of(), List
                                .of()));
                break;
            default:
                entities.add(new Entity("o-ran-smo-teiv-ran", "TestEntityA", "TestEntityA_1", Map.of(), List.of()));
                entities.add(new Entity("o-ran-smo-teiv-ran", "TestEntityA", "TestEntityA_2", Map.of(), List.of()));
                entities.add(new Entity("o-ran-smo-teiv-ran", "TestEntityA", "TestEntityA_3", Map.of(), List.of()));
                entities.add(new Entity("o-ran-smo-teiv-ran", "TestEntityB", "TestEntityB_1", Map.of(), List.of()));
                entities.add(new Entity("o-ran-smo-teiv-ran", "TestEntityB", "TestEntityB_2", Map.of(), List.of()));
                entities.add(new Entity("o-ran-smo-teiv-ran", "TestEntityB", "TestEntityB_3", Map.of(), List.of()));
                entities.add(new Entity("o-ran-smo-teiv-ran", "TestEntityB", "TestEntityB_4", Map.of(), List.of()));
                break;
        }
        return entities;
    }

}
