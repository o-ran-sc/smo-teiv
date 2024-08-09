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

public enum YangDataTypes {
    STRING,
    BOOLEAN,
    NUMERIC;

    public static YangDataTypes fromYangDataType(final String yangDatatype) {
        return switch (yangDatatype.toUpperCase(Locale.US)) {
            case "TEXT" -> STRING;
            case "BOOLEAN" -> BOOLEAN;
            case "INT", "DOUBLE" -> NUMERIC;
            default -> throw new IllegalStateException("Unexpected value: " + yangDatatype.toUpperCase(Locale.US));
        };
    }

    public static YangDataTypes fromRequestDataType(final Class<?> requestDatatype) {
        return switch (requestDatatype.getSimpleName()) {
            case "String" -> STRING;
            case "Integer", "Double" -> NUMERIC;
            case "Boolean" -> BOOLEAN;
            default -> throw new IllegalStateException("Unexpected value: " + requestDatatype);
        };
    }
}
