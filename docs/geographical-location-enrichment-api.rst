.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. SPDX-License-Identifier: CC-BY-4.0
.. Copyright (C) 2024 Nordix Foundation. All rights Reserved
.. Copyright (C) 2024 OpenInfra Foundation Europe. All Rights Reserved

Geographical Location Enrichment API 1.0.0beta1.0
#################################################

Geographical Location Enrichment API to enrich topology with
geographical data.

Operations
==========

.. _Ingestion Create:

PUB ``ingestionCreate`` Operation
---------------------------------

*Topology & Inventory entities and relationships can be created.*

-  Operation ID: ``Ingestion``

Operation on Topology Inventory and Exposure input topic.

Message Topology CREATE ``ingestionCreate``
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

*Creates components in topology.*

-  Message ID: ``create``
-  Content type: application/json

CREATE Headers
^^^^^^^^^^^^^^

+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| Name           | Type   | Description                               | Value                    | Constraints     | Notes             |
+================+========+===========================================+==========================+=================+===================+
| (root)         | object | -                                         | -                        | -               | | **additional**  |
|                |        |                                           |                          |                 | | **properties**  |
|                |        |                                           |                          |                 | | **are allowed** |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| ce_id          | string | Unique identifier for the event.          | -                        | -               | -                 |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| ce_source      | string | Source of the CloudEvent.                 | -                        | | format        | -                 |
|                |        |                                           |                          | | (`uri`)       |                   |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| ce_type        | string | | Event type. It can be one of            | -                        | -               | -                 |
|                |        | | topology-inventory-ingestion-merge,     |                          |                 |                   |
|                |        | | topology-inventory-ingestion-delete, or |                          |                 |                   |
|                |        | | topology-inventory-ingestion-create.    |                          |                 |                   |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| content-type   | string | | Content-type of the data contained      | | const                  | -               | -                 |
|                |        | | within the cloud event. It is           | | (`"application/json"`) |                 |                   |
|                |        | | application/json.                       |                          |                 |                   |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| ce_dataschema  | string | | URI representing the schema of the data | -                        | | format        | -                 |
|                |        | | This references the event-specific yang |                          | | (`uri`)       |                   |
|                |        | | schema.                                 |                          |                 |                   |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| ce_time        | string | Event timestamp.                          | -                        | | format        | -                 |
|                |        |                                           |                          | | (`date-time`) |                   |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| ce_specversion | string | | The version of the CloudEvents          | const (`"1.0"`)          | -               | -                 |
|                |        | | specification which the event uses.     |                          |                 |                   |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+

..

   Examples of headers *(generated)*

.. code:: json

   {
     "ce_specversion": "1.0",
     "ce_id": "a30e63c9-d29e-46ff-b99a-b63ed83fd233",
     "ce_source": "dmi-plugin:nm-1",
     "ce_type": "topology-inventory-ingestion-create",
     "ce_time": "2023-06-12T09:05:00Z",
     "content-type": "application/json",
     "ce_dataschema": "topology-inventory-ingestion:events:create:1.0.0"
   }

CREATE Payload
^^^^^^^^^^^^^^

+------------------------------------------------------+---------------+----------------------------------------------------------------+
| Name                                                 | Type          | Description                                                    |
+======================================================+===============+================================================================+
| (root)                                               | Object        | -                                                              |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| data                                                 | -             | | The data part consists of the actual topology data. It       |
|                                                      |               | | contains all the entities and their associated               |
|                                                      |               | | relationships.                                               |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| data.entities                                        | Array<Object> | | Entities are topology objects comprising of an id, consumer  |
|                                                      |               | | data, attributes and metadata for each. It contains the id   |
|                                                      |               | | only in case of delete cloud event.                          |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| | data.entities                                      | Object        | | Entities schema is adherent to the entity types and          |
| | .<module_name>                                     |               | | attributes mentioned in the yang modules. For yang modules,  |
| | :<mo_type>                                         |               | | see [Data Models][Data Models].                              |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| data.relationships                                   | Array<Object> | | Relationships comprising of an A-side and a B-side for each. |
|                                                      |               | | The A-side is considered the originating side of the         |
|                                                      |               | | relationship; the B-side is considered the terminating side  |
|                                                      |               | | of the relationship. The order of A-side and B-side is of    |
|                                                      |               | | importance and MUST NOT be changed once defined. It          |
|                                                      |               | | contains the id only in case of delete event.                |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| | data.relationships                                 | Object        | | Relationship schema is adherent to the relationship types    |
| | .<module_name>                                     |               | | mentioned in the yang modules. For yang modules,             |
| | :<relationship_type>                               |               | | see [Data Models][Data Models].                              |
+------------------------------------------------------+---------------+----------------------------------------------------------------+

   Examples of payload *(generated)*

