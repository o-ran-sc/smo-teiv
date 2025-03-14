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
        description "SUCCESS - 200: Get classifiers using getRelationshipsByType - EQUALS"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural']"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
              	"items": [
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10",
                                "classifiers": [
                                    "test-app-module:Rural",
                                    "test-app-module:Weekend"
                                ],
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=661A89AD3C2702233CD9E96E97E738C05C35EC5FDF32DC78D149B773726350067315B72448D004C938BCD0263F0C4BCCC8A5F9CDD145B9B740983D1523664328"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,ODUFunction=16",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16",
                                "classifiers": [
                                    "test-app-module:Indoor",
                                    "test-app-module:Rural",
                                    "test-app-module:Weekend"
                                ],
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=D67357F682531C7B068486313B0FDAC3E719A166229520196FB9CE917E0236754226A5BCBF7BB7240E516D7ED3FEA852855EC3F121DD4BAFEC5646F2A37F57EE"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[@item='test-app-module:Rural']&targetFilter=/classifiers"
                },
                "first": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[@item='test-app-module:Rural']&targetFilter=/classifiers"
                },
                "prev": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[@item='test-app-module:Rural']&targetFilter=/classifiers"
                },
                "next": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[@item='test-app-module:Rural']&targetFilter=/classifiers"
                },
                "last": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[@item='test-app-module:Rural']&targetFilter=/classifiers"
                },
                "totalCount": 2
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get classifiers using getRelationshipsByType - EQUALS(AND)"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Rural' and @item='test-app-module:Weekend']"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10",
                                "classifiers": [
                                    "test-app-module:Rural",
                                    "test-app-module:Weekend"
                                ],
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=661A89AD3C2702233CD9E96E97E738C05C35EC5FDF32DC78D149B773726350067315B72448D004C938BCD0263F0C4BCCC8A5F9CDD145B9B740983D1523664328"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,ODUFunction=16",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16",
                                "classifiers": [
                                    "test-app-module:Indoor",
                                    "test-app-module:Rural",
                                    "test-app-module:Weekend"
                                ],
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=D67357F682531C7B068486313B0FDAC3E719A166229520196FB9CE917E0236754226A5BCBF7BB7240E516D7ED3FEA852855EC3F121DD4BAFEC5646F2A37F57EE"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[@item='test-app-module:Rural' and @item='test-app-module:Weekend']&targetFilter=/classifiers"
                },
                "first": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[@item='test-app-module:Rural' and @item='test-app-module:Weekend']&targetFilter=/classifiers"
                },
                "prev": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[@item='test-app-module:Rural' and @item='test-app-module:Weekend']&targetFilter=/classifiers"
                },
                "next": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[@item='test-app-module:Rural' and @item='test-app-module:Weekend']&targetFilter=/classifiers"
                },
                "last": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[@item='test-app-module:Rural' and @item='test-app-module:Weekend']&targetFilter=/classifiers"
                },
                "totalCount": 2
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get classifiers using getRelationshipsByType - CONTAINS"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[contains(@item,'ural')]"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10",
                                "classifiers": [
                                    "test-app-module:Rural",
                                    "test-app-module:Weekend"
                                ],
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=661A89AD3C2702233CD9E96E97E738C05C35EC5FDF32DC78D149B773726350067315B72448D004C938BCD0263F0C4BCCC8A5F9CDD145B9B740983D1523664328"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,ODUFunction=16",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16",
                                "classifiers": [
                                    "module-x:Indoor",
                                    "module-y:Rural",
                                    "module-z:Weekend"
                                ],
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=D67357F682531C7B068486313B0FDAC3E719A166229520196FB9CE917E0236754226A5BCBF7BB7240E516D7ED3FEA852855EC3F121DD4BAFEC5646F2A37F57EE"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[contains(@item,'ural')]&targetFilter=/classifiers"
                },
                "first": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[contains(@item,'ural')]&targetFilter=/classifiers"
                },
                "prev": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[contains(@item,'ural')]&targetFilter=/classifiers"
                },
                "next": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[contains(@item,'ural')]&targetFilter=/classifiers"
                },
                "last": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/classifiers[contains(@item,'ural')]&targetFilter=/classifiers"
                },
                "totalCount": 2
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get classifiers using getRelationshipsByType - CONTAINS"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[contains(@item,'NOT_EXISTING')]"
        }
        response {
            status NOT_FOUND()
            headers {
                contentType('application/json')
            }
            body('''{
                    "status": "NOT_FOUND",
                    "message": "Invalid classifiers",
                    "details": "The provided classifiers are invalid [NOT_EXISTING]"
                }''')
            bodyMatchers {
                jsonPath('$.status', byEquality())
                jsonPath('$.message', byEquality())
                jsonPath('$.details', byEquality())
            }
        }
    },
]
