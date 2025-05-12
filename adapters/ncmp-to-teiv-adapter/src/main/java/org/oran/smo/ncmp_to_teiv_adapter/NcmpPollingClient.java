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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.oran.smo.ncmp_to_teiv_adapter.models.ManagedElementWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class NcmpPollingClient {

    @Value("${polling.base-url}")
    private String baseUrl;

    @Value("${polling.searches-url}")
    private String searchesUrl;

    @Value("${polling.data-store-url}")
    private String dataStoreUrl;

    @Value("${polling.include-descendants}")
    private String includeDescendants;

    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public List<String> getAllCmHandlesFromNcmp() {
        try {
            HttpUrl url = HttpUrl.parse(baseUrl + searchesUrl).newBuilder().build();
            String requestBody = "{\"cmHandleQueryParameters\":[{\"conditionName\":\"cmHandleWithCpsPath\",\"conditionParameters\":[{\"cpsPath\":\"//state[@cm-handle-state='READY']\"}]}]}";
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

    public ManagedElementWrapper getAllManagedElementsFromNcmp(String cmHandle) {
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
}
