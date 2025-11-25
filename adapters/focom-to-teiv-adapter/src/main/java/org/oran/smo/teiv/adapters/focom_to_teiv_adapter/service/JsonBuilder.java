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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.oran.smo.teiv.adapters.focom_to_teiv_adapter.focomprovisioningrequest.FocomProvisioningRequest;
import org.oran.smo.teiv.adapters.focom_to_teiv_adapter.custom_resource_json.*;
import org.oran.smo.teiv.adapters.focom_to_teiv_adapter.o2imsprovisioningrequest.O2imsProvisioningRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

import static org.oran.smo.teiv.adapters.common.utils.Constants.FOCOM_NAMESPACE;
import static org.oran.smo.teiv.adapters.focom_to_teiv_adapter.service.EntityAndRelationship.*;

@Component
public class JsonBuilder {

    private static FocomProvisioningRequestService service;

    public JsonBuilder(FocomProvisioningRequestService service) {
        this.service = service;
    }

    public static Map<String, Object> getFocomtoTeivJson(String name) throws IOException {

        FocomProvisioningRequest focomProvisioningRequest = service.getFocomProvisioningRequest(FOCOM_NAMESPACE, name);
        if (!focomProvisioningRequest.getStatus().getPhase().equals("Fulfilled")) {
            throw new RuntimeException("Requested Focom provisioning request is not achieved Fulfilled phase");
        }
        O2imsProvisioningRequest o2imsProvisioningRequest = service.getO2imsProvisioningRequest(name);
        return getProvisioningRequestJson(focomProvisioningRequest, o2imsProvisioningRequest);

    }

    private static Map<String, Object> getProvisioningRequestJson(FocomProvisioningRequest focomProvisioningRequest, O2imsProvisioningRequest o2imsProvisioningRequest) throws IOException {

        List<Map<String, List<EntityItem>>> entities = new ArrayList<>();

        Map<String, List<EntityItem>> entityForOCloudNamespace = getTeivEntityForOCloudNamespace(focomProvisioningRequest);
        entities.add(entityForOCloudNamespace);

        Map<String, List<EntityItem>> entityForNodeCluster = getTeivEntityForNodeCluster(focomProvisioningRequest, o2imsProvisioningRequest);
        entities.add(entityForNodeCluster);

        OutputModel output = new OutputModel();
        output.setEntities(entities);
        output.setRelationships(getTeivRelationships(entities));

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        Map<String, Object> json = mapper.convertValue(output, new TypeReference<Map<String, Object>>() {});

        System.out.println(mapper.writeValueAsString(output));
        return json;
    }
}

