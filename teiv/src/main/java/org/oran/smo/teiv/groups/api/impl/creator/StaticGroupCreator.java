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

import static org.oran.smo.teiv.groups.rest.controller.GroupsUtil.generateMembersHref;
import static org.oran.smo.teiv.groups.rest.controller.GroupsUtil.generateProvidedMembersHref;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.oran.smo.teiv.api.model.OranTeivCreateGroupPayload;
import org.oran.smo.teiv.api.model.OranTeivGroupByIdResponse;
import org.oran.smo.teiv.api.model.OranTeivStatic;
import org.oran.smo.teiv.api.model.OranTeivStaticEnum;
import org.oran.smo.teiv.api.model.OranTeivStaticGroupByIdResponse;
import org.oran.smo.teiv.groups.spi.GroupsRepository;
import org.oran.smo.teiv.groups.spi.StaticGroupRecord;
import org.oran.smo.teiv.groups.spi.StaticGroupMemberProcessor;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("groups")
public class StaticGroupCreator implements GroupCreator {

    private final GroupsRepository groupsRepository;
    private final StaticGroupMemberProcessor staticGroupMemberProcessor;

    @Override
    public OranTeivGroupByIdResponse create(final OranTeivCreateGroupPayload createGroupPayload) {
        log.debug("Create a static group: {}", createGroupPayload.toString());
        final OranTeivStatic staticGroup = (OranTeivStatic) createGroupPayload;

        final String groupId = GroupCreator.generateGroupId();
        final StaticGroupRecord staticGroupRecord = StaticGroupRecord.builder().id(groupId).groupName(staticGroup.getName())
                .groupType(staticGroup.getType()).groupedProvidedMembers(staticGroupMemberProcessor.groupProvidedMembers(
                        staticGroup.getProvidedMembers(), groupId)).build();

        groupsRepository.createStaticGroup(staticGroupRecord);

        return OranTeivStaticGroupByIdResponse.builder().id(groupId).name(staticGroup.getName()).type(
                OranTeivStaticEnum.STATIC).providedMembers(generateProvidedMembersHref(groupId)).members(
                        generateMembersHref(groupId)).build();
    }

}
