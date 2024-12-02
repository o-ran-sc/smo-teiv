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

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.val;
import static org.oran.smo.teiv.exposure.tiespath.innerlanguage.QueryFunction.EQ;
import static org.oran.smo.teiv.utils.PersistableUtil.getTableNameWithColumnName;
import static org.oran.smo.teiv.utils.TiesConstants.ITEM;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.JSONB;

import lombok.experimental.UtilityClass;
import org.oran.smo.teiv.schema.DataType;
import org.oran.smo.teiv.schema.EntityType;
import org.oran.smo.teiv.schema.Persistable;
import org.oran.smo.teiv.schema.RelationType;
import org.oran.smo.teiv.schema.RelationshipDataLocation;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.utils.query.exception.TiesPathException;

@UtilityClass
public class ConditionFactory {
    private final String sharedRegex = "-?[\\d]+([.][\\d]+)? -?[\\d]+([.][\\d]+)?";
    private final String pointRegex = "'(POINT|point) ?[(]" + sharedRegex + "[)]'";
    private final String polygonRegex = "(POLYGON|polygon) ?[(][(](?:" + sharedRegex + ", ?){3,}(" + sharedRegex + ")[)][)]";

    public static AnyCondition create(final ScopeObject scopeObject) {

        return switch (scopeObject.getContainer()) {
            case ATTRIBUTES -> new AttributesCondition();
            case RELATION -> new RelationCondition();
            case ID -> new IdCondition();
            case ASSOCIATION -> new AssociationCondition();
            case CLASSIFIERS, SOURCE_IDS -> new ConsumerDataListCondition();
            case DECORATORS -> new ConsumerDataMapCondition();
            case METADATA -> new MetaDataMapCondition();
        };
    }

    abstract static class AnyCondition {

        public abstract Condition getCondition(final ScopeObject scopeObject);

        protected static Object convert(final ScopeObject scopeObject) {

            switch (scopeObject.getDataType()) {
                case PRIMITIVE -> {
                    return String.valueOf(scopeObject.getParameter());
                }
                case INTEGER -> {
                    return Integer.valueOf(scopeObject.getParameter());
                }
                case BIGINT -> {
                    return new BigInteger(scopeObject.getParameter());
                }
                case DECIMAL -> {
                    return new BigDecimal(scopeObject.getParameter());
                }
                case CONTAINER -> {
                    return JSONB.valueOf(scopeObject.getParameter());
                }
                case GEOGRAPHIC -> {
                    validateGeographicPoint(scopeObject.getParameter(), true);
                    return scopeObject.getParameter();
                }
                default -> throw TiesPathException.invalidQueryCondition("Unexpected value: " + scopeObject.getDataType());
            }
        }

        protected static String processContainers(final Persistable persistable, final List<String> innerContainer) {
            String tableName = persistable.getTableName();
            String rootElement = name(innerContainer.get(0)).toString();
            if (innerContainer.size() == 1) {
                return String.format("%s.%s", tableName, rootElement);
            }

            List<String> remainingElements = innerContainer.subList(1, innerContainer.size());
            String path = remainingElements.stream().map(element -> String.format("'%s'", element)).collect(Collectors
                    .joining(" -> "));
            return String.format("%s.%s -> %s", tableName, rootElement, path);
        }

        protected static String processArrayContainers(final Persistable persistable, final List<String> innerContainer) {
            String tableName = persistable.getTableName();
            String rootElement = name(innerContainer.get(0)).toString();
            if (innerContainer.size() == 1) {
                return String.format("%s.%s", tableName, rootElement);
            }

            List<String> middleElements = innerContainer.subList(1, innerContainer.size() - 1);
            String middlePath = middleElements.stream().map(element -> String.format("'%s'", element)).collect(Collectors
                    .joining(" -> "));

            String lastElement = String.format("'%s'", innerContainer.get(innerContainer.size() - 1));
            String path = middleElements.isEmpty() ?
                    String.format("->> %s", lastElement) :
                    String.format("-> %s ->> %s", middlePath, lastElement);
            return String.format("%s.%s %s", tableName, rootElement, path);
        }

        public static String handleEqComplexParameter(final ScopeObject scopeObject) {
            if (scopeObject.getDataType() == DataType.INTEGER) {
                return scopeObject.getParameter();
            }
            return String.valueOf(name(scopeObject.getParameter()));
        }

        public static String handleLikeComplexParameter(final ScopeObject scopeObject) {
            if (scopeObject.getDataType() == DataType.INTEGER) {
                return scopeObject.getParameter();
            }
            return String.format("%%%s%%", scopeObject.getParameter());
        }

