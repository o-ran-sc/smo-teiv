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
        description "SUCCESS - 200: Get all the available topology relationships types in REL_OAM_RAN domain."
        request {
            method GET()
            url "/topology-inventory/v1/domains/REL_OAM_RAN/relationship-types"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "relationshipTypeName": "MANAGEDELEMENT_MANAGES_NEARRTRICFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_NEARRTRICFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "MANAGEDELEMENT_MANAGES_OCUCPFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_OCUCPFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "MANAGEDELEMENT_MANAGES_OCUUPFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_OCUUPFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "MANAGEDELEMENT_MANAGES_ODUFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "MANAGEDELEMENT_MANAGES_ORUFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ORUFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "NEARRTRICFUNCTION_O1LINK_SMO",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/NEARRTRICFUNCTION_O1LINK_SMO/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "NRCELLDU_USES_NRSECTORCARRIER",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/NRCELLDU_USES_NRSECTORCARRIER/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "NRSECTORCARRIER_USES_ANTENNACAPABILITY",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/NRSECTORCARRIER_USES_ANTENNACAPABILITY/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "OCUCPFUNCTION_E1LINK_OCUUPFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/OCUCPFUNCTION_E1LINK_OCUUPFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "OCUCPFUNCTION_E2LINK_NEARRTRICFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/OCUCPFUNCTION_E2LINK_NEARRTRICFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "OCUCPFUNCTION_O1LINK_SMO",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/OCUCPFUNCTION_O1LINK_SMO/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "OCUCPFUNCTION_PROVIDES_NRCELLCU",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/OCUCPFUNCTION_PROVIDES_NRCELLCU/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "OCUUPFUNCTION_E2LINK_NEARRTRICFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/OCUUPFUNCTION_E2LINK_NEARRTRICFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_E2LINK_NEARRTRICFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/ODUFUNCTION_E2LINK_NEARRTRICFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_F1CLINK_OCUCPFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/ODUFUNCTION_F1CLINK_OCUCPFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_F1ULINK_OCUUPFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/ODUFUNCTION_F1ULINK_OCUUPFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_O1LINK_SMO",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/ODUFUNCTION_O1LINK_SMO/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_PROVIDES_NRCELLDU",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/ODUFUNCTION_PROVIDES_NRCELLDU/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_PROVIDES_NRSECTORCARRIER",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/ODUFUNCTION_PROVIDES_NRSECTORCARRIER/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_O1LINK_SMO",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/ORUFUNCTION_O1LINK_SMO/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_OFHCLINK_ODUFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/ORUFUNCTION_OFHCLINK_ODUFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_OFHMLINK_ODUFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/ORUFUNCTION_OFHMLINK_ODUFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_OFHMLINK_SMO",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/ORUFUNCTION_OFHMLINK_SMO/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_OFHSLINK_ODUFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/ORUFUNCTION_OFHSLINK_ODUFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_OFHULINK_ODUFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_OAM_RAN/relationship-types/ORUFUNCTION_OFHULINK_ODUFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "SECTOR_GROUPS_NRCELLDU",
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
                "totalCount": 26
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(26)
                })
                jsonPath('$.items[0].relationshipTypeName', byEquality())
                jsonPath('$.items[0].relationships.href', byEquality())
                jsonPath('$.items[1].relationshipTypeName', byEquality())
                jsonPath('$.items[1].relationships.href', byEquality())
                jsonPath('$.items[2].relationshipTypeName', byEquality())
                jsonPath('$.items[2].relationships.href', byEquality())
                jsonPath('$.items[3].relationshipTypeName', byEquality())
                jsonPath('$.items[3].relationships.href', byEquality())
                jsonPath('$.items[4].relationshipTypeName', byEquality())
                jsonPath('$.items[4].relationships.href', byEquality())
                jsonPath('$.items[5].relationshipTypeName', byEquality())
                jsonPath('$.items[5].relationships.href', byEquality())
                jsonPath('$.items[6].relationshipTypeName', byEquality())
                jsonPath('$.items[6].relationships.href', byEquality())
                jsonPath('$.items[7].relationshipTypeName', byEquality())
                jsonPath('$.items[7].relationships.href', byEquality())
                jsonPath('$.items[8].relationshipTypeName', byEquality())
                jsonPath('$.items[8].relationships.href', byEquality())
                jsonPath('$.items[9].relationshipTypeName', byEquality())
                jsonPath('$.items[9].relationships.href', byEquality())
                jsonPath('$.items[10].relationshipTypeName', byEquality())
                jsonPath('$.items[10].relationships.href', byEquality())
                jsonPath('$.items[11].relationshipTypeName', byEquality())
                jsonPath('$.items[11].relationships.href', byEquality())
                jsonPath('$.items[12].relationshipTypeName', byEquality())
                jsonPath('$.items[12].relationships.href', byEquality())
                jsonPath('$.items[13].relationshipTypeName', byEquality())
                jsonPath('$.items[13].relationships.href', byEquality())
                jsonPath('$.items[14].relationshipTypeName', byEquality())
                jsonPath('$.items[14].relationships.href', byEquality())
                jsonPath('$.items[15].relationshipTypeName', byEquality())
                jsonPath('$.items[15].relationships.href', byEquality())
                jsonPath('$.items[16].relationshipTypeName', byEquality())
                jsonPath('$.items[16].relationships.href', byEquality())
                jsonPath('$.items[17].relationshipTypeName', byEquality())
                jsonPath('$.items[17].relationships.href', byEquality())
                jsonPath('$.items[18].relationshipTypeName', byEquality())
                jsonPath('$.items[18].relationships.href', byEquality())
                jsonPath('$.items[19].relationshipTypeName', byEquality())
                jsonPath('$.items[19].relationships.href', byEquality())
                jsonPath('$.items[20].relationshipTypeName', byEquality())
                jsonPath('$.items[20].relationships.href', byEquality())
                jsonPath('$.items[21].relationshipTypeName', byEquality())
                jsonPath('$.items[21].relationships.href', byEquality())
                jsonPath('$.items[22].relationshipTypeName', byEquality())
                jsonPath('$.items[22].relationships.href', byEquality())
                jsonPath('$.items[23].relationshipTypeName', byEquality())
                jsonPath('$.items[23].relationships.href', byEquality())
                jsonPath('$.items[24].relationshipTypeName', byEquality())
                jsonPath('$.items[24].relationships.href', byEquality())
                jsonPath('$.items[25].relationshipTypeName', byEquality())
                jsonPath('$.items[25].relationships.href', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all the available topology relationship types in REL_EQUIPMENT_RAN domain."
        request {
            method GET()
            url "/topology-inventory/v1/domains/REL_EQUIPMENT_RAN/relationship-types"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "relationshipTypeName": "ANTENNAMODULE_INSTALLED_AT_SITE",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ANTENNAMODULE_INSTALLED_AT_SITE/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ANTENNAMODULE_SERVES_ANTENNACAPABILITY",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ANTENNAMODULE_SERVES_ANTENNACAPABILITY/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ANTENNAMODULE_SERVES_NRCELLDU",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ANTENNAMODULE_SERVES_NRCELLDU/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "NEARRTRICFUNCTION_O1LINK_SMO",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/NEARRTRICFUNCTION_O1LINK_SMO/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "NRCELLDU_USES_NRSECTORCARRIER",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/NRCELLDU_USES_NRSECTORCARRIER/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "NRSECTORCARRIER_USES_ANTENNACAPABILITY",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/NRSECTORCARRIER_USES_ANTENNACAPABILITY/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "OCUCPFUNCTION_E1LINK_OCUUPFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/OCUCPFUNCTION_E1LINK_OCUUPFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "OCUCPFUNCTION_E2LINK_NEARRTRICFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/OCUCPFUNCTION_E2LINK_NEARRTRICFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "OCUCPFUNCTION_O1LINK_SMO",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/OCUCPFUNCTION_O1LINK_SMO/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "OCUCPFUNCTION_PROVIDES_NRCELLCU",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/OCUCPFUNCTION_PROVIDES_NRCELLCU/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "OCUUPFUNCTION_E2LINK_NEARRTRICFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/OCUUPFUNCTION_E2LINK_NEARRTRICFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_E2LINK_NEARRTRICFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ODUFUNCTION_E2LINK_NEARRTRICFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_F1CLINK_OCUCPFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ODUFUNCTION_F1CLINK_OCUCPFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_F1ULINK_OCUUPFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ODUFUNCTION_F1ULINK_OCUUPFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_O1LINK_SMO",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ODUFUNCTION_O1LINK_SMO/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_PROVIDES_NRCELLDU",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ODUFUNCTION_PROVIDES_NRCELLDU/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_PROVIDES_NRSECTORCARRIER",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ODUFUNCTION_PROVIDES_NRSECTORCARRIER/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_O1LINK_SMO",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ORUFUNCTION_O1LINK_SMO/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_OFHCLINK_ODUFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ORUFUNCTION_OFHCLINK_ODUFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_OFHMLINK_ODUFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ORUFUNCTION_OFHMLINK_ODUFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_OFHMLINK_SMO",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ORUFUNCTION_OFHMLINK_SMO/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_OFHSLINK_ODUFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ORUFUNCTION_OFHSLINK_ODUFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_OFHULINK_ODUFUNCTION",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/ORUFUNCTION_OFHULINK_ODUFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "SECTOR_GROUPS_ANTENNAMODULE",
                        "relationships": {
                            "href": "/domains/REL_EQUIPMENT_RAN/relationship-types/SECTOR_GROUPS_ANTENNAMODULE/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "SECTOR_GROUPS_NRCELLDU",
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
                "totalCount": 25
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all the available topology relationship types in RAN domain."
        request {
            method GET()
            url "/topology-inventory/v1/domains/RAN/relationship-types"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "relationshipTypeName": "NEARRTRICFUNCTION_O1LINK_SMO",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/NEARRTRICFUNCTION_O1LINK_SMO/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "NRCELLDU_USES_NRSECTORCARRIER",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/NRCELLDU_USES_NRSECTORCARRIER/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "NRSECTORCARRIER_USES_ANTENNACAPABILITY",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/NRSECTORCARRIER_USES_ANTENNACAPABILITY/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "OCUCPFUNCTION_E1LINK_OCUUPFUNCTION",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/OCUCPFUNCTION_E1LINK_OCUUPFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "OCUCPFUNCTION_E2LINK_NEARRTRICFUNCTION",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/OCUCPFUNCTION_E2LINK_NEARRTRICFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "OCUCPFUNCTION_O1LINK_SMO",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/OCUCPFUNCTION_O1LINK_SMO/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "OCUCPFUNCTION_PROVIDES_NRCELLCU",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/OCUCPFUNCTION_PROVIDES_NRCELLCU/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "OCUUPFUNCTION_E2LINK_NEARRTRICFUNCTION",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/OCUUPFUNCTION_E2LINK_NEARRTRICFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_E2LINK_NEARRTRICFUNCTION",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/ODUFUNCTION_E2LINK_NEARRTRICFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_F1CLINK_OCUCPFUNCTION",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/ODUFUNCTION_F1CLINK_OCUCPFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_F1ULINK_OCUUPFUNCTION",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/ODUFUNCTION_F1ULINK_OCUUPFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_O1LINK_SMO",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/ODUFUNCTION_O1LINK_SMO/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_PROVIDES_NRCELLDU",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/ODUFUNCTION_PROVIDES_NRCELLDU/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_PROVIDES_NRSECTORCARRIER",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/ODUFUNCTION_PROVIDES_NRSECTORCARRIER/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_O1LINK_SMO",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/ORUFUNCTION_O1LINK_SMO/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_OFHCLINK_ODUFUNCTION",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/ORUFUNCTION_OFHCLINK_ODUFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_OFHMLINK_ODUFUNCTION",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/ORUFUNCTION_OFHMLINK_ODUFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_OFHMLINK_SMO",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/ORUFUNCTION_OFHMLINK_SMO/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_OFHSLINK_ODUFUNCTION",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/ORUFUNCTION_OFHSLINK_ODUFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_OFHULINK_ODUFUNCTION",
                        "relationships": {
                            "href": "/domains/RAN/relationship-types/ORUFUNCTION_OFHULINK_ODUFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "SECTOR_GROUPS_NRCELLDU",
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
                "totalCount": 21
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all the available topology relationship types in TEIV."
        request {
            method GET()
            url "/topology-inventory/v1/domains/TEIV/relationship-types"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "relationshipTypeName": "ANTENNAMODULE_INSTALLED_AT_SITE",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ANTENNAMODULE_INSTALLED_AT_SITE/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ANTENNAMODULE_SERVES_ANTENNACAPABILITY",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ANTENNAMODULE_SERVES_ANTENNACAPABILITY/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ANTENNAMODULE_SERVES_NRCELLDU",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ANTENNAMODULE_SERVES_NRCELLDU/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "CLOUDIFIEDNF_COMPRISES_NFDEPLOYMENT",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/CLOUDIFIEDNF_COMPRISES_NFDEPLOYMENT/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ENTITYTYPEA_GROUPS_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ENTITYTYPEA_GROUPS_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ENTITYTYPEA_INSTALLED_AT_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ENTITYTYPEA_INSTALLED_AT_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ENTITYTYPEA_PROVIDES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ENTITYTYPEA_PROVIDES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ENTITYTYPEA_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ENTITYTYPEA_USES_ENTITYTYPEAWITHENTITYTYPENAMELENGTHLONGERTHANSIXTYTHREECHARACTERS/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "MANAGEDELEMENT_DEPLOYED_AS_CLOUDIFIEDNF",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/MANAGEDELEMENT_DEPLOYED_AS_CLOUDIFIEDNF/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "MANAGEDELEMENT_MANAGES_NEARRTRICFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/MANAGEDELEMENT_MANAGES_NEARRTRICFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "MANAGEDELEMENT_MANAGES_OCUCPFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/MANAGEDELEMENT_MANAGES_OCUCPFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "MANAGEDELEMENT_MANAGES_OCUUPFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/MANAGEDELEMENT_MANAGES_OCUUPFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "MANAGEDELEMENT_MANAGES_ODUFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "MANAGEDELEMENT_MANAGES_ORUFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/MANAGEDELEMENT_MANAGES_ORUFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "NEARRTRICFUNCTION_O1LINK_SMO",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/NEARRTRICFUNCTION_O1LINK_SMO/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "NFDEPLOYMENT_DEPLOYED_ON_OCLOUDNAMESPACE",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/NFDEPLOYMENT_DEPLOYED_ON_OCLOUDNAMESPACE/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "NFDEPLOYMENT_SERVES_MANAGEDELEMENT",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/NFDEPLOYMENT_SERVES_MANAGEDELEMENT/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "NFDEPLOYMENT_SERVES_NEARRTRICFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/NFDEPLOYMENT_SERVES_NEARRTRICFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "NFDEPLOYMENT_SERVES_OCUCPFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/NFDEPLOYMENT_SERVES_OCUCPFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "NFDEPLOYMENT_SERVES_OCUUPFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/NFDEPLOYMENT_SERVES_OCUUPFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "NFDEPLOYMENT_SERVES_ODUFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/NFDEPLOYMENT_SERVES_ODUFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "NODECLUSTER_LOCATED_AT_OCLOUDSITE",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/NODECLUSTER_LOCATED_AT_OCLOUDSITE/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "NRCELLDU_USES_NRSECTORCARRIER",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/NRCELLDU_USES_NRSECTORCARRIER/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "NRSECTORCARRIER_USES_ANTENNACAPABILITY",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/NRSECTORCARRIER_USES_ANTENNACAPABILITY/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "OCLOUDNAMESPACE_DEPLOYED_ON_NODECLUSTER",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/OCLOUDNAMESPACE_DEPLOYED_ON_NODECLUSTER/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "OCUCPFUNCTION_E1LINK_OCUUPFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/OCUCPFUNCTION_E1LINK_OCUUPFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "OCUCPFUNCTION_E2LINK_NEARRTRICFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/OCUCPFUNCTION_E2LINK_NEARRTRICFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "OCUCPFUNCTION_O1LINK_SMO",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/OCUCPFUNCTION_O1LINK_SMO/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "OCUCPFUNCTION_PROVIDES_NRCELLCU",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/OCUCPFUNCTION_PROVIDES_NRCELLCU/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "OCUUPFUNCTION_E2LINK_NEARRTRICFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/OCUUPFUNCTION_E2LINK_NEARRTRICFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_E2LINK_NEARRTRICFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ODUFUNCTION_E2LINK_NEARRTRICFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_F1CLINK_OCUCPFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ODUFUNCTION_F1CLINK_OCUCPFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_F1ULINK_OCUUPFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ODUFUNCTION_F1ULINK_OCUUPFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_O1LINK_SMO",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ODUFUNCTION_O1LINK_SMO/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_PROVIDES_NRCELLDU",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ODUFUNCTION_PROVIDES_NRCELLDU/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ODUFUNCTION_PROVIDES_NRSECTORCARRIER",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ODUFUNCTION_PROVIDES_NRSECTORCARRIER/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_O1LINK_SMO",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ORUFUNCTION_O1LINK_SMO/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_OFHCLINK_ODUFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ORUFUNCTION_OFHCLINK_ODUFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_OFHMLINK_ODUFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ORUFUNCTION_OFHMLINK_ODUFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_OFHMLINK_SMO",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ORUFUNCTION_OFHMLINK_SMO/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_OFHSLINK_ODUFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ORUFUNCTION_OFHSLINK_ODUFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "ORUFUNCTION_OFHULINK_ODUFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/ORUFUNCTION_OFHULINK_ODUFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "PHYSICALAPPLIANCE_INSTALLEDAT_SITE",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/PHYSICALAPPLIANCE_INSTALLEDAT_SITE/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "PHYSICALAPPLIANCE_SERVES_NEARRTRICFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/PHYSICALAPPLIANCE_SERVES_NEARRTRICFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "PHYSICALAPPLIANCE_SERVES_OCUCPFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/PHYSICALAPPLIANCE_SERVES_OCUCPFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "PHYSICALAPPLIANCE_SERVES_OCUUPFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/PHYSICALAPPLIANCE_SERVES_OCUUPFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "PHYSICALAPPLIANCE_SERVES_ODUFUNCTION",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/PHYSICALAPPLIANCE_SERVES_ODUFUNCTION/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "SECTOR_GROUPS_ANTENNAMODULE",
                        "relationships": {
                            "href": "/domains/TEIV/relationship-types/SECTOR_GROUPS_ANTENNAMODULE/relationships"
                        }
                    },
                    {
                        "relationshipTypeName": "SECTOR_GROUPS_NRCELLDU",
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
                "totalCount": 50
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Get all the available topology relationship types in invalid domain."
        request {
            method GET()
            url "/topology-inventory/v1/domains/INVALID/relationship-types"
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
                "detail": "Unknown domain: INVALID, known domains: [CLOUD, EQUIPMENT, OAM, PHYSICAL, RAN, REL_CLOUD_RAN, REL_EQUIPMENT_RAN, REL_OAM_CLOUD, REL_OAM_RAN, REL_PHYSICAL_RAN, TEIV, TEST]",
                "instance": ""
            }''')
        }
    }
]
