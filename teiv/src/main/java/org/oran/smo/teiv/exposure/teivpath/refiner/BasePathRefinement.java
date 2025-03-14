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
package org.oran.smo.teiv.exposure.teivpath.refiner;

import lombok.RequiredArgsConstructor;
import org.oran.smo.teiv.exposure.consumerdata.ConsumerDataValidator;
import org.oran.smo.teiv.exposure.consumerdata.model.Classifiers;
import org.oran.smo.teiv.exposure.consumerdata.model.Decorators;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.AndLogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.AndOrLogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.ContainerType;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.EmptyLogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.FilterCriteria;

import org.oran.smo.teiv.exposure.teivpath.innerlanguage.QueryFunction;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.InnerFilterCriteria;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.LogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.OrLogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.ScopeLogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.ScopeObject;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.TopologyObjectType;
import org.oran.smo.teiv.exposure.teivpath.resolver.ResolverDataType;
import org.oran.smo.teiv.schema.DataType;
import org.oran.smo.teiv.schema.EntityType;
import org.oran.smo.teiv.schema.Persistable;
import org.oran.smo.teiv.schema.RelationType;
import org.oran.smo.teiv.schema.Reliability;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.utils.query.exception.TeivPathException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.TargetObject;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import static org.oran.smo.teiv.exposure.teivpath.innerlanguage.ContainerType.ATTRIBUTES;
import static org.oran.smo.teiv.exposure.teivpath.innerlanguage.ContainerType.ID;
import static org.oran.smo.teiv.exposure.teivpath.innerlanguage.ContainerType.NOT_NULL;
import static org.oran.smo.teiv.exposure.teivpath.innerlanguage.FilterCriteria.ResolvingTopologyObjectType.RELATIONSHIP;
import static org.oran.smo.teiv.exposure.teivpath.innerlanguage.TopologyObjectType.ASSOCIATION;
import static org.oran.smo.teiv.schema.DataType.BIGINT;
import static org.oran.smo.teiv.schema.DataType.DECIMAL;
import static org.oran.smo.teiv.schema.DataType.INTEGER;
import static org.oran.smo.teiv.schema.DataType.PRIMITIVE;
import static org.oran.smo.teiv.utils.TeivConstants.ID_COLUMN_NAME;
import static org.oran.smo.teiv.utils.TeivConstants.ITEM;
import static org.oran.smo.teiv.utils.TeivConstants.WILDCARD;

@Slf4j
@Component
@RequiredArgsConstructor
public class BasePathRefinement {
    private final ConsumerDataValidator consumerDataValidator;

    /**
     * Main method to orchestrate the refinement process
     *
     * @param filterCriteria
     *     the filter criteria
     */
    public void refine(FilterCriteria filterCriteria) {
        filterCriteria.getFilterCriteriaList().forEach(innerFilterCriteria -> runOnTree(innerFilterCriteria.getScope(),
                filterCriteria.getDomain(), this::handleAssociationsInScope));
        processTopologyObjectsWithContainerTypeNull(filterCriteria);
        if (filterCriteria.getResolvingTopologyObjectType().equals(RELATIONSHIP)) {
            filterCriteria.getFilterCriteriaList().forEach(innerFilterCriteria -> runOnTree(innerFilterCriteria.getScope(),
                    filterCriteria.getDomain(), this::restrictQueriesOnAssociationOfRelationIfNotId));
        }
        filterCriteria.getFilterCriteriaList().forEach(innerFilterCriteria -> resolveWildCardObjectsInScopeAndTarget(
                innerFilterCriteria, filterCriteria.getDomain(), filterCriteria.getResolvingTopologyObjectType()));
        resolveUndefinedTopologyObjectTypes(filterCriteria);
        validateContainers(filterCriteria);
        validateScopeParametersDataType(filterCriteria);
        filterCriteria.getFilterCriteriaList().forEach(innerFilterCriteria -> checkIfTargetMatchesWithScope(
                innerFilterCriteria, filterCriteria.getDomain()));
        splitFilterCriteria(filterCriteria);
        validateQuery(filterCriteria);
        matchTopologyObjectTypeWithResolvingTopologyObjectType(filterCriteria);
        applyRelationshipCriteria(filterCriteria);
    }

    public void handleAssociationsInScope(ScopeLogicalBlock slb, String domain) {
        String topologyObject = slb.getScopeObject().getTopologyObject();
        String[] topologyObjectSplit = topologyObject.split("/");
        if (topologyObjectSplit.length == 2) {
            if ((SchemaRegistry.getEntityNamesByDomain(domain).contains(topologyObjectSplit[0]) && SchemaRegistry
                    .getAssociationNamesByEntityName(topologyObjectSplit[0]).contains(
                            topologyObjectSplit[1])) || SchemaRegistry.getAllRelationNamesByAssociationName(
                                    topologyObjectSplit[1]).stream().anyMatch(rel -> rel.getName().equals(
                                            topologyObjectSplit[0]))) {
                slb.getScopeObject().setTopologyObjectType(ASSOCIATION);
            } else if (topologyObjectSplit[0].equals(WILDCARD)) {
                if (SchemaRegistry.getEntityNamesByDomain(domain).stream().anyMatch(entityName -> SchemaRegistry
                        .getAssociationNamesByEntityName(entityName).contains(topologyObjectSplit[1]))) {
                    slb.getScopeObject().setTopologyObjectType(ASSOCIATION);
                } else {
                    slb.getScopeObject().setTopologyObject(topologyObjectSplit[1]);
                }
            } else {
                throw TeivPathException.grammarError(String.format("Invalid parameter name (%s) after %s",
                        topologyObjectSplit[1], topologyObjectSplit[0]));
            }
        }
    }

    public void restrictQueriesOnAssociationOfRelationIfNotId(ScopeLogicalBlock slb, String domain) {
        if (slb.getScopeObject().getTopologyObjectType().equals(ASSOCIATION) && (!ID.equals(slb.getScopeObject()
                .getContainer()) && !NOT_NULL.equals(slb.getScopeObject().getContainer()))) {
            throw TeivPathException.grammarError(
                    "Querying any thing other than id is not supported in case of association of relation");
        }
    }

