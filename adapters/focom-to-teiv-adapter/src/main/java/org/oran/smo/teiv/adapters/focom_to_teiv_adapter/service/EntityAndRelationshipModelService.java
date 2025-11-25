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

import org.oran.smo.teiv.adapters.focom_to_teiv_adapter.custom_resource_json.EntityItem;
import org.oran.smo.teiv.adapters.focom_to_teiv_adapter.custom_resource_json.RelationshipItem;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.oran.smo.teiv.adapters.common.utils.Constants.ENTITY_OCLOUD_NAMESPACE;
import static org.oran.smo.teiv.adapters.common.utils.Constants.REL_DEPLOYED_ON;
import static org.oran.smo.teiv.adapters.common.utils.Constants.ENTITY_NODE_CLUSTER;
import static org.oran.smo.teiv.adapters.common.utils.TeivIdBuilder.buildFunctionFdn;
import static org.oran.smo.teiv.adapters.common.utils.TeivIdBuilder.buildTeivFocomRelationshipTypeName;

@Component
public class EntityAndRelationshipModelService {

    public EntityItem getOCloudNamespaceEntity(String oCloudNamespaceName, int index) {
        EntityItem oCloudNamespace = new EntityItem();
        oCloudNamespace.setId(buildFunctionFdn("OCloudNamespace" + ":" + index));
        oCloudNamespace.setAttributes(Map.of("oCloudNamespaceName", oCloudNamespaceName));

        return oCloudNamespace;
    }

    public EntityItem getNodeClusterEntity(String clusterName, String oCloudNodeClusterId, int index) {
        EntityItem nodeCluster = new EntityItem();
        nodeCluster.setId(buildFunctionFdn("NodeCluster" + ":" + index));

        Map<String, Object> clusterAttributes = new HashMap<>();
        clusterAttributes.put("nodeClusterName", clusterName);
        clusterAttributes.put("nodeClusterId", oCloudNodeClusterId);
        nodeCluster.setAttributes(clusterAttributes);

        return nodeCluster;
    }

    public RelationshipItem getTeivRelationshipDeployOn(EntityItem entityItem1, EntityItem entityItem2, int index) {
        RelationshipItem relDeployOn = new RelationshipItem();
        relDeployOn.setId(buildTeivFocomRelationshipTypeName(REL_DEPLOYED_ON, ENTITY_OCLOUD_NAMESPACE.toUpperCase(), ENTITY_NODE_CLUSTER.toUpperCase() + ":" + index));
        relDeployOn.setASide(entityItem1.getId());
        relDeployOn.setBSide(entityItem2.getId());
        relDeployOn.setSourceIds(List.of("source1", "source2"));
        return relDeployOn;
    }
}

