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
import static org.oran.smo.teiv.utils.TiesConstants.TIES_DATA_SCHEMA;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
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
import org.jooq.Record;
import org.jooq.Result;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.oran.smo.teiv.CustomMetrics;
import org.oran.smo.teiv.availability.DependentServiceAvailabilityKafka;
import org.oran.smo.teiv.config.KafkaConfig;
import org.oran.smo.teiv.db.TestPostgresqlContainer;
import org.oran.smo.teiv.listener.ListenerStarter;
import org.oran.smo.teiv.service.kafka.KafkaTopicService;
import org.oran.smo.teiv.startup.AppInit;
import org.oran.smo.teiv.utils.CloudEventTestUtil;
import org.oran.smo.teiv.utils.EndToEndExpectedResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@EmbeddedKafka
@SpringBootTest(properties = {
        "kafka.server.bootstrap-server-host:#{environment.getProperty(\"spring.embedded.kafka.brokers\").split(\":\")[0]}",
        "kafka.server.bootstrap-server-port:#{environment.getProperty(\"spring.embedded.kafka.brokers\").split(\":\")[1]}",
        "kafka.availability.retryIntervalMs:10", "kafka.topic.replicas:1",
        "kafka.topology-ingestion.consumer.concurrency:2" })
@ActiveProfiles({ "test", "ingestion" })
public class EndToEndDbTest {
    private static TestPostgresqlContainer postgresqlContainer = TestPostgresqlContainer.getInstance();

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

    private AppInit appInit;

    @Autowired
    private KafkaConfig kafkaConfig;

    @Autowired
    private KafkaAdmin kafkaAdmin;

    @Autowired
    private DSLContext writeDataDslContext;

    static Producer<String, CloudEvent> producer;

