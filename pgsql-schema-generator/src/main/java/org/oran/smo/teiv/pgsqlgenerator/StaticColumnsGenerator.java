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
package org.oran.smo.teiv.pgsqlgenerator;

import static org.oran.smo.teiv.pgsqlgenerator.Constants.COLUMN;
import static org.oran.smo.teiv.pgsqlgenerator.Constants.JSONB;
import static org.oran.smo.teiv.pgsqlgenerator.Constants.METADATA;
import static org.oran.smo.teiv.pgsqlgenerator.Constants.NO_PREFIX;
import static org.oran.smo.teiv.pgsqlgenerator.Constants.REL_METADATA_PREFIX;

import java.util.List;

import org.oran.smo.teiv.pgsqlgenerator.schema.model.HashInfoDataGenerator;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StaticColumnsGenerator {

    public List<Column> getStaticColumnsForEntityTables(HashInfoDataGenerator hashInfoDataGenerator) {
        return List.of(Column.builder().name(hashInfoDataGenerator.generateHashAndRegisterTableRow(NO_PREFIX, METADATA,
                COLUMN)).dataType(JSONB).build());
    }

    public List<Column> getStaticColumnsForRelationshipTables(HashInfoDataGenerator hashInfoDataGenerator) {
        return List.of(Column.builder().name(hashInfoDataGenerator.generateHashAndRegisterTableRow(NO_PREFIX, METADATA,
                COLUMN)).dataType(JSONB).build());
    }

    public List<Column> getStaticColumnsForRelationshipsInEntity(String relationshipName,
            HashInfoDataGenerator hashInfoDataGenerator) {
        return List.of(Column.builder().name(hashInfoDataGenerator.generateHashAndRegisterTableRow(REL_METADATA_PREFIX,
                relationshipName, COLUMN)).dataType(JSONB).build());
    }

}
