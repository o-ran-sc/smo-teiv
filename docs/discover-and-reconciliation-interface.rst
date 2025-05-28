.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. SPDX-License-Identifier: CC-BY-4.0
.. Copyright (C) 2024-2025 Nordix Foundation. All rights Reserved
.. Copyright (C) 2024 OpenInfra Foundation Europe. All Rights Reserved

Discover and Reconciliation Interface
#####################################

Discover and Reconciliation Interface to enrich topology with data.

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
| ce_source      | string | Source of the CloudEvent.                 | | template ("adapterId(  | | format        | | This is a       |
|                |        |                                           | | {namespace}-           | | (`uri`)       | | template for    |
|                |        |                                           | | {nameOverride})")      |                 | | the adapterId   |
|                |        |                                           |                          |                 | | structure. An   |
|                |        |                                           |                          |                 | | example is      |
|                |        |                                           |                          |                 | | "dmi-2-oss-ran- |
|                |        |                                           |                          |                 | | topology-       |
|                |        |                                           |                          |                 | | adapter"        |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| ce_type        | string | | Event type. It can be one of            | -                        | -               | -                 |
|                |        | | topology-inventory-ingestion.merge,     |                          |                 |                   |
|                |        | | topology-inventory-ingestion.delete, or |                          |                 |                   |
|                |        | | topology-inventory-ingestion.create.    |                          |                 |                   |
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
     "ce_type": "topology-inventory-ingestion.create",
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
| entities                                             | Array<Object> | | Entities are topology objects comprising of an id, consumer  |
|                                                      |               | | data, attributes and metadata for each. It contains the id   |
|                                                      |               | | only in case of delete cloud event.                          |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| | entities                                           | Object        | | Entities schema is adherent to the entity types and          |
| | .<module_name>                                     |               | | attributes mentioned in the yang modules. For yang modules,  |
| | :<mo_type>                                         |               | | see [Data Models][Data Models].                              |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| relationships                                        | Array<Object> | | Relationships comprising of an A-side and a B-side for each. |
|                                                      |               | | The A-side is considered the originating side of the         |
|                                                      |               | | relationship; the B-side is considered the terminating side  |
|                                                      |               | | of the relationship. The order of A-side and B-side is of    |
|                                                      |               | | importance and MUST NOT be changed once defined. It          |
|                                                      |               | | contains the id only in case of delete event.                |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| | relationships                                      | Object        | | Relationship schema is adherent to the relationship types    |
| | .<module_name>                                     |               | | mentioned in the yang modules. For yang modules,             |
| | :<relationship_type>                               |               | | see [Data Models][Data Models].                              |
+------------------------------------------------------+---------------+----------------------------------------------------------------+

   Examples of payload *(generated)*

