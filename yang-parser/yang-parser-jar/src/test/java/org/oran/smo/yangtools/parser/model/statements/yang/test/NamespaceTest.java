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
package org.oran.smo.yangtools.parser.model.statements.yang.test;

import org.junit.Test;
import org.oran.smo.yangtools.parser.findings.ParserFindingType;
import org.oran.smo.yangtools.parser.testutils.YangTestCommon;

import java.util.Arrays;
import java.util.Collections;

public class NamespaceTest extends YangTestCommon {

    private static final String TEST_DIR = "src/test/resources/model-statements-yang/namespace-test/";

    @Test
    public void test___modA_nsA___modB_nsB__ok() {

        parseAbsoluteImplementsYangModels(Arrays.asList(TEST_DIR + "modA_nsA.yang", TEST_DIR + "modB_nsB.yang"));

        assertNoFindings();
    }

    @Test
    public void test___modA_nsA___modA_nsB___namespace_different() {

        parseYangModels(Collections.emptyList(), Arrays.asList(TEST_DIR + "modA_nsA.yang"), Collections.emptyList(), Arrays
                .asList(TEST_DIR + "modA_nsB.yang"));

        assertHasFindingOfType(ParserFindingType.P007_MODULE_NAMESPACE_MISMATCH.toString());
    }

    @Test
    public void test___modA_nsA___modB_nsA___module_different() {

        parseYangModels(Collections.emptyList(), Arrays.asList(TEST_DIR + "modA_nsA.yang"), Collections.emptyList(), Arrays
                .asList(TEST_DIR + "modB_nsA.yang"));

        assertHasFindingOfType(ParserFindingType.P007_MODULE_NAMESPACE_MISMATCH.toString());
    }
}
