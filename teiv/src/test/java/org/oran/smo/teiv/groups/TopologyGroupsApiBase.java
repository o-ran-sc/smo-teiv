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
package org.oran.smo.teiv.groups;

import static org.oran.smo.teiv.utils.TeivConstants.TEIV_CONSUMER_DATA_SCHEMA;
import static org.oran.smo.teiv.utils.TeivConstants.TEIV_DATA_SCHEMA;
import static org.oran.smo.teiv.utils.TeivConstants.TEIV_MODEL_SCHEMA;

import java.util.List;

import javax.sql.DataSource;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.springframework.context.ApplicationContext;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.oran.smo.teiv.exposure.audit.LoggerHandler;
import org.oran.smo.teiv.groups.rest.controller.GroupCreationRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.oran.smo.teiv.db.TestPostgresqlContainer;
import org.oran.smo.teiv.TopologyApiBase;
import org.oran.smo.teiv.schema.PostgresSchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoaderException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles({ "test", "groups" })
public abstract class TopologyGroupsApiBase extends TopologyApiBase {
    public static final String TEIV_GROUPS = "teiv_groups";

    @Autowired
    private ApplicationContext context;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LoggerHandler loggerHandler;

    @BeforeAll
    public static void beforeAll() throws UnsupportedOperationException, SchemaLoaderException {
        String url = postgresSQLContainer.getJdbcUrl();
        DataSource ds = DataSourceBuilder.create().url(url).username("test").password("test").build();
        DSLContext dslContext = DSL.using(ds, SQLDialect.POSTGRES);
        TestPostgresqlContainer.truncateSchemas(List.of(TEIV_DATA_SCHEMA, TEIV_CONSUMER_DATA_SCHEMA, TEIV_MODEL_SCHEMA,
                TEIV_GROUPS), dslContext);
        TestPostgresqlContainer.loadModels();
        TestPostgresqlContainer.loadSampleGroupsData();
        PostgresSchemaLoader postgresSchemaLoader = new PostgresSchemaLoader(dslContext, new ObjectMapper());
        postgresSchemaLoader.loadSchemaRegistry();
    }

    @BeforeEach
    public void setup() {
        GroupCreationRequestFilter groupCreationRequestFilter = new GroupCreationRequestFilter(loggerHandler, objectMapper);
        mockMvc = MockMvcBuilders.webAppContextSetup((WebApplicationContext) context).addFilter(groupCreationRequestFilter,
                "/topology-inventory/v1/groups").build();

        RestAssuredMockMvc.mockMvc(mockMvc);
    }
}
