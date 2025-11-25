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
package org.oran.smo.teiv.adapters.focom_to_teiv_adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oran.smo.teiv.adapters.focom_to_teiv_adapter.service.FocomToTeivModelBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FocomToTeivIngestion {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaEventProducer kafkaEventProducer;
    private final FocomToTeivModelBuilder jsonBuilder;

    @Scheduled(fixedRateString = "${polling.interval}")
    public void pollExternalApi() throws IOException {
        Map<String, Object> json = jsonBuilder.getFocomtoTeivJson();
        log.debug("Retrieved JSON for FOCOM_PROVISION_REQUEST_NAME: {}", json);
        try {
            sendCloudEvent(json, "merge");
        } catch (IOException e) {
            log.error("Failed to poll external API or send CloudEvent", e);
        }
    }

    private void sendCloudEvent(Map<String, Object> json, String eventType) throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(json);
        CloudEvent event = CloudEventFactory.createEvent(payload, eventType);
        log.info("Sending CloudEvent with payload: {}", payload);
        kafkaEventProducer.sendCloudEvent(event);
    }
}
