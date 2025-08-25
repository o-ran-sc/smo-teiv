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
        description "SUCCESS - 204: Delete an existing classifier schema - test-app-for-deletion-module"
        request {
            method DELETE()
            url("/topology-inventory/v1/user-defined-schemas/test-app-for-deletion-module")
        }
        response {
            status NO_CONTENT()
        }
    },
    Contract.make {
        description "ERROR - 400: Delete a schema that does not exists"
        request {
            method DELETE()
            url("/topology-inventory/v1/user-defined-schemas/does-not-exist-rapp-module")
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid schema name",
                "details": "Invalid schema name: does-not-exist-rapp-module"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Delete a schema that is in deleting status"
        request {
            method DELETE()
            url("/topology-inventory/v1/user-defined-schemas/test-module-in-deleting-state")
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Invalid schema name",
                "details": "Invalid schema name: test-module-in-deleting-state"
            }''')
        }
    }
]