    /**
     * Process topology objects with container type null.
     * If scopeResolver can not differentiate ASSOCIATION from ENTITY and RELATION,
     * this function does additional settings of ScopeObjects.
     *
     * @param filterCriteria
     *     the filter criteria
     */
    public void processTopologyObjectsWithContainerTypeNull(FilterCriteria filterCriteria) {
        filterCriteria.getFilterCriteriaList().forEach(innerFilterCriteria -> runOnTree(innerFilterCriteria.getScope(),
                filterCriteria.getDomain(), this::setContainerTypesAndTopologyObjects));
    }

    private void setContainerTypesAndTopologyObjects(ScopeLogicalBlock slb, String domain) {
        if (slb.getScopeObject().getContainer() == null) {
            if (slb.getScopeObject().getLeaf() != null) {
                if (slb.getScopeObject().getLeaf().equals("id")) {
                    slb.getScopeObject().setContainer(ID);
                    slb.getScopeObject().setLeaf(null);
                } else {
                    throw TeivPathException.grammarError(String.format("Missing container type for queried leaf %s", slb
                            .getScopeObject().getLeaf()));
                }
            } else {
                if (slb.getScopeObject().getTopologyObjectType().equals(ASSOCIATION)) {
                    slb.getScopeObject().setContainer(NOT_NULL);
                } else {
                    throw TeivPathException.invalidDataInScopeFilter(slb.getScopeObject().getTopologyObject());
                }
            }
        } else if (slb.getScopeObject().getContainer().equals(ATTRIBUTES) && !slb.getScopeObject().getInnerContainer()
                .isEmpty() && slb.getScopeObject().getTopologyObject() == null) {
            slb.getScopeObject().setTopologyObject(WILDCARD);
        }
    }

    /**
     * Unifies duplicated targets by container type.
     * e.g. TargetFilter: /NRCellDU/attributes(fdn); /NRCellDU/attributes(nCI) --> /NRCellDU/attributes(fdn,nCI)
     * TargetFilter: /attributes; /attributes(fdn) --> /attributes(fdn) and isAllParamQueried = true (every attribute of
     * TopologyObjects if they have attribute fdn)
     */
    private void unifyDuplicatedTargetsByContainerType(List<TargetObject> targetObjects, ContainerType containerType) {
        Set<String> uniqueTopologyObjects = new HashSet<>();
        targetObjects.stream().filter(targetObject -> targetObject.getContainer().equals(containerType)).forEach(
                targetObject -> uniqueTopologyObjects.add(targetObject.getTopologyObject()));
        for (String topologyObject : uniqueTopologyObjects) {
            final Set<String> unifiedParams = new HashSet<>();
            boolean isAllParamQueried = containerType == ATTRIBUTES && targetObjects.stream().anyMatch(
                    targetObject -> targetObject.getTopologyObject().equals(topologyObject) && (targetObject.getParams()
                            .isEmpty() || targetObject.isAllParamQueried()) && targetObject.getContainer().equals(
                                    containerType));
            targetObjects.stream().filter(targetObject -> targetObject.getTopologyObject().equals(
                    topologyObject) && targetObject.getContainer().equals(containerType)).forEach(
                            targetObject -> unifiedParams.addAll(targetObject.getParams()));
            targetObjects.removeIf(targetObject -> targetObject.getTopologyObject().equals(topologyObject) && targetObject
                    .getContainer().equals(containerType));
            TargetObject unifiedTarget = TargetObject.builder(topologyObject).container(containerType).params(unifiedParams
                    .stream().toList()).isAllParamQueried(isAllParamQueried).build();
            targetObjects.add(unifiedTarget);
        }
    }

    /**
     * Resolve wildcard topologyObjects in scopeObjects and in targetObjects.
     *
     * @param filterCriteria
     *     the filter criteria
     */
    public void resolveWildCardObjectsInScopeAndTarget(InnerFilterCriteria filterCriteria, String domain,
            FilterCriteria.ResolvingTopologyObjectType resolvingTopologyObjectType) {
        if (filterCriteria.getTargets() == null || filterCriteria.getTargets().isEmpty()) {
            filterCriteria.setTargets(List.of(TargetObject.builder("*").container(ID).build()));
        }
        // merge targets
        Arrays.stream(ContainerType.values()).toList().forEach(containerType -> unifyDuplicatedTargetsByContainerType(
                filterCriteria.getTargets(), containerType));
        // calculate situation
        final List<ScopeLogicalBlock> wildCardsInScope = new ArrayList<>();
        final List<ScopeLogicalBlock> nonWildCardsInScope = new ArrayList<>();
        runOnTree(filterCriteria.getScope(), domain, (lb, s) -> {
            if (lb.getScopeObject().getTopologyObject().split("/")[0].equals("*")) {
                wildCardsInScope.add(lb);
            } else {
                nonWildCardsInScope.add(lb);
            }
        });
        boolean wildCardInTarget = filterCriteria.getTargets().stream().anyMatch(t -> t.getTopologyObject().equals("*"));
        boolean wildCardInScope = (filterCriteria.getScope() instanceof EmptyLogicalBlock) || !wildCardsInScope.isEmpty();
        boolean isAllParamRequired = filterCriteria.getTargets().stream().anyMatch(t -> t.getTopologyObject().equals(
                "*") && t.getContainer().equals(ATTRIBUTES) && t.isAllParamQueried());
        // skip when nothing to do
        if ((!wildCardInScope || filterCriteria.getScope() instanceof EmptyLogicalBlock) && !wildCardInTarget) {
            return;
        }
        List<Persistable> possibleTypes = listPossibleTypes(filterCriteria, domain, nonWildCardsInScope, wildCardInTarget,
                wildCardInScope, resolvingTopologyObjectType);
        filterPossibleTypesInTarget(filterCriteria, possibleTypes);
        switch (resolvingTopologyObjectType) {
            case ENTITY:
                filterPossibleTypesInScopeForEntities(wildCardsInScope, possibleTypes);
                break;
            case RELATIONSHIP:
                filterPossibleTypesInScopeForRelations(wildCardsInScope, possibleTypes);
                break;
            default:
                break;
        }
        // check
        if (possibleTypes.isEmpty()) {
            throw TeivPathException.notMatchingScopeAndTargetFilter();
        }
        resolveTargets(filterCriteria, isAllParamRequired, possibleTypes);
        // merge targets
        Arrays.stream(ContainerType.values()).toList().forEach(containerType -> unifyDuplicatedTargetsByContainerType(
                filterCriteria.getTargets(), containerType));
        //resolve scope
        List<LogicalBlock> resolvedTrees = new ArrayList<>();
        if (!(filterCriteria.getScope() instanceof EmptyLogicalBlock) && wildCardInScope) {
            possibleTypes.forEach(t -> resolvedTrees.add(copyTree(filterCriteria.getScope(), t.getName())));
        } else {
            return;
        }
        rebuildBinaryTree(filterCriteria, resolvedTrees);
    }

