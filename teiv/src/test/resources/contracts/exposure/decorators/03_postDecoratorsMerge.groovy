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
package contracts.decorators

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "NOT FOUND - 404: Merge decorators with wrong entity ids."
        request {
            method POST()
            url "/topology-inventory/v1/manage-decorators"
            headers {
                contentType("application/json")
                accept('application/problem+json')
            }
            body('''{
                "decorators": {
                    "test-app-module:textdata": "Budapest",
                    "test-app-module:intdata": 123
                },
                "entityIds": [
                    "urn:WRONG_ENTITY_ID",
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
                "details": "The requested resource with the following ids cannot be found. Entities: [urn:WRONG_ENTITY_ID] Relationships: []"
            }''')
            bodyMatchers {
                jsonPath('$.status', byEquality())
                jsonPath('$.message', byEquality())
                jsonPath('$.details', byEquality())
            }
        }
    },
    Contract.make {
        description "NOT FOUND - 404: Merge decorators where entity id does not start with urn:"
        request {
            method POST()
            url "/topology-inventory/v1/manage-decorators"
            headers {
                contentType("application/json")
                accept('application/problem+json')
            }
            body('''{
                "decorators": {
                    "test-app-module:textdata": "Budapest",
                    "test-app-module:intdata": 123
                },
                "entityIds": [
                    "WRONG_ENTITY_ID",
                    "urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A"
                ],
                "operation": "merge"
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Topology ID format not supported",
                "details": "Topology ID : WRONG_ENTITY_ID is not in supported format. Topology ID should start with urn:"
            }''')
        }
    },
    Contract.make {
        description "NOT FOUND - 404: Merge decorators with wrong relationship ids."
        request {
            method POST()
            url "/topology-inventory/v1/manage-decorators"
            headers {
                contentType("application/json")
                accept('application/problem+json')
            }
            body('''{
                "decorators": {
                    "test-app-module:textdata": "Budapest",
                    "test-app-module:intdata": 123
                },
                "relationshipIds": [
                    "urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_ANTENNAMODULE=44F4F4FC906E9A7525065E4565246F7469CBD11FC7752C61EA6D74776845900AFF472DCAACA1F66443490B6CE0DD9AC9A5E1467022118599F6B4C6EC63400512",
                    "urn:WRONG_RELATIONSHIP_ID"
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
                "details": "The requested resource with the following ids cannot be found. Entities: [] Relationships: [urn:WRONG_RELATIONSHIP_ID]"
            }''')
            bodyMatchers {
                jsonPath('$.status', byEquality())
                jsonPath('$.message', byEquality())
                jsonPath('$.details', byEquality())
            }
        }
    },
    Contract.make {
        description "ERROR - 400: Merge decorators where relationship id does not start with urn:"
        request {
            method POST()
            url "/topology-inventory/v1/manage-decorators"
            headers {
                contentType("application/json")
                accept('application/problem+json')
            }
            body('''{
                "decorators": {
                    "test-app-module:textdata": "Budapest",
                    "test-app-module:intdata": 123
                },
                "relationshipIds": [
                    "urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_ANTENNAMODULE=44F4F4FC906E9A7525065E4565246F7469CBD11FC7752C61EA6D74776845900AFF472DCAACA1F66443490B6CE0DD9AC9A5E1467022118599F6B4C6EC63400512",
                    "WRONG_RELATIONSHIP_ID"
                ],
                "operation": "merge"
            }''')
        }
        response {
            status BAD_REQUEST()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "status": "BAD_REQUEST",
                "message": "Topology ID format not supported",
                "details": "Topology ID : WRONG_RELATIONSHIP_ID is not in supported format. Topology ID should start with urn:"
            }''')
        }
    },
    Contract.make {
        description "NOT FOUND - 404: Merge decorators with wrong entity and relationship ids."
        request {
            method POST()
            url "/topology-inventory/v1/manage-decorators"
            headers {
                contentType("application/json")
                accept('application/problem+json')
            }
            body('''{
                "decorators": {
                    "test-app-module:textdata": "Budapest",
                    "test-app-module:intdata": 123
                },
                "entityIds": [
                    "urn:WRONG_ENTITY_ID",
                    "urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A"
                ],
                "relationshipIds": [
                    "urn:WRONG_RELATIONSHIP_ID"
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
                "details": "The requested resource with the following ids cannot be found. Entities: [urn:WRONG_ENTITY_ID] Relationships: [urn:WRONG_RELATIONSHIP_ID]"
            }''')
            bodyMatchers {
                jsonPath('$.status', byEquality())
                jsonPath('$.message', byEquality())
                jsonPath('$.details', byEquality())
            }
        }
    },
    Contract.make {
        description "NOT FOUND - 404: Merge invalid decorators."
        request {
            method POST()
            url "/topology-inventory/v1/manage-decorators"
            headers {
                contentType("application/json")
                accept('application/problem+json')
            }
            body('''{
                "decorators": {
                    "test-app-module:te": "Budapest",
                    "test-app-module:intdata": "123"
                },
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
            status NOT_FOUND()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "status": "NOT_FOUND",
                "message": "Invalid decorators",
                "details": "The provided decorators are invalid {test-app-module:intdata=is_not_compatible, test-app-module:te=is_not_available}"
            }''')
            bodyMatchers {
                jsonPath('$.status', byEquality())
                jsonPath('$.message', byEquality())
                jsonPath('$.details', byEquality())
            }
        }
    },
    Contract.make {
        description "SUCCESS - 204: Merge valid decorators, no topology object given."
        request {
            method POST()
            url "/topology-inventory/v1/manage-decorators"
            headers {
                contentType("application/json")
                accept('application/problem+json')
            }
            body('''{
                "decorators": {
                    "test-app-module:textdata": "Budapest",
                    "test-app-module:intdata": 123
                },
                "operation": "merge"
            }''')
        }
        response {
            status NO_CONTENT()
        }
    },
    Contract.make {
        description "SUCCESS - 204: Merge valid decorators to entities (add)."
        request {
            method POST()
            url "/topology-inventory/v1/manage-decorators"
            headers {
                contentType("application/json")
                accept('application/problem+json')
            }
            body('''{
                "decorators": {
                    "test-app-module:textdata": "Budapest",
                    "test-app-module:intdata": 123
                },
                "entityIds": [
                    "urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7",
                    "urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A"
                ],
                "operation": "merge"
            }''')
        }
        response {
            status NO_CONTENT()
        }
    },
    Contract.make {
        description "SUCCESS - 204: Merge valid decorators to relationships (add)."
        request {
            method POST()
            url "/topology-inventory/v1/manage-decorators"
            headers {
                contentType("application/json")
                accept('application/problem+json')
            }
            body('''{
                "decorators": {
                    "test-app-module:textdata": "Budapest",
                    "test-app-module:intdata": 123
                },
                "relationshipIds": [
                    "urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_ANTENNAMODULE=44F4F4FC906E9A7525065E4565246F7469CBD11FC7752C61EA6D74776845900AFF472DCAACA1F66443490B6CE0DD9AC9A5E1467022118599F6B4C6EC63400512",
                    "urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_ANTENNAMODULE=CEEC51BE136D671D2101C09FEDD8A1D95E1E177A4818E9FC0D6E63E610BC8FE26FC9C729A1E58AD43D70472F4CD54403E25CB1E5D2BBA66966625C21435C4A78"
                ],
                "operation": "merge"
            }''')
        }
        response {
            status NO_CONTENT()
        }
    },
    Contract.make {
        description "SUCCESS - 204: Merge valid decorators to entities and relationships (add)."
        request {
            method POST()
            url "/topology-inventory/v1/manage-decorators"
            headers {
                contentType("application/json")
                accept('application/problem+json')
            }
            body('''{
                "decorators": {
                    "test-app-module:textdata": "Budapest",
                    "test-app-module:intdata": 123
                },
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
    },
    Contract.make {
        description "SUCCESS - 204: Merge valid decorators to entities and relationships (update)."
        request {
            method POST()
            url "/topology-inventory/v1/manage-decorators"
            headers {
                contentType("application/json")
                accept('application/problem+json')
            }
            body('''{
                "decorators": {
                    "test-app-module:textdata": "Athlone",
                    "test-app-module:intdata": 456
                },
                "entityIds": [
                    "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10"
                ],
                "relationshipIds": [
                    "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=661A89AD3C2702233CD9E96E97E738C05C35EC5FDF32DC78D149B773726350067315B72448D004C938BCD0263F0C4BCCC8A5F9CDD145B9B740983D1523664328"
                ],
                "operation": "merge"
            }''')
        }
        response {
            status NO_CONTENT()
        }
    }
]
