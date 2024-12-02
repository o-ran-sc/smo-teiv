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
package org.oran.smo.teiv.listener.audit;

import static org.oran.smo.teiv.listener.audit.ExecutionStatus.SUCCESS;

import org.apache.commons.lang3.StringUtils;

import lombok.Builder;

@Builder
public class IngestionAuditInfo {
    private final ExecutionStatus status;
    private final String operation;
    private final String messageKey;
    private final String cloudEvent;
    private final String exceptionMessage;

    @Override
    public String toString() {
        String baseMessage = String.format("%s - %s topology. Message key: %s, CloudEvent: %s", status, StringUtils
                .capitalize(operation), messageKey, cloudEvent);

        return status.equals(SUCCESS) ? baseMessage : (baseMessage + ", " + exceptionMessage);
    }
}
