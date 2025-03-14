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
package org.oran.smo.teiv.exposure.teivpath.innerlanguage;

import static org.oran.smo.teiv.exposure.teivpath.refiner.AliasMapper.hashAlias;
import static org.oran.smo.teiv.schema.DataType.*;
import static org.jooq.impl.DSL.field;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.schema.MockSchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoaderException;

public class SelectBlockTest {

    @BeforeAll
    static void setup() throws SchemaLoaderException {
        new MockSchemaLoader().loadSchemaRegistry();
    }

    @Test
    void idInEntity() {
        List<TargetObject> targets = new ArrayList<>();
        InnerFilterCriteria filterCriteria = new InnerFilterCriteria(targets, null);

        targets.add(TargetObject.builder("ODUFunction").params(List.of()).container(ContainerType.ID).topologyObjectType(
                TopologyObjectType.ENTITY).build());

        Assertions.assertEquals(Map.of(field("teiv_data.\"ODUFunction\".\"id\"").as("o-ran-smo-teiv-ran:ODUFunction.id"),
                Map.of(field("teiv_data.\"ODUFunction\".\"id\"").as("o-ran-smo-teiv-ran:ODUFunction.id"), PRIMITIVE)),
                filterCriteria.getSelects());
    }

    @Test
    void attributesInEntity() {
        List<TargetObject> targets = new ArrayList<>();
        InnerFilterCriteria filterCriteria = new InnerFilterCriteria(targets, null);

        targets.add(TargetObject.builder("ODUFunction").params(List.of("gNBDUId", "dUpLMNId")).container(
                ContainerType.ATTRIBUTES).topologyObjectType(TopologyObjectType.ENTITY).build());

        Assertions.assertEquals(Map.of(field("teiv_data.\"ODUFunction\".\"id\"").as("o-ran-smo-teiv-ran:ODUFunction.id"),
                Map.of(field("teiv_data.\"ODUFunction\".\"id\"").as("o-ran-smo-teiv-ran:ODUFunction.id"), PRIMITIVE, field(
                        "teiv_data.\"ODUFunction\".\"gNBDUId\"").as("o-ran-smo-teiv-ran:ODUFunction.attr.gNBDUId"), BIGINT,
                        field("teiv_data.\"ODUFunction\".\"dUpLMNId\"").as("o-ran-smo-teiv-ran:ODUFunction.attr.dUpLMNId"),
                        CONTAINER)), filterCriteria.getSelects());
    }

    @Test
    void allAttributesInEntity() {
        List<TargetObject> targets = new ArrayList<>();
        InnerFilterCriteria filterCriteria = new InnerFilterCriteria(targets, null);

        targets.add(TargetObject.builder("ODUFunction").params(List.of()).container(ContainerType.ATTRIBUTES)
                .topologyObjectType(TopologyObjectType.ENTITY).isAllParamQueried(true).build());

        Assertions.assertEquals(Map.of(field("teiv_data.\"ODUFunction\".\"id\"").as("o-ran-smo-teiv-ran:ODUFunction.id"),
                Map.of(field("teiv_data.\"ODUFunction\".\"id\"").as("o-ran-smo-teiv-ran:ODUFunction.id"), PRIMITIVE, field(
                        "teiv_data.\"ODUFunction\".\"gNBId\"").as("o-ran-smo-teiv-ran:ODUFunction.attr.gNBId"), BIGINT,
                        field("teiv_data.\"ODUFunction\".\"gNBIdLength\"").as(
                                "o-ran-smo-teiv-ran:ODUFunction.attr.gNBIdLength"), INTEGER, field(
                                        "teiv_data.\"ODUFunction\".\"dUpLMNId\"").as(
                                                "o-ran-smo-teiv-ran:ODUFunction.attr.dUpLMNId"), CONTAINER, field(
                                                        "teiv_data.\"ODUFunction\".\"gNBDUId\"").as(
                                                                "o-ran-smo-teiv-ran:ODUFunction.attr.gNBDUId"), BIGINT)),
                filterCriteria.getSelects());
    }

    @Test
    void decoratorsInEntity() {
        List<TargetObject> targets = new ArrayList<>();
        InnerFilterCriteria filterCriteria = new InnerFilterCriteria(targets, null);

        targets.add(TargetObject.builder("ODUFunction").params(List.of()).container(ContainerType.DECORATORS)
                .topologyObjectType(TopologyObjectType.ENTITY).build());

        Assertions.assertEquals(Map.of(field("teiv_data.\"ODUFunction\".\"id\"").as("o-ran-smo-teiv-ran:ODUFunction.id"),
                Map.of(field("teiv_data.\"ODUFunction\".\"id\"").as("o-ran-smo-teiv-ran:ODUFunction.id"), PRIMITIVE, field(
                        "teiv_data.\"ODUFunction\".\"CD_decorators\"").as("o-ran-smo-teiv-ran:ODUFunction.decorators"),
                        CONTAINER)), filterCriteria.getSelects());
    }

