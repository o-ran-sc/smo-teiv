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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.oran.smo.teiv.utils.JooqTypeConverter.jsonbToMap;
import static org.oran.smo.teiv.utils.TeivConstants.TEIV_DATA_SCHEMA;
import static org.oran.smo.teiv.utils.TeivTestConstants.KAFKA_RETRY_INTERVAL_10_MS;
import static org.oran.smo.teiv.utils.TeivTestConstants.SPRING_BOOT_SERVER_HOST;
import static org.oran.smo.teiv.utils.TeivTestConstants.SPRING_BOOT_SERVER_PORT;

import java.io.File;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import io.cloudevents.CloudEvent;
import io.cloudevents.kafka.CloudEventSerializer;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.DescribeConsumerGroupsOptions;
import org.apache.kafka.clients.admin.DescribeConsumerGroupsResult;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.awaitility.Awaitility;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.Result;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.oran.smo.teiv.CustomMetrics;
import org.oran.smo.teiv.availability.DependentServiceAvailabilityKafka;
import org.oran.smo.teiv.config.KafkaConfig;
import org.oran.smo.teiv.db.TestPostgresqlContainer;
import org.oran.smo.teiv.listener.ListenerStarter;
import org.oran.smo.teiv.service.kafka.KafkaTopicService;
import org.oran.smo.teiv.startup.AppInit;
import org.oran.smo.teiv.utils.CloudEventTestUtil;
import org.oran.smo.teiv.utils.EndToEndExpectedResults;
import org.oran.smo.teiv.utils.KafkaTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestExecutionListeners;
import org.testcontainers.shaded.org.checkerframework.checker.nullness.qual.Nullable;

@EmbeddedKafka
@ActiveProfiles({ "test", "ingestion" })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(properties = { SPRING_BOOT_SERVER_HOST, SPRING_BOOT_SERVER_PORT, KAFKA_RETRY_INTERVAL_10_MS,
        "kafka.topic.replicas:1", "kafka.topology-ingestion.consumer.concurrency:2", "data-catalog.enabled:true" })
