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
package org.oran.smo.teiv.exposure.model.rest.controller;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.oran.smo.teiv.api.SchemasApi;
import org.oran.smo.teiv.api.model.OranTeivSchemaList;
import org.oran.smo.teiv.exception.TiesException;
import org.oran.smo.teiv.exposure.audit.LoggerHandler;
import org.oran.smo.teiv.exposure.model.api.ModelService;
import org.oran.smo.teiv.exposure.utils.RequestDetails;
import org.oran.smo.teiv.exposure.utils.RequestValidator;
import org.oran.smo.teiv.utils.TiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(TiesConstants.REQUEST_MAPPING)
@RequiredArgsConstructor
public class ModelController implements SchemasApi {

    private final ModelService modelService;
    private final RequestValidator requestValidator;
    private final Logger logger = LoggerFactory.getLogger(ModelController.class);
    private final LoggerHandler loggerHandler;
    private final HttpServletRequest context;

    @Override
    public ResponseEntity<Void> createSchema(String accept, String contentType, MultipartFile file) {
        requestValidator.validateYangFile(file);
        runSafeMethod(() -> modelService.createModule(file), "create");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<OranTeivSchemaList> getSchemas(@NotNull final String accept, @Valid final String domain,
            @Min(0) @Valid final Integer offset, @Min(1) @Max(500) @Valid final Integer limit) {
        final RequestDetails.RequestDetailsBuilder builder = RequestDetails.builder().basePath("/schemas").offset(offset)
                .limit(limit);
        if (!Objects.isNull(domain)) {
            builder.queryParam("domain", domain);
        }

        return new ResponseEntity<>(modelService.getModulesByDomain(domain, builder.build()), HttpStatus.OK);
    }

    @Override
    @SneakyThrows
    public ResponseEntity<String> getSchemaByName(@NotNull final String accept, final String schemaName) {
        final String module = modelService.getModuleContentByName(schemaName);
        return new ResponseEntity<>(module, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteSchema(String accept, String schemaName) {
        runSafeMethod(() -> modelService.deleteConsumerModule(schemaName), "delete");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    protected void runSafeMethod(final Runnable runnable, String operation) {
        try {
            runnable.run();
            loggerHandler.logAudit(logger, String.format("%s schema (successful)", StringUtils.capitalize(operation)),
                    context);
        } catch (TiesException ex) {
            loggerHandler.logAudit(logger, String.format("%s schema (failed)", StringUtils.capitalize(operation)), context);
            log.error("Exception during service call", ex);
            throw ex;
        }
    }

}
