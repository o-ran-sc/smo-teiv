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
package org.oran.smo.teiv.utils.yangparser;

import static org.oran.smo.teiv.utils.TeivConstants.CLASSIFIERS;
import static org.oran.smo.teiv.utils.TeivConstants.SEMICOLON_SEPARATION;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.multipart.MultipartFile;

import org.oran.smo.yangtools.parser.ParserExecutionContext;
import org.oran.smo.yangtools.parser.YangDeviceModel;
import org.oran.smo.yangtools.parser.findings.FindingFilterPredicate;
import org.oran.smo.yangtools.parser.findings.ModuleAndFindingTypeAndSchemaNodePathFilterPredicate;
import org.oran.smo.yangtools.parser.input.ByteArrayYangInput;
import org.oran.smo.yangtools.parser.model.ConformanceType;
import org.oran.smo.yangtools.parser.model.YangModel;
import org.oran.smo.yangtools.parser.model.yangdom.YangDomElement;
import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.exception.YangParsingException;
import org.oran.smo.teiv.exception.YangSchemaException;

public class ExposureYangParser extends YangModelLoader {
    private static final List<YangModel> yangModelInputs = new CopyOnWriteArrayList<>();

    private static final Set<FindingFilterPredicate> FILTER_PREDICATES = Set.of(
            ModuleAndFindingTypeAndSchemaNodePathFilterPredicate.fromString("ietf-*;*;*"),
            ModuleAndFindingTypeAndSchemaNodePathFilterPredicate.fromString("_3gpp*;*;*"));

    public static void loadAndValidateModels() throws YangSchemaException {
        ParserExecutionContext context = createParserExecutionContext(List.of(), List.of(), FILTER_PREDICATES);
        context.setFailFast(false);
        final List<YangModel> yangModels = YangModelLoader.loadModulesFromSchemaRegistry();
        List<YangModel> models = YangModelLoader.parseModelsIntoContext(context, yangModels);
        yangModelInputs.clear();
        yangModelInputs.addAll(models);
    }

    public static Map<String, Object> validateSchemasYang(final MultipartFile file) throws YangParsingException {
        ParserExecutionContext context = createParserExecutionContext(List.of(), List.of(), FILTER_PREDICATES);
        context.setFailFast(false);

        List<YangModel> localYangModelInputs = new ArrayList<>();
        YangModel inputYangModel = executeIOOperation(() -> new YangModel(new ByteArrayYangInput(file.getBytes(),
                "requestYang"), ConformanceType.IMPORT));
        localYangModelInputs.add(inputYangModel);
        localYangModelInputs.addAll(yangModelInputs);

        new YangDeviceModel("R1 Schema validation model").parseIntoYangModels(context, localYangModelInputs);

        Map<String, Object> result = new ConcurrentHashMap<>();
        result.put("classifiers", getClassifiers(inputYangModel.getYangDomDocumentRoot().getChildren().get(0).getChildren(),
                findPrefixName(inputYangModel)));
        result.put("decorators", getDecorators(inputYangModel.getYangDomDocumentRoot().getChildren().get(0).getChildren()));
        result.put("moduleName", inputYangModel.getModuleIdentity().getModuleName());
        result.put("revision", inputYangModel.getModuleIdentity().getRevision());
        return result;
    }

    private static String findPrefixName(YangModel yangModel) {
        return yangModel.getYangDomDocumentRoot().getChildren().get(0).getChildren().stream().filter(child -> child
                .getValue().contains("common-yang-types")).findFirst().map(child -> child.getChildren().get(0).getValue())
                .orElse(null);
    }

    private static List<String> getClassifiers(List<YangDomElement> elements, String prefixName) {

        List<String> result = new ArrayList<>();
        String formattedName = String.format(SEMICOLON_SEPARATION, prefixName, CLASSIFIERS);

        elements.stream().filter(element -> element.getName().contains("identity")).forEach(element -> {
            String childValue = element.getChildren().get(0).getValue();
            String classifier;
            if (!childValue.equals(formattedName)) {
                List<YangDomElement> domElement = element.getParentElement().getChildren().stream().filter(parent -> parent
                        .getName().contains("identity")).filter(parent -> parent.getValue().equals(childValue) && parent
                                .getChildren().get(0).getValue().equals(formattedName)).toList();

                if (domElement.isEmpty()) {
                    throw TeivException.invalidSchema("Invalid classifier " + element.getValue());
                } else {
                    classifier = element.getValue();
                }
            } else {
                classifier = element.getValue();
            }
            if (result.contains(classifier)) {
                throw TeivException.duplicateEntryForClassifiers(classifier);
            } else {
                result.add(classifier);
            }
        });
        return result;
    }

    private static Map<String, String> getDecorators(final List<YangDomElement> elements) {
        Map<String, String> resultMap = new HashMap<>();
        elements.stream().filter(key -> key.getValue().contains("decorators")).flatMap(key -> key.getChildren().stream())
                .forEach(yangDomElement -> {
                    String key = yangDomElement.getValue();
                    String value = switch (yangDomElement.getChildren().get(0).getValue()) {
                        case "string" -> "TEXT";
                        case "boolean" -> "BOOLEAN";
                        case "int8", "int16", "int32", "int64", "uint8", "uint16", "uint32", "uint64" -> "INT";
                        default -> throw TeivException.invalidFileInput("Invalid data type");
                    };
                    if (resultMap.putIfAbsent(key, value) != null) {
                        throw TeivException.duplicateEntryForDecorators(key);
                    }
                });
        return resultMap;
    }

}
