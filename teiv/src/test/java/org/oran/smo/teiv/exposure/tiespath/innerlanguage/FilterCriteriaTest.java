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

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jooq.SelectField;
import org.jooq.Table;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.oran.smo.teiv.exception.TiesException;
import org.oran.smo.teiv.exposure.consumerdata.ConsumerDataValidator;
import org.oran.smo.teiv.exposure.tiespath.refiner.BasePathRefinement;
import org.oran.smo.teiv.exposure.tiespath.resolver.ScopeResolver;
import org.oran.smo.teiv.exposure.tiespath.resolver.TargetResolver;
import org.oran.smo.teiv.schema.DataType;
import org.oran.smo.teiv.schema.MockSchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoaderException;

class FilterCriteriaTest {

    private static BasePathRefinement basePathRefinement;
    private static ConsumerDataValidator consumerDataValidator;
    private final TargetResolver targetResolver = new TargetResolver();
    private final ScopeResolver scopeResolver = new ScopeResolver();

    @BeforeAll
    static void setUp() throws SchemaLoaderException {
        new MockSchemaLoader().loadSchemaRegistry();
        basePathRefinement = new BasePathRefinement(consumerDataValidator);
    }

    @Test
    void testFilterCriteria() {
        InnerFilterCriteria filterCriteria = new InnerFilterCriteria(null, null);
        List<TargetObject> targetObjects = targetResolver.resolve("ODUFunction", "/attributes(gNBId)");
        filterCriteria.setTargets(targetObjects);
        LogicalBlock logicalBlock = scopeResolver.resolve("ODUFunction", "/attributes[@gNBIdLength=1]");
        filterCriteria.setScope(logicalBlock);
        Assertions.assertEquals(1, filterCriteria.getTargets().size());
        Assertions.assertEquals(QueryFunction.EQ, ((ScopeLogicalBlock) filterCriteria.getScope()).getScopeObject()
                .getQueryFunction());
        Assertions.assertEquals("gNBIdLength", ((ScopeLogicalBlock) filterCriteria.getScope()).getScopeObject().getLeaf());
        Assertions.assertEquals(ContainerType.ATTRIBUTES, ((ScopeLogicalBlock) filterCriteria.getScope()).getScopeObject()
                .getContainer());

    }

    @Test
    void testGetTables() {
        InnerFilterCriteria filterCriteria = new InnerFilterCriteria(null, null);
        List<TargetObject> targetObjects = targetResolver.resolve("ODUFunction", "/attributes(gNBId,gNBIdLength)");
        filterCriteria.setTargets(targetObjects);
        targetObjects.get(0).setTopologyObjectType(TopologyObjectType.ENTITY);
        LogicalBlock logicalBlock = scopeResolver.resolve("ODUFunction", "/attributes[@gNBIdLength=1 or @gNBId=8]");
        ((OrLogicalBlock) logicalBlock).getChildren().forEach(l -> ((ScopeLogicalBlock) l).getScopeObject()
                .setTopologyObjectType(TopologyObjectType.ENTITY));
        filterCriteria.setScope(logicalBlock);
        basePathRefinement.resolveUndefinedTopologyObjectTypes(FilterCriteria.builder("RAN").filterCriteriaList(List.of(
                filterCriteria)).build());

        Set<Table> result = new HashSet<>();
        result.add(table("ties_data.\"o-ran-smo-teiv-ran_ODUFunction\""));

        Assertions.assertEquals(result, filterCriteria.getTables());

        InnerFilterCriteria filterCriteria2 = new InnerFilterCriteria(null, null);
        List<TargetObject> targetObjects2 = targetResolver.resolve("ODUFUNCTION_PROVIDES_NRCELLDU", null);
        filterCriteria2.setTargets(targetObjects2);
        LogicalBlock logicalBlock2 = scopeResolver.resolve("NRCellDU", "/attributes[@nCI=12]");
        filterCriteria2.setScope(logicalBlock2);
        basePathRefinement.resolveUndefinedTopologyObjectTypes(FilterCriteria.builder("RAN").filterCriteriaList(List.of(
                filterCriteria2)).build());

        result.clear();

        result.add(table("ties_data.\"o-ran-smo-teiv-ran_NRCellDU\""));

        Assertions.assertEquals(result, filterCriteria2.getTables());

        InnerFilterCriteria filterCriteria3 = new InnerFilterCriteria(null, null);
        List<TargetObject> targetObjects3 = targetResolver.resolve("ODUFunction", "/attributes(gNBId,gNBIdLength)");
        filterCriteria3.setTargets(targetObjects3);
        LogicalBlock logicalBlock3 = scopeResolver.resolve("ODUFunction", "/attributes[@gNBIdLength=1 or @gNBId=8]");
        filterCriteria3.setScope(logicalBlock3);

        Assertions.assertThrows(TiesException.class, filterCriteria3::getTables);

    }

    @Test
    void testGetSelects() {
        InnerFilterCriteria filterCriteria = new InnerFilterCriteria(null, null);
        List<TargetObject> targetObjects = targetResolver.resolve("ODUFunction", "/attributes(gNBId,gNBIdLength)");
        targetObjects.get(0).setTopologyObjectType(TopologyObjectType.ENTITY);
        filterCriteria.setTargets(targetObjects);
        Map<SelectField, Map<SelectField, DataType>> expected = new HashMap<>();
        expected.put(field("ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"id\"").as("o-ran-smo-teiv-ran:ODUFunction.id"),
                new HashMap<>());
        expected.get(field("ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"id\"").as("o-ran-smo-teiv-ran:ODUFunction.id"))
                .put(field("ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"gNBId\"").as(
                        "o-ran-smo-teiv-ran:ODUFunction.attr.gNBId"), DataType.BIGINT);
        expected.get(field("ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"id\"").as("o-ran-smo-teiv-ran:ODUFunction.id"))
                .put(field("ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"id\"").as("o-ran-smo-teiv-ran:ODUFunction.id"),
                        DataType.PRIMITIVE);
        expected.get(field("ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"id\"").as("o-ran-smo-teiv-ran:ODUFunction.id"))
                .put(field("ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"gNBIdLength\"").as(
                        "o-ran-smo-teiv-ran:ODUFunction.attr.gNBIdLength"), DataType.INTEGER);
        Assertions.assertEquals(expected, filterCriteria.getSelects());
    }

    @Test
    void testGetCondition() {
        InnerFilterCriteria filterCriteria = new InnerFilterCriteria(null, null);
        LogicalBlock logicalBlock = scopeResolver.resolve("ODUFunction", "/attributes[@gNBIdLength=1]");
        filterCriteria.setScope(logicalBlock);
        filterCriteria.setTargets(List.of());
        basePathRefinement.resolveUndefinedTopologyObjectTypes(FilterCriteria.builder("RAN").filterCriteriaList(List.of(
                filterCriteria)).build());
        basePathRefinement.validateScopeParametersDataType(FilterCriteria.builder("RAN").filterCriteriaList(List.of(
                filterCriteria)).build());
        Assertions.assertEquals(condition(field("ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"gNBIdLength\"").eq(1))
                .toString(), filterCriteria.getCondition().toString());
    }
}
