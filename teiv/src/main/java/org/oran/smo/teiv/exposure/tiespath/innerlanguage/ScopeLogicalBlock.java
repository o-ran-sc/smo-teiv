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
package org.oran.smo.teiv.exposure.tiespath.innerlanguage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Table;
import org.oran.smo.teiv.exception.TiesException;
import org.oran.smo.teiv.schema.EntityType;
import org.oran.smo.teiv.schema.RelationCardinality;
import org.oran.smo.teiv.schema.RelationType;
import org.oran.smo.teiv.schema.RelationshipDataLocation;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.utils.query.exception.TiesPathException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.oran.smo.teiv.utils.PersistableUtil.getTableNameWithColumnName;
import static org.oran.smo.teiv.utils.TiesConstants.ID_COLUMN_NAME;

@Data
@EqualsAndHashCode(callSuper = true)
public class ScopeLogicalBlock extends LogicalBlock {
    private final ScopeObject scopeObject;

    @Override
    public Condition getCondition() {
        return ConditionFactory.create(scopeObject).getCondition(scopeObject);
    }

    @Override
    public Set<Table> getTables() {

        Set<Table> result = new HashSet<>();

        if (scopeObject.getTopologyObjectType() == TopologyObjectType.ENTITY) {
            result.add(getEntityTable());
        } else if (scopeObject.getTopologyObjectType() == TopologyObjectType.RELATION) {
            result.add(getRelationTable());
        } else if (scopeObject.getTopologyObjectType() == TopologyObjectType.UNDEFINED) {
            throw TiesException.unknownTopologyObjectType(scopeObject.getTopologyObject());
        }
        if (scopeObject.getContainer().equals(ContainerType.ASSOCIATION)) {
            result.add(getAssociationTable());
        }
        return result;
    }

    public Set<Pair<String, Field>> getJoinCondition() {
        if (scopeObject.getContainer().equals(ContainerType.ASSOCIATION)) {
            switch (scopeObject.getTopologyObjectType()) {
                case ENTITY:
                    return constructEntityAssociationJoin(scopeObject);
                case RELATION:
                    return constructRelationAssociationJoin(scopeObject);
                default:
                    break;
            }
        }
        return new HashSet<>();
    }

    private static Set<Pair<String, Field>> constructEntityAssociationJoin(ScopeObject scopeObject) {
        Set<Pair<String, Field>> joinCondition = new HashSet<>();
        EntityType entityType = SchemaRegistry.getEntityTypeByName(scopeObject.getTopologyObject());
        String association = scopeObject.getInnerContainer().get(0);
        List<RelationType> relationTypes = SchemaRegistry.getAllRelationNamesByAssociationName(association);

        RelationType relationType = relationTypes.stream().filter(relation -> relation.getASide().equals(
                entityType) || relation.getBSide().equals(entityType)).findFirst().orElseThrow(() -> TiesPathException
                        .invalidAssociation(entityType.getName(), association));
        if (relationType.getRelationshipStorageLocation().equals(RelationshipDataLocation.RELATION)) {
            String columnName = relationType.getASide().equals(entityType) ?
                    relationType.aSideColumnName() :
                    relationType.bSideColumnName();
            String col1 = getTableNameWithColumnName(relationType.getTableName(), columnName);
            String col2 = getTableNameWithColumnName(entityType.getTableName(), ID_COLUMN_NAME);
            joinCondition.add(constructJoinConditionPair(relationType, col1, col2));
        } else if (!relationType.getStoringSideEntityType().getName().equals(scopeObject.getTopologyObject())) {
            String col1 = getTableNameWithColumnName(relationType.getTableName(), relationType
                    .getNotStoringSideEntityIdColumnNameInStoringSideTable());
            String col2 = getTableNameWithColumnName(relationType.getNotStoringSideTableName(), ID_COLUMN_NAME);
            joinCondition.add(constructJoinConditionPair(relationType, col1, col2));
        }
        return joinCondition;
    }

