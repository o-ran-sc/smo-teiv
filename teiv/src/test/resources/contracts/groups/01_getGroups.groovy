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
        description "SUCCESS - 200: Get all groups"
        request {
            method GET()
            url("/topology-inventory/v1/groups?offset=0&limit=2")
            headers {
                accept("application/json")
            }
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            // Note: The totalCount in the response includes the groups created in createGroup contract.
            body ('''{
                    "items": [
                        {
                            "id":"urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440050",
                            "name":"test-dynamic-group-for-rename",
                            "type":"dynamic",
                            "members":{
                                "href":"/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440050/members"
                            }
                        },
                        {
                            "id":"urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440051",
                            "name":"test-static-group-for-rename",
                            "type":"static",
                            "members":{
                                "href":"/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440051/members"
                            }
                        },
                        {
                            "id":"urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440000",
                            "name":"test-dynamic-group-for-delete",
                            "type":"dynamic",
                            "members":{
                                "href":"/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440000/members"
                            }
                        }
                    ],
                    "self": {
                        "href": "/groups?offset=0&limit=2"
                    },
                    "first": {
                        "href": "/groups?offset=0&limit=2"
                    },
                    "prev": {
                        "href": "/groups?offset=0&limit=2"
                    },
                    "next": {
                        "href": "/groups?offset=2&limit=2"
                    },
                    "last": {
                        "href": "/groups?offset=32&limit=2"
                    },
                    "totalCount": 33
                }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].id', byEquality())
                jsonPath('$.items[1].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all groups by group name filter (static group type)"
        request {
            method GET()
            url("/topology-inventory/v1/groups?offset=0&limit=2&name=test-group-get-by-name-static-group")
            headers {
                accept("application/json")
            }
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body ('''{
                    "items": [
                        {
                            "id":"urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440202",
                            "name":"test-group-get-by-name-static-group",
                            "type":"static",
                            "providedMembers": {
                                "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440202/provided-members"
                            },
                            "members":{
                                "href":"/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440202/members"
                            }
                        },
                        {
                            "id":"urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440203",
                            "name":"test-group-get-by-name-static-group",
                            "type":"static",
                            "providedMembers": {
                                "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440203/provided-members"
                            },
                            "members":{
                                "href":"/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440203/members"
                            }
                        }
                    ],
                    "self": {
                        "href": "/groups?offset=0&limit=2&name=test-group-get-by-name-static-group"
                    },
                    "first": {
                        "href": "/groups?offset=0&limit=2&name=test-group-get-by-name-static-group"
                    },
                    "prev": {
                        "href": "/groups?offset=0&limit=2&name=test-group-get-by-name-static-group"
                    },
                    "next": {
                        "href": "/groups?offset=0&limit=2&name=test-group-get-by-name-static-group"
                    },
                    "last": {
                        "href": "/groups?offset=0&limit=2&name=test-group-get-by-name-static-group"
                    },
                    "totalCount": 2
                }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].id', byEquality())
                jsonPath('$.items[0].name', byEquality())
                jsonPath('$.items[0].type', byEquality())
                jsonPath('$.items[0].providedMembers.href', byEquality())
                jsonPath('$.items[0].members.href', byEquality())
                jsonPath('$.items[1].id', byEquality())
                jsonPath('$.items[1].name', byEquality())
                jsonPath('$.items[1].type', byEquality())
                jsonPath('$.items[1].providedMembers.href', byEquality())
                jsonPath('$.items[1].members.href', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all groups by group name filter (dynamic group type)"
        request {
            method GET()
            url("/topology-inventory/v1/groups?offset=0&limit=2&name=test-group-get-entities-by-type")
            headers {
                accept("application/json")
            }
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body ('''{
                    "items": [
                        {
                            "id":"urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440001",
                            "name":"test-group-get-entities-by-type",
                            "type":"dynamic",
                            "members":{
                                "href":"/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440001/members"
                            }
                        }
                    ],
                    "self": {
                        "href": "/groups?offset=0&limit=2&name=test-group-get-entities-by-type"
                    },
                    "first": {
                        "href": "/groups?offset=0&limit=2&name=test-group-get-entities-by-type"
                    },
                    "prev": {
                        "href": "/groups?offset=0&limit=2&name=test-group-get-entities-by-type"
                    },
                    "next": {
                        "href": "/groups?offset=0&limit=2&name=test-group-get-entities-by-type"
                    },
                    "last": {
                        "href": "/groups?offset=0&limit=2&name=test-group-get-entities-by-type"
                    },
                    "totalCount": 1
                }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(1)
                })
                jsonPath('$.items[0].id', byEquality())
                jsonPath('$.items[0].name', byEquality())
                jsonPath('$.items[0].type', byEquality())
                jsonPath('$.items[0].members.href', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get group by name that does not exist"
        request {
            method GET()
            url("/topology-inventory/v1/groups?offset=0&limit=2&name=non-existing-group")
            headers {
                accept("application/json")
            }
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body ('''{
                    "items": [],
                    "self": {
                        "href": "/groups?offset=0&limit=2&name=non-existing-group"
                    },
                    "first": {
                        "href": "/groups?offset=0&limit=2&name=non-existing-group"
                    },
                    "prev": {
                        "href": "/groups?offset=0&limit=2&name=non-existing-group"
                    },
                    "next": {
                        "href": "/groups?offset=0&limit=2&name=non-existing-group"
                    },
                    "last": {
                        "href": "/groups?offset=0&limit=2&name=non-existing-group"
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
        description "ERROR - 400: Bad request when invalid parameters are sent"
        request {
            method GET()
            url("/topology-inventory/v1/groups?offset=0&limit=561")
            headers {
                accept("application/json")
            }
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
                "detail": "getAllGroups.limit: must be less than or equal to 500",
                "instance": ""
                }''')
        }
    }
]
