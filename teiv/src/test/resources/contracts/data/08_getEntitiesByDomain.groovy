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
        description "SUCCESS - 200: Get entities in RAN domain with targetFilter=/GNBDUFunction/attributes(gNBDUId)."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/attributes(gNBDUId)"
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
                                "attributes": {
                                    "gNBDUId": null
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,GNBDUFunction=10"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBDUFunction": [
                            {
                                "attributes": {
                                    "gNBDUId": null
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,GNBDUFunction=13"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBDUFunction": [
                            {
                                "attributes": {
                                    "gNBDUId": null
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,GNBDUFunction=14"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBDUFunction": [
                            {
                                "attributes": {
                                    "gNBDUId": 16
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,GNBDUFunction=16"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBDUFunction": [
                            {
                                "attributes": {
                                    "gNBDUId": null
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,GNBDUFunction=19"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBDUFunction": [
                            {
                                "attributes": {
                                    "gNBDUId": null
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=28,GNBDUFunction=28"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBDUFunction": [
                            {
                                "attributes": {
                                    "gNBDUId": null
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/attributes(gNBDUId)"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/attributes(gNBDUId)"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/attributes(gNBDUId)"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/attributes(gNBDUId)"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/GNBDUFunction/attributes(gNBDUId)"
                },
                "totalCount": 7
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(7)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:GNBDUFunction[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:GNBDUFunction[0].attributes.gNBDUId', byNull())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:GNBDUFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:GNBDUFunction[0].attributes.gNBDUId', byNull())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:GNBDUFunction[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:GNBDUFunction[0].attributes.gNBDUId', byNull())
                jsonPath('$.items[3].o-ran-smo-teiv-ran:GNBDUFunction[0].id', byEquality())
                jsonPath('$.items[3].o-ran-smo-teiv-ran:GNBDUFunction[0].attributes.gNBDUId', byRegex(anInteger()).asInteger())
                jsonPath('$.items[4].o-ran-smo-teiv-ran:GNBDUFunction[0].id', byEquality())
                jsonPath('$.items[4].o-ran-smo-teiv-ran:GNBDUFunction[0].attributes.gNBDUId', byNull())
                jsonPath('$.items[5].o-ran-smo-teiv-ran:GNBDUFunction[0].id', byEquality())
                jsonPath('$.items[5].o-ran-smo-teiv-ran:GNBDUFunction[0].attributes.gNBDUId', byNull())
                jsonPath('$.items[6].o-ran-smo-teiv-ran:GNBDUFunction[0].id', byEquality())
                jsonPath('$.items[6].o-ran-smo-teiv-ran:GNBDUFunction[0].attributes.gNBDUId', byNull())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get entities in TEIV domain with targetFilter=/AntennaModule;/GNBCUUPFunction."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/TEIV/entities?offset=0&limit=100&targetFilter=/AntennaModule;/GNBCUUPFunction"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-ran:GNBCUUPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,GNBCUUPFunction=10"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBCUUPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,GNBCUUPFunction=13"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBCUUPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,GNBCUUPFunction=14"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBCUUPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,GNBCUUPFunction=16"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBCUUPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,GNBCUUPFunction=19"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBCUUPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=28,GNBCUUPFunction=28"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBCUUPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBCUUPFunction=9"
                            }
                        ]
                    },
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
                                "id": "urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/TEIV/entities?offset=0&limit=100&targetFilter=/AntennaModule;/GNBCUUPFunction"
                },
                "first": {
                    "href": "/domains/TEIV/entities?offset=0&limit=100&targetFilter=/AntennaModule;/GNBCUUPFunction"
                },
                "prev": {
                    "href": "/domains/TEIV/entities?offset=0&limit=100&targetFilter=/AntennaModule;/GNBCUUPFunction"
                },
                "next": {
                    "href": "/domains/TEIV/entities?offset=0&limit=100&targetFilter=/AntennaModule;/GNBCUUPFunction"
                },
                "last": {
                    "href": "/domains/TEIV/entities?offset=0&limit=100&targetFilter=/AntennaModule;/GNBCUUPFunction"
                },
                "totalCount": 9
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(9)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:GNBCUUPFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:GNBCUUPFunction[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:GNBCUUPFunction[0].id', byEquality())
                jsonPath('$.items[3].o-ran-smo-teiv-ran:GNBCUUPFunction[0].id', byEquality())
                jsonPath('$.items[4].o-ran-smo-teiv-ran:GNBCUUPFunction[0].id', byEquality())
                jsonPath('$.items[5].o-ran-smo-teiv-ran:GNBCUUPFunction[0].id', byEquality())
                jsonPath('$.items[6].o-ran-smo-teiv-ran:GNBCUUPFunction[0].id', byEquality())
                jsonPath('$.items[7].o-ran-smo-teiv-equipment:AntennaModule[0].id', byEquality())
                jsonPath('$.items[8].o-ran-smo-teiv-equipment:AntennaModule[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get entities in RAN domain with scopeFilter=/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,GNBDUFunction=9,NRCellDU=1')] or /NRCellDU/sourceIds[contains(@item,'ManagedElement=9,GNBDUFunction=9,NRCellDU=2')] and targetFilter=/NRCellDU/sourceIds."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entities?offset=0&limit=100&scopeFilter=/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,GNBDUFunction=9,NRCellDU=1')]|/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,GNBDUFunction=9,NRCellDU=2')]&targetFilter=/NRCellDU/sourceIds"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1",
                                "sourceIds": [
                                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1",
                                    "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"
                                    ]
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:NRCellDU":[
                            {
                                "id":"urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=2",
                                "sourceIds":[
                                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=2",
                                    "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"
                                ]
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/NRCellDU/sourceIds&scopeFilter=/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,GNBDUFunction=9,NRCellDU=1')]|/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,GNBDUFunction=9,NRCellDU=2')]"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/NRCellDU/sourceIds&scopeFilter=/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,GNBDUFunction=9,NRCellDU=1')]|/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,GNBDUFunction=9,NRCellDU=2')]"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/NRCellDU/sourceIds&scopeFilter=/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,GNBDUFunction=9,NRCellDU=1')]|/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,GNBDUFunction=9,NRCellDU=2')]"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/NRCellDU/sourceIds&scopeFilter=/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,GNBDUFunction=9,NRCellDU=1')]|/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,GNBDUFunction=9,NRCellDU=2')]"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=100&targetFilter=/NRCellDU/sourceIds&scopeFilter=/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,GNBDUFunction=9,NRCellDU=1')]|/NRCellDU/sourceIds[contains(@item,'ManagedElement=9,GNBDUFunction=9,NRCellDU=2')]"
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
            url "/topology-inventory/v1alpha11/domains/RAN/entities?offset=0&limit=100&scopeFilter=/NRCellDU/attributes[@cellLocalId=1]&targetFilter=/NRCellDU/attributes(nCI)"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1",
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
            url "/topology-inventory/v1alpha11/domains/RAN/entities?scopeFilter=/id[text()='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=2']"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=2"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/id[text()='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=2']"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/id[text()='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=2']"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/id[text()='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=2']"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/id[text()='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=2']"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/id[text()='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=2']"
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
            url "/topology-inventory/v1alpha11/domains/RAN/entities?offset=0&limit=100&scopeFilter=/NRCellDU/attributes[@cellLocalId=8989439]&targetFilter=/NRCellDU"
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
        description "SUCCESS - 200: Get entities in RAN domain with scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10'] and targetFilter=/attributes."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entities?offset=0&limit=500&scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10']&targetFilter=/attributes"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                            "items": [
                    {
                        "o-ran-smo-teiv-ran:GNBCUUPFunction": [
                            {
                                "attributes": {
                                    "gNBId": 10,
                                    "gNBIdLength": 2
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,GNBCUUPFunction=10"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBDUFunction": [
                            {
                                "attributes": {
                                    "dUpLMNId": {
                                        "mcc": "456",
                                        "mnc": "82"
                                    },
                                    "gNBId": 10,
                                    "gNBDUId": null,
                                    "gNBIdLength": 2
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,GNBDUFunction=10"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBCUCPFunction": [
                            {
                                "attributes": {
                                    "gNBId": 10,
                                    "pLMNId": {
                                        "mcc": "01",
                                        "mnc": "234"
                                    },
                                    "gNBIdLength": 2,
                                    "gNBCUName": "gnbcucp-10"
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,GNBCUCPFunction=10"
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
                jsonPath('$.items[0].o-ran-smo-teiv-ran:GNBCUUPFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:GNBDUFunction[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:GNBCUCPFunction[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get entities in RAN domain with scopeFilter=/managed-by-managedElement; /attributes[@gNBId=10] and targetFilter=/attributes."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entities?offset=0&limit=500&scopeFilter=/managed-by-managedElement[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10']&targetFilter=/attributes"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                            "items": [
                    {
                        "o-ran-smo-teiv-ran:GNBCUUPFunction": [
                            {
                                "attributes": {
                                    "gNBId": 10,
                                    "gNBIdLength": 2
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,GNBCUUPFunction=10"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBDUFunction": [
                            {
                                "attributes": {
                                    "dUpLMNId": {
                                        "mcc": "456",
                                        "mnc": "82"
                                    },
                                    "gNBId": 10,
                                    "gNBDUId": null,
                                    "gNBIdLength": 2
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,GNBDUFunction=10"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBCUCPFunction": [
                            {
                                "attributes": {
                                    "gNBId": 10,
                                    "pLMNId": {
                                        "mcc": "01",
                                        "mnc": "234"
                                    },
                                    "gNBIdLength": 2,
                                    "gNBCUName": "gnbcucp-10"
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,GNBCUCPFunction=10"
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
                jsonPath('$.items[0].o-ran-smo-teiv-ran:GNBCUUPFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:GNBDUFunction[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:GNBCUCPFunction[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get entities in RAN domain with scopeFilter=/attributes[@gNBIdLength=1]; /NRCellDU/attributes[@nCI=1] and targetFilter=/attributes(gNBId, dUpLMNId); /NRCellDU."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entities?offset=0&limit=500&scopeFilter=/attributes[@gNBIdLength=1]; /NRCellDU/attributes[@nCI=1]&targetFilter=/attributes(gNBId, dUpLMNId); /NRCellDU"
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
                                "attributes": {
                                    "dUpLMNId": {
                                        "mcc": "456",
                                        "mnc": "82"
                                    },
                                    "gNBId": 9
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/attributes(gNBId, dUpLMNId); /NRCellDU&scopeFilter=/attributes[@gNBIdLength=1]; /NRCellDU/attributes[@nCI=1]"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/attributes(gNBId, dUpLMNId); /NRCellDU&scopeFilter=/attributes[@gNBIdLength=1]; /NRCellDU/attributes[@nCI=1]"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/attributes(gNBId, dUpLMNId); /NRCellDU&scopeFilter=/attributes[@gNBIdLength=1]; /NRCellDU/attributes[@nCI=1]"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/attributes(gNBId, dUpLMNId); /NRCellDU&scopeFilter=/attributes[@gNBIdLength=1]; /NRCellDU/attributes[@nCI=1]"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&targetFilter=/attributes(gNBId, dUpLMNId); /NRCellDU&scopeFilter=/attributes[@gNBIdLength=1]; /NRCellDU/attributes[@nCI=1]"
                },
                "totalCount": 2
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:GNBDUFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get entities in RAN domain with scopeFilter on 2 different entity types and combining the condition on complex attributes"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entities?scopeFilter=/NRCellDU/attributes[@nCI=1];/GNBDUFunction/attributes/dUpLMNId[@mcc='456' or @mnc='83']"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,GNBDUFunction=10"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBDUFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,GNBDUFunction=16"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/NRCellDU/attributes[@nCI=1];/GNBDUFunction/attributes/dUpLMNId[@mcc='456' or @mnc='83']"
                },
                "first": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/NRCellDU/attributes[@nCI=1];/GNBDUFunction/attributes/dUpLMNId[@mcc='456' or @mnc='83']"
                },
                "prev": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/NRCellDU/attributes[@nCI=1];/GNBDUFunction/attributes/dUpLMNId[@mcc='456' or @mnc='83']"
                },
                "next": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/NRCellDU/attributes[@nCI=1];/GNBDUFunction/attributes/dUpLMNId[@mcc='456' or @mnc='83']"
                },
                "last": {
                    "href": "/domains/RAN/entities?offset=0&limit=500&scopeFilter=/NRCellDU/attributes[@nCI=1];/GNBDUFunction/attributes/dUpLMNId[@mcc='456' or @mnc='83']"
                },
                "totalCount": 3
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(3)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:GNBDUFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:GNBDUFunction[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "ERROR - 400: Get entities in RAN domain with invalid offset (greater than total count)."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entities?&targetFilter=/NRCellDU;&offset=1000"
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid Value",
                "details": "Offset cannot be larger than 5"
            }''')
        }
    },
    Contract.make {
        description "Error - 400: Get entities with wrong syntax items instead of item with scopeFilter=/NRCellDU/sourceIds[contains(@items,'ManagedElement=9,GNBDUFunction=9,NRCellDU=1')]"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entities?offset=0&limit=100&scopeFilter=/NRCellDU/sourceIds[contains(@items,'ManagedElement=9,GNBDUFunction=9,NRCellDU=1')]"
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid parameter error",
                "details": "Invalid source id parameter provided for NRCellDU"
            }''')
        }
    }
]
