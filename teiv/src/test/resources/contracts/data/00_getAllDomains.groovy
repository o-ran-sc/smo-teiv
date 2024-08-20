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
package contracts.data

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "SUCCESS - 200: Get all the available topology domains."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "name": "EQUIPMENT",
                        "entityTypes": {
                            "href": "/domains/EQUIPMENT/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/EQUIPMENT/relationship-types"
                        }
                    },
                    {
                        "name": "OAM",
                        "entityTypes": {
                            "href": "/domains/OAM/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/OAM/relationship-types"
                        }
                    },
                    {
                        "name": "RAN",
                        "entityTypes": {
                            "href": "/domains/RAN/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/RAN/relationship-types"
                        }
                    },
                    {
                        "name": "REL_EQUIPMENT_RAN",
                        "entityTypes": {
                            "href": "/domains/REL_EQUIPMENT_RAN/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types"
                        }
                    },
                    {
                        "name": "REL_OAM_RAN",
                        "entityTypes": {
                            "href": "/domains/REL_OAM_RAN/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/REL_OAM_RAN/relationship-types"
                        }
                    },
                    {
                        "name": "TEIV",
                        "entityTypes": {
                            "href": "/domains/TEIV/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/TEIV/relationship-types"
                        }
                    },
                    {
                        "name": "TEST",
                        "entityTypes": {
                            "href": "/domains/TEST/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/TEST/relationship-types"
                        }
                    }
                ],
                "self": {
                    "href": "/domains?offset=0&limit=500"
                },
                "first": {
                    "href": "/domains?offset=0&limit=500"
                },
                "prev": {
                    "href": "/domains?offset=0&limit=500"
                },
                "next": {
                    "href": "/domains?offset=0&limit=500"
                },
                "last": {
                    "href": "/domains?offset=0&limit=500"
                },
                "totalCount": 7
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(7)
                })
                jsonPath('$.items[0].name', byEquality())
                jsonPath('$.items[0].entityTypes.href', byEquality())
                jsonPath('$.items[0].relationshipTypes.href', byEquality())
                jsonPath('$.items[1].name', byEquality())
                jsonPath('$.items[1].entityTypes.href', byEquality())
                jsonPath('$.items[1].relationshipTypes.href', byEquality())
                jsonPath('$.items[2].name', byEquality())
                jsonPath('$.items[2].entityTypes.href', byEquality())
                jsonPath('$.items[2].relationshipTypes.href', byEquality())
                jsonPath('$.items[3].name', byEquality())
                jsonPath('$.items[3].entityTypes.href', byEquality())
                jsonPath('$.items[3].relationshipTypes.href', byEquality())
                jsonPath('$.items[4].name', byEquality())
                jsonPath('$.items[4].entityTypes.href', byEquality())
                jsonPath('$.items[4].relationshipTypes.href', byEquality())
                jsonPath('$.items[5].name', byEquality())
                jsonPath('$.items[5].entityTypes.href', byEquality())
                jsonPath('$.items[5].relationshipTypes.href', byEquality())
                jsonPath('$.items[6].name', byEquality())
                jsonPath('$.items[6].entityTypes.href', byEquality())
                jsonPath('$.items[6].relationshipTypes.href', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all the available topology domains with offset as 2 and limit as 3."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains?offset=2&limit=3"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "name": "RAN",
                        "entityTypes": {
                            "href": "/domains/RAN/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/RAN/relationship-types"
                        }
                    },
                    {
                        "name": "REL_EQUIPMENT_RAN",
                        "entityTypes": {
                            "href": "/domains/REL_EQUIPMENT_RAN/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types"
                        }
                    },
                    {
                        "name": "REL_OAM_RAN",
                        "entityTypes": {
                            "href": "/domains/REL_OAM_RAN/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/REL_OAM_RAN/relationship-types"
                        }
                    }
                ],
                "self": {
                    "href": "/domains?offset=2&limit=3"
                },
                "first": {
                    "href": "/domains?offset=0&limit=3"
                },
                "prev": {
                    "href": "/domains?offset=0&limit=3"
                },
                "next": {
                    "href": "/domains?offset=5&limit=3"
                },
                "last": {
                    "href": "/domains?offset=5&limit=3"
                },
                "totalCount": 7
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(3)
                })
                jsonPath('$.items[0].name', byEquality())
                jsonPath('$.items[0].entityTypes.href', byEquality())
                jsonPath('$.items[0].relationshipTypes.href', byEquality())
                jsonPath('$.items[1].name', byEquality())
                jsonPath('$.items[1].entityTypes.href', byEquality())
                jsonPath('$.items[1].relationshipTypes.href', byEquality())
                jsonPath('$.items[2].name', byEquality())
                jsonPath('$.items[2].entityTypes.href', byEquality())
                jsonPath('$.items[2].relationshipTypes.href', byEquality())
            }
        }
    },
    Contract.make {
        description "ERROR - 400: Get all the available topology domains with invalid offset (greater than total count)."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains?offset=100"
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid Value",
                "details": "Offset cannot be larger than 6"
            }''')
        }
    }
]
