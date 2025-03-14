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
package org.oran.smo.teiv.schema;

import static org.oran.smo.teiv.exposure.teivpath.refiner.AliasMapper.hashAlias;
import static org.oran.smo.teiv.schema.BidiDbNameMapper.getDbName;
import static org.oran.smo.teiv.schema.RelationshipDataLocation.RELATION;
import static org.jooq.impl.DSL.field;
import static org.oran.smo.teiv.utils.TeivConstants.CLASSIFIERS;
import static org.oran.smo.teiv.utils.TeivConstants.CONSUMER_DATA_PREFIX;
import static org.oran.smo.teiv.utils.TeivConstants.DECORATORS;
import static org.oran.smo.teiv.utils.TeivConstants.ID_COLUMN_NAME;
import static org.oran.smo.teiv.utils.TeivConstants.METADATA;
import static org.oran.smo.teiv.utils.TeivConstants.QUOTED_STRING;
import static org.oran.smo.teiv.utils.TeivConstants.SOURCE_IDS;
import static org.oran.smo.teiv.utils.TeivConstants.TEIV_DATA;
import org.oran.smo.teiv.exposure.spi.Module;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Value;
import org.jooq.Field;

@Value
@Builder
public class RelationType implements Persistable {
    private static final String REL_ID_COL_PREFIX = "REL_ID_%s";
    private static final String REL_FK_COL_PREFIX = "REL_FK_%s";
    private static final String A_SIDE_PREFIX = "aSide_%s";
    private static final String B_SIDE_PREFIX = "bSide_%s";
    private static final String REL_SOURCE_IDS_COL_PREFIX = "REL_CD_sourceIds_%s";
    private static final String REL_CLASSIFIERS_COL_PREFIX = "REL_CD_classifiers_%s";
    private static final String REL_DECORATORS_COL_PREFIX = "REL_CD_decorators_%s";
    private static final String REL_METADATA_COL_PREFIX = "REL_metadata_%s";

    String name;
    Association aSideAssociation;
    EntityType aSide;
    Association bSideAssociation;
    EntityType bSide;
    boolean connectsSameEntity;
    RelationshipDataLocation relationshipStorageLocation;
    String tableName;
    Module module;
    List<String> attributeNames;

    @Override
    public String getTableName() {
        return String.format(TEIV_DATA, getDbName(this.tableName));
    }

    @Override
    public String getIdColumnName() {
        if (relationshipStorageLocation.equals(RELATION)) {
            return getDbName(ID_COLUMN_NAME);
        } else {
            return getDbName(String.format(REL_ID_COL_PREFIX, name));
        }
    }

    @Override
    public String getIdColumnNameWithTableName() {
        if (relationshipStorageLocation.equals(RELATION)) {
            return getTableName() + "." + String.format(QUOTED_STRING, ID_COLUMN_NAME);
        } else {
            return getTableName() + "." + String.format(QUOTED_STRING, getDbName(String.format(REL_ID_COL_PREFIX, name)));
        }
    }

    @Override
    public Map<Field, DataType> getSpecificAttributeColumns(List<String> attributes) {
        // attributes are yet to be supported for relations
        return Map.of();
    }

    @Override
    public List<Field> getAllFieldsWithId() {
        List<Field> result = Persistable.super.getAllFieldsWithId();
        result.add(field(getTableName() + "." + String.format(QUOTED_STRING, aSideColumnName())).as(hashAlias(
                getFullyQualifiedName() + ".aSide")));
        result.add(field(getTableName() + "." + String.format(QUOTED_STRING, bSideColumnName())).as(hashAlias(
                getFullyQualifiedName() + ".bSide")));
        return result;
    }

    /**
     * Gets id, aSide and bSide column name of the relation.
     *
     * @return the list of {@link Field}
     */
    public List<Field> getBaseFieldsWithId() {
        return List.of(field(getTableName() + "." + String.format(QUOTED_STRING, aSideColumnName())), field(
                getTableName() + "." + String.format(QUOTED_STRING, bSideColumnName())), field(getTableName() + "." + String
                        .format(QUOTED_STRING, getIdColumnName())));
    }

    @Override
    public String getSourceIdsColumnName() {
        if (relationshipStorageLocation.equals(RELATION)) {
            return getDbName(CONSUMER_DATA_PREFIX + SOURCE_IDS);
        } else {
            return getDbName(String.format(REL_SOURCE_IDS_COL_PREFIX, name));
        }
    }

    @Override
    public String getClassifiersColumnName() {
        if (relationshipStorageLocation.equals(RELATION)) {
            return getDbName(CONSUMER_DATA_PREFIX + CLASSIFIERS);
        } else {
            return getDbName(String.format(REL_CLASSIFIERS_COL_PREFIX, name));
        }
    }

    @Override
    public String getDecoratorsColumnName() {
        if (relationshipStorageLocation.equals(RELATION)) {
            return getDbName(CONSUMER_DATA_PREFIX + DECORATORS);
        } else {
            return getDbName(String.format(REL_DECORATORS_COL_PREFIX, name));
        }
    }

    @Override
    public String getMetadataColumnName() {
        if (relationshipStorageLocation.equals(RELATION)) {
            return getDbName(METADATA);
        } else {
            return getDbName(String.format(REL_METADATA_COL_PREFIX, name));
        }
    }

    /**
     * Gets the aSide column name of the relation.
     *
     * @return the aSide column name
     */
    public String aSideColumnName() {
        return switch (relationshipStorageLocation) {
            case RELATION -> getDbName(String.format(A_SIDE_PREFIX, aSide.getName()));
            case A_SIDE -> ID_COLUMN_NAME;
            case B_SIDE -> getDbName(String.format(REL_FK_COL_PREFIX, getBSideAssociation().getName()));
        };
    }

    /**
     * Gets the bSide column name of the relation.
     *
     * @return the bSide column name
     */
    public String bSideColumnName() {
        return switch (relationshipStorageLocation) {
            case RELATION -> getDbName(String.format(B_SIDE_PREFIX, bSide.getName()));
            case A_SIDE -> getDbName(String.format(REL_FK_COL_PREFIX, getASideAssociation().getName()));
            case B_SIDE -> ID_COLUMN_NAME;
        };
    }

    @Override
    public String getFullyQualifiedName() {
        return String.format("%s:%s", module.getName(), name);
    }

    public String getNotStoringSideTableName() {
        return switch (relationshipStorageLocation) {
            case RELATION -> null;
            case A_SIDE -> bSide.getTableName();
            case B_SIDE -> aSide.getTableName();
        };
    }

    public String getNotStoringSideEntityIdColumnNameInStoringSideTable() {
        return switch (relationshipStorageLocation) {
            case RELATION -> null;
            case A_SIDE -> this.bSideColumnName();
            case B_SIDE -> this.aSideColumnName();
        };
    }

    public EntityType getStoringSideEntityType() {
        return switch (relationshipStorageLocation) {
            case RELATION -> null;
            case A_SIDE -> aSide;
            case B_SIDE -> bSide;
        };
    }

    public EntityType getNotStoringSideEntityType() {
        return switch (relationshipStorageLocation) {
            case RELATION -> null;
            case A_SIDE -> bSide;
            case B_SIDE -> aSide;
        };
    }

    @Override
    public String getCategory() {
        return "relationship";
    }
}
