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

package org.oran.smo.teiv.exposure;

import lombok.Builder;
import org.oran.smo.teiv.api.model.OranTeivErrorMessage;

public class OranTeivErrorJsonMessage extends OranTeivErrorMessage {
    @Builder(builderMethodName = "extendedBuilder")
    public OranTeivErrorJsonMessage(String status, final String message, final String details) {
        super(status, message, details);
    }

    public String toJson() {
        return "{" + "\"status\": \"" + this.getStatus() + "\"," + "\"message\": \"" + this
                .getMessage() + "\"," + "\"details\": \"" + this.getDetails() + "\"}";
    }
}
