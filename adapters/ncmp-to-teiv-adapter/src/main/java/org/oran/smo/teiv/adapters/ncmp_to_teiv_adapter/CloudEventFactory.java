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

import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.UUID;

public class CloudEventFactory {

    public static CloudEvent createEvent(String data, String type) {
        return CloudEventBuilder.v1().withId(UUID.randomUUID().toString()).withSource(URI.create("ncmp-dmi-plugin:nm-1"))
                .withType("topology-inventory-ingestion." + type).withDataSchema(URI.create(
                        "https://teiv:8080/schemas/v1/r1-topology")).withTime(OffsetDateTime.now()).withData(
                                "application/yang-data+json", data.getBytes(StandardCharsets.UTF_8)).build();
    }
}
