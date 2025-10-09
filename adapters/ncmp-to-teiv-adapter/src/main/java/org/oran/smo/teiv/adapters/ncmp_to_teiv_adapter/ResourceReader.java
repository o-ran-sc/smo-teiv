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

package org.oran.smo.teiv.adapters.ncmp_to_teiv_adapter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceReader {

    public static String readResourceFile(String resourcePath) throws IOException {
        try {
            URL resource = ResourceReader.class.getClassLoader().getResource(resourcePath);
            if (resource == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }

            if (resource.getProtocol().equals("jar")) {
                try (var inputStream = ResourceReader.class.getClassLoader().getResourceAsStream(resourcePath)) {
                    if (inputStream == null) {
                        throw new IllegalArgumentException("Could not open resource as stream: " + resourcePath);
                    }
                    return new String(inputStream.readAllBytes());
                }
            } else {
                Path path = Paths.get(resource.toURI());
                return Files.readString(path);
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to read resource: " + resourcePath, e);
        }
    }

}
