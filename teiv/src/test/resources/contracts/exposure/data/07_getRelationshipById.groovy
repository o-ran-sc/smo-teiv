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
package contracts.data

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description "SUCCESS - 200: Get relationship with specified id"
        request {
            method GET()
            url "/topology-inventory/v1/domains/REL_EQUIPMENT_RAN/relationship-types/SECTOR_GROUPS_ANTENNAMODULE/relationships/urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_ANTENNAMODULE=44F4F4FC906E9A7525065E4565246F7469CBD11FC7752C61EA6D74776845900AFF472DCAACA1F66443490B6CE0DD9AC9A5E1467022118599F6B4C6EC63400512"
        }
        response {
            status OK()
            headers {
                contentType('application/yang.data+json')
            }
            body('''{
                "o-ran-smo-teiv-rel-equipment-ran:SECTOR_GROUPS_ANTENNAMODULE": [
                    {
                        "bSide": "urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A",
                        "aSide": "urn:Sector=2",
                        "id": "urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_ANTENNAMODULE=44F4F4FC906E9A7525065E4565246F7469CBD11FC7752C61EA6D74776845900AFF472DCAACA1F66443490B6CE0DD9AC9A5E1467022118599F6B4C6EC63400512",
                        "sourceIds": []
                    }
                ]
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get relationship with specified id"
        request {
            method GET()
            url "/topology-inventory/v1/domains/REL_OAM_RAN/relationship-types/ODUFUNCTION_PROVIDES_NRCELLDU/relationships/urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=4E40BE000AFEA418CE1B9ED9E21D38DA51772175BD498BE825D9EA362F9B7393C36AB72F6FDEE702439143D578268A2E84719A9352C8EA70F847B7B7664E047C"
        }
        response {
            status OK()
            headers {
                contentType('application/yang.data+json')
            }
            body('''{
                "o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU": [
                    {
                        "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1",
                        "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9",
                        "id": "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=4E40BE000AFEA418CE1B9ED9E21D38DA51772175BD498BE825D9EA362F9B7393C36AB72F6FDEE702439143D578268A2E84719A9352C8EA70F847B7B7664E047C",
                        "sourceIds": []
                    }
                ]
            }''')
        }
    },
    Contract.make {
        description "ERROR - 404: Get relationship with non existing id 'urn:non-existing-id'"
        request {
            method GET()
            url "/topology-inventory/v1/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships/urn:non-existing-id"
        }
        response {
            status NOT_FOUND()
            headers {
                contentType('application/problem+json')
            }
            body('''{
                "status": "NOT_FOUND",
                "message": "Resource Not Found",
                "details": "The requested resource is not found. ID: urn:non-existing-id"
            }''')
        }
    },
    Contract.make {
        description "SUCCESS - 200: Get relationship with metadata for specified id "
        request {
            method GET()
            url "/topology-inventory/v1/domains/REL_OAM_RAN/relationship-types/MANAGEDELEMENT_MANAGES_ODUFUNCTION/relationships/urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=6BD25E5C8FB7842F69010736253CC47F43535D7238E9E9A03E8092E8C019C83270DE47C96EF1049C40B83A130F9F129AE93B9C8538B6B004AE89BD0A098E48DD"
        }
        response {
            status OK()
            headers {
                contentType('application/yang.data+json')
            }
            body('''{
                "o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION": [
                    {
                        "metadata": {
                            "lastModified":"2025-01-13T16:40:36.461565Z",
                            "firstDiscovered":"2025-01-13T16:40:36.461565Z",
                            "reliabilityIndicator": "OK"
                        },
                        "bSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19",
                        "aSide": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19",
                        "id": "urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=6BD25E5C8FB7842F69010736253CC47F43535D7238E9E9A03E8092E8C019C83270DE47C96EF1049C40B83A130F9F129AE93B9C8538B6B004AE89BD0A098E48DD",
                        "sourceIds": []
                    }
                ]
            }''')
            bodyMatchers {
                jsonPath('o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].id', byEquality())
                jsonPath('o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].aSide', byEquality())
                jsonPath('o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].bSide', byEquality())
                jsonPath('o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].metadata.lastModified', byEquality())
                jsonPath('o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].metadata.firstDiscovered', byEquality())
                jsonPath('o-ran-smo-teiv-rel-oam-ran:MANAGEDELEMENT_MANAGES_ODUFUNCTION[0].metadata.reliabilityIndicator', byEquality())
            }
        }
    }
]