    @SuppressWarnings({ "java:S1874" })
    private List<Persistable> listPossibleTypes(InnerFilterCriteria filterCriteria, String domain,
            List<ScopeLogicalBlock> nonWildCardsInScope, boolean wildCardInTarget, boolean wildCardInScope,
            FilterCriteria.ResolvingTopologyObjectType resolvingTopologyObjectType) {
        List<Persistable> result = new ArrayList<>();
        if (!wildCardInTarget) {
            if (resolvingTopologyObjectType.isResolveEntities()) {
                result.addAll(filterCriteria.getTargets().stream().map(TargetObject::getTopologyObject).map(
                        SchemaRegistry::getEntityTypeByName).filter(Objects::nonNull).toList());
            }
            if (resolvingTopologyObjectType.isResolveRelationships()) {
                result.addAll(filterCriteria.getTargets().stream().map(TargetObject::getTopologyObject).map(
                        SchemaRegistry::getRelationTypeByName).filter(Objects::nonNull).toList());
            }
        } else if (!wildCardInScope) {
            if (resolvingTopologyObjectType.isResolveEntities()) {
                result.addAll(nonWildCardsInScope.stream().map(ScopeLogicalBlock::getScopeObject).map(
                        scopeObject -> scopeObject.getTopologyObject().split("/")[0]).map(
                                SchemaRegistry::getEntityTypeByName).filter(Objects::nonNull).toList());
            }
            if (resolvingTopologyObjectType.isResolveRelationships()) {
                result.addAll(nonWildCardsInScope.stream().map(ScopeLogicalBlock::getScopeObject).map(
                        scopeObject -> scopeObject.getTopologyObject().split("/")[0]).map(
                                SchemaRegistry::getRelationTypeByName).filter(Objects::nonNull).toList());
            }
        } else {
            if (resolvingTopologyObjectType.isResolveEntities()) {
                result.addAll(SchemaRegistry.getEntityTypesByDomain(domain));
            }
            if (resolvingTopologyObjectType.isResolveRelationships()) {
                result.addAll(SchemaRegistry.getRelationTypesByDomain(domain));
            }
        }
        return result;
    }

    private void filterPossibleTypesInTarget(InnerFilterCriteria filterCriteria, List<Persistable> possibleTypes) {
        for (TargetObject targetObject : filterCriteria.getTargets().stream().filter(t -> t.getTopologyObject().equals("*"))
                .toList()) {
            if (targetObject.getContainer().equals(ATTRIBUTES)) {
                for (String parameter : targetObject.getParams()) {
                    possibleTypes.removeIf(t -> !t.getAttributeNames().contains(parameter));
                }
            }
        }
    }

    private void filterPossibleTypesInScopeForEntities(List<ScopeLogicalBlock> wildCardsInScope,
            List<Persistable> possibleTypes) {
        for (ScopeObject scopeObject : wildCardsInScope.stream().map(ScopeLogicalBlock::getScopeObject).toList()) {
            if (scopeObject.getTopologyObjectType().equals(ASSOCIATION)) {
                possibleTypes.removeIf(t -> SchemaRegistry.getAllEntityForAssociation(scopeObject.getTopologyObject().split(
                        "/")[1]).stream().noneMatch(r -> r.getName().equals(t.getName())));
            }
            if (ATTRIBUTES.equals(scopeObject.getContainer()) && !scopeObject.getTopologyObjectType().equals(ASSOCIATION)) {
                if (!scopeObject.getInnerContainer().isEmpty()) {
                    possibleTypes.removeIf(t -> !t.getAttributeNames().contains(scopeObject.getInnerContainer().get(0)));
                } else {
                    possibleTypes.removeIf(t -> !t.getAttributeNames().contains(scopeObject.getLeaf()));
                }
            }
        }
    }

    private void filterPossibleTypesInScopeForRelations(List<ScopeLogicalBlock> wildCardsInScope,
            List<Persistable> possibleTypes) {
        for (ScopeObject scopeObject : wildCardsInScope.stream().map(ScopeLogicalBlock::getScopeObject).toList()) {
            if (scopeObject.getContainer().equals(ContainerType.ATTRIBUTES)) {
                if (!scopeObject.getInnerContainer().isEmpty()) {
                    possibleTypes.removeIf(t -> !t.getAttributeNames().contains(scopeObject.getInnerContainer().get(0)));
                } else {
                    possibleTypes.removeIf(t -> !t.getAttributeNames().contains(scopeObject.getLeaf()));
                }
            }
            if (scopeObject.getTopologyObjectType().equals(TopologyObjectType.ASSOCIATION)) {
                filterPossibleTypesInScopeByAssociations(possibleTypes, scopeObject);
            }
        }
    }

    @SuppressWarnings({ "java:S1874" })
    private void filterPossibleTypesInScopeByAssociations(List<Persistable> possibleTypes, ScopeObject scopeObject) {
        String[] topologyObjectSplit = scopeObject.getTopologyObject().split("/");
        List<Persistable> entityTypesCopy = new ArrayList<>(possibleTypes);
        for (Persistable t : entityTypesCopy) {
            if ((t instanceof RelationType && !(SchemaRegistry.getRelationTypeByName(t.getName()).getASideAssociation()
                    .getName().equals(topologyObjectSplit[1]) || SchemaRegistry.getRelationTypeByName(t.getName())
                            .getBSideAssociation().getName().equals(
                                    topologyObjectSplit[1]))) || (t instanceof EntityType && !SchemaRegistry
                                            .getAssociationNamesByEntityName(t.getName()).contains(
                                                    topologyObjectSplit[1]))) {
                possibleTypes.remove(t);
            }
        }
    }

