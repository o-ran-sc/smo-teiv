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
package org.oran.smo.teiv.exposure.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.oran.smo.teiv.exception.TiesException;
import org.oran.smo.teiv.schema.SchemaRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestValidator {

    public void validateDomain(String domain) {
        if (!SchemaRegistry.getDomains().contains(domain)) {
            throw TiesException.unknownDomain(domain, SchemaRegistry.getDomains());
        }
    }

    public void validateEntityType(String entityType) {
        if (!SchemaRegistry.getEntityNames().contains(entityType)) {
            throw TiesException.unknownEntityType(entityType, SchemaRegistry.getEntityNames());
        }
    }

    public void validateEntityTypeInDomain(String entityType, String domain) {
        if (!SchemaRegistry.getEntityNamesByDomain(domain).contains(entityType)) {
            throw TiesException.unknownEntityTypeInDomain(entityType, domain, SchemaRegistry.getEntityNamesByDomain(
                    domain));
        }
    }

    public void validateRelationshipType(String relationShipType) {
        if (!SchemaRegistry.getRelationNames().contains(relationShipType)) {
            throw TiesException.unknownRelationshipType(relationShipType, SchemaRegistry.getRelationNames());
        }
    }

    public void validateRelationshipTypeInDomain(String relationshipType, String domain) {
        if (!SchemaRegistry.getRelationNamesByDomain(domain).contains(relationshipType)) {
            throw TiesException.unknownRelationshipTypeInDomain(relationshipType, domain, SchemaRegistry
                    .getRelationNamesByDomain(domain));
        }
    }

    public void validateYangFile(MultipartFile file) {

        if (!Objects.equals(file.getContentType(), "application/yang")) {
            throw TiesException.invalidFileInput("Invalid file");
        }

    }
}
