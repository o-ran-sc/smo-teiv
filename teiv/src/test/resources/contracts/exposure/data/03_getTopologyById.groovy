/*
 *  ============LICENSE_START=======================================================
 *  Copyright (C) 2024 Ericsson
 *  Modifications Copyright (C) 2025 OpenInfra Foundation Europe
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
        description "SUCCESS - 200: Get topology for ManagedElement entity with id 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9' in OAM domain."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/OAM/entity-types/ManagedElement/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9"
        }
        response {
            status OK()
            headers {
                contentType('application/yang.data+json')
            }
            body('''{
                "o-ran-smo-teiv-oam:ManagedElement": [
                    {
                        "decorators": {},
                        "classifiers": [],
                        "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9",
                        "sourceIds": [
                            "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9",
                            "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"
                        ]
                    }
                ]
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get topology for ODUFunction entity with id 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9' with REL_EQUIPMENT_RAN (domain including other domains)."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_EQUIPMENT_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9"
        }
        response {
            status OK()
            headers {
                contentType('application/yang.data+json')
            }
            body('''{
                "o-ran-smo-teiv-ran:ODUFunction": [
                    {
                        "decorators": {
                            "test-app-module:textdata": "ORAN"
                        },
                        "classifiers": [
                            "test-app-module:Indoor",
                            "test-app-module:Weekend"
                        ],
                        "attributes": {
                            "gNBDUId": null,
                            "gNBId": 9,
                            "gNBIdLength": 1
                        },
                        "id": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9",
                        "sourceIds": [
                            "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9",
                            "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"
                        ]
                    }
                ]
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Get topology for ODUFunction entity with non existing id 'urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=99999'."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_EQUIPMENT_RAN/entity-types/ODUFunction/entities/urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=99999"
        }
        response {
            status NOT_FOUND()
            headers {
                contentType('application/json')
            }
            body('''{
                "status": "NOT_FOUND",
                "message": "Resource Not Found",
                "details": "The requested resource is not found. ID: urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=99999"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Get topology for ODUFunction entity with  with no supported topology id - 3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9 ."
        request {
            method GET()
            url "/topology-inventory/v1alpha11/domains/REL_EQUIPMENT_RAN/entity-types/ODUFunction/entities/3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9"
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Topology ID format not supported",
                "details": "Topology ID : 3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9 is not in supported format. Topology ID should start with urn:"
            }''')
        }
    }
]
