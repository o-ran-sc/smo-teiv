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

Developer Guide
###############

Developer Guide Overview
========================

In this guide, we explore the use of Topology & Inventory to manage the
topology and inventory data in your network.

Introducing topology and inventory data
=======================================

Topology and inventory data is the information that represents entities
in a telecommunications network and the relationships between them that
provide insight into a particular aspect of the network of importance to
specific use cases. Topology and inventory data can be derived from
inventory, configuration, or other data. Topology & Inventory is being
updated autonomously based on changes in the network.

Topology & Inventory supports several topology and inventory domains,
see the :doc:`Data Models </data-models-guide>` for
details on the topology and inventory model. The understanding of the
model is important to enable a user making queries on topology and
inventory data. The entities are modeled as managed objects (found under
the schema in the data dictionary) and grouped together in modules based
on functionality. See
:ref:`Supported domains <Supported domains>`
for the list of the topology and inventory domains currently supported
in Topology & Inventory capability.

Concepts
--------

The building blocks of the Topology & Inventory are domains, entities,
and the relationships between each other. From a graph perspective,
entities are the vertices and relationships are the edges. These two
components are part of a subgraph, or the so-called domain. A
relationship can go beyond a single domain, since it can happen that the
two entities come from two separate ones. In this particular case, they
have a cross-domain relationship.

Domain
~~~~~~

A domain is a grouping of topology and inventory entities that handles
topology and inventory data. Topology and inventory data is the
information that represents entities in a telecommunications network and
the relationships between them that provides insight into a particular
aspect of the network of import to specific use cases. Topology and
inventory data can be derived from inventory, configuration, or other
data. Therefore, the topology and inventory model must define what the
telecoms network entities and relationships are. More information can be
found in :ref:`Supported domains <Supported domains>`.
The Topology Exposure and Inventory Management (TEIV) domain is the
parent domain used for entities and relationships. This domain can be
used in reading and querying topology and inventory data when the domain
name of an entity or relationship is not known.

Entity
~~~~~~

Entities are enabling the modelling and storage of complex network
infrastructure and relationships. The following are two examples of the
entities and their attributes from :doc:`Topology & Inventory Data
Models <data-models-guide>`.

.. image:: _static/sample-entities.svg
  :width: 900

Relationship
~~~~~~~~~~~~

It is a bi-directional connection between two entities, one of which is
the originating side (A-side) and the other is the terminating side
(B-side). The order of the sides matters since it defines the
relationship itself which must be unique. A relationship between two
entities is based on the effect that one has on the other. An entity can
have one or multiple relationships which can be defined by the user. A
possible relationship between ManagedElement and GNBDUFunction can be
*MANAGEDELEMENT_MANAGES_GNBDUFUNCTION*.

Topology & Inventory models
---------------------------

The Topology & Inventory objects are managed and standardized using YANG
models. These YANG models describe managed network entities and their
attributes, while also providing information on the relations between
the network entities. YANG data models are structured into modules and
submodules. Management instance data is a graph of objects which have
attributes (see the **schema** in the data models).

The :doc:`Topology & Inventory Data Models <data-models-guide>` includes:
- Modules for each supported domain that describe the structure of the
managed objects within it as well as any relationships between them. -
Modules that describe cross-domain relationships. - Modules that define
proprietary extensions and types used to describe the structure of
objects and attributes within the domains.

The following sample diagram shows some managed objects and their
relationships in the RAN domain.

.. image:: _static/sample-object-relationships.svg
  :width: 900

A direct relationship is a connection between two entities without any
in-between entity and an indirect relationship contains at least one.
NRCellDU has direct relationships with GNBDUFunction and
NRSectorCarrier, while it also has indirect relationships with
ManagedElement, AntennaCapability, and AntennaModule.

Supported domains
-----------------

+-------------------+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Domain            | Description                                                                                                                                                                                                                                                                 |
+===================+=============================================================================================================================================================================================================================================================================+
| RAN               | This model contains the topology entities and relations in the RAN domain, which represents the functional capability of the deployed RAN that are relevant to rApps use cases.                                                                                             |
+-------------------+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| EQUIPMENT         | This model contains the topology entities and relations in the Equipment domain, which is modelled to understand the physical location of equipment such as antennas associated with a cell/carrier and their relevant properties, for example, tilt, max power, and so on. |
+-------------------+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| OAM               | This model contains the topology entities and relations in the O&M domain, which are intended to represent management systems and management interfaces.                                                                                                                    |
+-------------------+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| REL_EQUIPMENT_RAN | This model contains the topology relations between Equipment and RAN.                                                                                                                                                                                                       |
+-------------------+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| REL_OAM_RAN       | This model contains the topology relations between O&M and RAN.                                                                                                                                                                                                             |
+-------------------+-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+


Retrieving and using topology modules
=====================================

Topology & Inventory provides APIs to enable users :download:`query module
data <../teiv/src/main/resources/v1/topology-exposure-inventory-openapi.yaml>`
that can be used to understand the existing topology and inventory model,
parse the modules, and understand what objects are supported over the R1
interface, so adequate queries can be made on topology and inventory data.

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
parameters can be found in :doc:`Supported filter options </supported-filter-options>`.

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
