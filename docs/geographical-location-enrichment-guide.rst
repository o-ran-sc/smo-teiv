..  ============LICENSE_START=======================================================
..  Copyright (C) 2024 Ericsson
..  Modifications Copyright (C) 2024 OpenInfra Foundation Europe
..  ================================================================================
..  Licensed under the Apache License, Version 2.0 (the "License");
..  you may not use this file except in compliance with the License.
..  You may obtain a copy of the License at
..
..        http://www.apache.org/licenses/LICENSE-2.0
..
..  Unless required by applicable law or agreed to in writing, software
..  distributed under the License is distributed on an "AS IS" BASIS,
..  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
..  See the License for the specific language governing permissions and
..  limitations under the License.
..
..  SPDX-License-Identifier: Apache-2.0
..  ============LICENSE_END=========================================================

Geographical enrichment guide
#############################

Geographical Enrichment Guide Overview
======================================

In this guide, we explore how to use the Geographical Location
Enrichment API to enrich Topology & Inventory with geographical data.

Geographical enrichment
=======================

Geographical enrichment is the adding, modifying, and removing of
geographical entities that supports geographical information.
Geographical entities are associated to topology data. The following
geographical entities support geographical enrichment:

-  Sector
-  AntennaModule
-  Site

For information on the entity types and their supported relationships,
see the :doc:`Data Models </data-models-guide>`.

The format of the geographical enrichment message is CloudEvents.
Topology & Inventory uses `CloudEvents
Kafka® <https://cloudevents.github.io/sdk-java/kafka.html>`__ version
2.5.x. The link provides a sample CloudEvents implementation in Java®.
CloudEvents SDKs also supports other languages. See
`CloudEvents <https://cloudevents.io/>`__.

The “data” element consists of the actual topology and inventory data.
It contains all the geographical entities and their associated
relationships in application/json format, and each entity and
relationships are represented in application/yang-data+json format.

CloudEvents attributes are validated against the :doc:`Data Models
</data-models-guide>`. If there
is an unknown attribute in the CloudEvents, Topology & Inventory does
not drop the whole event but parses and persists only valid attributes,
and unknown parts are logged and ignored. If an empty or bad payload is
sent, the data is not persisted.

The value of non-mandatory fields can be deleted by sending a merge
request to set it to null. Null means that the value is not set.

Example of enrich Topology & Inventory with geographical data
-------------------------------------------------------------

This example creates a new topology Site entity and a relationship
between the Site and an AntennaModule. Using the
:ref:`create schema <Ingestion Create>`
the data producer can create entities that support geographical
enrichment. Attributes with null means not set.

.. code:: json

   {
       "ce_specversion": "1.0",
       "ce_id": "a30e63c9-d29e-46ff-b99a-b63ed83fd233",
       "ce_source": "dmi-plugin:nm-1",
       "ce_type": "topology-inventory-ingestion-create",
       "ce_time": "2023-06-12T09:05:00Z",
       "content-type": "application/json",
       "ce_dataschema": "topology-inventory-ingestion:events:create:1.0.0",
       "data": {
         "entities": [
           {
             "o-ran-smo-teiv-equipment:Site": [
               {
                 "id": "urn:o-ran:smo:teiv:sha512:Site=1F137533843657E9E9DBE60DBD86B045A057DB6D04B6A07AC153",
                 "attributes": {
                   "name": "Dublin",
                   "location": {
                     "geo-location": {
                       "latitude": 41.73297,
                       "longitude": -73.007696
                     }
                   }
                 }
               }
             ]
           },
           {
             "o-ran-smo-teiv-equipment:AntennaModule": [
               {
                 "id": "urn:o-ran:smo:teiv:sha512:AntennaModule=1FEBF137533843657E9E9DBE60DBD86B045A057DB6D04B6A07AC15323F1906228E93CFA4A1DB37D50252B3AFE6AEC9860E2CEA4A77BB3A25C9EA45DEDA87E765",
                 "attributes": {
                   "geo-location": {
                     "latitude": 41.73297,
                     "longitude": -73.007696
                   }
                 }
               }
             ]
           }
         ],
         "relationships": [
           {
             "o-ran-smo-teiv-equipment:ANTENNAMODULE_INSTALLED_AT_SITE": [
               {
                 "id": "urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_INSTALLED_AT_SITE=TlJDZWxsRFU6U3ViTmV0d29yaz1FdXJvcGUsU3ViTmV0d29yaz1JcmVs=",
                 "aSide": "urn:o-ran:smo:teiv:sha512:AntennaModule=1FEBF137533843657E9E9DBE60DBD86B045A057DB6D04B6A07AC15323F1906228E93CFA4A1DB37D50252B3AFE6AEC9860E2CEA4A77BB3A25C9EA45DEDA87E765",
                 "bSide": "urn:o-ran:smo:teiv:sha512:Site=1F137533843657E9E9DBE60DBD86B045A057DB6D04B6A07AC153"
               }
             ]
           }
         ]
       }
   }

