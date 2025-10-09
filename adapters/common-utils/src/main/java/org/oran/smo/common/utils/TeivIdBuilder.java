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

import static org.oran.smo.common.utils.Constants.ORAN_SMO_TEIV_URN_PREFIX;
import static org.oran.smo.common.utils.Constants.SMO_TEIV_RAN_PREFIX;
import static org.oran.smo.common.utils.Constants.SMO_TEIV_REL_OAM_RAN_PREFIX;

public class TeivIdBuilder {

    /**
     * Builds a fully qualified function FDN using the given ID.
     *
     * @param id
     *     the identifier
     * @return the fully qualified FDN string
     */
    public static String buildFunctionFdn(String id) {
        return ORAN_SMO_TEIV_URN_PREFIX + ":" + id;
    }

    /**
     * Builds relationshipType name with the given prefix model, relationship type, sourceId, and destinationId.
     *
     * @param type
     *     the relationship type
     * @param sourceId
     *     the first identifier
     * @param destinationId
     *     the second identifier
     * @return a relationshipTypeName
     */
    public static String buildRelationshipTypeName(String prefix, String type, String sourceId, String destinationId) {
        return prefix + ":" + sourceId + "_" + type + "_" + destinationId;
    }

    public static String buildTeivRelationshipTypeName(String type, String sourceId, String destinationId) {
        return buildRelationshipTypeName(ORAN_SMO_TEIV_URN_PREFIX, type, sourceId, destinationId);
    }

    public static String buildRanRelationshipTypeName(String type, String sourceId, String destinationId) {
        return buildRelationshipTypeName(SMO_TEIV_RAN_PREFIX, type, sourceId, destinationId);
    }

    public static String buildRanOamRelationshipTypeName(String type, String sourceId, String destinationId) {
        return buildRelationshipTypeName(SMO_TEIV_REL_OAM_RAN_PREFIX, type, sourceId, destinationId);
    }

    /**
     * Builds a entityTypeName with given prefix and entityType.
     *
     * @param prefix
     *     the string prefix
     * @param entityType
     *     the entity type
     * @return a combined prefix and type string
     */
    public static String buildEntityTypeName(String prefix, String entityType) {
        return prefix + ":" + entityType;
    }
}
