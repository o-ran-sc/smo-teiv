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
package org.oran.smo.teiv.exception;

public class CloudEventParserException extends Exception {
    protected CloudEventParserException(final String message) {
        super(message);
    }

    private CloudEventParserException(final String issue, final String type, final String node, final String cause) {
        super(String.format("Error %s %s: %s Cause: %s", issue, type, node, cause));
    }

    public static CloudEventParserException noEventData() {
        return new CloudEventParserException("CloudEvent doesn't contain data.");
    }

    public static CloudEventParserException invalidStructure(final String node) {
        return new CloudEventParserException(String.format(
                "Invalid structure. Event elements must be an array of JSON objects. Node: %s", node));
    }

    public static CloudEventParserException eventDataReading(final String node, final Exception e) {
        return new CloudEventParserException("reading", "cloudevent", node, e.getMessage());
    }

    public static CloudEventParserException entityParsing(final String node, final Exception e) {
        return new CloudEventParserException("parsing", "entity", node, e.getMessage());
    }

    public static CloudEventParserException entityValidating(final String node, final Exception e) {
        return new CloudEventParserException("validating", "entity", node, e.getMessage());
    }

    public static CloudEventParserException relationshipParsing(final String node, final Exception e) {
        return new CloudEventParserException("parsing", "relationship", node, e.getMessage());
    }

    public static CloudEventParserException relationshipValidating(final String node, final Exception e) {
        return new CloudEventParserException("validating", "relationship", node, e.getMessage());
    }
}
