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
package org.oran.smo.teiv.exposure.tiespath.innerlanguage;

import static org.oran.smo.teiv.utils.TiesConstants.ITEM;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.oran.smo.teiv.exposure.tiespath.resolver.ResolverDataType;
import org.oran.smo.teiv.utils.query.exception.TiesPathException;
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
    private static final String NRCELLDU = "NRCellDU";
    private static final String SECTOR = "Sector";

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

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION\" is not null\n" +
                "  or ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION\" is not null\n")
            .toString(), getTestOrCondition(List.of(lb1, lb2)).toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAttributes_primitive() {
        ScopeObject scopeObject1 = ScopeObject.builder(NRCELLDU).topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ATTRIBUTES).leaf("nRPCI").queryFunction(QueryFunction.EQ).parameter("ABC789").dataType(
                        DataType.PRIMITIVE).build();

        ScopeObject scopeObject2 = ScopeObject.builder(NRCELLDU).topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ATTRIBUTES).leaf("nRPCI").queryFunction(QueryFunction.CONTAINS).parameter("ABC789").dataType(
                        DataType.PRIMITIVE).build();

        ScopeObject scopeObject3 = ScopeObject.builder(NRCELLDU).topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ATTRIBUTES).leaf("nRPCI").queryFunction(QueryFunction.NOT_NULL).dataType(DataType.PRIMITIVE)
                .build();

        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);
        LogicalBlock slb3 = new ScopeLogicalBlock(scopeObject3);

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"nRPCI\" = 'ABC789'\n" +
                "  or ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"nRPCI\" like (('%' || replace(\n" +
                "    replace(\n" +
                "      replace('ABC789', '!', '!!'),\n" +
                "      '%',\n" +
                "      '!%'\n" +
                "    ),\n" +
                "    '_',\n" +
                "    '!_'\n" +
                "  )) || '%') escape '!'\n" +
                "  or ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"nRPCI\" is not null\n")
            .toString(), getTestOrCondition(List.of(slb1, slb2, slb3)).toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAttributes_bigint() {
        ScopeObject scopeObject1 = ScopeObject.builder(NRCELLDU).topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ATTRIBUTES).leaf("gNBId").queryFunction(QueryFunction.EQ).parameter("1").dataType(
                        DataType.BIGINT).build();

        ScopeObject scopeObject2 = ScopeObject.builder(NRCELLDU).topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ATTRIBUTES).leaf("gNBId").queryFunction(QueryFunction.EQ).parameter("2").dataType(
                        DataType.BIGINT).build();

        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"gNBId\" = 1\n" +
                "  or ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"gNBId\" = 2\n")
            .toString(), getTestOrCondition(List.of(slb1, slb2)).toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAttributes_decimal() {
        ScopeObject scopeObject1 = ScopeObject.builder(NRCELLDU).topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ATTRIBUTES).leaf("decimalColumn").queryFunction(QueryFunction.EQ).parameter("2.5").dataType(
                        DataType.DECIMAL).build();

        ScopeObject scopeObject2 = ScopeObject.builder(NRCELLDU).topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ATTRIBUTES).leaf("decimalColumn").queryFunction(QueryFunction.EQ).parameter("15.25").dataType(
                        DataType.DECIMAL).build();

        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"decimalColumn\" = 2.5\n" +
                "  or ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"decimalColumn\" = 15.25\n")
            .toString(), getTestOrCondition(List.of(slb1, slb2)).toString());
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

        ScopeObject valid3 = ScopeObject.builder("AntennaModule").topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ATTRIBUTES).leaf("geoColumn").queryFunction(QueryFunction.EQ).parameter(
                        "POINT Z (39.4019881 67.9419888 123.9878)").dataType(DataType.GEOGRAPHIC).build();

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
                        "POINT(39.4019881 INVALID)").dataType(DataType.GEOGRAPHIC).build();

        ScopeObject invalidCoordinate4 = ScopeObject.builder("AntennaModule").topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).leaf("geoColumn").queryFunction(QueryFunction.EQ).parameter(
                        "POINT Z (39.4019881 67.9419888)").dataType(DataType.GEOGRAPHIC).build();

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
        assertEquals(condition("ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"geoColumn\" = st_geomfromtext('point(39.4019881 67.9419888)')")
            .toString(), new ScopeLogicalBlock(valid1).getCondition().toString());
        assertEquals(condition("ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"geoColumn\" = st_geomfromtext('POINT(39.4019881 67.9419888)')")
            .toString(), new ScopeLogicalBlock(valid2).getCondition().toString());
        assertEquals(condition("ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"geoColumn\" = st_geomfromtext('POINT Z (39.4019881 67.9419888 123.9878)')")
            .toString(), new ScopeLogicalBlock(valid3).getCondition().toString());

        assertThrows(TiesPathException.class, new ScopeLogicalBlock(invalidContains)::getCondition);
        assertThrows(TiesPathException.class, new ScopeLogicalBlock(invalidCoordinate0)::getCondition);
        assertThrows(TiesPathException.class, new ScopeLogicalBlock(invalidCoordinate1)::getCondition);
        assertThrows(TiesPathException.class, new ScopeLogicalBlock(invalidCoordinate2)::getCondition);
        assertThrows(TiesPathException.class, new ScopeLogicalBlock(invalidCoordinate3)::getCondition);
        assertThrows(TiesPathException.class, new ScopeLogicalBlock(invalidCoordinate4)::getCondition);
        assertThrows(TiesPathException.class, new ScopeLogicalBlock(invalidFormat1)::getCondition);
        assertThrows(TiesPathException.class, new ScopeLogicalBlock(invalidFormat2)::getCondition);
        assertThrows(TiesPathException.class, new ScopeLogicalBlock(invalidFormat3)::getCondition);
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

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"dUpLMNId\" -> 'mnc' = '\"789\"'\n" +
                "  or ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"dUpLMNId\" ->> 'mcc' like '%456%'\n")
            .toString(), getTestOrCondition(List.of(slb1, slb2)).toString());

        assertThrows(TiesPathException.class, new ScopeLogicalBlock(invalidQueryFunction)::getCondition);
        // spotless:on
    }

    @Test
    void testConditions_entityAttributes_containerObject_multipleElements() {
        ScopeObject scopeObject1 = ScopeObject.builder(ODU_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).innerContainer(List.of("dUpLMNId", "mcc")).leaf("mcca").queryFunction(
                        QueryFunction.EQ).parameter("789").dataType(DataType.PRIMITIVE).build();

        ScopeObject scopeObject2 = ScopeObject.builder(ODU_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).innerContainer(List.of("dUpLMNId", "mcc")).leaf("mccb").queryFunction(
                        QueryFunction.EQ).parameter("789").dataType(DataType.INTEGER).build();

        ScopeObject scopeObject3 = ScopeObject.builder(ODU_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).innerContainer(List.of("dUpLMNId", "mcc")).leaf("mcca").queryFunction(
                        QueryFunction.CONTAINS).parameter("789").dataType(DataType.PRIMITIVE).build();

        LogicalBlock lb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock lb2 = new ScopeLogicalBlock(scopeObject2);
        LogicalBlock lb3 = new ScopeLogicalBlock(scopeObject3);

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"dUpLMNId\" -> 'mcc' -> 'mcca' = '\"789\"'\n" +
                "  or ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"dUpLMNId\" -> 'mcc' -> 'mccb' = '789'\n" +
                "  or ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"dUpLMNId\" -> 'mcc' ->> 'mcca' like '%789%'\n")
            .toString(), getTestOrCondition(List.of(lb1, lb2, lb3)).toString());
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

        assertThrows(TiesPathException.class, scopeObject::getCondition);
    }

    @Test
    void testConditions_geoLocation_withinMeters_invalidParameter2() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "'POINT (12.3426 45.24568 345.6)', 500").resolverDataType(ResolverDataType.STRING).queryFunction(
                                QueryFunction.WITHIN_METERS).build());

        assertThrows(TiesPathException.class, scopeObject::getCondition);
    }

    @Test
    void testConditions_geoLocation_withinMeters_invalidParameter3() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "'POINT (12.3426 45.)', 500").resolverDataType(ResolverDataType.STRING).queryFunction(
                                QueryFunction.WITHIN_METERS).build());

        assertThrows(TiesPathException.class, scopeObject::getCondition);
    }

    @Test
    void testConditions_geoLocation_withinMeters_invalidParameter4() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "'POINT (12.3426)', 500").resolverDataType(ResolverDataType.STRING).queryFunction(
                                QueryFunction.WITHIN_METERS).build());

        assertThrows(TiesPathException.class, scopeObject::getCondition);
    }

    @Test
    void testConditions_geoLocation_withinMeters_invalidParameter5() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "'(12.3426 345.533)', 500").resolverDataType(ResolverDataType.STRING).queryFunction(
                                QueryFunction.WITHIN_METERS).build());

        assertThrows(TiesPathException.class, scopeObject::getCondition);
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

        assertThrows(TiesPathException.class, scopeObject::getCondition);
    }

    @Test
    void testConditions_geoLocation_coveredBy_invalidParameter2() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "POLYGON(12.3426 45.24568, 13.3426 45.24568, 12.3426 44.24568, 13.3426 44.)").resolverDataType(
                                ResolverDataType.STRING).queryFunction(QueryFunction.COVERED_BY).build());

        assertThrows(TiesPathException.class, scopeObject::getCondition);
    }

    @Test
    void testConditions_geoLocation_coveredBy_invalidParameter3() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "POLYGON(12.3426 45.24568, 13.3426 45.24568, 12.3426 44.24568, 13.3426 44.24568 34.63)")
                .resolverDataType(ResolverDataType.STRING).queryFunction(QueryFunction.COVERED_BY).build());

        assertThrows(TiesPathException.class, scopeObject::getCondition);
    }

    @Test
    void testConditions_geoLocation_coveredBy_invalidParameter4() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "POLYGON(12.3426 45.24568, 13.3426 45.24568, 12.3426 44.24568, 13.3426 44.24568))")
                .resolverDataType(ResolverDataType.STRING).queryFunction(QueryFunction.COVERED_BY).build());

        assertThrows(TiesPathException.class, scopeObject::getCondition);
    }

    @Test
    void testConditions_geoLocation_coveredBy_invalidParameter5() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").dataType(DataType.GEOGRAPHIC).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "(12.3426 45.24568, 13.3426 45.24568, 12.3426 44.24568, 13.3426 44.24568)").resolverDataType(
                                ResolverDataType.STRING).queryFunction(QueryFunction.COVERED_BY).build());

        assertThrows(TiesPathException.class, scopeObject::getCondition);
    }

    @Test
    void testConditions_geoLocation_coveredBy_invalidDataType() {
        ScopeLogicalBlock scopeObject = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("mcca").dataType(DataType.PRIMITIVE).topologyObjectType(TopologyObjectType.ENTITY).parameter(
                        "POLYGON (12.3426 45.24568, 13.3426 45.24568, 12.3426 44.24568, 13.3426 44.24568)")
                .resolverDataType(ResolverDataType.STRING).queryFunction(QueryFunction.COVERED_BY).build());

        assertThrows(TiesPathException.class, scopeObject::getCondition);
    }

    @Test
    void testConditions_entityAttributes_containerArray() {
        ScopeObject scopeObject1 = ScopeObject.builder(ANTENNA_CAPABILITY).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).innerContainer(List.of("eUtranFqBands")).leaf(ITEM).queryFunction(
                        QueryFunction.EQ).parameter("789").dataType(DataType.PRIMITIVE).build();

        ScopeObject scopeObject2 = ScopeObject.builder(ANTENNA_CAPABILITY).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).innerContainer(List.of("eUtranFqBands")).leaf(ITEM).queryFunction(
                        QueryFunction.EQ).parameter("789").dataType(DataType.INTEGER).build();

        ScopeObject scopeObject3 = ScopeObject.builder(ANTENNA_CAPABILITY).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ATTRIBUTES).innerContainer(List.of("eUtranFqBands")).leaf(ITEM).queryFunction(
                        QueryFunction.CONTAINS).parameter("456").dataType(DataType.PRIMITIVE).build();

        LogicalBlock lb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock lb2 = new ScopeLogicalBlock(scopeObject2);
        LogicalBlock lb3 = new ScopeLogicalBlock(scopeObject3);

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  (ties_data.\"o-ran-smo-teiv-ran_AntennaCapability\".\"eUtranFqBands\" @> '\"789\"')\n" +
                "  or (ties_data.\"o-ran-smo-teiv-ran_AntennaCapability\".\"eUtranFqBands\" @> '789')\n" +
                "  or (ties_data.\"o-ran-smo-teiv-ran_AntennaCapability\".\"eUtranFqBands\"::text like '%456%')\n")
            .toString(), getTestOrCondition(List.of(lb1, lb2, lb3)).toString());
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

        LogicalBlock lb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock lb2 = new ScopeLogicalBlock(scopeObject2);
        LogicalBlock lb3 = new ScopeLogicalBlock(scopeObject3);

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  (ties_data.\"o-ran-smo-teiv-ran_AntennaCapability\".\"jsonbColumn\" -> 'levelOneField' -> 'levelTwoField' @> '\"456\"')\n"
                +
                "  or (ties_data.\"o-ran-smo-teiv-ran_AntennaCapability\".\"jsonbColumn\" -> 'levelOneField' -> 'levelTwoField' @> '456')\n"
                +
                "  or (ties_data.\"o-ran-smo-teiv-ran_AntennaCapability\".\"jsonbColumn\" -> 'levelOneField' ->> 'levelTwoField'::text like '%456%')\n")
            .toString(), getTestOrCondition(List.of(lb1, lb2, lb3)).toString());
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

        ScopeObject scopeObject3 = ScopeObject.builder(NRCELLDU).topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ATTRIBUTES).leaf("nRPCI").queryFunction(QueryFunction.CONTAINS).parameter("ABC789").dataType(
                        DataType.PRIMITIVE).build();

        ScopeObject scopeObject4 = ScopeObject.builder(NRCELLDU).topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ATTRIBUTES).innerContainer(List.of("pLMNId")).leaf("mnc").queryFunction(QueryFunction.EQ)
                .parameter("789").dataType(DataType.PRIMITIVE).build();

        LogicalBlock lb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock lb2 = new ScopeLogicalBlock(scopeObject2);
        LogicalBlock lb3 = new ScopeLogicalBlock(scopeObject3);
        LogicalBlock lb4 = new ScopeLogicalBlock(scopeObject4);

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"gNBId\" = 1\n" +
                "  and ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"decimalId\" = 2.5\n"
                +
                "  and ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"nRPCI\" like (('%' || replace(\n"
                +
                "    replace(\n" +
                "      replace('ABC789', '!', '!!'),\n" +
                "      '%',\n" +
                "      '!%'\n" +
                "    ),\n" +
                "    '_',\n" +
                "    '!_'\n" +
                "  )) || '%') escape '!'\n" +
                "  and ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"pLMNId\" -> 'mnc' = '\"789\"'\n")
            .toString(), getTestAndCondition(List.of(lb1, lb2, lb3, lb4)).toString());
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

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"rel_column1\" = 1\n" +
                "  and ties_data.\"o-ran-smo-teiv-ran_NRSectorCarrier\".\"rel_column2\" = 'ABC789'\n")
            .toString(), getTestAndCondition(List.of(slb1, slb2)).toString());
        // spotless:on
    }

    @Test
    void testConditions_undefinedAttributes_throws() {
        ScopeObject scopeObject1 = ScopeObject.builder("ODUFUNCTION_PROVIDES_NRCELLDU").topologyObjectType(
                TopologyObjectType.UNDEFINED).container(ContainerType.ATTRIBUTES).leaf("rel_column1").queryFunction(
                        QueryFunction.EQ).parameter("1").dataType(DataType.BIGINT).build();

        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);

        assertThrows(TiesPathException.class, slb1::getCondition);
    }

    @Test
    void testConditions_relation() {
        ScopeObject scopeObject1 = ScopeObject.builder(NRCELLDU).topologyObjectType(TopologyObjectType.ENTITY).container(
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

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"REL_ID_ODUFUNCTION_PROVIDES_NRCELLDU\" = 'urn:base64:TWFuYWdlZEV'\n"
                +
                "  and ties_data.\"o-ran-smo-teiv-ran_NRSectorCarrier\".\"REL_ID_ODUFUNCTION_PROVIDES_NRSECTORCARRIER\" = 'urn:base64:TWFuYWdlZEW'\n"
                +
                "  and ties_data.\"o-ran-smo-teiv-ran_NRSectorCarrier\".\"REL_ID_ODUFUNCTION_PROVIDES_NRSECTORCARRIER\" = 'urn:base64:TWFuYWdlZEZ'\n")
            .toString(), getTestAndCondition(List.of(slb1, slb2, slb3)).toString());
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

        assertThrows(TiesPathException.class, slb1::getCondition);
        assertThrows(TiesPathException.class, slb2::getCondition);
        assertThrows(TiesPathException.class, slb3::getCondition);
    }

    @Test
    void testConditions_entityId() {
        ScopeObject scopeObject1 = ScopeObject.builder(ODU_FUNCTION).topologyObjectType(TopologyObjectType.ENTITY)
                .container(ContainerType.ID).queryFunction(QueryFunction.EQ).parameter("ABC123").dataType(
                        DataType.PRIMITIVE).build();

        ScopeObject scopeObject2 = ScopeObject.builder(NRCELLDU).topologyObjectType(TopologyObjectType.ENTITY).container(
                ContainerType.ID).queryFunction(QueryFunction.CONTAINS).parameter("DEF456").dataType(DataType.PRIMITIVE)
                .build();

        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);
        LogicalBlock slb2 = new ScopeLogicalBlock(scopeObject2);

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"id\" = 'ABC123'\n" +
                "  and ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"id\" like (('%' || replace(\n"
                +
                "    replace(\n" +
                "      replace('DEF456', '!', '!!'),\n" +
                "      '%',\n" +
                "      '!%'\n" +
                "    ),\n" +
                "    '_',\n" +
                "    '!_'\n" +
                "  )) || '%') escape '!'\n")
            .toString(), getTestAndCondition(List.of(slb1, slb2)).toString());
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
                "  ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"REL_ID_ODUFUNCTION_PROVIDES_NRCELLDU\" = 'urn:base64:TWFuYWdlZEV'\n" +
                "  and ties_data.\"o-ran-smo-teiv-ran_NRSectorCarrier\".\"REL_ID_ODUFUNCTION_PROVIDES_NRSECTORCARRIER\" like (('%' || replace(\n" +
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

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"REL_ID_ODUFUNCTION_PROVIDES_NRCELLDU\" is not null\n"
                +
                "  and ties_data.\"o-ran-smo-teiv-ran_NRSectorCarrier\".\"REL_ID_ODUFUNCTION_PROVIDES_NRSECTORCARRIER\" is not null\n")
            .toString(), getTestAndCondition(List.of(slb1, slb2)).toString());
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

        assertThrows(TiesPathException.class, undefinedTOType::getCondition);
        assertThrows(TiesPathException.class, invalidTopologyObjectType::getCondition);
    }

    @Test
    void testConditions_entityAssociation_oneToMany_equals_id() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder("ORUFunction").topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "managed-by-managedElement")).leaf("id").queryFunction(QueryFunction.EQ).parameter("me1").build());

        ScopeLogicalBlock scopeObject2 = new ScopeLogicalBlock(ScopeObject.builder(ODU_FUNCTION).topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "managed-by-managedElement")).leaf(null).queryFunction(QueryFunction.NOT_NULL).parameter(null)
                .build());

        // spotless:off
        assertEquals(condition(
                "\n" +
                    "  ties_data.\"o-ran-smo-teiv-ran_ORUFunction\".\"REL_FK_managed-by-managedElement\" = 'me1'\n"
                    +
                    "  and ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"REL_FK_managed-by-managedElement\" is not null\n")
                .toString(),
            getTestAndCondition(List.of(scopeObject1, scopeObject2)).toString());
        // spotless:on

        ScopeLogicalBlock scopeObject3 = new ScopeLogicalBlock(ScopeObject.builder(MANAGED_ELEMENT).topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "managed-oduFunction")).leaf("id").queryFunction(QueryFunction.EQ).parameter("odu1").build());

        ScopeLogicalBlock scopeObject4 = new ScopeLogicalBlock(ScopeObject.builder(MANAGED_ELEMENT).topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "managed-ocucpFunction")).leaf(null).queryFunction(QueryFunction.NOT_NULL).parameter(null).build());

        // spotless:off
        assertEquals(condition(
                "\n" +
                    "  ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"REL_FK_managed-by-managedElement\" is not null\n"
                    +
                    "  and ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"id\" = 'odu1'\n"
                    +
                    "  and ties_data.\"o-ran-smo-teiv-ran_OCUCPFunction\".\"REL_FK_managed-by-managedElement\" is not null\n")
                .toString(),
            getTestAndCondition(List.of(scopeObject3, scopeObject4)).toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAssociation_oneToMany_equals_attribute() {
        ScopeLogicalBlock associationScope1 = new ScopeLogicalBlock(ScopeObject.builder(NRCELLDU).topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of("grouped-by-sector"))
                .leaf("geoColumn").queryFunction(QueryFunction.EQ).parameter("point(39.4019881 67.9419888)").dataType(
                        DataType.GEOGRAPHIC).build());
        // spotless:off
        assertEquals(condition(
            "ties_data.\"o-ran-smo-teiv-ran_Sector\".\"geoColumn\" = st_geomfromtext('point(39.4019881 67.9419888)')")
            .toString(), associationScope1.getCondition().toString());
        // spotless:on

        // Condition for Sectors where NRCellDU's nCI equals..
        ScopeLogicalBlock associationScope2 = new ScopeLogicalBlock(ScopeObject.builder(SECTOR).topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of("grouped-nrCellDu"))
                .leaf("nCI").queryFunction(QueryFunction.EQ).parameter("400").dataType(DataType.BIGINT).build());
        // spotless:off
        assertEquals(condition("\n" +
                "  ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"REL_FK_grouped-by-sector\" is not null\n"
                +
                "  and ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"nCI\" = 400\n").toString(),
            associationScope2.getCondition().toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAssociation_oneToMany_contains_id() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder("ORUFunction").topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "managed-by-managedElement")).leaf("id").queryFunction(QueryFunction.CONTAINS).parameter("me1")
                .build());

        Condition actualCondition = scopeObject1.getCondition();
        // spotless:off
        assertEquals(
            ("ties_data.\"o-ran-smo-teiv-ran_ORUFunction\".\"REL_FK_managed-by-managedElement\"like(('%'||replace("
                + "\n" +
                "replace(" + "\n" +
                "replace('me1','!','!!')," + "\n" +
                "'%'," + "\n" +
                "'!%'" + "\n" +
                ")," + "\n" +
                "'_'," + "\n" +
                "'!_'" + "\n" +
                "))||'%')escape'!'").replace(" ", ""),
            actualCondition.toString().replace(" ", ""));
        // spotless:on
    }

    @Test
    void testConditions_entityAssociation_manyToOne_equals_id() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder(ANTENNA_MODULE).topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of("installed-at-site"))
                .leaf("id").queryFunction(QueryFunction.EQ).parameter("site1").build());

        ScopeLogicalBlock scopeObject2 = new ScopeLogicalBlock(ScopeObject.builder(ANTENNA_MODULE).topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of("installed-at-site"))
                .leaf(null).queryFunction(QueryFunction.NOT_NULL).parameter(null).build());

        // spotless:off
        assertEquals(condition(
                "\n" +
                    "  ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"REL_FK_installed-at-site\" = 'site1'\n"
                    +
                    "  or ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"REL_FK_installed-at-site\" is not null\n")
                .toString(),
            getTestOrCondition(List.of(scopeObject1, scopeObject2)).toString());
        // spotless:on

        ScopeLogicalBlock scopeObject3 = new ScopeLogicalBlock(ScopeObject.builder("Site").topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "installed-antennaModule")).leaf("id").queryFunction(QueryFunction.EQ).parameter("am1").build());

        ScopeLogicalBlock scopeObject4 = new ScopeLogicalBlock(ScopeObject.builder("Site").topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "installed-antennaModule")).leaf(null).queryFunction(QueryFunction.NOT_NULL).parameter(null)
                .build());

        // spotless:off
        assertEquals(condition(
                "\n" +
                    "  (\n" +
                    "    ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"REL_FK_installed-at-site\" is not null\n"
                    +
                    "    and ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"id\" = 'am1'\n"
                    +
                    "  )\n" +
                    "  or ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"REL_FK_installed-at-site\" is not null\n")
                .toString(),
            getTestOrCondition(List.of(scopeObject3, scopeObject4)).toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAssociation_manyToOne_equals_attribute() {
        ScopeLogicalBlock associationScope1 = new ScopeLogicalBlock(ScopeObject.builder("AntennaModule").topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of("installed-at-site"))
                .leaf("geoColumn").queryFunction(QueryFunction.EQ).parameter("point(39.4019881 67.9419888)").dataType(
                        DataType.GEOGRAPHIC).build());
        // spotless:off
        assertEquals(condition(
            "((ties_data.\"o-ran-smo-teiv-equipment_Site\".\"geoColumn\" = st_geomfromtext('point(39.4019881 67.9419888)')))")
            .toString(), String.format("((%s))", associationScope1.getCondition().toString()));
        // spotless:on

        ScopeLogicalBlock associationScope2 = new ScopeLogicalBlock(ScopeObject.builder("Site").topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "installed-antennaModule")).leaf("electronicalAntennaTilt").queryFunction(QueryFunction.EQ)
                .parameter("10").dataType(DataType.BIGINT).build());

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"REL_FK_installed-at-site\" is not null\n"
                +
                "  and ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"electronicalAntennaTilt\" = 10\n")
            .toString(), associationScope2.getCondition().toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAssociation_manyToOne_contains_id() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder("Site").topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "installed-antennaModule")).leaf("id").queryFunction(QueryFunction.CONTAINS).parameter("am1")
                .build());

        // spotless:off
        assertEquals(condition(
            "\n" +
                "ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"REL_FK_installed-at-site\" is not null\n"
                +
                "and ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"id\" like ("
                +
                "('%' || replace(" + "\n" +
                "replace(" + "\n" +
                "replace('am1', '!', '!!')," + "\n" +
                "'%'," + "\n" +
                "'!%'" + "\n" +
                ")," + "\n" +
                "'_'," + "\n" +
                "'!_'" + "\n" +
                ")) || '%') escape '!'" + "\n")
            .toString().replace(" ", ""), scopeObject1.getCondition().toString().replace(" ", ""));
        // spotless:on

        ScopeLogicalBlock scopeObject2 = new ScopeLogicalBlock(ScopeObject.builder(ANTENNA_MODULE).topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "serviced-antennaCapability")).leaf("id").queryFunction(QueryFunction.CONTAINS).parameter("ac1")
                .build());

        // spotless:off
        assertEquals(("ties_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\"" +
            ".\"bSide_AntennaCapability\"like(('%'||replace(" + "\n" +
            "replace(" + "\n" +
            "replace('ac1','!','!!')," + "\n" +
            "'%'," + "\n" +
            "'!%'" + "\n" +
            ")," + "\n" +
            "'_'," + "\n" +
            "'!_'" + "\n" +
            "))||'%')escape'!'")
            .replace(" ", ""), scopeObject2.getCondition().toString().replace(" ", ""));
        // spotless:on
    }

    @Test
    void testConditions_entityAssociation_manyToMany_equals_id() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder(ANTENNA_MODULE).topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "serviced-antennaCapability")).leaf("id").queryFunction(QueryFunction.EQ).parameter("ac1").build());

        ScopeLogicalBlock scopeObject2 = new ScopeLogicalBlock(ScopeObject.builder(ANTENNA_MODULE).topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "serviced-antennaCapability")).leaf(null).queryFunction(QueryFunction.NOT_NULL).parameter(null)
                .build());

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  ties_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\".\"bSide_AntennaCapability\" = 'ac1'\n"
                +
                "  and ties_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\".id is not null\n")
            .toString(), getTestAndCondition(List.of(scopeObject1, scopeObject2)).toString());
        // spotless:on

        ScopeLogicalBlock scopeObject3 = new ScopeLogicalBlock(ScopeObject.builder(ANTENNA_CAPABILITY).topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "serving-antennaModule")).leaf("id").queryFunction(QueryFunction.EQ).parameter("am1").build());

        ScopeLogicalBlock scopeObject4 = new ScopeLogicalBlock(ScopeObject.builder(ANTENNA_CAPABILITY).topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "serving-antennaModule")).leaf(null).queryFunction(QueryFunction.NOT_NULL).parameter(null).build());

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  ties_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\".\"aSide_AntennaModule\" = 'am1'\n"
                +
                "  and ties_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\".id is not null\n")
            .toString(), getTestAndCondition(List.of(scopeObject3, scopeObject4)).toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAssociation_manyToMany_contains_id() {
        ScopeLogicalBlock associationScope1 = new ScopeLogicalBlock(ScopeObject.builder("AntennaModule").topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "serviced-antennaCapability")).leaf("nRFqBands").queryFunction(QueryFunction.EQ).parameter(
                                "13252AC12BF").dataType(DataType.PRIMITIVE).build());

        // spotless:off
        assertEquals(condition(
            "ties_data.\"o-ran-smo-teiv-ran_AntennaCapability\".\"nRFqBands\" = '13252AC12BF'")
            .toString(), String.format("(%s)", associationScope1.getCondition().toString()));
        // spotless:on

        ScopeLogicalBlock associationScope2 = new ScopeLogicalBlock(ScopeObject.builder("AntennaCapability")
                .topologyObjectType(TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "serving-antennaModule")).leaf("id").queryFunction(QueryFunction.CONTAINS).parameter(
                                "AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A")
                .build());
        // spotless:off
        assertEquals(("ties_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\"" +
            ".\"aSide_AntennaModule\"like(('%'||replace(" + "\n" +
            "replace(" + "\n" +
            "replace('AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A','!','!!'),"
            + "\n" +
            "'%'," + "\n" +
            "'!%'" + "\n" +
            ")," + "\n" +
            "'_'," + "\n" +
            "'!_'" + "\n" +
            "))||'%')escape'!'")
            .replace(" ", ""), associationScope2.getCondition().toString().replace(" ", ""));
        // spotless:on
    }

    @Test
    void testConditions_entityAssociation_manyToMany_equals_attribute() {
        ScopeLogicalBlock associationScope1 = new ScopeLogicalBlock(ScopeObject.builder("AntennaModule").topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "serviced-antennaCapability")).leaf("nRFqBands").queryFunction(QueryFunction.EQ).parameter(
                                "13252AC12BF").dataType(DataType.PRIMITIVE).build());

        // spotless:off
        assertEquals(condition(
            "ties_data.\"o-ran-smo-teiv-ran_AntennaCapability\".\"nRFqBands\" = '13252AC12BF'")
            .toString(), String.format("(%s)", associationScope1.getCondition().toString()));
        // spotless:on

        ScopeLogicalBlock associationScope2 = new ScopeLogicalBlock(ScopeObject.builder("AntennaCapability")
                .topologyObjectType(TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "serving-antennaModule")).leaf("geoColumn").queryFunction(QueryFunction.EQ).parameter(
                                "point(39.4019881 67.9419888)").dataType(DataType.GEOGRAPHIC).build());
        // spotless:off
        assertEquals(condition(
            "ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"geoColumn\" = st_geomfromtext('point(39.4019881 67.9419888)')")
            .toString(), associationScope2.getCondition().toString());
        // spotless:on
    }

    @Test
    void testConditions_relationAssociation_oneToMany_equals_id() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder("MANAGEDELEMENT_MANAGES_ORUFUNCTION")
                .topologyObjectType(TopologyObjectType.RELATION).container(ContainerType.ASSOCIATION).innerContainer(List
                        .of("managed-by-managedElement")).leaf("id").queryFunction(QueryFunction.EQ).parameter("me1")
                .build());

        ScopeLogicalBlock scopeObject2 = new ScopeLogicalBlock(ScopeObject.builder("MANAGEDELEMENT_MANAGES_ODUFUNCTION")
                .topologyObjectType(TopologyObjectType.RELATION).container(ContainerType.ASSOCIATION).innerContainer(List
                        .of("managed-by-managedElement")).leaf(null).queryFunction(QueryFunction.NOT_NULL).parameter(null)
                .build());

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  ties_data.\"o-ran-smo-teiv-ran_ORUFunction\".\"REL_FK_managed-by-managedElement\" = 'me1'\n"
                +
                "  and ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"REL_FK_managed-by-managedElement\" is not null\n")
            .toString(), getTestAndCondition(List.of(scopeObject1, scopeObject2)).toString());
        // spotless:on

        ScopeLogicalBlock scopeObject3 = new ScopeLogicalBlock(ScopeObject.builder("MANAGEDELEMENT_MANAGES_ORUFUNCTION")
                .topologyObjectType(TopologyObjectType.RELATION).container(ContainerType.ASSOCIATION).innerContainer(List
                        .of("managed-oruFunction")).leaf("id").queryFunction(QueryFunction.EQ).parameter("oruf1").build());

        ScopeLogicalBlock scopeObject4 = new ScopeLogicalBlock(ScopeObject.builder("MANAGEDELEMENT_MANAGES_ODUFUNCTION")
                .topologyObjectType(TopologyObjectType.RELATION).container(ContainerType.ASSOCIATION).innerContainer(List
                        .of("managed-by-managedElement")).leaf(null).queryFunction(QueryFunction.NOT_NULL).parameter(null)
                .build());

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  ties_data.\"o-ran-smo-teiv-ran_ORUFunction\".\"REL_FK_managed-by-managedElement\" is not null\n"
                +
                "  and ties_data.\"o-ran-smo-teiv-ran_ORUFunction\".\"id\" = 'oruf1'\n"
                +
                "  and ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"REL_FK_managed-by-managedElement\" is not null\n")
            .toString(), getTestAndCondition(List.of(scopeObject3, scopeObject4)).toString());
        // spotless:on
    }

    @Test
    void testConditions_relationAssociation_manyToOne_equals_id() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder("ANTENNAMODULE_INSTALLED_AT_SITE")
                .topologyObjectType(TopologyObjectType.RELATION).container(ContainerType.ASSOCIATION).innerContainer(List
                        .of("installed-at-site")).leaf("id").queryFunction(QueryFunction.EQ).parameter("site1").build());

        ScopeLogicalBlock scopeObject2 = new ScopeLogicalBlock(ScopeObject.builder("ANTENNAMODULE_INSTALLED_AT_SITE")
                .topologyObjectType(TopologyObjectType.RELATION).container(ContainerType.ASSOCIATION).innerContainer(List
                        .of("installed-at-site")).leaf(null).queryFunction(QueryFunction.NOT_NULL).parameter(null).build());

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"REL_FK_installed-at-site\" = 'site1'\n"
                +
                "  or ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"REL_FK_installed-at-site\" is not null\n")
            .toString(), getTestOrCondition(List.of(scopeObject1, scopeObject2)).toString());
        // spotless:on

        ScopeLogicalBlock scopeObject3 = new ScopeLogicalBlock(ScopeObject.builder("ANTENNAMODULE_INSTALLED_AT_SITE")
                .topologyObjectType(TopologyObjectType.RELATION).container(ContainerType.ASSOCIATION).innerContainer(List
                        .of("installed-antennaModule")).leaf("id").queryFunction(QueryFunction.EQ).parameter("am1")
                .build());

        ScopeLogicalBlock scopeObject4 = new ScopeLogicalBlock(ScopeObject.builder("ANTENNAMODULE_INSTALLED_AT_SITE")
                .topologyObjectType(TopologyObjectType.RELATION).container(ContainerType.ASSOCIATION).innerContainer(List
                        .of("installed-antennaModule")).leaf(null).queryFunction(QueryFunction.NOT_NULL).parameter(null)
                .build());

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  (\n" +
                "    ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"REL_FK_installed-at-site\" is not null\n"
                +
                "    and ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"id\" = 'am1'\n"
                +
                "  )\n" +
                "  or ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"REL_FK_installed-at-site\" is not null\n")
            .toString(), getTestOrCondition(List.of(scopeObject3, scopeObject4)).toString());
        // spotless:on
    }

    @Test
    void testConditions_relationAssociation_manyToMany_equals_id() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder("ANTENNAMODULE_SERVES_ANTENNACAPABILITY")
                .topologyObjectType(TopologyObjectType.RELATION).container(ContainerType.ASSOCIATION).innerContainer(List
                        .of("serviced-antennaCapability")).leaf("id").queryFunction(QueryFunction.EQ).parameter("ac1")
                .build());

        ScopeLogicalBlock scopeObject2 = new ScopeLogicalBlock(ScopeObject.builder("ANTENNAMODULE_SERVES_ANTENNACAPABILITY")
                .topologyObjectType(TopologyObjectType.RELATION).container(ContainerType.ASSOCIATION).innerContainer(List
                        .of("serviced-antennaCapability")).leaf(null).queryFunction(QueryFunction.NOT_NULL).parameter(null)
                .build());

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  ties_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\".\"bSide_AntennaCapability\" = 'ac1'\n"
                +
                "  and ties_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\".id is not null\n")
            .toString(), getTestAndCondition(List.of(scopeObject1, scopeObject2)).toString());
        // spotless:on

        ScopeLogicalBlock scopeObject3 = new ScopeLogicalBlock(ScopeObject.builder("ANTENNAMODULE_SERVES_ANTENNACAPABILITY")
                .topologyObjectType(TopologyObjectType.RELATION).container(ContainerType.ASSOCIATION).innerContainer(List
                        .of("serving-antennaModule")).leaf("id").queryFunction(QueryFunction.EQ).parameter("am1").build());

        ScopeLogicalBlock scopeObject4 = new ScopeLogicalBlock(ScopeObject.builder("ANTENNAMODULE_SERVES_ANTENNACAPABILITY")
                .topologyObjectType(TopologyObjectType.RELATION).container(ContainerType.ASSOCIATION).innerContainer(List
                        .of("serving-antennaModule")).leaf(null).queryFunction(QueryFunction.NOT_NULL).parameter(null)
                .build());

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  ties_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\".\"aSide_AntennaModule\" = 'am1'\n"
                +
                "  and ties_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\".id is not null\n")
            .toString(), getTestAndCondition(List.of(scopeObject3, scopeObject4)).toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAssociation_sameEntity_equals_id() {
        ScopeLogicalBlock scopeObject1 = new ScopeLogicalBlock(ScopeObject.builder("AntennaModule").topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "serving-antennaModule")).leaf("id").queryFunction(QueryFunction.EQ).parameter("ac1").build());

        ScopeLogicalBlock scopeObject2 = new ScopeLogicalBlock(ScopeObject.builder("AntennaModule").topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "serving-antennaModule")).leaf(null).queryFunction(QueryFunction.NOT_NULL).parameter(null).build());

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  ties_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\".\"bSide_AntennaCapability\" = 'ac1'\n"
                +
                "  and ties_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\".id is not null\n")
            .toString(), getTestAndCondition(List.of(scopeObject1, scopeObject2)).toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAssociation_equals_attributes_nested_leaf() {
        ScopeLogicalBlock associationScope3 = new ScopeLogicalBlock(ScopeObject.builder(SECTOR).topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of("grouped-nrCellDu",
                        "plmnId")).leaf("mcc").queryFunction(QueryFunction.EQ).parameter("599").dataType(DataType.BIGINT)
                .build());
        // spotless:off
        assertEquals(condition("\n" +
                "  ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"REL_FK_grouped-by-sector\" is not null\n"
                +
                "  and ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"plmnId\" -> 'mcc' = 599\n")
                .toString(),
            associationScope3.getCondition().toString());
        // spotless:on

        ScopeLogicalBlock associationScope4 = new ScopeLogicalBlock(ScopeObject.builder(SECTOR).topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of("grouped-nrCellDu",
                        "plmnId", "mcc")).leaf("mcca").queryFunction(QueryFunction.EQ).parameter("599").dataType(
                                DataType.BIGINT).build());
        // spotless:off
        assertEquals(condition("\n" +
                "  ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"REL_FK_grouped-by-sector\" is not null\n"
                +
                "  and ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"plmnId\" -> 'mcc' -> 'mcca' = 599\n")
                .toString(),
            associationScope4.getCondition().toString());
        // spotless:on
    }

    @Test
    void testConditions_entityAssociation_throws() {
        ScopeLogicalBlock associationScope1 = new ScopeLogicalBlock(ScopeObject.builder("CloudNativeApplication").container(
                ContainerType.ASSOCIATION).innerContainer(List.of("realised-oduFunction")).leaf("id").queryFunction(
                        QueryFunction.CONTAINS).parameter("odu1").build());
        assertThrows(TiesPathException.class, associationScope1::getCondition);

        // Equals Condition with missing datatype
        ScopeLogicalBlock associationScope3 = new ScopeLogicalBlock(ScopeObject.builder(NRCELLDU).topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of("grouped-by-sector"))
                .leaf("geoColumn").queryFunction(QueryFunction.EQ).parameter("point(39.4019881 67.9419888)").build());
        assertThrows(TiesPathException.class, associationScope3::getCondition);

        // Contains Condition with missing datatype
        ScopeLogicalBlock associationScope4 = new ScopeLogicalBlock(ScopeObject.builder(NRCELLDU).topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of("grouped-by-sector"))
                .leaf("geoColumn").queryFunction(QueryFunction.CONTAINS).parameter("point(39.4019881 67.9419888)").build());
        assertThrows(TiesPathException.class, associationScope4::getCondition);

        // Equals Condition for Attributes on Relation
        ScopeLogicalBlock associationScope5 = new ScopeLogicalBlock(ScopeObject.builder(
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY").topologyObjectType(TopologyObjectType.RELATION).container(
                        ContainerType.ASSOCIATION).innerContainer(List.of("serving-antennaModule")).leaf("field")
                .queryFunction(QueryFunction.EQ).parameter("am1").build());
        assertThrows(TiesPathException.class, associationScope5::getCondition);

        // Contains Condition for Attributes on Relation
        ScopeLogicalBlock associationScope6 = new ScopeLogicalBlock(ScopeObject.builder(
                "ANTENNAMODULE_SERVES_ANTENNACAPABILITY").topologyObjectType(TopologyObjectType.RELATION).container(
                        ContainerType.ASSOCIATION).innerContainer(List.of("serving-antennaModule")).leaf("field")
                .queryFunction(QueryFunction.CONTAINS).parameter("am1").build());
        assertThrows(TiesPathException.class, associationScope6::getCondition);
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

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  (ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"CD_classifiers\"::text like '%odu-function-model:Ru%')\n"
                +
                "  and (ties_data.\"o-ran-smo-teiv-ran_OCUCPFunction\".\"CD_classifiers\" @> '\"ocucp-function-model:Weekend\"')\n")
            .toString(), getTestAndCondition(List.of(slb1, slb2)).toString());
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

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  (ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"REL_CD_classifiers_ODUFUNCTION_PROVIDES_NRCELLDU\"::text like '%odu-function-model:Ru%')\n"
                +
                "  and (ties_data.\"o-ran-smo-teiv-ran_NRSectorCarrier\".\"REL_CD_classifiers_ODUFUNCTION_PROVIDES_NRSECTORCARRIER\" @> '\"ocucp-function-model:Weekend\"')\n")
            .toString(), getTestAndCondition(List.of(slb1, slb2)).toString());
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

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  (ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"CD_sourceIds\"::text like '%urn:cmHandle:%')\n"
                +
                "  and (ties_data.\"o-ran-smo-teiv-ran_OCUCPFunction\".\"CD_sourceIds\" @> '\"urn:3gpp:dn:/SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/ODUFunction=16\"')\n")
            .toString(), getTestAndCondition(List.of(slb1, slb2)).toString());
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

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  (ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"REL_CD_sourceIds_ODUFUNCTION_PROVIDES_NRCELLDU\"::text like '%urn:cmHandle:%')\n"
                +
                "  and (ties_data.\"o-ran-smo-teiv-ran_NRSectorCarrier\".\"REL_CD_sourceIds_ODUFUNCTION_PROVIDES_NRSECTORCARRIER\" @> '\"urn:3gpp:dn:/SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/ODUFunction=16\"')\n")
            .toString(), getTestAndCondition(List.of(slb1, slb2)).toString());
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

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"CD_decorators\" ->> 'odu-function-model:location' like '%Stock%'\n"
                +
                "  and ties_data.\"o-ran-smo-teiv-ran_OCUCPFunction\".\"CD_decorators\" -> 'odu-function-model:stringdata' = '\"ASD\"'\n"
                +
                "  and ties_data.\"o-ran-smo-teiv-ran_OCUCPFunction\".\"CD_decorators\" -> 'odu-function-model:intdata' = '2'\n")
            .toString(), getTestAndCondition(List.of(slb1, slb2, slb3)).toString());
    }

    @Test
    void testConditions_entityMetadata() {
        ScopeObject scopeObject1 = ScopeObject.builder(ODU_FUNCTION)
            .topologyObjectType(TopologyObjectType.ENTITY)
            .container(ContainerType.METADATA).leaf("reliabilityIndicator")
            .queryFunction(QueryFunction.EQ).parameter(
                "OK")
            .dataType(DataType.PRIMITIVE).build();

        LogicalBlock slb1 = new ScopeLogicalBlock(scopeObject1);

        // spotless:off
        assertEquals(condition(
            "ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"metadata\" -> 'reliabilityIndicator' = '\"OK\"'")
            .toString(), slb1.getCondition().toString());
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

        // spotless:off
        assertEquals(condition(
            "\n" +
                "  ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"REL_CD_decorators_ODUFUNCTION_PROVIDES_NRCELLDU\" ->> 'odu-function-model:location' like '%Stock%'\n"
                +
                "  and ties_data.\"o-ran-smo-teiv-ran_NRSectorCarrier\".\"REL_CD_decorators_ODUFUNCTION_PROVIDES_NRSECTORCARRIER\" -> 'odu-function-model:stringdata' = '\"ASD\"'\n"
                +
                "  and ties_data.\"o-ran-smo-teiv-ran_NRSectorCarrier\".\"REL_CD_decorators_ODUFUNCTION_PROVIDES_NRSECTORCARRIER\" -> 'odu-function-model:intdata' = '2'\n")
            .toString(), getTestAndCondition(List.of(slb1, slb2, slb3)).toString());
        // spotless:on
    }

    @Test
    void testGetJoinCondition() {
        LogicalBlock slb1 = new ScopeLogicalBlock(ScopeObject.builder("ODUFunction").topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of("provided-nrCellDu"))
                .build());

        InnerFilterCriteria innerFilterCriteria1 = InnerFilterCriteria.builder().scope(slb1).build();

        Pair<String, Field> pair1 = new ImmutablePair<>("ties_data.\"o-ran-smo-teiv-ran_NRCellDU\"", field(
                "ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"REL_FK_provided-by-oduFunction\"" + "=" + "ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"id\""));

        Assertions.assertEquals(new HashSet(Arrays.asList(pair1)), innerFilterCriteria1.builder().scope(slb1).build()
                .getJoinCondition());

        LogicalBlock slb2 = new ScopeLogicalBlock(ScopeObject.builder("AntennaCapability").topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "serving-antennaModule")).build());

        InnerFilterCriteria innerFilterCriteria2 = InnerFilterCriteria.builder().scope(slb2).build();

        Pair<String, Field> pair2 = new ImmutablePair<>("ties_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\"", field(
                "ties_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\".\"bSide_AntennaCapability\"" + "=" + "ties_data.\"o-ran-smo-teiv-ran_AntennaCapability\".\"id\""));

        Assertions.assertEquals(new HashSet(Arrays.asList(pair2)), innerFilterCriteria2.builder().scope(slb2).build()
                .getJoinCondition());
    }

    Condition getTestAndCondition(List<LogicalBlock> logicalBlocks) {
        return AndLogicalBlock.fromLogicalBlockList(logicalBlocks).getCondition();
    }

    Condition getTestOrCondition(List<LogicalBlock> logicalBlocks) {
        return OrLogicalBlock.fromLogicalBlockList(logicalBlocks).getCondition();
    }
}