    private static HashSet<Pair<String, Field>> constructRelationAssociationJoin(ScopeObject scopeObject) {
        HashSet<Pair<String, Field>> joinCondition = new HashSet<>();
        String association = scopeObject.getInnerContainer().get(0);
        List<RelationType> relationTypes = SchemaRegistry.getAllRelationNamesByAssociationName(association);

        RelationType relationType = relationTypes.stream().filter(relation -> relation.getName().equals(scopeObject
                .getTopologyObject())).findFirst().orElseThrow(() -> TiesPathException.invalidAssociation(scopeObject
                        .getTopologyObject(), association));

        EntityType targetEntity = relationType.getAssociationSide(association);
        RelationCardinality relationCardinality = relationType.getRelationCardinality(targetEntity.getName());
        if (relationCardinality == RelationCardinality.ONE_TO_MANY || relationCardinality == RelationCardinality.MANY_TO_ONE) {
            String tableName = relationCardinality == RelationCardinality.ONE_TO_MANY ?
                    relationType.getTableName() :
                    targetEntity.getTableName();
            Pair<String, Field> pairOneToMany = createJoinPair(tableName, relationType.getNotStoringSideTableName(),
                    relationType.getNotStoringSideEntityIdColumnNameInStoringSideTable());
            joinCondition.add(pairOneToMany);
        } else if (relationCardinality == RelationCardinality.MANY_TO_MANY || relationCardinality == RelationCardinality.ONE_TO_ONE) {
            Pair<String, Field> pairRelationTable = createMultipleJoinPair(relationType.getTableName(), relationType
                    .getBSide().getTableName(), relationType.aSideColumnName(), relationType.bSideColumnName());
            joinCondition.add(pairRelationTable);
        }
        return joinCondition;
    }

    private static Pair<String, Field> createJoinPair(String tableName, String tableName2, String foreignKeyColumn) {
        String foreignKeyField = getTableNameWithColumnName(tableName, foreignKeyColumn);
        String idField = getTableNameWithColumnName(tableName2, ID_COLUMN_NAME);
        return new ImmutablePair<>(tableName2, field(foreignKeyField + "=" + idField));
    }

    private static Pair<String, Field> createMultipleJoinPair(String tableName, String relationTable,
            String foreignKeyColumn, String foreignKeyColumn2) {
        String foreignKeyField = getTableNameWithColumnName(tableName, foreignKeyColumn);
        String foreignKeyField2 = getTableNameWithColumnName(tableName, foreignKeyColumn2);
        String idField = getTableNameWithColumnName(relationTable, ID_COLUMN_NAME);
        String idField2 = getTableNameWithColumnName(relationTable, ID_COLUMN_NAME);
        return new ImmutablePair<>(relationTable, field(foreignKeyField + "=" + idField + " OR " + field(
                foreignKeyField2 + "=" + idField2)));
    }

    private static Pair<String, Field> constructJoinConditionPair(RelationType relationType, String col1, String col2) {
        Field equalsField = field(col1 + "=" + col2);
        return new ImmutablePair<>(relationType.getTableName(), equalsField);
    }

    private Table getEntityTable() {
        return table(SchemaRegistry.getEntityTypeByName(scopeObject.getTopologyObject()).getTableName());
    }

    private Table getRelationTable() {
        return table(SchemaRegistry.getRelationTypeByName(scopeObject.getTopologyObject()).getTableName());
    }

    private Table getAssociationTable() {
        List<RelationType> relationTypes = SchemaRegistry.getAllRelationNamesByAssociationName(scopeObject
                .getInnerContainer().get(0));
        if (scopeObject.getTopologyObjectType().equals(TopologyObjectType.ENTITY)) {
            for (RelationType relation : relationTypes) {
                if (relation.getASide().getName().equals(scopeObject.getTopologyObject())) {
                    return table(relation.getBSide().getTableName());
                } else if (relation.getBSide().getName().equals(scopeObject.getTopologyObject())) {
                    return table(relation.getASide().getTableName());
                }
            }
        } else {
            Optional<RelationType> relation = relationTypes.stream().filter(relationType -> relationType.getName().equals(
                    scopeObject.getTopologyObject())).findFirst();
            if (relation.isPresent()) {
                return table(relation.get().getTableName());
            }
        }
        throw TiesException.invalidAssociationType(scopeObject.getInnerContainer().get(0));
    }

    @Override
    public boolean isFirstHop() {
        return scopeObject.getContainer() != null && scopeObject.getContainer().equals(ContainerType.ASSOCIATION);
    }
}
