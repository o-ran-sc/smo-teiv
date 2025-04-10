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
package org.oran.smo.teiv.exposure.spi.impl;

import static org.oran.smo.teiv.utils.TeivConstants.MODULE_REFERENCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.oran.smo.teiv.utils.TeivConstants.TEIV_CONSUMER_DATA;
import static org.oran.smo.teiv.utils.TeivConstants.TEIV_CONSUMER_DATA_SCHEMA;
import static org.oran.smo.teiv.utils.TeivConstants.TEIV_DATA_SCHEMA;
import static org.oran.smo.teiv.utils.TeivConstants.TEIV_MODEL;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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
import org.oran.smo.teiv.schema.EntityType;
import org.oran.smo.teiv.schema.RelationType;
import org.oran.smo.teiv.schema.SchemaRegistryException;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;

import org.oran.smo.teiv.db.TestPostgresqlContainer;
import org.oran.smo.teiv.exposure.spi.ModelRepository;
import org.oran.smo.teiv.exposure.spi.Module;
import org.oran.smo.teiv.exposure.spi.ModuleStatus;
import org.oran.smo.teiv.exposure.teivpath.refiner.BasePathRefinement;
import org.oran.smo.teiv.schema.PostgresSchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoaderException;
import org.oran.smo.teiv.schema.SchemaRegistry;

