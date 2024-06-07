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

Topology & Inventory Data Models
================================

The following are the currently supported modules in Topology &
Inventory.

Common YANG extensions
----------------------
.. literalinclude:: ../teiv/src/main/resources/models/o-ran-smo-teiv-common-yang-extensions.yang
   :language: yang

Common YANG types
-----------------

.. image:: _static/data-model/yang-types.svg
  :width: 900

.. literalinclude:: ../teiv/src/main/resources/models/o-ran-smo-teiv-common-yang-types.yang
   :language: yang

Equipment
---------

.. image:: _static/data-model/equipment.svg
  :width: 900

.. image:: _static/data-model/equipment-relationships.svg
  :width: 900

.. literalinclude:: ../teiv/src/main/resources/models/o-ran-smo-teiv-equipment.yang
   :language: yang

RAN
---

.. image:: _static/data-model/ran.svg
  :width: 900

.. image:: _static/data-model/ran-relationships.svg
  :width: 900

.. literalinclude:: ../teiv/src/main/resources/models/o-ran-smo-teiv-ran.yang
   :language: yang

Relationship: Equipment RAN
---------------------------

.. image:: _static/data-model/equipment-ran.svg
  :width: 900

.. literalinclude:: ../teiv/src/main/resources/models/o-ran-smo-teiv-equipment-to-ran.yang
   :language: yang

OAM
---

.. image:: _static/data-model/oam.svg
  :width: 900

.. literalinclude:: ../teiv/src/main/resources/models/o-ran-smo-teiv-oam.yang
   :language: yang

Relationship: OAM RAN
---------------------

.. image:: _static/data-model/oam-ran.svg
  :width: 900

.. literalinclude:: ../teiv/src/main/resources/models/o-ran-smo-teiv-oam-to-ran.yang
   :language: yang
