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

import org.oran.smo.teiv.availability.DependentServiceAvailabilityKafka;
import org.springframework.boot.actuate.health.Health;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Health Check component for TEIV exposure.
 */

@Component
@Profile("exposure")
public class TeivExposureHealthIndicator extends TeivKafkaHealthIndicator {

    public TeivExposureHealthIndicator(HealthStatus healthStatus,
            DependentServiceAvailabilityKafka dependentServiceAvailabilityKafka) {
        super(healthStatus, dependentServiceAvailabilityKafka);
    }

    @Override
    protected String getServiceName() {
        return "topology-exposure-inventory";
    }

    @Override
    public Health health() {
        if (!healthStatus.isSchemaInitialized()) {
            return healthDown("Schema is yet to be initialized.");
        }
        return healthUp();
    }
}
