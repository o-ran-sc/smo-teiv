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
        description "SUCCESS - 200: Get topology relationships of type MANAGEDELEMENT_MANAGES_GNBDUFUNCTION(OneToMany)."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships"
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
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,GNBDUFunction=10",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10",
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION=661A89AD3C2702233CD9E96E97E738C05C35EC5FDF32DC78D149B773726350067315B72448D004C938BCD0263F0C4BCCC8A5F9CDD145B9B740983D1523664328"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,GNBDUFunction=19",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19",
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION=6BD25E5C8FB7842F69010736253CC47F43535D7238E9E9A03E8092E8C019C83270DE47C96EF1049C40B83A130F9F129AE93B9C8538B6B004AE89BD0A098E48DD"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,GNBDUFunction=14",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14",
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION=6F7BFBD3DA2A9A592084C75242210A33C9DCF10CFCD53B761A6ACCD385132921679EC3C16394A4DEEE5883712C9719511388230151BA84FBF209DFCFB639E2EA"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9",
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION=9243B48F7D6A6CF471226915C74CF5FE4BDA6FA3CF7D897473007B46DF7FC50230BD6B8B4256116A6AFBF4D822CF9379EB56DE9490C1C0B54238263F2574B426"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=28,GNBDUFunction=28",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=28",
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION=ADD4A82DFBAF0409FA9D3C929A09314088627B447C733429D4EE7AAE2FFAEE4894F90826B6814B63431EC07140783C7861E463C5AF8330E29469D704675EAB43"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,GNBDUFunction=16",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16",
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION=D67357F682531C7B068486313B0FDAC3E719A166229520196FB9CE917E0236754226A5BCBF7BB7240E516D7ED3FEA852855EC3F121DD4BAFEC5646F2A37F57EE"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,GNBDUFunction=13",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13",
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION=DD9259A1B57FF2BB9DEC77C29DBFA4A5C49960D80622F603809ACA47E786DDD5C7ABD267D554A7C796477A9B2E02E072A8E682E4ED38F331BFB6DC3827CE4DB7"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500"
                },
                "first": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500"
                },
                "prev": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500"
                },
                "next": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500"
                },
                "last": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=500"
                },
                "totalCount": 7
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(7)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].aSide', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].bSide', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].aSide', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].bSide', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].aSide', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].bSide', byEquality())
                jsonPath('$.items[3].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].id', byEquality())
                jsonPath('$.items[3].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].aSide', byEquality())
                jsonPath('$.items[3].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].bSide', byEquality())
                jsonPath('$.items[4].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].id', byEquality())
                jsonPath('$.items[4].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].aSide', byEquality())
                jsonPath('$.items[4].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].bSide', byEquality())
                jsonPath('$.items[5].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].id', byEquality())
                jsonPath('$.items[5].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].aSide', byEquality())
                jsonPath('$.items[5].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].bSide', byEquality())
                jsonPath('$.items[6].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].id', byEquality())
                jsonPath('$.items[6].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].aSide', byEquality())
                jsonPath('$.items[6].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION[0].bSide', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get topology relationships of type NRSECTORCARRIER_USES_ANTENNACAPABILITY(ManyToOne)."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/relationship-types/NRSECTORCARRIER_USES_ANTENNACAPABILITY/relationships"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-ran:NRSECTORCARRIER_USES_ANTENNACAPABILITY": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,NodeSupport=1,SectorEquipmentFunction=1",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRSectorCarrier=1",
                                "id": "urn:o-ran:smo:teiv:sha512:NRSECTORCARRIER_USES_ANTENNACAPABILITY=11EDFC31E2BE240D3CB15DB1A3FA3B78C828524BC8FCA3365A615129A61A627C21DA8EBF6DD788CDBDEC668344D1F79A371749083D6AE04DDDD57CB4FA8C3ECB"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:NRSECTORCARRIER_USES_ANTENNACAPABILITY": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,NodeSupport=1,SectorEquipmentFunction=1",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRSectorCarrier=3",
                                "id": "urn:o-ran:smo:teiv:sha512:NRSECTORCARRIER_USES_ANTENNACAPABILITY=1B891FCC4F5479BC71127ED2EB43EA26AC3452F8C47792786373442C10BBC408FE5B779BF1CF732C81220803342F4FB969E348F9C5CEEDEC78F9764E186C633F"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:NRSECTORCARRIER_USES_ANTENNACAPABILITY": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,NodeSupport=1,SectorEquipmentFunction=1",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRSectorCarrier=2",
                                "id": "urn:o-ran:smo:teiv:sha512:NRSECTORCARRIER_USES_ANTENNACAPABILITY=27DF07D016FE349EC565DE2FB09303EE7D8700346624046F79D8DAC176E7FA221E918E3030758B51931C430919E14FD7D16720460F6E1585000C72874A1641DA"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/relationship-types/NRSECTORCARRIER_USES_ANTENNACAPABILITY/relationships?offset=0&limit=500"
                },
                "first": {
                    "href": "/domains/RAN/relationship-types/NRSECTORCARRIER_USES_ANTENNACAPABILITY/relationships?offset=0&limit=500"
                },
                "prev": {
                    "href": "/domains/RAN/relationship-types/NRSECTORCARRIER_USES_ANTENNACAPABILITY/relationships?offset=0&limit=500"
                },
                "next": {
                    "href": "/domains/RAN/relationship-types/NRSECTORCARRIER_USES_ANTENNACAPABILITY/relationships?offset=0&limit=500"
                },
                "last": {
                    "href": "/domains/RAN/relationship-types/NRSECTORCARRIER_USES_ANTENNACAPABILITY/relationships?offset=0&limit=500"
                },
                "totalCount": 3
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get topology relationships of type ANTENNAMODULE_SERVES_ANTENNACAPABILITY(ManyToMany)."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_EQUIPMENT_RAN/relationship-types/ANTENNAMODULE_SERVES_ANTENNACAPABILITY/relationships"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_ANTENNACAPABILITY": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,NodeSupport=1,SectorEquipmentFunction=1",
                                "aSide": "urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7",
                                "id": "urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_SERVES_ANTENNACAPABILITY=8940999E2069725B463052BC35572FDB888C7B734459EE78A01B9F91E2607D87356425BC8EFF0B1C9057D852A4D3F9E1B09479D32FEE68C65EF2821B65F7BD80"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_ANTENNACAPABILITY": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,NodeSupport=1,SectorEquipmentFunction=1",
                                "aSide": "urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A",
                                "id": "urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_SERVES_ANTENNACAPABILITY=ABD52B030DF1169F9F41C898913EF30F7BB5741F53352F482310B280C90AC569B7D31D52A2BB41F1F0099AE1EDD56CACF0B285D145A5584D376DD45DED1E2D65"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ANTENNAMODULE_SERVES_ANTENNACAPABILITY/relationships?offset=0&limit=500"
                },
                "first": {
                    "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ANTENNAMODULE_SERVES_ANTENNACAPABILITY/relationships?offset=0&limit=500"
                },
                "prev": {
                    "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ANTENNAMODULE_SERVES_ANTENNACAPABILITY/relationships?offset=0&limit=500"
                },
                "next": {
                    "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ANTENNAMODULE_SERVES_ANTENNACAPABILITY/relationships?offset=0&limit=500"
                },
                "last": {
                    "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ANTENNAMODULE_SERVES_ANTENNACAPABILITY/relationships?offset=0&limit=500"
                },
                "totalCount": 2
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get topology relationships of type MANAGEDELEMENT_MANAGES_GNBDUFUNCTION with offset=1 and limit=1."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=1&limit=1"
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
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,GNBDUFunction=19",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19",
                                "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_GNBDUFUNCTION=6BD25E5C8FB7842F69010736253CC47F43535D7238E9E9A03E8092E8C019C83270DE47C96EF1049C40B83A130F9F129AE93B9C8538B6B004AE89BD0A098E48DD"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=1&limit=1"
                },
                "first": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=1"
                },
                "prev": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=0&limit=1"
                },
                "next": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=2&limit=1"
                },
                "last": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=6&limit=1"
                },
                "totalCount": 7
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get topology relationships of type ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS where used-entityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters id is 'Rel_OneToOne_SameEntity_LongEntityType1_LongEntityType2'."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/TEST/relationship-types/ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships?offset=0&limit=100&scopeFilter=/used-entityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters[@id='LongEntityType1']"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "test-built-in-module:ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS": [
                            {
                                "bSide": "LongEntityType2",
                                "aSide": "LongEntityType1",
                                "id": "Rel_OneToOne_SameEntity_LongEntityType1_LongEntityType2"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/TEST/relationship-types/ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships?offset=0&limit=100&scopeFilter=/used-entityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters[@id='LongEntityType1']"
                },
                "first": {
                    "href": "/domains/TEST/relationship-types/ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships?offset=0&limit=100&scopeFilter=/used-entityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters[@id='LongEntityType1']"
                },
                "prev": {
                    "href": "/domains/TEST/relationship-types/ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships?offset=0&limit=100&scopeFilter=/used-entityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters[@id='LongEntityType1']"
                },
                "next": {
                    "href": "/domains/TEST/relationship-types/ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships?offset=0&limit=100&scopeFilter=/used-entityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters[@id='LongEntityType1']"
                },
                "last": {
                    "href": "/domains/TEST/relationship-types/ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships?offset=0&limit=100&scopeFilter=/used-entityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters[@id='LongEntityType1']"
                },
                "totalCount": 1
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get topology relationships of type ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS where used-by-entityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters id is 'Rel_OneToOne_SameEntity_LongEntityType1_LongEntityType2'."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/TEST/relationship-types/ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships?offset=0&limit=100&scopeFilter=/used-by-entityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters[@id='LongEntityType2']"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "test-built-in-module:ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS": [
                            {
                                "bSide": "LongEntityType2",
                                "aSide": "LongEntityType1",
                                "id": "Rel_OneToOne_SameEntity_LongEntityType1_LongEntityType2"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/TEST/relationship-types/ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships?offset=0&limit=100&scopeFilter=/used-by-entityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters[@id='LongEntityType2']"
                },
                "first": {
                    "href": "/domains/TEST/relationship-types/ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships?offset=0&limit=100&scopeFilter=/used-by-entityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters[@id='LongEntityType2']"
                },
                "prev": {
                    "href": "/domains/TEST/relationship-types/ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships?offset=0&limit=100&scopeFilter=/used-by-entityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters[@id='LongEntityType2']"
                },
                "next": {
                    "href": "/domains/TEST/relationship-types/ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships?offset=0&limit=100&scopeFilter=/used-by-entityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters[@id='LongEntityType2']"
                },
                "last": {
                    "href": "/domains/TEST/relationship-types/ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships?offset=0&limit=100&scopeFilter=/used-by-entityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters[@id='LongEntityType2']"
                },
                "totalCount": 1
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get topology relationships of type SECTOR_GROUPS_NRCELLDU where grouped-nrCellDu id is 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1'."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_EQUIPMENT_RAN/relationship-types/SECTOR_GROUPS_NRCELLDU/relationships?offset=0&limit=100&scopeFilter=/grouped-nrCellDu[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1']"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                  {
                    "o-ran-smo-teiv-ran:SECTOR_GROUPS_NRCELLDU": [
                      {
                        "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1",
                        "aSide": "Sector=2",
                        "id": "urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_NRCELLDU=4BBE73F685A98EF799968ACFE76F376D795F4CC3B56A6B867642048CDF4C1B8E323430EA7C6C38E4031FB891158763CC5459A8704E1A9FBFBD53CE8AD23BF463"
                      }
                    ]
                  }
                ],
                "self": {
                  "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/SECTOR_GROUPS_NRCELLDU/relationships?offset=0&limit=100&scopeFilter=/grouped-nrCellDu[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1']"
                },
                "first": {
                  "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/SECTOR_GROUPS_NRCELLDU/relationships?offset=0&limit=100&scopeFilter=/grouped-nrCellDu[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1']"
                },
                "prev": {
                  "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/SECTOR_GROUPS_NRCELLDU/relationships?offset=0&limit=100&scopeFilter=/grouped-nrCellDu[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1']"
                },
                "next": {
                  "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/SECTOR_GROUPS_NRCELLDU/relationships?offset=0&limit=100&scopeFilter=/grouped-nrCellDu[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1']"
                },
                "last": {
                  "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/SECTOR_GROUPS_NRCELLDU/relationships?offset=0&limit=100&scopeFilter=/grouped-nrCellDu[@id='urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1']"
                },
                "totalCount": 1
                }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(1)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:SECTOR_GROUPS_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:SECTOR_GROUPS_NRCELLDU[0].aSide', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:SECTOR_GROUPS_NRCELLDU[0].bSide', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get topology relationships of type SECTOR_GROUPS_NRCELLDU where relation id is 'urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_NRCELLDU=4BBE73F685A98EF799968ACFE76F376D795F4CC3B56A6B867642048CDF4C1B8E323430EA7C6C38E4031FB891158763CC5459A8704E1A9FBFBD53CE8AD23BF463'."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_EQUIPMENT_RAN/relationship-types/SECTOR_GROUPS_NRCELLDU/relationships?offset=0&limit=100&scopeFilter=/SECTOR_GROUPS_NRCELLDU[@id='urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_NRCELLDU=4BBE73F685A98EF799968ACFE76F376D795F4CC3B56A6B867642048CDF4C1B8E323430EA7C6C38E4031FB891158763CC5459A8704E1A9FBFBD53CE8AD23BF463']"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                  {
                    "o-ran-smo-teiv-ran:SECTOR_GROUPS_NRCELLDU": [
                      {
                        "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1",
                        "aSide": "Sector=2",
                        "id": "urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_NRCELLDU=4BBE73F685A98EF799968ACFE76F376D795F4CC3B56A6B867642048CDF4C1B8E323430EA7C6C38E4031FB891158763CC5459A8704E1A9FBFBD53CE8AD23BF463"
                      }
                    ]
                  }
                ],
                "self": {
                  "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/SECTOR_GROUPS_NRCELLDU/relationships?offset=0&limit=100&scopeFilter=/SECTOR_GROUPS_NRCELLDU[@id='urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_NRCELLDU=4BBE73F685A98EF799968ACFE76F376D795F4CC3B56A6B867642048CDF4C1B8E323430EA7C6C38E4031FB891158763CC5459A8704E1A9FBFBD53CE8AD23BF463']"
                },
                "first": {
                  "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/SECTOR_GROUPS_NRCELLDU/relationships?offset=0&limit=100&scopeFilter=/SECTOR_GROUPS_NRCELLDU[@id='urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_NRCELLDU=4BBE73F685A98EF799968ACFE76F376D795F4CC3B56A6B867642048CDF4C1B8E323430EA7C6C38E4031FB891158763CC5459A8704E1A9FBFBD53CE8AD23BF463']"
                },
                "prev": {
                  "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/SECTOR_GROUPS_NRCELLDU/relationships?offset=0&limit=100&scopeFilter=/SECTOR_GROUPS_NRCELLDU[@id='urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_NRCELLDU=4BBE73F685A98EF799968ACFE76F376D795F4CC3B56A6B867642048CDF4C1B8E323430EA7C6C38E4031FB891158763CC5459A8704E1A9FBFBD53CE8AD23BF463']"
                },
                "next": {
                  "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/SECTOR_GROUPS_NRCELLDU/relationships?offset=0&limit=100&scopeFilter=/SECTOR_GROUPS_NRCELLDU[@id='urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_NRCELLDU=4BBE73F685A98EF799968ACFE76F376D795F4CC3B56A6B867642048CDF4C1B8E323430EA7C6C38E4031FB891158763CC5459A8704E1A9FBFBD53CE8AD23BF463']"
                },
                "last": {
                  "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/SECTOR_GROUPS_NRCELLDU/relationships?offset=0&limit=100&scopeFilter=/SECTOR_GROUPS_NRCELLDU[@id='urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_NRCELLDU=4BBE73F685A98EF799968ACFE76F376D795F4CC3B56A6B867642048CDF4C1B8E323430EA7C6C38E4031FB891158763CC5459A8704E1A9FBFBD53CE8AD23BF463']"
                },
                "totalCount": 1
                }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(1)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:SECTOR_GROUPS_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:SECTOR_GROUPS_NRCELLDU[0].aSide', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:SECTOR_GROUPS_NRCELLDU[0].bSide', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get topology relationships for same entity long relationship name of type ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/TEST/relationship-types/ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "test-built-in-module:ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS": [
                            {
                                "bSide": "LongEntityType2",
                                "aSide": "LongEntityType1",
                                "id": "Rel_OneToOne_SameEntity_LongEntityType1_LongEntityType2"
                            }
                        ]
                    },
                    {
                        "test-built-in-module:ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS": [
                            {
                                "bSide": "LongEntityType3",
                                "aSide": "LongEntityType2",
                                "id": "Rel_OneToOne_SameEntity_LongEntityType2_LongEntityType3"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/TEST/relationship-types/ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships?offset=0&limit=500"
                },
                "first": {
                    "href": "/domains/TEST/relationship-types/ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships?offset=0&limit=500"
                },
                "prev": {
                    "href": "/domains/TEST/relationship-types/ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships?offset=0&limit=500"
                },
                "next": {
                    "href": "/domains/TEST/relationship-types/ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships?offset=0&limit=500"
                },
                "last": {
                    "href": "/domains/TEST/relationship-types/ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships?offset=0&limit=500"
                },
                "totalCount": 2
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get empty items array when no relationship exists of given type."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ENODEBFUNCTION/relationships"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_GNBCUCPFUNCTION": []
                    }
                ],
                "self": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ENODEBFUNCTION/relationships?offset=0&limit=500"
                },
                "first": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ENODEBFUNCTION/relationships?offset=0&limit=500"
                },
                "prev": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ENODEBFUNCTION/relationships?offset=0&limit=500"
                },
                "next": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ENODEBFUNCTION/relationships?offset=0&limit=500"
                },
                "last": {
                    "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ENODEBFUNCTION/relationships?offset=0&limit=500"
                },
                "totalCount": 0
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get relationship by type checking total count"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/relationship-types/GNBDUFUNCTION_PROVIDES_NRCELLDU/relationships"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU": [
                           {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9",
                                "id": "urn:o-ran:smo:teiv:sha512:GNBDUFUNCTION_PROVIDES_NRCELLDU=4E40BE000AFEA418CE1B9ED9E21D38DA51772175BD498BE825D9EA362F9B7393C36AB72F6FDEE702439143D578268A2E84719A9352C8EA70F847B7B7664E047C"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU": [
                           {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=3",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9",
                                "id": "urn:o-ran:smo:teiv:sha512:GNBDUFUNCTION_PROVIDES_NRCELLDU=714C1B73945C298CAA03FE0D800053CDD1C571BBF375DC647B9F23FDA861CEB369832A3593BB1AA4B8A7245AD187ED24ADDF6FB147130827CDC17BA8370C4838"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU": [
                           {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,GNBDUFunction=19,NRCellDU=93",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,GNBDUFunction=19",
                                "id": "urn:o-ran:smo:teiv:sha512:GNBDUFUNCTION_PROVIDES_NRCELLDU=7723E5D5B3332E0890EAA620C77A6A47065E15A2EA28AD83F3B3CFEA5A7E3BB5965AE78890F1BF000EAA89BF8DE209E506192BF5EA6871426603ED76CBFAF088"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU": [
                           {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=2",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9",
                                "id": "urn:o-ran:smo:teiv:sha512:GNBDUFUNCTION_PROVIDES_NRCELLDU=78ECC09D4832328976EF0F9C19699EE05D98E3837368D386AE39AD027543494AC620086BD2A7403DACFAA7B474B3DEBD313E0906F1EDE7FA2B584E16542A706A"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU": [
                           {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,GNBDUFunction=19,NRCellDU=92",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,GNBDUFunction=19",
                                "id": "urn:o-ran:smo:teiv:sha512:GNBDUFUNCTION_PROVIDES_NRCELLDU=DDECCEFB8831FA4EB21B121BA35EAB07ED8D841B5A38580C5F3AD11E66FE73D2FC42E823C6C73288860C7562B610C3D07B6C39FD386171A3BE622096F4B3D006"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/relationship-types/GNBDUFUNCTION_PROVIDES_NRCELLDU/relationships?offset=0&limit=500"
                },
                "first": {
                    "href": "/domains/RAN/relationship-types/GNBDUFUNCTION_PROVIDES_NRCELLDU/relationships?offset=0&limit=500"
                },
                "prev": {
                    "href": "/domains/RAN/relationship-types/GNBDUFUNCTION_PROVIDES_NRCELLDU/relationships?offset=0&limit=500"
                },
                "next": {
                    "href": "/domains/RAN/relationship-types/GNBDUFUNCTION_PROVIDES_NRCELLDU/relationships?offset=0&limit=500"
                },
                "last": {
                    "href": "/domains/RAN/relationship-types/GNBDUFUNCTION_PROVIDES_NRCELLDU/relationships?offset=0&limit=500"
                },
                "totalCount": 5
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(5)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU[0].aSide', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU[0].bSide', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU[0].aSide', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU[0].bSide', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU[0].aSide', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU[0].bSide', byEquality())
                jsonPath('$.items[3].o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[3].o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU[0].aSide', byEquality())
                jsonPath('$.items[3].o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU[0].bSide', byEquality())
                jsonPath('$.items[4].o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[4].o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU[0].aSide', byEquality())
                jsonPath('$.items[4].o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU[0].bSide', byEquality())
                jsonPath('$.totalCount', byEquality())
            }
        }
    },
    Contract.make {
        description "ERROR - 400: Get topology relationships of type MANAGEDELEMENT_MANAGES_GNBDUFUNCTION with invalid offset (greater than total count)."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships?offset=1000"
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
    },
    Contract.make {
        description "ERROR - 400: Get topology relationships of type SECTOR_GROUPS_NRCELLDU with wrong domain."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/OAM/relationship-types/SECTOR_GROUPS_NRCELLDU/relationships"
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Unknown relationship type",
                "details": "Relationship type SECTOR_GROUPS_NRCELLDU is not part of the domain OAM, known relationship types: []"
            }''')
        }
    }
]