.. code:: json

   {
     "data": {
       "entities": [
         {
           "o-ran-smo-teiv-equipment:AntennaModule": [
             {
               "id": "urn:oran:smo:teiv:AntennaModule=1",
               "attributes": {
                 "antennaModelNumber": "1",
                 "mechanicalAntennaBearing": 50,
                 "mechanicalAntennaTilt": 10,
                 "positionWithinSector": "Unknown",
                 "totalTilt": 14,
                 "electricalAntennaTilt": 2,
                 "antennaBeamWidth": [
                   35,
                   23,
                   21
                 ],
                 "geo-location": {
                   "latitude": 41.73297,
                   "longitude": -73.007696,
                   "height": 3000
                 }
               },
               "sourceIds": [
                 "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1",
                 "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1",
                 "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=1,RetSubUnit=1"
               ],
               "metadata": {
                 "trustLevel": "RELIABLE"
               }
             }
           ]
         },
         {
           "o-ran-smo-teiv-equipment:AntennaModule": [
             {
               "id": "urn:oran:smo:teiv:AntennaModule=2",
               "attributes": {
                 "antennaModelNumber": "2",
                 "mechanicalAntennaBearing": 61,
                 "mechanicalAntennaTilt": 21,
                 "positionWithinSector": "Unknown",
                 "totalTilt": 25,
                 "electricalAntennaTilt": 3,
                 "antennaBeamWidth": [
                   46,
                   34,
                   32
                 ],
                 "geo-location": {
                   "latitude": 52.84308,
                   "longitude": -84.118707,
                   "height": 41111
                 }
               },
               "sourceIds": [
                 "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=2",
                 "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=2,AntennaSubunit=1",
                 "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=2,RetSubUnit=1"
               ],
               "metadata": {
                 "trustLevel": "RELIABLE"
               }
             }
           ]
         }
       ],
       "relationships": [
         {
           "o-ran-smo-teiv-equipment:ANTENNAMODULE_INSTALLED_AT_SITE": [
             {
               "id": "urn:sha512:TlJDZWxsRFU6U3ViTmV0d29yaz1FdXJvcGUsU3ViTmV0d29yaz1JcmVs=",
               "aSide": "urn:oran:smo:teiv:AntennaModule=1",
               "bSide": "urn:oran:smo:teiv:Site=1"
             }
           ]
         },
         {
           "o-ran-smo-teiv-equipment:ANTENNAMODULE_INSTALLED_AT_SITE": [
             {
               "id": "urn:sha512:TlJDZWxsRFU6U3ViTmV0d29yaz1FdXJvcGUsU3ViTmV0d29yaz1JcmVsYW5kLE1lQ2=",
               "aSide": "urn:oran:smo:teiv:AntennaModule=2",
               "bSide": "urn:oran:smo:teiv:Site=2"
             }
           ]
         }
       ]
     }
   }

.. _Ingestion Merge:

PUB ``ingestionMerge`` Operation
--------------------------------

*Topology & Inventory entities and relationships can be updated.*

-  Operation ID: ``Ingestion``

Operation on Topology Inventory and Exposure input topic.

Message Topology MERGE ``ingestionMerge``
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

*Updates components in topology.*

-  Message ID: ``merge``
-  Content type: application/json

MERGE Headers
^^^^^^^^^^^^^

