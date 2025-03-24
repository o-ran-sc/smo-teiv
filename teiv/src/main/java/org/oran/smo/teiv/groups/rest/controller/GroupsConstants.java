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

import lombok.experimental.UtilityClass;

@UtilityClass
public class GroupsConstants {
    public static final String MEMBERS_HREF_TEMPLATE = "/groups/%s/members";
    public static final String PROVIDED_MEMBERS_HREF_TEMPLATE = "/groups/%s/provided-members";
    public static final String GROUP_ID_PREFIX = "urn:o-ran:smo:teiv:group=";
    public static final String RESOURCE_NOT_FOUND = "Resource Not Found";
}
