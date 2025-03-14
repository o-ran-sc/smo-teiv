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
import static org.oran.smo.teiv.utils.TeivConstants.CLASSIFIERS;
import static org.oran.smo.teiv.utils.TeivConstants.DECORATORS;
import static org.oran.smo.teiv.utils.TeivConstants.MODULE_REFERENCE;
import static org.oran.smo.teiv.utils.TeivConstants.TEIV_CONSUMER_DATA;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.SelectConditionStep;
import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.schema.YangDataTypes;
import org.oran.smo.teiv.utils.TeivConstants;
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
                classifierName).from(String.format(TEIV_CONSUMER_DATA, CLASSIFIERS)).join(String.format(TEIV_CONSUMER_DATA,
                        MODULE_REFERENCE)).on(field("\"moduleReferenceName\"").eq(field(String.format(TEIV_CONSUMER_DATA,
                                MODULE_REFERENCE) + ".name"))).where(field("status").like(TeivConstants.IN_USAGE)));
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
                decoratorName, dataType).from(String.format(TEIV_CONSUMER_DATA, DECORATORS)).join(String.format(
                        TEIV_CONSUMER_DATA, MODULE_REFERENCE)).on(field("\"moduleReferenceName\"").eq(field(String.format(
                                TEIV_CONSUMER_DATA, MODULE_REFERENCE) + ".name"))).where(field("status").like(
                                        TeivConstants.IN_USAGE)));
        Map<String, YangDataTypes> result = new HashMap<>();
        for (Record record : availableDecorators) {
            result.put((String) record.get("decoratorName"), YangDataTypes.fromYangDataType("" + record.get("dataType")));
        }
        return result;
    }

    protected <T> T runMethodSafe(Supplier<T> supp) {
        try {
            return supp.get();
        } catch (TeivException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Sql exception during query execution", ex);
            throw TeivException.serverSQLException();
        }
    }

}
