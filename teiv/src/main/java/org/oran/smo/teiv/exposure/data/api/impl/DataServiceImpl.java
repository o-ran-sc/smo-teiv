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
package org.oran.smo.teiv.exposure.data.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.oran.smo.teiv.exposure.tiespath.resolver.ScopeResolver;
import org.jooq.Record;
import org.jooq.Result;
import org.oran.smo.teiv.exception.TiesException;
import org.oran.smo.teiv.exposure.spi.DataRepository;
import org.oran.smo.teiv.exposure.spi.mapper.EntityMapper;
import org.oran.smo.teiv.exposure.spi.mapper.RelationshipMapper;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.AndLogicalBlock;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.FilterCriteria;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.InnerFilterCriteria;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.OrLogicalBlock;
import org.oran.smo.teiv.exposure.tiespath.refiner.BasePathRefinement;
import org.oran.smo.teiv.exposure.tiespath.resolver.TargetResolver;
import org.oran.smo.teiv.exposure.utils.RequestDetails;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import org.oran.smo.teiv.api.model.OranTeivDomains;
import org.oran.smo.teiv.api.model.OranTeivDomainsItemsInner;
import org.oran.smo.teiv.api.model.OranTeivEntitiesResponseMessage;
import org.oran.smo.teiv.api.model.OranTeivEntityTypes;
import org.oran.smo.teiv.api.model.OranTeivEntityTypesItemsInner;
import org.oran.smo.teiv.api.model.OranTeivHref;
import org.oran.smo.teiv.api.model.OranTeivRelationshipTypes;
import org.oran.smo.teiv.api.model.OranTeivRelationshipTypesItemsInner;
import org.oran.smo.teiv.api.model.OranTeivRelationshipsResponseMessage;
import org.oran.smo.teiv.exposure.data.api.DataService;
import org.oran.smo.teiv.schema.EntityType;
import org.oran.smo.teiv.schema.RelationType;
import org.oran.smo.teiv.schema.SchemaRegistry;
import lombok.RequiredArgsConstructor;

import static org.oran.smo.teiv.exposure.utils.PaginationUtil.firstHref;
import static org.oran.smo.teiv.exposure.utils.PaginationUtil.getViableLimit;
import static org.oran.smo.teiv.exposure.utils.PaginationUtil.lastHref;
import static org.oran.smo.teiv.exposure.utils.PaginationUtil.nextHref;
import static org.oran.smo.teiv.exposure.utils.PaginationUtil.prevHref;
import static org.oran.smo.teiv.exposure.utils.PaginationUtil.selfHref;

@Service
@RequiredArgsConstructor
@Profile({ "exposure", "groups" })
public class DataServiceImpl implements DataService {
    private final DataRepository dataRepository;
    private final ScopeResolver scopeResolver;
    private final TargetResolver targetResolver;
    private final EntityMapper entityMapper;
    private final RelationshipMapper relationshipMapper;
    private final BasePathRefinement basePathRefinement;

    @Override
    public OranTeivDomains getDomainTypes(final RequestDetails requestDetails) {
        final Set<String> domains = SchemaRegistry.getDomains();
        final int totalCount = domains.size();

        final List<OranTeivDomainsItemsInner> items = domains.stream().skip(requestDetails.getOffset()).limit(
                getViableLimit(requestDetails.getOffset(), requestDetails.getLimit(), totalCount)).map(
                        domain -> OranTeivDomainsItemsInner.builder().name(domain).entityTypes(OranTeivHref.builder().href(
                                requestDetails.getBasePath() + "/" + domain + "/entity-types").build()).relationshipTypes(
                                        OranTeivHref.builder().href(requestDetails
                                                .getBasePath() + "/" + domain + "/relationship-types").build()).build())
                .toList();

        return OranTeivDomains.builder().items(items).first(firstHref(requestDetails)).prev(prevHref(requestDetails,
                totalCount)).self(selfHref(requestDetails)).next(nextHref(requestDetails, totalCount)).last(lastHref(
                        requestDetails, totalCount)).totalCount(totalCount).build();
    }

    @Override
    public OranTeivEntityTypes getTopologyEntityTypes(final String domain, final RequestDetails requestDetails) {
        final List<String> entityTypeNames = SchemaRegistry.getEntityNamesByDomain(domain);
        final int totalCount = entityTypeNames.size();

        final List<OranTeivEntityTypesItemsInner> items = entityTypeNames.stream().skip(requestDetails.getOffset()).limit(
                getViableLimit(requestDetails.getOffset(), requestDetails.getLimit(), totalCount)).map(
                        entityTypeName -> OranTeivEntityTypesItemsInner.builder().name(entityTypeName).entities(OranTeivHref
                                .builder().href(requestDetails.getBasePath() + "/" + entityTypeName + "/entities").build())
                                .build()).toList();

        return OranTeivEntityTypes.builder().items(items).first(firstHref(requestDetails)).prev(prevHref(requestDetails,
                totalCount)).self(selfHref(requestDetails)).next(nextHref(requestDetails, totalCount)).last(lastHref(
                        requestDetails, totalCount)).totalCount(totalCount).build();
    }