        protected static Persistable getPersistable(final ScopeObject scopeObject) {
            switch (scopeObject.getTopologyObjectType()) {
                case ENTITY -> {
                    return SchemaRegistry.getEntityTypeByName(scopeObject.getTopologyObject());
                }
                case RELATION -> {
                    return SchemaRegistry.getRelationTypeByName(scopeObject.getTopologyObject());
                }
                default -> throw TiesPathException.invalidTopologyObjectType();
            }
        }

        private static void validateGeographicPoint(final String point, boolean allowHeight) {
            final boolean is2D = point.matches("(?i)POINT\\([-?\\d]+(\\.\\d+)? -?\\d+(\\.\\d+)?\\)");
            final boolean is3D = point.matches("(?i)POINT Z \\([-?\\d]+(\\.\\d+)? -?\\d+(\\.\\d+)? -?\\d+(\\.\\d+)?\\)");

            if (!is2D && !is3D) {
                if (allowHeight) {
                    throw TiesPathException.invalidQueryCondition(
                            "Invalid geographic format, geographic type must be 2D or 3D coordinates. For example: POINT(39.40 67.94) or POINT Z (47.49 19.04 111.11)");
                } else {
                    throw TiesPathException.invalidQueryCondition(
                            "Invalid geographic format, geographic type must be 2D coordinate. For example: POINT(39.40 67.94)");
                }
            } else if (is3D && !allowHeight) {
                throw TiesPathException.invalidQueryCondition("Invalid geographic format, 3D coordinates are not allowed.");
            }
        }
    }

    static class AttributesCondition extends AnyCondition {

        @Override
        public Condition getCondition(final ScopeObject scopeObject) {
            return switch (scopeObject.getQueryFunction()) {
                case EQ -> equalsCondition(scopeObject);
                case CONTAINS -> containsCondition(scopeObject);
                case WITHIN_METERS -> withinMetersCondition(scopeObject);
                case COVERED_BY -> coveredByCondition(scopeObject);
                case NOT_NULL -> {
                    if (!scopeObject.getInnerContainer().isEmpty()) {
                        throw TiesPathException.invalidQueryCondition("Inner container should be empty");
                    }
                    yield field(getTableNameWithColumnName(getPersistable(scopeObject).getTableName(), scopeObject
                            .getLeaf())).isNotNull();
                }
                default -> {
                    throw TiesPathException.invalidQueryFunction();
                }
            };
        }

        private static Condition equalsCondition(final ScopeObject scopeObject) {
            if (scopeObject.getInnerContainer().isEmpty()) {
                if (scopeObject.getDataType() == DataType.GEOGRAPHIC) {
                    return condition(getTableNameWithColumnName(getPersistable(scopeObject).getTableName(), scopeObject
                            .getLeaf()) + " = st_geomfromtext(?)", val(convert(scopeObject)));
                }
                return field(getTableNameWithColumnName(getPersistable(scopeObject).getTableName(), scopeObject.getLeaf()))
                        .eq(convert(scopeObject));
            } else if (scopeObject.getLeaf().equals(ITEM)) {
                return condition(processContainers(getPersistable(scopeObject), scopeObject.getInnerContainer()) + " @> ?",
                        inline(handleEqComplexParameter(scopeObject)));
            } else {
                return field(processContainers(getPersistable(scopeObject), scopeObject.getInnerContainer()) + " -> ?", val(
                        scopeObject.getLeaf())).eq(field(inline(handleEqComplexParameter(scopeObject))));
            }
        }

        private static Condition containsCondition(final ScopeObject scopeObject) {
            if (scopeObject.getInnerContainer().isEmpty()) {
                if (scopeObject.getDataType() == DataType.GEOGRAPHIC) {
                    throw TiesPathException.invalidQueryCondition("Cannot have CONTAINS on geography type data");
                }
                return field(getTableNameWithColumnName(getPersistable(scopeObject).getTableName(), scopeObject.getLeaf()))
                        .contains(convert(scopeObject));
            } else if (scopeObject.getLeaf().equals(ITEM)) {
                return condition(processArrayContainers(getPersistable(scopeObject), scopeObject
                        .getInnerContainer()) + "::text like ?", val("%" + scopeObject.getParameter() + "%"));
            } else {
                return condition(field(processContainers(getPersistable(scopeObject), scopeObject
                        .getInnerContainer()) + " ->> ?", val(scopeObject.getLeaf())).like(handleLikeComplexParameter(
                                scopeObject)));
            }
        }

