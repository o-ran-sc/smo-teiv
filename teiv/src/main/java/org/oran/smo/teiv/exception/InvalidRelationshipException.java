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
package org.oran.smo.teiv.exception;

import org.oran.smo.teiv.service.cloudevent.data.Relationship;

public class InvalidRelationshipException extends CloudEventParserException {
    private InvalidRelationshipException(final String message) {
        super(message);
    }

    public static InvalidRelationshipException missingId(final Relationship relationship) {
        return new InvalidRelationshipException(String.format("Missing id in relationship! ASide: %s, BSide: %s",
                relationship.getASide(), relationship.getBSide()));
    }

    public static InvalidRelationshipException missingSide(final Relationship relationship) {
        return new InvalidRelationshipException(String.format("Missing a side in relationship! Relationship id: %s",
                relationship.getId()));
    }

    public static InvalidRelationshipException invalidType(final Relationship relationship) {
        return new InvalidRelationshipException(String.format("Invalid relationship type! Relationship id: %s", relationship
                .getId()));
    }

    public static InvalidRelationshipException missingSourceIds(final String relationshipId) {
        return new InvalidRelationshipException(String.format("Relationship is missing sourceIds! Relationship id: %s",
                relationshipId));
    }

    public static InvalidRelationshipException invalidModule(final Relationship relationship) {
        return new InvalidRelationshipException(String.format("Invalid module in relationship! Relationship id: %s",
                relationship.getId()));
    }

    public static InvalidRelationshipException invalidModuleTypePair(final Relationship relationship) {
        return new InvalidRelationshipException(String.format("Invalid relationship module-type pair! Relationship id: %s",
                relationship.getId()));
    }
}
