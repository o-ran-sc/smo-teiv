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
package org.oran.smo.teiv.groups.api.impl;

import static org.oran.smo.teiv.exposure.utils.PaginationUtil.firstHref;
import static org.oran.smo.teiv.exposure.utils.PaginationUtil.lastHref;
import static org.oran.smo.teiv.exposure.utils.PaginationUtil.nextHref;
import static org.oran.smo.teiv.exposure.utils.PaginationUtil.prevHref;
import static org.oran.smo.teiv.exposure.utils.PaginationUtil.selfHref;
import static org.oran.smo.teiv.groups.rest.controller.GroupsUtil.generateMembersHref;
import static org.oran.smo.teiv.groups.rest.controller.GroupsUtil.generateProvidedMembersHref;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.oran.smo.teiv.api.model.OranTeivCreateGroupPayload;
import org.oran.smo.teiv.api.model.OranTeivCriteria;
import org.oran.smo.teiv.api.model.OranTeivDynamicEnum;
import org.oran.smo.teiv.api.model.OranTeivDynamicGroupByIdResponse;
import org.oran.smo.teiv.api.model.OranTeivDynamicGroupResponse;
import org.oran.smo.teiv.api.model.OranTeivGroupByIdResponse;
import org.oran.smo.teiv.api.model.OranTeivGroupResponse;
import org.oran.smo.teiv.api.model.OranTeivGroupsResponse;
import org.oran.smo.teiv.api.model.OranTeivMembersResponse;
import org.oran.smo.teiv.api.model.OranTeivStaticEnum;
import org.oran.smo.teiv.api.model.OranTeivStaticGroupByIdResponse;
import org.oran.smo.teiv.api.model.OranTeivStaticGroupResponse;
import org.oran.smo.teiv.api.model.OranTeivUpdateGroupNamePayload;
import org.oran.smo.teiv.api.model.OranTeivUpdateProvidedMembersPayload;
import org.oran.smo.teiv.exposure.utils.RequestDetails;
import org.oran.smo.teiv.groups.api.GroupsService;
import org.oran.smo.teiv.groups.api.impl.creator.DynamicGroupCreator;
import org.oran.smo.teiv.groups.api.impl.creator.StaticGroupCreator;
import org.oran.smo.teiv.groups.api.impl.resolver.DynamicGroupResolver;
import org.oran.smo.teiv.groups.api.impl.resolver.GroupResolver;
import org.oran.smo.teiv.groups.api.impl.resolver.ProvidedMembersRetriever;
import org.oran.smo.teiv.groups.api.impl.resolver.ProvidedMembersRetrieverRegistry;
import org.oran.smo.teiv.groups.api.impl.resolver.StaticGroupResolver;
import org.oran.smo.teiv.groups.rest.controller.GroupsException;
import org.oran.smo.teiv.groups.spi.GroupNotFoundException;
import org.oran.smo.teiv.groups.spi.GroupRecord;
import org.oran.smo.teiv.groups.spi.GroupsRepository;
import org.oran.smo.teiv.groups.spi.ProvidedMembersStatus;
import org.oran.smo.teiv.groups.spi.StaticGroupMemberProcessor;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("groups")
public class GroupsServiceImpl implements GroupsService {
    private final GroupsRepository groupsRepository;
    private final DynamicGroupCreator dynamicGroupCreator;
    private final StaticGroupCreator staticGroupCreator;
    private final StaticGroupResolver staticGroupResolver;
    private final DynamicGroupResolver dynamicGroupResolver;
    private final ObjectMapper objectMapper;
    private final StaticGroupMemberProcessor staticGroupMemberProcessor;
    private final ProvidedMembersRetrieverRegistry providedMembersRetrieverRegistry;

    @Override
    public OranTeivGroupByIdResponse createGroup(final OranTeivCreateGroupPayload createGroupPayload) {
        if (createGroupPayload.getType().equals("dynamic")) {
            return dynamicGroupCreator.create(createGroupPayload);
        } else {
            return staticGroupCreator.create(createGroupPayload);
        }
    }

    @Override
    public OranTeivGroupsResponse getAllGroups(final RequestDetails requestDetails, final String name) {
        Pair<List<GroupRecord>, Integer> results;
        if (name != null) {
            results = groupsRepository.getGroupsByName(name, requestDetails.getOffset(), requestDetails.getLimit());
        } else {
            results = groupsRepository.getAllGroups(requestDetails.getOffset(), requestDetails.getLimit());
        }
        int totalCount = results.getRight();
        List<OranTeivGroupResponse> items = new ArrayList<>();
        if (!results.getLeft().isEmpty()) {
            items = results.getLeft().stream().map(this::mapToGroupResponse).toList();
        }
        return OranTeivGroupsResponse.builder().items(items).self(selfHref(requestDetails)).first(firstHref(requestDetails))
                .prev(prevHref(requestDetails, totalCount)).next(nextHref(requestDetails, totalCount)).last(lastHref(
                        requestDetails, totalCount)).totalCount(totalCount).build();
    }

    @Override
    public void deleteGroup(final String groupId) {
        try {
            groupsRepository.deleteGroupById(groupId);
        } catch (final GroupNotFoundException e) {
            log.warn("Error while deleting the group: ", e);
            throw GroupsException.resourceNotFoundException();
        }
    }