Example of modify enriched Topology & Inventory with geographical data
----------------------------------------------------------------------

This example updates an existing Site entity. Using the
:ref:`merge schema <Ingestion Merge>`
the data producer can update entities that support geographical
enrichment.

.. code:: json


   {
     "ce_specversion": "1.0",
     "ce_id": "a30e63c9-d29e-46ff-b99a-b63ed83fd234",
     "ce_source": "dmi-plugin:nm-1",
     "ce_type": "topology-inventory-ingestion-merge",
     "ce_time": "2023-06-12T09:05:00Z",
     "content-type": "application/json",
     "ce_dataschema": "topology-inventory-ingestion:events:merge:1.0.0",
     "data": {
       "entities": [
         {
           "o-ran-smo-teiv-equipment:Site": [
             {
               "id": "urn:o-ran:smo:teiv:sha512:Site=1F137533843657E9E9DBE60DBD86B045A057DB6D04B6A07AC153",
               "attributes": {
                 "name": "Dublin",
                 "location": {
                   "geo-location": {
                     "latitude": 52.73297,
                     "longitude": -84.007696
                   }
                 }
               }
             }
           ]
         },
         {
           "o-ran-smo-teiv-equipment:AntennaModule": [
             {
               "id": "urn:o-ran:smo:teiv:sha512:AntennaModule=1FEBF137533843657E9E9DBE60DBD86B045A057DB6D04B6A07AC15323F1906228E93CFA4A1DB37D50252B3AFE6AEC9860E2CEA4A77BB3A25C9EA45DEDA87E765",
               "attributes": {
                 "geo-location": {
                   "latitude": 52.73297,
                   "longitude": -84.007696
                 }
               }
             }
           ]
         }
       ],
       "relationships": [
         {
           "o-ran-smo-teiv-equipment:ANTENNAMODULE_INSTALLED_AT_SITE": [
             {
               "id": "urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_INSTALLED_AT_SITE=TlJDZWxsRFU6U3ViTmV0d29yaz1FdXJvcGUsU3ViTmV0d29yaz1JcmVs=",
               "aSide": "urn:o-ran:smo:teiv:sha512:AntennaModule=1FEBF137533843657E9E9DBE60DBD86B045A057DB6D04B6A07AC15323F1906228E93CFA4A1DB37D50252B3AFE6AEC9860E2CEA4A77BB3A25C9EA45DEDA87E765",
               "bSide": "urn:o-ran:smo:teiv:sha512:Site=1F137533843657E9E9DBE60DBD86B045A057DB6D04B6A07AC153"
             }
           ]
         }
       ]
     }
   }

Example of delete enriched data from Topology & Inventory
---------------------------------------------------------

This example deletes a topology Site entity and its relationship to an
AntennaModule entity. Using the
:ref:`delete schema <Ingestion Delete>`
the data producer can delete entities that support geographical
enrichment.

.. code:: json

   {
       "ce_specversion": "1.0",
       "ce_id": "a30e63c9-d29e-46ff-b99a-b63ed83fd235",
       "ce_source": "dmi-plugin:nm-1",
       "ce_type": "topology-inventory-ingestion-delete",
       "ce_time": "2023-06-12T09:05:00Z",
       "content-type": "application/json",
       "ce_dataschema": "topology-inventory-ingestion:events:delete:1.0.0",
       "data": {
           "entities" : [
             {
               "o-ran-smo-teiv-equipment:Site": [
                   {
                       "id": "urn:o-ran:smo:teiv:sha512:Site=1F137533843657E9E9DBE60DBD86B045A057DB6D04B6A07AC153"
                   }
               ]
             }
           ],
           "relationships": [
             {
               "o-ran-smo-teiv-equipment:ANTENNAMODULE_INSTALLED_AT_SITE": [
                   {
                       "id" : "urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_INSTALLED_AT_SITE=TlJDZWxsRFU6U3ViTmV0d29yaz1FdXJvcGUsU3ViTmV0d29yaz1JcmVs="
                   }
               ]
             }
           ]
       }
   }

How to create and produce an event
==================================

To create and produce an event, you can use the `CloudEventBuilder.v1
and
KafkaProducer <https://cloudevents.github.io/sdk-java/kafka.html>`__.
The link provides a sample CloudEvents implementation in Java.
CloudEvents SDKs also supports other languages. See
`CloudEvents <https://cloudevents.io/>`__.

Troubleshooting
===============

If CloudEvents were sent but no data was persisted, check validation
failures and logs. Update the CloudEvent based on the logs and send it again.
