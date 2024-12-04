/*
 *  ============LICENSE_START=======================================================
 *  Copyright (C) 2024 Ericsson
 *  Modifications Copyright (C) 2024 OpenInfra Foundation Europe
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
package org.oran.smo.teiv.service.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class OperationResult {
    private String id;
    private String type; // e.g.: NRCellDU, Site, CloudNativeApplication
    private String category; // "entity" or "relationship"
    private Map<String, Object> attributes;
    private String aSide;
    private String bSide;
    private List<String> classifiers;
    private Map<String, Object> decorators;
    private Map<String, Object> metadata;
    private List<String> sourceIds;
    private boolean isUpdatedInDb;
    public static final String ENTITY_CATEGORY = "entity";
    public static final String RELATIONSHIP_CATEGORY = "relationship";

    @JsonIgnore
    public boolean isRelationship() {
        return getCategory().equals(RELATIONSHIP_CATEGORY);
    }

    @JsonIgnore
    public boolean isEntity() {
        return getCategory().equals(ENTITY_CATEGORY);
    }

    public OperationResult setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata.isEmpty() ? null : new HashMap<>(metadata);
        return this;
    }
}
