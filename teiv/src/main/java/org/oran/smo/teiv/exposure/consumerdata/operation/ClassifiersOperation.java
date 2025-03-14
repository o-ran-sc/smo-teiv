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

import org.jooq.DSLContext;

import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.jooq.Result;
import org.jooq.Record;
import org.jooq.JSONB;
import org.oran.smo.teiv.schema.Persistable;
import org.oran.smo.teiv.service.models.OperationResult;
import org.oran.smo.teiv.utils.JooqTypeConverter;

import static org.oran.smo.teiv.utils.TeivConstants.QUOTED_STRING;

@Slf4j
public abstract class ClassifiersOperation extends ConsumerDataOperation<List<String>> {

    protected ClassifiersOperation(DSLContext readDataDslContext, DSLContext writeDataDslContext) {
        super(readDataDslContext, writeDataDslContext);
    }

    protected Map<String, OperationResult> createOperationResults(final Persistable persistable,
            final Result<Record> results) {
        final Map<String, OperationResult> opResults = new HashMap<>();
        for (Record record : results) {
            String id = (String) record.get(persistable.getIdColumnNameWithTableName());
            List<String> classifierListAfterUpdate = JooqTypeConverter.jsonbToList(record.get(String.format(QUOTED_STRING,
                    persistable.getClassifiersColumnName()), JSONB.class));
            OperationResult operationResult = OperationResult.builder().id(id).type(persistable.getName()).category(
                    persistable.getCategory()).classifiers(classifierListAfterUpdate).isUpdatedInDb(true).build();

            opResults.put(id, operationResult);
        }
        return opResults;
    }
}
