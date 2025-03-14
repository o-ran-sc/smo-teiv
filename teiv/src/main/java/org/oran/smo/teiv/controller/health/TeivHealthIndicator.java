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
package org.oran.smo.teiv.controller.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class TeivHealthIndicator implements HealthIndicator {

    protected final HealthStatus healthStatus;

    protected abstract String getServiceName();

    protected TeivHealthIndicator(HealthStatus healthStatus) {
        this.healthStatus = healthStatus;
    }

    protected Health healthUp() {
        String message = getServiceName() + " is UP and Healthy.";
        log.debug(message);
        return Health.up().withDetail("UP", message).build();
    }

    protected Health healthDown(String reason) {
        String errorMessage = getServiceName() + " is DOWN because: " + reason;
        log.error(errorMessage);
        return Health.down().withDetail("Error", errorMessage).build();
    }
}
