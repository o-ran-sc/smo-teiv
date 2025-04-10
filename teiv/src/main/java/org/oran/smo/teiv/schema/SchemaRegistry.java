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
package org.oran.smo.teiv.schema;

import static org.oran.smo.teiv.schema.SchemaRegistryErrorCode.DUPLICATE_ENTITY_NAME_IN_DOMAIN;
import static org.oran.smo.teiv.schema.SchemaRegistryErrorCode.DUPLICATE_ENTITY_NAME_IN_TEIV_DOMAIN;
import static org.oran.smo.teiv.schema.SchemaRegistryErrorCode.DUPLICATE_RELATION_NAME_IN_DOMAIN;
import static org.oran.smo.teiv.schema.SchemaRegistryErrorCode.DUPLICATE_RELATION_NAME_IN_TEIV_DOMAIN;
import static org.oran.smo.teiv.schema.SchemaRegistryErrorCode.ENTITY_NOT_FOUND_IN_DOMAIN;
import static org.oran.smo.teiv.schema.SchemaRegistryErrorCode.ENTITY_NOT_FOUND_IN_MODULE;
import static org.oran.smo.teiv.schema.SchemaRegistryErrorCode.RELATIONSHIP_NOT_FOUND_IN_DOMAIN;
import static org.oran.smo.teiv.schema.SchemaRegistryErrorCode.RELATIONSHIP_NOT_FOUND_IN_MODULE;
import static org.oran.smo.teiv.utils.TeivConstants.TEIV_DOMAIN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.experimental.UtilityClass;

import jakarta.annotation.Nullable;

import org.oran.smo.teiv.utils.TeivConstants;
import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.exposure.spi.Module;

@Slf4j
@UtilityClass
public class SchemaRegistry {
    @Getter
    private static Map<String, Module> moduleRegistry;
    @Getter
    private static List<EntityType> entityTypes;
    @Getter
    private static List<RelationType> relationTypes;

    // Modules section
    /**
     * Initializes the modules. Once set cannot be overridden.
     *
     * @param moduleTypes-
     *     module types
     */
    static void initializeModules(Map<String, Module> moduleTypes) {
        moduleRegistry = Collections.unmodifiableMap(moduleTypes);
    }

    /**
     * Gets the module by the given name.
     *
     * @param name
     *     - module name
     * @return the {@link Module}
     */
    public static Module getModuleByName(String name) {
        return Optional.ofNullable(moduleRegistry.get(name)).orElseThrow(() -> TeivException.unknownModule(name));
    }

    /**
     * Gets the module by the given domain name.
     *
     * @param domain
     *     - name of the domain
     * @return the {@link Module}
     */
    public static Module getModuleByDomain(String domain) {
        return moduleRegistry.values().stream().filter(module -> module.getDomain() != null && module.getDomain().equals(
                domain)).findFirst().orElseThrow(() -> TeivException.unknownDomain(domain, getDomains()));
    }

    /**
     * Gets the included domain names for the given domain name.
     *
     * @param domain
     *     - name of the domain
     * @return the list of included domains
     */
    public static List<String> getIncludedDomains(String domain) {
        return getModuleByDomain(domain).getIncludedModuleNames().stream().map(moduleName -> getModuleByName(moduleName)
                .getDomain()).filter(Objects::nonNull).toList();
    }

    /**
     * Gets all the supported domains based on the modules in the module registry.
     *
     * @return the set of domains
     */
    @Cacheable("availableDomains")
    public static Set<String> getDomains() {
        return moduleRegistry.values().stream().map(Module::getDomain).filter(Objects::nonNull).collect(Collectors
                .toCollection(TreeSet::new));
    }

    // Entities section

    /**
     * Initializes the entity types. Once set cannot be overridden.
     *
     * @param entityTypes
     *     - entity types
     */
    static void initializeEntityTypes(final List<EntityType> entityTypes) {
        entityTypes.sort(Comparator.comparing(EntityType::getName));
        SchemaRegistry.entityTypes = Collections.unmodifiableList(entityTypes);
    }

    /**
     * Gets all the supported entity names.
     *
     * @return the list of entity names
     */
    public static List<String> getEntityNames() {
        return entityTypes.stream().map(EntityType::getName).toList();
    }

    /**
     * Deprecated: Gets the {@link EntityType} by the given name.
     *
     * @param name
     *     - entity name
     * @return the {@link EntityType}
     * @deprecated Use {@link #getEntityTypeByDomainAndName(String, String)} or
     *     {@link #getEntityTypeByModuleAndName(String, String)} instead.
     */
    @Nullable
    @Deprecated(since = "Entity type should be retrieved by supplying the domain or module name")
    public static EntityType getEntityTypeByName(String name) {
        return entityTypes.stream().filter(entityType -> entityType.getName().equals(name)).findFirst().orElseGet(() -> {
            log.warn("Unknown entity name: {}", name);
            return null;
        });
    }

