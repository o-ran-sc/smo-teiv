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
package org.oran.smo.teiv.exposure.model.api;

import org.oran.smo.teiv.api.model.OranTeivSchemaList;
import org.oran.smo.teiv.exposure.utils.RequestDetails;
import org.springframework.web.multipart.MultipartFile;

public interface ModelService {

    /**
     * Creates module from the yang file.
     *
     * @param yangFile
     *     the yang content
     */
    String createModule(final MultipartFile yangFile);

    /**
     * Gets all modules in a domain
     *
     * @param domain
     *     domain name
     * @param paginationDTO
     *     pagination data
     * @return a map of all schemas in a domain
     */
    OranTeivSchemaList getModulesByDomain(final String domain, final RequestDetails paginationDTO);

    /**
     * Gets module content
     *
     * @param name
     *     name
     * @return the content
     */
    String getModuleContentByName(final String name);

    /**
     * Gets all user defined modules in a domain
     *
     * @param domain
     *     domain name
     * @param paginationDTO
     *     pagination data
     * @return a map of all user defined schemas in a domain
     */
    OranTeivSchemaList getUserDefinedModulesByDomain(final String domain, final RequestDetails paginationDTO);

    /**
     * Gets module content
     *
     * @param name
     *     name
     * @return the content
     */
    String getUserDefinedModuleContentByName(final String name);

    /**
     * Deletes module.
     *
     * @param name
     *     the schema name
     */
    void deleteConsumerModule(final String name);
}
