--
-- ============LICENSE_START=======================================================
-- Copyright (C) 2024 Ericsson
-- Modifications Copyright (C) 2024 OpenInfra Foundation Europe
-- ================================================================================
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--       http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--
-- SPDX-License-Identifier: Apache-2.0
-- ============LICENSE_END=========================================================
--

BEGIN;

COPY ties_groups."groups" ("id", "name", "type") FROM stdin;
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440050	test-dynamic-group-for-rename	dynamic
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440051	test-static-group-for-rename	static
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440000	test-dynamic-group-for-delete	dynamic
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655441000	test-static-group-for-delete	static
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440001	test-group-get-entities-by-type	dynamic
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440002	test-group-get-rels-for-entity-id	dynamic
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440003	test-group-get-rels-by-type	dynamic
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440004	test-group-get-rels-for-non-existing-entity-id	dynamic
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440005	test-group-get-entities-by-type-with-invalid-scope	dynamic
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440100	test-group-get-entities-by-type-invalid-json	dynamic
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201	test-group-get-by-id-static-group	static
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440202	test-group-get-by-name-static-group	static
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440203	test-group-get-by-name-static-group	static
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440391	test-group-empty-provided-members	static
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440371	test-group-invalid-topology-type	static
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440361	test-group	static
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440351	test-group	static
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440341	test-group-get-static-members	static
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211	test-group-merge-remove-provided-members-in-static-group	static
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440212	test-group-no-data-for-merge-in-static-group	static
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440213	test-group-no-data-for-remove-in-static-group	static
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440214	test-group-with-max-provided-members-in-static-group	static
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440215	test-group-merge-all-provided-members-in-static-group	static
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440216	test-group-almost-max-provided-members-in-static-group	static
\.

