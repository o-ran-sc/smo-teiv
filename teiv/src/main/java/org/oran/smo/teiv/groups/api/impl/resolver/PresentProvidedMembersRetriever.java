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
package org.oran.smo.teiv.groups.api.impl.resolver;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Record2;
import org.jooq.Select;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import org.oran.smo.teiv.api.model.OranTeivMembersResponse;
import org.oran.smo.teiv.groups.spi.GroupNotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.oran.smo.teiv.exposure.utils.RequestDetails;
import org.oran.smo.teiv.groups.spi.GroupsRepository;
import org.oran.smo.teiv.groups.spi.StaticGroupRecord;
import org.oran.smo.teiv.schema.Persistable;

@Slf4j
@Component
@Profile("groups")
public class PresentProvidedMembersRetriever extends ProvidedMembersRetriever {
    private final GroupsRepository groupsRepository;

    public PresentProvidedMembersRetriever(final GroupsRepository groupsRepository) {
        super(groupsRepository);
        this.groupsRepository = groupsRepository;
    }

    @Override
    public OranTeivMembersResponse retrieve(final String groupId, final RequestDetails requestDetails)
            throws GroupNotFoundException {
        StaticGroupRecord staticGroupRecord = groupsRepository.getProvidedMembersById(groupId);
        List<Select<Record2<String, String>>> combinedQueries = new ArrayList<>();
        staticGroupRecord.getGroupedProvidedMembers().forEach((topologyType, providedMembersIds) -> {
            Persistable topologyObjectType = resolveTopologyObjectType(topologyType, groupId);
            if (topologyObjectType == null) {
                return;
            }
            combinedQueries.add(groupsRepository.createQueryForPresentProvidedMembers(topologyObjectType.getTableName(),
                    topologyType, providedMembersIds, topologyObjectType.getIdColumnName()));
        });
        return processCombinedQueries(combinedQueries, requestDetails);
    }
}
