<!--
  ============LICENSE_START=======================================================
  Modifications Copyright (C) 2025 OpenInfra Foundation Europe
  ================================================================================
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.

  SPDX-License-Identifier: Apache-2.0
  ============LICENSE_END=========================================================
-->

# FOCOM Adapter Integration:

The FOCOM Adapter provides integration with FOCOM clusters to retrieve Custom Resources (CRs),
extract the required attributes, and map them to Cloud Model entities and relationships for TEIV.

## Overview:

The FOCOM Adapter connects to FOCOM clusters, retrieves specific CR information, and transforms it into TEIV-compliant entities and relationships.
This integration enables automated ingestion of cloud model topology data into the TEIV.

## Prerequisites:

Before running the adapter, ensure you have:
Access to the FOCOM clusters [FOCOM](https://docs.nephio.org/docs/guides/user-guides/usecase-user-guides/exercise-4-ocloud-cluster-prov/). 
Port forward needs to be done to access the clusters like sample below with VM where clusters are deployed:
- ssh -L 39121:127.0.0.1:39121 est-selfservice@10.101.0.239
- ssh -L 44873:127.0.0.1:44873 est-selfservice@10.101.0.239

## Current Implementation Status:

### Cluster Connectivity
The adapter successfully establishes connections to both clusters using kubeconfig files:
- FOCOM cluster
- O2IMS cluster

### CR Retrieval & Entity Construction
The adapter is currently capable of retrieving three attributes and mapping them to corresponding Cloud Model entities and relationships.
Extracted Attributes:
- OCloudNamespaceName
- nodeClusterName
- nodeClusterId

Generated Cloud Model Entities:
- OCloudNamespace
- NodeCluster

Generated Relationship:
- OCLOUDNAMESPACE_DEPLOYED_ON_NODECLUSTER

## How to Run the FOCOM Adapter

### Build the Parent TEIV Project

```yaml
mvn clean install
```

### Run the FOCOM Adapter via Docker Compose
Copy the [kubeconfigs](src/main/resources/kubeconfig) to docker-compose module.
Inside [docker-compose.yml](../../docker-compose/docker-compose.yml), add the following service:
```yaml
  focom-to-teiv-adapter:
    container_name: focom-to-teiv-adapter
    image: o-ran-sc/smo-focom-to-teiv-adapter:latest
    depends_on:
      - topology-ingestion-inventory
    environment:
      SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
      JAVA_TOOL_OPTIONS: -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
    volumes:
      - ./kubeconfig:/opt/app/teiv/config/kubeconfig:ro
    extra_hosts:
      - "focom-cluster-control-plane:host-gateway"
      - "kind-control-plane:host-gateway"
```
Then start the stack:
```yaml
docker-compose up
```
