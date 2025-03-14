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
package org.oran.smo.teiv.exposure.teivpath.refiner;

import static org.oran.smo.teiv.utils.TeivConstants.ID_COLUMN_NAME;
import static org.oran.smo.teiv.utils.TeivConstants.ITEM;
import static org.oran.smo.teiv.utils.TeivConstants.WILDCARD;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.oran.smo.teiv.exposure.teivpath.innerlanguage.EmptyLogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.InnerFilterCriteria;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import org.oran.smo.teiv.exposure.consumerdata.ConsumerDataValidator;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.AndLogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.AndOrLogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.ContainerType;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.FilterCriteria;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.LogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.OrLogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.QueryFunction;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.ScopeLogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.ScopeObject;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.TargetObject;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.TopologyObjectType;
import org.oran.smo.teiv.exposure.teivpath.resolver.ResolverDataType;
import org.oran.smo.teiv.exposure.teivpath.resolver.ScopeResolver;
import org.oran.smo.teiv.exposure.teivpath.resolver.TargetResolver;
import org.oran.smo.teiv.schema.DataType;
import org.oran.smo.teiv.schema.MockSchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoaderException;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.utils.query.exception.TeivPathException;

class BasePathRefinementTest {
    private static final ConsumerDataValidator consumerDataValidator = mock(ConsumerDataValidator.class);
    private static BasePathRefinement basePathRefinement;
    private final TargetResolver targetResolver = new TargetResolver();
    private final ScopeResolver scopeResolver = new ScopeResolver();

    private static final String ODU_FUNCTION = "ODUFunction";

    @BeforeAll
    static void setUp() throws SchemaLoaderException {
        SchemaLoader mockedSchemaLoader = new MockSchemaLoader();
        mockedSchemaLoader.loadSchemaRegistry();
        basePathRefinement = new BasePathRefinement(consumerDataValidator);
    }

