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

import java.util.List;
import java.util.Map;

public class NRCellCU extends AbstractEntity {

    @Getter
    @JsonProperty("attributes")
    private NRCellCUAttributes attributes;

    @Override
    public Map<String, Object> addTeivEntitiesAndRelationships(Map<String, List<Object>> entityMap,
            Map<String, List<Object>> relationshipMap, String parentId) {
        String nrcellcucpFdn = "urn:oran:smo:teiv:" + getId();
        return Map.of("id", nrcellcucpFdn, "attributes", attributes.createEntityAttributes(), "sourceIds", List.of(
                nrcellcucpFdn, "urn:oran:smo:teiv:" + parentId));
    }

    public Map<String, Object> createRelationshipWithGnbcucpFunction(String gnbcucpFunctionId) {
        String gnbcucpFunctionFdn = "urn:oran:smo:teiv:" + gnbcucpFunctionId;
        String nrcellCuFdn = "urn:oran:smo:teiv:" + getId();
        return Map.of("id", String.format("urn:oran:smo:teiv:%s_PROVIDES_%s", gnbcucpFunctionId, getId()), "aSide",
                gnbcucpFunctionFdn, "bSide", nrcellCuFdn, "sourceIds", List.of(gnbcucpFunctionFdn, nrcellCuFdn));
    }

    @Override
    public String getTeivEntityType() {
        return "o-ran-smo-teiv-ran:NRCellCU";
    }
}
