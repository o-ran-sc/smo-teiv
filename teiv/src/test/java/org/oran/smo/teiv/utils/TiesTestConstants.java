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
package org.oran.smo.teiv.utils;

public class TiesTestConstants {

    public static final String APPLICATION_JSON = "application/json";
    public static final String SPRING_BOOT_SERVER_HOST = "kafka.server.bootstrap-server-host:#{environment.getProperty(\"spring.embedded.kafka.brokers\").split(\":\")[0]}";
    public static final String SPRING_BOOT_SERVER_PORT = "kafka.server.bootstrap-server-port:#{environment.getProperty(\"spring.embedded.kafka.brokers\").split(\":\")[1]}";
    public static final String KAFKA_RETRY_INTERVAL_10_MS = "kafka.availability.retryIntervalMs:10";

}
