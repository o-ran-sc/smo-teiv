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
package org.oran.smo.teiv.utils.query.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Getter
public class TeivPathException extends RuntimeException {

    private final String message;
    private final String details;
    private final HttpStatus httpStatus;
    private final transient List<Object> response;
    private static final List<Object> defaultResponse = Collections.emptyList();

    public static TeivPathException invalidRelationshipName(final String relationship) {
        return clientException("Invalid relationship name", String.format("%s is not a known relationship", relationship));
    }

    public static TeivPathException noConnectionFound(final String object) {
        return clientException("Objects are not related", String.format(
                "No relationship can be found between %s and scopeFilter elements", object));
    }

    public static TeivPathException noConnectionFoundWhenRootIsNull() {
        return clientException("Objects are not related",
                "None of the elements in the targetFilter is part of connection provided in relationships filter");
    }

    public static TeivPathException grammarError(final String message) {
        return clientException("Grammar error", message);
    }

    public static TeivPathException columnNameError(String entity, String column) {
        return clientException("Grammar Error", String.format("%s is not a valid attribute of %s", column, entity));
    }

    public static TeivPathException columnNamesError(String entity, List<String> columns) {
        if (columns.size() == 1) {
            return columnNameError(entity, columns.get(0));
        } else {
            return clientException("Invalid parameter error", String.format("%s are not valid attributes of %s", String
                    .join(", ", columns), entity));
        }
    }

    public static TeivPathException sourceIdNameError(String entity) {
        return clientException("Invalid parameter error", String.format("Invalid source id parameter provided for %s",
                entity));
    }

    private static TeivPathException clientException(final String message, final String details) {
        return new TeivPathException(message, details, HttpStatus.BAD_REQUEST, null);
    }

    public static TeivPathException idAmongAttributesError() {
        return clientException("Grammar Error", "ID is not considered to be an attribute");
    }

    public static TeivPathException invalidQueryError() {
        return clientException("Invalid query", "The provided query in scopeFilter is invalid");
    }

    public static TeivPathException invalidTargetFilter(String topologyObject) {
        return clientException("Invalid target filter, only relationship conditions can be provided", String.format(
                "%s is not a valid relation", topologyObject));
    }

    public static TeivPathException entityNameError(String entity) {
        return clientException("Grammar Error", String.format("%s is not a valid entity", entity));
    }

    public static TeivPathException invalidTopologyObject(String topologyObject) {
        return clientException("Invalid topology object", String.format(
                "%s did not match any topology objects in the given domain", topologyObject));
    }

    public static TeivPathException invalidDataInScopeFilter(String data) {
        return clientException("Invalid data in scopeFilter", String.format("Unable to resolve %s", data));
    }

    public static TeivPathException invalidTargetFilter(Set<String> params) {
        return clientException("Invalid targetFilter content", String.format(
                "%s did not match any topology objects in the given domain", String.join(", ", params)));
    }

    public static TeivPathException invalidTargetFilter() {
        return clientException("Invalid targetFilter content", "Given targetFilters could not be resolved");
    }

    public static TeivPathException invalidTargetFilterScopeFilterCombination() {
        return clientException("Invalid targetFilter and scopeFilter content",
                "Given targetFilter and scopeFilter can not be combined");
    }

    public static TeivPathException invalidScopeFilter(String leaf) {
        return clientException("Invalid scopeFilter content", String.format(
                "%s did not match any topology objects in the given domain", leaf));
    }

    public static TeivPathException invalidMetadaFilter(String leaf) {
        return clientException("Invalid metadata content", String.format("%s is not a valid metadata", leaf));
    }

    public static TeivPathException ambiguousTopologyObject(String topologyObject) {
        return clientException("Invalid topology object", String.format(
                "%s is ambiguous, %s matches multiple topology object types", topologyObject, topologyObject));
    }

    public static TeivPathException invalidAssociation(String topologyObject, String associationName) {
        return clientException("Invalid association name", String.format(
                "%s is not a valid association name for topology object %s", associationName, topologyObject));
    }

    public static TeivPathException invalidParamsForAssociation(String associationName) {
        return clientException("Invalid parameters for association", String.format(
                "Invalid parameters provided for association %s", associationName));
    }

    public static TeivPathException containerValidationWithUndefinedTopologyObjectType(String topologyObject) {
        return clientException("Container validation error", String.format(
                "Container validation is not possible for undefined %s", topologyObject));
    }

    public static TeivPathException notMatchingScopeAndTargetFilter() {
        return clientException("Filter Error", "TopologyObjects given in scopeFilter and targetFilter are not matching");
    }

    public static TeivPathException invalidQueryCondition(String details) {
        return clientException("Invalid query condition", details);
    }

    public static TeivPathException invalidDataType() {
        return clientException("Invalid data type", "Invalid data type provided for scopeFilter");
    }

    private TeivPathException(final String message, final String details, final HttpStatus httpStatus,
            final List<Object> response) {
        this.message = message;
        this.details = details;
        this.httpStatus = httpStatus;
        this.response = response != null ? new ArrayList<>(response) : null;
    }
}
