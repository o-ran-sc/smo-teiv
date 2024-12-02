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
package org.oran.smo.teiv.exposure.decorators.api.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oran.smo.teiv.api.model.OranTeivDecorator;
import org.oran.smo.teiv.exception.TiesException;
import org.oran.smo.teiv.exposure.consumerdata.ConsumerDataOperationRegistry;
import org.oran.smo.teiv.exposure.consumerdata.model.Decorators;
import org.oran.smo.teiv.exposure.consumerdata.operation.DecoratorsOperation;
import org.oran.smo.teiv.exposure.decorators.api.DecoratorsService;
import org.oran.smo.teiv.exposure.spi.ModelRepository;
import org.oran.smo.teiv.schema.ConsumerDataCache;
import org.oran.smo.teiv.schema.YangDataTypes;
import org.oran.smo.teiv.service.models.OperationResult;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.function.Consumer;

import static org.oran.smo.teiv.utils.TiesConstants.TIES_CONSUMER_DATA;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("exposure")
public class DecoratorsServiceImpl implements DecoratorsService {

    private final ModelRepository modelRepository;
    private final ConsumerDataCache consumerDataCache;
    private final ConsumerDataOperationRegistry consumerDataOperationRegistry;

    @Override
    public void update(final OranTeivDecorator oranTeivDecorator) {

        log.debug(String.format("Executing %s on decorators", oranTeivDecorator.getOperation()));

        List<String> entityIds = oranTeivDecorator.getEntityIds();
        if (entityIds == null) {
            entityIds = Collections.emptyList();
        }

        List<String> relationshipIds = oranTeivDecorator.getRelationshipIds();
        if (relationshipIds == null) {
            relationshipIds = Collections.emptyList();
        }

        final Map<String, Object> decoratorNames = oranTeivDecorator.getDecorators();
        Decorators decorators = Decorators.builder().data(decoratorNames).entityIds(entityIds).relationshipIds(
                relationshipIds).build();

        if (oranTeivDecorator.getOperation().equals(OranTeivDecorator.OperationEnum.MERGE)) {
            runMethodSafe(this::validateMerge, decorators);
        } else {
            runMethodSafe(this::validateDelete, decorators);
        }

        final DecoratorsOperation operation = consumerDataOperationRegistry.getDecoratorsOperation(oranTeivDecorator
                .getOperation());

        List<OperationResult> results = operation.execute(decorators);
    }

    public void validateMerge(final Decorators decorators) {
        log.debug(String.format("Validating merging %s", decorators));

        final Map<String, String> problems = checkAvailability(decorators);

        if (!problems.isEmpty()) {
            throw TiesException.invalidDecoratorsException(problems);
        }
    }

    public void validateDelete(final Decorators decorators) {
        log.debug(String.format("Validating deleting %s", decorators));

        for (String decorator : decorators.data().keySet()) {
            final String schemaName = decorator.split(":")[0];
            if (!modelRepository.doesModuleExists(TIES_CONSUMER_DATA, schemaName)) {
                throw TiesException.invalidSchema(schemaName);
            }
        }
    }

    private Map<String, String> checkAvailability(final Decorators decorators) {
        final Map<String, String> invalidKeys = new HashMap<>();

        for (String key : decorators.data().keySet()) {
            invalidKeys.put(key, "is_not_available");
        }

        decorators.data().forEach((key, value) -> {

            if (consumerDataCache.getDecorators().containsKey(key)) {

                final boolean isCompatible = checkCompatibility(key, value);

                if (isCompatible) {
                    invalidKeys.remove(key);
                } else {
                    invalidKeys.put(key, "is_not_compatible");
                }
            }
        });

        return invalidKeys;
    }

    private boolean checkCompatibility(final String key, final Object value) {
        return consumerDataCache.getDecorators().get(key).equals(YangDataTypes.fromRequestDataType(value.getClass()));
    }

    private void runMethodSafe(Consumer<Decorators> consumer, Decorators classifiers) {
        try {
            consumer.accept(classifiers);
        } catch (TiesException ex) {
            log.error("Exception during validation", ex);
            throw ex;
        }
    }
}
