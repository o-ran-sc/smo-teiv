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
package org.oran.smo.teiv.exposure.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.oran.smo.teiv.api.model.OranTeivHref;
import org.oran.smo.teiv.exception.TeivException;

class PaginationUtilTest {
    private static final RequestDetails requestDetails = RequestDetails.builder().basePath("/test").queryParam("q1", "val2")
            .offset(2).limit(5).build();

    @Test
    void testGetViableLimit() {
        //offset and limit are less than totalCount
        final int limit1 = PaginationUtil.getViableLimit(0, 5, 10);
        assertEquals(5, limit1);

        //offset is less than totalCount and limit is greater than totalCount
        final int limit2 = PaginationUtil.getViableLimit(0, 5, 3);
        assertEquals(3, limit2);

        //totalCount is 0
        final int limit3 = PaginationUtil.getViableLimit(0, 5, 0);
        assertEquals(0, limit3);

        //offset is greater than totalCount
        TeivException exception = assertThrows(TeivException.class, () -> PaginationUtil.getViableLimit(15, 5, 10));
        assertEquals("Offset cannot be larger than 9", exception.getDetails());
    }

    @Test
    void firstHref() {
        final OranTeivHref actual = PaginationUtil.firstHref(requestDetails);

        assertEquals(OranTeivHref.builder().href("/test?offset=0&limit=5&q1=val2").build(), actual);
    }

    @Test
    void prevHref() {
        //totalCount < limit
        final OranTeivHref actual1 = PaginationUtil.prevHref(requestDetails, 3);

        assertEquals(OranTeivHref.builder().href("/test?offset=0&limit=5&q1=val2").build(), actual1);

        //totalCount = limit
        final OranTeivHref actual2 = PaginationUtil.prevHref(requestDetails, 5);

        assertEquals(OranTeivHref.builder().href("/test?offset=0&limit=5&q1=val2").build(), actual2);

        //totalCount > limit
        final OranTeivHref actual3 = PaginationUtil.prevHref(requestDetails, 10);

        assertEquals(OranTeivHref.builder().href("/test?offset=0&limit=5&q1=val2").build(), actual3);
    }

    @Test
    void selfHref() {
        final OranTeivHref actual = PaginationUtil.selfHref(requestDetails);

        assertEquals(OranTeivHref.builder().href("/test?offset=2&limit=5&q1=val2").build(), actual);
    }

    @Test
    void nextHref() {
        //totalCount < limit
        final OranTeivHref actual1 = PaginationUtil.nextHref(requestDetails, 3);

        assertEquals(OranTeivHref.builder().href("/test?offset=2&limit=5&q1=val2").build(), actual1);

        //totalCount = limit
        final OranTeivHref actual2 = PaginationUtil.nextHref(requestDetails, 5);

        assertEquals(OranTeivHref.builder().href("/test?offset=2&limit=5&q1=val2").build(), actual2);

        //totalCount > limit
        final OranTeivHref actual3 = PaginationUtil.nextHref(requestDetails, 10);

        assertEquals(OranTeivHref.builder().href("/test?offset=7&limit=5&q1=val2").build(), actual3);
    }

    @Test
    void lastHref() {
        //totalCount < limit
        final OranTeivHref actual = PaginationUtil.lastHref(requestDetails, 3);

        assertEquals(OranTeivHref.builder().href("/test?offset=2&limit=5&q1=val2").build(), actual);

        //totalCount = limit
        final OranTeivHref actual1 = PaginationUtil.lastHref(requestDetails, 5);

        assertEquals(OranTeivHref.builder().href("/test?offset=2&limit=5&q1=val2").build(), actual1);

        //totalCount > limit
        final OranTeivHref actual2 = PaginationUtil.lastHref(requestDetails, 10);

        assertEquals(OranTeivHref.builder().href("/test?offset=7&limit=5&q1=val2").build(), actual2);
    }
}