    @Test
    void metadataInEntity() {
        List<TargetObject> targets = new ArrayList<>();
        InnerFilterCriteria filterCriteria = new InnerFilterCriteria(targets, null);

        targets.add(TargetObject.builder("ODUFunction").params(List.of()).container(ContainerType.METADATA)
                .topologyObjectType(TopologyObjectType.ENTITY).build());

        Assertions.assertEquals(Map.of(field("teiv_data.\"ODUFunction\".\"id\"").as("o-ran-smo-teiv-ran:ODUFunction.id"),
                Map.of(field("teiv_data.\"ODUFunction\".\"id\"").as("o-ran-smo-teiv-ran:ODUFunction.id"), PRIMITIVE, field(
                        "teiv_data.\"ODUFunction\".\"CD_decorators\"").as("o-ran-smo-teiv-ran:ODUFunction.metadata"),
                        CONTAINER)), filterCriteria.getSelects());
    }

    @Test
    void classifiersInEntity() {
        List<TargetObject> targets = new ArrayList<>();
        InnerFilterCriteria filterCriteria = new InnerFilterCriteria(targets, null);

        targets.add(TargetObject.builder("ODUFunction").params(List.of()).container(ContainerType.CLASSIFIERS)
                .topologyObjectType(TopologyObjectType.ENTITY).build());

        Assertions.assertEquals(Map.of(field("teiv_data.\"ODUFunction\".\"id\"").as("o-ran-smo-teiv-ran:ODUFunction.id"),
                Map.of(field("teiv_data.\"ODUFunction\".\"id\"").as("o-ran-smo-teiv-ran:ODUFunction.id"), PRIMITIVE, field(
                        "teiv_data.\"ODUFunction\".\"CD_classifiers\"").as("o-ran-smo-teiv-ran:ODUFunction.classifiers"),
                        CONTAINER)), filterCriteria.getSelects());
    }

    @Test
    void sourceIdInEntity() {
        List<TargetObject> targets = new ArrayList<>();
        InnerFilterCriteria filterCriteria = new InnerFilterCriteria(targets, null);

        targets.add(TargetObject.builder("ODUFunction").params(List.of()).container(ContainerType.SOURCE_IDS)
                .topologyObjectType(TopologyObjectType.ENTITY).build());

        Assertions.assertEquals(Map.of(field("teiv_data.\"ODUFunction\".\"id\"").as("o-ran-smo-teiv-ran:ODUFunction.id"),
                Map.of(field("teiv_data.\"ODUFunction\".\"id\"").as("o-ran-smo-teiv-ran:ODUFunction.id"), PRIMITIVE, field(
                        "teiv_data.\"ODUFunction\".\"CD_sourceIds\"").as("o-ran-smo-teiv-ran:ODUFunction.sourceIds"),
                        CONTAINER)), filterCriteria.getSelects());
    }

    @Test
    void idInRelationship() {
        List<TargetObject> targets = new ArrayList<>();
        InnerFilterCriteria filterCriteria = new InnerFilterCriteria(targets, null);

        targets.add(TargetObject.builder("SECTOR_GROUPS_ANTENNAMODULE").params(List.of()).container(ContainerType.ID)
                .topologyObjectType(TopologyObjectType.RELATION).build());

        Assertions.assertEquals(Map.of(field("teiv_data.\"AntennaModule\".\"REL_ID_SECTOR_GROUPS_ANTENNAMODULE\"").as(
                hashAlias("o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE.id")), Map.of(field(
                        "teiv_data.\"AntennaModule\".\"REL_ID_SECTOR_GROUPS_ANTENNAMODULE\"").as(hashAlias(
                                "o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE.id")), PRIMITIVE, field(
                                        "teiv_data.\"AntennaModule\".\"REL_FK_grouped-by-sector\"").as(hashAlias(
                                                "o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE.aSide")),
                        PRIMITIVE, field("teiv_data.\"AntennaModule\".\"id\"").as(hashAlias(
                                "o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE.bSide")), PRIMITIVE)),
                filterCriteria.getSelects());
    }

