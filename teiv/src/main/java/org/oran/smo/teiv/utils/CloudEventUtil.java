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

import io.cloudevents.CloudEvent;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;

@UtilityClass
public class CloudEventUtil {

    public static String cloudEventToPrettyString(CloudEvent cloudEvent) {
        String cloudEventData;
        if (cloudEvent.getData() == null) {
            cloudEventData = "null";
        } else {
            cloudEventData = new String(cloudEvent.getData().toBytes(), StandardCharsets.UTF_8);
        }
        return String.format("CloudEvent{id=%s, source=%s, type=%s, dataschema=%s, data=%s}", cloudEvent.getId(), cloudEvent
                .getSource(), cloudEvent.getType(), cloudEvent.getDataSchema(), cloudEventData);
    }

    public static boolean hasInvalidCharacter(String id) {
        if (id == null || id.isEmpty()) {
            return false;
        }
        // Characters that are permitted in URL components according to RFC3986, https://datatracker.ietf.org/doc/html/rfc3986,
        // as well as the ':' character which is used in IDs
        final String permittedCharactersPattern = "[-!$&'()*+,:;=._~0-9a-zA-Z]+";
        // General delimiters meant for delimiting URL components - these are not expected to be part of an id (: excluded)
        final String forbiddenCharactersPattern = "[\\[\\]/?#@]+";
        return (!id.matches(permittedCharactersPattern) || id.matches(forbiddenCharactersPattern));
    }
}
