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

import org.oran.smo.teiv.adapters.focom_to_teiv_adapter.focomprovisioningrequest.FocomProvisioningRequest;
import org.oran.smo.teiv.adapters.focom_to_teiv_adapter.custom_resource_json.EntityItem;
import org.oran.smo.teiv.adapters.focom_to_teiv_adapter.custom_resource_json.RelationshipItem;
import org.oran.smo.teiv.adapters.focom_to_teiv_adapter.o2imsprovisioningrequest.O2imsProvisioningRequest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.oran.smo.teiv.adapters.common.utils.Constants.*;
import static org.oran.smo.teiv.adapters.common.utils.TeivIdBuilder.*;

public class EntityAndRelationship {

    public static Map<String, List<EntityItem>> getTeivEntityForOCloudNamespace(FocomProvisioningRequest focomProvisioningRequest) {

        //OCloudNamespace entity
        EntityItem oCloudNamespace = new EntityItem();
        oCloudNamespace.setId(buildFunctionFdn("OCloudNamespace:1"));
        oCloudNamespace.setAttributes(Map.of("oCloudNamespaceName",focomProvisioningRequest.getSpec().getOCloudNamespace()));

        Map<String, List<EntityItem>> entityMap = new LinkedHashMap<>();
        entityMap.put(buildEntityTypeName(SMO_TEIV_CLOUD_PREFIX, ENTITY_OCLOUD_NAMESPACE), List.of(oCloudNamespace));

        return entityMap;
    }

    public static Map<String, List<EntityItem>> getTeivEntityForNodeCluster(FocomProvisioningRequest focomProvisioningRequest, O2imsProvisioningRequest o2imsProvisioningRequest) {

        Map<String, List<EntityItem>> entityMap = new LinkedHashMap<>();

        //NodeCluster entity
        EntityItem nodeCluster = new EntityItem();
        nodeCluster.setId(buildFunctionFdn("NodeCluster:1"));

        Map<String, Object> clusterAttributes = new HashMap<>();
        clusterAttributes.put("nodeClusterName", focomProvisioningRequest.getSpec().getTemplateParameters().get("clusterName"));
        clusterAttributes.put("nodeClusterId", o2imsProvisioningRequest.getStatus().getProvisionedResourceSet().get("oCloudNodeClusterId"));
        nodeCluster.setAttributes(clusterAttributes);

        entityMap.put(buildEntityTypeName(SMO_TEIV_CLOUD_PREFIX, ENTITY_NODE_CLUSTER),
                List.of(nodeCluster));

        return entityMap;
    }

    public static List<Map<String, List<RelationshipItem>>> getTeivRelationships(
            List<Map<String, List<EntityItem>>> entityMaps) {

        EntityItem aSide = entityMaps.get(0).get(buildEntityTypeName(SMO_TEIV_CLOUD_PREFIX, ENTITY_OCLOUD_NAMESPACE)).get(0);
        EntityItem bSide = entityMaps.get(1).get(buildEntityTypeName(SMO_TEIV_CLOUD_PREFIX, ENTITY_NODE_CLUSTER)).get(0);

        RelationshipItem deployOn = new RelationshipItem();
        deployOn.setId(buildTeivFocomRelationshipTypeName(REL_DEPLOYED_ON, ENTITY_OCLOUD_NAMESPACE.toUpperCase(), ENTITY_NODE_CLUSTER.toUpperCase() + "=1"));
        deployOn.setASide(aSide.getId());
        deployOn.setBSide(bSide.getId());
        deployOn.setSourceIds(List.of("source1", "source2"));

        Map<String, List<RelationshipItem>> relMap = new LinkedHashMap<>();
        relMap.put(buildTeivFocomRelationshipTypeName(REL_DEPLOYED_ON, ENTITY_OCLOUD_NAMESPACE.toUpperCase(), ENTITY_NODE_CLUSTER.toUpperCase()), List.of(deployOn));

        return List.of(relMap);
    }

}

