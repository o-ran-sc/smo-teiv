/*
 *  ============LICENSE_START=======================================================
 *  Modifications Copyright (C) 2025 OpenInfra Foundation Europe
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
package org.oran.smo.teiv.adapters.ncmp_to_teiv_adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.oran.smo.teiv.adapters.common.utils.TeivIdBuilder;

public class SmoPayloadBuilder {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String build() {
        ArrayNode sourceIds = objectMapper.createArrayNode();
        sourceIds.add(TeivIdBuilder.buildFunctionFdn("SMO"));
        sourceIds.add("http://smo.o-ran-sc.org");
        sourceIds.add("http://gateway.smo.o-ran-sc.org");
        sourceIds.add("http://dentity.smo.o-ran-sc.org");
        sourceIds.add("http://messages.smo.o-ran-sc.org");
        sourceIds.add("http://kafka-bridge.smo.o-ran-sc.org");
        sourceIds.add("http://kafka-ui.smo.o-ran-sc.org");
        sourceIds.add("http://odlux.oam.smo.o-ran-sc.org");
        sourceIds.add("http://flows.oam.smo.o-ran-sc.org");
        sourceIds.add("http://tests.oam.smo.o-ran-sc.org");
        sourceIds.add("http://ves-collector.dcn.smo.o-ran-sc.org");
        sourceIds.add("http://controller.dcn.smo.o-ran-sc.org");
        sourceIds.add("http://nginx-loadbalancer:80/ncmp/v1/ch");
        ObjectNode attributes = objectMapper.createObjectNode();
        attributes.put("smoName", "SMO");
        ObjectNode smo = objectMapper.createObjectNode();
        smo.put("id", TeivIdBuilder.buildFunctionFdn("SMO"));
        smo.set("sourceIds", sourceIds);
        smo.set("attributes", attributes);
        ArrayNode smoArray = objectMapper.createArrayNode();
        smoArray.add(smo);
        ObjectNode entity = objectMapper.createObjectNode();
        entity.set("o-ran-smo-teiv-ran:SMO", smoArray);
        ArrayNode entitiesArray = objectMapper.createArrayNode();
        entitiesArray.add(entity);
        ObjectNode root = objectMapper.createObjectNode();
        root.set("entities", entitiesArray);
        return root.toString();
    }
}