.. code:: json

    {
      "entities": [
        {
          "o-ran-smo-teiv-equipment:Site": [
            {
              "id": "urn:o-ran:smo:teiv:sha512:Site=8864DE35AF8F7552810308401DE712AD07877BBA7568860029BCECD3667F7A9D6DF5DFDA72BF475E5153BBE3035AAC229AD63DECC539C541B45598509088DB4E",
              "attributes": {
                "name": "Dublin",
                "geo-location": {
                  "latitude": 41.73297,
                  "longitude": -73.007696
                }
              },
               "sourceIds": [
                  "urn:oran:smo:teiv:atoll:Site=1"
               ]
            }
          ]
        },
        {
          "o-ran-smo-teiv-equipment:Site": [
            {
              "id": "urn:o-ran:smo:teiv:sha512:Site=32fedd82fd4a6d1c21ab68eb35c725fdd4ffcca963ea17e90775c9608829b03655d2133238df83334f7824bef4230292f97653b4a426847daa55005162e7c697",
              "attributes": {
                "name": "Dublin",
                "geo-location": {
                  "latitude": 41.73297,
                  "longitude": -73.007696
                }
              },
               "sourceIds": [
                  "urn:oran:smo:teiv:atoll:Site=2"
               ]
            }
          ]
        },
        {
          "o-ran-smo-teiv-equipment:AntennaModule": [
            {
              "id": "urn:oran:smo:teiv:sha512:AntennaModule=1FEBF137533843657E9E9DBE60DBD86B045A057DB6D04B6A07AC15323F1906228E93CFA4A1DB37D50252B3AFE6AEC9860E2CEA4A77BB3A25C9EA45DEDA87E765",
              "attributes": {
                "geo-location": {
                  "latitude": 41.73297,
                  "longitude": -73.007696,
                  "height": 3000
                }
              },
              "sourceIds": [
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1",
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1",
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=1,RetSubUnit=1",
                "urn:cmHandle:72538B1D598FA5A901D945A187D5542A"
              ]
            }
          ]
        },
        {
          "o-ran-smo-teiv-equipment:AntennaModule": [
            {
              "id": "urn:oran:smo:teiv:sha512:AntennaModule=3FF03633DCCAF1C44409FA0D0D3C32F00635DDAD5363A5E175A04A4AE5125641FCC6D727801275E8E6879AFB6D342B3E9473CC1307A702E41389882ECB513C8A",
              "attributes": {
                "geo-location": {
                  "latitude": 52.84308,
                  "longitude": -84.118707,
                  "height": 41111
                }
              },
              "sourceIds": [
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=2",
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=2,AntennaSubunit=1",
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=2,RetSubUnit=1",
                "urn:cmHandle:72538B1D598FA5A901D945A187D5542A"
              ]
            }
          ]
        }
      ],
      "relationships": [
        {
          "o-ran-smo-teiv-equipment:ANTENNAMODULE_INSTALLED_AT_SITE": [
            {
              "id": "urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_INSTALLED_AT_SITE=E725018642FCC6D9BD7EB846DF31F080B878420A9C5E002CFB39F2AAEB6D3D66E655A132DB0852C6984B2052ABB62B1815A9C802A35ED865F8992328F1144C25",
              "aSide": "urn:oran:smo:teiv:sha512:AntennaModule=1FEBF137533843657E9E9DBE60DBD86B045A057DB6D04B6A07AC15323F1906228E93CFA4A1DB37D50252B3AFE6AEC9860E2CEA4A77BB3A25C9EA45DEDA87E765",
              "bSide": "urn:oran:smo:teiv:sha512:Site=8864DE35AF8F7552810308401DE712AD07877BBA7568860029BCECD3667F7A9D6DF5DFDA72BF475E5153BBE3035AAC229AD63DECC539C541B45598509088DB4E",
              "sourceIds": [
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1",
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1",
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=1,RetSubUnit=1",
                "urn:cmHandle:72538B1D598FA5A901D945A187D5542A",
                "urn:oran:smo:teiv:atoll:Site=1"
              ]
            }
          ]
        },
        {
          "o-ran-smo-teiv-equipment:ANTENNAMODULE_INSTALLED_AT_SITE": [
            {
              "id": "urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_INSTALLED_AT_SITE=3262e72478e8ee8cc26c62600ecd93ad917c3d6bcc4db8e7baa14dd1ecc8dff0c660b28bbd93c55bb71de4ebc5a10e32ea2d40147e24a086b2e57556f4552170",
              "aSide": "urn:oran:smo:teiv:sha512:AntennaModule=3FF03633DCCAF1C44409FA0D0D3C32F00635DDAD5363A5E175A04A4AE5125641FCC6D727801275E8E6879AFB6D342B3E9473CC1307A702E41389882ECB513C8A",
              "bSide": "urn:oran:smo:teiv:sha512:Site=32fedd82fd4a6d1c21ab68eb35c725fdd4ffcca963ea17e90775c9608829b03655d2133238df83334f7824bef4230292f97653b4a426847daa55005162e7c697",
              "sourceIds": [
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=2",
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=2,AntennaSubunit=1",
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=2,RetSubUnit=1",
                "urn:cmHandle:72538B1D598FA5A901D945A187D5542A",
                "urn:oran:smo:teiv:atoll:Site=2"
              ]
            }
          ]
        }
      ]
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
| ce_source      | string | Source of the CloudEvent.                 | | template ("adapterId(  | | format        | | This is a       |
|                |        |                                           | | {namespace}-           | | (`uri`)       | | template for    |
|                |        |                                           | | {nameOverride})")      |                 | | the adapterId   |
|                |        |                                           |                          |                 | | structure. An   |
|                |        |                                           |                          |                 | | example is      |
|                |        |                                           |                          |                 | | "dmi-2-oss-ran- |
|                |        |                                           |                          |                 | | topology-       |
|                |        |                                           |                          |                 | | adapter"        |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| ce_type        | string | | Event type. It can be one of            | -                        | -               | -                 |
|                |        | | topology-inventory-ingestion.merge,     |                          |                 |                   |
|                |        | | topology-inventory-ingestion.delete, or |                          |                 |                   |
|                |        | | topology-inventory-ingestion.create.    |                          |                 |                   |
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
     "ce_type": "topology-inventory-ingestion.merge",
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
| entities                                             | Array<Object> | | Entities are topology objects comprising of an id, consumer  |
|                                                      |               | | data, attributes and metadata for each. It contains the id   |
|                                                      |               | | only in case of delete cloud event.                          |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| | entities                                           | Object        | | Entities schema is adherent to the entity types and          |
| | .<module_name>                                     |               | | attributes mentioned in the yang modules. For yang modules,  |
| | :<mo_type>                                         |               | | see [Data Models][Data Models].                              |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| relationships                                        | Array<Object> | | Relationships comprising of an A-side and a B-side for each. |
|                                                      |               | | The A-side is considered the originating side of the         |
|                                                      |               | | relationship; the B-side is considered the terminating side  |
|                                                      |               | | of the relationship. The order of A-side and B-side is of    |
|                                                      |               | | importance and MUST NOT be changed once defined. It          |
|                                                      |               | | contains the id only in case of delete event.                |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| | relationships                                      | Object        | | Relationship schema is adherent to the relationship types    |
| | .<module_name>                                     |               | | mentioned in the yang modules. For yang modules,             |
| | :<relationship_type>                               |               | | see [Data Models][Data Models].                              |
+------------------------------------------------------+---------------+----------------------------------------------------------------+

..

   Examples of payload *(generated)*

.. code:: json


    {
      "entities": [
        {
          "o-ran-smo-teiv-equipment:Site": [
            {
              "id": "urn:o-ran:smo:teiv:sha512:Site=8864DE35AF8F7552810308401DE712AD07877BBA7568860029BCECD3667F7A9D6DF5DFDA72BF475E5153BBE3035AAC229AD63DECC539C541B45598509088DB4E",
              "attributes": {
                "name": "Dublin",
                "geo-location": {
                  "latitude": 41.73297,
                  "longitude": -73.007696
                }
              },
               "sourceIds": [
                  "urn:oran:smo:teiv:atoll:Site=1"
               ]
            }
          ]
        },
          {
            "o-ran-smo-teiv-equipment:Site": [
            {
              "id": "urn:o-ran:smo:teiv:sha512:Site=32fedd82fd4a6d1c21ab68eb35c725fdd4ffcca963ea17e90775c9608829b03655d2133238df83334f7824bef4230292f97653b4a426847daa55005162e7c697",
              "attributes": {
                "name": "Dublin",
                "geo-location": {
                  "latitude": 41.73297,
                  "longitude": -73.007696
                }
              },
               "sourceIds": [
                  "urn:oran:smo:teiv:atoll:Site=2"
               ]
            }
          ]
        },
        {
          "o-ran-smo-teiv-equipment:AntennaModule": [
            {
              "id": "urn:oran:smo:teiv:sha512:AntennaModule=1FEBF137533843657E9E9DBE60DBD86B045A057DB6D04B6A07AC15323F1906228E93CFA4A1DB37D50252B3AFE6AEC9860E2CEA4A77BB3A25C9EA45DEDA87E765",
              "attributes": {
                "geo-location": {
                  "latitude": 41.73289,
                  "longitude": -73.007687,
                  "height": 3000
                }
              },
              "sourceIds": [
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1",
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1",
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=1,RetSubUnit=1",
                "urn:cmHandle:72538B1D598FA5A901D945A187D5542A"
              ]
            }
          ]
        },
        {
          "o-ran-smo-teiv-equipment:AntennaModule": [
            {
              "id": "urn:oran:smo:teiv:sha512:AntennaModule=3FF03633DCCAF1C44409FA0D0D3C32F00635DDAD5363A5E175A04A4AE5125641FCC6D727801275E8E6879AFB6D342B3E9473CC1307A702E41389882ECB513C8A",
              "attributes": {
                "geo-location": {
                  "latitude": 52.84398,
                  "longitude": -84.118776,
                  "height": 41111
                }
              },
              "sourceIds": [
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=2",
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=2,AntennaSubunit=1",
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=2,RetSubUnit=1",
                "urn:cmHandle:72538B1D598FA5A901D945A187D5542A"
              ]
            }
          ]
        }
      ],
      "relationships": [
        {
          "o-ran-smo-teiv-equipment:ANTENNAMODULE_INSTALLED_AT_SITE": [
            {
              "id": "urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_INSTALLED_AT_SITE=E725018642FCC6D9BD7EB846DF31F080B878420A9C5E002CFB39F2AAEB6D3D66E655A132DB0852C6984B2052ABB62B1815A9C802A35ED865F8992328F1144C25",
              "aSide": "urn:oran:smo:teiv:sha512:AntennaModule=1FEBF137533843657E9E9DBE60DBD86B045A057DB6D04B6A07AC15323F1906228E93CFA4A1DB37D50252B3AFE6AEC9860E2CEA4A77BB3A25C9EA45DEDA87E765",
              "bSide": "urn:oran:smo:teiv:sha512:Site=8864DE35AF8F7552810308401DE712AD07877BBA7568860029BCECD3667F7A9D6DF5DFDA72BF475E5153BBE3035AAC229AD63DECC539C541B45598509088DB4E",
              "sourceIds": [
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1",
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1",
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=1,RetSubUnit=1",
                "urn:cmHandle:72538B1D598FA5A901D945A187D5542A",
                "urn:oran:smo:teiv:atoll:Site=1"
              ]
            }
          ]
        },
        {
          "o-ran-smo-teiv-equipment:ANTENNAMODULE_INSTALLED_AT_SITE": [
            {
              "id": "urn:o-ran:smo:teiv:sha512:urn:sha512:ANTENNAMODULE_INSTALLED_AT_SITE=3262e72478e8ee8cc26c62600ecd93ad917c3d6bcc4db8e7baa14dd1ecc8dff0c660b28bbd93c55bb71de4ebc5a10e32ea2d40147e24a086b2e57556f4552170",
              "aSide": "urn:oran:smo:teiv:sha512:AntennaModule=3FF03633DCCAF1C44409FA0D0D3C32F00635DDAD5363A5E175A04A4AE5125641FCC6D727801275E8E6879AFB6D342B3E9473CC1307A702E41389882ECB513C8A",
              "bSide": "urn:oran:smo:teiv:sha512:Site=32fedd82fd4a6d1c21ab68eb35c725fdd4ffcca963ea17e90775c9608829b03655d2133238df83334f7824bef4230292f97653b4a426847daa55005162e7c697",
              "sourceIds": [
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=2",
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaUnit=2,AntennaSubunit=1",
                "urn:3gpp:dn:ManagedElement=NR01,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=2,RetSubUnit=1",
                "urn:cmHandle:72538B1D598FA5A901D945A187D5542A",
                "urn:oran:smo:teiv:atoll:Site=2"
              ]
            }
          ]
        }
      ]
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
| ce_source      | string | Source of the CloudEvent.                 | | template ("adapterId(  | | format        | | This is a       |
|                |        |                                           | | {namespace}-           | | (`uri`)       | | template for    |
|                |        |                                           | | {nameOverride})")      |                 | | the adapterId   |
|                |        |                                           |                          |                 | | structure. An   |
|                |        |                                           |                          |                 | | example is      |
|                |        |                                           |                          |                 | | "dmi-2-oss-ran- |
|                |        |                                           |                          |                 | | topology-       |
|                |        |                                           |                          |                 | | adapter"        |
+----------------+--------+-------------------------------------------+--------------------------+-----------------+-------------------+
| ce_type        | string | | Event type. It can be one of            | -                        | -               | -                 |
|                |        | | topology-inventory-ingestion.merge,     |                          |                 |                   |
|                |        | | topology-inventory-ingestion.delete, or |                          |                 |                   |
|                |        | | topology-inventory-ingestion.create.    |                          |                 |                   |
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
     "ce_type": "topology-inventory-ingestion.delete",
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
| entities                                             | Array<Object> | | Entities are topology objects comprising of an id, consumer  |
|                                                      |               | | data, attributes and metadata for each. It contains the id   |
|                                                      |               | | only in case of delete cloud event.                          |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| | entities                                           | Object        | | Entities schema is adherent to the entity types and          |
| | .<module_name>                                     |               | | attributes mentioned in the yang modules. For yang modules,  |
| | :<mo_type>                                         |               | | see [Data Models][Data Models].                              |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| relationships                                        | Array<Object> | | Relationships comprising of an A-side and a B-side for each. |
|                                                      |               | | The A-side is considered the originating side of the         |
|                                                      |               | | relationship; the B-side is considered the terminating side  |
|                                                      |               | | of the relationship. The order of A-side and B-side is of    |
|                                                      |               | | importance and MUST NOT be changed once defined. It          |
|                                                      |               | | contains the id only in case of delete event.                |
+------------------------------------------------------+---------------+----------------------------------------------------------------+
| | relationships                                      | Object        | | Relationship schema is adherent to the relationship types    |
| | .<module_name>                                     |               | | mentioned in the yang modules. For yang modules,             |
| | :<relationship_type>                               |               | | see [Data Models][Data Models].                              |
+------------------------------------------------------+---------------+----------------------------------------------------------------+

..

   Examples of payload *(generated)*

.. code:: json

   {
      "entities": [
        {
          "o-ran-smo-teiv-equipment:Site": [
            {
              "id": "urn:o-ran:smo:teiv:sha512:Site=8864DE35AF8F7552810308401DE712AD07877BBA7568860029BCECD3667F7A9D6DF5DFDA72BF475E5153BBE3035AAC229AD63DECC539C541B45598509088DB4E"
            }
          ]
        },
          {
            "o-ran-smo-teiv-equipment:Site": [
            {
              "id": "urn:o-ran:smo:teiv:sha512:Site=32fedd82fd4a6d1c21ab68eb35c725fdd4ffcca963ea17e90775c9608829b03655d2133238df83334f7824bef4230292f97653b4a426847daa55005162e7c697"
            }
          ]
        },
          {
            "o-ran-smo-teiv-equipment:AntennaModule": [
            {
              "id": "urn:oran:smo:teiv:AntennaModule=1FEBF137533843657E9E9DBE60DBD86B045A057DB6D04B6A07AC15323F1906228E93CFA4A1DB37D50252B3AFE6AEC9860E2CEA4A77BB3A25C9EA45DEDA87E765"
            }
          ]
        },
        {
          "o-ran-smo-teiv-equipment:AntennaModule": [
            {
              "id": "urn:oran:smo:teiv:AntennaModule=3FF03633DCCAF1C44409FA0D0D3C32F00635DDAD5363A5E175A04A4AE5125641FCC6D727801275E8E6879AFB6D342B3E9473CC1307A702E41389882ECB513C8A"
            }
          ]
        }
      ],
      "relationships": [
        {
          "o-ran-smo-teiv-equipment:ANTENNAMODULE_INSTALLED_AT_SITE": [
            {
              "id": "urn:sha512:ANTENNAMODULE_INSTALLED_AT_SITE=E725018642FCC6D9BD7EB846DF31F080B878420A9C5E002CFB39F2AAEB6D3D66E655A132DB0852C6984B2052ABB62B1815A9C802A35ED865F8992328F1144C25"
            }
          ]
        },
        {
          "o-ran-smo-teiv-equipment:ANTENNAMODULE_INSTALLED_AT_SITE": [
            {
              "id": "urn:sha512:ANTENNAMODULE_INSTALLED_AT_SITE=3262e72478e8ee8cc26c62600ecd93ad917c3d6bcc4db8e7baa14dd1ecc8dff0c660b28bbd93c55bb71de4ebc5a10e32ea2d40147e24a086b2e57556f4552170"
            }
          ]
        }
      ]
    }