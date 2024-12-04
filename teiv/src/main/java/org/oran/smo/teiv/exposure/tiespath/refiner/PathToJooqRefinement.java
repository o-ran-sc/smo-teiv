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
package org.oran.smo.teiv.exposure.tiespath.refiner;

import org.apache.commons.lang3.tuple.Pair;
import org.jooq.CommonTableExpression;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.OrderField;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.jooq.SelectField;
import org.jooq.SelectOrderByStep;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.FilterCriteria;
import lombok.experimental.UtilityClass;
import org.jooq.SelectJoinStep;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.InnerFilterCriteria;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.TargetObject;
import org.oran.smo.teiv.schema.DataType;
import org.oran.smo.teiv.schema.RelationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectCount;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.with;
import static org.oran.smo.teiv.utils.TiesConstants.ID_COLUMN_NAME;
import static org.oran.smo.teiv.utils.TiesConstants.QUOTED_STRING;

@UtilityClass
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PathToJooqRefinement {

    /**
     * Converts LogicalBlocks of InnerLanguageDTO to SelectJoinStep
     *
     * @param filterCriteria
     *     the InnerLanguageDTO
     * @return the query
     */
    public static SelectOrderByStep<Record> toJooq(FilterCriteria filterCriteria, int offset, int limit) {
        SelectOrderByStep<Record> basicQuery = createBasicQuery(filterCriteria, false);
        SelectOrderByStep<Record> countQuery = createBasicQuery(filterCriteria, true);
        List<Entry<SelectField, Map<SelectField, DataType>>> selectList = new ArrayList<>(filterCriteria.getSelects()
                .entrySet());
        return createQueryWithCount(selectList, basicQuery, countQuery, offset, limit);
    }

    private static SelectOrderByStep<Record> createQueryWithCount(
            List<Entry<SelectField, Map<SelectField, DataType>>> selectList, SelectOrderByStep<Record> basicQuery,
            SelectOrderByStep<Record> countQuery, int offset, int limit) {
        List<SelectField> fields = new ArrayList<>();
        for (Entry<SelectField, Map<SelectField, DataType>> fieldSet : selectList) {
            for (Entry<SelectField, DataType> field : fieldSet.getValue().entrySet()) {
                if (field.getValue() == DataType.CONTAINER) {
                    fields.add(field(String.format(QUOTED_STRING, field.getKey().getName()), JSONB.class));
                } else {
                    fields.add(field(String.format(QUOTED_STRING, field.getKey().getName())));
                }
            }
        }

        List<SelectField> fieldsWithNullCount = new ArrayList<>(fields);
        fieldsWithNullCount.add(field("null").as("count"));

        List<SelectField> nullFieldsWithCount = getNulledFields(selectList);
        nullFieldsWithCount.add(field(selectCount().from(countQuery)).as("count"));

        return select(nullFieldsWithCount).unionAll(select(fieldsWithNullCount).from(basicQuery).limit(offset, limit));
    }

    private static SelectOrderByStep<Record> createBasicQuery(FilterCriteria filterCriteria, boolean countMode) {
        Map<SelectField, DataType> fields = getFields(filterCriteria.getSelects(), countMode);
        SelectOrderByStep<Record> query = createBasicOrFirstHopInnerQuery(filterCriteria.getFilterCriteriaList().get(0),
                fields, countMode);

        for (int i = 1; i < filterCriteria.getFilterCriteriaList().size(); i++) {
            query = query.unionAll(createBasicOrFirstHopInnerQuery(filterCriteria.getFilterCriteriaList().get(i), fields,
                    countMode));
        }

        return query;
    }

    private static SelectOrderByStep<Record> createBasicOrFirstHopInnerQuery(InnerFilterCriteria filterCriteria,
            Map<SelectField, DataType> fields, boolean countMode) {
        if (filterCriteria.getScope().isFirstHop()) {
            return createFirstHopInnerQuery(filterCriteria, fields, countMode);
        }
        return createInnerQuery(filterCriteria, fields, countMode);
    }

    private static SelectOrderByStep<Record> createFirstHopInnerQuery(InnerFilterCriteria filterCriteria,
            Map<SelectField, DataType> otherFields, boolean countMode) {
        List<SelectField> resolvedFields = getResolvedFields(filterCriteria.getSelects(), otherFields.entrySet());
        SelectOrderByStep<Record> query = buildSingleFirstHopQuery(filterCriteria, resolvedFields);

        if (countMode) {
            return query;
        } else {
            return (SelectOrderByStep) query.orderBy(filterCriteria.getSelects().keySet().stream().map(i -> field(i).asc())
                    .toList().toArray(new OrderField[0]));
        }
    }

    private static SelectOrderByStep<Record> createInnerQuery(InnerFilterCriteria filterCriteria,
            Map<SelectField, DataType> otherFields, boolean countMode) {
        List<SelectField> resolvedFields = getResolvedFields(filterCriteria.getSelects(), otherFields.entrySet());
        SelectJoinStep<Record> query = select(resolvedFields).from(filterCriteria.getTableFromTarget(filterCriteria
                .getTargets().get(0)));

        List<Pair<String, Field>> joinConditions = new ArrayList<>(filterCriteria.getJoinCondition());

        for (int i = 0; i < joinConditions.size(); i++) {
            query = query.join(joinConditions.get(i).getLeft()).on(joinConditions.get(i).getRight());
        }

        SelectConditionStep<Record> conditionStep = query.where(filterCriteria.getCondition());
        if (countMode) {
            return conditionStep;
        } else {
            return (SelectOrderByStep) conditionStep.orderBy(filterCriteria.getSelects().keySet().stream().map(i -> field(i)
                    .asc()).toList().toArray(new OrderField[0]));
        }
    }

    private SelectConditionStep<Record> buildSingleFirstHopQuery(InnerFilterCriteria innerFilterCriteria,
            List<SelectField> resolvedFields) {
        TargetObject targetObject = innerFilterCriteria.getTargets().get(0);
        return switch (innerFilterCriteria.getRelationTypeFromTarget(targetObject).getRelationCardinality(targetObject
                .getTopologyObject())) {
            case ONE_TO_MANY -> buildOneToManyQuery(innerFilterCriteria, resolvedFields);
            case MANY_TO_ONE -> buildManyToOneQuery(innerFilterCriteria, resolvedFields);
            case MANY_TO_MANY, ONE_TO_ONE -> buildManyToManyQuery(innerFilterCriteria, resolvedFields);
        };
    }

    private static SelectConditionStep<Record> buildManyToManyQuery(InnerFilterCriteria innerFilterCriteria,
            List<SelectField> resolvedFields) {
        TargetObject targetObject = innerFilterCriteria.getTargets().get(0);
        RelationType relation = innerFilterCriteria.getRelationTypeFromTarget(targetObject);

        String associtionTableName = relation.getOtherSideTableName(targetObject.getTopologyObject());
        boolean isAssociationASide = associtionTableName.equals(relation.getASide().getTableName());
        String entityTableName = isAssociationASide ?
                relation.getBSide().getTableName() :
                relation.getASide().getTableName();
        String outerSelectField = isAssociationASide ? relation.bSideColumnName() : relation.aSideColumnName();
        String outerWhereField = isAssociationASide ? relation.aSideColumnName() : relation.bSideColumnName();
        String relationTable = relation.getTableName();

        Field idField = field("id");
        SelectConditionStep<Record> innerQuery = select(idField).from(associtionTableName).where(innerFilterCriteria
                .getCondition());

        Field selectField = field(DSL.quotedName(outerSelectField));
        Field whereField = field(DSL.quotedName(outerWhereField));

        CommonTableExpression<Record1<String>> cell = name(targetObject.getTopologyObject()).fields("id").as(select(
                selectField).from(relationTable).where(whereField.in(innerQuery)));

        return with(cell).select(resolvedFields).from(table(entityTableName)).where(field("id").in(select(field("id")).from(
                cell)));
    }

    private static SelectConditionStep<Record> buildOneToManyQuery(InnerFilterCriteria innerFilterCriteria,
            List<SelectField> resolvedFields) {
        TargetObject targetObject = innerFilterCriteria.getTargets().get(0);
        RelationType relation = innerFilterCriteria.getRelationTypeFromTarget(targetObject);
        String filterTable = relation.getOtherSideTableName(targetObject.getTopologyObject());
        Table targetTable = innerFilterCriteria.getTableFromTarget(targetObject);
        Field targetIdField = field(ID_COLUMN_NAME);

        Field filterForeignKeyField = field(DSL.quotedName(relation
                .getNotStoringSideEntityIdColumnNameInStoringSideTable()));

        SelectConditionStep<Record> subQuery = select(filterForeignKeyField).from(filterTable).where(innerFilterCriteria
                .getCondition());
        return select(resolvedFields).from(targetTable).where(targetIdField.in(subQuery));
    }

    public static SelectConditionStep<Record> buildManyToOneQuery(InnerFilterCriteria innerFilterCriteria,
            List<SelectField> resolvedFields) {
        TargetObject targetObject = innerFilterCriteria.getTargets().get(0);
        RelationType relation = innerFilterCriteria.getRelationTypeFromTarget(targetObject);
        String filterTable = relation.getOtherSideTableName(targetObject.getTopologyObject());
        Field targetForeignKeyField = field(DSL.quotedName(relation
                .getNotStoringSideEntityIdColumnNameInStoringSideTable()));

        Field filterIdField = field(ID_COLUMN_NAME);

        SelectConditionStep<Record> subQuery = select(filterIdField).from(filterTable).where(innerFilterCriteria
                .getCondition());
        return select(resolvedFields).from(innerFilterCriteria.getTableFromTarget(targetObject)).where(targetForeignKeyField
                .in(subQuery));
    }

    private static List<SelectField> getNulledFields(List<Entry<SelectField, Map<SelectField, DataType>>> selectList) {
        List<SelectField> nulledFields = new ArrayList<>();
        for (int i = 0; i < selectList.size(); i++) {
            addNullFields(selectList, nulledFields, i);
        }
        return nulledFields;
    }

    private static Map<SelectField, DataType> getFields(Map<SelectField, Map<SelectField, DataType>> getSelects,
            boolean countMode) {
        Map<SelectField, DataType> fields = new HashMap<>();
        if (countMode) {
            for (SelectField id : getSelects.keySet()) {
                fields.put(id, DataType.PRIMITIVE);
            }
        } else {
            for (Map<SelectField, DataType> fieldSet : getSelects.values()) {
                fields.putAll(fieldSet);
            }
        }
        return fields;
    }

    private static List<SelectField> getResolvedFields(Map<SelectField, Map<SelectField, DataType>> getSelects,
            Set<Entry<SelectField, DataType>> otherFields) {
        Set<SelectField> selfFields = new HashSet<>();
        for (Map<SelectField, DataType> fieldSet : getSelects.values()) {
            selfFields.addAll(fieldSet.keySet());
        }
        List<SelectField> resolvedFields = new ArrayList<>();
        for (Entry<SelectField, DataType> field : otherFields) {
            if (selfFields.contains(field.getKey())) {
                resolvedFields.add(field.getKey());
            } else if (field.getValue() == DataType.CONTAINER) {
                resolvedFields.add(field("null::jsonb", JSONB.class).as(field.getKey().getName()));
            } else if (field.getValue() == DataType.INTEGER) {
                resolvedFields.add(field("null::integer").as(field.getKey().getName()));
            } else if (field.getValue() == DataType.BIGINT) {
                resolvedFields.add(field("null::bigint").as(field.getKey().getName()));
            } else if (field.getValue() == DataType.DECIMAL) {
                resolvedFields.add(field("null::numeric").as(field.getKey().getName()));
            } else if (field.getValue() == DataType.TIMESTAMPTZ) {
                resolvedFields.add(field("null::timestamptz").as(field.getKey().getName()));
            } else if (field.getValue() == DataType.BYTEA) {
                resolvedFields.add(field("null::bytea").as(field.getKey().getName()));
            } else {
                resolvedFields.add(field("null").as(field.getKey().getName()));
            }
        }
        return resolvedFields;
    }

    private static void addNullFields(List<Entry<SelectField, Map<SelectField, DataType>>> selectList,
            List<SelectField> nulledFields, int i) {
        for (SelectField field : selectList.get(i).getValue().keySet()) {
            if (selectList.get(i).getValue().get(field) == DataType.CONTAINER) {
                nulledFields.add(field("null::jsonb", JSONB.class).as(field.getName()));
            } else if (selectList.get(i).getValue().get(field) == DataType.INTEGER) {
                nulledFields.add(field("null::integer").as(field.getName()));
            } else if (selectList.get(i).getValue().get(field) == DataType.BIGINT) {
                nulledFields.add(field("null::bigint").as(field.getName()));
            } else if (selectList.get(i).getValue().get(field) == DataType.DECIMAL) {
                nulledFields.add(field("null::numeric").as(field.getName()));
            } else if (selectList.get(i).getValue().get(field) == DataType.TIMESTAMPTZ) {
                nulledFields.add(field("null::timestamptz").as(field.getName()));
            } else if (selectList.get(i).getValue().get(field) == DataType.BYTEA) {
                nulledFields.add(field("null::bytea").as(field.getName()));
            } else {
                nulledFields.add(field("null").as(field.getName()));
            }
        }

    }
}
