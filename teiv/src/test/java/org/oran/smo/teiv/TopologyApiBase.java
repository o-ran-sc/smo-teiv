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
package org.oran.smo.teiv;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

import org.oran.smo.teiv.db.TestPostgresqlContainer;
import org.oran.smo.teiv.startup.SchemaHandler;

@AutoConfigureMockMvc
public abstract class TopologyApiBase {
    public static TestPostgresqlContainer postgresSQLContainer = TestPostgresqlContainer.getInstance();

    @MockBean
    private SchemaHandler schemaHandler;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.read.jdbc-url", () -> postgresSQLContainer.getJdbcUrl());
        registry.add("spring.datasource.read.username", () -> postgresSQLContainer.getUsername());
        registry.add("spring.datasource.read.password", () -> postgresSQLContainer.getPassword());

        registry.add("spring.datasource.write.jdbc-url", () -> postgresSQLContainer.getJdbcUrl());
        registry.add("spring.datasource.write.username", () -> postgresSQLContainer.getUsername());
        registry.add("spring.datasource.write.password", () -> postgresSQLContainer.getPassword());
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup((WebApplicationContext) context).addFilters().build();

        RestAssuredMockMvc.mockMvc(mockMvc);
    }
}
