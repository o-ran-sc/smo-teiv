/*
 *  ============LICENSE_START=======================================================
 *  Modifications Copyright (C) 2025 OpenInfra Foundation Europe
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
package org.oran.smo.teiv.adapters.common.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.oran.smo.yangtools.parser.model.statements.yang.YModule;
import org.oran.smo.yangtools.parser.simple.example.YangModelExtractor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.oran.smo.teiv.adapters.common.utils.Constants.MANAGEDELEMENT_MANAGES_OCUCPFUNCTION;
import static org.oran.smo.teiv.adapters.common.utils.Constants.SMO_TEIV_REL_OAM_RAN_PREFIX;
import static org.oran.smo.teiv.adapters.common.utils.Constants.OCUCPFUNCTION_PROVIDES_NRCELLCU;
import static org.oran.smo.teiv.adapters.common.utils.Constants.SMO_TEIV_RAN_PREFIX;

public class YangModelServiceTest {

    private MockedStatic<YangModelExtractor> yangModelExtractorMock;

    @BeforeEach
    void setup() {
        // Mock static methods in YangModelExtractor
        yangModelExtractorMock = mockStatic(YangModelExtractor.class);
    }

    @Test
    void testValidateAndExtractFromYangModel_withRanPrefix() {
        // Arrange
        String typeName = OCUCPFUNCTION_PROVIDES_NRCELLCU;
        YModule yModuleMock = mock(YModule.class);

        yangModelExtractorMock.when(() -> YangModelExtractor.parseAndExtractYModule(SMO_TEIV_RAN_PREFIX)).thenReturn(
                yModuleMock);

        yangModelExtractorMock.when(() -> YangModelExtractor.validateNameSpaceValue(yModuleMock, typeName)).thenReturn(
                typeName);

        // Act
        String result = YangModelService.validateAndExtractFromYangModel(typeName);

        // Assert
        assertEquals(typeName, result);
        yangModelExtractorMock.verify(() -> YangModelExtractor.parseAndExtractYModule(SMO_TEIV_RAN_PREFIX));
        yangModelExtractorMock.verify(() -> YangModelExtractor.validateNameSpaceValue(yModuleMock, typeName));
    }

    @Test
    void testValidateAndExtractFromYangModel_withOamRanPrefix() {
        String typeName = MANAGEDELEMENT_MANAGES_OCUCPFUNCTION;
        YModule yModuleMock = mock(YModule.class);

        yangModelExtractorMock.when(() -> YangModelExtractor.parseAndExtractYModule(SMO_TEIV_REL_OAM_RAN_PREFIX))
                .thenReturn(yModuleMock);

        yangModelExtractorMock.when(() -> YangModelExtractor.validateNameSpaceValue(yModuleMock, typeName)).thenReturn(
                typeName);

        String result = YangModelService.validateAndExtractFromYangModel(typeName);

        assertEquals(typeName, result);
    }

    @AfterEach
    void teardown() {
        yangModelExtractorMock.close();
    }

}