    private void resolveTargets(InnerFilterCriteria filterCriteria, boolean isAllParamRequired,
            List<Persistable> possibleTypes) {
        List<TargetObject> resolvedTargets = new ArrayList<>();
        for (TargetObject targetObject : filterCriteria.getTargets()) {
            if (targetObject.getTopologyObject().equals("*")) {
                possibleTypes.forEach(t -> resolvedTargets.add(TargetObject.builder(t.getName()).container(targetObject
                        .getContainer()).params(targetObject.getParams()).isAllParamQueried(isAllParamRequired).build()));
            } else {
                resolvedTargets.add(targetObject);
            }
        }
        filterCriteria.setTargets(resolvedTargets);
    }

    private LogicalBlock copyTree(LogicalBlock logicalBlock, String topologyObject) {
        if (logicalBlock instanceof AndLogicalBlock andLogicalBlock) {
            AndLogicalBlock lb = new AndLogicalBlock();
            lb.setValid(logicalBlock.isValid());
            lb.getChildren().addAll(andLogicalBlock.getChildren().stream().map(child -> copyTree(child, topologyObject))
                    .toList());
            return lb;
        }
        if (logicalBlock instanceof OrLogicalBlock orLogicalBlock) {
            OrLogicalBlock lb = new OrLogicalBlock();
            lb.setValid(logicalBlock.isValid());
            lb.getChildren().addAll(orLogicalBlock.getChildren().stream().map(child -> copyTree(child, topologyObject))
                    .toList());
            return lb;
        }
        if (logicalBlock instanceof ScopeLogicalBlock scopeLogicalBlock) {
            ScopeObject scopeObject = buildScopeObject(topologyObject, scopeLogicalBlock);
            if (scopeObject.getLeaf() != null && scopeObject.getLeaf().equals(ID_COLUMN_NAME) && scopeObject.getContainer()
                    .equals(ID)) {
                scopeObject.setLeaf(null);
            }
            ScopeLogicalBlock lb = new ScopeLogicalBlock(scopeObject);
            lb.setValid(logicalBlock.isValid());
            return lb;
        }
        return EmptyLogicalBlock.getInstance();
    }

    private static ScopeObject buildScopeObject(String topologyObject, ScopeLogicalBlock scopeLogicalBlock) {
        if (scopeLogicalBlock.getScopeObject().getTopologyObjectType().equals(ASSOCIATION)) {
            return ScopeObject.builder((topologyObject == null || !scopeLogicalBlock.getScopeObject().getTopologyObject()
                    .split("/")[0].equals("*")) ?
                            scopeLogicalBlock.getScopeObject().getTopologyObject() :
                            String.format("%s/%s", topologyObject, scopeLogicalBlock.getScopeObject().getTopologyObject()
                                    .split("/")[1])).topologyObjectType(ASSOCIATION).container(scopeLogicalBlock
                                            .getScopeObject().getContainer()).innerContainer(scopeLogicalBlock
                                                    .getScopeObject().getInnerContainer()).leaf(scopeLogicalBlock
                                                            .getScopeObject().getLeaf()).queryFunction(scopeLogicalBlock
                                                                    .getScopeObject().getQueryFunction()).parameter(
                                                                            scopeLogicalBlock.getScopeObject()
                                                                                    .getParameter()).dataType(
                                                                                            scopeLogicalBlock
                                                                                                    .getScopeObject()
                                                                                                    .getDataType())
                    .resolverDataType(scopeLogicalBlock.getScopeObject().getResolverDataType()).build();
        } else {
            return ScopeObject.builder((topologyObject == null || !scopeLogicalBlock.getScopeObject().getTopologyObject()
                    .split("/")[0].equals("*")) ?
                            scopeLogicalBlock.getScopeObject().getTopologyObject().split("/")[0] :
                            topologyObject.split("/")[0]).topologyObjectType((topologyObject == null || !scopeLogicalBlock
                                    .getScopeObject().getTopologyObject().split("/")[0].equals("*")) ?
                                            scopeLogicalBlock.getScopeObject().getTopologyObjectType() :
                                            TopologyObjectType.UNDEFINED).container(scopeLogicalBlock.getScopeObject()
                                                    .getContainer()).innerContainer(scopeLogicalBlock.getScopeObject()
                                                            .getInnerContainer()).leaf(scopeLogicalBlock.getScopeObject()
                                                                    .getLeaf()).queryFunction(scopeLogicalBlock
                                                                            .getScopeObject().getQueryFunction()).parameter(
                                                                                    scopeLogicalBlock.getScopeObject()
                                                                                            .getParameter()).dataType(
                                                                                                    scopeLogicalBlock
                                                                                                            .getScopeObject()
                                                                                                            .getDataType())
                    .resolverDataType(scopeLogicalBlock.getScopeObject().getResolverDataType()).build();
        }
    }

    private void rebuildBinaryTree(InnerFilterCriteria filterCriteria, List<LogicalBlock> resolvedTrees) {
        LogicalBlock subTree = resolvedTrees.get(0);
        for (int i = 1; i < resolvedTrees.size(); i++) {
            OrLogicalBlock lb = new OrLogicalBlock();
            lb.getChildren().add(copyTree(subTree, null));
            lb.getChildren().add(resolvedTrees.get(i));
            subTree = lb;
        }
        filterCriteria.setScope(subTree);
    }

    /**
     * Resolve undefined topology object types to either relation or entity in scope and target.
     * In case there is no match or multiple match error is thrown.
     *
     * @param filterCriteria
     *     the filter criteria
     */
    public void resolveUndefinedTopologyObjectTypes(FilterCriteria filterCriteria) {
        resolveUndefinedObjectTypesInTarget(filterCriteria);
        resolveUndefinedObjectTypesInScope(filterCriteria);
    }

    private void resolveUndefinedObjectTypesInScope(FilterCriteria filterCriteria) {
        filterCriteria.getFilterCriteriaList().forEach(innerFilterCriteria -> runOnTree(innerFilterCriteria.getScope(),
                filterCriteria.getDomain(), this::defineTopologyObjectTypeInScope));
    }

