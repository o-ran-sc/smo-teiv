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
package org.oran.smo.yangtools.parser.simple.example;

import org.oran.smo.yangtools.parser.ParserExecutionContext;
import org.oran.smo.yangtools.parser.YangDeviceModel;
import org.oran.smo.yangtools.parser.input.FileBasedYangInput;
import org.oran.smo.yangtools.parser.model.ConformanceType;
import org.oran.smo.yangtools.parser.model.YangModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class YangParserExample {

    private static final String DIR = "yang-parser/parser-example/src/main/resources/yang/";

    public static List<YangModel> parseYangModels(YangDeviceModel yangDeviceModel, ParserExecutionContext context) {

        List<String> yangImplementFiles = new ArrayList<>();
        List<String> yangImportFiles = new ArrayList<>();

        // these yang files are the versions as of 2025-01-29
        // for the current versions of the yang files check teiv/src/main/resources/models
        yangImplementFiles.add("2024-10-08-o-ran-smo-teiv-ran.yang");
        yangImplementFiles.add("2024-05-24-o-ran-smo-teiv-common-yang-extensions.yang");
        yangImplementFiles.add("2024-10-04-o-ran-smo-teiv-common-yang-types.yang");

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

}
