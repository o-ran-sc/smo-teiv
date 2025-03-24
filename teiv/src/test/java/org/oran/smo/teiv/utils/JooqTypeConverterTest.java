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
package org.oran.smo.teiv.utils;

import static org.oran.smo.teiv.utils.JooqTypeConverter.toGeography;
import static org.oran.smo.teiv.utils.JooqTypeConverter.toJsonb;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

import org.jooq.JSONB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.oran.smo.teiv.exception.InvalidFieldInYangDataException;
import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.utils.schema.Geography;

class JooqTypeConverterTest {

    @Test
    void testToJsonb() {
        assertEquals(JSONB.jsonb("{}"), toJsonb("{}"));
        assertEquals(JSONB.jsonb("{\"key\":\"value\"}"), toJsonb("{\"key\":\"value\"}"));
        assertEquals(JSONB.jsonb("[]"), toJsonb("[]"));
        assertEquals(JSONB.jsonb("[1]"), toJsonb("[1]"));
        assertEquals(JSONB.jsonb("[1,2,3]"), toJsonb("[1,2,3]"));
        assertEquals(JSONB.jsonb("[\"value1\",\"value2\"]"), toJsonb("[\"value1\",\"value2\"]"));
        assertEquals(JSONB.jsonb("[\"leading\",\"whitespaces\"]"), toJsonb("     [\"leading\",\"whitespaces\"]"));
        assertEquals(JSONB.jsonb("[\"a_string\"]"), toJsonb("a_string"));
        assertEquals(JSONB.jsonb("[\"23\"]"), toJsonb("23"));
        assertEquals(JSONB.jsonb("[54]"), toJsonb(54L));
        assertEquals(JSONB.jsonb("[92.13]"), toJsonb(92.13));
        assertEquals(JSONB.jsonb("{\"key\":\"value\"}"), toJsonb("key", "value"));
        assertEquals(JSONB.jsonb("{\"key\":\"value\"}"), toJsonb(Map.of("key", "value")));
        assertEquals(JSONB.jsonb("{\"key1\":\"value1\",\"key2\":\"value2\"}"), toJsonb(Map.of("key1", "value1", "key2",
                "value2")));
    }

    @Test
    void testToGeography() throws InvalidFieldInYangDataException {
        assertEquals(new Geography(19.040236, 47.497913), toGeography(
                "{\"longitude\": 19.040236,\"latitude\": 47.497913}"));

        assertEquals(new Geography(19.040236, 47.497913, 100.1), toGeography(
                "{\"longitude\": 19.040236,\"latitude\": 47.497913, \"height\": 100.1}"));

        assertEquals(new Geography(19.040236, 47.497913, null), toGeography(
                "{\"longitude\": 19.040236,\"latitude\": 47.497913}"));

        assertThrows(InvalidFieldInYangDataException.class, () -> toGeography("{invalidjson"));
        assertThrows(InvalidFieldInYangDataException.class, () -> toGeography(9));
    }

    @Test
    void testJsonbToList() {
        final String inputStr = "[\"one\",\"two\",\"three\"]";
        final JSONB input = JSONB.valueOf(inputStr);
        final List<String> outputList = JooqTypeConverter.jsonbToList(input);

        final String expectedStr = "[one, two, three]";
        Assertions.assertEquals(expectedStr, outputList.toString());
    }

    @Test
    void testJsonbToMap() {
        final String inputStr = "{\"key1\":\"value1\", \"key2\": \"value2\"}";
        final JSONB input = JSONB.valueOf(inputStr);
        final Map<String, Object> outputMap = JooqTypeConverter.jsonbToMap(input);

        final String expectedStr = "{key1=value1, key2=value2}";
        Assertions.assertEquals(expectedStr, outputMap.toString());
    }

    @Test
    void testThrowExceptionJsonbToList() {
        final JSONB invalidJson = JSONB.valueOf("[\"one\", \"two]");
        Assertions.assertThrowsExactly(TeivException.class, () -> JooqTypeConverter.jsonbToList(invalidJson));
    }

    @Test
    void testThrowExceptionJsonbToMap() {
        final JSONB invalidJson = JSONB.valueOf("{\"key\": \"valu");
        Assertions.assertThrowsExactly(TeivException.class, () -> JooqTypeConverter.jsonbToList(invalidJson));
    }
}
