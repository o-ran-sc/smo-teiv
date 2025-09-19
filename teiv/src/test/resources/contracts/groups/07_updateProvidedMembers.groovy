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
        description "SUCCESS - 204: Merge into provided members in an existing static topology group"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "merge",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=5"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=6"
                            }
                        ]
                    }
                ]
            }''')
        }
        response {
            status NO_CONTENT()
        }
    },
    Contract.make {
        description "SUCCESS - 204: Merge into provided members in an existing static topology group which has no static group data present in db"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440212/provided-members-operations")
            headers {
                contentType("application/json")
            }

            body('''{
                "operation": "merge",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-oam:ManagedElement": [
                            {
                                "id": "urn:3gpp:dn:NRCellDU=1,ODUFunction=1,ManagedElement=1"
                            },
                            {
                                "id": "urn:3gpp:dn:NRCellDU=1,ODUFunction=1,ManagedElement=2"
                            }
                        ]
                    }
                ]
            }''')
        }
        response {
            status NO_CONTENT()
        }
    },
    Contract.make {
        description "SUCCESS - 204: Merge into provided members in an existing static topology group with new topology type and its associated provided members"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations")
            headers {
                contentType("application/json")
            }

            body('''{
                "operation": "merge",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-equipment:AntennaModule": [
                            {
                                "id": "urn:3gpp:dn:NRCellDU=1,ODUFunction=1,AntennaModule=1"
                            },
                            {
                                "id": "urn:3gpp:dn:NRCellDU=1,ODUFunction=1,AntennaModule=2"
                            }
                        ]
                    }
                ]
            }''')
        }
        response {
            status NO_CONTENT()
        }
    },
    Contract.make {
        description "SUCCESS - 204: Merge duplicate ids (some already present in db) into provided members in an existing static topology group"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "merge",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=3"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=3"
                            }
                        ]
                    }
                ]
            }''')
        }
        response {
            status NO_CONTENT()
        }
    },
    Contract.make {
        description "SUCCESS - 204: Remove from provided members in an existing static topology group"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "remove",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=2"
                            }
                        ]
                    }
                ]
            }''')
        }
        response {
            status NO_CONTENT()
        }
    },
    Contract.make {
        description "SUCCESS - 204: Remove from provided members in an existing static topology group, where user requests to delete same id multiple times"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "remove",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=3"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=3"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=4"
                            }
                        ]
                    }
                ]
            }''')
        }
        response {
            status NO_CONTENT()
        }
    },
    Contract.make {
        description "SUCCESS - 204: Remove from provided members (with duplicates in db) in an existing static topology"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440215/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "remove",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRSectorCarrier": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,NRSectorCarrier=1"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,NRSectorCarrier=2"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,NRSectorCarrier=3"
                            }
                        ]
                    }
                ]
            }''')
        }
        response {
            status NO_CONTENT()
        }
    },
    Contract.make {
        description "ERROR - 400: Remove from provided members in an existing dynamic topology group"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440050/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "remove",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=2"
                            }
                        ]
                    }
                ]
            }''')
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
                "detail": "The specified provided members for group cannot be updated as group type is not static",
                "instance": ""
            }''')
            bodyMatchers {
                jsonPath('$.type', byEquality())
                jsonPath('$.title', byEquality())
                jsonPath('$.status', byEquality())
                jsonPath('$.detail', byEquality())
                jsonPath('$.instance', byEquality())
            }
        }
    },
    Contract.make {
        description "ERROR - 400: Merge provided members into static group when merge request is more than allowed in a single POST request"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "merge",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-equipment:AntennaModule": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,AntennaModule=1"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:OCUCPFunction": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,OCUCPFunction=1"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,OCUCPFunction=2"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,OCUCPFunction=3"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,OCUCPFunction=4"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:NRCellCU": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,NRCellCU=1"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,NRCellCU=2"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,NRCellCU=3"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,NRCellDU=1"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,NRCellDU=2"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,NRCellDU=3"
                            }
                        ]
                    }
                ]
            }''')
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
                "detail": "The total number of topology identifiers in the request is 11. The maximum allowed in a single request is 10.",
                "instance": ""
            }''')
            bodyMatchers {
                jsonPath('$.type', byEquality())
                jsonPath('$.title', byEquality())
                jsonPath('$.status', byEquality())
                jsonPath('$.detail', byEquality())
                jsonPath('$.instance', byEquality())
            }
        }
    },
    Contract.make {
        description "ERROR - 400: Merge provided members into static group which contains max number of provided members"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440214/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "merge",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-equipment:AntennaModule": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,AntennaModule=1"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,AntennaModule=2"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,AntennaModule=3"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,AntennaModule=4"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,AntennaModule=5"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,AntennaModule=6"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,AntennaModule=7"
                            }
                        ]
                    }
                ]
            }''')
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
                "detail": "Merging topology identifiers in request to the group will result in 17 members. The maximum allowed members in the static group is 15.",
                "instance": "" 
            }''')
            bodyMatchers {
                jsonPath('$.type', byEquality())
                jsonPath('$.title', byEquality())
                jsonPath('$.status', byEquality())
                jsonPath('$.detail', byEquality())
                jsonPath('$.instance', byEquality())
            }
        }
    },
    Contract.make {
        description "ERROR - 400: Merge provided members into static group which on merge will exceed the max limit assigned to provided members"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440216/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "merge",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-equipment:AntennaModule": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,AntennaModule=11"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,AntennaModule=22"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:NRSectorCarrier": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,NRSectorCarrier=11"
                            }
                        ]
                    },
                    {
                        "o-ran-smo-teiv-ran:NRCellCU": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,NRCellCU=11"
                            }
                        ]
                    }
                ]
            }''')
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
                "detail": "Merging topology identifiers in request to the group will result in 16 members. The maximum allowed members in the static group is 15.",
                "instance": ""
            }''')
            bodyMatchers {
                jsonPath('$.type', byEquality())
                jsonPath('$.title', byEquality())
                jsonPath('$.status', byEquality())
                jsonPath('$.detail', byEquality())
                jsonPath('$.instance', byEquality())
            }
        }
    },
    Contract.make {
        description "ERROR - 400: Merge into provided members in an existing static topology group with invalid module name for the entity type"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440341/provided-members-operations")
            headers {
                contentType("application/json")
            }

            body('''{
                "operation": "merge",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:ManagedElement": [
                            {
                                "id": "urn:3gpp:dn:NRCellDU=1,ODUFunction=1,ManagedElement=1"
                            },
                            {
                                "id": "urn:3gpp:dn:NRCellDU=1,ODUFunction=1,ManagedElement=2"
                            }
                        ]
                    }
                ]
            }''')
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
                "detail": "Unable to parse the given providedMembers. Invalid topology type 'o-ran-smo-teiv-ran:ManagedElement', not found in the model",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 404: Merge provided members into a non existing topology group"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=non-existing-group/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "merge",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=2"
                            }
                        ]
                    }
                ]
            }''')
        }
        response {
            status NOT_FOUND()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "NOT_FOUND",
                "status": 404,
                "detail": "The requested group is not found",
                "instance": ""
            }''')
            bodyMatchers {
                jsonPath('$.type', byEquality())
                jsonPath('$.title', byEquality())
                jsonPath('$.status', byEquality())
                jsonPath('$.detail', byEquality())
                jsonPath('$.instance', byEquality())
            }
        }
    },
    Contract.make {
        description "ERROR - 400: Remove non existing topology identifiers from provided members in a static topology group"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "remove",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=non-existing-id-1"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=non-existing-id-2"
                            }
                        ]
                    }
                ]
            }''')
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
                "detail": "The specified provided members topology identifier: [urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=non-existing-id-2, urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=non-existing-id-1] is not part of the group.",
                "instance": ""
            }''')
            bodyMatchers {
                jsonPath('$.type', byEquality())
                jsonPath('$.title', byEquality())
                jsonPath('$.status', byEquality())
                jsonPath('$.detail', byEquality())
                jsonPath('$.instance', byEquality())
            }
        }
    },
    Contract.make {
        description "ERROR - 400: Remove from provided members in an existing topology group and no static group data"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440213/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "remove",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=2"
                            }
                        ]
                    }
                ]
            }''')
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
                "detail": "The specified topology entity/relation: o-ran-smo-teiv-ran:NRCellDU is not part of the group.",
                "instance": ""
            }''')
            bodyMatchers {
                jsonPath('$.type', byEquality())
                jsonPath('$.title', byEquality())
                jsonPath('$.status', byEquality())
                jsonPath('$.detail', byEquality())
                jsonPath('$.instance', byEquality())
            }
        }
    },
    Contract.make {
        description "ERROR - 400: Remove from provided members in an existing static topology group where body is empty"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{}''')
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
                "instance": "/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Remove from provided members in an existing static topology group where topology_type provided is not present in static group"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "remove",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-oam:ManagedElement": [
                            {
                                "id": "urn:3gpp:dn:NRCellDU=1,ODUFunction=1, ManagedElement=1"
                            },
                            {
                                "id": "urn:3gpp:dn:NRCellDU=1,ODUFunction=1, ManagedElement=2"
                            }
                        ]
                    }
                ]
            }''')
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
                "detail": "The specified topology entity/relation: o-ran-smo-teiv-oam:ManagedElement is not part of the group.",
                "instance": ""
            }''')
            bodyMatchers {
                jsonPath('$.type', byEquality())
                jsonPath('$.title', byEquality())
                jsonPath('$.status', byEquality())
                jsonPath('$.detail', byEquality())
                jsonPath('$.instance', byEquality())
            }
        }
    },
    Contract.make {
        description "ERROR - 400: Remove from provided members in an existing static topology group with invalid operation type in body"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "invalid",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:ManagedElement": [
                            {
                                "id": "urn:3gpp:dn:NRCellDU=1,ODUFunction=1, ManagedElement=1"
                            },
                            {
                                "id": "urn:3gpp:dn:NRCellDU=1,ODUFunction=1, ManagedElement=2"
                            }
                        ]
                    }
                ]
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
                "detail": "Failed to read request",
                "instance": "/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Remove from provided members in an existing static topology group with operation type missing in body"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations")
            headers {
                contentType("application/json")
            }

            body('''{
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:ManagedElement": [
                            {
                                "id": "urn:3gpp:dn:NRCellDU=1,ODUFunction=1, ManagedElement=1"
                            },
                            {
                                "id": "urn:3gpp:dn:NRCellDU=1,ODUFunction=1, ManagedElement=2"
                            }
                        ]
                    }
                ]
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
                "instance": "/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Remove from provided members in an existing static topology group with invalid providedMembers(empty list)"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "remove",
                "providedMembers": []
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
                "instance": "/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations"
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Remove from provided members in an existing static topology group with invalid providedMembers"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "remove",
                "providedMembers": [
                    "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1",
                    "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=2"
                ]
            }''')
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
                "detail": "Unable to parse the given providedMembers. List elements are not of the type object.",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Remove from provided members in an existing static topology group with invalid providedMembers"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "remove",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1",
                            "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=2"
                        ]
                    }
                ]
            }''')
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
                "detail": "Unable to parse the given providedMembers. List elements are not of the type object.",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Remove from provided members in an existing static topology group with invalid providedMembers(list contains empty object)"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "remove",
                "providedMembers": [
                    {}
                ]
            }''')
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
                "detail": "Unable to parse the given providedMembers. Empty object present in providedMembers.",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Remove from provided members in an existing static topology group with invalid providedMembers(list contains empty object)"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "remove",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {}
                        ]
                    }
                ]
            }''')
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
                "detail": "Unable to parse the given providedMembers. Empty object present in providedMembers.",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Remove from provided members in an existing static topology group with invalid providedMembers(invalid id key)"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "remove",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "idKey": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1"
                            }
                        ]
                    }
                ]
            }''')
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
                "detail": "Unable to parse the given providedMembers. Invalid key/value present in {\\"idKey\\":\\"urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1\\"}.",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Remove from provided members in an existing static topology group with invalid providedMembers(id value is not of type string)"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "remove",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": ["urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1"]
                            }
                        ]
                    }
                ]
            }''')
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
                "detail": "Unable to parse the given providedMembers. Invalid key/value present in {\\"id\\":[\\"urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1\\"]}.",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Remove from provided members in an existing static topology group with invalid providedMembers(group id present in providedMembers)"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "remove",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440050"
                            }
                        ]
                    }
                ]
            }''')
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
                "detail": "Unable to parse the given providedMembers. Nested topology groups is not supported. Provided members contain topology group id: urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440050",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Remove from provided members in an existing static topology group with invalid providedMembers"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "remove",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1",
                                "extraKey": "extraValue"
                            }
                        ]
                    }
                ]
            }''')
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
                "detail": "Unable to parse the given providedMembers. More than one key:value present in {\\"id\\":\\"urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1\\",\\"extraKey\\":\\"extraValue\\"}.",
                "instance": "" 
            }''')
        }
    },
    Contract.make {
        description "ERROR - 400: Remove from provided members in an existing static topology group with invalid providedMembers(more than one topology type in an anonymous object)"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "remove",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1"
                            }
                        ],
                        "o-ran-smo-teiv-ran:ODUFunction": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1"
                            }
                        ]
                    }
                ]
            }''')
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
                "detail": "Unable to parse the given providedMembers. More than one key:value present in {\\"o-ran-smo-teiv-ran:NRCellDU\\":[{\\"id\\":\\"urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1\\"}],\\"o-ran-smo-teiv-ran:ODUFunction\\":[{\\"id\\":\\"urn:3gpp:dn:ManagedElement=1,ODUFunction=1\\"}]}.",
                "instance": ""
            }''')
        }
    },
    Contract.make {
        description "ERROR - 404: Remove provided members from a non existing topology group"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=non-existing-group/provided-members-operations")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "remove",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=2"
                            }
                        ]
                    }
                ]
            }''')
        }
        response {
            status NOT_FOUND()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "type": "about:blank",
                "title": "NOT_FOUND",
                "status": 404,
                "detail": "The requested group is not found",
                "instance": ""
            }''')
            bodyMatchers {
                jsonPath('$.type', byEquality())
                jsonPath('$.title', byEquality())
                jsonPath('$.status', byEquality())
                jsonPath('$.detail', byEquality())
                jsonPath('$.instance', byEquality())
            }
        }
    },
    Contract.make {
        description "ERROR - 400: Invalid endpoint"
        request {
            method POST()
            url("/topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-update")
            headers {
                contentType("application/json")
            }
            body('''{
                "operation": "remove",
                "providedMembers": [
                    {
                        "o-ran-smo-teiv-ran:NRCellDU": [
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1"
                            },
                            {
                                "id": "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=2"
                            }
                        ]
                    }
                ]
            }''')
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
                "detail": "No static resource topology-inventory/v1/groups/urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211/provided-members-update.",
                "instance": ""
            }''')
        }
    }
]
