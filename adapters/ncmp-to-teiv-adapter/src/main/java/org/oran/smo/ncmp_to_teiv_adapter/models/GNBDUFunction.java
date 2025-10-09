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

import static org.oran.smo.common.utils.Constants.SMO_TEIV_RAN_PREFIX;
import static org.oran.smo.common.utils.Constants.SMO_TEIV_REL_OAM_RAN_PREFIX;

public class GNBDUFunction extends AbstractFunction {

    @Getter
    @JsonProperty("attributes")
    private GNBDUAttributes attributes;
    @Getter
    @JsonProperty("_3gpp-nr-nrm-nrcelldu:NRCellDU")
    private List<NRCellDU> nrCellDUs;

    @Override
    public Map<String, Object> addTeivEntitiesAndRelationships(Map<String, List<Object>> entityMap,
            Map<String, List<Object>> relationshipMap, String parentId) {
        createRelationshipWithSmo(relationshipMap);
        String gnbduFunctionFdn = TeivIdBuilder.buildFunctionFdn(getId());

        for (NRCellDU nrCellDU : getNrCellDUs()) {
            addNrcellduEntitiesAndRelationships(nrCellDU, entityMap, relationshipMap);
            addRelationshipWithNrcelldu(nrCellDU, relationshipMap);
        }

        return Map.of("id", gnbduFunctionFdn, "attributes", attributes.createEntityAttributes(), "sourceIds", List.of(
                gnbduFunctionFdn, TeivIdBuilder.buildFunctionFdn(parentId)));
    }

    private void addNrcellduEntitiesAndRelationships(NRCellDU nrCellDU, Map<String, List<Object>> entityMap,
            Map<String, List<Object>> relationshipMap) {
        String type = TeivIdBuilder.buildEntityTypeName(SMO_TEIV_RAN_PREFIX, "NRCellDU");
        if (!entityMap.containsKey(type)) {
            entityMap.put(type, new ArrayList<>());
        }
        entityMap.get(type).add(nrCellDU.addTeivEntitiesAndRelationships(entityMap, relationshipMap, getId()));
    }

    private void addRelationshipWithNrcelldu(NRCellDU nrCellDU, Map<String, List<Object>> relationshipMap) {
        String relType = TeivIdBuilder.buildEntityTypeName(SMO_TEIV_RAN_PREFIX, "ODUFUNCTION_PROVIDES_NRCELLDU");
        if (!relationshipMap.containsKey(relType)) {
            relationshipMap.put(relType, new ArrayList<>());
        }
        relationshipMap.get(relType).add(nrCellDU.createRelationshipWithGnbduFunction(getId()));
    }

    @Override
    public Map<String, Object> createRelationshipWithManagedElement(String managedElementId) {
        String gnbduFunctionFdn = TeivIdBuilder.buildFunctionFdn(getId());
        String managedElementFdn = TeivIdBuilder.buildFunctionFdn(managedElementId);
        return Map.of("id", TeivIdBuilder.buildRelationshipTypeName("MANAGES", gnbduFunctionFdn, getId()), "aSide",
                managedElementFdn, "bSide", gnbduFunctionFdn, "sourceIds", List.of(managedElementFdn, gnbduFunctionFdn));
    }

    @Override
    public String getTeivEntityType() {
        return TeivIdBuilder.buildEntityTypeName(SMO_TEIV_RAN_PREFIX, "ODUFunction");
    }

    @Override
    public String getTeivRelationshipWithManagedElement() {
        return TeivIdBuilder.buildEntityTypeName(SMO_TEIV_REL_OAM_RAN_PREFIX, "MANAGEDELEMENT_MANAGES_OCUCPFUNCTION");
    }

    @Override
    public void createRelationshipWithSmo(Map<String, List<Object>> relationshipMap) {
        String ranFunctionRelType = TeivIdBuilder.buildEntityTypeName(SMO_TEIV_RAN_PREFIX, "ODUFUNCTION_O1LINK_SMO");
        if (!relationshipMap.containsKey(ranFunctionRelType)) {
            relationshipMap.put(ranFunctionRelType, new ArrayList<>());
        }
        String gnbduFunctionId = getId();
        String smoId = "SMO";
        Map<String, Object> test = Map.of("id", TeivIdBuilder.buildRelationshipTypeName("O1LINK", gnbduFunctionId, smoId),
                "bSide", TeivIdBuilder.buildFunctionFdn(smoId), "aSide", TeivIdBuilder.buildFunctionFdn(gnbduFunctionId),
                "sourceIds", List.of(smoId, gnbduFunctionId));
        relationshipMap.get(ranFunctionRelType).add(test);
    }
}
