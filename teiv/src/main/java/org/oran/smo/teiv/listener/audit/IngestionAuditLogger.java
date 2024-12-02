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
package org.oran.smo.teiv.listener.audit;

import org.oran.smo.teiv.exposure.audit.LoggerHandler;
import org.springframework.stereotype.Component;

import org.oran.smo.teiv.utils.CloudEventUtil;
import io.cloudevents.CloudEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Component
public class IngestionAuditLogger {

    private final LoggerHandler loggerHandler;

    public void auditLog(ExecutionStatus status, String operationType, CloudEvent cloudEvent, String messageKey,
            String errorMsg) {
        loggerHandler.logAuditBase(log, IngestionAuditInfo.builder().status(status).operation(operationType).messageKey(
                messageKey).cloudEvent(CloudEventUtil.cloudEventToPrettyString(cloudEvent)).exceptionMessage(errorMsg)
                .build().toString(), String.valueOf(cloudEvent.getSource()));
    }
}
