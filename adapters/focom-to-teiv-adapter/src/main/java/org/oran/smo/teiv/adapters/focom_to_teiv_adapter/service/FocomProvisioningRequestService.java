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
package org.oran.smo.teiv.adapters.focom_to_teiv_adapter.service;

import io.fabric8.kubernetes.client.KubernetesClient;
import org.oran.smo.teiv.adapters.focom_to_teiv_adapter.focomprovisioningrequest.FocomProvisioningRequest;
import org.oran.smo.teiv.adapters.focom_to_teiv_adapter.o2imsprovisioningrequest.O2imsProvisioningRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FocomProvisioningRequestService {

    private final KubernetesClient focomClient;
    private final KubernetesClient o2imsClient;

    public FocomProvisioningRequestService(@Qualifier("focomKubernetesClient") KubernetesClient focomClient,
                                           @Qualifier("o2imsKubernetesClient") KubernetesClient o2imsClient) {
        this.focomClient = focomClient;
        this.o2imsClient = o2imsClient;
    }

    public List<FocomProvisioningRequest> getAllFocomProvisioningRequests() {
        List<FocomProvisioningRequest> focomProvisioningRequestList = focomClient.resources(FocomProvisioningRequest.class)
                .inAnyNamespace()
                .list()
                .getItems();

        return focomProvisioningRequestList;
    }

    public FocomProvisioningRequest getFocomProvisioningRequest(String namespace, String name) {
        FocomProvisioningRequest focomProvisioningRequest = focomClient.resources(FocomProvisioningRequest.class)
                .inNamespace(namespace)
                .withName(name)
                .get();

        return focomProvisioningRequest;
    }

    public List<FocomProvisioningRequest> getAllO2imsProvisioningRequests() {
        List<FocomProvisioningRequest> o2imsProvisioningRequestList = o2imsClient.resources(FocomProvisioningRequest.class)
                .inAnyNamespace()
                .list()
                .getItems();

        return o2imsProvisioningRequestList;
    }

    public O2imsProvisioningRequest getO2imsProvisioningRequest(String name) {
        O2imsProvisioningRequest o2imsProvisioningRequest = o2imsClient.resources(O2imsProvisioningRequest.class)
                .withName(name)
                .get();

        return o2imsProvisioningRequest;
    }
}
