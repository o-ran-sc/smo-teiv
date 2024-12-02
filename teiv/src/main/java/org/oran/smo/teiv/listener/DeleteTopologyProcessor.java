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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.oran.smo.teiv.CustomMetrics;
import org.oran.smo.teiv.listener.audit.ExecutionStatus;
import org.oran.smo.teiv.listener.audit.IngestionAuditLogger;
import org.oran.smo.teiv.schema.EntityType;
import org.oran.smo.teiv.schema.RelationType;
import org.oran.smo.teiv.schema.RelationshipDataLocation;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.service.TiesDbOperations;
import org.oran.smo.teiv.service.TiesDbService;
import org.oran.smo.teiv.service.cloudevent.CloudEventParser;
import org.oran.smo.teiv.service.cloudevent.data.ParsedCloudEventData;
import org.oran.smo.teiv.service.models.OperationResult;
import org.oran.smo.teiv.utils.CloudEventUtil;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import static org.oran.smo.teiv.utils.TiesConstants.CLOUD_EVENT_WITH_TYPE_DELETE;

@Component
@Slf4j
@AllArgsConstructor
@Profile("ingestion")
public class DeleteTopologyProcessor implements TopologyProcessor {

    private final CloudEventParser cloudEventParser;
    private final TiesDbService tiesDbService;
    private final TiesDbOperations tiesDbOperations;
    private final CustomMetrics customMetrics;
    private final IngestionAuditLogger auditLogger;

    //spotless:off
    @Override
    public void process(final CloudEvent cloudEvent, final String messageKey) {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
        if (parsedCloudEventData == null) {
            log.error("Failed to parse the following CloudEvent: {}", CloudEventUtil.cloudEventToPrettyString(cloudEvent));
            customMetrics.incrementNumUnsuccessfullyParsedDeleteCloudEvents();
            auditLogger.auditLog(ExecutionStatus.FAILED, CLOUD_EVENT_WITH_TYPE_DELETE, cloudEvent, messageKey, "Failed to parse the CloudEvent");
            return;
        }
        parsedCloudEventData.sort();
        stopWatch.stop();
        customMetrics.recordCloudEventDeleteParseTime(stopWatch.lastTaskInfo().getTimeNanos());
        customMetrics.incrementNumSuccessfullyParsedDeleteCloudEvents();

        stopWatch.start();

        List<Consumer<DSLContext>> dbOperations = new ArrayList<>();
        List<OperationResult> operationResults = new ArrayList<>();

        parsedCloudEventData.getEntities().forEach(entity -> {
            EntityType entityType = SchemaRegistry.getEntityTypeByName(entity.getType());
            dbOperations.add(dslContext -> operationResults
                .addAll(tiesDbOperations.deleteEntity(dslContext, entityType, entity.getId())));
        });

        parsedCloudEventData.getRelationships().forEach(relationship -> {
            RelationType relationType = SchemaRegistry.getRelationTypeByName(relationship.getType());
            if (Objects.requireNonNull(relationType)
                .getRelationshipStorageLocation() == RelationshipDataLocation.RELATION) {
                dbOperations.add(dslContext -> tiesDbOperations.deleteManyToManyRelationByRelationId(dslContext,
                    relationType, relationship.getId()).ifPresent(operationResults::add));
            } else {
                dbOperations
                    .add(dslContext -> tiesDbOperations.deleteRelationFromEntityTableByRelationId(dslContext,
                        relationship.getId(), relationType).ifPresent(operationResults::add));
            }
        });

        try {
            tiesDbService.execute(dbOperations);
        } catch (RuntimeException e) {
            log.error("Failed to process a CloudEvent. Discarded CloudEvent: {}. Used kafka message key: {}. Reason: {}",
                CloudEventUtil.cloudEventToPrettyString(cloudEvent), messageKey, e.getMessage());
            customMetrics.incrementNumUnsuccessfullyPersistedDeleteCloudEvents();
            auditLogger.auditLog(ExecutionStatus.FAILED, CLOUD_EVENT_WITH_TYPE_DELETE, cloudEvent, messageKey, e.getMessage());
            return;
        }
        stopWatch.stop();
        customMetrics.recordCloudEventDeletePersistTime(stopWatch.lastTaskInfo().getTimeNanos());
        customMetrics.incrementNumSuccessfullyPersistedDeleteCloudEvents();
        auditLogger.auditLog(ExecutionStatus.SUCCESS, CLOUD_EVENT_WITH_TYPE_DELETE, cloudEvent, messageKey, "");
    }
    //spotless:on
}
