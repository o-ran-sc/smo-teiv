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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.oran.smo.teiv.listener.audit.ExecutionStatus;
import org.oran.smo.teiv.listener.audit.IngestionAuditLogger;
import org.oran.smo.teiv.service.cloudevent.CloudEventParser;
import org.oran.smo.teiv.service.models.OperationResult;
import org.oran.smo.teiv.utils.CloudEventTestUtil;

import io.cloudevents.CloudEvent;

import org.oran.smo.teiv.CustomMetrics;
import org.oran.smo.teiv.schema.BidiDbNameMapper;
import org.oran.smo.teiv.schema.EntityType;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.service.TiesDbOperations;
import org.oran.smo.teiv.service.TiesDbService;

@ExtendWith(MockitoExtension.class)
class SourceEntityDeleteTopologyProcessorTest {

    private SourceEntityDeleteTopologyProcessor sourceEntityDeleteTopologyProcessor;

    @Mock
    private CloudEventParser cloudEventParser;

    @Mock
    private CustomMetrics metrics;

    @Mock
    private TiesDbService tiesDbService;

    @Mock
    private TiesDbOperations tiesDbOperations;

    @Mock
    private IngestionAuditLogger auditLogger;

    private static final String EVENT_TYPE = "topology-inventory-ingestion.source-entity-delete";

    @BeforeEach
    void setUp() {
        sourceEntityDeleteTopologyProcessor = new SourceEntityDeleteTopologyProcessor(tiesDbService, new ObjectMapper(),
                metrics, tiesDbOperations, auditLogger);
    }

    @Test
    void testSourceEntityDeleteWithInvalidEventData() {
        // given
        try (MockedStatic<SchemaRegistry> mockedSchemaRegistry = Mockito.mockStatic(SchemaRegistry.class)) {
            CloudEvent event = CloudEventTestUtil.getCloudEvent(EVENT_TYPE, "{\"type\":\"cmHandle\",\"invalid\":\"abc\"}");
            // when
            assertDoesNotThrow(() -> sourceEntityDeleteTopologyProcessor.process(event, "messageKey"));
            // then
            mockedSchemaRegistry.verifyNoInteractions();
            verifyNoInteractions(tiesDbService);
            verify(metrics, times(1)).incrementNumUnsuccessfullyParsedSourceEntityDeleteCloudEvents();
            verifyNoMoreInteractions(metrics);

            verify(auditLogger).auditLog(eq(ExecutionStatus.FAILED), eq("source-entity-delete"), any(CloudEvent.class),
                    anyString(), anyString());
        }
    }

    @Test
    void testSourceEntityDeleteWithUnsupportedEntityType() {
        // given
        try (MockedStatic<SchemaRegistry> mockedSchemaRegistry = Mockito.mockStatic(SchemaRegistry.class)) {
            CloudEvent event = CloudEventTestUtil.getCloudEvent(EVENT_TYPE,
                    "{\"type\":\"unsupported-type\",\"value\":\"abc\"}");
            // when
            assertDoesNotThrow(() -> sourceEntityDeleteTopologyProcessor.process(event, "messageKey"));
            // then
            mockedSchemaRegistry.verifyNoInteractions();
            verifyNoInteractions(tiesDbService);
            verify(metrics, times(1)).incrementNumReceivedCloudEventNotSupported();
            verifyNoMoreInteractions(metrics);

            verify(auditLogger).auditLog(eq(ExecutionStatus.FAILED), eq("source-entity-delete"), any(CloudEvent.class),
                    anyString(), anyString());
        }
    }

