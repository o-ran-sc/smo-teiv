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
package org.oran.smo.teiv.exposure.audit;

import java.util.List;
import java.util.Map;

import lombok.experimental.UtilityClass;
import org.oran.smo.teiv.api.model.OranTeivClassifier;
import org.oran.smo.teiv.api.model.OranTeivDecorator;
import org.springframework.http.HttpStatus;

@UtilityClass
public class AuditMapper {
    public AuditInfo<List<String>> fromClassifiersRequest(final OranTeivClassifier request, final HttpStatus status) {
        return new AuditInfo<>(request.getOperation().toString(), request.getClassifiers(), request.getEntityIds(), request
                .getRelationshipIds(), status);
    }

    public AuditInfo<Map<String, Object>> fromDecoratorsRequest(final OranTeivDecorator request, final HttpStatus status) {
        return new AuditInfo<>(request.getOperation().toString(), request.getDecorators(), request.getEntityIds(), request
                .getRelationshipIds(), status);
    }
}
