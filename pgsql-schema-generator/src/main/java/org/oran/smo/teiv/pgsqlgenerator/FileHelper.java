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
package org.oran.smo.teiv.pgsqlgenerator;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FileHelper {

    public static void copyResourceToFile(Resource resource, File destinationFile) throws IOException {
        try {
            File resourceFile = resource.getFile();
            Files.copy(resourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            writeResourceToFile(destinationFile, resource);
        }
    }

    public static List<File> getYangFilesFromResource(String resourcePath) throws IOException {
        Resource resource = new ClassPathResource(resourcePath);

        try {
            File directory = resource.getFile();
            if (directory.isDirectory()) {
                return Collections.singletonList(directory);
            } else {
                throw PgSchemaGeneratorException.extractYangDataException(new IOException());
            }
        } catch (IOException e) {
            Resource jarResourceDir = new ClassPathResource(resourcePath);
            File tempDir = Files.createTempDirectory(jarResourceDir.getFilename()).toFile();
            String pattern = resourcePath + "/**";
            Resource[] jarResources = new PathMatchingResourcePatternResolver().getResources(pattern);
            for (Resource jarResource : jarResources) {
                createTempFileFromJarResource(jarResource, tempDir.toPath());
            }
            tempDir.deleteOnExit();
            return Collections.singletonList(tempDir);
        }
    }

    private static Optional<File> createTempFileFromJarResource(Resource resource, Path path) throws IOException {
        if (resource.getFilename().isEmpty()) {
            return Optional.empty();
        }
        Path pathToFile = Path.of(path + "/" + resource.getFilename());
        File file = Files.createFile(pathToFile).toFile();
        writeResourceToFile(file, resource);
        return Optional.of(file);
    }

    private static void writeResourceToFile(File file, Resource resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream(); OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }
}
