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
package contracts.classifier

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "NOT FOUND - 404: Merge classifiers with wrong entity ids."
        request {
            method POST()
            url "/topology-inventory/v1alpha11/classifiers"
            headers {
                contentType("application/json")
                accept('application/problem+json')
            }
            body('''{
                "classifiers": [
                        "test-app-module:Rural",
                        "test-app-module:Weekend"
                 ],
                "entityIds": [
                    "WRONG_ENTITY_ID",
                    "urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A"
                ],
                "operation": "merge"
            }''')
        }
        response {
            status NOT_FOUND()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                    "status": "NOT_FOUND",
                    "message": "Resource Not Found",
                    "details": "The requested resource with the following ids cannot be found. Entities: [WRONG_ENTITY_ID] Relationships: []"
                }''')
            bodyMatchers {
                jsonPath('$.status', byEquality())
                jsonPath('$.message', byEquality())
                jsonPath('$.details', byEquality())
            }
        }
    },
    Contract.make {
        description "NOT FOUND - 404: Merge classifiers with wrong relationship ids."
        request {
            method POST()
            url "/topology-inventory/v1alpha11/classifiers"
            headers {
                contentType("application/json")
                accept('application/problem+json')
            }
            body('''{
                "classifiers": [
                    "test-app-module:Rural",
                    "test-app-module:Weekend"
                ],
                "relationshipIds": [
                    "urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_ANTENNAMODULE=44F4F4FC906E9A7525065E4565246F7469CBD11FC7752C61EA6D74776845900AFF472DCAACA1F66443490B6CE0DD9AC9A5E1467022118599F6B4C6EC63400512",
                    "WRONG_RELATIONSHIP_ID"
                ],
                "operation": "merge"
            }''')
        }
        response {
            status NOT_FOUND()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                    "status": "NOT_FOUND",
                    "message": "Resource Not Found",
                    "details": "The requested resource with the following ids cannot be found. Entities: [] Relationships: [WRONG_RELATIONSHIP_ID]"
                }''')
            bodyMatchers {
                jsonPath('$.status', byEquality())
                jsonPath('$.message', byEquality())
                jsonPath('$.details', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 204: Merge valid classifiers to entities and relationships (add)."
        request {
            method POST()
            url "/topology-inventory/v1alpha11/classifiers"
            headers {
                contentType("application/json")
                accept('application/problem+json')
            }
            body('''{
                "classifiers": [
                    "test-app-module:Weekday"
                ],
                "entityIds": [
                    "urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7",
                    "urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A"
                ],
                "relationshipIds": [
                    "urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_ANTENNAMODULE=44F4F4FC906E9A7525065E4565246F7469CBD11FC7752C61EA6D74776845900AFF472DCAACA1F66443490B6CE0DD9AC9A5E1467022118599F6B4C6EC63400512"
                ],
                "operation": "merge"
            }''')
        }
        response {
            status NO_CONTENT()
        }
    }
]
