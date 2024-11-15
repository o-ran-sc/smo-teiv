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
package org.oran.smo.teiv.schema;

import static org.oran.smo.teiv.exposure.teivpath.refiner.AliasMapper.hashAlias;
import static org.oran.smo.teiv.schema.RelationshipDataLocation.A_SIDE;
import static org.oran.smo.teiv.schema.RelationshipDataLocation.B_SIDE;
import static org.oran.smo.teiv.schema.RelationshipDataLocation.RELATION;
import static org.oran.smo.teiv.schema.SchemaRegistryErrorCode.ENTITY_NOT_FOUND_IN_DOMAIN;
import static org.oran.smo.teiv.schema.SchemaRegistryErrorCode.RELATIONSHIP_NOT_FOUND_IN_DOMAIN;
import static org.oran.smo.teiv.schema.SchemaRegistryErrorCode.RELATIONSHIP_NOT_FOUND_IN_MODULE;
import static org.oran.smo.teiv.utils.TeivConstants.QUOTED_STRING;
import static org.oran.smo.teiv.utils.TeivConstants.TEIV_DOMAIN;
import static org.oran.smo.teiv.utils.TeivConstants.TEIV_DATA;
import static org.jooq.impl.DSL.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jooq.JSONB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.exposure.spi.Module;

class SchemaRegistryTest {

    @BeforeAll
    public static void beforeAll() throws UnsupportedOperationException, SchemaLoaderException {
        MockSchemaLoader mockSchemaLoader = new MockSchemaLoader();
        mockSchemaLoader.loadSchemaRegistry();
    }

    @Test
    void testGetModulesName() {
        //given
        Set<String> expectedModuleNames = Set.of("o-ran-smo-teiv-oam", "o-ran-smo-teiv-ran", "o-ran-smo-teiv-equipment",
                "o-ran-smo-teiv-rel-oam-ran", "o-ran-smo-teiv-rel-equipment-ran", TEIV_DOMAIN,
                "_3gpp-common-yang-extensions", "_3gpp-common-yang-types", "ietf-geo-location", "ietf-inet-types",
                "ietf-yang-types", "o-ran-smo-teiv-common-yang-extensions", "o-ran-smo-teiv-common-yang-types",
                "o-ran-smo-teiv-rel-oam-cloud", "o-ran-smo-teiv-cloud", "o-ran-smo-teiv-rel-cloud-ran");
        //when
        Set<String> moduleNames = SchemaRegistry.getModuleRegistry().keySet();
        //then
        assertEquals(expectedModuleNames.size(), moduleNames.size());
        assertEquals(expectedModuleNames, moduleNames);
    }

    @Test
    void testGetModulesByName() {
        //given
        String expectedName = "o-ran-smo-teiv-rel-oam-ran";
        String expectedNamespace = "urn:o-ran:smo-teiv-rel-oam-ran";
        String expectedDomain = "REL_OAM_RAN";
        List<String> expectedIncludedModules = List.of("o-ran-smo-teiv-oam", "o-ran-smo-teiv-ran");
        //when
        Module oamToRanModule = SchemaRegistry.getModuleByName("o-ran-smo-teiv-rel-oam-ran");
        //then
        assertEquals(expectedName, oamToRanModule.getName());
        assertEquals(expectedNamespace, oamToRanModule.getNamespace());
        assertEquals(expectedDomain, oamToRanModule.getDomain());
        assertEquals(expectedIncludedModules, oamToRanModule.getIncludedModuleNames());

        assertThrows(TeivException.class, () -> SchemaRegistry.getModuleByName("invalid-module"));
    }

    @Test
    void testGetAllDomainsIncludingRootDomain() {
        //given
        Set<String> expectedDomains = Set.of(TEIV_DOMAIN, "EQUIPMENT", "REL_EQUIPMENT_RAN", "OAM", "REL_OAM_RAN", "RAN",
                "CLOUD", "REL_CLOUD_RAN", "REL_OAM_CLOUD");
        //when
        Set<String> actualDomains = SchemaRegistry.getDomains();
        //then
        assertEquals(expectedDomains, actualDomains);
    }

