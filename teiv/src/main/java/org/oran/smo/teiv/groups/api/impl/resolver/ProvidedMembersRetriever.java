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

import static org.oran.smo.teiv.exposure.utils.PaginationUtil.firstHref;
import static org.oran.smo.teiv.exposure.utils.PaginationUtil.lastHref;
import static org.oran.smo.teiv.exposure.utils.PaginationUtil.nextHref;
import static org.oran.smo.teiv.exposure.utils.PaginationUtil.prevHref;
import static org.oran.smo.teiv.exposure.utils.PaginationUtil.selfHref;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Select;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.oran.smo.teiv.groups.spi.GroupsRepository;
import org.oran.smo.teiv.api.model.OranTeivMembersResponse;
import org.oran.smo.teiv.exposure.utils.RequestDetails;
import org.oran.smo.teiv.groups.spi.GroupNotFoundException;
import org.oran.smo.teiv.schema.Persistable;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.schema.SchemaRegistryException;

@Slf4j
@RequiredArgsConstructor
public abstract class ProvidedMembersRetriever {

    private final GroupsRepository groupsRepository;

    /**
     * Retrieves provided members of the group.
     *
     * @param groupId
     *     - group id
     * @param requestDetails
     *     - request details used for pagination link construction
     * @return provided members of the group
     */
    public abstract OranTeivMembersResponse retrieve(final String groupId, final RequestDetails requestDetails)
            throws GroupNotFoundException;

    protected final Persistable resolveTopologyObjectType(final String topologyType, final String groupId) {
        final String[] parts = topologyType.split(":");
        if (parts.length != 2) {
            log.info(
                    "Invalid topology type: {} for groupId: {}. The topology object is neither an entity nor a relationship.",
                    topologyType, groupId);
            return null;
        }
        final String moduleName = parts[0];
        final String topologyTypeName = parts[1];
        try {
            return SchemaRegistry.getTopologyTypeByModuleAndTopologyName(moduleName, topologyTypeName);
        } catch (SchemaRegistryException e) {
            log.info(
                    "Failed to resolve topology type: {} for groupId: {}. The topology object is neither an entity nor a relationship.",
                    topologyType, groupId, e);
            return null;
        }
    }

    protected final OranTeivMembersResponse processCombinedQueries(
            final List<Select<Record2<String, String>>> combinedQueries, final RequestDetails requestDetails) {
        if (combinedQueries.isEmpty()) {
            return buildResponse(Collections.emptyList(), 0, requestDetails);
        }
        Select<Record2<String, String>> unionQuery = combinedQueries.stream().reduce(Select::unionAll).orElse(null);
        Pair<List<Record>, Integer> result = groupsRepository.getStaticMembers(unionQuery, requestDetails.getOffset(),
                requestDetails.getLimit());
        return buildResponse(result.getLeft(), result.getRight(), requestDetails);
    }

    protected final OranTeivMembersResponse buildResponse(final List<Record> memberRecords, final int totalCount,
            final RequestDetails requestDetails) {
        List<Object> items = new ArrayList<>();
        memberRecords.forEach(staticGroupRecord -> {
            String id = staticGroupRecord.getValue("id", String.class);
            String topologyType = staticGroupRecord.getValue("topology_type", String.class);
            items.add(Map.of(topologyType, List.of(Map.of("id", id))));
        });

        return OranTeivMembersResponse.builder().totalCount(totalCount).items(items).first(firstHref(requestDetails)).prev(
                prevHref(requestDetails, totalCount)).self(selfHref(requestDetails)).next(nextHref(requestDetails,
                        totalCount)).last(lastHref(requestDetails, totalCount)).build();
    }
}
