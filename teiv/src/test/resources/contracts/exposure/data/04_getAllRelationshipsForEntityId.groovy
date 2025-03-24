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
        description "SUCCESS - 200: Get all relationships for entity type NRCellDU with ID urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1/relationships?offset=0&limit=100"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
              "items": [
                {
                  "o-ran-smo-teiv-ran:NRCELLDU_USES_NRSECTORCARRIER": [
                    {
                      "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRSectorCarrier=1",
                      "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1",
                      "id": "urn:o-ran:smo:teiv:sha512:NRCELLDU_USES_NRSECTORCARRIER=7B9425BBD6977FEA6C180F6078CFBAEBE65400223B29E0EFA4F38424FAD66C690806778909177ECF1457CAC18E5BCF6FA4F24E3ECE524C89DE68108708D6D876"
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
                  "o-ran-smo-teiv-ran:SECTOR_GROUPS_NRCELLDU": [
                    {
                      "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1",
                      "aSide": "urn:Sector=2",
                      "id": "urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_NRCELLDU=4BBE73F685A98EF799968ACFE76F376D795F4CC3B56A6B867642048CDF4C1B8E323430EA7C6C38E4031FB891158763CC5459A8704E1A9FBFBD53CE8AD23BF463"
                    }
                  ]
                }
              ],
              "self": {
                "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1/relationships?offset=0&limit=100"
              },
              "first": {
                "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1/relationships?offset=0&limit=100"
              },
              "prev": {
                "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1/relationships?offset=0&limit=100"
              },
              "next": {
                "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1/relationships?offset=0&limit=100"
              },
              "last": {
                "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1/relationships?offset=0&limit=100"
              },
              "totalCount": 3
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(3)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCELLDU_USES_NRSECTORCARRIER[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCELLDU_USES_NRSECTORCARRIER[0].aSide', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCELLDU_USES_NRSECTORCARRIER[0].bSide', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].aSide', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].bSide', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:SECTOR_GROUPS_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:SECTOR_GROUPS_NRCELLDU[0].aSide', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:SECTOR_GROUPS_NRCELLDU[0].bSide', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all relationships for entity type NRCellDU  with ID urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1 & targetFilter=/ODUFUNCTION_PROVIDES_NRCELLDU."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/ODUFUNCTION_PROVIDES_NRCELLDU"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9",
                                "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=4E40BE000AFEA418CE1B9ED9E21D38DA51772175BD498BE825D9EA362F9B7393C36AB72F6FDEE702439143D578268A2E84719A9352C8EA70F847B7B7664E047C"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/ODUFUNCTION_PROVIDES_NRCELLDU"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/ODUFUNCTION_PROVIDES_NRCELLDU"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/ODUFUNCTION_PROVIDES_NRCELLDU"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/ODUFUNCTION_PROVIDES_NRCELLDU"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/ODUFUNCTION_PROVIDES_NRCELLDU"
                },
                "totalCount": 1
}''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all relationships for entity type NRCellDU  with ID urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1 & targetFilter=/ODUFUNCTION_PROVIDES_NRCELLDU;/SECTOR_GROUPS_NRCELLDU."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/ODUFUNCTION_PROVIDES_NRCELLDU;/SECTOR_GROUPS_NRCELLDU"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
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
                        "o-ran-smo-teiv-ran:SECTOR_GROUPS_NRCELLDU": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1",
                                "aSide": "urn:Sector=2",
                                "id": "urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_NRCELLDU=4BBE73F685A98EF799968ACFE76F376D795F4CC3B56A6B867642048CDF4C1B8E323430EA7C6C38E4031FB891158763CC5459A8704E1A9FBFBD53CE8AD23BF463"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/ODUFUNCTION_PROVIDES_NRCELLDU;/SECTOR_GROUPS_NRCELLDU"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/ODUFUNCTION_PROVIDES_NRCELLDU;/SECTOR_GROUPS_NRCELLDU"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/ODUFUNCTION_PROVIDES_NRCELLDU;/SECTOR_GROUPS_NRCELLDU"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/ODUFUNCTION_PROVIDES_NRCELLDU;/SECTOR_GROUPS_NRCELLDU"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/ODUFUNCTION_PROVIDES_NRCELLDU;/SECTOR_GROUPS_NRCELLDU"
                },
                "totalCount": 2
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all relationships for entity type ODUFunction with ID urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1. & scopeFilter=/provided-nrCellDu[@id = 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1']"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9/relationships?offset=0&limit=100&scopeFilter=/provided-nrCellDu[@id = 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1']"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9",
                                "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=4E40BE000AFEA418CE1B9ED9E21D38DA51772175BD498BE825D9EA362F9B7393C36AB72F6FDEE702439143D578268A2E84719A9352C8EA70F847B7B7664E047C"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9/relationships?offset=0&limit=100&scopeFilter=/provided-nrCellDu[@id = 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1']"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9/relationships?offset=0&limit=100&scopeFilter=/provided-nrCellDu[@id = 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1']"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9/relationships?offset=0&limit=100&scopeFilter=/provided-nrCellDu[@id = 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1']"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9/relationships?offset=0&limit=100&scopeFilter=/provided-nrCellDu[@id = 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1']"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9/relationships?offset=0&limit=100&scopeFilter=/provided-nrCellDu[@id = 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1']"
                },
                "totalCount": 1
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all relationships for entity type NRCellDU with ID urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=3 with limit as 2."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=3/relationships?limit=2"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "o-ran-smo-teiv-ran:NRCELLDU_USES_NRSECTORCARRIER": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRSectorCarrier=3",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=3",
                                "id": "urn:o-ran:smo:teiv:sha512:NRCELLDU_USES_NRSECTORCARRIER=950ED4540349F9859CEA9E47884A28CD567BDD2505A3C5335C8851A7AADF2AF65542157BB42D607EE3847E4223D76DE88B90762D0590E48693822FD6DCAE60CD"
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
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=3/relationships?offset=0&limit=2"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=3/relationships?offset=0&limit=2"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=3/relationships?offset=0&limit=2"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=3/relationships?offset=2&limit=2"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=3/relationships?offset=2&limit=2"
                },
                "totalCount": 3
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCELLDU_USES_NRSECTORCARRIER[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCELLDU_USES_NRSECTORCARRIER[0].aSide', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:NRCELLDU_USES_NRSECTORCARRIER[0].bSide', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].aSide', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].bSide', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all relationships for long entity type name with id as urn:LongEntityType1 (OneToOne, ManyToOne, ManyToMany and Same Entity with aSide of relationship)"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType1/relationships"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "test-built-in-module:ENTITYTYPEA_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS": [
                            {
                                "bSide": "urn:LongEntityType1",
                                "aSide": "urn:EntityType1",
                                "id": "urn:RelId_OneToOne_EntityType1_LongEntityType1"
                            }
                        ]
                    },
                    {
                        "test-built-in-module:ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS": [
                            {
                                "bSide": "urn:LongEntityType2",
                                "aSide": "urn:LongEntityType1",
                                "id": "urn:Rel_OneToOne_SameEntity_LongEntityType1_LongEntityType2"
                            }
                        ]
                    },
                    {
                        "test-built-in-module:ENTITYTYPEA_GROUPS_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS": [
                            {
                                "bSide": "urn:LongEntityType1",
                                "aSide": "urn:EntityType2",
                                "id": "urn:Rel_ManyToOne_EntityType2_LongEntityType1"
                            }
                        ]
                    },
                    {
                        "test-built-in-module:ENTITYTYPEA_INSTALLED_AT_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS": [
                            {
                                "bSide": "urn:LongEntityType1",
                                "aSide": "urn:EntityType1",
                                "id": "urn:Rel_ManyToMany_EntityType1_LongEntityType1"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType1/relationships?offset=0&limit=500"
                },
                "first": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType1/relationships?offset=0&limit=500"
                },
                "prev": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType1/relationships?offset=0&limit=500"
                },
                "next": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType1/relationships?offset=0&limit=500"
                },
                "last": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType1/relationships?offset=0&limit=500"
                },
                "totalCount": 4
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all relationships for long entity type name with id as urn:LongEntityType2  (OneToMany and Same Entity with bSide of relationship)"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType2/relationships"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
            "items": [
                {
                    "test-built-in-module:ENTITYTYPEA_PROVIDES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS": [
                        {
                            "bSide": "urn:LongEntityType2",
                            "aSide": "urn:EntityType2",
                            "id": "urn:Rel_OneToMany_EntityType2_LongEntityType2"
                        }
                    ]
                },
                {
                    "test-built-in-module:ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS": [
                        {
                            "bSide": "urn:LongEntityType2",
                            "aSide": "urn:LongEntityType1",
                            "id": "urn:Rel_OneToOne_SameEntity_LongEntityType1_LongEntityType2"
                        }
                    ]
                },
                {
                    "test-built-in-module:ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS": [
                        {
                            "bSide": "urn:LongEntityType3",
                            "aSide": "urn:LongEntityType2",
                            "id": "urn:Rel_OneToOne_SameEntity_LongEntityType2_LongEntityType3"
                        }
                    ]
                }
            ],
            "self": {
                "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType2/relationships?offset=0&limit=500"
            },
            "first": {
                "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType2/relationships?offset=0&limit=500"
            },
            "prev": {
                "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType2/relationships?offset=0&limit=500"
            },
            "next": {
                "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType2/relationships?offset=0&limit=500"
            },
            "last": {
                "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType2/relationships?offset=0&limit=500"
            },
            "totalCount": 3
        }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all relationships for long entity type name with id as urn:LongEntityType1 and with scope filter on sourceIds"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType1/relationships?scopeFilter=/sourceIds[@item = 'urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616']&targetFilter=/sourceIds"
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
                                "bSide": "urn:LongEntityType2",
                                "aSide": "urn:LongEntityType1",
                                "id": "urn:Rel_OneToOne_SameEntity_LongEntityType1_LongEntityType2"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType1/relationships?offset=0&limit=500&targetFilter=/sourceIds&scopeFilter=/sourceIds[@item = 'urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616']"
                },
                "first": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType1/relationships?offset=0&limit=500&targetFilter=/sourceIds&scopeFilter=/sourceIds[@item = 'urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616']"
                },
                "prev": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType1/relationships?offset=0&limit=500&targetFilter=/sourceIds&scopeFilter=/sourceIds[@item = 'urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616']"
                },
                "next": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType1/relationships?offset=0&limit=500&targetFilter=/sourceIds&scopeFilter=/sourceIds[@item = 'urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616']"
                },
                "last": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType1/relationships?offset=0&limit=500&targetFilter=/sourceIds&scopeFilter=/sourceIds[@item = 'urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616']"
                },
                "totalCount": 1
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all relationships for long entity type name with id as urn:LongEntityType3 and with scope filter on classifiers"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType3/relationships?scopeFilter=/classifiers[@item='test-app-module:Weekend']&targetFilter=/classifiers"
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
                                "bSide": "urn:LongEntityType3",
                                "aSide": "urn:LongEntityType2",
                                "id": "urn:Rel_OneToOne_SameEntity_LongEntityType2_LongEntityType3"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType3/relationships?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Weekend']"
                },
                "first": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType3/relationships?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Weekend']"
                },
                "prev": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType3/relationships?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Weekend']"
                },
                "next": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType3/relationships?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Weekend']"
                },
                "last": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType3/relationships?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Weekend']"
                },
                "totalCount": 1
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all relationships for long entity type name with id as urn:LongEntityType2 and with scope filter on decorators"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType2/relationships?&scopeFilter=/decorators[contains(@test-app-module:textdata, 'ORAN')]&targetFilter=/decorators"
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
                                "bSide": "urn:LongEntityType2",
                                "aSide": "urn:LongEntityType1",
                                "id": "urn:Rel_OneToOne_SameEntity_LongEntityType1_LongEntityType2"
                            }
                        ]
                    },
                    {
                        "test-built-in-module:ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS": [
                            {
                                "bSide": "urn:LongEntityType3",
                                "aSide": "urn:LongEntityType2",
                                "id": "urn:Rel_OneToOne_SameEntity_LongEntityType2_LongEntityType3"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType2/relationships?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'ORAN')]"
                },
                "first": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType2/relationships?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'ORAN')]"
                },
                "prev": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType2/relationships?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'ORAN')]"
                },
                "next": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType2/relationships?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'ORAN')]"
                },
                "last": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/urn:LongEntityType2/relationships?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'ORAN')]"
                },
                "totalCount": 2
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Return items as empty for a entity with no relationship"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=91/relationships"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
            "items": [],
            "self": {
                "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=91/relationships?offset=0&limit=500"
            },
            "first": {
                "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=91/relationships?offset=0&limit=500"
            },
            "prev": {
                "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=91/relationships?offset=0&limit=500"
            },
            "next": {
                "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=91/relationships?offset=0&limit=500"
            },
            "last": {
                "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=91/relationships?offset=0&limit=500"
            },
            "totalCount": 0
        }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all relationships for an entity id where there exists no relationships for the entity type in the given domain"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/OCUUPFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,OCUUPFunction=9/relationships"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [],
                "self": {
                    "href": "/domains/RAN/entity-types/OCUUPFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,OCUUPFunction=9/relationships?offset=0&limit=500"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/OCUUPFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,OCUUPFunction=9/relationships?offset=0&limit=500"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/OCUUPFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,OCUUPFunction=9/relationships?offset=0&limit=500"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/OCUUPFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,OCUUPFunction=9/relationships?offset=0&limit=500"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/OCUUPFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,OCUUPFunction=9/relationships?offset=0&limit=500"
                },
                "totalCount": 0
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all relationships for an entity id where there exists no relationships in the given domain"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/OAM/entity-types/ManagedElement/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9/relationships?offset=0&limit=100"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [],
                "self": {
                    "href": "/domains/OAM/entity-types/ManagedElement/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9/relationships?offset=0&limit=100"
                },
                "first": {
                    "href": "/domains/OAM/entity-types/ManagedElement/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9/relationships?offset=0&limit=100"
                },
                "prev": {
                    "href": "/domains/OAM/entity-types/ManagedElement/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9/relationships?offset=0&limit=100"
                },
                "next": {
                    "href": "/domains/OAM/entity-types/ManagedElement/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9/relationships?offset=0&limit=100"
                },
                "last": {
                    "href": "/domains/OAM/entity-types/ManagedElement/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9/relationships?offset=0&limit=100"
                },
                "totalCount": 0
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all relationships for entity type AntennaModule with ID urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/TEIV/entity-types/AntennaModule/entities/urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A/relationships?offset=0&limit=100"
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
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,NodeSupport=1,SectorEquipmentFunction=1",
                                "aSide": "urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A",
                                "id": "urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_SERVES_ANTENNACAPABILITY=ABD52B030DF1169F9F41C898913EF30F7BB5741F53352F482310B280C90AC569B7D31D52A2BB41F1F0099AE1EDD56CACF0B285D145A5584D376DD45DED1E2D65"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE":[
                            {
                                "bSide":"urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A",
                                "aSide":"urn:Sector=2",
                                "id":"urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_ANTENNAMODULE=44F4F4FC906E9A7525065E4565246F7469CBD11FC7752C61EA6D74776845900AFF472DCAACA1F66443490B6CE0DD9AC9A5E1467022118599F6B4C6EC63400512"
                            }
                        ]
                    },
                    {
                    "o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_NRCELLDU":[
                        {
                            "bSide":"urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1",
                            "aSide":"urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A",
                            "id":"urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_SERVES_NRCELLDU=ABD52B030DF1169F9F41C898913EF30F7BB5741F53352F482310B280C90AC569B7D31D52A2BB41F1F0099AE1EDD56CACF0B285D145A5584D376DD45DED1E2D65"
                        }
                    ]
                }
                ],
                "self": {
                    "href": "/domains/TEIV/entity-types/AntennaModule/entities/urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A/relationships?offset=0&limit=100"
                },
                "first": {
                    "href": "/domains/TEIV/entity-types/AntennaModule/entities/urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A/relationships?offset=0&limit=100"
                },
                "prev": {
                    "href": "/domains/TEIV/entity-types/AntennaModule/entities/urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A/relationships?offset=0&limit=100"
                },
                "next": {
                    "href": "/domains/TEIV/entity-types/AntennaModule/entities/urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A/relationships?offset=0&limit=100"
                },
                "last": {
                    "href": "/domains/TEIV/entity-types/AntennaModule/entities/urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A/relationships?offset=0&limit=100"
                },
                "totalCount": 3
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(3)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_ANTENNACAPABILITY[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_ANTENNACAPABILITY[0].aSide', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_ANTENNACAPABILITY[0].bSide', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE[0].aSide', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE[0].bSide', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_NRCELLDU[0].aSide', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_NRCELLDU[0].bSide', byEquality())
            }
        }
    },
    Contract.make {
        description "ERROR - 400: Get all relationships for NRCellDU entity with invalid offset (greater than total count)."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=3/relationships?offset=1000"
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/json')
            }
            body('''{
            "status": "BAD_REQUEST",
            "message": "Invalid Value",
            "details": "Offset cannot be larger than 2"
        }''')
        }
    },
    Contract.make {
        description "ERROR - 404: Get all relationships for non existing NRCellDU id."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/NRCellDU/entities/urn:non-existent/relationships"
        }
        response {
            status NOT_FOUND()
            headers {
                contentType('application/json')
            }
            body('''{
            "status": "NOT_FOUND",
            "message": "Resource Not Found",
            "details": "The requested resource is not found. ID: urn:non-existent"
        }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200 Get all relationships with entityId for ODUFunction using metadata"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?targetFilter=/metadata&scopeFilter=/metadata[@reliabilityIndicator='OK']"
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
                              "metadata": {
                                   "reliabilityIndicator": "OK"
                               },
                               "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19",
                               "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19",
                               "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=6BD25E5C8FB7842F69010736253CC47F43535D7238E9E9A03E8092E8C019C83270DE47C96EF1049C40B83A130F9F129AE93B9C8538B6B004AE89BD0A098E48DD"
                           }
                       ]
                   }
               ],
               "self": {
                   "href": "/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@reliabilityIndicator='OK']"
               },
               "first": {
                   "href": "/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@reliabilityIndicator='OK']"
               },
               "prev": {
                   "href": "/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@reliabilityIndicator='OK']"
               },
               "next": {
                   "href": "/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@reliabilityIndicator='OK']"
               },
               "last": {
                   "href": "/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@reliabilityIndicator='OK']"
               },
               "totalCount": 1
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(1)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].aSide', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].bSide', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200 Get all relationships with entityId for ODUFunction using firstDiscovered in metadata as filter - LESS THAN"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?targetFilter=/metadata&scopeFilter=/metadata[@firstDiscovered<'2025-01-21T11:12:48.628172460Z']"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''
{
    "items": [
        {
            "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU": [
                {
                    "metadata": {
                        "lastModified": "2025-01-08T10:40:36.46156500Z",
                        "firstDiscovered": "2025-01-08T10:40:36.46156500Z",
                        "reliabilityIndicator": "RESTORED"
                    },
                    "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=93",
                    "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19",
                    "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=7723E5D5B3332E0890EAA620C77A6A47065E15A2EA28AD83F3B3CFEA5A7E3BB5965AE78890F1BF000EAA89BF8DE209E506192BF5EA6871426603ED76CBFAF088"
                }
            ]
        },
        {
            "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU": [
                {
                    "metadata": {
                        "lastModified": "2025-01-08T10:40:36.46156500Z",
                        "firstDiscovered": "2025-01-08T10:40:36.46156500Z",
                        "reliabilityIndicator": "ADVISED"
                    },
                    "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=92",
                    "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19",
                    "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=DDECCEFB8831FA4EB21B121BA35EAB07ED8D841B5A38580C5F3AD11E66FE73D2FC42E823C6C73288860C7562B610C3D07B6C39FD386171A3BE622096F4B3D006"
                }
            ]
        },
        {
            "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION": [
                {
                    "metadata": {
                        "lastModified": "2025-01-13T16:40:36.461565Z",
                        "firstDiscovered": "2025-01-13T16:40:36.461565Z",
                        "reliabilityIndicator": "OK"
                    },
                    "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19",
                    "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19",
                    "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=6BD25E5C8FB7842F69010736253CC47F43535D7238E9E9A03E8092E8C019C83270DE47C96EF1049C40B83A130F9F129AE93B9C8538B6B004AE89BD0A098E48DD"
                }
            ]
        }
    ],
    "self": {
        "href": "/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@firstDiscovered<'2025-01-21T11:12:48.628172460Z']"
    },
    "first": {
        "href": "/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@firstDiscovered<'2025-01-21T11:12:48.628172460Z']"
    },
    "prev": {
        "href": "/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@firstDiscovered<'2025-01-21T11:12:48.628172460Z']"
    },
    "next": {
        "href": "/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@firstDiscovered<'2025-01-21T11:12:48.628172460Z']"
    },
    "last": {
        "href": "/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@firstDiscovered<'2025-01-21T11:12:48.628172460Z']"
    },
    "totalCount": 3
}
             ''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(3)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].metadata.lastModified', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].metadata.firstDiscovered', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].metadata.reliabilityIndicator', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].aSide', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].bSide', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].metadata.lastModified', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].metadata.firstDiscovered', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].metadata.reliabilityIndicator', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].aSide', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].bSide', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].metadata.lastModified', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].metadata.firstDiscovered', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].metadata.reliabilityIndicator', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].aSide', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].bSide', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200 Get all relationships with entityId for ODUFunction using firstDiscovered in metadata as filter - GREATER THAN"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?targetFilter=/metadata&scopeFilter=/metadata[@firstDiscovered>'2024-01-21T11:12:48.628172460+01:00']"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''
{
    "items": [
        {
            "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU": [
                {
                    "metadata": {
                        "lastModified": "2025-01-08T10:40:36.46156500Z",
                        "firstDiscovered": "2025-01-08T10:40:36.46156500Z",
                        "reliabilityIndicator": "RESTORED"
                    },
                    "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=93",
                    "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19",
                    "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=7723E5D5B3332E0890EAA620C77A6A47065E15A2EA28AD83F3B3CFEA5A7E3BB5965AE78890F1BF000EAA89BF8DE209E506192BF5EA6871426603ED76CBFAF088"
                }
            ]
        },
        {
            "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU": [
                {
                    "metadata": {
                        "lastModified": "2025-01-08T10:40:36.46156500Z",
                        "firstDiscovered": "2025-01-08T10:40:36.46156500Z",
                        "reliabilityIndicator": "ADVISED"
                    },
                    "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=92",
                    "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19",
                    "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=DDECCEFB8831FA4EB21B121BA35EAB07ED8D841B5A38580C5F3AD11E66FE73D2FC42E823C6C73288860C7562B610C3D07B6C39FD386171A3BE622096F4B3D006"
                }
            ]
        },
        {
            "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION": [
                {
                    "metadata": {
                        "lastModified": "2025-01-13T16:40:36.461565Z",
                        "firstDiscovered": "2025-01-13T16:40:36.461565Z",
                        "reliabilityIndicator": "OK"
                    },
                    "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19",
                    "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19",
                    "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=6BD25E5C8FB7842F69010736253CC47F43535D7238E9E9A03E8092E8C019C83270DE47C96EF1049C40B83A130F9F129AE93B9C8538B6B004AE89BD0A098E48DD"
                }
            ]
        }
    ],
    "self": {
        "href": "/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@firstDiscovered>'2024-01-21T11:12:48.628172460+01:00']"
    },
    "first": {
        "href": "/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@firstDiscovered>'2024-01-21T11:12:48.628172460+01:00']"
    },
    "prev": {
        "href": "/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@firstDiscovered>'2024-01-21T11:12:48.628172460+01:00']"
    },
    "next": {
        "href": "/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@firstDiscovered>'2024-01-21T11:12:48.628172460+01:00']"
    },
    "last": {
        "href": "/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@firstDiscovered>'2024-01-21T11:12:48.628172460+01:00']"
    },
    "totalCount": 3
}
             ''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(3)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].metadata.lastModified', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].metadata.firstDiscovered', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].metadata.reliabilityIndicator', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].aSide', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].bSide', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].metadata.lastModified', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].metadata.firstDiscovered', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].metadata.reliabilityIndicator', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].aSide', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU[0].bSide', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].metadata.lastModified', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].metadata.firstDiscovered', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].metadata.reliabilityIndicator', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].aSide', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].bSide', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200 Get all relationships with entityId for ODUFunction using firstDiscovered in metadata as filter - LESS THAN AND GREATER THAN"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?targetFilter=/metadata&scopeFilter=/metadata[@firstDiscovered>'2025-01-21T11:12:48.628172460Z' and @firstDiscovered<'2025-01-31T11:12:48.628172460Z']"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''
{
    "items": [],
    "self": {
        "href": "/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@firstDiscovered>'2025-01-21T11:12:48.628172460Z' and @firstDiscovered<'2025-01-31T11:12:48.628172460Z']"
    },
    "first": {
        "href": "/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@firstDiscovered>'2025-01-21T11:12:48.628172460Z' and @firstDiscovered<'2025-01-31T11:12:48.628172460Z']"
    },
    "prev": {
        "href": "/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@firstDiscovered>'2025-01-21T11:12:48.628172460Z' and @firstDiscovered<'2025-01-31T11:12:48.628172460Z']"
    },
    "next": {
        "href": "/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@firstDiscovered>'2025-01-21T11:12:48.628172460Z' and @firstDiscovered<'2025-01-31T11:12:48.628172460Z']"
    },
    "last": {
        "href": "/domains/REL_OAM_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19/relationships?offset=0&limit=500&targetFilter=/metadata&scopeFilter=/metadata[@firstDiscovered>'2025-01-21T11:12:48.628172460Z' and @firstDiscovered<'2025-01-31T11:12:48.628172460Z']"
    },
    "totalCount": 0
}
             ''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(0)
                })
            }
        }
    }
]
