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
package org.oran.smo.teiv.utils;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.extern.slf4j.Slf4j;

import org.oran.smo.yangtools.parser.data.dom.YangDataDomNode;
import org.oran.smo.yangtools.parser.data.instance.AbstractStructureInstance;
import org.oran.smo.yangtools.parser.data.instance.RootInstance;
import org.oran.smo.yangtools.parser.model.YangModel;
import org.oran.smo.teiv.CustomMetrics;
import org.oran.smo.teiv.exception.YangException;
import org.oran.smo.teiv.exception.YangSchemaException;
import org.oran.smo.teiv.exception.YangValidationException;
import org.oran.smo.teiv.schema.MockSchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoaderException;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.utils.yangparser.IngestionYangParser;
import org.oran.smo.teiv.utils.yangparser.YangModelLoader;

@Slf4j
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class IngestionYangParserTest {
    @Mock
    private CustomMetrics customMetrics;

    @BeforeAll
    public void initSchema() throws SchemaLoaderException, YangException {
        SchemaLoader mockSchemaLoader = new MockSchemaLoader();
        mockSchemaLoader.loadSchemaRegistry();
        IngestionYangParser.loadModels();
    }

    @Test
    void testLoadAndValuateModels_Successful() {
        final Set<String> modulesToImport = Set.of("_3gpp-common-yang-extensions", "_3gpp-common-yang-types",
                "ietf-geo-location", "ietf-inet-types", "ietf-yang-types");

        final Set<String> modulesToImplement = Set.of("o-ran-smo-teiv-common-yang-extensions",
                "o-ran-smo-teiv-common-yang-types", "o-ran-smo-teiv-equipment", "o-ran-smo-teiv-oam", "o-ran-smo-teiv-ran",
                "o-ran-smo-teiv-rel-equipment-ran", "o-ran-smo-teiv-rel-oam-ran");

        List<String> importModelList = assertDoesNotThrow(() -> YangModelLoader.readYangModelsFromPath(
                "classpath:models/import/*.yang"));
        List<String> implementModelList = assertDoesNotThrow(() -> YangModelLoader.readYangModelsFromPath(
                "classpath:models/*.yang"));

        Set<String> importModulesSet = new HashSet<>(importModelList);
        Set<String> implementModulesSet = new HashSet<>(implementModelList);

        assertTrue(importModulesSet.containsAll(modulesToImport));
        assertTrue(implementModulesSet.containsAll(modulesToImplement));
    }

    @Test
    void parseMultipleTypesOfEntities() throws YangException {
        RootInstance rootInstance = parseAndValidateFromFile("src/test/resources/yang/parser/multiple-entity.json",
                customMetrics);
        assertEquals(3, rootInstance.getStructureChildren().size());

        AbstractStructureInstance OCUCPFunction = rootInstance.getStructureChildren().get(0);
        assertEquals("OCUCPFunction", OCUCPFunction.getName());
        assertEquals("ocucp_1", OCUCPFunction.getContentChildren().get(0).getValue());
        assertEquals(12L, OCUCPFunction.getStructureChildren().get(0).getContentChildren().get(0).getValue());
        assertEquals("str", OCUCPFunction.getStructureChildren().get(0).getContentChildren().get(1).getValue());
        List<YangDataDomNode> sourceIdsYang = OCUCPFunction.getContentChildren().get(0).getParent().getDataDomNode()
                .getChildren().stream().filter(child -> child.getName().equals("sourceIds")).toList();
        List<String> sourceIds = sourceIdsYang.stream().map(YangDataDomNode::getStringValue).toList();
        assertEquals(List.of("fdn_ocucp_1", "cmHandle_ocucp_1"), sourceIds);

        AbstractStructureInstance ODUFunction = rootInstance.getStructureChildren().get(1);
        assertEquals("odu_21", ODUFunction.getContentChildren().get(0).getValue());
        assertEquals(1L, ODUFunction.getStructureChildren().get(0).getContentChildren().get(0).getValue());
        assertEquals(21L, ODUFunction.getStructureChildren().get(0).getContentChildren().get(1).getValue());
        assertEquals("021", ODUFunction.getStructureChildren().get(0).getStructureChildren().get(0).getContentChildren()
                .get(0).getValue());
        assertEquals("210", ODUFunction.getStructureChildren().get(0).getStructureChildren().get(0).getContentChildren()
                .get(1).getValue());
        sourceIdsYang = ODUFunction.getContentChildren().get(0).getParent().getDataDomNode().getChildren().stream().filter(
                child -> child.getName().equals("sourceIds")).toList();
        sourceIds = sourceIdsYang.stream().map(YangDataDomNode::getStringValue).toList();
        assertEquals(List.of("fdn_odu_21", "cmHandle_odu_21"), sourceIds);

        ODUFunction = rootInstance.getStructureChildren().get(2);
        assertEquals("odu_22", ODUFunction.getContentChildren().get(0).getValue());
        assertEquals(2L, ODUFunction.getStructureChildren().get(0).getContentChildren().get(0).getValue());
        assertEquals(22L, ODUFunction.getStructureChildren().get(0).getContentChildren().get(1).getValue());
        assertEquals("022", ODUFunction.getStructureChildren().get(0).getStructureChildren().get(0).getContentChildren()
                .get(0).getValue());
        assertEquals("220", ODUFunction.getStructureChildren().get(0).getStructureChildren().get(0).getContentChildren()
                .get(1).getValue());
        sourceIdsYang = ODUFunction.getContentChildren().get(0).getParent().getDataDomNode().getChildren().stream().filter(
                child -> child.getName().equals("sourceIds")).toList();
        sourceIds = sourceIdsYang.stream().map(YangDataDomNode::getStringValue).toList();
        assertEquals(List.of("fdn_odu_22", "cmHandle_odu_22"), sourceIds);
    }

    @Test
    void testGeolocationEntity() {
        RootInstance rootInstance = assertDoesNotThrow(() -> parseAndValidateFromFile(
                "src/test/resources/yang/parser/geolocation.json", customMetrics));
        assertEquals(2, rootInstance.getStructureChildren().size());

        // GeoLocation with latitude and longitude
        assertEquals("id1", rootInstance.getStructureChildren().get(0).getContentChildren().get(0).getValue());
        assertEquals("6", rootInstance.getStructureChildren().get(0).getStructureChildren().get(0).getContentChildren().get(
                0).getValue());
        assertEquals(12.78232, rootInstance.getStructureChildren().get(0).getStructureChildren().get(0)
                .getStructureChildren().get(0).getContentChildren().get(0).getValue());
        assertEquals(56.7455, rootInstance.getStructureChildren().get(0).getStructureChildren().get(0)
                .getStructureChildren().get(0).getContentChildren().get(1).getValue());

        // GeoLocation with latitude, longitude and height
        assertEquals("id2", rootInstance.getStructureChildren().get(1).getContentChildren().get(0).getValue());
        assertEquals("7", rootInstance.getStructureChildren().get(1).getStructureChildren().get(0).getContentChildren().get(
                0).getValue());
        assertEquals(22.78232, rootInstance.getStructureChildren().get(1).getStructureChildren().get(0)
                .getStructureChildren().get(0).getContentChildren().get(0).getValue());
        assertEquals(66.7455, rootInstance.getStructureChildren().get(1).getStructureChildren().get(0)
                .getStructureChildren().get(0).getContentChildren().get(1).getValue());
        assertEquals(123.1234, rootInstance.getStructureChildren().get(1).getStructureChildren().get(0)
                .getStructureChildren().get(0).getContentChildren().get(2).getValue());
    }

    @Test
    void testWrongEntityAttributes() {
        RootInstance rootInstance = assertDoesNotThrow(() -> parseAndValidateFromFile(
                "src/test/resources/yang/parser/wrong-attributes.json", customMetrics));
        assertEquals("OCUCPName_1", rootInstance.getStructureChildren().get(0).getContentChildren().get(0).getValue());
        assertEquals(12L, rootInstance.getStructureChildren().get(0).getStructureChildren().get(0).getContentChildren().get(
                0).getValue());
        assertEquals("str", rootInstance.getStructureChildren().get(0).getStructureChildren().get(0).getContentChildren()
                .get(1).getValue());
        assertThrows(IndexOutOfBoundsException.class, () -> rootInstance.getStructureChildren().get(0)
                .getStructureChildren().get(0).getContentChildren().get(2).getValue());
    }

    @Test
    void testSourceIdsWithoutList() throws YangException {
        RootInstance rootInstance = parseAndValidateFromFile("src/test/resources/yang/parser/source-ids-without-list.json",
                customMetrics);
        assertEquals(1, rootInstance.getStructureChildren().size());

        AbstractStructureInstance ODUFunction = rootInstance.getStructureChildren().get(0);
        List<YangDataDomNode> sourceIdsYang = ODUFunction.getContentChildren().get(0).getParent().getDataDomNode()
                .getChildren().stream().filter(child -> child.getName().equals("sourceIds")).toList();
        List<String> sourceIds = sourceIdsYang.stream().map(YangDataDomNode::getStringValue).toList();
        assertEquals(List.of("fdn_odu_21"), sourceIds);
    }

    @Test
    void testParseAndValidateFromFileWithUnknownPath() {
        assertThrows(YangException.class, () -> parseAndValidateFromFile("invalidPath.abc", customMetrics));
    }

    @Test
    void testEmptyYangModelException() throws IOException, YangException {
        assertThrows(YangSchemaException.class, () -> mockLoadModels(List.of(), List.of()));
        IngestionYangParser.loadModels();
    }

    @Test
    void testModulesAreNullException() throws IOException, YangException {
        try (MockedStatic<SchemaRegistry> mockedSchema = Mockito.mockStatic(SchemaRegistry.class)) {
            mockedSchema.when(SchemaRegistry::getModuleRegistry).thenReturn(null);
            assertThrows(YangSchemaException.class, () -> IngestionYangParser.loadModels());
        }
        IngestionYangParser.loadModels();
    }

    private void mockLoadModels(List<String> importList, List<String> implementList) throws YangException {
        List<YangModel> models = YangModelLoader.createYangModels(importList, implementList);
        try (MockedStatic<YangModelLoader> mockedLoader = Mockito.mockStatic(YangModelLoader.class)) {
            mockedLoader.when(YangModelLoader::loadModulesFromSchemaRegistry).thenReturn(models);
            mockedLoader.when(() -> YangModelLoader.createParserExecutionContext(any(), any(), any())).thenCallRealMethod();
            mockedLoader.when(() -> YangModelLoader.parseModelsIntoContext(any(), any())).thenCallRealMethod();
            IngestionYangParser.loadModels();
        }
    }

    private String loadYangModelFromTestFile(String resourcePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(resourcePath)), StandardCharsets.UTF_8);
    }

    private void assertFindingType(YangValidationException exception, String findingType) {
        assertEquals(1, exception.errorMessages.size());
        assertFindingsContainType(exception, findingType);
    }

    private void assertFindingsContainType(YangValidationException exception, String findingType) {
        assertTrue(exception.errorMessages.stream().anyMatch(finding -> findingType.equals(finding.getFindingType())));
    }

    private static RootInstance parseAndValidateFromFile(final String path, CustomMetrics customMetrics)
            throws YangException {
        String data = YangModelLoader.executeIOOperation(() -> Files.readString(Paths.get(path), StandardCharsets.UTF_8));
        return IngestionYangParser.parseData(data, true, customMetrics);
    }
}
