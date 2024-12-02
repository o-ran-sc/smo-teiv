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

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.UpdateResultStep;
import org.oran.smo.teiv.exposure.consumerdata.model.PersistableIdMap;
import org.oran.smo.teiv.service.models.OperationResult;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;
import static org.oran.smo.teiv.utils.TiesConstants.QUOTED_STRING;

@Component
@Profile("exposure")
public class MergeClassifiersOperation extends ClassifiersOperation {

    public MergeClassifiersOperation(DSLContext readDataDslContext, DSLContext writeDataDslContext) {
        super(readDataDslContext, writeDataDslContext);
    }

    @Override
    protected List<OperationResult> performOperation(final PersistableIdMap map, final List<String> consumerData,
            final DSLContext writeDataDslContext) {

        // Newer operations on the same entity/relationship makes old OperationResults obsolete,
        // since we only care about the end result for given entity/relationship.
        // We use a map with entity/relship ID as key,
        // so we can automatically discard intermediary results.
        final Map<String, OperationResult> allResults = new HashMap<>();

        map.persistableWithIds().forEach((persistable, ids) -> {

            for (String data : consumerData) {
                final String classifiersColumnName = String.format(QUOTED_STRING, persistable.getClassifiersColumnName());
                final String concat = classifiersColumnName + " || '" + String.format(QUOTED_STRING, data) + "'";

                final Field<JSONB> targetField = field(classifiersColumnName, JSONB.class);
                final Field<JSONB> concatField = field(concat, JSONB.class);

                final UpdateResultStep<Record> update = writeDataDslContext.update(table(persistable.getTableName())).set(
                        targetField, concatField).where(notExistsCondition(classifiersColumnName, data)).and(field(
                                persistable.getIdColumnNameWithTableName()).in(ids)).returning(field(persistable
                                        .getIdColumnNameWithTableName()), field(String.format(QUOTED_STRING, persistable
                                                .getClassifiersColumnName()), JSONB.class));

                final Result<Record> newResults = update.fetch();
                final Map<String, OperationResult> newOpResults = createOperationResults(persistable, newResults);
                allResults.putAll(newOpResults);
            }
        });

        // Simplifying return type, as we need it to be a List anyway.
        return new ArrayList<>(allResults.values());
    }

    private Condition notExistsCondition(final String classifiersColumnName, final String data) {
        return notExists(select(field("1")).from(table("jsonb_array_elements_text(" + field(classifiersColumnName) + ")")
                .as("element")).where(field("element").eq(val(data))));
    }
}
