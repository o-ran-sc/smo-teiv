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
package contracts.groups

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "SUCCESS - 204: Update group name for an dynamic existing group."
        request {
            method PUT()
            url "/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440050/name"
            headers {
                contentType("application/json")
            }
            body('''{
                "name": "test-group-new-name"
            }''')
        }
        response {
            status NO_CONTENT()
        }
    },
    Contract.make {
        description "SUCCESS - 204: Update group name for an static existing group, where new name matches the name of another group already in the database."
        request {
            method PUT()
            url "/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440051/name"
            headers {
                contentType("application/json")
            }
            body('''{
                "name": "test-group-new-name"
            }''')
        }
        response {
            status NO_CONTENT()
        }
    },
    Contract.make {
        description "NOT FOUND - 404: Update group name for a group that doesn't exists."
        request {
            method PUT()
            url "/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:non-existing-group/name"
            headers {
                contentType("application/json")
            }
            body('''{
                "name": "test-group-new-name"
            }''')
        }
        response {
            status NOT_FOUND()
            headers {
                contentType('application/json')
            }
            body('''{
                "status": "NOT_FOUND",
                "message": "Resource Not Found",
                "details": "The requested group is not found"
            }''')
            bodyMatchers {
                jsonPath('$.status', byEquality())
                jsonPath('$.message', byEquality())
                jsonPath('$.details', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 400: Update group name for an existing group with an invalid name."
        request {
            method PUT()
            url "/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440050/name"
            headers {
                contentType("application/json")
            }
            body('''{
                "name": "test-group-new-name-which-is-invalid-since-it-is-toooooo-long-than-allowed-group-name-length-that-is-greater-than-hundred-and-fifty-characters-in-length"
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "Bad Request",
                "status": 400,
                "detail": "Invalid request content.",
                "instance": "/topology-inventory/v1alpha11/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440050/name"
            }''')
        }
    }
]
