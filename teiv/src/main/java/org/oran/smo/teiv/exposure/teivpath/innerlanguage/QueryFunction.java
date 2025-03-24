/*
 *  ============LICENSE_START=======================================================
 *  Copyright (C) 2024 Ericsson
 *  Modifications Copyright (C) 2024-2025 OpenInfra Foundation Europe
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
package org.oran.smo.teiv.exposure.teivpath.innerlanguage;

import lombok.Getter;
import org.oran.smo.teiv.utils.query.exception.TeivPathException;

@Getter
public enum QueryFunction {
    EQ("="),
    LESS_THAN("<"),
    LESS_THAN_OR_EQ("<="),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQ(">="),
    CONTAINS("contains"),
    NOT_NULL("notNull"),
    WITHIN_METERS("withinMeters"),
    COVERED_BY("coveredBy");

    private final String value;

    QueryFunction(String value) {
        this.value = value;
    }

    public static QueryFunction fromValue(String value) {
        for (QueryFunction queryFunction : QueryFunction.values()) {
            if (queryFunction.getValue().equals(value)) {
                return queryFunction;
            }
        }
        throw TeivPathException.grammarError("Unsupported operation: " + value);
    }
}
