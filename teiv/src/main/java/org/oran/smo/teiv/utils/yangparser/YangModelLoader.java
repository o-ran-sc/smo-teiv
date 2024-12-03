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
package org.oran.smo.teiv.utils.yangparser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.oran.smo.yangtools.parser.CustomProcessor;
import org.oran.smo.yangtools.parser.ParserExecutionContext;
import org.oran.smo.yangtools.parser.YangDeviceModel;
import org.oran.smo.yangtools.parser.findings.FindingFilterPredicate;
import org.oran.smo.yangtools.parser.findings.FindingsManager;
import org.oran.smo.yangtools.parser.findings.ModifyableFindingSeverityCalculator;
import org.oran.smo.yangtools.parser.input.StringYangInput;
import org.oran.smo.yangtools.parser.model.ConformanceType;
import org.oran.smo.yangtools.parser.model.YangModel;
import org.oran.smo.yangtools.parser.model.statements.StatementClassSupplier;
import org.oran.smo.teiv.exception.YangParsingException;
import org.oran.smo.teiv.exception.YangSchemaException;
import org.oran.smo.teiv.exposure.spi.Module;
import org.oran.smo.teiv.schema.SchemaRegistry;

public abstract class YangModelLoader {
    private static final Set<String> MODULES_TO_IMPORT = Set.of("_3gpp-common-yang-extensions", "_3gpp-common-yang-types",
            "ietf-geo-location", "ietf-inet-types", "ietf-yang-types");

    private static final Set<String> MODULES_TO_IMPLEMENT = Set.of("o-ran-smo-teiv-common-yang-extensions",
            "o-ran-smo-teiv-common-yang-types", "o-ran-smo-teiv-equipment", "o-ran-smo-teiv-oam", "o-ran-smo-teiv-ran",
            "o-ran-smo-teiv-rel-equipment-ran", "o-ran-smo-teiv-rel-oam-ran");

    public static ParserExecutionContext createParserExecutionContext(List<StatementClassSupplier> extensions,
            List<CustomProcessor> customProcessors, Set<FindingFilterPredicate> filterPredicates) {
        FindingsManager findingsManager = new FindingsManager(new ModifyableFindingSeverityCalculator());
        ParserExecutionContext context = new ParserExecutionContext(findingsManager, extensions, customProcessors);
        filterPredicates.forEach(findingsManager::addFilterPredicate);
        context.setIgnoreImportedProtocolAccessibleObjects(true);
        return context;
    }

    public static List<YangModel> loadModulesFromSchemaRegistry() throws YangSchemaException {
        Map<String, Module> modules = SchemaRegistry.getModuleRegistry();
        if (modules == null) {
            throw YangSchemaException.failedToLoadSchema();
        }
        List<String> implementList = new ArrayList<>();
        List<String> importList = new ArrayList<>();
        modules.values().stream().filter(module -> module.getContent() != null).forEach(module -> {
            String content = new String(Base64.getDecoder().decode(module.getContent()), StandardCharsets.UTF_8);
            if (MODULES_TO_IMPORT.contains(module.getName())) {
                importList.add(content);
            } else if (MODULES_TO_IMPLEMENT.contains(module.getName())) {
                implementList.add(content);
            }
        });
        return createYangModels(importList, implementList);
    }

    public static List<YangModel> parseModelsIntoContext(ParserExecutionContext context, List<YangModel> yangModelInputs)
            throws YangSchemaException {
        if (yangModelInputs.isEmpty()) {
            throw YangSchemaException.failedToLoadModels();
        }
        new YangDeviceModel("Schema validation model").parseIntoYangModels(context, yangModelInputs);
        return yangModelInputs;
    }

    public static List<YangModel> createYangModels(List<String> importList, List<String> implementList) {
        List<YangModel> yangModelInputs = new ArrayList<>();
        yangModelInputs.addAll(createYangModels(importList, ConformanceType.IMPORT));
        yangModelInputs.addAll(createYangModels(implementList, ConformanceType.IMPLEMENT));
        return yangModelInputs;
    }

    private static List<YangModel> createYangModels(List<String> resources, ConformanceType conformanceType) {
        return resources.stream().map(resource -> new YangModel(new StringYangInput(resource, "",
                "application/yang-data+json"), conformanceType)).filter(Objects::nonNull).toList();
    }

    public static <T> T executeIOOperation(IOSupplier<T> operation) throws YangParsingException {
        try {
            return operation.get();
        } catch (IOException e) {
            throw YangParsingException.failedIOExecution(e);
        }
    }

    @FunctionalInterface
    public interface IOSupplier<T> {
        T get() throws IOException;
    }
}
