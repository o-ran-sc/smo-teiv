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
        description "SUCCESS - 200: Get all the available topology relationships types in REL_OAM_RAN domain."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_OAM_RAN/relationship-types"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "name": "ENODEBFUNCTION_PROVIDES_EUTRANCELL",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/ENODEBFUNCTION_PROVIDES_EUTRANCELL/relationships"
                        }
                    },
                    {
                        "name": "ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER/relationships"
                        }
                    },
                    {
                        "name": "EUTRANCELL_USES_LTESECTORCARRIER",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/EUTRANCELL_USES_LTESECTORCARRIER/relationships"
                        }
                    },
                    {
                        "name": "GNBCUCPFUNCTION_PROVIDES_NRCELLCU",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/GNBCUCPFUNCTION_PROVIDES_NRCELLCU/relationships"
                        }
                    },
                    {
                        "name": "GNBDUFUNCTION_PROVIDES_NRCELLDU",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/GNBDUFUNCTION_PROVIDES_NRCELLDU/relationships"
                        }
                    },
                    {
                        "name": "GNBDUFUNCTION_PROVIDES_NRSECTORCARRIER",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/GNBDUFUNCTION_PROVIDES_NRSECTORCARRIER/relationships"
                        }
                    },
                    {
                        "name": "LTESECTORCARRIER_USES_ANTENNACAPABILITY",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/LTESECTORCARRIER_USES_ANTENNACAPABILITY/relationships"
                        }
                    },
                    {
                        "name": "MANAGEDELEMENT_MANAGES_ENODEBFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ENODEBFUNCTION/relationships"
                        }
                    },
                    {
                        "name": "MANAGEDELEMENT_MANAGES_GNBCUCPFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBCUCPFUNCTION/relationships"
                        }
                    },
                    {
                        "name": "MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION/relationships"
                        }
                    },
                    {
                        "name": "MANAGEDELEMENT_MANAGES_GNBDUFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships"
                        }
                    },
                    {
                        "name": "NRCELLDU_USES_NRSECTORCARRIER",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/NRCELLDU_USES_NRSECTORCARRIER/relationships"
                        }
                    },
                    {
                        "name": "NRSECTORCARRIER_USES_ANTENNACAPABILITY",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/NRSECTORCARRIER_USES_ANTENNACAPABILITY/relationships"
                        }
                    },
                    {
                        "name": "SECTOR_GROUPS_EUTRANCELL",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/SECTOR_GROUPS_EUTRANCELL/relationships"
                        }
                    },
                    {
                        "name": "SECTOR_GROUPS_NRCELLDU",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/SECTOR_GROUPS_NRCELLDU/relationships"
                        }
                    }
                ],
                "self": {
                    "href": "/domains/REL_OAM_RAN/relationship-types?offset=0&limit=500"
                },
                "first": {
                    "href": "/domains/REL_OAM_RAN/relationship-types?offset=0&limit=500"
                },
                "prev": {
                    "href": "/domains/REL_OAM_RAN/relationship-types?offset=0&limit=500"
                },
                "next": {
                    "href": "/domains/REL_OAM_RAN/relationship-types?offset=0&limit=500"
                },
                "last": {
                    "href": "/domains/REL_OAM_RAN/relationship-types?offset=0&limit=500"
                },
                "totalCount": 15
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(15)
                })
                jsonPath('$.items[0].name', byEquality())
                jsonPath('$.items[0].relationships.href', byEquality())
                jsonPath('$.items[1].name', byEquality())
                jsonPath('$.items[1].relationships.href', byEquality())
                jsonPath('$.items[2].name', byEquality())
                jsonPath('$.items[2].relationships.href', byEquality())
                jsonPath('$.items[3].name', byEquality())
                jsonPath('$.items[3].relationships.href', byEquality())
                jsonPath('$.items[4].name', byEquality())
                jsonPath('$.items[4].relationships.href', byEquality())
                jsonPath('$.items[5].name', byEquality())
                jsonPath('$.items[5].relationships.href', byEquality())
                jsonPath('$.items[6].name', byEquality())
                jsonPath('$.items[6].relationships.href', byEquality())
                jsonPath('$.items[7].name', byEquality())
                jsonPath('$.items[7].relationships.href', byEquality())
                jsonPath('$.items[8].name', byEquality())
                jsonPath('$.items[8].relationships.href', byEquality())
                jsonPath('$.items[9].name', byEquality())
                jsonPath('$.items[9].relationships.href', byEquality())
                jsonPath('$.items[10].name', byEquality())
                jsonPath('$.items[10].relationships.href', byEquality())
                jsonPath('$.items[11].name', byEquality())
                jsonPath('$.items[11].relationships.href', byEquality())
                jsonPath('$.items[12].name', byEquality())
                jsonPath('$.items[12].relationships.href', byEquality())
                jsonPath('$.items[13].name', byEquality())
                jsonPath('$.items[13].relationships.href', byEquality())
                jsonPath('$.items[14].name', byEquality())
                jsonPath('$.items[14].relationships.href', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all the available topology relationship types in REL_EQUIPMENT_RAN domain."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_EQUIPMENT_RAN/relationship-types"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "name": "ANTENNAMODULE_INSTALLED_AT_SITE",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ANTENNAMODULE_INSTALLED_AT_SITE/relationships"
                        }
                    },
                    {
                        "name": "ANTENNAMODULE_SERVES_ANTENNACAPABILITY",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ANTENNAMODULE_SERVES_ANTENNACAPABILITY/relationships"
                        }
                    },
                    {
                        "name": "ENODEBFUNCTION_PROVIDES_EUTRANCELL",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ENODEBFUNCTION_PROVIDES_EUTRANCELL/relationships"
                        }
                    },
                    {
                        "name": "ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER/relationships"
                        }
                    },
                    {
                        "name": "EUTRANCELL_USES_LTESECTORCARRIER",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/EUTRANCELL_USES_LTESECTORCARRIER/relationships"
                        }
                    },
                    {
                        "name": "GNBCUCPFUNCTION_PROVIDES_NRCELLCU",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/GNBCUCPFUNCTION_PROVIDES_NRCELLCU/relationships"
                        }
                    },
                    {
                        "name": "GNBDUFUNCTION_PROVIDES_NRCELLDU",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/GNBDUFUNCTION_PROVIDES_NRCELLDU/relationships"
                        }
                    },
                    {
                        "name": "GNBDUFUNCTION_PROVIDES_NRSECTORCARRIER",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/GNBDUFUNCTION_PROVIDES_NRSECTORCARRIER/relationships"
                        }
                    },
                    {
                        "name": "LTESECTORCARRIER_USES_ANTENNACAPABILITY",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/LTESECTORCARRIER_USES_ANTENNACAPABILITY/relationships"
                        }
                    },
                    {
                        "name": "NRCELLDU_USES_NRSECTORCARRIER",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/NRCELLDU_USES_NRSECTORCARRIER/relationships"
                        }
                    },
                    {
                        "name": "NRSECTORCARRIER_USES_ANTENNACAPABILITY",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/NRSECTORCARRIER_USES_ANTENNACAPABILITY/relationships"
                        }
                    },
                    {
                        "name": "SECTOR_GROUPS_ANTENNAMODULE",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/SECTOR_GROUPS_ANTENNAMODULE/relationships"
                        }
                    },
                    {
                        "name": "SECTOR_GROUPS_EUTRANCELL",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/SECTOR_GROUPS_EUTRANCELL/relationships"
                        }
                    },
                    {
                        "name": "SECTOR_GROUPS_NRCELLDU",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/SECTOR_GROUPS_NRCELLDU/relationships"
                        }
                    }
                ],
                "self": {
                    "href": "/domains/REL_EQUIPMENT_RAN/relationship-types?offset=0&limit=500"
                },
                "first": {
                    "href": "/domains/REL_EQUIPMENT_RAN/relationship-types?offset=0&limit=500"
                },
                "prev": {
                    "href": "/domains/REL_EQUIPMENT_RAN/relationship-types?offset=0&limit=500"
                },
                "next": {
                    "href": "/domains/REL_EQUIPMENT_RAN/relationship-types?offset=0&limit=500"
                },
                "last": {
                    "href": "/domains/REL_EQUIPMENT_RAN/relationship-types?offset=0&limit=500"
                },
                "totalCount": 14
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all the available topology relationship types in RAN domain."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/relationship-types"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "name": "ENODEBFUNCTION_PROVIDES_EUTRANCELL",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/ENODEBFUNCTION_PROVIDES_EUTRANCELL/relationships"
                        }
                    },
                    {
                        "name": "ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER/relationships"
                        }
                    },
                    {
                        "name": "EUTRANCELL_USES_LTESECTORCARRIER",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/EUTRANCELL_USES_LTESECTORCARRIER/relationships"
                        }
                    },
                    {
                        "name": "GNBCUCPFUNCTION_PROVIDES_NRCELLCU",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/GNBCUCPFUNCTION_PROVIDES_NRCELLCU/relationships"
                        }
                    },
                    {
                        "name": "GNBDUFUNCTION_PROVIDES_NRCELLDU",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/GNBDUFUNCTION_PROVIDES_NRCELLDU/relationships"
                        }
                    },
                    {
                        "name": "GNBDUFUNCTION_PROVIDES_NRSECTORCARRIER",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/GNBDUFUNCTION_PROVIDES_NRSECTORCARRIER/relationships"
                        }
                    },
                    {
                        "name": "LTESECTORCARRIER_USES_ANTENNACAPABILITY",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/LTESECTORCARRIER_USES_ANTENNACAPABILITY/relationships"
                        }
                    },
                    {
                        "name": "NRCELLDU_USES_NRSECTORCARRIER",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/NRCELLDU_USES_NRSECTORCARRIER/relationships"
                        }
                    },
                    {
                        "name": "NRSECTORCARRIER_USES_ANTENNACAPABILITY",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/NRSECTORCARRIER_USES_ANTENNACAPABILITY/relationships"
                        }
                    },
                    {
                        "name": "SECTOR_GROUPS_EUTRANCELL",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/SECTOR_GROUPS_EUTRANCELL/relationships"
                        }
                    },
                    {
                        "name": "SECTOR_GROUPS_NRCELLDU",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/SECTOR_GROUPS_NRCELLDU/relationships"
                        }
                    }
                ],
                "self": {
                    "href": "/domains/RAN/relationship-types?offset=0&limit=500"
                },
                "first": {
                    "href": "/domains/RAN/relationship-types?offset=0&limit=500"
                },
                "prev": {
                    "href": "/domains/RAN/relationship-types?offset=0&limit=500"
                },
                "next": {
                    "href": "/domains/RAN/relationship-types?offset=0&limit=500"
                },
                "last": {
                    "href": "/domains/RAN/relationship-types?offset=0&limit=500"
                },
                "totalCount": 11
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all the available topology relationship types in TEIV."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/TEIV/relationship-types"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "name": "ANTENNAMODULE_INSTALLED_AT_SITE",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ANTENNAMODULE_INSTALLED_AT_SITE/relationships"
                        }
                    },
                    {
                        "name": "ANTENNAMODULE_SERVES_ANTENNACAPABILITY",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ANTENNAMODULE_SERVES_ANTENNACAPABILITY/relationships"
                        }
                    },
                    {
                        "name": "ENODEBFUNCTION_PROVIDES_EUTRANCELL",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ENODEBFUNCTION_PROVIDES_EUTRANCELL/relationships"
                        }
                    },
                    {
                        "name": "ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER/relationships"
                        }
                    },
                    {
                        "name": "ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships"
                        }
                    },
                    {
                        "name": "ENTITYTYPEA_GROUPS_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ENTITYTYPEA_GROUPS_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships"
                        }
                    },
                    {
                        "name": "ENTITYTYPEA_INSTALLED_AT_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ENTITYTYPEA_INSTALLED_AT_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships"
                        }
                    },
                    {
                        "name": "ENTITYTYPEA_PROVIDES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ENTITYTYPEA_PROVIDES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships"
                        }
                    },
                    {
                        "name": "ENTITYTYPEA_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ENTITYTYPEA_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships"
                        }
                    },
                    {
                        "name": "EUTRANCELL_USES_LTESECTORCARRIER",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/EUTRANCELL_USES_LTESECTORCARRIER/relationships"
                        }
                    },
                    {
                        "name": "GNBCUCPFUNCTION_PROVIDES_NRCELLCU",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/GNBCUCPFUNCTION_PROVIDES_NRCELLCU/relationships"
                        }
                    },
                    {
                        "name": "GNBDUFUNCTION_PROVIDES_NRCELLDU",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/GNBDUFUNCTION_PROVIDES_NRCELLDU/relationships"
                        }
                    },
                    {
                        "name": "GNBDUFUNCTION_PROVIDES_NRSECTORCARRIER",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/GNBDUFUNCTION_PROVIDES_NRSECTORCARRIER/relationships"
                        }
                    },
                    {
                        "name": "LTESECTORCARRIER_USES_ANTENNACAPABILITY",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/LTESECTORCARRIER_USES_ANTENNACAPABILITY/relationships"
                        }
                    },
                    {
                        "name": "MANAGEDELEMENT_MANAGES_ENODEBFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/MANAGEDELEMENT_MANAGES_ENODEBFUNCTION/relationships"
                        }
                    },
                    {
                        "name": "MANAGEDELEMENT_MANAGES_GNBCUCPFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/MANAGEDELEMENT_MANAGES_GNBCUCPFUNCTION/relationships"
                        }
                    },
                    {
                        "name": "MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION/relationships"
                        }
                    },
                    {
                        "name": "MANAGEDELEMENT_MANAGES_GNBDUFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/MANAGEDELEMENT_MANAGES_GNBDUFUNCTION/relationships"
                        }
                    },
                    {
                        "name": "NRCELLDU_USES_NRSECTORCARRIER",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/NRCELLDU_USES_NRSECTORCARRIER/relationships"
                        }
                    },
                    {
                        "name": "NRSECTORCARRIER_USES_ANTENNACAPABILITY",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/NRSECTORCARRIER_USES_ANTENNACAPABILITY/relationships"
                        }
                    },
                    {
                        "name": "SECTOR_GROUPS_ANTENNAMODULE",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/SECTOR_GROUPS_ANTENNAMODULE/relationships"
                        }
                    },
                    {
                        "name": "SECTOR_GROUPS_EUTRANCELL",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/SECTOR_GROUPS_EUTRANCELL/relationships"
                        }
                    },
                    {
                        "name": "SECTOR_GROUPS_NRCELLDU",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/SECTOR_GROUPS_NRCELLDU/relationships"
                        }
                    }
                ],
                "self": {
                    "href": "/domains/TEIV/relationship-types?offset=0&limit=500"
                },
                "first": {
                    "href": "/domains/TEIV/relationship-types?offset=0&limit=500"
                },
                "prev": {
                    "href": "/domains/TEIV/relationship-types?offset=0&limit=500"
                },
                "next": {
                    "href": "/domains/TEIV/relationship-types?offset=0&limit=500"
                },
                "last": {
                    "href": "/domains/TEIV/relationship-types?offset=0&limit=500"
                },
                "totalCount": 23
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Get all the available topology relationship types in invalid domain."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/INVALID/relationship-types"
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Unknown domain",
                "details": "Unknown domain: INVALID, known domains: [EQUIPMENT, OAM, RAN, REL_EQUIPMENT_RAN, REL_OAM_RAN, TEIV, TEST]"
            }''')
        }
    }
]
