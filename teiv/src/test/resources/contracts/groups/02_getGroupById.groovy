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
        description "SUCCESS - 200: Get a dynamic group by id (criteria: getEntitiesByDomain)"
        request {
            method GET()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440000")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "id": "urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440000",
                "name": "test-dynamic-group-for-delete",
                "type": "dynamic",
                "criteria": {
                    "queryType": "getEntitiesByDomain",
                    "domain": "OAM"
                },
                "members": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440000/members"
                }
            }''')
            bodyMatchers {
                jsonPath('$.id', byEquality())
                jsonPath('$.name', byEquality())
                jsonPath('$.type', byEquality())
                jsonPath('$.criteria.queryType', byEquality())
                jsonPath('$.criteria.domain', byEquality())
                jsonPath('$.members.href', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get a dynamic group by id (criteria: getEntitiesByType)"
        request {
            method GET()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440001")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "id": "urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440001",
                "name": "test-group-get-entities-by-type",
                "type": "dynamic",
                "criteria": {
                    "queryType": "getEntitiesByType",
                    "domain": "RAN",
                    "entityTypeName": "ODUFunction",
                    "targetFilter": "/sourceIds"
                },
                "members": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440001/members"
                }
            }''')
            bodyMatchers {
                jsonPath('$.id', byEquality())
                jsonPath('$.name', byEquality())
                jsonPath('$.type', byEquality())
                jsonPath('$.criteria.queryType', byEquality())
                jsonPath('$.criteria.domain', byEquality())
                jsonPath('$.criteria.entityTypeName', byEquality())
                jsonPath('$.criteria.targetFilter', byEquality())
                jsonPath('$.members.href', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get a dynamic group by id (criteria: getRelationshipsForEntityId)"
        request {
            method GET()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440002")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "id": "urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440002",
                "name": "test-group-get-rels-for-entity-id",
                "type": "dynamic",
                "criteria": {
                    "queryType": "getRelationshipsForEntityId",
                    "domain": "TEIV",
                    "entityTypeName": "ODUFunction",
                    "entityId": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9"
                },
                "members": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440002/members"
                }
            }''')
            bodyMatchers {
                jsonPath('$.id', byEquality())
                jsonPath('$.name', byEquality())
                jsonPath('$.type', byEquality())
                jsonPath('$.criteria.queryType', byEquality())
                jsonPath('$.criteria.domain', byEquality())
                jsonPath('$.criteria.entityTypeName', byEquality())
                jsonPath('$.criteria.entityId', byEquality())
                jsonPath('$.members.href', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get a dynamic group by id (criteria: getRelationshipsByType)"
        request {
            method GET()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440003")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "id": "urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440003",
                "name": "test-group-get-rels-by-type",
                "type": "dynamic",
                "criteria": {
                    "queryType": "getRelationshipsByType",
                    "domain": "REL_OAM_RAN",
                    "relationshipTypeName": "MANAGEDELEMENT_MANAGES_ODUFUNCTION"
                },
                "members": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440003/members"
                }
            }''')
            bodyMatchers {
                jsonPath('$.id', byEquality())
                jsonPath('$.name', byEquality())
                jsonPath('$.type', byEquality())
                jsonPath('$.criteria.queryType', byEquality())
                jsonPath('$.criteria.domain', byEquality())
                jsonPath('$.criteria.relationshipTypeName', byEquality())
                jsonPath('$.members.href', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get a static group by id"
        request {
            method GET()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "id": "urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201",
                "name": "test-group-get-by-id-static-group",
                "type": "static",
                "providedMembers": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members"
                  },
                "members": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/members"
                }
            }''')
            bodyMatchers {
                jsonPath('$.id', byEquality())
                jsonPath('$.name', byEquality())
                jsonPath('$.type', byEquality())
                jsonPath('$.providedMembers.href', byEquality())
                jsonPath('$.members.href', byEquality())
            }
        }
    },
    Contract.make {
        description "ERROR - 400: Get a group by id (Invalid JSON structure)"
        request {
            method GET()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440100")
        }
        response {
            status INTERNAL_SERVER_ERROR()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "status": "INTERNAL_SERVER_ERROR",
                "message": "Criteria deserialization exception",
                "details": "Could not resolve subtype of [simple type, class org.oran.smo.teiv.api.model.OranTeivCriteria]: missing type id property 'queryType'\\n"
            }''')
            bodyMatchers {
                jsonPath('$.status', byEquality())
                jsonPath('$.message', byEquality())
                jsonPath('$.details', byRegex("Could not resolve subtype of \\[simple type, class org\\.oran\\.smo\\.teiv\\.api\\.model\\.OranTeivCriteria\\]: missing type id property 'queryType'[\\s\\S]*"))
            }
        }
    },
    Contract.make {
        description "ERROR - 400: Invalid endpoint"
        request {
            method GET()
            url("/topology-inventory/v1/get-groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201")
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "No static resource topology-inventory/v1/get-groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201.",
                "details": null
            }''')
        }
    },
    Contract.make {
        description "ERROR - 404: Get a group by id that does not exists"
        request {
            method GET()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=non-existing-group")
        }
        response {
            status NOT_FOUND()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "status": "NOT_FOUND",
                "message": "Resource Not Found",
                "details": "The requested group is not found"
            }''')
        }
    }
]
