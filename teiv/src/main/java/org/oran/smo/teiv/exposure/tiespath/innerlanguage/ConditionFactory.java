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

import org.jooq.Condition;
import org.jooq.JSONB;

import lombok.experimental.UtilityClass;
import org.oran.smo.teiv.schema.DataType;
import org.oran.smo.teiv.schema.EntityType;
import org.oran.smo.teiv.schema.Persistable;
import org.oran.smo.teiv.schema.RelationType;
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
                    validateGeographicPoint(scopeObject.getParameter());
                    return scopeObject.getParameter();
                }
                default -> throw TiesPathException.invalidQueryCondition("Unexpected value: " + scopeObject.getDataType());
            }
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

        private static void validateGeographicPoint(final String point) {
            final boolean is2D = point.matches("(?i)POINT\\([-?\\d]+(\\.\\d+)? -?\\d+(\\.\\d+)?\\)");
            final boolean is3D = point.matches("(?i)POINT Z \\([-?\\d]+(\\.\\d+)? -?\\d+(\\.\\d+)? -?\\d+(\\.\\d+)?\\)");

            if (!is2D && !is3D) {
                throw TiesPathException.invalidQueryCondition(
                        "Invalid geographic format, geographic type must be 2D or 3D coordinates. For example: POINT(39.40 67.94) or POINT Z (47.49 19.04 111.11)");
            }
        }
    }

    static class AttributesCondition extends AnyCondition {

        @Override
        public Condition getCondition(final ScopeObject scopeObject) {
            if (scopeObject.getInnerContainer().isEmpty()) {
                return handleSimpleAttribute(scopeObject);
            }
            return handleComplexAttribute(scopeObject);
        }

        private Condition handleSimpleAttribute(final ScopeObject scopeObject) {
            switch (scopeObject.getQueryFunction()) {
                case EQ -> {
                    if (scopeObject.getDataType().equals(DataType.GEOGRAPHIC)) {
                        return condition("? = st_geomfromtext(?)", field(handleSimpleLeaf(scopeObject)), val(convert(
                                scopeObject)));
                    }
                    return field(handleSimpleLeaf(scopeObject)).eq(convert(scopeObject));
                }
                case CONTAINS -> {
                    if (scopeObject.getDataType().equals(DataType.GEOGRAPHIC)) {
                        throw TiesPathException.invalidQueryCondition("Cannot have CONTAINS on geography type data");
                    }
                    return field(handleSimpleLeaf(scopeObject)).contains(convert(scopeObject));
                }
                case WITHIN_METERS -> {
                    if (!scopeObject.getDataType().equals(DataType.GEOGRAPHIC)) {
                        throw TiesPathException.invalidQueryCondition("Within meters condition needs geography type data");
                    }

                    String[] parameters = scopeObject.getParameter().split(",");
                    if (parameters.length != 2 || !parameters[0].matches(pointRegex)) {
                        throw TiesPathException.invalidQueryCondition("Invalid parameter for within meters condition");
                    }

                    return condition("ST_DWithin(?, ST_GeographyFromText(?), ?)", field("\"" + scopeObject
                            .getLeaf() + "\""), field(parameters[0]), field(parameters[1].replaceAll(" ", "")));
                }
                case COVERED_BY -> {
                    if (!scopeObject.getDataType().equals(DataType.GEOGRAPHIC)) {
                        throw TiesPathException.invalidQueryCondition("Covered by condition needs geography type data");
                    }

                    if (!scopeObject.getParameter().matches(polygonRegex)) {
                        throw TiesPathException.invalidQueryCondition("Invalid parameter for covered by condition");
                    }

                    return condition("ST_CoveredBy(?, ST_GeographyFromText(?))", field("\"" + scopeObject.getLeaf() + "\""),
                            val(scopeObject.getParameter()));
                }
                default -> {
                    return field(handleSimpleLeaf(scopeObject)).isNotNull();
                }
            }
        }

        private Condition handleComplexAttribute(final ScopeObject scopeObject) {
            switch (scopeObject.getQueryFunction()) {
                case EQ -> {
                    if (scopeObject.getLeaf().equals(ITEM)) {
                        return condition(handleContainers(scopeObject) + " @> ?", inline(handleEqComplexParameter(
                                scopeObject)));
                    }
                    return field(handleContainers(scopeObject) + " -> ?", val(scopeObject.getLeaf())).eq(field(applyQuotes(
                            handleEqComplexParameter(scopeObject))));
                }
                case CONTAINS -> {
                    if (scopeObject.getLeaf().equals(ITEM)) {
                        return condition(handleContainersForArray(scopeObject) + "::text like ?", val("%" + scopeObject
                                .getParameter() + "%"));
                    }
                    return condition(field(handleContainers(scopeObject) + " ->> ?", val(scopeObject.getLeaf())).like(
                            handleLikeComplexParameter(scopeObject)));
                }
                default -> throw TiesPathException.invalidQueryFunction();
            }
        }

        private String handleEqComplexParameter(final ScopeObject scopeObject) {
            if (scopeObject.getDataType().equals(DataType.INTEGER)) {
                return scopeObject.getParameter();
            }
            return String.valueOf(name(scopeObject.getParameter()));
        }

        private String handleLikeComplexParameter(final ScopeObject scopeObject) {
            if (scopeObject.getDataType().equals(DataType.INTEGER)) {
                return scopeObject.getParameter();
            }
            return String.format("%%%s%%", scopeObject.getParameter());
        }

        private String handleSimpleLeaf(final ScopeObject scopeObject) {
            return getPersistable(scopeObject).getTableName() + "." + name(scopeObject.getLeaf());
        }

        private String handleContainers(final ScopeObject scopeObject) {

            if (scopeObject.getInnerContainer().size() > 1) {
                StringBuilder sb = new StringBuilder();

                for (String element : scopeObject.getInnerContainer().subList(1, scopeObject.getInnerContainer().size())) {
                    sb.append(" -> ").append(applyQuotes(element));
                }
                return getPersistable(scopeObject).getTableName() + "." + name(scopeObject.getInnerContainer().get(0)) + sb;
            }
            return getPersistable(scopeObject).getTableName() + "." + name(scopeObject.getInnerContainer().get(0));
        }

        private String handleContainersForArray(final ScopeObject scopeObject) {

            if (scopeObject.getInnerContainer().size() > 1) {
                StringBuilder sb = new StringBuilder();

                for (String element : scopeObject.getInnerContainer().subList(1, scopeObject.getInnerContainer()
                        .size() - 1)) {
                    sb.append(" -> ").append(applyQuotes(element));
                }
                sb.append(" ->> ").append(applyQuotes(scopeObject.getInnerContainer().get(scopeObject.getInnerContainer()
                        .size() - 1)));
                return getPersistable(scopeObject).getTableName() + "." + name(scopeObject.getInnerContainer().get(0)) + sb;
            }
            return getPersistable(scopeObject).getTableName() + "." + name(scopeObject.getInnerContainer().get(0));
        }

        private static String applyQuotes(String element) {
            return String.format("'%s'", element);
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
                case EQ, CONTAINS, COVERED_BY, WITHIN_METERS -> createCondition(scopeObject);
                case NOT_NULL -> field(SchemaRegistry.getReferenceColumnName(getRelationType(scopeObject))).isNotNull();
            };
        }

        private Condition createCondition(final ScopeObject scopeObject) {
            if (scopeObject.getLeaf().equals("id")) {
                return createIdConditionForFilterSide(scopeObject);
            }
            return createAttributeConditionForFilterSide(scopeObject);
        }

        private Condition createIdConditionForFilterSide(final ScopeObject scopeObject) {
            RelationType relation = getRelationType(scopeObject);
            String associationName = scopeObject.getInnerContainer().get(0);
            EntityType filter = relation.getAssociationSide(associationName);
            ScopeObject modifiedScopeObject = ScopeObject.copy(scopeObject);
            modifiedScopeObject.setContainer(ContainerType.ID);
            modifiedScopeObject.setTopologyObject(filter.getName());
            modifiedScopeObject.setTopologyObjectType(TopologyObjectType.ENTITY);
            modifiedScopeObject.setDataType(DataType.PRIMITIVE);
            return ConditionFactory.create(modifiedScopeObject).getCondition(modifiedScopeObject);
        }

        private Condition createAttributeConditionForFilterSide(final ScopeObject scopeObject) {
            ScopeObject modifiedScopeObject = ScopeObject.copy(scopeObject);
            RelationType relation = getRelationType(scopeObject);
            EntityType filter = relation.getAssociationSide(scopeObject.getInnerContainer().get(0));
            modifiedScopeObject.setContainer(ContainerType.ATTRIBUTES);
            modifiedScopeObject.setTopologyObject(filter.getName());
            modifiedScopeObject.setInnerContainer(modifiedScopeObject.getInnerContainer().subList(1, modifiedScopeObject
                    .getInnerContainer().size()));
            modifiedScopeObject.setTopologyObjectType(TopologyObjectType.ENTITY);
            if (scopeObject.getDataType() == null) {
                throw TiesPathException.invalidQueryCondition("Data type was not found");
            }
            Condition condition = ConditionFactory.create(modifiedScopeObject).getCondition(modifiedScopeObject);
            if (filter.equals(relation.getStoringSideEntityType())) {
                condition = field(getTableNameWithColumnName(filter.getTableName(), relation
                        .getNotStoringSideEntityIdColumnNameInStoringSideTable())).isNotNull().and(condition);
            }
            return condition;
        }

        private static RelationType getRelationType(final ScopeObject scopeObject) {
            return switch (scopeObject.getTopologyObjectType()) {
                case ENTITY -> SchemaRegistry.getAllRelationNamesByAssociationName(scopeObject.getInnerContainer().get(0))
                        .stream().filter(r -> topologyObjectEquals(scopeObject, r.getASide()) || topologyObjectEquals(
                                scopeObject, r.getBSide())).findFirst().orElseThrow(() -> TiesPathException
                                        .invalidQueryCondition("Relation was not found"));
                case RELATION -> SchemaRegistry.getRelationTypeByName(scopeObject.getTopologyObject());
                default -> throw TiesPathException.invalidTopologyObjectType();
            };
        }

        private static boolean topologyObjectEquals(final ScopeObject scopeObject, final EntityType entityType) {
            return entityType.getName().equals(scopeObject.getTopologyObject());
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
