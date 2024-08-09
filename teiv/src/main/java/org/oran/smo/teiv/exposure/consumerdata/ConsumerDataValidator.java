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
package org.oran.smo.teiv.exposure.consumerdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oran.smo.teiv.exception.TiesException;
import org.oran.smo.teiv.exposure.consumerdata.model.Classifiers;
import org.oran.smo.teiv.exposure.consumerdata.model.Decorators;
import org.oran.smo.teiv.schema.ConsumerDataCache;
import org.oran.smo.teiv.schema.YangDataTypes;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumerDataValidator {

    private final ConsumerDataCache consumerDataCache;

    public void validate(final Classifiers classifiers) {
        log.debug(String.format("Validating %s", classifiers));

        final List<String> problems = checkAvailability(classifiers);

        if (!problems.isEmpty()) {
            throw TiesException.invalidClassifiersException(problems);
        }
    }

    private List<String> checkAvailability(final Classifiers classifiers) {
        final List<String> invalidClassifiers = new ArrayList<>(classifiers.data());

        classifiers.data().stream().filter(classifier -> !consumerDataCache.getValidClassifiers(classifier).isEmpty())
                .forEach(invalidClassifiers::remove);

        return invalidClassifiers;
    }

    public void validate(final Decorators decorators) {
        log.debug(String.format("Validating %s", decorators));

        final Map<String, String> problems = checkAvailability(decorators);

        if (!problems.isEmpty()) {
            throw TiesException.invalidDecoratorsException(problems);
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
}
