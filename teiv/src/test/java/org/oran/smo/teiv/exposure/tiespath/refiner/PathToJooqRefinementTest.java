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
package org.oran.smo.teiv.exposure.tiespath.refiner;

import java.util.ArrayList;
import java.util.List;

import org.jooq.SelectOrderByStep;
import org.jooq.Record;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.oran.smo.teiv.exposure.tiespath.innerlanguage.ContainerType;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.FilterCriteria;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.InnerFilterCriteria;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.QueryFunction;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.ScopeLogicalBlock;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.ScopeObject;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.TargetObject;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.TopologyObjectType;
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
            "        select ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"id\" \"o-ran-smo-teiv-ran:ODUFunction.id\"\n"
            +
            "        from ties_data.\"o-ran-smo-teiv-ran_ODUFunction\"\n" +
            "        where ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"id\" = '123'\n" +
            "      ) \"alias_101460301\"\n" +
            "    ) \"count\"\n" +
            ")\n" +
            "union all (\n" +
            "  select\n" +
            "    \"o-ran-smo-teiv-ran:ODUFunction.attr.gNBDUId\",\n" +
            "    \"o-ran-smo-teiv-ran:ODUFunction.id\",\n" +
            "    null \"count\"\n" +
            "  from (\n" +
            "    select\n" +
            "      ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"gNBDUId\" \"o-ran-smo-teiv-ran:ODUFunction.attr.gNBDUId\",\n" +
            "      ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"id\" \"o-ran-smo-teiv-ran:ODUFunction.id\"\n"
            +
            "    from ties_data.\"o-ran-smo-teiv-ran_ODUFunction\"\n" +
            "    where ties_data.\"o-ran-smo-teiv-ran_ODUFunction\".\"id\" = '123'\n" +
            "    order by \"o-ran-smo-teiv-ran:ODUFunction.id\" asc\n" +
            "  ) \"alias_47405590\"\n" +
            "  limit 100\n" +
            "  offset 0\n" +
            ")", PathToJooqRefinement.toJooq(filterCriteria, 0, 100).toString());
        // spotless:on
    }

    @Test
    void associationToJooqFirstHopeManyToOneSingleTest() {
        List<TargetObject> targetObjects = getTargetObjects(List.of("AntennaModule"));
        ScopeLogicalBlock scopeLogicalBlock = new ScopeLogicalBlock(ScopeObject.builder("AntennaModule").topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of("installed-at-site"))
                .leaf("geo-location").queryFunction(QueryFunction.EQ).parameter("point(39.4019881 67.9419888)").dataType(
                        DataType.GEOGRAPHIC).build());

        FilterCriteria filterCriteria = FilterCriteria.builder("RAN").filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(targetObjects).scope(scopeLogicalBlock).build())).build();

        SelectOrderByStep<Record> query = PathToJooqRefinement.toJooq(filterCriteria, 0, 100);

        // spotless:off
        assertEquals("(\n" + //
            "  select\n" + //
            "    null \"o-ran-smo-teiv-equipment:AntennaModule.id\",\n" + //
            "    (\n" + //
            "      select count(*)\n" + //
            "      from (\n" + //
            "        select ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"id\" \"o-ran-smo-teiv-equipment:AntennaModule.id\"\n"
            + //
            "        from ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\"\n" + //
            "        where \"REL_FK_installed-at-site\" in (\n" + //
            "          select id\n" + //
            "          from ties_data.\"o-ran-smo-teiv-equipment_Site\"\n" + //
            "          where (ties_data.\"o-ran-smo-teiv-equipment_Site\".\"geo-location\" = st_geomfromtext('point(39.4019881 67.9419888)'))\n"
            + //
            "        )\n" + //
            "      ) \"alias_3993031\"\n" + //
            "    ) \"count\"\n" + //
            ")\n" + //
            "union all (\n" + //
            "  select\n" + //
            "    \"o-ran-smo-teiv-equipment:AntennaModule.id\",\n" + //
            "    null \"count\"\n" + //
            "  from (\n" + //
            "    select ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"id\" \"o-ran-smo-teiv-equipment:AntennaModule.id\"\n"
            + //
            "    from ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\"\n" + //
            "    where \"REL_FK_installed-at-site\" in (\n" + //
            "      select id\n" + //
            "      from ties_data.\"o-ran-smo-teiv-equipment_Site\"\n" + //
            "      where (ties_data.\"o-ran-smo-teiv-equipment_Site\".\"geo-location\" = st_geomfromtext('point(39.4019881 67.9419888)'))\n"
            + //
            "    )\n" + //
            "    order by \"o-ran-smo-teiv-equipment:AntennaModule.id\" asc\n" + //
            "  ) \"alias_77076166\"\n" + //
            "  limit 100\n" + //
            "  offset 0\n" + //
            ")", query.toString());
        // spotless:on
    }

    @Test
    void associationToJooqFirstHopeOneToManySingleTest() {
        List<TargetObject> targetObjects = getTargetObjects(List.of("Sector"));
        ScopeLogicalBlock scopeLogicalBlock = new ScopeLogicalBlock(ScopeObject.builder("Sector").topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of("grouped-nrCellDu"))
                .leaf("nCI").queryFunction(QueryFunction.EQ).parameter("400").dataType(DataType.BIGINT).build());

        FilterCriteria filterCriteria = FilterCriteria.builder("RAN").filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(targetObjects).scope(scopeLogicalBlock).build())).build();

        SelectOrderByStep<Record> query = PathToJooqRefinement.toJooq(filterCriteria, 0, 100);

        // spotless:off
        assertEquals("(\n" + //
            "  select\n" + //
            "    null \"o-ran-smo-teiv-ran:Sector.id\",\n" + //
            "    (\n" + //
            "      select count(*)\n" + //
            "      from (\n" + //
            "        select ties_data.\"o-ran-smo-teiv-ran_Sector\".\"id\" \"o-ran-smo-teiv-ran:Sector.id\"\n" + //
            "        from ties_data.\"o-ran-smo-teiv-ran_Sector\"\n" + //
            "        where id in (\n" + //
            "          select \"REL_FK_grouped-by-sector\"\n" + //
            "          from ties_data.\"o-ran-smo-teiv-ran_NRCellDU\"\n" + //
            "          where (\n" + //
            "            ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"REL_FK_grouped-by-sector\" is not null\n" + //
            "            and ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"nCI\" = 400\n" + //
            "          )\n" + //
            "        )\n" + //
            "      ) \"alias_78318003\"\n" + //
            "    ) \"count\"\n" + //
            ")\n" + //
            "union all (\n" + //
            "  select\n" + //
            "    \"o-ran-smo-teiv-ran:Sector.id\",\n" + //
            "    null \"count\"\n" + //
            "  from (\n" + //
            "    select ties_data.\"o-ran-smo-teiv-ran_Sector\".\"id\" \"o-ran-smo-teiv-ran:Sector.id\"\n" + //
            "    from ties_data.\"o-ran-smo-teiv-ran_Sector\"\n" + //
            "    where id in (\n" + //
            "      select \"REL_FK_grouped-by-sector\"\n" + //
            "      from ties_data.\"o-ran-smo-teiv-ran_NRCellDU\"\n" + //
            "      where (\n" + //
            "        ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"REL_FK_grouped-by-sector\" is not null\n" + //
            "        and ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"nCI\" = 400\n" + //
            "      )\n" + //
            "    )\n" + //
            "    order by \"o-ran-smo-teiv-ran:Sector.id\" asc\n" + //
            "  ) \"alias_21420574\"\n" + //
            "  limit 100\n" + //
            "  offset 0\n" + //
            ")", query.toString());
        // spotless:on

    }

    @Test
    void associationToJooqFirstHopeManyToManySingleTest() {
        List<TargetObject> targetObjects = getTargetObjects(List.of("NRCellDU"));

        ScopeLogicalBlock scope = new ScopeLogicalBlock(ScopeObject.builder("NRCellDU").topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "serving-antennaModule")).leaf("id").queryFunction(QueryFunction.EQ).parameter("AntennaModule_1")
                .dataType(DataType.PRIMITIVE).build());

        FilterCriteria filterCriteria = FilterCriteria.builder("RAN").filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(targetObjects).scope(scope).build())).build();

        SelectOrderByStep<Record> query = PathToJooqRefinement.toJooq(filterCriteria, 0, 100);

        assertEquals("(\n" + //
                "  select\n" + //
                "    null \"o-ran-smo-teiv-ran:NRCellDU.id\",\n" + //
                "    (\n" + //
                "      select count(*)\n" + //
                "      from (\n" + //
                "        with\n" + //
                "          \"NRCellDU\"(\"id\") as (\n" + //
                "            select \"bSide_NRCellDU\"\n" + //
                "            from ties_data.\"o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU\"\n" + //
                "            where \"aSide_AntennaModule\" in (\n" + //
                "              select id\n" + //
                "              from ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\"\n" + //
                "              where ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"id\" = 'AntennaModule_1'\n" + //
                "            )\n" + //
                "          )\n" + //
                "        select ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"id\" \"o-ran-smo-teiv-ran:NRCellDU.id\"\n" + //
                "        from ties_data.\"o-ran-smo-teiv-ran_NRCellDU\"\n" + //
                "        where id in (\n" + //
                "          select id\n" + //
                "          from \"NRCellDU\"\n" + //
                "        )\n" + //
                "      ) \"alias_53607736\"\n" + //
                "    ) \"count\"\n" + //
                ")\n" + //
                "union all (\n" + //
                "  select\n" + //
                "    \"o-ran-smo-teiv-ran:NRCellDU.id\",\n" + //
                "    null \"count\"\n" + //
                "  from (\n" + //
                "    with\n" + //
                "      \"NRCellDU\"(\"id\") as (\n" + //
                "        select \"bSide_NRCellDU\"\n" + //
                "        from ties_data.\"o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU\"\n" + //
                "        where \"aSide_AntennaModule\" in (\n" + //
                "          select id\n" + //
                "          from ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\"\n" + //
                "          where ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"id\" = 'AntennaModule_1'\n" + //
                "        )\n" + //
                "      )\n" + //
                "    select ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"id\" \"o-ran-smo-teiv-ran:NRCellDU.id\"\n" + //
                "    from ties_data.\"o-ran-smo-teiv-ran_NRCellDU\"\n" + //
                "    where id in (\n" + //
                "      select id\n" + //
                "      from \"NRCellDU\"\n" + //
                "    )\n" + //
                "    order by \"o-ran-smo-teiv-ran:NRCellDU.id\" asc\n" + //
                "  ) \"alias_8945972\"\n" + //
                "  limit 100\n" + //
                "  offset 0\n" + //
                ")", query.toString());
    }

    @Test
    void associationToJooqFirstHopeManyToManyMultipleTest() {
        List<TargetObject> targetObjects = getTargetObjects(List.of("AntennaCapability"));
        List<TargetObject> targetObjects2 = getTargetObjects(List.of("NRCellDU"));

        ScopeLogicalBlock scope = new ScopeLogicalBlock(ScopeObject.builder("AntennaCapability").topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "serving-antennaModule")).leaf("id").queryFunction(QueryFunction.EQ).parameter("AntennaModule_1")
                .dataType(DataType.PRIMITIVE).build());
        ScopeLogicalBlock scope2 = new ScopeLogicalBlock(ScopeObject.builder("NRCellDU").topologyObjectType(
                TopologyObjectType.ENTITY).container(ContainerType.ASSOCIATION).innerContainer(List.of(
                        "serving-antennaModule")).leaf("id").queryFunction(QueryFunction.EQ).parameter("AntennaModule_3")
                .dataType(DataType.PRIMITIVE).build());

        FilterCriteria filterCriteria = FilterCriteria.builder("RAN").filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(targetObjects).scope(scope).build(), InnerFilterCriteria.builder().targets(
                        targetObjects2).scope(scope2).build())).build();

        SelectOrderByStep<Record> query = PathToJooqRefinement.toJooq(filterCriteria, 0, 100);

        // spotless:off
        assertEquals("(\n" +
            "  select\n" +
            "    null \"o-ran-smo-teiv-ran:AntennaCapability.id\",\n" +
            "    null \"o-ran-smo-teiv-ran:NRCellDU.id\",\n" +
            "    (\n" +
            "      select count(*)\n" +
            "      from (\n" +
            "        with\n" +
            "          \"AntennaCapability\"(\"id\") as (\n" +
            "            select \"bSide_AntennaCapability\"\n" +
            "            from ties_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\"\n" +
            "            where \"aSide_AntennaModule\" in (\n" +
            "              select id\n" +
            "              from ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\"\n" +
            "              where ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"id\" = 'AntennaModule_1'\n"
            +
            "            )\n" +
            "          )\n" +
            "        (\n" +
            "          select\n" +
            "            ties_data.\"o-ran-smo-teiv-ran_AntennaCapability\".\"id\" \"o-ran-smo-teiv-ran:AntennaCapability.id\",\n"
            +
            "            null \"o-ran-smo-teiv-ran:NRCellDU.id\"\n" +
            "          from ties_data.\"o-ran-smo-teiv-ran_AntennaCapability\"\n" +
            "          where id in (\n" +
            "            select id\n" +
            "            from \"AntennaCapability\"\n" +
            "          )\n" +
            "        )\n" +
            "        union all (\n" +
            "          with\n" +
            "            \"NRCellDU\"(\"id\") as (\n" +
            "              select \"bSide_NRCellDU\"\n" +
            "              from ties_data.\"o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU\"\n"
            +
            "              where \"aSide_AntennaModule\" in (\n" +
            "                select id\n" +
            "                from ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\"\n" +
            "                where ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"id\" = 'AntennaModule_3'\n"
            +
            "              )\n" +
            "            )\n" +
            "          select\n" +
            "            null \"o-ran-smo-teiv-ran:AntennaCapability.id\",\n" +
            "            ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"id\" \"o-ran-smo-teiv-ran:NRCellDU.id\"\n"
            +
            "          from ties_data.\"o-ran-smo-teiv-ran_NRCellDU\"\n" +
            "          where id in (\n" +
            "            select id\n" +
            "            from \"NRCellDU\"\n" +
            "          )\n" +
            "        )\n" +
            "      ) \"alias_81628762\"\n" +
            "    ) \"count\"\n" +
            ")\n" +
            "union all (\n" +
            "  select\n" +
            "    \"o-ran-smo-teiv-ran:AntennaCapability.id\",\n" +
            "    \"o-ran-smo-teiv-ran:NRCellDU.id\",\n" +
            "    null \"count\"\n" +
            "  from (\n" +
            "    with\n" +
            "      \"AntennaCapability\"(\"id\") as (\n" +
            "        select \"bSide_AntennaCapability\"\n" +
            "        from ties_data.\"CFC235E0404703D1E4454647DF8AAE2C193DB402\"\n" +
            "        where \"aSide_AntennaModule\" in (\n" +
            "          select id\n" +
            "          from ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\"\n" +
            "          where ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"id\" = 'AntennaModule_1'\n"
            +
            "        )\n" +
            "      )\n" +
            "    (\n" +
            "      select\n" +
            "        ties_data.\"o-ran-smo-teiv-ran_AntennaCapability\".\"id\" \"o-ran-smo-teiv-ran:AntennaCapability.id\",\n"
            +
            "        null \"o-ran-smo-teiv-ran:NRCellDU.id\"\n" +
            "      from ties_data.\"o-ran-smo-teiv-ran_AntennaCapability\"\n" +
            "      where id in (\n" +
            "        select id\n" +
            "        from \"AntennaCapability\"\n" +
            "      )\n" +
            "      order by \"o-ran-smo-teiv-ran:AntennaCapability.id\" asc\n" +
            "    )\n" +
            "    union all (\n" +
            "      with\n" +
            "        \"NRCellDU\"(\"id\") as (\n" +
            "          select \"bSide_NRCellDU\"\n" +
            "          from ties_data.\"o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU\"\n"
            +
            "          where \"aSide_AntennaModule\" in (\n" +
            "            select id\n" +
            "            from ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\"\n" +
            "            where ties_data.\"o-ran-smo-teiv-equipment_AntennaModule\".\"id\" = 'AntennaModule_3'\n"
            +
            "          )\n" +
            "        )\n" +
            "      select\n" +
            "        null \"o-ran-smo-teiv-ran:AntennaCapability.id\",\n" +
            "        ties_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"id\" \"o-ran-smo-teiv-ran:NRCellDU.id\"\n"
            +
            "      from ties_data.\"o-ran-smo-teiv-ran_NRCellDU\"\n" +
            "      where id in (\n" +
            "        select id\n" +
            "        from \"NRCellDU\"\n" +
            "      )\n" +
            "      order by \"o-ran-smo-teiv-ran:NRCellDU.id\" asc\n" +
            "    )\n" +
            "  ) \"alias_37365486\"\n" +
            "  limit 100\n" +
            "  offset 0\n" +
            ")", query.toString());
        // spotless:on
    }

    private List<TargetObject> getTargetObjects(List<String> targetObjects) {
        List<TargetObject> targetObjectList = new ArrayList<>();
        for (String targetObject : targetObjects) {
            targetObjectList.add(TargetObject.builder(targetObject).topologyObjectType(TopologyObjectType.ENTITY)
                    .isAllParamQueried(true).isGenerated(false).container(ContainerType.ID).params(List.of()).build());
        }
        return targetObjectList;
    }
}
