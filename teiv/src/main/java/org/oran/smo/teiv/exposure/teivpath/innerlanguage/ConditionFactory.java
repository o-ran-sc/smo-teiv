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

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.val;
import static org.oran.smo.teiv.exposure.teivpath.innerlanguage.QueryFunction.EQ;
import static org.oran.smo.teiv.exposure.teivpath.innerlanguage.QueryFunction.GREATER_THAN;
import static org.oran.smo.teiv.exposure.teivpath.innerlanguage.QueryFunction.GREATER_THAN_OR_EQ;
import static org.oran.smo.teiv.exposure.teivpath.innerlanguage.QueryFunction.LESS_THAN;
import static org.oran.smo.teiv.exposure.teivpath.innerlanguage.QueryFunction.LESS_THAN_OR_EQ;
import static org.oran.smo.teiv.utils.PersistableUtil.getTableNameWithColumnName;
import static org.oran.smo.teiv.utils.TeivConstants.ITEM;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.JSONB;

import lombok.experimental.UtilityClass;
import org.oran.smo.teiv.schema.DataType;
import org.oran.smo.teiv.schema.EntityType;
import org.oran.smo.teiv.schema.Persistable;
import org.oran.smo.teiv.schema.RelationType;
import org.oran.smo.teiv.schema.RelationshipDataLocation;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.utils.query.exception.TeivPathException;

@Slf4j
@UtilityClass
public class ConditionFactory {
    private static final String INVALID_TOPOLOGY_OBJECT_TYPE = "Invalid topology object type";
    private static final String INVALID_QUERY_FUNCTION = "Invalid query function";
    private final String sharedRegex = "-?[\\d]+([.][\\d]+)? -?[\\d]+([.][\\d]+)?";
    private final String pointRegex = "'(POINT|point) ?[(]" + sharedRegex + "[)]'";
    private final String polygonRegex = "(POLYGON|polygon) ?[(][(](?:" + sharedRegex + ", ?){3,}(" + sharedRegex + ")[)][)]";
    private final String multiPolygonRegex = "(MULTIPOLYGON|multipolygon) ?[(]([(][(](?:" + sharedRegex + ", ?){3,}(" + sharedRegex + ")[)][)],){1,}[(][(](?:" + sharedRegex + ", ?){3,}(" + sharedRegex + ")[)][)][)]";

    public static AnyCondition create(final ScopeObject scopeObject) {
        return switch (scopeObject.getContainer()) {
            case ATTRIBUTES -> new AttributesCondition();
            case RELATION -> new RelationCondition();
            case ID -> switch (scopeObject.getTopologyObjectType()) {
                    case ASSOCIATION -> new AssociationCondition();
                    default -> new IdCondition();
                };
            case NOT_NULL -> new AssociationCondition();
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
                case TIMESTAMPTZ -> {
                    try {
                        return OffsetDateTime.parse(scopeObject.getParameter());
                    } catch (Exception e) {
                        log.error("Error while parsing timestamp in scope filter:", e);
                        throw TeivPathException.invalidQueryCondition("Invalid timestamp: " + scopeObject.getParameter());
                    }
                }
                default -> throw TeivPathException.invalidQueryCondition("Unexpected value: " + scopeObject.getDataType());
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
                case ASSOCIATION -> {
                    String[] topologyObjectTypeSplit = scopeObject.getTopologyObject().split("/");
                    List<RelationType> relationTypes = SchemaRegistry.getAllRelationNamesByAssociationName(
                            topologyObjectTypeSplit[1]);
                    RelationType relationType = relationTypes.stream().filter(rel -> rel.getASide().getName().equals(
                            topologyObjectTypeSplit[0]) || rel.getBSide().getName().equals(topologyObjectTypeSplit[0]))
                            .findFirst().orElseThrow(() -> TeivPathException.grammarError("invalid relation"));
                    return relationType.getASide().getName().equals(topologyObjectTypeSplit[0]) ?
                            relationType.getBSide() :
                            relationType.getASide();
                }
                default -> throw TeivPathException.invalidQueryCondition(INVALID_TOPOLOGY_OBJECT_TYPE);
            }
        }

