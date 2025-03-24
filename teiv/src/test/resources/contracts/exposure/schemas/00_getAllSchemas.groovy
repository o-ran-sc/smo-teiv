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
package contracts.schemas

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "SUCCESS - 200: Get a list of all schemas"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/schemas")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "name": "_3gpp-common-yang-extensions",
                        "domain": "",
                        "revision": "2023-09-18",
                        "content": {
                            "href": "/schemas/_3gpp-common-yang-extensions/content"
                        }
                    },
                    {
                        "name": "_3gpp-common-yang-types",
                        "domain": "",
                        "revision": "2023-11-06",
                        "content": {
                            "href": "/schemas/_3gpp-common-yang-types/content"
                        }
                    },
                    {
                        "name": "ietf-geo-location",
                        "domain": "",
                        "revision": "2022-02-11",
                        "content": {
                            "href": "/schemas/ietf-geo-location/content"
                        }
                    },
                    {
                        "name": "ietf-inet-types",
                        "domain": "",
                        "revision": "2013-07-15",
                        "content": {
                            "href": "/schemas/ietf-inet-types/content"
                        }
                    },
                    {
                        "name": "ietf-yang-types",
                        "domain": "",
                        "revision": "2013-07-15",
                        "content": {
                            "href": "/schemas/ietf-yang-types/content"
                        }
                    },
                    {
                        "name": "o-ran-smo-teiv-cloud",
                        "domain": "CLOUD",
                        "revision": "2024-10-04",
                        "content": {
                        "href": "/schemas/o-ran-smo-teiv-cloud/content"
                        }
                    },
                    {
                        "name": "o-ran-smo-teiv-common-yang-extensions",
                        "domain": "",
                        "revision": "2025-02-14",
                        "content": {
                            "href": "/schemas/o-ran-smo-teiv-common-yang-extensions/content"
                        }
                    },
                    {
                        "name": "o-ran-smo-teiv-common-yang-types",
                        "domain": "",
                        "revision": "2025-03-20",
                        "content": {
                            "href": "/schemas/o-ran-smo-teiv-common-yang-types/content"
                        }
                    },
                    {
                        "name": "o-ran-smo-teiv-equipment",
                        "domain": "EQUIPMENT",
                        "revision": "2024-10-21",
                        "content": {
                        "href": "/schemas/o-ran-smo-teiv-equipment/content"
                        }
                    },
                    {
                        "name": "o-ran-smo-teiv-oam",
                        "domain": "OAM",
                        "revision": "2024-10-04",
                        "content": {
                            "href": "/schemas/o-ran-smo-teiv-oam/content"
                        }
                    },
                    {
                        "name": "o-ran-smo-teiv-ran",
                        "domain": "RAN",
                        "revision": "2025-03-20",
                        "content": {
                            "href": "/schemas/o-ran-smo-teiv-ran/content"
                        }
                    },
                    {
                        "name": "o-ran-smo-teiv-rel-cloud-ran",
                        "domain": "REL_CLOUD_RAN",
                        "revision": "2024-10-04",
                        "content": {
                            "href": "/schemas/o-ran-smo-teiv-rel-cloud-ran/content"
                        }
                    },
                    {
                        "name": "o-ran-smo-teiv-rel-equipment-ran",
                        "domain": "REL_EQUIPMENT_RAN",
                        "revision": "2024-10-08",
                        "content": {
                            "href": "/schemas/o-ran-smo-teiv-rel-equipment-ran/content"
                        }
                    },
                    {
                        "name": "o-ran-smo-teiv-rel-oam-cloud",
                        "domain": "REL_OAM_CLOUD",
                        "revision": "2024-10-04",
                        "content": {
                            "href": "/schemas/o-ran-smo-teiv-rel-oam-cloud/content"
                        }
                    },
                    {
                        "name": "o-ran-smo-teiv-rel-oam-ran",
                        "domain": "REL_OAM_RAN",
                        "revision": "2024-10-04",
                        "content": {
                            "href": "/schemas/o-ran-smo-teiv-rel-oam-ran/content"
                        }
                    },
                    {
                        "name": "test-existing-rapp-module",
                        "domain": "",
                        "revision": "2024-05-02",
                        "content": {
                            "href": "/schemas/test-existing-rapp-module/content"
                        }
                    },
                    {
                        "name": "test-module-for-deletion",
                        "domain": "",
                        "revision": "2024-05-02",
                        "content": {
                            "href": "/schemas/test-module-for-deletion/content"
                        }
                    },
                    {
                        "name":"test-module-in-deleting-state",
                        "domain":"",
                        "revision":"2024-05-02",
                        "content":{
                          "href":"/schemas/test-module-in-deleting-state/content"
                        }
                    },
                    {
                        "name":"module-rapp-module",
                        "domain":"",
                        "revision":"2024-05-01",
                        "content":{
                          "href":"/schemas/module-rapp-module/content"
                        }
                    },
                    {
                        "name": "test-built-in-module",
                        "domain": "TEST",
                        "revision": "2024-05-24",
                        "content": {
                            "href": "/schemas/test-built-in-module/content"
                        }
                    },
                    {
                        "name": "test-app-module",
                        "domain": "",
                        "revision": "2024-05-24",
                        "content": {
                            "href": "/schemas/test-app-module/content"
                        }
                    },
                    {
                        "name": "test-app-for-deletion-module",
                        "domain": "",
                        "revision": "2024-05-24",
                        "content": {
                            "href": "/schemas/test-app-for-deletion-module/content"
                        }
                    }
                ],
                "self": {
                    "href": "/schemas?offset=0&limit=500"
                },
                "first": {
                    "href": "/schemas?offset=0&limit=500"
                },
                "prev": {
                    "href": "/schemas?offset=0&limit=500"
                },
                "next": {
                    "href": "/schemas?offset=0&limit=500"
                },
                "last": {
                    "href": "/schemas?offset=0&limit=500"
                },
                "totalCount": 22
            }''')
            bodyMatchers {
                jsonPath('$.items', byType {
                    occurrence(22)
                })
                jsonPath('$.items[0].name', byEquality())
                jsonPath('$.items[0].domain', byEquality())
                jsonPath('$.items[0].revision', byEquality())
                jsonPath('$.items[0].content.href', byEquality())
                jsonPath('$.items[1].name', byEquality())
                jsonPath('$.items[1].domain', byEquality())
                jsonPath('$.items[1].revision', byEquality())
                jsonPath('$.items[1].content.href', byEquality())
                jsonPath('$.items[2].name', byEquality())
                jsonPath('$.items[2].domain', byEquality())
                jsonPath('$.items[2].revision', byEquality())
                jsonPath('$.items[2].content.href', byEquality())
                jsonPath('$.items[3].name', byEquality())
                jsonPath('$.items[3].domain', byEquality())
                jsonPath('$.items[3].revision', byEquality())
                jsonPath('$.items[3].content.href', byEquality())
                jsonPath('$.items[4].name', byEquality())
                jsonPath('$.items[4].domain', byEquality())
                jsonPath('$.items[4].revision', byEquality())
                jsonPath('$.items[4].content.href', byEquality())
                jsonPath('$.items[5].name', byEquality())
                jsonPath('$.items[5].domain', byEquality())
                jsonPath('$.items[5].revision', byEquality())
                jsonPath('$.items[5].content.href', byEquality())
                jsonPath('$.items[6].name', byEquality())
                jsonPath('$.items[6].domain', byEquality())
                jsonPath('$.items[6].revision', byEquality())
                jsonPath('$.items[6].content.href', byEquality())
                jsonPath('$.items[7].name', byEquality())
                jsonPath('$.items[7].domain', byEquality())
                jsonPath('$.items[7].revision', byEquality())
                jsonPath('$.items[7].content.href', byEquality())
                jsonPath('$.items[8].name', byEquality())
                jsonPath('$.items[8].domain', byEquality())
                jsonPath('$.items[8].revision', byEquality())
                jsonPath('$.items[8].content.href', byEquality())
                jsonPath('$.items[9].name', byEquality())
                jsonPath('$.items[9].domain', byEquality())
                jsonPath('$.items[9].revision', byEquality())
                jsonPath('$.items[9].content.href', byEquality())
                jsonPath('$.items[10].name', byEquality())
                jsonPath('$.items[10].domain', byEquality())
                jsonPath('$.items[10].revision', byEquality())
                jsonPath('$.items[10].content.href', byEquality())
                jsonPath('$.items[11].name', byEquality())
                jsonPath('$.items[11].domain', byEquality())
                jsonPath('$.items[11].revision', byEquality())
                jsonPath('$.items[11].content.href', byEquality())
                jsonPath('$.items[12].name', byEquality())
                jsonPath('$.items[12].domain', byEquality())
                jsonPath('$.items[12].revision', byEquality())
                jsonPath('$.items[12].content.href', byEquality())
                jsonPath('$.items[13].name', byEquality())
                jsonPath('$.items[13].domain', byEquality())
                jsonPath('$.items[13].revision', byEquality())
                jsonPath('$.items[13].content.href', byEquality())
                jsonPath('$.items[14].name', byEquality())
                jsonPath('$.items[14].domain', byEquality())
                jsonPath('$.items[14].revision', byEquality())
                jsonPath('$.items[14].content.href', byEquality())
                jsonPath('$.items[16].name', byEquality())
                jsonPath('$.items[16].domain', byEquality())
                jsonPath('$.items[16].revision', byEquality())
                jsonPath('$.items[16].content.href', byEquality())
                jsonPath('$.items[17].name', byEquality())
                jsonPath('$.items[17].domain', byEquality())
                jsonPath('$.items[17].revision', byEquality())
                jsonPath('$.items[17].content.href', byEquality())
                jsonPath('$.items[18].name', byEquality())
                jsonPath('$.items[18].domain', byEquality())
                jsonPath('$.items[18].revision', byEquality())
                jsonPath('$.items[18].content.href', byEquality())
                jsonPath('$.items[19].name', byEquality())
                jsonPath('$.items[19].domain', byEquality())
                jsonPath('$.items[19].revision', byEquality())
                jsonPath('$.items[19].content.href', byEquality())
                jsonPath('$.items[20].name', byEquality())
                jsonPath('$.items[20].domain', byEquality())
                jsonPath('$.items[20].revision', byEquality())
                jsonPath('$.items[20].content.href', byEquality())
                jsonPath('$.items[21].name', byEquality())
                jsonPath('$.items[21].domain', byEquality())
                jsonPath('$.items[21].revision', byEquality())
                jsonPath('$.items[21].content.href', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get a list of all schemas with offset as 0 and limit as 1."
        request {
            method GET()
            url("/topology-inventory/v1alpha11/schemas?offset=0&limit=1")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "name": "_3gpp-common-yang-extensions",
                        "domain": "",
                        "revision": "2023-09-18",
                        "content": {
                            "href": "/schemas/_3gpp-common-yang-extensions/content"
                        }
                    }
                ],
                "self": {
                    "href": "/schemas?offset=0&limit=1"
                },
                "first": {
                    "href": "/schemas?offset=0&limit=1"
                },
                "prev": {
                    "href": "/schemas?offset=0&limit=1"
                },
                "next": {
                    "href": "/schemas?offset=1&limit=1"
                },
                "last": {
                    "href": "/schemas?offset=21&limit=1"
                },
                "totalCount": 22
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get a list of all schemas with offset as 3 and limit as 3."
        request {
            method GET()
            url("/topology-inventory/v1alpha11/schemas?offset=3&limit=3")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "name": "ietf-inet-types",
                        "domain": "",
                        "revision": "2013-07-15",
                        "content": {
                            "href": "/schemas/ietf-inet-types/content"
                        }
                    },
                    {
                        "name": "ietf-yang-types",
                        "domain": "",
                        "revision": "2013-07-15",
                        "content": {
                            "href": "/schemas/ietf-yang-types/content"
                        }
                    },
                    {
                        "name": "o-ran-smo-teiv-cloud",
                        "domain": "CLOUD",
                        "revision": "2024-10-04",
                        "content": {
                            "href": "/schemas/o-ran-smo-teiv-cloud/content"
                        }
                    }
                ],
                "self": {
                    "href": "/schemas?offset=3&limit=3"
                },
                "first": {
                    "href": "/schemas?offset=0&limit=3"
                },
                "prev": {
                    "href": "/schemas?offset=0&limit=3"
                },
                "next": {
                    "href": "/schemas?offset=6&limit=3"
                },
                "last": {
                    "href": "/schemas?offset=21&limit=3"
                },
                "totalCount": 22
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get a list of all schemas with RAN domain"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/schemas?domain=RAN&offset=0&limit=100")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "name": "o-ran-smo-teiv-ran",
                        "domain": "RAN",
                        "revision": "2025-03-20",
                        "content": {
                            "href": "/schemas/o-ran-smo-teiv-ran/content"
                        }
                    }
                ],
                "self": {
                    "href": "/schemas?offset=0&limit=100&domain=RAN"
                },
                "first": {
                    "href": "/schemas?offset=0&limit=100&domain=RAN"
                },
                "prev": {
                    "href": "/schemas?offset=0&limit=100&domain=RAN"
                },
                "next": {
                    "href": "/schemas?offset=0&limit=100&domain=RAN"
                },
                "last": {
                    "href": "/schemas?offset=0&limit=100&domain=RAN"
                },
                "totalCount": 1
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get a list of all schemas with domain name containing RAN"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/schemas?domain=.*RAN.*")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [
                    {
                        "name": "o-ran-smo-teiv-rel-oam-ran",
                        "domain": "REL_OAM_RAN",
                        "revision": "2024-10-04",
                        "content": {
                            "href": "/schemas/o-ran-smo-teiv-rel-oam-ran/content"
                        }
                    },
                    {
                        "name": "o-ran-smo-teiv-rel-cloud-ran",
                        "domain": "REL_CLOUD_RAN",
                        "revision": "2024-10-04",
                        "content": {
                            "href": "/schemas/o-ran-smo-teiv-rel-cloud-ran/content"
                        }
                    },
                    {
                        "name": "o-ran-smo-teiv-ran",
                        "domain": "RAN",
                        "revision": "2024-10-08",
                        "content": {
                            "href": "/schemas/o-ran-smo-teiv-ran/content"
                        }
                    },
                    {
                        "name": "o-ran-smo-teiv-rel-equipment-ran",
                        "domain": "REL_EQUIPMENT_RAN",
                        "revision": "2024-10-04",
                        "content": {
                            "href": "/schemas/o-ran-smo-teiv-rel-equipment-ran/content"
                        }
                    }
                ],
                "self": {
                    "href": "/schemas?offset=0&limit=500&domain=.*RAN.*"
                },
                "first": {
                    "href": "/schemas?offset=0&limit=500&domain=.*RAN.*"
                },
                "prev": {
                    "href": "/schemas?offset=0&limit=500&domain=.*RAN.*"
                },
                "next": {
                    "href": "/schemas?offset=0&limit=500&domain=.*RAN.*"
                },
                "last": {
                    "href": "/schemas?offset=0&limit=500&domain=.*RAN.*"
                },
                "totalCount": 4
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get a list of all schemas with invalid domain"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/schemas?domain=INVALID")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
                "items": [],
                "self": {
                    "href": "/schemas?offset=0&limit=500&domain=INVALID"
                },
                "first": {
                    "href": "/schemas?offset=0&limit=500&domain=INVALID"
                },
                "prev": {
                    "href": "/schemas?offset=0&limit=500&domain=INVALID"
                },
                "next": {
                    "href": "/schemas?offset=0&limit=500&domain=INVALID"
                },
                "last": {
                    "href": "/schemas?offset=0&limit=500&domain=INVALID"
                },
                "totalCount": 0
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Get a list of all schemas with offset greater than totalCount"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/schemas?domain=RAN.*&offset=100")
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid Value",
                "details": "Offset cannot be larger than 0"
            }''')
        }
    }
]
