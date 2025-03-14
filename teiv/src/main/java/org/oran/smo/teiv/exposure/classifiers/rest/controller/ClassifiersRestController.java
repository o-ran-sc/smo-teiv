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
package org.oran.smo.teiv.exposure.classifiers.rest.controller;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.HttpServletRequest;

import org.oran.smo.teiv.CustomMetrics;
import org.oran.smo.teiv.api.ClassifiersApi;
import org.oran.smo.teiv.api.model.OranTeivClassifier;
import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.exposure.audit.AuditMapper;
import org.oran.smo.teiv.exposure.audit.LoggerHandler;
import org.oran.smo.teiv.exposure.classifiers.api.ClassifiersService;
import org.oran.smo.teiv.utils.TeivConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(TeivConstants.REQUEST_MAPPING)
@RequiredArgsConstructor
@Profile("exposure")
public class ClassifiersRestController implements ClassifiersApi {

    private final ClassifiersService classifiersService;
    private final CustomMetrics customMetrics;
    private final LoggerHandler loggerHandler;
    private final HttpServletRequest context;

    @Value("${consumer-data.batch-size}")
    private int limit;

    @Override
    @Timed("teiv_exposure_http_update_classifiers_seconds")
    public ResponseEntity<Void> updateClassifier(final String accept, final String contentType,
            final OranTeivClassifier oranTeivClassifier) {
        return runWithFailCheck(() -> {
            if (Optional.ofNullable(oranTeivClassifier.getEntityIds()).orElseGet(Collections::emptyList).size() + Optional
                    .ofNullable(oranTeivClassifier.getRelationshipIds()).orElseGet(Collections::emptyList).size() > limit) {
                throw TeivException.clientException("Limit exceeded",
                        "Number of entities and relationships exceeded the limit");
            }
            runSafeMethod(() -> classifiersService.update(oranTeivClassifier), status -> loggerHandler.logAudit(log,
                    AuditMapper.fromClassifiersRequest(oranTeivClassifier, status).toString(), context));

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }, customMetrics::incrementNumUnsuccessfullyUpdatedClassifiers);
    }

    private <T> T runWithFailCheck(final Supplier<T> supp, final Runnable runnable) {
        try {
            return supp.get();
        } catch (Exception ex) {
            log.error("Exception during service call", ex);
            runnable.run();
            throw ex;
        }
    }

    protected void runSafeMethod(final Runnable runnable, final Consumer<HttpStatus> logAudit) {
        try {
            runnable.run();
            logAudit.accept(HttpStatus.NO_CONTENT);
        } catch (TeivException ex) {
            logAudit.accept(ex.getStatus());
            log.error("Exception during service call", ex);
            throw ex;
        }
    }
}
