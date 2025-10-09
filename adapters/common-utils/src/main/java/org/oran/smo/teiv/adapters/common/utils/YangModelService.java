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

import org.oran.smo.yangtools.parser.model.statements.yang.YModule;
import org.oran.smo.yangtools.parser.simple.example.YangModelExtractor;

import static org.oran.smo.teiv.adapters.common.utils.Constants.OCUCPFUNCTION_PROVIDES_NRCELLCU;
import static org.oran.smo.teiv.adapters.common.utils.Constants.ODUFUNCTION_PROVIDES_NRCELLDU;
import static org.oran.smo.teiv.adapters.common.utils.Constants.ODUFUNCTION_O1LINK_SMO;
import static org.oran.smo.teiv.adapters.common.utils.Constants.OCUCPFUNCTION_O1LINK_SMO;
import static org.oran.smo.teiv.adapters.common.utils.Constants.MANAGEDELEMENT_MANAGES_OCUCPFUNCTION;
import static org.oran.smo.teiv.adapters.common.utils.Constants.MANAGEDELEMENT_MANAGES_ODUFUNCTION;
import static org.oran.smo.teiv.adapters.common.utils.Constants.SMO_TEIV_RAN_PREFIX;
import static org.oran.smo.teiv.adapters.common.utils.Constants.SMO_TEIV_REL_OAM_RAN_PREFIX;

public class YangModelService {

    /**
     * Parse the yang models, validate and extract the relationshipTypeName/EntityTypeName directly from the yan models.
     *
     * @param typeName
     *     the relationshipTypeName/EntityTypeName
     * @return validated typeName.
     */
    public static String validateAndExtractFromYangModel(String typeName) {
        if (typeName == null || typeName.isBlank()) {
            throw new IllegalArgumentException("Type name cannot be null or blank.");
        }

        String yangModelNameSpace = getYangModelNamespace(typeName);
        if (yangModelNameSpace == null || yangModelNameSpace.isBlank()) {
            throw new IllegalStateException("Failed to derive from YANG model for type: " + typeName);
        }

        YModule yModule = YangModelExtractor.parseAndExtractYModule(yangModelNameSpace);
        if (yModule == null) {
            throw new IllegalStateException("Unable to parse YANG module for namespace: " + yangModelNameSpace);
        }
        return YangModelExtractor.validateNameSpaceValue(yModule, typeName);
    }

    private static String getYangModelNamespace(String typeName) {
        switch (typeName) {
            case OCUCPFUNCTION_PROVIDES_NRCELLCU:
            case ODUFUNCTION_PROVIDES_NRCELLDU:
            case ODUFUNCTION_O1LINK_SMO:
            case OCUCPFUNCTION_O1LINK_SMO:
                return SMO_TEIV_RAN_PREFIX;

            case MANAGEDELEMENT_MANAGES_OCUCPFUNCTION:
            case MANAGEDELEMENT_MANAGES_ODUFUNCTION:
                return SMO_TEIV_REL_OAM_RAN_PREFIX;

            default:
                return null;
        }
    }
}
