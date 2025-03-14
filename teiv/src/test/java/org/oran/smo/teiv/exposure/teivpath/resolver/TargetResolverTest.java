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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.oran.smo.teiv.exposure.teivpath.innerlanguage.ContainerType;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.TargetObject;
import org.oran.smo.teiv.utils.query.exception.TeivPathException;

class TargetResolverTest {

    private final TargetResolver targetResolver = new TargetResolver();

    @Test
    void testIdOnlyWhenTopologyObjectInRootObjectType() {
        List<TargetObject> expectedObject = List.of(TargetObject.builder("GNBDUFunction").build());

        Assertions.assertEquals(expectedObject, targetResolver.resolve("GNBDUFunction", null));
    }

    @Test
    void testIdOnlyWhenTopologyObjectAndContainerInTargetOnlyTest() {
        List<TargetObject> expectedObject = List.of(TargetObject.builder("ENodeBFunction").build());
        Assertions.assertEquals(expectedObject, targetResolver.resolve(null, "/ENodeBFunction/id"));
    }

    @Test
    void testIdOnlyWhenSameTopologyObjectInTargetAndRootObjectType() {
        List<TargetObject> expectedObject = List.of(TargetObject.builder("GNBDUFunction").build());
        Assertions.assertEquals(expectedObject, targetResolver.resolve("GNBDUFunction", "/GNBDUFunction"));
    }

    @Test
    void testExceptionNonMatchingContainerType() {
        TeivPathException thrown = assertThrows(TeivPathException.class, () -> targetResolver.resolve("GNBDUFunction",
                "/bla"));
        assertEquals("Invalid Container name or Root Object name does not match to the path parameter", thrown
                .getDetails());
    }

    @Test
    void testExceptionWhenTopologyObjectInTargetWithParamButNoMatchingContainer() {
        TeivPathException thrown = assertThrows(TeivPathException.class, () -> targetResolver.resolve(null,
                "/GNBDUFunction(fdn, enbId)"));
        assertEquals("Attributes cannot be associated at this level", thrown.getDetails());
    }

    @Test
    void testEmptyTargetAndRootObjectType() {
        List<TargetObject> expectedObject = List.of(TargetObject.builder("*").build());
        Assertions.assertEquals(expectedObject, targetResolver.resolve(null, null));
    }

    @Test
    void testAllAttributes() {
        List<TargetObject> expectedObject = List.of(TargetObject.builder("GNBDUFunction").container(
                ContainerType.ATTRIBUTES).build());
        Assertions.assertEquals(expectedObject, targetResolver.resolve("GNBDUFunction", "/attributes"));
    }

    @Test
    void testAllAttributesWithEmptyRootObject() {
        List<TargetObject> expectedObject = List.of(TargetObject.builder("*").container(ContainerType.ATTRIBUTES).build());
        Assertions.assertEquals(expectedObject, targetResolver.resolve(null, "/attributes"));
    }

    @Test
    void testSelectedAttributes() {
        List<TargetObject> expectedObject = List.of(TargetObject.builder("GNBDUFunction").container(
                ContainerType.ATTRIBUTES).params(List.of("fdn", "enbId")).build());
        Assertions.assertEquals(expectedObject, targetResolver.resolve("GNBDUFunction",
                "/GNBDUFunction/attributes(fdn, enbId)"));

    }

    @Test
    void testAttributesOnSourceIds() {
        TeivPathException thrown = assertThrows(TeivPathException.class, () -> targetResolver.resolve(null,
                "/GNBDUFunction/sourceIds(fdn, enbId)"));
        assertEquals("Parameters are not supported for sourceIds in target filter", thrown.getDetails());
    }

    @Test
    void testLogicalANDWithTwoDifferentContainersTypes() {
        List<TargetObject> expectedObject = List.of(TargetObject.builder("GNBDUFunction").container(
                ContainerType.ATTRIBUTES).params(List.of("fdn", "enbId")).build(), TargetObject.builder("GNBDUFunction")
                        .container(ContainerType.SOURCE_IDS).build());
        Assertions.assertEquals(expectedObject, targetResolver.resolve("GNBDUFunction",
                "/GNBDUFunction/attributes(fdn, enbId);/sourceIds"));
    }

