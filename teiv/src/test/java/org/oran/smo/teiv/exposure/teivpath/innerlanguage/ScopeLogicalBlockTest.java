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
package org.oran.smo.teiv.exposure.teivpath.innerlanguage;

import static org.jooq.impl.DSL.table;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jooq.Table;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.schema.DataType;
import org.oran.smo.teiv.schema.MockSchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoaderException;

class ScopeLogicalBlockTest {
    @BeforeAll
    static void setUp() throws SchemaLoaderException {
        SchemaLoader mockedSchemaLoader = new MockSchemaLoader();
        mockedSchemaLoader.loadSchemaRegistry();
    }

    @Test
    void testGetTables() {
        Set<Table> resultTables = new HashSet<>();
        ScopeObject scopeObject = ScopeObject.builder("ODUFunction/provided-nrCellDu").container(ContainerType.ID).leaf(
                "id").queryFunction(QueryFunction.EQ).parameter("1").dataType(DataType.BIGINT).build();
        scopeObject.setTopologyObjectType(TopologyObjectType.ASSOCIATION);
        ScopeObject scopeObject1 = ScopeObject.builder("ODUFunction").container(ContainerType.ATTRIBUTES).leaf(
                "gNBIdLength").queryFunction(QueryFunction.EQ).parameter("1").dataType(DataType.INTEGER).build();
        scopeObject1.setTopologyObjectType(TopologyObjectType.ENTITY);
        ScopeObject scopeObject2 = ScopeObject.builder("ODUFunction").container(ContainerType.ATTRIBUTES).leaf("gNBId")
                .queryFunction(QueryFunction.EQ).parameter("8").dataType(DataType.BIGINT).build();
        scopeObject2.setTopologyObjectType(TopologyObjectType.ENTITY);
        ScopeObject scopeObject3 = ScopeObject.builder("ODUFunction").container(ContainerType.ATTRIBUTES).leaf("gNBId")
                .queryFunction(QueryFunction.EQ).parameter("1").dataType(DataType.BIGINT).build();
        scopeObject3.setTopologyObjectType(TopologyObjectType.UNDEFINED);
        ScopeObject scopeObject4 = ScopeObject.builder("ODUFUNCTION_PROVIDES_NRCELLDU").container(ContainerType.ID)
                .queryFunction(QueryFunction.EQ).parameter("1").dataType(DataType.PRIMITIVE).build();
        scopeObject4.setTopologyObjectType(TopologyObjectType.RELATION);
        ScopeObject scopeObject5 = ScopeObject.builder("ODUFunction/managed-by-managedElement").container(ContainerType.ID)
                .leaf("id").queryFunction(QueryFunction.EQ).parameter("1").dataType(DataType.BIGINT).build();
        scopeObject5.setTopologyObjectType(TopologyObjectType.ASSOCIATION);
        ScopeObject scopeObject6 = ScopeObject.builder("MANAGEDELEMENT_MANAGES_ODUFUNCTION").container(ContainerType.ID)
                .leaf("id").queryFunction(QueryFunction.EQ).parameter("me1").dataType(DataType.PRIMITIVE).build();
        scopeObject6.setInnerContainer(Arrays.asList("managed-by-managedElement"));
        scopeObject6.setTopologyObjectType(TopologyObjectType.RELATION);
        ScopeObject scopeObject7 = ScopeObject.builder("MANAGEDELEMENT_MANAGES_ODUFUNCTION/provided-by-oduFunction")
                .container(ContainerType.ID).leaf("id").queryFunction(QueryFunction.EQ).parameter("me1").dataType(
                        DataType.PRIMITIVE).build();
        scopeObject7.setInnerContainer(Arrays.asList("provided-by-oduFunction"));
        scopeObject7.setTopologyObjectType(TopologyObjectType.ASSOCIATION);
        ScopeObject scopeObject8 = ScopeObject.builder("NRCellDU/grouped-by-sector").topologyObjectType(
                TopologyObjectType.ASSOCIATION).container(ContainerType.ATTRIBUTES).innerContainer(List.of()).leaf(
                        "geoColumn").queryFunction(QueryFunction.EQ).parameter("point(39.4019881 67.9419888)").dataType(
                                DataType.GEOGRAPHIC).build();
        ScopeLogicalBlock scopeLogicalBlock = new ScopeLogicalBlock(scopeObject);
        ScopeLogicalBlock scopeLogicalBlock1 = new ScopeLogicalBlock(scopeObject1);
        ScopeLogicalBlock scopeLogicalBlock2 = new ScopeLogicalBlock(scopeObject2);
        ScopeLogicalBlock scopeLogicalBlock3 = new ScopeLogicalBlock(scopeObject3);
        ScopeLogicalBlock scopeLogicalBlock4 = new ScopeLogicalBlock(scopeObject4);
        ScopeLogicalBlock scopeLogicalBlock5 = new ScopeLogicalBlock(scopeObject5);
        ScopeLogicalBlock scopeLogicalBlock6 = new ScopeLogicalBlock(scopeObject6);
        ScopeLogicalBlock scopeLogicalBlock7 = new ScopeLogicalBlock(scopeObject7);
        ScopeLogicalBlock scopeLogicalBlock8 = new ScopeLogicalBlock(scopeObject8);
        resultTables.add(table("teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\""));
        Assertions.assertEquals(resultTables, scopeLogicalBlock.getTables());
        resultTables.clear();
        resultTables.add(table("teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\""));
        Assertions.assertEquals(resultTables, scopeLogicalBlock1.getTables());
        resultTables.clear();
        resultTables.add(table("teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\""));
        Assertions.assertEquals(resultTables, scopeLogicalBlock2.getTables());
        resultTables.clear();
        assertThrows(TeivException.class, scopeLogicalBlock3::getTables);
        resultTables.add(table("teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\""));
        Assertions.assertEquals(resultTables, scopeLogicalBlock4.getTables());
        resultTables.clear();
        resultTables.add(table("teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\""));
        Assertions.assertEquals(resultTables, scopeLogicalBlock5.getTables());
        resultTables.clear();
        resultTables.add(table("teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\""));
        Assertions.assertEquals(resultTables, scopeLogicalBlock6.getTables());
        resultTables.clear();
        resultTables.add(table("teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\""));
        Assertions.assertEquals(resultTables, scopeLogicalBlock8.getTables());
        resultTables.clear();
        Assertions.assertThrows(TeivException.class, () -> scopeLogicalBlock7.getTables());
    }
}
