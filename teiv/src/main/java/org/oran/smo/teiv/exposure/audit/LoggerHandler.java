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
package org.oran.smo.teiv.exposure.audit;

import org.slf4j.Logger;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class LoggerHandler {

    public void logAudit(Logger log, String message, HttpServletRequest context) {
        String requestInfo = getRequestInfo(context);
        log.info("Audit Log: {} - Request Info: {}", message, requestInfo);
    }

    private String getRequestInfo(HttpServletRequest context) {
        String method = context.getMethod();
        String requestURI = context.getRequestURI();

        return String.format("Method: %s, URI: %s", method, requestURI);
    }
}
