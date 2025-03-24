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
package org.oran.smo.teiv.groups.rest.controller;

import static org.oran.smo.teiv.groups.audit.ExecutionStatus.FAILED;
import static org.oran.smo.teiv.groups.audit.ExecutionStatus.SUCCESS;
import static org.oran.smo.teiv.groups.audit.GroupOperation.CREATE;
import static org.oran.smo.teiv.groups.audit.GroupOperation.DELETE;
import static org.oran.smo.teiv.groups.audit.GroupOperation.MERGE_PROVIDED_MEMBERS;
import static org.oran.smo.teiv.groups.audit.GroupOperation.REMOVE_PROVIDED_MEMBERS;
import static org.oran.smo.teiv.groups.audit.GroupOperation.UPDATE_NAME;
import static org.oran.smo.teiv.groups.rest.controller.GroupsConstants.MEMBERS_HREF_TEMPLATE;
import static org.oran.smo.teiv.groups.rest.controller.GroupsConstants.PROVIDED_MEMBERS_HREF_TEMPLATE;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.Pair;
import org.oran.smo.teiv.api.model.OranTeivDynamicGroupByIdResponse;
import org.oran.smo.teiv.api.model.OranTeivStaticGroupByIdResponse;
import org.oran.smo.teiv.exposure.audit.LoggerHandler;
import org.oran.smo.teiv.groups.audit.AuditInfo;
import org.oran.smo.teiv.groups.audit.ExecutionStatus;
import org.oran.smo.teiv.groups.utils.GroupCreationRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.oran.smo.teiv.api.model.OranTeivUpdateProvidedMembersPayload;
import io.micrometer.core.annotation.Timed;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.oran.smo.teiv.api.GroupsApi;
import org.oran.smo.teiv.api.model.OranTeivCreateGroupPayload;
import org.oran.smo.teiv.api.model.OranTeivErrorMessage;
import org.oran.smo.teiv.api.model.OranTeivGroupByIdResponse;
import org.oran.smo.teiv.api.model.OranTeivGroupsResponse;
import org.oran.smo.teiv.api.model.OranTeivMembersResponse;
import org.oran.smo.teiv.api.model.OranTeivUpdateGroupNamePayload;
import org.oran.smo.teiv.exposure.utils.RequestDetails;
import org.oran.smo.teiv.groups.GroupsCustomMetrics;
import org.oran.smo.teiv.groups.api.GroupsService;
import org.oran.smo.teiv.utils.TeivConstants;

@Slf4j
@RestController
@RequestMapping(TeivConstants.REQUEST_MAPPING)
@RequiredArgsConstructor
@Profile("groups")
public class GroupsController implements GroupsApi {
    private final GroupsService groupsService;
    private final GroupsCustomMetrics customMetrics;
    private final LoggerHandler loggerHandler;
    private final HttpServletRequest context;

