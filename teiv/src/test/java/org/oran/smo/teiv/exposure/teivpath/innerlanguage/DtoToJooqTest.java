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

import static org.oran.smo.teiv.utils.TeivConstants.ITEM;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.oran.smo.teiv.exposure.teivpath.resolver.ResolverDataType;
import org.oran.smo.teiv.utils.query.exception.TeivPathException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.Condition;
import org.jooq.Field;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.oran.smo.teiv.schema.DataType;
import org.oran.smo.teiv.schema.MockSchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoaderException;

class DtoToJooqTest {
    private static final String OCUCP_FUNCTION = "OCUCPFunction";
    private static final String ODU_FUNCTION = "ODUFunction";
    private static final String ANTENNA_CAPABILITY = "AntennaCapability";
    private static final String ANTENNA_MODULE = "AntennaModule";
    private static final String MANAGED_ELEMENT = "ManagedElement";
    private static final String NR_CELL_DU = "NRCellDU";

    @BeforeAll
    static void setUp() throws SchemaLoaderException {
        MockSchemaLoader mockSchemaLoader = new MockSchemaLoader();
        mockSchemaLoader.loadSchemaRegistry();
    }

    @Test
    void testConditions_relationIsNotNull() {
        ScopeObject scopeObject1 = ScopeObject.builder(MANAGED_ELEMENT).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.RELATION).innerContainer(List.of("MANAGEDELEMENT_MANAGES_ODUFUNCTION"))
                .queryFunction(QueryFunction.NOT_NULL).build();
        ScopeObject scopeObject2 = ScopeObject.builder(ODU_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.RELATION).innerContainer(List.of("MANAGEDELEMENT_MANAGES_ODUFUNCTION"))
                .queryFunction(QueryFunction.NOT_NULL).build();
        LogicalBlock lb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock lb2 = new ScopeLogicalBlock(scopeObject2);
        AndOrLogicalBlock olb1 = new OrLogicalBlock();
        olb1.setChildren(List.of(lb1, lb2));
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION\" is not null\n" +
                "  or teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION\" is not null\n")
            .toString(), olb1.getCondition().toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAttributes_primitive() {
        ScopeObject scopeObject1 = ScopeObject.builder(NR_CELL_DU).topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ATTRIBUTES).leaf("nRPCI").queryFunction(QueryFunction.EQ).parameter("ABC789").dataType(
                        DataType.PRIMITIVE).build();
        ScopeObject scopeObject2 = ScopeObject.builder(NR_CELL_DU).topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ATTRIBUTES).leaf("nRPCI").queryFunction(QueryFunction.CONTAINS).parameter("ABC789").dataType(
                        DataType.PRIMITIVE).build();
        ScopeObject scopeObject3 = ScopeObject.builder(NR_CELL_DU).topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ATTRIBUTES).leaf("nRPCI").queryFunction(QueryFunction.NOT_NULL).dataType(DataType.PRIMITIVE)
                .build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);
        LogicalBlock slb3 = new ScopeLogicalBlock(scopeObject3);
        AndOrLogicalBlock alb1 = new OrLogicalBlock();
        alb1.setChildren(List.of(slb1, slb2));
        AndOrLogicalBlock alb2 = new OrLogicalBlock();
        alb2.setChildren(List.of(alb1, slb3));
        Condition actualCondition = alb2.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"nRPCI\" = 'ABC789'\n" +
                "  or teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"nRPCI\" like (('%' || replace(\n" +
                "    replace(\n" +
                "      replace('ABC789', '!', '!!'),\n" +
                "      '%',\n" +
                "      '!%'\n" +
                "    ),\n" +
                "    '_',\n" +
                "    '!_'\n" +
                "  )) || '%') escape '!'\n" +
                "  or teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"nRPCI\" is not null\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAttributes_bigint() {
        ScopeObject scopeObject1 = ScopeObject.builder(NR_CELL_DU).topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ATTRIBUTES).leaf("gNBId").queryFunction(QueryFunction.EQ).parameter("1").dataType(
                        DataType.BIGINT).build();
        ScopeObject scopeObject2 = ScopeObject.builder(NR_CELL_DU).topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ATTRIBUTES).leaf("gNBId").queryFunction(QueryFunction.EQ).parameter("2").dataType(
                        DataType.BIGINT).build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);
        AndOrLogicalBlock alb1 = new OrLogicalBlock();
        alb1.setChildren(List.of(slb1, slb2));
        Condition actualCondition = alb1.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"gNBId\" = 1\n" +
                "  or teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"gNBId\" = 2\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAttributes_decimal() {
        ScopeObject scopeObject1 = ScopeObject.builder(NR_CELL_DU).topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ATTRIBUTES).leaf("decimalColumn").queryFunction(QueryFunction.EQ).parameter("2.5").dataType(
                        DataType.DECIMAL).build();
        ScopeObject scopeObject2 = ScopeObject.builder(NR_CELL_DU).topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ATTRIBUTES).leaf("decimalColumn").queryFunction(QueryFunction.EQ).parameter("15.25").dataType(
                        DataType.DECIMAL).build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);
        AndOrLogicalBlock alb1 = new OrLogicalBlock();
        alb1.setChildren(List.of(slb1, slb2));
        Condition actualCondition = alb1.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"decimalColumn\" = 2.5\n" +
                "  or teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"decimalColumn\" = 15.25\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAttributes_geography() {
        ScopeObject valid1 = ScopeObject.builder("AntennaModule").topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ATTRIBUTES).leaf("geoColumn").queryFunction(QueryFunction.EQ).parameter(
                        "point(39.4019881 67.9419888)").dataType(DataType.GEOGRAPHIC).build();
        ScopeObject valid2 = ScopeObject.builder("AntennaModule").topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ATTRIBUTES).leaf("geoColumn").queryFunction(QueryFunction.EQ).parameter(
                        "POINT(39.4019881 67.9419888)").dataType(DataType.GEOGRAPHIC).build();
        ScopeObject invalidContains = ScopeObject.builder("AntennaModule").topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).leaf("geoColumn").queryFunction(QueryFunction.CONTAINS).parameter(
                        "POINT(39.4019881 67.9419888)").dataType(DataType.GEOGRAPHIC).build();
        ScopeObject invalidCoordinate0 = ScopeObject.builder("AntennaModule").topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).leaf("geoColumn").queryFunction(QueryFunction.EQ).parameter(
                        "POINT(39.4019881 67.9419888 123.9878)").dataType(DataType.GEOGRAPHIC).build();
        ScopeObject invalidCoordinate1 = ScopeObject.builder("AntennaModule").topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).leaf("geoColumn").queryFunction(QueryFunction.EQ).parameter(
                        "POINT(39.4019881)").dataType(DataType.GEOGRAPHIC).build();
        ScopeObject invalidCoordinate2 = ScopeObject.builder("AntennaModule").topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).leaf("geoColumn").queryFunction(QueryFunction.EQ).parameter(
                        "POINT(39.4019881 INVALID)").dataType(DataType.GEOGRAPHIC).build();
        ScopeObject invalidCoordinate3 = ScopeObject.builder("AntennaModule").topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).leaf("geoColumn").queryFunction(QueryFunction.EQ).parameter(
                        "POINT(39.4019881 67.9419888    123.9878)").dataType(DataType.GEOGRAPHIC).build();
        ScopeObject invalidFormat1 = ScopeObject.builder("AntennaModule").topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).leaf("geoColumn").queryFunction(QueryFunction.EQ).parameter(
                        "(39.4019881 67.9419888)").dataType(DataType.GEOGRAPHIC).build();
        ScopeObject invalidFormat2 = ScopeObject.builder("AntennaModule").topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).leaf("geoColumn").queryFunction(QueryFunction.EQ).parameter(
                        "POINT39.4019881 67.9419888)").dataType(DataType.GEOGRAPHIC).build();
        ScopeObject invalidFormat3 = ScopeObject.builder("AntennaModule").topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).leaf("geoColumn").queryFunction(QueryFunction.EQ).parameter(
                        "POINT(39.4019881 67.9419888").dataType(DataType.GEOGRAPHIC).build();
        // spotless:off
        assertEquals(condition("teiv_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"geoColumn\" = st_geomfromtext('point(39.4019881 67.9419888)')")
            .toString(), new ScopeLogicalBlock(valid1).getCondition().toString());
        assertEquals(condition("teiv_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"geoColumn\" = st_geomfromtext('POINT(39.4019881 67.9419888)')")
            .toString(), new ScopeLogicalBlock(valid2).getCondition().toString());
        assertThrows(TeivPathException.class, new ScopeLogicalBlock(invalidContains)::getCondition);
        assertThrows(TeivPathException.class, new ScopeLogicalBlock(invalidCoordinate0)::getCondition);
        assertThrows(TeivPathException.class, new ScopeLogicalBlock(invalidCoordinate1)::getCondition);
        assertThrows(TeivPathException.class, new ScopeLogicalBlock(invalidCoordinate2)::getCondition);
        assertThrows(TeivPathException.class, new ScopeLogicalBlock(invalidCoordinate3)::getCondition);
        assertThrows(TeivPathException.class, new ScopeLogicalBlock(invalidFormat1)::getCondition);
        assertThrows(TeivPathException.class, new ScopeLogicalBlock(invalidFormat2)::getCondition);
        assertThrows(TeivPathException.class, new ScopeLogicalBlock(invalidFormat3)::getCondition);
        // spotless:on
    }

    @Test
    void testConditions_entityAttributes_containerObject() {
        ScopeObject scopeObject1 = ScopeObject.builder(ODU_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).innerContainer(List.of("dUpLMNId")).leaf("mnc").queryFunction(
                        QueryFunction.EQ).parameter("789").dataType(DataType.PRIMITIVE).build();
        ScopeObject scopeObject2 = ScopeObject.builder(ODU_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).innerContainer(List.of("dUpLMNId")).leaf("mcc").queryFunction(
                        QueryFunction.CONTAINS).parameter("456").dataType(DataType.PRIMITIVE).build();
        ScopeObject invalidQueryFunction = ScopeObject.builder(ODU_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).innerContainer(List.of("dUpLMNId")).leaf("mcc").queryFunction(
                        QueryFunction.NOT_NULL).parameter("456").dataType(DataType.PRIMITIVE).build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);
        AndOrLogicalBlock alb1 = new OrLogicalBlock();
        alb1.setChildren(List.of(slb1, slb2));
        Condition actualCondition = alb1.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"dUpLMNId\" -> 'mnc' = '\"789\"'\n" +
                "  or teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"dUpLMNId\" ->> 'mcc' like '%456%'\n")
            .toString(), actualCondition.toString());
        assertThrows(TeivPathException.class, new ScopeLogicalBlock(invalidQueryFunction)::getCondition);
        // spotless:on
    }

    @Test
    void testConditions_entityAttributes_containerObject_multipleElements() {
        ScopeObject scopeObject1 = ScopeObject.builder(ODU_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).innerContainer(List.of("dUpLMNId", "mcc")).leaf("mcca").queryFunction(
                        QueryFunction.EQ).parameter("789").dataType(DataType.PRIMITIVE).build();
        ScopeObject scopeObject11 = ScopeObject.builder(ODU_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).innerContainer(List.of("dUpLMNId", "mcc")).leaf("mccb").queryFunction(
                        QueryFunction.EQ).parameter("789").dataType(DataType.INTEGER).build();
        ScopeObject scopeObject2 = ScopeObject.builder(ODU_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).innerContainer(List.of("dUpLMNId", "mcc")).leaf("mcca").queryFunction(
                        QueryFunction.CONTAINS).parameter("789").dataType(DataType.PRIMITIVE).build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb11 = new ScopeLogicalBlock(scopeObject11);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);
        AndOrLogicalBlock alb1 = new OrLogicalBlock();
        alb1.setChildren(List.of(slb1, slb11));
        AndOrLogicalBlock alb2 = new OrLogicalBlock();
        alb2.setChildren(List.of(alb1, slb2));
        Condition actualCondition = alb2.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"dUpLMNId\" -> 'mcc' -> 'mcca' = '\"789\"'\n" +
                "  or teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"dUpLMNId\" -> 'mcc' -> 'mccb' = '789'\n" +
                "  or teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"dUpLMNId\" -> 'mcc' ->> 'mcca' like '%789%'\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_geoLocation_withinMeters() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "'POINT (12.3426 -45.24568)', 500").resolverDataType(ResolverDataType.STRING).queryFunction(
                                QueryFunction.WITHIN_METERS).build());
        Condition actualCondition = scopeObject.getCondition();
        assertEquals(condition("ST_DWithin(\"geo-location\", ST_GeographyFromText('POINT (12.3426 -45.24568)'), 500)")
                .toString(), actualCondition.toString());
    }

    @Test
    void testConditions_geoLocation_withinMeters_invalidParameter() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "'POINT (12.3426 45.24568))', 500").resolverDataType(ResolverDataType.STRING).queryFunction(
                                QueryFunction.WITHIN_METERS).build());
        assertThrows(TeivPathException.class, () -> scopeObject.getCondition());
    }

    @Test
    void testConditions_geoLocation_withinMeters_invalidParameter2() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "'POINT (12.3426 45.24568 345.6)', 500").resolverDataType(ResolverDataType.STRING).queryFunction(
                                QueryFunction.WITHIN_METERS).build());
        assertThrows(TeivPathException.class, () -> scopeObject.getCondition());
    }

    @Test
    void testConditions_geoLocation_withinMeters_invalidParameter3() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "'POINT (12.3426 45.)', 500").resolverDataType(ResolverDataType.STRING).queryFunction(
                                QueryFunction.WITHIN_METERS).build());
        assertThrows(TeivPathException.class, () -> scopeObject.getCondition());
    }

    @Test
    void testConditions_geoLocation_withinMeters_invalidParameter4() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "'POINT (12.3426)', 500").resolverDataType(ResolverDataType.STRING).queryFunction(
                                QueryFunction.WITHIN_METERS).build());
        assertThrows(TeivPathException.class, () -> scopeObject.getCondition());
    }

    @Test
    void testConditions_geoLocation_withinMeters_invalidParameter5() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "'(12.3426 345.533)', 500").resolverDataType(ResolverDataType.STRING).queryFunction(
                                QueryFunction.WITHIN_METERS).build());
        assertThrows(TeivPathException.class, () -> scopeObject.getCondition());
    }

    @Test
    void testConditions_geoLocation_withinMeters_withoutSpaceAfterComma() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "'POINT (12.3426 45.24568)',500").resolverDataType(ResolverDataType.STRING).queryFunction(
                                QueryFunction.WITHIN_METERS).build());
        Condition actualCondition = scopeObject.getCondition();
        assertEquals(condition("ST_DWithin(\"geo-location\", ST_GeographyFromText('POINT (12.3426 45.24568)'), 500)")
                .toString(), actualCondition.toString());
    }

    @Test
    void testConditions_geoLocation_withinMeters2() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "'POINT(12 45.24568)',500").resolverDataType(ResolverDataType.STRING).queryFunction(
                                QueryFunction.WITHIN_METERS).build());
        Condition actualCondition = scopeObject.getCondition();
        assertEquals(condition("ST_DWithin(\"geo-location\", ST_GeographyFromText('POINT(12 45.24568)'), 500)").toString(),
                actualCondition.toString());
    }

    @Test
    void testConditions_geoLocation_coveredBy() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "POLYGON((12.3426 45.24568, 13.3426 45.24568, 12.3426 44.24568, 13.3426 44.24568))")
                .resolverDataType(ResolverDataType.STRING).queryFunction(QueryFunction.COVERED_BY).build());
        Condition actualCondition = scopeObject.getCondition();
        assertEquals(condition(
                "ST_CoveredBy(\"geo-location\", ST_GeographyFromText('POLYGON((12.3426 45.24568, 13.3426 45.24568, 12.3426 44.24568, 13.3426 44.24568))'))")
                        .toString(), actualCondition.toString());
    }

    @Test
    void testConditions_geoLocation_coveredBy2() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "POLYGON((12.3426 45.24568, 13.3426 45.24568, 12.3426 44.24568, 13.3426 -44.24568, 35.2 45.6))")
                .resolverDataType(ResolverDataType.STRING).queryFunction(QueryFunction.COVERED_BY).build());
        Condition actualCondition = scopeObject.getCondition();
        assertEquals(condition(
                "ST_CoveredBy(\"geo-location\", ST_GeographyFromText('POLYGON((12.3426 45.24568, 13.3426 45.24568, 12.3426 44.24568, 13.3426 -44.24568, 35.2 45.6))'))")
                        .toString(), actualCondition.toString());
    }

    @Test
    void testConditions_geoLocation_coveredBy_invalidParameter() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "POLYGON((12.3426 45.24568, 13.3426 45.24568, 12.3426 44.24568, 13.3426 44.24568,))")
                .resolverDataType(ResolverDataType.STRING).queryFunction(QueryFunction.COVERED_BY).build());
        assertThrows(TeivPathException.class, () -> scopeObject.getCondition());
    }

    @Test
    void testConditions_geoLocation_coveredBy_invalidParameter2() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "POLYGON(12.3426 45.24568, 13.3426 45.24568, 12.3426 44.24568, 13.3426 44.)").resolverDataType(
                                ResolverDataType.STRING).queryFunction(QueryFunction.COVERED_BY).build());
        assertThrows(TeivPathException.class, () -> scopeObject.getCondition());
    }

    @Test
    void testConditions_geoLocation_coveredBy_invalidParameter3() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "POLYGON(12.3426 45.24568, 13.3426 45.24568, 12.3426 44.24568, 13.3426 44.24568 34.63)")
                .resolverDataType(ResolverDataType.STRING).queryFunction(QueryFunction.COVERED_BY).build());
        assertThrows(TeivPathException.class, () -> scopeObject.getCondition());
    }

    @Test
    void testConditions_geoLocation_coveredBy_invalidParameter4() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "POLYGON(12.3426 45.24568, 13.3426 45.24568, 12.3426 44.24568, 13.3426 44.24568))")
                .resolverDataType(ResolverDataType.STRING).queryFunction(QueryFunction.COVERED_BY).build());
        assertThrows(TeivPathException.class, () -> scopeObject.getCondition());
    }

    @Test
    void testConditions_geoLocation_coveredBy_invalidParameter5() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "(12.3426 45.24568, 13.3426 45.24568, 12.3426 44.24568, 13.3426 44.24568)").resolverDataType(
                                ResolverDataType.STRING).queryFunction(QueryFunction.COVERED_BY).build());
        assertThrows(TeivPathException.class, () -> scopeObject.getCondition());
    }

    @Test
    void testConditions_geoLocation_coveredBy_invalidDataType() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("mcca").dataType(DataType.PRIMITIVE).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "POLYGON (12.3426 45.24568, 13.3426 45.24568, 12.3426 44.24568, 13.3426 44.24568)")
                .resolverDataType(ResolverDataType.STRING).queryFunction(QueryFunction.COVERED_BY).build());
        assertThrows(TeivPathException.class, () -> scopeObject.getCondition());
    }

    @Test
    void testConditions_entityAttributes_containerArray() {
        ScopeObject scopeObject1 = ScopeObject.builder(ANTENNA_CAPABILITY).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).innerContainer(List.of("eUtranFqBands")).leaf(ITEM).queryFunction(
                        QueryFunction.EQ).parameter("789").dataType(DataType.PRIMITIVE).build();
        ScopeObject scopeObject11 = ScopeObject.builder(ANTENNA_CAPABILITY).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).innerContainer(List.of("eUtranFqBands")).leaf(ITEM).queryFunction(
                        QueryFunction.EQ).parameter("789").dataType(DataType.INTEGER).build();
        ScopeObject scopeObject2 = ScopeObject.builder(ANTENNA_CAPABILITY).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).innerContainer(List.of("eUtranFqBands")).leaf(ITEM).queryFunction(
                        QueryFunction.CONTAINS).parameter("456").dataType(DataType.PRIMITIVE).build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb11 = new ScopeLogicalBlock(scopeObject11);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);
        AndOrLogicalBlock alb1 = new OrLogicalBlock();
        alb1.setChildren(List.of(slb1, slb11));
        AndOrLogicalBlock alb2 = new OrLogicalBlock();
        alb2.setChildren(List.of(alb1, slb2));
        Condition actualCondition = alb2.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  (teiv_data.\"o-ran-smo-teiv-ran_AntennaCapability\".\"eUtranFqBands\" @> '\"789\"')\n" +
                "  or (teiv_data.\"o-ran-smo-teiv-ran_AntennaCapability\".\"eUtranFqBands\" @> '789')\n" +
                "  or (teiv_data.\"o-ran-smo-teiv-ran_AntennaCapability\".\"eUtranFqBands\"::text like '%456%')\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAttributes_containerArray_multipleLevels() {
        ScopeObject scopeObject1 = ScopeObject.builder(ANTENNA_CAPABILITY).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).innerContainer(List.of("jsonbColumn", "levelOneField",
                        "levelTwoField")).leaf(ITEM).queryFunction(QueryFunction.EQ).parameter("456").dataType(
                                DataType.PRIMITIVE).build();
        ScopeObject scopeObject2 = ScopeObject.builder(ANTENNA_CAPABILITY).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).innerContainer(List.of("jsonbColumn", "levelOneField",
                        "levelTwoField")).leaf(ITEM).queryFunction(QueryFunction.EQ).parameter("456").dataType(
                                DataType.INTEGER).build();
        ScopeObject scopeObject3 = ScopeObject.builder(ANTENNA_CAPABILITY).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).innerContainer(List.of("jsonbColumn", "levelOneField",
                        "levelTwoField")).leaf(ITEM).queryFunction(QueryFunction.CONTAINS).parameter("456").dataType(
                                DataType.INTEGER).build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);
        LogicalBlock slb3 = new ScopeLogicalBlock(scopeObject3);
        AndOrLogicalBlock alb1 = new OrLogicalBlock();
        alb1.setChildren(List.of(slb1, slb2));
        AndOrLogicalBlock alb2 = new OrLogicalBlock();
        alb2.setChildren(List.of(alb1, slb3));
        Condition actualCondition = alb2.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  (teiv_data.\"o-ran-smo-teiv-ran_AntennaCapability\".\"jsonbColumn\" -> 'levelOneField' -> 'levelTwoField' @> '\"456\"')\n" +
                "  or (teiv_data.\"o-ran-smo-teiv-ran_AntennaCapability\".\"jsonbColumn\" -> 'levelOneField' -> 'levelTwoField' @> '456')\n" +
                "  or (teiv_data.\"o-ran-smo-teiv-ran_AntennaCapability\".\"jsonbColumn\" -> 'levelOneField' ->> 'levelTwoField'::text like '%456%')\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAttributes() {
        ScopeObject scopeObject1 = ScopeObject.builder(ODU_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).leaf("gNBId").queryFunction(QueryFunction.EQ).parameter("1").dataType(
                        DataType.BIGINT).build();
        ScopeObject scopeObject2 = ScopeObject.builder(ODU_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).leaf("decimalId").queryFunction(QueryFunction.EQ).parameter("2.5")
                .dataType(DataType.DECIMAL).build();
        ScopeObject scopeObject3 = ScopeObject.builder(NR_CELL_DU).topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ATTRIBUTES).leaf("nRPCI").queryFunction(QueryFunction.CONTAINS).parameter("ABC789").dataType(
                        DataType.PRIMITIVE).build();
        ScopeObject scopeObject4 = ScopeObject.builder(NR_CELL_DU).topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ATTRIBUTES).innerContainer(List.of("pLMNId")).leaf("mnc").queryFunction(QueryFunction.EQ)
                .parameter("789").dataType(DataType.PRIMITIVE).build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);
        LogicalBlock slb3 = new ScopeLogicalBlock(scopeObject3);
        LogicalBlock slb4 = new ScopeLogicalBlock(scopeObject4);
        AndOrLogicalBlock alb1 = new AndLogicalBlock();
        alb1.setChildren(List.of(slb1, slb2));
        AndOrLogicalBlock alb2 = new AndLogicalBlock();
        alb2.setChildren(List.of(slb3, slb4));
        AndOrLogicalBlock alb3 = new AndLogicalBlock();
        alb3.setChildren(List.of(alb1, alb2));
        Condition actualCondition = alb3.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"gNBId\" = 1\n" +
                "  and teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"decimalId\" = 2.5\n" +
                "  and teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"nRPCI\" like (('%' || replace(\n" +
                "    replace(\n" +
                "      replace('ABC789', '!', '!!'),\n" +
                "      '%',\n" +
                "      '!%'\n" +
                "    ),\n" +
                "    '_',\n" +
                "    '!_'\n" +
                "  )) || '%') escape '!'\n" +
                "  and teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"pLMNId\" -> 'mnc' = '\"789\"'\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_relationshipAttributes() {
        ScopeObject scopeObject1 = ScopeObject.builder("ODUFUNCTION_PROVIDES_NRCELLDU").topologyObjectType(
                TopologyObjectType.RELATION).container(ContainerType.ATTRIBUTES).leaf("rel_column1").queryFunction(
                        QueryFunction.EQ).parameter("1").dataType(DataType.BIGINT).build();
        ScopeObject scopeObject2 = ScopeObject.builder("ODUFUNCTION_PROVIDES_NRSECTORCARRIER").topologyObjectType(
                TopologyObjectType.RELATION).container(ContainerType.ATTRIBUTES).leaf("rel_column2").queryFunction(
                        QueryFunction.EQ).parameter("ABC789").dataType(DataType.PRIMITIVE).build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);
        AndOrLogicalBlock alb = new AndLogicalBlock();
        alb.setChildren(List.of(slb1, slb2));
        Condition actualCondition = alb.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"rel_column1\" = 1\n" +
                "  and teiv_data.\"o-ran-smo-teiv-ran_NRSectorCarrier\".\"rel_column2\" = 'ABC789'\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_undefinedAttributes_throws() {
        ScopeObject scopeObject1 = ScopeObject.builder("ODUFUNCTION_PROVIDES_NRCELLDU").topologyObjectType(
                TopologyObjectType.UNDEFINED).container(ContainerType.ATTRIBUTES).leaf("rel_column1").queryFunction(
                        QueryFunction.EQ).parameter("1").dataType(DataType.BIGINT).build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        assertThrows(TeivPathException.class, slb1::getCondition);
    }

    @Test
    void testConditions_relation() {
        ScopeObject scopeObject1 = ScopeObject.builder(NR_CELL_DU).topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.RELATION).innerContainer(List.of("ODUFUNCTION_PROVIDES_NRCELLDU")).queryFunction(
                        QueryFunction.EQ).parameter("urn:base64:TWFuYWdlZEV").dataType(DataType.PRIMITIVE).build();
        ScopeObject scopeObject2 = ScopeObject.builder("NRSectorCarrier").topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.RELATION).innerContainer(List.of("ODUFUNCTION_PROVIDES_NRSECTORCARRIER"))
                .queryFunction(QueryFunction.EQ).parameter("urn:base64:TWFuYWdlZEW").dataType(DataType.PRIMITIVE).build();
        ScopeObject scopeObject3 = ScopeObject.builder(ODU_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.RELATION).innerContainer(List.of("ODUFUNCTION_PROVIDES_NRSECTORCARRIER")).leaf(
                        "id").queryFunction(QueryFunction.EQ).parameter("urn:base64:TWFuYWdlZEZ").dataType(
                                DataType.PRIMITIVE).build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);
        LogicalBlock slb3 = new ScopeLogicalBlock(scopeObject3);
        AndOrLogicalBlock alb1 = new AndLogicalBlock();
        alb1.setChildren(List.of(slb1, slb2));
        AndOrLogicalBlock alb2 = new AndLogicalBlock();
        alb2.setChildren(List.of(alb1, slb3));
        Condition actualCondition = alb2.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"REL_ID_ODUFUNCTION_PROVIDES_NRCELLDU\" = 'urn:base64:TWFuYWdlZEV'\n" +
                "  and teiv_data.\"o-ran-smo-teiv-ran_NRSectorCarrier\".\"REL_ID_ODUFUNCTION_PROVIDES_NRSECTORCARRIER\" = 'urn:base64:TWFuYWdlZEW'\n" +
                "  and teiv_data.\"o-ran-smo-teiv-ran_NRSectorCarrier\".\"REL_ID_ODUFUNCTION_PROVIDES_NRSECTORCARRIER\" = 'urn:base64:TWFuYWdlZEZ'\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_relation_throws() {
        ScopeObject scopeObject1 = ScopeObject.builder("NOT_ENTITY").topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.RELATION).innerContainer(List.of("ODUFUNCTION_PROVIDES_NRCELLDU")).leaf("id")
                .queryFunction(QueryFunction.EQ).parameter("urn:base64:TWFuYWdlZEZ").dataType(DataType.PRIMITIVE).build();
        ScopeObject scopeObject2 = ScopeObject.builder("NOT_RELATION").topologyObjectType(TopologyObjectType.RELATION)
                .container(ContainerType.RELATION).innerContainer(List.of("ODUFUNCTION_PROVIDES_NRCELLDU")).leaf("id")
                .queryFunction(QueryFunction.EQ).parameter("urn:base64:TWFuYWdlZEZ").dataType(DataType.PRIMITIVE).build();
        ScopeObject scopeObject3 = ScopeObject.builder("NOT_RELATION").topologyObjectType(TopologyObjectType.RELATION)
                .container(ContainerType.RELATION).innerContainer(List.of("ODUFUNCTION_PROVIDES_NRCELLDU")).leaf("id")
                .queryFunction(QueryFunction.CONTAINS).parameter("urn:base64:TWFuYWdlZEZ").dataType(DataType.PRIMITIVE)
                .build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);
        LogicalBlock slb3 = new ScopeLogicalBlock(scopeObject3);
        assertThrows(TeivPathException.class, slb1::getCondition);
        assertThrows(TeivPathException.class, slb2::getCondition);
        assertThrows(TeivPathException.class, slb3::getCondition);
    }

    @Test
    void testConditions_entityId() {
        ScopeObject scopeObject1 = ScopeObject.builder(ODU_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ID).queryFunction(QueryFunction.EQ).parameter("ABC123").dataType(
                        DataType.PRIMITIVE).build();
        ScopeObject scopeObject2 = ScopeObject.builder(NR_CELL_DU).topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ID).queryFunction(QueryFunction.CONTAINS).parameter("DEF456").dataType(DataType.PRIMITIVE)
                .build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);
        AndOrLogicalBlock alb = new AndLogicalBlock();
        alb.setChildren(List.of(slb1, slb2));
        Condition actualCondition = alb.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"id\" = 'ABC123'\n" +
                "  and teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"id\" like (('%' || replace(\n" +
                "    replace(\n" +
                "      replace('DEF456', '!', '!!'),\n" +
                "      '%',\n" +
                "      '!%'\n" +
                "    ),\n" +
                "    '_',\n" +
                "    '!_'\n" +
                "  )) || '%') escape '!'\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_relationId() {
        ScopeObject scopeObject1 = ScopeObject.builder("ODUFUNCTION_PROVIDES_NRCELLDU").topologyObjectType(
                TopologyObjectType.RELATION).container(ContainerType.ID).queryFunction(QueryFunction.EQ).parameter(
                        "urn:base64:TWFuYWdlZEV").dataType(DataType.PRIMITIVE).build();
        ScopeObject scopeObject2 = ScopeObject.builder("ODUFUNCTION_PROVIDES_NRSECTORCARRIER").topologyObjectType(
                TopologyObjectType.RELATION).container(ContainerType.ID).queryFunction(QueryFunction.CONTAINS).parameter(
                        "urn:base64:TWFuYWdlZEW").dataType(DataType.PRIMITIVE).build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);
        AndOrLogicalBlock alb = new AndLogicalBlock();
        alb.setChildren(List.of(slb1, slb2));
        Condition actualCondition = alb.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"REL_ID_ODUFUNCTION_PROVIDES_NRCELLDU\" = 'urn:base64:TWFuYWdlZEV'\n" +
                "  and teiv_data.\"o-ran-smo-teiv-ran_NRSectorCarrier\".\"REL_ID_ODUFUNCTION_PROVIDES_NRSECTORCARRIER\" like (('%' || replace(\n" +
                "    replace(\n" +
                "      replace('urn:base64:TWFuYWdlZEW', '!', '!!'),\n" +
                "      '%',\n" +
                "      '!%'\n" +
                "    ),\n" +
                "    '_',\n" +
                "    '!_'\n" +
                "  )) || '%') escape '!'\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_relationIdIsNotNull() {
        ScopeObject scopeObject1 = ScopeObject.builder("ODUFUNCTION_PROVIDES_NRCELLDU").topologyObjectType(
                TopologyObjectType.RELATION).container(ContainerType.ID).queryFunction(QueryFunction.NOT_NULL).dataType(
                        DataType.PRIMITIVE).build();
        ScopeObject scopeObject2 = ScopeObject.builder("ODUFUNCTION_PROVIDES_NRSECTORCARRIER").topologyObjectType(
                TopologyObjectType.RELATION).container(ContainerType.ID).queryFunction(QueryFunction.NOT_NULL).dataType(
                        DataType.PRIMITIVE).build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);
        AndOrLogicalBlock alb = new AndLogicalBlock();
        alb.setChildren(List.of(slb1, slb2));
        Condition actualCondition = alb.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"REL_ID_ODUFUNCTION_PROVIDES_NRCELLDU\" is not null\n" +
                "  and teiv_data.\"o-ran-smo-teiv-ran_NRSectorCarrier\".\"REL_ID_ODUFUNCTION_PROVIDES_NRSECTORCARRIER\" is not null\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_idThrows() {
        ScopeLogicalBlock undefinedTOType = new ScopeLogicalBlock(ScopeObject.builder("ODUFUNCTION_PROVIDES_NRCELLDU")
                .topologyObjectType(TopologyObjectType.UNDEFINED).container(ContainerType.ID).queryFunction(
                        QueryFunction.EQ).parameter("urn:base64:TWFuYWdlZEV").dataType(DataType.PRIMITIVE).build());
        ScopeLogicalBlock invalidTopologyObjectType = new ScopeLogicalBlock(ScopeObject.builder("ODUFunction")
                .topologyObjectType(TopologyObjectType.UNDEFINED).container(ContainerType.ID).queryFunction(
                        QueryFunction.NOT_NULL).parameter("urn:base64:TWFuYWdlZEV").dataType(DataType.PRIMITIVE).build());
        assertThrows(TeivPathException.class, undefinedTOType::getCondition);
        assertThrows(TeivPathException.class, invalidTopologyObjectType::getCondition);
    }

    @Test
    void testConditions_entityAssociation_oneToMany1() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder("ORUFunction/managed-by-managedElement")
                .topologyObjectType(TopologyObjectType.ASSOCIATION).container(ContainerType.ID).innerContainer(List.of(
                        "managed-by-managedElement")).leaf("id").queryFunction(QueryFunction.EQ).parameter("me1").build());
        ScopeLogicalBlock scopeObject2 = new ScopeLogicalBlock(ScopeObject.builder(
                ODU_FUNCTION + "/managed-by-managedElement").topologyObjectType(TopologyObjectType.ASSOCIATION).container(
                        ContainerType.NOT_NULL).innerContainer(List.of()).leaf(null).queryFunction(QueryFunction.NOT_NULL)
                .parameter(null).build());
        AndOrLogicalBlock alb = new AndLogicalBlock();
        alb.setChildren(List.of(scopeObject1, scopeObject2));
        Condition actualCondition = alb.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"o-ran-smo-teiv-ran_ORUFunction\".\"REL_FK_managed-by-managedElement\" = 'me1'\n" +
                "  and teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"REL_FK_managed-by-managedElement\" is not null\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAssociation_oneToMany2() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder(MANAGED_ELEMENT + "/managed-oduFunction")
                .topologyObjectType(TopologyObjectType.ASSOCIATION).container(ContainerType.ID).innerContainer(List.of())
                .leaf("id").queryFunction(QueryFunction.EQ).parameter("odu1").build());
        ScopeLogicalBlock scopeObject2 = new ScopeLogicalBlock(ScopeObject.builder(
                MANAGED_ELEMENT + "/managed-ocucpFunction").topologyObjectType(TopologyObjectType.ASSOCIATION).container(
                        ContainerType.NOT_NULL).innerContainer(List.of()).leaf(null).queryFunction(QueryFunction.NOT_NULL)
                .parameter(null).build());
        AndOrLogicalBlock alb = new AndLogicalBlock();
        alb.setChildren(List.of(scopeObject1, scopeObject2));
        Condition actualCondition = alb.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"REL_FK_managed-by-managedElement\" is not null\n" +
                "  and teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"id\" = 'odu1'\n" +
                "  and teiv_data.\"o-ran-smo-teiv-ran_OCUCPFunction\".\"REL_FK_managed-by-managedElement\" is not null\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAssociation_manyToOne1() {
        //antennamodule/entites?scopeFilter=/installed-at-site[@id=site']
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder(ANTENNA_MODULE + "/installed-at-site")
                .topologyObjectType(TopologyObjectType.ASSOCIATION).container(ContainerType.ID).innerContainer(List.of())
                .leaf("id").queryFunction(QueryFunction.EQ).parameter("site1").build());
        ScopeLogicalBlock scopeObject2 = new ScopeLogicalBlock(ScopeObject.builder(ANTENNA_MODULE + "/installed-at-site")
                .topologyObjectType(TopologyObjectType.ASSOCIATION).container(ContainerType.NOT_NULL).innerContainer(List
                        .of()).leaf(null).queryFunction(QueryFunction.NOT_NULL).parameter(null).build());
        AndOrLogicalBlock alb = new OrLogicalBlock();
        alb.setChildren(List.of(scopeObject1, scopeObject2));
        Condition actualCondition = alb.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"REL_FK_installed-at-site\" = 'site1'\n" +
                "  or teiv_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"REL_FK_installed-at-site\" is not null\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAssociation_manyToOne2() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder("Site/installed-antennaModule")
                .topologyObjectType(TopologyObjectType.ASSOCIATION).container(ContainerType.ID).innerContainer(List.of())
                .leaf("id").queryFunction(QueryFunction.EQ).parameter("am1").build());
        ScopeLogicalBlock scopeObject2 = new ScopeLogicalBlock(ScopeObject.builder("Site/installed-antennaModule")
                .topologyObjectType(TopologyObjectType.ASSOCIATION).container(ContainerType.NOT_NULL).innerContainer(List
                        .of()).leaf(null).queryFunction(QueryFunction.NOT_NULL).parameter(null).build());
        AndOrLogicalBlock alb = new OrLogicalBlock();
        alb.setChildren(List.of(scopeObject1, scopeObject2));
        Condition actualCondition = alb.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  (\n" +
                "    teiv_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"REL_FK_installed-at-site\" is not null\n" +
                "    and teiv_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"id\" = 'am1'\n" +
                "  )\n" +
                "  or teiv_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"REL_FK_installed-at-site\" is not null\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAssociation_manyToOne1Contains() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder("Site/installed-antennaModule")
                .topologyObjectType(TopologyObjectType.ASSOCIATION).container(ContainerType.ID).dataType(DataType.PRIMITIVE)
                .innerContainer(List.of()).leaf("id").queryFunction(QueryFunction.CONTAINS).parameter("am1").build());
        Condition actualCondition = scopeObject1.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "teiv_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"REL_FK_installed-at-site\" is not null\n" +
                "and teiv_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"id\" like (" +
                "('%' || replace(" + "\n"+
                "replace(" + "\n"+
                "replace('am1', '!', '!!')," + "\n"+
                "'%'," + "\n"+
                "'!%'" + "\n"+
                ")," + "\n"+
                "'_'," + "\n"+
                "'!_'" + "\n"+
                ")) || '%') escape '!'" + "\n")
            .toString().replace(" ", ""), actualCondition.toString().replace(" ", ""));
        // spotless:on
    }

    @Test
    void testConditions_entityAssociation_manyToMany1Contains() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder(
                ANTENNA_MODULE + "/serviced-antennaCapability").topologyObjectType(TopologyObjectType.ASSOCIATION)
                .container(ContainerType.ID).innerContainer(List.of()).leaf("id").queryFunction(QueryFunction.CONTAINS)
                .parameter("ac1").build());
        Condition actualCondition = scopeObject1.getCondition();
        // spotless:off
        assertEquals(("teiv_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\"" +
            ".\"bSide_AntennaCapability\"like(('%'||replace(" + "\n"+
            "replace(" + "\n"+
            "replace('ac1','!','!!')," + "\n"+
            "'%'," + "\n"+
            "'!%'" + "\n"+
            ")," + "\n"+
            "'_'," + "\n"+
            "'!_'" + "\n"+
            "))||'%')escape'!'")
            .replace(" ", ""), actualCondition.toString().replace(" ", ""));
        // spotless:on
    }

    @Test
    void testConditions_entityAssociation_oneToMany1Contains() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder("ORUFunction/managed-by-managedElement")
                .topologyObjectType(TopologyObjectType.ASSOCIATION).container(ContainerType.ID).innerContainer(List.of())
                .leaf("id").queryFunction(QueryFunction.CONTAINS).parameter("me1").build());
        Condition actualCondition = scopeObject1.getCondition();
        // spotless:off
        assertEquals(
            ("teiv_data.\"o-ran-smo-teiv-ran_ORUFunction\".\"REL_FK_managed-by-managedElement\"like(('%'||replace(" + "\n" +
                "replace(" +"\n"+
                "replace('me1','!','!!')," + "\n" +
                "'%'," + "\n" +
                "'!%'" + "\n" +
                ")," + "\n" +
                "'_'," + "\n" +
                "'!_'" + "\n" +
                "))||'%')escape'!'"
            ).replace(" ", ""), actualCondition.toString().replace(" ", ""));
        // spotless:on
    }

    @Test
    void testConditions_entityAssociation_manyToMany1() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder(
                ANTENNA_MODULE + "/serviced-antennaCapability").topologyObjectType(TopologyObjectType.ASSOCIATION)
                .container(ContainerType.ID).innerContainer(List.of()).leaf("id").queryFunction(QueryFunction.EQ).parameter(
                        "ac1").build());
        ScopeLogicalBlock scopeObject2 = new ScopeLogicalBlock(ScopeObject.builder(
                ANTENNA_MODULE + "/serviced-antennaCapability").topologyObjectType(TopologyObjectType.ASSOCIATION)
                .container(ContainerType.NOT_NULL).innerContainer(List.of()).leaf(null).queryFunction(
                        QueryFunction.NOT_NULL).parameter(null).build());
        AndOrLogicalBlock alb = new AndLogicalBlock();
        alb.setChildren(List.of(scopeObject1, scopeObject2));
        Condition actualCondition = alb.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\".\"bSide_AntennaCapability\" = 'ac1'\n" +
                "  and teiv_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\".id is not null\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAssociation_manyToMany2() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder(
                ANTENNA_CAPABILITY + "/serving-antennaModule").topologyObjectType(TopologyObjectType.ASSOCIATION).container(
                        ContainerType.ID).innerContainer(List.of()).leaf("id").queryFunction(QueryFunction.EQ).parameter(
                                "am1").build());
        ScopeLogicalBlock scopeObject2 = new ScopeLogicalBlock(ScopeObject.builder(
                ANTENNA_CAPABILITY + "/serving-antennaModule").topologyObjectType(TopologyObjectType.ASSOCIATION).container(
                        ContainerType.NOT_NULL).innerContainer(List.of()).leaf(null).queryFunction(QueryFunction.NOT_NULL)
                .parameter(null).build());
        AndOrLogicalBlock alb = new AndLogicalBlock();
        alb.setChildren(List.of(scopeObject1, scopeObject2));
        Condition actualCondition = alb.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\".\"aSide_AntennaModule\" = 'am1'\n" +
                "  and teiv_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\".id is not null\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_relationAssociation_oneToMany1() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder(
                "MANAGEDELEMENT_MANAGES_ORUFUNCTION/managed-by-managedElement").topologyObjectType(
                        TopologyObjectType.ASSOCIATION).container(ContainerType.ID).innerContainer(List.of()).leaf("id")
                .queryFunction(QueryFunction.EQ).parameter("me1").build());
        ScopeLogicalBlock scopeObject2 = new ScopeLogicalBlock(ScopeObject.builder(
                "MANAGEDELEMENT_MANAGES_ODUFUNCTION/managed-by-managedElement").topologyObjectType(
                        TopologyObjectType.ASSOCIATION).container(ContainerType.ID).innerContainer(List.of()).leaf(null)
                .queryFunction(QueryFunction.NOT_NULL).parameter(null).build());
        AndOrLogicalBlock alb = new AndLogicalBlock();
        alb.setChildren(List.of(scopeObject1, scopeObject2));
        Condition actualCondition = alb.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"o-ran-smo-teiv-ran_ORUFunction\".\"REL_FK_managed-by-managedElement\" = 'me1'\n" +
                "  and teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"REL_FK_managed-by-managedElement\" is not null\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_relationAssociation_oneToMany2() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder(
                "MANAGEDELEMENT_MANAGES_ORUFUNCTION/managed-oruFunction").topologyObjectType(TopologyObjectType.ASSOCIATION)
                .container(ContainerType.ID).innerContainer(List.of()).leaf("id").queryFunction(QueryFunction.EQ).parameter(
                        "oruf1").build());
        ScopeLogicalBlock scopeObject2 = new ScopeLogicalBlock(ScopeObject.builder(
                "MANAGEDELEMENT_MANAGES_ODUFUNCTION/managed-by-managedElement").topologyObjectType(
                        TopologyObjectType.ASSOCIATION).container(ContainerType.NOT_NULL).innerContainer(List.of()).leaf(
                                null).queryFunction(QueryFunction.NOT_NULL).parameter(null).build());
        AndOrLogicalBlock alb = new AndLogicalBlock();
        alb.setChildren(List.of(scopeObject1, scopeObject2));
        Condition actualCondition = alb.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"o-ran-smo-teiv-ran_ORUFunction\".\"REL_FK_managed-by-managedElement\" is not null\n" +
                "  and teiv_data.\"o-ran-smo-teiv-ran_ORUFunction\".\"id\" = 'oruf1'\n" +
                "  and teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"REL_FK_managed-by-managedElement\" is not null\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_relationAssociation_manyToOne1() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder(
                "ANTENNAMODULE_INSTALLED_AT_SITE/installed-at-site").topologyObjectType(TopologyObjectType.ASSOCIATION)
                .container(ContainerType.ID).innerContainer(List.of()).leaf("id").queryFunction(QueryFunction.EQ).parameter(
                        "site1").build());
        ScopeLogicalBlock scopeObject2 = new ScopeLogicalBlock(ScopeObject.builder(
                "ANTENNAMODULE_INSTALLED_AT_SITE/installed-at-site").topologyObjectType(TopologyObjectType.ASSOCIATION)
                .container(ContainerType.NOT_NULL).innerContainer(List.of()).leaf(null).queryFunction(
                        QueryFunction.NOT_NULL).parameter(null).build());
        AndOrLogicalBlock alb = new OrLogicalBlock();
        alb.setChildren(List.of(scopeObject1, scopeObject2));
        Condition actualCondition = alb.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"REL_FK_installed-at-site\" = 'site1'\n" +
                "  or teiv_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"REL_FK_installed-at-site\" is not null\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_relationAssociation_manyToOne2() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder(
                "ANTENNAMODULE_INSTALLED_AT_SITE/installed-antennaModule").topologyObjectType(
                        TopologyObjectType.ASSOCIATION).container(ContainerType.ID).innerContainer(List.of()).leaf("id")
                .queryFunction(QueryFunction.EQ).parameter("am1").build());
        ScopeLogicalBlock scopeObject2 = new ScopeLogicalBlock(ScopeObject.builder(
                "ANTENNAMODULE_INSTALLED_AT_SITE/installed-antennaModule").topologyObjectType(
                        TopologyObjectType.ASSOCIATION).container(ContainerType.NOT_NULL).innerContainer(List.of()).leaf(
                                null).queryFunction(QueryFunction.NOT_NULL).parameter(null).build());
        AndOrLogicalBlock alb = new OrLogicalBlock();
        alb.setChildren(List.of(scopeObject1, scopeObject2));
        Condition actualCondition = alb.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  (\n" +
                "    teiv_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"REL_FK_installed-at-site\" is not null\n" +
                "    and teiv_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"id\" = 'am1'\n" +
                "  )\n" +
                "  or teiv_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"REL_FK_installed-at-site\" is not null\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_relationAssociation_manyToMany1() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder(
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY/serviced-antennaCapability").topologyObjectType(
                        TopologyObjectType.ASSOCIATION).container(ContainerType.ID).innerContainer(List.of()).leaf("id")
                .queryFunction(QueryFunction.EQ).parameter("ac1").build());
        ScopeLogicalBlock scopeObject2 = new ScopeLogicalBlock(ScopeObject.builder(
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY/serviced-antennaCapability").topologyObjectType(
                        TopologyObjectType.ASSOCIATION).container(ContainerType.NOT_NULL).innerContainer(List.of()).leaf(
                                null).queryFunction(QueryFunction.NOT_NULL).parameter(null).build());
        AndOrLogicalBlock alb = new AndLogicalBlock();
        alb.setChildren(List.of(scopeObject1, scopeObject2));
        Condition actualCondition = alb.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\".\"bSide_AntennaCapability\" = 'ac1'\n" +
                "  and teiv_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\".id is not null\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_relationAssociation_manyToMany2() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder(
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY/serving-antennaModule").topologyObjectType(
                        TopologyObjectType.ASSOCIATION).container(ContainerType.ID).innerContainer(List.of()).leaf("id")
                .queryFunction(QueryFunction.EQ).parameter("am1").build());
        ScopeLogicalBlock scopeObject2 = new ScopeLogicalBlock(ScopeObject.builder(
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY/serving-antennaModule").topologyObjectType(
                        TopologyObjectType.ASSOCIATION).container(ContainerType.NOT_NULL).innerContainer(List.of()).leaf(
                                null).queryFunction(QueryFunction.NOT_NULL).parameter(null).build());
        AndOrLogicalBlock alb = new AndLogicalBlock();
        alb.setChildren(List.of(scopeObject1, scopeObject2));
        Condition actualCondition = alb.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\".\"aSide_AntennaModule\" = 'am1'\n" +
                "  and teiv_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\".id is not null\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAssociation_throws() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder(
                "CloudNativeApplication/realised-oduFunction").container(ContainerType.ID).innerContainer(List.of()).leaf(
                        "id").queryFunction(QueryFunction.CONTAINS).parameter("odu1").build());
        assertThrows(TeivPathException.class, scopeObject1::getCondition);
    }

    @Test
    void testConditions_entityClassifiers() {
        ScopeObject scopeObject1 = ScopeObject.builder(ODU_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.CLASSIFIERS).leaf(ITEM).queryFunction(QueryFunction.CONTAINS).parameter(
                        "odu-function-model:Ru").dataType(DataType.CONTAINER).build();
        ScopeObject scopeObject2 = ScopeObject.builder(OCUCP_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.CLASSIFIERS).leaf(ITEM).queryFunction(QueryFunction.EQ).parameter(
                        "ocucp-function-model:Weekend").dataType(DataType.CONTAINER).build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);
        AndOrLogicalBlock alb = new AndLogicalBlock();
        alb.setChildren(List.of(slb1, slb2));
        Condition actualCondition = alb.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  (teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"CD_classifiers\"::text like '%odu-function-model:Ru%')\n" +
                "  and (teiv_data.\"o-ran-smo-teiv-ran_OCUCPFunction\".\"CD_classifiers\" @> '\"ocucp-function-model:Weekend\"')\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_relationshipClassifiers() {
        ScopeObject scopeObject1 = ScopeObject.builder("ODUFUNCTION_PROVIDES_NRCELLDU").topologyObjectType(
                TopologyObjectType.RELATION).container(ContainerType.CLASSIFIERS).leaf(ITEM).queryFunction(
                        QueryFunction.CONTAINS).parameter("odu-function-model:Ru").dataType(DataType.CONTAINER).build();
        ScopeObject scopeObject2 = ScopeObject.builder("ODUFUNCTION_PROVIDES_NRSECTORCARRIER").topologyObjectType(
                TopologyObjectType.RELATION).container(ContainerType.CLASSIFIERS).leaf(ITEM).queryFunction(QueryFunction.EQ)
                .parameter("ocucp-function-model:Weekend").dataType(DataType.CONTAINER).build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);
        AndOrLogicalBlock alb = new AndLogicalBlock();
        alb.setChildren(List.of(slb1, slb2));
        Condition actualCondition = alb.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  (teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"REL_CD_classifiers_ODUFUNCTION_PROVIDES_NRCELLDU\"::text like '%odu-function-model:Ru%')\n" +
                "  and (teiv_data.\"o-ran-smo-teiv-ran_NRSectorCarrier\".\"REL_CD_classifiers_ODUFUNCTION_PROVIDES_NRSECTORCARRIER\" @> '\"ocucp-function-model:Weekend\"')\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_entitySourceIds() {
        ScopeObject scopeObject1 = ScopeObject.builder(ODU_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.SOURCE_IDS).leaf(ITEM).queryFunction(QueryFunction.CONTAINS).parameter(
                        "urn:cmHandle:").dataType(DataType.CONTAINER).build();
        ScopeObject scopeObject2 = ScopeObject.builder(OCUCP_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.SOURCE_IDS).leaf(ITEM).queryFunction(QueryFunction.EQ).parameter(
                        "urn:3gpp:dn:/SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/ODUFunction=16")
                .dataType(DataType.CONTAINER).build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);
        AndOrLogicalBlock alb = new AndLogicalBlock();
        alb.setChildren(List.of(slb1, slb2));
        Condition actualCondition = alb.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  (teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"CD_sourceIds\"::text like '%urn:cmHandle:%')\n" +
                "  and (teiv_data.\"o-ran-smo-teiv-ran_OCUCPFunction\".\"CD_sourceIds\" @> '\"urn:3gpp:dn:/SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/ODUFunction=16\"')\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_relationshipSourceIds() {
        ScopeObject scopeObject1 = ScopeObject.builder("ODUFUNCTION_PROVIDES_NRCELLDU").topologyObjectType(
                TopologyObjectType.RELATION).container(ContainerType.SOURCE_IDS).leaf(ITEM).queryFunction(
                        QueryFunction.CONTAINS).parameter("urn:cmHandle:").dataType(DataType.CONTAINER).build();
        ScopeObject scopeObject2 = ScopeObject.builder("ODUFUNCTION_PROVIDES_NRSECTORCARRIER").topologyObjectType(
                TopologyObjectType.RELATION).container(ContainerType.SOURCE_IDS).leaf(ITEM).queryFunction(QueryFunction.EQ)
                .parameter(
                        "urn:3gpp:dn:/SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/ODUFunction=16")
                .dataType(DataType.CONTAINER).build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);
        AndOrLogicalBlock alb = new AndLogicalBlock();
        alb.setChildren(List.of(slb1, slb2));
        Condition actualCondition = alb.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  (teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"REL_CD_sourceIds_ODUFUNCTION_PROVIDES_NRCELLDU\"::text like '%urn:cmHandle:%')\n" +
                "  and (teiv_data.\"o-ran-smo-teiv-ran_NRSectorCarrier\".\"REL_CD_sourceIds_ODUFUNCTION_PROVIDES_NRSECTORCARRIER\" @> '\"urn:3gpp:dn:/SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/ODUFunction=16\"')\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_entityDecorators() {
        ScopeObject scopeObject1 = ScopeObject.builder(ODU_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.DECORATORS).leaf("odu-function-model:location").queryFunction(
                        QueryFunction.CONTAINS).parameter("Stock").dataType(DataType.PRIMITIVE).build();
        ScopeObject scopeObject2 = ScopeObject.builder(OCUCP_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.DECORATORS).leaf("odu-function-model:stringdata").queryFunction(QueryFunction.EQ)
                .parameter("ASD").dataType(DataType.PRIMITIVE).build();
        ScopeObject scopeObject3 = ScopeObject.builder(OCUCP_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.DECORATORS).leaf("odu-function-model:intdata").queryFunction(QueryFunction.EQ)
                .parameter("2").dataType(DataType.BIGINT).build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);
        LogicalBlock slb3 = new ScopeLogicalBlock(scopeObject3);
        AndOrLogicalBlock alb1 = new AndLogicalBlock();
        AndOrLogicalBlock alb2 = new AndLogicalBlock();
        alb1.setChildren(List.of(slb1, slb2));
        alb2.setChildren(List.of(alb1, slb3));
        Condition actualCondition = alb2.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"CD_decorators\" ->> 'odu-function-model:location' like '%Stock%'\n" +
                "  and teiv_data.\"o-ran-smo-teiv-ran_OCUCPFunction\".\"CD_decorators\" -> 'odu-function-model:stringdata' = '\"ASD\"'\n" +
                "  and teiv_data.\"o-ran-smo-teiv-ran_OCUCPFunction\".\"CD_decorators\" -> 'odu-function-model:intdata' = '2'\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_entityMetadata() {
        ScopeObject scopeObject1 = ScopeObject.builder(ODU_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.METADATA).leaf("reliabilityIndicator").queryFunction(QueryFunction.EQ).parameter(
                        "OK").dataType(DataType.PRIMITIVE).build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        AndOrLogicalBlock alb1 = new AndLogicalBlock();
        AndOrLogicalBlock alb2 = new AndLogicalBlock();
        alb1.setChildren(List.of(slb1));
        Condition actualCondition = slb1.getCondition();
        // spotless:off
        assertEquals(condition("teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"metadata\" -> 'reliabilityIndicator' = '\"OK\"'").toString()
            , actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_entityMetadata_lastModified() {
        ScopeObject scopeObject1 = ScopeObject.builder(ODU_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.METADATA).leaf("lastModified").queryFunction(QueryFunction.LESS_THAN).parameter(
                        "2025-01-21T11:12:48.628172460Z").dataType(DataType.TIMESTAMPTZ).build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        AndOrLogicalBlock alb1 = new AndLogicalBlock();
        AndOrLogicalBlock alb2 = new AndLogicalBlock();
        alb1.setChildren(List.of(slb1));
        Condition actualCondition = slb1.getCondition();
        // spotless:off
        assertEquals(condition("(teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"metadata\" ->> 'lastModified')::timestamptz < timestamp with time zone '2025-01-21 11:12:48.62817246+00:00'").toString()
            , actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_entityMetadata_firstDiscovered() {
        ScopeObject scopeObject1 = ScopeObject.builder(ODU_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.METADATA).leaf("firstDiscovered").queryFunction(QueryFunction.GREATER_THAN)
                .parameter("2025-01-21T11:12:48.628172460Z").dataType(DataType.TIMESTAMPTZ).build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        AndOrLogicalBlock alb1 = new AndLogicalBlock();
        alb1.setChildren(List.of(slb1));
        Condition actualCondition = slb1.getCondition();
        // spotless:off
        assertEquals(condition("(teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"metadata\" ->> 'firstDiscovered')::timestamptz > timestamp with time zone '2025-01-21 11:12:48.62817246+00:00'").toString()
            , actualCondition.toString());
        // spotless:on
    }

    @Test
    void testConditions_relationshipDecorators() {
        ScopeObject scopeObject1 = ScopeObject.builder("ODUFUNCTION_PROVIDES_NRCELLDU").topologyObjectType(
                TopologyObjectType.RELATION).container(ContainerType.DECORATORS).leaf("odu-function-model:location")
                .queryFunction(QueryFunction.CONTAINS).parameter("Stock").dataType(DataType.PRIMITIVE).build();
        ScopeObject scopeObject2 = ScopeObject.builder("ODUFUNCTION_PROVIDES_NRSECTORCARRIER").topologyObjectType(
                TopologyObjectType.RELATION).container(ContainerType.DECORATORS).leaf("odu-function-model:stringdata")
                .queryFunction(QueryFunction.EQ).parameter("ASD").dataType(DataType.PRIMITIVE).build();
        ScopeObject scopeObject3 = ScopeObject.builder("ODUFUNCTION_PROVIDES_NRSECTORCARRIER").topologyObjectType(
                TopologyObjectType.RELATION).container(ContainerType.DECORATORS).leaf("odu-function-model:intdata")
                .queryFunction(QueryFunction.EQ).parameter("2").dataType(DataType.BIGINT).build();
        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);
        LogicalBlock slb3 = new ScopeLogicalBlock(scopeObject3);
        AndOrLogicalBlock alb1 = new AndLogicalBlock();
        AndOrLogicalBlock alb2 = new AndLogicalBlock();
        alb1.setChildren(List.of(slb1, slb2));
        alb2.setChildren(List.of(alb1, slb3));
        Condition actualCondition = alb2.getCondition();
        // spotless:off
        assertEquals(condition(
            "\n" +
                "  teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"REL_CD_decorators_ODUFUNCTION_PROVIDES_NRCELLDU\" ->> 'odu-function-model:location' like '%Stock%'\n" +
                "  and teiv_data.\"o-ran-smo-teiv-ran_NRSectorCarrier\".\"REL_CD_decorators_ODUFUNCTION_PROVIDES_NRSECTORCARRIER\" -> 'odu-function-model:stringdata' = '\"ASD\"'\n" +
                "  and teiv_data.\"o-ran-smo-teiv-ran_NRSectorCarrier\".\"REL_CD_decorators_ODUFUNCTION_PROVIDES_NRSECTORCARRIER\" -> 'odu-function-model:intdata' = '2'\n")
            .toString(), actualCondition.toString());
        // spotless:on
    }

    @Test
    void testGetJoinCondition() {
        LogicalBlock slb1 = new ScopeLogicalBlock(ScopeObject.builder("ODUFunction/provided-nrCellDu").topologyObjectType(
                TopologyObjectType.ASSOCIATION).innerContainer(List.of()).build());
        InnerFilterCriteria innerFilterCriteria1 = InnerFilterCriteria.builder().scope(slb1).build();
        Pair<String, Field> pair1 = new ImmutablePair<>("teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\"", field(
                "teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"REL_FK_provided-by-oduFunction\"" + "=" + "teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"id\""));
        Assertions.assertEquals(new HashSet(Arrays.asList(pair1)), innerFilterCriteria1.builder().scope(slb1).build()
                .getJoinCondition());
        LogicalBlock slb2 = new ScopeLogicalBlock(ScopeObject.builder("AntennaCapability/serving-antennaModule")
                .topologyObjectType(TopologyObjectType.ASSOCIATION).container(ContainerType.ID).innerContainer(List.of())
                .build());
        InnerFilterCriteria innerFilterCriteria2 = InnerFilterCriteria.builder().scope(slb2).build();
        Pair<String, Field> pair2 = new ImmutablePair<>("teiv_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\"", field(
                "teiv_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\".\"bSide_AntennaCapability\"" + "=" + "teiv_data.\"o-ran-smo-teiv-ran_AntennaCapability\".\"id\""));
        Assertions.assertEquals(new HashSet(Arrays.asList(pair2)), innerFilterCriteria2.builder().scope(slb2).build()
                .getJoinCondition());
    }
}
