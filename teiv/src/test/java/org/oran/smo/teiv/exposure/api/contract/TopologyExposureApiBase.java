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
package org.oran.smo.teiv.exposure.api.contract;

import javax.sql.DataSource;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.oran.smo.teiv.CustomMetrics;
import org.oran.smo.teiv.TopologyApiBase;
import org.oran.smo.teiv.db.TestPostgresqlContainer;
import org.oran.smo.teiv.exception.YangException;
import org.oran.smo.teiv.exposure.audit.LoggerHandler;
import org.oran.smo.teiv.exposure.classifiers.rest.controller.ClassifiersRequestFilter;
import org.oran.smo.teiv.exposure.decorators.rest.controller.DecoratorsRequestFilter;
import org.oran.smo.teiv.service.kafka.KafkaTopicService;
import org.oran.smo.teiv.startup.SchemaCleanUpHandler;
import org.oran.smo.teiv.utils.yangparser.ExposureYangParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.oran.smo.teiv.schema.PostgresSchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoaderException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.oran.smo.teiv.utils.TeivConstants.*;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles({ "test", "exposure" })
public abstract class TopologyExposureApiBase extends TopologyApiBase {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private CustomMetrics customMetrics;
    @Autowired
    private LoggerHandler loggerHandler;
    @Autowired
    private ObjectMapper objectMapper;

    // This is required so that "Schema in deleting state" contract test works from 03_postSchemas.groovy
    @MockitoBean
    private SchemaCleanUpHandler schemaCleanUpHandler;
    @MockitoBean
    KafkaTopicService kafkaTopicService;

    @BeforeAll
    public static void beforeAll() throws SchemaLoaderException, YangException {
        String url = postgresSQLContainer.getJdbcUrl();
        DataSource ds = DataSourceBuilder.create().url(url).username("test").password("test").build();
        DSLContext dslContext = DSL.using(ds, SQLDialect.POSTGRES);
        TestPostgresqlContainer.truncateSchemas(List.of(TEIV_DATA_SCHEMA, TEIV_CONSUMER_DATA_SCHEMA, TEIV_MODEL_SCHEMA),
                dslContext);
        TestPostgresqlContainer.loadModels();
        TestPostgresqlContainer.loadSampleData();
        PostgresSchemaLoader postgresSchemaLoader = new PostgresSchemaLoader(dslContext, new ObjectMapper());
        postgresSchemaLoader.loadSchemaRegistry();
        ExposureYangParser.loadAndValidateModels();
    }

    @BeforeEach
    public void setup() {
        ClassifiersRequestFilter classifiersRequestFilter = new ClassifiersRequestFilter(loggerHandler, objectMapper,
                customMetrics);
        DecoratorsRequestFilter decoratorsRequestFilter = new DecoratorsRequestFilter(loggerHandler, objectMapper,
                customMetrics);
        mockMvc = MockMvcBuilders.webAppContextSetup((WebApplicationContext) context).addFilter(classifiersRequestFilter,
                REQUEST_MAPPING + "/classifiers").addFilter(decoratorsRequestFilter, REQUEST_MAPPING + "/decorators")
                .build();

        RestAssuredMockMvc.mockMvc(mockMvc);

    }
}