    @Test
    void testRootDomainIncludesAllAvailableDomains() {
        //given
        Set<String> availableDomains = SchemaRegistry.getDomains();
        availableDomains.remove(TEIV_DOMAIN);
        //when
        List<String> rootIncludedDomains = SchemaRegistry.getIncludedDomains(TEIV_DOMAIN);
        //then
        assertEquals(availableDomains.size(), rootIncludedDomains.size());
        assertTrue(availableDomains.containsAll(rootIncludedDomains));
    }

    @Test
    void testGetModuleByDomainThrowsUnknownDomainException() {
        assertThrows(TeivException.class, () -> SchemaRegistry.getModuleByDomain("throwError"));
    }

    //Entities

    @Test
    void testGetEntityTypeByDomainAndName() throws SchemaRegistryException {
        //when
        List<EntityType> entityTypes = SchemaRegistry.getEntityTypeByDomainAndName("RAN", "ODUFunction");
        //then
        assertEquals(1, entityTypes.size());
        EntityType entityType = entityTypes.get(0);
        assertEquals("ODUFunction", entityType.getName());
        assertEquals("RAN", entityType.getModule().getDomain());

        final SchemaRegistryException exception = assertThrows(SchemaRegistryException.class, () -> SchemaRegistry
                .getEntityTypeByDomainAndName("OAM", "ODUFunction"));
        assertEquals(ENTITY_NOT_FOUND_IN_DOMAIN, exception.getErrorCode());
    }

    @Test
    void testGetEntityTypeByModuleAndName() throws SchemaRegistryException {
        //when
        EntityType entityType = SchemaRegistry.getEntityTypeByModuleAndName("o-ran-smo-teiv-ran", "ODUFunction");
        //then
        assertEquals("ODUFunction", entityType.getName());
        assertEquals("RAN", entityType.getModule().getDomain());

        final SchemaRegistryException exception = assertThrows(SchemaRegistryException.class, () -> SchemaRegistry
                .getEntityTypeByDomainAndName("o-ran-smo-teiv-oam", "ODUFunction"));
        assertEquals(ENTITY_NOT_FOUND_IN_DOMAIN, exception.getErrorCode());
    }

    @Test
    void testGetEntityNames() {
        //given
        List<String> expectedEntityName = List.of("AntennaCapability", "AntennaModule", "ORUFunction", "OCUCPFunction",
                "OCUUPFunction", "ODUFunction", "ManagedElement", "NRCellCU", "CloudifiedNF", "NRCellDU", "NRSectorCarrier",
                "SMO", "Sector", "Site", "NearRTRICFunction", "NFDeployment", "OCloudNamespace", "NodeCluster",
                "OCloudSite");
        //when
        List<String> actualEntityNames = SchemaRegistry.getEntityNames();
        //then
        assertEquals(expectedEntityName.size(), actualEntityNames.size());
        assertTrue(actualEntityNames.containsAll(expectedEntityName));
    }

    @Test
    void testGetTableNameForEntity() throws SchemaRegistryException {
        //given
        EntityType oduFunction = SchemaRegistry.getEntityTypeByModuleAndName("o-ran-smo-teiv-ran", "ODUFunction");
        //then
        assertEquals("teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\"", oduFunction.getTableName());
    }

    @Test
    void testGetClassifiersColumnNameForEntity() throws SchemaRegistryException {
        //given
        EntityType oduFunction = SchemaRegistry.getEntityTypeByModuleAndName("o-ran-smo-teiv-ran", "ODUFunction");
        //then
        assertEquals("CD_classifiers", oduFunction.getClassifiersColumnName());
    }

    @Test
    void testGetDecoratorsColumnNameForEntity() throws SchemaRegistryException {
        //given
        EntityType oduFunction = SchemaRegistry.getEntityTypeByModuleAndName("o-ran-smo-teiv-ran", "ODUFunction");
        //then
        assertEquals("CD_decorators", oduFunction.getDecoratorsColumnName());
    }

