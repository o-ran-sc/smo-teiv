/*
 *  ============LICENSE_START=======================================================
 *  Copyright (C) 2024 Ericsson
 *  Modifications Copyright (C) 2024-2025 OpenInfra Foundation Europe
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
                    "        \"RAN O&M to Logical topology model.\n" +
                    "\n" +
                    "        This model contains the RAN O&M to Logical topology relations\n" +
                    "\n" +
                    "        Copyright (C) 2024 Ericsson\n" +
                    "        Modifications Copyright (C) 2024 OpenInfra Foundation Europe\n" +
                    "\n" +
                    "        Licensed under the Apache License, Version 2.0 (the \\\"License\\\");\n" +
                    "        you may not use this file except in compliance with the License.\n" +
                    "        You may obtain a copy of the License at\n" +
                    "\n" +
                    "        http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "        Unless required by applicable law or agreed to in writing, software\n" +
                    "        distributed under the License is distributed on an \\\"AS IS\\\" BASIS,\n" +
                    "        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "        See the License for the specific language governing permissions and\n" +
                    "        limitations under the License.\n" +
                    "\n" +
                    "        SPDX-License-Identifier: Apache-2.0\";\n" +
                    "\n" +
                    "    revision \"2024-10-04\" {\n" +
                    "        description \"Added grouping, Origin_Relationship_Mapping_Grp to the topology object.\";\n" +
                    "        or-teiv-yext:label 0.5.0;\n" +
                    "    }\n" +
                    "\n" +
                    "    revision \"2024-07-15\" {\n" +
                    "        description \"This revision aligns O-RAN Work Group 10 Stage 2 Specification (O-RAN.WG10.TE&IV-CIMI.0-R004.v02.00)\";\n" +
                    "        or-teiv-yext:label 0.4.0;\n" +
                    "    }\n" +
                    "\n" +
                    "    revision \"2024-05-24\" {\n" +
                    "        description \"Initial revision.\";\n" +
                    "        or-teiv-yext:label 0.3.0;\n" +
                    "    }\n" +
                    "\n" +
                    "    or-teiv-yext:domain REL_OAM_RAN;\n" +
                    "\n" +
                    "    or-teiv-yext:biDirectionalTopologyRelationship MANAGEDELEMENT_MANAGES_ODUFUNCTION {    // 1 to 0..n\n" +
                    "\n" +
                    "        description\n" +
                    "            \"The aSide of this relationship is an instance of the ManagedElement type.\n" +
                    "            The bSide of this relationship is an instance of the ODUFunction type.\n" +
                    "            The ManagedElement represents the node that manages the ODUFunction.\n" +
                    "            A ManagedElement instance can manage many ODUFunctions.\n" +
                    "            An ODUFunction instance must be managed by one ManagedElement.\n" +
                    "            \";\n" +
                    "\n" +
                    "        uses or-teiv-types:Top_Grp_Type;\n" +
                    "        uses or-teiv-types:Origin_Relationship_Mapping_Grp;\n" +
                    "        key id;\n" +
                    "\n" +
                    "        leaf-list managed-oduFunction {\n" +
                    "            description \"Managed Element manages O-DU Function.\";\n" +
                    "            or-teiv-yext:aSide or-teiv-oam:ManagedElement;\n" +
                    "            type instance-identifier;\n" +
                    "        }\n" +
                    "\n" +
                    "        leaf managed-by-managedElement {\n" +
                    "            description \"O-DU Function managed by Managed Element.\";\n" +
                    "            or-teiv-yext:bSide or-teiv-ran:ODUFunction;\n" +
                    "            type instance-identifier;\n" +
                    "            mandatory true;\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    or-teiv-yext:biDirectionalTopologyRelationship MANAGEDELEMENT_MANAGES_OCUCPFUNCTION {    // 1 to 0..n\n" +
                    "\n" +
                    "        description\n" +
                    "            \"The aSide of this relationship is an instance of the ManagedElement type.\n" +
                    "            The bSide of this relationship is an instance of the OCUCPFunction type.\n" +
                    "            The ManagedElement represents the node that manages the OCUCPFunction.\n" +
                    "            A ManagedElement instance can manage many OCUCPFunctions.\n" +
                    "            An OCUCPFunction instance must be managed by one ManagedElement.\n" +
                    "            \";\n" +
                    "\n" +
                    "        uses or-teiv-types:Top_Grp_Type;\n" +
                    "        uses or-teiv-types:Origin_Relationship_Mapping_Grp;\n" +
                    "        key id;\n" +
                    "\n" +
                    "        leaf-list managed-ocucpFunction {\n" +
                    "            description \"Managed Element manages O-CU-CP Function.\";\n" +
                    "            or-teiv-yext:aSide or-teiv-oam:ManagedElement;\n" +
                    "            type instance-identifier;\n" +
                    "        }\n" +
                    "\n" +
                    "        leaf managed-by-managedElement {\n" +
                    "            description \"O-CU-CP Function managed by Managed Element.\";\n" +
                    "            or-teiv-yext:bSide or-teiv-ran:OCUCPFunction;\n" +
                    "            type instance-identifier;\n" +
                    "            mandatory true;\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    or-teiv-yext:biDirectionalTopologyRelationship MANAGEDELEMENT_MANAGES_OCUUPFUNCTION {    // 1 to 0..n\n" +
                    "\n" +
                    "        description\n" +
                    "            \"The aSide of this relationship is an instance of the ManagedElement type.\n" +
                    "            The bSide of this relationship is an instance of the OCUUPFunction type.\n" +
                    "            The ManagedElement represents the node that manages the OCUUPFunction.\n" +
                    "            A ManagedElement instance can manage many OCUUPFunctions.\n" +
                    "            An OCUUPFunction instance must be managed by one ManagedElement.\n" +
                    "            \";\n" +
                    "\n" +
                    "        uses or-teiv-types:Top_Grp_Type;\n" +
                    "        uses or-teiv-types:Origin_Relationship_Mapping_Grp;\n" +
                    "        key id;\n" +
                    "\n" +
                    "        leaf-list managed-ocuupFunction {\n" +
                    "            description \"Managed Element manages O-CU-UP Function.\";\n" +
                    "            or-teiv-yext:aSide or-teiv-oam:ManagedElement;\n" +
                    "            type instance-identifier;\n" +
                    "        }\n" +
                    "\n" +
                    "        leaf managed-by-managedElement {\n" +
                    "            description \"O-CU-UP Function managed by Managed Element.\";\n" +
                    "            or-teiv-yext:bSide or-teiv-ran:OCUUPFunction;\n" +
                    "            type instance-identifier;\n" +
                    "            mandatory true;\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    or-teiv-yext:biDirectionalTopologyRelationship MANAGEDELEMENT_MANAGES_ORUFUNCTION {    // 1 to 0..n\n" +
                    "\n" +
                    "        description\n" +
                    "            \"The aSide of this relationship is an instance of the ManagedElement type.\n" +
                    "            The bSide of this relationship is an instance of the ORUFunction type.\n" +
                    "            The ManagedElement represents the node that manages the ORUFunction.\n" +
                    "            A ManagedElement instance can manage many ORUFunction.\n" +
                    "            An ORUFunction instance must be managed by one ManagedElement.\n" +
                    "            \";\n" +
                    "\n" +
                    "        uses or-teiv-types:Top_Grp_Type;\n" +
                    "        uses or-teiv-types:Origin_Relationship_Mapping_Grp;\n" +
                    "        key id;\n" +
                    "\n" +
                    "        leaf-list managed-oruFunction {\n" +
                    "            description \"Managed Element manages O-RU Function.\";\n" +
                    "            or-teiv-yext:aSide or-teiv-oam:ManagedElement;\n" +
                    "            type instance-identifier;\n" +
                    "        }\n" +
                    "\n" +
                    "        leaf managed-by-managedElement {\n" +
                    "            description \"O-RU Function managed by Managed Element.\";\n" +
                    "            or-teiv-yext:bSide or-teiv-ran:ORUFunction;\n" +
                    "            type instance-identifier;\n" +
                    "            mandatory true;\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    or-teiv-yext:biDirectionalTopologyRelationship MANAGEDELEMENT_MANAGES_NEARRTRICFUNCTION {    // 1 to 0..n\n" +
                    "\n" +
                    "        description\n" +
                    "            \"The aSide of this relationship is an instance of the ManagedElement type.\n" +
                    "            The bSide of this relationship is an instance of the NearRTRICFunction type.\n" +
                    "            The ManagedElement represents the node that manages the NearRTRICFunction.\n" +
                    "            A ManagedElement instance can manage many NearRTRICFunction.\n" +
                    "            An NearRTRICFunction instance must be managed by one ManagedElement.\n" +
                    "            \";\n" +
                    "\n" +
                    "        uses or-teiv-types:Top_Grp_Type;\n" +
                    "        uses or-teiv-types:Origin_Relationship_Mapping_Grp;\n" +
                    "        key id;\n" +
                    "\n" +
                    "        leaf-list managed-nearRTRICFunction {\n" +
                    "            description \"Managed Element manages Near RT RIC Function.\";\n" +
                    "            or-teiv-yext:aSide or-teiv-oam:ManagedElement;\n" +
                    "            type instance-identifier;\n" +
                    "        }\n" +
                    "\n" +
                    "        leaf managed-by-managedElement {\n" +
                    "            description \"Near RT RIC Function managed by Managed Element.\";\n" +
                    "            or-teiv-yext:bSide or-teiv-ran:NearRTRICFunction;\n" +
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
