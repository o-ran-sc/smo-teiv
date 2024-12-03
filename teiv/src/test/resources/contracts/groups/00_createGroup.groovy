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
package contracts.groups

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "SUCCESS - 201: Create a dynamic getEntitiesByDomain group"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/groups")
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
            url("/topology-inventory/v1alpha11/groups")
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
        description "SUCCESS - 201: Create a dynamic getRelationshipsForEntityId group"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/groups")
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
                    "entityId": "NRCellDU-1"
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
            url("/topology-inventory/v1alpha11/groups")
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
            url("/topology-inventory/v1alpha11/groups")
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
                                "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=4E40BE000AFEA418"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=9CD8DCA1FE61CE75"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=9243B48F7D6A6C56"
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
            url("/topology-inventory/v1alpha11/groups")
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
                "instance": "/topology-inventory/v1alpha11/groups"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a dynamic group with invalid group type"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/groups")
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
                "type": "about:blank",
                "title": "Bad Request",
                "status": 400,
                "detail": "Failed to read request",
                "instance": "/topology-inventory/v1alpha11/groups"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a dynamic group with invalid query type"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/groups")
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
                "type": "about:blank",
                "title": "Bad Request",
                "status": 400,
                "detail": "Failed to read request",
                "instance": "/topology-inventory/v1alpha11/groups"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a dynamic group with missing criteria property(entityTypeName) for getEntitiesByType"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/groups")
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
                "instance": "/topology-inventory/v1alpha11/groups"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with no providedMembers specified"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/groups")
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
                "instance": "/topology-inventory/v1alpha11/groups"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with invalid providedMembers(empty list)"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/groups")
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
                "instance": "/topology-inventory/v1alpha11/groups"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with invalid providedMembers"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/groups")
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
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid providedMembers",
                "details": "Unable to parse the given providedMembers. List elements are not of the type object."
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with invalid providedMembers"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/groups")
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
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid providedMembers",
                "details": "Unable to parse the given providedMembers. List elements are not of the type object."
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with invalid providedMembers(list contains empty object)"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/groups")
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
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid providedMembers",
                "details": "Unable to parse the given providedMembers. Empty object present in providedMembers."
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with invalid providedMembers(list contains empty object)"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/groups")
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
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid providedMembers",
                "details": "Unable to parse the given providedMembers. Empty object present in providedMembers."
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with invalid providedMembers(invalid id key)"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/groups")
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
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid providedMembers",
                "details": "Unable to parse the given providedMembers. Invalid key/value present in {\\"idKey\\":\\"urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1\\"}."
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with invalid providedMembers(id value is not of type string)"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/groups")
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
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid providedMembers",
                "details": "Unable to parse the given providedMembers. Invalid key/value present in {\\"id\\":[\\"urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1\\"]}."
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with invalid providedMembers(group id present in providedMembers)"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/groups")
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
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid providedMembers",
                "details": "Unable to parse the given providedMembers. Nested topology groups is not supported. Provided members contain topology group id: urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440050"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with invalid providedMembers"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/groups")
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
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid providedMembers",
                "details": "Unable to parse the given providedMembers. More than one key:value present in {\\"id\\":\\"urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1\\",\\"extraKey\\":\\"extraValue\\"}."
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with invalid providedMembers(more than one topology type in an anonymous object)"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/groups")
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
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid providedMembers",
                "details": "Unable to parse the given providedMembers. More than one key:value present in {\\"o-ran-smo-teiv-ran:NRCellDU\\":[{\\"id\\":\\"urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1\\"}],\\"o-ran-smo-teiv-ran:ODUFunction\\":[{\\"id\\":\\"urn:3gpp:dn:ManagedElement=1,ODUFunction=1\\"}]}."
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Create a static group with invalid providedMembers(topology identifiers exceed max limit)"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/groups")
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
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Limit exceeded",
                "details": "The total number of topology identifiers in the request is 11. The maximum allowed in a single request is 10."
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Invalid endpoint"
        request {
            method POST()
            url("/topology-inventory/v1alpha11/ties-groups")
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
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "No static resource topology-inventory/v1alpha11/ties-groups.",
                "details": null
            }''')
        }
    }
]
