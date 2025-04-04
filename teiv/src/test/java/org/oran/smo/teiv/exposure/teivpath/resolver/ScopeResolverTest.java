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
package org.oran.smo.teiv.exposure.teivpath.resolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.oran.smo.teiv.exposure.teivpath.innerlanguage.AndLogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.AndOrLogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.ContainerType;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.EmptyLogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.LogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.OrLogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.QueryFunction;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.ScopeLogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.ScopeObject;
import org.oran.smo.teiv.utils.query.exception.TeivPathException;

class ScopeResolverTest {

    private final ScopeResolver scopeResolver = new ScopeResolver();

    @Test
    void testEmptyScope() {
        LogicalBlock expected = EmptyLogicalBlock.getInstance();
        final LogicalBlock resolvedScope = scopeResolver.resolve(null, null);
        Assertions.assertEquals(expected, resolvedScope);
    }

    @Test
    void resolveScopeWithMultipleTokensWithSameContainer() {
        ScopeLogicalBlock scopeLogicalBlock1 = new ScopeLogicalBlock(ScopeObject.builder("*/GNBDUFunction").container(
                ContainerType.ATTRIBUTES).leaf("gnBId").queryFunction(QueryFunction.EQ).parameter("1").resolverDataType(
                        ResolverDataType.INTEGER).build());
        ScopeLogicalBlock scopeLogicalBlock2 = new ScopeLogicalBlock(ScopeObject.builder("*/GNBDUFunction").container(
                ContainerType.ATTRIBUTES).leaf("date").queryFunction(QueryFunction.EQ).parameter("2").resolverDataType(
                        ResolverDataType.STRING).build());
        ScopeLogicalBlock scopeLogicalBlock3 = new ScopeLogicalBlock(ScopeObject.builder("*/GNBDUFunction").container(
                ContainerType.ATTRIBUTES).leaf("refs").queryFunction(QueryFunction.EQ).parameter("12341").resolverDataType(
                        ResolverDataType.INTEGER).build());
        ScopeLogicalBlock scopeLogicalBlock4 = new ScopeLogicalBlock(ScopeObject.builder("*").container(
                ContainerType.ATTRIBUTES).leaf("fdn").queryFunction(QueryFunction.CONTAINS).parameter("GNBDUFunction=10")
                .resolverDataType(ResolverDataType.STRING).build());
        AndOrLogicalBlock logicalBlock1 = new AndLogicalBlock();
        AndOrLogicalBlock logicalBlock2 = new OrLogicalBlock();
        AndOrLogicalBlock logicalBlock3 = new AndLogicalBlock();
        logicalBlock1.addChild(scopeLogicalBlock1);
        logicalBlock1.addChild(scopeLogicalBlock2);
        logicalBlock2.addChild(logicalBlock1);
        logicalBlock2.addChild(scopeLogicalBlock3);
        logicalBlock3.addChild(scopeLogicalBlock4);
        logicalBlock3.addChild(logicalBlock2);
        final LogicalBlock resolverLB = scopeResolver.resolve(null,
                "/GNBDUFunction/attributes[@gnBId = 1 and @date = \"2\" or @refs = 12341];/attributes[contains(@fdn,\"'GNBDUFunction=10'\")]");
        Assertions.assertEquals(logicalBlock3, resolverLB);
    }

