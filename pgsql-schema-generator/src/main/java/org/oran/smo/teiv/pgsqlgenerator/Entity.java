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

import java.util.List;
import java.util.stream.Collectors;

import lombok.Value;
import org.oran.smo.teiv.pgsqlgenerator.schema.Table;
import lombok.Builder;

import static org.oran.smo.teiv.pgsqlgenerator.Constants.CLASSIFIERS;
import static org.oran.smo.teiv.pgsqlgenerator.Constants.DECORATORS;
import static org.oran.smo.teiv.pgsqlgenerator.Constants.ID;
import static org.oran.smo.teiv.pgsqlgenerator.Constants.JSONB;
import static org.oran.smo.teiv.pgsqlgenerator.Constants.SOURCE_IDS;

@Value
@Builder
public class Entity implements Table {
    String entityName;
    String storedAt;
    String moduleReferenceName;
    List<Attribute> attributes;
    @Builder.Default
    List<ConsumerData> consumerData = List.of(ConsumerData.builder().name(SOURCE_IDS).dataType(JSONB).defaultValue("[]")
            .indexType(IndexType.GIN_TRGM_OPS_ON_LIST_AS_JSONB).build(), ConsumerData.builder().name(CLASSIFIERS).dataType(
                    JSONB).defaultValue("[]").indexType(IndexType.GIN_TRGM_OPS_ON_LIST_AS_JSONB).build(), ConsumerData
                            .builder().name(DECORATORS).dataType(JSONB).defaultValue("{}").indexType(IndexType.GIN)
                            .build());

    @Override
    public String getTableName() {
        return "entity_info";
    }

    @Override
    public String getColumnsForCopyStatement() {
        return "(\"storedAt\", \"name\", \"moduleReferenceName\", \"attributeNames\")";
    }

    @Override
    public String getRecordForCopyStatement() {
        return this.getStoredAt() + "\t" + this.getEntityName() + "\t" + this.getModuleReferenceName() + "\t" + this
                .getAttributesNames().stream().map(attr -> "\"" + attr + "\"").toList() + "\n";
    }

    public List<String> getAttributesNames() {
        return this.getAttributes().stream().map(Attribute::getName).filter(name -> !name.equals(ID)).collect(Collectors
                .toList());
    }

}
