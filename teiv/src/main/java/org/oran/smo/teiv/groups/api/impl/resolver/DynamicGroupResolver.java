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

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.oran.smo.teiv.api.model.OranTeivCriteria;
import org.oran.smo.teiv.api.model.OranTeivMembersResponse;
import org.oran.smo.teiv.exposure.utils.RequestDetails;
import org.oran.smo.teiv.groups.rest.controller.GroupsException;
import org.oran.smo.teiv.groups.spi.GroupNotFoundException;
import org.oran.smo.teiv.groups.spi.GroupsRepository;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("groups")
public class DynamicGroupResolver implements GroupResolver {
    private final GroupsRepository groupsRepository;
    private final ObjectMapper objectMapper;
    private final CriteriaResolverRegistry criteriaResolverRegistry;

    @Override
    public OranTeivMembersResponse resolve(final String groupId, final RequestDetails requestDetails)
            throws GroupNotFoundException {
        try {
            final String criteriaString = this.groupsRepository.getCriteriaByGroupId(groupId);
            final OranTeivCriteria criteria = objectMapper.readValue(criteriaString, OranTeivCriteria.class);
            return criteriaResolverRegistry.getResolver(criteria.getQueryType()).resolveByCriteria(criteria,
                    requestDetails);
        } catch (final JsonProcessingException e) {
            log.warn("Error while deserializing the criteria for the dynamic group: {} ", groupId, e);
            throw GroupsException.criteriaDeserializationException(e.getMessage());
        }
    }

}
