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
        description "SUCCESS - 200: Get entities in RAN domain with targetFilter=/ODUFunction/attributes(gNBDUId)."
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?offset=0&limit=100&targetFilter=/ODUFunction/attributes(gNBDUId)"
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
                                "attributes": {
                                    "gNBDUId": null
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "attributes": {
                                    "gNBDUId": null
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,ODUFunction=13"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "attributes": {
                                    "gNBDUId": null
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,ODUFunction=14"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "attributes": {
                                    "gNBDUId": 16
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,ODUFunction=16"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "attributes": {
                                    "gNBDUId": null
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "attributes": {
                                    "gNBDUId": null
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=28,ODUFunction=28"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "attributes": {
                                    "gNBDUId": null
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/ODUFunction/attributes(gNBDUId)"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/ODUFunction/attributes(gNBDUId)"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/ODUFunction/attributes(gNBDUId)"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/ODUFunction/attributes(gNBDUId)"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/ODUFunction/attributes(gNBDUId)"
                },
                "totalCount": 7
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(7)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].attributes.gNBDUId', byNull())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFunction[0].attributes.gNBDUId', byNull())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:ODUFunction[0].attributes.gNBDUId', byNull())
                jsonPath('$.items[3].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[3].o-ran-smo-teiv-ran:ODUFunction[0].attributes.gNBDUId', byRegex(anInteger()).asInteger())
                jsonPath('$.items[4].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[4].o-ran-smo-teiv-ran:ODUFunction[0].attributes.gNBDUId', byNull())
                jsonPath('$.items[5].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[5].o-ran-smo-teiv-ran:ODUFunction[0].attributes.gNBDUId', byNull())
                jsonPath('$.items[6].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[6].o-ran-smo-teiv-ran:ODUFunction[0].attributes.gNBDUId', byNull())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get entities in TEIV domain with targetFilter=/AntennaModule;/OCUUPFunction."
        request {
            method GET()
            url "/topology-inventory/v1/domains/TEIV/entities?offset=0&limit=100&targetFilter=/AntennaModule;/OCUUPFunction"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-equipment:AntennaModule": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-equipment:AntennaModule": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:AntennaModule=72AC3D5E2A5F1C47BD09258A9F7B48E0123E9AD752AC54F7E8D8F9D3A6BC487A89A762A5D12CB9D148BB9E5D53A4F3F981345ACDF7B4CB55D67BC12A13FD5B7A"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-equipment:AntennaModule": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:AntennaModule=84A3E5D7C916F4B2390DC45F178BE6A9235FD80CB41972E3456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-equipment:AntennaModule": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-equipment:AntennaModule": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:AntennaModule=B7F52C914E8D36A0185BC9D47EF230A95C681D7B4F923E0A5D8C741F6B9203E85A4D967B312C8F405E9B7831A6D2C5904F8B3E167A9D204C5B8371F9E6A02D45"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:OCUUPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,OCUUPFunction=10"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:OCUUPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,OCUUPFunction=13"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:OCUUPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,OCUUPFunction=14"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:OCUUPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,OCUUPFunction=16"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:OCUUPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,OCUUPFunction=19"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:OCUUPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=28,OCUUPFunction=28"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:OCUUPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,OCUUPFunction=9"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/TEIV/entities?offset=0&limit=100&targetFilter=/AntennaModule;/OCUUPFunction"
                },
                "first": {
                    "href": "/domains/TEIV/entities?offset=0&limit=100&targetFilter=/AntennaModule;/OCUUPFunction"
                },
                "prev": {
                    "href": "/domains/TEIV/entities?offset=0&limit=100&targetFilter=/AntennaModule;/OCUUPFunction"
                },
                "next": {
                    "href": "/domains/TEIV/entities?offset=0&limit=100&targetFilter=/AntennaModule;/OCUUPFunction"
                },
                "last": {
                    "href": "/domains/TEIV/entities?offset=0&limit=100&targetFilter=/AntennaModule;/OCUUPFunction"
                },
                "totalCount": 12
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(12)
                })
                jsonPath('$.items[5].o-ran-smo-teiv-ran:OCUUPFunction[0].id', byEquality())
                jsonPath('$.items[6].o-ran-smo-teiv-ran:OCUUPFunction[0].id', byEquality())
                jsonPath('$.items[7].o-ran-smo-teiv-ran:OCUUPFunction[0].id', byEquality())
                jsonPath('$.items[8].o-ran-smo-teiv-ran:OCUUPFunction[0].id', byEquality())
                jsonPath('$.items[9].o-ran-smo-teiv-ran:OCUUPFunction[0].id', byEquality())
                jsonPath('$.items[10].o-ran-smo-teiv-ran:OCUUPFunction[0].id', byEquality())
                jsonPath('$.items[11].o-ran-smo-teiv-ran:OCUUPFunction[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-equipment:AntennaModule[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-equipment:AntennaModule[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-equipment:AntennaModule[0].id', byEquality())
                jsonPath('$.items[3].o-ran-smo-teiv-equipment:AntennaModule[0].id', byEquality())
                jsonPath('$.items[4].o-ran-smo-teiv-equipment:AntennaModule[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get entities in RAN domain with scopeFilter=/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,ODUFunction=9,NRCellDU=1')] or /NRCellDU/sourceIds[contains(@item,'ManagedElement=9,ODUFunction=9,NRCellDU=2')] and targetFilter=/NRCellDU/sourceIds."
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?offset=0&limit=100&scopeFilter=/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,ODUFunction=9,NRCellDU=1')]|/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,ODUFunction=9,NRCellDU=2')]&targetFilter=/NRCellDU/sourceIds"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1",
                                "sourceIds": [
                                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1",
                                    "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"
                                    ]
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:NRCellDU":[
                            {
                                "id":"urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2",
                                "sourceIds":[
                                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2",
                                    "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"
                                ]
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/NRCellDU/sourceIds&scopeFilter=/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,ODUFunction=9,NRCellDU=1')]|/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,ODUFunction=9,NRCellDU=2')]"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/NRCellDU/sourceIds&scopeFilter=/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,ODUFunction=9,NRCellDU=1')]|/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,ODUFunction=9,NRCellDU=2')]"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/NRCellDU/sourceIds&scopeFilter=/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,ODUFunction=9,NRCellDU=1')]|/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,ODUFunction=9,NRCellDU=2')]"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/NRCellDU/sourceIds&scopeFilter=/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,ODUFunction=9,NRCellDU=1')]|/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,ODUFunction=9,NRCellDU=2')]"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/NRCellDU/sourceIds&scopeFilter=/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,ODUFunction=9,NRCellDU=1')]|/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,ODUFunction=9,NRCellDU=2')]"
                },
                "totalCount": 2
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCellDU[0].sourceIds[0]', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCellDU[0].sourceIds[1]', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:NRCellDU[0].sourceIds[0]', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:NRCellDU[0].sourceIds[1]', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get entities in RAN domain with scopeFilter=/NRCellDU/attributes[@cellLocalId=1] and targetFilter=/NRCellDU/attributes(nCI)."
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?offset=0&limit=100&scopeFilter=/NRCellDU/attributes[@cellLocalId=1]&targetFilter=/NRCellDU/attributes(nCI)"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1",
                                "attributes": {
                                    "nCI": 1
                                }
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/NRCellDU/attributes(nCI)&scopeFilter=/NRCellDU/attributes[@cellLocalId=1]"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/NRCellDU/attributes(nCI)&scopeFilter=/NRCellDU/attributes[@cellLocalId=1]"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/NRCellDU/attributes(nCI)&scopeFilter=/NRCellDU/attributes[@cellLocalId=1]"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/NRCellDU/attributes(nCI)&scopeFilter=/NRCellDU/attributes[@cellLocalId=1]"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/NRCellDU/attributes(nCI)&scopeFilter=/NRCellDU/attributes[@cellLocalId=1]"
                },
                "totalCount": 1
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(1)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCellDU[0].attributes.nCI', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get topology for an entity with given ID (exact ID match) without knowing entity type."
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?scopeFilter=/id[text()='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2']"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/id[text()='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2']"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/id[text()='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2']"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/id[text()='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2']"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/id[text()='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2']"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/id[text()='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2']"
                },
                "totalCount": 1
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(1)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get empty items array when no entity exists of given filter criteria."
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?offset=0&limit=100&scopeFilter=/NRCellDU/attributes[@cellLocalId=8989439]&targetFilter=/NRCellDU"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/NRCellDU&scopeFilter=/NRCellDU/attributes[@cellLocalId=8989439]"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/NRCellDU&scopeFilter=/NRCellDU/attributes[@cellLocalId=8989439]"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/NRCellDU&scopeFilter=/NRCellDU/attributes[@cellLocalId=8989439]"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/NRCellDU&scopeFilter=/NRCellDU/attributes[@cellLocalId=8989439]"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/NRCellDU&scopeFilter=/NRCellDU/attributes[@cellLocalId=8989439]"
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
        description "SUCCESS - 200: Get entities in TEIV domain with targetFilter=/AntennaModule;/OCUUPFunction."
        request {
            method GET()
            url "/topology-inventory/v1/domains/TEIV/entities?offset=0&limit=100&targetFilter=/AntennaModule;/OCUUPFunction"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-equipment:AntennaModule": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-equipment:AntennaModule": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:AntennaModule=72AC3D5E2A5F1C47BD09258A9F7B48E0123E9AD752AC54F7E8D8F9D3A6BC487A89A762A5D12CB9D148BB9E5D53A4F3F981345ACDF7B4CB55D67BC12A13FD5B7A"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-equipment:AntennaModule": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:AntennaModule=84A3E5D7C916F4B2390DC45F178BE6A9235FD80CB41972E3456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-equipment:AntennaModule": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-equipment:AntennaModule": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:AntennaModule=B7F52C914E8D36A0185BC9D47EF230A95C681D7B4F923E0A5D8C741F6B9203E85A4D967B312C8F405E9B7831A6D2C5904F8B3E167A9D204C5B8371F9E6A02D45"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:OCUUPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,OCUUPFunction=10"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:OCUUPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,OCUUPFunction=13"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:OCUUPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,OCUUPFunction=14"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:OCUUPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,OCUUPFunction=16"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:OCUUPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,OCUUPFunction=19"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:OCUUPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=28,OCUUPFunction=28"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:OCUUPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,OCUUPFunction=9"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/TEIV/entities?offset=0&limit=100&targetFilter=/AntennaModule;/OCUUPFunction"
                },
                "first": {
                    "href": "/domains/TEIV/entities?offset=0&limit=100&targetFilter=/AntennaModule;/OCUUPFunction"
                },
                "prev": {
                    "href": "/domains/TEIV/entities?offset=0&limit=100&targetFilter=/AntennaModule;/OCUUPFunction"
                },
                "next": {
                    "href": "/domains/TEIV/entities?offset=0&limit=100&targetFilter=/AntennaModule;/OCUUPFunction"
                },
                "last": {
                    "href": "/domains/TEIV/entities?offset=0&limit=100&targetFilter=/AntennaModule;/OCUUPFunction"
                },
                "totalCount": 12
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(12)
                })
                jsonPath('$.items[5].o-ran-smo-teiv-ran:OCUUPFunction[0].id', byEquality())
                jsonPath('$.items[6].o-ran-smo-teiv-ran:OCUUPFunction[0].id', byEquality())
                jsonPath('$.items[7].o-ran-smo-teiv-ran:OCUUPFunction[0].id', byEquality())
                jsonPath('$.items[8].o-ran-smo-teiv-ran:OCUUPFunction[0].id', byEquality())
                jsonPath('$.items[9].o-ran-smo-teiv-ran:OCUUPFunction[0].id', byEquality())
                jsonPath('$.items[10].o-ran-smo-teiv-ran:OCUUPFunction[0].id', byEquality())
                jsonPath('$.items[11].o-ran-smo-teiv-ran:OCUUPFunction[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-equipment:AntennaModule[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-equipment:AntennaModule[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-equipment:AntennaModule[0].id', byEquality())
                jsonPath('$.items[3].o-ran-smo-teiv-equipment:AntennaModule[0].id', byEquality())
                jsonPath('$.items[4].o-ran-smo-teiv-equipment:AntennaModule[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get entities in RAN domain with scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10'] and targetFilter=/attributes."
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?offset=0&limit=500&scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10']&targetFilter=/attributes"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
            "items": [
                    {
                        "o-ran-smo-teiv-ran:OCUCPFunction": [
                            {
                                "attributes": {
                                    "gNBId": 10,
                                    "pLMNId": {
                                        "mcc": "456",
                                        "mnc": "83"
                                    },
                                    "gNBIdLength": 2,
                                    "gNBCUName": "ocucp-10"
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,OCUCPFunction=10"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:OCUUPFunction": [
                            {
                                "attributes": {
                                    "gNBId": 10,
                                    "gNBIdLength": 2
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,OCUUPFunction=10"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "attributes": {
                                    "gNBId": 10,
                                    "gNBDUId": null,
                                    "gNBIdLength": 2
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/attributes&scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10']"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/attributes&scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10']"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/attributes&scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10']"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/attributes&scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10']"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/attributes&scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10']"
                },
                "totalCount": 3
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(3)
                })
                jsonPath('$.items[1].o-ran-smo-teiv-ran:OCUUPFunction[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:OCUCPFunction[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get entities in RAN domain with scopeFilter=/managed-by-managedElement; /attributes[@gNBId=10] and targetFilter=/attributes."
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?offset=0&limit=500&scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10']&targetFilter=/attributes"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
            "items": [
                    {
                        "o-ran-smo-teiv-ran:OCUCPFunction": [
                            {
                                "attributes": {
                                    "gNBId": 10,
                                    "pLMNId": {
                                        "mcc": "456",
                                        "mnc": "83"
                                    },
                                    "gNBIdLength": 2,
                                    "gNBCUName": "ocucp-10"
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,OCUCPFunction=10"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:OCUUPFunction": [
                            {
                                "attributes": {
                                    "gNBId": 10,
                                    "gNBIdLength": 2
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,OCUUPFunction=10"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "attributes": {
                                    "gNBId": 10,
                                    "gNBDUId": null,
                                    "gNBIdLength": 2
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/attributes&scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10']"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/attributes&scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10']"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/attributes&scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10']"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/attributes&scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10']"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/attributes&scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10']"
                },
                "totalCount": 3
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(3)
                })
                jsonPath('$.items[1].o-ran-smo-teiv-ran:OCUUPFunction[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:OCUCPFunction[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get entities in RAN domain with scopeFilter=/attributes[@gNBIdLength=1]; /NRCellDU/attributes[@nCI=1] and targetFilter=/attributes(gNBId, pLMNId); /NRCellDU."
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?offset=0&limit=500&scopeFilter=/attributes[@gNBIdLength=1]; /NRCellDU/attributes[@nCI=1]&targetFilter=/attributes(gNBId, pLMNId); /NRCellDU"
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
                        "o-ran-smo-teiv-ran:OCUCPFunction": [
                            {
                                "attributes": {
                                    "pLMNId": {
                                        "mcc": "456",
                                        "mnc": "82"
                                    },
                                    "gNBId": 9
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,OCUCPFunction=9"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/attributes(gNBId, pLMNId); /NRCellDU&scopeFilter=/attributes[@gNBIdLength=1]; /NRCellDU/attributes[@nCI=1]"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/attributes(gNBId, pLMNId); /NRCellDU&scopeFilter=/attributes[@gNBIdLength=1]; /NRCellDU/attributes[@nCI=1]"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/attributes(gNBId, pLMNId); /NRCellDU&scopeFilter=/attributes[@gNBIdLength=1]; /NRCellDU/attributes[@nCI=1]"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/attributes(gNBId, pLMNId); /NRCellDU&scopeFilter=/attributes[@gNBIdLength=1]; /NRCellDU/attributes[@nCI=1]"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/attributes(gNBId, pLMNId); /NRCellDU&scopeFilter=/attributes[@gNBIdLength=1]; /NRCellDU/attributes[@nCI=1]"
                },
                "totalCount": 2
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[1].o-ran-smo-teiv-ran:OCUCPFunction[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description 'SUCCESS - 200: Get entities in RAN domain with scopeFilter on 2 different entity types and combining the condition on complex attributes'
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?scopeFilter=/NRCellDU/attributes[@nCI=1];/ODUFunction/attributes/dUpLMNId[@mcc='456' or @mnc='83']"
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
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,ODUFunction=16"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/NRCellDU/attributes[@nCI=1];/ODUFunction/attributes/dUpLMNId[@mcc='456' or @mnc='83']"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/NRCellDU/attributes[@nCI=1];/ODUFunction/attributes/dUpLMNId[@mcc='456' or @mnc='83']"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/NRCellDU/attributes[@nCI=1];/ODUFunction/attributes/dUpLMNId[@mcc='456' or @mnc='83']"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/NRCellDU/attributes[@nCI=1];/ODUFunction/attributes/dUpLMNId[@mcc='456' or @mnc='83']"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/NRCellDU/attributes[@nCI=1];/ODUFunction/attributes/dUpLMNId[@mcc='456' or @mnc='83']"
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
        description "SUCCESS - 200: Get entityId by using metadata filter on getEntitiesByDomain - EQUALS"
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?scopeFilter=/ODUFunction/metadata[@reliabilityIndicator='OK']"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/ODUFunction/metadata[@reliabilityIndicator='OK']"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/ODUFunction/metadata[@reliabilityIndicator='OK']"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/ODUFunction/metadata[@reliabilityIndicator='OK']"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/ODUFunction/metadata[@reliabilityIndicator='OK']"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/ODUFunction/metadata[@reliabilityIndicator='OK']"
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
    },
    Contract.make {
        description "SUCCESS - 200: Get entityId by using metadata filter on getEntitiesByDomain - EQUALS"
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?targetFilter=/metadata&scopeFilter=/ODUFunction/metadata[@reliabilityIndicator='OK']"
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
                            "metadata": {
                                "reliabilityIndicator": "OK"
                            },
                            "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9"
                        }
                    ]
                }
            ],
            "self": {
                "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/ODUFunction/metadata[@reliabilityIndicator='OK']"
            },
            "first": {
                "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/ODUFunction/metadata[@reliabilityIndicator='OK']"
            },
            "prev": {
                "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/ODUFunction/metadata[@reliabilityIndicator='OK']"
            },
            "next": {
                "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/ODUFunction/metadata[@reliabilityIndicator='OK']"
            },
            "last": {
                "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/ODUFunction/metadata[@reliabilityIndicator='OK']"
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
    },
    Contract.make {
        description "SUCCESS - 200: Get metadata using getEntitiesByDomain - EQUALS"
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?targetFilter=/metadata&scopeFilter=/metadata[@reliabilityIndicator='OK']"
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
                            "metadata": {
                                "lastModified":"2025-01-09T10:40:36.461565Z",
                                "firstDiscovered":"2025-01-09T10:40:36.461565Z",
                                "reliabilityIndicator": "OK"
                            },
                            "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9"
                        }
                    ]
                }
            ],
            "self": {
                "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@reliabilityIndicator='OK']"
            },
            "first": {
                "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@reliabilityIndicator='OK']"
            },
            "prev": {
                "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@reliabilityIndicator='OK']"
            },
            "next": {
                "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@reliabilityIndicator='OK']"
            },
            "last": {
                "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@reliabilityIndicator='OK']"
            },
            "totalCount": 1
        }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(1)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].metadata.lastModified', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].metadata.firstDiscovered', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].metadata.reliabilityIndicator', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all entities in RAN domain for scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9']|/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10'];/provided-nrCellDu[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1']."
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9']|/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10'];/provided-nrCellDu[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1']"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9']|/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10'];/provided-nrCellDu[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1']"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9']|/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10'];/provided-nrCellDu[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1']"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9']|/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10'];/provided-nrCellDu[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1']"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9']|/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10'];/provided-nrCellDu[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1']"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9']|/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10'];/provided-nrCellDu[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1']"
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
    },
    Contract.make {
        description "ERROR - 400: Get entities in RAN domain with invalid offset (greater than total count)."
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?&targetFilter=/NRCellDU;&offset=1000"
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
                "detail": "Offset cannot be larger than 5",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "Error - 400: Get entities with wrong syntax items instead of item with scopeFilter=/NRCellDU/sourceIds[contains(@items,'ManagedElement=9,ODUFunction=9,NRCellDU=1')]"
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?offset=0&limit=100&scopeFilter=/NRCellDU/sourceIds[contains(@items,'ManagedElement=9,ODUFunction=9,NRCellDU=1')]"
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
                "detail": "Invalid source id parameter provided for NRCellDU",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get entities of domain Equipment within 500 meters radius"
        request {
            method GET()
            url "/topology-inventory/v1/domains/EQUIPMENT/entities?offset=0&limit=500&scopeFilter=/attributes[withinMeters(@geo-location, 'POINT(49.40199 68.94199)', 500)]"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
            "items": [
                {
                    "o-ran-smo-teiv-equipment:AntennaModule": [
                        {
                            "id": "urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7"
                        }
                    ]
                }
            ],
            "self": {
                "href": "/domains/EQUIPMENT/entities?offset=0&limit=500&scopeFilter=/attributes[withinMeters(@geo-location, 'POINT(49.40199 68.94199)', 500)]"
            },
            "first": {
                "href": "/domains/EQUIPMENT/entities?offset=0&limit=500&scopeFilter=/attributes[withinMeters(@geo-location, 'POINT(49.40199 68.94199)', 500)]"
            },
            "prev": {
                "href": "/domains/EQUIPMENT/entities?offset=0&limit=500&scopeFilter=/attributes[withinMeters(@geo-location, 'POINT(49.40199 68.94199)', 500)]"
            },
            "next": {
                "href": "/domains/EQUIPMENT/entities?offset=0&limit=500&scopeFilter=/attributes[withinMeters(@geo-location, 'POINT(49.40199 68.94199)', 500)]"
            },
            "last": {
                "href": "/domains/EQUIPMENT/entities?offset=0&limit=500&scopeFilter=/attributes[withinMeters(@geo-location, 'POINT(49.40199 68.94199)', 500)]"
            },
            "totalCount": 1
        }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(1)
                })
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get entities of domain Equipment inside the specified Polygon"
        request {
            method GET()
            url "/topology-inventory/v1/domains/EQUIPMENT/entities?offset=0&limit=500&scopeFilter=/attributes[coveredBy(@geo-location, 'POLYGON((48 68, 50 68, 50 69, 48 69, 48 68))')]"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-equipment:AntennaModule": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/EQUIPMENT/entities?offset=0&limit=500&scopeFilter=/attributes[coveredBy(@geo-location, 'POLYGON((48 68, 50 68, 50 69, 48 69, 48 68))')]"
                },
                "first": {
                    "href": "/domains/EQUIPMENT/entities?offset=0&limit=500&scopeFilter=/attributes[coveredBy(@geo-location, 'POLYGON((48 68, 50 68, 50 69, 48 69, 48 68))')]"
                },
                "prev": {
                    "href": "/domains/EQUIPMENT/entities?offset=0&limit=500&scopeFilter=/attributes[coveredBy(@geo-location, 'POLYGON((48 68, 50 68, 50 69, 48 69, 48 68))')]"
                },
                "next": {
                    "href": "/domains/EQUIPMENT/entities?offset=0&limit=500&scopeFilter=/attributes[coveredBy(@geo-location, 'POLYGON((48 68, 50 68, 50 69, 48 69, 48 68))')]"
                },
                "last": {
                    "href": "/domains/EQUIPMENT/entities?offset=0&limit=500&scopeFilter=/attributes[coveredBy(@geo-location, 'POLYGON((48 68, 50 68, 50 69, 48 69, 48 68))')]"
                },
                "totalCount": 1
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(1)
                })
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get entities of domain RAN by AntennaModule id"
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule[@id='urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7']"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-ran:AntennaCapability": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,NodeSupport=1,SectorEquipmentFunction=1"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule[@id='urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7']"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule[@id='urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7']"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule[@id='urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7']"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule[@id='urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7']"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule[@id='urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7']"
                },
                "totalCount": 2
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get entities of domain RAN by Sector id"
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector[@id='urn:Sector=2']"
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
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector[@id='urn:Sector=2']"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector[@id='urn:Sector=2']"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector[@id='urn:Sector=2']"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector[@id='urn:Sector=2']"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector[@id='urn:Sector=2']"
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
    },
    Contract.make {
        description "SUCCESS - 200: Get entities of domain RAN by AntennaModule id"
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule[@id='urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7']"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:AntennaCapability": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,NodeSupport=1,SectorEquipmentFunction=1"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule[@id='urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7']"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule[@id='urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7']"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule[@id='urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7']"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule[@id='urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7']"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule[@id='urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7']"
                },
                "totalCount": 2
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:AntennaCapability[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description 'SUCCESS - 200: Get entities of domain RAN inside the specified Polygon of AntennaModule'
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[coveredBy(@geo-location, 'POLYGON((48 68, 50 68, 50 69, 48 69, 48 68))')]"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:AntennaCapability": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,NodeSupport=1,SectorEquipmentFunction=1"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[coveredBy(@geo-location, 'POLYGON((48 68, 50 68, 50 69, 48 69, 48 68))')]"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[coveredBy(@geo-location, 'POLYGON((48 68, 50 68, 50 69, 48 69, 48 68))')]"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[coveredBy(@geo-location, 'POLYGON((48 68, 50 68, 50 69, 48 69, 48 68))')]"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[coveredBy(@geo-location, 'POLYGON((48 68, 50 68, 50 69, 48 69, 48 68))')]"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[coveredBy(@geo-location, 'POLYGON((48 68, 50 68, 50 69, 48 69, 48 68))')]"
                },
                "totalCount": 2
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:AntennaCapability[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description 'SUCCESS - 200: Get entities of domain RAN  with complex filter here many to many relationship(ANTENNAMODULE_SERVES_NRCELLDU) , one to many(NRCELLDU_USES_NRSECTORCARRIER) where relationship information is stored in NRSECTORCARRIER , and NRCELLDU Attribute. So 3 Logical blocks and 3 joins from NRCELLDU connects to NRSectorCarrier,ANTENNAMODULE_SERVES_NRCELLDU and AntennaModule'
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[@antennaModelNumber='5'];/used-nrSectorCarrier[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRSectorCarrier=1'];/attributes[@nCI=1]"
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
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[@antennaModelNumber='5'];/used-nrSectorCarrier[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRSectorCarrier=1'];/attributes[@nCI=1]"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[@antennaModelNumber='5'];/used-nrSectorCarrier[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRSectorCarrier=1'];/attributes[@nCI=1]"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[@antennaModelNumber='5'];/used-nrSectorCarrier[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRSectorCarrier=1'];/attributes[@nCI=1]"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[@antennaModelNumber='5'];/used-nrSectorCarrier[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRSectorCarrier=1'];/attributes[@nCI=1]"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[@antennaModelNumber='5'];/used-nrSectorCarrier[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRSectorCarrier=1'];/attributes[@nCI=1]"
                },
                "totalCount": 1
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(1)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description 'SUCCESS - 200: Get entities with filter on attributes and associations. Based on local dataset ODUFunction with gNBId=9 is connected to three instances of NRCellDU but result should contain just one ODUFunction'
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?offset=0&limit=100&scopeFilter=/attributes[@gNBId=9];/provided-nrCellDu[contains(@id,'urn:3gpp:dn:')]"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&scopeFilter=/attributes[@gNBId=9];/provided-nrCellDu[contains(@id,'urn:3gpp:dn:')]"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&scopeFilter=/attributes[@gNBId=9];/provided-nrCellDu[contains(@id,'urn:3gpp:dn:')]"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&scopeFilter=/attributes[@gNBId=9];/provided-nrCellDu[contains(@id,'urn:3gpp:dn:')]"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&scopeFilter=/attributes[@gNBId=9];/provided-nrCellDu[contains(@id,'urn:3gpp:dn:')]"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&scopeFilter=/attributes[@gNBId=9];/provided-nrCellDu[contains(@id,'urn:3gpp:dn:')]"
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
    },
    Contract.make {
        description 'SUCCESS - 200: Get entities of domain RAN within 500 meters radius of AntennaModule'
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[withinMeters(@geo-location, 'POINT(49.40199 68.94199)', 500)]"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:AntennaCapability": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,NodeSupport=1,SectorEquipmentFunction=1"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[withinMeters(@geo-location, 'POINT(49.40199 68.94199)', 500)]"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[withinMeters(@geo-location, 'POINT(49.40199 68.94199)', 500)]"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[withinMeters(@geo-location, 'POINT(49.40199 68.94199)', 500)]"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[withinMeters(@geo-location, 'POINT(49.40199 68.94199)', 500)]"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[withinMeters(@geo-location, 'POINT(49.40199 68.94199)', 500)]"
                },
                "totalCount": 2
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:AntennaCapability[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description 'SUCCESS - 200: Get entities of domain RAN where AntennaModule electricalAntennaTilt is 11'
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[@electricalAntennaTilt=11]"
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
                        "o-ran-smo-teiv-ran:AntennaCapability": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,NodeSupport=1,SectorEquipmentFunction=1"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:AntennaCapability": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,NodeSupport=1,SectorEquipmentFunction=1"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[@electricalAntennaTilt=11]"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[@electricalAntennaTilt=11]"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[@electricalAntennaTilt=11]"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[@electricalAntennaTilt=11]"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule/attributes[@electricalAntennaTilt=11]"
                },
                "totalCount": 4
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(4)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:AntennaCapability[0].id', byEquality())
                jsonPath('$.items[3].o-ran-smo-teiv-ran:AntennaCapability[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description 'SUCCESS - 200: Get entities of domain RAN inside the specified Polygon of Sector'
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector/attributes[coveredBy(@geo-location, 'POLYGON((60 18, 61 18, 61 20, 60 20, 60 18))')]"
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
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector/attributes[coveredBy(@geo-location, 'POLYGON((60 18, 61 18, 61 20, 60 20, 60 18))')]"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector/attributes[coveredBy(@geo-location, 'POLYGON((60 18, 61 18, 61 20, 60 20, 60 18))')]"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector/attributes[coveredBy(@geo-location, 'POLYGON((60 18, 61 18, 61 20, 60 20, 60 18))')]"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector/attributes[coveredBy(@geo-location, 'POLYGON((60 18, 61 18, 61 20, 60 20, 60 18))')]"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector/attributes[coveredBy(@geo-location, 'POLYGON((60 18, 61 18, 61 20, 60 20, 60 18))')]"
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
    },
    Contract.make {
        description 'SUCCESS - 200: Get entities of domain RAN within 500 meters radius of Sector'
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector/attributes[withinMeters(@geo-location, 'POINT(60.4019881 18.9419888)', 500)]"
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
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector/attributes[withinMeters(@geo-location, 'POINT(60.4019881 18.9419888)', 500)]"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector/attributes[withinMeters(@geo-location, 'POINT(60.4019881 18.9419888)', 500)]"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector/attributes[withinMeters(@geo-location, 'POINT(60.4019881 18.9419888)', 500)]"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector/attributes[withinMeters(@geo-location, 'POINT(60.4019881 18.9419888)', 500)]"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector/attributes[withinMeters(@geo-location, 'POINT(60.4019881 18.9419888)', 500)]"
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
    },
    Contract.make {
        description 'SUCCESS - 200: Get entities of domain RAN where Sector sectorId is 2'
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector/attributes[@sectorId=2]"
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
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector/attributes[@sectorId=2]"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector/attributes[@sectorId=2]"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector/attributes[@sectorId=2]"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector/attributes[@sectorId=2]"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-by-sector/attributes[@sectorId=2]"
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
    },
    Contract.make {
        description 'SUCCESS - 200: Get entities of domain RAN where NRCellDU cellLocalId is 3'
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-nrCellDu/attributes[@cellLocalId=3]"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                     {
                        "o-ran-smo-teiv-ran:Sector": [
                            {
                                "id": "urn:Sector=2"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-nrCellDu/attributes[@cellLocalId=3]"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-nrCellDu/attributes[@cellLocalId=3]"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-nrCellDu/attributes[@cellLocalId=3]"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-nrCellDu/attributes[@cellLocalId=3]"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/grouped-nrCellDu/attributes[@cellLocalId=3]"
                },
                "totalCount": 1
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(1)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:Sector[0].id', byEquality())
            }
        }
    }
]
