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
package org.oran.smo.ncmp_to_teiv_adapter.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class NRCellCUAttributes extends Attributes {

    @Getter
    @JsonProperty("cellLocalId")
    private Integer cellLocalId;
    @Getter
    @JsonProperty("nCI")
    private Integer nCI;
    @Getter
    @JsonProperty("primaryPLMNId")
    private Plmnid primaryPLMNId;
    @Getter
    @JsonProperty("nRTAC")
    private Integer nRTAC;

    public Map<String, Object> createEntityAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        if (this.cellLocalId != null) {
            attributes.put("cellLocalId", this.cellLocalId);
        }
        if (this.nCI != null) {
            attributes.put("nCI", this.nCI);
        }
        if (this.nRTAC != null) {
            attributes.put("nRTAC", this.nRTAC);
        }
        if (this.primaryPLMNId != null) {
            attributes.put("plmnId", this.primaryPLMNId);
        }
        return attributes;
    }
}
