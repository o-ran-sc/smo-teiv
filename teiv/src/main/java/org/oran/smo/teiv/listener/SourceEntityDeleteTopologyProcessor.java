/*
 *  ============LICENSE_START=======================================================
 *  Copyright (C) 2024 Ericsson
 *  Modifications Copyright (C) 2024-2025 OpenInfra Foundation Europe
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.oran.smo.teiv.listener.audit.ExecutionStatus;
import org.oran.smo.teiv.listener.audit.IngestionAuditLogger;
import org.oran.smo.teiv.service.models.OperationResult;
import org.oran.smo.teiv.utils.CloudEventUtil;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import org.oran.smo.teiv.schema.SchemaRegistry;
import io.cloudevents.CloudEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.oran.smo.teiv.CustomMetrics;
import org.oran.smo.teiv.service.TeivDbOperations;
import org.oran.smo.teiv.service.TeivDbService;

import static org.oran.smo.teiv.utils.TeivConstants.CLOUD_EVENT_WITH_TYPE_SOURCE_ENTITY_DELETE;

@Component
@Slf4j
@AllArgsConstructor
@Profile("ingestion")
public class SourceEntityDeleteTopologyProcessor implements TopologyProcessor {
    private final TeivDbService teivDbService;
    private final ObjectMapper objectMapper;
    private final CustomMetrics customMetrics;
    private final TeivDbOperations teivDbOperations;
    private final IngestionAuditLogger auditLogger;

    @Override
    public void process(CloudEvent cloudEvent, String messageKey) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        SourceEntityDelete sourceEntityDelete;
        try {
            sourceEntityDelete = objectMapper.readValue(cloudEvent.getData().toBytes(), SourceEntityDelete.class);
        } catch (IOException e) {
            log.error("Error while parsing the {} event.", e.getMessage());
            customMetrics.incrementNumUnsuccessfullyParsedSourceEntityDeleteCloudEvents();
            auditLogger.logError(ExecutionStatus.FAILED, CLOUD_EVENT_WITH_TYPE_SOURCE_ENTITY_DELETE, cloudEvent, messageKey,
                    "Failed to parse the CloudEvent");
            return;
        }

        //currently only cmHandle delete supported
        if (!"cmHandle".equalsIgnoreCase(sourceEntityDelete.type)) {
            log.error("Unsupported type: {} for source-entity-delete event. Event: {}", sourceEntityDelete.type,
                    cloudEvent);
            customMetrics.incrementNumReceivedCloudEventNotSupported();
            auditLogger.logError(ExecutionStatus.FAILED, CLOUD_EVENT_WITH_TYPE_SOURCE_ENTITY_DELETE, cloudEvent, messageKey,
                    String.format("Unsupported type: %s for source-entity-delete event.", sourceEntityDelete.type));
            return;
        }

        stopWatch.stop();
        customMetrics.recordCloudEventSourceEntityDeleteParseTime(stopWatch.lastTaskInfo().getTimeNanos());
        customMetrics.incrementNumSuccessfullyParsedSourceEntityDeleteCloudEvents();

        stopWatch.start();
        List<OperationResult> operationResults = new ArrayList<>();
        try {
            List<Consumer<DSLContext>> dbOperations = new ArrayList<>();
            SchemaRegistry.getEntityTypes().forEach(entityType -> dbOperations.add(dslContext -> {
                List<String> results = teivDbOperations.selectByCmHandleFormSourceIds(dslContext, entityType.getTableName(),
                        sourceEntityDelete.value);
                if (!results.isEmpty()) {
                    for (String result : results) {
                        operationResults.addAll(teivDbOperations.deleteEntity(dslContext, entityType, result));
                    }
                }
            }));
            teivDbService.execute(dbOperations);
        } catch (RuntimeException e) {
            log.error("Failed to process a CloudEvent. Discarded CloudEvent: {}. Used kafka message key: {}. Reason: {}",
                    CloudEventUtil.cloudEventToPrettyString(cloudEvent), messageKey, e.getMessage());
            customMetrics.incrementNumUnsuccessfullyPersistedSourceEntityDeleteCloudEvents();
            auditLogger.logError(ExecutionStatus.FAILED, CLOUD_EVENT_WITH_TYPE_SOURCE_ENTITY_DELETE, cloudEvent, messageKey,
                    e.getMessage());
            return;
        }

        stopWatch.stop();
        customMetrics.recordCloudEventSourceEntityDeletePersistTime(stopWatch.lastTaskInfo().getTimeNanos());
        customMetrics.incrementNumSuccessfullyPersistedSourceEntityDeleteCloudEvents();
        auditLogger.logSuccess(ExecutionStatus.SUCCESS, CLOUD_EVENT_WITH_TYPE_SOURCE_ENTITY_DELETE, cloudEvent, messageKey,
                List.of());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record SourceEntityDelete(@JsonProperty(value = "type", required = true) String type,
                              @JsonProperty(value = "value", required = true) String value) {
    }
}
