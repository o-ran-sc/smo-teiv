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
package org.oran.smo.teiv.groups.api.impl.creator;

import static org.oran.smo.teiv.groups.rest.controller.GroupsConstants.MEMBERS_HREF_TEMPLATE;
import static org.oran.smo.teiv.groups.rest.controller.GroupsConstants.RESOURCE_NOT_FOUND;
import static org.oran.smo.teiv.groups.rest.controller.GroupsUtil.generateMembersHref;

import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.exposure.utils.RequestDetails;
import org.oran.smo.teiv.groups.api.impl.resolver.CriteriaResolverRegistry;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.oran.smo.teiv.api.model.OranTeivCreateGroupPayload;
import org.oran.smo.teiv.api.model.OranTeivDynamic;
import org.oran.smo.teiv.api.model.OranTeivDynamicEnum;
import org.oran.smo.teiv.api.model.OranTeivDynamicGroupByIdResponse;
import org.oran.smo.teiv.api.model.OranTeivGroupByIdResponse;
import org.oran.smo.teiv.groups.rest.controller.GroupsException;
import org.oran.smo.teiv.groups.spi.DynamicGroupRecord;
import org.oran.smo.teiv.groups.spi.GroupsRepository;
import org.oran.smo.teiv.utils.JooqTypeConverter;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("groups")
public class DynamicGroupCreator implements GroupCreator {
    private final ObjectMapper objectMapper;
    private final GroupsRepository groupsRepository;
    private final CriteriaResolverRegistry criteriaResolverRegistry;

    @Override
    public OranTeivGroupByIdResponse create(final OranTeivCreateGroupPayload createGroupPayload) {
        log.debug("Create a dynamic group: {}", createGroupPayload.toString());
        final OranTeivDynamic dynamicGroup = (OranTeivDynamic) createGroupPayload;
        String criteriaString;
        try {
            criteriaString = objectMapper.writeValueAsString(dynamicGroup.getCriteria());
        } catch (JsonProcessingException e) {
            log.warn("Error while serializing the criteria for the dynamic group: {}", dynamicGroup, e);
            throw GroupsException.criteriaSerializationException(e.getMessage());
        }
        validateGroup(dynamicGroup);
        final String groupId = GroupCreator.generateGroupId();
        final DynamicGroupRecord groupRecord = DynamicGroupRecord.builder().id(groupId).groupName(dynamicGroup.getName())
                .groupType(dynamicGroup.getType()).criteria(JooqTypeConverter.toJsonb(criteriaString)).build();

        groupsRepository.createDynamicGroup(groupRecord);

        return OranTeivDynamicGroupByIdResponse.builder().id(groupId).name(dynamicGroup.getName()).type(
                OranTeivDynamicEnum.DYNAMIC).members(generateMembersHref(groupId)).criteria(dynamicGroup.getCriteria())
                .build();
    }

    private void validateGroup(OranTeivDynamic dynamicGroup) {
        try {
            criteriaResolverRegistry.getResolver(dynamicGroup.getCriteria().getQueryType()).resolveByCriteria(dynamicGroup
                    .getCriteria(), RequestDetails.builder().basePath(String.format(MEMBERS_HREF_TEMPLATE, "tempGroup"))
                            .limit(1).build());
        } catch (TeivException ex) {
            if (!(dynamicGroup.getCriteria().getQueryType().equals("getRelationshipsForEntityId") && ex
                    .getLocalizedMessage().equals(RESOURCE_NOT_FOUND))) {
                throw ex;
            }
        }
    }
}
