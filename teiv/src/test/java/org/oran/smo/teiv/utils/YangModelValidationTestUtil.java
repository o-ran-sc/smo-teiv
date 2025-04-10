/*
 *  ============LICENSE_START=======================================================
 *  Modifications Copyright (C) 2025 OpenInfra Foundation Europe
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

package org.oran.smo.teiv.utils;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.oran.smo.teiv.exception.YangException;
import org.oran.smo.teiv.exception.YangSchemaException;
import org.oran.smo.teiv.exposure.spi.Module;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.utils.yangparser.IngestionYangParser;
import org.oran.smo.teiv.utils.yangparser.YangModelLoader;
import org.oran.smo.yangtools.parser.input.StringYangInput;
import org.oran.smo.yangtools.parser.model.ConformanceType;
import org.oran.smo.yangtools.parser.model.YangModel;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;

public class YangModelValidationTestUtil {
    public static void mockLoadAndValidateModels() throws YangException, IOException {
        try (MockedStatic<YangModelLoader> mockedLoader = Mockito.mockStatic(YangModelLoader.class)) {
            List<String> modulesImport = collectYangFilesNameFromPath("src/main/resources/models/import");
            List<String> modulesImplement = collectYangFilesNameFromPath("src/main/resources/models");
            modulesImplement.addAll(collectYangFilesNameFromPath("src/test/resources/yang/models"));

            Map<String, Module> modules = SchemaRegistry.getModuleRegistry();
            if (modules == null) {
                throw YangSchemaException.failedToLoadSchema();
            }

            List<String> implementList = new ArrayList<>();
            List<String> importList = new ArrayList<>();

            modules.values().stream().filter(module -> module.getContent() != null).forEach(module -> {
                String content = new String(Base64.getDecoder().decode(module.getContent()), StandardCharsets.UTF_8);
                if (modulesImport.contains(module.getName())) {
                    importList.add(content);
                }
                if (modulesImplement.contains(module.getName())) {
                    implementList.add(content);
                }
            });

            List<YangModel> models = createYangModels(importList, implementList);
            mockedLoader.when(() -> YangModelLoader.loadModulesFromSchemaRegistry()).thenReturn(models);
            mockedLoader.when(() -> YangModelLoader.createParserExecutionContext(any(), any(), any())).thenCallRealMethod();
            mockedLoader.when(() -> YangModelLoader.parseModelsIntoContext(any(), any())).thenReturn(models);
            IngestionYangParser.loadModels();
        }
    }

    private static List<String> collectYangFilesNameFromPath(String resourcePath) throws IOException {
        File directory = new File(resourcePath);
        if (directory.isFile()) {
            return List.of(StringUtils.stripFilenameExtension(directory.getName().toLowerCase(Locale.ROOT)));
        }
        File[] files = directory.listFiles();
        if (files == null) {
            throw new IOException();
        }
        return Arrays.stream(files).filter(File::isFile).filter(file -> file.getName().endsWith(".yang")).map(
                file -> StringUtils.stripFilenameExtension(file.getName().toLowerCase(Locale.ROOT))).collect(Collectors
                        .toList());
    }

    public static List<YangModel> createYangModels(List<String> importList, List<String> implementList) {
        List<YangModel> yangModelInputs = new ArrayList<>();
        yangModelInputs.addAll(createYangModels(importList, ConformanceType.IMPORT));
        yangModelInputs.addAll(createYangModels(implementList, ConformanceType.IMPLEMENT));
        return yangModelInputs;
    }

    private static List<YangModel> createYangModels(List<String> resources, ConformanceType conformanceType) {
        return resources.stream().map(resource -> new YangModel(new StringYangInput(resource, resource,
                "application/yang-data+json"), conformanceType)).filter(Objects::nonNull).toList();
    }
}
