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

Static groups
=============
To create a static group, use:
    POST /groups

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

**NOTE:** Static groups can include a maximum of 25,000 provided members,
with a limit of 5,000 in each creation request.

Dynamic groups
==============

To create a dynamic group, use:
    POST /groups

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

To get a specific group by its *topologyGroupId*, use:
    GET /groups/{topologyGroupId}

**Example:** Fetch a group with topologyGroupId *urn:o-ran:smo:teiv:group=123e4567-e89b-12d3-a456-426614174000*:

::

    GET 'https://<host>/topology-inventory/<API_VERSION>/groups/urn:o-ran:smo:teiv:group=123e4567-e89b-12d3-a456-426614174000'
    Accept: application/json

Resolving group members
=======================
To get the members of a group using its *topologyGroupId*, use:
    GET /groups/{topologyGroupId}/members

**Example:** Get the members of a group with topologyGroupId
*urn:o-ran:smo:teiv:group=123e4567-e89b-12d3-a456-426614174000*:

::

    GET 'https://<host>/topology-inventory/<API_VERSION>/groups/urn:o-ran:smo:teiv:group=123e4567-e89b-12d3-a456-426614174000/members'
    Accept: application/json


**NOTE:** This query returns only the IDs of the topology entities or relationships 
that are present in your inventory. The members provided by the user (in the case of
static groups) that are invalid or not present are discarded in the response.

To get the provided members of a **static** group using its *topologyGroupId*, use:
    GET /groups/{topologyGroupId}/provided-members

This fetches all members provided by the user including members that are invalid or not present
in your inventory.

**Example:** Get the provided members of a static group with topologyGroupId
*urn:o-ran:smo:teiv:group=123e4567-e89b-12d3-a456-426614174000*:

::

    GET 'https://<host>/topology-inventory/<API_VERSION>/groups/urn:o-ran:smo:teiv:group=123e4567-e89b-12d3-a456-426614174000/provided-members'
    Accept: application/json

The provided members in a static group can be filtered using the *status* query parameter.

**Example:** Get the provided members of a static group with topologyGroupId
*urn:o-ran:smo:teiv:group=123e4567-e89b-12d3-a456-426614174000* that are *not-present*:

::

    GET 'https://<host>/topology-inventory/<API_VERSION>/groups/urn:o-ran:smo:teiv:group=123e4567-e89b-12d3-a456-426614174000/provided-members?status=not-present'
    Accept: application/json

**NOTE:** The accepted values for 'status' are *present*, *not-present*, and *invalid*.

Modifying topology groups
-------------------------

Update a group name
===================

To update the name of a topology group specified by its *topologyGroupId*, use:
    PUT /groups/{topologyGroupId}/name

**Example:** Update the name of a group with topologyGroupId
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
    POST /groups/{topologyGroupId}/provided-members-operations

**NOTE:** This operation is applicable for static groups only.

**Example:** Merge members of a group with topologyGroupId
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

**Example:** Remove members from a group with topologyGroupId
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