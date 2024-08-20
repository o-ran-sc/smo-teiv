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
package org.oran.smo.teiv.service.cloudevent.data;

import static org.oran.smo.teiv.schema.RelationshipDataLocation.A_SIDE;
import static org.oran.smo.teiv.schema.RelationshipDataLocation.B_SIDE;
import static org.oran.smo.teiv.schema.RelationshipDataLocation.RELATION;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import org.oran.smo.teiv.schema.RelationType;
import org.oran.smo.teiv.schema.SchemaRegistry;

public class ParsedCloudEventDataTest {

    @Test
    void testSortForEntities() {
        Entity e1 = new Entity("m", "type3", "id1", Map.of(), List.of());
        Entity e2 = new Entity("a", "type2", "id2", Map.of(), List.of());
        Entity e3 = new Entity("m", "type2", "id1", Map.of(), List.of());
        Entity e4 = new Entity("m", "type5", "id1", null, null);
        Entity e5 = new Entity("m", "type1", "id9999", Map.of(), List.of());
        List<Entity> entities = List.of(e1, e2, e3, e4, e5);
        ParsedCloudEventData data = new ParsedCloudEventData(entities, null);
        data.sort();
        assertEquals(e5, data.getEntities().get(0));
        assertEquals(e3, data.getEntities().get(1));
        assertEquals(e2, data.getEntities().get(2));
        assertEquals(e1, data.getEntities().get(3));
        assertEquals(e4, data.getEntities().get(4));
    }

    @Test
    void testSortForRelationships() {
        Relationship r1 = new Relationship("a", "type3", "id1", "a", "b", null);
        Relationship r2 = new Relationship("m", "type2", "id2", "y", "a", null);
        Relationship r3 = new Relationship("m", "type2", "id3", "a", "c", null);
        Relationship r4 = new Relationship("m", "type2", "id4", "z", "b", null);
        Relationship r5 = new Relationship("m", "type5", "id5", "a", "b", null);
        Relationship r6 = new Relationship("m", "type1", "id9999", "a", "b", null);
        List<Relationship> relationships = List.of(r1, r2, r3, r4, r5, r6);
        ParsedCloudEventData data = new ParsedCloudEventData(null, relationships);

        try (MockedStatic<SchemaRegistry> staticMock = Mockito.mockStatic(SchemaRegistry.class)) {
            staticMock.when(() -> SchemaRegistry.getRelationTypeByName("type2")).thenReturn(RelationType.builder()
                    .relationshipStorageLocation(A_SIDE).build());
            data.sort();
            assertEquals(r6, data.getRelationships().get(0));
            assertEquals(r3, data.getRelationships().get(1));
            assertEquals(r2, data.getRelationships().get(2));
            assertEquals(r4, data.getRelationships().get(3));
            assertEquals(r1, data.getRelationships().get(4));
            assertEquals(r5, data.getRelationships().get(5));
        }

        try (MockedStatic<SchemaRegistry> staticMock = Mockito.mockStatic(SchemaRegistry.class)) {
            staticMock.when(() -> SchemaRegistry.getRelationTypeByName("type2")).thenReturn(RelationType.builder()
                    .relationshipStorageLocation(B_SIDE).build());
            data.sort();
            assertEquals(r6, data.getRelationships().get(0));
            assertEquals(r2, data.getRelationships().get(1));
            assertEquals(r4, data.getRelationships().get(2));
            assertEquals(r3, data.getRelationships().get(3));
            assertEquals(r1, data.getRelationships().get(4));
            assertEquals(r5, data.getRelationships().get(5));
        }

        try (MockedStatic<SchemaRegistry> staticMock = Mockito.mockStatic(SchemaRegistry.class)) {
            staticMock.when(() -> SchemaRegistry.getRelationTypeByName("type2")).thenReturn(RelationType.builder()
                    .relationshipStorageLocation(RELATION).build());
            data.sort();
            assertEquals(r6, data.getRelationships().get(0));
            assertEquals(r2, data.getRelationships().get(1));
            assertEquals(r3, data.getRelationships().get(2));
            assertEquals(r4, data.getRelationships().get(3));
            assertEquals(r1, data.getRelationships().get(4));
            assertEquals(r5, data.getRelationships().get(5));
        }
    }

}
