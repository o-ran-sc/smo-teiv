/*
 *  ============LICENSE_START=======================================================
 *  Copyright (C) 2024 Ericsson
 *  Modifications Copyright (C) 2025 OpenInfra Foundation Europe
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
package contracts.decorators

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "SUCCESS - 200: Get decorators using getTopologyByEntityTypeName - EQUALS"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[@test-app-module:textdata = 'Stockholm']"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 456
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,ODUFunction=13"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 123
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,ODUFunction=14"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 123
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,ODUFunction=16"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[@test-app-module:textdata = 'Stockholm']"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[@test-app-module:textdata = 'Stockholm']"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[@test-app-module:textdata = 'Stockholm']"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[@test-app-module:textdata = 'Stockholm']"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[@test-app-module:textdata = 'Stockholm']"
                },
                "totalCount": 3
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(3)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get decorators using getTopologyByEntityTypeName - CONTAINS"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')]"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 456
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,ODUFunction=13"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 123
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,ODUFunction=14"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 123
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,ODUFunction=16"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')]"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')]"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')]"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')]"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')]"
                },
                "totalCount": 3
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(3)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get decorators using getTopologyByEntityTypeName - CONTAINS and EQUALS"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')] ; /decorators[@test-app-module:intdata = 123]"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 123
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,ODUFunction=14"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 123
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,ODUFunction=16"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')] ; /decorators[@test-app-module:intdata = 123]"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')] ; /decorators[@test-app-module:intdata = 123]"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')] ; /decorators[@test-app-module:intdata = 123]"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')] ; /decorators[@test-app-module:intdata = 123]"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')] ; /decorators[@test-app-module:intdata = 123]"
                },
                "totalCount": 2
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get decorators using association name - contains"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/NRCellDU/entities?offset=0&limit=100&scopeFilter=/provided-by-oduFunction/decorators[contains(@test-app-module:textdata, 'ORAN')]"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                            "o-ran-smo-teiv-ran:NRCellDU": [
                                {
                                    "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1"
                                }
                            ]
                    },
                    {
                            "o-ran-smo-teiv-ran:NRCellDU": [
                                {
                                    "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2"
                                }
                            ]
                    },
                    {
                            "o-ran-smo-teiv-ran:NRCellDU": [
                                {
                                    "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=3"
                                }
                            ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities?offset=0&limit=100&scopeFilter=/provided-by-oduFunction/decorators[contains(@test-app-module:textdata, 'ORAN')]"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities?offset=0&limit=100&scopeFilter=/provided-by-oduFunction/decorators[contains(@test-app-module:textdata, 'ORAN')]"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities?offset=0&limit=100&scopeFilter=/provided-by-oduFunction/decorators[contains(@test-app-module:textdata, 'ORAN')]"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities?offset=0&limit=100&scopeFilter=/provided-by-oduFunction/decorators[contains(@test-app-module:textdata, 'ORAN')]"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities?offset=0&limit=100&scopeFilter=/provided-by-oduFunction/decorators[contains(@test-app-module:textdata, 'ORAN')]"
                },
                "totalCount": 3
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(3)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
            }
        }
    }
]
