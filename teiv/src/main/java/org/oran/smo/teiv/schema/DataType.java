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
package org.oran.smo.teiv.schema;

import java.util.Locale;

public enum DataType {
    PRIMITIVE,
    DECIMAL,
    INTEGER,
    BIGINT,
    CONTAINER,
    GEOGRAPHIC,
    BYTEA,
    RELIABILITY,
    TIMESTAMPTZ;

    public static DataType fromDbDataType(final String dbDatatype) {
        return switch (dbDatatype.toUpperCase(Locale.US)) {
            case "TEXT", "VARCHAR" -> PRIMITIVE;
            case "NUMERIC", "DECIMAL" -> DECIMAL;
            case "INT4" -> INTEGER;
            case "BIGINT", "INT8" -> BIGINT;
            case "JSONB" -> CONTAINER;
            case "GEOGRAPHY" -> GEOGRAPHIC;
            case "BYTEA" -> BYTEA;
            case "RELIABILITY" -> RELIABILITY;
            case "TIMESTAMPTZ" -> TIMESTAMPTZ;
            default -> throw new IllegalStateException("Unexpected value: " + dbDatatype.toUpperCase(Locale.US));
        };
    }
}
