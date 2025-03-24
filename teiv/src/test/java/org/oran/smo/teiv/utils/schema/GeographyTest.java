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
package org.oran.smo.teiv.utils.schema;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class GeographyTest {

    @Test
    void test2DGeographyFromJson() throws IOException {
        assertEquals("POINT(19.040236 47.497913)", new Geography("{\"longitude\": 19.040236, \"latitude\":47.497913}")
                .toString());

        assertEquals("POINT(19.040236 47.497913)", new Geography(
                "{\"longitude\": 19.040236, \"latitude\":47.497913, \"height\": null}").toString());

        assertEquals("POINT(19.040236 47.497913)", new Geography(
                "{\"location\":{\"ellipsoid\":{\"longitude\": 19.040236, \"latitude\":47.497913}}}").toString());

        assertEquals("POINT(19.040236 47.497913)", new Geography(
                "{\"location\":{\"longitude\": 19.040236, \"latitude\":47.497913}}").toString());
    }

    @Test
    void test3DGeographyFromJson() throws IOException {
        assertEquals("POINT Z (19.040236 47.497913 123.9878)", new Geography(
                "{\"longitude\": 19.040236,\"latitude\": 47.497913, \"height\": 123.9878}").toString());

        assertEquals("POINT Z (19.040236 47.497913 123.9878)", new Geography(
                "{\"location\":{\"ellipsoid\":{\"longitude\": 19.040236, \"latitude\":47.497913 , \"height\": 123.9878}}}")
                        .toString());

        assertEquals("POINT Z (19.040236 47.497913 123.9878)", new Geography(
                "{\"location\":{\"longitude\": 19.040236, \"latitude\":47.497913, \"height\": 123.9878}}}").toString());
    }

    @Test
    void testGeographyExpectionFromJson() {
        assertThrows(IOException.class, () -> new Geography("{\"longitude\": 19.040236}"));
        assertThrows(IOException.class, () -> new Geography("{\"longitude\": 19.040236, \"latitude\": null}"));

        assertThrows(IOException.class, () -> new Geography("{\"latitude\": 47.497913}"));
        assertThrows(IOException.class, () -> new Geography("{\"longitude\": null, \"latitude\": 47.497913}"));

        assertThrows(IOException.class, () -> new Geography("{\"longitude\": 19.040236 \"height\": 19.040236}"));
        assertThrows(IOException.class, () -> new Geography("{\"latitude\": 47.497913 \"height\": 19.040236}"));

        assertThrows(IOException.class, () -> new Geography(
                "{\"longitude\": 180.040236 \"latitude\": 47.497913 \"height\": 19.040236}"));
        assertThrows(IOException.class, () -> new Geography(
                "{\"longitude\": 19.040236 \"latitude\": -147.497913 \"height\": 19.040236}"));
    }
}
