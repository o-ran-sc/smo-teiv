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
package org.oran.smo.teiv.exposure.spi.impl;

import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.name;
import static org.oran.smo.teiv.utils.TeivConstants.ID_COLUMN_NAME;
import static org.oran.smo.teiv.utils.TeivConstants.QUOTED_STRING;
import static org.oran.smo.teiv.utils.TeivConstants.TEIV_CONSUMER_DATA;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.SelectOrderByStep;
import org.jooq.impl.DSL;
import org.oran.smo.teiv.schema.Persistable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.exposure.spi.DataRepository;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.FilterCriteria;
import org.oran.smo.teiv.exposure.teivpath.refiner.BasePathRefinement;
import org.oran.smo.teiv.exposure.teivpath.refiner.PathToJooqRefinement;
import org.oran.smo.teiv.schema.EntityType;
import org.oran.smo.teiv.schema.RelationType;
import org.oran.smo.teiv.utils.query.exception.TeivPathException;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataRepositoryImpl implements DataRepository {
    private final BasePathRefinement basePathRefinement;

    private final DSLContext readDataDslContext;

    @Value("${consumer-data.batch-size:100}")
    private int batchSize = 100;

    @Override
    public Result<Record> getEntityById(final EntityType entityType, final String id) {
        return runMethodSafe(() -> readDataDslContext.select(entityType.getAllFieldsWithId()).from(entityType
                .getTableName()).where(field(ID_COLUMN_NAME).eq(id)).fetch());
    }

    @Override
    public Result<Record> getRelationshipById(final String id, final RelationType relationType) {
        return runMethodSafe(() -> readDataDslContext.select(relationType.getAllFieldsWithId()).from(table(relationType
                .getTableName())).where(field(String.format(QUOTED_STRING, relationType.getIdColumnName())).eq(id))
                .fetch());
    }

    @Override
    public Result<Record> getTopologyByFilterCriteria(final FilterCriteria filterCriteria, final int limit,
            final int offset) {
        basePathRefinement.refine(filterCriteria);
        SelectOrderByStep<Record> query = PathToJooqRefinement.toJooq(filterCriteria, offset, limit);
        return runMethodSafe(() -> readDataDslContext.fetch(query));
    }

    @Override
    public Set<String> getClassifiersForSchema(final String schemaName) {
        SelectConditionStep<Record> availableClassifiers = runMethodSafe(() -> readDataDslContext.select().from(String
                .format(TEIV_CONSUMER_DATA, "classifiers")).where(field("\"moduleReferenceName\"").eq(schemaName)));
        Set<String> result = new HashSet<>();
        for (Record record : availableClassifiers) {
            result.add((String) record.get("name"));
        }
        return result;
    }

    @Override
    public Set<String> getDecoratorsForSchema(final String schemaName) {
        SelectConditionStep<Record> availableDecorators = runMethodSafe(() -> readDataDslContext.select().from(String
                .format(TEIV_CONSUMER_DATA, "decorators")).where(field("\"moduleReferenceName\"").like(schemaName)));
        Set<String> result = new HashSet<>();
        for (Record record : availableDecorators) {
            result.add((String) record.get("name"));
        }
        return result;
    }

    @Override
    public List<String> getRelationshipIdsForDecoratorDeletion(final RelationType relationType,
            final Set<String> decorators) {
        Condition condition = falseCondition();
        for (String decorator : decorators) {
            condition = condition.or(DSL.condition("{0}->>{1} IS NOT NULL", field(String.format(QUOTED_STRING, relationType
                    .getDecoratorsColumnName()), JSONB.class), val(decorator)));
        }

        final Condition finalCondition = condition;
        return runMethodSafe(() -> readDataDslContext.select(field(relationType.getTableName() + "." + String.format(
                QUOTED_STRING, relationType.getIdColumnName()))).from(table(relationType.getTableName())).where(field(
                        relationType.getTableName() + "." + String.format(QUOTED_STRING, relationType.getIdColumnName()))
                                .isNotNull().and(finalCondition)).orderBy(field(String.format(QUOTED_STRING, "id"))).limit(
                                        batchSize).fetchInto(String.class));
    }

    @Override
    public List<String> getRelationshipIdsForClassifierDeletion(final RelationType relationType,
            final Set<String> classifiers) {
        Condition condition = falseCondition();
        for (String classifier : classifiers) {
            condition = condition.or(field(String.format(QUOTED_STRING, relationType.getClassifiersColumnName()),
                    JSONB.class).contains(JSONB.valueOf("[\"" + classifier + "\"]")));
        }

        final Condition finalCondition = condition;
        return runMethodSafe(() -> readDataDslContext.select(field(relationType.getTableName() + "." + String.format(
                QUOTED_STRING, relationType.getIdColumnName()))).from(table(relationType.getTableName())).where(field(
                        relationType.getTableName() + "." + String.format(QUOTED_STRING, relationType.getIdColumnName()))
                                .isNotNull().and(finalCondition)).orderBy(field(String.format(QUOTED_STRING, "id"))).limit(
                                        batchSize).fetchInto(String.class));
    }

    @Override
    public List<String> getEntityIdsForDecoratorDeletion(final EntityType entityType, final Set<String> decorators) {
        Condition condition = falseCondition();
        for (String decorator : decorators) {
            condition = condition.or(DSL.condition("{0}->>{1} IS NOT NULL", field(String.format(QUOTED_STRING, entityType
                    .getDecoratorsColumnName()), JSONB.class), val(decorator)));
        }

        final Condition finalCondition = condition;
        return runMethodSafe(() -> readDataDslContext.select(field(entityType.getIdColumnName())).from(table(entityType
                .getTableName())).where(finalCondition).orderBy(field(String.format(QUOTED_STRING, "id"))).limit(batchSize)
                .fetchInto(String.class));
    }

    @Override
    public List<String> getEntityIdsForClassifierDeletion(final EntityType entityType, final Set<String> classifiers) {
        Condition condition = falseCondition();
        for (String classifier : classifiers) {
            condition = condition.or(field(String.format(QUOTED_STRING, entityType.getClassifiersColumnName()), JSONB.class)
                    .contains(JSONB.valueOf("[\"" + classifier + "\"]")));
        }

        final Condition finalCondition = condition;
        return runMethodSafe(() -> readDataDslContext.select(field(entityType.getIdColumnName())).from(table(entityType
                .getTableName())).where(finalCondition).orderBy(field(String.format(QUOTED_STRING, "id"))).limit(batchSize)
                .fetchInto(String.class));
    }

    @Override
    public boolean isTopologyExist(final Persistable type, final String id) {
        return runMethodSafe(() -> readDataDslContext.fetchExists(readDataDslContext.selectFrom(table(type.getTableName()))
                .where(field(name(type.getIdColumnName())).eq(id))));
    }

    protected <T> T runMethodSafe(Supplier<T> supp) {
        try {
            return supp.get();
        } catch (TeivException ex) {
            throw ex;
        } catch (TeivPathException ex) {
            log.error("Exception during query construction", ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Sql exception during query execution", ex);
            throw TeivException.serverSQLException();
        }
    }

}
