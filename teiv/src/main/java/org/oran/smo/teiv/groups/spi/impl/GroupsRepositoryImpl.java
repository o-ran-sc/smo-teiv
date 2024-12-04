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
package org.oran.smo.teiv.groups.spi.impl;

import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.jooq.Query;
import org.jooq.Record4;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.SelectForUpdateStep;
import org.jooq.SelectJoinStep;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.Row2;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.oran.smo.teiv.groups.rest.controller.GroupsException;
import org.oran.smo.teiv.groups.spi.DynamicGroupRecord;
import org.oran.smo.teiv.groups.spi.GroupNotFoundException;
import org.oran.smo.teiv.groups.spi.GroupRecord;
import org.oran.smo.teiv.groups.spi.GroupsRepository;
import org.oran.smo.teiv.groups.spi.StaticGroupRecord;

@Slf4j
@Repository
@RequiredArgsConstructor
@Profile("groups")
public class GroupsRepositoryImpl implements GroupsRepository {
    @Value("${groups.static.provided-members-ids.max-limit}")
    private int maxLimit;
    private static final String TIES_GROUPS = "ties_groups.%s";
    private static final Table<Record> GROUPS_TABLE = table(String.format(TIES_GROUPS, "groups"));
    private static final Field<String> GROUP_ID = field(name("id"), String.class);
    private static final Field<String> GROUP_NAME = field(name("name"), String.class);
    private static final Field<String> GROUP_TYPE = field(name("type"), String.class);
    private static final Table<Record> DYNAMIC_GROUPS_TABLE = table(String.format(TIES_GROUPS, "dynamic_groups"));
    private static final Field<JSONB> CRITERIA = field(name("criteria"), JSONB.class);
    private static final Table<Record> STATIC_GROUPS_TABLE = table(String.format(TIES_GROUPS, "static_groups"));
    private static final Field<String> TOPOLOGY_TYPE = field(name("topology_type"), String.class);
    private static final Field<String[]> PROVIDED_MEMBERS_IDS = field(name("provided_members_ids"), String[].class);

    private final DSLContext readDataDslContext;
    private final DSLContext writeDataDslContext;

    @Override
    public GroupRecord getGroupById(String groupId) throws GroupNotFoundException {
        return Optional.ofNullable(readDataDslContext.select().from(GROUPS_TABLE).where(GROUP_ID.eq(groupId)).fetchOne(
                grpRecord -> GroupRecord.builder().groupType(grpRecord.getValue("type", String.class)).groupName(grpRecord
                        .getValue("name", String.class)).build())).orElseThrow(() -> {
                            log.warn("Group with ID: {} does not exist.", groupId);
                            return new GroupNotFoundException(groupId);
                        });
    }

    @Override
    @Cacheable("groupTypeByIdCache")
    public String getGroupTypeById(final String groupId) throws GroupNotFoundException {
        log.debug("Get group type for the group with id: {}", groupId);
        return Optional.ofNullable(readDataDslContext.select(GROUP_TYPE).from(GROUPS_TABLE).where(GROUP_ID.as("id").eq(
                groupId)).fetchOneInto(String.class)).orElseThrow(() -> {
                    log.warn("Group with ID: {} does not exist when trying to get the type.", groupId);
                    return new GroupNotFoundException(
                            "Group with ID: " + groupId + "doesn't exist when trying to get the type.");
                });
    }

    @Override
    public Pair<Set<String>, Integer> getStaticGroupDetailsById(String groupId) {
        Set<String> topologyType = new HashSet<>();
        int providedMembersCount = 0;
        Record2<String[], BigDecimal> result = readDataDslContext.select(function("array_agg", String[].class,
                TOPOLOGY_TYPE).as("topology_type"), sum(function("array_length", Integer.class, PROVIDED_MEMBERS_IDS, DSL
                        .inline(1))).as("provided_members_count")).from(STATIC_GROUPS_TABLE).where(GROUP_ID.eq(groupId))
                .fetchOne();
        if (result != null && result.get("topology_type") != null && result.get("provided_members_count") != null) {
            topologyType = Arrays.stream(result.get("topology_type", String[].class)).collect(Collectors.toSet());
            providedMembersCount = result.get("provided_members_count", Integer.class);
        }
        return Pair.of(topologyType, providedMembersCount);
    }

