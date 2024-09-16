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
| | To return the ids for all instances of | RAN    | GNBDUFunction  |              |                        | | All ids of every     |
| | the entityTypeName used in the query.  |        |                |              |                        | | GNBDUFunction        |
+------------------------------------------+--------+----------------+--------------+------------------------+------------------------+
| | To return all attributes of every      | RAN    | GNBDUFunction  | /attributes  |                        | | All GNBDUFunctions   |
| | instance of the entityTypeName used    |        |                |              |                        | | with every attribute |
| | in the query.                          |        |                |              |                        |                        |
+------------------------------------------+--------+----------------+--------------+------------------------+------------------------+
| | To return every instance of the        | RAN    | GNBDUFunction  | /attributes  |                        | | All gNBIds of every  |
| | entityTypeName used in the query, but  |        |                | (gNBId)      |                        | | GNBDUFunction        |
| | only the attribute that was            |        |                |              |                        |                        |
| | defined in the *targetFilter*          |        |                |              |                        |                        |
| | parameter. Note: The attribute must be |        |                |              |                        |                        |
| | a valid field of the object.           |        |                |              |                        |                        |
+------------------------------------------+--------+----------------+--------------+------------------------+------------------------+
| | To return every instance of the        | RAN    | GNBDUFunction  | /attributes  |                        | | All gNBIds and       |
| | entityTypeName used in the query, but  |        |                |              |                        | | gNBIdLengths of      |
| | only the attributes that were          |        |                | (gNBId,      |                        | | every GNBDUFunction  |
| | defined in the *targetFilter*          |        |                | gNBIdLength) |                        |                        |
| | parameter. Note: The attributes must   |        |                |              |                        |                        |
| | be separated by a comma "," when       |        |                |              |                        |                        |
| | using parenthesis "()".                |        |                |              |                        |                        |
+------------------------------------------+--------+----------------+--------------+------------------------+------------------------+
| | To return the ids for all instances of | RAN    | GNBDUFunction  |              | /sourceIds             | | Unique set of ids    |
| | the entityTypeName used in the query,  |        |                |              | [contains (@item,      | | of GNBDUFunctions,   |
| | that matches the given                 |        |                |              | 'SubNetwork=Ireland')] | | where sourceIds      |
| | property in the *scopeFilter*          |        |                |              |                        | | contains             |
| | parameter.                             |        |                |              |                        | | *SubNetwork=Ireland* |
+------------------------------------------+--------+----------------+--------------+------------------------+------------------------+
| | To return the ids for all instances of | RAN    | GNBDUFunction  |              | /attributes            | | Unique set of ids of |
| | the entityTypeName used in the query,  |        |                |              | [@gNBId                | | GNBDUFunctions,where |
| | that matches the given attributes in   |        |                |              | Length=3 and           | | the gNBIdLength      |
| | the *scopeFilter* parameter. Note: The |        |                |              | @gNBId=111]            | | equals 3 and the     |
| | attributes must be separated by a      |        |                |              |                        | | gNBId equals 111     |
| | *AND* or *OR*".                        |        |                |              |                        |                        |
+------------------------------------------+--------+----------------+--------------+------------------------+------------------------+
| | To return the ids for all instances of | RAN    | GNBDUFunction  |              | /attributes            | | Unique set of ids of |
| | the entityTypeName used in the query,  |        |                |              | [@gNBId Length=3] |    | | GNBDUFunctions,      |
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
| | To return the ids for all instances of | REL_OAM_RAN | GNBDUFunction  |              | /managed-by-managedElement | | All GNBDUFunction entities that are managed    |
| | an entityTypeName related by an        |             |                |              |                            | | by any Managed Element.                        |
| | association.                           |             |                |              |                            |                                                  |
+------------------------------------------+-------------+----------------+--------------+----------------------------+--------------------------------------------------+
| | To return the ids for all instances of | REL_OAM_RAN | GNBDUFunction  |              | /managed-by-managedElement | | All GNBDUFunction entities that are managed    |
| | an entityTypeName related by an        |             |                |              | [@id = 'urn\:3gpp:dn:      | | by the Managed Element                         |
| | association to another entity          |             |                |              | ManagedElement=1']         | | *urn\:3gpp:dn: ManagedElement=1*.              |
| | specified by its *id*.                 |             |                |              |                            |                                                  |
+------------------------------------------+-------------+----------------+--------------+----------------------------+--------------------------------------------------+
| | To return the attributes for all       | REL_OAM_RAN | GNBDUFunction  | /attributes  | /attributes [@enbId=1];    | | All GNBDUFunction entities with enbId as *1*   |
| | instances of an entityTypeName         |             |                |              |                            | | managed by the Managed Element                 |
| | related by one or more associations    |             |                |              | /managed-by-managedElement | | *urn\:3gpp:dn: ManagedElement=1* or            |
| | to other entities specified by their   |             |                |              | [@id='urn\:3gpp:dn:        | | *urn\:3gpp:dn: ManagedElement=2*               |
| | *id*.                                  |             |                |              | ManagedElement=1'] |       |                                                  |
|                                          |             |                |              |                            |                                                  |
|                                          |             |                |              | /managed-by-managedElement |                                                  |
|                                          |             |                |              | [@id='urn\:3gpp:dn:        |                                                  |
|                                          |             |                |              | ManagedElement=2']         |                                                  |
+------------------------------------------+-------------+----------------+--------------+----------------------------+--------------------------------------------------+


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
| | To return the relationships for a      | RAN         | GNBDUFunction  | urn\:3gpp:dn:     |                 |                            | | All relations for the GNBDUFunction with id       |
| | given entity specified by its id.      |             |                | ManagedElement=1, |                 |                            | | *urn\:3gpp:dn: ManagedElement=1, GNBDUFunction=1* |
|                                          |             |                | GNBDUFunction=1   |                 |                            |                                                     |
+------------------------------------------+-------------+----------------+-------------------+-----------------+----------------------------+-----------------------------------------------------+
| | To return specific relationships for a | REL_OAM_RAN | GNBDUFunction  | urn\:3gpp:dn:     | /MANAGEDELEMENT |                            | | All *MANAGEDELEMENT _MANAGES _GNBDUFUNCTION*      |
| | given entity specified by its id.      |             |                | ManagedElement=1, | _MANAGES        |                            | | relations for the GNBDUFunction with id           |
|                                          |             |                | GNBDUFunction=1   | _GNBDUFUNCTION  |                            | | *urn\:3gpp:dn: ManagedElement=1, GNBDUFunction=1* |
+------------------------------------------+-------------+----------------+-------------------+-----------------+----------------------------+-----------------------------------------------------+
| | To return specific relationships for   | REL_OAM_RAN | GNBDUFunction  | urn\:3gpp:dn:     |                 | /managed-by-managedElement | | All *MANAGEDELEMENT _MANAGES _GNBDUFUNCTION*      |
| | an entity specified by its id to       |             |                | ManagedElement=1, |                 | [@id = 'urn\:3gpp:dn:      | | relations for the GNBDUFunction with id           |
| | another entity using its id and        |             |                | GNBDUFunction=1   |                 | ManagedElement=1']         | | *urn\:3gpp:dn: ManagedElement=1, GNBDUFunction=1* |
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
| | To return all relationships for a      | REL_OAM_RAN | MANAGEDELEMENT  |        |                            | | All MANAGEDELEMENT_MANAGES_ENODEBFUNCTION     |
| | specified relationship                 |             | _MANAGES        |        |                            | | relationships                                 |
|                                          |             | _ENODEBFUNCTION |        |                            |                                                 |
+------------------------------------------+-------------+-----------------+--------+----------------------------+-------------------------------------------------+
| | To return all relationships for a      | REL_OAM_RAN | MANAGEDELEMENT  |        | /managed-by-managedElement | | All MANAGEDELEMENT_MANAGES_ENODEBFUNCTION     |
| | specified relationship type with a     |             | _MANAGES        |        | [@id='urn\:3gpp:dn:        | | relationships having an association           |
| | specified association to an entity.    |             | _ENODEBFUNCTION |        | ManagedElement=1']         | | *managed-by-managedElement* to ManagedElement |
|                                          |             |                 |        |                            | | *urn\:3gpp:dn: ManagedElement=1*.             |
+------------------------------------------+-------------+-----------------+--------+----------------------------+-------------------------------------------------+

..

   To get a relationship with a specific id, use:
   **/domains/{domainName}/relationship-types/{relationshipTypeName}/relationships/{relationshipId}**

**Example:** Get the *MANAGEDELEMENT_MANAGES_ENODEBFUNCTION*
relationship with id *rel1* in the *REL_OAM_RAN* domain:

::

   GET https://<host>/topology-inventory/<API_VERSION>/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ENODEBFUNCTION/relationships/rel1

Querying on classifiers and decorators
**************************************

This functionality is supported by the following endpoints

::

   **/domains/{domainName}/entities**

+-------------------------------------------+--------+--------+-----------------------+------------------------------------------+
| Use case                                  | domain | target | scopeFilter           | Query result                             |
|                                           |        |        |                       |                                          |
|                                           | Name   | Filter |                       |                                          |
+===========================================+========+========+=======================+==========================================+
| | Return all related entity IDs that are  | RAN    |        | /classifiers[@item =  | | All the entity IDs that are classified |
| | exactly matched with the specified      |        |        | 'gnbdu-function-model | | with "gnbdu-function-model:Indoor"     |
| | classifier with given domain name.      |        |        | :Indoor']             | | in RAN domain.                         |
+-------------------------------------------+--------+--------+-----------------------+------------------------------------------+
| | Return all related entity IDs that are  | RAN    |        | /classifiers[contains | | All the entity IDs that are partially  |
| | partially matched for the given         |        |        | (@item, 'Ind')]       | | matched with "Ind" in RAN domain.      |
| | classifier with given domain name.      |        |        |                       |                                          |
+-------------------------------------------+--------+--------+-----------------------+------------------------------------------+
| | Return all related entity IDs that are  | RAN    |        | /decorators[          | | All the entity IDs that are exactly    |
| | exactly matched with the key-value pair |        |        | @gnbdu-function-model | | matched with                           |
| | that specified decorators               |        |        | :textdata =           | | "gnbdu-function-model:textdata =       |
| | with given domain name.                 |        |        | 'Stockholm']          | | 'Stockholm'" in RAN domain.            |
+-------------------------------------------+--------+--------+-----------------------+------------------------------------------+
| | Return all related entity IDs that are  | RAN    |        | /decorators[contains( | | All the entity IDs that are exactly    |
| | exactly matched with key parameter      |        |        | @gnbdu-function-model | | matched with                           |
| | where the value of the decorator is     |        |        | :textdata, "")]       | | "gnbdu-function-model:textdata as key  |
| | unknown with given domain name.         |        |        |                       | | of the decorator in RAN domain.        |
+-------------------------------------------+--------+--------+-----------------------+------------------------------------------+

**Example:** Get the decorators *gnbdu-function-model:textdata = 'Stockholm' in the RAN domain*

::

   GET https://<eic-host>/topology-inventory/<API_VERSION>/domains/REL_OAM_RAN/entities?scopeFilter=/decorators[@o-ran-smo-teiv-ran:textdata = 'Stockholm']

**Result**

.. code:: json

    {
        "items": [
            {
                "o-ran-smo-teiv-ran:GNBDUFunction": [
                    {
                        "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,GNBDUFunction=13"
                    }
                ]
            },
            {
                "o-ran-smo-teiv-ran:GNBDUFunction": [
                    {
                        "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,GNBDUFunction=14"
                    }
                ]
            },
            {
                "o-ran-smo-teiv-ran:GNBDUFunction": [
                    {
                        "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,GNBDUFunction=16"
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
| | Return all related entity IDs     | NRCellDU     |              |                           | | /classifiers[@item = 'gnbdu-function-model:Indoor']; | | All NRCellDU IDs where key of the decorator is  |
| | that are an exact match for the   |              |              |                           | | /decorators[@gnbdu-function-model:textdata =         | | "gnbdu-function-model:textdata" and the value   |
| | given classifiers and decorators. |              |              |                           | | 'Stockholm']                                         | | of the decorator is 'Stockholm' and classifiers |
|                                     |              |              |                           |                                                        | | exactly contain "gnbdu-function-model:Indoor".  |
+-------------------------------------+--------------+--------------+---------------------------+--------------------------------------------------------+---------------------------------------------------+
| | Return all related entity IDs and | NRCellDU     |              | /classifiers              | /classifiers[contains(@item, 'Ind')]                   | | All NRCellDU IDs and classifiers partially      |
| | classifiers that are partially    |              |              |                           |                                                        | | contain the text "Ind".                         |
| | matched for the given classifier. |              |              |                           |                                                        |                                                   |
+-------------------------------------+--------------+--------------+---------------------------+--------------------------------------------------------+---------------------------------------------------+
| | Return all related entity IDs and | NRCellDU     |              | /decorators               | | /decorators[contains(@gnbdu-function-model:textdata, | | All NRCellDU IDs and where key of the decorator |
| | decorators where the key is an    |              |              |                           | | 'Stoc')]                                             | | is "gnbdu-function-model:textdata" and the      |
| | exact match and the value is a    |              |              |                           |                                                        | | value of the decorator partially contains       |
| | partial match.                    |              |              |                           |                                                        | | 'Stoc'                                          |
+-------------------------------------+--------------+--------------+---------------------------+--------------------------------------------------------+---------------------------------------------------+
| | Return all related entity IDs and | NRCellDU     |              | /classifiers; /decorators | | /classifiers[contains(@item, 'Ind')];                | | All NRCellDU IDs and decorators where the key   |
| | decorators where the key is an    |              |              |                           | | /decorators[contains(@gnbdu-function-model:textdata, | | of the decorator is                             |
| | exact match and the value is a    |              |              |                           | | 'Stoc')]                                             | | "gnbdu-function-model:textdata", the value of   |
| | partial match.                    |              |              |                           |                                                        | | the decorator partially contains 'Stoc', and    |
|                                     |              |              |                           |                                                        | | the classifiers partially contain "Ind".        |
+-------------------------------------+--------------+--------------+---------------------------+--------------------------------------------------------+---------------------------------------------------+

**Example:** Get the entities and classifiers where the classifier contains the text *Indoor*

::

   GET https://<eic-host>/topology-inventory/<API_VERSION>/domains/RAN/entity-types/NRCellDU/entities?targetFilter=/classifiers&scopeFilter=/classifiers[contains(@item, 'Rural')]

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
                        "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,GNBDUFunction=19,NRCellDU=93"
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
| | relationship IDs and        |        | _ENODEBFUNCTION         |                    |                             |
| | classifiers.                |        |                         |                    |                             |
+-------------------------------+--------+-------------------------+--------------------+-----------------------------+
| | Return all related          |        | MANAGEDELEMENT _MANAGES | /decorators        |                             |
| | relationship IDs and        |        | _ENODEBFUNCTION         |                    |                             |
| | decorators.                 |        |                         |                    |                             |
+-------------------------------+--------+-------------------------+--------------------+-----------------------------+
| | Return related relationship |        | MANAGEDELEMENT _MANAGES |                    | /classifiers[@item =        |
| | IDs that match the          |        | _ENODEBFUNCTION         |                    | 'gnbdu-function-model       |
| | classifier and decorator.   |        |                         |                    | :Indoor'];                  |
|                               |        |                         |                    |                             |
|                               |        |                         |                    | /decorators[@gnbdu-function |
|                               |        |                         |                    | -model:textdata =           |
|                               |        |                         |                    | 'Stockholm']                |
+-------------------------------+--------+-------------------------+--------------------+-----------------------------+
| | Return related relationship |        | MANAGEDELEMENT _MANAGES | /classifiers       | /classifiers[contains       |
| | IDs and classifiers that    |        | _ENODEBFUNCTION         |                    | (@item, 'Ind')]             |
| | are partially matched       |        |                         |                    |                             |
| | for the classifier.         |        |                         |                    |                             |
+-------------------------------+--------+-------------------------+--------------------+-----------------------------+
| | Return related relationship |        | MANAGEDELEMENT _MANAGES | /decorators        | /decorators[contains        |
| | IDs and decorators where    |        | _ENODEBFUNCTION         |                    | (@gnbdu-function-model:     |
| | the key matches exactly and |        |                         |                    | textdata, 'Stock')]         |
| | the value matches           |        |                         |                    |                             |
| | partially.                  |        |                         |                    |                             |
+-------------------------------+--------+-------------------------+--------------------+-----------------------------+
| | Return related relationship |        | MANAGEDELEMENT _MANAGES | | /classifiers     | /classifiers[contains       |
| | IDs, decorators, and        |        | _ENODEBFUNCTION         | | /decorators      | (@item, 'Ind')];            |
| | classifiers where decorator |        |                         |                    | /decorators[contains        |
| | key is exact and value      |        |                         |                    | (@gnbdu-function-model:     |
| | partially matches, and      |        |                         |                    | textdata, 'Stock')]         |
| | classifiers partially match |        |                         |                    |                             |
| | the parameters.             |        |                         |                    |                             |
+-------------------------------+--------+-------------------------+--------------------+-----------------------------+


**Result**

.. code:: json

    {
        "items": [
            {
                "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION": [
                    {
                        "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,GNBDUFunction=10",
                        "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10",
                        "classifiers": [
                            "o-ran-smo-teiv-ran:Rural",
                            "o-ran-smo-teiv-ran:Weekend"
                        ],
                        "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION=661A89AD3C2702233CD9E96E97E738C05C35EC5FDF32DC78D149B773726350067315B72448D004C938BCD0263F0C4BCCC8A5F9CDD145B9B740983D1523664328"
                    }
                ]
            }
        ],
        "self": {
            "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[@item = 'o-ran-smo-teiv-ran:Rural']&targetFilter=/classifiers"
        },
        "first": {
            "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[@item = 'o-ran-smo-teiv-ran:Rural']&targetFilter=/classifiers"
        },
        "prev": {
            "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[@item = 'o-ran-smo-teiv-ran:Rural']&targetFilter=/classifiers"
        },
        "next": {
            "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[@item = 'o-ran-smo-teiv-ran:Rural']&targetFilter=/classifiers"
        },
        "last": {
            "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[@item = 'o-ran-smo-teiv-ran:Rural']&targetFilter=/classifiers"
        },
        "totalCount": 1
    }