        private static Condition withinMetersCondition(final ScopeObject scopeObject) {
            if (scopeObject.getDataType() != DataType.GEOGRAPHIC) {
                throw TiesPathException.invalidQueryCondition("Within meters condition needs geography type data");
            }

            String[] parameters = scopeObject.getParameter().split(",");
            if (parameters.length != 2 || !parameters[0].matches(pointRegex)) {
                throw TiesPathException.invalidQueryCondition("Invalid parameter for within meters condition");
            }

            return condition("ST_DWithin(?, ST_GeographyFromText(?), ?)", field("\"" + scopeObject.getLeaf() + "\""), field(
                    parameters[0]), field(parameters[1].replaceAll(" ", "")));
        }

        private static Condition coveredByCondition(final ScopeObject scopeObject) {
            if (scopeObject.getDataType() != DataType.GEOGRAPHIC) {
                throw TiesPathException.invalidQueryCondition("Covered by condition needs geography type data");
            }

            if (!scopeObject.getParameter().matches(polygonRegex)) {
                throw TiesPathException.invalidQueryCondition("Invalid parameter for covered by condition");
            }

            return condition("ST_CoveredBy(?, ST_GeographyFromText(?))", field("\"" + scopeObject.getLeaf() + "\""), val(
                    scopeObject.getParameter()));
        }

    }

    static class RelationCondition extends AnyCondition {

        @Override
        public Condition getCondition(final ScopeObject scopeObject) {

            switch (scopeObject.getQueryFunction()) {
                case EQ -> {
                    return equalsCondition(scopeObject);
                }
                case NOT_NULL -> {
                    return notNullCondition(scopeObject);
                }
                default -> throw TiesPathException.invalidQueryFunction();
            }
        }

        private static Condition equalsCondition(final ScopeObject scopeObject) {

            return field(getColumnName(scopeObject)).eq(convert(scopeObject));
        }

        private static String getColumnName(final ScopeObject scopeObject) {

            if (Objects.requireNonNull(scopeObject.getTopologyObjectType()) == TopologyObjectType.ENTITY) {

                final List<String> relationNames = SchemaRegistry.getRelationNamesByEntityName(scopeObject
                        .getTopologyObject());

                final String relationName = relationNames.stream().filter(name -> name.equals(scopeObject
                        .getInnerContainer().get(0))).findFirst().orElseThrow(() -> TiesPathException.invalidQueryCondition(
                                "Relationship was not found for topology object"));

                final Persistable persistable = SchemaRegistry.getRelationTypeByName(relationName);

                return Objects.requireNonNull(persistable).getTableName() + "." + name(persistable.getIdColumnName());
            } else {
                throw TiesPathException.invalidTopologyObjectType();
            }
        }

        private static Condition notNullCondition(final ScopeObject scopeObject) {

            return field(getColumnName(scopeObject)).isNotNull();
        }
    }

    static class AssociationCondition extends AnyCondition {

        @Override
        public Condition getCondition(final ScopeObject scopeObject) {
            return switch (scopeObject.getQueryFunction()) {
                case EQ, CONTAINS -> createEqOrContainsCondition(scopeObject);
                case NOT_NULL -> field(SchemaRegistry.getReferenceColumnName(getRelationType(scopeObject))).isNotNull();
                default -> throw TiesPathException.invalidQueryFunction();
            };
        }

        private Condition createEqOrContainsCondition(final ScopeObject scopeObject) {
            return scopeObject.getLeaf().equals("id") ?
                    createIdCondition(scopeObject) :
                    createAttributeCondition(scopeObject);
        }

        private static Condition createIdCondition(final ScopeObject scopeObject) {
            RelationType relation = getRelationType(scopeObject);
            if (scopeObject.getDataType() == null) {
                scopeObject.setDataType(DataType.PRIMITIVE);
            }

            String idColumn = getIdColumn(scopeObject, relation);
            if (relation.getRelationshipStorageLocation() == RelationshipDataLocation.RELATION) {
                String fullColumnName = getTableNameWithColumnName(relation.getTableName(), idColumn);
                return createEqualsOrContainsCondition(scopeObject, fullColumnName);
            }

            EntityType entityType = relation.getStoringSideEntityType();
            idColumn = relation.getNotStoringSideEntityIdColumnNameInStoringSideTable();
            boolean isEntityTypeMatch = matchTopologyObjectOrAssociation(scopeObject, entityType);
            if (isEntityTypeMatch) {
                String fullIdColumn = getTableNameWithColumnName(entityType.getTableName(), idColumn);
                return createEqualsOrContainsCondition(scopeObject, fullIdColumn);
            }

            String nonNullColumn = getTableNameWithColumnName(entityType.getTableName(), idColumn);
            String fullIdColumn = getTableNameWithColumnName(entityType.getTableName(), entityType.getIdColumnName());
            return field(nonNullColumn).isNotNull().and(createEqualsOrContainsCondition(scopeObject, fullIdColumn));
        }

