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
package org.oran.smo.teiv.adapters.focom_to_teiv_adapter;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
public class KubernetesConfig {

    @Value("${focom.kubeconfig:}")
    private String focomKubeconfigPath;

    @Value("${o2ims.kubeconfig:}")
    private String o2imsKubeconfigPath;

    @Bean(name = "focomKubernetesClient")
    public KubernetesClient focomKubernetesClient() throws IOException {
        log.info("Creating Focom Kubernetes client");
        return buildClient(focomKubeconfigPath);
    }

    @Bean(name = "o2imsKubernetesClient")
    public KubernetesClient o2imsKubernetesClient() throws IOException {
        log.info("Creating o2ims Kubernetes client");
        return buildClient(o2imsKubeconfigPath);
    }

    private KubernetesClient buildClient(String path) throws IOException {
        Config config;

        if (path != null && !path.isBlank()) {
            log.debug("Using explicit kubeconfig from path: {}", path);
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);

            if (inputStream == null) {
                throw new IOException("Kubeconfig file not found in classpath: " + path);
            }
            String kubeconfig = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            config = Config.fromKubeconfig(kubeconfig);
        } else {
            log.info("No kubeconfig path provided. Using default auto-configured Kubernetes client");
            config = Config.autoConfigure(null);
        }
        return new KubernetesClientBuilder().withConfig(config).build();
    }
}