    private void resolveUndefinedObjectTypesInTarget(FilterCriteria filterCriteria) {
        filterCriteria.getFilterCriteriaList().forEach(innerFilterCriteria -> innerFilterCriteria.getTargets().stream()
                .filter(targetObject -> targetObject.getTopologyObjectType().equals(TopologyObjectType.UNDEFINED)).forEach(
                        targetObject -> defineTopologyObjectTypeForTarget(targetObject, filterCriteria.getDomain())));
    }

    private void defineTopologyObjectTypeInScope(ScopeLogicalBlock scopeLogicalBlock, String domain) {
        if (scopeLogicalBlock.getScopeObject().getTopologyObjectType().equals(TopologyObjectType.UNDEFINED)) {
            boolean isResolved = false;
            String topologyObject = scopeLogicalBlock.getScopeObject().getTopologyObject().split("/")[0];
            if (SchemaRegistry.getEntityNamesByDomain(domain).contains(topologyObject)) {
                scopeLogicalBlock.getScopeObject().setTopologyObjectType(TopologyObjectType.ENTITY);
                isResolved = true;
            }
            if (SchemaRegistry.getRelationNamesByDomain(domain).contains(topologyObject)) {
                if (!isResolved) {
                    scopeLogicalBlock.getScopeObject().setTopologyObjectType(TopologyObjectType.RELATION);
                    isResolved = true;
                } else {
                    throw TeivPathException.ambiguousTopologyObject(topologyObject);
                }
            }
            if (!isResolved) {
                throw TeivPathException.invalidTopologyObject(topologyObject);
            }
        }
    }

    private void defineTopologyObjectTypeForTarget(TargetObject targetObject, String domain) {
        boolean isResolved = false;
        if (SchemaRegistry.getEntityNamesByDomain(domain).contains(targetObject.getTopologyObject())) {
            targetObject.setTopologyObjectType(TopologyObjectType.ENTITY);
            isResolved = true;
        }
        if (SchemaRegistry.getRelationNamesByDomain(domain).contains(targetObject.getTopologyObject())) {
            if (!isResolved) {
                targetObject.setTopologyObjectType(TopologyObjectType.RELATION);
                isResolved = true;
            } else {
                throw TeivPathException.ambiguousTopologyObject(targetObject.getTopologyObject());
            }
        }
        if (!isResolved) {
            throw TeivPathException.invalidTopologyObject(targetObject.getTopologyObject());
        }
    }

    /**
     * Validates containers of targetObjects and scopeObjects through
     * checking whether the topologyObject with the selected containerType has the parameters that are listed in either
     * scopeObjects' leaf or targetObjects' params list.
     *
     * @param filterCriteria
     *     the filter criteria
     */
    public void validateContainers(FilterCriteria filterCriteria) {
        filterCriteria.getFilterCriteriaList().forEach(innerFilterCriteria -> innerFilterCriteria.getTargets().forEach(
                targetObject -> validateContainerWithMatchingParameters(targetObject.getContainer(), targetObject
                        .getParams(), targetObject.getTopologyObject(), targetObject.getTopologyObjectType(), Collections
                                .emptyList())));
        filterCriteria.getFilterCriteriaList().forEach(innerFilterCriteria -> runOnTree(innerFilterCriteria.getScope(),
                filterCriteria.getDomain(), this::validateScope));
    }

    private void validateScope(LogicalBlock logicalBlock, String domain) {
        ScopeObject scopeObject = ((ScopeLogicalBlock) logicalBlock).getScopeObject();
        if (scopeObject.getContainer() != null) {
            validateContainerWithMatchingParameters(scopeObject.getContainer(), new ArrayList<>(Collections.singletonList(
                    scopeObject.getLeaf())), scopeObject.getTopologyObject(), scopeObject.getTopologyObjectType(),
                    scopeObject.getInnerContainer());
        }
    }

    private void validateContainerWithMatchingParameters(ContainerType containerType, List<String> params,
            String topologyObject, TopologyObjectType topologyObjectType, List<String> innerContainer) {
        switch (containerType) {
            case ID:
                if (!params.isEmpty() && !(params.size() == 1 && params.contains(null))) {
                    throw TeivPathException.grammarError("Adding parameters for id container is not supported");
                }
                break;
            case ATTRIBUTES:
                if (innerContainer.isEmpty()) {
                    checkAttributesOfTopologyObject(params, topologyObject, topologyObjectType);
                } else {
                    checkAttributesOfTopologyObject(List.of(innerContainer.get(0)), topologyObject, topologyObjectType);
                }
                break;
            case SOURCE_IDS, CLASSIFIERS:
                checkSourceIdTopologyObject(params, topologyObject);
                break;
            default:
                break;
        }
    }

    @SuppressWarnings({ "java:S1874" })
    private void checkAssociationTopologyObject(List<String> params, String topologyObject) {
        if (topologyObject.split("/").length < 2) {
            throw TeivPathException.grammarError("Missing association name");
        }
        String[] topologyObjectArray = topologyObject.split("/");
        String realTopologyObject = topologyObjectArray[0];
        String association = topologyObjectArray[1];
        RelationType relation;
        // Case Entity
        if (SchemaRegistry.getEntityNames().contains(realTopologyObject)) {
            relation = SchemaRegistry.getRelationTypes().stream().filter(relationType -> (relationType.getASide().getName()
                    .equals(realTopologyObject) && relationType.getASideAssociation().getName().equals(
                            association)) || (relationType.getBSide().getName().equals(realTopologyObject) && relationType
                                    .getBSideAssociation().getName().equals(association))).findFirst().orElseThrow(
                                            () -> TeivPathException.invalidAssociation(realTopologyObject, association));
        }
        // Case Relation
        else if (SchemaRegistry.getRelationNames().contains(realTopologyObject)) {
            relation = SchemaRegistry.getRelationTypeByName(realTopologyObject);
            if (!relation.getASideAssociation().getName().equals(association) && !relation.getBSideAssociation().getName()
                    .equals(association)) {
                throw TeivPathException.invalidAssociation(realTopologyObject, association);
            }
        } else {
            throw TeivPathException.invalidTopologyObject(realTopologyObject);
        }
        if (!params.isEmpty() && params.get(0) != null) {
            checkParamsForAssociation(relation, association, params);
        }
    }

