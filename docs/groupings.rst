.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. SPDX-License-Identifier: CC-BY-4.0
.. Copyright (C) 2024 Nordix Foundation. All rights Reserved
.. Copyright (C) 2024 OpenInfra Foundation Europe. All Rights Reserved

Topology grouping
#################

Topology groups provide the capability to create user-defined
collections of topology entities and/or relationships of any
type. Groups can be either *static* or *dynamic* based on how
they are created.

    **Static group**: This is a collection of topology identifiers 
    specified by the user.

    **Dynamic group**: This holds a single resource query specified
    by the user, which, when resolved, fetches the desired collection
    of topology entities or relationships.

Creating topology groups
------------------------

The following describes how to create topology groups for entities and relationships
in both static and dynamic ways.

After a successful creation, a group ID is generated
and returned in the response. This ID should be stored as it is used when resolving
a topology group (described under
:ref:`Resolving group members <Resolving group members>`


    **NOTE:** There is no validation done during the creation of topology groups,
    this is done during the group resolution.

Static groups
=============
To create a static group, use:
    POST /groups

See the following table for a description of the payload required
when creating static groups.

**Static group payload**

+-----------------+---------------------------------------------+--------------+
| **Parameter**   | **Description**                             | **Required** |
+=================+=============================================+==============+
| name            | | The desired name of the group (this is    | True         |
|                 | | not unique).                              |              |
+-----------------+---------------------------------------------+--------------+
| type            | The group type (`static` in this case).     | True         |
+-----------------+---------------------------------------------+--------------+
| providedMembers | | A list containing the identities of the   | True         |
|                 | | desired members (entities or              |              |
|                 | | relationships) in the model format.       |              |
+-----------------+---------------------------------------------+--------------+

**Example:** Create a static group with two provided members:

::

   POST 'https://<host>/topology-inventory/<API_VERSION>/groups'
   Content-Type: application/json
   Accept: application/json

.. code-block:: json

    {
      "name": "cell-filter-group-1",
      "type": "static",
      "providedMembers": [
        {
          "o-ran-smo-teiv-ran:NRCellDU": [
            {
              "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1"
            }
          ]
        },
        {
          "o-ran-smo-teiv-ran:ODUFunction": [
            {
              "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1"
            }
          ]
        }
      ]
    }

**NOTE:**
  - Static groups can include a maximum of 10,000 provided members,
    with a limit of 2,000 members during creation and 2,000 in each
    subsequent merge operation. 
  - The performance of static groups is related to the number of members.
    If a group with a large number of members is required, it is recommended
    to use dynamic groups in combination with classifiers for best performance.
  - It is not possible to create a group of groups.

Dynamic groups
==============

To create a dynamic group, use:
    POST /groups

See the following table for a description of the payload required
when creating dynamic groups.

**Dynamic group payload**

+-------------------------------+------------------------------------------------+--------------------------------------------------+
| **Parameter**                 | **Description**                                | **Required**                                     |
+===============================+================================================+==================================================+
| name                          | | The desired name of the group (this is not   | True                                             |
|                               | | unique).                                     |                                                  |
+-------------------------------+------------------------------------------------+--------------------------------------------------+
| type                          | The group type (`dynamic` in this case).       | True                                             |
+-------------------------------+------------------------------------------------+--------------------------------------------------+
| criteria                      | | The set of parameters needed to specify the  | True                                             |
|                               | | members that are returned when the dynamic   |                                                  |
|                               | | group is resolved.                           |                                                  |
+-------------------------------+------------------------------------------------+--------------------------------------------------+
| criteria.queryType            | | This is the type of query that is performed  | True                                             |
|                               | | when resolving a topology group. Use one of  |                                                  |
|                               | | the following query types when creating a    |                                                  |
|                               | | dynamic group:                               |                                                  |
|                               | | <ul><li>getEntitiesByDomain</li>             |                                                  |
|                               | | <li>getEntitiesByType</li>                   |                                                  |
|                               | | <li>getRelationshipsForEntityId</li>         |                                                  |
|                               | | <li>getRelationshipsByType</li></ul>         |                                                  |
+-------------------------------+------------------------------------------------+--------------------------------------------------+
| criteria.domain               | | This is the topology domain. Use TEIV if not | True                                             |
|                               | | known.                                       |                                                  |
+-------------------------------+------------------------------------------------+--------------------------------------------------+
| criteria.entityTypeName       | The entity type, for example, `OCUCPFunction`  | | Required only when *criteria.queryType* is     |
|                               |                                                | | `getEntitiesByType` or                         |
|                               |                                                | | `getRelationshipsForEntityId`                  |
+-------------------------------+------------------------------------------------+--------------------------------------------------+
| criteria.entityId             | | The entity identifier, for example, urn:     | | Required only when *criteria.queryType* is     |
|                               | | 3gpp: dn :ManagedElement=1,ODUFunction=1,    | | `getRelationshipsForEntityId`                  |
|                               | | NRCellDU=1.                                  |                                                  |
+-------------------------------+------------------------------------------------+--------------------------------------------------+
| criteria.relationshipTypeName | | The relationship type, for example,          | | Required only when *criteria.queryType* is     |
|                               | | `ODUFUNCTION_PROVIDES_NRCELLDU`.             | | `getRelationshipsByType`                       |
+-------------------------------+------------------------------------------------+--------------------------------------------------+
| criteria.targetFilter         | | Use the targetFilter parameter to narrow     | False                                            |
|                               | | down the fields to return. This is similar   |                                                  |
|                               | | to the SELECT keyword in an SQL statement.   |                                                  |
+-------------------------------+------------------------------------------------+--------------------------------------------------+
| criteria.scopeFilter          | | Use the scopeFilter parameter to filter the  | False                                            |
|                               | | results using specific criteria. This is     |                                                  |
|                               | | similar to the WHERE keyword in an SQL       |                                                  |
|                               | | statement.                                   |                                                  |
+-------------------------------+------------------------------------------------+--------------------------------------------------+


**Example:** Create a dynamic group of *NRCellDU* entities that have a *cellLocalId*
equal to 1:

::

    POST 'https://<host>/topology-inventory/<API_VERSION>/groups'
    Content-Type: application/json
    Accept: application/json

.. code-block:: json

   {
     "name": "cell-filter-group-2",
     "type": "dynamic",
     "criteria": {
       "queryType": "getEntitiesByDomain",
       "domain": "RAN",
       "targetFilter": "/NRCellDU/attributes(nCI)",
       "scopeFilter": "/NRCellDU/attributes[@cellLocalId=1]"
     }
   }

**NOTE:** For dynamic groups, there is no limit to the amount of members
returned when resolving the group.

Querying topology groups
------------------------

Fetching groups
===============

To fetch a list of all groups (static or dynamic), use:
    GET /groups

**Example:** Get all groups:

::

    GET 'https://<host>/topology-inventory/<API_VERSION>/groups'
    Accept: application/json


The user can filter the result by specifying a group name as a query parameter.
This returns a list of all groups that exactly match the provided name string.

    **NOTE:** The topology group 'name' parameter is not unique.

**Example:** Get all groups with names that match *cell-filter-group*:

::

    GET 'https://<host>/topology-inventory/<API_VERSION>/groups?name=cell-filter-group'
    Accept: application/json

To get a specific group by its *groupId*, use:
    GET /groups/{groupId}

**Example:** Fetch a group with groupId *urn:o-ran:smo:teiv:group=123e4567-e89b-12d3-a456-426614174000*:

::

    GET 'https://<host>/topology-inventory/<API_VERSION>/groups/urn:o-ran:smo:teiv:group=123e4567-e89b-12d3-a456-426614174000'
    Accept: application/json

Resolving group members
=======================
To get the members of a group using its *groupId*, use:
    GET /groups/{groupId}/members

**Example:** Get the members of a group with groupId
*urn:o-ran:smo:teiv:group=123e4567-e89b-12d3-a456-426614174000*:

::

    GET 'https://<host>/topology-inventory/<API_VERSION>/groups/urn:o-ran:smo:teiv:group=123e4567-e89b-12d3-a456-426614174000/members'
    Accept: application/json


**NOTE:** This query returns only the IDs of the topology entities or relationships 
that are present in your inventory. The members provided by the user (in the case of
static groups) that are invalid or not present are discarded in the response.

To get the provided members of a **static** group using its *groupId*, use:
    GET /groups/{groupId}/provided-members

This fetches all members provided by the user including members that are invalid or not present
in your inventory.

**Example:** Get the provided members of a static group with groupId
*urn:o-ran:smo:teiv:group=123e4567-e89b-12d3-a456-426614174000*:

::

    GET 'https://<host>/topology-inventory/<API_VERSION>/groups/urn:o-ran:smo:teiv:group=123e4567-e89b-12d3-a456-426614174000/provided-members'
    Accept: application/json

The provided members in a static group can be filtered using the *status* query parameter.

**Example:** Get the provided members of a static group with groupId
*urn:o-ran:smo:teiv:group=123e4567-e89b-12d3-a456-426614174000* that are *not-present*:

::

    GET 'https://<host>/topology-inventory/<API_VERSION>/groups/urn:o-ran:smo:teiv:group=123e4567-e89b-12d3-a456-426614174000/provided-members?status=not-present'
    Accept: application/json

**NOTE:** The accepted values for 'status' are *present*, *not-present*, and *invalid*.
See the following 'Topology status and descriptions' table for more information.

**Topology status and descriptions**

+-------------+-----------------------------------------------------------------------------------+
| **Status**  | **Description**                                                                   |
+=============+===================================================================================+
| present     | Entity or relationship IDs that currently exist in your topology.                 |
+-------------+-----------------------------------------------------------------------------------+
| not-present | Entity or relationship IDs that do not exist in your topology.                    |
+-------------+-----------------------------------------------------------------------------------+
| invalid     | Entity or relationship IDs of a topology type that does not match the TEIV model. |
+-------------+-----------------------------------------------------------------------------------+


Modifying topology groups
-------------------------

Update a group name
===================

To update the name of a topology group specified by its *groupId*, use:
    PUT /groups/{groupId}/name

**Example:** Update the name of a group with groupId
*urn:o-ran:smo:teiv:group=123e4567-e89b-12d3-a456-426614174000*:

::

    PUT 'https://<host>/topology-inventory/<API_VERSION>/groups/urn:o-ran:smo:teiv:group=123e4567-e89b-12d3-a456-426614174000/name'
    Content-Type: application/json

.. code-block:: json

    {
        "name": "cell-filter-group-5"
    }

Update the members in a group
=============================

To merge or remove members in an existing topology group, use:
    POST /groups/{groupId}/provided-members-operations

**NOTE:** This operation is applicable for static groups only.

**Example:** Merge members of a group with groupId
*urn:o-ran:smo:teiv:group=123e4567-e89b-12d3-a456-426614174000*:

::

    POST 'https://<host>/topology-inventory/<API_VERSION>/groups/urn:o-ran:smo:teiv:group=123e4567-e89b-12d3-a456-426614174000/provided-members-operations'
    Content-Type: application/json
    Accept: application/json

.. code-block:: json

    {
      "operation": "merge",
      "providedMembers": [
        {
          "o-ran-smo-teiv-ran:NRCellDU": [
            {
              "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1"
            }
          ]
        }
      ]
    }

**Example:** Remove members from a group with groupId
*urn:o-ran:smo:teiv:group=123e4567-e89b-12d3-a456-426614174000*:

::

    POST 'https://<host>/topology-inventory/<API_VERSION>/groups/urn:o-ran:smo:teiv:group=123e4567-e89b-12d3-a456-426614174000/provided-members-operations'
    Content-Type: application/json
    Accept: application/json

.. code-block:: json

    {
      "operation": "remove",
      "providedMembers": [
        {
          "o-ran-smo-teiv-ran:NRCellDU": [
            {
              "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1"
            }
          ]
        }
      ]
    }