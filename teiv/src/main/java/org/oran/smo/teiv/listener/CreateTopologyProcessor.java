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
package org.oran.smo.teiv.listener;

import io.cloudevents.CloudEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oran.smo.teiv.CustomMetrics;
import org.oran.smo.teiv.exception.InvalidFieldInYangDataException;
import org.oran.smo.teiv.listener.audit.ExecutionStatus;
import org.oran.smo.teiv.listener.audit.IngestionAuditLogger;
import org.oran.smo.teiv.service.TiesDbOperations;
import org.oran.smo.teiv.service.cloudevent.CloudEventParser;
import org.oran.smo.teiv.service.cloudevent.data.ParsedCloudEventData;
import org.oran.smo.teiv.utils.CloudEventUtil;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import static org.oran.smo.teiv.utils.TiesConstants.CLOUD_EVENT_WITH_TYPE_CREATE;

@Component
@Slf4j
@Profile("ingestion")
@AllArgsConstructor
public class CreateTopologyProcessor implements TopologyProcessor {

    private final CloudEventParser cloudEventParser;
    private final CustomMetrics customMetrics;
    private final TiesDbOperations tiesDbOperations;
    private final IngestionAuditLogger auditLogger;

    @Override
    public void process(final CloudEvent cloudEvent, final String messageKey) {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
        if (null == parsedCloudEventData) {
            log.error("Failed to parse the following CloudEvent: {}", CloudEventUtil.cloudEventToPrettyString(cloudEvent));
            customMetrics.incrementNumUnsuccessfullyParsedCreateCloudEvents();
            auditLogger.auditLog(ExecutionStatus.FAILED, CLOUD_EVENT_WITH_TYPE_CREATE, cloudEvent, messageKey,
                    "Failed to parse the CloudEvent");
            return;
        }
        parsedCloudEventData.sort();
        stopWatch.stop();
        customMetrics.recordCloudEventCreateParseTime(stopWatch.lastTaskInfo().getTimeNanos());
        customMetrics.incrementNumSuccessfullyParsedCreateCloudEvents();

        stopWatch.start();
        final String sourceAdapter = String.valueOf(cloudEvent.getSource());
        try {
            tiesDbOperations.executeEntityAndRelationshipMergeOperations(parsedCloudEventData, sourceAdapter);
        } catch (InvalidFieldInYangDataException e) {
            log.error("Invalid field in yang data. Discarded CloudEvent: {}. Used kafka message key: {}. Reason: {}",
                    CloudEventUtil.cloudEventToPrettyString(cloudEvent), messageKey, e.getMessage());
            customMetrics.incrementNumUnsuccessfullyPersistedCreateCloudEvents();
            auditLogger.auditLog(ExecutionStatus.FAILED, CLOUD_EVENT_WITH_TYPE_CREATE, cloudEvent, messageKey, e
                    .getMessage());
            return;
        } catch (RuntimeException e) {
            log.error("Failed to process a CloudEvent. Discarded CloudEvent: {}. Used kafka message key: {}. Reason: {}",
                    CloudEventUtil.cloudEventToPrettyString(cloudEvent), messageKey, e.getMessage());
            customMetrics.incrementNumUnsuccessfullyPersistedCreateCloudEvents();
            auditLogger.auditLog(ExecutionStatus.FAILED, CLOUD_EVENT_WITH_TYPE_CREATE, cloudEvent, messageKey, e
                    .getMessage());
            return;
        }
        stopWatch.stop();
        customMetrics.incrementNumSuccessfullyPersistedCreateCloudEvents();
        customMetrics.recordCloudEventCreatePersistTime(stopWatch.lastTaskInfo().getTimeNanos());
        auditLogger.auditLog(ExecutionStatus.SUCCESS, CLOUD_EVENT_WITH_TYPE_CREATE, cloudEvent, messageKey, "");
    }
}
