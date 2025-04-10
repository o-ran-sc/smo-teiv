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
package org.oran.smo.teiv.exposure.exception;

import org.oran.smo.teiv.api.model.OranTeivErrorMessage;
import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.utils.query.exception.TeivPathException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.oran.smo.teiv.utils.ResponseUtil.getHeadersContentTypeAppProblemJson;

@Slf4j
@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ExceptionHandler(TeivException.class)
    public ResponseEntity<OranTeivErrorMessage> handleTeivException(final TeivException exception) {
        if (exception.getException() != null) {
            log.error(exception.getMessage(), exception.getException());
        }
        return ResponseEntity.status(exception.getStatus()).headers(getHeadersContentTypeAppProblemJson()).body(
                OranTeivErrorMessage.builder().status(exception.getStatus().name()).message(exception.getMessage()).details(
                        exception.getDetails()).build());
    }

    @ResponseBody
    @ExceptionHandler(TeivPathException.class)
    public ResponseEntity<Object> handleTeivPathException(final TeivPathException exception) {
        if (exception.getResponse() != null) {
            return new ResponseEntity<>(exception.getResponse(), HttpStatus.OK);
        } else {
            return ResponseEntity.status(exception.getHttpStatus()).headers(getHeadersContentTypeAppProblemJson()).body(
                    OranTeivErrorMessage.builder().status(exception.getHttpStatus().name()).message(exception.getMessage())
                            .details(exception.getDetails()).build());
        }
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<OranTeivErrorMessage> handleGeneralException(final Exception ex) {
        log.error("Handling general exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(getHeadersContentTypeAppProblemJson()).body(
                new OranTeivErrorMessage().status(HttpStatus.INTERNAL_SERVER_ERROR.name()));
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<OranTeivErrorMessage> handleConstraintViolationException(ConstraintViolationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(getHeadersContentTypeAppProblemJson()).body(
                getBadRequestErrorMessage(exception));
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException exception, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(getHeadersContentTypeAppProblemJson()).body(
                getBadRequestErrorMessage(exception));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(getHeadersContentTypeAppProblemJson()).body(
                getBadRequestErrorMessage(exception));
    }

    public static OranTeivErrorMessage getBadRequestErrorMessage(final Exception e) {
        return OranTeivErrorMessage.builder().status(HttpStatus.BAD_REQUEST.name()).message(e.getMessage()).build();
    }
}
