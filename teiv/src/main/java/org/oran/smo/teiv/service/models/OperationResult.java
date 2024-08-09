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

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.oran.smo.teiv.service.cloudevent.data.Entity;
import org.oran.smo.teiv.service.cloudevent.data.Relationship;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationResult {
    private String id;
    private String type; // e.g.: NRCellDU, Site, CloudNativeApplication
    private String category; // "entity" or "relationship"
    private Map<String, Object> attributes;
    private String aSide;
    private String bSide;
    private List<String> classifiers;
    private Map<String, Object> decorators;
    private List<String> sourceIds;

    public static OperationResult createEntityOperationResult(String id, String type, Map<String, Object> attributes,
            List<String> sourceIds) {
        return new OperationResult(id, type, "entity", attributes, null, null, null, null, sourceIds);
    }

    public static OperationResult createEntityOperationResult(String id, String type, Map<String, Object> attributes) {
        return createEntityOperationResult(id, type, attributes, null);
    }

    public static OperationResult createEntityOperationResult(String id, String type) {
        return OperationResult.createEntityOperationResult(id, type, null, null);
    }

    public static OperationResult createEntityOperationResult(Entity entity) {
        return OperationResult.createEntityOperationResult(entity.getId(), entity.getType(), entity.getAttributes(), entity
                .getSourceIds());
    }

    public static OperationResult createRelationshipOperationResult(String id, String type, String aSide, String bSide,
            List<String> sourceIds) {
        return new OperationResult(id, type, "relationship", null, aSide, bSide, null, null, sourceIds);
    }

    public static OperationResult createRelationshipOperationResult(String id, String type, String aSide, String bSide) {
        return createRelationshipOperationResult(id, type, aSide, bSide, null);
    }

    public static OperationResult createRelationshipOperationResult(String id, String type) {
        return OperationResult.createRelationshipOperationResult(id, type, null, null, null);
    }

    public static OperationResult createRelationshipOperationResult(Relationship relationship) {
        return OperationResult.createRelationshipOperationResult(relationship.getId(), relationship.getType(), relationship
                .getASide(), relationship.getBSide(), relationship.getSourceIds());
    }

    public static OperationResult createClassifierOperationResult(String id, String type, String category,
            List<String> classifiers) {
        return new OperationResult(id, type, category, null, null, null, classifiers, null, null);
    }

    public static OperationResult createDecoratorOperationResult(String id, String type, String category,
            Map<String, Object> decorators) {
        return new OperationResult(id, type, category, null, null, null, null, decorators, null);
    }

    @JsonIgnore
    public boolean isRelationship() {
        return getCategory().equals("relationship");
    }

    @JsonIgnore
    public boolean isEntity() {
        return getCategory().equals("entity");
    }
}