    @Test
    void testSourceEntityDeleteUponRuntimeExceptionDuringDeletion() {
        // given
        try (MockedStatic<SchemaRegistry> mockedSchemaRegistry = Mockito.mockStatic(SchemaRegistry.class)) {
            EntityType entityType = mock(EntityType.class);
            CloudEvent event = CloudEventTestUtil.getCloudEvent(EVENT_TYPE, "{\"type\":\"cmHandle\",\"value\":\"abc\"}");

            mockedSchemaRegistry.when(SchemaRegistry::getEntityTypes).thenReturn(List.of(entityType));
            doThrow(new RuntimeException()).when(tiesDbService).execute(anyList());
            // when
            assertDoesNotThrow(() -> sourceEntityDeleteTopologyProcessor.process(event, "messageKey"));
            // then
            mockedSchemaRegistry.verify(SchemaRegistry::getEntityTypes, times(1));
            verify(tiesDbService, times(1)).execute(anyList());

            verify(metrics, times(1)).incrementNumSuccessfullyParsedSourceEntityDeleteCloudEvents();
            verify(metrics, times(1)).incrementNumUnsuccessfullyPersistedSourceEntityDeleteCloudEvents();
            verify(metrics, times(1)).recordCloudEventSourceEntityDeleteParseTime(anyLong());
            verifyNoMoreInteractions(metrics);

            verify(auditLogger).auditLog(eq(ExecutionStatus.FAILED), eq("source-entity-delete"), any(CloudEvent.class),
                    anyString(), any());
        }
    }

    @Test
    void testSourceEntityDelete() throws NoSuchFieldException {
        // given
        try (MockedStatic<SchemaRegistry> mockedSchemaRegistry = Mockito.mockStatic(SchemaRegistry.class)) {
            DSLContext stream = mock(DSLContext.class);
            CloudEvent event = CloudEventTestUtil.getCloudEvent(EVENT_TYPE,
                    "{\"type\":\"cmHandle\",\"value\":\"395221E080CCF0FD1924103B15873814\"}");

            Map<String, String> mockNameMap = new HashMap<>();
            mockNameMap.put("GNBDUFunction", "GNBDUFunction");
            Field nameMapField = BidiDbNameMapper.class.getDeclaredField("nameMap");
            nameMapField.setAccessible(true);
            nameMapField.set(null, mockNameMap);

            OperationResult mockOperationResult = mock(OperationResult.class);
            EntityType gnbduFunction = EntityType.builder().name("GNBDUFunction").build();
            when(SchemaRegistry.getEntityTypes()).thenReturn(List.of(gnbduFunction));
            when(tiesDbOperations.selectByCmHandleFormSourceIds(any(), anyString(), anyString())).thenReturn(List.of(
                    "result1"));
            when(tiesDbOperations.deleteEntity(any(), any(), anyString())).thenReturn(List.of(mockOperationResult));
            doAnswer(new Answer<Void>() {
                @Override
                public Void answer(InvocationOnMock invocation) {
                    List<Consumer<DSLContext>> consumers = invocation.getArgument(0);
                    consumers.forEach(consumer -> {
                        consumer.accept(stream);
                    });
                    return null;
                }
            }).when(tiesDbService).execute(anyList());
            // when
            assertDoesNotThrow(() -> sourceEntityDeleteTopologyProcessor.process(event, "messageKey"));
            // then
            mockedSchemaRegistry.verify(SchemaRegistry::getEntityTypes, times(1));
            verify(tiesDbService, atLeastOnce()).execute(anyList());
            verify(tiesDbOperations, atLeastOnce()).selectByCmHandleFormSourceIds(any(), anyString(), anyString());
            verify(tiesDbOperations, atLeastOnce()).deleteEntity(any(), any(), anyString());

            verify(metrics, times(1)).incrementNumSuccessfullyParsedSourceEntityDeleteCloudEvents();
            verify(metrics, times(1)).incrementNumSuccessfullyPersistedSourceEntityDeleteCloudEvents();
            verify(metrics, times(1)).recordCloudEventSourceEntityDeleteParseTime(anyLong());
            verify(metrics, times(1)).recordCloudEventSourceEntityDeletePersistTime(anyLong());

            verifyNoMoreInteractions(metrics);

            verify(auditLogger).auditLog(eq(ExecutionStatus.SUCCESS), eq("source-entity-delete"), any(CloudEvent.class),
                    anyString(), any());
            verifyNoMoreInteractions(metrics);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
