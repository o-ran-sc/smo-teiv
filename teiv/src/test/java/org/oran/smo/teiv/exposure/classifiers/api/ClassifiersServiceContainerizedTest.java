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
package org.oran.smo.teiv.exposure.classifiers.api;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.oran.smo.teiv.utils.TiesConstants.CLASSIFIERS;
import static org.oran.smo.teiv.utils.TiesConstants.CONSUMER_DATA_PREFIX;
import static org.oran.smo.teiv.utils.TiesConstants.QUOTED_STRING;
import static org.oran.smo.teiv.utils.TiesConstants.REL_PREFIX;
import static org.oran.smo.teiv.utils.TiesConstants.TIES_DATA;
import static org.oran.smo.teiv.utils.TiesConstants.TIES_DATA_SCHEMA;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.apache.kafka.common.serialization.StringDeserializer;

import lombok.Getter;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.oran.smo.teiv.api.model.OranTeivClassifier;
import org.oran.smo.teiv.api.model.OranTeivClassifier.OperationEnum;
import org.oran.smo.teiv.db.TestPostgresqlContainerV1;
import org.oran.smo.teiv.exception.TiesException;
import org.oran.smo.teiv.schema.PostgresSchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoaderException;
import org.oran.smo.teiv.startup.SchemaHandler;
import org.oran.smo.teiv.utils.JooqTypeConverter;

import lombok.extern.slf4j.Slf4j;

@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@EmbeddedKafka
@Slf4j
@SpringBootTest(properties = {
        "kafka.server.bootstrap-server-host:#{environment.getProperty(\"spring.embedded.kafka.brokers\").split(\":\")[0]}",
        "kafka.server.bootstrap-server-port:#{environment.getProperty(\"spring.embedded.kafka.brokers\").split(\":\")[1]}",
        "kafka.availability.retryIntervalMs:10", "data-catalog.use-dc-for-kafka-bootstrap:true",
        "notification.consumer-data.enabled:true" })
@ActiveProfiles({ "test", "exposure" })
class ClassifiersServiceContainerizedTest {

    public static TestPostgresqlContainerV1 postgreSQLContainer = TestPostgresqlContainerV1.getInstance();
    private static DSLContext writeDataDslContext;
    private static DSLContext readDataDslContext;
    @Autowired
    private ClassifiersService classifiersService;

    @Getter
    @Value("${spring.embedded.kafka.brokers}")
    private String embeddedKafkaServer;

    private KafkaConsumer<String, String> testConsumer;

    private static final String ENTITY_TYPE = "ODUFunction";
    private static final String TABLE_NAME = String.format(TIES_DATA, "o-ran-smo-teiv-ran_ODUFunction");
    private static final String ENTITY_ID = "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,ODUFunction=16";
    private static final String RELATIONSHIP_ID = "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=D67357F682531C7B068486313B0FDAC3E719A166229520196FB9CE917E0236754226A5BCBF7BB7240E516D7ED3FEA852855EC3F121DD4BAFEC5646F2A37F57EE";
    private static final String RELATIONSHIP_TYPE = "MANAGEDELEMENT_MANAGES_ODUFUNCTION";
    private static final String ENTITY_CLASSIFIERS = String.format(QUOTED_STRING, CONSUMER_DATA_PREFIX + CLASSIFIERS);
    private static final String RELATIONSHIP_CLASSIFIERS = String.format(QUOTED_STRING,
            REL_PREFIX + CONSUMER_DATA_PREFIX + CLASSIFIERS + "_" + RELATIONSHIP_TYPE);

    @MockBean
    private SchemaHandler schemaHandler;

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
    static void setUpAll() throws SchemaLoaderException {
        String url = postgreSQLContainer.getJdbcUrl();
        DataSource ds = DataSourceBuilder.create().url(url).username("test").password("test").build();
        DSLContext dslContext = DSL.using(ds, SQLDialect.POSTGRES);
        PostgresSchemaLoader postgresSchemaLoader = new PostgresSchemaLoader(dslContext, new ObjectMapper());
        writeDataDslContext = DSL.using(ds, SQLDialect.POSTGRES);
        readDataDslContext = DSL.using(ds, SQLDialect.POSTGRES);
        postgresSchemaLoader.loadSchemaRegistry();
        TestPostgresqlContainerV1.loadSampleData();
    }

    @BeforeEach
    public void setUp() {
        testConsumer = createConsumerForTest(getEmbeddedKafkaServer());
    }

    @BeforeEach
    public void reloadBeforeEach() {
        reloadData();
    }

    @AfterAll
    public static void reloadAfterAll() {
        reloadData();
    }

    @AfterEach
    public void cleanUp() {
        testConsumer.close();
    }

