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

import static org.oran.smo.teiv.groups.rest.controller.GroupsConstants.MEMBERS_HREF_TEMPLATE;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.oran.smo.teiv.api.model.OranTeivCriteria;
import org.oran.smo.teiv.exposure.utils.RequestDetails;
import org.oran.smo.teiv.groups.rest.controller.GroupsException;
import org.oran.smo.teiv.groups.spi.GroupNotFoundException;
import org.oran.smo.teiv.groups.spi.GroupsRepository;

@ExtendWith(MockitoExtension.class)
class DynamicGroupResolverTest {
    @InjectMocks
    DynamicGroupResolver dynamicGroupResolver;

    @Mock
    GroupsRepository groupsRepository;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    CriteriaResolverRegistry criteriaResolverRegistry;

    @Test
    void testResolveDynamicGroupsThrowsException() throws GroupNotFoundException, JsonProcessingException {
        //given
        when(groupsRepository.getCriteriaByGroupId(anyString())).thenReturn("{}");
        when(objectMapper.readValue(anyString(), eq(OranTeivCriteria.class))).thenThrow(new JsonProcessingException(
                "Error") {
        });
        //then
        assertThrows(GroupsException.criteriaDeserializationException(anyString()).getClass(), () -> dynamicGroupResolver
                .resolve("testGroup", RequestDetails.builder().basePath(String.format(MEMBERS_HREF_TEMPLATE, "testGroup"))
                        .build()));
    }
}