    @Override
    public String getCriteriaByGroupId(final String groupId) throws GroupNotFoundException {
        return Optional.ofNullable(readDataDslContext.select(CRITERIA).from(DYNAMIC_GROUPS_TABLE).where(GROUP_ID.eq(
                groupId)).fetchOneInto(String.class)).orElseThrow(() -> {
                    log.warn("Group with ID: {} does not exist when trying to get the criteria.", groupId);
                    return new GroupNotFoundException(groupId);
                });
    }

    @Override
    public Pair<List<GroupRecord>, Integer> getAllGroups(final int offset, final int limit) {
        log.debug("Fetch all groups");
        Select<Record4<String, String, String, Integer>> paginatedQuery = createBasicQuery(Optional.empty(), false, limit,
                offset);
        Select<Record4<String, String, String, Integer>> countQuery = createBasicQuery(Optional.empty(), true, limit,
                offset);
        List<Record4<String, String, String, Integer>> results = paginatedQuery.unionAll(countQuery).fetch();
        int totalCount = results.get(results.size() - 1).get("totalCount", Integer.class);
        List<GroupRecord> groupRecords = results.stream().limit((long) results.size() - 1).map(groupRecord -> groupRecord
                .into(GroupRecord.class)).toList();
        return Pair.of(groupRecords, totalCount);
    }

    @Override
    public Pair<List<GroupRecord>, Integer> getGroupsByName(final String name, final int offset, final int limit) {
        log.debug("Fetch groups by name: {}", name);
        Select<Record4<String, String, String, Integer>> paginatedQuery = createBasicQuery(Optional.of(name), false, limit,
                offset);
        Select<Record4<String, String, String, Integer>> countQuery = createBasicQuery(Optional.of(name), true, limit,
                offset);
        int totalCount = Optional.ofNullable(countQuery.fetchOne()).map(totalRecord -> totalRecord.get("totalCount",
                Integer.class)).orElse(0);
        List<GroupRecord> groupRecords = new ArrayList<>();
        if (totalCount > 0) {
            List<Record4<String, String, String, Integer>> results = paginatedQuery.unionAll(countQuery).fetch();
            groupRecords = results.stream().limit((long) results.size() - 1).map(groupRecord -> groupRecord.into(
                    GroupRecord.class)).toList();
        }
        return Pair.of(groupRecords, totalCount);
    }

    @Override
    public Pair<List<Record>, Integer> getStaticMembers(Select<Record2<String, String>> unionQuery, int offset, int limit) {
        Table<?> unionTable = unionQuery.asTable("combined_query");
        SelectForUpdateStep<Record3<String, String, Integer>> combinedQuery = readDataDslContext.select(unionTable.field(
                "id", String.class), unionTable.field("topology_type", String.class), count().over().as("total_count"))
                .from(unionTable).orderBy(unionTable.field("id", String.class)).limit(limit).offset(offset);
        Result<Record3<String, String, Integer>> result = readDataDslContext.fetch(combinedQuery);
        int totalCount = result.isEmpty() ? 0 : result.get(0).get("total_count", Integer.class);
        List<Record> records = new ArrayList<>(result);
        return Pair.of(records, totalCount);
    }

    @Override
    public Pair<List<Record>, Integer> getAllProvidedMembersByGroupId(final String groupId, final int offset,
            final int limit) {
        Select<Record3<String, String, Integer>> countQuery = readDataDslContext.select(DSL.val((String) null).as(
                "topology_type"), DSL.val((String) null).as("id"), DSL.count(DSL.field("unnested_id")).as("total_count"))
                .from(readDataDslContext.select(DSL.field("unnest(" + PROVIDED_MEMBERS_IDS.getName() + ")", String.class)
                        .as("unnested_id")).from(STATIC_GROUPS_TABLE).where(GROUP_ID.eq(groupId)).asTable(
                                "unnested_members"));
        Select<Record3<String, String, Integer>> paginatedQuery = readDataDslContext.select(TOPOLOGY_TYPE, field(
                "unnest(" + PROVIDED_MEMBERS_IDS.getName() + ")", String.class).as("id"), DSL.val((Integer) null).as(
                        "total_count")).from(STATIC_GROUPS_TABLE).where(GROUP_ID.eq(groupId)).orderBy(TOPOLOGY_TYPE, field(
                                "id")).limit(limit).offset(offset);
        Result<Record3<String, String, Integer>> result = countQuery.unionAll(paginatedQuery).fetch();
        int totalCount = result.isEmpty() ? 0 : result.get(0).get("total_count", Integer.class);
        List<Record> records = result.stream().skip(1).collect(Collectors.toList());
        return Pair.of(records, totalCount);
    }

