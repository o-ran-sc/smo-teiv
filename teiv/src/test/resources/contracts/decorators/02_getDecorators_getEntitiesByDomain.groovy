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
package contracts.decorators

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "SUCCESS - 200: Get decorators using getEntitiesByDomain - EQUALS"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/decorators&scopeFilter=/GNBDUFunction/decorators[@test-app-module:intdata=123]"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-ran:GNBDUFunction": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Budapest",
                                    "test-app-module:intdata": 123
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,GNBDUFunction=10"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBDUFunction": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 123
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,GNBDUFunction=14"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBDUFunction": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 123
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,GNBDUFunction=16"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/decorators&scopeFilter=/GNBDUFunction/decorators[@test-app-module:intdata=123]"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/decorators&scopeFilter=/GNBDUFunction/decorators[@test-app-module:intdata=123]"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/decorators&scopeFilter=/GNBDUFunction/decorators[@test-app-module:intdata=123]"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/decorators&scopeFilter=/GNBDUFunction/decorators[@test-app-module:intdata=123]"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/decorators&scopeFilter=/GNBDUFunction/decorators[@test-app-module:intdata=123]"
                },
                "totalCount": 3
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(3)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:GNBDUFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:GNBDUFunction[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:GNBDUFunction[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get decorators using getEntitiesByDomain - CONTAINS"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/decorators&scopeFilter=/GNBDUFunction/decorators[contains(@test-app-module:textdata,'Stock')]"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-ran:GNBDUFunction": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 456
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,GNBDUFunction=13"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBDUFunction": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 123
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,GNBDUFunction=14"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBDUFunction": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 123
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,GNBDUFunction=16"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/decorators&scopeFilter=/GNBDUFunction/decorators[contains(@test-app-module:textdata,'Stock')]"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/decorators&scopeFilter=/GNBDUFunction/decorators[contains(@test-app-module:textdata,'Stock')]"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/decorators&scopeFilter=/GNBDUFunction/decorators[contains(@test-app-module:textdata,'Stock')]"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/decorators&scopeFilter=/GNBDUFunction/decorators[contains(@test-app-module:textdata,'Stock')]"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/decorators&scopeFilter=/GNBDUFunction/decorators[contains(@test-app-module:textdata,'Stock')]"
                },
                "totalCount": 3
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(3)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:GNBDUFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:GNBDUFunction[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:GNBDUFunction[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get decorators using getEntitiesByDomain - EQUALS and CONTAINS"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/decorators&scopeFilter=/GNBDUFunction/decorators[@test-app-module:intdata=123];/GNBDUFunction/decorators[contains(@test-app-module:textdata,'Stock')]"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-ran:GNBDUFunction": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 123
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,GNBDUFunction=14"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBDUFunction": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 123
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,GNBDUFunction=16"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/decorators&scopeFilter=/GNBDUFunction/decorators[@test-app-module:intdata=123];/GNBDUFunction/decorators[contains(@test-app-module:textdata,'Stock')]"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/decorators&scopeFilter=/GNBDUFunction/decorators[@test-app-module:intdata=123];/GNBDUFunction/decorators[contains(@test-app-module:textdata,'Stock')]"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/decorators&scopeFilter=/GNBDUFunction/decorators[@test-app-module:intdata=123];/GNBDUFunction/decorators[contains(@test-app-module:textdata,'Stock')]"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/decorators&scopeFilter=/GNBDUFunction/decorators[@test-app-module:intdata=123];/GNBDUFunction/decorators[contains(@test-app-module:textdata,'Stock')]"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/decorators&scopeFilter=/GNBDUFunction/decorators[@test-app-module:intdata=123];/GNBDUFunction/decorators[contains(@test-app-module:textdata,'Stock')]"
                },
                "totalCount": 2
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:GNBDUFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:GNBDUFunction[0].id', byEquality())
            }
        }
    }
]
