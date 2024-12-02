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
package org.oran.smo.teiv.groups.api;

import org.oran.smo.teiv.api.model.OranTeivCreateGroupPayload;
import org.oran.smo.teiv.api.model.OranTeivGroupByIdResponse;
import org.oran.smo.teiv.api.model.OranTeivGroupsResponse;
import org.oran.smo.teiv.api.model.OranTeivMembersResponse;
import org.oran.smo.teiv.api.model.OranTeivUpdateGroupNamePayload;
import org.oran.smo.teiv.api.model.OranTeivUpdateProvidedMembersPayload;
import org.oran.smo.teiv.exposure.utils.RequestDetails;

public interface GroupsService {

    /**
     * Creates a new group.
     *
     * @param createGroupPayload
     *     - A {@link OranTeivCreateGroupPayload} to create
     * @return created group response
     */
    OranTeivGroupByIdResponse createGroup(final OranTeivCreateGroupPayload createGroupPayload);

    /**
     * Get all groups.
     *
     * @param requestDetails
     *     The request details containing pagination info.
     * @return The groups response containing the list of groups.
     */
    OranTeivGroupsResponse getAllGroups(final RequestDetails requestDetails, final String name);

    /**
     * Deletes the group.
     *
     * @param groupId
     *     - group id
     */
    void deleteGroup(final String groupId);

    /**
     * Updates a group name
     *
     * @param groupId
     *     - group id
     */
    void renameGroup(final String groupId, final OranTeivUpdateGroupNamePayload newGroupName);

    /**
     * Get group by specified id.
     *
     * @param groupId
     *     - group id
     */
    OranTeivGroupByIdResponse getGroupById(final String groupId);

    /**
     * Resolves the members of the group.
     *
     * @param groupId
     *     - group id
     * @param requestDetails
     *     - details from the request used for pagination
     * @return resolved members of the group
     */
    OranTeivMembersResponse getResolvedMembers(final String groupId, final RequestDetails requestDetails);

    /**
     * Update the provided members of a static group by specified id.
     *
     * @param groupId
     *     - group id
     * @param updateProvidedMembersPayload
     *     - A {@link OranTeivUpdateProvidedMembersPayload} to update provided members ids
     */
    void updateProvidedMembers(final String groupId,
            final OranTeivUpdateProvidedMembersPayload updateProvidedMembersPayload);

    /**
     * Get provided members of a static group based on the status.
     *
     * @param groupId
     *     - group id
     * @param status
     *     - status
     * @param requestDetails
     *     - details from the request used for pagination
     * @return provided members of the group
     */
    OranTeivMembersResponse getProvidedMembersByStatus(final String groupId, final String status,
            final RequestDetails requestDetails);
}
