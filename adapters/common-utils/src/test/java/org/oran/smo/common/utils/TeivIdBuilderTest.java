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

import static org.oran.smo.common.utils.Constants.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

public class TeivIdBuilderTest {

    @Test
    public void testBuildFunctionFdn() {
        String id = "SMO";
        String expected = ORAN_SMO_TEIV_URN_PREFIX + ":" + id;
        String actual = TeivIdBuilder.buildFunctionFdn(id);
        assertEquals("Function FDN should be correctly built", expected, actual);
    }

    @Test
    public void testBuildRelationshipTypeName() {
        String sourceId = "OCUCPFUNCTION";
        String destinationId = "NRCellCU";

        String relTypeExpected = ORAN_SMO_TEIV_URN_PREFIX + ":" + sourceId + "_" + PROVIDES + "_" + destinationId;
        String relTypeRanExpected = SMO_TEIV_RAN_PREFIX + ":" + sourceId + "_" + MANAGES + "_" + destinationId;
        String relTypeRanOamExpected = SMO_TEIV_REL_OAM_RAN_PREFIX + ":" + sourceId + "_" + O1LINK + "_" + destinationId;

        String relType = TeivIdBuilder.buildTeivRelationshipTypeName(PROVIDES, sourceId, destinationId);
        String relTypeRan = TeivIdBuilder.buildRanRelationshipTypeName(MANAGES, sourceId, destinationId);
        String relTypeRanOam = TeivIdBuilder.buildRanOamRelationshipTypeName(O1LINK, sourceId, destinationId);

        assertEquals("Relationship type name should be correctly formatted", relTypeExpected, relType);
        assertEquals("Relationship type name should be correctly formatted", relTypeRanExpected, relTypeRan);
        assertEquals("Relationship type name should be correctly formatted", relTypeRanOamExpected, relTypeRanOam);
    }

    @Test
    public void testBuildEntityTypeName() {
        String entityType = "NRCellCU";
        String expected = ORAN_SMO_TEIV_URN_PREFIX + ":" + entityType;
        String actual = TeivIdBuilder.buildEntityTypeName(ORAN_SMO_TEIV_URN_PREFIX, entityType);
        assertEquals("Entity type name should be correctly formatted", expected, actual);
    }
}