    @Override
    public void createDynamicGroup(final DynamicGroupRecord groupRecord) {
        log.debug("Create a dynamic group with name {}", groupRecord.getGroupName());
        writeDataDslContext.transaction((Configuration trx) -> {
            trx.dsl().insertInto(GROUPS_TABLE).set(GROUP_ID, groupRecord.getId()).set(GROUP_NAME, groupRecord
                    .getGroupName()).set(GROUP_TYPE, groupRecord.getGroupType()).execute();
            trx.dsl().insertInto(DYNAMIC_GROUPS_TABLE).set(GROUP_ID, groupRecord.getId()).set(CRITERIA, groupRecord
                    .getCriteria()).execute();
        });
    }

    @Override
    public void createStaticGroup(final StaticGroupRecord groupRecord) {
        log.debug("Create a static group with name {}", groupRecord.getGroupName());
        writeDataDslContext.transaction((Configuration trx) -> {
            trx.dsl().insertInto(GROUPS_TABLE).set(GROUP_ID, groupRecord.getId()).set(GROUP_NAME, groupRecord
                    .getGroupName()).set(GROUP_TYPE, groupRecord.getGroupType()).execute();
            trx.dsl().batch(insertGroupedProvidedMembers(trx, groupRecord)).execute();
        });
    }

    @Override
    public void mergeProvidedMembersToGroup(final String groupId, final Map<String, List<String>> membersToMerge,
            final Set<String> availableTopologyType, final int availableProvidedMembersCount) {
        log.debug("Merging provided members to group with ID: {}", groupId);
        writeDataDslContext.transaction((Configuration trx) -> trx.dsl().batch(mergeGroupedProvidedMembers(trx, groupId,
                membersToMerge, availableTopologyType, availableProvidedMembersCount)).execute());

    }

    @Override
    public void removeProvidedMembersFromGroup(final String groupId, final Map<String, List<String>> membersToRemove,
            final Set<String> availableTopologyType) {
        log.debug("Removing members from group with ID: {}", groupId);
        writeDataDslContext.transaction((Configuration trx) -> trx.dsl().batch(removeGroupedProvidedMembers(trx, groupId,
                membersToRemove, availableTopologyType)).execute());
    }

    @Override
    public void renameGroupById(final String groupId, final String newGroupName) throws GroupNotFoundException {
        log.debug("Rename group with id: {} to {}", groupId, newGroupName);
        final int updatedRows = writeDataDslContext.update(GROUPS_TABLE).set(GROUP_NAME, newGroupName).where(GROUP_ID.eq(
                groupId)).execute();
        if (updatedRows == 0) {
            log.warn("Group with ID: {} does not exist when trying to rename the group.", groupId);
            throw new GroupNotFoundException(
                    "Group with ID: " + groupId + " does not exist when trying to rename the group.");
        }
    }

    @Override
    public void deleteGroupById(final String groupId) throws GroupNotFoundException {
        log.debug("Delete group with id: {}", groupId);
        final int deletedRows = writeDataDslContext.deleteFrom(GROUPS_TABLE).where(GROUP_ID.eq(groupId)).execute();
        if (deletedRows == 0) {
            log.warn("Group with ID: {} doesn't exist when trying to delete the group.", groupId);
            throw new GroupNotFoundException(
                    "Group with ID: " + groupId + "doesn't exist when tyring to delete the group.");
        }
    }

    @Override
    public StaticGroupRecord getProvidedMembersById(final String groupId) throws GroupNotFoundException {
        final List<Record2<String, String[]>> result = readDataDslContext.select(TOPOLOGY_TYPE, PROVIDED_MEMBERS_IDS).from(
                STATIC_GROUPS_TABLE).where(field(name("id")).eq(groupId)).fetch();
        if (result.isEmpty()) {
            log.warn("Group with ID: {} doesn't exist when trying to get the provided members.", groupId);
            throw new GroupNotFoundException(
                    "Group with ID: " + groupId + "doesn't exist when tyring to get the provided members.");
        }
        final Map<String, List<String>> groupedProvidedMembers = new HashMap<>();
        result.forEach(staticGroupRecord -> groupedProvidedMembers.put(staticGroupRecord.get(TOPOLOGY_TYPE), List.of(
                staticGroupRecord.get(PROVIDED_MEMBERS_IDS))));
        return StaticGroupRecord.builder().id(groupId).groupedProvidedMembers(groupedProvidedMembers).build();
    }

