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
package org.oran.smo.ncmp_to_teiv_adapter.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.oran.smo.common.utils.TeivIdBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.oran.smo.common.utils.Constants.PROVIDES;
import static org.oran.smo.common.utils.Constants.MANAGES;
import static org.oran.smo.common.utils.Constants.O1LINK;
import static org.oran.smo.common.utils.Constants.SMO_TEIV_RAN_PREFIX;

public class GNBCUCPFunction extends AbstractFunction {

    @Getter
    @JsonProperty("attributes")
    private GNBCUCPAttributes attributes;
    @Getter
    @JsonProperty("_3gpp-nr-nrm-nrcellcu:NRCellCU")
    private List<NRCellCU> nrCellCUs;

    @Override
    public Map<String, Object> addTeivEntitiesAndRelationships(Map<String, List<Object>> entityMap,
            Map<String, List<Object>> relationshipMap, String parentId) {
        createRelationshipWithSmo(relationshipMap);
        String gnbcucpFunctionFdn = TeivIdBuilder.buildFunctionFdn(getId());

        for (NRCellCU nrCellCU : getNrCellCUs()) {
            addNrcellcuEntitiesAndRelationships(nrCellCU, entityMap, relationshipMap);
            addRelationshipWithNrcellcu(nrCellCU, relationshipMap);
        }

        return Map.of("id", gnbcucpFunctionFdn, "attributes", attributes.createEntityAttributes(), "sourceIds", List.of(
                gnbcucpFunctionFdn, TeivIdBuilder.buildFunctionFdn(parentId)));
    }

    private void addNrcellcuEntitiesAndRelationships(NRCellCU nrCellCU, Map<String, List<Object>> entityMap,
            Map<String, List<Object>> relationshipMap) {
        String type = TeivIdBuilder.buildEntityTypeName(SMO_TEIV_RAN_PREFIX, "NRCellCU");
        if (!entityMap.containsKey(type)) {
            entityMap.put(type, new ArrayList<>());
        }
        entityMap.get(type).add(nrCellCU.addTeivEntitiesAndRelationships(entityMap, relationshipMap, getId()));
    }

    private void addRelationshipWithNrcellcu(NRCellCU nrCellCU, Map<String, List<Object>> relationshipMap) {
        String relType = TeivIdBuilder.buildRanRelationshipTypeName(PROVIDES, "OCUCPFUNCTION", "NRCELLCU");
        if (!relationshipMap.containsKey(relType)) {
            relationshipMap.put(relType, new ArrayList<>());
        }
        relationshipMap.get(relType).add(nrCellCU.createRelationshipWithGnbcucpFunction(getId()));
    }

    @Override
    public Map<String, Object> createRelationshipWithManagedElement(String managedElementId) {
        String gnbcucpFunctionFdn = TeivIdBuilder.buildFunctionFdn(getId());
        String managedElementFdn = TeivIdBuilder.buildFunctionFdn(managedElementId);
        return Map.of("id", TeivIdBuilder.buildTeivRelationshipTypeName(MANAGES, managedElementId, getId()), "aSide",
                managedElementFdn, "bSide", gnbcucpFunctionFdn, "sourceIds", List.of(managedElementFdn,
                        gnbcucpFunctionFdn));
    }

    @Override
    public String getTeivEntityType() {
        return TeivIdBuilder.buildEntityTypeName(SMO_TEIV_RAN_PREFIX, "OCUCPFunction");
    }

    @Override
    public String getTeivRelationshipWithManagedElement() {
        return TeivIdBuilder.buildRanOamRelationshipTypeName(MANAGES, "MANAGEDELEMENT", "OCUCPFUNCTION");
    }

    @Override
    public void createRelationshipWithSmo(Map<String, List<Object>> relationshipMap) {
        String ranFunctionRelType = TeivIdBuilder.buildRanRelationshipTypeName(O1LINK, "OCUCPFUNCTION", "SMO");
        if (!relationshipMap.containsKey(ranFunctionRelType)) {
            relationshipMap.put(ranFunctionRelType, new ArrayList<>());
        }
        String gnbcucpFunctionId = getId();
        String smoId = "SMO";
        Map<String, Object> test = Map.of("id", TeivIdBuilder.buildTeivRelationshipTypeName(O1LINK, gnbcucpFunctionId, smoId),
                "bSide", TeivIdBuilder.buildFunctionFdn(smoId), "aSide", TeivIdBuilder.buildFunctionFdn(gnbcucpFunctionId),
                "sourceIds", List.of(smoId, gnbcucpFunctionId));
        relationshipMap.get(ranFunctionRelType).add(test);
    }
}
