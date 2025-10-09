/*
 *  ============LICENSE_START=======================================================
 *  Modifications Copyright (C) 2025 OpenInfra Foundation Europe
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
package org.oran.smo.common.utils;

import org.junit.Test;

import static org.oran.smo.common.utils.Constants.ORAN_SMO_TEIV_URN_PREFIX;
import static org.springframework.test.util.AssertionErrors.assertEquals;

public class TeivIdBuilderTest {

    @Test
    public void testBuildFunctionFdn() {
        String id = "functionFdn123";
        String expected = ORAN_SMO_TEIV_URN_PREFIX + id;
        String actual = TeivIdBuilder.buildFunctionFdn(id);
        assertEquals("Function FDN should be correctly built", expected, actual);
    }

    @Test
    public void testBuildRelationshipTypeName() {
        String type = "PROVIDES";
        String sourceId = "OCUCPFUNCTION";
        String destinationId = "NRCellCU";
        String expected = ORAN_SMO_TEIV_URN_PREFIX + sourceId + "_" + type + "_" + destinationId;
        String actual = TeivIdBuilder.buildRelationshipTypeName(type, sourceId, destinationId);
        assertEquals("Relationship type name should be correctly formatted", expected, actual);
    }

    @Test
    public void testBuildEntityTypeName() {
        String entityType = "NRCellCU";
        String expected = ORAN_SMO_TEIV_URN_PREFIX + ":" + entityType;
        String actual = TeivIdBuilder.buildEntityTypeName(ORAN_SMO_TEIV_URN_PREFIX, entityType);
        assertEquals("Entity type name should be correctly formatted", expected, actual);
    }
}