    @Override
    public List<String> getTopologyTypesByGroupId(String groupId) {
        return readDataDslContext.select(TOPOLOGY_TYPE).from(STATIC_GROUPS_TABLE).where(GROUP_ID.eq(groupId)).fetchInto(
                String.class);
    }

    @Override
    public Select<Record2<String, String>> createQueryForPresentProvidedMembers(String groupId, String tableName,
            String topologyType, String idColumnName) {
        return readDataDslContext.select(DSL.field(DSL.name(idColumnName), String.class).as("id"), DSL.inline(topologyType)
                .as("topology_type")).from(DSL.table(tableName)).where(DSL.field(DSL.name(idColumnName)).in(
                        readDataDslContext.select(DSL.field("unnest(" + PROVIDED_MEMBERS_IDS.getName() + ")", String.class)
                                .as("unnested_id")).from(STATIC_GROUPS_TABLE).where(GROUP_ID.eq(groupId)).and(TOPOLOGY_TYPE
                                        .eq(topologyType))));
    }

    @Override
    public Select<Record2<String, String>> createQueryForNotPresentProvidedMembers(String tableName, String topologyType,
            List<String> providedMembersIds, String idColumnName) {
        Select<Record2<String, String>> providedIdsQuery = readDataDslContext.select(DSL.field("id", String.class), DSL
                .inline(topologyType).as("topology_type")).from(DSL.values(providedMembersIds.stream().map(id -> DSL.row(DSL
                        .inline(id), DSL.inline(topologyType))).toArray(Row2[]::new)).as("provided_ids", "id",
                                "topology_type"));
        Select<Record2<String, String>> existingIdsQuery = readDataDslContext.select(DSL.field(DSL.name(idColumnName),
                String.class).as("id"), DSL.inline(topologyType).as("topology_type")).from(DSL.table(tableName));
        return providedIdsQuery.except(existingIdsQuery);
    }

    @Override
    public Select<Record2<String, String>> createQueryForProvidedMembersByTopologyType(final String groupId,
            final String topologyType) {
        return readDataDslContext.select(DSL.val(topologyType).as("topology_type"), DSL.field(
                "unnest(" + PROVIDED_MEMBERS_IDS.getName() + ")", String.class).as("id")).from(STATIC_GROUPS_TABLE).where(
                        GROUP_ID.eq(groupId).and(TOPOLOGY_TYPE.eq(topologyType)));
    }

    /**
     * Task runs every 5 hours to clear the cache used by groups repository
     */
    @CacheEvict(value = { "groupTypeByIdCache" }, allEntries = true)
    @Scheduled(fixedRateString = "18000000")
    public void evictGroupRepositoryCache() {
        log.debug("Emptying group repository cache");
    }

    private Select<Record4<String, String, String, Integer>> createBasicQuery(Optional<String> groupName, boolean countMode,
            int limit, int offset) {
        SelectJoinStep<Record4<String, String, String, Integer>> baseQuery = readDataDslContext.select(GROUP_ID, GROUP_NAME
                .as("groupName"), GROUP_TYPE.as("groupType"), DSL.val((Integer) null).as("totalCount")).from(GROUPS_TABLE);
        groupName.ifPresent(s -> baseQuery.where(GROUP_NAME.eq(s)));
        if (countMode) {
            return readDataDslContext.select(DSL.val((String) null).as("id"), DSL.val((String) null).as("groupName"), DSL
                    .val((String) null).as("groupType"), DSL.selectCount().from(baseQuery.asTable()).asField("totalCount"));
        } else {
            return baseQuery.limit(limit).offset(offset);
        }
    }

    private List<Query> insertGroupedProvidedMembers(final Configuration trx, final StaticGroupRecord groupRecord) {
        List<Query> insertQuery = new ArrayList<>();
        groupRecord.getGroupedProvidedMembers().forEach((key, value) -> insertQuery.add(trx.dsl().insertInto(
                STATIC_GROUPS_TABLE).set(GROUP_ID, groupRecord.getId()).set(TOPOLOGY_TYPE, key).set(PROVIDED_MEMBERS_IDS,
                        value.toArray(new String[0]))));
        return insertQuery;
    }

