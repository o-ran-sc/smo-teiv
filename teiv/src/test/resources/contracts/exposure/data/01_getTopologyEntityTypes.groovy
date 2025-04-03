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
        description "SUCCESS - 200: Get all the available topology entity types in OAM domain."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/OAM/entity-types"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "name": "ManagedElement",
                        "entities": {
                            "href": "/domains/OAM/entity-types/ManagedElement/entities"
                        }
                    }
                ],
                "self": {
                    "href": "/domains/OAM/entity-types?offset=0&limit=500"
                },
                "first": {
                    "href": "/domains/OAM/entity-types?offset=0&limit=500"
                },
                "prev": {
                    "href": "/domains/OAM/entity-types?offset=0&limit=500"
                },
                "next": {
                    "href": "/domains/OAM/entity-types?offset=0&limit=500"
                },
                "last": {
                    "href": "/domains/OAM/entity-types?offset=0&limit=500"
                },
                "totalCount": 1
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(1)
                })
                jsonPath('$.items[0].name', byEquality())
                jsonPath('$.items[0].entities.href', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all the available topology entity types in REL_OAM_RAN domain (includes OAM and RAN domains)."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_OAM_RAN/entity-types"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "name": "AntennaCapability",
                        "entities": {
                            "href": "/domains/REL_OAM_RAN/entity-types/AntennaCapability/entities"
                        }
                    },
                    {
                        "name": "ManagedElement",
                        "entities": {
                            "href": "/domains/REL_OAM_RAN/entity-types/ManagedElement/entities"
                        }
                    },
                    {
                        "name": "NRCellCU",
                        "entities": {
                            "href": "/domains/REL_OAM_RAN/entity-types/NRCellCU/entities"
                        }
                    },
                    {
                        "name": "NRCellDU",
                        "entities": {
                            "href": "/domains/REL_OAM_RAN/entity-types/NRCellDU/entities"
                        }
                    },
                    {
                        "name": "NRSectorCarrier",
                        "entities": {
                            "href": "/domains/REL_OAM_RAN/entity-types/NRSectorCarrier/entities"
                        }
                    },
                    {
                        "name": "NearRTRICFunction",
                        "entities": {
                            "href": "/domains/REL_OAM_RAN/entity-types/NearRTRICFunction/entities"
                        }
                    },
                    {
                        "name": "OCUCPFunction",
                        "entities": {
                            "href": "/domains/REL_OAM_RAN/entity-types/OCUCPFunction/entities"
                        }
                    },
                    {
                        "name": "OCUUPFunction",
                        "entities": {
                            "href": "/domains/REL_OAM_RAN/entity-types/OCUUPFunction/entities"
                        }
                    },
                    {
                        "name": "ODUFunction",
                        "entities": {
                            "href": "/domains/REL_OAM_RAN/entity-types/ODUFunction/entities"
                        }
                    },
                    {
                        "name": "ORUFunction",
                        "entities": {
                            "href": "/domains/REL_OAM_RAN/entity-types/ORUFunction/entities"
                        }
                    },
                    {
                        "name": "Sector",
                        "entities": {
                            "href": "/domains/REL_OAM_RAN/entity-types/Sector/entities"
                        }
                    }
                ],
                "self": {
                    "href": "/domains/REL_OAM_RAN/entity-types?offset=0&limit=500"
                },
                "first": {
                    "href": "/domains/REL_OAM_RAN/entity-types?offset=0&limit=500"
                },
                "prev": {
                    "href": "/domains/REL_OAM_RAN/entity-types?offset=0&limit=500"
                },
                "next": {
                    "href": "/domains/REL_OAM_RAN/entity-types?offset=0&limit=500"
                },
                "last": {
                    "href": "/domains/REL_OAM_RAN/entity-types?offset=0&limit=500"
                },
                "totalCount": 11
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(11)
                })
                jsonPath('$.items[0].name', byEquality())
                jsonPath('$.items[0].entities.href', byEquality())
                jsonPath('$.items[1].name', byEquality())
                jsonPath('$.items[1].entities.href', byEquality())
                jsonPath('$.items[2].name', byEquality())
                jsonPath('$.items[2].entities.href', byEquality())
                jsonPath('$.items[3].name', byEquality())
                jsonPath('$.items[3].entities.href', byEquality())
                jsonPath('$.items[4].name', byEquality())
                jsonPath('$.items[4].entities.href', byEquality())
                jsonPath('$.items[5].name', byEquality())
                jsonPath('$.items[5].entities.href', byEquality())
                jsonPath('$.items[6].name', byEquality())
                jsonPath('$.items[6].entities.href', byEquality())
                jsonPath('$.items[7].name', byEquality())
                jsonPath('$.items[7].entities.href', byEquality())
                jsonPath('$.items[8].name', byEquality())
                jsonPath('$.items[8].entities.href', byEquality())
                jsonPath('$.items[9].name', byEquality())
                jsonPath('$.items[9].entities.href', byEquality())
                jsonPath('$.items[10].name', byEquality())
                jsonPath('$.items[10].entities.href', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all the available topology entity types in RAN domain."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/RAN/entity-types"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "name": "AntennaCapability",
                        "entities": {
                            "href": "/domains/RAN/entity-types/AntennaCapability/entities"
                        }
                    },
                    {
                        "name": "NRCellCU",
                        "entities": {
                            "href": "/domains/RAN/entity-types/NRCellCU/entities"
                        }
                    },
                    {
                        "name": "NRCellDU",
                        "entities": {
                            "href": "/domains/RAN/entity-types/NRCellDU/entities"
                        }
                    },
                    {
                        "name": "NRSectorCarrier",
                        "entities": {
                            "href": "/domains/RAN/entity-types/NRSectorCarrier/entities"
                        }
                    },
                    {
                        "name": "NearRTRICFunction",
                        "entities": {
                            "href": "/domains/RAN/entity-types/NearRTRICFunction/entities"
                        }
                    },
                    {
                        "name": "OCUCPFunction",
                        "entities": {
                            "href": "/domains/RAN/entity-types/OCUCPFunction/entities"
                        }
                    },
                    {
                        "name": "OCUUPFunction",
                        "entities": {
                            "href": "/domains/RAN/entity-types/OCUUPFunction/entities"
                        }
                    },
                    {
                        "name": "ODUFunction",
                        "entities": {
                            "href": "/domains/RAN/entity-types/ODUFunction/entities"
                        }
                    },
                    {
                        "name": "ORUFunction",
                        "entities": {
                            "href": "/domains/RAN/entity-types/ORUFunction/entities"
                        }
                    },
                    {
                        "name": "Sector",
                        "entities": {
                            "href": "/domains/RAN/entity-types/Sector/entities"
                        }
                    }
                ],
                "self": {
                    "href": "/domains/RAN/entity-types?offset=0&limit=500"
                },
                "first": {
                    "href": "/domains/RAN/entity-types?offset=0&limit=500"
                },
                "prev": {
                    "href": "/domains/RAN/entity-types?offset=0&limit=500"
                },
                "next": {
                    "href": "/domains/RAN/entity-types?offset=0&limit=500"
                },
                "last": {
                    "href": "/domains/RAN/entity-types?offset=0&limit=500"
                },
                "totalCount": 10
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(10)
                })
                jsonPath('$.items[0].name', byEquality())
                jsonPath('$.items[0].entities.href', byEquality())
                jsonPath('$.items[1].name', byEquality())
                jsonPath('$.items[1].entities.href', byEquality())
                jsonPath('$.items[2].name', byEquality())
                jsonPath('$.items[2].entities.href', byEquality())
                jsonPath('$.items[3].name', byEquality())
                jsonPath('$.items[3].entities.href', byEquality())
                jsonPath('$.items[4].name', byEquality())
                jsonPath('$.items[4].entities.href', byEquality())
                jsonPath('$.items[5].name', byEquality())
                jsonPath('$.items[5].entities.href', byEquality())
                jsonPath('$.items[6].name', byEquality())
                jsonPath('$.items[6].entities.href', byEquality())
                jsonPath('$.items[7].name', byEquality())
                jsonPath('$.items[7].entities.href', byEquality())
                jsonPath('$.items[8].name', byEquality())
                jsonPath('$.items[8].entities.href', byEquality())
                jsonPath('$.items[9].name', byEquality())
                jsonPath('$.items[9].entities.href', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get all the available topology entity types in TEIV domain (includes all the supported topology domains)."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/TEIV/entity-types"
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "name": "AntennaCapability",
                        "entities": {
                            "href": "/domains/TEIV/entity-types/AntennaCapability/entities"
                        }
                    },
                    {
                        "name": "AntennaModule",
                        "entities": {
                            "href": "/domains/TEIV/entity-types/AntennaModule/entities"
                        }
                    },
                    {
                        "name": "CloudifiedNF",
                        "entities": {
                            "href": "/domains/TEIV/entity-types/CloudifiedNF/entities"
                        }
                    },
                    {
                        "name": "EntityTypeA",
                        "entities": {
                            "href": "/domains/TEIV/entity-types/EntityTypeA/entities"
                        }
                    },
                    {
                        "name": "EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters",
                        "entities": {
                            "href": "/domains/TEIV/entity-types/EntityTypeAWithEntityTypeNameLengthLongerThanSixtyThreeCharacters/entities"
                        }
                    },
                    {
                        "name": "ManagedElement",
                        "entities": {
                            "href": "/domains/TEIV/entity-types/ManagedElement/entities"
                        }
                    },
                    {
                        "name": "NFDeployment",
                        "entities": {
                            "href": "/domains/TEIV/entity-types/NFDeployment/entities"
                        }
                    },
                    {
                        "name": "NRCellCU",
                        "entities": {
                            "href": "/domains/TEIV/entity-types/NRCellCU/entities"
                        }
                    },
                    {
                        "name": "NRCellDU",
                        "entities": {
                            "href": "/domains/TEIV/entity-types/NRCellDU/entities"
                        }
                    },
                    {
                        "name": "NRSectorCarrier",
                        "entities": {
                            "href": "/domains/TEIV/entity-types/NRSectorCarrier/entities"
                        }
                    },
                    {
                        "name": "NearRTRICFunction",
                        "entities": {
                            "href": "/domains/TEIV/entity-types/NearRTRICFunction/entities"
                        }
                    },
                    {
                        "name": "NodeCluster",
                        "entities": {
                            "href": "/domains/TEIV/entity-types/NodeCluster/entities"
                        }
                    },
                    {
                        "name": "OCUCPFunction",
                        "entities": {
                            "href": "/domains/TEIV/entity-types/OCUCPFunction/entities"
                        }
                    },
                    {
                        "name": "OCUUPFunction",
                        "entities": {
                            "href": "/domains/TEIV/entity-types/OCUUPFunction/entities"
                        }
                    },
                    {
                        "name": "OCloudNamespace",
                        "entities": {
                            "href": "/domains/TEIV/entity-types/OCloudNamespace/entities"
                        }
                    },
                    {
                        "name": "OCloudSite",
                        "entities": {
                            "href": "/domains/TEIV/entity-types/OCloudSite/entities"
                        }
                    },
                    {
                        "name": "ODUFunction",
                        "entities": {
                            "href": "/domains/TEIV/entity-types/ODUFunction/entities"
                        }
                    },
                    {
                        "name": "ORUFunction",
                        "entities": {
                            "href": "/domains/TEIV/entity-types/ORUFunction/entities"
                        }
                    },
                    {
                        "name": "PhysicalAppliance",
                        "entities": {
                            "href": "/domains/TEIV/entity-types/PhysicalAppliance/entities"
                        }
                    },
                    {
                        "name": "Sector",
                        "entities": {
                            "href": "/domains/TEIV/entity-types/Sector/entities"
                        }
                    },
                    {
                        "name": "Site",
                        "entities": {
                            "href": "/domains/TEIV/entity-types/Site/entities"
                        }
                    },
                    {
                        "name": "Site",
                        "entities": {
                            "href": "/domains/TEIV/entity-types/Site/entities"
                        }
                    }
                ],
                "self": {
                    "href": "/domains/TEIV/entity-types?offset=0&limit=500"
                },
                "first": {
                    "href": "/domains/TEIV/entity-types?offset=0&limit=500"
                },
                "prev": {
                    "href": "/domains/TEIV/entity-types?offset=0&limit=500"
                },
                "next": {
                    "href": "/domains/TEIV/entity-types?offset=0&limit=500"
                },
                "last": {
                    "href": "/domains/TEIV/entity-types?offset=0&limit=500"
                },
                "totalCount": 22
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(22)
                })
                jsonPath('$.items[0].name', byEquality())
                jsonPath('$.items[0].entities.href', byEquality())
                jsonPath('$.items[1].name', byEquality())
                jsonPath('$.items[1].entities.href', byEquality())
                jsonPath('$.items[2].name', byEquality())
                jsonPath('$.items[2].entities.href', byEquality())
                jsonPath('$.items[3].name', byEquality())
                jsonPath('$.items[3].entities.href', byEquality())
                jsonPath('$.items[4].name', byEquality())
                jsonPath('$.items[4].entities.href', byEquality())
                jsonPath('$.items[5].name', byEquality())
                jsonPath('$.items[5].entities.href', byEquality())
                jsonPath('$.items[6].name', byEquality())
                jsonPath('$.items[6].entities.href', byEquality())
                jsonPath('$.items[7].name', byEquality())
                jsonPath('$.items[7].entities.href', byEquality())
                jsonPath('$.items[8].name', byEquality())
                jsonPath('$.items[8].entities.href', byEquality())
                jsonPath('$.items[9].name', byEquality())
                jsonPath('$.items[9].entities.href', byEquality())
                jsonPath('$.items[10].name', byEquality())
                jsonPath('$.items[10].entities.href', byEquality())
                jsonPath('$.items[11].name', byEquality())
                jsonPath('$.items[11].entities.href', byEquality())
                jsonPath('$.items[12].name', byEquality())
                jsonPath('$.items[12].entities.href', byEquality())
                jsonPath('$.items[13].name', byEquality())
                jsonPath('$.items[13].entities.href', byEquality())
                jsonPath('$.items[14].name', byEquality())
                jsonPath('$.items[14].entities.href', byEquality())
                jsonPath('$.items[15].name', byEquality())
                jsonPath('$.items[15].entities.href', byEquality())
                jsonPath('$.items[16].name', byEquality())
                jsonPath('$.items[16].entities.href', byEquality())
                jsonPath('$.items[17].name', byEquality())
                jsonPath('$.items[17].entities.href', byEquality())
                jsonPath('$.items[18].name', byEquality())
                jsonPath('$.items[18].entities.href', byEquality())
                jsonPath('$.items[19].name', byEquality())
                jsonPath('$.items[19].entities.href', byEquality())
                jsonPath('$.items[20].name', byEquality())
                jsonPath('$.items[20].entities.href', byEquality())
                jsonPath('$.items[21].name', byEquality())
                jsonPath('$.items[21].entities.href', byEquality())
            }
        }
    },
    Contract.make {
        description "ERROR - 400: Get all the available topology entity types with invalid domain."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/INVALID/entity-types"
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Unknown domain",
                "details": "Unknown domain: INVALID, known domains: [CLOUD, EQUIPMENT, OAM, PHYSICAL, RAN, REL_CLOUD_RAN, REL_EQUIPMENT_RAN, REL_OAM_CLOUD, REL_OAM_RAN, REL_PHYSICAL_RAN, TEIV, TEST]"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Get all the available topology entity types in TEIV domain with invalid offset (greater than total count)."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/TEIV/entity-types?offset=100"
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid Value",
                "details": "Offset cannot be larger than 21"
            }''')
        }
    }
]
