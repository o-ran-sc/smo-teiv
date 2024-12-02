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
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Table;
import org.oran.smo.teiv.exception.TiesException;
import org.oran.smo.teiv.schema.EntityType;
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
import static org.oran.smo.teiv.utils.TiesConstants.QUOTED_STRING;

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

        if (scopeObject.getContainer().equals(ContainerType.ASSOCIATION)) {
            result.add(addAssociationToTable());
        } else if (scopeObject.getTopologyObjectType() == TopologyObjectType.ENTITY) {
            result.add(addEntityToTable());
        } else if (scopeObject.getTopologyObjectType() == TopologyObjectType.RELATION) {
            result.add(addRelationToTable());
        } else if (scopeObject.getTopologyObjectType() == TopologyObjectType.UNDEFINED) {
            throw TiesException.unknownTopologyObjectType(scopeObject.getTopologyObject());
        }

        return result;
    }

    public Set<Pair<String, Field>> getJoinCondition() {
        HashSet<Pair<String, Field>> joinCondition = new HashSet<>();
        if (scopeObject.getContainer().equals(ContainerType.ASSOCIATION) && scopeObject.getTopologyObjectType().equals(
                TopologyObjectType.ENTITY)) {
            EntityType entityType = SchemaRegistry.getEntityTypeByName(scopeObject.getTopologyObject());
            String association = scopeObject.getInnerContainer().get(0);
            List<RelationType> relationTypes = SchemaRegistry.getAllRelationNamesByAssociationName(association);

            RelationType relationType = relationTypes.stream().filter(relation -> relation.getASide().equals(
                    entityType) || relation.getBSide().equals(entityType)).findFirst().orElseThrow(() -> TiesPathException
                            .invalidAssociation(entityType.getName(), association));

            if (relationType.getRelationshipStorageLocation().equals(RelationshipDataLocation.RELATION)) {
                String columnName = "";
                if (relationType.getASide().equals(entityType)) {
                    columnName = relationType.aSideColumnName();
                } else {
                    columnName = relationType.bSideColumnName();
                }
                String col1 = constructColumnNameForJoinCondition(relationType, columnName);
                String col2 = getTableNameWithColumnName(entityType.getTableName(), ID_COLUMN_NAME);

                joinCondition.add(constructJoinConditionPair(relationType, col1, col2));
            } else if (!relationType.getStoringSideEntityType().getName().equals(scopeObject.getTopologyObject())) {
                String col1 = constructColumnNameForJoinCondition(relationType, relationType
                        .getNotStoringSideEntityIdColumnNameInStoringSideTable());
                String col2 = getTableNameWithColumnName(relationType.getNotStoringSideTableName(), ID_COLUMN_NAME);

                joinCondition.add(constructJoinConditionPair(relationType, col1, col2));
            }
        }
        return joinCondition;
    }

    private static String constructColumnNameForJoinCondition(RelationType relationType, String columnName) {
        return relationType.getTableName() + "." + String.format(QUOTED_STRING, columnName);
    }

    private static Pair<String, Field> constructJoinConditionPair(RelationType relationType, String col1, String col2) {
        Field equalsField = field(col1 + "=" + col2);
        return new ImmutablePair<>(relationType.getTableName(), equalsField);
    }

    private Table addEntityToTable() {
        return table(SchemaRegistry.getEntityTypeByName(scopeObject.getTopologyObject()).getTableName());
    }

    private Table addRelationToTable() {
        return table(SchemaRegistry.getRelationTypeByName(scopeObject.getTopologyObject()).getTableName());
    }

    private Table addAssociationToTable() {
        List<RelationType> relationTypes = SchemaRegistry.getAllRelationNamesByAssociationName(scopeObject
                .getInnerContainer().get(0));
        if (scopeObject.getTopologyObjectType().equals(TopologyObjectType.ENTITY)) {
            for (RelationType relation : relationTypes) {
                if (relation.getASide().getName().equals(scopeObject.getTopologyObject()) || relation.getBSide().getName()
                        .equals(scopeObject.getTopologyObject())) {
                    return table(relation.getTableName());
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

}
