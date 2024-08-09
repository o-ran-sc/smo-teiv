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
package org.oran.smo.teiv.exposure.data.rest.controller;

import java.util.function.Supplier;

import org.oran.smo.teiv.CustomMetrics;
import org.oran.smo.teiv.api.EntitiesAndRelationshipsApi;
import org.oran.smo.teiv.api.model.OranTeivDomains;
import org.oran.smo.teiv.api.model.OranTeivEntitiesResponseMessage;
import org.oran.smo.teiv.api.model.OranTeivEntityTypes;
import org.oran.smo.teiv.api.model.OranTeivRelationshipTypes;
import org.oran.smo.teiv.api.model.OranTeivRelationshipsResponseMessage;
import org.oran.smo.teiv.exposure.data.api.DataService;
import org.oran.smo.teiv.exposure.utils.RequestDetails;
import org.oran.smo.teiv.exposure.utils.RequestValidator;
import org.oran.smo.teiv.utils.TiesConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping(TiesConstants.REQUEST_MAPPING)
@RequiredArgsConstructor
public class DataController implements EntitiesAndRelationshipsApi {
    public static final String SCOPE_FILTER = "scopeFilter";
    public static final String TARGET_FILTER = "targetFilter";

    private final RequestValidator requestValidator;
    private final DataService dataService;
    private final CustomMetrics customMetrics;

    @Override
    @Timed("ties_exposure_http_get_domain_types_seconds")
    public ResponseEntity<OranTeivDomains> getAllDomains(@NotNull String accept, @Min(0) @Valid final Integer offset,
            @Min(1) @Max(500) @Valid final Integer limit) {
        return runWithFailCheck(() -> new ResponseEntity<>(dataService.getDomainTypes(RequestDetails.builder().offset(
                offset).limit(limit).basePath("/domains").build()), HttpStatus.OK),
                customMetrics::incrementNumUnsuccessfullyExposedDomainTypes);
    }

    @Override
    @Timed("ties_exposure_http_get_entity_types_seconds")
    public ResponseEntity<OranTeivEntityTypes> getTopologyEntityTypes(@NotNull String accept, String domain,
            @Min(0) @Valid final Integer offset, @Min(1) @Max(500) @Valid final Integer limit) {
        return runWithFailCheck(() -> {
            requestValidator.validateDomain(domain);
            return new ResponseEntity<>(dataService.getTopologyEntityTypes(domain, RequestDetails.builder().offset(offset)
                    .limit(limit).basePath(String.format("/domains/%s/entity-types", domain)).build()), HttpStatus.OK);
        }, customMetrics::incrementNumUnsuccessfullyExposedEntityTypes);
    }

    @Override
    @Timed("ties_exposure_http_get_relationship_types_seconds")
    public ResponseEntity<OranTeivRelationshipTypes> getTopologyRelationshipTypes(@NotNull String accept, String domain,
            @Min(0) @Valid final Integer offset, @Min(1) @Max(500) @Valid final Integer limit) {
        return runWithFailCheck(() -> {
            requestValidator.validateDomain(domain);
            return new ResponseEntity<>(dataService.getTopologyRelationshipTypes(domain, RequestDetails.builder().offset(
                    offset).limit(limit).basePath(String.format("/domains/%s/relationship-types", domain)).build()),
                    HttpStatus.OK);
        }, customMetrics::incrementNumUnsuccessfullyExposedRelationshipTypes);
    }

    @Override
    @Timed("ties_exposure_http_get_relationships_by_type_seconds")
    public ResponseEntity<OranTeivRelationshipsResponseMessage> getRelationshipsByType(@NotNull String accept,
            String domain, String relationshipType, @Valid String targetFilter, @Valid String scopeFilter,
            @Min(0) @Valid final Integer offset, @Min(1) @Max(500) @Valid final Integer limit) {
        return runWithFailCheck(() -> {
            requestValidator.validateDomain(domain);
            requestValidator.validateRelationshipType(relationshipType);
            requestValidator.validateRelationshipTypeInDomain(relationshipType, domain);
            return ResponseEntity.ok(dataService.getRelationshipsByType(domain, relationshipType, targetFilter, scopeFilter,
                    RequestDetails.builder().offset(offset).limit(limit).basePath(String.format(
                            "/domains/%s/relationship-types/%s/relationships", domain, relationshipType)).queryParam(
                                    SCOPE_FILTER, scopeFilter).queryParam(TARGET_FILTER, targetFilter).build()));
        }, customMetrics::incrementNumUnsuccessfullyExposedRelationshipsByType);
    }

