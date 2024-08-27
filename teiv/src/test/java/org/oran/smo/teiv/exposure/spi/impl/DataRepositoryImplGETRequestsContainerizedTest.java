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
package org.oran.smo.teiv.exposure.spi.impl;

import static org.oran.smo.teiv.utils.TiesConstants.MODULE_REFERENCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.oran.smo.teiv.utils.TiesConstants.TIES_CONSUMER_DATA;
import static org.oran.smo.teiv.utils.TiesConstants.TIES_DATA_SCHEMA;
import static org.oran.smo.teiv.utils.TiesConstants.TIES_MODEL;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

import org.oran.smo.teiv.db.TestPostgresqlContainer;
import org.oran.smo.teiv.exposure.spi.ModelRepository;
import org.oran.smo.teiv.exposure.spi.Module;
import org.oran.smo.teiv.exposure.spi.ModuleStatus;
import org.oran.smo.teiv.exposure.tiespath.refiner.BasePathRefinement;
import org.oran.smo.teiv.schema.PostgresSchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoaderException;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.startup.SchemaHandler;

@Configuration
@SpringBootTest
class DataRepositoryImplGETRequestsContainerizedTest {
    public static TestPostgresqlContainer postgreSQLContainer = TestPostgresqlContainer.getInstance();
    private static DataRepositoryImpl underTest;
    private static ModelRepository modelRepository;
    private static BasePathRefinement basePathRefinement;
    private static DSLContext readWriteDataDslContext;
    private static DSLContext readDataDslContext;
    private static DSLContext writeDataDslContext;

    @MockBean
    private SchemaHandler schemaHandler;

    @BeforeAll
    public static void beforeAll() throws UnsupportedOperationException, SchemaLoaderException {
        String url = postgreSQLContainer.getJdbcUrl();
        DataSource ds = DataSourceBuilder.create().url(url).username("test").password("test").build();
        readDataDslContext = DSL.using(ds, SQLDialect.POSTGRES);
        writeDataDslContext = DSL.using(ds, SQLDialect.POSTGRES);
        underTest = new DataRepositoryImpl(basePathRefinement, readDataDslContext);
        modelRepository = new ModelRepositoryImpl(readDataDslContext, readWriteDataDslContext, writeDataDslContext);
        PostgresSchemaLoader postgresSchemaLoader = new PostgresSchemaLoader(readDataDslContext, new ObjectMapper());
        postgresSchemaLoader.loadSchemaRegistry();
        TestPostgresqlContainer.loadSampleData();
    }

    @BeforeEach
    public void deleteAll() {
        writeDataDslContext.meta().filterSchemas(s -> s.getName().equals(TIES_DATA_SCHEMA)).getTables().forEach(
                t -> writeDataDslContext.truncate(t).cascade().execute());
        TestPostgresqlContainer.loadSampleData();
    }

    //TODO: Make this test class generic for all repositories
    @Test
    void testSchemaCRUD() {
        final String moduleName = "newSchema";
        Optional<Module> schemaByName = modelRepository.getConsumerModuleByName(moduleName);
        Assertions.assertFalse(schemaByName.isPresent());

        Module schema = Module.builder().name(moduleName).namespace("new-namespace").domain("NEW_DOMAIN").content(
                "yang content {} \n\n \t\t\t;").ownerAppId("APP").status(ModuleStatus.IN_USAGE).revision("2024-07-15")
                .build();

        modelRepository.createModule(schema);

        schemaByName = modelRepository.getConsumerModuleByName(moduleName);
        Assertions.assertTrue(schemaByName.isPresent());

        modelRepository.doesModuleExists(TIES_MODEL, moduleName);
        modelRepository.doesModuleExists(TIES_CONSUMER_DATA, moduleName);

        modelRepository.updateModuleStatus(moduleName, ModuleStatus.DELETING);

        final @NotNull Record1<Object> status = readDataDslContext.select(field("status")).from(table(String.format(
                TIES_CONSUMER_DATA, MODULE_REFERENCE))).where(field("name").eq(moduleName)).fetchAny();

        Assertions.assertEquals(ModuleStatus.DELETING.name(), status.get("status"));

        modelRepository.deleteModuleByName(moduleName);

        schemaByName = modelRepository.getConsumerModuleByName(moduleName);
        Assertions.assertFalse(schemaByName.isPresent());
    }

