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
package org.oran.smo.teiv.exposure.consumerdata.operation;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.jooq.Configuration;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.UpdateResultStep;
import org.oran.smo.teiv.exposure.consumerdata.model.ConsumerData;
import org.oran.smo.teiv.exposure.consumerdata.model.PersistableIdMap;
import org.oran.smo.teiv.schema.EntityType;
import org.oran.smo.teiv.schema.RelationType;
import org.oran.smo.teiv.service.models.OperationResult;
import org.springframework.stereotype.Component;

import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.oran.smo.teiv.utils.TiesConstants.QUOTED_STRING;

@Component
public class DeleteClassifiersOperation extends ClassifiersOperation {

    public DeleteClassifiersOperation(DSLContext readDataDslContext, DSLContext writeDataDslContext) {
        super(readDataDslContext, writeDataDslContext);
    }

    @Override
    protected List<OperationResult> performOperation(final PersistableIdMap map, final List<String> consumerData,
            final DSLContext writeDataDslContext) {

        final Map<String, OperationResult> allResults = new HashMap<>();

        map.persistableWithIds().forEach((persistable, ids) -> {

            for (String data : consumerData) {
                final String classifiersColumnName = String.format(QUOTED_STRING, persistable.getClassifiersColumnName());
                final Field<JSONB> targetField = field(classifiersColumnName, JSONB.class);
                final Field<JSONB> concatField = field(String.format("%s - '%s'", classifiersColumnName, data),
                        JSONB.class);

                final UpdateResultStep<Record> update = writeDataDslContext.update(table(persistable.getTableName())).set(
                        targetField, concatField).where(existsCondition(classifiersColumnName, data)).and(field(persistable
                                .getIdColumnNameWithTableName()).in(ids)).returning(field(persistable
                                        .getIdColumnNameWithTableName()), field(String.format(QUOTED_STRING, persistable
                                                .getClassifiersColumnName()), JSONB.class));

                final Result<Record> newResults = update.fetch();
                final Map<String, OperationResult> newOpResults = createOperationResults(persistable, newResults);
                allResults.putAll(newOpResults);
            }
        });

        return new ArrayList<>(allResults.values());
    }

    private Condition existsCondition(final String classifiersColumnName, final String data) {
        return exists(select(field("1")).from(table("jsonb_array_elements_text(" + classifiersColumnName + ")").as(
                "element")).where(field("element").eq(data)));
    }

    /**
     * Executes the operation for the given consumer data for specific entity type.
     *
     * @param consumerData
     *     which holds the information for classifiers/decorators, entity ids and relationship ids
     * @param entityType
     *     entity type
     */
    public List<OperationResult> delete(final ConsumerData<List<String>> consumerData, EntityType entityType) {
        PersistableIdMap persistableIdMap = PersistableIdMap.builder().persistableWithIds(Map.of(entityType, consumerData
                .entityIds())).build();

        final List<OperationResult> results = new ArrayList<>();
        writeDataDslContext.transaction((Configuration config) -> results.addAll(performOperation(persistableIdMap,
                consumerData.data(), config.dsl())));
        return results;
    }

    /**
     * Executes the operation for the given consumer data for specific relation type.
     *
     * @param consumerData
     *     which holds the information for classifiers/decorators, entity ids and relationship ids
     * @param relationType
     *     relation type
     */
    public List<OperationResult> delete(final ConsumerData<List<String>> consumerData, RelationType relationType) {
        PersistableIdMap persistableIdMap = PersistableIdMap.builder().persistableWithIds(Map.of(relationType, consumerData
                .relationshipIds())).build();

        final List<OperationResult> results = new ArrayList<>();
        writeDataDslContext.transaction((Configuration config) -> results.addAll(performOperation(persistableIdMap,
                consumerData.data(), config.dsl())));
        return results;
    }
}