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
package org.oran.smo.teiv.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.exception.YangModelException;
import org.oran.smo.yangtools.parser.data.YangData;
import org.oran.smo.yangtools.parser.findings.FindingSeverity;
import org.oran.smo.yangtools.parser.findings.ModuleAndFindingTypeAndSchemaNodePathFilterPredicate;
import org.oran.smo.yangtools.parser.input.BufferedStreamYangInput;
import org.oran.smo.yangtools.parser.input.ByteArrayYangInput;
import org.oran.smo.yangtools.parser.input.YangInput;
import org.oran.smo.yangtools.parser.model.YangModel;
import org.oran.smo.yangtools.parser.model.statements.StatementModuleAndName;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.oran.smo.yangtools.parser.model.yangdom.YangDomElement;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.oran.smo.yangtools.parser.ParserExecutionContext;
import org.oran.smo.yangtools.parser.YangDeviceModel;
import org.oran.smo.yangtools.parser.data.dom.YangDataDomDocumentRoot;
import org.oran.smo.yangtools.parser.findings.Finding;
import org.oran.smo.yangtools.parser.findings.FindingsManager;
import org.oran.smo.yangtools.parser.findings.ModifyableFindingSeverityCalculator;
import org.oran.smo.yangtools.parser.model.ConformanceType;
import org.oran.smo.yangtools.parser.model.statements.AbstractStatement;
import org.oran.smo.teiv.api.model.OranTeivSchema;
import org.oran.smo.teiv.api.model.OranTeivHref;
import org.springframework.web.multipart.MultipartFile;

import static org.oran.smo.teiv.utils.TeivConstants.CLASSIFIERS;
import static org.oran.smo.teiv.utils.TeivConstants.INVALID_SCHEMA;
import static org.oran.smo.teiv.utils.TeivConstants.SEMICOLON_SEPARATION;

@Service
@Slf4j
public class YangParser {

    /**
     * Extracting data from all yang schemas, return required information
     *
     * @return yangDeviceModel
     */
    public YangDeviceModel extractYangData() {
        YangDeviceModel yangDeviceModel = new YangDeviceModel("r1");
        final List<YangModel> yangModels = new ArrayList<>();
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(this.getClass().getClassLoader());
            Resource[] yangResources = resolver.getResources("classpath:models/*.yang");
            for (Resource yangResource : yangResources) {
                yangModels.add(new YangModel(new ByteArrayYangInput(yangResource.getContentAsByteArray(), Objects
                        .requireNonNull(yangResource.getFilename())), ConformanceType.IMPORT));
            }
        } catch (final IOException ex) {
            log.error("Unable to load schemas", ex);
        }