        private static Condition createAttributeCondition(final ScopeObject scopeObject) {
            RelationType relation = getRelationType(scopeObject);
            EntityType entityType = getEntityTypeOnFilterSide(scopeObject, relation);
            if (scopeObject.getDataType() == null) {
                throw TiesPathException.invalidQueryCondition("Datatype for parameter is missing");
            }
            if (scopeObject.getQueryFunction().equals(QueryFunction.CONTAINS) && scopeObject.getDataType().equals(
                    DataType.GEOGRAPHIC)) {
                throw TiesPathException.invalidQueryCondition("Cannot have CONTAINS on geography type data");
            }
            List<String> innerContainer = scopeObject.getInnerContainer().subList(1, scopeObject.getInnerContainer()
                    .size());
            if (innerContainer.isEmpty() && scopeObject.getDataType() == DataType.GEOGRAPHIC) {
                return condition(getTableNameWithColumnName(entityType.getTableName(), scopeObject
                        .getLeaf()) + " = st_geomfromtext(?)", val(convert(scopeObject)));
            }

            String compareField = getAttributeColumn(scopeObject, innerContainer, relation);
            if (!relation.getRelationshipStorageLocation().equals(RelationshipDataLocation.RELATION)) {
                if (relation.getStoringSideEntityType().equals(entityType)) {
                    String idColumn = getTableNameWithColumnName(entityType.getTableName(), getIdColumn(scopeObject,
                            relation));
                    return field(idColumn).isNotNull().and(createEqualsOrContainsCondition(scopeObject, compareField));
                }
                return createEqualsOrContainsCondition(scopeObject, compareField);
            }
            return createEqualsOrContainsCondition(scopeObject, compareField);
        }

        private static String getIdColumn(final ScopeObject scopeObject, final RelationType relation) {
            if (relation.getRelationshipStorageLocation() != RelationshipDataLocation.RELATION) {
                return relation.getNotStoringSideEntityIdColumnNameInStoringSideTable();
            }
            if (!relation.isConnectsSameEntity()) {
                return matchTopologyObjectOrAssociation(scopeObject, relation.getASide()) ?
                        relation.bSideColumnName() :
                        relation.aSideColumnName();
            }
            boolean aSideAssociation = relation.getASideAssociation().getName().equals(scopeObject.getInnerContainer().get(
                    0));
            return aSideAssociation ? relation.aSideColumnName() : relation.bSideColumnName();
        }

        private static String getAttributeColumn(final ScopeObject scopeObject, final List<String> innerContainer,
                final RelationType relation) {
            if (!scopeObject.getTopologyObjectType().equals(TopologyObjectType.ENTITY)) {
                throw TiesPathException.invalidTopologyObjectType();
            }
            EntityType entityType = getEntityTypeOnFilterSide(scopeObject, relation);
            if (innerContainer.isEmpty()) {
                return getTableNameWithColumnName(entityType.getTableName(), scopeObject.getLeaf());
            }
            return processContainers(entityType, innerContainer) + String.format(" -> '%s'", scopeObject.getLeaf());
        }

        private static RelationType getRelationType(final ScopeObject scopeObject) {
            switch (scopeObject.getTopologyObjectType()) {
                case ENTITY:
                    return SchemaRegistry.getAllRelationNamesByAssociationName(scopeObject.getInnerContainer().get(0))
                            .stream().filter(r -> topologyObjectEquals(scopeObject, r.getASide()) || topologyObjectEquals(
                                    scopeObject, r.getBSide())).findFirst().orElseThrow(() -> TiesPathException
                                            .invalidQueryCondition("Relation was not found"));
                case RELATION:
                    return SchemaRegistry.getRelationTypeByName(scopeObject.getTopologyObject());
                default:
                    throw TiesPathException.invalidTopologyObjectType();
            }
        }

        private static EntityType getEntityTypeOnFilterSide(final ScopeObject scopeObject, final RelationType relation) {
            boolean isTopologyObjASide = matchTopologyObjectOrAssociation(scopeObject, relation.getASide());
            return isTopologyObjASide ? relation.getBSide() : relation.getASide();
        }

        private static boolean matchTopologyObjectOrAssociation(final ScopeObject scopeObject,
                final EntityType entityType) {
            RelationType relation = getRelationType(scopeObject);
            return topologyObjectEquals(scopeObject, entityType) || SchemaRegistry.getEntityTypeOnAssociationSide(relation,
                    scopeObject.getInnerContainer().get(0)).equals(entityType);
        }

