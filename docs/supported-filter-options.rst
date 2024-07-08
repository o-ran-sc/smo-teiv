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
| | the entityTypeName used in the query,  |        |                |              | [@gNBId Length=3]      | | GNBDUFunctions,      |
| | that satisfies one of                  |        |                |              | &#124;                 | | where the            |
| | the conditions in the *scopeFilter*    |        |                |              |                        | | gNBIdLength equals 3 |
| | parameter. A condition is a complete   |        |                |              | /sourceIds             | | or the sourceIds     |
| | unit of *scopeFilter*                  |        |                |              | [contains (@item,      | | contains an item     |
| | represent OR.                          |        |                |              | 'SubNetwork=Ireland')] | | with                 |
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
| | To return the ids for all instances of | OAM_TO_RAN  | ENodeBFunction |              | /managed-by-managedElement | | All ENodeBFunction entities that are managed   |
| | an entityTypeName related by an        |             |                |              |                            | | by any Managed Element.                        |
| | association.                           |             |                |              |                            |                                                  |
+------------------------------------------+-------------+----------------+--------------+----------------------------+--------------------------------------------------+
| | To return the ids for all instances of | OAM_TO_RAN  | ENodeBFunction |              | /managed-by-managedElement | | All ENodeBFunction entities that are managed   |
| | an entityTypeName related by an        |             |                |              | [@id = 'urn\:3gpp:dn:      | | by the Managed Element                         |
| | association to another entity          |             |                |              | ManagedElement=1']         | | *urn\:3gpp:dn: ManagedElement=1*.              |
| | specified by its *id*.                 |             |                |              |                            |                                                  |
+------------------------------------------+-------------+----------------+--------------+----------------------------+--------------------------------------------------+
| | To return the attributes for all       | OAM_TO_RAN  | ENodeBFunction | /attributes  | /attributes [@enbId=1];    | | All EnodeBFunction entities with enbId as *1*  |
| | instances of an entityTypeName         |             |                |              |                            | | managed by the Managed Element                 |
| | related by one or more associations    |             |                |              | /managed-by-managedElement | | *urn\:3gpp:dn: ManagedElement=1* or            |
| | to other entities specified by their   |             |                |              | [@id='urn\:3gpp:dn:        | | *urn\:3gpp:dn: ManagedElement=2*,              |
| | *id*.                                  |             |                |              | ManagedElement=1'] &#124;  | | and provides EuTranCell                        |
|                                          |             |                |              |                            | | *urn\:3gpp:dn: ManagedElement=1, EUtranCell=2* |
|                                          |             |                |              | /managed-by-managedElement |                                                  |
|                                          |             |                |              | [@id='urn\:3gpp:dn:        |                                                  |
|                                          |             |                |              | ManagedElement=2'] ;       |                                                  |
|                                          |             |                |              |                            |                                                  |
|                                          |             |                |              | /provided-euTranCell       |                                                  |
|                                          |             |                |              | [@id='urn\:3gpp:dn:        |                                                  |
|                                          |             |                |              | ManagedElement=1,          |                                                  |
|                                          |             |                |              | EUtranCell=2']             |                                                  |
+------------------------------------------+-------------+----------------+--------------+----------------------------+--------------------------------------------------+


Querying entities for relationships
-----------------------------------

   This functionality is supported by the following endpoints:
   **/domains/{domainName}/entity-types/{entityTypeName}/entities/{entityId}/relationships**\ 

The *entityTypeName* is used as the root of the queries.

+------------------------------------------+--------+----------------+-------------------+--------+----------------------------+-----------------------------------------------------+
| Use case                                 | domain | entityTypeName | entityId          | target | scopeFilter                | Query result                                        |
|                                          |        |                |                   |        |                            |                                                     |
|                                          | Name   |                |                   | Filter |                            |                                                     |
+==========================================+========+================+===================+========+============================+=====================================================+
| | To return the relationships for a      | RAN    | GNBDUFunction  | urn\:3gpp:dn:     |        |                            | | All relations for the GNBDUFunction with id       |
| | given entity specified by its id.      |        |                | ManagedElement=1, |        |                            | | *urn\:3gpp:dn: ManagedElement=1, GNBDUFunction=1* |
|                                          |        |                | GNBDUFunction=1   |        |                            |                                                     |
+------------------------------------------+--------+----------------+-------------------+--------+----------------------------+-----------------------------------------------------+
| | To return specific relationships for a | RAN    | GNBDUFunction  | urn\:3gpp:dn:     |        | /MANAGEDELEMENT            | | All *MANAGEDELEMENT _MANAGES _GNBDUFUNCTION*      |
| | given entity specified by its id.      |        |                | ManagedElement=1, |        | _MANAGES                   | | relations for the GNBDUFunction with id           |
|                                          |        |                | GNBDUFunction=1   |        | _GNBDUFUNCTION             | | *urn\:3gpp:dn: ManagedElement=1, GNBDUFunction=1* |
+------------------------------------------+--------+----------------+-------------------+--------+----------------------------+-----------------------------------------------------+
| | To return specific relationships for   | RAN    | GNBDUFunction  | urn\:3gpp:dn:     |        | /managed-by-managedElement | | All *MANAGEDELEMENT _MANAGES _GNBDUFUNCTION*      |
| | an entity specified by its id to       |        |                | ManagedElement=1, |        | [@id = 'urn\:3gpp:dn:      | | relations for the GNBDUFunction with id           |
| | another entity using its id and        |        |                | GNBDUFunction=1   |        | ManagedElement=1']         | | *urn\:3gpp:dn: ManagedElement=1, GNBDUFunction=1* |
| | association.                           |        |                |                   |        |                            | | where the managed element is                      |
|                                          |        |                |                   |        |                            | | *urn\:3gpp:dn: ManagedElement=1*.                 |
+------------------------------------------+--------+----------------+-------------------+--------+----------------------------+-----------------------------------------------------+

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
| | To return all relationships for a      | OAM_TO_RAN  | MANAGEDELEMENT  |        |                            | | All MANAGEDELEMENT_MANAGES_ENODEBFUNCTION     |
| | specified relationship                 |             | _MANAGES        |        |                            | | relationships                                 |
|                                          |             | _ENODEBFUNCTION |        |                            |                                                 |
+------------------------------------------+-------------+-----------------+--------+----------------------------+-------------------------------------------------+
| | To return all relationships for a      | OAM_TO_RAN  | MANAGEDELEMENT  |        | /managed-by-managedElement | | All MANAGEDELEMENT_MANAGES_ENODEBFUNCTION     |
| | specified relationship type with a     |             | _MANAGES        |        | [@id='urn\:3gpp:dn:        | | relationships having an association           |
| | specified association to an entity.    |             | _ENODEBFUNCTION |        | ManagedElement=1']         | | *managed-by-managedElement* to ManagedElement |
|                                          |             |                 |        |                            | | *urn\:3gpp:dn: ManagedElement=1*.             |
+------------------------------------------+-------------+-----------------+--------+----------------------------+-------------------------------------------------+

..

   To get a relationship with a specific id, use:
   **/domains/{domainName}/relationship-types/{relationshipTypeName}/relationships/{relationshipId}**

**Example:** Get the *MANAGEDELEMENT_MANAGES_ENODEBFUNCTION*
relationship with id *rel1* in the *OAM_TO_RAN* domain:

::

   GET https://<host>/topology-inventory/<API_VERSION>/domains/OAM_TO_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ENODEBFUNCTION/relationships/rel1