+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| Name           | Type   | Description                               | Value                    | Constraints     | Notes             |
+================+========+===========================================+==========================+=================+===================+
| (root)         | object | -                                         | -                        | -               | | **additional**  |
|                |        |                                           |                          |                 | | **properties**  |
|                |        |                                           |                          |                 | | **are allowed** |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| ce_id          | string | Unique identifier for the event.          | -                        | -               | -                 |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| ce_source      | string | Source of the CloudEvent.                 | -                        | | format        | -                 |
|                |        |                                           |                          | | (`uri`)       |                   |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| ce_type        | string | | Event type. It can be one of            | -                        | -               | -                 |
|                |        | | topology-inventory-ingestion-merge,     |                          |                 |                   |
|                |        | | topology-inventory-ingestion-delete, or |                          |                 |                   |
|                |        | | topology-inventory-ingestion-create.    |                          |                 |                   |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| content-type   | string | | Content-type of the data contained      | | const                  | -               | -                 |
|                |        | | within the cloud event. It is           | | (`"application/json"`) |                 |                   |
|                |        | | application/json.                       |                          |                 |                   |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| ce_dataschema  | string | | URI representing the schema of the data | -                        | | format        | -                 |
|                |        | | This references the event-specific yang |                          | | (`uri`)       |                   |
|                |        | | schema.                                 |                          |                 |                   |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| ce_time        | string | Event timestamp.                          | -                        | | format        | -                 |
|                |        |                                           |                          | | (`date-time`) |                   |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| ce_specversion | string | | The version of the CloudEvents          | const (`"1.0"`)          | -               | -                 |
|                |        | | specification which the event uses.     |                          |                 |                   |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+

..

   Examples of headers *(generated)*

.. code:: json


   {
     "ce_specversion": "1.0",
     "ce_id": "a30e63c9-d29e-46ff-b99a-b63ed83fd234",
     "ce_source": "dmi-plugin:nm-1",
     "ce_type": "topology-inventory-ingestion-merge",
     "ce_time": "2023-06-12T09:05:00Z",
     "content-type": "application/json",
     "ce_dataschema": "topology-inventory-ingestion:events:merge:1.0.0"
   }

MERGE Payload
^^^^^^^^^^^^^

+------------------------------------------------------+---------------+----------------------------------------------------------------+
| Name                                                 | Type          | Description                                                    |
+======================================================+===============+================================================================+
| (root)                                               | Object        | -                                                              |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| data                                                 | -             | | The data part consists of the actual topology data. It       |
|                                                      |               | | contains all the entities and their associated               |
|                                                      |               | | relationships.                                               |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| data.entities                                        | Array<Object> | | Entities are topology objects comprising of an id, consumer  |
|                                                      |               | | data, attributes and metadata for each. It contains the id   |
|                                                      |               | | only in case of delete cloud event.                          |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| | data.entities                                      | Object        | | Entities schema is adherent to the entity types and          |
| | .<module_name>                                     |               | | attributes mentioned in the yang modules. For yang modules,  |
| | :<mo_type>                                         |               | | see [Data Models][Data Models].                              |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| data.relationships                                   | Array<Object> | | Relationships comprising of an A-side and a B-side for each. |
|                                                      |               | | The A-side is considered the originating side of the         |
|                                                      |               | | relationship; the B-side is considered the terminating side  |
|                                                      |               | | of the relationship. The order of A-side and B-side is of    |
|                                                      |               | | importance and MUST NOT be changed once defined. It          |
|                                                      |               | | contains the id only in case of delete event.                |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| | data.relationships                                 | Object        | | Relationship schema is adherent to the relationship types    |
| | .<module_name>                                     |               | | mentioned in the yang modules. For yang modules,             |
| | :<relationship_type>                               |               | | see [Data Models][Data Models].                              |
+------------------------------------------------------+---------------+----------------------------------------------------------------+

..

   Examples of payload *(generated)*