    @Test
    void testGetFieldsForEntity() throws SchemaRegistryException {
        //given
        EntityType OduFunction = SchemaRegistry.getEntityTypeByModuleAndName("o-ran-smo-teiv-ran", "ODUFunction");
        //then
        assertEquals(Set.of(field("dUpLMNId", JSONB.class).as("o-ran-smo-teiv-ran:ODUFunction.attr.dUpLMNId"), field(
                "gNBDUId").as("o-ran-smo-teiv-ran:ODUFunction.attr.gNBDUId"), field("gNBId").as(
                        "o-ran-smo-teiv-ran:ODUFunction.attr.gNBId"), field("gNBIdLength").as(
                                "o-ran-smo-teiv-ran:ODUFunction.attr.gNBIdLength"), field("id").as(
                                        "o-ran-smo-teiv-ran:ODUFunction.id"), field("CD_sourceIds").as(
                                                "o-ran-smo-teiv-ran:ODUFunction.sourceIds"), field("CD_classifiers").as(
                                                        "o-ran-smo-teiv-ran:ODUFunction.classifiers"), field(
                                                                "CD_decorators").as(
                                                                        "o-ran-smo-teiv-ran:ODUFunction.decorators"), field(
                                                                                "metadata").as(
                                                                                        "o-ran-smo-teiv-ran:ODUFunction.metadata")),
                new HashSet<>(OduFunction.getAllFieldsWithId()));
    }

    @Test
    void testGetEntityTypesByDomain() {
        //given
        List<String> expectedEntities = List.of("AntennaCapability", "AntennaModule", "ORUFunction", "OCUCPFunction",
                "OCUUPFunction", "ODUFunction", "NRCellCU", "NRCellDU", "NRSectorCarrier", "SMO", "Sector", "Site",
                "NearRTRICFunction");
        //when
        List<String> equipmentToRanEntityTypes = SchemaRegistry.getEntityNamesByDomain("REL_EQUIPMENT_RAN");
        //then
        assertEquals(expectedEntities.size(), equipmentToRanEntityTypes.size());
        assertTrue(expectedEntities.containsAll(equipmentToRanEntityTypes));
    }

    //Relations
    @Test
    void getRelationNames() {
        List<String> expectedRelationNames = List.of("ANTENNAMODULE_SERVES_ANTENNACAPABILITY",
                "NRSECTORCARRIER_USES_ANTENNACAPABILITY", "ODUFUNCTION_PROVIDES_NRCELLDU", "NEARRTRICFUNCTION_O1LINK_SMO",
                "OCUCPFUNCTION_O1LINK_SMO", "ODUFUNCTION_E2LINK_NEARRTRICFUNCTION", "ODUFUNCTION_F1CLINK_OCUCPFUNCTION",
                "ODUFUNCTION_F1ULINK_OCUUPFUNCTION", "ODUFUNCTION_O1LINK_SMO", "ORUFUNCTION_O1LINK_SMO",
                "ORUFUNCTION_OFHCLINK_ODUFUNCTION", "ORUFUNCTION_OFHMLINK_ODUFUNCTION", "ORUFUNCTION_OFHMLINK_SMO",
                "ORUFUNCTION_OFHSLINK_ODUFUNCTION", "ORUFUNCTION_OFHULINK_ODUFUNCTION",
                "MANAGEDELEMENT_MANAGES_OCUCPFUNCTION", "MANAGEDELEMENT_MANAGES_NEARRTRICFUNCTION",
                "NRCELLDU_USES_NRSECTORCARRIER", "MANAGEDELEMENT_MANAGES_ORUFUNCTION",
                "MANAGEDELEMENT_MANAGES_OCUUPFUNCTION", "MANAGEDELEMENT_MANAGES_ODUFUNCTION", "SECTOR_GROUPS_ANTENNAMODULE",
                "ODUFUNCTION_PROVIDES_NRSECTORCARRIER", "SECTOR_GROUPS_NRCELLDU", "ANTENNAMODULE_INSTALLED_AT_SITE",
                "MANAGEDELEMENT_DEPLOYED_AS_CLOUDIFIEDNF", "NFDEPLOYMENT_SERVES_MANAGEDELEMENT",
                "NFDEPLOYMENT_SERVES_ODUFUNCTION", "OCUCPFUNCTION_PROVIDES_NRCELLCU", "NFDEPLOYMENT_SERVES_OCUCPFUNCTION",
                "NFDEPLOYMENT_SERVES_OCUUPFUNCTION", "NFDEPLOYMENT_SERVES_NEARRTRICFUNCTION",
                "NODECLUSTER_LOCATED_AT_OCLOUDSITE", "OCLOUDNAMESPACE_DEPLOYED_ON_NODECLUSTER",
                "NFDEPLOYMENT_DEPLOYED_ON_OCLOUDNAMESPACE", "CLOUDIFIEDNF_COMPRISES_NFDEPLOYMENT",
                "ANTENNAMODULE_SERVES_NRCELLDU");
        //when
        List<String> relationNames = SchemaRegistry.getRelationNames();
        //then
        assertEquals(expectedRelationNames.size(), relationNames.size());
        assertTrue(relationNames.containsAll(expectedRelationNames));
    }

