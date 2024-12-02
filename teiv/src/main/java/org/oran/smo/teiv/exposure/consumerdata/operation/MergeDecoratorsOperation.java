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

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.UpdateResultStep;
import org.oran.smo.teiv.exposure.consumerdata.model.PersistableIdMap;
import org.oran.smo.teiv.service.models.OperationResult;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.not;
import static org.jooq.impl.DSL.table;
import static org.oran.smo.teiv.utils.TiesConstants.QUOTED_STRING;

@Component
@Profile("exposure")
public class MergeDecoratorsOperation extends DecoratorsOperation {

    public MergeDecoratorsOperation(DSLContext readDataDslContext, DSLContext writeDataDslContext) {
        super(readDataDslContext, writeDataDslContext);
    }

    @Override
    protected List<OperationResult> performOperation(final PersistableIdMap map, final Map<String, Object> consumerData,
            final DSLContext writeDataDslContext) {

        final Map<String, OperationResult> allResults = new HashMap<>();

        map.persistableWithIds().forEach((persistable, ids) -> consumerData.forEach((key, value) -> {

            final String decoratorsColumnName = String.format(QUOTED_STRING, persistable.getDecoratorsColumnName());
            final String concat = String.format("%s || '{%s: %s}'", decoratorsColumnName, transformKey(key), transformValue(
                    value));

            final Field<JSONB> targetField = field(decoratorsColumnName, JSONB.class);
            final Field<JSONB> concatField = field(concat, JSONB.class);

            final UpdateResultStep<Record> update = writeDataDslContext.update(table(persistable.getTableName())).set(
                    targetField, concatField).where(not(keyValuePairExistsCondition(decoratorsColumnName, key, value)).or(
                            not(keyExistsCondition(decoratorsColumnName, key))).and(field(persistable
                                    .getIdColumnNameWithTableName()).in(ids))).returning(field(persistable
                                            .getIdColumnNameWithTableName()), field(String.format(QUOTED_STRING, persistable
                                                    .getDecoratorsColumnName()), JSONB.class));

            final Result<Record> newResults = update.fetch();
            saveResults(persistable, newResults, allResults);
        }));
        return new ArrayList<>(allResults.values());
    }

    private String transformKey(final String key) {
        return String.format("\"%s\"", key);
    }

    private String transformValue(final Object value) {
        return value instanceof String ? String.format("\"%s\"", value) : value.toString();
    }

    private Condition keyValuePairExistsCondition(final String decoratorsColumnName, final String key, final Object value) {
        return condition("? -> ? = ?", field(decoratorsColumnName), inline(key), inline(transformValue(value)));
    }

    private Condition keyExistsCondition(final String decoratorsColumnName, final String key) {
        return condition("jsonb_exists(?, ?)", field(decoratorsColumnName), inline(key));
    }
}