COPY ties_groups."dynamic_groups" ("id", "criteria") FROM stdin;
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440000	{"queryType": "getEntitiesByDomain", "domain": "OAM"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440050	{"queryType": "getEntitiesByDomain", "domain": "RAN"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440001	{"queryType": "getEntitiesByType", "domain": "RAN", "entityTypeName": "ODUFunction", "targetFilter": "/sourceIds"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440002	{"queryType": "getRelationshipsForEntityId", "domain": "TEIV", "entityTypeName": "ODUFunction", "entityId": "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440003	{"queryType": "getRelationshipsByType", "domain": "REL_OAM_RAN", "relationshipTypeName": "MANAGEDELEMENT_MANAGES_ODUFUNCTION"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440004	{"queryType": "getRelationshipsForEntityId", "domain": "RAN", "entityTypeName": "ODUFunction", "entityId": "urn:3gpp:dn:non-existing-odufunction"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440005	{"queryType": "getEntitiesByType", "domain": "RAN", "entityTypeName": "ODUFunction", "scopeFilter": "/attributes"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440100	{"criteria": {"queryType": "getEntitiesByType", "domain": "RAN", "entityTypeName": "ODUFunction", "targetFilter": "/sourceIds"}}
\.

COPY ties_groups."static_groups" ("id", "topology_type", "provided_members_ids") FROM stdin;
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201	o-ran-smo-teiv-ran:NRCellDU	{"urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=91", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=92", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=500"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201	o-ran-smo-teiv-ran:ODUFunction	{"urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201	o-ran-smo-teiv-oam:ManagedElement	{"urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=500"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201	o-ran-smo-teiv-ran:ODUFUNCTION_PROVIDES_NRCELLDU	{"urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=4E40BE000AFEA418CE1B9ED9E21D38DA51772175BD498BE825D9EA362F9B7393C36AB72F6FDEE702439143D578268A2E84719A9352C8EA70F847B7B7664E047C", "urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=78ECC09D4832328976EF0F9C19699EE05D98E3837368D386AE39AD027543494AC620086BD2A7403DACFAA7B474B3DEBD313E0906F1EDE7FA2B584E16542A706A"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201	o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_ANTENNACAPABILITY	{"urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_SERVES_ANTENNACAPABILITY=ABD52B030DF1169F9F41C898913EF30F7BB5741F53352F482310B280C90AC569B7D31D52A2BB41F1F0099AE1EDD56CACF0B285D145A5584D376DD45DED1E2D65", "urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_SERVES_ANTENNACAPABILITY=ABD52B030DF1169F9F41C898913EF30F7BB5741F53352F482310B280C90AC569B7D31D52A2BB41F1F0099AE1EDD56CACF0B285D145A5584D376DD45DEDUMMYID"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201	invalid-module:InvalidTopology	{"urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440201	invalid-module:InvalidTopology:InvalidString	{"urn:3gpp:dn:providedMemberId"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440351	invalid-module:InvalidTopology	{"urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440371	invalid-module:InvalidTopology:InvalidString	{"urn:3gpp:dn:providedMemberIds1"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440391	o-ran-smo-teiv-ran:NRCellDU	{}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440341	o-ran-smo-teiv-ran:NRCellDU	{"urn:3gpp:dn:providedMemberIds1", "urn:3gpp:dn:providedMemberIds2"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440341	o-ran-smo-teiv-rel-equipment-ran:ANTENNAMODULE_SERVES_ANTENNACAPABILITY	{"urn:3gpp:dn:providedMemberIds3", "urn:3gpp:dn:providedMemberIds4"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440211	o-ran-smo-teiv-ran:NRCellDU	{"urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=1", "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=2", "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=3", "urn:3gpp:dn:ManagedElement=1,ODUFunction=1,NRCellDU=4"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440214	o-ran-smo-teiv-ran:ODUFunction	{"urn:3gpp:dn:ManagedElement=1,ODUFunction=1", "urn:3gpp:dn:ManagedElement=1,ODUFunction=2", "urn:3gpp:dn:ManagedElement=1,ODUFunction=3", "urn:3gpp:dn:ManagedElement=1,ODUFunction=4", "urn:3gpp:dn:ManagedElement=1,ODUFunction=5", "urn:3gpp:dn:ManagedElement=1,ODUFunction=6", "urn:3gpp:dn:ManagedElement=1,ODUFunction=7", "urn:3gpp:dn:ManagedElement=1,ODUFunction=8", "urn:3gpp:dn:ManagedElement=1,ODUFunction=9", "urn:3gpp:dn:ManagedElement=1,ODUFunction=10"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440215	o-ran-smo-teiv-ran:NRSectorCarrier	{"urn:3gpp:dn:ManagedElement=1,NRSectorCarrier=1", "urn:3gpp:dn:ManagedElement=1,NRSectorCarrier=1", "urn:3gpp:dn:ManagedElement=1,NRSectorCarrier=2", "urn:3gpp:dn:ManagedElement=1,NRSectorCarrier=3", "urn:3gpp:dn:ManagedElement=1,NRSectorCarrier=3", "urn:3gpp:dn:ManagedElement=1,NRSectorCarrier=3"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440216	o-ran-smo-teiv-equipment:AntennaModule	{"urn:3gpp:dn:ManagedElement=1,AntennaModule=1", "urn:3gpp:dn:ManagedElement=1,AntennaModule=2", "urn:3gpp:dn:ManagedElement=1,AntennaModule=2", "urn:3gpp:dn:ManagedElement=1,AntennaModule=2"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440216	o-ran-smo-teiv-ran:NRSectorCarrier	{"urn:3gpp:dn:ManagedElement=1,NRSectorCarrier=1", "urn:3gpp:dn:ManagedElement=1,NRSectorCarrier=1", "urn:3gpp:dn:ManagedElement=1,NRSectorCarrier=2", "urn:3gpp:dn:ManagedElement=1,NRSectorCarrier=3"}
urn:o-ran:smo:teiv:group=550e8400-e29b-41d4-a716-446655440216	o-ran-smo-teiv-ran:NRCellCU	{"urn:3gpp:dn:ManagedElement=1,NRCellCU=1", "urn:3gpp:dn:ManagedElement=1,NRCellCU=1", "urn:3gpp:dn:ManagedElement=1,NRCellCU=2", "urn:3gpp:dn:ManagedElement=1,NRCellCU=3"}
\.

COMMIT;
