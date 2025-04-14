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
package org.oran.smo.ncmp_to_teiv_adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oran.smo.ncmp_to_teiv_adapter.models.ManagedElementWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.oran.smo.ncmp_to_teiv_adapter.ResourceReader.readResourceFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class NcmpToTeivIngestion {

    @Value("${send-sample-ocucp-event}")
    private boolean sendSampleOCUCPEvent;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaEventProducer kafkaEventProducer;
    private final NcmpPollingClient ncmpPollingClient;

    private Map<String, Object> addedCmHandles = new HashMap<>();

    @Scheduled(fixedRateString = "${polling.interval}")
    public void pollExternalApi() throws IOException {
        List<String> cmHandlesToAdd = ncmpPollingClient.getAllCmHandlesFromNcmp();
        List<String> cmHandlesToDelete = addedCmHandles.keySet().stream().filter(v -> !cmHandlesToAdd.contains(v)).toList();
        addSmo();

        for (String cmHandle : cmHandlesToAdd) {
            addCmHandle(cmHandle);
        }
        for (String cmHandle : cmHandlesToDelete) {
            removeCmHandle(cmHandle);
        }
        if (sendSampleOCUCPEvent) {
            sendSampleOCUCPEvent();
        }
    }

    private void addSmo() {
        String payload = SmoPayloadBuilder.build();
        CloudEvent event = CloudEventFactory.createEvent(payload, "merge");
        log.info("Sending CloudEvent with payload: {}", payload);
        kafkaEventProducer.sendCloudEvent(event);
    }

    private void addCmHandle(String cmHandle) {
        ManagedElementWrapper wrapper = ncmpPollingClient.getAllManagedElementsFromNcmp(cmHandle);
        Map<String, Object> json = wrapper.toTeivCloudEventPayload();
        try {
            String payload = objectMapper.writeValueAsString(json);
            CloudEvent event = CloudEventFactory.createEvent(payload, "merge");
            log.info("Sending CloudEvent with payload: {}", payload);
            kafkaEventProducer.sendCloudEvent(event);
            addedCmHandles.put(cmHandle, json);
        } catch (JsonProcessingException e) {
            log.error("Error processing data from cmHandle {}. Event not sent. Error message: {}", cmHandle, e
                    .getMessage());
        }
    }

    private void removeCmHandle(String cmHandle) {
        try {
            Object json = addedCmHandles.get(cmHandle);
            String payload = objectMapper.writeValueAsString(json);
            CloudEvent event = CloudEventFactory.createEvent(payload, "delete");
            log.info("Sending CloudEvent with payload: {}", payload);
            kafkaEventProducer.sendCloudEvent(event);
            addedCmHandles.remove(cmHandle);
        } catch (JsonProcessingException e) {
            log.error("Error processing data from cmHandle {}. Event not sent. Error message: {}", cmHandle, e
                    .getMessage());
        }
    }

    private void sendSampleOCUCPEvent() throws IOException {
        String content = readResourceFile("sample-responses/_3gpp-common-managed-element-ocucp.json");
        ManagedElementWrapper wrapper = objectMapper.readValue(content, ManagedElementWrapper.class);
        Map<String, Object> toJson = wrapper.toTeivCloudEventPayload();
        try {
            String payload = objectMapper.writeValueAsString(toJson);
            CloudEvent event = CloudEventFactory.createEvent(payload, "merge");
            log.info("Sending CloudEvent with payload: {}", payload);
            kafkaEventProducer.sendCloudEvent(event);
            addedCmHandles.put("pynts-o-cu-cp-1", toJson);
        } catch (JsonProcessingException e) {
            log.error("Error processing ocucp data. Event not sent. Error message: {}", e.getMessage());
        }
    }
}