    @Test
    void testGetRelationTypeByName() {
        //given
        Association expectedASideAssociation = new Association("managed-ocucpFunction", 1, 1);
        Association expectedBSideAssociation = new Association("managed-by-managedElement", 0, 9223372036854775807L);
        //when
        RelationType managedElementManagesOcucpfunction = SchemaRegistry.getRelationTypeByName(
                "MANAGEDELEMENT_MANAGES_OCUCPFUNCTION");
        //then
        assertEquals(expectedASideAssociation.toString(), managedElementManagesOcucpfunction.getASideAssociation()
                .toString());
        assertEquals(expectedBSideAssociation.toString(), managedElementManagesOcucpfunction.getBSideAssociation()
                .toString());
    }

    @Test
    void testGetRelationTypeByModuleAndName() throws SchemaRegistryException {
        //given
        Association expectedASideAssociation = new Association("managed-ocucpFunction", 1, 1);
        Association expectedBSideAssociation = new Association("managed-by-managedElement", 0, 9223372036854775807L);
        //when
        RelationType managedElementManagesOcucpfunction = SchemaRegistry.getRelationTypeByModuleAndName(
                "o-ran-smo-teiv-rel-oam-ran", "MANAGEDELEMENT_MANAGES_OCUCPFUNCTION");
        //then
        assertEquals(expectedASideAssociation.toString(), managedElementManagesOcucpfunction.getASideAssociation()
                .toString());
        assertEquals(expectedBSideAssociation.toString(), managedElementManagesOcucpfunction.getBSideAssociation()
                .toString());

        final SchemaRegistryException exception = assertThrows(SchemaRegistryException.class, () -> SchemaRegistry
                .getRelationTypeByModuleAndName("o-ran-smo-teiv-ran", "MANAGEDELEMENT_MANAGES_OCUCPFUNCTION"));
        assertEquals(RELATIONSHIP_NOT_FOUND_IN_MODULE, exception.getErrorCode());
    }

    @Test
    void testGetRelationTypeByDomainAndName() throws SchemaRegistryException {
        //given
        Association expectedASideAssociation = new Association("managed-ocucpFunction", 1, 1);
        Association expectedBSideAssociation = new Association("managed-by-managedElement", 0, 9223372036854775807L);
        //when
        List<RelationType> relationTypes = SchemaRegistry.getRelationTypeByDomainAndName("REL_OAM_RAN",
                "MANAGEDELEMENT_MANAGES_OCUCPFUNCTION");
        //then
        assertEquals(1, relationTypes.size());
        RelationType managedElementManagesOcucpfunction = relationTypes.get(0);
        assertEquals(expectedASideAssociation.toString(), managedElementManagesOcucpfunction.getASideAssociation()
                .toString());
        assertEquals(expectedBSideAssociation.toString(), managedElementManagesOcucpfunction.getBSideAssociation()
                .toString());

        final SchemaRegistryException exception = assertThrows(SchemaRegistryException.class, () -> SchemaRegistry
                .getRelationTypeByDomainAndName("OAM", "MANAGEDELEMENT_MANAGES_OCUCPFUNCTION"));
        assertEquals(RELATIONSHIP_NOT_FOUND_IN_DOMAIN, exception.getErrorCode());
    }

