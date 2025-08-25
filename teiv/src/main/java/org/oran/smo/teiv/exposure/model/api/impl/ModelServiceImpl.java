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
package org.oran.smo.teiv.exposure.model.api.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oran.smo.teiv.api.model.*;
import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.exception.YangModelException;
import org.oran.smo.teiv.exposure.model.api.ModelService;
import org.oran.smo.teiv.exposure.spi.ModelRepository;
import org.oran.smo.teiv.exposure.spi.Module;
import org.oran.smo.teiv.exposure.spi.ModuleStatus;
import org.oran.smo.teiv.exposure.utils.RequestDetails;
import org.oran.smo.teiv.schema.ConsumerDataCache;
import org.oran.smo.teiv.service.SchemaCleanUpService;
import org.oran.smo.teiv.utils.YangParser;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.oran.smo.teiv.exposure.utils.PaginationUtil.getViableLimit;
import static org.oran.smo.teiv.utils.TeivConstants.CLASSIFIERS;
import static org.oran.smo.teiv.utils.TeivConstants.DECORATORS;
import static org.oran.smo.teiv.utils.TeivConstants.INVALID_SCHEMA;
import static org.oran.smo.teiv.utils.TeivConstants.USER_DEFINED_SCHEMAS;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("exposure")
public class ModelServiceImpl implements ModelService {
    private static final String CONTENT_HREF = "%s/%s/content";

    private final ModelRepository modelRepository;
    private final YangParser yangParser;
    private final ConsumerDataCache consumerDataCaches;
    private final SchemaCleanUpService schemaCleanUpService;

    @Override
    public String createModule(final MultipartFile yangFile) {
        log.debug("Create module with file: {}", yangFile);
        final Map<String, Object> result;
        final String content;

        try {
            result = yangParser.validateSchemasYang(yangFile);
            content = Base64.getEncoder().encodeToString(yangFile.getBytes());
        } catch (YangModelException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String moduleName = (String) result.get("moduleName");
        String revision = (String) result.get("revision");
        List<String> classifiers = (List<String>) result.get(CLASSIFIERS);
        Map<String, String> decorators = (Map<String, String>) result.get(DECORATORS);
        if (classifiers.isEmpty() && decorators.isEmpty()) {
            log.warn("No classifiers and decorators found in module {} ", moduleName);
            throw TeivException.invalidFileInput(INVALID_SCHEMA);
        }
        modelRepository.createConsumerDataModule(Module.builder().name(moduleName).content(content).revision(revision)
                .ownerAppId("APP").status(ModuleStatus.IN_USAGE).build(), classifiers, decorators);
        consumerDataCaches.emptyConsumerDataCaches();
        return moduleName;

    }

    @Override
    public OranTeivSchemas getModulesByDomain(final String domain, final RequestDetails requestDetails) {
        log.debug("Get modules with domain : {}", domain);
        final List<Module> modulesByDomain = modelRepository.getModules().stream().filter(s -> (domain == null) || (s
                .getDomain() != null && s.getDomain().matches(domain))).toList();
        int totalCount = modulesByDomain.size();

        final List<OranTeivSchema> items = modulesByDomain.stream().skip(requestDetails.getOffset()).limit(getViableLimit(
                requestDetails.getOffset(), requestDetails.getLimit(), totalCount)).map(module -> OranTeivSchema.builder()
                        .name(module.getName()).domain(module.getDomain() == null ? "" : module.getDomain()).revision(module
                                .getRevision()).content(OranTeivHref.builder().href(String.format(CONTENT_HREF,
                                        requestDetails.getBasePath(), module.getName())).build()).build()).toList();

        return OranTeivSchemas.builder().items(items).build();
    }

    @Override
    public OranTeivUserDefinedSchemas getUserDefinedModulesByDomain(final RequestDetails requestDetails) {
        final List<Module> modulesByDomain = modelRepository.getUserDefinedModules();
        int totalCount = modulesByDomain.size();

        final List<OranTeivUserDefinedSchema> items = modulesByDomain.stream().skip(requestDetails.getOffset()).limit(
                getViableLimit(requestDetails.getOffset(), requestDetails.getLimit(), totalCount)).map(
                        module -> OranTeivUserDefinedSchema.builder().name(module.getName()).domain(module
                                .getDomain() == null ? "" : module.getDomain()).revision(module.getRevision()).content(
                                        OranTeivHref.builder().href(String.format(CONTENT_HREF, requestDetails
                                                .getBasePath(), module.getName())).build()).build()).toList();

        return OranTeivUserDefinedSchemas.builder().items(items).build();
    }

    @Override
    public String getModuleContentByName(final String name) {
        log.debug("Get {} module content", name);
        return Optional.ofNullable(modelRepository.getModuleContentByName(name)).map(content -> new String(Base64
                .getDecoder().decode(content), StandardCharsets.UTF_8)).orElseGet(() -> {
                    log.warn("No schema found with name: {}", name);
                    throw TeivException.invalidSchema(name);
                });
    }

    @Override
    public String getUserDefinedModuleContentByName(final String name) {
        log.debug("Get {} user defined module content", name);
        return Optional.ofNullable(modelRepository.getUserDefinedModuleContentByName(name)).map(content -> new String(Base64
                .getDecoder().decode(content), StandardCharsets.UTF_8)).orElseGet(() -> {
                    log.warn("No user defined schema found with name: {}", name);
                    throw TeivException.invalidSchema(name);
                });
    }

    @Override
    public OranTeivUserDefinedSchema getUserDefinedModuleByName(final String name) {
        log.debug("Get {} user defined module ", name);
        return modelRepository.getConsumerModuleByName(name).map(module -> OranTeivUserDefinedSchema.builder().name(module
                .getName()).domain(module.getDomain() == null ? "" : module.getDomain()).revision(module.getRevision())
                .content(OranTeivHref.builder().href(String.format(CONTENT_HREF, USER_DEFINED_SCHEMAS, module.getName()))
                        .build()).build()).orElseThrow(() -> {
                            log.warn("No module found with name: {}", name);
                            return TeivException.invalidSchema(name);
                        });
    }

    @Override
    public void deleteConsumerModule(final String name) {
        log.debug("Delete module: {}", name);
        modelRepository.getConsumerModuleByName(name).ifPresentOrElse(module -> {
            modelRepository.updateModuleStatus(name, ModuleStatus.DELETING);
            schemaCleanUpService.cleanUpModule(name);
        }, () -> {
            log.warn("No module found with name: {}", name);
            throw TeivException.invalidSchema(name);
        });
    }

}
