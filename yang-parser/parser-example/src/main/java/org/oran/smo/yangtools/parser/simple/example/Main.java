/*
 *  ============LICENSE_START=======================================================
 *  Copyright (C) 2024 Ericsson
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
import org.oran.smo.yangtools.parser.data.parser.JsonParser.JsonArray;
import org.oran.smo.yangtools.parser.data.parser.JsonParser.JsonObject;
import org.oran.smo.yangtools.parser.data.parser.JsonParser.JsonObjectMemberName;
import org.oran.smo.yangtools.parser.data.parser.JsonParser.JsonValue;
import org.oran.smo.yangtools.parser.data.parser.JsonWriter;
import org.oran.smo.yangtools.parser.findings.FindingsManager;
import org.oran.smo.yangtools.parser.findings.ModifyableFindingSeverityCalculator;
import org.oran.smo.yangtools.parser.findings.ModuleAndFindingTypeAndSchemaNodePathFilterPredicate;
import org.oran.smo.yangtools.parser.model.YangModel;
import org.oran.smo.yangtools.parser.model.statements.ietf.IetfExtensionsClassSupplier;
import org.oran.smo.yangtools.parser.model.statements.oran.OranExtensionsClassSupplier;
import org.oran.smo.yangtools.parser.model.statements.threegpp.ThreeGppExtensionsClassSupplier;
import org.oran.smo.yangtools.parser.model.statements.yang.YModule;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Main {

    private static YangDeviceModel yangDeviceModel;
    private static ModifyableFindingSeverityCalculator severityCalculator;
    private static FindingsManager findingsManager;
    private static ParserExecutionContext context;

    public static void main(String[] args) {

        // Setup for yang parser.
        // The parser execution context directs the behaviour of the parser. It is vital that the
        // context is correctly setup.
        setUp();

        // using the yang-parser's jsonParser parse json output from TEIV response:
        // GET http://localhost:31074/topology-inventory/v1/domains/EQUIPMENT/entity-types/AntennaModule/entities?targetFilter=/attributes
        JsonObject parsedJsonObject = (JsonObject) JsonParserExample.parseJsonFile(context);
        // get the "items" from the response
        JsonArray items = (JsonArray) getJsonObjectMemberValue(parsedJsonObject, "items");
        System.out.println("Response of all AntennaModules from TEIV:");
        for (JsonValue item : items.getValues()) {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                JsonValue value = getJsonObjectMemberValue((JsonObject) item, "o-ran-smo-teiv-equipment:AntennaModule");
                // using the yang-parser's jsonWriter write the json to stream
                JsonWriter.write(value, baos);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // print the output to console
            System.out.println(baos);
        }

        System.out.println("\n------------------------------------------------------------------");

        // using the yang-parser's parseIntoYangModels
        List<YangModel> yangModels = YangParserExample.parseYangModels(yangDeviceModel, context);
        System.out.println("\nList of all entities/lists from o-ran-smo-teiv-ran model:");
        Optional<YangModel> optionalRanModel = yangModels.stream().filter(yangModel -> "o-ran-smo-teiv-ran".equals(yangModel
                .getModuleIdentity().getModuleName())).findFirst();
        optionalRanModel.ifPresent(yangModel -> {
            YModule yModule = yangModel.getYangModelRoot().getModule();
            yModule.getLists().stream().forEach(yList -> {
                System.out.println(yList.getListName());
            });
        });

    }

    private static JsonValue getJsonObjectMemberValue(final JsonObject jsonObject, final String memberName) {

        final Set<Map.Entry<JsonObjectMemberName, JsonValue>> entrySet = jsonObject.getValuesByMember().entrySet();
        for (final Map.Entry<JsonObjectMemberName, JsonValue> entry : entrySet) {

            if (memberName.equals(entry.getKey().getMemberName())) {
                return entry.getValue();
            }
        }

        return null;
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
