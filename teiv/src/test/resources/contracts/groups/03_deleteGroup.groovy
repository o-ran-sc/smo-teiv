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
package contracts.groups

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "SUCCESS - 204: Delete an existing dynamic group"
        request {
            method DELETE()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440000")
        }
        response {
            status NO_CONTENT()
        }
    },
    Contract.make {
        description "SUCCESS - 204: Delete an existing static group"
        request {
            method DELETE()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655441000")
        }
        response {
            status NO_CONTENT()
        }
    },
    Contract.make {
        description "ERROR - 404: Delete a group that does not exist"
        request {
            method DELETE()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=non-existing-group")
        }
        response {
            status NOT_FOUND()
            body('''{
                "status": "NOT_FOUND",
                "message": "Resource Not Found",
                "details": "The requested group is not found"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 404: Delete an existing group using an invalid endpoint"
        request {
            method DELETE()
            url("/topology-inventory/v1/groups-delete/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655441000")
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
        }
    }
]
