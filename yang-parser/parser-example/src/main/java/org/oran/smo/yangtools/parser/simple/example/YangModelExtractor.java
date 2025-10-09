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
package org.oran.smo.yangtools.parser.simple.example;

import org.oran.smo.yangtools.parser.ParserExecutionContext;
import org.oran.smo.yangtools.parser.YangDeviceModel;
import org.oran.smo.yangtools.parser.findings.FindingsManager;
import org.oran.smo.yangtools.parser.findings.ModifyableFindingSeverityCalculator;
import org.oran.smo.yangtools.parser.findings.ModuleAndFindingTypeAndSchemaNodePathFilterPredicate;
import org.oran.smo.yangtools.parser.input.FileBasedYangInput;
import org.oran.smo.yangtools.parser.model.ConformanceType;
import org.oran.smo.yangtools.parser.model.YangModel;
import org.oran.smo.yangtools.parser.model.statements.AbstractStatement;
import org.oran.smo.yangtools.parser.model.statements.ietf.IetfExtensionsClassSupplier;
import org.oran.smo.yangtools.parser.model.statements.oran.OranExtensionsClassSupplier;
import org.oran.smo.yangtools.parser.model.statements.threegpp.ThreeGppExtensionsClassSupplier;
import org.oran.smo.yangtools.parser.model.statements.yang.YModule;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class YangModelExtractor {

    private static YangDeviceModel yangDeviceModel;
    private static ModifyableFindingSeverityCalculator severityCalculator;
    private static FindingsManager findingsManager;
    private static ParserExecutionContext context;

    private static final String DIR = "teiv/src/main/resources/models/";

    public static YModule parseAndExtractYModule(String yangModelNameSpace) {
        setUp();
        List<YangModel> yangModels = parseYangModels(yangDeviceModel, context);
        Optional<YangModel> optionalRanModel = yangModels.stream().filter(yangModel -> yangModelNameSpace.equals(yangModel
                .getModuleIdentity().getModuleName())).findFirst();
        return optionalRanModel.map(yangModel -> yangModel.getYangModelRoot().getModule()).orElse(null);
    }

    public static String validateNameSpaceValue(YModule yModule, String nameSpace) {
        String validNameSpace = null;
        List<AbstractStatement> children = yModule.getChildStatements();
        for (AbstractStatement child : children) {
            if (child.getDomElement().getValue().equalsIgnoreCase(nameSpace)) {
                validNameSpace = child.getDomElement().getValue();
                break;
            }
        }
        return validNameSpace;
    }

    private static List<YangModel> parseYangModels(YangDeviceModel yangDeviceModel, ParserExecutionContext context) {

        List<String> yangImplementFiles = new ArrayList<>();
        List<String> yangImportFiles = new ArrayList<>();

        //latest revised yang models used
        yangImplementFiles.add("o-ran-smo-teiv-ran.yang");
        yangImplementFiles.add("o-ran-smo-teiv-rel-oam-ran.yang");

        final List<YangModel> yangModels = new ArrayList<>();

        for (final String relativeFilePath : yangImplementFiles) {
            yangModels.add(new YangModel(new FileBasedYangInput(new File(DIR + relativeFilePath)),
                    ConformanceType.IMPLEMENT));
        }
        for (final String relativeFilePath : yangImportFiles) {
            yangModels.add(new YangModel(new FileBasedYangInput(new File(DIR + "import/" + relativeFilePath)),
                    ConformanceType.IMPORT));
        }

        yangDeviceModel.parseIntoYangModels(context, yangModels);

        return yangModels;
    }

    private static void setUp() {
        yangDeviceModel = new YangDeviceModel("Yang Parser JAR Test Device Model");

        severityCalculator = new ModifyableFindingSeverityCalculator();

        findingsManager = new FindingsManager(severityCalculator);

        // suppress all findings from ietf and iana
        findingsManager.addFilterPredicate(ModuleAndFindingTypeAndSchemaNodePathFilterPredicate.fromString(
                "ietf*,iana*;*;*"));

        final ThreeGppExtensionsClassSupplier threeGppStatementFactory = new ThreeGppExtensionsClassSupplier();
        final IetfExtensionsClassSupplier ietfStatementFactory = new IetfExtensionsClassSupplier();
        final OranExtensionsClassSupplier oranStatementFactory = new OranExtensionsClassSupplier();

        context = new ParserExecutionContext(findingsManager, Arrays.asList(threeGppStatementFactory, oranStatementFactory,
                ietfStatementFactory));
        context.setFailFast(false);
        context.setSuppressFindingsOnUnusedSchemaNodes(true);
    }
}