    @Override
    @Timed("ties_exposure_http_get_entity_by_id_seconds")
    public ResponseEntity<Object> getTopologyById(@NotNull final String accept, final String domain,
            final String entityType, final String id) {
        return runWithFailCheck(() -> {
            requestValidator.validateDomain(domain);
            requestValidator.validateEntityType(entityType);
            requestValidator.validateEntityTypeInDomain(entityType, domain);
            return new ResponseEntity<>(dataService.getEntityById(entityType, id), HttpStatus.OK);
        }, customMetrics::incrementNumUnsuccessfullyExposedEntityById);
    }

    @Override
    @Timed("ties_exposure_http_get_entities_by_type_seconds")
    public ResponseEntity<OranTeivEntitiesResponseMessage> getTopologyByEntityTypeName(@NotNull final String accept,
            final String domain, final String entityType, @Valid final String targetFilter, @Valid final String scopeFilter,
            @Min(0) @Valid final Integer offset, @Min(1) @Max(500) @Valid final Integer limit) {
        return runWithFailCheck(() -> {
            requestValidator.validateDomain(domain);
            requestValidator.validateEntityType(entityType);
            requestValidator.validateEntityTypeInDomain(entityType, domain);
            return ResponseEntity.ok(dataService.getTopologyByType(domain, entityType, targetFilter, scopeFilter,
                    RequestDetails.builder().offset(offset).limit(limit).basePath(String.format(
                            "/domains/%s/entity-types/%s/entities", domain, entityType)).queryParam(TARGET_FILTER,
                                    targetFilter).queryParam(SCOPE_FILTER, scopeFilter).build()));
        }, customMetrics::incrementNumUnsuccessfullyExposedEntitiesByType);
    }

    @Override
    @Timed("ties_exposure_http_get_entities_by_domain_seconds")
    public ResponseEntity<OranTeivEntitiesResponseMessage> getEntitiesByDomain(@NotNull final String accept,
            final String domain, @Valid final String targetFilter, @Valid final String scopeFilter,
            @Min(0) @Valid final Integer offset, @Min(1) @Max(500) @Valid final Integer limit) {
        return runWithFailCheck(() -> {
            requestValidator.validateDomain(domain);
            return ResponseEntity.ok(dataService.getEntitiesByDomain(domain, targetFilter, scopeFilter, RequestDetails
                    .builder().offset(offset).limit(limit).basePath("/domains/" + domain + "/entities").queryParam(
                            TARGET_FILTER, targetFilter).queryParam(SCOPE_FILTER, scopeFilter).build()));
        }, customMetrics::incrementNumUnsuccessfullyExposedEntitiesByDomain);
    }

    @Override
    @Timed("ties_exposure_http_get_relationships_by_entity_id_seconds")
    public ResponseEntity<OranTeivRelationshipsResponseMessage> getAllRelationshipsForEntityId(@NotNull final String accept,
            final String domain, final String entityType, final String id, @Valid final String targetFilter,
            @Valid final String scopeFilter, @Min(0) @Valid final Integer offset,
            @Min(1) @Max(500) @Valid final Integer limit) {
        return runWithFailCheck(() -> {
            requestValidator.validateDomain(domain);
            requestValidator.validateEntityType(entityType);
            requestValidator.validateEntityTypeInDomain(entityType, domain);
            return ResponseEntity.ok(dataService.getAllRelationshipsForObjectId(domain, entityType, id, targetFilter,
                    scopeFilter, RequestDetails.builder().offset(offset).limit(limit).basePath(String.format(
                            "/domains/%s/entity-types/%s/entities/%s/relationships", domain, entityType, id)).queryParam(
                                    TARGET_FILTER, targetFilter).queryParam(SCOPE_FILTER, scopeFilter).build()));
        }, customMetrics::incrementNumUnsuccessfullyExposedAllRelationshipsByEntityId);
    }

    @Override
    @Timed("ties_exposure_http_get_relationship_by_id_seconds")
    public ResponseEntity<Object> getRelationshipById(final String accept, final String domain,
            final String relationshipType, final String id) {
        return runWithFailCheck(() -> {
            requestValidator.validateDomain(domain);
            requestValidator.validateRelationshipType(relationshipType);
            requestValidator.validateRelationshipTypeInDomain(relationshipType, domain);
            return ResponseEntity.ok(dataService.getRelationshipById(relationshipType, id));
        }, customMetrics::incrementNumUnsuccessfullyExposedRelationshipById);
    }

    protected <T> T runWithFailCheck(final Supplier<T> supp, final Runnable runnable) {
        try {
            return supp.get();
        } catch (Exception ex) {
            log.error("Exception during service call", ex);
            runnable.run();
            throw ex;
        }
    }
}