    @Test
    void testLogicalANDWithTwoDifferentContainersTypesOneWithParams() {
        List<TargetObject> expectedObject = List.of(TargetObject.builder("GNBDUFunction").container(
                ContainerType.ATTRIBUTES).params(List.of("fdn", "enbId")).build(), TargetObject.builder("GNBDUFunction")
                        .container(ContainerType.DECORATORS).build());
        Assertions.assertEquals(expectedObject, targetResolver.resolve(null,
                "/GNBDUFunction/attributes(fdn, enbId);" + "/GNBDUFunction/decorators"));
    }

    @Test
    void testExceptionWithParamOnDecorators() {
        TeivPathException thrown = assertThrows(TeivPathException.class, () -> targetResolver.resolve(null,
                "/GNBDUFunction/attributes(fdn, enbId); /GNBDUFunction/decorators(module-x:location,module-y:vendor)"));
        assertEquals("Parameters are not supported for decorators in target filter", thrown.getDetails());
    }

    @Test
    void testExceptionWithMoreThanTwoLevel() {
        TeivPathException thrown = assertThrows(TeivPathException.class, () -> targetResolver.resolve(null,
                "/GNBDUFunction/NRCellDU/attributes"));
        assertEquals("More than two level deep path is not allowed", thrown.getDetails());
    }

    @Test
    void testExceptionWithPipeInTargetFilter() {
        TeivPathException thrown = assertThrows(TeivPathException.class, () -> targetResolver.resolve(null,
                "/GNBDUFunction/attributes(fdn, enbId)|/GNBDUFunction/classifiers"));
        assertEquals("OR (|) is not supported for target filter", thrown.getDetails());
    }

    @Test
    void testExceptionWithScopeFilterInTargetFilter() {
        TeivPathException thrown = assertThrows(TeivPathException.class, () -> targetResolver.resolve("GNBDUFunction",
                "/GNBDUFunction/attributes[@gNBIdLength=3 or @abc=5 ]"));
        assertEquals("Parameter condition is not supported in target filter", thrown.getDetails());
    }

    @Test
    void testTargetWithoutSlash() {
        TeivPathException thrown = assertThrows(TeivPathException.class, () -> targetResolver.resolve("GNBDUFunction",
                "attributes"));
        assertEquals("no viable alternative at input 'attributes' at line 1:0", thrown.getDetails());
    }

    @Test
    void testDifferentTopologyObjectInTargetFilterAndRootObjectType() {
        TeivPathException thrown = assertThrows(TeivPathException.class, () -> targetResolver.resolve("GNBDUFunction",
                "/ENodeBFunction"));
        assertEquals("Invalid Container name or Root Object name does not match to the path parameter", thrown
                .getDetails());
    }

    @Test
    void testDifferentTopologyObjectInTargetFilterWithAttributesAndRootObjectType() {
        TeivPathException thrown = assertThrows(TeivPathException.class, () -> targetResolver.resolve("GNBDUFunction",
                "/ENodeBFunction/attributes"));
        assertEquals("Target/Scope filter can only contain Root Object types mentioned in the path parameter", thrown
                .getDetails());
    }

    @Test
    void testEmptyRootObjectTypeWithOneOfTheWrongTargetToken() {
        TeivPathException thrown = assertThrows(TeivPathException.class, () -> targetResolver.resolve(null,
                "/GNBDUFunction/attributes(fdn, enbId);/GNBDUFunction/NRCellDU/attributes"));
        assertEquals("More than two level deep path is not allowed", thrown.getDetails());
    }

    @Test
    void testTargetResolverWithTwoObjects() {
        List<TargetObject> expectedObject = List.of(TargetObject.builder("GNBDUFunction").build(), TargetObject.builder(
                "ENodeBFunction").build());
        Assertions.assertEquals(expectedObject, targetResolver.resolve(null, "/GNBDUFunction; /ENodeBFunction"));
    }
}
