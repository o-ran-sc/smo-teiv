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
package org.oran.smo.teiv.exposure.teivpath.innerlanguage;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder(builderMethodName = "hiddenBuilder")
public class TargetObject {
    @NonNull
    private String topologyObject;
    @Builder.Default
    private TopologyObjectType topologyObjectType = TopologyObjectType.UNDEFINED;
    @Builder.Default
    private ContainerType container = ContainerType.ID;
    @Builder.Default
    private List<String> params = List.of();

    private boolean isAllParamQueried;

    private boolean isGenerated;

    public static TargetObjectBuilder builder(final String topologyObject) {
        return hiddenBuilder().topologyObject(topologyObject);
    }
}