    /**
     * Gets the {@link EntityType} by the given domain name and the entity type name.
     * Since TEIV supports TEIV as top level domain on the exposure side, there is possibility to return more than one
     * EntityType for a given name with domain as TEIV. In this instance, an error will be thrown because this is not yet
     * implemented in TEIV.
     *
     * @param domain
     *     - name of the domain
     * @param entityTypeName
     *     - name of the entityType
     * @return the {@link EntityType}
     * @throws SchemaRegistryException
     *     if entity type is not found in the domain
     */
    @Cacheable("entityTypeByDomainAndName")
    public static EntityType getEntityTypeByDomainAndName(final String domain, final String entityTypeName)
            throws SchemaRegistryException {
        final List<EntityType> matchedEntityTypes = getEntityTypesByDomain(domain).stream().filter(entityType -> entityType
                .getName().equals(entityTypeName)).toList();
        if (matchedEntityTypes.size() == 1) {
            return matchedEntityTypes.get(0);
        } else if (matchedEntityTypes.isEmpty()) {
            log.warn("Domain: {} does not contain the entity type: {}", domain, entityTypeName);
            throw new SchemaRegistryException(ENTITY_NOT_FOUND_IN_DOMAIN, String.format(
                    "Entity type: %s not found in domain: %s", entityTypeName, domain));
        } else {
            if (domain.equals(TEIV_DOMAIN)) {
                log.warn("Domain: {} contains duplicate entity type: {}. This is yet to be implemented in TEIV.", domain,
                        entityTypeName);
                throw new SchemaRegistryException(DUPLICATE_ENTITY_NAME_IN_TEIV_DOMAIN, String.format(
                        "Duplicate entity type: %s found in domain: %s. This is yet to be implemented in TEIV.",
                        entityTypeName, domain));
            }
            log.warn("Domain: {} contains duplicate entity type: {}. This is not supported in TEIV.", domain,
                    entityTypeName);
            throw new SchemaRegistryException(DUPLICATE_ENTITY_NAME_IN_DOMAIN, String.format(
                    "Duplicate entity type: %s found in domain: %s. This is not supported in TEIV.", entityTypeName,
                    domain));
        }
    }

    /**
     * Gets the {@link EntityType} by the given module name and the entity type name.
     *
     * @param moduleName
     *     - name of the module
     * @param entityTypeName
     *     - name of the entityType
     * @return the list of {@link EntityType}
     * @throws SchemaRegistryException
     *     if entity type is not found in the module
     */
    public static EntityType getEntityTypeByModuleAndName(final String moduleName, final String entityTypeName)
            throws SchemaRegistryException {
        return entityTypes.stream().filter(entityType -> entityType.getModule().getName().equals(moduleName) && entityType
                .getName().equals(entityTypeName)).findFirst().orElseThrow(() -> {
                    log.warn("Module: {} does not contain the entity type: {}", moduleName, entityTypeName);
                    return new SchemaRegistryException(ENTITY_NOT_FOUND_IN_MODULE, String.format(
                            "Entity type: %s not found in module: %s", entityTypeName, moduleName));
                });
    }

    /**
     * Gets the {@link Persistable} by the given module name and the topology type name.
     *
     * @param moduleName
     *     - name of the module
     * @param topologyTypeName
     *     - name of the topologyType
     * @return the topology type
     */
    public static Persistable getTopologyTypeByModuleAndTopologyName(final String moduleName, final String topologyTypeName)
            throws SchemaRegistryException {
        try {
            return getEntityTypeByModuleAndName(moduleName, topologyTypeName);
        } catch (SchemaRegistryException e) {
            log.debug("Failed to get entity type by module: {} and topology: {}. Attempting relation type lookup.",
                    moduleName, topologyTypeName, e);
            return getRelationTypeByModuleAndName(moduleName, topologyTypeName);
        }
    }

    /**
     * Gets the list of {@link EntityType} by the given domain.
     *
     * @param domain
     *     - name of the domain
     * @return the list of {@link EntityType}
     */
    public static List<EntityType> getEntityTypesByDomain(String domain) {
        List<String> domains = getIncludedDomains(domain);
        return entityTypes.stream().filter(entityType -> {
            String entityDomain = entityType.getModule().getDomain();
            return domains.contains(entityDomain) || entityDomain.equals(domain);
        }).toList();
    }

