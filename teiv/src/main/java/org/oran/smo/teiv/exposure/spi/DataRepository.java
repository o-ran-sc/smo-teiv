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
package org.oran.smo.teiv.exposure.spi;

import java.util.List;
import java.util.Set;

import org.jooq.Record;
import org.jooq.Result;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.FilterCriteria;
import org.oran.smo.teiv.schema.EntityType;
import org.oran.smo.teiv.schema.Persistable;
import org.oran.smo.teiv.schema.RelationType;

public interface DataRepository {
    /**
     * Gets topology.
     *
     * @param entityType
     *     the entityType
     * @param id
     *     the id
     * @return the topology
     */
    Result<Record> getEntityById(final EntityType entityType, final String id);

    /**
     * Gets relationship by id
     *
     * @param id
     *     the relationType id
     * @param relationType
     *     the relationType
     * @return relationship
     */
    Result<Record> getRelationshipById(final String id, final RelationType relationType);

    /**
     * Get topology objects by a filter criteria
     *
     * @param filterCriteria
     *     the filter criteria
     * @param limit
     *     the limit
     * @param offset
     *     the offset
     *
     * @return the all topology objects of type that match the filter criteria
     */
    Result<Record> getTopologyByFilterCriteria(final FilterCriteria filterCriteria, final int limit, final int offset);

    /**
     * Gets classifiers for schema.
     *
     * @param schemaName
     *     the schema name
     * @return the classifiers for schema
     */
    Set<String> getClassifiersForSchema(final String schemaName);

    /**
     * Gets decorators for schema.
     *
     * @param schemaName
     *     the schema name
     * @return the decorators for schema
     */
    Set<String> getDecoratorsForSchema(final String schemaName);

    /**
     * Gets relationship ids for decorator deletion.
     *
     * @param relationType
     *     the relation type
     * @param decorators
     *     the decorators
     * @return the relationship ids for decorator deletion
     */
    List<String> getRelationshipIdsForDecoratorDeletion(final RelationType relationType, final Set<String> decorators);

    /**
     * Gets relationship ids for classifier deletion.
     *
     * @param relationType
     *     the relation type
     * @param classifiers
     *     the classifiers
     * @return the relationship ids for classifier deletion
     */
    List<String> getRelationshipIdsForClassifierDeletion(final RelationType relationType, final Set<String> classifiers);

    /**
     * Gets entity ids for decorator deletion.
     *
     * @param entityType
     *     the entity type
     * @param decorators
     *     the decorators
     * @return the entity ids for decorator deletion
     */
    List<String> getEntityIdsForDecoratorDeletion(final EntityType entityType, final Set<String> decorators);

    /**
     * Gets entity ids for classifier deletion.
     *
     * @param entityType
     *     the entity type
     * @param classifiers
     *     the classifiers
     * @return the entity ids for classifier deletion
     */
    List<String> getEntityIdsForClassifierDeletion(final EntityType entityType, final Set<String> classifiers);

    /**
     * Check if a topology object - entity or relationship - exists with given id and type.
     *
     * @param type
     *     The topology type
     * @param id
     *     The id
     * @return true if exists, false if it does not
     */
    boolean doesTopologyExist(final Persistable type, final String id);
}
