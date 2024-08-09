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
package org.oran.smo.teiv.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.oran.smo.teiv.api.model.OranTeivClassifier;
import org.oran.smo.teiv.api.model.OranTeivDecorator;
import org.oran.smo.teiv.exposure.consumerdata.ConsumerDataOperationRegistry;
import org.oran.smo.teiv.exposure.consumerdata.operation.ClassifiersOperation;
import org.oran.smo.teiv.exposure.consumerdata.operation.DecoratorsOperation;
import org.oran.smo.teiv.exposure.consumerdata.operation.DeleteClassifiersOperation;
import org.oran.smo.teiv.exposure.consumerdata.operation.DeleteDecoratorsOperation;
import org.oran.smo.teiv.exposure.spi.DataRepository;
import org.oran.smo.teiv.exposure.spi.ModelRepository;
import org.oran.smo.teiv.exposure.spi.impl.DataRepositoryImpl;
import org.oran.smo.teiv.exposure.spi.impl.ModelRepositoryImpl;
import org.oran.smo.teiv.schema.MockSchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoaderException;
import org.oran.smo.teiv.schema.SchemaRegistry;

class SchemaCleanUpServiceTest {

    private static final ModelRepository MODEL_REPOSITORY = mock(ModelRepositoryImpl.class);
    private static final DataRepository DATA_REPOSITORY = mock(DataRepositoryImpl.class);
    private static final ConsumerDataOperationRegistry consumerDataOperationRegistry = mock(
            ConsumerDataOperationRegistry.class);
    private static final SchemaCleanUpService schemaCleanUpService = new SchemaCleanUpService(MODEL_REPOSITORY,
            DATA_REPOSITORY, consumerDataOperationRegistry);
    private static final ClassifiersOperation deleteClassifiersOperation = mock(DeleteClassifiersOperation.class);
    private static final DecoratorsOperation deleteDecoratorsOperation = mock(DeleteDecoratorsOperation.class);

    @BeforeAll
    static void setUp() throws SchemaLoaderException {
        new MockSchemaLoader().loadSchemaRegistry();

        when(DATA_REPOSITORY.getDecoratorsForSchema("gnbcucp-gnbcuup-model")).thenReturn(Collections.singleton(
                "gnbcucp-gnbcuup-model:metadata"));
        when(DATA_REPOSITORY.getClassifiersForSchema("gnbcucp-gnbcuup-model")).thenReturn(Collections.singleton(
                "gnbcucp-gnbcuup-model:Weekend"));

        when(DATA_REPOSITORY.getRelationshipIdsForClassifierDeletion(any(), any())).thenReturn(Collections.EMPTY_LIST);
        when(DATA_REPOSITORY.getRelationshipIdsForDecoratorDeletion(any(), any())).thenReturn(Collections.EMPTY_LIST);
        when(DATA_REPOSITORY.getEntityIdsForClassifierDeletion(any(), any())).thenReturn(Collections.EMPTY_LIST);
        when(DATA_REPOSITORY.getEntityIdsForDecoratorDeletion(any(), any())).thenReturn(Collections.EMPTY_LIST);

        when(DATA_REPOSITORY.getRelationshipIdsForClassifierDeletion(SchemaRegistry.getRelationTypeByName(
                "GNBCUUPFUNCTION_REALISED_BY_CLOUDNATIVEAPPLICATION"), Set.of("gnbcucp-gnbcuup-model:Weekend"))).thenReturn(
                        Collections.singletonList(
                                "urn:base64:R05CQ1VVUEZ1bmN0aW9uOkJGRUVBQzJDRTYwMjczQ0IwQTc4MzE5Q0MyMDFBN0ZFOlJFQUxJU0VEX0JZOkNsb3VkTmF0aXZlQXBwbGljYXRpb246QUQ0MkQ5MDQ5N0U5M0QyNzYyMTVERjZEM0I4OTlFMTc="),
                        Collections.EMPTY_LIST);
        when(DATA_REPOSITORY.getRelationshipIdsForDecoratorDeletion(SchemaRegistry.getRelationTypeByName(
                "GNBCUUPFUNCTION_REALISED_BY_CLOUDNATIVEAPPLICATION"), Set.of("gnbcucp-gnbcuup-model:metadata")))
                        .thenReturn(Collections.singletonList(
                                "urn:base64:R05CQ1VVUEZ1bmN0aW9uOkJGRUVBQzJDRTYwMjczQ0IwQTc4MzE5Q0MyMDFBN0ZFOlJFQUxJU0VEX0JZOkNsb3VkTmF0aXZlQXBwbGljYXRpb246QUQ0MkQ5MDQ5N0U5M0QyNzYyMTVERjZEM0I4OTlFMTc="),
                                Collections.EMPTY_LIST);
        when(DATA_REPOSITORY.getRelationshipIdsForClassifierDeletion(SchemaRegistry.getRelationTypeByName(
                "MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION"), Set.of("gnbcucp-gnbcuup-model:Weekend"))).thenReturn(Collections
                        .singletonList(
                                "urn:base64:TWFuYWdlZEVsZW1lbnQ6RTY0MzcxQ0Q0RDEyRUQwQ0VEMjAwREQzQTc1OTE3ODQ6TUFOQUdFUzpHTkJDVVVQRnVuY3Rpb246QkZFRUFDMkNFNjAyNzNDQjBBNzgzMTlDQzIwMUE3RkU="),
                        Collections.EMPTY_LIST);
        when(DATA_REPOSITORY.getRelationshipIdsForDecoratorDeletion(SchemaRegistry.getRelationTypeByName(
                "MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION"), Set.of("gnbcucp-gnbcuup-model:metadata"))).thenReturn(Collections
                        .singletonList(
                                "urn:base64:TWFuYWdlZEVsZW1lbnQ6RTY0MzcxQ0Q0RDEyRUQwQ0VEMjAwREQzQTc1OTE3ODQ6TUFOQUdFUzpHTkJDVVVQRnVuY3Rpb246QkZFRUFDMkNFNjAyNzNDQjBBNzgzMTlDQzIwMUE3RkU="),
                        Collections.EMPTY_LIST);
        when(DATA_REPOSITORY.getEntityIdsForClassifierDeletion(SchemaRegistry.getEntityTypeByName("NRSectorCarrier"), Set
                .of("gnbcucp-gnbcuup-model:Weekend"))).thenReturn(Collections.singletonList(
                        "E49D942C16E0364E1E0788138916D70C"), Collections.EMPTY_LIST);
        when(DATA_REPOSITORY.getEntityIdsForDecoratorDeletion(SchemaRegistry.getEntityTypeByName("NRSectorCarrier"), Set.of(
                "gnbcucp-gnbcuup-model:metadata"))).thenReturn(Collections.singletonList(
                        "E49D942C16E0364E1E0788138916D70C"), Collections.EMPTY_LIST);

        when(consumerDataOperationRegistry.getClassifiersOperation(OranTeivClassifier.OperationEnum.DELETE)).thenReturn(
                deleteClassifiersOperation);
        when(consumerDataOperationRegistry.getDecoratorsOperation(OranTeivDecorator.OperationEnum.DELETE)).thenReturn(
                deleteDecoratorsOperation);
    }

    @Test
    void cleanUpModuleTest() {
        schemaCleanUpService.cleanUpModule("gnbcucp-gnbcuup-model");

        verify(MODEL_REPOSITORY).deleteModuleByName("gnbcucp-gnbcuup-model");
    }
}