    @Override
    @Timed("teiv_groups_http_create_group_seconds")
    public ResponseEntity<OranTeivGroupByIdResponse> createGroup(final String accept, final String contentType,
            final OranTeivCreateGroupPayload createGroupPayload) {
        return runWithFailCheck(() -> {
            final OranTeivGroupByIdResponse getGroupByIdResponse;
            try {
                getGroupByIdResponse = groupsService.createGroup(createGroupPayload);
                if (getGroupByIdResponse instanceof OranTeivStaticGroupByIdResponse staticResponse) {
                    loggerHandler.logAudit(log, AuditInfo.builder().operation(CREATE).createGroupPayload(createGroupPayload)
                            .groupId(staticResponse.getId()).status(SUCCESS).build().toString(), context);
                } else if (getGroupByIdResponse instanceof OranTeivDynamicGroupByIdResponse dynamicResponse) {
                    loggerHandler.logAudit(log, AuditInfo.builder().operation(CREATE).createGroupPayload(createGroupPayload)
                            .groupId(dynamicResponse.getId()).status(SUCCESS).build().toString(), context);
                }
            } catch (final Exception ex) {
                final String exceptionMessage = (ex instanceof GroupsException groupsException) ?
                        groupsException.getDetails() :
                        ex.getLocalizedMessage();
                loggerHandler.logAudit(log, AuditInfo.builder().operation(CREATE).createGroupPayload(createGroupPayload)
                        .status(FAILED).exceptionMessage(exceptionMessage).build().toString(), context);
                log.error("Exception while creating the group: ", ex);
                throw ex;
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(getGroupByIdResponse);
        }, customMetrics::incrementHttpCreateGroupFailedCount);
    }

    @Override
    @Timed("teiv_groups_http_get_all_groups_seconds")
    public ResponseEntity<OranTeivGroupsResponse> getAllGroups(final String accept, final Integer offset,
            final Integer limit, final String name) {
        return runWithFailCheck(() -> {
            final RequestDetails requestDetails = RequestDetails.builder().basePath("/groups").offset(offset).limit(limit)
                    .queryParam("name", name).build();
            return ResponseEntity.ok(groupsService.getAllGroups(requestDetails, name));
        }, customMetrics::incrementHttpGetAllGroupsFailedCount);
    }

    @Override
    @Timed("teiv_groups_http_delete_group_seconds")
    public ResponseEntity<Void> deleteGroup(final String groupId) {
        return runWithFailCheck(() -> {
            runWithAuditLogs(() -> groupsService.deleteGroup(groupId), response -> loggerHandler.logAudit(log, AuditInfo
                    .builder().operation(DELETE).groupId(groupId).status(response.getKey()).exceptionMessage(response
                            .getValue()).build().toString(), context));
            return ResponseEntity.noContent().build();
        }, customMetrics::incrementHttpDeleteGroupFailedCount);
    }

    @Override
    @Timed("teiv_groups_http_update_group_name_seconds")
    public ResponseEntity<Void> updateGroupName(final String contentType, final String groupId,
            final OranTeivUpdateGroupNamePayload groupNameUpdatePayload) {
        return runWithFailCheck(() -> {
            runWithAuditLogs(() -> groupsService.renameGroup(groupId, groupNameUpdatePayload), response -> loggerHandler
                    .logAudit(log, AuditInfo.builder().operation(UPDATE_NAME).groupId(groupId).status(response.getKey())
                            .groupName(groupNameUpdatePayload.getName()).exceptionMessage(response.getValue()).build()
                            .toString(), context));
            return ResponseEntity.noContent().build();
        }, customMetrics::incrementHttpUpdateGroupNameFailedCount);
    }

    @Override
    @Timed("teiv_groups_http_get_members_seconds")
    public ResponseEntity<OranTeivMembersResponse> getMembers(final String accept, final String groupId,
            final Integer offset, final Integer limit) {
        return runWithFailCheck(() -> {
            final RequestDetails requestDetails = RequestDetails.builder().basePath(String.format(MEMBERS_HREF_TEMPLATE,
                    groupId)).offset(offset).limit(limit).build();
            return ResponseEntity.ok(groupsService.getResolvedMembers(groupId, requestDetails));
        }, customMetrics::incrementHttpGetMembersFailedCount);
    }

    @Override
    @Timed("teiv_groups_http_get_provided_members_seconds")
    public ResponseEntity<OranTeivMembersResponse> getProvidedMembers(final String accept, final String groupId,
            final String status, final Integer offset, final Integer limit) {
        return runWithFailCheck(() -> {
            final RequestDetails requestDetails = RequestDetails.builder().basePath(String.format(
                    PROVIDED_MEMBERS_HREF_TEMPLATE, groupId)).offset(offset).limit(limit).queryParam("status", status)
                    .build();
            return ResponseEntity.ok(groupsService.getProvidedMembersByStatus(groupId, status, requestDetails));
        }, customMetrics::incrementHttpGetProvidedMembersFailedCount);
    }

    @Override
    @Timed("teiv_groups_http_get_group_by_id_seconds")
    public ResponseEntity<OranTeivGroupByIdResponse> getGroupById(final String accept, final String groupId) {
        return runWithFailCheck(() -> ResponseEntity.ok(groupsService.getGroupById(groupId)),
                customMetrics::incrementHttpGetGroupByIdFailedCount);
    }

    @Override
    @Timed("teiv_groups_http_update_provided_members_seconds")
    public ResponseEntity<Void> updateProvidedMembers(final String accept, final String contentType, final String groupId,
            final OranTeivUpdateProvidedMembersPayload updateProvidedMembersPayload) {
        return runWithFailCheck(() -> {
            runWithAuditLogs(() -> groupsService.updateProvidedMembers(groupId, updateProvidedMembersPayload),
                    response -> loggerHandler.logAudit(log, AuditInfo.builder().operation(updateProvidedMembersPayload
                            .getOperation().getValue().equals("merge") ? MERGE_PROVIDED_MEMBERS : REMOVE_PROVIDED_MEMBERS)
                            .groupId(groupId).status(response.getKey()).providedMembers(updateProvidedMembersPayload
                                    .getProvidedMembers()).exceptionMessage(response.getValue()).build().toString(),
                            context));
            return ResponseEntity.noContent().build();
        }, customMetrics::incrementHttpUpdateProvidedMembersFailedCount);
    }

    @ExceptionHandler(GroupsException.class)
    public ResponseEntity<OranTeivErrorMessage> handleGroupsException(final GroupsException exception) {
        if (exception.getException() != null) {
            log.warn(exception.getMessage(), exception.getException());
        }
        final OranTeivErrorMessage errorMessage = OranTeivErrorMessage.builder().status(exception.getStatus().name())
                .message(exception.getMessage()).details(exception.getDetails()).build();

        return new ResponseEntity<>(errorMessage, exception.getStatus());
    }

    private <T> T runWithFailCheck(final Supplier<T> supp, final Runnable runnable) {
        try {
            return supp.get();
        } catch (Exception ex) {
            log.error("Exception during service call", ex);
            runnable.run();
            throw ex;
        }
    }

    private void runWithAuditLogs(final Runnable runnable, final Consumer<Pair<ExecutionStatus, String>> logAudit) {
        try {
            runnable.run();
            logAudit.accept(Pair.of(SUCCESS, null));
        } catch (Exception ex) {
            log.error("Exception during service call", ex);
            if (ex instanceof GroupsException groupsException) {
                logAudit.accept(Pair.of(FAILED, groupsException.getDetails()));
            } else {
                logAudit.accept(Pair.of(FAILED, ex.getMessage()));
            }
            throw ex;
        }
    }

    @Bean
    public FilterRegistrationBean<GroupCreationRequestFilter> loggingFilter() {
        FilterRegistrationBean<GroupCreationRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new GroupCreationRequestFilter(loggerHandler));
        registrationBean.addUrlPatterns("/groups");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}