    private void checkParamsForAssociation(RelationType relationType, String associationName, List<String> params) {
        if ((relationType.getASideAssociation().getName().equals(associationName) && !params.stream().allMatch(
                param -> relationType.getBSide().getAttributeNames().contains(param))) || (relationType
                        .getBSideAssociation().getName().equals(associationName) && !params.stream().allMatch(
                                param -> relationType.getASide().getAttributeNames().contains(param)))) {
            throw TeivPathException.invalidParamsForAssociation(associationName);
        }
    }

    private void checkSourceIdTopologyObject(List<String> params, String topologyObject) {
        if (params.stream().anyMatch(param -> !param.equals(ITEM))) {
            throw TeivPathException.sourceIdNameError(topologyObject);
        }
    }

    @SuppressWarnings({ "java:S1874" })
    private void checkAttributesOfTopologyObject(List<String> params, String topologyObject,
            TopologyObjectType topologyObjectType) {
        switch (topologyObjectType) {
            case ENTITY -> {
                EntityType entityType = SchemaRegistry.getEntityTypeByName(topologyObject);
                List<String> notMatchingParams = params.stream().filter(a -> !entityType.getAttributeNames().contains(a))
                        .toList();
                if (!notMatchingParams.isEmpty()) {
                    throw TeivPathException.columnNamesError(topologyObject, notMatchingParams);
                }
            }
            case RELATION -> {
                RelationType relation = SchemaRegistry.getRelationTypeByName(topologyObject);
                List<String> notMatchingParams2 = params.stream().filter(a -> !relation.getAttributeNames().contains(a))
                        .toList();
                if (!notMatchingParams2.isEmpty()) {
                    throw TeivPathException.columnNamesError(topologyObject, notMatchingParams2);
                }
            }
            case ASSOCIATION -> checkAssociationTopologyObject(params, topologyObject);
            default -> throw TeivPathException.grammarError(String.format("Undefined topologyObject: %s", topologyObject));
        }
    }

    /**
     * Validate scope parameters' data type.
     * If resolverDataType does not match the accepted dataType of the attribute/id/source_id etc., error is thrown.
     * resolverDataType: INTEGER - accepted dataTypes: INTEGER, BIGINT, DECIMAL
     * resolverDataType: STRING - accepted dataTypes: all other
     *
     * @param filterCriteria
     *     the filter criteria
     */
    public void validateScopeParametersDataType(FilterCriteria filterCriteria) {
        filterCriteria.getFilterCriteriaList().forEach(innerFilterCriteria -> runOnTree(innerFilterCriteria.getScope(),
                filterCriteria.getDomain(), this::validateParameterOfScopeObject));
    }

    private void validateParameterOfScopeObject(ScopeLogicalBlock scopeLogicalBlock, String domain) {
        if (scopeLogicalBlock.getScopeObject().getContainer() != null) {
            ScopeObject so = scopeLogicalBlock.getScopeObject();
            switch (so.getContainer()) {
                case ATTRIBUTES:
                    validateAttributesParameter(so);
                    break;
                case ID:
                    validateIdParameter(so);
                    break;
                case RELATION:
                    validateAssociationAndRelationParameter(so);
                    break;
                case SOURCE_IDS:
                    validateSourceIdsParameter(so);
                    break;
                case CLASSIFIERS:
                    validateClassifiersParameter(so);
                    break;
                case DECORATORS:
                    validateDecoratorsParameter(so);
                    break;
                case METADATA:
                    validateMetadataParameter(so);
                    break;
                default:
                    break;
            }
        }
    }

    @SuppressWarnings({ "java:S1874" })
    private void validateAttributesParameter(final ScopeObject so) {
        String[] topologyObject = so.getTopologyObject().split("/");
        if (topologyObject.length > 1) {
            if (so.getInnerContainer().isEmpty()) {
                so.setDataType(SchemaRegistry.getAssociationSideEntity(topologyObject[1]).getFields().get(so.getLeaf()));
                compareResolverDataTypeToDataType(so);
            } else {
                setDataTypeForComplexAttribute(so);
            }
        } else {
            if (so.getInnerContainer().isEmpty()) {
                so.setDataType(SchemaRegistry.getEntityTypeByName(topologyObject[0]).getFields().get(so.getLeaf()));
                compareResolverDataTypeToDataType(so);
            } else {
                setDataTypeForComplexAttribute(so);
            }
        }
    }

    private void validateIdParameter(final ScopeObject so) {
        if (so.getResolverDataType().equals(ResolverDataType.INTEGER)) {
            throw TeivPathException.grammarError("Data is given in invalid datatype in scopeFilter");
        }
        so.setDataType(DataType.PRIMITIVE);
    }

    private void validateAssociationAndRelationParameter(final ScopeObject so) {
        if (so.getLeaf() != null && !ID_COLUMN_NAME.equals(so.getLeaf())) {
            throw TeivPathException.grammarError("Only id condition can be queried in case of association container");
        } else if (so.getResolverDataType().equals(ResolverDataType.INTEGER)) {
            throw TeivPathException.invalidDataType();
        } else if (so.getResolverDataType().equals(ResolverDataType.STRING)) {
            so.setDataType(DataType.PRIMITIVE);
        }
    }

    private void validateSourceIdsParameter(final ScopeObject so) {
        if (so.getResolverDataType().equals(ResolverDataType.INTEGER)) {
            throw TeivPathException.invalidDataType();
        }
        so.setDataType(DataType.PRIMITIVE);
    }

    private void validateMetadataParameter(final ScopeObject so) {
        final List<String> validMetadataParameters = List.of("lastModified", "firstDiscovered", "reliabilityIndicator");
        if (so.getLeaf() != null && validMetadataParameters.contains(so.getLeaf())) {
            if (so.getResolverDataType().equals(ResolverDataType.STRING)) {
                if (so.getLeaf().equals("reliabilityIndicator")) {
                    validateReliabilityIndicator(so.getParameter());
                }
            } else {
                throw TeivPathException.grammarError("Invalid data type provided for scopeFilter");
            }
        } else {
            throw TeivPathException.invalidMetadaFilter(so.getLeaf());
        }
        if (so.getLeaf().equals("lastModified") || so.getLeaf().equals("firstDiscovered")) {
            so.setDataType(DataType.TIMESTAMPTZ);
        } else {
            so.setDataType(DataType.PRIMITIVE);
        }
    }