        private static void validateGeographicPoint(final String point) {
            final boolean is2D = point.matches("(?i)POINT\\([-?\\d]+(\\.\\d+)? -?\\d+(\\.\\d+)?\\)");
            final boolean is3D = point.matches("(?i)POINT Z \\([-?\\d]+(\\.\\d+)? -?\\d+(\\.\\d+)? -?\\d+(\\.\\d+)?\\)");

            if (!is2D && !is3D) {
                throw TeivPathException.invalidQueryCondition(
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
                        throw TeivPathException.invalidQueryCondition("Cannot have CONTAINS on geography type data");
                    }
                    return field(handleSimpleLeaf(scopeObject)).contains(convert(scopeObject));
                }
                case WITHIN_METERS -> {
                    if (!scopeObject.getDataType().equals(DataType.GEOGRAPHIC)) {
                        throw TeivPathException.invalidQueryCondition("Within meters condition needs geography type data");
                    }
                    String[] parameters = scopeObject.getParameter().split(",");
                    if (parameters.length != 2 || !parameters[0].matches(pointRegex)) {
                        throw TeivPathException.invalidQueryCondition("Invalid parameter for within meters condition");
                    }
                    return condition("ST_DWithin(?, ST_GeographyFromText(?), ?)", field("\"" + scopeObject
                            .getLeaf() + "\""), field(parameters[0]), field(parameters[1].replaceAll(" ", "")));
                }
                case COVERED_BY -> {
                    if (!scopeObject.getDataType().equals(DataType.GEOGRAPHIC)) {
                        throw TeivPathException.invalidQueryCondition("Covered by condition needs geography type data");
                    }
                    if (!scopeObject.getParameter().matches(polygonRegex) && !scopeObject.getParameter().matches(
                            multiPolygonRegex)) {
                        throw TeivPathException.invalidQueryCondition("Invalid parameter for covered by condition");
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
                default -> throw TeivPathException.invalidQueryCondition(INVALID_QUERY_FUNCTION);
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
            return handleTopologyObjectType(scopeObject).getTableName() + "." + name(scopeObject.getLeaf());
        }

        private String handleContainers(final ScopeObject scopeObject) {
            if (scopeObject.getInnerContainer().size() > 1) {
                StringBuilder sb = new StringBuilder();
                for (String element : scopeObject.getInnerContainer().subList(1, scopeObject.getInnerContainer().size())) {
                    sb.append(" -> ").append(applyQuotes(element));
                }
                return handleTopologyObjectType(scopeObject).getTableName() + "." + name(scopeObject.getInnerContainer()
                        .get(0)) + sb;
            }
            return handleTopologyObjectType(scopeObject).getTableName() + "." + name(scopeObject.getInnerContainer().get(
                    0));
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
                return handleTopologyObjectType(scopeObject).getTableName() + "." + name(scopeObject.getInnerContainer()
                        .get(0)) + sb;
            }
            return handleTopologyObjectType(scopeObject).getTableName() + "." + name(scopeObject.getInnerContainer().get(
                    0));
        }

        private Persistable handleTopologyObjectType(final ScopeObject scopeObject) {
            Persistable persistable;
            switch (scopeObject.getTopologyObjectType()) {
                case ENTITY -> persistable = SchemaRegistry.getEntityTypeByName(scopeObject.getTopologyObject());
                case RELATION -> persistable = SchemaRegistry.getRelationTypeByName(scopeObject.getTopologyObject());
                case ASSOCIATION -> {
                    String[] topologyObjectSplit = scopeObject.getTopologyObject().split("/");
                    List<RelationType> relationTypes = SchemaRegistry.getAllRelationNamesByAssociationName(
                            topologyObjectSplit[1]);
                    RelationType relation = relationTypes.stream().filter(r -> r.getASide().getName().equals(
                            topologyObjectSplit[0]) || r.getBSide().getName().equals(topologyObjectSplit[0])).findFirst()
                            .orElseThrow(() -> TeivPathException.invalidQueryCondition("Relation was not found"));
                    persistable = relation.getASideAssociation().getName().equals(topologyObjectSplit[1]) ?
                            relation.getBSide() :
                            relation.getASide();
                }
                default -> throw TeivPathException.invalidQueryCondition(INVALID_TOPOLOGY_OBJECT_TYPE);
            }
            return persistable;
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
                default -> throw TeivPathException.invalidQueryCondition(INVALID_QUERY_FUNCTION);
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
                        .getInnerContainer().get(0))).findFirst().orElseThrow(() -> TeivPathException.invalidQueryCondition(
                                "Relationship was not found for topology object"));
                final Persistable persistable = SchemaRegistry.getRelationTypeByName(relationName);
                return Objects.requireNonNull(persistable).getTableName() + "." + name(persistable.getIdColumnName());
            } else {
                throw TeivPathException.invalidQueryCondition(INVALID_TOPOLOGY_OBJECT_TYPE);
            }
        }

        private static Condition notNullCondition(final ScopeObject scopeObject) {
            return field(getColumnName(scopeObject)).isNotNull();
        }
    }

    static class AssociationCondition extends AnyCondition {
        @Override
        public Condition getCondition(final ScopeObject scopeObject) {
            RelationType relation;
            switch (scopeObject.getTopologyObjectType()) {
                case ENTITY -> {
                    final List<RelationType> relationTypes = SchemaRegistry.getAllRelationNamesByAssociationName(scopeObject
                            .getInnerContainer().get(0));
                    relation = relationTypes.stream().filter(r -> r.getASide().getName().equals(scopeObject
                            .getTopologyObject()) || r.getBSide().getName().equals(scopeObject.getTopologyObject()))
                            .findFirst().orElseThrow(() -> TeivPathException.invalidQueryCondition(
                                    "Relation was not found"));
                }
                case RELATION -> relation = SchemaRegistry.getRelationTypeByName(scopeObject.getTopologyObject());
                case ASSOCIATION -> {
                    String[] topologyObjectSplit = scopeObject.getTopologyObject().split("/");
                    List<RelationType> relationTypes = SchemaRegistry.getAllRelationNamesByAssociationName(
                            topologyObjectSplit[1]);
                    relation = relationTypes.stream().filter(r -> r.getASide().getName().equals(topologyObjectSplit[0]) || r
                            .getBSide().getName().equals(topologyObjectSplit[0]) || r.getName().equals(
                                    topologyObjectSplit[0])).findFirst().orElseThrow(() -> TeivPathException
                                            .invalidQueryCondition("Relation was not found"));
                }
                default -> throw TeivPathException.invalidQueryCondition(INVALID_TOPOLOGY_OBJECT_TYPE);
            }
            switch (scopeObject.getQueryFunction()) {
                case EQ, CONTAINS -> {
                    EntityType entityType;
                    String sideColumnName;
                    if (Objects.requireNonNull(relation).getRelationshipStorageLocation().equals(
                            RelationshipDataLocation.A_SIDE)) {
                        entityType = relation.getASide();
                        sideColumnName = relation.bSideColumnName();
                    } else if (relation.getRelationshipStorageLocation().equals(RelationshipDataLocation.B_SIDE)) {
                        entityType = relation.getBSide();
                        sideColumnName = relation.aSideColumnName();
                    } else {
                        if (relation.isConnectsSameEntity()) {
                            sideColumnName = checkSameEntityRelationship(relation, scopeObject);
                        } else {
                            sideColumnName = checkManyToMany(relation, scopeObject);
                        }
                        if (scopeObject.getQueryFunction().equals(EQ)) {
                            return condition(field(getTableNameWithColumnName(relation.getTableName(), sideColumnName)).eq(
                                    scopeObject.getParameter()));
                        }
                        return condition(field(getTableNameWithColumnName(relation.getTableName(), sideColumnName))
                                .contains(scopeObject.getParameter()));
                    }
                    return ConditionFactory.getConditionContainsOrEquals(scopeObject, relation, entityType, sideColumnName);
                }
                case NOT_NULL -> {
                    return condition(field(SchemaRegistry.getReferenceColumnName(Objects.requireNonNull(relation)))
                            .isNotNull());
                }
                default -> throw TeivPathException.invalidQueryCondition(INVALID_QUERY_FUNCTION);
            }
        }

        private static String checkManyToMany(RelationType relation, ScopeObject scopeObject) {
            if (relation.getASide().getName().equals(scopeObject.getTopologyObject().split("/")[0]) || SchemaRegistry
                    .getEntityTypeOnAssociationSide(relation, scopeObject.getTopologyObject().split("/")[1]).getName()
                    .equals(relation.getASide().getName())) {
                return relation.bSideColumnName();
            } else {
                return relation.aSideColumnName();
            }
        }

        private static String checkSameEntityRelationship(RelationType relation, ScopeObject scopeObject) {
            if (relation.getASideAssociation().getName().equals(scopeObject.getTopologyObject().split("/")[1])) {
                return relation.aSideColumnName();
            } else {
                return relation.bSideColumnName();
            }
        }
    }

    static class MetaDataMapCondition extends AnyCondition {
        @Override
        public Condition getCondition(final ScopeObject scopeObject) {
            if (scopeObject.getQueryFunction().equals(LESS_THAN)) {
                return lessThanCondition(scopeObject);
            }
            if (scopeObject.getQueryFunction().equals(GREATER_THAN)) {
                return greaterThanCondition(scopeObject);
            }
            if (scopeObject.getQueryFunction().equals(LESS_THAN_OR_EQ)) {
                return lessThanOrEqualsCondition(scopeObject);
            }
            if (scopeObject.getQueryFunction().equals(GREATER_THAN_OR_EQ)) {
                return greaterThanOrEqualsCondition(scopeObject);
            }
            if (scopeObject.getQueryFunction().equals(EQ)) {
                return equalsCondition(scopeObject);
            } else {
                throw TeivPathException.invalidQueryCondition(INVALID_QUERY_FUNCTION);
            }
        }

        private static String getColumnName(final ScopeObject scopeObject) {
            final Persistable persistable = getPersistable(scopeObject);
            return Objects.requireNonNull(persistable).getTableName() + "." + name(persistable.getMetadataColumnName());
        }

        private static Condition equalsCondition(final ScopeObject scopeObject) {
            if (scopeObject.getDataType().equals(DataType.TIMESTAMPTZ)) {
                return condition("(? ->> ?)::timestamptz = ?", field(getColumnName(scopeObject)), inline(scopeObject
                        .getLeaf()), inline(convert(scopeObject)));
            }
            return condition("? -> ? = ?", field(getColumnName(scopeObject)), inline(scopeObject.getLeaf()), inline(
                    "\"" + convert(scopeObject) + "\""));
        }

        private static Condition lessThanCondition(final ScopeObject scopeObject) {
            return condition("(? ->> ?)::timestamptz < ?", field(getColumnName(scopeObject)), inline(scopeObject.getLeaf()),
                    inline(convert(scopeObject)));
        }

        private static Condition lessThanOrEqualsCondition(final ScopeObject scopeObject) {
            return condition("(? ->> ?)::timestamptz <= ?", field(getColumnName(scopeObject)), inline(scopeObject
                    .getLeaf()), inline(convert(scopeObject)));
        }

        private static Condition greaterThanCondition(final ScopeObject scopeObject) {
            return condition("(? ->> ?)::timestamptz > ?", field(getColumnName(scopeObject)), inline(scopeObject.getLeaf()),
                    inline(convert(scopeObject)));
        }

        private static Condition greaterThanOrEqualsCondition(final ScopeObject scopeObject) {
            return condition("(? ->> ?)::timestamptz >= ?", field(getColumnName(scopeObject)), inline(scopeObject
                    .getLeaf()), inline(convert(scopeObject)));
        }
    }

    private static Condition getConditionContainsOrEquals(ScopeObject scopeObject, RelationType relation,
            EntityType entityType, String sideColumnName) {
        if (scopeObject.getTopologyObject().split("/")[0].equals(entityType.getName()) || SchemaRegistry
                .getEntityTypeOnAssociationSide(relation, scopeObject.getTopologyObject().split("/")[1]).getName().equals(
                        entityType.getName())) {
            Field<Object> field = field(getTableNameWithColumnName(Objects.requireNonNull(entityType).getTableName(),
                    sideColumnName));
            if (scopeObject.getQueryFunction().equals(EQ)) {
                return condition(field.eq(scopeObject.getParameter()));
            }
            return condition(field.contains(scopeObject.getParameter()));
        } else {
            if (scopeObject.getQueryFunction().equals(EQ)) {
                return condition(field(getTableNameWithColumnName(entityType.getTableName(), sideColumnName)).isNotNull())
                        .and(condition(field(getTableNameWithColumnName(entityType.getTableName(), entityType
                                .getIdColumnName())).eq(scopeObject.getParameter())));
            }
            return condition(field(getTableNameWithColumnName(entityType.getTableName(), sideColumnName)).isNotNull()).and(
                    condition(field(getTableNameWithColumnName(entityType.getTableName(), entityType.getIdColumnName()))
                            .contains(scopeObject.getParameter())));
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
            if (scopeObject.getDataType().equals(DataType.PRIMITIVE)) {
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
                default -> throw TeivPathException.invalidQueryCondition(INVALID_QUERY_FUNCTION);
            }
        }

        private static String getColumnName(final ScopeObject scopeObject) {
            final Persistable persistable = getPersistable(scopeObject);
            return persistable.getIdColumnNameWithTableName();
        }
    }
}
