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
package org.oran.smo.teiv.exposure.spi;

import java.util.List;
import java.util.Optional;

/**
 * Handles database operations for {@link Module}
 */
public interface ModelRepository {

    /**
     * Checks whether the given module exists in the given schema.
     *
     * @param schemaName
     *     - schemaName name
     * @param name
     *     - module name
     * @return true if module exists
     */

    boolean doesModuleExists(final String schemaName, final String name);

    /**
     * Gets module by name.
     *
     * @param name
     *     - the name
     * @return the module
     */
    Optional<Module> getConsumerModuleByName(final String name);

    /**
     * Gets modules.
     *
     * @return the modules
     */
    List<Module> getModules();

    /**
     * Gets module that were in the process of being deleted prior to pod restart.
     *
     * @return the module
     */
    List<Module> getDeletingModulesOnStartup();

    /**
     * Gets module content by name.
     *
     * @param name
     *     the name
     * @return the schema
     */
    String getModuleContentByName(final String name);

    /**
     * Creates module.
     *
     * @param module
     *     - module to create
     */
    void createModule(Module module);

    /**
     * Updates module status.
     *
     * @param name
     *     - name of the module
     * @param status
     *     - status to update
     */
    void updateModuleStatus(final String name, final ModuleStatus status);

    /**
     * Deletes a module.
     *
     * @param name
     *     - name of the module
     */
    void deleteModuleByName(final String name);

}
