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

import org.oran.smo.teiv.api.model.OranTeivCriteria;
import org.oran.smo.teiv.api.model.OranTeivEntitiesResponseMessage;
import org.oran.smo.teiv.api.model.OranTeivGetEntitiesByDomain;
import org.oran.smo.teiv.api.model.OranTeivMembersResponse;
import org.oran.smo.teiv.exposure.data.api.DataService;
import org.oran.smo.teiv.exposure.utils.RequestDetails;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("groups")
public class EntityByDomainResolver implements CriteriaResolver {
    private final DataService dataService;

    @Override
    public OranTeivMembersResponse resolveByCriteria(final OranTeivCriteria criteria, final RequestDetails requestDetails) {
        log.debug("Resolve group with getEntitiesByDomain criteria: {}", criteria);
        OranTeivGetEntitiesByDomain entitiesByDomainCriteria = (OranTeivGetEntitiesByDomain) criteria;
        final OranTeivEntitiesResponseMessage entitiesByDomain = dataService.getEntitiesByDomain(entitiesByDomainCriteria
                .getDomain(), entitiesByDomainCriteria.getTargetFilter(), entitiesByDomainCriteria.getScopeFilter(),
                requestDetails);

        return OranTeivMembersResponse.builder().items(entitiesByDomain.getItems()).first(entitiesByDomain.getFirst()).prev(
                entitiesByDomain.getPrev()).self(entitiesByDomain.getSelf()).next(entitiesByDomain.getNext()).last(
                        entitiesByDomain.getLast()).totalCount(entitiesByDomain.getTotalCount()).build();
    }
}
