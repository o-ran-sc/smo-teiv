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

COPY teiv_model.hash_info("name", "hashedValue", "type") FROM stdin;
FK_TestEntityB_REL_FK_used-by-testEntityA	FK_TestEntityB_REL_FK_used-by-testEntityA	CONSTRAINT
UNIQUE_TestEntityB_REL_ID_TESTENTITYA_USES_TESTENTITYB	UNIQUE_TestEntityB_REL_ID_TESTENTITYA_USES_TESTENTITYB	CONSTRAINT
PK_TESTENTITYA_PROVIDES_TESTENTITYB_id	PK_TESTENTITYA_PROVIDES_TESTENTITYB_id	CONSTRAINT
FK_TESTENTITYA_PROVIDES_TESTENTITYB_bSide_TestEntityB	FK_TESTENTITYA_PROVIDES_TESTENTITYB_bSide_TestEntityB	CONSTRAINT
o-ran-smo-teiv-ran_TestEntityA	o-ran-smo-teiv-ran_TestEntityA	TABLE
o-ran-smo-teiv-ran_TestEntityB	o-ran-smo-teiv-ran_TestEntityB	TABLE
attribute1	attribute1	COLUMN
FK_TESTENTITYA_PROVIDES_TESTENTITYB_aSide_TestEntityA	FK_TESTENTITYA_PROVIDES_TESTENTITYB_aSide_TestEntityA	CONSTRAINT
REL_FK_used-by-testEntityA	REL_FK_used-by-testEntityA	COLUMN
PK_TestEntityB_id	PK_TestEntityB_id	CONSTRAINT
FK_TESTENTITYA_GROUPS_TESTENTITYB_aSide_TestEntityA	FK_TESTENTITYA_GROUPS_TESTENTITYB_aSide_TestEntityA	CONSTRAINT
aSide_TestEntityA	aSide_TestEntityA	COLUMN
REL_ID_TESTENTITYA_USES_TESTENTITYB	REL_ID_TESTENTITYA_USES_TESTENTITYB	COLUMN
o-ran-smo-teiv-ran_TESTENTITYA_GROUPS_TESTENTITYB	o-ran-smo-teiv-ran_TESTENTITYA_GROUPS_TESTENTITYB	TABLE
o-ran-smo-teiv-ran_TESTENTITYA_PROVIDES_TESTENTITYB	o-ran-smo-teiv-ran_TESTENTITYA_PROVIDES_TESTENTITYB	TABLE
bSide_TestEntityB	bSide_TestEntityB	COLUMN
FK_TESTENTITYA_GROUPS_TESTENTITYB_bSide_TestEntityB	FK_TESTENTITYA_GROUPS_TESTENTITYB_bSide_TestEntityB	CONSTRAINT
PK_TestEntityA_id	PK_TestEntityA_id	CONSTRAINT
PK_TESTENTITYA_GROUPS_TESTENTITYB_id	PK_TESTENTITYA_GROUPS_TESTENTITYB_id	CONSTRAINT
REL_CD_decorators_TESTENTITYA_USES_TESTENTITYB	REL_CD_decorators_TESTENTITYA_USES_TESTENTITYB	COLUMN
\.

COPY teiv_model.entity_info("storedAt", "name", "moduleReferenceName") FROM stdin;
o-ran-smo-teiv-ran_TestEntityB	TestEntityB	o-ran-smo-teiv-ran
o-ran-smo-teiv-ran_TestEntityA	TestEntityA	o-ran-smo-teiv-ran
\.

COPY teiv_model.relationship_info("name", "aSideAssociationName", "aSideMOType", "aSideModule", "aSideMinCardinality", "aSideMaxCardinality", "bSideAssociationName", "bSideMOType", "bSideModule", "bSideMinCardinality", "bSideMaxCardinality", "associationKind", "connectSameEntity", "relationshipDataLocation", "storedAt", "moduleReferenceName") FROM stdin;
TESTENTITYA_PROVIDES_TESTENTITYB	provided-testEntityB	TestEntityA	o-ran-smo-teiv-ran	0	2	provided-by-testEntityA	TestEntityB	o-ran-smo-teiv-ran	0	3	BI_DIRECTIONAL	false	RELATION	o-ran-smo-teiv-ran_TESTENTITYA_PROVIDES_TESTENTITYB	o-ran-smo-teiv-ran
TESTENTITYA_USES_TESTENTITYB	used-TestEntityB	TestEntityA	o-ran-smo-teiv-ran	0	1	used-by-testEntityA	TestEntityB	o-ran-smo-teiv-ran	0	2	BI_DIRECTIONAL	false	B_SIDE	o-ran-smo-teiv-ran_TestEntityB	o-ran-smo-teiv-ran
TESTENTITYA_GROUPS_TESTENTITYB	grouped-testEntityB	TestEntityA	o-ran-smo-teiv-ran	0	2	grouped-by-testEntityA	TestEntityB	o-ran-smo-teiv-ran	0	9223372036854775807	BI_DIRECTIONAL	false	RELATION	o-ran-smo-teiv-ran_TESTENTITYA_GROUPS_TESTENTITYB	o-ran-smo-teiv-ran
\.

;

COMMIT;