    private static final String TEST_EVENT_FOLDER = "src/test/resources/cloudeventdata/end-to-end/";
    private static final String EXPECTED_RESULTS_FOLDER = "src/test/resources/cloudeventdata/end-to-end/expected-results/";

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.read.jdbc-url", () -> postgresqlContainer.getJdbcUrl());
        registry.add("spring.datasource.read.username", () -> postgresqlContainer.getUsername());
        registry.add("spring.datasource.read.password", () -> postgresqlContainer.getPassword());
        registry.add("spring.datasource.write.jdbc-url", () -> postgresqlContainer.getJdbcUrl());
        registry.add("spring.datasource.write.username", () -> postgresqlContainer.getUsername());
        registry.add("spring.datasource.write.password", () -> postgresqlContainer.getPassword());
    }

    @BeforeEach
    void setUp() {
        writeDataDslContext.meta().filterSchemas(s -> s.getName().equals(TIES_DATA_SCHEMA)).getTables().forEach(
                t -> writeDataDslContext.truncate(t).cascade().execute());
        appInit = new AppInit(dependentServiceAvailabilityKafka, kafkaTopicService, listenerStarter);
        appInit.startUpHandler();
        producer = new DefaultKafkaProducerFactory<>(new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker)),
                new StringSerializer(), new CloudEventSerializer()).createProducer();
    }

    @AfterEach
    void tearDown() {
        embeddedKafkaBroker.destroy();
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void testEndToEndDb() {
        final String CREATE_ONE_TO_ONE_PATH = TEST_EVENT_FOLDER + "ce-create-one-to-one.json";
        final String CREATE_MANY_TO_MANY_PATH = TEST_EVENT_FOLDER + "ce-create-many-to-many.json";
        final String CREATE_SECOND_CASE_PATH = TEST_EVENT_FOLDER + "ce-create-second-case.json";
        final String MERGE_ONE_TO_MANY_PATH = TEST_EVENT_FOLDER + "ce-merge-one-to-many.json";
        final String DELETE_MANY_TO_MANY_PATH = TEST_EVENT_FOLDER + "ce-delete-many-to-many.json";
        final String DELETE_ONE_TO_ONE_PATH = TEST_EVENT_FOLDER + "ce-delete-one-to-one.json";
        final String DELETE_CMHANDLE_PATH = TEST_EVENT_FOLDER + "ce-source-entity-delete-cm-handle.json";

        final String EXP_CREATE_ONE_TO_ONE_PATH = EXPECTED_RESULTS_FOLDER + "exp-create-one-to-one.json";
        final String EXP_CREATE_MANY_TO_MANY_PATH = EXPECTED_RESULTS_FOLDER + "exp-create-many-to-many.json";
        final String EXP_CREATE_SECOND_CASE_PATH = EXPECTED_RESULTS_FOLDER + "exp-create-second-case.json";
        final String EXP_MERGE_ONE_TO_MANY_PATH = EXPECTED_RESULTS_FOLDER + "exp-merge-one-to-many.json";
        final String EXP_DELETE_ONE_TO_ONE_PATH = EXPECTED_RESULTS_FOLDER + "exp-delete-one-to-one.json";
        final String NOT_EXP_DELETE_ONE_TO_ONE_PATH = EXPECTED_RESULTS_FOLDER + "not-exp-delete-one-to-one.json";
        final String NOT_EXP_DELETE_MANY_TO_MANY_PATH = EXPECTED_RESULTS_FOLDER + "not-exp-delete-many-to-many.json";
        final String NOT_EXP_DELETE_CMHANDLE_PATH = EXPECTED_RESULTS_FOLDER + "not-exp-source-entity-delete-cm-handle.json";

        validateReceivedCloudEventMetrics(0, 0, 0, 0);
        sendEventFromFile(CREATE_ONE_TO_ONE_PATH);
        validateWithTimeout(999999, () -> {
            EndToEndExpectedResults expected = getExpectedResults(EXP_CREATE_ONE_TO_ONE_PATH);
            assertDbContainsExpectedValues(expected);
            validateReceivedCloudEventMetrics(1, 0, 0, 0);
        });

        sendEventFromFile(CREATE_MANY_TO_MANY_PATH);
        validateWithTimeout(20, () -> {
            EndToEndExpectedResults expected = getExpectedResults(EXP_CREATE_MANY_TO_MANY_PATH);
            assertDbContainsExpectedValues(expected);
            validateReceivedCloudEventMetrics(2, 0, 0, 0);
        });

        sendEventFromFile(MERGE_ONE_TO_MANY_PATH);

        validateWithTimeout(20, () -> {
            EndToEndExpectedResults expected = getExpectedResults(EXP_MERGE_ONE_TO_MANY_PATH);
            assertDbContainsExpectedValues(expected);
            validateReceivedCloudEventMetrics(2, 1, 0, 0);
        });

        sendEventFromFile(CREATE_SECOND_CASE_PATH);
        validateWithTimeout(20, () -> {
            EndToEndExpectedResults expected = getExpectedResults(EXP_CREATE_SECOND_CASE_PATH);
            assertDbContainsExpectedValues(expected);
            validateReceivedCloudEventMetrics(3, 1, 0, 0);
        });

        sendEventFromFile(DELETE_MANY_TO_MANY_PATH);
        validateWithTimeout(20, () -> {
            EndToEndExpectedResults notExpected = getExpectedResults(NOT_EXP_DELETE_MANY_TO_MANY_PATH);
            assertDbNotContainsExpectedValues(notExpected);
            validateReceivedCloudEventMetrics(3, 1, 1, 0);
        });

        sendEventFromFile(DELETE_ONE_TO_ONE_PATH);
        validateWithTimeout(20, () -> {
            EndToEndExpectedResults expected = getExpectedResults(EXP_DELETE_ONE_TO_ONE_PATH);
            assertDbContainsExpectedValues(expected);
            EndToEndExpectedResults notExpected = getExpectedResults(NOT_EXP_DELETE_ONE_TO_ONE_PATH);
            assertDbNotContainsExpectedValues(notExpected);
            validateReceivedCloudEventMetrics(3, 1, 2, 0);
        });

        sendEventFromFile(DELETE_CMHANDLE_PATH);
        validateWithTimeout(20, () -> {
            EndToEndExpectedResults notExpected = getExpectedResults(NOT_EXP_DELETE_CMHANDLE_PATH);
            assertDbNotContainsExpectedValues(notExpected);
            validateReceivedCloudEventMetrics(3, 1, 2, 1);
        });
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
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
        validateWithTimeout(10, () -> {
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
                attributes -> assertDatabaseContainsValues(tableName, attributes)));
    }

    private void assertDbNotContainsExpectedValues(EndToEndExpectedResults expectedResults) {
        expectedResults.getTables().forEach(tableName -> expectedResults.getTableEntryIds(tableName).forEach(
                ids -> assertDatabaseDoesNotContainRecord(tableName, ids)));
    }

    private void assertDatabaseContainsValues(final String table, final Map<String, Object> attributes) {
        Result<Record> results = TiesDbServiceContainerizedTest.selectAllRowsFromTable(writeDataDslContext,
                "ties_data.\"" + table + "\"");
        boolean containsExpectedData = results.stream().anyMatch(row -> attributes.entrySet().stream().allMatch(attr -> {
            if (attr.getValue() != null) {
                return Objects.equals(attr.getValue().toString(), row.get(attr.getKey()).toString());
            }
            return row.get(attr.getKey()) == null;
        }));
        assertTrue(containsExpectedData, String.format(
                "Database table \"%s\" does not contain expected data, but it should.", table));
    }

    private void assertDatabaseDoesNotContainRecord(final String table, final String id) {
        Result<Record> results = TiesDbServiceContainerizedTest.selectAllRowsFromTable(writeDataDslContext,
                "ties_data.\"" + table + "\"");
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
