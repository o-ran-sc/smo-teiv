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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.selectOne;
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
        Condition scopeCondition = ConditionFactory.create(scopeObject).getCondition(scopeObject);
        List<Pair<String, Field>> whereExistsConditions = new ArrayList<>(getWhereExistsConditions());
        if (!whereExistsConditions.isEmpty()) {
            if (whereExistsConditions.size() > 2) {
                //error out as with in a scope block there cannot be more than two joins/where exists conditions(many to many)
                throw TeivException.serverException("Server unknown exception",
                        "More than two where exists condition within a logical block is not supported", null);
            }
            Condition existsCondition;
            if (whereExistsConditions.size() == 1) {
                existsCondition = exists(selectOne().from(whereExistsConditions.get(0).getLeft()).where(scopeCondition).and(
                        whereExistsConditions.get(0).getRight()));
            } else {
                existsCondition = exists(selectOne().from(whereExistsConditions.get(0).getLeft()).where(
                        whereExistsConditions.get(0).getRight()).andExists(selectOne().from(whereExistsConditions.get(1)
                                .getLeft()).where(whereExistsConditions.get(1).getRight()).and(scopeCondition)));
            }
            return existsCondition;
        }
        return scopeCondition;
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
    public Set<Pair<String, Field>> getWhereExistsConditions() {
        Set<Pair<String, Field>> whereExistsCondition = new LinkedHashSet<>();
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
                String columnName = getColumnNameForManyToManyWhereExists(entityType, relationType);
                String col1 = constructColumnNameForWhereExistsCondition(relationType, columnName);
                String col2 = getTableNameWithColumnName(entityType.getTableName(), ID_COLUMN_NAME);
                whereExistsCondition.add(constructWhereExistsConditionPair(relationType.getTableName(), col1, col2));

                if (isNestedWhereExistsRequired()) {
                    whereExistsCondition.add(getNestedWhereExistsCondition(entityType, relationType));
                }
            } else if (isWhereExistsNeeded(topologyObjectSplit, relationType)) {
                String col1 = constructColumnNameForWhereExistsCondition(relationType, relationType
                        .getNotStoringSideEntityIdColumnNameInStoringSideTable());
                String col2 = getTableNameWithColumnName(relationType.getNotStoringSideTableName(), ID_COLUMN_NAME);
                String tableName = getTableNameForWhereExists(topologyObjectSplit, relationType);
                whereExistsCondition.add(constructWhereExistsConditionPair(tableName, col1, col2));
            }
        }
        return whereExistsCondition;
    }

    private boolean isNestedWhereExistsRequired() {
        return !scopeObject.getContainer().equals(ContainerType.ID) && !scopeObject.getContainer().equals(
                ContainerType.NOT_NULL);
    }

    private static boolean isManyToMany(RelationType relationType) {
        return relationType.getRelationshipStorageLocation().equals(RelationshipDataLocation.RELATION);
    }

    private boolean isWhereExistsNeeded(String[] topologyObjectSplit, RelationType relationType) {
        return !relationType.getStoringSideEntityType().getName().equals(topologyObjectSplit[0]) || (!scopeObject
                .getContainer().equals(ContainerType.ID) && !scopeObject.getContainer().equals(ContainerType.NOT_NULL));
    }

    private static String getTableNameForWhereExists(String[] topologyObjectSplit, RelationType relationType) {
        if (relationType.getStoringSideEntityType().getName().equals(topologyObjectSplit[0])) {
            return relationType.getNotStoringSideTableName();
        } else {
            return relationType.getTableName();
        }
    }

    private static Pair<String, Field> getNestedWhereExistsCondition(EntityType entityType, RelationType relationType) {
        EntityType entityTypeForNestedWhereExists;
        String columnNameForNestedWhereExists;
        if (!relationType.getASide().equals(entityType)) {
            columnNameForNestedWhereExists = relationType.aSideColumnName();
            entityTypeForNestedWhereExists = relationType.getASide();
        } else {
            columnNameForNestedWhereExists = relationType.bSideColumnName();
            entityTypeForNestedWhereExists = relationType.getBSide();
        }
        String col1ForNestedWhereExists = constructColumnNameForWhereExistsCondition(relationType,
                columnNameForNestedWhereExists);
        String col2ForNestedWhereExists = getTableNameWithColumnName(entityTypeForNestedWhereExists.getTableName(),
                ID_COLUMN_NAME);
        return constructWhereExistsConditionPair(entityTypeForNestedWhereExists.getTableName(), col1ForNestedWhereExists,
                col2ForNestedWhereExists);
    }

    private static String getColumnNameForManyToManyWhereExists(EntityType entityType, RelationType relationType) {
        if (relationType.getASide().equals(entityType)) {
            return relationType.aSideColumnName();
        } else {
            return relationType.bSideColumnName();
        }
    }

    private static String constructColumnNameForWhereExistsCondition(RelationType relationType, String columnName) {
        return relationType.getTableName() + "." + String.format(QUOTED_STRING, columnName);
    }

    private static Pair<String, Field> constructWhereExistsConditionPair(String tableName, String col1, String col2) {
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
