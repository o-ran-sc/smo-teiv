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

import static org.oran.smo.teiv.groups.rest.controller.GroupsConstants.GROUP_ID_PREFIX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.oran.smo.teiv.groups.rest.controller.GroupsException;

@Slf4j
@Component
@RequiredArgsConstructor
public class StaticGroupMemberProcessor {

    @Value("${groups.static.provided-members-ids.insert-max-limit}")
    private int insertMaxLimit;

    private final ObjectMapper objectMapper;

    public Map<String, List<String>> groupProvidedMembers(final List<Object> providedMembersObject, final String groupId) {
        Map<String, List<String>> groupedProvidedMembers = new HashMap<>();
        JsonNode providedMembers;
        try {
            providedMembers = objectMapper.readValue(objectMapper.writeValueAsString(providedMembersObject),
                    JsonNode.class);
        } catch (final JsonProcessingException ex) {
            log.warn("Error while serializing the providedMembers for the static group: {}", groupId, ex);
            throw GroupsException.providedMembersSerializationException(ex.getMessage());
        }

        for (final JsonNode providedMember : providedMembers) {
            validateListItem(providedMember);
            final Map.Entry<String, JsonNode> topologyEntry = providedMember.fields().next();
            final String topologyType = topologyEntry.getKey();
            for (JsonNode topologyIdObject : topologyEntry.getValue()) {
                validateListItem(topologyIdObject);
                final Map.Entry<String, JsonNode> topologyIdEntry = topologyIdObject.fields().next();
                if (topologyIdEntry.getKey().equals("id") && topologyIdEntry.getValue().isTextual()) {
                    final String id = topologyIdEntry.getValue().asText();
                    groupIdCheck(id);
                    groupedProvidedMembers.computeIfAbsent(topologyType, v -> new ArrayList<>()).add(id);
                } else {
                    throw GroupsException.invalidProvidedMembers(String.format("Invalid key/value present in %s.",
                            topologyIdObject));
                }
            }
        }
        validateProvidedMembersSize(groupedProvidedMembers);
        return groupedProvidedMembers;
    }

    private void validateListItem(final JsonNode jsonNode) {
        if (!jsonNode.isObject()) {
            throw GroupsException.invalidProvidedMembers("List elements are not of the type object.");
        } else if (jsonNode.isEmpty()) {
            throw GroupsException.invalidProvidedMembers("Empty object present in providedMembers.");
        } else if (jsonNode.size() > 1) {
            throw GroupsException.invalidProvidedMembers(String.format("More than one key:value present in %s.", jsonNode));
        }
    }

    private void groupIdCheck(final String id) {
        if (id.startsWith(GROUP_ID_PREFIX)) {
            throw GroupsException.invalidProvidedMembers(String.format(
                    "Nested topology groups is not supported. Provided members contain topology group id: %s", id));
        }
    }

    private void validateProvidedMembersSize(final Map<String, List<String>> groupedProvidedMembers) {
        final int totalProvidedMembersIds = groupedProvidedMembers.values().stream().mapToInt(List::size).sum();
        if (totalProvidedMembersIds > insertMaxLimit) {
            throw GroupsException.providedMembersSingleRequestLimitViolationException(totalProvidedMembersIds,
                    insertMaxLimit);
        }
    }

}