@Configuration
class DataRepositoryImplGETRequestsContainerizedTest {
    private static TestPostgresqlContainer postgreSQLContainer = TestPostgresqlContainer.getInstance();
    private static DataRepositoryImpl underTest;
    private static ModelRepository modelRepository;
    private static BasePathRefinement basePathRefinement;
    private static DSLContext readWriteDataDslContext;
    private static DSLContext readDataDslContext;
    private static DSLContext writeDataDslContext;

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
    }

    @BeforeEach
    public void deleteAll() {
        TestPostgresqlContainer.truncateSchemas(List.of(TEIV_DATA_SCHEMA, TEIV_CONSUMER_DATA_SCHEMA), writeDataDslContext);
        TestPostgresqlContainer.loadSampleData();
    }

    //TODO: Make this test class generic for all repositories
    @Test
    void testSchemaCRUD() {
        final String moduleName = "newSchema";
        Optional<Module> schemaByName = modelRepository.getConsumerModuleByName(moduleName);
        Assertions.assertFalse(schemaByName.isPresent());

        Module schema = Module.builder().name(moduleName).namespace("new-namespace").domain("NEW_DOMAIN").content(
                "yang content {} \n\n \t\t\t;").ownerAppId("APP").revision("2024-07-15").status(ModuleStatus.IN_USAGE)
                .revision("2024-10-04").build();
        modelRepository.createConsumerDataModule(schema, List.of(), Map.of());
        schemaByName = modelRepository.getConsumerModuleByName(moduleName);
        Assertions.assertTrue(schemaByName.isPresent());

        modelRepository.doesModuleExists(TEIV_MODEL, moduleName);
        modelRepository.doesModuleExists(TEIV_CONSUMER_DATA, moduleName);

        modelRepository.updateModuleStatus(moduleName, ModuleStatus.DELETING);

        final @NotNull Record1<Object> status = readDataDslContext.select(field("status")).from(table(String.format(
                TEIV_CONSUMER_DATA, MODULE_REFERENCE))).where(field("name").eq(moduleName)).fetchAny();

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
    void getRelationshipIdsForDecoratorDeletionTest() throws SchemaRegistryException {
        Assertions.assertEquals(Collections.singletonList(
                "urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_SERVES_ANTENNACAPABILITY=ABD52B030DF1169F9F41C898913EF30F7BB5741F53352F482310B280C90AC569B7D31D52A2BB41F1F0099AE1EDD56CACF0B285D145A5584D376DD45DED1E2D65"),
                underTest.getRelationshipIdsForDecoratorDeletion(SchemaRegistry.getRelationTypeByModuleAndName(
                        "o-ran-smo-teiv-rel-equipment-ran", "ANTENNAMODULE_SERVES_ANTENNACAPABILITY"), Set.of(
                                "ocucp-ocuup-model:metadata")));

        Assertions.assertEquals(Collections.singletonList(
                "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_OCUUPFUNCTION=5255F37093F8EB3763CE5F017DFC1E162B44FC9DF6E13744C04DC1832C5E754AB7BE440DBE1187EE8EEE42FD04E652BB8148655C6F977B1FFDDA54FE87C6411A"),
                underTest.getRelationshipIdsForDecoratorDeletion(SchemaRegistry.getRelationTypeByModuleAndName(
                        "o-ran-smo-teiv-rel-oam-ran", "MANAGEDELEMENT_MANAGES_OCUUPFUNCTION"), Set.of(
                                "ocucp-ocuup-model:metadata")));
    }

    @Test
    void getRelationshipIdsForClassifierDeletionTest() throws SchemaRegistryException {
        Assertions.assertEquals(Collections.singletonList(
                "urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_SERVES_ANTENNACAPABILITY=ABD52B030DF1169F9F41C898913EF30F7BB5741F53352F482310B280C90AC569B7D31D52A2BB41F1F0099AE1EDD56CACF0B285D145A5584D376DD45DED1E2D65"),
                underTest.getRelationshipIdsForClassifierDeletion(SchemaRegistry.getRelationTypeByModuleAndName(
                        "o-ran-smo-teiv-rel-equipment-ran", "ANTENNAMODULE_SERVES_ANTENNACAPABILITY"), Set.of(
                                "ocucp-ocuup-model:Weekend")));

        Assertions.assertEquals(Collections.singletonList(
                "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_OCUUPFUNCTION=5255F37093F8EB3763CE5F017DFC1E162B44FC9DF6E13744C04DC1832C5E754AB7BE440DBE1187EE8EEE42FD04E652BB8148655C6F977B1FFDDA54FE87C6411A"),
                underTest.getRelationshipIdsForClassifierDeletion(SchemaRegistry.getRelationTypeByModuleAndName(
                        "o-ran-smo-teiv-rel-oam-ran", "MANAGEDELEMENT_MANAGES_OCUUPFUNCTION"), Set.of(
                                "ocucp-ocuup-model:Weekend")));
    }

    @Test
    void getEntityIdsForDecoratorDeletionTest() throws SchemaRegistryException {
        Assertions.assertEquals(Collections.singletonList(
                "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRSectorCarrier=1"),
                underTest.getEntityIdsForDecoratorDeletion(SchemaRegistry.getEntityTypeByModuleAndName("o-ran-smo-teiv-ran",
                        "NRSectorCarrier"), Set.of("ocucp-ocuup-model:metadata")));
    }

    @Test
    void getEntityIdsForClassifierDeletionTest() throws SchemaRegistryException {
        Assertions.assertEquals(Collections.singletonList(
                "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRSectorCarrier=1"),
                underTest.getEntityIdsForClassifierDeletion(SchemaRegistry.getEntityTypeByModuleAndName(
                        "o-ran-smo-teiv-ran", "NRSectorCarrier"), Set.of("ocucp-ocuup-model:Weekend")));
    }

    @Test
    void doesTopologyExist_entityTest() throws SchemaRegistryException {
        EntityType entityType = SchemaRegistry.getEntityTypeByDomainAndName("RAN", "NRCellDU");
        String entityId = "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=91";
        Assertions.assertTrue(underTest.doesTopologyExist(entityType, entityId),
                "That's strange, the result should have been true as this id in DB dataset: " + entityId);
        String nonExistentEntityId = "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=911";
        Assertions.assertFalse(underTest.doesTopologyExist(entityType, nonExistentEntityId),
                "Result should have been false as the entity id is not in DB data set: " + nonExistentEntityId);
    }

    @Test
    void doesTopologyExist_relationshipTest() throws SchemaRegistryException {
        RelationType relationType = SchemaRegistry.getRelationTypeByDomainAndName("RAN",
                "NRSECTORCARRIER_USES_ANTENNACAPABILITY");
        String aSideRelationshipId = "urn:o-ran:smo:teiv:sha512:NRSECTORCARRIER_USES_ANTENNACAPABILITY=11EDFC31E2BE240D3CB15DB1A3FA3B78C828524BC8FCA3365A615129A61A627C21DA8EBF6DD788CDBDEC668344D1F79A371749083D6AE04DDDD57CB4FA8C3ECB";
        Assertions.assertTrue(underTest.doesTopologyExist(relationType, aSideRelationshipId),
                "That's strange, the result should have been true as this id in DB dataset: " + aSideRelationshipId);

        relationType = SchemaRegistry.getRelationTypeByDomainAndName("REL_OAM_RAN", "MANAGEDELEMENT_MANAGES_OCUUPFUNCTION");
        String bSideRelationshipId = "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_OCUUPFUNCTION=5255F37093F8EB3763CE5F017DFC1E162B44FC9DF6E13744C04DC1832C5E754AB7BE440DBE1187EE8EEE42FD04E652BB8148655C6F977B1FFDDA54FE87C6411A";
        Assertions.assertTrue(underTest.doesTopologyExist(relationType, bSideRelationshipId),
                "That's strange, the result should have been true as this id in DB dataset: " + bSideRelationshipId);

        relationType = SchemaRegistry.getRelationTypeByDomainAndName("REL_EQUIPMENT_RAN",
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY");
        String manyToManyRelationshipId = "urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_SERVES_ANTENNACAPABILITY=ABD52B030DF1169F9F41C898913EF30F7BB5741F53352F482310B280C90AC569B7D31D52A2BB41F1F0099AE1EDD56CACF0B285D145A5584D376DD45DED1E2D65";
        Assertions.assertTrue(underTest.doesTopologyExist(relationType, manyToManyRelationshipId),
                "That's strange, the result should have been true as this id in DB dataset: " + manyToManyRelationshipId);

        String nonExistentRelationshipId = "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_OCUUPFUNCTION=5255F37093F8EB3763CE5F017DFC1E162B44FC9DF6E13744C04DC1832C5E754AB7BE440DBE1187EE8EEE42FD04E652BB8148655C6F977B1FFDDA54FE87C6411A";
        Assertions.assertFalse(underTest.doesTopologyExist(relationType, nonExistentRelationshipId),
                "Result should have been false as the relationship id is not in DB data set: " + nonExistentRelationshipId);
    }

}
