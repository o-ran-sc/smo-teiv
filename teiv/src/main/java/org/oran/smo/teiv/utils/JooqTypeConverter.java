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

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jooq.JSONB;
import org.jooq.tools.json.JSONArray;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.experimental.UtilityClass;

import lombok.extern.slf4j.Slf4j;
import org.oran.smo.teiv.exception.InvalidFieldInYangDataException;
import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.utils.schema.Geography;

@UtilityClass
@Slf4j
public class JooqTypeConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<String> jsonbToList(final JSONB jsonb) {
        try {
            return objectMapper.readValue(jsonb.data(), List.class);
        } catch (JsonProcessingException e) {
            String message = "Exception during converting jsonb to list";
            log.error(message, e);
            throw TeivException.serverException(message, null, e);
        }
    }

    public static Map<String, Object> jsonbToMap(final JSONB jsonb) {
        try {
            return objectMapper.readValue(jsonb.data(), Map.class);
        } catch (JsonProcessingException e) {
            String message = "Exception during converting jsonb to map";
            log.error(message, e);
            throw TeivException.serverException(message, null, e);
        }
    }

    /**
     * Creates a jooq.JSONB object from a json formatted object.
     *
     * The Yang Parser library parses the single element arrays as a JSON primitive.
     * The use of yang models will solve this problem. As a temporary workaround,
     * if one of the following is true, then the @param value is transformed into
     * a single element json array before creating the JSONB object:
     * - @param value is not a String, or
     * - @param value is a String, but does not contain a JSON.
     *
     * @param value
     * @return The jooq.JSONB object created from the value.
     */
    public static JSONB toJsonb(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof String str && isJsonObjectOrArray(str)) {
            return JSONB.jsonb(str);
        } else if (value instanceof Map<?, ?> map) {
            return toJsonb(objectMapper.convertValue(map, ObjectNode.class).toString());
        } else {
            return JSONB.jsonb(makeSingleElementJsonArray(value));
        }
    }

    /**
     * Creates a jooq.JSONB object from a key-value pair.
     *
     * @param key
     * @param value
     * @return The jooq.JSONB object created from the key-value pair
     */
    public static JSONB toJsonb(final String key, String value) {
        return JooqTypeConverter.toJsonb(objectMapper.createObjectNode().put(key, value).toString());
    }

    /**
     * Creates a jooq.JSONB object from a List of strings.
     *
     * @param stringList
     * @return The jooq.JSONB object created from the value.
     */
    public static JSONB toJsonb(List<String> stringList) {
        return JSONB.jsonb(JSONArray.toJSONString(stringList));
    }

    private static boolean isJsonObjectOrArray(String str) {
        String trimmedStr = str.stripLeading();
        return trimmedStr.startsWith("{") || trimmedStr.startsWith("[");
    }

    private static String makeSingleElementJsonArray(Object obj) {
        return JSONArray.toJSONString(List.of(obj));
    }

    public static Geography toGeography(Object value) throws InvalidFieldInYangDataException {
        try {
            if (value == null) {
                return null;
            }
            return new Geography((String) value);
        } catch (ClassCastException | IOException e) {
            throw new InvalidFieldInYangDataException(String.format("Can't create a Geography object from: %s", value), e);
        }
    }
}
