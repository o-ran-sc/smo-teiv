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
package org.oran.smo.teiv.db;

import java.io.IOException;
import java.util.List;

import org.jooq.DSLContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

public class TestPostgresqlContainer extends PostgreSQLContainer<TestPostgresqlContainer> {

    private static TestPostgresqlContainer container;

    private TestPostgresqlContainer(DockerImageName image) {
        super(image);
    }

    public static TestPostgresqlContainer getInstance() {
        if (container == null) {
            container = new TestPostgresqlContainer(DockerImageName.parse("postgis/postgis:13-3.4-alpine")
                    .asCompatibleSubstituteFor("postgres"));
            container.withCopyFileToContainer(MountableFile.forClasspathResource(
                    "pgsqlschema/00_init-oran-smo-teiv-data.sql"), "/pgsqlschema/00_init-oran-smo-teiv-data.sql");
            container.withCopyFileToContainer(MountableFile.forClasspathResource(
                    "pgsqlschema/02_init-oran-smo-teiv-consumer-data.sql"),
                    "/pgsqlschema/02_init-oran-smo-teiv-consumer-data.sql");
            container.withCopyFileToContainer(MountableFile.forClasspathResource(
                    "pgsqlschema/03_init-oran-smo-teiv-groups.sql"), "/pgsqlschema/03_init-oran-smo-teiv-groups.sql");
            container.withCopyFileToContainer(MountableFile.forClasspathResource("pgsqlschema/model.sql"),
                    "/pgsqlschema/model.sql");
            container.withCopyFileToContainer(MountableFile.forClasspathResource("pgsqlschema/data.sql"),
                    "/pgsqlschema/data.sql");
            container.withCopyFileToContainer(MountableFile.forClasspathResource("pgsqlschema/ingestion-test-model.sql"),
                    "/pgsqlschema/ingestion-test-model.sql");
            container.withCopyFileToContainer(MountableFile.forClasspathResource("pgsqlschema/ingestion-test-data.sql"),
                    "/pgsqlschema/ingestion-test-data.sql");
            container.withCopyFileToContainer(MountableFile.forClasspathResource("pgsqlschema/end-to-end-test-model.sql"),
                    "/pgsqlschema/end-to-end-test-model.sql");
            container.withCopyFileToContainer(MountableFile.forClasspathResource("pgsqlschema/end-to-end-test-data.sql"),
                    "/pgsqlschema/end-to-end-test-data.sql");
            container.withCopyFileToContainer(MountableFile.forClasspathResource("pgsqlschema/consumer-data.sql"),
                    "/pgsqlschema/consumer-data.sql");
            container.withCopyFileToContainer(MountableFile.forClasspathResource("pgsqlschema/groups.sql"),
                    "/pgsqlschema/groups.sql");
            container.withCopyFileToContainer(MountableFile.forClasspathResource(
                    "pgsqlschema/test-model-for-ingestion-validation.sql"),
                    "/pgsqlschema/test-model-for-ingestion-validation.sql");
            container.withCopyFileToContainer(MountableFile.forClasspathResource(
                    "pgsqlschema/test-data-for-ingestion-validation.sql"),
                    "/pgsqlschema/test-data-for-ingestion-validation.sql");
            container.withCopyFileToContainer(MountableFile.forClasspathResource(
                    "pgsqlschema/01_init-oran-smo-teiv-model.sql"), "/pgsqlschema/01_init-oran-smo-teiv-model.sql");
            container.setCommand("postgres", "-c", "max_connections=2000");

            container.start();
            try {
                loadModels();
                container.execInContainer("psql", "-U", "test", "-w", "-f", "/pgsqlschema/00_init-oran-smo-teiv-data.sql",
                        "--set=pguser=test");
                container.execInContainer("psql", "-U", "test", "-w", "-f",
                        "/pgsqlschema/02_init-oran-smo-teiv-consumer-data.sql", "--set=pguser=test");
                container.execInContainer("psql", "-U", "test", "-w", "-f", "/pgsqlschema/03_init-oran-smo-teiv-groups.sql",
                        "--set=pguser=test");
            } catch (UnsupportedOperationException | IOException | InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return container;
    }

    public static void loadSampleData() {
        try {
            loadData();
            container.execInContainer("psql", "-U", "test", "-w", "-f", "/pgsqlschema/consumer-data.sql",
                    "--set=pguser=test");
        } catch (UnsupportedOperationException | IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void loadSampleGroupsData() {
        try {
            loadSampleData();
            container.execInContainer("psql", "-U", "test", "-w", "-f", "/pgsqlschema/groups.sql", "--set=pguser=test");
        } catch (UnsupportedOperationException | IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void loadEndToEndTestData() {
        try {
            container.execInContainer("psql", "-U", "test", "-w", "-f", "/pgsqlschema/end-to-end-test-data.sql",
                    "--set=pguser=test");
            container.execInContainer("psql", "-U", "test", "-w", "-f", "/pgsqlschema/end-to-end-test-model.sql",
                    "--set=pguser=test");
        } catch (UnsupportedOperationException | IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void loadIngestionTestData() {
        try {
            container.execInContainer("psql", "-U", "test", "-w", "-f", "/pgsqlschema/ingestion-test-data.sql",
                    "--set=pguser=test");
            container.execInContainer("psql", "-U", "test", "-w", "-f", "/pgsqlschema/ingestion-test-model.sql",
                    "--set=pguser=test");
        } catch (UnsupportedOperationException | IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void truncateSchemas(List<String> schemas, DSLContext dslContext) {
        dslContext.meta().filterSchemas(s -> schemas.contains(s.getName())).getTables().forEach(t -> dslContext.truncate(t)
                .cascade().execute());
    }

    public static void loadData() {
        try {
            container.execInContainer("psql", "-U", "test", "-w", "-f", "/pgsqlschema/data.sql", "--set=pguser=test");
            loadUpdateData();
        } catch (UnsupportedOperationException | IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void loadModels() {
        try {
            container.execInContainer("psql", "-U", "test", "-w", "-f", "/pgsqlschema/01_init-oran-smo-teiv-model.sql",
                    "--set=pguser=test");
            container.execInContainer("psql", "-U", "test", "-w", "-f", "/pgsqlschema/model.sql", "--set=pguser=test");
        } catch (UnsupportedOperationException | IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void loadValidationTestData() {
        try {
            container.execInContainer("psql", "-U", "test", "-w", "-f",
                    "/pgsqlschema/test-data-for-ingestion-validation.sql", "--set=pguser=test");
            container.execInContainer("psql", "-U", "test", "-w", "-f",
                    "/pgsqlschema/test-model-for-ingestion-validation.sql", "--set=pguser=test");
        } catch (UnsupportedOperationException | IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void loadUpdateData() {
        //        try {
        //            container.execInContainer("psql", "-U", "test", "-w", "-f",
        //                "/pgsqlschema/04_init-oran-smo-teiv-data-update.sql", "--set=pguser=\"test\"",
        //                "--set=adapterId=namespace-oran-smo-ran-topology-adapter",
        //                "--set=adapterHashedId=17a7ac8c7522bc5a27e5d095088ec1a9c8b2a7da", "--set=upgradeTime=" + OffsetDateTime
        //                    .now(ZoneOffset.UTC));
        //        } catch (UnsupportedOperationException | IOException | InterruptedException e) {
        //            throw new RuntimeException(e.getMessage());
        //        }
    }
}