        final ModifyableFindingSeverityCalculator severityCalculator = new ModifyableFindingSeverityCalculator();
        final FindingsManager findingsManager = new FindingsManager(severityCalculator);
        final ParserExecutionContext context = new ParserExecutionContext(findingsManager);
        context.setIgnoreImportedProtocolAccessibleObjects(true);
        context.setFailFast(false);
        yangDeviceModel.parseIntoYangModels(context, yangModels);
        return yangDeviceModel;
    }

    /**
     * Validating yang file from /schemas POST request
     *
     * @return map of validated and parsed yang
     */
    public Map<String, Object> validateSchemasYang(MultipartFile file) throws YangModelException {

        Map<String, Object> result = new HashMap<>();

        YangDeviceModel yangDeviceModel = new YangDeviceModel("r1");
        YangModel inputYangModel = null;
        final List<YangModel> yangModels = new ArrayList<>();
        try {
            inputYangModel = new YangModel(new ByteArrayYangInput(file.getBytes(), "requestYang"), ConformanceType.IMPORT);
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(this.getClass().getClassLoader());
            Resource[] yangResourcesImplement = resolver.getResources("classpath:models/*.yang");
            Resource[] yangResourcesImport = resolver.getResources("classpath:models/import/*.yang");

            for (Resource yangResource : yangResourcesImplement) {
                yangModels.add(new YangModel(new ByteArrayYangInput(yangResource.getContentAsByteArray(), Objects
                        .requireNonNull(yangResource.getFilename())), ConformanceType.IMPLEMENT));
            }

            for (Resource yangResource : yangResourcesImport) {
                yangModels.add(new YangModel(new ByteArrayYangInput(yangResource.getContentAsByteArray(), Objects
                        .requireNonNull(yangResource.getFilename())), ConformanceType.IMPORT));
            }

            yangModels.add(inputYangModel);
        } catch (final IOException ex) {
            log.error("Unable to load schema", ex);
        }

        final ModifyableFindingSeverityCalculator severityCalculator = new ModifyableFindingSeverityCalculator();
        final FindingsManager findingsManager = new FindingsManager(severityCalculator);
        findingsManager.addFilterPredicate(ModuleAndFindingTypeAndSchemaNodePathFilterPredicate.fromString("ietf-*;*;*"));
        findingsManager.addFilterPredicate(ModuleAndFindingTypeAndSchemaNodePathFilterPredicate.fromString("_3gpp*;*;*"));
        final ParserExecutionContext context = new ParserExecutionContext(findingsManager);
        context.setIgnoreImportedProtocolAccessibleObjects(true);
        context.setFailFast(false);
        yangDeviceModel.parseIntoYangModels(context, yangModels);
        checkFindings(context.getFindingsManager(), severityCalculator);

        assert inputYangModel != null;
        String prefixName = null;

        for (YangDomElement children : inputYangModel.getYangDomDocumentRoot().getChildren().get(0).getChildren()) {
            if (children.getValue().contains("common-yang-types")) {
                prefixName = children.getChildren().get(0).getValue();
                break;
            }
        }

        result.put("classifiers", getClassifiers(inputYangModel.getYangDomDocumentRoot().getChildren().get(0).getChildren(),
                prefixName));
        result.put("decorators", getDecorators(inputYangModel.getYangDomDocumentRoot().getChildren().get(0).getChildren()));
        result.put("moduleName", inputYangModel.getModuleIdentity().getModuleName());
        result.put("revision", inputYangModel.getModuleIdentity().getRevision());
        return result;
    }

    /**
     * Store domains and revision by schema name, return object of all schemas
     *
     * @param yangDeviceModel
     *     YangDeviceModel instance
     *
     * @return OranTeivSchemasMetaData
     */
    public static List<OranTeivSchema> returnAllTeivSchemas(YangDeviceModel yangDeviceModel) {
        List<OranTeivSchema> OranTeivSchemasMetaData = new ArrayList<>();
        for (final YangModel yangModel : yangDeviceModel.getModuleRegistry().getAllYangModels()) {
            OranTeivSchema schemasMetaData = new OranTeivSchema();
            OranTeivHref href = new OranTeivHref();

            schemasMetaData.setName(yangModel.getModuleIdentity().getModuleName());
            href.setHref(String.format("/schemas/%s/content", schemasMetaData.getName()));
            schemasMetaData.setContent(href);
            schemasMetaData.setRevision(yangModel.getModuleIdentity().getRevision());

            final AbstractStatement moduleOrSubmodule = yangModel.getYangModelRoot().getModuleOrSubmodule();
            final AbstractStatement domainStatement = moduleOrSubmodule.getChild(new StatementModuleAndName(
                    "o-ran-smo-teiv-common-yang-extensions", "domain"));
            String value = new String();
            if (domainStatement != null && domainStatement.getDomElement() != null) {
                value = domainStatement.getDomElement().getValue();
            }
            schemasMetaData.setDomain(value);

            if (schemasMetaData.getDomain() != null) {
                OranTeivSchemasMetaData.add(schemasMetaData);
            }
        }
        return OranTeivSchemasMetaData;
    }

    /**
     * Filter schemas by schema name from extracted yang model
     *
     * @param yangDeviceModel
     *     YangDeviceModel instance
     * @param schemaName
     *     Schema to be fetched
     */
    public static String returnSchemaByName(YangDeviceModel yangDeviceModel, String schemaName) {
        StringBuilder sb = new StringBuilder();
        for (final YangModel yangModel : yangDeviceModel.getModuleRegistry().getAllYangModels()) {
            if (yangModel.getYangModelRoot().getModuleOrSubmodule().getDomElement().getValue().equals(schemaName)) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(yangModel.getYangInput().getInputStream(),
                        StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append(" ");
                    }
                } catch (IOException ex) {
                    log.error("Reading failed", ex);
                }
            }
        }
        if (sb.length() > 1) {
            sb = new StringBuilder(sb.deleteCharAt(sb.length() - 1).toString().replaceAll("\\s+", " "));
        }
        return sb.toString();
    }

    public static YangDataDomDocumentRoot getYangDataDomDocumentRoot(final JsonNode jsonNode) throws IOException {
        final YangDataDomDocumentRoot yangDataDomDocument = parse(jsonNode.toString());
        if (yangDataDomDocument == null) {
            throw new IOException("YangDataDomDocumentRoot is null");
        }
        final Set<Finding> findings = yangDataDomDocument.getFindings();
        if (!findings.isEmpty()) {
            throw new IOException("Findings when parsing yang: " + jsonNode);
        }
        return yangDataDomDocument;
    }

    private List<String> getClassifiers(List<YangDomElement> elements, String prefixName) {

        List<String> result = new ArrayList<>();
        String formattedName = String.format(SEMICOLON_SEPARATION, prefixName, CLASSIFIERS);

        elements.stream().filter(element -> element.getName().contains("identity")).forEach(element -> {
            String childValue = element.getChildren().get(0).getValue();

            if (!childValue.equals(formattedName)) {
                List<YangDomElement> domElement = element.getParentElement().getChildren().stream().filter(parent -> parent
                        .getName().contains("identity")).filter(parent -> parent.getValue().equals(childValue) && parent
                                .getChildren().get(0).getValue().equals(formattedName)).toList();

                if (domElement.isEmpty()) {
                    throw TeivException.invalidSchema("Invalid classifier " + element.getValue());
                } else {
                    result.add(element.getValue());
                }
            } else {
                result.add(element.getValue());
            }
        });

        return result;
    }

    private Map<String, String> getDecorators(List<YangDomElement> elements) {
        Map<String, String> result = new HashMap<>();

        elements.forEach(keys -> {
            if (keys.getValue().contains("decorators")) {
                keys.getChildren().forEach(values -> result.put(values.getValue(), switch (values.getChildren().get(0)
                        .getValue()) {
                    case "string" -> "TEXT";
                    case "boolean" -> "BOOLEAN";
                    case "int8", "int16", "int32", "int64", "uint8", "uint16", "uint32", "uint64" -> "INT";
                    default -> throw TeivException.invalidFileInput("Invalid data type");
                }));
            }
        });

        return result;
    }

    private static void checkFindings(FindingsManager findingsManager,
            ModifyableFindingSeverityCalculator severityCalculator) {
        for (Finding finding : findingsManager.getAllFindings()) {
            if (severityCalculator.calculateSeverity(finding.getFindingType()).equals(FindingSeverity.ERROR)) {
                if (finding.getMessage().contains("exception") || finding.getFindingType().contains("UNSPECIFIED_ERROR")) {
                    throw TeivException.invalidFileInput(INVALID_SCHEMA);
                } else {
                    throw TeivException.invalidFileInput(finding.getMessage());
                }
            }
        }
    }

    private static YangDataDomDocumentRoot parse(final String yangTopology) throws IOException {

        final ModifyableFindingSeverityCalculator severityCalculator = new ModifyableFindingSeverityCalculator();
        final FindingsManager findingsManager = new FindingsManager(severityCalculator);
        findingsManager.addFilterPredicate(ModuleAndFindingTypeAndSchemaNodePathFilterPredicate.fromString(
                "ietf*,iana*;*;*"));

        final ParserExecutionContext context = new ParserExecutionContext(findingsManager);
        context.setFailFast(false);

        final InputStream inputStream = new ByteArrayInputStream(yangTopology.getBytes(StandardCharsets.UTF_8));
        final YangInput yangInput = new BufferedStreamYangInput("yang-topology", inputStream,
                YangInput.MEDIA_TYPE_YANG_DATA_JSON);
        final YangData yangData = new YangData(yangInput);
        yangData.parse(context);

        return yangData.getYangDataDomDocumentRoot();
    }
}
