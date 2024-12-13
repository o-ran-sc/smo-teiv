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

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("groups")
public class CriteriaResolverRegistry {
    private final EntityByDomainResolver entityByDomainResolver;
    private final EntityByTypeResolver entityByTypeResolver;
    private final RelationshipByEntityIdResolver relationshipByEntityIdResolver;
    private final RelationshipByTypeResolver relationshipByTypeResolver;

    /**
     * Gets the group resolver based on the group type.
     *
     * @param queryType
     *     - group type
     * @return {@link GroupResolver}
     */
    public CriteriaResolver getResolver(final String queryType) {
        log.debug("Get criteria resolver for query type: {}", queryType);
        switch (queryType) {
            case "getEntitiesByDomain" -> {
                return this.entityByDomainResolver;
            }
            case "getEntitiesByType" -> {
                return this.entityByTypeResolver;
            }
            case "getRelationshipsForEntityId" -> {
                return this.relationshipByEntityIdResolver;
            }
            case "getRelationshipsByType" -> {
                return this.relationshipByTypeResolver;
            }
            default -> throw new IllegalArgumentException("Unsupported query type: " + queryType);
        }
    }
}