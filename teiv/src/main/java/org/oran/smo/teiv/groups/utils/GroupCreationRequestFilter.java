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
package org.oran.smo.teiv.groups.utils;

import static org.oran.smo.teiv.groups.audit.ExecutionStatus.FAILED;
import static org.oran.smo.teiv.groups.audit.GroupOperation.CREATE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oran.smo.teiv.exposure.audit.LoggerHandler;
import org.oran.smo.teiv.groups.audit.AuditInfo;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@Profile("groups")
public class GroupCreationRequestFilter implements jakarta.servlet.Filter {

    private final LoggerHandler loggerHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        ContentCachingRequestWrapper httpServletRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
        CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(httpServletRequest);
        ContentCachingResponseWrapper httpServletResponse = new ContentCachingResponseWrapper(
                (HttpServletResponse) response);
        String payloadString = readRequestBody(cachedBodyHttpServletRequest);
        try {
            if (cachedBodyHttpServletRequest.getMethod().equalsIgnoreCase("POST")) {
                JsonNode createGroupPayload = objectMapper.readValue(payloadString, JsonNode.class);
                String message = validateGroupType(createGroupPayload);
                if (!message.isEmpty()) {
                    loggerHandler.logAudit(log, AuditInfo.builder().operation(CREATE).createGroupPayloadString(
                            payloadString).exceptionMessage(message).status(FAILED).build().toString(),
                            cachedBodyHttpServletRequest);
                    httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    httpServletResponse.setContentType("application/json");
                    httpServletResponse.getWriter().write(generateResponse(message));
                    httpServletResponse.getWriter().flush();
                    return;
                }
            }
            chain.doFilter(cachedBodyHttpServletRequest, httpServletResponse);
        } catch (JsonProcessingException ex) {
            String message = String.format("Unable to parse the given request body. %s", ex.getMessage());
            loggerHandler.logAudit(log, AuditInfo.builder().operation(CREATE).createGroupPayloadString(payloadString)
                    .exceptionMessage(message).status(FAILED).build().toString(), cachedBodyHttpServletRequest);
            chain.doFilter(cachedBodyHttpServletRequest, httpServletResponse);
        } finally {
            httpServletResponse.copyBodyToResponse();
        }
    }

    private String validateGroupType(JsonNode createGroupPayload) {
        String message = "";
        if (createGroupPayload.has("type")) {
            String groupType = createGroupPayload.get("type").asText();
            if (createGroupPayload.has("providedMembers") && groupType.equals("dynamic")) {
                message = "A static group cannot be created of type 'dynamic'";
            } else if (createGroupPayload.has("criteria") && groupType.equals("static")) {
                message = "A dynamic group cannot be created of type 'static'";
            } else if (!"static".equals(groupType) && !groupType.equals("dynamic")) {
                message = "Invalid group type. Only 'static' or 'dynamic' types are allowed";
            } else if (createGroupPayload.has("criteria") && createGroupPayload.get("criteria").has("queryType")) {
                String queryType = createGroupPayload.get("criteria").get("queryType").asText();
                if (!List.of("getRelationshipsForEntityId", "getEntitiesByDomain", "getEntitiesByType",
                        "getRelationshipsByType").contains(queryType)) {
                    return "Invalid query type. Only 'getRelationshipsForEntityId', 'getEntitiesByDomain', 'getEntitiesByType' & 'getRelationshipsByType' are supported.";
                }
            }
        }
        return message;
    }

    private String generateResponse(String details) throws JsonProcessingException {
        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("details", details);
        jsonResponse.put("message", "Invalid type specified");
        jsonResponse.put("status", HttpStatus.BAD_REQUEST);
        return objectMapper.writeValueAsString(jsonResponse);
    }

    private String readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder body = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(),
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        }
        return body.toString();
    }
}
