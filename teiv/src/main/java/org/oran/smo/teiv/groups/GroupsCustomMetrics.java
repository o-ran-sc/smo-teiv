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
package org.oran.smo.teiv.groups;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Data;

@Data
@Component
@Profile("groups")
public class GroupsCustomMetrics {

    private final Counter httpCreateGroupFailedCount;
    private final Counter httpGetAllGroupsFailedCount;
    private final Counter httpGetGroupByIdFailedCount;
    private final Counter httpDeleteGroupFailedCount;
    private final Counter httpUpdateGroupNameFailedCount;
    private final Counter httpGetMembersFailedCount;
    private final Counter httpGetProvidedMembersFailedCount;
    private final Counter httpUpdateProvidedMembersFailedCount;

    public GroupsCustomMetrics(final MeterRegistry meterRegistry) {
        httpCreateGroupFailedCount = Counter.builder("teiv_groups_http_create_group_fail_total").register(meterRegistry);
        this.httpGetAllGroupsFailedCount = Counter.builder("teiv_groups_http_get_all_groups_fail_total").register(
                meterRegistry);
        this.httpGetGroupByIdFailedCount = Counter.builder("teiv_groups_http_get_group_by_id_fail_total").register(
                meterRegistry);
        this.httpDeleteGroupFailedCount = Counter.builder("teiv_groups_http_delete_group_fail_total").register(
                meterRegistry);
        this.httpUpdateGroupNameFailedCount = Counter.builder("teiv_groups_http_update_group_name_fail_total").register(
                meterRegistry);
        this.httpGetMembersFailedCount = Counter.builder("teiv_groups_http_get_members_fail_total").register(meterRegistry);
        this.httpGetProvidedMembersFailedCount = Counter.builder("teiv_groups_http_get_provided_members_fail_total")
                .register(meterRegistry);
        this.httpUpdateProvidedMembersFailedCount = Counter.builder("teiv_groups_http_update_provided_members_fail_total")
                .register(meterRegistry);
    }

    /**
     * It increments the httpCreateGroupFailedCount metric.
     */
    public void incrementHttpCreateGroupFailedCount() {
        httpCreateGroupFailedCount.increment();
    }

    /**
     * It increments the httpGetAllGroupsFailedCount metric.
     */
    public void incrementHttpGetAllGroupsFailedCount() {
        httpGetAllGroupsFailedCount.increment();
    }

    /**
     * It increments the httpGetGroupByIdFailedCount metric.
     */
    public void incrementHttpGetGroupByIdFailedCount() {
        httpGetGroupByIdFailedCount.increment();
    }

    /**
     * It increments the httpDeleteGroupFailedCount metric.
     */
    public void incrementHttpDeleteGroupFailedCount() {
        httpDeleteGroupFailedCount.increment();
    }

    /**
     * It increments the httpUpdateGroupNameFailedCount metric.
     */
    public void incrementHttpUpdateGroupNameFailedCount() {
        httpUpdateGroupNameFailedCount.increment();
    }

    /**
     * It increments the httpGetMembersFailedCount metric.
     */
    public void incrementHttpGetMembersFailedCount() {
        httpGetMembersFailedCount.increment();
    }

    /**
     * It increments the httpGetProvidedMembersFailedCount metric.
     */
    public void incrementHttpGetProvidedMembersFailedCount() {
        httpGetProvidedMembersFailedCount.increment();
    }

    /**
     * It increments the httpUpdateProvidedMembersFailedCount metric.
     */
    public void incrementHttpUpdateProvidedMembersFailedCount() {
        httpUpdateProvidedMembersFailedCount.increment();
    }
}
