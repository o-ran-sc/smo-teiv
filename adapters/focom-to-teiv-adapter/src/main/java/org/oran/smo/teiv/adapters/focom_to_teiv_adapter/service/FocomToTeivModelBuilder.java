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
import lombok.extern.slf4j.Slf4j;
import org.nephio.focom.v1alpha1.FocomProvisioningRequest;
import org.oran.provisioning.o2ims.v1alpha1.ProvisioningRequest;
import org.oran.smo.teiv.adapters.focom_to_teiv_adapter.custom_resource_json.EntityAndRelationshipModel;
import org.oran.smo.teiv.adapters.focom_to_teiv_adapter.custom_resource_json.EntityItem;
import org.oran.smo.teiv.adapters.focom_to_teiv_adapter.custom_resource_json.RelationshipItem;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.oran.smo.teiv.adapters.common.utils.Constants.ENTITY_OCLOUD_NAMESPACE;
import static org.oran.smo.teiv.adapters.common.utils.Constants.SMO_TEIV_CLOUD_PREFIX;
import static org.oran.smo.teiv.adapters.common.utils.Constants.ENTITY_NODE_CLUSTER;
import static org.oran.smo.teiv.adapters.common.utils.Constants.REL_DEPLOYED_ON;
import static org.oran.smo.teiv.adapters.common.utils.TeivIdBuilder.buildEntityTypeName;
import static org.oran.smo.teiv.adapters.common.utils.TeivIdBuilder.buildTeivFocomRelationshipTypeName;

@Slf4j
@Component
public class FocomToTeivModelBuilder {

    private final FocomProvisioningRequestService service;
    private final EntityAndRelationshipModelService modelService;

    public FocomToTeivModelBuilder(FocomProvisioningRequestService service,
                                   EntityAndRelationshipModelService modelService) {
        this.service = service;
        this.modelService = modelService;
    }

    public Map<String, Object> getFocomtoTeivJson() throws IOException {

        List<EntityItem> oCloudNamespaces = new ArrayList<>();
        List<EntityItem> nodeClusters = new ArrayList<>();
        List<RelationshipItem> deployOnRelationships = new ArrayList<>();

        int index = 1;

        List<FocomProvisioningRequest> focomRequests = service.getAllFocomProvisioningRequests();

        for (FocomProvisioningRequest focomProvisioningRequest : focomRequests) {

            validateProvisioningRequest(focomProvisioningRequest);

            EntityItem oCloudNamespace = buildOCloudNamespace(focomProvisioningRequest, index);
            oCloudNamespaces.add(oCloudNamespace);

            EntityItem nodeCluster = buildNodeCluster(focomProvisioningRequest, index);
            nodeClusters.add(nodeCluster);

            RelationshipItem rel = modelService.getTeivRelationshipDeployOn(oCloudNamespace, nodeCluster, index);
            deployOnRelationships.add(rel);

            index++;
        }

        Map<String, List<EntityItem>> entities = new HashMap<>();
        entities.put(
                buildEntityTypeName(SMO_TEIV_CLOUD_PREFIX, ENTITY_OCLOUD_NAMESPACE),
                oCloudNamespaces
        );
        entities.put(
                buildEntityTypeName(SMO_TEIV_CLOUD_PREFIX, ENTITY_NODE_CLUSTER),
                nodeClusters
        );

        Map<String, List<RelationshipItem>> relationships = new HashMap<>();
        relationships.put(
                buildTeivFocomRelationshipTypeName(
                        REL_DEPLOYED_ON,
                        ENTITY_OCLOUD_NAMESPACE.toUpperCase(),
                        ENTITY_NODE_CLUSTER.toUpperCase()
                ),
                deployOnRelationships
        );

        EntityAndRelationshipModel entityAndRelationshipModel = new EntityAndRelationshipModel();
        entityAndRelationshipModel.setEntities(List.of(entities));
        entityAndRelationshipModel.setRelationships(List.of(relationships));

        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        Map<String, Object> json = mapper.convertValue(entityAndRelationshipModel, new TypeReference<Map<String, Object>>() {
        });

        log.info(mapper.writeValueAsString(entityAndRelationshipModel));
        return json;
    }

    private void validateProvisioningRequest(FocomProvisioningRequest focomProvisioningRequest) {
        if (!"Fulfilled".equals(focomProvisioningRequest.getStatus().getPhase())) {
            throw new RuntimeException("Requested Focomprovisioning request is not in Fulfilled phase");
        }
    }

    private EntityItem buildOCloudNamespace(FocomProvisioningRequest focomProvisioningRequest, int index) {
        String namespace = focomProvisioningRequest.getSpec().getOCloudNamespace();
        return modelService.getOCloudNamespaceEntity(namespace, index);
    }

    private EntityItem buildNodeCluster(FocomProvisioningRequest focomProvisioningRequest, int index) {
        ProvisioningRequest o2imsReq = service.getO2imsProvisioningRequest(focomProvisioningRequest.getMetadata().getName());

        String clusterName = focomProvisioningRequest.getSpec().getTemplateParameters()
                .getAdditionalProperties()
                .get("clusterName")
                .toString();

        String nodeClusterId = String.valueOf(o2imsReq.getStatus()
                .getProvisionedResourceSet()
                .getOCloudNodeClusterId());

        return modelService.getNodeClusterEntity(clusterName, nodeClusterId, index);
    }
}