    private List<Query> mergeGroupedProvidedMembers(final Configuration trx, final String groupId,
            final Map<String, List<String>> membersToMerge, final Set<String> availableTopologyType,
            final int availableProvidedMembersCount) {
        final int totalCountAfterMerge = availableProvidedMembersCount + membersToMerge.values().stream().mapToInt(
                List::size).sum();
        List<Query> mergeQueries = new ArrayList<>();
        if (totalCountAfterMerge <= maxLimit) {
            for (Map.Entry<String, List<String>> entry : membersToMerge.entrySet()) {
                final String[] providedMembersToMerge = entry.getValue().toArray(new String[0]);
                final String topologyType = entry.getKey();
                if (availableTopologyType.contains(topologyType)) {
                    Field<String[]> updatedProvidedMembers = field(
                            "array(SELECT unnest(\"provided_members_ids\") UNION SELECT unnest(?::text[]))", String[].class,
                            (Object) providedMembersToMerge);
                    mergeQueries.add(trx.dsl().update(STATIC_GROUPS_TABLE).set(PROVIDED_MEMBERS_IDS, updatedProvidedMembers)
                            .where(GROUP_ID.eq(groupId).and(TOPOLOGY_TYPE.eq(topologyType))));
                } else {
                    mergeQueries.add(trx.dsl().insertInto(STATIC_GROUPS_TABLE).set(GROUP_ID, groupId).set(TOPOLOGY_TYPE,
                            topologyType).set(PROVIDED_MEMBERS_IDS, providedMembersToMerge));
                }
            }
        } else {
            log.warn(
                    "Updating provided members for group with id: {} causing the total to exceed beyond the maximum allowed for a group which is: {}",
                    groupId, maxLimit);
            throw GroupsException.providedMembersMaxLimitViolationException(totalCountAfterMerge, maxLimit);
        }
        return mergeQueries;
    }

    private List<Query> removeGroupedProvidedMembers(final Configuration trx, final String groupId,
            final Map<String, List<String>> membersToRemove, final Set<String> availableTopologyType) {
        List<Query> removeQueries = new ArrayList<>();
        membersToRemove.forEach((topologyType, providedMembersToRemove) -> {
            if (availableTopologyType.contains(topologyType)) {
                final String[] providedMembersToMerge = providedMembersToRemove.toArray(new String[0]);
                final List<String> unavailableProvidedMember = readDataDslContext.select(field("unnest(array[?]::text[])",
                        String.class, (Object) providedMembersToMerge)).except(select(field(
                                "unnest(\"provided_members_ids\")", String.class)).from(STATIC_GROUPS_TABLE).where(GROUP_ID
                                        .eq(groupId)).and(TOPOLOGY_TYPE.eq(topologyType))).fetchInto(String.class);
                if (unavailableProvidedMember.isEmpty()) {
                    Field<String[]> updatedProvidedMembers = field(
                            "array(SELECT unnest(\"provided_members_ids\") EXCEPT SELECT unnest(?::text[]))",
                            String[].class, (Object) providedMembersToMerge);
                    removeQueries.add(trx.dsl().update(STATIC_GROUPS_TABLE).set(PROVIDED_MEMBERS_IDS,
                            updatedProvidedMembers).where(GROUP_ID.eq(groupId).and(TOPOLOGY_TYPE.eq(topologyType))));
                } else {
                    log.warn(
                            "Updating provided members for group with id: {} failed due to unavailable provided member {} provided.",
                            groupId, unavailableProvidedMember);
                    throw GroupsException.providedMembersUpdateException(String.format(
                            "The specified provided members topology identifier: %s is not part of the group.",
                            unavailableProvidedMember));
                }
            } else {
                log.warn(
                        "Updating provided members for group with id: {} failed due to unavailable topology entity/relation {} provided.",
                        groupId, topologyType);
                throw GroupsException.providedMembersUpdateException(String.format(
                        "The specified topology entity/relation: %s is not part of the group.", topologyType));
            }
        });
        removeQueries.add(trx.dsl().deleteFrom(STATIC_GROUPS_TABLE).where(GROUP_ID.eq(groupId)).and(field(
                "array_length(\"provided_members_ids\", 1)").isNull()));
        return removeQueries;
    }

}