    @Test
    void testGetRelationTypesByEntityType() throws SchemaRegistryException {
        //given
        List<RelationType> expectedRelationsList = new ArrayList<>();
        expectedRelationsList.add(SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-rel-equipment-ran",
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY"));
        expectedRelationsList.add(SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-rel-equipment-ran",
                "SECTOR_GROUPS_ANTENNAMODULE"));
        expectedRelationsList.add(SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-equipment",
                "ANTENNAMODULE_INSTALLED_AT_SITE"));
        expectedRelationsList.add(SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-rel-equipment-ran",
                "ANTENNAMODULE_SERVES_NRCELLDU"));
        expectedRelationsList.add(SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-rel-equipment-ran",
                "ANTENNAMODULE_SERVES_NRCELLDU"));
        //when
        List<RelationType> relationTypes = SchemaRegistry.getRelationTypesByEntityName("AntennaModule");
        //then
        assertEquals(4, relationTypes.size());
        assertTrue(relationTypes.containsAll(expectedRelationsList));
    }

    @Test
    void testGetFullyQualifiedNameForRelation() throws SchemaRegistryException {
        //given
        RelationType oduFunctionRealisedByCloudnativeapplication = SchemaRegistry.getRelationTypeByModuleAndName(
                "o-ran-smo-teiv-equipment", "ANTENNAMODULE_INSTALLED_AT_SITE");
        //then
        assertEquals("o-ran-smo-teiv-equipment:ANTENNAMODULE_INSTALLED_AT_SITE", oduFunctionRealisedByCloudnativeapplication
                .getFullyQualifiedName());
    }

    @Test
    void testGetTableNameForRelation() throws SchemaRegistryException {
        //given
        String expectedManyToMany = "teiv_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\"";
        String expectedOneToMany = "teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\"";
        String expectedManyToOne = "teiv_data.\"o-ran-smo-teiv-equipment_AntennaModule\"";
        //when
        RelationType manyToMany = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-rel-equipment-ran",
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY");
        RelationType oneToMany = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-ran",
                "ODUFUNCTION_PROVIDES_NRCELLDU");
        RelationType manyToOne = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-equipment",
                "ANTENNAMODULE_INSTALLED_AT_SITE");
        //then
        assertEquals(expectedManyToMany, manyToMany.getTableName());
        assertEquals(expectedOneToMany, oneToMany.getTableName());
        assertEquals(expectedManyToOne, manyToOne.getTableName());
    }

    @Test
    void testGetRelationshipDataLocationForRelation() throws SchemaRegistryException {
        //when
        RelationType manyToMany = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-rel-equipment-ran",
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY");
        RelationType oneToMany = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-ran",
                "ODUFUNCTION_PROVIDES_NRCELLDU");
        RelationType manyToOne = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-equipment",
                "ANTENNAMODULE_INSTALLED_AT_SITE");
        //then
        assertEquals(RELATION, manyToMany.getRelationshipStorageLocation());
        assertEquals(B_SIDE, oneToMany.getRelationshipStorageLocation());
        assertEquals(A_SIDE, manyToOne.getRelationshipStorageLocation());
    }

    @Test
    void testGetClassifiersColumnNameForRelationType() throws SchemaRegistryException {
        //when
        RelationType relationType = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-ran",
                "ODUFUNCTION_PROVIDES_NRCELLDU");
        //then
        assertEquals("REL_CD_classifiers_ODUFUNCTION_PROVIDES_NRCELLDU", relationType.getClassifiersColumnName());
    }

    @Test
    void testGetDecoratorsColumnNameForRelationType() throws SchemaRegistryException {
        //when
        RelationType relationType = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-ran",
                "ODUFUNCTION_PROVIDES_NRCELLDU");
        //then
        assertEquals("REL_CD_decorators_ODUFUNCTION_PROVIDES_NRCELLDU", relationType.getDecoratorsColumnName());
    }

    @Test
    void testGetMetadataColumnNameForRelationType() throws SchemaRegistryException {
        //when
        RelationType relationType = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-ran",
                "ODUFUNCTION_PROVIDES_NRCELLDU");
        //then
        assertEquals("REL_metadata_ODUFUNCTION_PROVIDES_NRCELLDU", relationType.getMetadataColumnName());
    }

    @Test
    void testGetIdColumnNameRelationTest() throws SchemaRegistryException {
        //given
        String expectedManyToManyId = "id";
        String expectedOneToManyId = "REL_ID_ODUFUNCTION_PROVIDES_NRCELLDU";
        //when
        RelationType manyToManyRelation = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-rel-equipment-ran",
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY");
        RelationType oneToMany = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-ran",
                "ODUFUNCTION_PROVIDES_NRCELLDU");
        //then
        assertEquals(expectedManyToManyId, manyToManyRelation.getIdColumnName());
        assertEquals(expectedOneToManyId, oneToMany.getIdColumnName());
    }

    @Test
    void testGetASideColumnNameForRelationType() throws SchemaRegistryException {
        //given
        String expectedManyToMany = "aSide_AntennaModule";
        String expectedOneToMany = "REL_FK_provided-by-oduFunction";
        //when
        RelationType manyToMany = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-rel-equipment-ran",
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY");
        RelationType oneToMany = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-ran",
                "ODUFUNCTION_PROVIDES_NRCELLDU");
        //then
        assertEquals(expectedManyToMany, manyToMany.aSideColumnName());
        assertEquals(expectedOneToMany, oneToMany.aSideColumnName());
    }

    @Test
    void testGetBSideColumnNameForRelationType() throws SchemaRegistryException {
        //given
        String expectedManyToMAny = "bSide_AntennaCapability";
        String expectedOneToMany = "id";
        //when
        RelationType manyToMany = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-rel-equipment-ran",
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY");
        RelationType oneToMany = SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-ran",
                "ODUFUNCTION_PROVIDES_NRCELLDU");
        //then
        assertEquals(expectedManyToMAny, manyToMany.bSideColumnName());
        assertEquals(expectedOneToMany, oneToMany.bSideColumnName());
    }

    @Test
    void testGetFieldsForRelationType() throws SchemaRegistryException {
        //given
        RelationType antennamoduleServesAntennacapability = SchemaRegistry.getRelationTypeByModuleAndName(
                "o-ran-smo-teiv-rel-equipment-ran", "ANTENNAMODULE_SERVES_ANTENNACAPABILITY");
        //then
        assertEquals(Set.of(field(String.format(TEIV_DATA, "CFC235E0404703D1E4454647DF8AAE2C193DB402") + "." + String
                .format(QUOTED_STRING, "aSide_AntennaModule")).as(hashAlias(
                        "o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_ANTENNACAPABILITY.aSide")), field(String
                                .format(TEIV_DATA, "CFC235E0404703D1E4454647DF8AAE2C193DB402") + "." + String.format(
                                        QUOTED_STRING, "bSide_AntennaCapability")).as(hashAlias(
                                                "o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_ANTENNACAPABILITY.bSide")),
                field(String.format(TEIV_DATA, "CFC235E0404703D1E4454647DF8AAE2C193DB402") + "." + String.format(
                        QUOTED_STRING, "id")).as(hashAlias(
                                "o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_ANTENNACAPABILITY.id")), field(String
                                        .format(TEIV_DATA, "CFC235E0404703D1E4454647DF8AAE2C193DB402") + "." + String
                                                .format(QUOTED_STRING, "CD_sourceIds")).as(hashAlias(
                                                        "o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_ANTENNACAPABILITY.sourceIds")),
                field(String.format(TEIV_DATA, "CFC235E0404703D1E4454647DF8AAE2C193DB402") + "." + String.format(
                        QUOTED_STRING, "CD_decorators")).as(hashAlias(
                                "o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_ANTENNACAPABILITY.decorators")),
                field(String.format(TEIV_DATA, "CFC235E0404703D1E4454647DF8AAE2C193DB402") + "." + String.format(
                        QUOTED_STRING, "CD_classifiers")).as(hashAlias(
                                "o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_ANTENNACAPABILITY.classifiers")),
                field(String.format(TEIV_DATA, "CFC235E0404703D1E4454647DF8AAE2C193DB402") + "." + String.format(
                        QUOTED_STRING, "metadata")).as(hashAlias(
                                "o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_ANTENNACAPABILITY.metadata"))),
                new HashSet<>(antennamoduleServesAntennacapability.getAllFieldsWithId()));
    }

    @Test
    void testGetRelationTypesByDomain() {
        //given
        List<String> expectedRelations = List.of("SECTOR_GROUPS_NRCELLDU", "NRCELLDU_USES_NRSECTORCARRIER",
                "ODUFUNCTION_PROVIDES_NRSECTORCARRIER", "ODUFUNCTION_PROVIDES_NRCELLDU", "NEARRTRICFUNCTION_O1LINK_SMO",
                "OCUCPFUNCTION_O1LINK_SMO", "ODUFUNCTION_E2LINK_NEARRTRICFUNCTION", "ODUFUNCTION_F1CLINK_OCUCPFUNCTION",
                "ODUFUNCTION_F1ULINK_OCUUPFUNCTION", "ODUFUNCTION_O1LINK_SMO", "ORUFUNCTION_O1LINK_SMO",
                "ORUFUNCTION_OFHCLINK_ODUFUNCTION", "ORUFUNCTION_OFHMLINK_ODUFUNCTION", "ORUFUNCTION_OFHMLINK_SMO",
                "ORUFUNCTION_OFHSLINK_ODUFUNCTION", "ORUFUNCTION_OFHULINK_ODUFUNCTION",
                "NRSECTORCARRIER_USES_ANTENNACAPABILITY", "OCUCPFUNCTION_PROVIDES_NRCELLCU",
                "MANAGEDELEMENT_MANAGES_OCUCPFUNCTION", "MANAGEDELEMENT_MANAGES_OCUUPFUNCTION",
                "MANAGEDELEMENT_MANAGES_ODUFUNCTION", "MANAGEDELEMENT_MANAGES_ORUFUNCTION",
                "MANAGEDELEMENT_MANAGES_NEARRTRICFUNCTION");
        //when
        List<String> oamToRanRelations = SchemaRegistry.getRelationNamesByDomain("REL_OAM_RAN");
        //then
        assertEquals(expectedRelations.size(), oamToRanRelations.size());
        assertTrue(expectedRelations.containsAll(oamToRanRelations));
    }

    @Test
    void getAttributeColumnsWithFilterTest() throws SchemaRegistryException {
        Assertions.assertEquals(Map.of(field("teiv_data.\"AntennaModule\".\"geo-location\"").as(
                "o-ran-smo-teiv-equipment:AntennaModule.attr.geo-location"), DataType.GEOGRAPHIC), SchemaRegistry
                        .getEntityTypeByModuleAndName("o-ran-smo-teiv-equipment", "AntennaModule")
                        .getSpecificAttributeColumns(List.of("geo-location")));
        Assertions.assertEquals(Map.of(), SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-ran",
                "ODUFUNCTION_PROVIDES_NRSECTORCARRIER").getSpecificAttributeColumns(List.of()));
    }

    @Test
    void testGetAttributeNamesForRelation() throws SchemaRegistryException {
        Assertions.assertEquals(Collections.emptyList(), SchemaRegistry.getRelationTypeByModuleAndName("o-ran-smo-teiv-ran",
                "ODUFUNCTION_PROVIDES_NRSECTORCARRIER").getAttributeNames());
    }

    @Test
    void testGetIncludedModules() {
        //when
        Module module = SchemaRegistry.getModuleByName("o-ran-smo-teiv-rel-oam-ran");
        //then
        assertEquals(2, module.getIncludedModuleNames().size());
        assertTrue(List.of("o-ran-smo-teiv-oam", "o-ran-smo-teiv-ran").containsAll(module.getIncludedModuleNames()));
    }

    @Test
    void testGetIncludedDomains() {
        //when
        List<String> includedDomains = SchemaRegistry.getIncludedDomains("REL_OAM_RAN");
        //then
        assertEquals(2, includedDomains.size());
        assertTrue(List.of("OAM", "RAN").containsAll(includedDomains));
    }
}