    private void validateClassifiersParameter(final ScopeObject so) {
        if (so.getResolverDataType().equals(ResolverDataType.INTEGER)) {
            throw TeivPathException.grammarError("Invalid data type provided for scopeFilter");
        }
        so.setDataType(DataType.PRIMITIVE);
        Classifiers classifier = Classifiers.builder().data(List.of(so.getParameter())).build();
        consumerDataValidator.validate(classifier);
    }

    private void validateDecoratorsParameter(final ScopeObject so) {
        Object value = null;
        if (so.getResolverDataType().equals(ResolverDataType.INTEGER)) {
            so.setDataType(DataType.BIGINT);
            value = Integer.valueOf(so.getParameter());
        } else {
            so.setDataType(DataType.PRIMITIVE);
            value = so.getParameter();
        }
        Decorators decorator = Decorators.builder().data(Map.of(so.getLeaf(), value)).build();
        consumerDataValidator.validate(decorator);
    }

    private void setDataTypeForComplexAttribute(final ScopeObject scopeObject) {
        switch (scopeObject.getResolverDataType()) {
            case INTEGER -> scopeObject.setDataType(INTEGER);
            case STRING -> scopeObject.setDataType(PRIMITIVE);
            default -> throw new IllegalStateException("Invalid data type for complex attribute");
        }
    }

    private void compareResolverDataTypeToDataType(final ScopeObject scopeObject) {
        List<DataType> numericDataTypes = List.of(INTEGER, BIGINT, DECIMAL);
        if ((scopeObject.getResolverDataType().equals(ResolverDataType.INTEGER) && !numericDataTypes.contains(scopeObject
                .getDataType())) || (scopeObject.getResolverDataType().equals(ResolverDataType.STRING) && numericDataTypes
                        .contains(scopeObject.getDataType()))) {
            throw TeivPathException.grammarError("Data is given in invalid datatype in scopeFilter");
        }
    }

    /**
     * Check if target's topologyObjects match with scope's topologyObjects.
     *
     * @param filterCriteria
     *     - filter criteria
     *
     */
    public void checkIfTargetMatchesWithScope(InnerFilterCriteria filterCriteria, String domain) {
        if (filterCriteria.getScope() instanceof EmptyLogicalBlock || filterCriteria.getTargets().isEmpty()) {
            return;
        }
        Set<String> scopeTopologyObjects = new HashSet<>();
        runOnTree(filterCriteria.getScope(), domain, (ScopeLogicalBlock lb, String dom) -> scopeTopologyObjects.add(lb
                .getScopeObject().getTopologyObject().split("/")[0]));
        Set<String> targetTopologyObjects = filterCriteria.getTargets().stream().map(TargetObject::getTopologyObject)
                .collect(Collectors.toSet());
        if (targetTopologyObjects.size() == scopeTopologyObjects.size()) {
            scopeTopologyObjects.removeAll(targetTopologyObjects);
            if (!scopeTopologyObjects.isEmpty()) {
                throw TeivPathException.notMatchingScopeAndTargetFilter();
            }
        } else {
            throw TeivPathException.notMatchingScopeAndTargetFilter();
        }
    }

    /**
     * Split inner filter criteria to multiple inner filter criteria and use union instance of or logical block
     *
     * @param filterCriteria
     *     the filter criteria
     */
    public void splitFilterCriteria(FilterCriteria filterCriteria) {
        Map<String, InnerFilterCriteria> innerFilterCriteriaMap = new HashMap<>();
        for (InnerFilterCriteria base : filterCriteria.getFilterCriteriaList()) {
            for (TargetObject targetObject : base.getTargets()) {
                if (!innerFilterCriteriaMap.containsKey(targetObject.getTopologyObject())) {
                    innerFilterCriteriaMap.put(targetObject.getTopologyObject(), InnerFilterCriteria.builder().targets(
                            new ArrayList<>()).build());
                }
                innerFilterCriteriaMap.get(targetObject.getTopologyObject()).getTargets().add(targetObject);
            }
        }
        for (InnerFilterCriteria base : filterCriteria.getFilterCriteriaList()) {
            List<String> types = base.getTargets().stream().map(TargetObject::getTopologyObject).toList();
            for (String type : types) {
                innerFilterCriteriaMap.get(type).setScope(generateCutTree(base.getScope(), type));
            }
        }
        filterCriteria.setFilterCriteriaList(new ArrayList<>(innerFilterCriteriaMap.values()));
    }

    /**
     * Validates the binary tree of LogicalBlocks (LB) in FilterCriteria based on isValid boolean of the objects.
     * Remove invalid LB, when an AND block has an invalid LB set the AND block to invalid
     * When an OR has an invalid block, replace the OR block with the valid child element.
     * Throw an error when the last LB became invalid.
     *
     * @param filterCriteria
     *     the filter criteria
     */
    public void validateQuery(FilterCriteria filterCriteria) {
        filterCriteria.getFilterCriteriaList().forEach(innerFilterCriteria -> {
            innerFilterCriteria.setScope(traverseTree(innerFilterCriteria.getScope()));
            if (!innerFilterCriteria.getScope().isValid()) {
                throw TeivPathException.invalidQueryError();
            }
        });
    }

    private void matchTopologyObjectTypeWithResolvingTopologyObjectType(FilterCriteria filterCriteria) {
        if (filterCriteria.getResolvingTopologyObjectType().equals(
                FilterCriteria.ResolvingTopologyObjectType.RELATIONSHIP)) {
            for (InnerFilterCriteria innerCriteria : filterCriteria.getFilterCriteriaList()) {
                for (TargetObject target : innerCriteria.getTargets()) {
                    if (target.getTopologyObjectType() != TopologyObjectType.RELATION) {
                        throw TeivPathException.invalidTargetFilter(target.getTopologyObject());
                    }
                }
            }
        }
    }

