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
package org.oran.smo.teiv.exposure.teivpath.innerlanguage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Table;
import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.schema.EntityType;
import org.oran.smo.teiv.schema.RelationType;
import org.oran.smo.teiv.schema.RelationshipDataLocation;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.utils.query.exception.TeivPathException;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.oran.smo.teiv.exposure.teivpath.innerlanguage.TopologyObjectType.ASSOCIATION;
import static org.oran.smo.teiv.utils.PersistableUtil.getTableNameWithColumnName;
import static org.oran.smo.teiv.utils.TeivConstants.ID_COLUMN_NAME;
import static org.oran.smo.teiv.utils.TeivConstants.QUOTED_STRING;

@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class ScopeLogicalBlock extends LogicalBlock {
    private final ScopeObject scopeObject;

    @Override
    public Condition getCondition() {
        return ConditionFactory.create(scopeObject).getCondition(scopeObject);
    }

    @Override
    public Set<Table> getTables() {
        Set<Table> result = new HashSet<>();
        if (scopeObject.getTopologyObjectType() == TopologyObjectType.ASSOCIATION) {
            result.add(addAssociationToTable());
        } else if (scopeObject.getTopologyObjectType() == TopologyObjectType.ENTITY) {
            result.add(addEntityToTable());
        } else if (scopeObject.getTopologyObjectType() == TopologyObjectType.RELATION) {
            result.add(addRelationToTable());
        } else if (scopeObject.getTopologyObjectType() == TopologyObjectType.UNDEFINED) {
            throw TeivException.unknownTopologyObjectType(scopeObject.getTopologyObject());
        }
        return result;
    }

    @SuppressWarnings({ "java:S1874" })
    public Set<Pair<String, Field>> getJoinCondition() {
        Set<Pair<String, Field>> joinCondition = new LinkedHashSet<>();
        String[] topologyObjectSplit = scopeObject.getTopologyObject().split("/");
        if (scopeObject.getTopologyObjectType().equals(ASSOCIATION) && SchemaRegistry.getEntityTypeByName(
                topologyObjectSplit[0]) != null) {
            EntityType entityType = SchemaRegistry.getEntityTypeByName(topologyObjectSplit[0]);
            String association = topologyObjectSplit[1];
            List<RelationType> relationTypes = SchemaRegistry.getAllRelationNamesByAssociationName(association);
            RelationType relationType = relationTypes.stream().filter(relation -> relation.getASide().equals(
                    entityType) || relation.getBSide().equals(entityType)).findFirst().orElseThrow(() -> TeivPathException
                            .invalidAssociation(entityType.getName(), association));
            if (isManyToMany(relationType)) {
                String columnName = getColumnNameForManyToManyJoin(entityType, relationType);
                String col1 = constructColumnNameForJoinCondition(relationType, columnName);
                String col2 = getTableNameWithColumnName(entityType.getTableName(), ID_COLUMN_NAME);
                joinCondition.add(constructJoinConditionPair(relationType.getTableName(), col1, col2));

                if (isSecondJoinNeeded()) {
                    joinCondition.add(getSecondJoinCondition(entityType, relationType));
                }
            } else if (isJoinNeeded(topologyObjectSplit, relationType)) {
                String col1 = constructColumnNameForJoinCondition(relationType, relationType
                        .getNotStoringSideEntityIdColumnNameInStoringSideTable());
                String col2 = getTableNameWithColumnName(relationType.getNotStoringSideTableName(), ID_COLUMN_NAME);
                String tableName = getTableNameForJoin(topologyObjectSplit, relationType);
                joinCondition.add(constructJoinConditionPair(tableName, col1, col2));
            }
        }
        return joinCondition;
    }

    private boolean isSecondJoinNeeded() {
        return !scopeObject.getContainer().equals(ContainerType.ID) && !scopeObject.getContainer().equals(
                ContainerType.NOT_NULL);
    }

    private static boolean isManyToMany(RelationType relationType) {
        return relationType.getRelationshipStorageLocation().equals(RelationshipDataLocation.RELATION);
    }

    private boolean isJoinNeeded(String[] topologyObjectSplit, RelationType relationType) {
        return !relationType.getStoringSideEntityType().getName().equals(topologyObjectSplit[0]) || (!scopeObject
                .getContainer().equals(ContainerType.ID) && !scopeObject.getContainer().equals(ContainerType.NOT_NULL));
    }

    private static String getTableNameForJoin(String[] topologyObjectSplit, RelationType relationType) {
        if (relationType.getStoringSideEntityType().getName().equals(topologyObjectSplit[0])) {
            return relationType.getNotStoringSideTableName();
        } else {
            return relationType.getTableName();
        }
    }

    private static Pair<String, Field> getSecondJoinCondition(EntityType entityType, RelationType relationType) {
        EntityType entityTypeForSecondJoin;
        String columnNameForSecondJoin;
        if (!relationType.getASide().equals(entityType)) {
            columnNameForSecondJoin = relationType.aSideColumnName();
            entityTypeForSecondJoin = relationType.getASide();
        } else {
            columnNameForSecondJoin = relationType.bSideColumnName();
            entityTypeForSecondJoin = relationType.getBSide();
        }
        String col1ForSecondJoin = constructColumnNameForJoinCondition(relationType, columnNameForSecondJoin);
        String col2ForSecondJoin = getTableNameWithColumnName(entityTypeForSecondJoin.getTableName(), ID_COLUMN_NAME);
        return constructJoinConditionPair(entityTypeForSecondJoin.getTableName(), col1ForSecondJoin, col2ForSecondJoin);
    }

    private static String getColumnNameForManyToManyJoin(EntityType entityType, RelationType relationType) {
        if (relationType.getASide().equals(entityType)) {
            return relationType.aSideColumnName();
        } else {
            return relationType.bSideColumnName();
        }
    }

    private static String constructColumnNameForJoinCondition(RelationType relationType, String columnName) {
        return relationType.getTableName() + "." + String.format(QUOTED_STRING, columnName);
    }

    private static Pair<String, Field> constructJoinConditionPair(String tableName, String col1, String col2) {
        Field equalsField = field(col1 + "=" + col2);
        return new ImmutablePair<>(tableName, equalsField);
    }

    @SuppressWarnings({ "java:S1874" })
    private Table addEntityToTable() {
        return table(SchemaRegistry.getEntityTypeByName(scopeObject.getTopologyObject()).getTableName());
    }

    @SuppressWarnings({ "java:S1874" })
    private Table addRelationToTable() {
        return table(SchemaRegistry.getRelationTypeByName(scopeObject.getTopologyObject()).getTableName());
    }

    private Table addAssociationToTable() {
        String[] topologyObjectArray = scopeObject.getTopologyObject().split("/");
        String topologyObject = topologyObjectArray[0];
        boolean isEntity = SchemaRegistry.getEntityNames().contains(topologyObject);
        List<RelationType> relationTypes = SchemaRegistry.getAllRelationNamesByAssociationName(topologyObjectArray[1]);
        if (isEntity) {
            for (RelationType relation : relationTypes) {
                if (relation.getASide().getName().equals(topologyObject) || relation.getBSide().getName().equals(
                        topologyObject)) {
                    return table(relation.getTableName());
                }
            }
        } else {
            Optional<RelationType> relation = relationTypes.stream().filter(relationType -> relationType.getName().equals(
                    topologyObject)).findFirst();
            if (relation.isPresent()) {
                return table(relation.get().getTableName());
            }
        }

        throw TeivException.invalidAssociationType(scopeObject.getInnerContainer().get(0));
    }
}
