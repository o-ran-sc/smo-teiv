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
package org.oran.smo.teiv.exposure.spi.mapper;

import static org.jooq.impl.DSL.field;

import java.util.List;
import java.util.Map;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.oran.smo.teiv.api.model.OranTeivEntitiesResponseMessage;
import org.oran.smo.teiv.exposure.utils.RequestDetails;
import org.oran.smo.teiv.schema.MockSchemaLoader;
import org.oran.smo.teiv.schema.SchemaLoaderException;

class EntityMapperTest {

    EntityMapper entityMapper = new EntityMapper();

    @BeforeAll
    static void setUp() throws SchemaLoaderException {
        MockSchemaLoader mockSchemaLoader = new MockSchemaLoader();
        mockSchemaLoader.loadSchemaRegistry();
    }

    @Test
    void testMap() {
        final Result<Record> records = DSL.using(SQLDialect.POSTGRES).newResult();
        records.add(DSL.using(SQLDialect.POSTGRES).newRecord(field("o-ran-smo-teiv-ran:GNBDUFunction.id"), field(
                "o-ran-smo-teiv-ran:GNBDUFunction.attr.gNBId"), field("o-ran-smo-teiv-ran:NRCellDU.id"), field("count"))
                .values("9BCD297B8258F67908477D895636ED65", 1, null, null));
        records.add(DSL.using(SQLDialect.POSTGRES).newRecord(field("o-ran-smo-teiv-ran:GNBDUFunction.id"), field(
                "o-ran-smo-teiv-ran:GNBDUFunction.attr.gNBId"), field("o-ran-smo-teiv-ran:NRCellDU.id"), field("count"))
                .values(null, null, "98C3A4591A37718E1330F0294E23B62A", null));
        records.add(DSL.using(SQLDialect.POSTGRES).newRecord(field("o-ran-smo-teiv-ran:GNBDUFunction.id"), field(
                "o-ran-smo-teiv-ran:GNBDUFunction.attr.gNBId"), field("o-ran-smo-teiv-ran:NRCellDU.id"), field("count"))
                .values(null, null, null, 2));

        OranTeivEntitiesResponseMessage result = entityMapper.mapEntities(records, RequestDetails.builder().basePath(
                "/test").offset(0).limit(100).build());

        Assertions.assertEquals(2, result.getTotalCount());
        Assertions.assertEquals(List.of(Map.of("o-ran-smo-teiv-ran:GNBDUFunction", List.of(Map.of("id",
                "9BCD297B8258F67908477D895636ED65", "attributes", Map.of("gNBId", 1)))), Map.of(
                        "o-ran-smo-teiv-ran:NRCellDU", List.of(Map.of("id", "98C3A4591A37718E1330F0294E23B62A")))), result
                                .getItems());
    }
}
