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
package contracts.groups

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "SUCCESS - 201: Create a dynamic getEntitiesByDomain group"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "dynamic-cell-filter-group",
                "type": "dynamic",
                "criteria": {
                    "queryType": "getEntitiesByDomain",
                    "domain": "RAN",
                    "targetFilter": "/NRCellDU/attributes(nCI)",
                    "scopeFilter": "/NRCellDU/attributes[@cellLocalId=1]"
                }
            }''')
        }
        response {
            status CREATED()
            headers {
                contentType('application/json')
            }
            body('''{
                "id": "urn:o-ran:smo:teiv:group=759bac4e-b4c0-468e-90c1-1a8b7d09c8d4",
                "name": "dynamic-cell-filter-group",
                "type": "dynamic",
                "criteria": {
                    "queryType": "getEntitiesByDomain",
                    "domain": "RAN",
                    "targetFilter": "/NRCellDU/attributes(nCI)",
                    "scopeFilter": "/NRCellDU/attributes[@cellLocalId=1]"
                },
                "members": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=759bac4e-b4c0-468e-90c1-1a8b7d09c8d4/members"
                }
            }''')
            bodyMatchers {
                jsonPath('$.id', byRegex("[a-zA-Z0-9:=-]{61}"))
                jsonPath('$.members.href', byRegex("[a-zA-Z0-9/:=-]{77}"))
            }
        }
    },
    Contract.make {
        description "SUCCESS - 201: Create a dynamic getEntitiesByType group"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "dynamic-cell-filter-group",
                "type": "dynamic",
                "criteria": {
                    "queryType": "getEntitiesByType",
                    "domain": "RAN",
                    "entityTypeName": "NRCellDU"
                }
            }''')
        }
        response {
            status CREATED()
            headers {
                contentType('application/json')
            }
            body('''{
                "id": "urn:o-ran:smo:teiv:group=a9c88001-33e0-4920-ba2c-8ee1a27f9849",
                "name": "dynamic-cell-filter-group",
                "type": "dynamic",
                "criteria": {
                    "queryType": "getEntitiesByType",
                    "domain": "RAN",
                    "entityTypeName": "NRCellDU"
                },
                "members": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=a9c88001-33e0-4920-ba2c-8ee1a27f9849/members"
                }
            }''')
            bodyMatchers {
                jsonPath('$.id', byRegex("[a-zA-Z0-9:=-]{61}"))
                jsonPath('$.members.href', byRegex("[a-zA-Z0-9/:=-]{77}"))
            }
        }
    },
    Contract.make {
        description "SUCCESS - 201: Create a dynamic getRelationshipsForEntityId group where entity id doesnt exists in teiv"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "dynamic-cell-filter-group",
                "type": "dynamic",
                "criteria": {
                    "queryType": "getRelationshipsForEntityId",
                    "domain": "RAN",
                    "entityTypeName": "NRCellDU",
                    "entityId": "urn:NRCellDU-1"
                }
            }''')
        }
        response {
            status CREATED()
            headers {
                contentType('application/json')
            }
            body('''{
                "id": "urn:o-ran:smo:teiv:group=bbeb1db4-88dc-47b3-aa07-fdad50145b16",
                "name": "dynamic-cell-filter-group",
                "type": "dynamic",
                "criteria": {
                    "queryType": "getRelationshipsForEntityId",
                    "domain": "RAN",
                    "entityTypeName": "NRCellDU",
                    "entityId": "urn:NRCellDU-1"
                },
                "members": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=bbeb1db4-88dc-47b3-aa07-fdad50145b16/members"
                }
            }''')
            bodyMatchers {
                jsonPath('$.id', byRegex("[a-zA-Z0-9:=-]{61}"))
                jsonPath('$.members.href', byRegex("[a-zA-Z0-9/:=-]{77}"))
            }
        }
    },
    Contract.make {
        description "SUCCESS - 201: Create a dynamic getRelationshipsByType group"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "dynamic-cell-filter-group",
                "type": "dynamic",
                "criteria": {
                    "queryType": "getRelationshipsByType",
                    "domain": "RAN",
                    "relationshipTypeName": "NRCELLDU_USES_NRSECTORCARRIER"
                }
            }''')
        }
        response {
            status CREATED()
            headers {
                contentType('application/json')
            }
            body('''{
                "id": "urn:o-ran:smo:teiv:group=1761d6ea-360a-4947-b608-cc2a8a87ca92",
                "name": "dynamic-cell-filter-group",
                "type": "dynamic",
                "criteria": {
                    "queryType": "getRelationshipsByType",
                    "domain": "RAN",
                    "relationshipTypeName": "NRCELLDU_USES_NRSECTORCARRIER"
                },
                "members": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=1761d6ea-360a-4947-b608-cc2a8a87ca92/members"
                }
            }''')
            bodyMatchers {
                jsonPath('$.id', byRegex("[a-zA-Z0-9:=-]{61}"))
                jsonPath('$.members.href', byRegex("[a-zA-Z0-9/:=-]{77}"))
            }
        }
    },
    Contract.make {
        description "SUCCESS - 201: Create a static group"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "static-cell-filter-group",
                "type": "static",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=2"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-oam:ManagedElement": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=EA8BF964B4888BFD1991D8E2ECDFA7723118D3829C1378ACBB5484F9ADE328957641013EDF2BEC80CB8E4E0A46CC2D85B960EF25ABF61CC8601095948E368624"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=7EB5B959010A84E95BFA6CAA314120CC335007FCDB2947A53A6E19171BB8742965A874FB89B73CD21EB790E52C8E6DC129B35469BA3867DAC67F4DE72E60185E"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=86084B5A80FAC7339117CEB91A4838FAC28C50AF00C9A13DF66FFA497356A8F440626A935B9621D4C833F0A6DE2722EDC9A312E506D80235A8C1BF54D8DFACC8"
                            }
                        ]
                    }
                ]
            }''')
        }
        response {
            status CREATED()
            headers {
                contentType('application/json')
            }
            body('''{
                "id": "urn:o-ran:smo:teiv:group=cce97e77-add2-4517-8a4e-d1b289003281",
                "name": "static-cell-filter-group",
                "type": "static",
                "providedMembers": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=cce97e77-add2-4517-8a4e-d1b289003281/provided-members"
                },
                "members": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=cce97e77-add2-4517-8a4e-d1b289003281/members"
                }
            }''')
            bodyMatchers {
                jsonPath('$.id', byRegex("[a-zA-Z0-9:=-]{61}"))
                jsonPath('$.providedMembers.href', byRegex("[a-zA-Z0-9/:=-]{86}"))
                jsonPath('$.members.href', byRegex("[a-zA-Z0-9/:=-]{77}"))
            }
        }
    },
    Contract.make {
        description "ERROR - 400: Create a dynamic group with no criteria specified"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "dynamic-cell-filter-group",
                "type": "dynamic",
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "Bad Request",
                "status": 400,
                "detail": "Failed to read request",
                "instance": "/topology-inventory/v1/groups"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a dynamic group with 'static' group type"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "dynamic-cell-filter-group",
                "type": "static",
                "criteria": {
                    "queryType": "getRelationshipsByType",
                    "domain": "RAN",
                    "relationshipTypeName": "NRCELLDU_USES_NRSECTORCARRIER"
                }
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid type specified",
                "details": "A dynamic group cannot be created of type 'static'"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a dynamic group with invalid group type"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "dynamic-cell-filter-group",
                "type": "invalidGroupType",
                "criteria": {
                    "queryType": "getRelationshipsByType",
                    "domain": "RAN",
                    "relationshipTypeName": "NRCELLDU_USES_NRSECTORCARRIER"
                }
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid type specified",
                "details": "Invalid group type. Only 'static' or 'dynamic' types are allowed"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a dynamic group with invalid query type"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "dynamic-cell-filter-group",
                "type": "dynamic",
                "criteria": {
                    "queryType": "invalidQueryType",
                    "domain": "RAN",
                    "relationshipTypeName": "NRCELLDU_USES_NRSECTORCARRIER"
                }
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid type specified",
                "details": "Invalid query type. Only 'getRelationshipsForEntityId', 'getEntitiesByDomain', 'getEntitiesByType' & 'getRelationshipsByType' are supported."
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a dynamic group with missing criteria property(entityTypeName) for getEntitiesByType"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "dynamic-cell-filter-group",
                "type": "dynamic",
                "criteria": {
                    "queryType": "getEntitiesByType",
                    "domain": "RAN",
                }
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "Bad Request",
                "status": 400,
                "detail": "Failed to read request",
                "instance": "/topology-inventory/v1/groups"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a dynamic group with unsupported domain for getRelationshipsByType"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "dynamic-cell-filter-group",
                "type": "dynamic",
                "criteria": {
                    "queryType": "getRelationshipsByType",
                    "domain": "bla",
                    "relationshipTypeName": "NRCELLDU_USES_NRSECTORCARRIER"
                }
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "BAD_REQUEST",
                "status": 400,
                "detail": "Unknown domain: bla, known domains: [CLOUD, EQUIPMENT, OAM, PHYSICAL, RAN, REL_CLOUD_RAN, REL_EQUIPMENT_RAN, REL_OAM_CLOUD, REL_OAM_RAN, REL_PHYSICAL_RAN, TEIV, TEST]",
                "instance": ""
            }''')
        }
    },
    //10
    Contract.make {
        description "ERROR - 400: Create a dynamic group with invalid filter"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "dynamic-cell-filter-group",
                "type": "dynamic",
                "criteria": {
                    "queryType": "getRelationshipsForEntityId",
                    "domain": "TEIV",
                    "entityTypeName": "NRCellDU",
                    "entityId": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1",
                    "targetFilter": "/NRCellDU/attributes(nCI)",
                    "scopeFilter": "/NRCellDU/attributes[@cellLocalId=2]"
                }
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "BAD_REQUEST",
                "status": 400,
                "detail": "NRCellDU is not a valid relation",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400:: Create a dynamic getRelationshipsForEntityId group where entity id does not start with urn:"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "dynamic-cell-filter-group",
                "type": "dynamic",
                "criteria": {
                    "queryType": "getRelationshipsForEntityId",
                    "domain": "RAN",
                    "entityTypeName": "NRCellDU",
                    "entityId": "NRCellDU-1"
                }
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "BAD_REQUEST",
                "status": 400,
                "detail": "Topology ID : NRCellDU-1 is not in supported format. Topology ID should start with urn:",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with no providedMembers specified"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "static-cell-filter-group",
                "type": "static"
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "Bad Request",
                "status": 400,
                "detail": "Invalid request content.",
                "instance": "/topology-inventory/v1/groups"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with invalid providedMembers(empty list)"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "static-cell-filter-group",
                "type": "static",
                "providedMembers": []
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "Bad Request",
                "status": 400,
                "detail": "Invalid request content.",
                "instance": "/topology-inventory/v1/groups"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with invalid providedMembers"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "static-cell-filter-group",
                "type": "static",
                "providedMembers": [
                    "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1",
                    "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=2"
                ]
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "BAD_REQUEST",
                "status": 400,
                "detail": "Unable to parse the given providedMembers. List elements are not of the type object.",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with invalid providedMembers"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "static-cell-filter-group",
                "type": "static",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1",
                            "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=2"
                        ]
                    }
                ]
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "BAD_REQUEST",
                "status": 400,
                "detail": "Unable to parse the given providedMembers. List elements are not of the type object.",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with invalid module name for the entity type"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "static-cell-filter-group",
                "type": "static",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:ManagedElement": [
                            {
                                "id": "urn:3gpp:dn:NRCellDU=1,ODUFunction=1,ManagedElement=1"
                            },
                            {
                                "id": "urn:3gpp:dn:NRCellDU=1,ODUFunction=1,ManagedElement=2"
                            }
                        ]
                    }
                ]
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "BAD_REQUEST",
                "status": 400,
                "detail": "Unable to parse the given providedMembers. Invalid topology type 'o-ran-smo-teiv-ran:ManagedElement', not found in the model",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with wrong topology type format"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "static-cell-filter-group",
                "type": "static",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-oam:ManagedElement:bla": [
                            {
                                "id": "urn:3gpp:dn:NRCellDU=1,ODUFunction=1,ManagedElement=1"
                            },
                            {
                                "id": "urn:3gpp:dn:NRCellDU=1,ODUFunction=1,ManagedElement=2"
                            }
                        ]
                    }
                ]
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "BAD_REQUEST",
                "status": 400,
                "detail": "Unable to parse the given providedMembers. Topology type must be in the format 'moduleName:topologyTypeName'. Provided: o-ran-smo-teiv-oam:ManagedElement:bla",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with wrong topology id where topology id doesnt start with urn:"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "static-cell-filter-group",
                "type": "static",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-oam:ManagedElement": [
                            {
                                "id": "urn:3gpp:dn:NRCellDU=1,ODUFunction=1,ManagedElement=1"
                            },
                            {
                                "id": "3gpp:dn:NRCellDU=1,ODUFunction=1,ManagedElement=2"
                            }
                        ]
                    }
                ]
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "BAD_REQUEST",
                "status": 400,
                "detail": "Unable to parse the given providedMembers. Topology id 3gpp:dn:NRCellDU=1,ODUFunction=1,ManagedElement=2 is not in supported format. Provided members id should start with urn:",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with invalid providedMembers(list contains empty object)"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "static-cell-filter-group",
                "type": "static",
                "providedMembers": [
                    {}
                ]
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "BAD_REQUEST",
                "status": 400,
                "detail": "Unable to parse the given providedMembers. Empty object present in providedMembers.",
                "instance": ""
            }''')
        }
    },
    //20
    Contract.make {
        description "ERROR - 400: Create a static group with invalid providedMembers(list contains empty object)"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "static-cell-filter-group",
                "type": "static",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {}
                        ]
                    }
                ]
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "BAD_REQUEST",
                "status": 400,
                "detail": "Unable to parse the given providedMembers. Empty object present in providedMembers.",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with invalid providedMembers(invalid id key)"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "static-cell-filter-group",
                "type": "static",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "idKey": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1"
                            }
                        ]
                    }
                ]
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "BAD_REQUEST",
                "status": 400,
                "detail": "Unable to parse the given providedMembers. Invalid key/value present in {\\"idKey\\":\\"urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1\\"}.",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with invalid providedMembers(id value is not of type string)"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "static-cell-filter-group",
                "type": "static",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": ["urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1"]
                            }
                        ]
                    }
                ]
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "BAD_REQUEST",
                "status": 400,
                "detail": "Unable to parse the given providedMembers. Invalid key/value present in {\\"id\\":[\\"urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1\\"]}.",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with invalid providedMembers(group id present in providedMembers)"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "static-cell-filter-group",
                "type": "static",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440050"
                            }
                        ]
                    }
                ]
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "BAD_REQUEST",
                "status": 400,
                "detail": "Unable to parse the given providedMembers. Nested topology groups is not supported. Provided members contain topology group id: urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440050",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with invalid providedMembers"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "static-cell-filter-group",
                "type": "static",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1",
                                "extraKey": "extraValue"
                            }
                        ]
                    }
                ]
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "BAD_REQUEST",
                "status": 400,
                "detail": "Unable to parse the given providedMembers. More than one key:value present in {\\"id\\":\\"urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1\\",\\"extraKey\\":\\"extraValue\\"}.",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with invalid providedMembers(more than one topology type in an anonymous object)"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "static-cell-filter-group",
                "type": "static",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1"
                            }
                        ],
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1"
                            }
                        ]
                    }
                ]
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "BAD_REQUEST",
                "status": 400,
                "detail": "Unable to parse the given providedMembers. More than one key:value present in {\\"o-ran-smo-teiv-ran:NRCellDU\\":[{\\"id\\":\\"urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1\\"}],\\"o-ran-smo-teiv-ran:ODUFunction\\":[{\\"id\\":\\"urn:3gpp:dn:ManagedElement=1,ODUFunction=1\\"}]}.",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with invalid providedMembers(topology identifiers exceed max limit)"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "static-cell-filter-group",
                "type": "static",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=2"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=3"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=4"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=5"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=5"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=6"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=2"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=3"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=3"
                            }
                        ]
                    }
                ]
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "BAD_REQUEST",
                "status": 400,
                "detail": "The total number of topology identifiers in the request is 11. The maximum allowed in a single request is 10.",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a dynamic group with 'static' group type"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "static-cell-filter-group",
                "type": "dynamic",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1",
                            "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=2"
                        ]
                    }
                ]
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid type specified",
                "details": "A static group cannot be created of type 'dynamic'"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a dynamic group with invalid group type"
        request {
            method POST()
            url("/topology-inventory/v1/groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "static-cell-filter-group",
                "type": "invalidGroup",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1",
                            "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=2"
                        ]
                    }
                ]
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid type specified",
                "details": "Invalid group type. Only 'static' or 'dynamic' types are allowed"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Invalid endpoint"
        request {
            method POST()
            url("/topology-inventory/v1/teiv-groups")
            headers {
                accept("application/json")
                contentType("application/json")
            }
            body('''{
                "name": "dynamic-cell-filter-group",
                "type": "dynamic",
                "criteria": {
                    "queryType": "getEntitiesByDomain",
                    "domain": "RAN",
                    "targetFilter": "/NRCellDU/attributes(nCI)",
                    "scopeFilter": "/NRCellDU/attributes[@cellLocalId=1]"
                }
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "BAD_REQUEST",
                "status": 400,
                "detail": "No static resource topology-inventory/v1/teiv-groups.",
                "instance": ""
            }''')
        }
    } //30
]
