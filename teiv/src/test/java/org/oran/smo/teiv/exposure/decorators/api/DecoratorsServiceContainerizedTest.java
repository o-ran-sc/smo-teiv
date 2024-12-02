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
package org.oran.smo.teiv.exposure.decorators.api;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.oran.smo.teiv.utils.TiesConstants.CONSUMER_DATA_PREFIX;
import static org.oran.smo.teiv.utils.TiesConstants.DECORATORS;
import static org.oran.smo.teiv.utils.TiesConstants.QUOTED_STRING;
import static org.oran.smo.teiv.utils.TiesConstants.REL_PREFIX;
import static org.oran.smo.teiv.utils.TiesConstants.TIES_CONSUMER_DATA_SCHEMA;
import static org.oran.smo.teiv.utils.TiesConstants.TIES_DATA;
import static org.oran.smo.teiv.utils.TiesConstants.TIES_DATA_SCHEMA;
import static org.oran.smo.teiv.utils.TiesTestConstants.KAFKA_RETRY_INTERVAL_10_MS;
import static org.oran.smo.teiv.utils.TiesTestConstants.SPRING_BOOT_SERVER_HOST;
import static org.oran.smo.teiv.utils.TiesTestConstants.SPRING_BOOT_SERVER_PORT;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;
import javax.sql.DataSource;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.oran.smo.teiv.db.TestPostgresqlContainer;
import org.oran.smo.teiv.startup.SchemaCleanUpHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.kafka.test.context.EmbeddedKafka;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

import org.oran.smo.teiv.api.model.OranTeivDecorator;
import org.oran.smo.teiv.exception.TiesException;
import org.oran.smo.teiv.schema.PostgresSchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoaderException;
import org.oran.smo.teiv.startup.SchemaHandler;
import org.oran.smo.teiv.utils.JooqTypeConverter;

@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@EmbeddedKafka
@ActiveProfiles({ "test", "exposure" })
@SpringBootTest(properties = { SPRING_BOOT_SERVER_HOST, SPRING_BOOT_SERVER_PORT, KAFKA_RETRY_INTERVAL_10_MS })
class DecoratorsServiceContainerizedTest {

    public static TestPostgresqlContainer postgreSQLContainer = TestPostgresqlContainer.getInstance();
    private static DSLContext writeDataDslContext;
    private static DSLContext readDataDslContext;

    private KafkaConsumer<String, String> testConsumer;

    private static final String TABLE_NAME = String.format(TIES_DATA, "o-ran-smo-teiv-ran_ODUFunction");

    private static final String ENTITY_ID = "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,ODUFunction=16";
    private static final String ENTITY_TYPE = "ODUFunction";
    private static final String RELATIONSHIP_ID = "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=D67357F682531C7B068486313B0FDAC3E719A166229520196FB9CE917E0236754226A5BCBF7BB7240E516D7ED3FEA852855EC3F121DD4BAFEC5646F2A37F57EE";
    private static final String ENTITY_DECORATORS = String.format(QUOTED_STRING, CONSUMER_DATA_PREFIX + DECORATORS);
    private static final String RELATIONSHIP_TYPE = "MANAGEDELEMENT_MANAGES_ODUFUNCTION";
    private static final String RELATIONSHIP_DECORATORS = String.format(QUOTED_STRING,
            REL_PREFIX + CONSUMER_DATA_PREFIX + DECORATORS + "_MANAGEDELEMENT_MANAGES_ODUFUNCTION");

    @Getter
    @Value("${spring.embedded.kafka.brokers}")
    private String embeddedKafkaServer;

    @Autowired
    private DecoratorsService decoratorsService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SchemaHandler schemaHandler;

    @MockBean
    private SchemaCleanUpHandler schemaCleanUpHandler;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.read.jdbc-url", () -> postgreSQLContainer.getJdbcUrl());
        registry.add("spring.datasource.read.username", () -> postgreSQLContainer.getUsername());
        registry.add("spring.datasource.read.password", () -> postgreSQLContainer.getPassword());

