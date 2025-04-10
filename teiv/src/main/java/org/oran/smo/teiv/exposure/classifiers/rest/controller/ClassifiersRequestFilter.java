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

package org.oran.smo.teiv.exposure.classifiers.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oran.smo.teiv.CustomMetrics;
import org.oran.smo.teiv.RequestFilter;
import org.oran.smo.teiv.api.model.OranTeivClassifier;
import org.oran.smo.teiv.exposure.audit.LoggerHandler;
import org.oran.smo.teiv.CachedBodyHttpServletRequest;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

@Slf4j
@Profile("exposure")
@RequiredArgsConstructor
public class ClassifiersRequestFilter extends RequestFilter {
    private final LoggerHandler loggerHandler;
    private final ObjectMapper objectMapper;
    private final CustomMetrics customMetrics;

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
            final FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest request) {
            CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(request);
            if (cachedBodyHttpServletRequest.getMethod().equalsIgnoreCase("POST")) {
                final String payload = cachedBodyHttpServletRequest.getRequestBody();
                try {
                    objectMapper.readValue(payload, OranTeivClassifier.class);
                    filterChain.doFilter(cachedBodyHttpServletRequest, servletResponse);
                } catch (final JsonProcessingException exception) {
                    customMetrics.incrementNumUnsuccessfullyUpdatedClassifiers();
                    final String logMsg = String.format(
                            "Failed to process the request to merge/remove classifiers. Exception= %s , Payload= %s",
                            exception.getMessage(), payload);
                    loggerHandler.logAudit(log, logMsg, cachedBodyHttpServletRequest);
                    writeError(servletResponse, exception, "Failed to process the request to merge/remove classifiers");
                }
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