    @Test
    void decoratorsInRelationship() {
        List<TargetObject> targets = new ArrayList<>();
        InnerFilterCriteria filterCriteria = new InnerFilterCriteria(targets, null);

        targets.add(TargetObject.builder("SECTOR_GROUPS_ANTENNAMODULE").params(List.of()).container(
                ContainerType.DECORATORS).topologyObjectType(TopologyObjectType.RELATION).build());

        Assertions.assertEquals(Map.of(field("teiv_data.\"AntennaModule\".\"REL_ID_SECTOR_GROUPS_ANTENNAMODULE\"").as(
                hashAlias("o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE.id")), Map.of(field(
                        "teiv_data.\"AntennaModule\".\"REL_ID_SECTOR_GROUPS_ANTENNAMODULE\"").as(hashAlias(
                                "o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE.id")), PRIMITIVE, field(
                                        "teiv_data.\"AntennaModule\".\"REL_CD_decorators_SECTOR_GROUPS_ANTENNAMODULE\"").as(
                                                hashAlias(
                                                        "o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE.decorators")),
                        CONTAINER, field("teiv_data.\"AntennaModule\".\"REL_FK_grouped-by-sector\"").as(hashAlias(
                                "o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE.aSide")), PRIMITIVE, field(
                                        "teiv_data.\"AntennaModule\".\"id\"").as(hashAlias(
                                                "o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE.bSide")),
                        PRIMITIVE)), filterCriteria.getSelects());
    }

    @Test
    void classifiersInRelationship() {
        List<TargetObject> targets = new ArrayList<>();
        InnerFilterCriteria filterCriteria = new InnerFilterCriteria(targets, null);

        targets.add(TargetObject.builder("SECTOR_GROUPS_ANTENNAMODULE").params(List.of()).container(
                ContainerType.CLASSIFIERS).topologyObjectType(TopologyObjectType.RELATION).build());

        Assertions.assertEquals(Map.of(field("teiv_data.\"AntennaModule\".\"REL_ID_SECTOR_GROUPS_ANTENNAMODULE\"").as(
                hashAlias("o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE.id")), Map.of(field(
                        "teiv_data.\"AntennaModule\".\"REL_ID_SECTOR_GROUPS_ANTENNAMODULE\"").as(hashAlias(
                                "o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE.id")), PRIMITIVE, field(
                                        "teiv_data.\"AntennaModule\".\"REL_CD_classifiers_SECTOR_GROUPS_ANTENNAMODULE\"")
                                                .as(hashAlias(
                                                        "o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE.classifiers")),
                        CONTAINER, field("teiv_data.\"AntennaModule\".\"REL_FK_grouped-by-sector\"").as(hashAlias(
                                "o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE.aSide")), PRIMITIVE, field(
                                        "teiv_data.\"AntennaModule\".\"id\"").as(hashAlias(
                                                "o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE.bSide")),
                        PRIMITIVE)), filterCriteria.getSelects());
    }

    @Test
    void sourceIdInRelationship() {
        List<TargetObject> targets = new ArrayList<>();
        InnerFilterCriteria filterCriteria = new InnerFilterCriteria(targets, null);

        targets.add(TargetObject.builder("SECTOR_GROUPS_ANTENNAMODULE").params(List.of()).container(
                ContainerType.SOURCE_IDS).topologyObjectType(TopologyObjectType.RELATION).build());

        Assertions.assertEquals(Map.of(field("teiv_data.\"AntennaModule\".\"REL_ID_SECTOR_GROUPS_ANTENNAMODULE\"").as(
                hashAlias("o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE.id")), Map.of(field(
                        "teiv_data.\"AntennaModule\".\"REL_ID_SECTOR_GROUPS_ANTENNAMODULE\"").as(hashAlias(
                                "o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE.id")), PRIMITIVE, field(
                                        "teiv_data.\"AntennaModule\".\"REL_CD_sourceIds_SECTOR_GROUPS_ANTENNAMODULE\"").as(
                                                hashAlias(
                                                        "o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE.sourceIds")),
                        CONTAINER, field("teiv_data.\"AntennaModule\".\"REL_FK_grouped-by-sector\"").as(hashAlias(
                                "o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE.aSide")), PRIMITIVE, field(
                                        "teiv_data.\"AntennaModule\".\"id\"").as(hashAlias(
                                                "o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE.bSide")),
                        PRIMITIVE)), filterCriteria.getSelects());
    }

    @Test
    void wrongTopologyObjectType() {
        List<TargetObject> targets = new ArrayList<>();
        InnerFilterCriteria filterCriteria = new InnerFilterCriteria(targets, null);

        targets.add(TargetObject.builder("ODUFunction").params(List.of()).container(ContainerType.CLASSIFIERS)
                .topologyObjectType(TopologyObjectType.UNDEFINED).build());

        Assertions.assertThrowsExactly(TeivException.class, filterCriteria::getSelects);
    }

    @Test
    void wrongContainerType() {
        List<TargetObject> targets = new ArrayList<>();
        InnerFilterCriteria filterCriteria = new InnerFilterCriteria(targets, null);

        targets.add(TargetObject.builder("ODUFunction").params(List.of()).container(ContainerType.RELATION)
                .topologyObjectType(TopologyObjectType.ENTITY).build());

        Assertions.assertThrowsExactly(TeivException.class, filterCriteria::getSelects);
    }
}