    @Test
    void getClassifiersForSchema() {
        assertThat(List.of("test-app-module:Indoor", "test-app-module:Outdoor", "test-app-module:Rural",
                "test-app-module:Weekday", "test-app-module:Weekend")).hasSameElementsAs(underTest.getClassifiersForSchema(
                        "test-app-module"));
    }

    @Test
    void getDecoratorsForSchema() {
        assertThat(List.of("test-app-module:textdata", "test-app-module:intdata")).hasSameElementsAs(underTest
                .getDecoratorsForSchema("test-app-module"));
    }

    @Test
    void getRelationshipIdsForDecoratorDeletionTest() {
        Assertions.assertEquals(Collections.singletonList(
                "urn:base64:R05CQ1VVUEZ1bmN0aW9uOkJGRUVBQzJDRTYwMjczQ0IwQTc4MzE5Q0MyMDFBN0ZFOlJFQUxJU0VEX0JZOkNsb3VkTmF0aXZlQXBwbGljYXRpb246QUQ0MkQ5MDQ5N0U5M0QyNzYyMTVERjZEM0I4OTlFMTc="),
                underTest.getRelationshipIdsForDecoratorDeletion(SchemaRegistry.getRelationTypeByName(
                        "GNBCUUPFUNCTION_REALISED_BY_CLOUDNATIVEAPPLICATION"), Set.of("gnbcucp-gnbcuup-model:metadata")));

        Assertions.assertEquals(Collections.singletonList(
                "urn:base64:TWFuYWdlZEVsZW1lbnQ6RTY0MzcxQ0Q0RDEyRUQwQ0VEMjAwREQzQTc1OTE3ODQ6TUFOQUdFUzpHTkJDVVVQRnVuY3Rpb246QkZFRUFDMkNFNjAyNzNDQjBBNzgzMTlDQzIwMUE3RkU="),
                underTest.getRelationshipIdsForDecoratorDeletion(SchemaRegistry.getRelationTypeByName(
                        "MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION"), Set.of("gnbcucp-gnbcuup-model:metadata")));
    }

    @Test
    void getRelationshipIdsForClassifierDeletionTest() {
        Assertions.assertEquals(Collections.singletonList(
                "urn:base64:R05CQ1VVUEZ1bmN0aW9uOkJGRUVBQzJDRTYwMjczQ0IwQTc4MzE5Q0MyMDFBN0ZFOlJFQUxJU0VEX0JZOkNsb3VkTmF0aXZlQXBwbGljYXRpb246QUQ0MkQ5MDQ5N0U5M0QyNzYyMTVERjZEM0I4OTlFMTc="),
                underTest.getRelationshipIdsForClassifierDeletion(SchemaRegistry.getRelationTypeByName(
                        "GNBCUUPFUNCTION_REALISED_BY_CLOUDNATIVEAPPLICATION"), Set.of("gnbcucp-gnbcuup-model:Weekend")));

        Assertions.assertEquals(Collections.singletonList(
                "urn:base64:TWFuYWdlZEVsZW1lbnQ6RTY0MzcxQ0Q0RDEyRUQwQ0VEMjAwREQzQTc1OTE3ODQ6TUFOQUdFUzpHTkJDVVVQRnVuY3Rpb246QkZFRUFDMkNFNjAyNzNDQjBBNzgzMTlDQzIwMUE3RkU="),
                underTest.getRelationshipIdsForClassifierDeletion(SchemaRegistry.getRelationTypeByName(
                        "MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION"), Set.of("gnbcucp-gnbcuup-model:Weekend")));
    }

    @Test
    void getEntityIdsForDecoratorDeletionTest() {
        Assertions.assertEquals(Collections.singletonList("E49D942C16E0364E1E0788138916D70C"), underTest
                .getEntityIdsForDecoratorDeletion(SchemaRegistry.getEntityTypeByName("NRSectorCarrier"), Set.of(
                        "gnbcucp-gnbcuup-model:metadata")));
    }

    @Test
    void getEntityIdsForClassifierDeletionTest() {
        Assertions.assertEquals(Collections.singletonList("E49D942C16E0364E1E0788138916D70C"), underTest
                .getEntityIdsForClassifierDeletion(SchemaRegistry.getEntityTypeByName("NRSectorCarrier"), Set.of(
                        "gnbcucp-gnbcuup-model:Weekend")));
    }

}
