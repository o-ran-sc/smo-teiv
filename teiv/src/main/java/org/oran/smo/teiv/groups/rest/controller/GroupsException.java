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
package org.oran.smo.teiv.groups.rest.controller;

import org.springframework.http.HttpStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupsException extends RuntimeException {
    private final HttpStatus status;
    private final String message;
    private final String details;
    private final Exception exception;

    public static GroupsException resourceNotFoundException() {
        return GroupsException.builder().status(HttpStatus.NOT_FOUND).message("Resource Not Found").details(
                "The requested group is not found").build();
    }

    public static GroupsException criteriaSerializationException(final String msg) {
        return GroupsException.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).message(
                "Criteria serialization exception").details(msg).build();
    }

    public static GroupsException criteriaDeserializationException(final String msg) {
        return GroupsException.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).message(
                "Criteria deserialization exception").details(msg).build();
    }

    public static GroupsException providedMembersSerializationException(final String msg) {
        return GroupsException.builder().status(HttpStatus.BAD_REQUEST).message("Provided members serialization exception")
                .details(msg).build();
    }

    public static GroupsException invalidProvidedMembers(final String msg) {
        return GroupsException.builder().status(HttpStatus.BAD_REQUEST).message("Invalid providedMembers").details(String
                .format("Unable to parse the given providedMembers. %s", msg)).build();
    }

    public static GroupsException providedMembersSingleRequestLimitViolationException(final int proposedInsertCount,
            final int insertMaxLimit) {
        return GroupsException.builder().status(HttpStatus.BAD_REQUEST).message("Limit exceeded").details(String.format(
                "The total number of topology identifiers in the request is %s. The maximum allowed in a single request is %s.",
                proposedInsertCount, insertMaxLimit)).build();
    }

    public static GroupsException providedMembersMaxLimitViolationException(final int proposedInsertCount,
            final int maxLimit) {
        return GroupsException.builder().status(HttpStatus.BAD_REQUEST).message("Limit exceeded").details(String.format(
                "Merging topology identifiers in request to the group will result in %s members. The maximum allowed members in the static group is %s.",
                proposedInsertCount, maxLimit)).build();
    }

    public static GroupsException providedMembersUpdateException(final String msg) {
        return GroupsException.builder().status(HttpStatus.BAD_REQUEST).message("Provided members update exception")
                .details(msg).build();
    }

    public static GroupsException invalidStatusException(final String msg) {
        return GroupsException.builder().status(HttpStatus.BAD_REQUEST).message("Invalid status").details(msg).build();
    }

    public static GroupsException providedMembersRetrievalException(final String msg) {
        return GroupsException.builder().status(HttpStatus.BAD_REQUEST).message("Provided members retrieval exception")
                .details(msg).build();
    }
}
