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
        description "SUCCESS - 200: Get all topology entities of type NRCellDU."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=91"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=92"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=93"
                            }
                        ]
                    },
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
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=500"
                },
                "first": {
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=500"
                },
                "prev": {
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=500"
                },
                "next": {
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=500"
                },
                "last": {
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=500"
                },
                "totalCount": 6
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(6)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[3].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[4].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[5].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all topology entities of type ODUFunction with offset as 1 and limit as 4."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/ODUFunction/entities?offset=1&limit=4"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,ODUFunction=13"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,ODUFunction=14"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,ODUFunction=16"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=1&limit=4"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=4"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=4"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=5&limit=4"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=5&limit=4"
                },
                "totalCount": 7
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(4)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[3].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all topology entities of type NRCellDU with scopeFilter."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=100&scopeFilter=/NRCellDU/attributes[@cellLocalId=1]"
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
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=100&scopeFilter=/NRCellDU/attributes[@cellLocalId=1]"
                },
                "first": {
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=100&scopeFilter=/NRCellDU/attributes[@cellLocalId=1]"
                },
                "prev": {
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=100&scopeFilter=/NRCellDU/attributes[@cellLocalId=1]"
                },
                "next": {
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=100&scopeFilter=/NRCellDU/attributes[@cellLocalId=1]"
                },
                "last": {
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=100&scopeFilter=/NRCellDU/attributes[@cellLocalId=1]"
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
        description "SUCCESS - 200: Get all topology entities of type NRCellDU which matches given ID (exact ID match). Alternate to GET entity by ID."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?scopeFilter=/NRCellDU[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2']"
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
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=500&scopeFilter=/NRCellDU[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2']"
                },
                "first": {
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=500&scopeFilter=/NRCellDU[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2']"
                },
                "prev": {
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=500&scopeFilter=/NRCellDU[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2']"
                },
                "next": {
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=500&scopeFilter=/NRCellDU[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2']"
                },
                "last": {
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=500&scopeFilter=/NRCellDU[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2']"
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
        description "SUCCESS - 200: Get all topology entities of type NRCellDU which contain 'ManagedElement=9,ODUFunction=9' in ID (partial ID match)."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?scopeFilter=/NRCellDU[contains(@id, 'ManagedElement=9,ODUFunction=9')]"
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
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=500&scopeFilter=/NRCellDU[contains(@id, 'ManagedElement=9,ODUFunction=9')]"
                },
                "first": {
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=500&scopeFilter=/NRCellDU[contains(@id, 'ManagedElement=9,ODUFunction=9')]"
                },
                "prev": {
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=500&scopeFilter=/NRCellDU[contains(@id, 'ManagedElement=9,ODUFunction=9')]"
                },
                "next": {
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=500&scopeFilter=/NRCellDU[contains(@id, 'ManagedElement=9,ODUFunction=9')]"
                },
                "last": {
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=500&scopeFilter=/NRCellDU[contains(@id, 'ManagedElement=9,ODUFunction=9')]"
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
        description "SUCCESS - 200: Get all topology entities of type AntennaCapability which has serving-antennaModule association with ID that contains 'AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A'"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/TEIV/entity-types/AntennaCapability/entities?scopeFilter=/serving-antennaModule[contains(@id,'AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A')]"
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
                        "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,NodeSupport=1,SectorEquipmentFunction=1"
                      }
                    ]
                  }
                ],
                "self": {
                    "href": "/domains/TEIV/entity-types/AntennaCapability/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule[contains(@id,'AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A')]"
                },
                "first": {
                    "href": "/domains/TEIV/entity-types/AntennaCapability/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule[contains(@id,'AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A')]"
                },
                "prev": {
                    "href": "/domains/TEIV/entity-types/AntennaCapability/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule[contains(@id,'AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A')]"
                },
                "next": {
                    "href": "/domains/TEIV/entity-types/AntennaCapability/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule[contains(@id,'AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A')]"
                },
                "last": {
                    "href": "/domains/TEIV/entity-types/AntennaCapability/entities?offset=0&limit=500&scopeFilter=/serving-antennaModule[contains(@id,'AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A')]"
                },
                "totalCount": 1
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(1)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:AntennaCapability[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all topology entities of type ODUFunction which has provided-nrCellDu association with ID that contains 'NRCellDU=1'."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/ODUFunction/entities?scopeFilter=/provided-nrCellDu[contains(@id,'NRCellDU=1')]"
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
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&scopeFilter=/provided-nrCellDu[contains(@id,'NRCellDU=1')]"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&scopeFilter=/provided-nrCellDu[contains(@id,'NRCellDU=1')]"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&scopeFilter=/provided-nrCellDu[contains(@id,'NRCellDU=1')]"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&scopeFilter=/provided-nrCellDu[contains(@id,'NRCellDU=1')]"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&scopeFilter=/provided-nrCellDu[contains(@id,'NRCellDU=1')]"
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
        description "SUCCESS - 200: Get all topology entities of type AntennaModule with targetFilter=/sourceIds"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/EQUIPMENT/entity-types/AntennaModule/entities?targetFilter=/sourceIds"
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
                                "id": "urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A",
                                "sourceIds": [
                                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1",
                                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1",
                                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=1,RetSubUnit=1",
                                    "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"
                                ]
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-equipment:AntennaModule": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7",
                                "sourceIds": [
                                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1",
                                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1",
                                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=1,RetSubUnit=1",
                                    "urn:cmHandle:03661FA2E41EF3D12CAAD5954CD985AC"
                                ]
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&targetFilter=/sourceIds"
                },
                "first": {
                    "href": "/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&targetFilter=/sourceIds"
                },
                "prev": {
                    "href": "/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&targetFilter=/sourceIds"
                },
                "next": {
                    "href": "/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&targetFilter=/sourceIds"
                },
                "last": {
                    "href": "/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&targetFilter=/sourceIds"
                },
                "totalCount": 2
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-equipment:AntennaModule[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-equipment:AntennaModule[0].sourceIds[0]', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-equipment:AntennaModule[0].sourceIds[1]', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-equipment:AntennaModule[0].sourceIds[2]', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-equipment:AntennaModule[0].sourceIds[3]', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-equipment:AntennaModule[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-equipment:AntennaModule[0].sourceIds[0]', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-equipment:AntennaModule[0].sourceIds[1]', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-equipment:AntennaModule[0].sourceIds[2]', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-equipment:AntennaModule[0].sourceIds[3]', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all topology entities of type AntennaModule with scopeFilter on sourceIds"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/EQUIPMENT/entity-types/AntennaModule/entities?scopeFilter=/sourceIds[@item = 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1']&targetFilter=/sourceIds"
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
                                "id": "urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A",
                                "sourceIds": [
                                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1",
                                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1",
                                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=1,RetSubUnit=1",
                                    "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"
                                ]
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&targetFilter=/sourceIds&scopeFilter=/sourceIds[@item = 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1']"
                },
                "first": {
                    "href": "/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&targetFilter=/sourceIds&scopeFilter=/sourceIds[@item = 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1']"
                },
                "prev": {
                    "href": "/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&targetFilter=/sourceIds&scopeFilter=/sourceIds[@item = 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1']"
                },
                "next": {
                    "href": "/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&targetFilter=/sourceIds&scopeFilter=/sourceIds[@item = 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1']"
                },
                "last": {
                    "href": "/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&targetFilter=/sourceIds&scopeFilter=/sourceIds[@item = 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1']"
                },
                "totalCount": 1
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(1)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-equipment:AntennaModule[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-equipment:AntennaModule[0].sourceIds[0]', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-equipment:AntennaModule[0].sourceIds[1]', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-equipment:AntennaModule[0].sourceIds[2]', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-equipment:AntennaModule[0].sourceIds[3]', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get empty items array when no entity exists of given filter criteria."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=100&scopeFilter=/NRCellDU/attributes[@cellLocalId=898989]"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [],
                "self": {
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=100&scopeFilter=/NRCellDU/attributes[@cellLocalId=898989]"
                },
                "first": {
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=100&scopeFilter=/NRCellDU/attributes[@cellLocalId=898989]"
                },
                "prev": {
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=100&scopeFilter=/NRCellDU/attributes[@cellLocalId=898989]"
                },
                "next": {
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=100&scopeFilter=/NRCellDU/attributes[@cellLocalId=898989]"
                },
                "last": {
                    "href": "/domains/REL_EQUIPMENT_RAN/entity-types/NRCellDU/entities?offset=0&limit=100&scopeFilter=/NRCellDU/attributes[@cellLocalId=898989]"
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
        description "SUCCESS - 200: Get entities by a long named entity type."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities?offset=0&limit=1&targetFilter=/attributes"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "test-built-in-module:EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters": [
                            {
                                "attributes": {
                                    "attributeA2WithAttributeNameLengthLongerThanSixtyThreeCharacters": 9223372036854775807,
                                    "attributeA6WithAttributeNameLengthLongerThanSixtyThreeCharacters": [
                                        "1000",
                                        "2000"
                                    ],
                                    "attributeA5WithAttributeNameLengthLongerThanSixtyThreeCharacters": 1.1,
                                    "attributeA4WithAttributeNameLengthLongerThanSixtyThreeCharacters": -9223372036854775807,
                                    "attributeA3WithAttributeNameLengthLongerThanSixtyThreeCharacters": 2147483647,
                                    "attributeA7WithAttributeNameLengthLongerThanSixtyThreeCharacters": {
                                        "mcc": "01",
                                        "mnc": "234"
                                    },
                                    "attributeA1WithAttributeNameLengthLongerThanSixtyThreeCharacters": "someStringValue"
                                },
                                "id": "LongEntityType1"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities?offset=0&limit=1&targetFilter=/attributes"
                },
                "first": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities?offset=0&limit=1&targetFilter=/attributes"
                },
                "prev": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities?offset=0&limit=1&targetFilter=/attributes"
                },
                "next": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities?offset=1&limit=1&targetFilter=/attributes"
                },
                "last": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities?offset=2&limit=1&targetFilter=/attributes"
                },
                "totalCount": 3
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(1)
                })
                jsonPath('$.items[0].test-built-in-module:EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters[0].id', byEquality())
                jsonPath('$.items[0].test-built-in-module:EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters[0].attributes.attributeA1WithAttributeNameLengthLongerThanSixtyThreeCharacters', byEquality())
                jsonPath('$.items[0].test-built-in-module:EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters[0].attributes.attributeA2WithAttributeNameLengthLongerThanSixtyThreeCharacters', byEquality())
                jsonPath('$.items[0].test-built-in-module:EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters[0].attributes.attributeA3WithAttributeNameLengthLongerThanSixtyThreeCharacters', byEquality())
                jsonPath('$.items[0].test-built-in-module:EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters[0].attributes.attributeA4WithAttributeNameLengthLongerThanSixtyThreeCharacters', byEquality())
                jsonPath('$.items[0].test-built-in-module:EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters[0].attributes.attributeA5WithAttributeNameLengthLongerThanSixtyThreeCharacters', byEquality())
                jsonPath('$.items[0].test-built-in-module:EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters[0].attributes.attributeA6WithAttributeNameLengthLongerThanSixtyThreeCharacters[0]', byEquality())
                jsonPath('$.items[0].test-built-in-module:EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters[0].attributes.attributeA6WithAttributeNameLengthLongerThanSixtyThreeCharacters[1]', byEquality())
                jsonPath('$.items[0].test-built-in-module:EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters[0].attributes.attributeA7WithAttributeNameLengthLongerThanSixtyThreeCharacters.mcc', byEquality())
                jsonPath('$.items[0].test-built-in-module:EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters[0].attributes.attributeA7WithAttributeNameLengthLongerThanSixtyThreeCharacters.mnc', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all topology entities of type NRCellDU with scopeFilter on association."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/NRCellDU/entities?offset=0&limit=100&scopeFilter=/provided-by-oduFunction[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9']&targetFilter=/attributes(nCI)"
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
                        "attributes": {
                          "nCI": 1
                        },
                        "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1"
                      }
                    ]
                  },
                  {
                    "o-ran-smo-teiv-ran:NRCellDU": [
                      {
                        "attributes": {
                          "nCI": 2
                        },
                        "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2"
                      }
                    ]
                  },
                  {
                    "o-ran-smo-teiv-ran:NRCellDU": [
                      {
                        "attributes": {
                          "nCI": 3
                        },
                        "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=3"
                      }
                    ]
                  }
                ],
                "self": {
                  "href": "/domains/RAN/entity-types/NRCellDU/entities?offset=0&limit=100&targetFilter=/attributes(nCI)&scopeFilter=/provided-by-oduFunction[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9']"
                },
                "first": {
                  "href": "/domains/RAN/entity-types/NRCellDU/entities?offset=0&limit=100&targetFilter=/attributes(nCI)&scopeFilter=/provided-by-oduFunction[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9']"
                },
                "prev": {
                  "href": "/domains/RAN/entity-types/NRCellDU/entities?offset=0&limit=100&targetFilter=/attributes(nCI)&scopeFilter=/provided-by-oduFunction[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9']"
                },
                "next": {
                  "href": "/domains/RAN/entity-types/NRCellDU/entities?offset=0&limit=100&targetFilter=/attributes(nCI)&scopeFilter=/provided-by-oduFunction[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9']"
                },
                "last": {
                  "href": "/domains/RAN/entity-types/NRCellDU/entities?offset=0&limit=100&targetFilter=/attributes(nCI)&scopeFilter=/provided-by-oduFunction[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9']"
                },
                "totalCount": 3
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(3)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCellDU[0].attributes.nCI', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:NRCellDU[0].attributes.nCI', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:NRCellDU[0].attributes.nCI', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all topology entities of type ODUFunction with scopeFilter on sourceIds and gNBId with AND condition."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&scopeFilter=/attributes[@gNBId=10]; /sourceIds[@item = 'urn:cmHandle:72FDA73D085F138FECC974CB91F1450E']&targetFilter=/attributes;/sourceIds"
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
                                    "pLMNId": {
                                        "mcc": "456",
                                        "mnc": "82"
                                    },
                                    "gNBId": 10,
                                    "gNBDUId": null,
                                    "gNBIdLength": 2
                                },
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10",
                                "sourceIds": [
                                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10",
                                    "urn:cmHandle:72FDA73D085F138FECC974CB91F1450E"
                                ]
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes;/sourceIds&scopeFilter=/attributes[@gNBId=10]; /sourceIds[@item = 'urn:cmHandle:72FDA73D085F138FECC974CB91F1450E']"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes;/sourceIds&scopeFilter=/attributes[@gNBId=10]; /sourceIds[@item = 'urn:cmHandle:72FDA73D085F138FECC974CB91F1450E']"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes;/sourceIds&scopeFilter=/attributes[@gNBId=10]; /sourceIds[@item = 'urn:cmHandle:72FDA73D085F138FECC974CB91F1450E']"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes;/sourceIds&scopeFilter=/attributes[@gNBId=10]; /sourceIds[@item = 'urn:cmHandle:72FDA73D085F138FECC974CB91F1450E']"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/attributes;/sourceIds&scopeFilter=/attributes[@gNBId=10]; /sourceIds[@item = 'urn:cmHandle:72FDA73D085F138FECC974CB91F1450E']"
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
        description "SUCCESS - 200: Get all topology entities of type ODUFunction with scopeFilter with AND an OR conditions."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&scopeFilter=/attributes[@gNBId=10 or @gNBId=13]; /attributes[@gNBIdLength = 2]&targetFilter=/ODUFunction"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,ODUFunction=13"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/ODUFunction&scopeFilter=/attributes[@gNBId=10 or @gNBId=13]; /attributes[@gNBIdLength = 2]"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/ODUFunction&scopeFilter=/attributes[@gNBId=10 or @gNBId=13]; /attributes[@gNBIdLength = 2]"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/ODUFunction&scopeFilter=/attributes[@gNBId=10 or @gNBId=13]; /attributes[@gNBIdLength = 2]"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/ODUFunction&scopeFilter=/attributes[@gNBId=10 or @gNBId=13]; /attributes[@gNBIdLength = 2]"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/ODUFunction&scopeFilter=/attributes[@gNBId=10 or @gNBId=13]; /attributes[@gNBIdLength = 2]"
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
        description "SUCCESS - 200: Get all topology entities of type ODUFunction with scopeFilter with AND an OR conditions."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&scopeFilter=/attributes[@gNBId = 9 and @gNBIdLength = 1] | /sourceIds[@item = 'urn:cmHandle:72FDA73D085F138FECC974CB91F1450E']&targetFilter=/id"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/id&scopeFilter=/attributes[@gNBId = 9 and @gNBIdLength = 1] | /sourceIds[@item = 'urn:cmHandle:72FDA73D085F138FECC974CB91F1450E']"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/id&scopeFilter=/attributes[@gNBId = 9 and @gNBIdLength = 1] | /sourceIds[@item = 'urn:cmHandle:72FDA73D085F138FECC974CB91F1450E']"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/id&scopeFilter=/attributes[@gNBId = 9 and @gNBIdLength = 1] | /sourceIds[@item = 'urn:cmHandle:72FDA73D085F138FECC974CB91F1450E']"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/id&scopeFilter=/attributes[@gNBId = 9 and @gNBIdLength = 1] | /sourceIds[@item = 'urn:cmHandle:72FDA73D085F138FECC974CB91F1450E']"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities?offset=0&limit=500&targetFilter=/id&scopeFilter=/attributes[@gNBId = 9 and @gNBIdLength = 1] | /sourceIds[@item = 'urn:cmHandle:72FDA73D085F138FECC974CB91F1450E']"
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
        description "SUCCESS - 200: Get all topology entities of type OCUCPFunction with scopeFilter on complex attribute STRING value - EQUALS - KEY AND VALUE EXISTS"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/OCUCPFunction/entities?offset=0&limit=500&targetFilter=/attributes(pLMNId)&scopeFilter=/attributes/pLMNId[@mcc='456']"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,OCUCPFunction=10",
                                "attributes": {
                                    "pLMNId": {
                                        "mcc": "456",
                                        "mnc": "83"
                                    }
                                }
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:OCUCPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,OCUCPFunction=16",
                                "attributes": {
                                    "pLMNId": {
                                        "mcc": "456",
                                        "mnc": "86"
                                    }
                                }
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/OCUCPFunction/entities?offset=0&limit=500&targetFilter=/attributes(pLMNId)&scopeFilter=/attributes/pLMNId[@mcc='456']"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/OCUCPFunction/entities?offset=0&limit=500&targetFilter=/attributes(pLMNId)&scopeFilter=/attributes/pLMNId[@mcc='456']"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/OCUCPFunction/entities?offset=0&limit=500&targetFilter=/attributes(pLMNId)&scopeFilter=/attributes/pLMNId[@mcc='456']"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/OCUCPFunction/entities?offset=0&limit=500&targetFilter=/attributes(pLMNId)&scopeFilter=/attributes/pLMNId[@mcc='456']"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/OCUCPFunction/entities?offset=0&limit=500&targetFilter=/attributes(pLMNId)&scopeFilter=/attributes/pLMNId[@mcc='456']"
                },
                "totalCount": 2
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:OCUCPFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:OCUCPFunction[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all topology entities of type OCUCPFunction with scopeFilter on complex attribute STRING value - EQUALS - KEY AND VALUE EXISTS"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/OCUCPFunction/entities?offset=0&limit=500&targetFilter=/attributes(pLMNId)&scopeFilter=/attributes/pLMNId[@mcc='456']"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,OCUCPFunction=10",
                                "attributes": {
                                    "pLMNId": {
                                        "mcc": "456",
                                        "mnc": "83"
                                    }
                                }
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:OCUCPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,OCUCPFunction=16",
                                "attributes": {
                                    "pLMNId": {
                                        "mcc": "456",
                                        "mnc": "86"
                                    }
                                }
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/OCUCPFunction/entities?offset=0&limit=500&targetFilter=/attributes(pLMNId)&scopeFilter=/attributes/pLMNId[@mcc='456']"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/OCUCPFunction/entities?offset=0&limit=500&targetFilter=/attributes(pLMNId)&scopeFilter=/attributes/pLMNId[@mcc='456']"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/OCUCPFunction/entities?offset=0&limit=500&targetFilter=/attributes(pLMNId)&scopeFilter=/attributes/pLMNId[@mcc='456']"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/OCUCPFunction/entities?offset=0&limit=500&targetFilter=/attributes(pLMNId)&scopeFilter=/attributes/pLMNId[@mcc='456']"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/OCUCPFunction/entities?offset=0&limit=500&targetFilter=/attributes(pLMNId)&scopeFilter=/attributes/pLMNId[@mcc='456']"
                },
                "totalCount": 2
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:OCUCPFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:OCUCPFunction[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all topology entities of type OCUCPFunction with scopeFilter on complex attribute STRING value - CONTAINS - KEY AND VALUE EXISTS"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/OCUCPFunction/entities?offset=0&limit=500&targetFilter=/attributes(pLMNId)&scopeFilter=/attributes/pLMNId[contains(@mcc,'78')]"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,OCUCPFunction=13",
                                "attributes": {
                                    "pLMNId": {
                                        "mcc": "789",
                                        "mnc": "84"
                                    }
                                }
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:OCUCPFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,OCUCPFunction=19",
                                "attributes": {
                                    "pLMNId": {
                                        "mcc": "789",
                                        "mnc": "87"
                                    }
                                }
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/OCUCPFunction/entities?offset=0&limit=500&targetFilter=/attributes(pLMNId)&scopeFilter=/attributes/pLMNId[contains(@mcc,'78')]"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/OCUCPFunction/entities?offset=0&limit=500&targetFilter=/attributes(pLMNId)&scopeFilter=/attributes/pLMNId[contains(@mcc,'78')]"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/OCUCPFunction/entities?offset=0&limit=500&targetFilter=/attributes(pLMNId)&scopeFilter=/attributes/pLMNId[contains(@mcc,'78')]"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/OCUCPFunction/entities?offset=0&limit=500&targetFilter=/attributes(pLMNId)&scopeFilter=/attributes/pLMNId[contains(@mcc,'78')]"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/OCUCPFunction/entities?offset=0&limit=500&targetFilter=/attributes(pLMNId)&scopeFilter=/attributes/pLMNId[contains(@mcc,'78')]"
                },
                "totalCount": 2
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:OCUCPFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:OCUCPFunction[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all topology entities of type AntennaCapability with scopeFilter on complex attribute STRING array - EXACT MATCH - VALUE EXISTS"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[@item='456']"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,NodeSupport=1,SectorEquipmentFunction=1",
                                "attributes": {
                                    "eUtranFqBands": [
                                        "123",
                                        "456",
                                        "789"
                                    ]
                                }
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:AntennaCapability": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,NodeSupport=1,SectorEquipmentFunction=1",
                                "attributes": {
                                    "eUtranFqBands": [
                                        "123",
                                        "456",
                                        "789"
                                    ]
                                }
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[@item='456']"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[@item='456']"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[@item='456']"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[@item='456']"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[@item='456']"
                },
                "totalCount": 2
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:AntennaCapability[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:AntennaCapability[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all topology entities of type AntennaCapability with scopeFilter on complex attribute INTEGER array - EXACT MATCH - VALUE NOT EXISTS"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[@item=456]"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [],
                "self": {
                    "href": "/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[@item=456]"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[@item=456]"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[@item=456]"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[@item=456]"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[@item=456]"
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
        description "SUCCESS - 200: Get all topology entities of type AntennaCapability with scopeFilter on complex attribute STRING array - PARTIAL MATCH - VALUE EXISTS"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[contains(@item,'45')]"
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
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,NodeSupport=1,SectorEquipmentFunction=1",
                                "attributes": {
                                    "eUtranFqBands": [
                                        "123",
                                        "456",
                                        "789"
                                    ]
                                }
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:AntennaCapability": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,NodeSupport=1,SectorEquipmentFunction=1",
                                "attributes": {
                                    "eUtranFqBands": [
                                        "123",
                                        "456",
                                        "789"
                                    ]
                                }
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[contains(@item,'45')]"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[contains(@item,'45')]"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[contains(@item,'45')]"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[contains(@item,'45')]"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[contains(@item,'45')]"
                },
                "totalCount": 2
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:AntennaCapability[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:AntennaCapability[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all topology entities of type AntennaCapability with scopeFilter on complex attribute STRING array - PARTIAL MATCH - VALUE NOT EXISTS"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[contains(@item,'999')]"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [],
                "self": {
                    "href": "/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[contains(@item,'999')]"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[contains(@item,'999')]"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[contains(@item,'999')]"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[contains(@item,'999')]"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/AntennaCapability/entities?offset=0&limit=500&targetFilter=/attributes(eUtranFqBands)&scopeFilter=/attributes/eUtranFqBands[contains(@item,'999')]"
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
        description "SUCCESS - 200: Get entities of type AntennaModule with scopeFilter on geographic attribute"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&targetFilter=/attributes(geo-location)&scopeFilter=/attributes[@geo-location = 'POINT(39.4019881 67.9419888)']"
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
                                "id": "urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A",
                                "attributes": {
                                    "geo-location": "POINT(39.4019881 67.9419888)"
                                }
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&targetFilter=/attributes(geo-location)&scopeFilter=/attributes[@geo-location = 'POINT(39.4019881 67.9419888)']"
                },
                "first": {
                    "href": "/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&targetFilter=/attributes(geo-location)&scopeFilter=/attributes[@geo-location = 'POINT(39.4019881 67.9419888)']"
                },
                "prev": {
                    "href": "/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&targetFilter=/attributes(geo-location)&scopeFilter=/attributes[@geo-location = 'POINT(39.4019881 67.9419888)']"
                },
                "next": {
                    "href": "/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&targetFilter=/attributes(geo-location)&scopeFilter=/attributes[@geo-location = 'POINT(39.4019881 67.9419888)']"
                },
                "last": {
                    "href": "/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&targetFilter=/attributes(geo-location)&scopeFilter=/attributes[@geo-location = 'POINT(39.4019881 67.9419888)']"
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
        description "ERROR - 400: Get entities of type AntennaModule with scopeFilter on geographic attribute - invalid format"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&targetFilter=/attributes(geo-location)&scopeFilter=/attributes[@geo-location = '(39.4019881 67.9419888)']"
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid query condition",
                "details": "Invalid geographic format."
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Get entities of type AntennaModule with scopeFilter on geographic attribute - 1 coordinate"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&targetFilter=/attributes(geo-location)&scopeFilter=/attributes[@geo-location = 'POINT(39.4019881)']"
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid query condition",
                "details": "A point should have 2 coordinates (longitude and latitude)."
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Get entities of type AntennaModule with scopeFilter on geographic attribute - invalid coordinate"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/EQUIPMENT/entity-types/AntennaModule/entities?offset=0&limit=500&targetFilter=/attributes(geo-location)&scopeFilter=/attributes[@geo-location = 'POINT(39.4019881 ABC)']"
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid query condition",
                "details": "Invalid coordinate: ABC"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Get all topology entities for an entity type that is not in the domain."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/EQUIPMENT/entity-types/NRCellDU/entities"
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Unknown entity type",
                "details": "Entity type NRCellDU is not part of the domain EQUIPMENT, known entity types: [AntennaModule, Site]"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Get all topology entities for wrong entity type name."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/NRCellDU1/entities"
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Unknown entity type",
                "details": "Entity type NRCellDU1 is not part of the model, known entity types: [AntennaCapability, AntennaModule, CloudifiedNF, EntityTypeA, EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters, ManagedElement, NFDeployment, NRCellCU, NRCellDU, NRSectorCarrier, NearRTRICFunction, NodeCluster, OCUCPFunction, OCUUPFunction, OCloudNamespace, OCloudSite, ODUFunction, ORUFunction, Sector, Site]"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Get all topology entities with invalid scopeFilter (attribute not prefixed with @)"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/OCUCPFunction/entities?scopeFilter=/attributes[contains(gNBCUName,'Cucp-1')]"
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Grammar error",
                "details": "no viable alternative at input '[contains(gNBCUName' at line 1:21"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Get all topology entities of type OCUUPFunction with invalid offset (greater than total count)."
        request {
            method GET()
            url "topology-inventory/v1alpha11/domains/RAN/entity-types/OCUUPFunction/entities?offset=10000"
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
