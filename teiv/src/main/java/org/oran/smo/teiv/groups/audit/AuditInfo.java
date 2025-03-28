/*
 *  ============LICENSE_START=======================================================
 *  Copyright (C) 2024 Ericsson
 *  Modifications Copyright (C) 2024-2025 OpenInfra Foundation Europe
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
package org.oran.smo.teiv.groups.audit;

import static org.oran.smo.teiv.groups.audit.ExecutionStatus.SUCCESS;

import java.util.List;

import lombok.Builder;

import org.oran.smo.teiv.api.model.OranTeivCreateGroupPayload;
import org.oran.smo.teiv.api.model.OranTeivDynamic;
import org.oran.smo.teiv.api.model.OranTeivStatic;

@Builder
public class AuditInfo {
    private final ExecutionStatus status;
    private final GroupOperation operation;
    private final OranTeivCreateGroupPayload createGroupPayload;
    private final String createGroupPayloadString;
    private final String groupId;
    private final String groupName;
    private final List<Object> providedMembers;
    private final String exceptionMessage;

    @Override
    public String toString() {
        if (status.equals(SUCCESS)) {
            return generateSuccessMessage();
        }
        return generateFailureMessage();
    }

    private String generateFailureMessage() {
        return switch (operation) {
            case CREATE -> String.format("%s - Create group %s, %s", status,
                    createGroupPayloadString != null && !createGroupPayloadString.isEmpty() ?
                            createGroupPayloadString :
                            generateCreateGroupMsg(), exceptionMessage);
            case DELETE -> String.format("%s - Delete group %s, %s", status, groupId, exceptionMessage);
            case UPDATE_NAME -> String.format("%s - Update name %s for group %s, %s", status, groupName, groupId,
                    exceptionMessage);
            case MERGE_PROVIDED_MEMBERS -> String.format("%s - Merge provided members %s for group %s, %s", status,
                    providedMembers, groupId, exceptionMessage);
            case REMOVE_PROVIDED_MEMBERS -> String.format("%s - Remove provided members %s for group %s, %s", status,
                    providedMembers, groupId, exceptionMessage);
        };
    }

    private String generateSuccessMessage() {
        StringBuilder str = new StringBuilder(String.format("%s - ", status));
        switch (operation) {
            case CREATE -> str.append(String.format("Create group %s with id %s", generateCreateGroupMsg(), this.groupId));
            case DELETE -> str.append(String.format("Delete group %s", groupId));
            case UPDATE_NAME -> str.append(String.format("Update name %s for group %s", groupName, groupId));
            case MERGE_PROVIDED_MEMBERS -> str.append(String.format("Merge provided members %s for group %s",
                    providedMembers, groupId));
            case REMOVE_PROVIDED_MEMBERS -> str.append(String.format("Remove provided members %s for group %s",
                    providedMembers, groupId));
        }
        return str.toString();
    }

    private String generateCreateGroupMsg() {
        if (createGroupPayload instanceof OranTeivDynamic payload) {
            return String.format("type=%s, name=%s, criteria=%s", payload.getType(), payload.getName(), payload
                    .getCriteria());
        } else if (createGroupPayload instanceof OranTeivStatic payload) {
            return String.format("type=%s, name=%s, providedMembers=%s", payload.getType(), payload.getName(), payload
                    .getProvidedMembers());
        }
        return null;
    }
}
