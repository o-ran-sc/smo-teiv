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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jooq.JSONB;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

public class EndToEndExpectedResults {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final JsonNode rootNode;

    @Getter
    public ArrayList<String> tables;

    public EndToEndExpectedResults(final String jsonPath) throws IOException {
        rootNode = OBJECT_MAPPER.readTree(Files.readString(Paths.get(jsonPath)));
        tables = new ArrayList<>();
        rootNode.fields().forEachRemaining(entry -> tables.add(entry.getKey()));
    }

    public Map<String, Object> get(final String entryId) {
        JsonNode attributesNode = rootNode.required(entryId);
        return processData(attributesNode);
    }

    public Map<String, Object> processData(JsonNode node) {
        Map<String, Object> tableData = new HashMap<>();
        node.fields().forEachRemaining(entry -> tableData.put(entry.getKey(), processNode(entry.getValue())));
        return tableData;
    }

    private Object processNode(JsonNode valueNode) {
        if (valueNode.isContainerNode()) {
            return JSONB.jsonb(valueNode.toString());
        } else if (valueNode.isTextual()) {
            return valueNode.asText();
        } else if (valueNode.isDouble()) {
            return valueNode.asDouble();
        } else if (valueNode.isNumber()) {
            return valueNode.asLong();
        } else if (valueNode.isBoolean()) {
            return valueNode.asBoolean();
        }
        return null;
    }

    public List<Map<String, Object>> getTableData(String tableName) {
        List<Map<String, Object>> tableData = new ArrayList<>();
        JsonNode tableArrayNode = rootNode.get(tableName);
        if (tableArrayNode != null && tableArrayNode.isArray()) {
            for (JsonNode contentNode : tableArrayNode) {
                tableData.add(processData(contentNode));
            }
        }
        return tableData;
    }

    public List<String> getTableEntryIds(String tableName) {
        List<String> entryIds = new ArrayList<>();
        JsonNode tableArrayNode = rootNode.get(tableName);
        if (tableArrayNode != null && tableArrayNode.isArray()) {
            for (JsonNode elementNode : tableArrayNode) {
                entryIds.add(elementNode.textValue());
            }
        }
        return entryIds;
    }
}