.. code:: json

   {
     "data": {
       "entities": [
         {
           "o-ran-smo-teiv-equipment:AntennaModule": [
             {
               "id": "urn:oran:smo:teiv:AntennaModule=1",
               "attributes": {
                 "antennaModelNumber": "1",
                 "mechanicalAntennaBearing": 50,
                 "mechanicalAntennaTilt": 10,
                 "positionWithinSector": "Unknown",
                 "totalTilt": 14,
                 "electricalAntennaTilt": 2,
                 "antennaBeamWidth": [
                   35,
                   23,
                   21
                 ],
                 "geo-location": {
                   "latitude": 41.73297,
                   "longitude": -73.007696,
                   "height": 3000
                 }
               },
               "sourceIds": [
                 "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1",
                 "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1",
                 "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=1,RetSubUnit=1"
               ],
               "metadata": {
                 "trustLevel": "RELIABLE"
               }
             }
           ]
         },
         {
           "o-ran-smo-teiv-equipment:AntennaModule": [
             {
               "id": "urn:oran:smo:teiv:AntennaModule=2",
               "attributes": {
                 "antennaModelNumber": "2",
                 "mechanicalAntennaBearing": 61,
                 "mechanicalAntennaTilt": 21,
                 "positionWithinSector": "Unknown",
                 "totalTilt": 25,
                 "electricalAntennaTilt": 3,
                 "antennaBeamWidth": [
                   46,
                   34,
                   32
                 ],
                 "geo-location": {
                   "latitude": 52.84308,
                   "longitude": -84.118707,
                   "height": 41111
                 }
               },
               "sourceIds": [
                 "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=2",
                 "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=2,AntennaSubunit=1",
                 "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=2,RetSubUnit=1"
               ],
               "metadata": {
                 "trustLevel": "RELIABLE"
               }
             }
           ]
         }
       ],
       "relationships": [
         {
           "o-ran-smo-teiv-equipment:ANTENNAMODULE_INSTALLED_AT_SITE": [
             {
               "id": "urn:sha512:TlJDZWxsRFU6U3ViTmV0d29yaz1FdXJvcGUsU3ViTmV0d29yaz1JcmVs=",
               "aSide": "urn:oran:smo:teiv:AntennaModule=1",
               "bSide": "urn:oran:smo:teiv:Site=1"
             }
           ]
         },
         {
           "o-ran-smo-teiv-equipment:ANTENNAMODULE_INSTALLED_AT_SITE": [
             {
               "id": "urn:sha512:TlJDZWxsRFU6U3ViTmV0d29yaz1FdXJvcGUsU3ViTmV0d29yaz1JcmVsYW5kLE1lQ2=",
               "aSide": "urn:oran:smo:teiv:AntennaModule=2",
               "bSide": "urn:oran:smo:teiv:Site=2"
             }
           ]
         }
       ]
     }
   }

.. _Ingestion Delete:

PUB ``ingestionDelete`` Operation
---------------------------------

*Topology & Inventory entities and relationships can be deleted.*

-  Operation ID: ``Ingestion``

Operation on Topology Inventory and Exposure input topic.

Message Topology DELETE ``ingestionDelete``
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

*Deletes components in topology.*

-  Message ID: ``delete``
-  Content type: application/json

DELETE Headers
^^^^^^^^^^^^^^

+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| Name           | Type   | Description                               | Value                    | Constraints     | Notes             |
+================+========+===========================================+==========================+=================+===================+
| (root)         | object | -                                         | -                        | -               | | **additional**  |
|                |        |                                           |                          |                 | | **properties**  |
|                |        |                                           |                          |                 | | **are allowed** |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| ce_id          | string | Unique identifier for the event.          | -                        | -               | -                 |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| ce_source      | string | Source of the CloudEvent.                 | -                        | | format        | -                 |
|                |        |                                           |                          | | (`uri`)       |                   |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| ce_type        | string | | Event type. It can be one of            | -                        | -               | -                 |
|                |        | | topology-inventory-ingestion-merge,     |                          |                 |                   |
|                |        | | topology-inventory-ingestion-delete, or |                          |                 |                   |
|                |        | | topology-inventory-ingestion-create.    |                          |                 |                   |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| content-type   | string | | Content-type of the data contained      | | const                  | -               | -                 |
|                |        | | within the cloud event. It is           | | (`"application/json"`) |                 |                   |
|                |        | | application/json.                       |                          |                 |                   |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| ce_dataschema  | string | | URI representing the schema of the data | -                        | | format        | -                 |
|                |        | | This references the event-specific yang |                          | | (`uri`)       |                   |
|                |        | | schema.                                 |                          |                 |                   |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| ce_time        | string | Event timestamp.                          | -                        | | format        | -                 |
|                |        |                                           |                          | | (`date-time`) |                   |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| ce_specversion | string | | The version of the CloudEvents          | const (`"1.0"`)          | -               | -                 |
|                |        | | specification which the event uses.     |                          |                 |                   |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+

