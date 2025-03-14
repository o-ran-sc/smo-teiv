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

import static org.oran.smo.teiv.utils.TeivConstants.CLASSIFIERS;
import static org.oran.smo.teiv.utils.TeivConstants.DECORATORS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.exception.YangException;
import org.oran.smo.teiv.schema.MockSchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoaderException;
import org.oran.smo.teiv.utils.yangparser.ExposureYangParser;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class ExposureYangParserTest {
    @InjectMocks
    ExposureYangParser exposureYangParser;

    @BeforeAll
    public void initSchema() throws SchemaLoaderException, YangException {
        SchemaLoader mockSchemaLoader = new MockSchemaLoader();
        mockSchemaLoader.loadSchemaRegistry();
        ExposureYangParser.loadAndValidateModels();
    }

    @Test
    void testValidateCorrectSchema() throws YangException {
        MultipartFile correctFile = new MockMultipartFile("file", "file.yang", MediaType.MULTIPART_FORM_DATA_VALUE,
                ("module module-rapp-module1 {\n" + " \n" + "\tyang-version 1.1;\n" + "\tnamespace \"urn:module-rapp-model\";\n" + "\tprefix testModule;\n" + "\t\n" + "    import o-ran-smo-teiv-common-yang-types { prefix model; }\n" + "\timport o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; }\n" + "\t\n" + "\trevision \"2024-05-08\" {\n" + "\t\tdescription\n" + "\t\t\"Initial revision.\";\n" + "\t\tor-teiv-yext:label 0.3.0;\n" + "\t}\n" + "\t\n" + "\taugment /model:decorators {\n" + "        leaf test1 {\n" + "            type string;\n" + "        }\n" + "\t\tleaf test2 {\n" + "            type boolean;\n" + "        }\n" + "\t\tleaf test3 {\n" + "            type uint32;\n" + "        }\n" + "    }\n" + "\t\n" + "\tidentity geo-classifier {\n" + "\t\tbase model:classifiers;\n" + "\t}\n" + " \n" + "    identity urban {\n" + "        base geo-classifier;\n" + "    }\n" + "\t\n" + "\tidentity rural {\n" + "        base geo-classifier;\n" + "    }\n" + "\n" + "}")
                        .getBytes());
        Map<String, Object> expected = new HashMap<>();
        expected.put("moduleName", "module-rapp-module1");
        expected.put("revision", "2024-05-08");
        expected.put(CLASSIFIERS, List.of("geo-classifier", "urban", "rural"));
        expected.put(DECORATORS, Map.of("test1", "TEXT", "test2", "BOOLEAN", "test3", "INT"));

        Map<String, Object> actual = ExposureYangParser.validateSchemasYang(correctFile);
        assertEquals(expected, actual);
    }

    @Test
    void testValidateSchemaWithEmptyClassifiers() throws YangException {
        MultipartFile correctFile = new MockMultipartFile("file", "file.yang", MediaType.MULTIPART_FORM_DATA_VALUE,
                ("module module-rapp-module1 {\n" + " \n" + "\tyang-version 1.1;\n" + "\tnamespace \"urn:module-rapp-model\";\n" + "\tprefix testModule;\n" + "\t\n" + "    import o-ran-smo-teiv-common-yang-types { prefix model; }\n" + "\timport o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; }\n" + "\t\n" + "\trevision \"2024-05-08\" {\n" + "\t\tdescription\n" + "\t\t\"Initial revision.\";\n" + "\t\tor-teiv-yext:label 0.3.0;\n" + "\t}\n" + "\t\n" + "\taugment /model:decorators {\n" + "        leaf test1 {\n" + "            type string;\n" + "        }\n" + "\t\tleaf test2 {\n" + "            type boolean;\n" + "        }\n" + "\t\tleaf test3 {\n" + "            type uint32;\n" + "        }\n" + "    }\n" + "}")
                        .getBytes());
        Map<String, Object> expected = new HashMap<>();
        expected.put("moduleName", "module-rapp-module1");
        expected.put("revision", "2024-05-08");
        expected.put(CLASSIFIERS, Collections.emptyList());
        expected.put(DECORATORS, Map.of("test1", "TEXT", "test2", "BOOLEAN", "test3", "INT"));

        Map<String, Object> actual = ExposureYangParser.validateSchemasYang(correctFile);
        assertEquals(expected, actual);
    }

    @Test
    void testValidateSchemaWithEmptyDecorators() throws YangException {
        MultipartFile correctFile = new MockMultipartFile("file", "file.yang", MediaType.MULTIPART_FORM_DATA_VALUE,
                ("module module-rapp-module1 {\n" + " \n" + "\tyang-version 1.1;\n" + "\tnamespace \"urn:module-rapp-model\";\n" + "\tprefix testModule;\n" + "\t\n" + "    import o-ran-smo-teiv-common-yang-types { prefix model; }\n" + "\timport o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; }\n" + "\t\n" + "\trevision \"2024-05-08\" {\n" + "\t\tdescription\n" + "\t\t\"Initial revision.\";\n" + "\t\tor-teiv-yext:label 0.3.0;\n" + "\t}\n" + "\t\n" + "\tidentity geo-classifier {\n" + "\t\tbase model:classifiers;\n" + "\t}\n" + " \n" + "    identity urban {\n" + "        base geo-classifier;\n" + "    }\n" + "\t\n" + "\tidentity rural {\n" + "        base geo-classifier;\n" + "    }\n" + "\n" + "}")
                        .getBytes());
        Map<String, Object> expected = new HashMap<>();
        expected.put("moduleName", "module-rapp-module1");
        expected.put("revision", "2024-05-08");
        expected.put(CLASSIFIERS, List.of("geo-classifier", "urban", "rural"));
        expected.put(DECORATORS, Collections.emptyMap());

        Map<String, Object> actual = ExposureYangParser.validateSchemasYang(correctFile);
        assertEquals(expected, actual);
    }

    @Test
    void testValidateSchemaWithWrongClassifiers() {
        MultipartFile wrongClassifiersFile = new MockMultipartFile("file", "file.yang", MediaType.MULTIPART_FORM_DATA_VALUE,
                ("module module-rapp-module1 {\n" + " \n" + "\tyang-version 1.1;\n" + "\tnamespace \"urn:module-rapp-model\";\n" + "\tprefix testModule;\n" + "\t\n" + "    import o-ran-smo-teiv-common-yang-types { prefix model; }\n" + "\timport o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; }\n" + "\t\n" + "\trevision \"2024-05-08\" {\n" + "\t\tdescription\n" + "\t\t\"Initial revision.\";\n" + "\t\tor-teiv-yext:label 0.3.0;\n" + "\t}\n" + "\t\n" + "\taugment /model:decorators {\n" + "        leaf test1 {\n" + "            type string;\n" + "        }\n" + "\t\tleaf test2 {\n" + "            type boolean;\n" + "        }\n" + "\t\tleaf test3 {\n" + "            type uint32;\n" + "        }\n" + "    }\n" + "\t\n" + "\tidentity geo-classifier {\n" + "\t\tbase geo-classifier;\n" + "\t}\n" + " \n" + "    identity urban {\n" + "        base geo-classifier;\n" + "    }\n" + "\t\n" + "\tidentity rural {\n" + "        base geo-classifier;\n" + "    }\n" + "\n" + "}")
                        .getBytes());
        assertThrows(TeivException.class, () -> ExposureYangParser.validateSchemasYang(wrongClassifiersFile));
    }

    @Test
    void testValidateSchemaWithWrongDecorators() {
        MultipartFile wrongDecoratorsFile = new MockMultipartFile("file", "file.yang", MediaType.MULTIPART_FORM_DATA_VALUE,
                ("module module-rapp-module1 {\n" + " \n" + "\tyang-version 1.1;\n" + "\tnamespace \"urn:module-rapp-model\";\n" + "\tprefix testModule;\n" + "\t\n" + "    import o-ran-smo-teiv-common-yang-types { prefix model; }\n" + "\timport o-ran-smo-teiv-common-yang-extensions {prefix or-teiv-yext; }\n" + "\t\n" + "\trevision \"2024-05-08\" {\n" + "\t\tdescription\n" + "\t\t\"Initial revision.\";\n" + "\t\tor-teiv-yext:label 0.3.0;\n" + "\t}\n" + "\t\n" + "\taugment /model:decorators {\n" + "        leaf test1 {\n" + "            type test;\n" + "        }\n" + "\t\tleaf test2 {\n" + "            type boolean;\n" + "        }\n" + "\t\tleaf test3 {\n" + "            type uint32;\n" + "        }\n" + "    }\n" + "\t\n" + "\tidentity geo-classifier {\n" + "\t\tbase geo-classifier;\n" + "\t}\n" + " \n" + "    identity urban {\n" + "        base geo-classifier;\n" + "    }\n" + "\t\n" + "\tidentity rural {\n" + "        base geo-classifier;\n" + "    }\n" + "\n" + "}")
                        .getBytes());
        assertThrows(TeivException.class, () -> ExposureYangParser.validateSchemasYang(wrongDecoratorsFile));
    }
}
