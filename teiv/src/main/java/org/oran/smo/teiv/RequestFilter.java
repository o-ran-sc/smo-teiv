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

package org.oran.smo.teiv;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import jakarta.servlet.Filter;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.oran.smo.teiv.exposure.OranTeivErrorJsonMessage;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;

public abstract class RequestFilter implements Filter {
    protected void writeError(final ServletResponse servletResponse, final JsonProcessingException exception,
            final String message) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        httpResponse.setContentType("application/problem+json");
        final OranTeivErrorJsonMessage errorMessage = OranTeivErrorJsonMessage.extendedBuilder().status(
                HttpStatus.BAD_REQUEST.name()).message(message).details(getErrorDetails(exception)).build();
        httpResponse.getWriter().write(errorMessage.toJson());
        httpResponse.getWriter().flush();
    }

    private String getErrorDetails(JsonProcessingException exception) {
        String details;
        if (exception instanceof ValueInstantiationException e && exception
                .getCause()instanceof IllegalArgumentException cause) {
            final Class<?> targetType = e.getType().getRawClass();
            details = cause.getMessage() + ". Invalid value for " + targetType.getSimpleName() + ", allowed values: " + List
                    .of(targetType.getEnumConstants());
        } else {
            details = exception.getOriginalMessage();
        }
        return details;
    }
}
