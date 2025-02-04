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
package org.oran.smo.yangtools.parser.simple.example;

import org.oran.smo.yangtools.parser.ParserExecutionContext;
import org.oran.smo.yangtools.parser.data.YangData;
import org.oran.smo.yangtools.parser.data.parser.JsonParser;
import org.oran.smo.yangtools.parser.data.parser.JsonParser.JsonValue;
import org.oran.smo.yangtools.parser.input.FileBasedYangInput;

import java.io.File;
import java.io.IOException;

public class JsonParserExample {

    private static final String DIR = "yang-parser-example/src/main/resources/json/";

    public static JsonValue parseJsonFile(ParserExecutionContext context) {
        final File jsonFile = new File(DIR + "AntennaModule-response.json");
        final FileBasedYangInput fileBasedYangInput = new FileBasedYangInput(jsonFile);
        final YangData yangInstanceDataInput = new YangData(fileBasedYangInput);

        try {
            return new JsonParser(context, yangInstanceDataInput, fileBasedYangInput.getInputStream()).parse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
