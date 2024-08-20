/*
 *  ============LICENSE_START=======================================================
 *  Copyright (C) 2024 Ericsson
 *  Modifications Copyright (C) 2024 OpenInfra Foundation Europe
 *  ================================================================================
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  SPDX-License-Identifier: Apache-2.0
 *  ============LICENSE_END=========================================================
 */
package contracts.schemas

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "SUCCESS - 200: Get schema with name o-ran-smo-teiv-rel-oam-ran"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/schemas/o-ran-smo-teiv-rel-oam-ran/content")
        }
        response {
            status OK()
            body("module o-ran-smo-teiv-rel-oam-ran {\n" +
                    "    yang-version 1.1;\n" +
                    "    namespace \"urn:o-ran:smo-teiv-rel-oam-ran\";\n" +
                    "    prefix or-teiv-rel-oamran;\n" +
                    "\n" +
                    "    import o-ran-smo-teiv-common-yang-types { prefix or-teiv-types; }\n" +
                    "\n" +
                    "    import o-ran-smo-teiv-common-yang-extensions { prefix or-teiv-yext; }\n" +
                    "\n" +
                    "    import o-ran-smo-teiv-oam { prefix or-teiv-oam; }\n" +
                    "\n" +
                    "    import o-ran-smo-teiv-ran { prefix or-teiv-ran; }\n" +
                    "\n" +
                    "    organization \"ORAN\";\n" +
                    "    contact \"The Authors\";\n" +
                    "    description\n" +
                    "    \"RAN O&M to Logical topology model.\n" +
                    "\n" +
                    "    This model contains the RAN O&M to Logical topology relations\n" +
                    "\n" +
                    "    Copyright (C) 2024 Ericsson\n" +
                    "    Modifications Copyright (C) 2024 OpenInfra Foundation Europe\n" +
                    "\n" +
                    "    Licensed under the Apache License, Version 2.0 (the \\\"License\\\");\n" +
                    "    you may not use this file except in compliance with the License.\n" +
                    "    You may obtain a copy of the License at\n" +
                    "\n" +
                    "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "    Unless required by applicable law or agreed to in writing, software\n" +
                    "    distributed under the License is distributed on an \\\"AS IS\\\" BASIS,\n" +
                    "    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "    See the License for the specific language governing permissions and\n" +
                    "    limitations under the License.\n" +
                    "\n" +
                    "    SPDX-License-Identifier: Apache-2.0\";\n" +
                    "\n" +
                    "    revision \"2024-05-24\" {\n" +
                    "        description \"Initial revision.\";\n" +
                    "        or-teiv-yext:label 0.3.0;\n" +
                    "    }\n" +
                    "\n" +
                    "    or-teiv-yext:domain REL_OAM_RAN;\n" +
                    "\n" +
                    "    or-teiv-yext:biDirectionalTopologyRelationship MANAGEDELEMENT_MANAGES_ENODEBFUNCTION {   // 1 to 0..n\n" +
                    "\n" +
                    "        uses or-teiv-types:Top_Grp_Type;\n" +
                    "        key id;\n" +
                    "\n" +
                    "        leaf-list managed-enodebFunction {\n" +
                    "            description \"Managed Element manages eNodeB Function.\";\n" +
                    "            or-teiv-yext:aSide or-teiv-oam:ManagedElement;\n" +
                    "            type instance-identifier;\n" +
                    "        }\n" +
                    "\n" +
                    "        leaf managed-by-managedElement {\n" +
                    "            description \"eNodeB Function managed by Managed Element.\";\n" +
                    "            or-teiv-yext:bSide or-teiv-ran:ENodeBFunction;\n" +
                    "            type instance-identifier;\n" +
                    "            mandatory true;\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    or-teiv-yext:biDirectionalTopologyRelationship MANAGEDELEMENT_MANAGES_GNBDUFUNCTION {    // 1 to 0..n\n" +
                    "\n" +
                    "        uses or-teiv-types:Top_Grp_Type;\n" +
                    "        key id;\n" +
                    "\n" +
                    "        leaf-list managed-gnbduFunction {\n" +
                    "            description \"Managed Element manages gNodeB-DU Function.\";\n" +
                    "            or-teiv-yext:aSide or-teiv-oam:ManagedElement;\n" +
                    "            type instance-identifier;\n" +
                    "        }\n" +
                    "\n" +
                    "        leaf managed-by-managedElement {\n" +
                    "            description \"gNodeB-DU Function managed by Managed Element.\";\n" +
                    "            or-teiv-yext:bSide or-teiv-ran:GNBDUFunction;\n" +
                    "            type instance-identifier;\n" +
                    "            mandatory true;\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    or-teiv-yext:biDirectionalTopologyRelationship MANAGEDELEMENT_MANAGES_GNBCUCPFUNCTION {    // 1 to 0..n\n" +
                    "\n" +
                    "        uses or-teiv-types:Top_Grp_Type;\n" +
                    "        key id;\n" +
                    "\n" +
                    "        leaf-list managed-gnbcucpFunction {\n" +
                    "            description \"Managed Element manages gNodeB-CU-CP Function.\";\n" +
                    "            or-teiv-yext:aSide or-teiv-oam:ManagedElement;\n" +
                    "            type instance-identifier;\n" +
                    "        }\n" +
                    "\n" +
                    "        leaf managed-by-managedElement {\n" +
                    "            description \"gNodeB-CU-CP Function managed by Managed Element.\";\n" +
                    "            or-teiv-yext:bSide or-teiv-ran:GNBCUCPFunction;\n" +
                    "            type instance-identifier;\n" +
                    "            mandatory true;\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    or-teiv-yext:biDirectionalTopologyRelationship MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION {    // 1 to 0..n\n" +
                    "\n" +
                    "        uses or-teiv-types:Top_Grp_Type;\n" +
                    "        key id;\n" +
                    "\n" +
                    "        leaf-list managed-gnbcuupFunction {\n" +
                    "            description \"Managed Element manages gNodeB-CU-UP Function.\";\n" +
                    "            or-teiv-yext:aSide or-teiv-oam:ManagedElement;\n" +
                    "            type instance-identifier;\n" +
                    "        }\n" +
                    "\n" +
                    "        leaf managed-by-managedElement {\n" +
                    "            description \"gNodeB-CU-UP Function managed by Managed Element.\";\n" +
                    "            or-teiv-yext:bSide or-teiv-ran:GNBCUUPFunction;\n" +
                    "            type instance-identifier;\n" +
                    "            mandatory true;\n" +
                    "        }\n" +
                    "    }\n" +
                    "}")
            headers {
                contentType('text/plain')
            }
        }
    },
    Contract.make {
        description "ERROR - 400: Get schema content with invalid name invalid"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/schemas/invalid/content")
        }
        response {
            status BAD_REQUEST()
            body('''{
                    "status": "BAD_REQUEST",
                    "message": "Invalid schema name",
                    "details": "Invalid schema name: invalid"
                }''')
            headers {
                contentType('application/json')
            }
        }
    }
]
