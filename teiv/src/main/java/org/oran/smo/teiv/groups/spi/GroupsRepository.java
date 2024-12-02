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
package org.oran.smo.teiv.groups.spi;

import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Select;

import java.util.Map;
import java.util.Set;

public interface GroupsRepository {

    /**
     * Gets the group by given ID.
     *
     * @param groupId
     *     - group id
     * @return the group record
     * @throws GroupNotFoundException
     *     if group id doesn't exists
     */
    GroupRecord getGroupById(final String groupId) throws GroupNotFoundException;

    /**
     * Gets the group type by id.
     *
     * @param groupId
     *     - group id
     * @return group type
     * @throws GroupNotFoundException
     *     if group id doesn't exists
     */
    String getGroupTypeById(final String groupId) throws GroupNotFoundException;

    /**
     * Gets the dynamic group criteria by given group ID.
     *
     * @param groupId
     *     - group id
     * @return the dynamic group criteria
     * @throws GroupNotFoundException
     *     if group id doesn't exists
     */
    String getCriteriaByGroupId(final String groupId) throws GroupNotFoundException;

    /**
     * Gets all the groups by name.
     *
     * @param name
     *     - group name
     * @param offset
     *     - number of items to skip
     * @param limit
     *     - number of records to fetch
     * @return the list of group records that matches the name along with the total count
     */
    Pair<List<GroupRecord>, Integer> getGroupsByName(final String name, final int offset, final int limit);

    /**
     * Gets all the groups.
     *
     * @param offset
     *     - number of items to skip
     * @param limit
     *     - number of records to fetch
     * @return the list of all group records along with the total count
     */
    Pair<List<GroupRecord>, Integer> getAllGroups(final int offset, final int limit);

    /**
     * Gets members of a static group.
     *
     * @param query
     *     - query to be executed
     * @param offset
     *     - number of items to skip
     * @param limit
     *     - number of records to fetch
     * @return the list of all records along with the total count
     */
    Pair<List<Record>, Integer> getStaticMembers(final Select<Record2<String, String>> query, final int offset,
            final int limit);

    /**
     * Gets all topology types of a Static group.
     *
     * @param groupId
     *     - group id
     * @return the list of all topology types
     */
    List<String> getTopologyTypesByGroupId(final String groupId);

    /**
     * Gets all provided members of a Static group.
     *
     * @param groupId
     *     - group id
     * @param offset
     *     - number of items to skip
     * @param limit
     *     - number of records to fetch
     * @return the list of all records along with the total count
     */
    Pair<List<Record>, Integer> getAllProvidedMembersByGroupId(final String groupId, final int offset, final int limit);

    /**
     * Gets static group details for a group by id.
     *
     * @param groupId
     *     - group id
     * @return A tuple which consists of a set of all available topology types and count of all provided members ids stored
     *     in a group for a group by id
     */
    Pair<Set<String>, Integer> getStaticGroupDetailsById(String groupId);

    /**
     * Creates a new dynamic group.
     *
     * @param groupRecord
     *     - A {@link DynamicGroupRecord} to create
     */
    void createDynamicGroup(final DynamicGroupRecord groupRecord);

    /**
     * Creates a new static group.
     *
     * @param groupRecord
     *     - A {@link StaticGroupRecord} to create
     */
    void createStaticGroup(final StaticGroupRecord groupRecord);

    /**
     * Merges members to the existing group.
     *
     * @param groupId
     *     - group id
     * @param membersToMerge
     *     - members to merge to the group
     * @param availableTopologyType
     *     - set of all topology types present in a group
     * @param availableProvidedMembersCount
     *     - count of all provided member identifiers present in a group
     */
    void mergeProvidedMembersToGroup(final String groupId, final Map<String, List<String>> membersToMerge,
            final Set<String> availableTopologyType, final int availableProvidedMembersCount);

    /**
     * Removes members from the existing group.
     *
     * @param groupId
     *     - group id
     * @param membersToRemove
     *     - members to remove from the group
     * @param availableTopologyType
     *     - set of all topology types present in a group
     */
    void removeProvidedMembersFromGroup(final String groupId, final Map<String, List<String>> membersToRemove,
            final Set<String> availableTopologyType);

    /**
     * Renames the existing group.
     *
     * @param groupId
     *     - group id
     * @param newGroupName
     *     - new group name
     * @throws GroupNotFoundException
     *     if group id doesn't exists
     */
    void renameGroupById(final String groupId, final String newGroupName) throws GroupNotFoundException;

    /**
     * Deletes the group by given id.
     *
     * @param groupId
     *     - group id
     * @throws GroupNotFoundException
     *     if group id doesn't exists
     */
    void deleteGroupById(final String groupId) throws GroupNotFoundException;

    /**
     * Gets all provided members of static group by the groupId.
     *
     * @param groupId
     *     - group id
     * @return a key value pair of topology type and provided member ids
     * @throws GroupNotFoundException
     *     if group id doesn't exist
     */
    StaticGroupRecord getProvidedMembersById(String groupId) throws GroupNotFoundException;

    /**
     * Retrieves all members of the specified static group that are present in the network for the given topology type
     *
     * @param tableName
     *     - table name where entity is stored
     * @param topologyType
     *     - topology type entity relates to
     * @param providedMembersIds
     *     - provided members ids to be matched on
     * @param idColumnName
     *     - id column name
     * @return a query to select present provided members
     */
    Select<Record2<String, String>> createQueryForPresentProvidedMembers(String tableName, String topologyType,
            List<String> providedMembersIds, String idColumnName);

    /**
     * Retrieves all members of the specified static group that are not present in the network for the given topology type
     *
     * @param tableName
     *     - table name where entity is stored
     * @param topologyType
     *     - topology type entity relates to
     * @param providedMembersIds
     *     - provided members ids to be matched on
     * @param idColumnName
     *     - id column name
     * @return a query to select non present provided members
     */
    Select<Record2<String, String>> createQueryForNotPresentProvidedMembers(String tableName, String topologyType,
            List<String> providedMembersIds, String idColumnName);

    /**
     * Retrieves all members of the specified static group that are invalid in the network for the given topology type
     *
     * @param topologyType
     *     - topology type entity relates to
     * @param groupId
     *     - group id
     * @return a query to select invalid provided members
     */
    Select<Record2<String, String>> createQueryForProvidedMembersByTopologyType(final String groupId,
            final String topologyType);
}