@TestExecutionListeners(listeners = KafkaTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class EndToEndDbTest {
    private static TestPostgresqlContainer postgresqlContainer = TestPostgresqlContainer.getInstance();

    private AppInit appInit;

    private DefaultKafkaProducerFactory<String, CloudEvent> factory;

    private Producer<String, CloudEvent> producer;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    private static final String TEST_EVENT_FOLDER = "src/test/resources/cloudeventdata/end-to-end/";
    private static final String EXPECTED_RESULTS_FOLDER = "src/test/resources/cloudeventdata/end-to-end/expected-results/";

    @Autowired
    private DependentServiceAvailabilityKafka dependentServiceAvailabilityKafka;

    @Autowired
    private KafkaTopicService kafkaTopicService;

    @Autowired
    private ListenerStarter listenerStarter;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private CustomMetrics customMetrics;

    @Autowired
    private KafkaConfig kafkaConfig;

    @Autowired
    private KafkaAdmin kafkaAdmin;

    @Autowired
    private DSLContext writeDataDslContext;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.read.jdbc-url", () -> postgresqlContainer.getJdbcUrl());
        registry.add("spring.datasource.read.username", () -> postgresqlContainer.getUsername());
        registry.add("spring.datasource.read.password", () -> postgresqlContainer.getPassword());
        registry.add("spring.datasource.write.jdbc-url", () -> postgresqlContainer.getJdbcUrl());
        registry.add("spring.datasource.write.username", () -> postgresqlContainer.getUsername());
        registry.add("spring.datasource.write.password", () -> postgresqlContainer.getPassword());
    }

    private static final String FIRST_DISCOVERED = "firstDiscovered";
    private static final String LAST_MODIFIED = "lastModified";
    private static final String RELIABILITY_INDICATOR = "reliabilityIndicator";

    private OffsetDateTime testStartTime;

    @BeforeAll
    static void beforeAll() {
        TestPostgresqlContainer.loadData();
        TestPostgresqlContainer.loadIngestionTestData();
    }

    @BeforeEach
    void setupEach() {
        TestPostgresqlContainer.truncateSchemas(List.of(TEIV_DATA_SCHEMA), writeDataDslContext);
        appInit = new AppInit(dependentServiceAvailabilityKafka, kafkaTopicService, listenerStarter);
        appInit.startUpHandler();
        factory = new DefaultKafkaProducerFactory<>(KafkaTestUtils.producerProps(embeddedKafkaBroker),
                new StringSerializer(), new CloudEventSerializer());
        producer = factory.createProducer();
        testStartTime = OffsetDateTime.now(ZoneOffset.UTC);
    }

    @AfterEach
    void cleanupEach() {
        factory.destroy();
    }

    @AfterAll
    void cleanupAll() {
        kafkaListenerEndpointRegistry.stop();
        Admin adminClient = Admin.create(kafkaAdmin.getConfigurationProperties());
        adminClient.close();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testEndToEndDb() {
        final String CREATE_ONE_TO_ONE_PATH = TEST_EVENT_FOLDER + "ce-create-one-to-one.json";
        final String CREATE_MANY_TO_MANY_PATH = TEST_EVENT_FOLDER + "ce-create-many-to-many.json";
        final String CREATE_SECOND_CASE_PATH = TEST_EVENT_FOLDER + "ce-create-second-case.json";
        final String CREATE_INFERRED_ENTITIES = TEST_EVENT_FOLDER + "ce-create-inferred.json";
        final String CREATE_GEOLOCATION_PATH = TEST_EVENT_FOLDER + "ce-create-geo-location.json";
        final String MERGE_ONE_TO_MANY_PATH = TEST_EVENT_FOLDER + "ce-merge-one-to-many.json";
        final String MERGE_ONE_TO_MANY3_PATH = TEST_EVENT_FOLDER + "ce-merge-one-to-many3.json";
        final String MERGE_MANY_TO_MANY_PATH = TEST_EVENT_FOLDER + "ce-merge-many-to-many.json";
        final String DELETE_MANY_TO_MANY_PATH = TEST_EVENT_FOLDER + "ce-delete-many-to-many.json";
        final String DELETE_ONE_TO_ONE_PATH = TEST_EVENT_FOLDER + "ce-delete-one-to-one.json";
        final String DELETE_CMHANDLE_PATH = TEST_EVENT_FOLDER + "ce-source-entity-delete-cm-handle.json";

        final String EXP_CREATE_ONE_TO_ONE_PATH = EXPECTED_RESULTS_FOLDER + "exp-create-one-to-one.json";
        final String EXP_CREATE_MANY_TO_MANY_PATH = EXPECTED_RESULTS_FOLDER + "exp-create-many-to-many.json";
        final String EXP_CREATE_SECOND_CASE_PATH = EXPECTED_RESULTS_FOLDER + "exp-create-second-case.json";
        final String EXP_CREATE_INFERRED_ENTITIES = EXPECTED_RESULTS_FOLDER + "exp-create-inferred.json";
        final String EXP_CREATE_GEOLOCATION_PATH = EXPECTED_RESULTS_FOLDER + "exp-create-geo-location.json";
        final String EXP_MERGE_ONE_TO_MANY_PATH = EXPECTED_RESULTS_FOLDER + "exp-merge-one-to-many.json";
        final String EXP_MERGE_ONE_TO_MANY3_PATH = EXPECTED_RESULTS_FOLDER + "exp-merge-one-to-many3.json";
        final String EXP_MERGE_MANY_TO_MANY_PATH = EXPECTED_RESULTS_FOLDER + "exp-merge-many-to-many.json";
        final String EXP_DELETE_ONE_TO_ONE_PATH = EXPECTED_RESULTS_FOLDER + "exp-delete-one-to-one.json";
        final String NOT_EXP_DELETE_ONE_TO_ONE_PATH = EXPECTED_RESULTS_FOLDER + "not-exp-delete-one-to-one.json";
        final String NOT_EXP_DELETE_MANY_TO_MANY_PATH = EXPECTED_RESULTS_FOLDER + "not-exp-delete-many-to-many.json";
        final String NOT_EXP_DELETE_CMHANDLE_PATH = EXPECTED_RESULTS_FOLDER + "not-exp-source-entity-delete-cm-handle.json";

        testStartTime = OffsetDateTime.now(ZoneOffset.UTC);
        validateReceivedCloudEventMetrics(0, 0, 0, 0);
        sendEventFromFile(CREATE_ONE_TO_ONE_PATH);
        validateWithTimeout(15, () -> {
            EndToEndExpectedResults expected = getExpectedResults(EXP_CREATE_ONE_TO_ONE_PATH);
            assertDbContainsExpectedValues(expected);
            validateReceivedCloudEventMetrics(1, 0, 0, 0);
        });

        testStartTime = OffsetDateTime.now(ZoneOffset.UTC);
        sendEventFromFile(CREATE_MANY_TO_MANY_PATH);
        validateWithTimeout(15, () -> {
            EndToEndExpectedResults expected = getExpectedResults(EXP_CREATE_MANY_TO_MANY_PATH);
            assertDbContainsExpectedValues(expected);
            validateReceivedCloudEventMetrics(2, 0, 0, 0);
        });

        testStartTime = OffsetDateTime.now(ZoneOffset.UTC);
        //Sends the same entities and relationships as previous CE, but with different entity attributes. Relationships same.
        sendEventFromFile(MERGE_MANY_TO_MANY_PATH);
        validateWithTimeout(15, () -> {
            EndToEndExpectedResults expected = getExpectedResults(EXP_MERGE_MANY_TO_MANY_PATH);
            assertDbContainsExpectedValues(expected);
            validateReceivedCloudEventMetrics(2, 1, 0, 0);
        });

        testStartTime = OffsetDateTime.now(ZoneOffset.UTC);
        sendEventFromFile(CREATE_GEOLOCATION_PATH);
        validateWithTimeout(15, () -> {
            EndToEndExpectedResults expected = getExpectedResults(EXP_CREATE_GEOLOCATION_PATH);
            assertDbContainsExpectedValues(expected);
            validateReceivedCloudEventMetrics(3, 1, 0, 0);
        });

        testStartTime = OffsetDateTime.now(ZoneOffset.UTC);
        sendEventFromFile(MERGE_ONE_TO_MANY_PATH);//This sends new entities even though it's a merge event
        validateWithTimeout(15, () -> {
            EndToEndExpectedResults expected = getExpectedResults(EXP_MERGE_ONE_TO_MANY_PATH);
            assertDbContainsExpectedValues(expected);
            validateReceivedCloudEventMetrics(3, 2, 0, 0);
        });

        testStartTime = OffsetDateTime.now(ZoneOffset.UTC);
        //Sends the same entities and relationships as previous CE, but with different entity attributes. Relationships same.
        sendEventFromFile(MERGE_ONE_TO_MANY3_PATH);
        validateWithTimeout(15, () -> {
            EndToEndExpectedResults expected = getExpectedResults(EXP_MERGE_ONE_TO_MANY3_PATH);
            assertDbContainsExpectedValues(expected);
            validateReceivedCloudEventMetrics(3, 3, 0, 0);
        });

        testStartTime = OffsetDateTime.now(ZoneOffset.UTC);
        sendEventFromFile(CREATE_SECOND_CASE_PATH);
        validateWithTimeout(15, () -> {
            EndToEndExpectedResults expected = getExpectedResults(EXP_CREATE_SECOND_CASE_PATH);
            assertDbContainsExpectedValues(expected);
            validateReceivedCloudEventMetrics(4, 3, 0, 0);
        });

        testStartTime = OffsetDateTime.now(ZoneOffset.UTC);
        sendEventFromFile(CREATE_INFERRED_ENTITIES);

        validateWithTimeout(25, () -> {
            EndToEndExpectedResults expected = getExpectedResults(EXP_CREATE_INFERRED_ENTITIES);
            assertDbContainsExpectedValues(expected);
            validateReceivedCloudEventMetrics(5, 3, 0, 0);
        });

        testStartTime = OffsetDateTime.now(ZoneOffset.UTC);
        sendEventFromFile(CREATE_GEOLOCATION_PATH);
        validateWithTimeout(15, () -> {
            EndToEndExpectedResults expected = getExpectedResults(EXP_CREATE_GEOLOCATION_PATH);
            assertDbContainsExpectedValues(expected);
            validateReceivedCloudEventMetrics(6, 3, 0, 0);
        });

        testStartTime = OffsetDateTime.now(ZoneOffset.UTC);
        sendEventFromFile(DELETE_MANY_TO_MANY_PATH);
        validateWithTimeout(15, () -> {
            EndToEndExpectedResults notExpected = getExpectedResults(NOT_EXP_DELETE_MANY_TO_MANY_PATH);
            assertDbNotContainsExpectedValues(notExpected);
            validateReceivedCloudEventMetrics(6, 3, 1, 0);
        });

        testStartTime = OffsetDateTime.now(ZoneOffset.UTC);
        sendEventFromFile(DELETE_ONE_TO_ONE_PATH);
        validateWithTimeout(15, () -> {
            EndToEndExpectedResults expected = getExpectedResults(EXP_DELETE_ONE_TO_ONE_PATH);
            assertDbContainsExpectedValues(expected);
            EndToEndExpectedResults notExpected = getExpectedResults(NOT_EXP_DELETE_ONE_TO_ONE_PATH);
            assertDbNotContainsExpectedValues(notExpected);
            validateReceivedCloudEventMetrics(6, 3, 2, 0);
        });

        testStartTime = OffsetDateTime.now(ZoneOffset.UTC);
        sendEventFromFile(DELETE_CMHANDLE_PATH);
        validateWithTimeout(15, () -> {
            EndToEndExpectedResults notExpected = getExpectedResults(NOT_EXP_DELETE_CMHANDLE_PATH);
            assertDbNotContainsExpectedValues(notExpected);
            validateReceivedCloudEventMetrics(6, 3, 2, 1);
        });
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testTopologyIngestionConcurrentListeners() {
        final String COMMON_EVENT_FOLDER = "src/test/resources/cloudeventdata/common/";

        List<String> commonPartition0 = List.of("ce-invalid-entity-attribute.json", "ce-one-entity.json",
                "ce-relationship-invalid-module.json", "ce-relationship-invalid-module-type-pair.json",
                "ce-create-geo-location.json", "ce-create-many-to-many.json");
        List<String> commonPartition1 = List.of("ce-relationship-invalid-type.json", "ce-relationship-missing-a-side.json",
                "ce-relationship-missing-b-side.json", "ce-relationship-missing-both-sides.json",
                "ce-create-many-to-one.json", "ce-create-one-to-many.json");
        List<String> partition2 = List.of("ce-create-one-to-one.json", "ce-create-relationship-connecting-same-entity.json",
                "ce-create-second-case.json", "ce-delete-many-to-many.json", "ce-delete-many-to-one.json",
                "ce-delete-one-to-many.json");
        List<String> partition3 = List.of("ce-delete-one-to-one.json", "ce-delete-relationship-connecting-same-entity.json",
                "ce-merge-long-names.json", "ce-merge-one-to-many2.json", "ce-source-entity-delete-cm-handle.json");

        List<String> cloudEventsForPartition0 = new ArrayList<>();
        cloudEventsForPartition0.addAll(getMultipleFilesInDirectory(COMMON_EVENT_FOLDER, commonPartition0));
        cloudEventsForPartition0.addAll(getMultipleFilesInDirectory(TEST_EVENT_FOLDER, commonPartition0));

        List<String> cloudEventsForPartition1 = new ArrayList<>();
        cloudEventsForPartition1.addAll(getMultipleFilesInDirectory(COMMON_EVENT_FOLDER, commonPartition1));
        cloudEventsForPartition1.addAll(getMultipleFilesInDirectory(TEST_EVENT_FOLDER, commonPartition1));

        List<String> cloudEventsForPartition2 = getMultipleFilesInDirectory(TEST_EVENT_FOLDER, partition2);
        List<String> cloudEventsForPartition3 = getMultipleFilesInDirectory(TEST_EVENT_FOLDER, partition3);

        List<List<String>> cloudEventPathLists = List.of(cloudEventsForPartition0, cloudEventsForPartition1,
                cloudEventsForPartition2, cloudEventsForPartition3);

        produceKafkaMessages(cloudEventPathLists, List.of(0, 1, 2, 3));
        validateWithTimeout(20, () -> {
            assertEquals(2, assertDoesNotThrow(() -> getActiveConsumers(kafkaConfig.getTopologyIngestion().getGroupId())));
            validateReceivedCloudEventMetrics(15, 2, 5, 1);
            validatePersistedCloudEventMetrics(8, 2, 5, 1);
        });
    }

    private void validatePersistedCloudEventMetrics(final int createEventQuantity, final int mergeEventQuantity,
            final int deleteEventQuantity, final int sourceEntityDeleteEventQuantity) {
        assertEquals(createEventQuantity, customMetrics.getNumSuccessfullyPersistedCreateCloudEvents().count());
        assertEquals(deleteEventQuantity, customMetrics.getNumSuccessfullyPersistedDeleteCloudEvents().count());
        assertEquals(mergeEventQuantity, customMetrics.getNumSuccessfullyPersistedMergeCloudEvents().count());
        assertEquals(sourceEntityDeleteEventQuantity, customMetrics
                .getNumSuccessfullyPersistedSourceEntityDeleteCloudEvents().count());
    }

    private void validateReceivedCloudEventMetrics(final int create, final int merge, final int delete,
            final int sourceDelete) {
        assertEquals(create, customMetrics.getNumReceivedCloudEventCreate().count());
        assertEquals(merge, customMetrics.getNumReceivedCloudEventMerge().count());
        assertEquals(delete, customMetrics.getNumReceivedCloudEventDelete().count());
        assertEquals(sourceDelete, customMetrics.getNumReceivedCloudEventSourceEntityDelete().count());
    }

    private void assertDbContainsExpectedValues(EndToEndExpectedResults expectedResults) {
        expectedResults.getTables().forEach(tableName -> expectedResults.getTableData(tableName).forEach(
                expectedAttributes -> assertDatabaseContainsValues(tableName, expectedAttributes)));
    }

    private void assertDbNotContainsExpectedValues(EndToEndExpectedResults expectedResults) {
        expectedResults.getTables().forEach(tableName -> {
            expectedResults.getTableEntryIds(tableName).forEach(ids -> assertDatabaseDoesNotContainRecord(tableName, ids));
        });
    }

    private void assertDatabaseContainsValues(final String table, final Map<String, Object> expectedFields) {
        Result<Record> results = TeivDbServiceContainerizedTest.selectAllRowsFromTable(writeDataDslContext,
                "teiv_data.\"" + table + "\"");
        AtomicReference<String> debugInfo = new AtomicReference<>("");
        AtomicReference<Record> targetRow = new AtomicReference<>();
        boolean containsExpectedRow = results.stream().anyMatch(row -> {
            if (Objects.equals(row.get("id"), expectedFields.get("id"))) {
                targetRow.set(row);
                return true;
            }
            return false;
        });
        assertTrue(containsExpectedRow, "Row not found with id = " + expectedFields.get("id"));
        boolean containsExpectedData = expectedFields.entrySet().stream().allMatch(expectedField -> {
            final String expectedFieldName = expectedField.getKey();
            @Nullable Object actualFieldValue = targetRow.get().get(expectedFieldName);
            if (expectedField.getValue() != null) {
                if (actualFieldValue instanceof byte[]) {
                    String hashString = bytesToHex((byte[]) Objects.requireNonNull(actualFieldValue));
                    if (!Objects.equals(expectedField.getValue().toString(), hashString)) {
                        debugInfo.set(expectedField.getKey() + " not as expected for " + targetRow.get().get("id"));
                        return false;
                    } else {
                        return true;
                    }
                }
                if (expectedField.getKey().contains("metadata")) {//Will be true for the metadata of the entity, and of each relation
                    Map<String, Object> expectedMetadata = jsonbToMap((JSONB) expectedField.getValue());
                    if (actualFieldValue == null) {
                        debugInfo.set(expectedFieldName + " of " + expectedFields.get("id") + " is null");
                        return false;
                    }
                    Map<String, Object> actualMetadata = jsonbToMap((JSONB) Objects.requireNonNull(actualFieldValue));
                    if (expectedMetadata.containsKey(RELIABILITY_INDICATOR)) {
                        if (!Objects.equals(expectedMetadata.get(RELIABILITY_INDICATOR), actualMetadata.get(
                                RELIABILITY_INDICATOR))) {
                            debugInfo.set(RELIABILITY_INDICATOR + " of " + expectedField.getKey() + " of " + targetRow.get()
                                    .get("id") + " should be " + expectedMetadata.get(
                                            RELIABILITY_INDICATOR) + " but it is " + actualMetadata.get(
                                                    RELIABILITY_INDICATOR));
                            return false;
                        }
                    }
                    if (expectedMetadata.containsKey(FIRST_DISCOVERED)) {
                        return validateMetadataTimestamp(FIRST_DISCOVERED, expectedField, actualMetadata, debugInfo,
                                targetRow, expectedMetadata);
                    }
                    if (expectedMetadata.containsKey(LAST_MODIFIED)) {
                        return validateMetadataTimestamp(LAST_MODIFIED, expectedField, actualMetadata, debugInfo, targetRow,
                                expectedMetadata);
                    }
                    return true;
                } else {
                    if (actualFieldValue == null || !Objects.equals(expectedField.getValue().toString(), actualFieldValue
                            .toString())) {
                        debugInfo.set(targetRow.get().get("id") + " " + expectedField
                                .getKey() + " should be " + expectedField.getValue() + " but it is " + actualFieldValue);
                        return false;
                    } else {
                        return true;
                    }
                }
            }
            if (actualFieldValue != null) {
                debugInfo.set(expectedFieldName + " should have been null.");
            }
            return actualFieldValue == null;
        });
        assertTrue(containsExpectedData, getAsserDbValuesFailureMessage(table, expectedFields, results, debugInfo.get()));
    }

    private boolean validateMetadataTimestamp(String timestampKeyToVerify, Map.Entry<String, Object> expectedField,
            Map<String, Object> actualMetadata, AtomicReference<String> debugInfo, AtomicReference<Record> targetRow,
            Map<String, Object> expectedMetadata) {
        Object actualtimestampObject = actualMetadata.get(timestampKeyToVerify);
        if (actualtimestampObject == null) {
            debugInfo.set(targetRow.get().get("id") + ":\n" + timestampKeyToVerify + " should not be null");
            return false;
        }
        OffsetDateTime actualTimestamp = OffsetDateTime.parse(actualtimestampObject.toString());
        String expectedTimestamp = expectedMetadata.get(timestampKeyToVerify).toString();
        if (expectedTimestamp.contains("AFTER TEST START TIME")) {
            if (actualTimestamp.isBefore(testStartTime)) {
                debugInfo.set(targetRow.get().get(
                        "id") + ":\n" + timestampKeyToVerify + " " + actualTimestamp + " should be after " + testStartTime + " in " + expectedField
                                .getKey());
                return false;
            }
        } else if (expectedTimestamp.contains("BEFORE TEST START TIME")) {
            if (actualTimestamp.isAfter(testStartTime)) {
                debugInfo.set(targetRow.get().get(
                        "id") + ": " + timestampKeyToVerify + " " + actualTimestamp + " should be before " + testStartTime + " in " + expectedField
                                .getKey());
                return false;
            }
        } else {
            if (!actualTimestamp.isEqual(OffsetDateTime.parse("2025-01-08T10:40:36.46156500Z")) && !actualTimestamp.isEqual(
                    OffsetDateTime.parse("2025-01-09T10:40:36.461565Z"))) {
                debugInfo.set(targetRow.get().get(
                        "id") + ": " + timestampKeyToVerify + " " + actualTimestamp + " should be " + "2025-01-08T10:40:36.46156500Z" + " in " + expectedField
                                .getKey());
                return false;
            }
        }
        return true;
    }

    private String bytesToHex(byte[] hashBytes) {
        return Base64.getEncoder().encodeToString(hashBytes);
    }

    private String getAsserDbValuesFailureMessage(String table, Map<String, Object> attributes, Result<Record> results,
            String extraInfo) {
        return String.format(
                "Database table \"%s\" does not contain expected data, but it should.\nExpected row:\n" + attributes + "\nActual data (all rows):\n" + results
                        .formatCSV() + "\n%s\n", table, extraInfo);
    }

    private void assertDatabaseDoesNotContainRecord(final String table, final String id) {
        Result<Record> results = TeivDbServiceContainerizedTest.selectAllRowsFromTable(writeDataDslContext,
                "teiv_data.\"" + table + "\"");
        if (results.isNotEmpty()) {
            boolean containsRecord = results.stream().map(row -> row.get("id").toString()).anyMatch(id::equals);
            assertFalse(containsRecord, String.format("Database table \"%s\" contains record: \"%s\", but it should not.",
                    table, id));
        }
    }

    private int getActiveConsumers(String consumerGroupId) throws InterruptedException, ExecutionException {
        try (Admin adminClient = Admin.create(kafkaAdmin.getConfigurationProperties())) {
            DescribeConsumerGroupsOptions options = new DescribeConsumerGroupsOptions().includeAuthorizedOperations(false);
            DescribeConsumerGroupsResult consumerGroupsResult = adminClient.describeConsumerGroups(List.of(consumerGroupId),
                    options);
            ConsumerGroupDescription description = consumerGroupsResult.describedGroups().get(consumerGroupId).get();
            return description.members().size();
        }
    }

    private List<String> getMultipleFilesInDirectory(String directoryName, List<String> filenames) {
        File[] files = new File(directoryName).listFiles();
        return files == null ?
                List.of() :
                Stream.of(files).map(File::getName).filter(filenames::contains).map(filename -> String.format("%s%s",
                        directoryName, filename)).toList();
    }

    private void produceKafkaMessages(List<List<String>> cloudEventPathLists, List<Integer> partitions) {
        if (cloudEventPathLists.size() != partitions.size()) {
            throw new RuntimeException("A partition is needed for every CloudEvent list");
        }
        int maxLength = cloudEventPathLists.stream().mapToInt(List::size).max().orElse(0);
        for (int i = 0; i < maxLength; i++) {
            for (int j = 0; j < cloudEventPathLists.size(); j++) {
                if (cloudEventPathLists.get(j).size() > i) {
                    sendEventFromFile(cloudEventPathLists.get(j).get(i), partitions.get(j));
                }
            }
        }
    }

    private void sendEventFromFile(final String path) {
        final int partition_0 = 0;
        sendEventFromFile(path, partition_0);
    }

    private void sendEventFromFile(final String path, int partition) {
        assertDoesNotThrow(() -> {
            CloudEvent event = CloudEventTestUtil.getCloudEventFromJsonFile(path);
            ProducerRecord<String, CloudEvent> producerRecord = new ProducerRecord<String, CloudEvent>(kafkaConfig
                    .getTopologyIngestion().getTopicName(), partition, null, event);
            producer.send(producerRecord);
        });

    }

    private EndToEndExpectedResults getExpectedResults(final String path) {
        return assertDoesNotThrow(() -> new EndToEndExpectedResults(path));
    }

    private void validateWithTimeout(int timeout, Runnable runnable) {
        Awaitility.await().atMost(timeout, TimeUnit.SECONDS).pollDelay(Duration.ofSeconds(5)).untilAsserted(() -> {
            try {
                runnable.run();
            } catch (AssertionError e) {
                throw new AssertionError("Assertion failed during validation of CloudEvent: " + e.getMessage(), e);
            }
        });
    }
}
