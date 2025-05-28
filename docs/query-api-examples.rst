.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. SPDX-License-Identifier: CC-BY-4.0
.. Copyright (C) 2024-2025 Nordix Foundation. All rights Reserved
.. Copyright (C) 2024 OpenInfra Foundation Europe. All Rights Reserved

Query API examples
##################

Retrieving and using topology modules
=====================================

Topology & Inventory provides APIs to enable users :doc:`query module data
<api-documentation>` that can be used to understand the existing
topology and inventory model,parse the modules, and understand what objects
are supported over the R1 interface, so adequate queries can be made on
topology and inventory data.

The API endpoints returning lists support pagination. The default value
for number of items returned is 500, which is also the upper limit.

**Sample request to fetch a list of all modules:**

::

   GET https://<host>/topology-inventory/<API_VERSION>/schemas

To get a list of all modules for a specific domain, use a *domain* query
parameter. For example, /schemas?domain=<domain>

**Sample request to fetch a list of all modules related to the RAN
domain:**

::

   GET https://<host>/topology-inventory/<API_VERSION>/schemas?domain=RAN

..

   **Note:**
    - The specified domain is case-sensitive.
    - If the specified domain does
      not exist, an empty list is returned.

To get a specific module, supply a module name in the path parameter.
For example, /schemas/<name>/content

**Sample request to fetch the module data for the o-ran-smo-teiv-ran
module:**

::

   GET https://<host>/topology-inventory/<API_VERSION>/schemas/o-ran-smo-teiv-ran/content

..

   **Note:** If the specified module does not exist, an
   *INVALID_MODULE_NAME* error is returned.

Reading and querying topology and inventory
===========================================

Reading entities and relationships
----------------------------------

To get a list of all entities with all properties in a specified domain
name, use:

``/domains/{domainName}/entities``

**Example:** Get all entities in the *RAN* domain:

::

   GET https://<host>/topology-inventory/<API_VERSION>/domains/RAN/entities

To get a list of all available entity types in a specified domain name,
use:

``/domains/{domainName}/entity-types``

**Example:** Get all entity types in the *RAN* domain:

::

   GET https://<host>/topology-inventory/<API_VERSION>/domains/RAN/entity-types

To get a list of all available relationship types in a specified domain
name, use:

``/domains/{domainName}/relationship-types``

**Example:** Get all relationship types in the *RAN* domain:

::

   GET https://<host>/topology-inventory/<API_VERSION>/domains/RAN/relationship-types

Querying entities and relationships
-----------------------------------

Use the *targetFilter* parameter to narrow down the fields to return. To
filter the results which match a given criteria, use the *scopeFilter*.
Think of it as an SQL statement, where the *targetFilter* is the SELECT,
and the *scopeFilter* is the WHERE tag.

A detailed explanation about the *targetFilter* and *scopeFilter*
parameters can be found in :doc:`Supported filter options
<supported-filter-options>`.

**Example:**

In this example, the user is only interested in NRCellDU entities.
Moreover, the user only wants those records that have sourceIds
containing “SubNetwork=Ireland”. These fields and filters can be defined
in the request as follows:

   **Parameters:**

   - **targetFilter**: ``/NRCellDU``
   - **scopeFilter**: ``/sourceIds[contains(@item,'SubNetwork=Europe')]``


::

   GET https://<host>/topology-inventory/<API_VERSION>/domains/RAN/entities?
   targetFilter=/NRCellDU&
   scopeFilter=/sourceIds[contains(@item,'SubNetwork=Ireland')]

..

   **Note:** If the targetFilter is not used here, the result contains
   all entities and relationships that matches the condition in the RAN
   domain.

Querying on geographical information examples
---------------------------------------------

Topology & Inventory supports querying entities based on geographical information
by updating the filters. Use the "Well-Known Text" (WKT) representation of geometry
to specify geometry objects. The WKT representation of coordinate systems is a text
markup language standard for representing vector geometry objects. For more information,
see the `WKT documentation <https://libgeos.org/specifications/wkt/>`_

    **NOTE:** This filter option is restricted to entities that have been enriched with geographical data.

Topology & Inventory supports the following geometric objects:

**POINT:** This can be used with the *withinMeters* function. It requires the latitude
and longitude of the point and then a range in meters. It returns the desired objects within the provided
distance in meters from the given point.

**Example:** Get all entities with geographical information in the 'EQUIPMENT' domain within 500 meters
from a point with latitude and longitude values of 49.40199 and 68.94199 respectively:

::

    GET https://<host>/topology-inventory/<API_VERSION>/domains/EQUIPMENT/entities?
    scopeFilter=/attributes[withinMeters(@geo-location, 'POINT(49.40199 68.94199)', 500)]

..

**POLYGON:** This can be used with the *coveredBy* function. It requires the latitude and longitude of the points of the polygon. It returns the desired objects covered by the given polygon.

**Example:** Get all 'AntennaModule' entities covered by the polygon with points (48 68) , (50 68), (50 69), (48 69), and (48 68):

::

    GET https://<host>/topology-inventory/<API_VERSION>/domains/EQUIPMENT/entity-types/AntennaModule/entities?
    scopeFilter=/attributes[coveredBy(@geo-location, 'POLYGON((48 68, 50 68, 50 69, 48 69, 48 68))')]

..

    **NOTE:** To draw a valid polygon, the first and last points must be identical.