    private LogicalBlock traverseTree(LogicalBlock logicalBlock) {
        if (logicalBlock instanceof AndOrLogicalBlock andOrLogicalBlock && andOrLogicalBlock.getChildren().size() == 2) {
            andOrLogicalBlock.getChildren().set(0, traverseTree(andOrLogicalBlock.getChildren().get(0)));
            andOrLogicalBlock.getChildren().set(1, traverseTree(andOrLogicalBlock.getChildren().get(1)));
            if (andOrLogicalBlock.getChildren().stream().anyMatch(child -> !child.isValid())) {
                if (logicalBlock instanceof OrLogicalBlock orLogicalBlock) {
                    Optional<LogicalBlock> validLogicalBlock = orLogicalBlock.getChildren().stream().filter(
                            LogicalBlock::isValid).findFirst();
                    return validLogicalBlock.orElseGet(() -> invalidateLogicalBlock(orLogicalBlock));
                }
                if (andOrLogicalBlock.getChildren().stream().anyMatch(child -> !child.isValid())) {
                    andOrLogicalBlock.getChildren().removeIf(child -> !child.isValid());
                    return invalidateLogicalBlock(andOrLogicalBlock);
                }
            }
        }
        return logicalBlock;
    }

    private LogicalBlock generateCutTree(LogicalBlock logicalBlock, String type) {
        if (!logicalBlock.isValid() || logicalBlock instanceof EmptyLogicalBlock) {
            return EmptyLogicalBlock.getInstance();
        }
        if (logicalBlock instanceof AndLogicalBlock andLogicalBlock) {
            return getAndOrLogicalBlock(type, andLogicalBlock, AndLogicalBlock::new);
        }
        if (logicalBlock instanceof OrLogicalBlock orLogicalBlock) {
            return getAndOrLogicalBlock(type, orLogicalBlock, OrLogicalBlock::new);
        }
        if (logicalBlock instanceof ScopeLogicalBlock scopeLogicalBlock) {
            if ((!scopeLogicalBlock.getScopeObject().getTopologyObject().equals(type) && !scopeLogicalBlock.getScopeObject()
                    .getTopologyObjectType().equals(ASSOCIATION)) || (!scopeLogicalBlock.getScopeObject()
                            .getTopologyObject().split("/")[0].equals(type) && scopeLogicalBlock.getScopeObject()
                                    .getTopologyObjectType().equals(ASSOCIATION))) {
                return EmptyLogicalBlock.getInstance();
            }
            ScopeObject response = ScopeObject.builder(type).topologyObjectType(scopeLogicalBlock.getScopeObject()
                    .getTopologyObjectType()).container(scopeLogicalBlock.getScopeObject().getContainer()).innerContainer(
                            scopeLogicalBlock.getScopeObject().getInnerContainer()).leaf(scopeLogicalBlock.getScopeObject()
                                    .getLeaf()).queryFunction(scopeLogicalBlock.getScopeObject().getQueryFunction())
                    .parameter(scopeLogicalBlock.getScopeObject().getParameter()).dataType(scopeLogicalBlock
                            .getScopeObject().getDataType()).resolverDataType(scopeLogicalBlock.getScopeObject()
                                    .getResolverDataType()).build();
            if (scopeLogicalBlock.getScopeObject().getTopologyObjectType().equals(ASSOCIATION)) {
                response.setTopologyObject(scopeLogicalBlock.getScopeObject().getTopologyObject());
            }
            return new ScopeLogicalBlock(response);
        }
        return EmptyLogicalBlock.getInstance();
    }

    private LogicalBlock getAndOrLogicalBlock(String type, AndOrLogicalBlock andLogicalBlock,
            Supplier<AndOrLogicalBlock> constructor) {
        LogicalBlock left = generateCutTree(andLogicalBlock.getChildren().get(0), type);
        LogicalBlock right = generateCutTree(andLogicalBlock.getChildren().get(1), type);
        if (!left.isValid() || left instanceof EmptyLogicalBlock) {
            return right;
        }
        if (!right.isValid() || right instanceof EmptyLogicalBlock || left.equals(right)) {
            return left;
        }
        AndOrLogicalBlock response = constructor.get();
        response.getChildren().add(left);
        response.getChildren().add(right);
        return response;
    }

    private LogicalBlock invalidateLogicalBlock(LogicalBlock logicalBlock) {
        logicalBlock.setValid(false);
        return logicalBlock;
    }

    public void runOnTree(LogicalBlock logicalBlock, String domain, BiConsumer<ScopeLogicalBlock, String> func) {
        if (!logicalBlock.isValid() || logicalBlock instanceof EmptyLogicalBlock) {
            return;
        }
        if (logicalBlock instanceof AndOrLogicalBlock andOrLogicalBlock) {
            andOrLogicalBlock.getChildren().forEach(l -> runOnTree(l, domain, func));
            return;
        }
        func.accept((ScopeLogicalBlock) logicalBlock, domain);
    }

    private void applyRelationshipCriteria(FilterCriteria filterCriteria) {
        if (filterCriteria.getResolvingTopologyObjectType().equals(
                FilterCriteria.ResolvingTopologyObjectType.RELATIONSHIP)) {
            filterCriteria.getFilterCriteriaList().forEach(criteria -> {
                if (criteria.getScope() instanceof EmptyLogicalBlock) {
                    criteria.setScope(buildScopeLogicalBlockForRelationship(criteria));
                } else if (criteria.getScope()instanceof AndOrLogicalBlock andOrLogicalBlock) {
                    AndOrLogicalBlock aolb = new AndLogicalBlock();
                    aolb.setChildren(List.of(andOrLogicalBlock, buildScopeLogicalBlockForRelationship(criteria)));
                    criteria.setScope(aolb);
                } else if (criteria.getScope()instanceof ScopeLogicalBlock scopeLogicalBlock) {
                    AndOrLogicalBlock aolb = new AndLogicalBlock();
                    aolb.setChildren(List.of(scopeLogicalBlock, buildScopeLogicalBlockForRelationship(criteria)));
                    criteria.setScope(aolb);
                }
            });
        }
    }

    private ScopeLogicalBlock buildScopeLogicalBlockForRelationship(InnerFilterCriteria criteria) {
        return new ScopeLogicalBlock(ScopeObject.builder(criteria.getTargets().get(0).getTopologyObject())
                .topologyObjectType(TopologyObjectType.RELATION).queryFunction(QueryFunction.NOT_NULL).container(ID)
                .resolverDataType(ResolverDataType.STRING).dataType(PRIMITIVE).build());
    }

    private boolean validateReliabilityIndicator(String param) {
        try {
            return Reliability.valueOf(param) != null;
        } catch (IllegalArgumentException e) {
            throw TeivPathException.grammarError("Invalid parameter type provided for scopeFilter");
        }
    }
}
