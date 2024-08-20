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
package org.oran.smo.teiv.exposure.consumerdata;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.oran.smo.teiv.utils.TiesConstants.CLASSIFIERS;
import static org.oran.smo.teiv.utils.TiesConstants.DECORATORS;
import static org.oran.smo.teiv.utils.TiesConstants.MODULE_REFERENCE;
import static org.oran.smo.teiv.utils.TiesConstants.MODULE_REFERENCE_NAME;
import static org.oran.smo.teiv.utils.TiesConstants.NAME;
import static org.oran.smo.teiv.utils.TiesConstants.QUOTED_STRING;
import static org.oran.smo.teiv.utils.TiesConstants.SEMICOLON_SEPARATION;
import static org.oran.smo.teiv.utils.TiesConstants.TIES_CONSUMER_DATA;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.InsertValuesStepN;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.SelectConditionStep;
import org.oran.smo.teiv.exception.TiesException;
import org.oran.smo.teiv.schema.YangDataTypes;
import org.oran.smo.teiv.utils.TiesConstants;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerDataRepositoryImpl implements ConsumerDataRepository {
    private final DSLContext readDataDslContext;
    private final DSLContext writeDataDslContext;

    @Override
    public Set<String> loadClassifiers() {
        final Field<Object> classifierName = field("classifiers.name").as("classifierName");
        SelectConditionStep<Record1<Object>> availableClassifiers = runMethodSafe(() -> readDataDslContext.select(
                classifierName).from(String.format(TIES_CONSUMER_DATA, CLASSIFIERS)).join(String.format(TIES_CONSUMER_DATA,
                        MODULE_REFERENCE)).on(field("\"moduleReferenceName\"").eq(field(String.format(TIES_CONSUMER_DATA,
                                MODULE_REFERENCE) + ".name"))).where(field("status").like(TiesConstants.IN_USAGE)));
        Set<String> result = new HashSet<>();
        for (Record record : availableClassifiers) {
            result.add((String) record.get("classifierName"));
        }
        return result;
    }

    @Override
    public Map<String, YangDataTypes> loadDecorators() {
        final Field<Object> decoratorName = field("decorators.name").as("decoratorName");
        final Field<Object> dataType = field("decorators.\"dataType\"").as("dataType");
        SelectConditionStep<Record2<Object, Object>> availableDecorators = runMethodSafe(() -> readDataDslContext.select(
                decoratorName, dataType).from(String.format(TIES_CONSUMER_DATA, DECORATORS)).join(String.format(
                        TIES_CONSUMER_DATA, MODULE_REFERENCE)).on(field("\"moduleReferenceName\"").eq(field(String.format(
                                TIES_CONSUMER_DATA, MODULE_REFERENCE) + ".name"))).where(field("status").like(
                                        TiesConstants.IN_USAGE)));
        Map<String, YangDataTypes> result = new HashMap<>();
        for (Record record : availableDecorators) {
            result.put((String) record.get("decoratorName"), YangDataTypes.fromYangDataType("" + record.get("dataType")));
        }
        return result;
    }

    @Override
    public void storeClassifiers(final List<String> elements, final String moduleName) {
        insertValues(CLASSIFIERS, List.of(NAME, String.format(QUOTED_STRING, MODULE_REFERENCE_NAME)), executable -> {
            for (String element : elements) {
                executable = executable.values(String.format(SEMICOLON_SEPARATION, moduleName, element), moduleName);
            }
        });
    }

    @Override
    public void storeDecorators(final Map<String, String> elements, final String moduleName) {
        insertValues(DECORATORS, List.of(NAME, String.format(QUOTED_STRING, "dataType"), String.format(QUOTED_STRING,
                MODULE_REFERENCE_NAME)), executable -> {
                    for (Map.Entry<String, String> element : elements.entrySet()) {
                        executable = executable.values(String.format(SEMICOLON_SEPARATION, moduleName, element.getKey()),
                                element.getValue(), moduleName);
                    }
                });
    }

    private void insertValues(final String tableName, final List<String> columns,
            final Consumer<InsertValuesStepN<?>> consumer) {
        InsertValuesStepN<?> executable = writeDataDslContext.insertInto(table(String.format(TIES_CONSUMER_DATA,
                tableName))).columns(columns.stream().map(column -> field(column, String.class)).toList());

        consumer.accept(executable);

        runMethodSafe(executable::execute);
    }

    protected <T> T runMethodSafe(Supplier<T> supp) {
        try {
            return supp.get();
        } catch (TiesException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Sql exception during query execution", ex);
            throw TiesException.serverSQLException();
        }
    }

}