    private static void reloadData() {
        writeDataDslContext.meta().filterSchemas(s -> s.getName().equals(TIES_DATA_SCHEMA)).getTables().forEach(
                t -> writeDataDslContext.truncate(t).cascade().execute());
        TestPostgresqlContainerV1.loadSampleData();
    }

    @Test
    void testAdd_entityClassifiers_emptyList() {
        List<String> classifiersToMerge = Collections.emptyList();
        List<String> classifiersExpected = List.of("test-app-module:Indoor", "test-app-module:Rural",
                "test-app-module:Weekend");

        classifiersService.update(OranTeivClassifier.builder().classifiers(classifiersToMerge).entityIds(List.of(ENTITY_ID))
                .relationshipIds(Collections.emptyList()).operation(OperationEnum.MERGE).build());

        verifyClassifiers(ENTITY_CLASSIFIERS, TABLE_NAME, ENTITY_ID, classifiersExpected);
    }

    @Test
    void testAdd_entityClassifiers() {
        List<String> classifiersToMerge = List.of("test-app-module:Outdoor", "test-app-module:Weekday");
        List<String> classifiersExpected = List.of("test-app-module:Indoor", "test-app-module:Rural",
                "test-app-module:Weekend", "test-app-module:Outdoor", "test-app-module:Weekday");

        classifiersService.update(OranTeivClassifier.builder().classifiers(classifiersToMerge).entityIds(List.of(ENTITY_ID))
                .relationshipIds(Collections.emptyList()).operation(OperationEnum.MERGE).build());

        verifyClassifiers(ENTITY_CLASSIFIERS, TABLE_NAME, ENTITY_ID, classifiersExpected);
    }

    @Test
    void testAdd_relationshipClassifiers() {
        List<String> classifiersToMerge = List.of("test-app-module:Outdoor", "test-app-module:Weekday");
        List<String> classifiersExpected = List.of("test-app-module:Indoor", "test-app-module:Rural",
                "test-app-module:Weekend", "test-app-module:Outdoor", "test-app-module:Weekday");

        classifiersService.update(OranTeivClassifier.builder().classifiers(classifiersToMerge).entityIds(Collections
                .emptyList()).relationshipIds(List.of(RELATIONSHIP_ID)).operation(OperationEnum.MERGE).build());

        verifyClassifiers(RELATIONSHIP_CLASSIFIERS, TABLE_NAME, ENTITY_ID, classifiersExpected);
    }

    @Test
    void testAdd_entityAndRelationshipClassifiers() {
        List<String> classifiersToMerge = List.of("test-app-module:Outdoor", "test-app-module:Weekday");
        List<String> classifiersExpected = List.of("test-app-module:Indoor", "test-app-module:Rural",
                "test-app-module:Weekend", "test-app-module:Outdoor", "test-app-module:Weekday");

        classifiersService.update(OranTeivClassifier.builder().classifiers(classifiersToMerge).entityIds(List.of(ENTITY_ID))
                .relationshipIds(List.of(RELATIONSHIP_ID)).operation(OperationEnum.MERGE).build());

        verifyClassifiers(ENTITY_CLASSIFIERS, TABLE_NAME, ENTITY_ID, classifiersExpected);
        verifyClassifiers(RELATIONSHIP_CLASSIFIERS, TABLE_NAME, ENTITY_ID, classifiersExpected);
    }

    @Test
    void testAddDuplicates_entityAndRelationshipClassifiers() {
        List<String> classifiersToMerge = List.of("test-app-module:Indoor", "test-app-module:Rural");
        List<String> classifiersExpected = List.of("test-app-module:Indoor", "test-app-module:Rural",
                "test-app-module:Weekend");

        classifiersService.update(OranTeivClassifier.builder().classifiers(classifiersToMerge).entityIds(List.of(ENTITY_ID))
                .relationshipIds(List.of(RELATIONSHIP_ID)).operation(OperationEnum.MERGE).build());

        verifyClassifiers(ENTITY_CLASSIFIERS, TABLE_NAME, ENTITY_ID, classifiersExpected);
        verifyClassifiers(RELATIONSHIP_CLASSIFIERS, TABLE_NAME, ENTITY_ID, classifiersExpected);
    }

    @Test
    void testAdd_entityAndRelationshipInvalidClassifiers() {
        List<String> classifiersToMerge = List.of("test-app-module:Indoor_WRONG", "test-app-module:Rural_WRONG");

        assertThatThrownBy(() -> classifiersService.update(OranTeivClassifier.builder().classifiers(classifiersToMerge)
                .entityIds(List.of(ENTITY_ID)).relationshipIds(List.of(RELATIONSHIP_ID)).operation(OperationEnum.MERGE)
                .build())).isInstanceOf(TiesException.class);
    }

