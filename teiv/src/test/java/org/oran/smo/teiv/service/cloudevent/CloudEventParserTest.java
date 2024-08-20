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
package org.oran.smo.teiv.service.cloudevent;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.oran.smo.teiv.exception.YangModelException;
import org.oran.smo.teiv.schema.MockSchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoaderException;
import org.oran.smo.teiv.startup.SchemaHandler;
import io.cloudevents.CloudEvent;
import org.junit.jupiter.api.Assertions;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.oran.smo.teiv.service.cloudevent.data.Entity;
import org.oran.smo.teiv.service.cloudevent.data.ParsedCloudEventData;
import org.oran.smo.teiv.service.cloudevent.data.Relationship;
import org.oran.smo.teiv.utils.CloudEventTestUtil;

import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class CloudEventParserTest {

    @Autowired
    private CloudEventParser cloudEventParser;

    @MockBean
    private SchemaHandler schemaHandler;

    @BeforeEach
    public void setup() throws SchemaLoaderException, YangModelException, IOException {
        SchemaLoader mockSchemaLoader = new MockSchemaLoader();
        mockSchemaLoader.loadSchemaRegistry();
    }

    @Test
    void testParseCloudEventData() {
        final CloudEvent cloudEvent = cloudEventFromJson("src/test/resources/cloudeventdata/common/ce-with-data.json");
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        validateEntity(parsedCloudEventData.getEntities().get(0), "o-ran-smo-teiv-ran", "NRCellDU", "entityId_1", 6, Map.of(
                "cellLocalId", 4589L, "nRPCI", 12L, "nRTAC", 310L, "primitiveArray", "[1, 2, 3]", "singleList", "12",
                "jsonObjectArray", "[{\"test2\":\"49\",\"test1\":\"128\"}, {\"test2\":\"50\",\"test1\":\"129\"}]"));

        validateEntity(parsedCloudEventData.getEntities().get(1), "o-ran-smo-teiv-ran", "NRCellDU", "entityId_3", 6, Map.of(
                "cellLocalId", 45891L, "nRPCI", 121L, "nRTAC", 3101L, "primitiveArray", "[1, 2, 3]", "singleList", "121",
                "jsonObjectArray", "[{\"test2\":\"491\",\"test1\":\"1281\"}, {\"test2\":\"501\",\"test1\":\"1291\"}]"));

        validateEntity(parsedCloudEventData.getEntities().get(2), "o-ran-smo-teiv-ran", "NRSectorCarrier", "entityId_2", 4,
                Map.of("arfcnDL", 4590L, "testDouble", 32.5, "testBoolean", true, "cmId",
                        "{\"option1\":\"test_option1\",\"option2\":\"test_option2\"}"));

        final List<Relationship> relationships = parsedCloudEventData.getRelationships();
        Assertions.assertEquals(4, relationships.size());

        Relationship relationship = parsedCloudEventData.getRelationships().get(0);
        assertEquals("o-ran-smo-teiv-ran", relationship.getModule());
        assertEquals("NRCELLDU_USES_NRSECTORCARRIER", relationship.getType());
        assertEquals("relationshipId", relationship.getId());
        assertEquals(
                "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Ireland,MeContext=NR004,ManagedElement=me04,GNBDUFunction=gnbdu04,NRCellDU=NR-Cell-07",
                relationship.getASide());
        assertEquals("entityId_2", relationship.getBSide());
        assertEquals(null, relationship.getSourceIds());

        relationship = parsedCloudEventData.getRelationships().get(1);
        assertEquals("o-ran-smo-teiv-ran", relationship.getModule());
        assertEquals("NRCELLDU_USES_NRSECTORCARRIER", relationship.getType());
        assertEquals("relationshipId2", relationship.getId());
        assertEquals("entityId_3", relationship.getASide());
        assertEquals("entityId_4", relationship.getBSide());
        assertEquals(List.of("source10", "source20"), relationship.getSourceIds());

        relationship = parsedCloudEventData.getRelationships().get(2);
        assertEquals("o-ran-smo-teiv-ran", relationship.getModule());
        assertEquals("GNBDUFUNCTION_PROVIDES_NRCELLDU", relationship.getType());
        assertEquals(
                "urn:o-ran:smo:teiv:sha512:GNBDUFUNCTION_PROVIDES_NRCELLDU=89FE9A4ED8451D779865C389900E247B13E360B0A4175EBA80AA9B384BFA4C688F17865AFD934085B0235BCA66128F2E6D4CE6953EAAB2EDEBD94B3683C1A064",
                relationship.getId());
        assertEquals("entityId_5", relationship.getASide());
        assertEquals(
                "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Ireland,MeContext=NR004,ManagedElement=me04,GNBDUFunction=gnbdu04,NRCellDU=NR-Cell-07",
                relationship.getBSide());

        relationship = parsedCloudEventData.getRelationships().get(3);
        assertEquals("o-ran-smo-teiv-ran", relationship.getModule());
        assertEquals("GNBDUFUNCTION_PROVIDES_NRCELLDU", relationship.getType());
        assertEquals("relationshipId4", relationship.getId());
        assertEquals("entityId_5", relationship.getASide());
        assertEquals("entityId_3", relationship.getBSide());
        assertEquals(List.of("source21"), relationship.getSourceIds());
    }

    @Test
    void testParseCloudEventDataWithArray() {
        final CloudEvent cloudEvent = cloudEventFromJson("src/test/resources/cloudeventdata/common/ce-arrays.json");
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        // Parse an array of 3 elements
        validateEntity(parsedCloudEventData.getEntities().get(0), "o-ran-smo-teiv-ran", "AntennaCapability", "entityId_0",
                Map.of("eUtranFqBands", "[a, b, c]"), null);

        // Parse an array of 2 elements
        validateEntity(parsedCloudEventData.getEntities().get(1), "o-ran-smo-teiv-ran", "AntennaCapability", "entityId_1",
                Map.of("eUtranFqBands", "[a, b]"), null);

        // Parse an array of 1 element
        validateEntity(parsedCloudEventData.getEntities().get(2), "o-ran-smo-teiv-ran", "AntennaCapability", "entityId_2",
                Map.of("geranFqBands", "a"), null);

        // Parse an empty array
        validateEntity(parsedCloudEventData.getEntities().get(3), "o-ran-smo-teiv-ran", "AntennaCapability", "entityId_3",
                Map.of("eUtranFqBands", "q"), null);

        Assertions.assertEquals(0, parsedCloudEventData.getRelationships().size());
    }

    @Test
    void testEmptyCloudEventData() {
        final CloudEvent cloudEvent = CloudEventTestUtil.getCloudEvent("create", "{}");
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
        Assertions.assertTrue(parsedCloudEventData.getEntities().isEmpty());
        Assertions.assertTrue(parsedCloudEventData.getRelationships().isEmpty());
    }

    @Test
    void testNoRelationshipInCloudEvent() {
        final CloudEvent cloudEvent = cloudEventFromJson("src/test/resources/cloudeventdata/common/ce-one-entity.json");
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
        validateEntity(parsedCloudEventData.getEntities().get(0), "o-ran-smo-teiv-ran", "NRCellDU", "entityId_1", Map.of(
                "cellLocalId", 4589L, "nRPCI", 12L, "nRTAC", 310L), null);
        Assertions.assertTrue(parsedCloudEventData.getRelationships().isEmpty());
    }

    @Test
    void testCloudEventDataIsNotAValidJson() {
        final CloudEvent cloudEvent = CloudEventTestUtil.getCloudEvent("create", "{invalidjson");
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
        assertNull(parsedCloudEventData);
    }

    @Test
    void testEntitiesAndRelationshipsAreArrays() {
        final CloudEvent cloudEvent = CloudEventTestUtil.getCloudEvent("create", "{\"entities\":[],\"relationships\":[]}");
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
        assertEquals(new ParsedCloudEventData(List.of(), List.of()), parsedCloudEventData);
    }

    @Test
    void testInvalidYangDataInEvent() {
        final CloudEvent arrayEntitiesCloudEvent = CloudEventTestUtil.getCloudEvent("create",
                "{\"entities\":[{\"some_entity_field\": 54321}]}");
        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(arrayEntitiesCloudEvent);
        assertNull(parsedCloudEventData);

        final CloudEvent arrayRelationshipsCloudEvent = CloudEventTestUtil.getCloudEvent("create",
                "{\"relationships\":[{\"some_relationship_field\": 54321}]}");
        parsedCloudEventData = cloudEventParser.getCloudEventData(arrayRelationshipsCloudEvent);
        assertNull(parsedCloudEventData);
    }

    @Test
    void testRelationshipsIsNotAValidYangData() {
        CloudEvent cloudEvent = CloudEventTestUtil.getCloudEvent("create", "{\"relationships\":[{\"a_field\": 123}]}");
        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        assertNull(parsedCloudEventData);

        cloudEvent = CloudEventTestUtil.getCloudEvent("merge", "{\"relationships\":[{\"a_field\": 123}]}");
        parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        assertNull(parsedCloudEventData);

        cloudEvent = CloudEventTestUtil.getCloudEvent("delete", "{\"relationships\":[{\"a_field\": 123}]}");
        parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        assertNull(parsedCloudEventData);
    }

    @Test
    void testRelationshipMissingASide() {
        final CloudEvent cloudEvent = cloudEventFromJson(
                "src/test/resources/cloudeventdata/common/ce-relationship-missing-a-side.json");

        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        assertNull(parsedCloudEventData);
    }

    @Test
    void testRelationshipMissingBSide() {
        final CloudEvent cloudEvent = cloudEventFromJson(
                "src/test/resources/cloudeventdata/common/ce-relationship-missing-b-side.json");

        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        assertNull(parsedCloudEventData);
    }

    @Test
    void testRelationshipMissingBothSides() {
        final CloudEvent cloudEvent = cloudEventFromJson(
                "src/test/resources/cloudeventdata/common/ce-relationship-missing-both-sides.json");

        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        assertNull(parsedCloudEventData);
    }

    @Test
    void testRelationshipMissingId() {
        final CloudEvent cloudEvent = cloudEventFromJson(
                "src/test/resources/cloudeventdata/common/ce-relationship-missing-id.json");

        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        assertNull(parsedCloudEventData);
    }

    @Test
    void testRelationshipInvalidType() {
        final CloudEvent cloudEvent = cloudEventFromJson(
                "src/test/resources/cloudeventdata/common/ce-relationship-invalid-type.json");

        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        assertNull(parsedCloudEventData);
    }

    @Test
    void testRelationshipInvalidModule() {
        final CloudEvent cloudEvent = cloudEventFromJson(
                "src/test/resources/cloudeventdata/common/ce-relationship-invalid-module.json");

        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        assertNull(parsedCloudEventData);
    }

    @Test
    void testRelationshipInvalidModuleTypePair() {
        final CloudEvent cloudEvent = cloudEventFromJson(
                "src/test/resources/cloudeventdata/common/ce-relationship-invalid-module-type-pair.json");

        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        assertNull(parsedCloudEventData);
    }

    @Test
    void testParseCloudEventDataWithInvalidRelationshipId() {
        final CloudEvent cloudEvent = cloudEventFromJson(
                "src/test/resources/cloudeventdata/common/ce-with-invalid-relationship-ids.json");
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        assertNull(parsedCloudEventData);
    }

    @Test
    void testParseCloudEventDataWithInvalidRelationshipASideId() {
        final CloudEvent cloudEvent = cloudEventFromJson(
                "src/test/resources/cloudeventdata/common/ce-with-invalid-relationship-ids2.json");
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        assertNull(parsedCloudEventData);
    }

    @Test
    void testParseCloudEventDataWithInvalidRelationshipBSideId() {
        final CloudEvent cloudEvent = cloudEventFromJson(
                "src/test/resources/cloudeventdata/common/ce-with-invalid-relationship-ids3.json");
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        assertNull(parsedCloudEventData);
    }

    private void validateEntity(final Entity entity, String expectedModuleReference, String expectedEntityName,
            String expectedId, Map<String, Object> expectedAttributes, List<String> expectedSourceIds) {
        assertEquals(expectedModuleReference, entity.getModule());
        assertEquals(expectedEntityName, entity.getType());
        assertEquals(expectedId, entity.getId());
        final Map<String, Object> attributes = entity.getAttributes();
        assertEquals(expectedAttributes.size(), attributes.size());
        for (Map.Entry<String, Object> entry : expectedAttributes.entrySet()) {
            assertTrue(attributes.containsKey(entry.getKey()));
            assertEquals(entry.getValue().getClass(), attributes.get(entry.getKey()).getClass());
            assertEquals(entry.getValue(), attributes.get(entry.getKey()));
        }
        assertEquals(expectedSourceIds, entity.getSourceIds());
    }

    private void validateEntity(final Entity entity, String expectedModuleReference, String expectedEntityName,
            String expectedId, int expectedAttributeCount, Map<String, Object> expectedAttributes) {
        assertEquals(expectedModuleReference, entity.getModule());
        assertEquals(expectedEntityName, entity.getType());
        assertEquals(expectedId, entity.getId());
        final Map<String, Object> attributes = entity.getAttributes();
        assertEquals(expectedAttributeCount, attributes.size());
        for (Map.Entry<String, Object> entry : expectedAttributes.entrySet()) {
            assertTrue(attributes.containsKey(entry.getKey()));
            assertEquals(entry.getValue(), attributes.get(entry.getKey()));
        }
    }

    private CloudEvent cloudEventFromJson(String path) {
        return assertDoesNotThrow(() -> CloudEventTestUtil.getCloudEventFromJsonFile(path),
                "Reading CloudEvent from JSON resulted in error.");

    }
}
