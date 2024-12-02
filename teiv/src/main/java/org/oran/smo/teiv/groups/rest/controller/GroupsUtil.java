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

import static org.oran.smo.teiv.groups.rest.controller.GroupsConstants.MEMBERS_HREF_TEMPLATE;
import static org.oran.smo.teiv.groups.rest.controller.GroupsConstants.PROVIDED_MEMBERS_HREF_TEMPLATE;

import lombok.experimental.UtilityClass;

import org.oran.smo.teiv.api.model.OranTeivHref;

@UtilityClass
public class GroupsUtil {

    public static OranTeivHref generateMembersHref(final String groupId) {
        return OranTeivHref.builder().href(String.format(MEMBERS_HREF_TEMPLATE, groupId)).build();
    }

    public static OranTeivHref generateProvidedMembersHref(final String groupId) {
        return OranTeivHref.builder().href(String.format(PROVIDED_MEMBERS_HREF_TEMPLATE, groupId)).build();
    }
}
