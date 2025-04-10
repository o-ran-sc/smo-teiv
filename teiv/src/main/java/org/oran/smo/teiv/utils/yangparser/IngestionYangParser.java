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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.oran.smo.teiv.exception.YangException;
import org.oran.smo.yangtools.parser.ParserExecutionContext;
import org.oran.smo.yangtools.parser.YangDeviceModel;
import org.oran.smo.yangtools.parser.data.YangData;
import org.oran.smo.yangtools.parser.data.dom.YangDataDomDocumentRoot;
import org.oran.smo.yangtools.parser.data.instance.DataTreeBuilderPredicate;
import org.oran.smo.yangtools.parser.data.instance.RootInstance;
import org.oran.smo.yangtools.parser.findings.Finding;
import org.oran.smo.yangtools.parser.findings.FindingFilterPredicate;
import org.oran.smo.yangtools.parser.findings.ModuleAndFindingTypeAndSchemaNodePathFilterPredicate;
import org.oran.smo.yangtools.parser.input.BufferedStreamYangInput;
import org.oran.smo.yangtools.parser.input.DirectYangInputResolver;
import org.oran.smo.yangtools.parser.input.YangInput;
import org.oran.smo.yangtools.parser.model.YangModel;
import org.oran.smo.yangtools.parser.model.statements.StatementClassSupplier;
import org.oran.smo.yangtools.parser.model.statements.ietf.IetfExtensionsClassSupplier;
import org.oran.smo.yangtools.parser.model.statements.threegpp.ThreeGppExtensionsClassSupplier;
import org.oran.smo.teiv.CustomMetrics;
import org.oran.smo.teiv.exception.YangParsingException;
import org.oran.smo.teiv.exception.YangValidationException;

public class IngestionYangParser extends YangModelLoader {
    private static final List<YangModel> yangModelInputs = new CopyOnWriteArrayList<>();

    private static final Set<FindingFilterPredicate> FILTER_PREDICATES = Set.of(
            ModuleAndFindingTypeAndSchemaNodePathFilterPredicate.fromString("ietf-*;*;*"),
            ModuleAndFindingTypeAndSchemaNodePathFilterPredicate.fromString("_3gpp*;*;*"));

    private static final List<StatementClassSupplier> EXTENSION_CREATORS = List.of(new IetfExtensionsClassSupplier(),
            new ThreeGppExtensionsClassSupplier());

    public static void loadModels() throws YangException {
        ParserExecutionContext context = createParserExecutionContext(EXTENSION_CREATORS, List.of(), FILTER_PREDICATES);
        final List<YangModel> yangModels = YangModelLoader.loadModulesFromSchemaRegistry();
        List<YangModel> models = YangModelLoader.parseModelsIntoContext(context, yangModels);
        yangModelInputs.clear();
        yangModelInputs.addAll(models);
    }

    public static YangDataDomDocumentRoot parseData(final String data) throws YangParsingException,
            YangValidationException {
        ParserExecutionContext context = createParserExecutionContext(List.of(), List.of(), Set.of(
                ModuleAndFindingTypeAndSchemaNodePathFilterPredicate.fromString("ietf-*;*;*")));
        context.setFailFast(false);

        InputStream inputStream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        YangInput yangInput = executeIOOperation(() -> new BufferedStreamYangInput("yang-topology", inputStream,
                YangInput.MEDIA_TYPE_YANG_DATA_JSON));
        YangData yangData = new YangData(yangInput);
        yangData.parse(context);

        YangDataDomDocumentRoot yangDataDomDocument = yangData.getYangDataDomDocumentRoot();
        final Set<Finding> findings = yangDataDomDocument.getFindings();
        if (!findings.isEmpty()) {
            throw YangValidationException.validationFailed(findings);
        }

        return yangDataDomDocument;
    }

    public static synchronized RootInstance parseData(final String data, final boolean areSidesAndSourceIdsMandatory,
            CustomMetrics customMetrics) throws YangParsingException, YangValidationException {
        ParserExecutionContext context = createParserExecutionContext(EXTENSION_CREATORS, List.of(), FILTER_PREDICATES);

        InputStream inputStream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        YangInput yangInput = executeIOOperation(() -> new BufferedStreamYangInput("yang-topology", inputStream,
                YangInput.MEDIA_TYPE_YANG_DATA_JSON));
        List<YangModel> localYangModelInputs = List.copyOf(yangModelInputs);
        YangDeviceModel yangDeviceModel = new YangDeviceModel("YangDeviceModel to parse into");
        yangDeviceModel.parseIntoYangModels(context, localYangModelInputs);
        yangDeviceModel.parseYangData(context, new DirectYangInputResolver(Set.of(yangInput)),
                DataTreeBuilderPredicate.ALLOW_ALL);

        return yangDeviceModel.getCombinedInstanceDataRoot();
    }
}
