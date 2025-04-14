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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.CloudEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.oran.smo.ncmp_to_teiv_adapter.models.ManagedElementWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.oran.smo.ncmp_to_teiv_adapter.ResourceReader.readResourceFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class PollNcmpCmHandlesForManagedElements {

    @Value("${polling.base-url}")
    private String baseUrl;

    @Value("${polling.searches-url}")
    private String searchesUrl;

    @Value("${polling.data-store-url}")
    private String dataStoreUrl;

    @Value("${polling.include-descendants}")
    private String includeDescendants;

    @Value("${send-sample-ocucp-event}")
    private boolean sendSampleOCUCPEvent;

    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaEventProducer kafkaEventProducer;
    
    private Map<String, Object> addedCmHandles = new HashMap<>();

    @Scheduled(fixedRateString = "${polling.interval}")
    public void pollExternalApi() throws IOException {
        List<String> cmHandlesToAdd = getAllCmHandlesFromNcmp();
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
        String payload = createSmo();
        CloudEvent event = createEvent(payload, "merge");
        log.info("Sending CloudEvent with payload: {}", payload);
        kafkaEventProducer.sendCloudEvent(event);
    }
    
    private String createSmo () {
        ArrayNode sourceIds = objectMapper.createArrayNode();
        sourceIds.add("urn:oran:smo:teiv:SMO");
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
        attributes.put("smoName","SMO");
        ObjectNode smo = objectMapper.createObjectNode();
        smo.put("id", "urn:oran:smo:teiv:SMO");
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

    private void addCmHandle (String cmHandle) {
        ManagedElementWrapper wrapper = getAllManagedElementsFromNcmp(cmHandle);
        Map<String, Object> json = wrapper.toTeivCloudEventPayload();
        try {
            String payload = objectMapper.writeValueAsString(json);
            CloudEvent event = createEvent(payload, "merge");
            log.info("Sending CloudEvent with payload: {}", payload);
            kafkaEventProducer.sendCloudEvent(event);
            addedCmHandles.put(cmHandle, json);
        } catch (JsonProcessingException e) {
            log.error("Error processing data from cmHandle {}. Event not sent. Error message: {}", cmHandle, e
                .getMessage());
        }
    }

    private void removeCmHandle (String cmHandle) {
        try {
            Object json = addedCmHandles.get(cmHandle);
            String payload = objectMapper.writeValueAsString(json);
            CloudEvent event = createEvent(payload, "delete");
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
            CloudEvent event = createEvent(payload, "merge");
            log.info("Sending CloudEvent with payload: {}", payload);
            kafkaEventProducer.sendCloudEvent(event);
            addedCmHandles.put("pynts-o-cu-cp-1", toJson);
        } catch (JsonProcessingException e) {
            log.error("Error processing ocucp data. Event not sent. Error message: {}", e.getMessage());
        }
    }

    private List<String> getAllCmHandlesFromNcmp() {
        try {
            HttpUrl url = HttpUrl.parse(baseUrl + searchesUrl).newBuilder().build();
            String requestBody = "{\"conditions\": [{\"name\": \"hasAllModules\"}]}";
            RequestBody body = RequestBody.create(requestBody, MediaType.parse("application/json"));

            Request request = new Request.Builder().url(url).post(body).addHeader("Content-Type", "application/json")
                    .build();
            log.info("Polling {}", url);
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    List<String> cmHandles = objectMapper.readValue(responseBody, new TypeReference<>() {
                    });
                    log.info("Parsed List: {}", cmHandles);
                    return cmHandles;
                } else {
                    log.error("Request failed for: {}, {}", url, response.code());
                }
            }
        } catch (Exception e) {
            log.error("Error polling API: {}", e.getMessage());
        }
        return List.of();
    }

    private ManagedElementWrapper getAllManagedElementsFromNcmp(String cmHandle) {
        try {
            HttpUrl url = HttpUrl.parse(baseUrl + "/" + cmHandle + dataStoreUrl).newBuilder().addQueryParameter(
                    "resourceIdentifier", "/").addQueryParameter("include-descendants", includeDescendants)
                    .addQueryParameter("options", "(fields=_3gpp-common-managed-element:ManagedElement)").build();
            Request request = new Request.Builder().url(url).get().addHeader("Content-Type", "application/json").build();
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    log.info("Raw Response for: {}, {}", url, responseBody);
                    return objectMapper.readValue(responseBody, ManagedElementWrapper.class);
                } else {
                    log.error("Request failed for: {}, {}", url, response.code());
                }
            }
        } catch (Exception e) {
            log.error("Error polling API: {}", e.getMessage());
        }
        return new ManagedElementWrapper();
    }

    private static CloudEvent createEvent(String data, String type) {
        return CloudEventBuilder.v1().withId(UUID.randomUUID().toString()).withSource(URI.create("ncmp-dmi-plugin:nm-1"))
                .withType("topology-inventory-ingestion." + type).withDataSchema(URI.create(
                        "https://teiv:8080/schemas/v1/r1-topology")).withTime(OffsetDateTime.now()).withData(
                                "application/yang-data+json", data.getBytes(StandardCharsets.UTF_8)).build();
    }
}