    @Test
    void testRefinementOrchestration() {
        LogicalBlock logicalBlock = scopeResolver.process(null, "/attributes[@gNBCUName='someCUCPName']");
        List<TargetObject> targets = targetResolver.resolve(null, "/OCUCPFunction/attributes");
        FilterCriteria filterCriteria = FilterCriteria.builder("RAN").filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(targets).scope(logicalBlock).build())).resolvingTopologyObjectType(
                        FilterCriteria.ResolvingTopologyObjectType.ENTITY).build();
        basePathRefinement.refine(filterCriteria);
        LogicalBlock expectedLogicalBlock = scopeResolver.process(null, "/attributes[@gNBCUName='someCUCPName']");
        List<TargetObject> expectedTargets = targetResolver.resolve(null, "/OCUCPFunction/attributes");
        FilterCriteria expectedFilterCriteria = FilterCriteria.builder("RAN").filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(expectedTargets).scope(expectedLogicalBlock).build())).resolvingTopologyObjectType(
                        FilterCriteria.ResolvingTopologyObjectType.ENTITY).build();
        ScopeObject expectedScopeObject = ((ScopeLogicalBlock) expectedLogicalBlock).getScopeObject();
        expectedScopeObject.setTopologyObject("OCUCPFunction");
        expectedScopeObject.setTopologyObjectType(TopologyObjectType.ENTITY);
        expectedScopeObject.setContainer(ContainerType.ATTRIBUTES);
        expectedScopeObject.setLeaf("gNBCUName");
        expectedScopeObject.setQueryFunction(QueryFunction.EQ);
        expectedScopeObject.setParameter("someCUCPName");
        expectedScopeObject.setDataType(DataType.PRIMITIVE);
        expectedTargets.get(0).setTopologyObjectType(TopologyObjectType.ENTITY);
        expectedTargets.get(0).setAllParamQueried(true);
        Assertions.assertEquals(expectedFilterCriteria, filterCriteria);
    }

    @Test
    void testRefinementOrchestrationMetadata() {
        LogicalBlock logicalBlock = scopeResolver.process(null, "/metadata[@reliabilityIndicator='OK']");
        List<TargetObject> targets = targetResolver.resolve(null, "/OCUCPFunction/sourceIds");
        FilterCriteria filterCriteria = FilterCriteria.builder("RAN").filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(targets).scope(logicalBlock).build())).resolvingTopologyObjectType(
                        FilterCriteria.ResolvingTopologyObjectType.ENTITY).build();
        basePathRefinement.refine(filterCriteria);
        LogicalBlock expectedLogicalBlock = scopeResolver.process(null, "/metadata[@reliabilityIndicator='OK']");
        List<TargetObject> expectedTargets = targetResolver.resolve(null, "/OCUCPFunction/sourceIds");
        FilterCriteria expectedFilterCriteria = FilterCriteria.builder("RAN").filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(expectedTargets).scope(expectedLogicalBlock).build())).resolvingTopologyObjectType(
                        FilterCriteria.ResolvingTopologyObjectType.ENTITY).build();
        ScopeObject expectedScopeObject = ((ScopeLogicalBlock) expectedLogicalBlock).getScopeObject();
        expectedScopeObject.setTopologyObject("OCUCPFunction");
        expectedScopeObject.setTopologyObjectType(TopologyObjectType.ENTITY);
        expectedScopeObject.setContainer(ContainerType.METADATA);
        expectedScopeObject.setLeaf("reliabilityIndicator");
        expectedScopeObject.setQueryFunction(QueryFunction.EQ);
        expectedScopeObject.setParameter("OK");
        expectedScopeObject.setDataType(DataType.PRIMITIVE);
        expectedTargets.get(0).setTopologyObjectType(TopologyObjectType.ENTITY);
        expectedTargets.get(0).setAllParamQueried(false);
        Assertions.assertEquals(expectedFilterCriteria, filterCriteria);
    }

    @Test
    void processTopologyObjectsWithContainerTypeNull() {
        FilterCriteria filterCriteria = FilterCriteria.builder("RAN").build();
        LogicalBlock logicalBlock1 = scopeResolver.process(null, "/managed-by-managedElement[@id='me1']");
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock1).build()));
        basePathRefinement.processTopologyObjectsWithContainerTypeNull(filterCriteria);
        ScopeObject scopeObjectResult1 = ScopeObject.builder(WILDCARD + "/managed-by-managedElement").container(
                ContainerType.ID).innerContainer(List.of()).resolverDataType(ResolverDataType.STRING).queryFunction(
                        QueryFunction.EQ).leaf(null).parameter("me1").build();
        Assertions.assertEquals(scopeObjectResult1, ((ScopeLogicalBlock) logicalBlock1).getScopeObject());
        LogicalBlock logicalBlock2 = scopeResolver.process(ODU_FUNCTION, "/managed-by-managedElement");
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock2).build()));
        basePathRefinement.handleAssociationsInScope((ScopeLogicalBlock) logicalBlock2, "RAN");
        basePathRefinement.processTopologyObjectsWithContainerTypeNull(filterCriteria);
        ScopeObject scopeObjectResult2 = ScopeObject.builder(ODU_FUNCTION + "/managed-by-managedElement")
                .topologyObjectType(TopologyObjectType.ASSOCIATION).container(ContainerType.NOT_NULL).resolverDataType(
                        ResolverDataType.NOT_NULL).queryFunction(QueryFunction.NOT_NULL).build();
        Assertions.assertEquals(scopeObjectResult2, ((ScopeLogicalBlock) logicalBlock2).getScopeObject());
        LogicalBlock logicalBlock3 = scopeResolver.process(null, "/ODUFUNCTION_PROVIDES_NRCELLDU");
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock3).build()));
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement
                .processTopologyObjectsWithContainerTypeNull(filterCriteria));
        LogicalBlock logicalBlock4 = scopeResolver.process(null, "/ODUFunction[@id='odu1']");
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock4).build()));
        basePathRefinement.processTopologyObjectsWithContainerTypeNull(filterCriteria);
        ScopeObject scopeObjectResult4 = ScopeObject.builder(WILDCARD + "/" + ODU_FUNCTION).container(ContainerType.ID)
                .resolverDataType(ResolverDataType.STRING).queryFunction(QueryFunction.EQ).parameter("odu1").build();
        Assertions.assertEquals(scopeObjectResult4, ((ScopeLogicalBlock) logicalBlock4).getScopeObject());
        try (MockedStatic<SchemaRegistry> utilities = Mockito.mockStatic(SchemaRegistry.class)) {
            utilities.when(() -> SchemaRegistry.getEntityNamesByDomain("RAN")).thenReturn(Arrays.asList(
                    "RelationAndEntity"));
            utilities.when(() -> SchemaRegistry.getRelationNamesByDomain("RAN")).thenReturn(Arrays.asList(
                    "RelationAndEntity"));
            LogicalBlock logicalBlock7 = scopeResolver.process(null, "/RelationAndEntity[@leaf='relAndEnt1']");
            filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock7).build()));
            Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement
                    .processTopologyObjectsWithContainerTypeNull(filterCriteria));
            utilities.when(() -> SchemaRegistry.getAssociationNamesByEntityName(ODU_FUNCTION)).thenReturn(Arrays.asList(
                    "RelationAndAssociation"));
            utilities.when(() -> SchemaRegistry.getRelationNamesByDomain("RAN")).thenReturn(Arrays.asList(
                    "RelationAndAssociation"));
            LogicalBlock logicalBlock8 = scopeResolver.process(null, "/RelationAndAssociation[@leaf='relAndAssoc1']");
            filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock8).build()));
            Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement
                    .processTopologyObjectsWithContainerTypeNull(filterCriteria));
        }
        LogicalBlock logicalBlock9 = scopeResolver.process(null, "/NRCellDU/attributes[@nCI=12]");
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock9).build()));
        basePathRefinement.processTopologyObjectsWithContainerTypeNull(filterCriteria);
        ScopeObject scopeObjectResult9 = ScopeObject.builder("*/NRCellDU").container(ContainerType.ATTRIBUTES)
                .resolverDataType(ResolverDataType.INTEGER).queryFunction(QueryFunction.EQ).leaf("nCI").parameter("12")
                .build();
        Assertions.assertEquals(scopeObjectResult9, ((ScopeLogicalBlock) logicalBlock9).getScopeObject());
        LogicalBlock logicalBlock10 = scopeResolver.process(null, "/NRCellDU[@nCI=12]");
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock10).build()));
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement
                .processTopologyObjectsWithContainerTypeNull(filterCriteria));
    }

    @Test
    void testResolveWildCardObjectsInScopeAndTarget_1() {
        InnerFilterCriteria filterCriteria = InnerFilterCriteria.builder().targets(targetResolver.resolve(null,
                "/ODUFunction/attributes(gNBDUId); /ODUFunction/attributes(gNBId); /attributes(name)")).scope(
                        EmptyLogicalBlock.getInstance()).build();
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.resolveWildCardObjectsInScopeAndTarget(
                filterCriteria, "RAN", FilterCriteria.ResolvingTopologyObjectType.ENTITY));
    }

    @Test
    void testResolveWildCardObjectsInScopeAndTarget_2() {
        //root:ODUFunction
        //Target: /attributes(gNBId); /attributes(dUpLMNId)
        //Scope: /attribute[gNBIdLength = 2]
        InnerFilterCriteria filterCriteria = InnerFilterCriteria.builder().build();
        filterCriteria.setTargets(targetResolver.resolve(null,
                "/ODUFunction/attributes(dUpLMNId); /ODUFunction/attributes(gNBId)"));
        filterCriteria.setScope(scopeResolver.resolve(null, "/ODUFunction/attributes[@gNBIdLength=2]"));
        ScopeObject scopeObject1 = ScopeObject.builder(ODU_FUNCTION).container(ContainerType.ATTRIBUTES).leaf("gNBIdLength")
                .queryFunction(QueryFunction.EQ).parameter("2").resolverDataType(ResolverDataType.INTEGER).build();
        LogicalBlock scopeResult1 = new ScopeLogicalBlock(scopeObject1);
        TargetObject targetObjectResult = TargetObject.builder(ODU_FUNCTION).container(ContainerType.ATTRIBUTES).params(
                new ArrayList<>(Arrays.asList("dUpLMNId", "gNBId"))).build();
        List<TargetObject> resultTargetObjects = new ArrayList<>();
        resultTargetObjects.add(targetObjectResult);
        basePathRefinement.resolveWildCardObjectsInScopeAndTarget(filterCriteria, "RAN",
                FilterCriteria.ResolvingTopologyObjectType.ENTITY);
        Assertions.assertEquals(resultTargetObjects, filterCriteria.getTargets());
        Assertions.assertEquals(scopeResult1, filterCriteria.getScope());
    }

    @Test
    void testResolveWildCardObjectsInScopeAndTarget_3() {
        //root:-
        //Target:/attributes;/attributes(cellLocalId,nCI)
        //Scope:/NRCellDU[nCI=12]
        InnerFilterCriteria filterCriteria = InnerFilterCriteria.builder().build();
        filterCriteria.setTargets(targetResolver.resolve(null, "/attributes;/attributes(cellLocalId,nCI)"));
        filterCriteria.setScope(scopeResolver.resolve(null, "/NRCellDU/attributes[@nCI=12]"));
        TargetObject targetObjectResult3_1 = TargetObject.builder("NRCellDU").container(ContainerType.ATTRIBUTES).params(
                new ArrayList<>(Arrays.asList("nCI", "cellLocalId"))).isAllParamQueried(true).build();
        List<TargetObject> resultTargetObjects3_1 = new ArrayList<>();
        resultTargetObjects3_1.add(targetObjectResult3_1);
        basePathRefinement.handleAssociationsInScope((ScopeLogicalBlock) filterCriteria.getScope(), "RAN");
        basePathRefinement.resolveWildCardObjectsInScopeAndTarget(filterCriteria, "RAN",
                FilterCriteria.ResolvingTopologyObjectType.ENTITY);
        Assertions.assertEquals(resultTargetObjects3_1, filterCriteria.getTargets());
    }

    @Test
    void testResolveWildCardObjectsInScopeAndTarget_4() {
        //root:-
        //Target:/attributes;/attributes(cellLocalId)
        //Scope:/attributes[nCI = 12]
        InnerFilterCriteria filterCriteria = InnerFilterCriteria.builder().build();
        filterCriteria.setTargets(targetResolver.resolve(null, "/attributes;/attributes(cellLocalId)"));
        filterCriteria.setScope(scopeResolver.resolve(null, "/attributes[@nCI=12]"));
        TargetObject targetObjectResult4_1 = TargetObject.builder("NRCellDU").container(ContainerType.ATTRIBUTES).params(
                List.of("cellLocalId")).isAllParamQueried(true).build();
        TargetObject targetObjectResult4_2 = TargetObject.builder("NRCellCU").container(ContainerType.ATTRIBUTES).params(
                List.of("cellLocalId")).isAllParamQueried(true).build();
        List<TargetObject> resultTargetObjects4_1 = new ArrayList<>();
        resultTargetObjects4_1.add(targetObjectResult4_1);
        resultTargetObjects4_1.add(targetObjectResult4_2);
        basePathRefinement.resolveWildCardObjectsInScopeAndTarget(filterCriteria, "RAN",
                FilterCriteria.ResolvingTopologyObjectType.ENTITY);
        Assertions.assertEquals(resultTargetObjects4_1, filterCriteria.getTargets());
    }

    @Test
    void testResolveWildCardObjectsInScopeAndTarget_5() {
        //root:-
        //Target:-
        //Scope:/attributes[gNBId = 6 and gNBIdLength = 1]
        InnerFilterCriteria filterCriteria = InnerFilterCriteria.builder().build();
        filterCriteria.setTargets(targetResolver.resolve(null, null));
        filterCriteria.setScope(scopeResolver.resolve(null, "/attributes[@gNBId = 6 and @gNBIdLength = 1]"));
        ScopeLogicalBlock OCUUPFunctionPgNBId = new ScopeLogicalBlock(ScopeObject.builder("OCUUPFunction").container(
                ContainerType.ATTRIBUTES).leaf("gNBId").queryFunction(QueryFunction.EQ).parameter("6").resolverDataType(
                        ResolverDataType.INTEGER).build());
        ScopeLogicalBlock ODUFunctionPgNBId = new ScopeLogicalBlock(ScopeObject.builder(ODU_FUNCTION).container(
                ContainerType.ATTRIBUTES).leaf("gNBId").queryFunction(QueryFunction.EQ).parameter("6").resolverDataType(
                        ResolverDataType.INTEGER).build());
        ScopeLogicalBlock OCUCPFunctionPgNBId = new ScopeLogicalBlock(ScopeObject.builder("OCUCPFunction").container(
                ContainerType.ATTRIBUTES).leaf("gNBId").queryFunction(QueryFunction.EQ).parameter("6").resolverDataType(
                        ResolverDataType.INTEGER).build());
        ScopeLogicalBlock OCUUPFunctionPgNBIdLength = new ScopeLogicalBlock(ScopeObject.builder("OCUUPFunction").container(
                ContainerType.ATTRIBUTES).leaf("gNBIdLength").queryFunction(QueryFunction.EQ).parameter("1")
                .resolverDataType(ResolverDataType.INTEGER).build());
        ScopeLogicalBlock ODUFunctionPgNBIdLength = new ScopeLogicalBlock(ScopeObject.builder(ODU_FUNCTION).container(
                ContainerType.ATTRIBUTES).leaf("gNBIdLength").queryFunction(QueryFunction.EQ).parameter("1")
                .resolverDataType(ResolverDataType.INTEGER).build());
        ScopeLogicalBlock OCUCPFunctionPgNBIdLength = new ScopeLogicalBlock(ScopeObject.builder("OCUCPFunction").container(
                ContainerType.ATTRIBUTES).leaf("gNBIdLength").queryFunction(QueryFunction.EQ).parameter("1")
                .resolverDataType(ResolverDataType.INTEGER).build());
        AndLogicalBlock OCUUPFunction = new AndLogicalBlock();
        AndLogicalBlock ODUFunction = new AndLogicalBlock();
        AndLogicalBlock OCUCPFunction = new AndLogicalBlock();
        OCUCPFunction.getChildren().add(OCUCPFunctionPgNBId);
        OCUCPFunction.getChildren().add(OCUCPFunctionPgNBIdLength);
        OCUUPFunction.getChildren().add(OCUUPFunctionPgNBId);
        OCUUPFunction.getChildren().add(OCUUPFunctionPgNBIdLength);
        ODUFunction.getChildren().add(ODUFunctionPgNBId);
        ODUFunction.getChildren().add(ODUFunctionPgNBIdLength);
        OrLogicalBlock or1 = new OrLogicalBlock();
        OrLogicalBlock or2 = new OrLogicalBlock();
        or1.getChildren().add(or2);
        or1.getChildren().add(ODUFunction);
        or2.getChildren().add(OCUCPFunction);
        or2.getChildren().add(OCUUPFunction);
        TargetObject targetObjectResult5_1 = TargetObject.builder("OCUUPFunction").container(ContainerType.ID).build();
        TargetObject targetObjectResult5_2 = TargetObject.builder(ODU_FUNCTION).container(ContainerType.ID).build();
        TargetObject targetObjectResult5_3 = TargetObject.builder("OCUCPFunction").container(ContainerType.ID).build();
        List<TargetObject> resultTargetObjects5_1 = new ArrayList<>();
        resultTargetObjects5_1.add(targetObjectResult5_3);
        resultTargetObjects5_1.add(targetObjectResult5_1);
        resultTargetObjects5_1.add(targetObjectResult5_2);
        basePathRefinement.resolveWildCardObjectsInScopeAndTarget(filterCriteria, "RAN",
                FilterCriteria.ResolvingTopologyObjectType.ENTITY);
        Assertions.assertEquals(resultTargetObjects5_1, filterCriteria.getTargets());
        Assertions.assertEquals(or1, filterCriteria.getScope());
    }

    @Test
    void testResolveWildCardObjectsInScopeAndTarget_6() {
        //root:-
        //Target:/NRCellDU
        //Scope:/attributes[@cellLocalId = 156]
        InnerFilterCriteria filterCriteria = InnerFilterCriteria.builder().build();
        filterCriteria.setTargets(targetResolver.resolve(null, "/NRCellDU"));
        filterCriteria.setScope(scopeResolver.resolve(null, "/attributes[@cellLocalId = 156]"));
        TargetObject targetObjectResult6_1 = TargetObject.builder("NRCellDU").container(ContainerType.ID).build();
        List<TargetObject> resultTargetObjects6_1 = new ArrayList<>();
        resultTargetObjects6_1.add(targetObjectResult6_1);
        basePathRefinement.resolveWildCardObjectsInScopeAndTarget(filterCriteria, "RAN",
                FilterCriteria.ResolvingTopologyObjectType.ENTITY);
        ScopeObject scopeObjectResult6_1 = ScopeObject.builder("NRCellDU").container(ContainerType.ATTRIBUTES).leaf(
                "cellLocalId").queryFunction(QueryFunction.EQ).parameter("156").resolverDataType(ResolverDataType.INTEGER)
                .build();
        LogicalBlock logicalBlockResult6_1 = new ScopeLogicalBlock(scopeObjectResult6_1);
        Assertions.assertEquals(resultTargetObjects6_1, filterCriteria.getTargets());
        Assertions.assertEquals(logicalBlockResult6_1, filterCriteria.getScope());
    }

    @Test
    void testResolveWildCardObjectsInScopeAndTarget_7() {
        //root:-
        //Target:/attributes(gNBId);/attributes(nCI)
        //Scope:-
        //error no intersection
        InnerFilterCriteria filterCriteria = InnerFilterCriteria.builder().build();
        filterCriteria.setTargets(targetResolver.resolve(null, "/attributes(gNBId);/attributes(nCI)"));
        filterCriteria.setScope(scopeResolver.resolve(null, null));
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.resolveWildCardObjectsInScopeAndTarget(
                filterCriteria, "RAN", FilterCriteria.ResolvingTopologyObjectType.ENTITY));
    }

    @Test
    void testResolveWildCardObjectsInScopeAndTarget_8() {
        //root:-
        //Target:/attributes(nCI)
        //Scope:/attributes[gNBId = 6]
        //error no intersection
        InnerFilterCriteria filterCriteria = InnerFilterCriteria.builder().build();
        filterCriteria.setTargets(targetResolver.resolve(null, "/attributes(nCI)"));
        filterCriteria.setScope(scopeResolver.resolve(null, "/attributes[@gNBId = 6]"));
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.resolveWildCardObjectsInScopeAndTarget(
                filterCriteria, "RAN", FilterCriteria.ResolvingTopologyObjectType.ENTITY));
    }

    @Test
    void testResolveWildCardObjectsInScopeAndTarget_9() {
        //root:-
        //Target:/attributes
        //Scope:/attributes[cellLocalId = 178]; /attributes[nCI = 12]
        InnerFilterCriteria filterCriteria = InnerFilterCriteria.builder().build();
        filterCriteria.setTargets(targetResolver.resolve(null, "/attributes"));
        filterCriteria.setScope(scopeResolver.resolve(null, "/attributes[@cellLocalId = 178]; /attributes[@nCI = 12]"));
        ScopeLogicalBlock NRCellDUCellLocalId = new ScopeLogicalBlock(ScopeObject.builder("NRCellDU").container(
                ContainerType.ATTRIBUTES).leaf("cellLocalId").queryFunction(QueryFunction.EQ).parameter("178")
                .resolverDataType(ResolverDataType.INTEGER).build());
        ScopeLogicalBlock NRCellCUCellLocalId = new ScopeLogicalBlock(ScopeObject.builder("NRCellCU").container(
                ContainerType.ATTRIBUTES).leaf("cellLocalId").queryFunction(QueryFunction.EQ).parameter("178")
                .resolverDataType(ResolverDataType.INTEGER).build());
        ScopeLogicalBlock NRCellDUnCI = new ScopeLogicalBlock(ScopeObject.builder("NRCellDU").container(
                ContainerType.ATTRIBUTES).leaf("nCI").queryFunction(QueryFunction.EQ).parameter("12").resolverDataType(
                        ResolverDataType.INTEGER).build());
        ScopeLogicalBlock NRCellCUnCI = new ScopeLogicalBlock(ScopeObject.builder("NRCellCU").container(
                ContainerType.ATTRIBUTES).leaf("nCI").queryFunction(QueryFunction.EQ).parameter("12").resolverDataType(
                        ResolverDataType.INTEGER).build());
        AndLogicalBlock NRCellDU = new AndLogicalBlock();
        NRCellDU.getChildren().add(NRCellDUnCI);
        NRCellDU.getChildren().add(NRCellDUCellLocalId);
        AndLogicalBlock NRCellCU = new AndLogicalBlock();
        NRCellCU.getChildren().add(NRCellCUnCI);
        NRCellCU.getChildren().add(NRCellCUCellLocalId);
        OrLogicalBlock or = new OrLogicalBlock();
        or.getChildren().add(NRCellCU);
        or.getChildren().add(NRCellDU);
        TargetObject targetObjectResult9_1 = TargetObject.builder("NRCellDU").container(ContainerType.ATTRIBUTES)
                .isAllParamQueried(true).isAllParamQueried(true).build();
        TargetObject targetObjectResult9_2 = TargetObject.builder("NRCellCU").container(ContainerType.ATTRIBUTES)
                .isAllParamQueried(true).isAllParamQueried(true).build();
        List<TargetObject> resultTargetObjects9_1 = new ArrayList<>();
        resultTargetObjects9_1.add(targetObjectResult9_1);
        resultTargetObjects9_1.add(targetObjectResult9_2);
        basePathRefinement.resolveWildCardObjectsInScopeAndTarget(filterCriteria, "RAN",
                FilterCriteria.ResolvingTopologyObjectType.ENTITY);
        Assertions.assertEquals(resultTargetObjects9_1, filterCriteria.getTargets());
        Assertions.assertEquals(or, filterCriteria.getScope());
    }

    @Test
    void testResolveWildCardObjectsInScopeAndTarget_10() {
        //root:-
        //Target:/attributes
        //Scope:/attributes[nCI = 12]; /attributes[gNBId = 6]
        //error no intersection in scope
        InnerFilterCriteria filterCriteria = InnerFilterCriteria.builder().build();
        filterCriteria.setTargets(targetResolver.resolve(null, "/attributes"));
        filterCriteria.setScope(scopeResolver.resolve(null, "/attributes[@nCI = 12]; /attributes[@gNBId = 6]"));
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.resolveWildCardObjectsInScopeAndTarget(
                filterCriteria, "RAN", FilterCriteria.ResolvingTopologyObjectType.ENTITY));
    }

    @Test
    void testResolveWildCardObjectsInScopeAndTarget_11() {
        //root:-
        //Target:/attributes, /id
        //Scope:/[id = "testId"]; /attributes[gNBId = 6]
        InnerFilterCriteria filterCriteria = InnerFilterCriteria.builder().build();
        filterCriteria.setTargets(targetResolver.resolve(null, "/attributes; /id"));
        filterCriteria.setScope(scopeResolver.resolve(null, "/id[@id = \"testId\"]; /attributes[@gNBId = 6]"));
        ScopeLogicalBlock OCUUPFunctionPgNBId = new ScopeLogicalBlock(ScopeObject.builder("OCUUPFunction").container(
                ContainerType.ATTRIBUTES).leaf("gNBId").queryFunction(QueryFunction.EQ).parameter("6").resolverDataType(
                        ResolverDataType.INTEGER).build());
        ScopeLogicalBlock ODUFunctionPgNBId = new ScopeLogicalBlock(ScopeObject.builder(ODU_FUNCTION).container(
                ContainerType.ATTRIBUTES).leaf("gNBId").queryFunction(QueryFunction.EQ).parameter("6").resolverDataType(
                        ResolverDataType.INTEGER).build());
        ScopeLogicalBlock OCUCPFunctionPgNBId = new ScopeLogicalBlock(ScopeObject.builder("OCUCPFunction").container(
                ContainerType.ATTRIBUTES).leaf("gNBId").queryFunction(QueryFunction.EQ).parameter("6").resolverDataType(
                        ResolverDataType.INTEGER).build());
        ScopeLogicalBlock OCUUPFunctionPid = new ScopeLogicalBlock(ScopeObject.builder("OCUUPFunction").container(
                ContainerType.ID).queryFunction(QueryFunction.EQ).parameter("testId").resolverDataType(
                        ResolverDataType.STRING).build());
        ScopeLogicalBlock ODUFunctionPid = new ScopeLogicalBlock(ScopeObject.builder(ODU_FUNCTION).container(
                ContainerType.ID).queryFunction(QueryFunction.EQ).parameter("testId").resolverDataType(
                        ResolverDataType.STRING).build());
        ScopeLogicalBlock OCUCPFunctionPid = new ScopeLogicalBlock(ScopeObject.builder("OCUCPFunction").container(
                ContainerType.ID).queryFunction(QueryFunction.EQ).parameter("testId").resolverDataType(
                        ResolverDataType.STRING).build());
        AndLogicalBlock OCUUPFunction = new AndLogicalBlock();
        OCUUPFunction.getChildren().add(OCUUPFunctionPgNBId);
        OCUUPFunction.getChildren().add(OCUUPFunctionPid);
        AndLogicalBlock ODUFunction = new AndLogicalBlock();
        ODUFunction.getChildren().add(ODUFunctionPgNBId);
        ODUFunction.getChildren().add(ODUFunctionPid);
        AndLogicalBlock OCUCPFunction = new AndLogicalBlock();
        OCUCPFunction.getChildren().add(OCUCPFunctionPgNBId);
        OCUCPFunction.getChildren().add(OCUCPFunctionPid);
        OrLogicalBlock or1 = new OrLogicalBlock();
        OrLogicalBlock or2 = new OrLogicalBlock();
        or1.getChildren().add(or2);
        or1.getChildren().add(ODUFunction);
        or2.getChildren().add(OCUCPFunction);
        or2.getChildren().add(OCUUPFunction);
        TargetObject targetObjectResult11_1 = TargetObject.builder("OCUUPFunction").container(ContainerType.ID).build();
        TargetObject targetObjectResult11_2 = TargetObject.builder(ODU_FUNCTION).container(ContainerType.ID).build();
        TargetObject targetObjectResult11_3 = TargetObject.builder("OCUCPFunction").container(ContainerType.ID).build();
        TargetObject targetObjectResult11_4 = TargetObject.builder("OCUUPFunction").container(ContainerType.ATTRIBUTES)
                .isAllParamQueried(true).build();
        TargetObject targetObjectResult11_5 = TargetObject.builder(ODU_FUNCTION).container(ContainerType.ATTRIBUTES)
                .isAllParamQueried(true).build();
        TargetObject targetObjectResult11_6 = TargetObject.builder("OCUCPFunction").container(ContainerType.ATTRIBUTES)
                .isAllParamQueried(true).build();
        List<TargetObject> resultTargetObjects11_1 = new ArrayList<>();
        resultTargetObjects11_1.add(targetObjectResult11_6);
        resultTargetObjects11_1.add(targetObjectResult11_4);
        resultTargetObjects11_1.add(targetObjectResult11_5);
        resultTargetObjects11_1.add(targetObjectResult11_3);
        resultTargetObjects11_1.add(targetObjectResult11_1);
        resultTargetObjects11_1.add(targetObjectResult11_2);
        basePathRefinement.resolveWildCardObjectsInScopeAndTarget(filterCriteria, "RAN",
                FilterCriteria.ResolvingTopologyObjectType.ENTITY);
        Assertions.assertEquals(resultTargetObjects11_1, filterCriteria.getTargets());
        Assertions.assertEquals(or1, filterCriteria.getScope());
    }

    @Test
    void testResolveWildCardObjectsInScopeAndTarget_12() {
        //root:-
        //Target: /attributes
        //Scope: /managed-by-managedElement[id = "me1"]
        InnerFilterCriteria filterCriteria2 = InnerFilterCriteria.builder().build();
        filterCriteria2.setTargets(targetResolver.resolve(null, "/attributes"));
        filterCriteria2.setScope(scopeResolver.resolve(null, "/managed-by-managedElement[@id = \"me1\"]"));
        basePathRefinement.handleAssociationsInScope((ScopeLogicalBlock) filterCriteria2.getScope(), "OAM");
        basePathRefinement.processTopologyObjectsWithContainerTypeNull(FilterCriteria.builder("OAM").filterCriteriaList(List
                .of(filterCriteria2)).build());
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.resolveWildCardObjectsInScopeAndTarget(
                filterCriteria2, "OAM", FilterCriteria.ResolvingTopologyObjectType.ENTITY));
    }

    @Test
    void testResolveWildCardObjectsInScopeAndTarget_13() {
        //root:-
        //Target: /attributes
        //Scope: /managed-by-managedElement[id = "me1"]
        InnerFilterCriteria filterCriteria2 = InnerFilterCriteria.builder().build();
        filterCriteria2.setTargets(targetResolver.resolve(null, "/attributes"));
        filterCriteria2.setScope(scopeResolver.resolve(null, "/managed-by-managedElement[@id = \"me1\"]"));
        TargetObject targetObjectResult13_1 = TargetObject.builder("OCUUPFunction").container(ContainerType.ATTRIBUTES)
                .isAllParamQueried(true).build();
        TargetObject targetObjectResult13_2 = TargetObject.builder("ORUFunction").container(ContainerType.ATTRIBUTES)
                .isAllParamQueried(true).build();
        TargetObject targetObjectResult13_3 = TargetObject.builder("NearRTRICFunction").container(ContainerType.ATTRIBUTES)
                .isAllParamQueried(true).build();
        TargetObject targetObjectResult13_4 = TargetObject.builder(ODU_FUNCTION).container(ContainerType.ATTRIBUTES)
                .isAllParamQueried(true).build();
        TargetObject targetObjectResult13_5 = TargetObject.builder("OCUCPFunction").container(ContainerType.ATTRIBUTES)
                .isAllParamQueried(true).build();
        List<TargetObject> resultTargetObjectsResult13_1 = new ArrayList<>();
        resultTargetObjectsResult13_1.add(targetObjectResult13_3);
        resultTargetObjectsResult13_1.add(targetObjectResult13_2);
        resultTargetObjectsResult13_1.add(targetObjectResult13_5);
        resultTargetObjectsResult13_1.add(targetObjectResult13_1);
        resultTargetObjectsResult13_1.add(targetObjectResult13_4);

        ScopeLogicalBlock NearRTRICFunction = new ScopeLogicalBlock(ScopeObject.builder(
                "NearRTRICFunction/managed-by-managedElement").container(ContainerType.ID).topologyObjectType(
                        TopologyObjectType.ASSOCIATION).innerContainer(List.of()).leaf(null).queryFunction(QueryFunction.EQ)
                .parameter("me1").resolverDataType(ResolverDataType.STRING).build());

        ScopeLogicalBlock ORUFunction = new ScopeLogicalBlock(ScopeObject.builder("ORUFunction/managed-by-managedElement")
                .container(ContainerType.ID).topologyObjectType(TopologyObjectType.ASSOCIATION).innerContainer(List.of())
                .leaf(null).queryFunction(QueryFunction.EQ).parameter("me1").resolverDataType(ResolverDataType.STRING)
                .build());
        ScopeLogicalBlock OCUUPFunction = new ScopeLogicalBlock(ScopeObject.builder(
                "OCUUPFunction/managed-by-managedElement").container(ContainerType.ID).topologyObjectType(
                        TopologyObjectType.ASSOCIATION).innerContainer(List.of()).leaf(null).queryFunction(QueryFunction.EQ)
                .parameter("me1").resolverDataType(ResolverDataType.STRING).build());
        ScopeLogicalBlock ODUFunction = new ScopeLogicalBlock(ScopeObject.builder("ODUFunction/managed-by-managedElement")
                .container(ContainerType.ID).topologyObjectType(TopologyObjectType.ASSOCIATION).innerContainer(List.of())
                .leaf(null).queryFunction(QueryFunction.EQ).parameter("me1").resolverDataType(ResolverDataType.STRING)
                .build());
        ScopeLogicalBlock OCUCPFunction = new ScopeLogicalBlock(ScopeObject.builder(
                "OCUCPFunction/managed-by-managedElement").container(ContainerType.ID).topologyObjectType(
                        TopologyObjectType.ASSOCIATION).innerContainer(List.of()).leaf(null).queryFunction(QueryFunction.EQ)
                .parameter("me1").resolverDataType(ResolverDataType.STRING).build());

        AndOrLogicalBlock or1 = new OrLogicalBlock();
        AndOrLogicalBlock or2 = new OrLogicalBlock();
        AndOrLogicalBlock or3 = new OrLogicalBlock();
        AndOrLogicalBlock or4 = new OrLogicalBlock();

        or4.getChildren().add(NearRTRICFunction);
        or4.getChildren().add(OCUCPFunction);
        or3.getChildren().add(or4);
        or3.getChildren().add(OCUUPFunction);
        or2.getChildren().add(or3);
        or2.getChildren().add(ODUFunction);
        or1.getChildren().add(or2);
        or1.getChildren().add(ORUFunction);
        basePathRefinement.handleAssociationsInScope((ScopeLogicalBlock) filterCriteria2.getScope(), "RAN");
        basePathRefinement.processTopologyObjectsWithContainerTypeNull(FilterCriteria.builder("RAN").filterCriteriaList(List
                .of(filterCriteria2)).build());
        basePathRefinement.resolveWildCardObjectsInScopeAndTarget(filterCriteria2, "RAN",
                FilterCriteria.ResolvingTopologyObjectType.ENTITY);

        Assertions.assertEquals(resultTargetObjectsResult13_1, filterCriteria2.getTargets());
        Assertions.assertEquals(or1, filterCriteria2.getScope());
    }

    @Test
    void testResolveWildCardObjectsInScopeAndTarget_14() {
        InnerFilterCriteria filterCriteria = InnerFilterCriteria.builder().build();
        filterCriteria.setTargets(targetResolver.resolve(null, null));
        filterCriteria.setScope(scopeResolver.resolve(null, "/ODUFunction[@id = \"testId\"]"));
        TargetObject targetObjectResult9_1 = TargetObject.builder(ODU_FUNCTION).container(ContainerType.ID)
                .isAllParamQueried(false).build();
        List<TargetObject> resultTargetObjects9_1 = new ArrayList<>();
        resultTargetObjects9_1.add(targetObjectResult9_1);
        basePathRefinement.handleAssociationsInScope((ScopeLogicalBlock) filterCriteria.getScope(), "RAN");
        basePathRefinement.processTopologyObjectsWithContainerTypeNull(FilterCriteria.builder("RAN").filterCriteriaList(List
                .of(filterCriteria)).build());
        basePathRefinement.resolveWildCardObjectsInScopeAndTarget(filterCriteria, "RAN",
                FilterCriteria.ResolvingTopologyObjectType.ENTITY);
        Assertions.assertEquals(resultTargetObjects9_1, filterCriteria.getTargets());
    }

    @Test
    void testResolveWildCardObjectsInScopeAndTarget_15() {
        InnerFilterCriteria filterCriteria = InnerFilterCriteria.builder().build();
        filterCriteria.setTargets(targetResolver.resolve(null, "/ODUFUNCTION_PROVIDES_NRCELLDU"));
        filterCriteria.setScope(scopeResolver.resolve(null, "/id[text() = \"testId\"]"));
        ScopeObject scopeObject = ScopeObject.builder("ODUFUNCTION_PROVIDES_NRCELLDU").container(ContainerType.ID)
                .parameter("testId").queryFunction(QueryFunction.EQ).resolverDataType(ResolverDataType.STRING).build();
        basePathRefinement.processTopologyObjectsWithContainerTypeNull(FilterCriteria.builder("RAN").filterCriteriaList(List
                .of(filterCriteria)).build());
        basePathRefinement.resolveWildCardObjectsInScopeAndTarget(filterCriteria, "RAN",
                FilterCriteria.ResolvingTopologyObjectType.RELATIONSHIP);
        Assertions.assertEquals(new ScopeLogicalBlock(scopeObject), filterCriteria.getScope());
    }

    @Test
    void testResolveWildCardObjectsInScopeAndTarget_16() {
        //root:-
        //Target: /decorators
        //Scope: /managed-by-managedElement[id = "me1"]
        InnerFilterCriteria filterCriteria2 = InnerFilterCriteria.builder().build();
        filterCriteria2.setTargets(targetResolver.resolve(null,
                "/MANAGEDELEMENT_MANAGES_OCUCPFUNCTION/decorators; /MANAGEDELEMENT_MANAGES_OCUUPFUNCTION/decorators"));
        filterCriteria2.setScope(scopeResolver.resolve(null, "/managed-by-managedElement[@id = \"me1\"]"));

        TargetObject targetObjectResult13_1 = TargetObject.builder("MANAGEDELEMENT_MANAGES_OCUCPFUNCTION").container(
                ContainerType.DECORATORS).build();
        TargetObject targetObjectResult13_2 = TargetObject.builder("MANAGEDELEMENT_MANAGES_OCUUPFUNCTION").container(
                ContainerType.DECORATORS).build();
        List<TargetObject> resultTargetObjectsResult13_1 = new ArrayList<>();
        resultTargetObjectsResult13_1.add(targetObjectResult13_2);
        resultTargetObjectsResult13_1.add(targetObjectResult13_1);

        ScopeLogicalBlock OCUCPFunction = new ScopeLogicalBlock(ScopeObject.builder(
                "MANAGEDELEMENT_MANAGES_OCUCPFUNCTION/managed-by-managedElement").topologyObjectType(
                        TopologyObjectType.ASSOCIATION).container(ContainerType.ID).innerContainer(List.of()).leaf(null)
                .queryFunction(QueryFunction.EQ).parameter("me1").resolverDataType(ResolverDataType.STRING).build());
        ScopeLogicalBlock OCUUPFunction = new ScopeLogicalBlock(ScopeObject.builder(
                "MANAGEDELEMENT_MANAGES_OCUUPFUNCTION/managed-by-managedElement").topologyObjectType(
                        TopologyObjectType.ASSOCIATION).container(ContainerType.ID).innerContainer(List.of()).leaf(null)
                .queryFunction(QueryFunction.EQ).parameter("me1").resolverDataType(ResolverDataType.STRING).build());

        AndOrLogicalBlock or1 = new OrLogicalBlock();

        or1.getChildren().add(OCUUPFunction);
        or1.getChildren().add(OCUCPFunction);

        basePathRefinement.handleAssociationsInScope((ScopeLogicalBlock) filterCriteria2.getScope(), "RAN");
        basePathRefinement.processTopologyObjectsWithContainerTypeNull(FilterCriteria.builder("RAN").filterCriteriaList(List
                .of(filterCriteria2)).build());
        basePathRefinement.resolveWildCardObjectsInScopeAndTarget(filterCriteria2, "RAN",
                FilterCriteria.ResolvingTopologyObjectType.RELATIONSHIP);

        Assertions.assertEquals(resultTargetObjectsResult13_1, filterCriteria2.getTargets());
        Assertions.assertEquals(or1, filterCriteria2.getScope());
    }

    @Test
    void testResolveWildCardObjectsInScopeAndTarget_17() {
        //root:-
        //Target: /decorators
        //Scope: /managed-by-managedElement[id = "me1"]
        InnerFilterCriteria filterCriteria2 = InnerFilterCriteria.builder().build();
        filterCriteria2.setTargets(targetResolver.resolve(null, "/decorators"));
        filterCriteria2.setScope(scopeResolver.resolve(null, "/managed-by-managedElement[@id = \"me1\"]"));

        TargetObject targetObjectResult13_1 = TargetObject.builder("MANAGEDELEMENT_MANAGES_OCUCPFUNCTION").container(
                ContainerType.DECORATORS).build();
        TargetObject targetObjectResult13_2 = TargetObject.builder("MANAGEDELEMENT_MANAGES_NEARRTRICFUNCTION").container(
                ContainerType.DECORATORS).build();
        TargetObject targetObjectResult13_3 = TargetObject.builder("MANAGEDELEMENT_MANAGES_OCUUPFUNCTION").container(
                ContainerType.DECORATORS).build();
        TargetObject targetObjectResult13_4 = TargetObject.builder("MANAGEDELEMENT_MANAGES_ODUFUNCTION").container(
                ContainerType.DECORATORS).build();
        TargetObject targetObjectResult13_5 = TargetObject.builder("MANAGEDELEMENT_MANAGES_ORUFUNCTION").container(
                ContainerType.DECORATORS).build();
        List<TargetObject> resultTargetObjectsResult13_1 = new ArrayList<>();
        resultTargetObjectsResult13_1.add(targetObjectResult13_3);
        resultTargetObjectsResult13_1.add(targetObjectResult13_5);
        resultTargetObjectsResult13_1.add(targetObjectResult13_4);
        resultTargetObjectsResult13_1.add(targetObjectResult13_1);
        resultTargetObjectsResult13_1.add(targetObjectResult13_2);

        ScopeLogicalBlock OCUUPFunction = new ScopeLogicalBlock(ScopeObject.builder(
                "MANAGEDELEMENT_MANAGES_OCUUPFUNCTION/managed-by-managedElement").topologyObjectType(
                        TopologyObjectType.ASSOCIATION).container(ContainerType.ID).innerContainer(List.of()).leaf(null)
                .queryFunction(QueryFunction.EQ).parameter("me1").resolverDataType(ResolverDataType.STRING).build());
        ScopeLogicalBlock NearRTRICFunction = new ScopeLogicalBlock(ScopeObject.builder(
                "MANAGEDELEMENT_MANAGES_NEARRTRICFUNCTION/managed-by-managedElement").topologyObjectType(
                        TopologyObjectType.ASSOCIATION).container(ContainerType.ID).innerContainer(List.of()).leaf(null)
                .queryFunction(QueryFunction.EQ).parameter("me1").resolverDataType(ResolverDataType.STRING).build());
        ScopeLogicalBlock ODUFunction = new ScopeLogicalBlock(ScopeObject.builder(
                "MANAGEDELEMENT_MANAGES_ODUFUNCTION/managed-by-managedElement").topologyObjectType(
                        TopologyObjectType.ASSOCIATION).container(ContainerType.ID).innerContainer(List.of()).leaf(null)
                .queryFunction(QueryFunction.EQ).parameter("me1").resolverDataType(ResolverDataType.STRING).build());
        ScopeLogicalBlock OCUCPFunction = new ScopeLogicalBlock(ScopeObject.builder(
                "MANAGEDELEMENT_MANAGES_OCUCPFUNCTION/managed-by-managedElement").topologyObjectType(
                        TopologyObjectType.ASSOCIATION).container(ContainerType.ID).innerContainer(List.of()).leaf(null)
                .queryFunction(QueryFunction.EQ).parameter("me1").resolverDataType(ResolverDataType.STRING).build());
        ScopeLogicalBlock ORUFunction = new ScopeLogicalBlock(ScopeObject.builder(
                "MANAGEDELEMENT_MANAGES_ORUFUNCTION/managed-by-managedElement").topologyObjectType(
                        TopologyObjectType.ASSOCIATION).container(ContainerType.ID).innerContainer(List.of()).leaf(null)
                .queryFunction(QueryFunction.EQ).parameter("me1").resolverDataType(ResolverDataType.STRING).build());

        AndOrLogicalBlock or1 = new OrLogicalBlock();
        AndOrLogicalBlock or2 = new OrLogicalBlock();
        AndOrLogicalBlock or3 = new OrLogicalBlock();
        AndOrLogicalBlock or4 = new OrLogicalBlock();

        or4.getChildren().add(NearRTRICFunction);
        or4.getChildren().add(OCUCPFunction);
        or3.getChildren().add(or4);
        or3.getChildren().add(OCUUPFunction);
        or2.getChildren().add(or3);
        or2.getChildren().add(ODUFunction);
        or1.getChildren().add(or2);
        or1.getChildren().add(ORUFunction);

        basePathRefinement.handleAssociationsInScope((ScopeLogicalBlock) filterCriteria2.getScope(), "REL_OAM_RAN");
        basePathRefinement.processTopologyObjectsWithContainerTypeNull(FilterCriteria.builder("REL_OAM_RAN")
                .filterCriteriaList(List.of(filterCriteria2)).build());
        basePathRefinement.resolveWildCardObjectsInScopeAndTarget(filterCriteria2, "REL_OAM_RAN",
                FilterCriteria.ResolvingTopologyObjectType.RELATIONSHIP);

        Assertions.assertEquals(resultTargetObjectsResult13_1, filterCriteria2.getTargets());
        Assertions.assertEquals(or1, filterCriteria2.getScope());
    }

    @Test
    void testResolveUndefinedTopologyObjects_Entity() {
        FilterCriteria filterCriteria = FilterCriteria.builder("RAN").build();
        TargetObject targetObject = TargetObject.builder(ODU_FUNCTION).build();
        ScopeObject scopeObject = ScopeObject.builder(ODU_FUNCTION).container(ContainerType.ATTRIBUTES).leaf("gNBId")
                .queryFunction(QueryFunction.EQ).parameter("1").dataType(DataType.BIGINT).build();
        LogicalBlock scope = new ScopeLogicalBlock(scopeObject);
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().targets(new ArrayList<>(Arrays.asList(
                targetObject))).scope(scope).build()));
        try (MockedStatic<SchemaRegistry> utilities = Mockito.mockStatic(SchemaRegistry.class)) {
            utilities.when(() -> SchemaRegistry.getEntityNamesByDomain("RAN")).thenReturn(Arrays.asList(ODU_FUNCTION,
                    "NRCellDU"));
            utilities.when(() -> SchemaRegistry.getRelationNamesByDomain("RAN")).thenReturn(Arrays.asList(
                    "ODUFUNCTION_PROVIDES_NRCELLDU"));
            basePathRefinement.resolveUndefinedTopologyObjectTypes(filterCriteria);
            Assertions.assertEquals(TopologyObjectType.ENTITY, filterCriteria.getFilterCriteriaList().get(0).getTargets()
                    .get(0).getTopologyObjectType());
            Assertions.assertEquals(TopologyObjectType.ENTITY, ((ScopeLogicalBlock) filterCriteria.getFilterCriteriaList()
                    .get(0).getScope()).getScopeObject().getTopologyObjectType());
            filterCriteria.getFilterCriteriaList().get(0).setScope(EmptyLogicalBlock.getInstance());
            targetObject.setTopologyObjectType(TopologyObjectType.UNDEFINED);
            basePathRefinement.resolveUndefinedTopologyObjectTypes(filterCriteria);
            Assertions.assertEquals(TopologyObjectType.ENTITY, filterCriteria.getFilterCriteriaList().get(0).getTargets()
                    .get(0).getTopologyObjectType());
        }
    }

    @Test
    void testResolveUndefinedTopologyObjects_Relation() {
        FilterCriteria filterCriteria = FilterCriteria.builder("RAN").build();
        TargetObject targetObject = TargetObject.builder("ODUFUNCTION_PROVIDES_NRCELLDU").build();
        ScopeObject scopeObject = ScopeObject.builder("ODUFUNCTION_PROVIDES_NRCELLDU").container(ContainerType.ID)
                .queryFunction(QueryFunction.EQ).parameter("1").dataType(DataType.PRIMITIVE).build();
        LogicalBlock scope = new ScopeLogicalBlock(scopeObject);
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().targets(new ArrayList<>(Arrays.asList(
                targetObject))).scope(scope).build()));
        try (MockedStatic<SchemaRegistry> utilities = Mockito.mockStatic(SchemaRegistry.class)) {
            utilities.when(() -> SchemaRegistry.getEntityNamesByDomain("RAN")).thenReturn(Arrays.asList(ODU_FUNCTION,
                    "NRCellDU"));
            utilities.when(() -> SchemaRegistry.getRelationNamesByDomain("RAN")).thenReturn(Arrays.asList(
                    "ODUFUNCTION_PROVIDES_NRCELLDU"));
            basePathRefinement.resolveUndefinedTopologyObjectTypes(filterCriteria);
            Assertions.assertEquals(TopologyObjectType.RELATION, filterCriteria.getFilterCriteriaList().get(0).getTargets()
                    .get(0).getTopologyObjectType());
            Assertions.assertEquals(TopologyObjectType.RELATION, ((ScopeLogicalBlock) filterCriteria.getFilterCriteriaList()
                    .get(0).getScope()).getScopeObject().getTopologyObjectType());
        }
    }

    @Test
    void testResolveUndefinedTopologyObjects_InvalidTargetAndScope() {
        FilterCriteria filterCriteria = FilterCriteria.builder("RAN").build();
        TargetObject targetObject = TargetObject.builder("InvalidObject").build();
        ScopeObject scopeObject = ScopeObject.builder("InvalidObject").container(ContainerType.ATTRIBUTES).leaf("gNBId")
                .queryFunction(QueryFunction.EQ).parameter("1").dataType(DataType.BIGINT).build();
        LogicalBlock scope = new ScopeLogicalBlock(scopeObject);
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().targets(new ArrayList<>(Arrays.asList(
                targetObject))).scope(scope).build()));
        try (MockedStatic<SchemaRegistry> utilities = Mockito.mockStatic(SchemaRegistry.class)) {
            utilities.when(() -> SchemaRegistry.getEntityNamesByDomain("RAN")).thenReturn(Arrays.asList(ODU_FUNCTION,
                    "NRCellDU", "EntityAndRelation"));
            utilities.when(() -> SchemaRegistry.getRelationNamesByDomain("RAN")).thenReturn(Arrays.asList(
                    "ODUFUNCTION_PROVIDES_NRCELLDU", "EntityAndRelation"));
            // Error thrown because of invalid topology object in targetFilter
            Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.resolveUndefinedTopologyObjectTypes(
                    filterCriteria));
            targetObject.setTopologyObject(ODU_FUNCTION);
            // Error thrown because of invalid topology object in scopeFilter
            Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.resolveUndefinedTopologyObjectTypes(
                    filterCriteria));
            scopeObject.setTopologyObject("EntityAndRelation");
            // Error thrown because of topology object type is ambiguous
            Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.resolveUndefinedTopologyObjectTypes(
                    filterCriteria));
            targetObject.setTopologyObjectType(TopologyObjectType.UNDEFINED);
            targetObject.setTopologyObject("EntityAndRelation");
            // Error thrown because of topology object type is ambiguous
            Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.resolveUndefinedTopologyObjectTypes(
                    filterCriteria));
        }
    }

    @Test
    void testValidateContainers() {
        FilterCriteria filterCriteria = FilterCriteria.builder("RAN").build();
        TargetObject targetObject0 = TargetObject.builder(ODU_FUNCTION).container(ContainerType.ID).params(new ArrayList<>(
                Arrays.asList("gNBId"))).build();
        targetObject0.setTopologyObjectType(TopologyObjectType.ENTITY);
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().targets(new ArrayList<>(Arrays.asList(
                targetObject0))).build()));
        // Reason: container:ID, params is not empty
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.validateContainers(filterCriteria));
        TargetObject targetObject1 = TargetObject.builder(ODU_FUNCTION).container(ContainerType.ATTRIBUTES).params(
                new ArrayList<>(Arrays.asList("gNBId", "gNBIdLength", "notValidAttribute1", "notValidAttribute2"))).build();
        targetObject1.setTopologyObjectType(TopologyObjectType.ENTITY);
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().targets(List.of(targetObject1))
                .build()));
        ScopeObject scopeObject = ScopeObject.builder(ODU_FUNCTION).container(ContainerType.ATTRIBUTES).leaf("gNBId")
                .queryFunction(QueryFunction.EQ).parameter("1").dataType(DataType.BIGINT).build();
        scopeObject.setTopologyObjectType(TopologyObjectType.ENTITY);
        LogicalBlock scopeLogicalBlock = new ScopeLogicalBlock(scopeObject);
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().targets(new ArrayList<>(Arrays.asList(
                targetObject1))).scope(scopeLogicalBlock).build()));
        // Reason: invalid attributes in ENTITY type targetObject
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.validateContainers(filterCriteria));
        TargetObject targetObject2 = TargetObject.builder("ODUFUNCTION_PROVIDES_NRCELLDU").container(
                ContainerType.ATTRIBUTES).topologyObjectType(TopologyObjectType.RELATION).params(new ArrayList<>(Arrays
                        .asList("notValidAttribute1", "notValidAttribute2"))).build();
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().targets(new ArrayList<>(Arrays.asList(
                targetObject2))).scope(scopeLogicalBlock).build()));
        // Reason: invalid attributes in RELATION type targetObject
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.validateContainers(filterCriteria));
        scopeObject.setLeaf("notValidAttribute");
        TargetObject targetObject3 = TargetObject.builder(ODU_FUNCTION).container(ContainerType.ATTRIBUTES)
                .topologyObjectType(TopologyObjectType.ENTITY).params(new ArrayList<>(Arrays.asList("gNBId",
                        "gNBIdLength"))).build();
        targetObject3.setTopologyObjectType(TopologyObjectType.ENTITY);
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().targets(new ArrayList<>(Arrays.asList(
                targetObject3))).scope(scopeLogicalBlock).build()));
        // Reason: invalid attributes in ENTITY type scopeObject
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.validateContainers(filterCriteria));
        scopeObject.setLeaf("gNBId");
        Assertions.assertDoesNotThrow(() -> basePathRefinement.validateContainers(filterCriteria));
        TargetObject targetObject4 = TargetObject.builder(ODU_FUNCTION).container(ContainerType.SOURCE_IDS)
                .topologyObjectType(TopologyObjectType.ENTITY).params(new ArrayList<>(Arrays.asList("gNBId"))).build();
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().targets(new ArrayList<>(Arrays.asList(
                targetObject4))).scope(scopeLogicalBlock).build()));
        // Reason: invalid source id param for ENTITY type targetObject
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.validateContainers(filterCriteria));
        targetObject4.setParams(Collections.emptyList());
        Assertions.assertDoesNotThrow(() -> basePathRefinement.validateContainers(filterCriteria));
        TargetObject targetObject5 = TargetObject.builder("ODUFUNCTION_PROVIDES_NRCELLDU").container(
                ContainerType.SOURCE_IDS).topologyObjectType(TopologyObjectType.RELATION).params(new ArrayList<>(Arrays
                        .asList("InvalidSourceIdParam"))).build();
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().targets(new ArrayList<>(Arrays.asList(
                targetObject5))).scope(scopeLogicalBlock).build()));
        // Reason: invalid source id param for RELATION type targetObject
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.validateContainers(filterCriteria));
        targetObject5.setParams(new ArrayList<>(Arrays.asList(ITEM)));
        Assertions.assertDoesNotThrow(() -> basePathRefinement.validateContainers(filterCriteria));
    }

    @Test
    void testValidateContainers_Associations() {
        FilterCriteria filterCriteria = FilterCriteria.builder("RAN").build();
        ScopeObject scopeObject = ScopeObject.builder(ODU_FUNCTION + "/provided-nrCellDu").topologyObjectType(
                TopologyObjectType.ASSOCIATION).container(ContainerType.ATTRIBUTES).leaf("nCI").queryFunction(
                        QueryFunction.EQ).parameter("1").dataType(DataType.BIGINT).build();
        // scopeObject.setInnerContainer(Arrays.asList("provided-nrCellDu"));
        scopeObject.setTopologyObjectType(TopologyObjectType.ASSOCIATION);
        LogicalBlock scopeLogicalBlock = new ScopeLogicalBlock(scopeObject);
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().targets(List.of()).scope(
                scopeLogicalBlock).build()));
        Assertions.assertDoesNotThrow(() -> basePathRefinement.validateContainers(filterCriteria));
        scopeObject.setLeaf(null);
        Assertions.assertDoesNotThrow(() -> basePathRefinement.validateContainers(filterCriteria));
        scopeObject.setLeaf("invalid");
        // Reason: invalid leaf for scopeObject
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.validateContainers(filterCriteria));
        scopeObject.setLeaf("nCI");
        scopeObject.setInnerContainer(Arrays.asList("invalid-association"));
        // Reason: invalid association added in innerContainer list for ENTITY type scopeObject
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.validateContainers(filterCriteria));
        scopeObject.setInnerContainer(Arrays.asList("invalid-innerContainer"));
        // Reason: in case of association there are no innerContainer elements
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.validateContainers(filterCriteria));
        scopeObject.setTopologyObject("ODUFUNCTION_PROVIDES_NRCELLDU/provided-nrCellDu");
        scopeObject.setTopologyObjectType(TopologyObjectType.ASSOCIATION);
        scopeObject.setInnerContainer(Collections.emptyList());
        Assertions.assertDoesNotThrow(() -> basePathRefinement.validateContainers(filterCriteria));
        scopeObject.setTopologyObjectType(TopologyObjectType.UNDEFINED);
        // Reason: cannot validate container for UNDEFINED type topologyObject
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.validateContainers(filterCriteria));
        scopeObject.setTopologyObjectType(TopologyObjectType.ASSOCIATION);
        scopeObject.setInnerContainer(Arrays.asList("invalid-association"));
        // Reason: invalid association added in innerContainer list for RELATION type scopeObject
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.validateContainers(filterCriteria));
    }

    @Test
    void testValidateScopeParametersDataType() {
        // for gNBId attribute the BIGINT dataType is set
        FilterCriteria filterCriteria = FilterCriteria.builder("RAN").build();
        LogicalBlock logicalBlock1 = scopeResolver.process(ODU_FUNCTION, "/attributes[@gNBId=21]");
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock1).build()));
        basePathRefinement.validateScopeParametersDataType(filterCriteria);
        Assertions.assertEquals(DataType.BIGINT, ((ScopeLogicalBlock) logicalBlock1).getScopeObject().getDataType());
        // error reason: for gNBId attribute string value is not accepted
        LogicalBlock logicalBlock2 = scopeResolver.process(ODU_FUNCTION, "/attributes[@gNBId='abc']");
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock2).build()));
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.validateScopeParametersDataType(
                filterCriteria));
        // error reason: id value should be entered as string (' ')
        LogicalBlock logicalBlock3 = scopeResolver.process(ODU_FUNCTION, "/ODUFunction[@id=1]");
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock3).build()));
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.validateScopeParametersDataType(
                filterCriteria));
        // scopeFilter: /managed-by-managedElement[@id='me1'] -> dataType is set to PRIMITIVE
        ScopeLogicalBlock scopeLogicalBlock1 = new ScopeLogicalBlock(ScopeObject.builder(ODU_FUNCTION).innerContainer(
                new ArrayList<>(Arrays.asList("managed-by-managedElement"))).container(ContainerType.ID).topologyObjectType(
                        TopologyObjectType.ENTITY).queryFunction(QueryFunction.EQ).leaf(ID_COLUMN_NAME).parameter("me1")
                .resolverDataType(ResolverDataType.STRING).build());
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(scopeLogicalBlock1).build()));
        basePathRefinement.validateScopeParametersDataType(filterCriteria);
        Assertions.assertEquals(DataType.PRIMITIVE, scopeLogicalBlock1.getScopeObject().getDataType());
        // error reason: scopeFilter: /managed-by-managedElement[@id=1] -> ResolverDataType is INTEGER, but in case of id it should be STRING
        ScopeLogicalBlock scopeLogicalBlock2 = new ScopeLogicalBlock(ScopeObject.builder(ODU_FUNCTION).innerContainer(
                new ArrayList<>(Arrays.asList("managed-by-managedElement"))).container(ContainerType.ID).topologyObjectType(
                        TopologyObjectType.ENTITY).queryFunction(QueryFunction.EQ).leaf(ID_COLUMN_NAME).parameter("1")
                .resolverDataType(ResolverDataType.INTEGER).build());
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(scopeLogicalBlock2).build()));
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.validateScopeParametersDataType(
                filterCriteria));
        // error reason: scopeFilter: /provided-nrCellDu[@cellLocalId=1] -> only id can be queried for associations
        ScopeLogicalBlock scopeLogicalBlock3 = new ScopeLogicalBlock(ScopeObject.builder(ODU_FUNCTION).innerContainer(
                new ArrayList<>(Arrays.asList("provided-nrCellDu"))).container(ContainerType.ID).topologyObjectType(
                        TopologyObjectType.ENTITY).queryFunction(QueryFunction.EQ).leaf("cellLocalId").parameter("1")
                .resolverDataType(ResolverDataType.INTEGER).build());
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(scopeLogicalBlock3).build()));
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.validateScopeParametersDataType(
                filterCriteria));
        // for id the PRIMITIVE dataType is set
        LogicalBlock logicalBlock4 = scopeResolver.process(ODU_FUNCTION, "/ODUFunction[@id='odu1']");
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock4).build()));
        basePathRefinement.validateScopeParametersDataType(filterCriteria);
        Assertions.assertEquals(DataType.PRIMITIVE, ((ScopeLogicalBlock) logicalBlock4).getScopeObject().getDataType());
        // error reason: for id INTEGER ResolverDataType is not accepted
        LogicalBlock logicalBlock5 = scopeResolver.process(ODU_FUNCTION, "/ODUFunction[@id=1]");
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock5).build()));
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.validateScopeParametersDataType(
                filterCriteria));
        // error reason: for gNBCUName attribute INTEGER ResolverDataType is not accepted
        LogicalBlock logicalBlock6 = scopeResolver.process("OCUCPFunction", "/attributes[@gNBCUName=1]");
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock6).build()));
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.validateScopeParametersDataType(
                filterCriteria));
        // for sourceIds container PRIMITIVE dataType is set
        LogicalBlock logicalBlock7 = scopeResolver.process(ODU_FUNCTION, "/sourceIds[@items = 'someSourceId']");
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock7).build()));
        basePathRefinement.validateScopeParametersDataType(filterCriteria);
        Assertions.assertEquals(DataType.PRIMITIVE, scopeLogicalBlock1.getScopeObject().getDataType());
        // error reason: for sourceIds container INTEGER dataType is not accepted
        LogicalBlock logicalBlock8 = scopeResolver.process(ODU_FUNCTION, "/sourceIds[@items = 1]");
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock8).build()));
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.validateScopeParametersDataType(
                filterCriteria));
        // for decorators container PRIMITIVE dataType is accepted
        LogicalBlock logicalBlock9 = scopeResolver.process(ODU_FUNCTION, "/decorators[@module-x:stringdata = 'Ericsson']");
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock9).build()));
        Assertions.assertDoesNotThrow(() -> basePathRefinement.validateScopeParametersDataType(filterCriteria));
        // for decorators container INTEGER dataType is accepted
        LogicalBlock logicalBlock10 = scopeResolver.process(ODU_FUNCTION, "/decorators[@module-x:intdata = 2]");
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock10).build()));
        Assertions.assertDoesNotThrow(() -> basePathRefinement.validateScopeParametersDataType(filterCriteria));
        LogicalBlock logicalBlock11 = scopeResolver.process(ODU_FUNCTION,
                "/decorators[contains(@odu-function-model:location, 'Stock')]");
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock11).build()));
        Assertions.assertDoesNotThrow(() -> basePathRefinement.validateScopeParametersDataType(filterCriteria));
        //For classifiers container PRIMITIVE dataType is accepted
        LogicalBlock logicalBlock12 = scopeResolver.process(ODU_FUNCTION,
                "/classifiers[@item = 'odu-function-model:Rural']");
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock12).build()));
        Assertions.assertDoesNotThrow(() -> basePathRefinement.validateScopeParametersDataType(filterCriteria));
        //For classifiers container INTEGER dataType is NOT accepted
        LogicalBlock logicalBlock13 = scopeResolver.process(ODU_FUNCTION, "/classifiers[@item = 23 ]");
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock13).build()));
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.validateScopeParametersDataType(
                filterCriteria));
        //for complex attribute INTEGER is accepted
        LogicalBlock logicalBlock14 = scopeResolver.process(ODU_FUNCTION, "/attributes/dUpLMNId[@mnc = 2]");
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock14).build()));
        Assertions.assertDoesNotThrow(() -> basePathRefinement.validateScopeParametersDataType(filterCriteria));
        //for complex attribute PRIMITIVE is accepted
        LogicalBlock logicalBlock15 = scopeResolver.process(ODU_FUNCTION, "/attributes/dUpLMNId[@mnc = 'some value']");
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock15).build()));
        Assertions.assertDoesNotThrow(() -> basePathRefinement.validateScopeParametersDataType(filterCriteria));
        //for metadata container
        LogicalBlock logicalBlock16 = scopeResolver.process(ODU_FUNCTION, "/metadata[@reliabilityIndicator = 'OK']");
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(logicalBlock16).build()));
        Assertions.assertDoesNotThrow(() -> basePathRefinement.validateScopeParametersDataType(filterCriteria));
    }

    @Test
    void testCheckIfTargetMatchesWithScope() {
        FilterCriteria filterCriteria = FilterCriteria.builder("RAN").build();
        TargetObject targetObject1 = TargetObject.builder(ODU_FUNCTION).container(ContainerType.ATTRIBUTES).params(List.of(
                "gNBId", "gNBIdLength")).build();
        TargetObject targetObject2 = TargetObject.builder("NRCellDU").container(ContainerType.ATTRIBUTES).params(List.of(
                "nCI")).build();
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().targets(new ArrayList<>(Arrays.asList(
                targetObject1, targetObject2))).build()));
        OrLogicalBlock orLogicalBlock1 = new OrLogicalBlock();
        ScopeObject scopeObject1 = ScopeObject.builder(ODU_FUNCTION).container(ContainerType.ATTRIBUTES).leaf("gNBIdLength")
                .queryFunction(QueryFunction.EQ).parameter("1").dataType(DataType.BIGINT).build();
        ScopeObject scopeObject2 = ScopeObject.builder(ODU_FUNCTION).container(ContainerType.ATTRIBUTES).leaf("gNBId")
                .queryFunction(QueryFunction.EQ).parameter("8").dataType(DataType.BIGINT).build();
        ScopeLogicalBlock scopeLogicalBlock1 = new ScopeLogicalBlock(scopeObject1);
        ScopeLogicalBlock scopeLogicalBlock2 = new ScopeLogicalBlock(scopeObject2);
        orLogicalBlock1.setChildren(new ArrayList<>(Arrays.asList(scopeLogicalBlock1, scopeLogicalBlock2)));
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().targets(new ArrayList<>(Arrays.asList(
                targetObject1, targetObject2))).scope(orLogicalBlock1).build()));
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.checkIfTargetMatchesWithScope(
                filterCriteria.getFilterCriteriaList().get(0), filterCriteria.getDomain()));
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().targets(new ArrayList<>(Arrays.asList(
                targetObject1))).scope(orLogicalBlock1).build()));
        Assertions.assertDoesNotThrow(() -> basePathRefinement.checkIfTargetMatchesWithScope(filterCriteria
                .getFilterCriteriaList().get(0), filterCriteria.getDomain()));
        ScopeObject scopeObject3 = ScopeObject.builder("NRSectorCarrier").container(ContainerType.ATTRIBUTES).leaf(
                "arfcnUL").queryFunction(QueryFunction.EQ).parameter("8").dataType(DataType.BIGINT).build();
        ScopeLogicalBlock scopeLogicalBlock3 = new ScopeLogicalBlock(scopeObject3);
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().targets(new ArrayList<>(Arrays.asList(
                targetObject1))).scope(scopeLogicalBlock3).build()));
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.checkIfTargetMatchesWithScope(
                filterCriteria.getFilterCriteriaList().get(0), filterCriteria.getDomain()));
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().targets(Collections.emptyList()).scope(
                scopeLogicalBlock3).build()));
        Assertions.assertDoesNotThrow(() -> basePathRefinement.checkIfTargetMatchesWithScope(filterCriteria
                .getFilterCriteriaList().get(0), filterCriteria.getDomain()));
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().targets(Collections.emptyList()).scope(
                EmptyLogicalBlock.getInstance()).build()));
        Assertions.assertDoesNotThrow(() -> basePathRefinement.checkIfTargetMatchesWithScope(filterCriteria
                .getFilterCriteriaList().get(0), filterCriteria.getDomain()));
    }

    @Test
    void testSplitFilterCriteria() {
        FilterCriteria filterCriteria = FilterCriteria.builder("RAN").build();
        TargetObject targetObject1 = TargetObject.builder(ODU_FUNCTION).container(ContainerType.ATTRIBUTES).params(List.of(
                "gNBId", "gNBIdLength")).build();
        TargetObject targetObject2 = TargetObject.builder("NRCellDU").container(ContainerType.ATTRIBUTES).params(List.of(
                "nCI")).build();
        TargetObject targetObject3 = TargetObject.builder("NRCellDU").container(ContainerType.ATTRIBUTES).params(List.of(
                "nRPCI")).build();
        TargetObject targetObject4 = TargetObject.builder("EUtranCell").container(ContainerType.ATTRIBUTES).params(List.of(
                "earFcn", "fdn")).isAllParamQueried(true).build();
        ScopeObject scopeObject1 = ScopeObject.builder(ODU_FUNCTION).leaf("leaf1").build();
        ScopeObject scopeObject2 = ScopeObject.builder("NRCellDU").leaf("leaf1").build();
        ScopeObject scopeObject3 = ScopeObject.builder("EUtranCell").leaf("leaf1").build();
        ScopeObject scopeObject4 = ScopeObject.builder(ODU_FUNCTION).leaf("leaf2").build();
        ScopeObject scopeObject5 = ScopeObject.builder("NRCellDU").leaf("leaf2").build();
        ScopeObject scopeObject6 = ScopeObject.builder("EUtranCell").leaf("leaf2").build();
        OrLogicalBlock or1 = new OrLogicalBlock();
        AndLogicalBlock and1 = new AndLogicalBlock();
        AndLogicalBlock and2 = new AndLogicalBlock();
        OrLogicalBlock or2 = new OrLogicalBlock();
        OrLogicalBlock or3 = new OrLogicalBlock();
        or1.getChildren().add(or2);
        or1.getChildren().add(and1);
        and1.getChildren().add(and2);
        and2.getChildren().add(or3);
        or2.getChildren().add(new ScopeLogicalBlock(scopeObject1));
        or2.getChildren().add(new ScopeLogicalBlock(scopeObject2));
        and1.getChildren().add(new ScopeLogicalBlock(scopeObject3));
        and2.getChildren().add(new ScopeLogicalBlock(scopeObject4));
        or3.getChildren().add(new ScopeLogicalBlock(scopeObject5));
        or3.getChildren().add(new ScopeLogicalBlock(scopeObject6));
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().targets(new ArrayList<>(Arrays.asList(
                targetObject1, targetObject2, targetObject3, targetObject4))).scope(or1).build()));
        basePathRefinement.splitFilterCriteria(filterCriteria);
        OrLogicalBlock fc1or1 = new OrLogicalBlock();
        fc1or1.getChildren().add(new ScopeLogicalBlock(scopeObject1));
        fc1or1.getChildren().add(new ScopeLogicalBlock(scopeObject4));
        OrLogicalBlock fc2or1 = new OrLogicalBlock();
        fc2or1.getChildren().add(new ScopeLogicalBlock(scopeObject2));
        fc2or1.getChildren().add(new ScopeLogicalBlock(scopeObject5));
        AndLogicalBlock fc3and1 = new AndLogicalBlock();
        fc3and1.getChildren().add(new ScopeLogicalBlock(scopeObject6));
        fc3and1.getChildren().add(new ScopeLogicalBlock(scopeObject3));
        InnerFilterCriteria innerFilterCriteria1 = InnerFilterCriteria.builder().targets(List.of(targetObject1)).scope(
                fc1or1).build();
        InnerFilterCriteria innerFilterCriteria2 = InnerFilterCriteria.builder().targets(List.of(targetObject2,
                targetObject3)).scope(fc2or1).build();
        InnerFilterCriteria innerFilterCriteria3 = InnerFilterCriteria.builder().targets(List.of(targetObject4)).scope(
                fc3and1).build();
        Assertions.assertEquals(Set.of(innerFilterCriteria1, innerFilterCriteria2, innerFilterCriteria3), new HashSet<>(
                filterCriteria.getFilterCriteriaList()));
    }

    @Test
    void testValidateQuery() {
        FilterCriteria filterCriteria = FilterCriteria.builder("RAN").build();
        ScopeLogicalBlock scopeLogicalBlock1 = new ScopeLogicalBlock(null);
        ScopeLogicalBlock scopeLogicalBlock2 = new ScopeLogicalBlock(ScopeObject.builder("NRCellDU").build());
        ScopeLogicalBlock scopeLogicalBlock3 = new ScopeLogicalBlock(ScopeObject.builder("NRSectorCarrier").build());
        ScopeLogicalBlock scopeLogicalBlock4 = new ScopeLogicalBlock(null);
        ScopeLogicalBlock scopeLogicalBlock5 = new ScopeLogicalBlock(null);
        ScopeLogicalBlock scopeLogicalBlock6 = new ScopeLogicalBlock(null);
        AndOrLogicalBlock orLogicalBlock1 = new OrLogicalBlock();
        AndOrLogicalBlock orLogicalBlock2 = new OrLogicalBlock();
        AndOrLogicalBlock orLogicalBlock3 = new OrLogicalBlock();
        AndOrLogicalBlock andLogicalBlock1 = new AndLogicalBlock();
        AndOrLogicalBlock andLogicalBlock2 = new AndLogicalBlock();
        orLogicalBlock1.setChildren(new ArrayList<>(Arrays.asList(orLogicalBlock2, andLogicalBlock1)));
        orLogicalBlock2.setChildren(new ArrayList<>(Arrays.asList(scopeLogicalBlock1, andLogicalBlock2)));
        andLogicalBlock2.setChildren(new ArrayList<>(Arrays.asList(scopeLogicalBlock2, scopeLogicalBlock3)));
        andLogicalBlock1.setChildren(new ArrayList<>(Arrays.asList(scopeLogicalBlock4, orLogicalBlock3)));
        orLogicalBlock3.setChildren(new ArrayList<>(Arrays.asList(scopeLogicalBlock5, scopeLogicalBlock6)));
        scopeLogicalBlock1.setValid(false);
        scopeLogicalBlock4.setValid(false);
        scopeLogicalBlock6.setValid(false);
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(orLogicalBlock1).build()));
        basePathRefinement.validateQuery(filterCriteria);
        Assertions.assertEquals(andLogicalBlock2, filterCriteria.getFilterCriteriaList().get(0).getScope());
        ScopeLogicalBlock scopeLogicalBlock2_1 = new ScopeLogicalBlock(null);
        ScopeLogicalBlock scopeLogicalBlock2_2 = new ScopeLogicalBlock(null);
        AndOrLogicalBlock andLogicalBlock2_1 = new AndLogicalBlock();
        scopeLogicalBlock2_1.setValid(false);
        andLogicalBlock2_1.setChildren(new ArrayList<>(Arrays.asList(scopeLogicalBlock2_1, scopeLogicalBlock2_2)));
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(andLogicalBlock2_1).build()));
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.validateQuery(filterCriteria));
        ScopeLogicalBlock scopeLogicalBlock3_1 = new ScopeLogicalBlock(null);
        ScopeLogicalBlock scopeLogicalBlock3_2 = new ScopeLogicalBlock(null);
        AndOrLogicalBlock orLogicalBlock3_1 = new OrLogicalBlock();
        scopeLogicalBlock3_2.setValid(false);
        scopeLogicalBlock3_1.setValid(false);
        orLogicalBlock3_1.setChildren(new ArrayList<>(Arrays.asList(scopeLogicalBlock3_1, scopeLogicalBlock3_2)));
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(orLogicalBlock3_1).build()));
        Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.validateQuery(filterCriteria));
    }

    @Test
    void testRunOnTree() {
        FilterCriteria filterCriteria = FilterCriteria.builder("RAN").build();
        OrLogicalBlock orLogicalBlock = new OrLogicalBlock();
        filterCriteria.setFilterCriteriaList(List.of(InnerFilterCriteria.builder().scope(orLogicalBlock).build()));
        OrLogicalBlock orLogicalBlockChild1 = new OrLogicalBlock();
        OrLogicalBlock orLogicalBlockChild2 = new OrLogicalBlock();
        LogicalBlock scopeLogicalBlock1 = new ScopeLogicalBlock(ScopeObject.builder(ODU_FUNCTION).container(
                ContainerType.ATTRIBUTES).leaf("gNBIdLength").queryFunction(QueryFunction.EQ).parameter("1").dataType(
                        DataType.BIGINT).build());
        LogicalBlock scopeLogicalBlock2 = new ScopeLogicalBlock(ScopeObject.builder(ODU_FUNCTION).container(
                ContainerType.ATTRIBUTES).leaf("gNBIdLength").queryFunction(QueryFunction.EQ).parameter("2").dataType(
                        DataType.BIGINT).build());
        LogicalBlock scopeLogicalBlock3 = new ScopeLogicalBlock(ScopeObject.builder(ODU_FUNCTION).container(
                ContainerType.ATTRIBUTES).leaf("gNBIdLength").queryFunction(QueryFunction.EQ).parameter("3").dataType(
                        DataType.BIGINT).build());
        LogicalBlock scopeLogicalBlock4 = new ScopeLogicalBlock(ScopeObject.builder(ODU_FUNCTION).container(
                ContainerType.ATTRIBUTES).leaf("gNBIdLength").queryFunction(QueryFunction.EQ).parameter("4").dataType(
                        DataType.BIGINT).build());
        orLogicalBlockChild1.setChildren(Arrays.asList(scopeLogicalBlock1, scopeLogicalBlock2));
        orLogicalBlockChild2.setChildren(Arrays.asList(scopeLogicalBlock3, scopeLogicalBlock4));
        orLogicalBlock.setChildren(Arrays.asList(orLogicalBlockChild1, orLogicalBlockChild2));
        basePathRefinement.runOnTree(orLogicalBlock, filterCriteria.getDomain(), (ScopeLogicalBlock lb, String domain) -> lb
                .getScopeObject().setParameter("0"));
        Assertions.assertEquals("0", ((ScopeLogicalBlock) scopeLogicalBlock1).getScopeObject().getParameter());
        Assertions.assertEquals("0", ((ScopeLogicalBlock) scopeLogicalBlock2).getScopeObject().getParameter());
        Assertions.assertEquals("0", ((ScopeLogicalBlock) scopeLogicalBlock3).getScopeObject().getParameter());
        Assertions.assertEquals("0", ((ScopeLogicalBlock) scopeLogicalBlock4).getScopeObject().getParameter());
    }

    @Test
    void testAssociationAttributesOnRelation() {
        LogicalBlock logicalBlock = scopeResolver.resolve(null,
                "/serving-antennaModule/attributes[@antennaModelNumber='5']");
        final FilterCriteria filterCriteria = FilterCriteria.builder("RAN").filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(targetResolver.resolve(null, null)).scope(logicalBlock).build()))
                .resolvingTopologyObjectType(FilterCriteria.ResolvingTopologyObjectType.RELATIONSHIP).build();

        TeivPathException thrown = Assertions.assertThrows(TeivPathException.class, () -> basePathRefinement.refine(
                filterCriteria));
        Assertions.assertEquals("Querying any thing other than id is not supported in case of association of relation",
                thrown.getDetails());

    }

    @Test
    void testAssociationShouldFailDifferentRoot() {
        Assertions.assertThrows(TeivPathException.class, () -> scopeResolver.resolve("NRCellDU",
                "/ManagedElement/serving-antennaModule/attributes[@antennaModelNumber='5']"));
    }

    @Test
    void testAssociationOnAttributes() {

        LogicalBlock logicalBlock = scopeResolver.resolve(null,
                "/NRCellDU/serving-antennaModule/attributes[@antennaModelNumber='5']");
        final FilterCriteria filterCriteria = FilterCriteria.builder("RAN").filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(targetResolver.resolve(null, null)).scope(logicalBlock).build()))
                .resolvingTopologyObjectType(FilterCriteria.ResolvingTopologyObjectType.ENTITY).build();

        LogicalBlock expectedLogicalBlock = scopeResolver.process(null,
                "/NRCellDU/serving-antennaModule/attributes[@antennaModelNumber='5']");

        ScopeObject expectedScopeObject = ((ScopeLogicalBlock) expectedLogicalBlock).getScopeObject();
        expectedScopeObject.setTopologyObject("NRCellDU/serving-antennaModule");
        expectedScopeObject.setTopologyObjectType(TopologyObjectType.ASSOCIATION);
        expectedScopeObject.setContainer(ContainerType.ATTRIBUTES);
        expectedScopeObject.setLeaf("antennaModelNumber");
        expectedScopeObject.setQueryFunction(QueryFunction.EQ);
        expectedScopeObject.setParameter("5");
        expectedScopeObject.setDataType(DataType.PRIMITIVE);

        basePathRefinement.refine(filterCriteria);

        Assertions.assertEquals(expectedScopeObject, ((ScopeLogicalBlock) logicalBlock).getScopeObject());

        LogicalBlock logicalBlock2 = scopeResolver.resolve("NRCellDU",
                "/serving-antennaModule/attributes[coveredBy(@geo-location, 'POLYGON((48 68, 50 68, 50 69, 48 69, 48 68))')]");
        final FilterCriteria filterCriteria2 = FilterCriteria.builder("RAN").filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(targetResolver.resolve(null, null)).scope(logicalBlock2).build()))
                .resolvingTopologyObjectType(FilterCriteria.ResolvingTopologyObjectType.ENTITY).build();

        LogicalBlock expectedLogicalBlock2 = scopeResolver.process("NRCellDU",
                "/serving-antennaModule/attributes[coveredBy(@geo-location, 'POLYGON((48 68, 50 68, 50 69, 48 69, 48 68))')]");

        ScopeObject expectedScopeObject2 = ((ScopeLogicalBlock) expectedLogicalBlock2).getScopeObject();
        expectedScopeObject2.setTopologyObject("NRCellDU/serving-antennaModule");
        expectedScopeObject2.setTopologyObjectType(TopologyObjectType.ASSOCIATION);
        expectedScopeObject2.setContainer(ContainerType.ATTRIBUTES);
        expectedScopeObject2.setLeaf("geo-location");
        expectedScopeObject2.setQueryFunction(QueryFunction.COVERED_BY);
        expectedScopeObject2.setParameter("POLYGON((48 68, 50 68, 50 69, 48 69, 48 68))");
        expectedScopeObject2.setDataType(DataType.GEOGRAPHIC);

        basePathRefinement.refine(filterCriteria2);

        Assertions.assertEquals(expectedScopeObject2, ((ScopeLogicalBlock) logicalBlock2).getScopeObject());

        LogicalBlock logicalBlock3 = scopeResolver.resolve("NRCellDU",
                "/serving-antennaModule/attributes[withinMeters(@geo-location, 'POINT(49.40199 68.94199)', 500)]");
        final FilterCriteria filterCriteria3 = FilterCriteria.builder("RAN").filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(targetResolver.resolve(null, null)).scope(logicalBlock3).build()))
                .resolvingTopologyObjectType(FilterCriteria.ResolvingTopologyObjectType.ENTITY).build();

        LogicalBlock expectedLogicalBlock3 = scopeResolver.process("NRCellDU",
                "/serving-antennaModule/attributes[withinMeters(@geo-location, 'POINT(49.40199 68.94199)', 500)]");

        ScopeObject expectedScopeObject3 = ((ScopeLogicalBlock) expectedLogicalBlock3).getScopeObject();
        expectedScopeObject3.setTopologyObject("NRCellDU/serving-antennaModule");
        expectedScopeObject3.setTopologyObjectType(TopologyObjectType.ASSOCIATION);
        expectedScopeObject3.setContainer(ContainerType.ATTRIBUTES);
        expectedScopeObject3.setLeaf("geo-location");
        expectedScopeObject3.setQueryFunction(QueryFunction.WITHIN_METERS);
        expectedScopeObject3.setParameter("'POINT(49.40199 68.94199)', 500");
        expectedScopeObject3.setDataType(DataType.GEOGRAPHIC);

        basePathRefinement.refine(filterCriteria3);

        Assertions.assertEquals(expectedScopeObject3, ((ScopeLogicalBlock) logicalBlock3).getScopeObject());

        LogicalBlock logicalBlock4 = scopeResolver.resolve("NRCellDU",
                "/serving-antennaModule/attributes[@antennaModelNumber='5']");
        final FilterCriteria filterCriteria4 = FilterCriteria.builder("RAN").filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(targetResolver.resolve(null, null)).scope(logicalBlock4).build()))
                .resolvingTopologyObjectType(FilterCriteria.ResolvingTopologyObjectType.ENTITY).build();

        LogicalBlock expectedLogicalBlock4 = scopeResolver.process("NRCellDU",
                "/serving-antennaModule/attributes[@antennaModelNumber='5']");

        ScopeObject expectedScopeObject4 = ((ScopeLogicalBlock) expectedLogicalBlock4).getScopeObject();
        expectedScopeObject4.setTopologyObject("NRCellDU/serving-antennaModule");
        expectedScopeObject4.setTopologyObjectType(TopologyObjectType.ASSOCIATION);
        expectedScopeObject4.setContainer(ContainerType.ATTRIBUTES);
        expectedScopeObject4.setLeaf("antennaModelNumber");
        expectedScopeObject4.setQueryFunction(QueryFunction.EQ);
        expectedScopeObject4.setParameter("5");
        expectedScopeObject4.setDataType(DataType.PRIMITIVE);

        basePathRefinement.refine(filterCriteria4);

        Assertions.assertEquals(expectedScopeObject4, ((ScopeLogicalBlock) logicalBlock4).getScopeObject());

        LogicalBlock logicalBlock5 = scopeResolver.resolve(null,
                "/serving-antennaModule/attributes[@antennaModelNumber='5']");
        final FilterCriteria filterCriteria5 = FilterCriteria.builder("RAN").filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(targetResolver.resolve(null, null)).scope(logicalBlock5).build()))
                .resolvingTopologyObjectType(FilterCriteria.ResolvingTopologyObjectType.ENTITY).build();

        LogicalBlock expectedLogicalBlock5 = scopeResolver.process(null,
                "/serving-antennaModule/attributes[@antennaModelNumber='5']");

        ScopeObject expectedScopeObject5 = ((ScopeLogicalBlock) expectedLogicalBlock5).getScopeObject();
        expectedScopeObject5.setTopologyObject("NRCellDU/serving-antennaModule");
        expectedScopeObject5.setTopologyObjectType(TopologyObjectType.ASSOCIATION);
        expectedScopeObject5.setContainer(ContainerType.ATTRIBUTES);
        expectedScopeObject5.setLeaf("antennaModelNumber");
        expectedScopeObject5.setQueryFunction(QueryFunction.EQ);
        expectedScopeObject5.setParameter("5");
        expectedScopeObject5.setDataType(DataType.PRIMITIVE);

        basePathRefinement.refine(filterCriteria5);
        Assertions.assertEquals(new ScopeLogicalBlock(expectedScopeObject5), filterCriteria5.getFilterCriteriaList().get(0)
                .getScope());

    }

    @Test
    void testAssociationOnSourceIds() {
        LogicalBlock logicalBlock = scopeResolver.resolve("NRCellDU",
                "/serving-antennaModule/sourceIds[@item = 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1']");
        final FilterCriteria filterCriteria = FilterCriteria.builder("RAN").filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(targetResolver.resolve(null, null)).scope(logicalBlock).build()))
                .resolvingTopologyObjectType(FilterCriteria.ResolvingTopologyObjectType.ENTITY).build();

        LogicalBlock expectedLogicalBlock = scopeResolver.process("NRCellDU",
                "/serving-antennaModule/sourceIds[@item = 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1']");

        ScopeObject expectedScopeObject = ((ScopeLogicalBlock) expectedLogicalBlock).getScopeObject();
        expectedScopeObject.setTopologyObject("NRCellDU/serving-antennaModule");
        expectedScopeObject.setTopologyObjectType(TopologyObjectType.ASSOCIATION);
        expectedScopeObject.setContainer(ContainerType.SOURCE_IDS);
        expectedScopeObject.setLeaf("item");
        expectedScopeObject.setQueryFunction(QueryFunction.EQ);
        expectedScopeObject.setParameter(
                "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1");
        expectedScopeObject.setDataType(DataType.PRIMITIVE);

        basePathRefinement.refine(filterCriteria);

        Assertions.assertEquals(expectedScopeObject, ((ScopeLogicalBlock) logicalBlock).getScopeObject());

        LogicalBlock logicalBlock2 = scopeResolver.resolve("NRCellDU",
                "/serving-antennaModule/sourceIds[contains(@item,'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1')]");
        final FilterCriteria filterCriteria2 = FilterCriteria.builder("RAN").filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(targetResolver.resolve(null, null)).scope(logicalBlock2).build()))
                .resolvingTopologyObjectType(FilterCriteria.ResolvingTopologyObjectType.ENTITY).build();

        LogicalBlock expectedLogicalBlock2 = scopeResolver.process("NRCellDU",
                "/serving-antennaModule/sourceIds[contains(@item,'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1')]");

        ScopeObject expectedScopeObject2 = ((ScopeLogicalBlock) expectedLogicalBlock2).getScopeObject();
        expectedScopeObject2.setTopologyObject("NRCellDU/serving-antennaModule");
        expectedScopeObject2.setTopologyObjectType(TopologyObjectType.ASSOCIATION);
        expectedScopeObject2.setContainer(ContainerType.SOURCE_IDS);
        expectedScopeObject2.setLeaf("item");
        expectedScopeObject2.setQueryFunction(QueryFunction.CONTAINS);
        expectedScopeObject2.setParameter(
                "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1");
        expectedScopeObject2.setDataType(DataType.PRIMITIVE);

        basePathRefinement.refine(filterCriteria2);

        Assertions.assertEquals(expectedScopeObject2, ((ScopeLogicalBlock) logicalBlock2).getScopeObject());

    }

    @Test
    void testAssociationWrongSyntax() {
        Assertions.assertThrows(TeivPathException.class, () -> scopeResolver.resolve("NRCellDU",
                "/serving-antennaModule/attributes/sourceIds[@item = 'sourceIds']"));
        Assertions.assertThrows(TeivPathException.class, () -> scopeResolver.resolve("NRCellDU",
                "/serving-antennaModule/sourceIds/attributes[@item = 'sourceIds']"));
    }

    @Test
    void testAssociationOnClassifiers() {
        LogicalBlock logicalBlock = scopeResolver.resolve("NRCellDU",
                "/serving-antennaModule/classifiers[@item='app-module:Rural']");
        final FilterCriteria filterCriteria = FilterCriteria.builder("RAN").filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(targetResolver.resolve(null, null)).scope(logicalBlock).build()))
                .resolvingTopologyObjectType(FilterCriteria.ResolvingTopologyObjectType.ENTITY).build();

        LogicalBlock expectedLogicalBlock = scopeResolver.process("NRCellDU",
                "/serving-antennaModule/classifiers[@item='app-module:Rural']");

        ScopeObject expectedScopeObject = ((ScopeLogicalBlock) expectedLogicalBlock).getScopeObject();
        expectedScopeObject.setTopologyObject("NRCellDU/serving-antennaModule");
        expectedScopeObject.setTopologyObjectType(TopologyObjectType.ASSOCIATION);
        expectedScopeObject.setContainer(ContainerType.CLASSIFIERS);
        expectedScopeObject.setLeaf("item");
        expectedScopeObject.setQueryFunction(QueryFunction.EQ);
        expectedScopeObject.setParameter("app-module:Rural");
        expectedScopeObject.setDataType(DataType.PRIMITIVE);

        basePathRefinement.refine(filterCriteria);

        Assertions.assertEquals(expectedScopeObject, ((ScopeLogicalBlock) logicalBlock).getScopeObject());

        LogicalBlock logicalBlock2 = scopeResolver.resolve("NRCellDU",
                "/serving-antennaModule/classifiers[contains(@item,'app-module:Rural')]");
        final FilterCriteria filterCriteria2 = FilterCriteria.builder("RAN").filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(targetResolver.resolve(null, null)).scope(logicalBlock2).build()))
                .resolvingTopologyObjectType(FilterCriteria.ResolvingTopologyObjectType.ENTITY).build();

        LogicalBlock expectedLogicalBlock2 = scopeResolver.process("NRCellDU",
                "/serving-antennaModule/classifiers[contains(@item,'app-module:Rural')]");

        ScopeObject expectedScopeObject2 = ((ScopeLogicalBlock) expectedLogicalBlock2).getScopeObject();
        expectedScopeObject2.setTopologyObject("NRCellDU/serving-antennaModule");
        expectedScopeObject2.setTopologyObjectType(TopologyObjectType.ASSOCIATION);
        expectedScopeObject2.setContainer(ContainerType.CLASSIFIERS);
        expectedScopeObject2.setLeaf("item");
        expectedScopeObject2.setQueryFunction(QueryFunction.CONTAINS);
        expectedScopeObject2.setParameter("app-module:Rural");
        expectedScopeObject2.setDataType(DataType.PRIMITIVE);

        basePathRefinement.refine(filterCriteria2);

        Assertions.assertEquals(expectedScopeObject2, ((ScopeLogicalBlock) logicalBlock2).getScopeObject());
    }

    @Test
    void testAssociationOnDecorators() {
        LogicalBlock logicalBlock = scopeResolver.resolve("NRCellDU",
                "/serving-antennaModule/decorators[@test-app-module:textdata='Ericsson']");
        final FilterCriteria filterCriteria = FilterCriteria.builder("RAN").filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(targetResolver.resolve(null, null)).scope(logicalBlock).build()))
                .resolvingTopologyObjectType(FilterCriteria.ResolvingTopologyObjectType.ENTITY).build();

        LogicalBlock expectedLogicalBlock = scopeResolver.process("NRCellDU",
                "/serving-antennaModule/decorators[@test-app-module:textdata='Ericsson']");

        ScopeObject expectedScopeObject = ((ScopeLogicalBlock) expectedLogicalBlock).getScopeObject();
        expectedScopeObject.setTopologyObject("NRCellDU/serving-antennaModule");
        expectedScopeObject.setTopologyObjectType(TopologyObjectType.ASSOCIATION);
        expectedScopeObject.setContainer(ContainerType.DECORATORS);
        expectedScopeObject.setLeaf("test-app-module:textdata");
        expectedScopeObject.setQueryFunction(QueryFunction.EQ);
        expectedScopeObject.setParameter("Ericsson");
        expectedScopeObject.setDataType(DataType.PRIMITIVE);

        basePathRefinement.refine(filterCriteria);

        Assertions.assertEquals(expectedScopeObject, ((ScopeLogicalBlock) logicalBlock).getScopeObject());

    }

    @Test
    void testAssociationOnMetadata() {
        LogicalBlock logicalBlock = scopeResolver.resolve("NRCellDU",
                "/serving-antennaModule/metadata[@reliabilityIndicator='OK']");
        final FilterCriteria filterCriteria = FilterCriteria.builder("RAN").filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(targetResolver.resolve(null, null)).scope(logicalBlock).build()))
                .resolvingTopologyObjectType(FilterCriteria.ResolvingTopologyObjectType.ENTITY).build();

        LogicalBlock expectedLogicalBlock = scopeResolver.process("NRCellDU",
                "/serving-antennaModule/metadata[@reliabilityIndicator='OK']");

        ScopeObject expectedScopeObject = ((ScopeLogicalBlock) expectedLogicalBlock).getScopeObject();
        expectedScopeObject.setTopologyObject("NRCellDU/serving-antennaModule");
        expectedScopeObject.setTopologyObjectType(TopologyObjectType.ASSOCIATION);
        expectedScopeObject.setContainer(ContainerType.METADATA);
        expectedScopeObject.setLeaf("reliabilityIndicator");
        expectedScopeObject.setQueryFunction(QueryFunction.EQ);
        expectedScopeObject.setParameter("OK");
        expectedScopeObject.setDataType(DataType.PRIMITIVE);

        basePathRefinement.refine(filterCriteria);

        Assertions.assertEquals(expectedScopeObject, ((ScopeLogicalBlock) logicalBlock).getScopeObject());
    }
}
