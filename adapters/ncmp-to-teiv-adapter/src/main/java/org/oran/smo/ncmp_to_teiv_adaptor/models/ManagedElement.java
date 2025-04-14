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

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ManagedElement {

    @Getter
    @JsonProperty("id")
    private String id;

    private List<AbstractFunction> ranFunctions = new ArrayList<>();

    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<>();

    @JsonAnySetter
    public void handleUnknownProperties(String name, Object value) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            if ("_3gpp-nr-nrm-gnbdufunction:GNBDUFunction".equals(name)) {
                List<Object> list = (List<Object>) value;
                for (Object item : list) {
                    GNBDUFunction du = mapper.convertValue(item, GNBDUFunction.class);
                    ranFunctions.add(du);
                }
            } else if ("_3gpp-nr-nrm-gnbcucpfunction:GNBCUCPFunction".equals(name)) {
                List<Object> list = (List<Object>) value;
                for (Object item : list) {
                    GNBCUCPFunction cu = mapper.convertValue(item, GNBCUCPFunction.class);
                    ranFunctions.add(cu);
                }
            } else {
                this.additionalProperties.put(name, value);
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
    }

    public Map<String, Object> addTeivEntitiesAndRelationships(Map<String, List<Object>> entityMap,
            Map<String, List<Object>> relationshipMap) {
        for (AbstractFunction ranFunction : this.ranFunctions) {
            addRanFunctionEntitiesAndRelationships(ranFunction, entityMap, relationshipMap);
            addRelationshipWithRanFunction(ranFunction, relationshipMap);
        }
        String managedElementId = "urn:oran:smo:teiv:" + this.getId();
        return Map.of("id", managedElementId, "sourceIds", List.of(managedElementId));
    }

    private void addRanFunctionEntitiesAndRelationships(AbstractFunction ranFunction, Map<String, List<Object>> entityMap,
            Map<String, List<Object>> relationshipMap) {
        String ranFunctionType = ranFunction.getTeivEntityType();
        if (!entityMap.containsKey(ranFunctionType)) {
            entityMap.put(ranFunctionType, new ArrayList<>());
        }
        entityMap.get(ranFunctionType).add(ranFunction.addTeivEntitiesAndRelationships(entityMap, relationshipMap));
    }

    private void addRelationshipWithRanFunction(AbstractFunction ranFunction, Map<String, List<Object>> relationshipMap) {
        String ranFunctionRelType = ranFunction.getTeivRelationshipWithManagedElement();
        if (!relationshipMap.containsKey(ranFunctionRelType)) {
            relationshipMap.put(ranFunctionRelType, new ArrayList<>());
        }
        relationshipMap.get(ranFunctionRelType).add(ranFunction.createRelationshipWithManagedElement(this.id));
    }
}