    /**
     * Gets the list of entity names by the given domain.
     *
     * @param domain
     *     - name of the domain
     * @return the list of entity names
     */
    @Cacheable("entityTypesByDomain")
    public static List<String> getEntityNamesByDomain(String domain) {
        return getEntityTypesByDomain(domain).stream().map(EntityType::getName).sorted().toList();
    }

    //Relations section

    /**
     * Initializes the relation types. Once set cannot be overridden.
     *
     * @param relationTypes
     *     - relation types
     */
    static void initializeRelationTypes(final List<RelationType> relationTypes) {
        relationTypes.sort(Comparator.comparing(RelationType::getName));
        SchemaRegistry.relationTypes = Collections.unmodifiableList(relationTypes);
    }

    /**
     * Gets all the supported relation names.
     *
     * @return the list of relation names.
     */
    public static List<String> getRelationNames() {
        return relationTypes.stream().map(RelationType::getName).toList();
    }

    /**
     * Deprecated: Gets the relation type by the given name.
     *
     * @param name
     *     - relation name
     * @return the {@link RelationType}
     * @deprecated Use {@link #getRelationTypeByDomainAndName(String, String)} or
     *     {@link #getRelationTypeByModuleAndName(String, String)} instead.
     */
    @Nullable
    @Deprecated(since = "Relation type should be retrieved by supplying the domain or module name")
    public static RelationType getRelationTypeByName(String name) {
        return relationTypes.stream().filter(entityType -> entityType.getName().equals(name)).findFirst().orElseGet(() -> {
            log.warn("Unknown relationship name: {}", name);
            return null;
        });
    }

    /**
     * Gets the {@link RelationType} by the given domain name and the relation type name.
     * Since TEIV supports TEIV as top level domain on the exposure side, there is possibility to return more than one
     * RelationType for a given name with domain as TEIV. In this instance, an error will be thrown because this is not yet
     * implemented in TEIV.
     *
     * @param domain
     *     - name of the domain
     * @param relationTypeName
     *     - name of the relation type
     * @return the {@link RelationType}
     * @throws SchemaRegistryException
     *     if relation type is not found in the domain
     */
    @Cacheable("relationTypeByDomainAndName")
    public static RelationType getRelationTypeByDomainAndName(final String domain, final String relationTypeName)
            throws SchemaRegistryException {
        final List<RelationType> matchedRelationTypes = getRelationTypesByDomain(domain).stream().filter(
                relationType -> relationType.getName().equals(relationTypeName)).toList();
        if (matchedRelationTypes.size() == 1) {
            return matchedRelationTypes.get(0);
        } else if (matchedRelationTypes.isEmpty()) {
            log.warn("Domain: {} does not contain the relation type: {}", domain, relationTypeName);
            throw new SchemaRegistryException(RELATIONSHIP_NOT_FOUND_IN_DOMAIN, String.format(
                    "Relation type: %s not found in domain: %s", relationTypeName, domain));
        } else {
            if (domain.equals(TEIV_DOMAIN)) {
                log.warn("Domain: {} contains duplicate relation type: {}. This is yet to be implemented in TEIV.", domain,
                        relationTypeName);
                throw new SchemaRegistryException(DUPLICATE_RELATION_NAME_IN_TEIV_DOMAIN, String.format(
                        "Duplicate relation type: %s found in domain: %s. This is yet to be implemented in TEIV.",
                        relationTypeName, domain));
            }
            log.warn("Domain: {} contains duplicate relation type: {}. This is not supported in TEIV.", domain,
                    relationTypeName);
            throw new SchemaRegistryException(DUPLICATE_RELATION_NAME_IN_DOMAIN, String.format(
                    "Duplicate relation type: %s found in domain: %s. This is not supported in TEIV.", relationTypeName,
                    domain));
        }
    }

    /**
     * Gets the {@link RelationType} by the given module name and the relation type name.
     *
     * @param moduleName
     *     - name of the module
     * @param relationTypeName
     *     - name of the relation type
     * @return the {@link RelationType}
     * @throws SchemaRegistryException
     *     if relation type is not found in the module
     */
    public static RelationType getRelationTypeByModuleAndName(final String moduleName, final String relationTypeName)
            throws SchemaRegistryException {
        return relationTypes.stream().filter(relationType -> relationType.getModule().getName().equals(
                moduleName) && relationType.getName().equals(relationTypeName)).findFirst().orElseThrow(() -> {
                    log.warn("Module: {} does not contain the relation type: {}", moduleName, relationTypeName);
                    return new SchemaRegistryException(RELATIONSHIP_NOT_FOUND_IN_MODULE, String.format(
                            "Relation type: %s not found in module: %s", relationTypeName, moduleName));
                });
    }

