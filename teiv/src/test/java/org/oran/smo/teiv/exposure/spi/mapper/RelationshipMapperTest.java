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

import static org.oran.smo.teiv.schema.RelationshipDataLocation.B_SIDE;
import static org.jooq.impl.DSL.field;

import java.util.List;
import java.util.Map;

import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.oran.smo.teiv.exposure.spi.Module;
import org.oran.smo.teiv.schema.Association;
import org.oran.smo.teiv.schema.EntityType;
import org.oran.smo.teiv.schema.MockSchemaLoader;
import org.oran.smo.teiv.schema.RelationType;
import org.oran.smo.teiv.schema.SchemaLoaderException;

class RelationshipMapperTest {
    private static RelationshipMapper relationshipMapper;
    private static RelationType relationType;

    @BeforeAll
    static void setUp() throws SchemaLoaderException {
        MockSchemaLoader mockSchemaLoader = new MockSchemaLoader();
        mockSchemaLoader.loadSchemaRegistry();
        relationType = RelationType.builder().name("GNBDUFUNCTION_PROVIDES_NRCELLDU").aSideAssociation(Association.builder()
                .name("provided-nrCellDu").build()).aSide(EntityType.builder().name("GNBDUFUNCTION").build())
                .bSideAssociation(Association.builder().name("provided-by-gnbduFunction").build()).bSide(EntityType
                        .builder().name("NRCellDU").build()).relationshipStorageLocation(B_SIDE).tableName(
                                "o-ran-smo-teiv-ran_NRCellDU").module(Module.builder().name("o-ran-smo-teiv-ran").build())
                .build();
        relationshipMapper = new RelationshipMapper();
    }

    @Test
    void testCreateProperties() {
        Record record = DSL.using(SQLDialect.POSTGRES).newRecord(field(
                "teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"REL_ID_GNBDUFUNCTION_PROVIDES_NRCELLDU\""), field(
                        "teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"REL_FK_provided-by-gnbduFunction\""), field(
                                "teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"id\""), field(
                                        "teiv_data.\"o-ran-smo-teiv-ran_NRCellDU\".\"REL_CD_sourceIds_GNBDUFUNCTION_PROVIDES_NRCELLDU\""))
                .values("urn:base64:R05CRFVGdW5jdGlvbjo2QTBENUFBMjhGNzcwQzk5NDFCNzRFQkU1NzYxMUFFMTpQUk9WSURFUzpOUkNlbGxEVTowMDAxNjFCMDE0QzMyMDEwNkE5RDZCMTQxN0Y4RUIwQQ==",
                        "6A0D5AA28F770C9941B74EBE57611AE1", "000161B014C320106A9D6B1417F8EB0A", List.of("sid1", "sid2"));

        Map<String, Object> result = Map.of("id",
                "urn:base64:R05CRFVGdW5jdGlvbjo2QTBENUFBMjhGNzcwQzk5NDFCNzRFQkU1NzYxMUFFMTpQUk9WSURFUzpOUkNlbGxEVTowMDAxNjFCMDE0QzMyMDEwNkE5RDZCMTQxN0Y4RUIwQQ==",
                "aSide", "6A0D5AA28F770C9941B74EBE57611AE1", "bSide", "000161B014C320106A9D6B1417F8EB0A", "sourceIds", List
                        .of("sid1", "sid2"));

        Assertions.assertEquals(result, relationshipMapper.createProperties(record, relationType));
    }
}
