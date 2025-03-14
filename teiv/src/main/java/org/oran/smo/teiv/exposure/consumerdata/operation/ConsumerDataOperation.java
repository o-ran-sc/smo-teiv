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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SelectConditionStep;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

import org.jooq.exception.DataAccessException;
import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.exposure.consumerdata.model.ConsumerData;
import org.oran.smo.teiv.exposure.consumerdata.model.PersistableIdMap;
import org.oran.smo.teiv.schema.Persistable;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.service.models.OperationResult;

@Slf4j
@RequiredArgsConstructor
public abstract class ConsumerDataOperation<C> {

    private final DSLContext readDataDslContext;
    protected final DSLContext writeDataDslContext;

    protected abstract List<OperationResult> performOperation(final PersistableIdMap map, final C consumerData,
            final DSLContext writeDataDslContext);

    /**
     * Executes the operation for the given consumer data.
     *
     * @param consumerData
     *     which holds the information for classifiers/decorators, entity ids and relationship ids
     * @return Map of Operation Results. Key is the ID of the Entity/Relationship.
     */
    public List<OperationResult> execute(final ConsumerData<C> consumerData) {
        return runMethodSafe(() -> {
            final Pair<PersistableIdMap, PersistableIdMap> maps = checkIfIdsExist(consumerData);
            final PersistableIdMap entityIdMap = maps.getKey();
            final PersistableIdMap relationshipIdMap = maps.getValue();

            final List<OperationResult> results = new ArrayList<>();

            writeDataDslContext.transaction((Configuration config) -> {
                if (!consumerData.entityIds().isEmpty()) {
                    final List<OperationResult> entityResults = performOperation(entityIdMap, consumerData.data(), config
                            .dsl());
                    results.addAll(entityResults);
                }

                if (!consumerData.relationshipIds().isEmpty()) {
                    final List<OperationResult> relshipResults = performOperation(relationshipIdMap, consumerData.data(),
                            config.dsl());
                    results.addAll(relshipResults);
                }
            });

            return results;
        });
    }

    protected Pair<PersistableIdMap, PersistableIdMap> checkIfIdsExist(final ConsumerData<C> consumerData) {
        log.debug("Checking entity and relationship ids");

        final List<String> entityNames = SchemaRegistry.getEntityNames();
        final List<String> relationNames = SchemaRegistry.getRelationNames();

        final PersistableIdMap entityCheckResult = performCheck(consumerData.entityIds(), entityNames,
                SchemaRegistry::getEntityTypeByName);
        final PersistableIdMap relationshipCheckResult = performCheck(consumerData.relationshipIds(), relationNames,
                SchemaRegistry::getRelationTypeByName);

        if (!entityCheckResult.idsNotFound().isEmpty() || !relationshipCheckResult.idsNotFound().isEmpty()) {

            throw TeivException.resourceNotFoundException(entityCheckResult.idsNotFound(), relationshipCheckResult
                    .idsNotFound());
        }

        return new ImmutablePair<>(entityCheckResult, relationshipCheckResult);
    }

    private PersistableIdMap performCheck(final List<String> ids, final List<String> names,
            final Function<String, Persistable> supp) {
        final Map<Persistable, List<String>> persistableWithIds = new HashMap<>();
        final Set<String> idsNotFound = new HashSet<>(ids);

        for (String name : names) {
            if (!idsNotFound.isEmpty()) {

                final Persistable persistable = supp.apply(name);
                final SelectConditionStep<Record1<Object>> select = readDataDslContext.select(field(persistable
                        .getIdColumnNameWithTableName())).from(table(persistable.getTableName())).where(field(persistable
                                .getIdColumnNameWithTableName()).in(idsNotFound));
                final Result<Record1<Object>> result = select.fetch();

                for (Record1<Object> record : result) {

                    if (!persistableWithIds.containsKey(persistable)) {

                        List<String> list = new ArrayList<>();
                        list.add(record.get(0, String.class));

                        persistableWithIds.put(persistable, list);
                    } else {
                        persistableWithIds.get(persistable).add(record.get(0, String.class));
                    }

                    idsNotFound.remove(record.get(0, String.class));
                }
            } else {
                break;
            }
        }
        return PersistableIdMap.builder().persistableWithIds(persistableWithIds).idsNotFound(idsNotFound).build();
    }

    protected <T> T runMethodSafe(final Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (DataAccessException ex) {
            log.error("Sql exception during query execution", ex);
            throw TeivException.serverSQLException();
        }
    }
}
