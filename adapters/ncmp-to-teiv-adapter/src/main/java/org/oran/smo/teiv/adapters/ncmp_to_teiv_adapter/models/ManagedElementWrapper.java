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
package org.oran.smo.teiv.adapters.ncmp_to_teiv_adapter.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.oran.smo.teiv.adapters.common.utils.TeivIdBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.oran.smo.teiv.adapters.common.utils.Constants.SMO_TEIV_OAM_PREFIX;

public class ManagedElementWrapper {

    @Getter
    @JsonProperty("_3gpp-common-managed-element:ManagedElement")
    private List<ManagedElement> managedElements;

    public Map<String, Object> toTeivCloudEventPayload() {
        Map<String, List<Object>> entityMap = new HashMap<>();
        Map<String, List<Object>> relationshipMap = new HashMap<>();
        List<Map<String, Object>> managedElements = new ArrayList<>();
        for (ManagedElement managedElement : this.managedElements) {
            managedElements.add(managedElement.addTeivEntitiesAndRelationships(entityMap, relationshipMap, ""));
        }
        entityMap.put(TeivIdBuilder.buildEntityTypeName(SMO_TEIV_OAM_PREFIX, "ManagedElement"), new ArrayList<>(
                managedElements));
        Map<String, Object> finalJson = Map.of("relationships", List.of(relationshipMap), "entities", List.of(entityMap));
        return finalJson;
    }
}
