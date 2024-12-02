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

import java.util.Set;
import java.util.stream.Collectors;

import org.oran.smo.yangtools.parser.findings.Finding;

public class YangValidationException extends YangException {
    public final transient Set<Finding> errorMessages;

    public YangValidationException(final String message) {
        super(message);
        errorMessages = null;
    }

    public YangValidationException(final String message, final Throwable cause) {
        super(message, cause);
        errorMessages = null;
    }

    public YangValidationException(final Set<Finding> errorMessages, final String message) {
        super(message);
        this.errorMessages = errorMessages;
    }

    public static YangValidationException validationFailed(final Set<Finding> errorMessages) {
        String message = errorMessages.stream().map(Finding::getMessage).collect(Collectors.joining("\n"));
        return new YangValidationException(errorMessages, message);
    }

    public static YangValidationException invalidSourceId(final Object sourceId) {
        return new YangValidationException("Invalid sourceId found: " + sourceId.getClass().getSimpleName());
    }
}