    @Override
    public void renameGroup(final String groupId, final OranTeivUpdateGroupNamePayload newGroupName) {
        try {
            groupsRepository.renameGroupById(groupId, newGroupName.getName());
        } catch (final GroupNotFoundException e) {
            log.warn("Error while updating the group name: ", e);
            throw GroupsException.resourceNotFoundException();
        }
    }

    @Override
    public OranTeivGroupByIdResponse getGroupById(final String groupId) {
        try {
            final GroupRecord grpRecord = groupsRepository.getGroupById(groupId);
            if (grpRecord.getGroupType().equals("dynamic")) {
                OranTeivCriteria groupCriteria = objectMapper.readValue(groupsRepository.getCriteriaByGroupId(groupId),
                        OranTeivCriteria.class);
                return OranTeivDynamicGroupByIdResponse.builder().id(groupId).name(grpRecord.getGroupName()).type(
                        OranTeivDynamicEnum.DYNAMIC).members(generateMembersHref(groupId)).criteria(groupCriteria).build();
            } else {
                return OranTeivStaticGroupByIdResponse.builder().id(groupId).name(grpRecord.getGroupName()).type(
                        OranTeivStaticEnum.STATIC).members(generateMembersHref(groupId)).providedMembers(
                                generateProvidedMembersHref(groupId)).build();
            }
        } catch (final GroupNotFoundException e) {
            log.warn("Error while fetching the group: ", e);
            throw GroupsException.resourceNotFoundException();
        } catch (final JsonProcessingException ex) {
            log.warn("Error while deserializing the criteria for the dynamic group: {} ", groupId, ex);
            throw GroupsException.criteriaDeserializationException(ex.getMessage());
        }
    }

    @Override
    public OranTeivMembersResponse getResolvedMembers(final String groupId, final RequestDetails requestDetails) {
        try {
            final String type = groupsRepository.getGroupTypeById(groupId);
            return getGroupResolver(type).resolve(groupId, requestDetails);
        } catch (final GroupNotFoundException e) {
            log.warn("Error while getting members of the group: ", e);
            throw GroupsException.resourceNotFoundException();
        }
    }

    @Override
    public void updateProvidedMembers(final String groupId,
            final OranTeivUpdateProvidedMembersPayload updateProvidedMembersPayload) {
        final String type;
        try {
            type = groupsRepository.getGroupTypeById(groupId);
        } catch (final GroupNotFoundException e) {
            log.warn("Error while updating provided members of static group: ", e);
            throw GroupsException.resourceNotFoundException();
        }
        if (type.equals("static")) {
            final Pair<Set<String>, Integer> groupDetails = groupsRepository.getStaticGroupDetailsById(groupId);
            final Map<String, List<String>> providedMembers = staticGroupMemberProcessor.groupProvidedMembers(
                    updateProvidedMembersPayload.getProvidedMembers(), groupId);
            if (updateProvidedMembersPayload.getOperation().equals(
                    OranTeivUpdateProvidedMembersPayload.OperationEnum.MERGE)) {
                groupsRepository.mergeProvidedMembersToGroup(groupId, providedMembers, groupDetails.getLeft(), groupDetails
                        .getRight());
            } else {
                groupsRepository.removeProvidedMembersFromGroup(groupId, providedMembers, groupDetails.getLeft());
            }
        } else {
            log.warn("Error when updating provided members for a group with id: {} as group type is not static", groupId);
            throw GroupsException.providedMembersUpdateException(
                    "The specified provided members for group cannot be updated as group type is not static");
        }
    }

    @Override
    public OranTeivMembersResponse getProvidedMembersByStatus(final String groupId, final String status,
            final RequestDetails requestDetails) {
        try {
            final ProvidedMembersStatus providedMembersStatus = ProvidedMembersStatus.fromValue(status);
            final String type = groupsRepository.getGroupTypeById(groupId);
            if (!type.equals("static")) {
                log.warn("Error retrieving provided members for a group with id: {} as group type is not static", groupId);
                throw GroupsException.providedMembersRetrievalException(
                        "Provided members can be retrieved only for static group");
            }
            ProvidedMembersRetriever retriever = providedMembersRetrieverRegistry.getProvidedMembersByStatus(
                    providedMembersStatus);
            return retriever.retrieve(groupId, requestDetails);
        } catch (GroupNotFoundException e) {
            log.warn("Error while getting provided members of the group: ", e);
            throw GroupsException.resourceNotFoundException();
        }
    }

    private GroupResolver getGroupResolver(final String type) {
        return type.equals("dynamic") ? dynamicGroupResolver : staticGroupResolver;
    }

    private OranTeivGroupResponse mapToGroupResponse(GroupRecord groupRecord) {
        if ("static".equals(groupRecord.getGroupType())) {
            return OranTeivStaticGroupResponse.builder().id(groupRecord.getId()).name(groupRecord.getGroupName()).type(
                    OranTeivStaticEnum.STATIC).providedMembers(generateProvidedMembersHref(groupRecord.getId())).members(
                            generateMembersHref(groupRecord.getId())).build();
        } else {
            return OranTeivDynamicGroupResponse.builder().id(groupRecord.getId()).name(groupRecord.getGroupName()).type(
                    OranTeivDynamicEnum.DYNAMIC).members(generateMembersHref(groupRecord.getId())).build();
        }
    }
}
