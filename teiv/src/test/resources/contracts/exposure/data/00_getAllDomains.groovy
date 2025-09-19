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
package contracts.data

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "SUCCESS - 200: Get all the available topology domains."
        request {
            method GET()
            url "/topology-inventory/v1/domains"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "domainName": "CLOUD",
                        "entityTypes": {
                            "href": "/domains/CLOUD/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/CLOUD/relationship-types"
                        }
                    },
                    {
                        "domainName": "EQUIPMENT",
                        "entityTypes": {
                            "href": "/domains/EQUIPMENT/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/EQUIPMENT/relationship-types"
                        }
                    },
                    {
                        "domainName": "OAM",
                        "entityTypes": {
                            "href": "/domains/OAM/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/OAM/relationship-types"
                        }
                    },
                    {
                        "domainName": "PHYSICAL",
                        "entityTypes": {
                            "href": "/domains/PHYSICAL/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/PHYSICAL/relationship-types"
                        }
                    },
                    {
                        "domainName": "RAN",
                        "entityTypes": {
                            "href": "/domains/RAN/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/RAN/relationship-types"
                        }
                    },
                    {
                        "domainName": "REL_CLOUD_RAN",
                        "entityTypes": {
                            "href": "/domains/REL_CLOUD_RAN/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/REL_CLOUD_RAN/relationship-types"
                        }
                    },
                    {
                        "domainName": "REL_EQUIPMENT_RAN",
                        "entityTypes": {
                            "href": "/domains/REL_EQUIPMENT_RAN/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types"
                        }
                    },
                    {
                        "domainName": "REL_OAM_CLOUD",
                        "entityTypes": {
                            "href": "/domains/REL_OAM_CLOUD/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/REL_OAM_CLOUD/relationship-types"
                        }
                    },
                    {
                        "domainName": "REL_OAM_RAN",
                        "entityTypes": {
                            "href": "/domains/REL_OAM_RAN/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/REL_OAM_RAN/relationship-types"
                        }
                    },
                    {
                        "domainName": "REL_PHYSICAL_RAN",
                        "entityTypes": {
                            "href": "/domains/REL_PHYSICAL_RAN/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/REL_PHYSICAL_RAN/relationship-types"
                        }
                    },
                    {
                        "domainName": "TEIV",
                        "entityTypes": {
                            "href": "/domains/TEIV/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/TEIV/relationship-types"
                        }
                    },
                    {
                        "domainName": "TEST",
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
                "totalCount": 12
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(12)
                })
                jsonPath('$.items[0].domainName', byEquality())
                jsonPath('$.items[0].entityTypes.href', byEquality())
                jsonPath('$.items[0].relationshipTypes.href', byEquality())
                jsonPath('$.items[1].domainName', byEquality())
                jsonPath('$.items[1].entityTypes.href', byEquality())
                jsonPath('$.items[1].relationshipTypes.href', byEquality())
                jsonPath('$.items[2].domainName', byEquality())
                jsonPath('$.items[2].entityTypes.href', byEquality())
                jsonPath('$.items[2].relationshipTypes.href', byEquality())
                jsonPath('$.items[3].domainName', byEquality())
                jsonPath('$.items[3].entityTypes.href', byEquality())
                jsonPath('$.items[3].relationshipTypes.href', byEquality())
                jsonPath('$.items[4].domainName', byEquality())
                jsonPath('$.items[4].entityTypes.href', byEquality())
                jsonPath('$.items[4].relationshipTypes.href', byEquality())
                jsonPath('$.items[5].domainName', byEquality())
                jsonPath('$.items[5].entityTypes.href', byEquality())
                jsonPath('$.items[5].relationshipTypes.href', byEquality())
                jsonPath('$.items[6].domainName', byEquality())
                jsonPath('$.items[6].entityTypes.href', byEquality())
                jsonPath('$.items[6].relationshipTypes.href', byEquality())
                jsonPath('$.items[7].domainName', byEquality())
                jsonPath('$.items[7].entityTypes.href', byEquality())
                jsonPath('$.items[7].relationshipTypes.href', byEquality())
                jsonPath('$.items[8].domainName', byEquality())
                jsonPath('$.items[8].entityTypes.href', byEquality())
                jsonPath('$.items[8].relationshipTypes.href', byEquality())
                jsonPath('$.items[9].domainName', byEquality())
                jsonPath('$.items[9].entityTypes.href', byEquality())
                jsonPath('$.items[9].relationshipTypes.href', byEquality())
                jsonPath('$.items[10].domainName', byEquality())
                jsonPath('$.items[10].entityTypes.href', byEquality())
                jsonPath('$.items[10].relationshipTypes.href', byEquality())
                jsonPath('$.items[11].domainName', byEquality())
                jsonPath('$.items[11].entityTypes.href', byEquality())
                jsonPath('$.items[11].relationshipTypes.href', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all the available topology domains with offset as 2 and limit as 3."
        request {
            method GET()
            url "/topology-inventory/v1/domains?offset=2&limit=3"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "domainName": "OAM",
                        "entityTypes": {
                            "href": "/domains/OAM/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/OAM/relationship-types"
                        }
                    },
                    {
                        "domainName": "PHYSICAL",
                        "entityTypes": {
                            "href": "/domains/PHYSICAL/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/PHYSICAL/relationship-types"
                        }
                    },
                    {
                        "domainName": "RAN",
                        "entityTypes": {
                            "href": "/domains/RAN/entity-types"
                        },
                        "relationshipTypes": {
                            "href": "/domains/RAN/relationship-types"
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
                    "href": "/domains?offset=11&limit=3"
                },
                "totalCount": 12
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(3)
                })
                jsonPath('$.items[0].domainName', byEquality())
                jsonPath('$.items[0].entityTypes.href', byEquality())
                jsonPath('$.items[0].relationshipTypes.href', byEquality())
                jsonPath('$.items[1].domainName', byEquality())
                jsonPath('$.items[1].entityTypes.href', byEquality())
                jsonPath('$.items[1].relationshipTypes.href', byEquality())
                jsonPath('$.items[2].domainName', byEquality())
                jsonPath('$.items[2].entityTypes.href', byEquality())
                jsonPath('$.items[2].relationshipTypes.href', byEquality())
            }
        }
    },
    Contract.make {
        description "ERROR - 400: Get all the available topology domains with invalid offset (greater than total count)."
        request {
            method GET()
            url "/topology-inventory/v1/domains?offset=100"
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
                "detail": "Offset cannot be larger than 11",
                "instance": ""
            }''')
        }
    }
]
