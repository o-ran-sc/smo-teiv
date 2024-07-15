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
package contracts.ran.schemas
import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "SUCCESS - 200: Get a list of all schemas"
        request {
            method GET()
            url("/topology-inventory/v1alpha11/schemas?offset=0&limit=100")
        }
        response {
            status OK()
            headers {
                contentType('application/json')
            }
            body('''{
    "items": [
        {
            "name": "o-ran-smo-teiv-cloud-to-ran",
            "domain": "CLOUD_TO_RAN",
            "revision": "2023-06-26",
            "content": {
                "href": "/schemas/o-ran-smo-teiv-cloud-to-ran/content"
            }
        }
    ],
    "self": {
        "href": "/schemas?offset=0&limit=100"
    },
    "first": {
        "href": "/schemas?offset=0&limit=100"
    },
    "prev": {
        "href": "/schemas?offset=0&limit=100"
    },
    "next": {
        "href": "/schemas?offset=0&limit=100"
    },
    "last": {
        "href": "/schemas?offset=0&limit=100"
    }
}''')
        }
    }
]
