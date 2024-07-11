.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. SPDX-License-Identifier: CC-BY-4.0
.. Copyright (C) 2024 Nordix Foundation. All rights Reserved
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

   GET https://<host>/topology-inventory/<API_VERSION>/schemas?domain=ran

..

   **Note:** - Partial matches are also supported in the query parameter
   using the ’*’ symbol as a wild card. - If the specified domain does
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
name, use: > /domains/{domainName}/entities

**Example:** Get all entities in the *RAN* domain:

::

   GET https://<host>/topology-inventory/<API_VERSION>/domains/RAN/entities

To get a list of all available entity types in a specified domain name,
use: > /domains/{domainName}/entity-types

**Example:** Get all entity types in the *RAN* domain:

::

   GET https://<host>/topology-inventory/<API_VERSION>/domains/RAN/entity-types

To get a list of all available relationship types in a specified domain
name, use: > /domains/{domainName}/relationship-types

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

   **Parameters:** - **targetFilter:** /NRCellDU - **scopeFilter:**
   /sourceIds[contains(@item,'SubNetwork=Ireland')]

::

   GET https://<host>/topology-inventory/<API_VERSION>/domains/RAN?targetFilter=/NRCellDU&scopeFilter=/sourceIds[contains(@item,'SubNetwork=Ireland')]

..

   **Note:** If the targetFilter is not used here, the result contains
   all entities and relationships that matches the condition in the RAN
   domain.
