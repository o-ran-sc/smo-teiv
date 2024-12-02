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

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.oran.smo.teiv.api.model.OranTeivCreateGroupPayload;
import org.oran.smo.teiv.api.model.OranTeivDynamic;
import org.oran.smo.teiv.api.model.OranTeivGetEntitiesByDomain;
import org.oran.smo.teiv.groups.rest.controller.GroupsException;
import org.oran.smo.teiv.groups.spi.impl.GroupsRepositoryImpl;

@ExtendWith(MockitoExtension.class)
class DynamicGroupCreatorTest {
    @InjectMocks
    DynamicGroupCreator dynamicGroupCreator;

    @Mock
    GroupsRepositoryImpl groupsRepository;
    @Mock
    ObjectMapper objectMapper;

    @Test
    void createGroupThrowsJsonException() throws JsonProcessingException {
        //given
        final OranTeivCreateGroupPayload dynamicGroupPayload = OranTeivDynamic.builder().name("testGroup").type("dynamic")
                .criteria(OranTeivGetEntitiesByDomain.builder().queryType("getEntitiesByDomain").domain("RAN").build())
                .build();
        //when
        when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Error") {
        });
        //then
        assertThrows(GroupsException.class, () -> dynamicGroupCreator.create(dynamicGroupPayload));
    }
}