..

   Examples of headers *(generated)*

.. code:: json

   {
     "ce_specversion": "1.0",
     "ce_id": "a30e63c9-d29e-46ff-b99a-b63ed83fd235",
     "ce_source": "dmi-plugin:nm-1",
     "ce_type": "topology-inventory-ingestion-delete",
     "ce_time": "2023-06-12T09:05:00Z",
     "content-type": "application/json",
     "ce_dataschema": "topology-inventory-ingestion:events:delete:1.0.0"
   }

DELETE Payload
^^^^^^^^^^^^^^

+------------------------------------------------------+---------------+----------------------------------------------------------------+
| Name                                                 | Type          | Description                                                    |
+======================================================+===============+================================================================+
| (root)                                               | Object        | -                                                              |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| data                                                 | -             | | The data part consists of the actual topology data. It       |
|                                                      |               | | contains all the entities and their associated               |
|                                                      |               | | relationships.                                               |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| data.entities                                        | Array<Object> | | Entities are topology objects comprising of an id, consumer  |
|                                                      |               | | data, attributes and metadata for each. It contains the id   |
|                                                      |               | | only in case of delete cloud event.                          |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| | data.entities                                      | Object        | | Entities schema is adherent to the entity types and          |
| | .<module_name>                                     |               | | attributes mentioned in the yang modules. For yang modules,  |
| | :<mo_type>                                         |               | | see [Data Models][Data Models].                              |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| data.relationships                                   | Array<Object> | | Relationships comprising of an A-side and a B-side for each. |
|                                                      |               | | The A-side is considered the originating side of the         |
|                                                      |               | | relationship; the B-side is considered the terminating side  |
|                                                      |               | | of the relationship. The order of A-side and B-side is of    |
|                                                      |               | | importance and MUST NOT be changed once defined. It          |
|                                                      |               | | contains the id only in case of delete event.                |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| | data.relationships                                 | Object        | | Relationship schema is adherent to the relationship types    |
| | .<module_name>                                     |               | | mentioned in the yang modules. For yang modules,             |
| | :<relationship_type>                               |               | | see [Data Models][Data Models].                              |
+------------------------------------------------------+---------------+----------------------------------------------------------------+

..

   Examples of payload *(generated)*

.. code:: json

   {
     "data": {
       "entities": [
         {
           "o-ran-smo-teiv-equipment:AntennaModule": [
             {
               "id": "urn:oran:smo:teiv:AntennaModule=1"
             }
           ]
         },
         {
           "o-ran-smo-teiv-equipment:AntennaModule": [
             {
               "id": "urn:oran:smo:teiv:AntennaModule=2"
             }
           ]
         }
       ],
       "relationships": [
         {
           "o-ran-smo-teiv-equipment:ANTENNAMODULE_INSTALLED_AT_SITE": [
             {
               "id": "urn:sha512:TlJDZWxsRFU6U3ViTmV0d29yaz1FdXJvcGUsU3ViTmV0d29yaz1JcmVs="
             }
           ]
         },
         {
           "o-ran-smo-teiv-equipment:ANTENNAMODULE_INSTALLED_AT_SITE": [
             {
               "id": "urn:sha512:TlJDZWxsRFU6U3ViTmV0d29yaz1FdXJvcGUsU3ViTmV0d29yaz1JcmVsYW5kLE1lQ2="
             }
           ]
         }
       ]
     }
   }