    /**
     * Gets all the relation types for the given entity name.
     *
     * @param entityName
     *     - entity name
     * @return the list of the {@link RelationType}
     */
    public static List<RelationType> getRelationTypesByEntityName(final String entityName) {
        return relationTypes.stream().filter(relationType -> relationType.getASide().getName().equals(
                entityName) || relationType.getBSide().getName().equals(entityName)).toList();
    }

    public static List<String> getRelationNamesByEntityName(final String entityName) {
        return getRelationTypesByEntityName(entityName).stream().map(RelationType::getName).toList();
    }

    public static List<String> getAssociationNamesByEntityName(final String entityName) {
        List<String> associations = new ArrayList<>();
        getRelationTypesByEntityName(entityName).stream().forEach(relationType -> {
            if (relationType.getASide().getName().equals(entityName)) {
                associations.add(relationType.getASideAssociation().getName());
            }
            if (relationType.getBSide().getName().equals(entityName)) {
                associations.add(relationType.getBSideAssociation().getName());
            }
        });
        return associations;
    }

    /**
     * Gets the relation types by the given domain.
     *
     * @param domain
     *     - name of the domain
     * @return the list of {@link RelationType}
     */
    public static List<RelationType> getRelationTypesByDomain(String domain) {
        List<String> includedDomains = getIncludedDomains(domain);
        return relationTypes.stream().filter(relationType -> {
            String relDomain = relationType.getModule().getDomain();
            return includedDomains.contains(relDomain) || relDomain.equals(domain);
        }).toList();
    }

    /**
     * Gets the relation names by the given domain.
     *
     * @param domain
     *     - name of the domain
     * @return the list of relation names
     */
    @Cacheable("relationTypesByDomain")
    public static List<String> getRelationNamesByDomain(String domain) {
        return getRelationTypesByDomain(domain).stream().map(RelationType::getName).sorted().toList();
    }

    /**
     * Gets the relation names for a given entity by the given domain.
     *
     * @param entityName
     *     - entity name
     * @param domain
     *     - name of the domain
     * @return the list of relation names
     */
    @Cacheable("relationTypesByDomain")
    public static List<RelationType> getRelationNamesForEntityByDomain(final String entityName, final String domain) {
        return getRelationTypesByDomain(domain).stream().filter(relationType -> relationType.getASide().getName().equals(
                entityName) || relationType.getBSide().getName().equals(entityName)).toList();
    }

    public static List<RelationType> getAllRelationNamesByAssociationName(String associationName) {
        return relationTypes.stream().filter(relationType -> relationType.getASideAssociation().getName().equals(
                associationName) || relationType.getBSideAssociation().getName().equals(associationName)).toList();
    }

    public static String getReferenceColumnName(RelationType relationType) {
        if (relationType.getRelationshipStorageLocation().equals(RelationshipDataLocation.A_SIDE)) {
            return Objects.requireNonNull(relationType).getTableName() + "." + String.format(TeivConstants.QUOTED_STRING,
                    relationType.bSideColumnName());
        } else if (relationType.getRelationshipStorageLocation().equals(RelationshipDataLocation.B_SIDE)) {
            return Objects.requireNonNull(relationType).getTableName() + "." + String.format(TeivConstants.QUOTED_STRING,
                    relationType.aSideColumnName());
        }
        return Objects.requireNonNull(relationType).getTableName() + "." + relationType.getIdColumnName();
    }

    public static EntityType getEntityTypeOnAssociationSide(RelationType relationType, String associationName) {
        boolean isAssociationASide = relationType.getASideAssociation().getName().equals(associationName);
        if (isAssociationASide) {
            return relationType.getASide();
        }
        return relationType.getBSide();
    }

    public static EntityType getAssociationSideEntity(String associationName) {
        RelationType relationType = getRelationTypes().stream().filter(relation -> (relation.getASideAssociation().getName()
                .equals(associationName) || relation.getBSideAssociation().getName().equals(associationName))).findFirst()
                .orElseGet(() -> {
                    log.warn("Unknown association name {}", associationName);
                    return null;
                });
        return relationType.getASideAssociation().getName().equals(associationName) ?
                relationType.getBSide() :
                relationType.getASide();
    }

    public static Set<EntityType> getAllEntityForAssociation(String associationName) {
        Set<EntityType> entities = new HashSet<>();

        getRelationTypes().stream().forEach(relation -> {
            if (relation.getASideAssociation().getName().equals(associationName) || relation.getBSideAssociation().getName()
                    .equals(associationName)) {
                entities.add(relation.getASide());
                entities.add(relation.getBSide());
            }
        });
        entities.remove(getAssociationSideEntity(associationName));
        return entities;
    }
}
