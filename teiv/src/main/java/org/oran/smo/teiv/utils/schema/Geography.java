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
package org.oran.smo.teiv.utils.schema;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Geography {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @NonNull
    private Double latitude;
    @NonNull
    private Double longitude;
    private Double height;

    /**
     * Creates a Geography object from a standard (RFC 9179) YANG geo-location type.
     * The "latitude" and "longitude" fields, are mandatory.
     * Optionally, it also supports the "height" field if provided in the JSON.
     * All other fields in the json are ignored.
     *
     * @param json
     *     A json that conforms to the "RFC 9179: A YANG Grouping for
     *     Geographic Location" standard.
     * @throws IOException
     *     when the json doesn't contain both "latitude" and
     *     "longitude" fields.
     */
    public Geography(final String json) throws IOException {
        JsonNode rootNode = objectMapper.readTree(json);

        JsonNode latitudeNode = findNode(rootNode, "latitude");
        JsonNode longitudeNode = findNode(rootNode, "longitude");

        if (isValidNumber(latitudeNode) && isValidNumber(longitudeNode)) {
            latitude = latitudeNode.asDouble();
            longitude = longitudeNode.asDouble();
            JsonNode heightNode = findNode(rootNode, "height");
            if (isValidNumber(heightNode)) {
                height = heightNode.asDouble();
            }
        } else {
            throw new IOException("Cannot find latitude, longitude fields in json: " + json);
        }
    }

    private boolean isValidNumber(JsonNode node) {
        return node != null && node.isNumber();
    }

    private JsonNode findNode(final JsonNode node, final String fieldName) {
        if (node.has(fieldName)) {
            return node.get(fieldName);
        }
        for (JsonNode child : node) {
            JsonNode found = findNode(child, fieldName);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return (height == null) ?
                String.format("POINT(%s %s)", latitude, longitude) :
                String.format("POINT Z (%s %s %s)", latitude, longitude, height);
    }
}
