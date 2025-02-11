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
package org.oran.smo.teiv.exception;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class TiesException extends RuntimeException {

    private final HttpStatus status;
    private final String message;
    private final String details;
    private final Exception exception;

    public static TiesException unknownModule(final String module) {
        return clientException("Unknown module", String.format("Unknown module: %s", module));
    }

    public static TiesException unknownTopologyObjectType(final String topologyObject) {
        return serverException("Unknown topology object type", String.format("Unknown topology object type: %s",
                topologyObject), null);
    }

    //  Request validation
    public static TiesException unknownDomain(final String domain, final Collection<String> domains) {
        return clientException("Unknown domain", String.format("Unknown domain: %s, known domains: %s", domain, domains));
    }

    public static TiesException unknownEntityType(final String entityType, final Collection<String> entityTypes) {
        return clientException("Unknown entity type", String.format(
                "Entity type %s is not part of the model, known entity types: %s", entityType, entityTypes));
    }

    public static TiesException unknownEntityTypeInDomain(final String entityType, final String domain,
            final Collection<String> entityTypesInDomain) {
        return clientException("Unknown entity type", String.format(
                "Entity type %s is not part of the domain %s, known entity types: %s", entityType, domain,
                entityTypesInDomain));
    }

    public static TiesException unknownRelationshipType(final String relationshipType,
            final Collection<String> relationshipTypes) {
        return clientException("Unknown relationship type", String.format(
                "Relationship type %s is not part of the model, known relationship types: %s", relationshipType,
                relationshipTypes));
    }

    public static TiesException unknownRelationshipTypeInDomain(final String relationshipType, final String domain,
            final Collection<String> relationshipTypesInDomain) {
        return clientException("Unknown relationship type", String.format(
                "Relationship type %s is not part of the domain %s, known relationship types: %s", relationshipType, domain,
                relationshipTypesInDomain));
    }

    // Schema validation
    public static TiesException invalidSchema(final String name) {
        return clientException("Invalid schema name", String.format("Invalid schema name: %s", name));
    }

    public static TiesException invalidFileInput(final String error) {
        return clientException("Invalid file input", String.format("Invalid file input: %s", error));
    }

    public static TiesException duplicateEntryForClassifiers(final String name) {
        return clientException("Duplicate entry in schema", String.format("Duplicate classifier present in schema: %s",
                name));
    }

    public static TiesException duplicateEntryForDecorators(final String name) {
        return clientException("Duplicate entry in schema", String.format("Duplicate decorator present in schema: %s",
                name));
    }

    public static TiesException schemaNotOwned(final String name) {
        return new TiesException("Forbidden", String.format("Schema %s is not owned by user", name), HttpStatus.FORBIDDEN,
                null);
    }

    public static TiesException serverSQLException() {
        return serverException("Sql exception during query execution", "Please check the logs for more details", null);
    }

    public static TiesException unParsedTopologyObjectType(String topologyObjectName) {
        return serverException(String.format("Un parsed topology object type: %s", topologyObjectName),
                "Please check the logs for more details", null);
    }

    public static TiesException invalidContainerType(String containerType) {
        return serverException(String.format("Invalid container type: %s", containerType),
                "Please check the logs for more details", null);
    }

    public static TiesException invalidAssociationType(final String association) {
        return serverException(String.format("Invalid association type: %s", association),
                "Please check the logs for more details", null);
    }

    public static TiesException resourceNotFoundException(final String id) {
        return new TiesException("Resource Not Found", String.format("The requested resource is not found. ID: %s", id),
                HttpStatus.NOT_FOUND, null);
    }

    public static TiesException resourceNotFoundException(Set<String> entityIds, Set<String> relationshipIds) {
        return new TiesException("Resource Not Found", String.format(
                "The requested resource with the following ids cannot be found. Entities: %s Relationships: %s", entityIds,
                relationshipIds), HttpStatus.NOT_FOUND, null);
    }

    public static TiesException invalidClassifiersException(List<String> classifiers) {
        return new TiesException("Invalid classifiers", String.format("The provided classifiers are invalid %s",
                classifiers), HttpStatus.NOT_FOUND, null);
    }

    public static TiesException invalidDecoratorsException(Map<String, String> problems) {
        return new TiesException("Invalid decorators", String.format("The provided decorators are invalid %s", problems),
                HttpStatus.NOT_FOUND, null);
    }

    public static TiesException invalidValueException(String valueName, Integer valueLimit, Boolean isLowerLimit) {
        return clientException("Invalid Value", String.format("%s cannot be %s than %d", valueName, isLowerLimit ?
                "larger" :
                "lower", valueLimit));
    }

    public static TiesException invalidJsonFormat(Exception exception) {
        return serverException("Invalid json format", "Please check the logs for more details", exception);
    }

    public static TiesException serverException(String message, String details, Exception exception) {
        return new TiesException(message, details, HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    public static TiesException clientException(String message, String details) {
        return new TiesException(message, details, HttpStatus.BAD_REQUEST, null);
    }

    public static TiesException schemaInDeletingState(String moduleName) {
        return new TiesException("Schema in deleting state", String.format(
                "Schema %s already exists and is in the process of being deleted. This may take some time, please try again later.",
                moduleName), HttpStatus.CONFLICT, null);
    }

    private TiesException(String message, String details, HttpStatus status, Exception exception) {
        this.status = status;
        this.message = message;
        this.details = details;
        this.exception = exception;
    }
}
