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
package org.oran.smo.teiv.exposure.data.api;

import org.oran.smo.teiv.api.model.OranTeivDomains;
import org.oran.smo.teiv.api.model.OranTeivEntities;
import org.oran.smo.teiv.api.model.OranTeivEntityTypes;
import org.oran.smo.teiv.api.model.OranTeivRelationshipTypes;
import org.oran.smo.teiv.api.model.OranTeivRelationships;
import org.oran.smo.teiv.exposure.utils.RequestDetails;

public interface DataService {

    /**
     * Get all the available topology domain types.
     *
     * @param requestDetails
     *     - request details
     *
     * @return a collection of domain types
     */
    OranTeivDomains getDomainTypes(final RequestDetails requestDetails);

    /**
     * Get all entity types in a topology domain types.
     *
     * @param domain
     *     domain name
     * @param requestDetails
     *     - request details
     *
     * @return a collection of domain types
     */
    OranTeivEntityTypes getTopologyEntityTypes(final String domain, final RequestDetails requestDetails);

    /**
     * Get all relationship types in a topology domain types.
     *
     * @param domain
     *     domain name
     ** @param requestDetails
     *     - request details
     *
     * @return a collection of relationship types
     */
    OranTeivRelationshipTypes getTopologyRelationshipTypes(final String domain, final RequestDetails requestDetails);

    /**
     * Get topology for entity type with specified id.
     *
     * @param entityType
     *     type of entity, e.g. NRCellDU
     * @param id
     *     unique identifier of entity
     *
     * @return a topology entity
     */
    Object getEntityById(final String entityType, final String id);

    /**
     * Get topology entities that match target, scope and relationships filters.
     *
     * @param entityType
     *     type of entity, e.g. NRCellDU
     * @param targetFilter
     *     specifies the entity type and attributes to be returned to the REST response
     * @param scopeFilter
     *     specifies the attributes to match on
     * @param requestDetails
     *     - request details
     *
     * @return a collection of entity data and attributes
     */
    OranTeivEntities getTopologyByType(final String domain, final String entityType, final String targetFilter,
            final String scopeFilter, final RequestDetails requestDetails);

    /**
     *
     * @param domain
     *     domain name
     * @param targetFilter
     *     specifies the entity type and attributes to be returned to the REST response
     * @param scopeFilter
     *     specifies the attributes to match on
     * @param requestDetails
     *     - request details
     *
     * @return a collection of entity data and attributes
     */
    OranTeivEntities getEntitiesByDomain(final String domain, final String targetFilter, final String scopeFilter,
            final RequestDetails requestDetails);

    /**
     * Get all relationships for entityType with specified id
     *
     * @param domain
     *     domain name
     * @param entityType
     *     type of entity, e.g. NRCellDU
     * @param id
     *     unique identifier of entity
     * @param targetFilter
     *     specifies the entity type and attributes to be returned to the REST response
     * @param scopeFilter
     *     specifies the attributes to match on
     * @param requestDetails
     *     - request details
     *
     * @return a collection of relationships for entity type
     */
    OranTeivRelationships getAllRelationshipsForObjectId(final String domain, final String entityType, final String id,
            final String targetFilter, final String scopeFilter, final RequestDetails requestDetails);

    /**
     * Get relationship with specified id
     *
     * @param relationshipType
     *     type of relationship
     * @param id
     *     unique identifier of the relationships
     *
     * @return a topology relationship
     */
    Object getRelationshipById(final String relationshipType, final String id);

    /**
     *
     * @param relationshipType
     *     type of relationship
     * @param scopeFilter
     *     specifies the attributes to match on
     * @param requestDetails
     *     - request details
     *
     * @return relationships by relationship type
     */
    OranTeivRelationships getRelationshipsByType(final String domain, final String relationshipType,
            final String targetFilter, final String scopeFilter, final RequestDetails requestDetails);
}
