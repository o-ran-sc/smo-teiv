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

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class AuditInfo<T> {
    private final String operation;
    private final T consumerData;
    private final List<String> entityIds;
    private final List<String> relationshipIds;
    private final HttpStatus status;

    @Override
    public String toString() {
        return String.format("%s %s=%s for entities=%s and relationships=%s (%s)", StringUtils.capitalize(operation),
                consumerData instanceof List<?> ? "classifiers" : "decorators", consumerData, entityIds, relationshipIds,
                status.equals(HttpStatus.NO_CONTENT) ? "successful" : "failed");
    }
}