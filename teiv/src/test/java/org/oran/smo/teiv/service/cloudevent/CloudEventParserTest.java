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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;

import org.oran.smo.teiv.CustomMetrics;
import org.oran.smo.teiv.exception.YangException;
import org.oran.smo.teiv.schema.MockSchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoaderException;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.schema.SchemaRegistryException;
import org.oran.smo.teiv.service.cloudevent.data.Entity;
import org.oran.smo.teiv.service.cloudevent.data.ParsedCloudEventData;
import org.oran.smo.teiv.service.cloudevent.data.Relationship;
import org.oran.smo.teiv.utils.CloudEventTestUtil;
import org.oran.smo.teiv.utils.yangparser.IngestionYangParser;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class CloudEventParserTest {
    private static CloudEventParser cloudEventParser;

    @Mock
    private static CustomMetrics customMetrics;

    @BeforeAll
    public static void setup() throws SchemaLoaderException, YangException {
        SchemaLoader mockSchemaLoader = new MockSchemaLoader();
        mockSchemaLoader.loadSchemaRegistry();
        IngestionYangParser.loadModels();
        cloudEventParser = new CloudEventParser(customMetrics, new ObjectMapper());
        ReflectionTestUtils.setField(cloudEventParser, "isYangValidationEnabled", true);
    }

    @Test
    void testParseCloudEventData() {
        final CloudEvent cloudEvent = cloudEventFromJson("src/test/resources/cloudeventdata/common/ce-with-data.json");
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        validateEntity(parsedCloudEventData.getEntities().get(0), "o-ran-smo-teiv-ran", "NRCellDU",
                "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Ireland,MeContext=NR004,ManagedElement=me04,ODUFunction=odu04,NRCellDU=NR-Cell-07",
                Map.of("cellLocalId", 4589L, "nRPCI", 12L, "nRTAC", 310L), List.of("source1", "source2"));

        validateEntity(parsedCloudEventData.getEntities().get(1), "o-ran-smo-teiv-ran", "NRCellDU", "entityId_3", Map.of(
                "cellLocalId", 45891L, "nRPCI", 121L, "nRTAC", 3101L), List.of("source3"));

        validateEntity(parsedCloudEventData.getEntities().get(2), "o-ran-smo-teiv-ran", "NRSectorCarrier", "entityId_2", Map
                .of("arfcnDL", 4590L), List.of("source1", "source2"));

        validateEntity(parsedCloudEventData.getEntities().get(3), "o-ran-smo-teiv-ran", "NRCellCU", "entityId_4", Map.of(
                "nCI", 123L, "nRTAC", 32L, "cellLocalId", 456L, "plmnId", "{\"mcc\":\"209\",\"mnc\":\"751\"}"), List.of(
                        "source1", "source2"));

        final List<Relationship> relationships = parsedCloudEventData.getRelationships();
        assertEquals(4, relationships.size());

        Relationship relationship = parsedCloudEventData.getRelationships().get(0);
        assertEquals("o-ran-smo-teiv-ran", relationship.getModule());
        assertEquals("NRCELLDU_USES_NRSECTORCARRIER", relationship.getType());
        assertEquals("relationshipId", relationship.getId());
        assertEquals(
                "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Ireland,MeContext=NR004,ManagedElement=me04,ODUFunction=odu04,NRCellDU=NR-Cell-07",
                relationship.getASide());
        assertEquals("entityId_2", relationship.getBSide());
        assertEquals(List.of("source1", "source2"), relationship.getSourceIds());

        relationship = parsedCloudEventData.getRelationships().get(1);
        assertEquals("o-ran-smo-teiv-ran", relationship.getModule());
        assertEquals("NRCELLDU_USES_NRSECTORCARRIER", relationship.getType());
        assertEquals("relationshipId2", relationship.getId());
        assertEquals("entityId_3", relationship.getASide());
        assertEquals("entityId_4", relationship.getBSide());
        assertEquals(List.of("source10", "source20"), relationship.getSourceIds());

        relationship = parsedCloudEventData.getRelationships().get(2);
        assertEquals("o-ran-smo-teiv-ran", relationship.getModule());
        assertEquals("ODUFUNCTION_PROVIDES_NRCELLDU", relationship.getType());
        assertEquals(
                "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=89FE9A4ED8451D779865C389900E247B13E360B0A4175EBA80AA9B384BFA4C688F17865AFD934085B0235BCA66128F2E6D4CE6953EAAB2EDEBD94B3683C1A064",
                relationship.getId());
        assertEquals("entityId_5", relationship.getASide());
        assertEquals(
                "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Ireland,MeContext=NR004,ManagedElement=me04,ODUFunction=odu04,NRCellDU=NR-Cell-07",
                relationship.getBSide());

        relationship = parsedCloudEventData.getRelationships().get(3);
        assertEquals("o-ran-smo-teiv-ran", relationship.getModule());
        assertEquals("ODUFUNCTION_PROVIDES_NRCELLDU", relationship.getType());
        assertEquals("relationshipId4", relationship.getId());
        assertEquals("entityId_5", relationship.getASide());
        assertEquals("entityId_3", relationship.getBSide());
        assertEquals(List.of("source21", "source22"), relationship.getSourceIds());
    }

    @Test
    void testParseCloudEventDataWithComplexAttributes() {
        final CloudEvent cloudEvent = cloudEventFromJson(
                "src/test/resources/cloudeventdata/common/ce-complex-attributes.json");
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        // Can parse complex attributes like "plmnId" into a json string
        validateEntity(parsedCloudEventData.getEntities().get(0), "o-ran-smo-teiv-ran", "NRCellCU", "entityId_1", Map.of(
                "nCI", 123L, "nRTAC", 32L, "cellLocalId", 456L, "plmnId", "{\"mcc\":\"209\",\"mnc\":\"751\"}"), List.of(
                        "source1", "source2"));

        // If the "plmnId" is not specified, then it's not added as an attribute
        validateEntity(parsedCloudEventData.getEntities().get(1), "o-ran-smo-teiv-ran", "NRCellCU", "entityId_2", Map.of(
                "nCI", 123L, "nRTAC", 32L), List.of("source1", "source2"));

        validateEntity(parsedCloudEventData.getEntities().get(2), "o-ran-smo-teiv-ran", "NRCellCU", "entityId_3", Map.of(
                "nCI", 123L, "nRTAC", 32L, "cellLocalId", 456L, "plmnId", "{\"mcc\":\"209\",\"mnc\":\"752\"}"), List.of(
                        "source1", "source2"));

        assertEquals(0, parsedCloudEventData.getRelationships().size());
    }

    @Test
    void testParseCloudEventDataWithNullAttribute() {
        final CloudEvent cloudEvent = cloudEventFromJson("src/test/resources/cloudeventdata/common/ce-null-attribute.json");
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
        final Map<String, Object> expectedAttributes = new HashMap<>();
        expectedAttributes.put("gNBDUId", null);
        expectedAttributes.put("dUpLMNId", "{}");

        validateEntity(parsedCloudEventData.getEntities().get(0), "o-ran-smo-teiv-ran", "ODUFunction", "entityId_1",
                expectedAttributes, List.of("source1", "source2"));
    }

    @Test
    void testParseCloudEventDataCannotGetAttributesFromSchemaRegistry() {
        final CloudEvent cloudEvent = cloudEventFromJson("src/test/resources/cloudeventdata/common/ce-null-attribute.json");
        try (MockedStatic<SchemaRegistry> utilities = Mockito.mockStatic(SchemaRegistry.class)) {
            utilities.when(() -> SchemaRegistry.getEntityTypeByModuleAndName(any(), any())).thenThrow(
                    SchemaRegistryException.class);

            final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
            validateEntity(parsedCloudEventData.getEntities().get(0), "o-ran-smo-teiv-ran", "ODUFunction", "entityId_1", Map
                    .of(), List.of("source1", "source2"));
        }
    }

    @Test
    void testParseCloudEventDataWithArray() {
        final CloudEvent cloudEvent = cloudEventFromJson("src/test/resources/cloudeventdata/common/ce-arrays.json");
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        // Parse an array of 3 elements
        validateEntity(parsedCloudEventData.getEntities().get(0), "o-ran-smo-teiv-ran", "AntennaCapability", "entityId_0",
                Map.of("eUtranFqBands", "[\"a\",\"b\",\"c\"]"), List.of("source1", "source2"));

        // Parse an array of 2 elements
        validateEntity(parsedCloudEventData.getEntities().get(1), "o-ran-smo-teiv-ran", "AntennaCapability", "entityId_1",
                Map.of("eUtranFqBands", "[\"a\",\"b\"]"), List.of("source1", "source2"));

        // Parse an array of 1 element
        validateEntity(parsedCloudEventData.getEntities().get(2), "o-ran-smo-teiv-ran", "AntennaCapability", "entityId_2",
                Map.of("geranFqBands", "[\"a\"]"), List.of("source1", "source2"));

        // Parse an empty array
        validateEntity(parsedCloudEventData.getEntities().get(3), "o-ran-smo-teiv-ran", "AntennaCapability", "entityId_3",
                Map.of("eUtranFqBands", "[\"q\"]"), List.of("source1", "source2"));

        assertEquals(0, parsedCloudEventData.getRelationships().size());
    }

    @Test
    void testParseIngestionDataWith64BitNumbersAsJsonString() {
        final CloudEvent cloudEvent = cloudEventFromJson(
                "src/test/resources/cloudeventdata/common/ce-64bit-numbers-as-string.json");
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        // Validate that the int64 attribute represented as a json string is parsed into
        // a java Long.
        validateEntity(parsedCloudEventData.getEntities().get(2), "o-ran-smo-teiv-ran", "NRCellCU", "entityId_1", Map.of(
                "nCI", 123L, "nRTAC", 32L), List.of("source1", "source2"));

        // Validate that a uint64 is parsed into a java Long. The value is greater than
        // Long.MAX_VALUE, so it's stored as a negative signed long.
        // The db column is BIGINT, which is a signed int64, so we have to store the
        // values greater than Long.MAX_VALUE as a negative value.
        validateEntity(parsedCloudEventData.getEntities().get(0), "o-ran-smo-teiv-ran", "Sector", "entityId_2", Map.of(
                "sectorId", -8223372036854775809L), List.of("source1", "source2"));

        // Validate that a decimal64 is parsed into a java BigDecimal. A java double
        // could store only a subset of the possible yang decimal64 values.
        validateEntity(parsedCloudEventData.getEntities().get(1), "o-ran-smo-teiv-ran", "Sector", "entityId_3", Map.of(
                "sectorId", 5L, "azimuth", BigDecimal.valueOf(Long.MIN_VALUE, 6)), List.of("source1", "source2"));

        assertEquals(0, parsedCloudEventData.getRelationships().size());
    }

    @Test
    void testParseIngestionDataWith64BitNumbersAsJsonNumbers() {
        final CloudEvent cloudEvent = cloudEventFromJson(
                "src/test/resources/cloudeventdata/common/ce-64bit-numbers-as-json-numbers.json");
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        validateEntity(parsedCloudEventData.getEntities().get(2), "o-ran-smo-teiv-ran", "NRCellCU", "entityId_1", Map.of(
                "nCI", 123L, "nRTAC", 32L), List.of("source1", "source2"));
        validateEntity(parsedCloudEventData.getEntities().get(0), "o-ran-smo-teiv-ran", "Sector", "entityId_2", Map.of(
                "sectorId", -8223372036854775809L), List.of("source1", "source2"));

        // The value in the json is not representable with a double with the same
        // precision. So we lose the last 3 digits. That's why the RFC7951 recommends to
        // encode decimal64 as json string.
        validateEntity(parsedCloudEventData.getEntities().get(1), "o-ran-smo-teiv-ran", "Sector", "entityId_3", Map.of(
                "sectorId", 5L, "azimuth", new BigDecimal("-9223372036854.775")), List.of("source1", "source2"));

        assertEquals(0, parsedCloudEventData.getRelationships().size());
    }

    @Test
    void testParseIngestionDataWithGeoLocation() {
        final CloudEvent cloudEvent = cloudEventFromJson("src/test/resources/cloudeventdata/common/ce-geo-location.json");
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        validateEntity(parsedCloudEventData.getEntities().get(0), "o-ran-smo-teiv-equipment", "Site", "Site1", Map.of(
                "name", "geo-location1", "geo-location", "{\"latitude\":12.78232,\"longitude\":56.7455}"), List.of(
                        "urn:3gpp:dn:fdn1", "urn:cmHandle:1234"));

        validateEntity(parsedCloudEventData.getEntities().get(1), "o-ran-smo-teiv-equipment", "Site", "Site2", Map.of(
                "name", "geo-location2", "geo-location",
                "{\"latitude\":12.78232,\"longitude\":56.7455,\"height\":123.1234}"), List.of("urn:3gpp:dn:fdn2",
                        "urn:cmHandle:2234"));
    }

    @Test
    void testDeleteParseCloudEventData() {
        final CloudEvent cloudEvent = cloudEventFromJson(
                "src/test/resources/cloudeventdata/common/ce-delete-entity-id.json");
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        validateEntity(parsedCloudEventData.getEntities().get(0), "o-ran-smo-teiv-ran", "NRCellDU",
                "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Ireland,MeContext=NR004,ManagedElement=me04,ODUFunction=odu04,NRCellDU=NR-Cell-07",
                Map.of(), null);
    }

    @Test
    void testEmptyCloudEventData() {
        final CloudEvent cloudEvent = CloudEventTestUtil.getCloudEvent("create", "{}");
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
        assertTrue(parsedCloudEventData.getEntities().isEmpty());
        assertTrue(parsedCloudEventData.getRelationships().isEmpty());
    }

    @Test
    void testNoRelationshipInCloudEvent() {
        final CloudEvent cloudEvent = cloudEventFromJson("src/test/resources/cloudeventdata/common/ce-one-entity.json");
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
        validateEntity(parsedCloudEventData.getEntities().get(0), "o-ran-smo-teiv-ran", "NRCellDU", "entityId_1", Map.of(
                "cellLocalId", 4589L, "nRPCI", 12L, "nRTAC", 310L), List.of("source1", "source2"));
        assertTrue(parsedCloudEventData.getRelationships().isEmpty());
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
    void testRelationshipMissingSourceIds() {
        final CloudEvent cloudEvent = cloudEventFromJson(
                "src/test/resources/cloudeventdata/common/ce-relationship-missing-source-ids.json");

        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        assertNull(parsedCloudEventData);
    }

    @Test
    void testRelationshipWithOneSourceId() {
        final CloudEvent cloudEvent = cloudEventFromJson(
                "src/test/resources/cloudeventdata/common/ce-relationship-with-one-source-id.json");

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
    void testParseCloudEventDataWithInvalidEntityId() {
        final CloudEvent cloudEvent = cloudEventFromJson(
                "src/test/resources/cloudeventdata/common/ce-with-invalid-entity-id.json");
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

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

    @Test
    void testParseCloudEventDataWithDeprecatedFormat() {
        final CloudEvent cloudEvent = cloudEventFromJson("src/test/resources/cloudeventdata/common/ce-deprecated.json");
        final ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);

        assertNull(parsedCloudEventData);
    }

    @Test
    void testParseNullCloudEvent() {
        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(null);
        assertNull(parsedCloudEventData);
    }

    @Test
    void testParseInvalidCloudEvent() {
        CloudEvent cloudEvent = CloudEventTestUtil.getCloudEvent("create", "{\"entities\":[{\"a_field\"# 123}]}");
        ParsedCloudEventData parsedCloudEventData = cloudEventParser.getCloudEventData(cloudEvent);
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
            if (Objects.nonNull(entry.getValue())) {
                assertEquals(entry.getValue().getClass(), attributes.get(entry.getKey()).getClass());
                assertEquals(entry.getValue(), attributes.get(entry.getKey()));
            } else {
                assertNull(attributes.get(entry.getKey()));
            }
        }
        assertEquals(expectedSourceIds, entity.getSourceIds());
    }

    private CloudEvent cloudEventFromJson(String path) {
        return assertDoesNotThrow(() -> CloudEventTestUtil.getCloudEventFromJsonFile(path),
                "Reading CloudEvent from JSON resulted in error.");

    }
}
