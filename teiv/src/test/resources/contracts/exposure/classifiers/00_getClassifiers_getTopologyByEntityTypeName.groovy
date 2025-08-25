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
package contracts.classifiers

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "SUCCESS - 200: Get all topology entities of type ODUFunction with classifiers in scopeFilter."
        request {
            method GET()
            url "topology-inventory/v1/domains/RAN/entity-types/ODUFunction/entities?targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural']"
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
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural']"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural']"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural']"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural']"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural']"
                },
                "totalCount": 2
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[0]', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[1]', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[0]', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[1]', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[2]', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all topology entities of type ODUFunction with classifiers in scopeFilter(OR)"
        request {
            method GET()
            url "topology-inventory/v1/domains/RAN/entity-types/ODUFunction/entities?targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural'] | /classifiers[@item='test-app-module:Indoor']"
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
                                    "test-app-module:Indoor"
                                ],
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,ODUFunction=13"
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
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "classifiers": [
                                    "test-app-module:Indoor",
                                    "test-app-module:Weekend"
                                ],
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural'] | /classifiers[@item='test-app-module:Indoor']"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural'] | /classifiers[@item='test-app-module:Indoor']"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural'] | /classifiers[@item='test-app-module:Indoor']"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural'] | /classifiers[@item='test-app-module:Indoor']"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural'] | /classifiers[@item='test-app-module:Indoor']"
                },
                "totalCount": 4
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(4)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[0]', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[0]', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[1]', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[0]', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[1]', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[2]', byEquality())
                jsonPath('$.items[3].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[3].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[0]', byEquality())
                jsonPath('$.items[3].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[1]', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all topology entities of type ODUFunction with classifiers in scopeFilter.(AND)"
        request {
            method GET()
            url "topology-inventory/v1/domains/RAN/entity-types/ODUFunction/entities?targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural' and @item='test-app-module:Weekend']"
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
                "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural' and @item='test-app-module:Weekend']"
            },
            "first": {
                "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural' and @item='test-app-module:Weekend']"
            },
            "prev": {
                "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural' and @item='test-app-module:Weekend']"
            },
            "next": {
                "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural' and @item='test-app-module:Weekend']"
            },
            "last": {
                "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural' and @item='test-app-module:Weekend']"
            },
            "totalCount": 2
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[0]', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[1]', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[0]', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[1]', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[2]', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all topology entities of type ODUFunction with classifiers in scopeFilter.(Contains)"
        request {
            method GET()
            url "topology-inventory/v1/domains/RAN/entity-types/ODUFunction/entities?targetFilter=/classifiers&scopeFilter=/classifiers[contains(@item,'app-module:Rural')]"
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
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[contains(@item,'app-module:Rural')]"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[contains(@item,'app-module:Rural')]"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[contains(@item,'app-module:Rural')]"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[contains(@item,'app-module:Rural')]"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[contains(@item,'app-module:Rural')]"
                },
                "totalCount": 2
                }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[0]', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[1]', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[0]', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[1]', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFunction[0].classifiers[2]', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all topology entities of type ODUFunction with classifiers in scopeFilter."
        request {
            method GET()
            url "topology-inventory/v1/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes(gNBId)&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural']"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19",
                                "attributes": {
                                    "gNBId" : 19
                                }
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes(gNBId)&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural']"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes(gNBId)&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural']"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes(gNBId)&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural']"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes(gNBId)&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural']"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes(gNBId)&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural']"
                },
                "totalCount": 1
                }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(1)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].attributes.gNBId', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all topology entities of type ODUFunction with classifiers in scopeFilter."
        request {
            method GET()
            url "topology-inventory/v1/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes(gNBId)&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural'] | /provided-nrCellDu/classifiers[@item='test-app-module:Indoor']"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19",
                                "attributes": {
                                    "gNBId" : 19
                                }
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes(gNBId)&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural'] | /provided-nrCellDu/classifiers[@item='test-app-module:Indoor']"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes(gNBId)&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural'] | /provided-nrCellDu/classifiers[@item='test-app-module:Indoor']"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes(gNBId)&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural'] | /provided-nrCellDu/classifiers[@item='test-app-module:Indoor']"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes(gNBId)&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural'] | /provided-nrCellDu/classifiers[@item='test-app-module:Indoor']"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes(gNBId)&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural'] | /provided-nrCellDu/classifiers[@item='test-app-module:Indoor']"
                },
                "totalCount": 1
                }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(1)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].attributes.gNBId', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all topology entities of type ODUFunction with classifiers in scopeFilter."
        request {
            method GET()
            url "topology-inventory/v1/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes(gNBId)&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural' and @item='test-app-module:Weekend']"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19",
                                "attributes": {
                                    "gNBId" : 19
                                }
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes(gNBId)&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural' and @item='test-app-module:Weekend']"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes(gNBId)&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural' and @item='test-app-module:Weekend']"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes(gNBId)&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural' and @item='test-app-module:Weekend']"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes(gNBId)&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural' and @item='test-app-module:Weekend']"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes(gNBId)&scopeFilter=/provided-nrCellDu/classifiers[@item='test-app-module:Rural' and @item='test-app-module:Weekend']"
                },
                "totalCount": 1
                }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(1)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].attributes.gNBId', byEquality())
            }
        }
    }
]
