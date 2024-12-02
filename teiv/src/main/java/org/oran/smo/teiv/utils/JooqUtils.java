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
package org.oran.smo.teiv.utils;

import static org.jooq.impl.DSL.field;

import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JooqUtils {
    public static Field<String> stringField(Table<?> table, String fieldName) {
        return table.field(fieldName, String.class);
    }

    public static Field<JSONB> jsonbField(Table<?> table, String fieldName) {
        return table.field(fieldName, JSONB.class);
    }

    public static Field<String> stringField(String fieldName) {
        return field(DSL.name(fieldName), String.class);
    }

    public static Field<JSONB> jsonbField(String fieldName) {
        return field(DSL.name(fieldName), JSONB.class);
    }

    public static Field<byte[]> byteArrayField(String fieldName) {
        return field(DSL.name(fieldName), byte[].class);
    }

    public static Field<String> jsonbFieldWithKey(String columnName, String jsonbKey) {
        return field("{0} ->> {1}", String.class, DSL.name(columnName), DSL.inline(jsonbKey));
    }

    public static Field<Integer> totalCount() {
        return DSL.count().over().as("total_count");
    }

    public static Field<String> valueField(String value) {
        return DSL.val(value);
    }

    public static Table<Record> tableName(String tableName) {
        return DSL.table(tableName);
    }
}
