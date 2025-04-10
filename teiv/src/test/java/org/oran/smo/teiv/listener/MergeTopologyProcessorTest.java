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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.oran.smo.teiv.ingestion.validation.IngestionOperationValidatorFactory;
import org.oran.smo.teiv.listener.audit.ExecutionStatus;
import org.oran.smo.teiv.listener.audit.IngestionAuditLogger;
import org.oran.smo.teiv.service.RelationshipMergeValidator;
import org.oran.smo.teiv.service.TeivDbOperations;

import io.cloudevents.CloudEvent;

import org.oran.smo.teiv.CustomMetrics;
import org.oran.smo.teiv.schema.MockSchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoaderException;
import org.oran.smo.teiv.service.TeivDbService;
import org.oran.smo.teiv.service.TeivMetadataResolver;
import org.oran.smo.teiv.service.cloudevent.CloudEventParser;
import org.oran.smo.teiv.service.cloudevent.data.Entity;
import org.oran.smo.teiv.service.cloudevent.data.ParsedCloudEventData;
import org.oran.smo.teiv.utils.CloudEventTestUtil;

@ExtendWith(MockitoExtension.class)
class MergeTopologyProcessorTest {

    private MergeTopologyProcessor mergeTopologyProcessor;

    @Mock
    private CloudEventParser cloudEventParser;

    @Mock
    private CustomMetrics metrics;

    @Mock
    private TeivDbService teivDbService;

    @Mock
    private IngestionAuditLogger auditLogger;

    private TeivDbOperations teivDbOperations;

    @BeforeAll
    static void setUpAll() throws SchemaLoaderException {
        // Load mock schema for testing
        SchemaLoader mockSchemaLoader = new MockSchemaLoader();
        mockSchemaLoader.loadSchemaRegistry();
    }

    @BeforeEach
    void setUp() {
        teivDbOperations = new TeivDbOperations(teivDbService, new IngestionOperationValidatorFactory(),
                new RelationshipMergeValidator(), new TeivMetadataResolver());
        mergeTopologyProcessor = new MergeTopologyProcessor(cloudEventParser, metrics, teivDbOperations, auditLogger);
    }

    @Test
    void testMergeEntity() {
        CloudEvent event = CloudEventTestUtil.getCloudEvent("merge", "{}");
        String entityType = "ODUFunction";
        Map<String, Object> yangParserOutputMapBSide = new HashMap<>();
        Entity entity = new Entity("", entityType, "cloud_id_1", yangParserOutputMapBSide, List.of());

        ParsedCloudEventData parsedData = new ParsedCloudEventData(List.of(entity), List.of());
        when(cloudEventParser.getCloudEventData(any())).thenReturn(parsedData);

        doThrow(new RuntimeException("Discard event by expected test behavior")).when(teivDbService).execute(anyList());
        Assertions.assertDoesNotThrow(() -> mergeTopologyProcessor.process(event, anyString()));

        verify(teivDbService, times(1)).execute(anyList());

        verify(auditLogger).logError(eq(ExecutionStatus.FAILED), eq("merge"), any(CloudEvent.class), anyString(),
                anyString());
    }

    @Test
    void testInvalidAttribute() {
        CloudEvent event = CloudEventTestUtil.getCloudEvent("merge", "{}");
        String entityType = "ManagedElement";
        Map<String, Object> yangParserOutputMap = new HashMap<>();
        yangParserOutputMap.put("invalidfield", "value1");
        Entity entity = new Entity("", entityType, "id1", yangParserOutputMap, List.of());
        ParsedCloudEventData parsedData = new ParsedCloudEventData(List.of(entity), List.of());
        when(cloudEventParser.getCloudEventData(ArgumentMatchers.any())).thenReturn(parsedData);

        mergeTopologyProcessor.process(event, anyString());
        verifyNoInteractions(teivDbService);

        verify(metrics, times(1)).incrementNumSuccessfullyParsedMergeCloudEvents();
        verify(metrics, times(1)).incrementNumUnsuccessfullyPersistedMergeCloudEvents();
        verify(metrics, times(1)).recordCloudEventMergeParseTime(anyLong());

        verifyNoMoreInteractions(metrics);

        verify(auditLogger).logError(eq(ExecutionStatus.FAILED), eq("merge"), any(CloudEvent.class), anyString(),
                anyString());
    }

    @Test
    void testInvalidGeoLocationAttribute() {
        CloudEvent event = CloudEventTestUtil.getCloudEvent("merge", "{}");
        String entityType = "Sector";
        Map<String, Object> yangParserOutputMap = new HashMap<>();
        yangParserOutputMap.put("geo-location", 0);
        Entity entity = new Entity("", entityType, "id1", yangParserOutputMap, List.of());
        ParsedCloudEventData parsedData = new ParsedCloudEventData(List.of(entity), List.of());
        when(cloudEventParser.getCloudEventData(ArgumentMatchers.any())).thenReturn(parsedData);

        mergeTopologyProcessor.process(event, anyString());
        verifyNoInteractions(teivDbService);

        verify(metrics, times(1)).incrementNumSuccessfullyParsedMergeCloudEvents();
        verify(metrics, times(1)).incrementNumUnsuccessfullyPersistedMergeCloudEvents();
        verify(metrics, times(1)).recordCloudEventMergeParseTime(anyLong());

        verifyNoMoreInteractions(metrics);

        verify(auditLogger).logError(eq(ExecutionStatus.FAILED), eq("merge"), any(CloudEvent.class), anyString(),
                anyString());
    }

    @Test
    void testNullParsedCloudEventData() {
        CloudEvent event = CloudEventTestUtil.getCloudEvent("merge", "{}");
        when(cloudEventParser.getCloudEventData(ArgumentMatchers.any())).thenReturn(null);

        mergeTopologyProcessor.process(event, anyString());
        verifyNoInteractions(teivDbService);
        verify(metrics, times(1)).incrementNumUnsuccessfullyParsedMergeCloudEvents();
        verifyNoMoreInteractions(metrics);

        verify(auditLogger).logError(eq(ExecutionStatus.FAILED), eq("merge"), any(CloudEvent.class), anyString(),
                anyString());
    }
}
