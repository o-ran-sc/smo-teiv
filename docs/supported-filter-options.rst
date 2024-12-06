.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. SPDX-License-Identifier: CC-BY-4.0
.. Copyright (C) 2024 Nordix Foundation. All rights Reserved
.. Copyright (C) 2024 OpenInfra Foundation Europe. All Rights Reserved

Supported filter options
========================

Using the filtering options, it is possible to define more specific
requests in case of several endpoints. To perform filtering,
*targetFilter* and *scopeFilter* parameters can be used in the path.

Sample structure using target and scope filters:

::

   GET https://<host>/topology-inventory/<API_VERSION>/domains/<domainName>?targetFilter=<targetFilter>&scopeFilter=<scopeFilter>

..

   See :doc:`Topology & Inventory API <api-documentation>` for all possible
   filter options and sample responses for each endpoint.

Querying simple entities
------------------------

   This functionality is supported by the following endpoints:
   **/domains/{domainName}/entity-types/{entityTypeName}/entities**

The *entityTypeName* is used as the root of the queries (from here
referred as RootObject). Every other object, either in *targetFilter* or
*scopeFilter*, has to relate to the RootObject. The queries are
constructed starting from the RootObject and all other objects are
joined to it. If there is no connection between the RootObject and the
other object(s), the query is not constructed. The RootObject still can
be retrieved and filtered using the /attributes.

+------------------------------------------+--------+----------------+--------------+------------------------+------------------------+
| Use case                                 | domain | entityTypeName | targetFilter | scopeFilter            | Query result           |
|                                          |        |                |              |                        |                        |
|                                          | Name   |                |              |                        |                        |
+==========================================+========+================+==============+========================+========================+
| | To return the ids for all instances of | RAN    | ODUFunction    |              |                        | | All ids of every     |
| | the entityTypeName used in the query.  |        |                |              |                        | | ODUFunction          |
+------------------------------------------+--------+----------------+--------------+------------------------+------------------------+
| | To return the metadata of every        | RAN    | ODUFunction    | /metadata    |                        | | All ODUFunctions     |
| | instance of the entitytypeName used in |        |                |              |                        | | with metadata        |
| | the query.                             |        |                |              |                        |                        |
+------------------------------------------+--------+----------------+--------------+------------------------+------------------------+
| | To return all attributes of every      | RAN    | ODUFunction    | /attributes  |                        | | All ODUFunctions     |
| | instance of the entityTypeName used    |        |                |              |                        | | with every attribute |
| | in the query.                          |        |                |              |                        |                        |
+------------------------------------------+--------+----------------+--------------+------------------------+------------------------+
| | To return every instance of the        | RAN    | ODUFunction    | /attributes  |                        | | All gNBIds of every  |
| | entityTypeName used in the query, but  |        |                | (gNBId)      |                        | | ODUFunction          |
| | only the attribute that was            |        |                |              |                        |                        |
| | defined in the *targetFilter*          |        |                |              |                        |                        |
| | parameter. Note: The attribute must be |        |                |              |                        |                        |
| | a valid field of the object.           |        |                |              |                        |                        |
+------------------------------------------+--------+----------------+--------------+------------------------+------------------------+
| | To return every instance of the        | RAN    | ODUFunction    | /attributes  |                        | | All gNBIds and       |
| | entityTypeName used in the query, but  |        |                |              |                        | | gNBIdLengths of      |
| | only the attributes that were          |        |                | (gNBId,      |                        | | every ODUFunction    |
| | defined in the *targetFilter*          |        |                | gNBIdLength) |                        |                        |
| | parameter. Note: The attributes must   |        |                |              |                        |                        |
| | be separated by a comma "," when       |        |                |              |                        |                        |
| | using parenthesis "()".                |        |                |              |                        |                        |
+------------------------------------------+--------+----------------+--------------+------------------------+------------------------+
| | To return the ids for all instances of | RAN    | ODUFunction    |              | /sourceIds             | | Unique set of ids    |
| | the entityTypeName used in the query,  |        |                |              | [contains (@item,      | | of ODUFunctions,     |
| | that partially matches the given       |        |                |              | 'SubNetwork=Ireland')] | | where sourceIds      |
| | property in the *scopeFilter*          |        |                |              |                        | | contains             |
| | parameter.                             |        |                |              |                        | | *SubNetwork=Ireland* |
+------------------------------------------+--------+----------------+--------------+------------------------+------------------------+
| | To return the metadata for all         | RAN    | ODUFunction    |              | /metadata              | | Unique set of        |
| | instances of the entityTypeName used   |        |                |              | [@reliabilityIndicator | | metadata of          |
| | in the query, that matches the given   |        |                |              | ='OK']                 | | ODUFunctions, where  |
| | property in the *scopeFilter*          |        |                |              |                        | | reliabilityIndicator |
| | parameter                              |        |                |              |                        | | equals OK            |
+------------------------------------------+--------+----------------+--------------+------------------------+------------------------+
| | To return the ids for all instances of | RAN    | ODUFunction    |              | /attributes            | | Unique set of ids of |
| | the entityTypeName used in the query,  |        |                |              | [@gNBId                | | ODUFunctions,where   |
| | that matches the given attributes in   |        |                |              | Length=3 and           | | the gNBIdLength      |
| | the *scopeFilter* parameter. Note: The |        |                |              | @gNBId=111]            | | equals 3 and the     |
| | attributes must be separated by a      |        |                |              |                        | | gNBId equals 111     |
| | *AND* or *OR*".                        |        |                |              |                        |                        |
+------------------------------------------+--------+----------------+--------------+------------------------+------------------------+
| | To return the ids for all instances of | RAN    | ODUFunction    |              | /attributes            | | Unique set of ids of |
| | the entityTypeName used in the query,  |        |                |              | [@gNBId Length=3] |    | | ODUFunctions,        |
| | that satisfies one of                  |        |                |              |                        | | where the            |
| | the conditions in the *scopeFilter*    |        |                |              | /sourceIds             | | gNBIdLength equals 3 |
| | parameter. A condition is a complete   |        |                |              | [contains (@item,      | | or the sourceIds     |
| | unit of *scopeFilter*                  |        |                |              | 'SubNetwork=Ireland')] | | contains an item     |
| | represent OR.                          |        |                |              |                        | | with                 |
| | parameter surrounded by square         |        |                |              |                        | | "SubNetwork=Ireland" |
| | brackets. Note: Multiple conditions    |        |                |              |                        |                        |
| | can be given in the scopeFilter        |        |                |              |                        |                        |
| | separated by a semicolon ";" to        |        |                |              |                        |                        |
| | represent AND, or a pipe symbol "|" to |        |                |              |                        |                        |
+------------------------------------------+--------+----------------+--------------+------------------------+------------------------+

