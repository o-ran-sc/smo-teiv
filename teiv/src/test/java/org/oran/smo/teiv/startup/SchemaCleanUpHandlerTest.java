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
package org.oran.smo.teiv.startup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.oran.smo.teiv.exposure.spi.ModelRepository;
import org.oran.smo.teiv.exposure.spi.Module;
import org.oran.smo.teiv.service.SchemaCleanUpService;

import static org.mockito.Mockito.*;

import java.util.List;

class SchemaCleanUpHandlerTest {

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private SchemaCleanUpService schemaCleanUpService;

    @InjectMocks
    private SchemaCleanUpHandler schemaCleanUpHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCleanUpSchema() {
        Module module = Module.builder().name("TestModule").build();
        when(modelRepository.getDeletingModulesOnStartup()).thenReturn(List.of(module));

        schemaCleanUpHandler.cleanUpSchema();

        verify(schemaCleanUpService).cleanUpModule("TestModule");
    }
}
