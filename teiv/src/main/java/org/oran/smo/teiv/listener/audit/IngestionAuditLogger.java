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

import org.apache.commons.lang3.StringUtils;
import org.oran.smo.teiv.exposure.audit.LoggerHandler;
import org.springframework.stereotype.Component;

import org.oran.smo.teiv.utils.CloudEventUtil;
import io.cloudevents.CloudEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static org.oran.smo.teiv.utils.TeivConstants.CLOUD_EVENT_WITH_TYPE_CREATE;
import static org.oran.smo.teiv.utils.TeivConstants.CLOUD_EVENT_WITH_TYPE_MERGE;

@Slf4j
@AllArgsConstructor
@Component
public class IngestionAuditLogger {

    private final LoggerHandler loggerHandler;

    public void logError(ExecutionStatus status, String operationType, CloudEvent cloudEvent, String messageKey,
            String errorMsg) {
        final String message = String.format("%s. Exception = %s", getMessage(status, operationType, cloudEvent,
                messageKey), errorMsg);
        loggerHandler.logAuditBase(log, message, cloudEvent.getSource().toString());
    }

    public void logSuccess(ExecutionStatus status, String operationType, CloudEvent cloudEvent, String messageKey,
            List<String> inferred) {
        final String message = String.format("%s. Implicitly %sd items = %s", getMessage(status, operationType, cloudEvent,
                messageKey), operationType.equals(CLOUD_EVENT_WITH_TYPE_MERGE) ?
                        CLOUD_EVENT_WITH_TYPE_CREATE :
                        operationType, inferred);
        loggerHandler.logAuditBase(log, message, cloudEvent.getSource().toString());
    }

    private String getMessage(final ExecutionStatus status, final String operation, final CloudEvent cloudEvent,
            final String messageKey) {
        return String.format("%s - %s topology. Message key: %s, CloudEvent: %s", status, StringUtils.capitalize(operation),
                messageKey, CloudEventUtil.cloudEventToPrettyString(cloudEvent));
    }
}
