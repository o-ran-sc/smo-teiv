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

import static org.jooq.impl.DSL.field;
import static org.oran.smo.teiv.exposure.tiespath.refiner.AliasMapper.hashAlias;
import static org.oran.smo.teiv.schema.BidiDbNameMapper.getDbName;
import static org.oran.smo.teiv.schema.DataType.CONTAINER;
import static org.oran.smo.teiv.schema.DataType.GEOGRAPHIC;
import static org.oran.smo.teiv.utils.TiesConstants.ATTRIBUTES_ABBREVIATION;
import static org.oran.smo.teiv.utils.TiesConstants.CLASSIFIERS;
import static org.oran.smo.teiv.utils.TiesConstants.CONSUMER_DATA_PREFIX;
import static org.oran.smo.teiv.utils.TiesConstants.DECORATORS;
import static org.oran.smo.teiv.utils.TiesConstants.ID_COLUMN_NAME;
import static org.oran.smo.teiv.utils.TiesConstants.QUOTED_STRING;
import static org.oran.smo.teiv.utils.TiesConstants.REL_PREFIX;
import static org.oran.smo.teiv.utils.TiesConstants.ST_TO_STRING;
import static org.oran.smo.teiv.utils.TiesConstants.TIES_DATA;
import static org.oran.smo.teiv.utils.TiesConstants.SOURCE_IDS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Value;
import org.jooq.Field;
import org.jooq.JSONB;

import lombok.Builder;
import lombok.EqualsAndHashCode;

import org.oran.smo.teiv.exposure.spi.Module;

@Value
@Builder
public class EntityType implements Persistable {
    @EqualsAndHashCode.Include
    String name;
    Map<String, DataType> fields;
    String tableName;
    Module module;

    @Override
    public String getTableName() {
        return String.format(TIES_DATA, getDbName(this.tableName));
    }

    @Override
    public String getIdColumnName() {
        return ID_COLUMN_NAME;
    }

    @Override
    public String getIdColumnNameWithTableName() {
        return getTableName() + "." + String.format(QUOTED_STRING, ID_COLUMN_NAME);
    }

    @Override
    @SuppressWarnings("java:S5738")
    public Map<Field, DataType> getSpecificAttributeColumns(List<String> attributes) {
        final Map<Field, DataType> fieldList = new HashMap<>();
        this.fields.forEach((fieldName, dataType) -> {
            if (!attributes.contains(fieldName) && !attributes.isEmpty()) {
                return;
            }
            if (fieldName.startsWith(REL_PREFIX) || fieldName.startsWith(CONSUMER_DATA_PREFIX) || fieldName.equals(
                    ID_COLUMN_NAME)) {
                return;
            }
            if (GEOGRAPHIC.equals(dataType)) {
                fieldList.put(field(String.format(ST_TO_STRING, getTableName() + "." + String.format(QUOTED_STRING,
                        getDbName(fieldName)))).as(hashAlias(
                                getFullyQualifiedName() + ATTRIBUTES_ABBREVIATION + fieldName)), dataType);
            } else if (CONTAINER.equals(dataType)) {
                fieldList.put(field(getTableName() + "." + String.format(QUOTED_STRING, getDbName(fieldName)), JSONB.class)
                        .as(hashAlias(getFullyQualifiedName() + ATTRIBUTES_ABBREVIATION + fieldName)), dataType);
            } else {
                fieldList.put(field(getTableName() + "." + String.format(QUOTED_STRING, getDbName(fieldName))).as(hashAlias(
                        getFullyQualifiedName() + ATTRIBUTES_ABBREVIATION + fieldName)), dataType);
            }
        });
        return fieldList;
    }

    public List<String> getAttributeNames() {
        return this.fields.keySet().stream().filter(field -> (!field.startsWith(REL_PREFIX) && !field.startsWith(
                CONSUMER_DATA_PREFIX) && !field.equals(ID_COLUMN_NAME))).toList();
    }

    /**
     * Gets the fully qualified name of the entity. Format - <moduleNameReference>:<entityName>
     *
     * @return the fully qualified name
     */
    @Override
    public String getFullyQualifiedName() {
        return String.format("%s:%s", module.getName(), name);
    }

    @Override
    public String getSourceIdsColumnName() {
        return getDbName(CONSUMER_DATA_PREFIX + SOURCE_IDS);
    }

    @Override
    public String getClassifiersColumnName() {
        return getDbName(CONSUMER_DATA_PREFIX + CLASSIFIERS);
    }

    @Override
    public String getDecoratorsColumnName() {
        return getDbName(CONSUMER_DATA_PREFIX + DECORATORS);
    }

    @Override
    public String getCategory() {
        return "entity";
    }
}
