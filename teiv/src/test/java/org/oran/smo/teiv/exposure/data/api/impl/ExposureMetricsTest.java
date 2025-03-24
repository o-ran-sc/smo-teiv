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
package org.oran.smo.teiv.exposure.data.api.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.oran.smo.teiv.utils.TeivTestConstants.APPLICATION_JSON;

import java.util.Collections;
import java.util.function.Supplier;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.oran.smo.teiv.CustomMetrics;
import org.oran.smo.teiv.api.model.OranTeivClassifier;
import org.oran.smo.teiv.api.model.OranTeivDecorator;
import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.exposure.audit.LoggerHandler;
import org.oran.smo.teiv.exposure.data.api.DataService;
import org.oran.smo.teiv.exposure.data.rest.controller.DataController;
import org.oran.smo.teiv.exposure.classifiers.api.ClassifiersService;
import org.oran.smo.teiv.exposure.classifiers.rest.controller.ClassifiersRestController;
import org.oran.smo.teiv.exposure.decorators.api.DecoratorsService;
import org.oran.smo.teiv.exposure.decorators.rest.controller.DecoratorsRestController;
import org.oran.smo.teiv.exposure.utils.RequestDetails;
import org.oran.smo.teiv.exposure.utils.RequestValidator;
import org.oran.smo.teiv.schema.MockSchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoaderException;

class ExposureMetricsTest {

    private DataService mockedDataService;
    private ClassifiersService mockedClassifiersService;
    private DecoratorsService mockedDecoratorsService;
    private RequestValidator mockedRequestValidator;
    private CustomMetrics underTest;
    private DataController dataController;
    private ClassifiersRestController classifiersRestController;
    private DecoratorsRestController decoratorsRestController;
    private static final String DOMAIN_NAME = "RAN";
    private static final String ENTITY_ID = "5A548EA9D166341776CA0695837E55D8";
    private static final String ENTITY_NAME = "GNBDUFunction";
    private static final String RELATION_TYPE = "GNBDUFUNCTION_PROVIDES_NRCELLDU";

    @BeforeEach
    void setUp() throws SchemaLoaderException {
        SchemaLoader mockedSchemaLoader = new MockSchemaLoader();
        mockedSchemaLoader.loadSchemaRegistry();
        mockedRequestValidator = mock(RequestValidator.class);
        mockedDataService = mock(DataService.class);
        mockedClassifiersService = mock(ClassifiersService.class);
        mockedDecoratorsService = mock(DecoratorsService.class);
        underTest = new CustomMetrics(new SimpleMeterRegistry());
        dataController = new DataController(mockedRequestValidator, mockedDataService, underTest);
        classifiersRestController = new ClassifiersRestController(mockedClassifiersService, underTest, mock(
                LoggerHandler.class), mock(HttpServletRequest.class));
        decoratorsRestController = new DecoratorsRestController(mockedDecoratorsService, underTest, mock(
                LoggerHandler.class), mock(HttpServletRequest.class));
    }

    @Test
    void testGetRelationshipsByEntityIdFailMetrics() {
        when(mockedDataService.getAllRelationshipsForObjectId(eq(DOMAIN_NAME), eq(ENTITY_NAME), eq(ENTITY_ID), anyString(),
                anyString(), any(RequestDetails.class))).thenThrow(TeivException.class);
        assertMetrics(() -> dataController.getAllRelationshipsForEntityId(APPLICATION_JSON, DOMAIN_NAME, ENTITY_NAME,
                ENTITY_ID, "", "", 0, 1), underTest.getNumUnsuccessfullyExposedRelationshipsByEntityId()::count);
    }

    @Test
    void testGetEntityByIdFailMetrics() {
        when(mockedDataService.getEntityById(ENTITY_NAME, ENTITY_ID)).thenThrow(TeivException.class);
        assertMetrics(() -> dataController.getTopologyById(APPLICATION_JSON, DOMAIN_NAME, ENTITY_NAME, ENTITY_ID), underTest
                .getNumUnsuccessfullyExposedEntityById()::count);
    }

    @Test
    void testGetEntitiesByTypeFailMetrics() {
        when(mockedDataService.getTopologyByType(anyString(), eq(ENTITY_NAME), anyString(), anyString(), any(
                RequestDetails.class))).thenThrow(TeivException.class);
        assertMetrics(() -> dataController.getTopologyByEntityTypeName(APPLICATION_JSON, DOMAIN_NAME, ENTITY_NAME, "", "",
                0, 1), underTest.getNumUnsuccessfullyExposedEntitiesByType()::count);
    }

