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
package contracts.groups

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "SUCCESS - 200: Get the members of a dynamic group with getEntitiesByDomain queryType"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440050/members?offset=0&limit=2")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body ('''{
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
                    }
                ],
                "self": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440050/members?offset=0&limit=2"
                },
                "first": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440050/members?offset=0&limit=2"
                },
                "prev": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440050/members?offset=0&limit=2"
                },
                "next": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440050/members?offset=2&limit=2"
                },
                "last": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440050/members?offset=34&limit=2"
                },
                "totalCount": 35
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get the members of a dynamic group with getEntitiesByType queryType"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440001/members?offset=0&limit=2")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body ('''{
                "items": [
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10",
                                "sourceIds": [
                                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10",
                                    "urn:cmHandle:72FDA73D085F138FECC974CB91F1450E"
                                ]
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,ODUFunction=13",
                                "sourceIds": [
                                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,ODUFunction=13",
                                    "urn:cmHandle:E5196035D0B49A65B00EAA392B4EE155"
                                ]
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440001/members?offset=0&limit=2"
                },
                "first": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440001/members?offset=0&limit=2"
                },
                "prev": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440001/members?offset=0&limit=2"
                },
                "next": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440001/members?offset=2&limit=2"
                },
                "last": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440001/members?offset=6&limit=2"
                },
                "totalCount": 7
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
        description "SUCCESS - 200: Get the members of a dynamic group with getRelationshipsForEntityId queryType"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440002/members")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body ('''{
                "items": [
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9",
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=9243B48F7D6A6CF471226915C74CF5FE4BDA6FA3CF7D897473007B46DF7FC50230BD6B8B4256116A6AFBF4D822CF9379EB56DE9490C1C0B54238263F2574B426"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRSECTORCARRIER": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRSectorCarrier=3",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9",
                                "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRSECTORCARRIER=BD87BC547A6731B2905A989EBA493810C74258337C49BBB288F4F55734D28B4E40D9C719EC3564348253905BD93EC78EB7C88F2297FF20778911635E94800F74"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRSECTORCARRIER": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRSectorCarrier=1",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9",
                                "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRSECTORCARRIER=BE61426CAEA457C85D8B551A1A75BFA157BBCE6F143110661C4C9D406A7AAF22D8522515CE924CFB3A9E54E68588A45D3A51065BD24ADBA62CC0FDA761AEE2FC"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRSECTORCARRIER": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRSectorCarrier=2",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9",
                                "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRSECTORCARRIER=C0E73797DB4599AB9ECACFC1FFE3543C92926070ECFBE77E7C15BA99DAFBB1D69352533D3DE5EB2D3D3CC84DAD51B242CB0FC594FF9E8B73C3B42106B0F9AF46"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9",
                                "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=4E40BE000AFEA418CE1B9ED9E21D38DA51772175BD498BE825D9EA362F9B7393C36AB72F6FDEE702439143D578268A2E84719A9352C8EA70F847B7B7664E047C"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=3",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9",
                                "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=714C1B73945C298CAA03FE0D800053CDD1C571BBF375DC647B9F23FDA861CEB369832A3593BB1AA4B8A7245AD187ED24ADDF6FB147130827CDC17BA8370C4838"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9",
                                "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=78ECC09D4832328976EF0F9C19699EE05D98E3837368D386AE39AD027543494AC620086BD2A7403DACFAA7B474B3DEBD313E0906F1EDE7FA2B584E16542A706A"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-physical-ran:PHYSICALAPPLIANCE_SERVES_ODUFUNCTION": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9",
                                "aSide": "urn:o-ran:smo:teiv:PhysicalAppliance=135",
                                "id": "urn:o-ran:smo:teiv:PHYSICALAPPLIANCE_SERVES_ODUFUNCTION=135"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440002/members?offset=0&limit=500"
                },
                "first": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440002/members?offset=0&limit=500"
                },
                "prev": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440002/members?offset=0&limit=500"
                },
                "next": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440002/members?offset=0&limit=500"
                },
                "last": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440002/members?offset=0&limit=500"
                },
                "totalCount": 8
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(8)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRSECTORCARRIER[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRSECTORCARRIER[0].id', byEquality())
                jsonPath('$.items[3].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRSECTORCARRIER[0].id', byEquality())
                jsonPath('$.items[4].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[5].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[6].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[7].o-ran-smo-teiv-rel-physical-ran:PHYSICALAPPLIANCE_SERVES_ODUFUNCTION[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get the members of a dynamic group with getRelationshipsByType queryType"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440003/members?offset=0&limit=2")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body ('''{
                "items": [
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10",
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=661A89AD3C2702233CD9E96E97E738C05C35EC5FDF32DC78D149B773726350067315B72448D004C938BCD0263F0C4BCCC8A5F9CDD145B9B740983D1523664328"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19",
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=6BD25E5C8FB7842F69010736253CC47F43535D7238E9E9A03E8092E8C019C83270DE47C96EF1049C40B83A130F9F129AE93B9C8538B6B004AE89BD0A098E48DD"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440003/members?offset=0&limit=2"
                },
                "first": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440003/members?offset=0&limit=2"
                },
                "prev": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440003/members?offset=0&limit=2"
                },
                "next": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440003/members?offset=2&limit=2"
                },
                "last": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440003/members?offset=6&limit=2"
                },
                "totalCount": 7
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
        description "SUCCESS - 200: Get the members of a static group"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/members?offset=0&limit=10")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body ('''{
                "items": [
                    {
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-oam:ManagedElement": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-oam:ManagedElement": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14"
                            }
                        ]
                    },
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
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_ANTENNACAPABILITY": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_SERVES_ANTENNACAPABILITY=ABD52B030DF1169F9F41C898913EF30F7BB5741F53352F482310B280C90AC569B7D31D52A2BB41F1F0099AE1EDD56CACF0B285D145A5584D376DD45DED1E2D65"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=4E40BE000AFEA418CE1B9ED9E21D38DA51772175BD498BE825D9EA362F9B7393C36AB72F6FDEE702439143D578268A2E84719A9352C8EA70F847B7B7664E047C"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU": [
                            {
                                "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=78ECC09D4832328976EF0F9C19699EE05D98E3837368D386AE39AD027543494AC620086BD2A7403DACFAA7B474B3DEBD313E0906F1EDE7FA2B584E16542A706A"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/members?offset=0&limit=10"
                },
                "first": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/members?offset=0&limit=10"
                },
                "prev": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/members?offset=0&limit=10"
                },
                "next": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/members?offset=0&limit=10"
                },
                "last": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201/members?offset=0&limit=10"
                },
                "totalCount": 9
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(9)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-oam:ManagedElement[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-oam:ManagedElement[0].id', byEquality())
                jsonPath('$.items[3].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[4].o-ran-smo-teiv-ran:NRCellDU[0].id', byEquality())
                jsonPath('$.items[5].o-ran-smo-teiv-ran:ODUFunction[0].id', byEquality())
                jsonPath('$.items[6].o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_ANTENNACAPABILITY[0].id', byEquality())
                jsonPath('$.items[7].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[8].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].id', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get the members of a static group when no providedMembersIds are present in the network"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440341/members?offset=0&limit=10")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body ('''{
                "items": [],
                "self": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440341/members?offset=0&limit=10"
                },
                "first": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440341/members?offset=0&limit=10"
                },
                "prev": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440341/members?offset=0&limit=10"
                },
                "next": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440341/members?offset=0&limit=10"
                },
                "last": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440341/members?offset=0&limit=10"
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
        description "SUCCESS - 200: Get the members of a static group when topology type is invalid (invalid module and topology name)"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440351/members?offset=0&limit=10")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body ('''{
                "items": [],
                "self": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440351/members?offset=0&limit=10"
                },
                "first": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440351/members?offset=0&limit=10"
                },
                "prev": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440351/members?offset=0&limit=10"
                },
                "next": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440351/members?offset=0&limit=10"
                },
                "last": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440351/members?offset=0&limit=10"
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
        description "SUCCESS - 200: Get the members of a static group when topology type is invalid (topologytype consist of three parts)"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440351/members?offset=0&limit=10")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body ('''{
                "items": [],
                "self": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440351/members?offset=0&limit=10"
                },
                "first": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440351/members?offset=0&limit=10"
                },
                "prev": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440351/members?offset=0&limit=10"
                },
                "next": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440351/members?offset=0&limit=10"
                },
                "last": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440351/members?offset=0&limit=10"
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
        description "SUCCESS - 200: Get the members of a dynamic group when valid target filter & valid scope filter)"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440217/members?offset=0&limit=10")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body ('''{
                "items": [
                    {
                        "o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_NRCELLDU": [
                        {
                            "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1",
                            "aSide": "urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A",
                            "id": "urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_SERVES_NRCELLDU=ABD52B030DF1169F9F41C898913EF30F7BB5741F53352F482310B280C90AC569B7D31D52A2BB41F1F0099AE1EDD56CACF0B285D145A5584D376DD45DED1E2D65",
                            "sourceIds": [
                                "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1",
                                "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1",
                                "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=1,RetSubUnit=1",
                                "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616",
                                "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,NodeSupport=1,SectorEquipmentFunction=1",
                                "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"
                            ]
                        }
                        ]
                    }
                ],
                "self": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440217/members?offset=0&limit=10"
                },
                "first": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440217/members?offset=0&limit=10"
                },
                "prev": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440217/members?offset=0&limit=10"
                },
                "next": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440217/members?offset=0&limit=10"
                },
                "last": {
                    "href": "/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440217/members?offset=0&limit=10"
                },
                "totalCount": 1
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(1)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_NRCELLDU[0].aSide', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_NRCELLDU[0].bSide', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_NRCELLDU[0].sourceIds[0]', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_NRCELLDU[0].sourceIds[1]', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_NRCELLDU[0].sourceIds[2]', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_NRCELLDU[0].sourceIds[3]', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_NRCELLDU[0].sourceIds[4]', byEquality())
            }
        }
    },
    Contract.make {
        description "ERROR - 404: Get a static group by id that does not exists"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440361/members?offset=0&limit=10")
        }
        response {
            status NOT_FOUND()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "status": "NOT_FOUND",
                "message": "Resource Not Found",
                "details": "The requested group is not found"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 404: Get the members of a dynamic group with getRelationshipsForEntityId queryType where entity id doesn't exists"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440004/members")
        }
        response {
            status NOT_FOUND()
            headers {
                contentType('application/problem+json')
            }
            body ('''{
                "status": "NOT_FOUND",
                "message": "Resource Not Found",
                "details": "The requested resource is not found. ID: urn:3gpp:dn:non-existing-odufunction"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Get the members of a dynamic group with wrong scope filter"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440005/members")
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Grammar error",
                "details": "Invalid data in scopeFilter"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 404: Get the members of a group that does not exists"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/groups/non-existing-group/members")
        }
        response {
            status NOT_FOUND()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "status": "NOT_FOUND",
                "message": "Resource Not Found",
                "details": "The requested group is not found"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: No target filter exists & invalid scope filter"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440218/members")
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Filter Error",
                "details": "TopologyObjects given in scopeFilter and targetFilter are not matching"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Invalid target filter & invalid scope filter"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440220/members")
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid target filter, only relationship conditions can be provided",
                "details": "NRCellDU is not a valid relation"
            }''')
        }
    }
]
