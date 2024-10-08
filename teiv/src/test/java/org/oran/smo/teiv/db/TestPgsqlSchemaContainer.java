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
package org.oran.smo.teiv.db;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestPgsqlSchemaContainer extends GenericContainer {

    private static TestPgsqlSchemaContainer container;

    private TestPgsqlSchemaContainer(DockerImageName image) {
        super(image);
    }

    public static TestPgsqlSchemaContainer getInstance() {
        ClassLoader classLoader = TestPgsqlSchemaContainer.class.getClassLoader();
        URL resourceUrl = classLoader.getResource("models");
        Path resourcePath = Paths.get(resourceUrl.getPath());

        URL resourceUrl1 = classLoader.getResource("pgsqlschema");
        Path resourcePath1 = Paths.get(resourceUrl1.getPath());

        try (GenericContainer<?> container = new GenericContainer<>("o-ran-sc/smo-teiv-pgsql-schema-generator:latest")
                .withFileSystemBind(resourcePath.toString(), "/opt/app/pgsql-schema-generator/resources/generate-defaults")
                .withCommand("/bin/sh", "-c",
                        "java ${JAVA_OPTS} -jar pgsql-schema-generator-app.jar && bash copySqlSchema.sh test && sleep infinity")) {
            container.start();
            Thread.sleep(3000);
            container.copyFileFromContainer("/opt/app/pgsql-schema-generator/sql_scripts/01_init-teiv-exposure-data.sql",
                    resourcePath1 + "/00_init-oran-smo-teiv-data-v1.sql");
            container.copyFileFromContainer("/opt/app/pgsql-schema-generator/sql_scripts/00_init-teiv-exposure-model.sql",
                    resourcePath1 + "/01_init-oran-smo-teiv-model-v1.sql");
            container.copyFileFromContainer(
                    "/opt/app/pgsql-schema-generator/sql_scripts/02_init-teiv-exposure-consumer-data.sql",
                    resourcePath1 + "/02_init-oran-smo-teiv-consumer-data-v1.sql");
            container.stop();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return container;
    }
}
