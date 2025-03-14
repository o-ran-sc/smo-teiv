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
package contracts.classifier

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "NOT FOUND - 404: Delete classifiers with wrong entity ids."
        request {
            method POST()
            url "/topology-inventory/v1alpha11/classifiers"
            headers {
                contentType("application/json")
                accept('application/problem+json')
            }
            body('''{
                "classifiers":[
                        "test-app-module:Rural",
                        "test-app-module:Weekend"
                 ],
                "entityIds": [
                    "WRONG_ENTITY_ID",
                    "urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A"
                ],
                "operation": "delete"
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
            "details": "The requested resource with the following ids cannot be found. Entities: [WRONG_ENTITY_ID] Relationships: []"}''')
            bodyMatchers {
                jsonPath('$.status', byEquality())
                jsonPath('$.message', byEquality())
                jsonPath('$.details', byEquality())
            }
        }
    },
    Contract.make {
        description "NOT FOUND - 404: Delete classifiers with wrong relationship ids."
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
                "operation": "delete"
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
            "details": "The requested resource with the following ids cannot be found. Entities: [] Relationships: [WRONG_RELATIONSHIP_ID]"}''')
            bodyMatchers {
                jsonPath('$.status', byEquality())
                jsonPath('$.message', byEquality())
                jsonPath('$.details', byEquality())
            }
        }
    },
    Contract.make {
        description "BAD REQUEST - 400: Delete classifiers from non-existing schema."
        request {
            method POST()
            url "/topology-inventory/v1alpha11/classifiers"
            headers {
                contentType("application/json")
                accept('application/problem+json')
            }
            body('''{
                "classifiers": [
                        "test-app-module-wrong:Rural",
                        "test-app-module:Weekend"
                 ],
                "entityIds": [
                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,ODUFunction=13",
                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,ODUFunction=14"
                ],
                "operation": "delete"
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
            "status": "BAD_REQUEST",
            "message": "Invalid schema name",
            "details": "Invalid schema name: test-app-module-wrong"}''')
            bodyMatchers {
                jsonPath('$.status', byEquality())
                jsonPath('$.message', byEquality())
                jsonPath('$.details', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 204: Delete valid classifiers, no topology object given."
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
                "operation": "delete"
            }''')
        }
        response {
            status NO_CONTENT()
        }
    },
    Contract.make {
        description "SUCCESS - 204: Delete valid classifiers on entities."
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
                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,ODUFunction=13",
                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,ODUFunction=14"
                ],
                "operation": "delete"
            }''')
        }
        response {
            status NO_CONTENT()
        }
    },
    Contract.make {
        description "SUCCESS - 204: Delete valid classifiers on relationships."
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
                    "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=6F7BFBD3DA2A9A592084C75242210A33C9DCF10CFCD53B761A6ACCD385132921679EC3C16394A4DEEE5883712C9719511388230151BA84FBF209DFCFB639E2EA",
                    "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=DD9259A1B57FF2BB9DEC77C29DBFA4A5C49960D80622F603809ACA47E786DDD5C7ABD267D554A7C796477A9B2E02E072A8E682E4ED38F331BFB6DC3827CE4DB7"
                ],
                "operation": "delete"
            }''')
        }
        response {
            status NO_CONTENT()
        }
    },
    Contract.make {
        description "SUCCESS - 204: Delete valid classifiers on entities and relationships."
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
                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,ODUFunction=13",
                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,ODUFunction=14"
                ],
                "relationshipIds": [
                    "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=6F7BFBD3DA2A9A592084C75242210A33C9DCF10CFCD53B761A6ACCD385132921679EC3C16394A4DEEE5883712C9719511388230151BA84FBF209DFCFB639E2EA",
                    "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=DD9259A1B57FF2BB9DEC77C29DBFA4A5C49960D80622F603809ACA47E786DDD5C7ABD267D554A7C796477A9B2E02E072A8E682E4ED38F331BFB6DC3827CE4DB7"
                ],
                "operation": "delete"
            }''')
        }
        response {
            status NO_CONTENT()
        }
    }
]