        registry.add("spring.datasource.write.jdbc-url", () -> postgreSQLContainer.getJdbcUrl());
        registry.add("spring.datasource.write.username", () -> postgreSQLContainer.getUsername());
        registry.add("spring.datasource.write.password", () -> postgreSQLContainer.getPassword());
    }

    @BeforeAll
    static void setupAll() throws SchemaLoaderException {
        String url = postgreSQLContainer.getJdbcUrl();
        DataSource ds = DataSourceBuilder.create().url(url).username("test").password("test").build();
        DSLContext dslContext = DSL.using(ds, SQLDialect.POSTGRES);
        PostgresSchemaLoader postgresSchemaLoader = new PostgresSchemaLoader(dslContext, new ObjectMapper());
        writeDataDslContext = DSL.using(ds, SQLDialect.POSTGRES);
        readDataDslContext = DSL.using(ds, SQLDialect.POSTGRES);
        postgresSchemaLoader.loadSchemaRegistry();
        TestPostgresqlContainer.loadSampleData();
    }

    @BeforeEach
    public void setupEach() {
        TestPostgresqlContainer.truncateSchemas(List.of(TIES_DATA_SCHEMA, TIES_CONSUMER_DATA_SCHEMA), writeDataDslContext);
        TestPostgresqlContainer.loadSampleData();
        Supplier<String> brokers = this::getEmbeddedKafkaServer;
        testConsumer = createConsumerForTest(getEmbeddedKafkaServer());
    }

    @AfterEach
    public void cleanupEach() {
        testConsumer.close();
    }

    @Test
    void testAdd_emptyEntityDecorators() {
        Map<String, Object> decoratorsToMerge = Collections.emptyMap();
        Map<String, Object> decoratorsExpected = Map.of("test-app-module:textdata", "Stockholm", "test-app-module:intdata",
                123);

        decoratorsService.update(OranTeivDecorator.builder().decorators(decoratorsToMerge).entityIds(List.of(ENTITY_ID))
                .relationshipIds(Collections.emptyList()).operation(OranTeivDecorator.OperationEnum.MERGE).build());

        verifyDecorators(ENTITY_DECORATORS, TABLE_NAME, ENTITY_ID, decoratorsExpected);
    }

    @Test
    void testAdd_entityDecorators() {
        Map<String, Object> decoratorsToMerge = Map.of("test-app-module:intdata", 456);
        Map<String, Object> decoratorsExpected = Map.of("test-app-module:intdata", 456, "test-app-module:textdata",
                "Stockholm");

        decoratorsService.update(OranTeivDecorator.builder().decorators(decoratorsToMerge).entityIds(List.of(ENTITY_ID))
                .relationshipIds(Collections.emptyList()).operation(OranTeivDecorator.OperationEnum.MERGE).build());

        verifyDecorators(ENTITY_DECORATORS, TABLE_NAME, ENTITY_ID, decoratorsExpected);
    }

    @Test
    void testAdd_relationshipDecorators() {
        Map<String, Object> decoratorsToMerge = Map.of("test-app-module:intdata", 456);
        Map<String, Object> decoratorsExpected = Map.of("test-app-module:intdata", 456, "test-app-module:textdata",
                "Stockholm");

        decoratorsService.update(OranTeivDecorator.builder().decorators(decoratorsToMerge).entityIds(Collections
                .emptyList()).relationshipIds(List.of(RELATIONSHIP_ID)).operation(OranTeivDecorator.OperationEnum.MERGE)
                .build());

        verifyDecorators(RELATIONSHIP_DECORATORS, TABLE_NAME, ENTITY_ID, decoratorsExpected);
    }

    @Test
    void testAdd_entityAndRelationshipDecorators() {
        Map<String, Object> decoratorsToMerge = Map.of("test-app-module:intdata", 456);
        Map<String, Object> decoratorsExpected = Map.of("test-app-module:intdata", 456, "test-app-module:textdata",
                "Stockholm");

        decoratorsService.update(OranTeivDecorator.builder().decorators(decoratorsToMerge).entityIds(List.of(ENTITY_ID))
                .relationshipIds(List.of(RELATIONSHIP_ID)).operation(OranTeivDecorator.OperationEnum.MERGE).build());

        verifyDecorators(ENTITY_DECORATORS, TABLE_NAME, ENTITY_ID, decoratorsExpected);
        verifyDecorators(RELATIONSHIP_DECORATORS, TABLE_NAME, ENTITY_ID, decoratorsExpected);
    }

    @Test
    void testAdd_entityAndRelationshipInvalidNotAvailableDecorators() {
        Map<String, Object> decoratorsToMerge = Map.of("test-app-module:location_WRONG", "Stockholm",
                "test-app-module:data_WRONG", true);

        assertThatThrownBy(() -> decoratorsService.update(OranTeivDecorator.builder().decorators(decoratorsToMerge)
                .entityIds(List.of(ENTITY_ID)).relationshipIds(List.of(RELATIONSHIP_ID)).operation(
                        OranTeivDecorator.OperationEnum.MERGE).build())).isInstanceOf(TiesException.class);
    }

    @Test
    void testAdd_entityAndRelationshipInvalidNotCompatibleDecorators() {
        Map<String, Object> decoratorsToMerge = Map.of("test-app-module:location", true, "test-app-module:data",
                "Stockholm");

        assertThatThrownBy(() -> decoratorsService.update(OranTeivDecorator.builder().decorators(decoratorsToMerge)
                .entityIds(List.of(ENTITY_ID)).relationshipIds(List.of(RELATIONSHIP_ID)).operation(
                        OranTeivDecorator.OperationEnum.MERGE).build())).isInstanceOf(TiesException.class);
    }

    @Test
    void testUpdate_entityDecorators() {
        Map<String, Object> decoratorsToMerge = Map.of("test-app-module:textdata", "Budapest");
        Map<String, Object> decoratorsExpected = Map.of("test-app-module:textdata", "Budapest", "test-app-module:intdata",
                123);

        decoratorsService.update(OranTeivDecorator.builder().decorators(decoratorsToMerge).entityIds(List.of(ENTITY_ID))
                .relationshipIds(Collections.emptyList()).operation(OranTeivDecorator.OperationEnum.MERGE).build());

        verifyDecorators(ENTITY_DECORATORS, TABLE_NAME, ENTITY_ID, decoratorsExpected);
    }

    @Test
    void testUpdate_relationshipDecorators() {
        Map<String, Object> decoratorsToMerge = Map.of("test-app-module:textdata", "Budapest");
        Map<String, Object> decoratorsExpected = Map.of("test-app-module:textdata", "Budapest", "test-app-module:intdata",
                123);

        decoratorsService.update(OranTeivDecorator.builder().decorators(decoratorsToMerge).entityIds(Collections
                .emptyList()).relationshipIds(List.of(RELATIONSHIP_ID)).operation(OranTeivDecorator.OperationEnum.MERGE)
                .build());

        verifyDecorators(RELATIONSHIP_DECORATORS, TABLE_NAME, ENTITY_ID, decoratorsExpected);
    }

    @Test
    void testUpdate_entityAndRelationshipDecorators() {
        Map<String, Object> decoratorsToMerge = Map.of("test-app-module:textdata", "Budapest");
        Map<String, Object> decoratorsExpected = Map.of("test-app-module:textdata", "Budapest", "test-app-module:intdata",
                123);

        decoratorsService.update(OranTeivDecorator.builder().decorators(decoratorsToMerge).entityIds(List.of(ENTITY_ID))
                .relationshipIds(List.of(RELATIONSHIP_ID)).operation(OranTeivDecorator.OperationEnum.MERGE).build());

        verifyDecorators(ENTITY_DECORATORS, TABLE_NAME, ENTITY_ID, decoratorsExpected);
        verifyDecorators(RELATIONSHIP_DECORATORS, TABLE_NAME, ENTITY_ID, decoratorsExpected);
    }

    @Test
    void testDelete_emptyEntityDecorators() {
        Map<String, Object> decoratorsToDelete = Collections.emptyMap();
        Map<String, Object> decoratorsExpected = Map.of("test-app-module:textdata", "Stockholm", "test-app-module:intdata",
                123);

        decoratorsService.update(OranTeivDecorator.builder().decorators(decoratorsToDelete).entityIds(List.of(ENTITY_ID))
                .relationshipIds(Collections.emptyList()).operation(OranTeivDecorator.OperationEnum.DELETE).build());

        verifyDecorators(ENTITY_DECORATORS, TABLE_NAME, ENTITY_ID, decoratorsExpected);
    }

    @Test
    void testDelete_existingEntityDecorators() {
        Map<String, Object> decoratorsToDelete = Map.of("test-app-module:textdata", "Stockholm");
        Map<String, Object> decoratorsExpected = Map.of("test-app-module:intdata", 123);

        decoratorsService.update(OranTeivDecorator.builder().decorators(decoratorsToDelete).entityIds(List.of(ENTITY_ID))
                .relationshipIds(Collections.emptyList()).operation(OranTeivDecorator.OperationEnum.DELETE).build());

        verifyDecorators(ENTITY_DECORATORS, TABLE_NAME, ENTITY_ID, decoratorsExpected);
    }

    @Test
    void testDelete_existingEntityDecorators_withNotExistingEntityId() {
        Map<String, Object> decoratorsToDelete = Map.of("test-app-module:textdata", "Stockholm");

        assertThatThrownBy(() -> decoratorsService.update(OranTeivDecorator.builder().decorators(decoratorsToDelete)
                .entityIds(List.of("WRONG_ID")).relationshipIds(Collections.emptyList()).operation(
                        OranTeivDecorator.OperationEnum.DELETE).build())).isInstanceOf(TiesException.class);
    }

    @Test
    void testDelete_existingRelationshipDecorators() {
        Map<String, Object> decoratorsToDelete = Map.of("test-app-module:textdata", "Stockholm");
        Map<String, Object> decoratorsExpected = Map.of("test-app-module:intdata", 123);

        decoratorsService.update(OranTeivDecorator.builder().decorators(decoratorsToDelete).entityIds(Collections
                .emptyList()).relationshipIds(List.of(RELATIONSHIP_ID)).operation(OranTeivDecorator.OperationEnum.DELETE)
                .build());

        verifyDecorators(RELATIONSHIP_DECORATORS, TABLE_NAME, ENTITY_ID, decoratorsExpected);
    }

    @Test
    void testDelete_existingRelationshipDecorators_withNotExistingRelationshipId() {
        Map<String, Object> decoratorsToDelete = Map.of("test-app-module:textdata", "Stockholm");

        assertThatThrownBy(() -> decoratorsService.update(OranTeivDecorator.builder().decorators(decoratorsToDelete)
                .entityIds(Collections.emptyList()).relationshipIds(List.of("WRONG_ID")).operation(
                        OranTeivDecorator.OperationEnum.DELETE).build())).isInstanceOf(TiesException.class);
    }

    @Test
    void testDelete_existingEntityAndRelationshipDecorators() {
        Map<String, Object> decoratorsToDelete = Map.of("test-app-module:textdata", "Stockholm");
        Map<String, Object> decoratorsExpected = Map.of("test-app-module:intdata", 123);

        decoratorsService.update(OranTeivDecorator.builder().decorators(decoratorsToDelete).entityIds(List.of(ENTITY_ID))
                .relationshipIds(List.of(RELATIONSHIP_ID)).operation(OranTeivDecorator.OperationEnum.DELETE).build());

        verifyDecorators(ENTITY_DECORATORS, TABLE_NAME, ENTITY_ID, decoratorsExpected);
        verifyDecorators(RELATIONSHIP_DECORATORS, TABLE_NAME, ENTITY_ID, decoratorsExpected);
    }

    @Test
    void testDelete_notExistingEntityAndRelationshipDecorators() {
        Map<String, Object> decoratorsToDelete = Map.of("test-app-module:location_WRONG", "Stockholm");
        Map<String, Object> decoratorsExpected = Map.of("test-app-module:intdata", 123, "test-app-module:textdata",
                "Stockholm");

        decoratorsService.update(OranTeivDecorator.builder().decorators(decoratorsToDelete).entityIds(List.of(ENTITY_ID))
                .relationshipIds(List.of(RELATIONSHIP_ID)).operation(OranTeivDecorator.OperationEnum.DELETE).build());

        verifyDecorators(ENTITY_DECORATORS, TABLE_NAME, ENTITY_ID, decoratorsExpected);
        verifyDecorators(RELATIONSHIP_DECORATORS, TABLE_NAME, ENTITY_ID, decoratorsExpected);
    }

    private void verifyDecorators(String fieldName, String tableName, String id, Map<String, Object> decoratorsExpected) {
        SelectConditionStep<Record1<JSONB>> select = readDataDslContext.select(field(fieldName, JSONB.class)).from(table(
                tableName)).where(field("id").eq(id));

        Result<Record1<JSONB>> result = select.fetch();

        Map<String, Object> decoratorsActual = JooqTypeConverter.jsonbToMap(result.get(0).value1());

        assertEquals(decoratorsExpected, decoratorsActual);
    }

    // TODO: create common utility lib
    private KafkaConsumer<String, String> createConsumerForTest(String server) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put("auto.offset.reset", "earliest");
        return new KafkaConsumer<>(properties);
    }

}
