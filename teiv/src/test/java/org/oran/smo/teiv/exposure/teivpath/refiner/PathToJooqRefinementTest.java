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
package org.oran.smo.teiv.exposure.teivpath.refiner;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.oran.smo.teiv.exposure.teivpath.innerlanguage.ContainerType;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.FilterCriteria;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.InnerFilterCriteria;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.QueryFunction;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.ScopeLogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.ScopeObject;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.TargetObject;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.TopologyObjectType;
import org.oran.smo.teiv.schema.DataType;
import org.oran.smo.teiv.schema.MockSchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoaderException;

import static org.junit.Assert.assertEquals;

class PathToJooqRefinementTest {

    @BeforeAll
    static void setUp() throws SchemaLoaderException {
        new MockSchemaLoader().loadSchemaRegistry();
    }

    @Test
    void basicToJooqTest() {
        FilterCriteria filterCriteria = FilterCriteria.builder("RAN").filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(List.of(TargetObject.builder("ODUFunction").topologyObjectType(TopologyObjectType.ENTITY)
                        .container(ContainerType.ATTRIBUTES).params(List.of("gNBDUId")).build())).scope(
                                new ScopeLogicalBlock(ScopeObject.builder("ODUFunction").topologyObjectType(
                                        TopologyObjectType.ENTITY).container(ContainerType.ID).dataType(DataType.PRIMITIVE)
                                        .parameter("123").queryFunction(QueryFunction.EQ).build())).build())).build();

        // spotless:off
        assertEquals("(\n" +
            "  select\n" +
            "    null::bigint \"o-ran-smo-teiv-ran:ODUFunction.attr.gNBDUId\",\n" +
            "    null \"o-ran-smo-teiv-ran:ODUFunction.id\",\n" +
            "    (\n" +
            "      select count(*)\n" +
            "      from (\n" +
            "        select teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"id\" \"o-ran-smo-teiv-ran:ODUFunction.id\"\n"
            +
            "        from teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\"\n" +
            "        where teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"id\" = '123'\n" +
            "      ) \"alias_10602194\"\n" +
            "    ) \"count\"\n" +
            ")\n" +
            "union all (\n" +
            "  select\n" +
            "    \"o-ran-smo-teiv-ran:ODUFunction.attr.gNBDUId\",\n" +
            "    \"o-ran-smo-teiv-ran:ODUFunction.id\",\n" +
            "    null \"count\"\n" +
            "  from (\n" +
            "    select\n" +
            "      teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"gNBDUId\" \"o-ran-smo-teiv-ran:ODUFunction.attr.gNBDUId\",\n" +
            "      teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"id\" \"o-ran-smo-teiv-ran:ODUFunction.id\"\n"
            +
            "    from teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\"\n" +
            "    where teiv_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"id\" = '123'\n" +
            "    order by \"o-ran-smo-teiv-ran:ODUFunction.id\" asc\n" +
            "  ) \"alias_8925548\"\n" +
            "  limit 100\n" +
            "  offset 0\n" +
            ")", PathToJooqRefinement.toJooq(filterCriteria, 0, 100).toString());
        // spotless:on
    }
}
