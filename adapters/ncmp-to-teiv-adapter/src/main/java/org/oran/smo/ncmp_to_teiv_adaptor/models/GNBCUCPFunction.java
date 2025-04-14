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

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GNBCUCPFunction extends AbstractFunction {

    @Getter
    @JsonProperty("id")
    private String id;
    @Getter
    @JsonProperty("attributes")
    private GNBCUCPAttributes attributes;
    @Getter
    @JsonProperty("_3gpp-nr-nrm-nrcellcu:NRCellCU")
    private List<NRCellCU> nrCellCUs;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Map<String, Object> addTeivEntitiesAndRelationships(Map<String, List<Object>> entityMap,
            Map<String, List<Object>> relationshipMap) {
        String gnbcucpFunctionFdn = "urn:oran:smo:teiv:" + this.id;
        List<Map<String, Object>> nrCellCUList = new ArrayList<>();
        List<Map<String, Object>> gnbcucpFunctionNrcellcuRel = new ArrayList<>();

        for (NRCellCU nrCellCU : getNrCellCUs()) {
            nrCellCUList.add(nrCellCU.createTeivEntity());
            gnbcucpFunctionNrcellcuRel.add(nrCellCU.createRelationshipWithGnbcucpFunction(this.id));
        }

        entityMap.put("o-ran-smo-teiv-ran:NRCellCU", new ArrayList<>(nrCellCUList));
        relationshipMap.put("o-ran-smo-teiv-ran:OCUCPFUNCTION_PROVIDES_NRCELLCU", new ArrayList<>(
                gnbcucpFunctionNrcellcuRel));
        return Map.of("id", gnbcucpFunctionFdn, "attributes", attributes.createTeivGnbcucpFunctionAttributes(), "sourceIds",
                List.of(gnbcucpFunctionFdn));
    }

    public Map<String, Object> createRelationshipWithManagedElement(String managedElementId) {
        String gnbcucpFunctionId = this.id;
        return Map.of("id", String.format("urn:oran:smo:teiv:%s_MANAGES_%s", managedElementId, gnbcucpFunctionId), "aSide",
                "urn:oran:smo:teiv:" + managedElementId, "bSide", "urn:oran:smo:teiv:" + gnbcucpFunctionId, "sourceIds",
                List.of(managedElementId, gnbcucpFunctionId));
    }

    public String getTeivEntityType() {
        return "o-ran-smo-teiv-ran:OCUCPFunction";
    }

    public String getTeivRelationshipWithManagedElement() {
        return "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_OCUCPFUNCTION";
    }
}
