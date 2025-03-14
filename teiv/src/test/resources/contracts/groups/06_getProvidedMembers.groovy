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
        description "SUCCESS - 200: Get all provided members of a static group"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?offset=0&limit=10")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body ('''{
                "items": [
                    {
                        "invalid-module:InvalidTopology": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9"
                            }
                        ]
                    },
                    {
                        "invalid-module:InvalidTopology:InvalidString": [
                            {
                                "id": "urn:3gpp:dn:providedMemberId"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-oam:ManagedElement": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-oam:ManagedElement": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-oam:ManagedElement": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=500"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=500"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=91"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=92"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=4E40BE000AFEA418CE1B9ED9E21D38DA51772175BD498BE825D9EA362F9B7393C36AB72F6FDEE702439143D578268A2E84719A9352C8EA70F847B7B7664E047C"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=78ECC09D4832328976EF0F9C19699EE05D98E3837368D386AE39AD027543494AC620086BD2A7403DACFAA7B474B3DEBD313E0906F1EDE7FA2B584E16542A706A"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?offset=0&limit=10"
                },
                "first": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?offset=0&limit=10"
                },
                "prev": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?offset=0&limit=10"
                },
                "next": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?offset=10&limit=10"
                },
                "last": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?offset=10&limit=10"
                },
                "totalCount": 14
            }'''        )
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(10)
                })
                jsonPath('$.items[0].invalid-module:InvalidTopology[0].id', byEquality())
                jsonPath('$.items[1].invalid-module:InvalidTopology:InvalidString[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-oam:ManagedElement[0].id', byEquality())
                jsonPath('$.items[3].o-ran-smo-teiv-oam:ManagedElement[0].id', byEquality())
                jsonPath('$.items[4].o-ran-smo-teiv-oam:ManagedElement[0].id', byEquality())
                jsonPath('$.items[5].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[6].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[7].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[8].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[9].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all provided members of a static group when status is present"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?status=present&offset=0&limit=10")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body ('''{
                "items": [
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-oam:ManagedElement": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-oam:ManagedElement": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=91"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=92"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_ANTENNACAPABILITY": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_SERVES_ANTENNACAPABILITY=ABD52B030DF1169F9F41C898913EF30F7BB5741F53352F482310B280C90AC569B7D31D52A2BB41F1F0099AE1EDD56CACF0B285D145A5584D376DD45DED1E2D65"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=4E40BE000AFEA418CE1B9ED9E21D38DA51772175BD498BE825D9EA362F9B7393C36AB72F6FDEE702439143D578268A2E84719A9352C8EA70F847B7B7664E047C"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=78ECC09D4832328976EF0F9C19699EE05D98E3837368D386AE39AD027543494AC620086BD2A7403DACFAA7B474B3DEBD313E0906F1EDE7FA2B584E16542A706A"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?offset=0&limit=10&status=present"
                },
                "first": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?offset=0&limit=10&status=present"
                },
                "prev": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?offset=0&limit=10&status=present"
                },
                "next": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?offset=0&limit=10&status=present"
                },
                "last": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?offset=0&limit=10&status=present"
                },
                "totalCount": 9
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(9)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-oam:ManagedElement[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-oam:ManagedElement[0].id', byEquality())
                jsonPath('$.items[3].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[4].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[5].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[6].o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_ANTENNACAPABILITY[0].id', byEquality())
                jsonPath('$.items[7].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[8].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get provided members of a static group when status is not-present"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?status=not-present&offset=0&limit=10")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body ('''{
                "items": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=500"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-oam:ManagedElement": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=500"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_ANTENNACAPABILITY": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_SERVES_ANTENNACAPABILITY=ABD52B030DF1169F9F41C898913EF30F7BB5741F53352F482310B280C90AC569B7D31D52A2BB41F1F0099AE1EDD56CACF0B285D145A5584D376DD45DEDUMMYID"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?offset=0&limit=10&status=not-present"
                },
                "first": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?offset=0&limit=10&status=not-present"
                },
                "prev": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?offset=0&limit=10&status=not-present"
                },
                "next": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?offset=0&limit=10&status=not-present"
                },
                "last": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?offset=0&limit=10&status=not-present"
                },
                "totalCount": 3
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(3)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-oam:ManagedElement[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_ANTENNACAPABILITY[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get provided members of a static group when status is invalid"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?status=invalid&offset=0&limit=10")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body ('''{
                "items": [
                    {
                        "invalid-module:InvalidTopology": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9"
                            }
                        ]
                    },
                    {
                        "invalid-module:InvalidTopology:InvalidString": [
                            {
                                "id": "urn:3gpp:dn:providedMemberId"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?offset=0&limit=10&status=invalid"
                },
                "first": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?offset=0&limit=10&status=invalid"
                },
                "prev": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?offset=0&limit=10&status=invalid"
                },
                "next": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?offset=0&limit=10&status=invalid"
                },
                "last": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?offset=0&limit=10&status=invalid"
                },
                "totalCount": 2
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].invalid-module:InvalidTopology[0].id', byEquality())
                jsonPath('$.items[1].invalid-module:InvalidTopology:InvalidString[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get provided members by groupId that does not have any provided members"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440391/provided-members?offset=0&limit=10")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body ('''{
                "items": [],
                "self": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440391/provided-members?offset=0&limit=10"
                },
                "first": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440391/provided-members?offset=0&limit=10"
                },
                "prev": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440391/provided-members?offset=0&limit=10"
                },
                "next": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440391/provided-members?offset=0&limit=10"
                },
                "last": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440391/provided-members?offset=0&limit=10"
                },
                "totalCount": 0
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(0)
                })
            }
        }
    },
    Contract.make {
        description "ERROR - 400: Get the provided members of a static group with wrong status"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/provided-members?status=absent&offset=0&limit=10")
        }
        response {
            status BAD_REQUEST()
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid status",
                "details": "Unsupported status for provided members: absent"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 404: Get the provided members of a group that does not exists"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/groups/non-existing-group/provided-members")
        }
        response {
            status NOT_FOUND()
            body('''{
                "status": "NOT_FOUND",
                "message": "Resource Not Found",
                "details": "The requested group is not found"
            }''')
        }
    }
]
