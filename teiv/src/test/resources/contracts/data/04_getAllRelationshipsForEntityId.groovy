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
        description "SUCCESS - 200: Get all relationships for entity type NRCellDU with ID urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1/relationships?offset=0&limit=100"
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
                  "o-ran-smo-teiv-ran:NRCELLDU_USES_NRSECTORCARRIER": [
                    {
                      "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRSectorCarrier=1",
                      "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1",
                      "id": "urn:o-ran:smo:teiv:sha512:NRCELLDU_USES_NRSECTORCARRIER=7B9425BBD6977FEA6C180F6078CFBAEBE65400223B29E0EFA4F38424FAD66C690806778909177ECF1457CAC18E5BCF6FA4F24E3ECE524C89DE68108708D6D876"
                    }
                  ]
                },
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
                "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1/relationships?offset=0&limit=100"
              },
              "first": {
                "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1/relationships?offset=0&limit=100"
              },
              "prev": {
                "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1/relationships?offset=0&limit=100"
              },
              "next": {
                "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1/relationships?offset=0&limit=100"
              },
              "last": {
                "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1/relationships?offset=0&limit=100"
              },
              "totalCount": 3
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(3)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU[0].aSide', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU[0].bSide', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:NRCELLDU_USES_NRSECTORCARRIER[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:NRCELLDU_USES_NRSECTORCARRIER[0].aSide', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:NRCELLDU_USES_NRSECTORCARRIER[0].bSide', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:SECTOR_GROUPS_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:SECTOR_GROUPS_NRCELLDU[0].aSide', byEquality())
                jsonPath('$.items[2].o-ran-smo-teiv-ran:SECTOR_GROUPS_NRCELLDU[0].bSide', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all relationships for entity type NRCellDU  with ID urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1 & targetFilter=/GNBDUFUNCTION_PROVIDES_NRCELLDU."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/GNBDUFUNCTION_PROVIDES_NRCELLDU"
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
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/GNBDUFUNCTION_PROVIDES_NRCELLDU"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/GNBDUFUNCTION_PROVIDES_NRCELLDU"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/GNBDUFUNCTION_PROVIDES_NRCELLDU"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/GNBDUFUNCTION_PROVIDES_NRCELLDU"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/GNBDUFUNCTION_PROVIDES_NRCELLDU"
                },
                "totalCount": 1
}''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all relationships for entity type NRCellDU  with ID urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1 & targetFilter=/GNBDUFUNCTION_PROVIDES_NRCELLDU;/SECTOR_GROUPS_NRCELLDU."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/GNBDUFUNCTION_PROVIDES_NRCELLDU;/SECTOR_GROUPS_NRCELLDU"
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
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/GNBDUFUNCTION_PROVIDES_NRCELLDU;/SECTOR_GROUPS_NRCELLDU"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/GNBDUFUNCTION_PROVIDES_NRCELLDU;/SECTOR_GROUPS_NRCELLDU"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/GNBDUFUNCTION_PROVIDES_NRCELLDU;/SECTOR_GROUPS_NRCELLDU"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/GNBDUFUNCTION_PROVIDES_NRCELLDU;/SECTOR_GROUPS_NRCELLDU"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1/relationships?offset=0&limit=100&targetFilter=/GNBDUFUNCTION_PROVIDES_NRCELLDU;/SECTOR_GROUPS_NRCELLDU"
                },
                "totalCount": 2
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all relationships for entity type GNBDUFunction with ID urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1. & scopeFilter=/provided-nrCellDu[@id = 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1']"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/GNBDUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9/relationships?offset=0&limit=100&scopeFilter=/provided-nrCellDu[@id = 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1']"
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
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/GNBDUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9/relationships?offset=0&limit=100&scopeFilter=/provided-nrCellDu[@id = 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1']"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/GNBDUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9/relationships?offset=0&limit=100&scopeFilter=/provided-nrCellDu[@id = 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1']"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/GNBDUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9/relationships?offset=0&limit=100&scopeFilter=/provided-nrCellDu[@id = 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1']"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/GNBDUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9/relationships?offset=0&limit=100&scopeFilter=/provided-nrCellDu[@id = 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1']"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/GNBDUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9/relationships?offset=0&limit=100&scopeFilter=/provided-nrCellDu[@id = 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=1']"
                },
                "totalCount": 1
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all relationships for entity type NRCellDU with ID urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=3 with limit as 2."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=3/relationships?limit=2"
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
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=3",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9",
                                "id": "urn:o-ran:smo:teiv:sha512:GNBDUFUNCTION_PROVIDES_NRCELLDU=714C1B73945C298CAA03FE0D800053CDD1C571BBF375DC647B9F23FDA861CEB369832A3593BB1AA4B8A7245AD187ED24ADDF6FB147130827CDC17BA8370C4838"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:NRCELLDU_USES_NRSECTORCARRIER": [
                            {
                                "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRSectorCarrier=3",
                                "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=3",
                                "id": "urn:o-ran:smo:teiv:sha512:NRCELLDU_USES_NRSECTORCARRIER=950ED4540349F9859CEA9E47884A28CD567BDD2505A3C5335C8851A7AADF2AF65542157BB42D607EE3847E4223D76DE88B90762D0590E48693822FD6DCAE60CD"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=3/relationships?offset=0&limit=2"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=3/relationships?offset=0&limit=2"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=3/relationships?offset=0&limit=2"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=3/relationships?offset=2&limit=2"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=3/relationships?offset=2&limit=2"
                },
                "totalCount": 3
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(2)
                })
                jsonPath('$.items[0].o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU[0].id', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU[0].aSide', byEquality())
                jsonPath('$.items[0].o-ran-smo-teiv-ran:GNBDUFUNCTION_PROVIDES_NRCELLDU[0].bSide', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:NRCELLDU_USES_NRSECTORCARRIER[0].id', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:NRCELLDU_USES_NRSECTORCARRIER[0].aSide', byEquality())
                jsonPath('$.items[1].o-ran-smo-teiv-ran:NRCELLDU_USES_NRSECTORCARRIER[0].bSide', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all relationships for long entity type name with id as LongEntityType1 (OneToOne, ManyToOne, ManyToMany and Same Entity with aSide of relationship)"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType1/relationships"
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
                                "bSide": "LongEntityType1",
                                "aSide": "EntityType1",
                                "id": "RelId_OneToOne_EntityType1_LongEntityType1"
                            }
                        ]
                    },
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
                        "test-built-in-module:ENTITYTYPEA_GROUPS_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS": [
                            {
                                "bSide": "LongEntityType1",
                                "aSide": "EntityType2",
                                "id": "Rel_ManyToOne_EntityType2_LongEntityType1"
                            }
                        ]
                    },
                    {
                        "test-built-in-module:ENTITYTYPEA_INSTALLED_AT_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS": [
                            {
                                "bSide": "LongEntityType1",
                                "aSide": "EntityType1",
                                "id": "Rel_ManyToMany_EntityType1_LongEntityType1"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType1/relationships?offset=0&limit=500"
                },
                "first": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType1/relationships?offset=0&limit=500"
                },
                "prev": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType1/relationships?offset=0&limit=500"
                },
                "next": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType1/relationships?offset=0&limit=500"
                },
                "last": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType1/relationships?offset=0&limit=500"
                },
                "totalCount": 4
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all relationships for long entity type name with id as LongEntityType2  (OneToMany and Same Entity with bSide of relationship)"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType2/relationships"
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
                            "bSide": "LongEntityType2",
                            "aSide": "EntityType2",
                            "id": "Rel_OneToMany_EntityType2_LongEntityType2"
                        }
                    ]
                },
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
                "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType2/relationships?offset=0&limit=500"
            },
            "first": {
                "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType2/relationships?offset=0&limit=500"
            },
            "prev": {
                "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType2/relationships?offset=0&limit=500"
            },
            "next": {
                "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType2/relationships?offset=0&limit=500"
            },
            "last": {
                "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType2/relationships?offset=0&limit=500"
            },
            "totalCount": 3
        }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all relationships for long entity type name with id as LongEntityType1 and with scope filter on sourceIds"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType1/relationships?scopeFilter=/sourceIds[@item = 'urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616']&targetFilter=/sourceIds"
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
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType1/relationships?offset=0&limit=500&targetFilter=/sourceIds&scopeFilter=/sourceIds[@item = 'urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616']"
                },
                "first": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType1/relationships?offset=0&limit=500&targetFilter=/sourceIds&scopeFilter=/sourceIds[@item = 'urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616']"
                },
                "prev": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType1/relationships?offset=0&limit=500&targetFilter=/sourceIds&scopeFilter=/sourceIds[@item = 'urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616']"
                },
                "next": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType1/relationships?offset=0&limit=500&targetFilter=/sourceIds&scopeFilter=/sourceIds[@item = 'urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616']"
                },
                "last": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType1/relationships?offset=0&limit=500&targetFilter=/sourceIds&scopeFilter=/sourceIds[@item = 'urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616']"
                },
                "totalCount": 1
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all relationships for long entity type name with id as LongEntityType3 and with scope filter on classifiers"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType3/relationships?scopeFilter=/classifiers[@item='test-app-module:Weekend']&targetFilter=/classifiers"
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
                                "bSide": "LongEntityType3",
                                "aSide": "LongEntityType2",
                                "id": "Rel_OneToOne_SameEntity_LongEntityType2_LongEntityType3"
                            }
                        ]
                    }
                ],
                "self": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType3/relationships?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Weekend']"
                },
                "first": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType3/relationships?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Weekend']"
                },
                "prev": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType3/relationships?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Weekend']"
                },
                "next": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType3/relationships?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Weekend']"
                },
                "last": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType3/relationships?offset=0&limit=500&targetFilter=/classifiers&scopeFilter=/classifiers[@item='test-app-module:Weekend']"
                },
                "totalCount": 1
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all relationships for long entity type name with id as LongEntityType2 and with scope filter on decorators"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType2/relationships?&scopeFilter=/decorators[contains(@test-app-module:textdata, 'ORAN')]&targetFilter=/decorators"
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
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType2/relationships?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'ORAN')]"
                },
                "first": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType2/relationships?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'ORAN')]"
                },
                "prev": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType2/relationships?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'ORAN')]"
                },
                "next": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType2/relationships?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'ORAN')]"
                },
                "last": {
                    "href": "/domains/TEST/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities/LongEntityType2/relationships?offset=0&limit=500&targetFilter=/decorators&scopeFilter=/decorators[contains(@test-app-module:textdata, 'ORAN')]"
                },
                "totalCount": 2
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Return items as empty for a entity with no relationship"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,GNBDUFunction=19,NRCellDU=91/relationships"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
            "items": [],
            "self": {
                "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,GNBDUFunction=19,NRCellDU=91/relationships?offset=0&limit=500"
            },
            "first": {
                "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,GNBDUFunction=19,NRCellDU=91/relationships?offset=0&limit=500"
            },
            "prev": {
                "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,GNBDUFunction=19,NRCellDU=91/relationships?offset=0&limit=500"
            },
            "next": {
                "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,GNBDUFunction=19,NRCellDU=91/relationships?offset=0&limit=500"
            },
            "last": {
                "href": "/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,GNBDUFunction=19,NRCellDU=91/relationships?offset=0&limit=500"
            },
            "totalCount": 0
        }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all relationships for an entity id where there exists no relationships for the entity type in the given domain"
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/GNBCUUPFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBCUUPFunction=9/relationships"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [],
                "self": {
                    "href": "/domains/RAN/entity-types/GNBCUUPFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBCUUPFunction=9/relationships?offset=0&limit=500"
                },
                "first": {
                    "href": "/domains/RAN/entity-types/GNBCUUPFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBCUUPFunction=9/relationships?offset=0&limit=500"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types/GNBCUUPFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBCUUPFunction=9/relationships?offset=0&limit=500"
                },
                "next": {
                    "href": "/domains/RAN/entity-types/GNBCUUPFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBCUUPFunction=9/relationships?offset=0&limit=500"
                },
                "last": {
                    "href": "/domains/RAN/entity-types/GNBCUUPFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBCUUPFunction=9/relationships?offset=0&limit=500"
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
        description "ERROR - 400: Get all relationships for NRCellDU entity with invalid offset (greater than total count)."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/NRCellDU/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,GNBDUFunction=9,NRCellDU=3/relationships?offset=1000"
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
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types/NRCellDU/entities/non-existent/relationships"
        }
        response {
            status NOT_FOUND()
            headers {
                contentType('application/json')
            }
            body('''{
            "status": "NOT_FOUND",
            "message": "Resource Not Found",
            "details": "The requested resource is not found"
        }''')
        }
    }
]