    @Test
    void resolveScopeWithCoveredByGeoLocationCondition() {
        ScopeLogicalBlock expected = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").parameter(
                        "POLYGON (12.3426 45.24568, 13.3426 45.24568, 12.3426 44.24568, 13.3426 44.24568)")
                .resolverDataType(ResolverDataType.STRING).queryFunction(QueryFunction.COVERED_BY).build());

        final LogicalBlock resolverLB = scopeResolver.resolve(null,
                "/attributes[coveredBy(@geo-location, 'POLYGON (12.3426 45.24568, 13.3426 45.24568, 12.3426 44.24568, 13.3426 44.24568)')]");
        Assertions.assertEquals(expected, resolverLB);
    }

    @Test
    void resolveScopeWithWithinMetersGeoLocationConditionWithInteger() {
        ScopeLogicalBlock expected = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").parameter("'POINT (12.3426 45.24568)', 500").resolverDataType(ResolverDataType.STRING)
                .queryFunction(QueryFunction.WITHIN_METERS).build());

        final LogicalBlock resolverLB = scopeResolver.resolve(null,
                "/attributes[withinMeters(@geo-location, 'POINT (12.3426 45.24568)', 500)]");
        Assertions.assertEquals(expected, resolverLB);
    }

    @Test
    void resolveScopeWithWithinMetersGeoLocationConditionWithDouble() {
        ScopeLogicalBlock expected = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").parameter("'POINT (12.3426 45.24568)', 500.34e1").resolverDataType(
                        ResolverDataType.STRING).queryFunction(QueryFunction.WITHIN_METERS).build());

        final LogicalBlock resolverLB = scopeResolver.resolve(null,
                "/attributes[withinMeters(@geo-location, 'POINT (12.3426 45.24568)', 500.34e1)]");
        Assertions.assertEquals(expected, resolverLB);
    }

    @Test
    void resolveScopeWithWithinMetersGeoLocationConditionWithDecimal() {
        ScopeLogicalBlock expected = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ATTRIBUTES)
                .leaf("geo-location").parameter("'POINT (12.3426 45.24568)', 500.34").resolverDataType(
                        ResolverDataType.STRING).queryFunction(QueryFunction.WITHIN_METERS).build());

        final LogicalBlock resolverLB = scopeResolver.resolve(null,
                "/attributes[withinMeters(@geo-location, 'POINT (12.3426 45.24568)', 500.34)]");
        Assertions.assertEquals(expected, resolverLB);
    }

    @Test
    void resolveScopeWithWithinMetersGeoLocationConditionWithInvalidNumber() {
        assertThrows(TeivPathException.class, () -> scopeResolver.resolve(null,
                "/attributes[withinMeters(@geo-location, 'POINT (12.3426 45.24568)', 50asd0)]"));
    }

    @Test
    void resolveScopeWithWithinMetersGeoLocationConditionWithInvalidGrammar1() {
        assertThrows(TeivPathException.class, () -> scopeResolver.resolve(null,
                "/attributes[withinMeters(@geo-location, 'POINT (12.3426 45.24568)' 500)]"));
    }

    @Test
    void resolveScopeWithWithinMetersGeoLocationConditionWithInvalidGrammar2() {
        assertThrows(TeivPathException.class, () -> scopeResolver.resolve(null,
                "/attributes[withinMeters(@geo-location, ,'POINT (12.3426 45.24568)', 500)]"));
    }

    @Test
    void resolveScopeWithWithinMetersGeoLocationConditionWithInvalidGrammar3() {
        assertThrows(TeivPathException.class, () -> scopeResolver.resolve(null,
                "/attributes[withinMeters('POINT (12.3426 45.24568)', 500)]"));
    }

    @Test
    void resolveScopeWithWithinMetersGeoLocationConditionWithInvalidGrammar4() {
        assertThrows(TeivPathException.class, () -> scopeResolver.resolve(null,
                "/attributes[withinMeters('POINT (12.3426 45.24568)', )]"));
    }

    @Test
    void resolveScopeWithMultipleTokensWithDifferentContainers() {
        ScopeLogicalBlock scopeLogicalBlock1 = new ScopeLogicalBlock(ScopeObject.builder("*/GNBDUFunction").container(
                ContainerType.ATTRIBUTES).leaf("gnBId").queryFunction(QueryFunction.EQ).parameter("1").resolverDataType(
                        ResolverDataType.INTEGER).build());
        ScopeLogicalBlock scopeLogicalBlock2 = new ScopeLogicalBlock(ScopeObject.builder("*/GNBDUFunction").container(
                ContainerType.ATTRIBUTES).leaf("date").queryFunction(QueryFunction.EQ).parameter("2").resolverDataType(
                        ResolverDataType.STRING).build());
        ScopeLogicalBlock scopeLogicalBlock3 = new ScopeLogicalBlock(ScopeObject.builder("*/GNBDUFunction").container(
                ContainerType.ATTRIBUTES).leaf("refs").queryFunction(QueryFunction.EQ).parameter("12341").resolverDataType(
                        ResolverDataType.INTEGER).build());
        ScopeLogicalBlock scopeLogicalBlock4 = new ScopeLogicalBlock(ScopeObject.builder("*").container(
                ContainerType.DECORATORS).leaf("vendor").queryFunction(QueryFunction.CONTAINS).parameter("ORAN")
                .resolverDataType(ResolverDataType.STRING).build());
        ScopeLogicalBlock scopeLogicalBlock5 = new ScopeLogicalBlock(ScopeObject.builder("*").container(
                ContainerType.CLASSIFIERS).leaf("item").queryFunction(QueryFunction.EQ).parameter("cmHandle")
                .resolverDataType(ResolverDataType.STRING).build());
        ScopeLogicalBlock scopeLogicalBlock6 = new ScopeLogicalBlock(ScopeObject.builder("*").container(
                ContainerType.SOURCE_IDS).leaf("item").queryFunction(QueryFunction.EQ).parameter("fdn1").resolverDataType(
                        ResolverDataType.STRING).build());
        AndOrLogicalBlock logicalBlock1 = new AndLogicalBlock();
        AndOrLogicalBlock logicalBlock2 = new OrLogicalBlock();
        AndOrLogicalBlock logicalBlock3 = new OrLogicalBlock();
        AndOrLogicalBlock logicalBlock4 = new AndLogicalBlock();
        AndOrLogicalBlock logicalBlock5 = new OrLogicalBlock();
        logicalBlock1.addChild(scopeLogicalBlock1);
        logicalBlock1.addChild(scopeLogicalBlock2);
        logicalBlock2.addChild(logicalBlock1);
        logicalBlock2.addChild(scopeLogicalBlock3);
        logicalBlock3.addChild(logicalBlock2);
        logicalBlock3.addChild(scopeLogicalBlock4);
        logicalBlock4.addChild(scopeLogicalBlock5);
        logicalBlock4.addChild(logicalBlock3);
        logicalBlock5.addChild(scopeLogicalBlock6);
        logicalBlock5.addChild(logicalBlock4);

        final LogicalBlock resolverLB = scopeResolver.resolve(null,
                "/decorators[contains(@vendor, 'ORAN')]|/GNBDUFunction/attributes[@gnBId = 1 and @date = '2' or @refs = 12341] ; " + "/classifiers[@item = 'cmHandle']| /sourceIds[@item = 'fdn1']");
        Assertions.assertEquals(logicalBlock5, resolverLB,
                "If in test failure contents are identical  then check if AndLB/OrLB are used appropriately");
    }

    @Test
    void testRelationshipInScope() {
        ScopeLogicalBlock expected = new ScopeLogicalBlock(ScopeObject.builder(
                "ManagedElement/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION").container(null).leaf(null).queryFunction(
                        QueryFunction.NOT_NULL).parameter(null).resolverDataType(ResolverDataType.NOT_NULL).build());
        final LogicalBlock logicalBlock = scopeResolver.resolve("ManagedElement", "/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION");
        Assertions.assertEquals(expected, logicalBlock);
    }

    @Test
    void testNoLeavesInScope() {
        ScopeLogicalBlock scopeLogicalBlock1 = new ScopeLogicalBlock(ScopeObject.builder(
                "ENodeBFunction/managed-by-managedObject").container(null).leaf(null).queryFunction(QueryFunction.NOT_NULL)
                .parameter(null).resolverDataType(ResolverDataType.NOT_NULL).build());
        ScopeLogicalBlock scopeLogicalBlock2 = new ScopeLogicalBlock(ScopeObject.builder("*/managed-by-managedObject")
                .container(null).innerContainer(List.of()).leaf("id").queryFunction(QueryFunction.EQ).parameter("me1")
                .resolverDataType(ResolverDataType.STRING).build());
        AndOrLogicalBlock logicalBlock = new AndLogicalBlock();
        logicalBlock.addChild(scopeLogicalBlock1);
        logicalBlock.addChild(scopeLogicalBlock2);
        final LogicalBlock actualLogicalBlock = scopeResolver.resolve(null,
                "/managed-by-managedObject[@id='me1']; /ENodeBFunction/managed-by-managedObject");
        Assertions.assertEquals(logicalBlock, actualLogicalBlock);
    }

    @Test
    void testResolveAssociationWithoutRootObjectInScopeFilter() {
        ScopeLogicalBlock expected = new ScopeLogicalBlock(ScopeObject.builder("*/managed-by-managedObject").container(null)
                .innerContainer(List.of()).leaf("id").queryFunction(QueryFunction.EQ).parameter("me1").resolverDataType(
                        ResolverDataType.STRING).build());
        final LogicalBlock resolvedScope = scopeResolver.resolve(null, "/managed-by-managedObject[@id='me1']");
        Assertions.assertEquals(expected, resolvedScope);
    }

    @Test
    void testEqualTextFunction() {
        ScopeLogicalBlock expected = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ID).leaf(null)
                .queryFunction(QueryFunction.EQ).parameter("me1").resolverDataType(ResolverDataType.STRING).build());
        final LogicalBlock resolvedScope = scopeResolver.resolve(null, "/id[text()='me1']");
        Assertions.assertEquals(expected, resolvedScope);
    }

    @Test
    void testContainsTxtFunctionCondition() {
        ScopeLogicalBlock expected = new ScopeLogicalBlock(ScopeObject.builder("*").container(ContainerType.ID).leaf(null)
                .queryFunction(QueryFunction.CONTAINS).parameter("me1").resolverDataType(ResolverDataType.STRING).build());
        final LogicalBlock resolvedScope = scopeResolver.resolve(null, "/id[contains(text(),'me1')]");
        Assertions.assertEquals(expected, resolvedScope);
    }

    @Test
    void testExceptionWhenTextConditionUsedOnOtherThanID() {
        TeivPathException thrown = assertThrows(TeivPathException.class, () -> scopeResolver.resolve(null,
                "/ManagedElement[text()='me1']"));
        assertEquals("text() is supported for ID only", thrown.getDetails());
    }

    @Test
    void testResolveWithRootObjectAndAssociationInScopeFilter() {
        ScopeLogicalBlock expected = new ScopeLogicalBlock(ScopeObject.builder("ENodeBFunction/managed-by-managedObject")
                .container(null).innerContainer(List.of()).leaf("id").queryFunction(QueryFunction.EQ).parameter("me1")
                .resolverDataType(ResolverDataType.STRING).build());
        final LogicalBlock resolvedScope = scopeResolver.resolve("ENodeBFunction", "/managed-by-managedObject[@id='me1']");
        Assertions.assertEquals(expected, resolvedScope);
    }

    @Test
    void testResolveWithRootObjectAndEntityInScopeFilter() {
        ScopeLogicalBlock expected = new ScopeLogicalBlock(ScopeObject.builder("ManagedElement/GNBDUFunction").container(
                null).innerContainer(List.of()).leaf("id").queryFunction(QueryFunction.CONTAINS).parameter("me1")
                .resolverDataType(ResolverDataType.STRING).build());
        final LogicalBlock resolvedScope = scopeResolver.resolve("ManagedElement", "/GNBDUFunction[contains(@id,'me1')]");
        Assertions.assertEquals(expected, resolvedScope);
    }

    @Test
    void testResolveObjectWithTopologyObjectInScopeFilter() {
        ScopeLogicalBlock expected = new ScopeLogicalBlock(ScopeObject.builder("ManagedElement").container(ContainerType.ID)
                .queryFunction(QueryFunction.EQ).parameter("me1").resolverDataType(ResolverDataType.STRING).build());
        final LogicalBlock resolvedScope = scopeResolver.resolve("ManagedElement", "/ManagedElement[@id='me1']");
        Assertions.assertEquals(expected, resolvedScope);
    }

    @Test
    void testResolveComplexAttributeInScopeFilter() {
        //Without root object & Entity type provided in the container
        ScopeLogicalBlock expected = new ScopeLogicalBlock(ScopeObject.builder("*/GNBCUCPFunction").container(
                ContainerType.ATTRIBUTES).innerContainer(List.of("pLMNId")).leaf("mcc").queryFunction(QueryFunction.EQ)
                .parameter("01").resolverDataType(ResolverDataType.STRING).build());
        final LogicalBlock resolvedScope = scopeResolver.resolve(null, "/GNBCUCPFunction/attributes/pLMNId[@mcc='01']");
        Assertions.assertEquals(expected, resolvedScope);

        //Without root object & Entity type not provided
        ScopeLogicalBlock expected1 = new ScopeLogicalBlock(ScopeObject.builder(null).container(ContainerType.ATTRIBUTES)
                .innerContainer(List.of("pLMNId")).leaf("mcc").queryFunction(QueryFunction.EQ).parameter("01")
                .resolverDataType(ResolverDataType.STRING).build());
        final LogicalBlock resolvedScope1 = scopeResolver.resolve(null, "/attributes/pLMNId[@mcc='01']");
        Assertions.assertEquals(expected1, resolvedScope1);

        //With root object & Entity type provided in the container
        ScopeLogicalBlock expected2 = new ScopeLogicalBlock(ScopeObject.builder("GNBCUCPFunction").container(
                ContainerType.ATTRIBUTES).innerContainer(List.of("pLMNId")).leaf("mcc").queryFunction(QueryFunction.EQ)
                .parameter("01").resolverDataType(ResolverDataType.STRING).build());
        final LogicalBlock resolvedScope2 = scopeResolver.resolve("GNBCUCPFunction",
                "/GNBCUCPFunction/attributes/pLMNId[@mcc='01']");
        Assertions.assertEquals(expected2, resolvedScope2);

        //With root object & Entity type not provided
        ScopeLogicalBlock expected3 = new ScopeLogicalBlock(ScopeObject.builder("GNBCUCPFunction").container(
                ContainerType.ATTRIBUTES).innerContainer(List.of("pLMNId")).leaf("mcc").queryFunction(QueryFunction.EQ)
                .parameter("01").resolverDataType(ResolverDataType.STRING).build());
        final LogicalBlock resolvedScope3 = scopeResolver.resolve("GNBCUCPFunction", "/attributes/pLMNId[@mcc='01']");
        Assertions.assertEquals(expected3, resolvedScope3);
    }

    @Test
    void testExceptionWithTopologyObjectInScopeFilterWhichDoesNotHaveValidContainerTypeInLeaf() {
        TeivPathException thrown = assertThrows(TeivPathException.class, () -> scopeResolver.resolve("ManagedElement",
                "/ManagedElement[@id1='me1']"));
        assertEquals("id1 is not a valid leaf for topology object: ManagedElement", thrown.getDetails());
    }

    @Test
    void testExceptionWithScopeFilterLeafButNoCondition() {
        TeivPathException thrown = assertThrows(TeivPathException.class, () -> scopeResolver.resolve("ManagedElement",
                "/GNBDUFunction(fdn))"));
        assertEquals("Parameter without any condition is not supported in scope filter", thrown.getDetails());
    }

    @Test
    void testSameRootObjectAndContainerNameButNonMatchingContainerTypeInScopeFilter() {
        ScopeLogicalBlock expected = new ScopeLogicalBlock(ScopeObject.builder("ManagedElement").container(ContainerType.ID)
                .queryFunction(QueryFunction.CONTAINS).parameter("me1").resolverDataType(ResolverDataType.STRING).build());
        final LogicalBlock resolvedScope = scopeResolver.resolve("ManagedElement", "/ManagedElement[contains(@id,'me1')]");
        Assertions.assertEquals(expected, resolvedScope);
    }

    @Test
    void testWithNoRootObjectAndNonMatchingContainerTypeInScopeFilter() {
        ScopeLogicalBlock expected = new ScopeLogicalBlock(ScopeObject.builder("ManagedElement/attr").container(null)
                .queryFunction(QueryFunction.CONTAINS).parameter("me1").resolverDataType(ResolverDataType.STRING)
                .innerContainer(List.of()).leaf("id").build());
        final LogicalBlock resolvedScope = scopeResolver.resolve(null, "/ManagedElement/attr[contains(@id,'me1')]");
        Assertions.assertEquals(expected, resolvedScope);
    }

    @Test
    void testWithNonMatchingRootObjectInScopeFilter() {
        TeivPathException thrown = assertThrows(TeivPathException.class, () -> scopeResolver.resolve("GNBDUFunction",
                "/ManagedElement/attr[contains(@id,'me1')]"));
        assertEquals("Target/Scope filter can only contain Root Object types mentioned in the path parameter", thrown
                .getDetails());
    }
}
