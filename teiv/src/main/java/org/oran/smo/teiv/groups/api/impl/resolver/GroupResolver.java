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
package org.oran.smo.teiv.groups.api.impl.resolver;

import org.oran.smo.teiv.api.model.OranTeivMembersResponse;
import org.oran.smo.teiv.exposure.utils.RequestDetails;
import org.oran.smo.teiv.groups.spi.GroupNotFoundException;

public interface GroupResolver {

    /**
     * Resolves the members of the group.
     *
     * @param groupId
     *     - group id
     * @param requestDetails
     *     - request details
     * @return resolved members of the group with pagination
     * @throws GroupNotFoundException
     *     if group id doesn't exists
     */
    OranTeivMembersResponse resolve(final String groupId, final RequestDetails requestDetails)
            throws GroupNotFoundException;
}
