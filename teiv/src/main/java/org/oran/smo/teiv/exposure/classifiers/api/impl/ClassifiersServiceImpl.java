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
package org.oran.smo.teiv.exposure.classifiers.api.impl;

import lombok.extern.slf4j.Slf4j;
import org.oran.smo.teiv.api.model.OranTeivClassifier;
import org.oran.smo.teiv.exception.TiesException;
import org.oran.smo.teiv.exposure.classifiers.api.ClassifiersService;
import org.oran.smo.teiv.exposure.consumerdata.ConsumerDataOperationRegistry;
import org.oran.smo.teiv.exposure.consumerdata.model.Classifiers;
import org.oran.smo.teiv.exposure.consumerdata.operation.ClassifiersOperation;
import org.oran.smo.teiv.exposure.spi.ModelRepository;
import org.oran.smo.teiv.schema.ConsumerDataCache;
import org.oran.smo.teiv.service.models.OperationResult;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.function.Consumer;

import static org.oran.smo.teiv.utils.TiesConstants.TIES_CONSUMER_DATA;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassifiersServiceImpl implements ClassifiersService {

    private final ModelRepository modelRepository;
    private final ConsumerDataCache consumerDataCache;
    private final ConsumerDataOperationRegistry consumerDataOperationRegistry;

    @Override
    public void update(final OranTeivClassifier oranTeivClassifier) {

        log.debug(String.format("Executing %s on classifiers", oranTeivClassifier.getOperation()));

        List<String> entityIds = oranTeivClassifier.getEntityIds();
        if (entityIds == null) {
            entityIds = Collections.emptyList();
        }

        List<String> relationshipIds = oranTeivClassifier.getRelationshipIds();
        if (relationshipIds == null) {
            relationshipIds = Collections.emptyList();
        }

        final List<String> classifierNames = oranTeivClassifier.getClassifiers();

        final Classifiers classifierRecords = Classifiers.builder().data(classifierNames).entityIds(entityIds)
                .relationshipIds(relationshipIds).build();

        if (oranTeivClassifier.getOperation().equals(OranTeivClassifier.OperationEnum.MERGE)) {
            runMethodSafe(this::validateMerge, classifierRecords);
        } else {
            runMethodSafe(this::validateDelete, classifierRecords);
        }

        final ClassifiersOperation operation = consumerDataOperationRegistry.getClassifiersOperation(oranTeivClassifier
                .getOperation());

        List<OperationResult> results = operation.execute(classifierRecords);
    }

    private void validateMerge(final Classifiers classifiers) {
        log.debug(String.format("Validating merging %s", classifiers));

        final List<String> problems = checkAvailability(classifiers);

        if (!problems.isEmpty()) {
            throw TiesException.invalidClassifiersException(problems);
        }
    }

    private void validateDelete(final Classifiers classifiers) {
        log.debug(String.format("Validating deleting %s", classifiers));

        for (String classifier : classifiers.data()) {
            final String schemaName = classifier.split(":")[0];
            if (!modelRepository.doesModuleExists(TIES_CONSUMER_DATA, schemaName)) {
                throw TiesException.invalidSchema(schemaName);
            }
        }
    }

    private List<String> checkAvailability(final Classifiers classifiers) {
        final List<String> invalidClassifiers = new ArrayList<>(classifiers.data());

        classifiers.data().stream().filter(classifier -> consumerDataCache.getClassifiers().contains(classifier)).forEach(
                invalidClassifiers::remove);

        return invalidClassifiers;
    }

    private void runMethodSafe(Consumer<Classifiers> consumer, Classifiers classifiers) {
        try {
            consumer.accept(classifiers);
        } catch (TiesException ex) {
            log.error("Exception during validation", ex);
            throw ex;
        }
    }
}