    @Override
    public OranTeivRelationshipTypes getTopologyRelationshipTypes(final String domain,
            final RequestDetails requestDetails) {
        final List<String> relationNames = SchemaRegistry.getRelationNamesByDomain(domain);
        final int totalCount = relationNames.size();

        final List<OranTeivRelationshipTypesItemsInner> items = relationNames.stream().skip(requestDetails.getOffset())
                .limit(getViableLimit(requestDetails.getOffset(), requestDetails.getLimit(), totalCount)).map(
                        relationName -> OranTeivRelationshipTypesItemsInner.builder().name(relationName).relationships(
                                OranTeivHref.builder().href(requestDetails
                                        .getBasePath() + "/" + relationName + "/relationships").build()).build()).toList();

        return OranTeivRelationshipTypes.builder().items(items).first(firstHref(requestDetails)).prev(prevHref(
                requestDetails, totalCount)).self(selfHref(requestDetails)).next(nextHref(requestDetails, totalCount)).last(
                        lastHref(requestDetails, totalCount)).totalCount(totalCount).build();
    }

    @Override
    public Object getEntityById(final String entityName, final String id) {
        final EntityType entityType = SchemaRegistry.getEntityTypeByName(entityName);
        final Result<Record> result = dataRepository.getEntityById(entityType, id);
        if (result.isEmpty()) {
            throw TiesException.resourceNotFoundException(id);
        }

        return entityMapper.getItemsWithTotalCount(result).getLeft().get(0);
    }

