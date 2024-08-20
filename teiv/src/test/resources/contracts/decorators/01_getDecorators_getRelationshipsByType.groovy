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
        description "SUCCESS - 200: Get decorators using getRelationshipsByType - EQUALS"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[@test-app-module:textdata = 'Stockholm']"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 123
                                },
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION=6F7BFBD3DA2A9A592084C75242210A33C9DCF10CFCD53B761A6ACCD385132921679EC3C16394A4DEEE5883712C9719511388230151BA84FBF209DFCFB639E2EA"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 123
                                },
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION=D67357F682531C7B068486313B0FDAC3E719A166229520196FB9CE917E0236754226A5BCBF7BB7240E516D7ED3FEA852855EC3F121DD4BAFEC5646F2A37F57EE"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 456
                                },
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION=DD9259A1B57FF2BB9DEC77C29DBFA4A5C49960D80622F603809ACA47E786DDD5C7ABD267D554A7C796477A9B2E02E072A8E682E4ED38F331BFB6DC3827CE4DB7"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/decorators[@test-app-module:textdata = 'Stockholm']&targetFilter=/decorators"
                },
                "first": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/decorators[@test-app-module:textdata = 'Stockholm']&targetFilter=/decorators"
                },
                "prev": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/decorators[@test-app-module:textdata = 'Stockholm']&targetFilter=/decorators"
                },
                "next": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/decorators[@test-app-module:textdata = 'Stockholm']&targetFilter=/decorators"
                },
                "last": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/decorators[@test-app-module:textdata = 'Stockholm']&targetFilter=/decorators"
                },
                "totalCount": 3
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(3)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get decorators using getRelationshipsByType - CONTAINS"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')]"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 123
                                },
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION=6F7BFBD3DA2A9A592084C75242210A33C9DCF10CFCD53B761A6ACCD385132921679EC3C16394A4DEEE5883712C9719511388230151BA84FBF209DFCFB639E2EA"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 123
                                },
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION=D67357F682531C7B068486313B0FDAC3E719A166229520196FB9CE917E0236754226A5BCBF7BB7240E516D7ED3FEA852855EC3F121DD4BAFEC5646F2A37F57EE"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 456
                                },
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION=DD9259A1B57FF2BB9DEC77C29DBFA4A5C49960D80622F603809ACA47E786DDD5C7ABD267D554A7C796477A9B2E02E072A8E682E4ED38F331BFB6DC3827CE4DB7"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')]&targetFilter=/decorators"
                },
                "first": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')]&targetFilter=/decorators"
                },
                "prev": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')]&targetFilter=/decorators"
                },
                "next": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')]&targetFilter=/decorators"
                },
                "last": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')]&targetFilter=/decorators"
                },
                "totalCount": 3
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(3)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get decorators using getRelationshipsByType - CONTAINS and EQUALS"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')] ; /decorators[@test-app-module:intdata = 123]"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 123
                                },
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION=6F7BFBD3DA2A9A592084C75242210A33C9DCF10CFCD53B761A6ACCD385132921679EC3C16394A4DEEE5883712C9719511388230151BA84FBF209DFCFB639E2EA"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION": [
                            {
                                "decorators": {
                                    "test-app-module:textdata": "Stockholm",
                                    "test-app-module:intdata": 123
                                },
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION=D67357F682531C7B068486313B0FDAC3E719A166229520196FB9CE917E0236754226A5BCBF7BB7240E516D7ED3FEA852855EC3F121DD4BAFEC5646F2A37F57EE"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')] ; /decorators[@test-app-module:intdata = 123]&targetFilter=/decorators"
                },
                "first": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')] ; /decorators[@test-app-module:intdata = 123]&targetFilter=/decorators"
                },
                "prev": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')] ; /decorators[@test-app-module:intdata = 123]&targetFilter=/decorators"
                },
                "next": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')] ; /decorators[@test-app-module:intdata = 123]&targetFilter=/decorators"
                },
                "last": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500&scopeFilter=/decorators[contains(@test-app-module:textdata, 'Stock')] ; /decorators[@test-app-module:intdata = 123]&targetFilter=/decorators"
                },
                "totalCount": 2
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].id', byEquality())
            }
        }
    }
]
