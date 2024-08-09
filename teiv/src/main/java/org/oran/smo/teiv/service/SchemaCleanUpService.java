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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.oran.smo.teiv.api.model.OranTeivClassifier;
import org.oran.smo.teiv.api.model.OranTeivDecorator;
import org.oran.smo.teiv.exposure.consumerdata.model.Classifiers;
import org.oran.smo.teiv.exposure.consumerdata.model.Decorators;
import org.oran.smo.teiv.exposure.consumerdata.operation.DeleteClassifiersOperation;
import org.oran.smo.teiv.exposure.consumerdata.operation.DeleteDecoratorsOperation;
import org.oran.smo.teiv.exposure.spi.ModelRepository;
import org.oran.smo.teiv.exposure.spi.DataRepository;
import org.oran.smo.teiv.exposure.consumerdata.ConsumerDataOperationRegistry;
import org.oran.smo.teiv.schema.EntityType;
import org.oran.smo.teiv.schema.RelationType;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.service.models.OperationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchemaCleanUpService {
    private final ModelRepository modelRepository;
    private final DataRepository dataRepository;
    private final ConsumerDataOperationRegistry consumerDataOperationRegistry;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Value("${spring.caching.consumer-data-ttl-ms}")
    private long timeToLeave;

    @Async
    public void cleanUpModule(final String name) {

        boolean cleanUpInProgress = true;
        long waitInLoop = timeToLeave;

        while (cleanUpInProgress) {
            try {
                waitInLoop = (long) (1.2 * waitInLoop);
                ScheduledFuture<Boolean> resp = executor.schedule(() -> innerCleanUp(name), waitInLoop,
                        TimeUnit.MILLISECONDS);
                cleanUpInProgress = resp.get();
            } catch (InterruptedException ex) {
                log.error("InterruptedException during [{}] schema deletion: {}", name, ex.getMessage());
                Thread.currentThread().interrupt();
            } catch (Exception ex) {
                log.error("Exception during [{}] schema deletion: {}", name, ex.getMessage(), ex);
            }
        }

        modelRepository.deleteModuleByName(name);
    }

    private boolean innerCleanUp(final String schemaName) {
        log.info("Start cleanup for {} schema", schemaName);

        boolean shouldRerun = false;

        final Set<String> decorators = dataRepository.getDecoratorsForSchema(schemaName);
        final Set<String> classifiers = dataRepository.getClassifiersForSchema(schemaName);

        final List<EntityType> entityTypes = new ArrayList<>(SchemaRegistry.getEntityTypes());
        final List<RelationType> relationTypes = new ArrayList<>(SchemaRegistry.getRelationTypes());

        Collections.shuffle(entityTypes);
        Collections.shuffle(relationTypes);

        for (EntityType e : entityTypes) {
            try {
                deleteClassifiersFromEntity(e, classifiers);
            } catch (Exception ex) {
                shouldRerun = true;
                log.error("Exception during [{}] schema deletion from entity type: {} : {}", schemaName, e, ex.getMessage(),
                        ex);
            }
            try {
                deleteDecoratorsFromEntity(e, decorators);
            } catch (Exception ex) {
                shouldRerun = true;
                log.error("Exception during [{}] schema deletion from entity type: {} : {}", schemaName, e, ex.getMessage(),
                        ex);
            }
        }
        for (RelationType r : relationTypes) {
            try {
                deleteClassifiersFromRelationship(r, classifiers);
            } catch (Exception ex) {
                shouldRerun = true;
                log.error("Exception during [{}] schema deletion from relationship type: {} : {}", schemaName, r, ex
                        .getMessage(), ex);
            }
            try {
                deleteDecoratorsFromRelationship(r, decorators);
            } catch (Exception ex) {
                shouldRerun = true;
                log.error("Exception during [{}] schema deletion from relationship type: {} : {}", schemaName, r, ex
                        .getMessage(), ex);
            }
        }

        log.info("Cleanup for {} schema has been finished", schemaName);

        return shouldRerun;
    }

    private void deleteClassifiersFromEntity(final EntityType entityType, final Set<String> classifiers) {
        Set<String> idsForDeletion;
        do {
            idsForDeletion = new LinkedHashSet<>(dataRepository.getEntityIdsForClassifierDeletion(entityType, classifiers));

            final DeleteClassifiersOperation deleteClassifiersOperation = (DeleteClassifiersOperation) consumerDataOperationRegistry
                    .getClassifiersOperation(OranTeivClassifier.OperationEnum.DELETE);

            List<OperationResult> results = deleteClassifiersOperation.delete(new Classifiers(new ArrayList<>(classifiers),
                    new ArrayList<>(idsForDeletion), new ArrayList<>()), entityType);
        } while (!idsForDeletion.isEmpty());
    }

    private void deleteDecoratorsFromEntity(final EntityType entityType, final Set<String> decorators) {
        Set<String> idsForDeletion;
        do {
            idsForDeletion = new LinkedHashSet<>(dataRepository.getEntityIdsForDecoratorDeletion(entityType, decorators));

            final Map<String, Object> decoratorsMap = new HashMap<>();
            for (String decorator : decorators) {
                decoratorsMap.put(decorator, "");
            }

            final DeleteDecoratorsOperation deleteDecoratorsOperation = (DeleteDecoratorsOperation) consumerDataOperationRegistry
                    .getDecoratorsOperation(OranTeivDecorator.OperationEnum.DELETE);

            List<OperationResult> results = deleteDecoratorsOperation.delete(new Decorators(decoratorsMap, new ArrayList<>(
                    idsForDeletion), new ArrayList<>()), entityType);
        } while (!idsForDeletion.isEmpty());
    }

    private void deleteClassifiersFromRelationship(final RelationType relationType, final Set<String> classifiers) {
        Set<String> idsForDeletion;
        do {
            idsForDeletion = new LinkedHashSet<>(dataRepository.getRelationshipIdsForClassifierDeletion(relationType,
                    classifiers));

            final DeleteClassifiersOperation deleteClassifiersOperation = (DeleteClassifiersOperation) consumerDataOperationRegistry
                    .getClassifiersOperation(OranTeivClassifier.OperationEnum.DELETE);

            List<OperationResult> results = deleteClassifiersOperation.delete(new Classifiers(new ArrayList<>(classifiers),
                    new ArrayList<>(), new ArrayList<>(idsForDeletion)), relationType);
        } while (!idsForDeletion.isEmpty());
    }

    private void deleteDecoratorsFromRelationship(final RelationType relationType, final Set<String> decorators) {
        Set<String> idsForDeletion;
        do {
            idsForDeletion = new LinkedHashSet<>(dataRepository.getRelationshipIdsForDecoratorDeletion(relationType,
                    decorators));

            final Map<String, Object> decoratorsMap = new HashMap<>();
            for (String decorator : decorators) {
                decoratorsMap.put(decorator, "");
            }

            final DeleteDecoratorsOperation deleteDecoratorsOperation = (DeleteDecoratorsOperation) consumerDataOperationRegistry
                    .getDecoratorsOperation(OranTeivDecorator.OperationEnum.DELETE);

            List<OperationResult> results = deleteDecoratorsOperation.delete(new Decorators(decoratorsMap,
                    new ArrayList<>(), new ArrayList<>(idsForDeletion)), relationType);
        } while (!idsForDeletion.isEmpty());
    }
}