    @Override
    public OranTeivEntitiesResponseMessage getTopologyByType(final String domain, final String entityName,
            final String target, final String scope, final RequestDetails requestDetails) {
        final FilterCriteria filterCriteria = FilterCriteria.builder(domain).filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(targetResolver.resolve(entityName, target)).scope(scopeResolver.resolve(entityName,
                        scope)).build())).resolvingTopologyObjectType(FilterCriteria.ResolvingTopologyObjectType.ENTITY)
                .build();
        final Result<Record> result = dataRepository.getTopologyByFilterCriteria(filterCriteria, requestDetails.getLimit(),
                requestDetails.getOffset());
        return entityMapper.mapEntities(result, requestDetails);
    }

    @Override
    public OranTeivEntitiesResponseMessage getEntitiesByDomain(final String domain, final String fields,
            final String filters, final RequestDetails requestDetails) {
        final FilterCriteria filterCriteria = FilterCriteria.builder(domain).filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(targetResolver.resolve(null, fields)).scope(scopeResolver.resolve(null, filters))
                .build())).resolvingTopologyObjectType(FilterCriteria.ResolvingTopologyObjectType.ENTITY).build();
        final Result<Record> result = dataRepository.getTopologyByFilterCriteria(filterCriteria, requestDetails.getLimit(),
                requestDetails.getOffset());
        return entityMapper.mapEntities(result, requestDetails);
    }

    @Override
    public OranTeivRelationshipsResponseMessage getAllRelationshipsForObjectId(final String domain, final String entityName,
            final String id, final String target, final String scope, final RequestDetails requestDetails) {

        final List<RelationType> relationNamesForEntityByDomain = SchemaRegistry.getRelationNamesForEntityByDomain(
                entityName, domain);
        final OranTeivRelationshipsResponseMessage response;
        if (!relationNamesForEntityByDomain.isEmpty()) {
            final FilterCriteria filterCriteria = FilterCriteria.builder(domain).filterCriteriaList(List.of(
                    InnerFilterCriteria.builder().targets(targetResolver.resolve(null, target)).scope(scopeResolver.resolve(
                            null, scope)).build())).resolvingTopologyObjectType(
                                    FilterCriteria.ResolvingTopologyObjectType.RELATIONSHIP).build();

            basePathRefinement.refine(filterCriteria);

            List<InnerFilterCriteria> resolvedCriteriaList = new ArrayList<>();

            relationNamesForEntityByDomain.forEach(relationType -> filterCriteria.getFilterCriteriaList().forEach(
                    innerFilterCriteria -> {
                        final String relationship = innerFilterCriteria.getTargets().get(0).getTopologyObject();
                        if (relationship.equals(relationType.getName())) {
                            if (relationType.isConnectsSameEntity()) {
                                addLogicalBlockForLoopBackRelations(innerFilterCriteria, relationType.getASideAssociation()
                                        .getName(), relationType.getBSideAssociation().getName(), id, resolvedCriteriaList);
                            } else if (relationType.getASide().getName().equals(entityName)) {
                                addAndLogicalBlockToFilter(innerFilterCriteria, relationType.getBSideAssociation()
                                        .getName(), id, resolvedCriteriaList);
                            } else if (relationType.getBSide().getName().equals(entityName)) {
                                addAndLogicalBlockToFilter(innerFilterCriteria, relationType.getASideAssociation()
                                        .getName(), id, resolvedCriteriaList);
                            }
                        }
                    }));

            final FilterCriteria build = FilterCriteria.builder(domain).resolvingTopologyObjectType(
                    FilterCriteria.ResolvingTopologyObjectType.RELATIONSHIP).filterCriteriaList(resolvedCriteriaList)
                    .build();

            final Result<Record> result = dataRepository.getTopologyByFilterCriteria(build, requestDetails.getLimit(),
                    requestDetails.getOffset());
            response = relationshipMapper.mapRelationships(result, requestDetails);
        } else {
            final int totalCount = 0;
            response = OranTeivRelationshipsResponseMessage.builder().items(List.of()).first(firstHref(requestDetails))
                    .prev(prevHref(requestDetails, totalCount)).self(selfHref(requestDetails)).next(nextHref(requestDetails,
                            totalCount)).last(lastHref(requestDetails, totalCount)).totalCount(totalCount).build();
        }

        if (response.getItems().isEmpty()) {
            getEntityById(entityName, id);
        }
        return response;
    }

    @Override
    public Object getRelationshipById(final String relationName, final String id) {
        final RelationType relationType = SchemaRegistry.getRelationTypeByName(relationName);
        final Result<Record> result = dataRepository.getRelationshipById(id, relationType);
        if (result.isEmpty()) {
            throw TiesException.resourceNotFoundException(id);
        }

        return relationshipMapper.getItemsWithTotalCount(result).getLeft().get(0);
    }

    @Override
    public OranTeivRelationshipsResponseMessage getRelationshipsByType(final String domain, final String relationshipType,
            final String targetFilter, final String scopeFilter, final RequestDetails requestDetails) {
        final FilterCriteria filterCriteria = FilterCriteria.builder(domain).filterCriteriaList(List.of(InnerFilterCriteria
                .builder().targets(targetResolver.resolve(relationshipType, targetFilter)).scope(scopeResolver.resolve(
                        relationshipType, scopeFilter)).build())).resolvingTopologyObjectType(
                                FilterCriteria.ResolvingTopologyObjectType.RELATIONSHIP).build();
        final Result<Record> result = dataRepository.getTopologyByFilterCriteria(filterCriteria, requestDetails.getLimit(),
                requestDetails.getOffset());
        return relationshipMapper.mapRelationships(result, requestDetails);
    }

    // Helper method for adding AndLogicalBlock to FilterCriteria
    private void addAndLogicalBlockToFilter(final InnerFilterCriteria innerFilterCriteria, final String associationName,
            final String id, final List<InnerFilterCriteria> resolvedCriteriaList) {
        AndLogicalBlock andLogicalBlock = createAndLogicalBlock(innerFilterCriteria, associationName, id);
        innerFilterCriteria.setScope(andLogicalBlock);
        resolvedCriteriaList.add(innerFilterCriteria);
    }

    // Helper method used to add LogicalBlock for Rel connecting same entity to FilterCriteria
    private void addLogicalBlockForLoopBackRelations(final InnerFilterCriteria innerFilterCriteria,
            final String aSideAssociationName, final String bSideAssociationName, final String id,
            final List<InnerFilterCriteria> resolvedCriteriaList) {
        OrLogicalBlock orLogicalBlock = new OrLogicalBlock();
        orLogicalBlock.addChild(scopeResolver.resolve(null, "/" + aSideAssociationName + "[@id='" + id + "']"));
        orLogicalBlock.addChild(scopeResolver.resolve(null, "/" + bSideAssociationName + "[@id='" + id + "']"));
        AndLogicalBlock andLogicalBlock = new AndLogicalBlock();
        andLogicalBlock.addChild(innerFilterCriteria.getScope());
        andLogicalBlock.addChild(orLogicalBlock);
        innerFilterCriteria.setScope(andLogicalBlock);
        resolvedCriteriaList.add(innerFilterCriteria);
    }

    // Helper method to create AndLogicalBlock
    private AndLogicalBlock createAndLogicalBlock(final InnerFilterCriteria innerFilterCriteria,
            final String associationName, final String id) {
        AndLogicalBlock andLogicalBlock = new AndLogicalBlock();
        andLogicalBlock.addChild(innerFilterCriteria.getScope());
        andLogicalBlock.addChild(scopeResolver.resolve(null, "/" + associationName + "[@id='" + id + "']"));
        return andLogicalBlock;
    }
}
