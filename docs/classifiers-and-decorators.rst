.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. SPDX-License-Identifier: CC-BY-4.0
.. Copyright (C) 2024 Nordix Foundation. All rights Reserved
.. Copyright (C) 2024 OpenInfra Foundation Europe. All Rights Reserved

Classifiers and decorators
##########################

Classifiers and decorators are user defined tags that represent
attributes of a topology object. Classifiers and decorators are added
to a topology object through a REST API. They can be used in querying
topology.

Classifier (also known as tag or label) permits the association of a
well defined user specified string with an entity or relationship.

Decorators are key-value pairs that can be associated with topology
objects. The type of the value is defined by the user in the schema
which declares the decorator. It can be either a string, number or
a boolean.

Before classifying the entities or the relationships, the schema
must be created and validated. The schema can be created, by using
its own endpoint, with a :doc:`Yang Module <data-models-guide>`.
The user must provide a unique module name, to avoid collision of
multiple users access that are defining classifiers and decorators.
The schema cannot be modified later on but only deleted and recreated,
if needed. When a schema is successfully created and validated, the user
can add the classifiers to the entities or relationships. Classifiers and
decorators have their own endpoints and the request type, to be used,
is POST. By default, maximum mergable classifiers and decorators are 100.
That means the sum of the given entities and the relationships in a single
request cannot exceed 100. Default value can be changed with using the
site values of the application at .Values.consumerData.batchSize using
an integer value.

How to use classifiers and decorators
-------------------------------------

 1. Create a schema with the schemas endpoint using :doc:`Yang Module
    <data-models-guide>`. After the successful schema creation, the
    topology objects are ready to be classified.
 2. Assign classifiers and/or decorators to the entities and/or relationships.
 3. Search classifiers and/or decorators by using :doc:`queries
    <supported-filter-options>`.

Classifiers
===========

Classifiers support the following two types of 'operation', which must be
identified in the body of the request:

 * merge: defined classifiers can be applied to entities and relationships
   within a single request.
 * delete: existing tagged classifiers can be removed.

**Example:**

In this example, user is classifying two given entity IDs and a single
relationship ID with a single request.

.. code:: json

   {
     "operation": "merge",
     "classifiers": [
       "module-x:Outdoor",
       "module-y:Rural",
       "module-z:Weekend"
     ],
     "entityIds": [
       "urn:3gpp:dn:ManagedElement=1,GNBDUFunction=1,NRCellDU=1",
       "urn:3gpp:dn:ManagedElement=1,GNBDUFunction=1,NRCellDU=2"
     ],
     "relationshipIds": [
       "urn:o-ran:smo:teiv:sha512:NRCELLDU_USES_NRSECTORCARRIER=CA576F4716C36A1BD1C506DCB58418FC731858D3D3F856F536813A8C4D3F1CC21292E506815410E04496D709D96066EBC0E4890DEFC3789EDC4BD9C28DA1D52B"
     ]
   }

Decorators
==========

Decorators support the following two types of 'operations', which must be
identified in the body of the request:

 * merge: existing decorators can be updated or applied to entities and
   relationships within a single request.
 * delete: existing tagged decorators can be removed.

**Example:**

In this example, user is tagging decorators with two given entity IDs and
a single relationship ID with a single request.

.. code:: json

   {
     "operation": "merge",
     "decorators": {
       "module-x:location": "Stockholm",
       "module-y:vendor": "Ericsson"
     },
     "entityIds": [
       "urn:3gpp:dn:ManagedElement=1,GNBDUFunction=1,NRCellDU=1",
       "urn:3gpp:dn:ManagedElement=1,GNBDUFunction=1,NRCellDU=2"
     ],
     "relationshipIds": [
       "urn:o-ran:smo:teiv:sha512:NRCELLDU_USES_NRSECTORCARRIER=CA576F4716C36A1BD1C506DCB58418FC731858D3D3F856F536813A8C4D3F1CC21292E506815410E04496D709D96066EBC0E4890DEFC3789EDC4BD9C28DA1D52B"
     ]
   }