    @Test
    void testGetEntitiesByDomainFailMetrics() {
        when(mockedDataService.getEntitiesByDomain(eq(DOMAIN_NAME), anyString(), anyString(), any(RequestDetails.class)))
                .thenThrow(TeivException.class);
        assertMetrics(() -> dataController.getEntitiesByDomain(APPLICATION_JSON, DOMAIN_NAME, "", "", 0, 1), underTest
                .getNumUnsuccessfullyExposedEntitiesByDomain()::count);
    }

    @Test
    void testGetRelationshipByIdFailMetrics() {
        when(mockedDataService.getRelationshipById(RELATION_TYPE, ENTITY_ID)).thenThrow(TeivException.class);
        assertMetrics(() -> dataController.getRelationshipById(APPLICATION_JSON, DOMAIN_NAME, RELATION_TYPE, ENTITY_ID),
                underTest.getNumUnsuccessfullyExposedRelationshipById()::count);
    }

    @Test
    void testGetRelationshipsByTypeFailMetrics() {
        when(mockedDataService.getRelationshipsByType(anyString(), eq(RELATION_TYPE), anyString(), anyString(), any(
                RequestDetails.class))).thenThrow(TeivException.class);
        assertMetrics(() -> dataController.getRelationshipsByType(APPLICATION_JSON, DOMAIN_NAME, RELATION_TYPE, "", "", 0,
                1), underTest.getNumUnsuccessfullyExposedRelationshipsByType()::count);
    }

    @Test
    void testGetRelationshipTypesFailMetrics() {
        when(mockedDataService.getTopologyRelationshipTypes(eq(DOMAIN_NAME), any(RequestDetails.class))).thenThrow(
                TeivException.class);
        assertMetrics(() -> dataController.getTopologyRelationshipTypes(APPLICATION_JSON, DOMAIN_NAME, 0, 1), underTest
                .getNumUnsuccessfullyExposedRelationshipTypes()::count);
    }

    @Test
    void testGetDomainTypesFailMetrics() {
        when(mockedDataService.getDomainTypes(any(RequestDetails.class))).thenThrow(TeivException.class);
        assertMetrics(() -> dataController.getAllDomains(APPLICATION_JSON, 0, 1), underTest
                .getNumUnsuccessfullyExposedDomainTypes()::count);
    }

    @Test
    void testGetEntityTypesFailMetrics() {
        when(mockedDataService.getTopologyEntityTypes(eq(DOMAIN_NAME), any(RequestDetails.class))).thenThrow(
                TeivException.class);
        assertMetrics(() -> dataController.getTopologyEntityTypes(APPLICATION_JSON, DOMAIN_NAME, 0, 1), underTest
                .getNumUnsuccessfullyExposedEntityTypes()::count);
    }

    @Test
    void testUpdateClassifiersFailMetrics() {
        doThrow(TeivException.invalidClassifiersException(Collections.emptyList())).when(mockedClassifiersService).update(
                any(OranTeivClassifier.class));
        assertMetrics(() -> classifiersRestController.updateClassifier(APPLICATION_JSON, APPLICATION_JSON,
                OranTeivClassifier.builder().operation(OranTeivClassifier.OperationEnum.MERGE).build()), underTest
                        .getNumUnsuccessfullyUpdatedClassifiers()::count);
    }

    @Test
    void testUpdateDecoratorsFailMetrics() {
        doThrow(TeivException.invalidClassifiersException(Collections.emptyList())).when(mockedDecoratorsService).update(
                any(OranTeivDecorator.class));
        assertMetrics(() -> decoratorsRestController.updateDecorator(APPLICATION_JSON, APPLICATION_JSON, OranTeivDecorator
                .builder().operation(OranTeivDecorator.OperationEnum.MERGE).build()), underTest
                        .getNumUnsuccessfullyUpdatedDecorators()::count);
    }

    private <T> void assertMetrics(Supplier<T> controllerMethod, Supplier<T> failerSupplier) {
        Assertions.assertThrowsExactly(TeivException.class, controllerMethod::get);
        Assertions.assertEquals(1.0, failerSupplier.get());
    }
}
