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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jooq.Field;
import org.jooq.JSONB;

import static org.jooq.impl.DSL.field;
import static org.oran.smo.teiv.exposure.tiespath.refiner.AliasMapper.hashAlias;
import static org.oran.smo.teiv.utils.PersistableUtil.getFullyQualifiedNameWithColumnName;
import static org.oran.smo.teiv.utils.TiesConstants.*;

public interface Persistable {

    /**
     * Gets the fully qualified table name.
     * Format - <schemaName>."<tableName>"
     *
     * @return the table name
     */
    String getTableName();

    /**
     * Gets the column name used for storing the unique identifier.
     *
     * @return the id column name
     */
    String getIdColumnName();

    /**
     * Gets the column name used for storing the unique identifier.
     *
     * @return the id column name with table name
     */
    String getIdColumnNameWithTableName();

    /**
     * Gets specific columns.
     *
     * @param attributes
     *     specific attributes names
     *
     * @return the list of columns
     */
    Map<Field, DataType> getSpecificAttributeColumns(List<String> attributes);

    /**
     * Gets all the columns names as list of {@link Field}
     *
     *
     * @return the list of {@link Field}
     */
    default List<Field> getAllFieldsWithId() {
        List<Field> result = new ArrayList<>(getSpecificAttributeColumns(List.of()).keySet());
        String fullyQuelifiedName = getFullyQualifiedName();
        result.add(field(getTableName() + "." + String.format(QUOTED_STRING, getIdColumnName())).as(hashAlias(
                getFullyQualifiedNameWithColumnName(fullyQuelifiedName, ID_COLUMN_NAME))));
        result.add(field(getTableName() + "." + String.format(QUOTED_STRING, getSourceIdsColumnName()), JSONB.class).as(
                hashAlias(getFullyQualifiedNameWithColumnName(fullyQuelifiedName, SOURCE_IDS))));
        result.add(field(getTableName() + "." + String.format(QUOTED_STRING, getClassifiersColumnName()), JSONB.class).as(
                hashAlias(getFullyQualifiedNameWithColumnName(fullyQuelifiedName, CLASSIFIERS))));
        result.add(field(getTableName() + "." + String.format(QUOTED_STRING, getDecoratorsColumnName()), JSONB.class).as(
                hashAlias(getFullyQualifiedNameWithColumnName(fullyQuelifiedName, DECORATORS))));
        return result;
    }

    List<String> getAttributeNames();

    /**
     * Gets sourceIds column name as String
     *
     * @return the String value of sourceIds column
     */
    String getSourceIdsColumnName();

    /**
     * Gets classifiers column name as String
     *
     * @return the String value of classifiers column
     */
    String getClassifiersColumnName();

    /**
     * Gets decorators column name as String
     *
     * @return the String value of decorators column
     */
    String getDecoratorsColumnName();

    String getName();

    /**
     * Gets the fully qualified name of the entity. Format - <moduleNameReference>:<entityName>
     *
     * @return the fully qualified name
     */
    String getFullyQualifiedName();

    /**
     * Gets the category of the persistable. e.g.: "relationship", "entity"
     *
     * @return category as a String
     */
    String getCategory();
}