        private static boolean topologyObjectEquals(final ScopeObject scopeObject, final EntityType entityType) {
            return entityType.getName().equals(scopeObject.getTopologyObject());
        }

        private static Condition createEqualsOrContainsCondition(final ScopeObject scopeObject, final String field) {
            return scopeObject.getQueryFunction() == EQ ?
                    condition(field(field).eq(convert(scopeObject))) :
                    condition(field(field).contains(convert(scopeObject)));
        }
    }

    static class MetaDataMapCondition extends AnyCondition {

        @Override
        public Condition getCondition(final ScopeObject scopeObject) {
            if (!scopeObject.getQueryFunction().equals(EQ)) {
                throw TiesPathException.invalidQueryFunction();
            }
            return equalsCondition(scopeObject);
        }

        private static String getColumnName(final ScopeObject scopeObject) {

            final Persistable persistable = getPersistable(scopeObject);

            return Objects.requireNonNull(persistable).getTableName() + "." + name(persistable.getMetadataColumnName());
        }

        private static Condition equalsCondition(final ScopeObject scopeObject) {
            return condition("? -> ? = ?", field(getColumnName(scopeObject)), inline(scopeObject.getLeaf()), inline(
                    "\"" + convert(scopeObject) + "\""));
        }
    }

    static class ConsumerDataMapCondition extends AnyCondition {

        @Override
        public Condition getCondition(final ScopeObject scopeObject) {
            if (scopeObject.getQueryFunction().equals(EQ)) {

                return equalsCondition(scopeObject);
            }

            return containsCondition(scopeObject);
        }

        private static String getColumnName(final ScopeObject scopeObject) {

            final Persistable persistable = getPersistable(scopeObject);

            return Objects.requireNonNull(persistable).getTableName() + "." + name(persistable.getDecoratorsColumnName());
        }

        private static Condition equalsCondition(final ScopeObject scopeObject) {

            String newValue = scopeObject.getParameter();

            if (scopeObject.getDataType() == DataType.PRIMITIVE) {
                newValue = String.format("\"%s\"", convert(scopeObject));

            }

            return field(getColumnName(scopeObject) + " -> '" + scopeObject.getLeaf() + "'").eq(field(
                    "'" + newValue + "'"));
        }

        private static Condition containsCondition(final ScopeObject scopeObject) {

            return field(getColumnName(scopeObject) + " ->> '" + scopeObject.getLeaf() + "'").like("%" + scopeObject
                    .getParameter() + "%");
        }
    }

    static class ConsumerDataListCondition extends AnyCondition {

        @Override
        public Condition getCondition(final ScopeObject scopeObject) {
            if (scopeObject.getQueryFunction().equals(EQ)) {

                return equalsCondition(scopeObject);
            }

            return containsCondition(scopeObject);
        }

        private static String getColumnName(final ScopeObject scopeObject) {

            final Persistable persistable = getPersistable(scopeObject);

            if (scopeObject.getContainer().equals(ContainerType.CLASSIFIERS)) {
                return Objects.requireNonNull(persistable).getTableName() + "." + name(persistable
                        .getClassifiersColumnName());
            }

            return Objects.requireNonNull(persistable).getTableName() + "." + name(persistable.getSourceIdsColumnName());
        }

        private static Condition equalsCondition(final ScopeObject scopeObject) {

            return condition("? @> ?", field(getColumnName(scopeObject)), inline("\"" + scopeObject.getParameter() + "\""));
        }

        private static Condition containsCondition(final ScopeObject scopeObject) {

            return condition("?::text like ?", field(getColumnName(scopeObject)), val("%" + scopeObject
                    .getParameter() + "%"));
        }
    }

    static class IdCondition extends AnyCondition {

        @Override
        public Condition getCondition(final ScopeObject scopeObject) {

            switch (scopeObject.getQueryFunction()) {
                case EQ -> {
                    return field(getColumnName(scopeObject)).eq(convert(scopeObject));
                }
                case CONTAINS -> {
                    return field(getColumnName(scopeObject)).contains(convert(scopeObject));
                }
                case NOT_NULL -> {
                    return field(getColumnName(scopeObject)).isNotNull();
                }
                default -> throw TiesPathException.invalidQueryFunction();
            }
        }

        private static String getColumnName(final ScopeObject scopeObject) {
            final Persistable persistable = getPersistable(scopeObject);
            return persistable.getIdColumnNameWithTableName();
        }
    }
}
