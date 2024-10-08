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

import java.util.ArrayList;
import java.util.List;
import org.oran.smo.teiv.CustomMetrics;

import org.oran.smo.teiv.service.models.OperationResult;
import org.oran.smo.teiv.utils.CloudEventUtil;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import io.cloudevents.CloudEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.oran.smo.teiv.exception.InvalidFieldInYangDataException;
import org.oran.smo.teiv.service.TiesDbOperations;
import org.oran.smo.teiv.service.cloudevent.CloudEventParser;
import org.oran.smo.teiv.service.cloudevent.data.ParsedCloudEventData;

import org.springframework.util.StopWatch;

@AllArgsConstructor
@Component
@Profile("ingestion")
@Slf4j

public class MergeTopologyProcessor implements TopologyProcessor {

    private final CloudEventParser cloudEventParser;
    private final CustomMetrics customMetrics;
    private final TiesDbOperations tiesDbOperations;

    //spotless:off
    @Override
    public void process(final CloudEvent cloudEvent, final String messageKey) {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
        if (null == parsedCloudEventData) {
            customMetrics.incrementNumUnsuccessfullyParsedMergeCloudEvents();
            return;
        }
        parsedCloudEventData.sort();
        stopWatch.stop();
        customMetrics.recordCloudEventMergeParseTime(stopWatch.lastTaskInfo().getTimeNanos());
        customMetrics.incrementNumSuccessfullyParsedMergeCloudEvents();

        stopWatch.start();
        List<OperationResult> operationResults = new ArrayList<>();
        try {
            operationResults = tiesDbOperations.executeEntityAndRelationshipMergeOperations(parsedCloudEventData);
        } catch (InvalidFieldInYangDataException e) {
            log.error("Invalid field in yang data. Discarded CloudEvent: {}. Used kafka message key: {}. Reason: {}",
                    CloudEventUtil.cloudEventToPrettyString(cloudEvent), messageKey, e.getMessage());
            customMetrics.incrementNumUnsuccessfullyPersistedMergeCloudEvents();
            return;
        } catch (RuntimeException e) {
            log.error("Failed to process a CloudEvent. Discarded CloudEvent: {}. Used kafka message key: {}. Reason: {}",
                    CloudEventUtil.cloudEventToPrettyString(cloudEvent), messageKey, e.getMessage());
            customMetrics.incrementNumUnsuccessfullyPersistedMergeCloudEvents();
            return;
        }
        stopWatch.stop();

        customMetrics.incrementNumSuccessfullyPersistedMergeCloudEvents();
        customMetrics.recordCloudEventMergePersistTime(stopWatch.lastTaskInfo().getTimeNanos());
    }
    //spotless:on
}
