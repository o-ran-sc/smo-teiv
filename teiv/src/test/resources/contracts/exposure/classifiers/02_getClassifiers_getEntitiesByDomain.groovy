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
package contracts.classifier

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "SUCCESS - 200: Get classifiers using getEntitiesByDomain - EQUALS"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entities?targetFilter=/ODUFunction/classifiers&scopeFilter=/ODUFunction/classifiers[@item='test-app-module:Rural']"
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
                                "classifiers": [
                                    "test-app-module:Rural",
                                    "test-app-module:Weekend"
                                ],
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,ODUFunction=14"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "classifiers": [
                                    "test-app-module:Indoor",
                                    "test-app-module:Rural",
                                    "test-app-module:Weekend"
                                ],
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,ODUFunction=16"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/ODUFunction/classifiers&scopeFilter=/ODUFunction/classifiers[@item='test-app-module:Rural']"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/ODUFunction/classifiers&scopeFilter=/ODUFunction/classifiers[@item='test-app-module:Rural']"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/ODUFunction/classifiers&scopeFilter=/ODUFunction/classifiers[@item='test-app-module:Rural']"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/ODUFunction/classifiers&scopeFilter=/ODUFunction/classifiers[@item='test-app-module:Rural']"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/ODUFunction/classifiers&scopeFilter=/ODUFunction/classifiers[@item='test-app-module:Rural']"
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
        description "SUCCESS - 200: Get classifiers using getEntitiesByDomain - EQUALS"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entities?targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural']"
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
                                "classifiers": [
                                    "test-app-module:Rural"
                                ],
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=93"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "classifiers": [
                                    "test-app-module:Rural",
                                    "test-app-module:Weekend"
                                ],
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,ODUFunction=14"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "classifiers": [
                                    "test-app-module:Indoor",
                                    "test-app-module:Rural",
                                    "test-app-module:Weekend"
                                ],
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,ODUFunction=16"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural']"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural']"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural']"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural']"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural']"
                },
                "totalCount": 3
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(3)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get classifiers using getEntitiesByDomain - EQUALS"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entities?targetFilter=/classifiers&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural']"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural']"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural']"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural']"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural']"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural']"
                },
                "totalCount": 1
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(1)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
            }
        }
    }
]