Querying connected entities
---------------------------

   This functionality is achieved using associations and is supported by
   the following endpoints:
   **/domains/{domainName}/entity-types/{entityTypeName}/entities**

The *entityTypeName* is used as the root of the queries.

+------------------------------------------+-------------+----------------+--------------+----------------------------+--------------------------------------------------+
| Use case                                 | domainName  | entityTypeName | targetFilter | scopeFilter                | Query result                                     |
+==========================================+=============+================+==============+============================+==================================================+
| | To return the ids for all instances of | REL_OAM_RAN | ODUFunction    |              | /managed-by-managedElement | | All ODUFunction entities that are managed      |
| | an entityTypeName related by an        |             |                |              |                            | | by any Managed Element.                        |
| | association.                           |             |                |              |                            |                                                  |
+------------------------------------------+-------------+----------------+--------------+----------------------------+--------------------------------------------------+
| | To return the ids for all instances of | REL_OAM_RAN | ODUFunction    |              | /managed-by-managedElement | | All ODUFunction entities that are managed      |
| | an entityTypeName related by an        |             |                |              | [@id = 'urn\:3gpp:dn:      | | by the Managed Element                         |
| | association to another entity          |             |                |              | ManagedElement=1']         | | *urn\:3gpp:dn: ManagedElement=1*.              |
| | specified by its *id*.                 |             |                |              |                            |                                                  |
+------------------------------------------+-------------+----------------+--------------+----------------------------+--------------------------------------------------+
| | To return the attributes for all       | REL_OAM_RAN | ODUFunction    | /attributes  | /attributes [@enbId=1];    | | All ODUFunction entities with enbId as *1*     |
| | instances of an entityTypeName         |             |                |              |                            | | managed by the Managed Element                 |
| | related by one or more associations    |             |                |              | /managed-by-managedElement | | *urn\:3gpp:dn: ManagedElement=1* or            |
| | to other entities specified by their   |             |                |              | [@id='urn\:3gpp:dn:        | | *urn\:3gpp:dn: ManagedElement=2*               |
| | *id*.                                  |             |                |              | ManagedElement=1'] |       |                                                  |
|                                          |             |                |              |                            |                                                  |
|                                          |             |                |              | /managed-by-managedElement |                                                  |
|                                          |             |                |              | [@id='urn\:3gpp:dn:        |                                                  |
|                                          |             |                |              | ManagedElement=2']         |                                                  |
+------------------------------------------+-------------+----------------+--------------+----------------------------+--------------------------------------------------+
| | To return the ids for all instances of | RAN         | NRCellDU       |              | /serving-antennaModule/    | | All NRCellDU entities served by AntennaModule  |
| | an entityTypeName related by an        |             |                |              | attributes[withinMeters    | | entities within 500.5 meters from a point with |
| | association to other entities whose    |             |                |              | (@geo-location, 'POINT(    | | latitude and longitude values of -73.958444    |
| | attribute matches the given            |             |                |              | -73.958444 40.800533)',    | | and 40.800533 respectively.                    |
| | *scopeFilter* parameter.               |             |                |              | 500.5)]                    |                                                  |
+------------------------------------------+-------------+----------------+--------------+----------------------------+--------------------------------------------------+

    **/domains/{domainName}/entities**

+------------------------------------------+-------------+----------------+-------------------------------------------+--------------------------------------------------+
| Use case                                 | domainName  | targetFilter   | scopeFilter                               | Query result                                     |
+------------------------------------------+-------------+----------------+-------------------------------------------+--------------------------------------------------+
| | To return the ids of all entities in a | RAN         |                | /managed-by-managedElement                | | All entities that are managed by any           |
| | given domain related by an association |             |                |                                           | | ManagedElement.                                |
+------------------------------------------+-------------+----------------+-------------------------------------------+--------------------------------------------------+
| | To return the ids of all entities in a | RAN         | /ODUFunction   | /managed-by-managedElement                | | All ODUFunction entities that are managed by   |
| | given domain related by an association |             |                | [@id = 'urn :3gpp :dn: ManagedElement=1'] | | the ManagedElement                             |
| | to another entity specified by its     |             |                |                                           | | *urn:3gpp:dn: ManagedElement=1*.               |
| | *id*.                                  |             |                |                                           |                                                  |
+------------------------------------------+-------------+----------------+-------------------------------------------+--------------------------------------------------+
| | To return the attributes for all       | RAN         | /attributes    | /managed-by-managedElement [@id=          | | All entities managed by the                    |
| | entities in a given domain related by  |             |                | 'urn: 3gpp:dn: ManagedElement=1'] |       | | ManagedElement *urn:3gpp:dn:ManagedElement=1*  |
| | one or more associations to other      |             |                |                                           | | or *urn:3gpp:dn: ManagedElement=2*, and        |
| | entities specified by their *id*.      |             |                | /managed-by-managedElement                | | provides NRCellDU *urn:3gpp:dn:*               |
|                                          |             |                | [@id='urn: 3gpp:dn: ManagedElement=2'] ;  | | *ManagedElement=1, NRCellDU=2*.                |
|                                          |             |                |                                           |                                                  |
|                                          |             |                | /provided-nrCellDu [@id='urn: 3gpp        |                                                  |
|                                          |             |                | :dn:ManagedElement=1, NRCellDU=2']        |                                                  |
+------------------------------------------+-------------+----------------+-------------------------------------------+--------------------------------------------------+
| | To return the ids of all entities in a | EQUIPMENT   |                | /grouped-by-sector/attributes[sectorId=1] | | All entities that grouped by a Sector whose    |
| | given domain related by one or more    |             |                |                                           | | sectorId equals 1                              |
| | associations to other entities whose   |             |                |                                           |                                                  |
| | attributes match a specified           |             |                |                                           |                                                  |
| | *scopeFilter* query.                   |             |                |                                           |                                                  |
+------------------------------------------+-------------+----------------+-------------------------------------------+--------------------------------------------------+

Querying entities for relationships
-----------------------------------

   This functionality is supported by the following endpoints:
   **/domains/{domainName}/entity-types/{entityTypeName}/entities/{entityId}/relationships**\ 

The *entityTypeName* is used as the root of the queries.

+------------------------------------------+-------------+----------------+-------------------+-----------------+----------------------------+-----------------------------------------------------+
| Use case                                 | domainName  | entityTypeName | entityId          | targetFilter    | scopeFilter                | Query result                                        |
|                                          |             |                |                   |                 |                            |                                                     |
|                                          |             |                |                   |                 |                            |                                                     |
+==========================================+=============+================+===================+=================+============================+=====================================================+
| | To return the relationships for a      | RAN         | ODUFunction    | urn\:3gpp:dn:     |                 |                            | | All relations for the ODUFunction with id         |
| | given entity specified by its id.      |             |                | ManagedElement=1, |                 |                            | | *urn\:3gpp:dn: ManagedElement=1, ODUFunction=1*   |
|                                          |             |                | ODUFunction=1     |                 |                            |                                                     |
+------------------------------------------+-------------+----------------+-------------------+-----------------+----------------------------+-----------------------------------------------------+
| | To return specific relationships for a | REL_OAM_RAN | ODUFunction    | urn\:3gpp:dn:     | /MANAGEDELEMENT |                            | | All *MANAGEDELEMENT _MANAGES _ODUFUNCTION*        |
| | given entity specified by its id.      |             |                | ManagedElement=1, | _MANAGES        |                            | | relations for the ODUFunction with id             |
|                                          |             |                | ODUFunction=1     | _ODUFUNCTION    |                            | | *urn\:3gpp:dn: ManagedElement=1, ODUFunction=1*   |
+------------------------------------------+-------------+----------------+-------------------+-----------------+----------------------------+-----------------------------------------------------+
| | To return specific relationships for   | REL_OAM_RAN | ODUFunction    | urn\:3gpp:dn:     |                 | /managed-by-managedElement | | All *MANAGEDELEMENT _MANAGES _ODUFUNCTION*        |
| | an entity specified by its id to       |             |                | ManagedElement=1, |                 | [@id = 'urn\:3gpp:dn:      | | relations for the ODUFunction with id             |
| | another entity using its id and        |             |                | ODUFunction=1     |                 | ManagedElement=1']         | | *urn\:3gpp:dn: ManagedElement=1, ODUFunction=1*   |
| | association.                           |             |                |                   |                 |                            | | where the managed element is                      |
|                                          |             |                |                   |                 |                            | | *urn\:3gpp:dn: ManagedElement=1*.                 |
+------------------------------------------+-------------+----------------+-------------------+-----------------+----------------------------+-----------------------------------------------------+

Querying on relationships
-------------------------

   This functionality is supported by the following endpoints:
   **/domains/{domainName}/relationship-types/{relationshipTypeName}/relationships**

Here, the *relationshipTypeName* is used as the root of the queries.

+------------------------------------------+-------------+-----------------+--------+----------------------------+-------------------------------------------------+
| Use case                                 | domainName  | relationship    | target | scopeFilter                | Query result                                    |
|                                          |             | Type Name       |        |                            |                                                 |
|                                          |             |                 | Filter |                            |                                                 |
+==========================================+=============+=================+========+============================+=================================================+
| | To return all relationships for a      | REL_OAM_RAN | MANAGEDELEMENT  |        |                            | | All MANAGEDELEMENT_MANAGES_ORUFUNCTION        |
| | specified relationship                 |             | _MANAGES        |        |                            | | relationships                                 |
|                                          |             | _ORUFUNCTION    |        |                            |                                                 |
+------------------------------------------+-------------+-----------------+--------+----------------------------+-------------------------------------------------+
| | To return all relationships for a      | REL_OAM_RAN | MANAGEDELEMENT  |        | /managed-by-managedElement | | All MANAGEDELEMENT_MANAGES_ORUFUNCTION        |
| | specified relationship type with a     |             | _MANAGES        |        | [@id='urn\:3gpp:dn:        | | relationships having an association           |
| | specified association to an entity.    |             | _ORUFUNCTION    |        | ManagedElement=1']         | | *managed-by-managedElement* to ManagedElement |
|                                          |             |                 |        |                            | | *urn\:3gpp:dn: ManagedElement=1*.             |
+------------------------------------------+-------------+-----------------+--------+----------------------------+-------------------------------------------------+

..

   To get a relationship with a specific id, use:
   **/domains/{domainName}/relationship-types/{relationshipTypeName}/relationships/{relationshipId}**

**Example:** Get the *MANAGEDELEMENT_MANAGES_ORUFUNCTION*
relationship with id *rel1* in the *REL_OAM_RAN* domain:

::

   GET https://<host>/topology-inventory/<API_VERSION>/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ORUFUNCTION/relationships/rel1

Querying on classifiers and decorators
--------------------------------------

This functionality is supported by the following endpoints

::

   **/domains/{domainName}/entities**

+-------------------------------------------+--------+--------+-----------------------+------------------------------------------+
| Use case                                  | domain | target | scopeFilter           | Query result                             |
|                                           |        |        |                       |                                          |
|                                           | Name   | Filter |                       |                                          |
+===========================================+========+========+=======================+==========================================+
| | Return all related entity IDs that are  | RAN    |        | /classifiers[@item =  | | All the entity IDs that are classified |
| | exactly matched with the specified      |        |        | 'odu-function-model   | | with "odu-function-model:Indoor"       |
| | classifier with given domain name.      |        |        | :Indoor']             | | in RAN domain.                         |
+-------------------------------------------+--------+--------+-----------------------+------------------------------------------+
| | Return all related entity IDs that are  | RAN    |        | /classifiers[contains | | All the entity IDs that are partially  |
| | partially matched for the given         |        |        | (@item, 'Ind')]       | | matched with "Ind" in RAN domain.      |
| | classifier with given domain name.      |        |        |                       |                                          |
+-------------------------------------------+--------+--------+-----------------------+------------------------------------------+
| | Return all related entity IDs that are  | RAN    |        | /decorators[          | | All the entity IDs that are exactly    |
| | exactly matched with the key-value pair |        |        | @odu-function-model   | | matched with                           |
| | that specified decorators               |        |        | :textdata =           | | "odu-function-model:textdata =         |
| | with given domain name.                 |        |        | 'Stockholm']          | | 'Stockholm'" in RAN domain.            |
+-------------------------------------------+--------+--------+-----------------------+------------------------------------------+
| | Return all related entity IDs that are  | RAN    |        | /decorators[contains( | | All the entity IDs that are exactly    |
| | exactly matched with key parameter      |        |        | @odu-function-model   | | matched with                           |
| | where the value of the decorator is     |        |        | :textdata, '')]       | | "odu-function-model:textdata as key    |
| | unknown with given domain name.         |        |        |                       | | of the decorator in RAN domain.        |
+-------------------------------------------+--------+--------+-----------------------+------------------------------------------+

**Example:** Get the decorators *odu-function-model:textdata = 'Stockholm' in the RAN domain*

::

   GET https://<host>/topology-inventory/<API_VERSION>/domains/REL_OAM_RAN/entities?scopeFilter=/decorators[@o-ran-smo-teiv-ran:textdata = 'Stockholm']

**Result**

.. code:: json

    {
        "items": [
            {
                "o-ran-smo-teiv-ran:ODUFunction": [
                    {
                        "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,ODUFunction=13"
                    }
                ]
            },
            {
                "o-ran-smo-teiv-ran:ODUFunction": [
                    {
                        "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,ODUFunction=14"
                    }
                ]
            },
            {
                "o-ran-smo-teiv-ran:ODUFunction": [
                    {
                        "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,ODUFunction=16"
                    }
                ]
            }
        ],
        "self": {
            "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/decorators[@o-ran-smo-teiv-ran:textdata = 'Stockholm']"
        },
        "first": {
            "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/decorators[@o-ran-smo-teiv-ran:textdata = 'Stockholm']"
        },
        "prev": {
            "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/decorators[@o-ran-smo-teiv-ran:textdata = 'Stockholm']"
        },
        "next": {
            "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/decorators[@o-ran-smo-teiv-ran:textdata = 'Stockholm']"
        },
        "last": {
            "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/decorators[@o-ran-smo-teiv-ran:textdata = 'Stockholm']"
        },
        "totalCount": 3
    }

::

   **/domains/{domainName}/entity-types/{entityName}/entities**

+-------------------------------------+--------------+--------------+---------------------------+--------------------------------------------------------+---------------------------------------------------+
| Use case                            | entityName   | relationship | targetFilter              | scopeFilter                                            | Query result                                      |
|                                     |              |              |                           |                                                        |                                                   |
|                                     |              | TypeName     |                           |                                                        |                                                   |
+=====================================+==============+==============+===========================+========================================================+===================================================+
| | Return all related entity IDs and | NRCellDU     |              | /classifiers              |                                                        | All NRCellDU IDs and classifiers.                 |
| | classifiers.                      |              |              |                           |                                                        |                                                   |
+-------------------------------------+--------------+--------------+---------------------------+--------------------------------------------------------+---------------------------------------------------+
| | Return all related entity IDs and | NRCellDU     |              | /decorators               |                                                        | All NRCellDU IDs and decorators.                  |
| | decorators.                       |              |              |                           |                                                        |                                                   |
+-------------------------------------+--------------+--------------+---------------------------+--------------------------------------------------------+---------------------------------------------------+
| | Return all related entity IDs     | NRCellDU     |              |                           | | /classifiers[@item = 'odu-function-model:Indoor'];   | | All NRCellDU IDs where key of the decorator is  |
| | that are an exact match for the   |              |              |                           | | /decorators[@odu-function-model:textdata =           | | "odu-function-model:textdata" and the value     |
| | given classifiers and decorators. |              |              |                           | | 'Stockholm']                                         | | of the decorator is 'Stockholm' and classifiers |
|                                     |              |              |                           |                                                        | | exactly contain "odu-function-model:Indoor".    |
+-------------------------------------+--------------+--------------+---------------------------+--------------------------------------------------------+---------------------------------------------------+
| | Return all related entity IDs and | NRCellDU     |              | /classifiers              | /classifiers[contains(@item, 'Ind')]                   | | All NRCellDU IDs and classifiers partially      |
| | classifiers that are partially    |              |              |                           |                                                        | | contain the text "Ind".                         |
| | matched for the given classifier. |              |              |                           |                                                        |                                                   |
+-------------------------------------+--------------+--------------+---------------------------+--------------------------------------------------------+---------------------------------------------------+
| | Return all related entity IDs and | NRCellDU     |              | /decorators               | | /decorators[contains(@odu-function-model:textdata,   | | All NRCellDU IDs and where key of the decorator |
| | decorators where the key is an    |              |              |                           | | 'Stoc')]                                             | | is "odu-function-model:textdata" and the        |
| | exact match and the value is a    |              |              |                           |                                                        | | value of the decorator partially contains       |
| | partial match.                    |              |              |                           |                                                        | | 'Stoc'                                          |
+-------------------------------------+--------------+--------------+---------------------------+--------------------------------------------------------+---------------------------------------------------+
| | Return all related entity IDs and | NRCellDU     |              | /classifiers; /decorators | | /classifiers[contains(@item, 'Ind')];                | | All NRCellDU IDs and decorators where the key   |
| | decorators where the key is an    |              |              |                           | | /decorators[contains(@odu-function-model:textdata,   | | of the decorator is                             |
| | exact match and the value is a    |              |              |                           | | 'Stoc')]                                             | | "odu-function-model:textdata", the value of     |
| | partial match.                    |              |              |                           |                                                        | | the decorator partially contains 'Stoc', and    |
|                                     |              |              |                           |                                                        | | the classifiers partially contain "Ind".        |
+-------------------------------------+--------------+--------------+---------------------------+--------------------------------------------------------+---------------------------------------------------+

**Example:** Get the entities and classifiers where the classifier contains the text *Rural*

::

   GET https://<host>/topology-inventory/<API_VERSION>/domains/RAN/entity-types/NRCellDU/entities?targetFilter=/classifiers&scopeFilter=/classifiers[contains(@item, 'Rural')]

**Result**

.. code:: json

    {
        "items": [
            {
                "o-ran-smo-teiv-ran:NRCellDU": [
                    {
                        "classifiers": [
                            "o-ran-smo-teiv-ran:Rural"
                        ],
                        "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=93"
                    }
                ]
            }
        ],
        "self": {
            "href": "/domains/RAN/entity-types/NRCellDU/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[contains(@item, 'Rural')]"
        },
        "first": {
            "href": "/domains/RAN/entity-types/NRCellDU/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[contains(@item, 'Rural')]"
        },
        "prev": {
            "href": "/domains/RAN/entity-types/NRCellDU/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[contains(@item, 'Rural')]"
        },
        "next": {
            "href": "/domains/RAN/entity-types/NRCellDU/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[contains(@item, 'Rural')]"
        },
        "last": {
            "href": "/domains/RAN/entity-types/NRCellDU/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[contains(@item, 'Rural')]"
        },
        "totalCount": 1
    }

::

   **/domains/{domainName}/relationship-types/{relationshipTypeName}/relationships**

+-------------------------------+--------+-------------------------+--------------------+-----------------------------+
| Use case                      | entity | relationshipTypeName    | targetFilter       | scopeFilter                 |
|                               |        |                         |                    |                             |
|                               | Name   |                         |                    |                             |
+===============================+========+=========================+====================+=============================+
| | Return all related          |        | MANAGEDELEMENT _MANAGES | /classifiers       |                             |
| | relationship IDs and        |        | _ORUFUNCTION            |                    |                             |
| | classifiers.                |        |                         |                    |                             |
+-------------------------------+--------+-------------------------+--------------------+-----------------------------+
| | Return all related          |        | MANAGEDELEMENT _MANAGES | /decorators        |                             |
| | relationship IDs and        |        | _ORUFUNCTION            |                    |                             |
| | decorators.                 |        |                         |                    |                             |
+-------------------------------+--------+-------------------------+--------------------+-----------------------------+
| | Return related relationship |        | MANAGEDELEMENT _MANAGES |                    | /classifiers[@item =        |
| | IDs that match the          |        | _ORUFUNCTION            |                    | 'odu-function-model         |
| | classifier and decorator.   |        |                         |                    | :Indoor'];                  |
|                               |        |                         |                    |                             |
|                               |        |                         |                    | /decorators[@odu-function   |
|                               |        |                         |                    | -model:textdata =           |
|                               |        |                         |                    | 'Stockholm']                |
+-------------------------------+--------+-------------------------+--------------------+-----------------------------+
| | Return related relationship |        | MANAGEDELEMENT _MANAGES | /classifiers       | /classifiers[contains       |
| | IDs and classifiers that    |        | _ORUFUNCTION            |                    | (@item, 'Ind')]             |
| | are partially matched       |        |                         |                    |                             |
| | for the classifier.         |        |                         |                    |                             |
+-------------------------------+--------+-------------------------+--------------------+-----------------------------+
| | Return related relationship |        | MANAGEDELEMENT _MANAGES | /decorators        | /decorators[contains        |
| | IDs and decorators where    |        | _ORUFUNCTION            |                    | (@odu-function-model:       |
| | the key matches exactly and |        |                         |                    | textdata, 'Stock')]         |
| | the value matches           |        |                         |                    |                             |
| | partially.                  |        |                         |                    |                             |
+-------------------------------+--------+-------------------------+--------------------+-----------------------------+
| | Return related relationship |        | MANAGEDELEMENT _MANAGES | | /classifiers     | /classifiers[contains       |
| | IDs, decorators, and        |        | _ORUFUNCTION            | | /decorators      | (@item, 'Ind')];            |
| | classifiers where decorator |        |                         |                    | /decorators[contains        |
| | key is exact and value      |        |                         |                    | (@odu-function-model:       |
| | partially matches, and      |        |                         |                    | textdata, 'Stock')]         |
| | classifiers partially match |        |                         |                    |                             |
| | the parameters.             |        |                         |                    |                             |
+-------------------------------+--------+-------------------------+--------------------+-----------------------------+


**Result**

.. code:: json

    {
        "items": [
            {
                "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION": [
                    {
                        "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10",
                        "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10",
                        "classifiers": [
                            "o-ran-smo-teiv-ran:Rural",
                            "o-ran-smo-teiv-ran:Weekend"
                        ],
                        "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=661A89AD3C2702233CD9E96E97E738C05C35EC5FDF32DC78D149B773726350067315B72448D004C938BCD0263F0C4BCCC8A5F9CDD145B9B740983D1523664328"
                    }
                ]
            }
        ],
        "self": {
            "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[@item = 'o-ran-smo-teiv-ran:Rural']&targetFilter=/classifiers"
        },
        "first": {
            "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[@item = 'o-ran-smo-teiv-ran:Rural']&targetFilter=/classifiers"
        },
        "prev": {
            "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[@item = 'o-ran-smo-teiv-ran:Rural']&targetFilter=/classifiers"
        },
        "next": {
            "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[@item = 'o-ran-smo-teiv-ran:Rural']&targetFilter=/classifiers"
        },
        "last": {
            "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[@item = 'o-ran-smo-teiv-ran:Rural']&targetFilter=/classifiers"
        },
        "totalCount": 1
    }

Querying on Geographical Information
------------------------------------

This functionality is supported by the following endpoints:

**/domains/{domainName}/entity-types/{entityTypeName}/entities**

The *entityTypeName* is used as the root of the queries. Use the "Well-Known Text" (WKT) representation of geometry to
specify geometry objects. See the `WKT documentation <https://libgeos.org/specifications/wkt/>`_ for more information.

For supported geometry objects, see `Querying on geographical information <#capabilities/topology-inventory/developer-guide?chapter=querying-on-geographical-information>`_.

+------------------------------------------+---------------+----------------+-------------------------------------------+--------------------------------------------------+
| Use case                                 | entityName    | targetFilter   | scopeFilter                               | Query result                                     |
+------------------------------------------+---------------+----------------+-------------------------------------------+--------------------------------------------------+
| | Return the ids for all instances of an | AntennaModule |                | /attributes[coveredBy(@geo-location,      | | All AntennaModule entities covered by the      |
| | entityTypeName covered by the given    |               |                | 'POLYGON ((-73.958444 40.800533           | | polygon ((-73.958444 40.800533, -73.981962     |
| | polygon                                |               |                | ,-73.981962 40.768558, -73.973207         | | 40.768558, -73.973207 40.765048, -73.949861    |
|                                          |               |                | 40.765048, -73.949861 40.797024           | | 40.797024, -73.958444 40.800533)).             |
|                                          |               |                | ,-73.958444 40.800533))')]                | |                                                |
+------------------------------------------+---------------+----------------+-------------------------------------------+--------------------------------------------------+
| | Return the attributes for all          | AntennaModule | /attributes    | /attributes[coveredBy(@geo-location,      | | All AntennaModule entities with attributes     |
| | instances of an entityTypeName covered |               |                | 'POLYGON ((40 40, 20 45, 45 30,           | | covered by the polygon                         |
| | by the given polygon.                  |               |                | 40 40))')]                                | | ((40 40, 20 45, 45 30, 40 40)).                |
+------------------------------------------+---------------+----------------+-------------------------------------------+--------------------------------------------------+
| | Return the ids for all instances of an | AntennaModule |                | /attributes[withinMeters(@geo-location,   | | All AntennaModule entities within 500.5 meters |
| | entityTypeName within a specified      |               |                | 'POINT(-73.958444 40.800533)', 500.5)]    | | from a point with latitude and longitude       |
| | distance in meters from a point.       |               |                |                                           | | values of -73.958444 and 40.800533             |
|                                          |               |                |                                           | | respectively.                                  |
+------------------------------------------+---------------+----------------+-------------------------------------------+--------------------------------------------------+

**Example:** Get all 'AntennaModule' entities covered by the polygon with points (48 68), (50 68), (50 69), (48 69), and (48 68):

::

   GET https://<host>/topology-inventory/<API_VERSION>/domains/EQUIPMENT/entity-types/AntennaModule/entities?scopeFilter=/attributes[coveredBy(@geo-location, 'POLYGON((48 68, 50 68, 50 69, 48 69, 48 68))')]

**Result**

.. code-block:: json

   {
       "items": [
           {
               "o-ran-smo-teiv-equipment:AntennaModule": [
                   {
                       "id": "urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7"
                   }
               ]
           }
       ],
       "self": {
           "href": "/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&scopeFilter=/attributes[coveredBy(@geo-location, 'POLYGON((48 68, 50 68, 50 69, 48 69, 48 68))')]"
       },
       "first": {
           "href": "/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&scopeFilter=/attributes[coveredBy(@geo-location, 'POLYGON((48 68, 50 68, 50 69, 48 69, 48 68))')]"
       },
       "prev": {
           "href": "/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&scopeFilter=/attributes[coveredBy(@geo-location, 'POLYGON((48 68, 50 68, 50 69, 48 69, 48 68))')]"
       },
       "next": {
           "href": "/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&scopeFilter=/attributes[coveredBy(@geo-location, 'POLYGON((48 68, 50 68, 50 69, 48 69, 48 68))')]"
       },
       "last": {
           "href": "/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&scopeFilter=/attributes[coveredBy(@geo-location, 'POLYGON((48 68, 50 68, 50 69, 48 69, 48 68))')]"
       },
       "totalCount": 1
   }

**/domains/{domainName}/entities**

+------------------------------------------+----------------+-------------------------------------------+--------------------------------------------------+
| Use case                                 | targetFilter   | scopeFilter                               | Query result                                     |
+------------------------------------------+----------------+-------------------------------------------+--------------------------------------------------+
| | Return the ids for all entities in a   |                | /attributes[coveredBy(@geo-location,      | | All AntennaModule entities covered by the      |
| | given domain that is covered by a      |                | 'POLYGON ((-73.958444 40.800533           | | polygon ((-73.958444 40.800533, -73.981962     |
| | specified polygon.                     |                | ,-73.981962 40.768558, -73.973207         | | 40.768558, -73.973207 40.765048, -73.949861    |
|                                          |                | 40.765048, -73.949861 40.797024           | | 40.797024, -73.958444 40.800533)).             |
|                                          |                | ,-73.958444 40.800533))')]                | |                                                |
+------------------------------------------+----------------+-------------------------------------------+--------------------------------------------------+
| | Return the attributes for all          | /AntennaModule | /attributes[coveredBy(@geo-location,      | | All AntennaModule entities covered by          |
| | AntennaModule entities in the given    | /attributes    | 'POLYGON ((40 40, 20 45, 45 30,           | | the polygon ((20 35, 10 30, 10 10, 30          |
| | domain covered by a specified polygon. |                | 40 40))')]                                | | 5, 45 20, 20 35)).                             |
+------------------------------------------+----------------+-------------------------------------------+--------------------------------------------------+
| | Return the ids for all AntennaModule   | /AntennaModule | /attributes[withinMeters(@geo-location,   | | All AntennaModule entities within 500.5 meters |
| | entities in the given domain within a  |                | 'POINT(-73.958444 40.800533)', 500.5)]    | | from a point with latitude and longitude       |
| | specified distance in meters from a    |                |                                           | | values of -73.958444 and 40.800533             |
| | point.                                 |                |                                           | | respectively.                                  |
+------------------------------------------+----------------+-------------------------------------------+--------------------------------------------------+

**Example:** Get all entities in the 'EQUIPMENT' domain within 500 meters from a point with latitude and longitude values of 49.40199 and 68.94199 respectively:

::

   GET https://<host>/topology-inventory/<API_VERSION>/domains/EQUIPMENT/entities?scopeFilter=/attributes[withinMeters(@geo-location, 'POINT(49.40199 68.94199)', 500)]

**Result**

.. code-block:: json

   {
       "items": [
           {
               "o-ran-smo-teiv-equipment:AntennaModule": [
                   {
                       "id": "urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7"
                   }
               ]
           }
       ],
       "self": {
           "href": "/domains/EQUIPMENT/entities?offset=0&limit=500&scopeFilter=/attributes[withinMeters(@geo-location, 'POINT(49.40199 68.94199)', 500)]"
       },
       "first": {
           "href": "/domains/EQUIPMENT/entities?offset=0&limit=500&scopeFilter=/attributes[withinMeters(@geo-location, 'POINT(49.40199 68.94199)', 500)]"
       },
       "prev": {
           "href": "/domains/EQUIPMENT/entities?offset=0&limit=500&scopeFilter=/attributes[withinMeters(@geo-location, 'POINT(49.40199 68.94199)', 500)]"
       },
       "next": {
           "href": "/domains/EQUIPMENT/entities?offset=0&limit=500&scopeFilter=/attributes[withinMeters(@geo-location, 'POINT(49.40199 68.94199)', 500)]"
       },
       "totalCount": 1
   }
