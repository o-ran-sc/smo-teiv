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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        String gnbcucpFunctionFdn = "urn:oran:smo:teiv:" + getId();

        for (NRCellCU nrCellCU : getNrCellCUs()) {
            addNrcellcuEntitiesAndRelationships(nrCellCU, entityMap, relationshipMap);
            addRelationshipWithNrcellcu(nrCellCU, relationshipMap);
        }

        return Map.of("id", gnbcucpFunctionFdn, "attributes", attributes.createEntityAttributes(), "sourceIds", List.of(
                gnbcucpFunctionFdn, "urn:oran:smo:teiv:" + parentId));
    }

    private void addNrcellcuEntitiesAndRelationships(NRCellCU nrCellCU, Map<String, List<Object>> entityMap,
            Map<String, List<Object>> relationshipMap) {
        String type = "o-ran-smo-teiv-ran:NRCellCU";
        if (!entityMap.containsKey(type)) {
            entityMap.put(type, new ArrayList<>());
        }
        entityMap.get(type).add(nrCellCU.addTeivEntitiesAndRelationships(entityMap, relationshipMap, getId()));
    }

    private void addRelationshipWithNrcellcu(NRCellCU nrCellCU, Map<String, List<Object>> relationshipMap) {
        String relType = "o-ran-smo-teiv-ran:OCUCPFUNCTION_PROVIDES_NRCELLCU";
        if (!relationshipMap.containsKey(relType)) {
            relationshipMap.put(relType, new ArrayList<>());
        }
        relationshipMap.get(relType).add(nrCellCU.createRelationshipWithGnbcucpFunction(getId()));
    }

    @Override
    public Map<String, Object> createRelationshipWithManagedElement(String managedElementId) {
        String gnbcucpFunctionFdn = "urn:oran:smo:teiv:" + getId();
        String managedElementFdn = "urn:oran:smo:teiv:" + managedElementId;
        return Map.of("id", String.format("urn:oran:smo:teiv:%s_MANAGES_%s", managedElementId, getId()), "aSide",
            managedElementFdn, "bSide", gnbcucpFunctionFdn, "sourceIds", List.of(managedElementFdn, gnbcucpFunctionFdn));
    }

    @Override
    public String getTeivEntityType() {
        return "o-ran-smo-teiv-ran:OCUCPFunction";
    }

    @Override
    public String getTeivRelationshipWithManagedElement() {
        return "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_OCUCPFUNCTION";
    }

    @Override
    public void createRelationshipWithSmo(Map<String, List<Object>> relationshipMap) {
        String ranFunctionRelType = "o-ran-smo-teiv-ran:OCUCPFUNCTION_O1LINK_SMO";
        if (!relationshipMap.containsKey(ranFunctionRelType)) {
            relationshipMap.put(ranFunctionRelType, new ArrayList<>());
        }
        String gnbcucpFunctionId = getId();
        String smoId = "SMO";
        Map<String, Object> test = Map.of("id", String.format("urn:oran:smo:teiv:%s_O1LINK_%s", gnbcucpFunctionId, smoId),
                "bSide", "urn:oran:smo:teiv:" + smoId, "aSide", "urn:oran:smo:teiv:" + gnbcucpFunctionId, "sourceIds", List
                        .of(smoId, gnbcucpFunctionId));
        relationshipMap.get(ranFunctionRelType).add(test);
    }
}
