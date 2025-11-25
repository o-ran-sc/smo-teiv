/*
 *  ============LICENSE_START=======================================================
 *  Modifications Copyright (C) 2025 OpenInfra Foundation Europe
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
package org.oran.smo.teiv.adapters.focom_to_teiv_adapter.custom_resource_json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class RelationshipItem {
    @JsonProperty("id")
    private String id;

    @Getter(onMethod_ = @JsonProperty("aSide"))
    @Setter(onMethod_ = @JsonProperty("aSide"))
    @JsonProperty("aSide")
    private String aSide;

    @Getter(onMethod_ = @JsonProperty("bSide"))
    @Setter(onMethod_ = @JsonProperty("bSide"))
    @JsonProperty("bSide")
    private String bSide;

    @JsonProperty("sourceIds")
    private List<String> sourceIds;
}

