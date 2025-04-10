/*
 *  ============LICENSE_START=======================================================
 *  Copyright (C) 2024 Ericsson
 *  Modifications Copyright (C) 2024-2025 OpenInfra Foundation Europe
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
package org.oran.smo.teiv.exposure.teivpath.innerlanguage;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.oran.smo.teiv.exposure.teivpath.refiner.AliasMapper.hashAlias;
import static org.oran.smo.teiv.utils.PersistableUtil.getFullyQualifiedNameWithColumnName;
import static org.oran.smo.teiv.utils.PersistableUtil.getTableNameWithColumnName;
import static org.oran.smo.teiv.utils.TeivConstants.ID_COLUMN_NAME;
import static org.oran.smo.teiv.utils.TeivConstants.METADATA;
import static org.oran.smo.teiv.utils.TeivConstants.PROPERTY_A_SIDE;
import static org.oran.smo.teiv.utils.TeivConstants.PROPERTY_B_SIDE;
import static org.oran.smo.teiv.utils.TeivConstants.SOURCE_IDS;
import static org.oran.smo.teiv.utils.TeivConstants.DECORATORS;
import static org.oran.smo.teiv.utils.TeivConstants.CLASSIFIERS;

import org.apache.commons.lang3.tuple.Pair;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.SelectField;
import org.jooq.Table;

import lombok.Builder;
import lombok.Data;
import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.schema.DataType;
import org.oran.smo.teiv.schema.Persistable;
import org.oran.smo.teiv.schema.RelationType;
import org.oran.smo.teiv.schema.SchemaRegistry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class InnerFilterCriteria {
    private List<TargetObject> targets;
    private LogicalBlock scope;

    public Condition getCondition() {
        return scope.getCondition();
    }

    public Set<Table> getTables() {
        Set<Table> tables = new HashSet<>();
        tables.addAll(scope.getTables());

        targets.forEach(t -> tables.add(getTableFromTarget(t)));

        return tables;
    }

    public Set<Pair<String, Field>> getWhereExistsCondition() {
        Set<Pair<String, Field>> whereExistsCondition = new LinkedHashSet<>();
        whereExistsCondition.addAll(scope.getWhereExistsConditions());
        return whereExistsCondition;
    }

    public Map<SelectField, Map<SelectField, DataType>> getSelects() {
        Map<SelectField, Map<SelectField, DataType>> selectFields = new HashMap<>();

        targets.forEach(t -> {
            Pair<SelectField, Map<SelectField, DataType>> select = getSelectFromTarget(t);
            if (!selectFields.containsKey(select.getLeft())) {
                selectFields.put(select.getLeft(), new HashMap<>());
            }
            selectFields.get(select.getLeft()).putAll(select.getRight());
        });

        return selectFields;
    }

    @SuppressWarnings({ "java:S108", "java:S5738" })
    private Pair<SelectField, Map<SelectField, DataType>> getSelectFromTarget(TargetObject targetObject) {
        Map<SelectField, DataType> select = new HashMap<>();

        final Persistable persistable;
        String idColumn;
        switch (targetObject.getTopologyObjectType()) {
            case ENTITY -> {
                persistable = SchemaRegistry.getEntityTypeByName(targetObject.getTopologyObject());
                idColumn = getTableNameWithColumnName(persistable.getTableName(), ID_COLUMN_NAME);
            }
            case RELATION -> {
                persistable = SchemaRegistry.getRelationTypeByName(targetObject.getTopologyObject());
                idColumn = getTableNameWithColumnName(persistable.getTableName(), persistable.getIdColumnName());

                select.put(field(getTableNameWithColumnName(persistable.getTableName(), ((RelationType) persistable)
                        .aSideColumnName())).as(hashAlias(getFullyQualifiedNameWithColumnName(persistable
                                .getFullyQualifiedName(), PROPERTY_A_SIDE))), DataType.PRIMITIVE);
                select.put(field(getTableNameWithColumnName(persistable.getTableName(), ((RelationType) persistable)
                        .bSideColumnName())).as(hashAlias(getFullyQualifiedNameWithColumnName(persistable
                                .getFullyQualifiedName(), PROPERTY_B_SIDE))), DataType.PRIMITIVE);
            }
            default -> throw TeivException.unParsedTopologyObjectType(targetObject.getTopologyObject());
        }

        select.put(field(idColumn).as(hashAlias(getFullyQualifiedNameWithColumnName(persistable.getFullyQualifiedName(),
                ID_COLUMN_NAME))), DataType.PRIMITIVE);

        switch (targetObject.getContainer()) {
            case ATTRIBUTES -> {
                if (targetObject.isAllParamQueried()) {
                    select.putAll(persistable.getSpecificAttributeColumns(persistable.getAttributeNames()));
                } else {
                    select.putAll(persistable.getSpecificAttributeColumns(targetObject.getParams()));
                }
            }
            case DECORATORS -> select.put(field(getTableNameWithColumnName(persistable.getTableName(), persistable
                    .getDecoratorsColumnName()), JSONB.class).as(hashAlias(getFullyQualifiedNameWithColumnName(persistable
                            .getFullyQualifiedName(), DECORATORS))), DataType.CONTAINER);
            case CLASSIFIERS -> select.put(field(getTableNameWithColumnName(persistable.getTableName(), persistable
                    .getClassifiersColumnName()), JSONB.class).as(hashAlias(getFullyQualifiedNameWithColumnName(persistable
                            .getFullyQualifiedName(), CLASSIFIERS))), DataType.CONTAINER);
            case METADATA -> select.put(field(getTableNameWithColumnName(persistable.getTableName(), persistable
                    .getMetadataColumnName()), JSONB.class).as(hashAlias(getFullyQualifiedNameWithColumnName(persistable
                            .getFullyQualifiedName(), METADATA))), DataType.CONTAINER);
            case ID -> {
            }
            case SOURCE_IDS -> select.put(field(getTableNameWithColumnName(persistable.getTableName(), persistable
                    .getSourceIdsColumnName()), JSONB.class).as(hashAlias(getFullyQualifiedNameWithColumnName(persistable
                            .getFullyQualifiedName(), SOURCE_IDS))), DataType.CONTAINER);
            default -> throw TeivException.invalidContainerType(targetObject.getContainer().getValue());
        }

        return Pair.of(field(idColumn).as(hashAlias(getFullyQualifiedNameWithColumnName(persistable.getFullyQualifiedName(),
                ID_COLUMN_NAME))), select);
    }

    public Table getTableFromTarget(TargetObject targetObject) {
        switch (targetObject.getTopologyObjectType()) {
            case ENTITY -> {
                return table(SchemaRegistry.getEntityTypeByName(targetObject.getTopologyObject()).getTableName());
            }
            case RELATION -> {
                return table(SchemaRegistry.getRelationTypeByName(targetObject.getTopologyObject()).getTableName());
            }
            default -> throw TeivException.unParsedTopologyObjectType(targetObject.getTopologyObject());
        }
    }
}
