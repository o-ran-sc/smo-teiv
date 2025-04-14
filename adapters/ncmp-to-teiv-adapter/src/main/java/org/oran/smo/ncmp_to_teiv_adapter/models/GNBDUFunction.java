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

public class GNBDUFunction extends AbstractFunction {

    @Getter
    @JsonProperty("attributes")
    private GNBDUAttributes attributes;
    @Getter
    @JsonProperty("_3gpp-nr-nrm-nrcelldu:NRCellDU")
    private List<NRCellDU> nrCellDUs;

    @Override
    public Map<String, Object> addTeivEntitiesAndRelationships(Map<String, List<Object>> entityMap,
            Map<String, List<Object>> relationshipMap) {
        createRelationshipWithSmo(relationshipMap);
        String gnbduFunctionId = "urn:oran:smo:teiv:" + getId();
        List<Map<String, Object>> nrCellDUList = new ArrayList<>();
        List<Map<String, Object>> gnbduFunctionNrcellduRel = new ArrayList<>();

        for (NRCellDU nrCellDU : getNrCellDUs()) {
            addNrcellduEntitiesAndRelationships(nrCellDU, entityMap, relationshipMap);
            addRelationshipWithNrcelldu(nrCellDU, relationshipMap);
            //            nrCellDUList.add(nrCellDU.addTeivEntitiesAndRelationships(entityMap, relationshipMap));
            //            gnbduFunctionNrcellduRel.add(nrCellDU.createRelationshipWithGnbduFunction(getId()));
        }

        //        String type = "o-ran-smo-teiv-ran:NRCellDU";
        //        if (!entityMap.containsKey(type)) {
        //            entityMap.put(type, new ArrayList<>());
        //        }
        //        entityMap.get(type).add(new ArrayList<>(nrCellDUList));
        //
        //        String relType = "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU";
        //        if (!relationshipMap.containsKey(relType)) {
        //            relationshipMap.put(relType, new ArrayList<>());
        //        }
        //        relationshipMap.get(relType).add( new ArrayList<>(gnbduFunctionNrcellduRel));

        return Map.of("id", gnbduFunctionId, "attributes", attributes.createEntityAttributes(), "sourceIds", List.of(
                gnbduFunctionId));
    }

    private void addNrcellduEntitiesAndRelationships(NRCellDU nrCellDU, Map<String, List<Object>> entityMap,
            Map<String, List<Object>> relationshipMap) {
        String type = "o-ran-smo-teiv-ran:NRCellDU";
        if (!entityMap.containsKey(type)) {
            entityMap.put(type, new ArrayList<>());
        }
        entityMap.get(type).add(nrCellDU.addTeivEntitiesAndRelationships(entityMap, relationshipMap));
    }

    private void addRelationshipWithNrcelldu(NRCellDU nrCellDU, Map<String, List<Object>> relationshipMap) {
        String relType = "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU";
        if (!relationshipMap.containsKey(relType)) {
            relationshipMap.put(relType, new ArrayList<>());
        }
        relationshipMap.get(relType).add(nrCellDU.createRelationshipWithGnbduFunction(getId()));
    }

    @Override
    public Map<String, Object> createRelationshipWithManagedElement(String managedElementId) {
        String gnbduFunctionFdn = "urn:oran:smo:teiv:" + getId();
        String managedElementFdn = "urn:oran:smo:teiv:" + managedElementId;
        return Map.of("id", String.format("urn:oran:smo:teiv:%s_MANAGES_%s", managedElementId, getId()), "aSide",
                managedElementFdn, "bSide", gnbduFunctionFdn, "sourceIds", List.of(managedElementFdn, gnbduFunctionFdn));
    }

    @Override
    public String getTeivEntityType() {
        return "o-ran-smo-teiv-ran:ODUFunction";
    }

    @Override
    public String getTeivRelationshipWithManagedElement() {
        return "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION";
    }

    @Override
    public void createRelationshipWithSmo(Map<String, List<Object>> relationshipMap) {
        String ranFunctionRelType = "o-ran-smo-teiv-ran:ODUFUNCTION_O1LINK_SMO";
        if (!relationshipMap.containsKey(ranFunctionRelType)) {
            relationshipMap.put(ranFunctionRelType, new ArrayList<>());
        }
        String gnbduFunctionId = getId();
        String smoId = "SMO";
        Map<String, Object> test = Map.of("id", String.format("urn:oran:smo:teiv:%s_O1LINK_%s", gnbduFunctionId, smoId),
                "bSide", "urn:oran:smo:teiv:" + smoId, "aSide", "urn:oran:smo:teiv:" + gnbduFunctionId, "sourceIds", List
                        .of(smoId, gnbduFunctionId));
        relationshipMap.get(ranFunctionRelType).add(test);
    }
}
