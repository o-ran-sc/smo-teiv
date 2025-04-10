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
package org.oran.smo.teiv;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpComponentsClientHttpRequestFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.oran.smo.teiv.service.JSONBSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.springframework.cache.annotation.EnableCaching;

/**
 * Core Application, the starting point of the application.
 */
@SpringBootApplication
@EnableAsync
@EnableCaching
@EnableScheduling
@EnableAspectJAutoProxy
public class CoreApplication {

    /**
     * Main entry point of the application.
     *
     * @param args
     *     Command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }

    /**
     * Making a RestClient, using RestClient.Builder, to use for consumption of RESTful
     * interfaces.
     *
     * @param restClientBuilder
     *     WebClient.Builder instance
     *
     * @return RestClient
     */
    @Bean
    public RestClient restClient(final RestClient.Builder restClientBuilder) {
        return restClientBuilder.build();
    }

    /**
     * Configuration bean for Web MVC.
     *
     * @return WebMvcConfigurer
     */
    @Bean
    public WebMvcConfigurer webConfigurer() {
        return new WebMvcConfigurer() {
        };
    }

    /**
     * Bean to create the default timedAspect in ApplicationContext.
     *
     * @param registry
     *     Meter Registry
     * @return TimedAspect
     */
    @Bean
    public TimedAspect timedAspect(final MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.serializationInclusion(JsonInclude.Include.USE_DEFAULTS).serializers(
                new JSONBSerializer());
    }

    /*
     * Apache HTTP Components have changed defaults in the HttpClient
     * relating to HTTP/1.1 TLS upgrades. This bean restores previous
     * behavior, fixing issues with Istio.
     * https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.4-Release-Notes
     */
    @Bean
    public HttpComponentsClientHttpRequestFactoryBuilder httpComponentsClientHttpRequestFactoryBuilder() {
        return ClientHttpRequestFactoryBuilder.httpComponents().withDefaultRequestConfigCustomizer(builder -> builder
                .setProtocolUpgradeEnabled(false));
    }

}