    @Test
    void testDelete_emptyEntityClassifiers() {
        List<String> classifiersToDelete = Collections.emptyList();
        List<String> classifiersExpected = List.of("test-app-module:Indoor", "test-app-module:Rural",
                "test-app-module:Weekend");

        classifiersService.update(OranTeivClassifier.builder().classifiers(classifiersToDelete).entityIds(List.of(
                ENTITY_ID)).relationshipIds(Collections.emptyList()).operation(OperationEnum.DELETE).build());

        verifyClassifiers(ENTITY_CLASSIFIERS, TABLE_NAME, ENTITY_ID, classifiersExpected);
    }

    @Test
    void testDelete_existingEntityClassifiers() {
        List<String> classifiersToDelete = List.of("test-app-module:Rural", "test-app-module:Weekend");
        List<String> classifiersExpected = List.of("test-app-module:Indoor");

        classifiersService.update(OranTeivClassifier.builder().classifiers(classifiersToDelete).entityIds(List.of(
                ENTITY_ID)).relationshipIds(Collections.emptyList()).operation(OperationEnum.DELETE).build());

        verifyClassifiers(ENTITY_CLASSIFIERS, TABLE_NAME, ENTITY_ID, classifiersExpected);
    }

    @Test
    void testDelete_entityClassifiers_withNotExistingEntityId() {
        List<String> classifiersToDelete = List.of("test-app-module:Rural", "test-app-module:Weekend");

        assertThatThrownBy(() -> classifiersService.update(OranTeivClassifier.builder().classifiers(classifiersToDelete)
                .entityIds(List.of("WRONG_ID")).relationshipIds(Collections.emptyList()).operation(OperationEnum.DELETE)
                .build())).isInstanceOf(TiesException.class);
    }

    @Test
    void testDelete_existingRelationshipClassifiers() {
        List<String> classifiersToDelete = List.of("test-app-module:Rural", "test-app-module:Weekend");
        List<String> classifiersExpected = List.of("test-app-module:Indoor");

        classifiersService.update(OranTeivClassifier.builder().classifiers(classifiersToDelete).entityIds(Collections
                .emptyList()).relationshipIds(List.of(RELATIONSHIP_ID)).operation(OperationEnum.DELETE).build());

        verifyClassifiers(RELATIONSHIP_CLASSIFIERS, TABLE_NAME, ENTITY_ID, classifiersExpected);
    }

    @Test
    void testDelete_relationshipClassifiers_withNotExistingRelationshipId() {
        List<String> classifiersToDelete = List.of("test-app-module:Rural", "test-app-module:Weekend");

        assertThatThrownBy(() -> classifiersService.update(OranTeivClassifier.builder().classifiers(classifiersToDelete)
                .entityIds(Collections.emptyList()).relationshipIds(List.of("WRONG_ID")).operation(OperationEnum.DELETE)
                .build())).isInstanceOf(TiesException.class);
    }

    @Test
    void testDelete_existingEntityAndRelationshipClassifiers() {
        List<String> classifiersToDelete = List.of("test-app-module:Rural", "test-app-module:Weekend");
        List<String> classifiersExpected = List.of("test-app-module:Indoor");

        classifiersService.update(OranTeivClassifier.builder().classifiers(classifiersToDelete).entityIds(List.of(
                ENTITY_ID)).relationshipIds(List.of(RELATIONSHIP_ID)).operation(OperationEnum.DELETE).build());

        verifyClassifiers(ENTITY_CLASSIFIERS, TABLE_NAME, ENTITY_ID, classifiersExpected);
        verifyClassifiers(RELATIONSHIP_CLASSIFIERS, TABLE_NAME, ENTITY_ID, classifiersExpected);
    }

    @Test
    void testDelete_NotExistingEntityAndRelationshipClassifiers() {
        List<String> classifiersToDelete = List.of("test-app-module:Rural_WRONG", "test-app-module:Weekend_WRONG");
        List<String> classifiersExpected = List.of("test-app-module:Indoor", "test-app-module:Rural",
                "test-app-module:Weekend");

        classifiersService.update(OranTeivClassifier.builder().classifiers(classifiersToDelete).entityIds(List.of(
                ENTITY_ID)).relationshipIds(List.of(RELATIONSHIP_ID)).operation(OperationEnum.DELETE).build());

        verifyClassifiers(ENTITY_CLASSIFIERS, TABLE_NAME, ENTITY_ID, classifiersExpected);
        verifyClassifiers(RELATIONSHIP_CLASSIFIERS, TABLE_NAME, ENTITY_ID, classifiersExpected);
    }

    private void verifyClassifiers(String fieldName, String tableName, String id, List<String> classifiersExpected) {
        SelectConditionStep<Record1<JSONB>> select = readDataDslContext.select(field(fieldName, JSONB.class)).from(table(
                tableName)).where(field("id").eq(id));

        Result<Record1<JSONB>> result = select.fetch();

        List<String> classifiersActual = JooqTypeConverter.jsonbToList(result.get(0).value1());

        assertEquals(classifiersExpected, classifiersActual);
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
