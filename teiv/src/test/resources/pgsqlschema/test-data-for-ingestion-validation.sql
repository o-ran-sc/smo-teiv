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

CREATE TABLE IF NOT EXISTS teiv_data."o-ran-smo-teiv-ran_TESTENTITYA_GROUPS_TESTENTITYB" (
	"id"			VARCHAR(511),
	"aSide_TestEntityA"			VARCHAR(511),
	"bSide_TestEntityB"			VARCHAR(511),
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"metadata"          jsonb
);

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_TESTENTITYA_GROUPS_TESTENTITYB" ADD COLUMN IF NOT EXISTS "metadata" jsonb;
ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_TESTENTITYA_GROUPS_TESTENTITYB" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';
ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_TESTENTITYA_GROUPS_TESTENTITYB" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';
ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_TESTENTITYA_GROUPS_TESTENTITYB" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."o-ran-smo-teiv-ran_TESTENTITYA_PROVIDES_TESTENTITYB" (
	"id"			VARCHAR(511),
	"aSide_TestEntityA"			VARCHAR(511),
	"bSide_TestEntityB"			VARCHAR(511),
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"metadata"          jsonb
);

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_TESTENTITYA_PROVIDES_TESTENTITYB" ADD COLUMN IF NOT EXISTS "metadata" jsonb;
ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_TESTENTITYA_PROVIDES_TESTENTITYB" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';
ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_TESTENTITYA_PROVIDES_TESTENTITYB" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';
ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_TESTENTITYA_PROVIDES_TESTENTITYB" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."o-ran-smo-teiv-ran_TestEntityA" (
	"id"			VARCHAR(511),
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"attribute1"			TEXT
);

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_TestEntityA" ADD COLUMN IF NOT EXISTS "metadata" jsonb;
ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_TestEntityA" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';
ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_TestEntityA" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';
ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_TestEntityA" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."o-ran-smo-teiv-ran_TestEntityB" (
	"id"			VARCHAR(511),
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"attribute1"			TEXT,
	"REL_FK_used-by-testEntityA"			VARCHAR(511),
	"REL_ID_TESTENTITYA_USES_TESTENTITYB"			VARCHAR(511),
	"REL_CD_sourceIds_TESTENTITYA_USES_TESTENTITYB"			jsonb,
	"REL_CD_classifiers_TESTENTITYA_USES_TESTENTITYB"			jsonb,
	"REL_CD_decorators_TESTENTITYA_USES_TESTENTITYB"			jsonb,
	"REL_metadata_TESTENTITYA_USES_TESTENTITYB"         jsonb
);

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_TestEntityB" ADD COLUMN IF NOT EXISTS "metadata" jsonb;
ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_TestEntityB" ADD COLUMN IF NOT EXISTS "REL_metadata_TESTENTITYA_USES_TESTENTITYB" jsonb;
ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_TestEntityB" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';
ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_TestEntityB" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';
ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_TestEntityB" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';
ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_TestEntityB" ALTER COLUMN "REL_CD_sourceIds_TESTENTITYA_USES_TESTENTITYB" SET DEFAULT '[]';
ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_TestEntityB" ALTER COLUMN "REL_CD_classifiers_TESTENTITYA_USES_TESTENTITYB" SET DEFAULT '[]';
ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_TestEntityB" ALTER COLUMN "REL_CD_decorators_TESTENTITYA_USES_TESTENTITYB" SET DEFAULT '{}';

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_TESTENTITYA_GROUPS_TESTENTITYB',
 'PK_TESTENTITYA_GROUPS_TESTENTITYB_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_TESTENTITYA_GROUPS_TESTENTITYB" ADD CONSTRAINT "PK_TESTENTITYA_GROUPS_TESTENTITYB_id" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_TESTENTITYA_PROVIDES_TESTENTITYB',
 'PK_TESTENTITYA_PROVIDES_TESTENTITYB_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_TESTENTITYA_PROVIDES_TESTENTITYB" ADD CONSTRAINT "PK_TESTENTITYA_PROVIDES_TESTENTITYB_id" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_TestEntityA',
 'PK_TestEntityA_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_TestEntityA" ADD CONSTRAINT "PK_TestEntityA_id" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_TestEntityB',
 'PK_TestEntityB_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_TestEntityB" ADD CONSTRAINT "PK_TestEntityB_id" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_TESTENTITYA_GROUPS_TESTENTITYB',
 'FK_TESTENTITYA_GROUPS_TESTENTITYB_aSide_TestEntityA',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_TESTENTITYA_GROUPS_TESTENTITYB" ADD CONSTRAINT "FK_TESTENTITYA_GROUPS_TESTENTITYB_aSide_TestEntityA" FOREIGN KEY ("aSide_TestEntityA") REFERENCES teiv_data."o-ran-smo-teiv-ran_TestEntityA" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_TESTENTITYA_GROUPS_TESTENTITYB',
 'FK_TESTENTITYA_GROUPS_TESTENTITYB_bSide_TestEntityB',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_TESTENTITYA_GROUPS_TESTENTITYB" ADD CONSTRAINT "FK_TESTENTITYA_GROUPS_TESTENTITYB_bSide_TestEntityB" FOREIGN KEY ("bSide_TestEntityB") REFERENCES teiv_data."o-ran-smo-teiv-ran_TestEntityB" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_TESTENTITYA_PROVIDES_TESTENTITYB',
 'FK_TESTENTITYA_PROVIDES_TESTENTITYB_aSide_TestEntityA',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_TESTENTITYA_PROVIDES_TESTENTITYB" ADD CONSTRAINT "FK_TESTENTITYA_PROVIDES_TESTENTITYB_aSide_TestEntityA" FOREIGN KEY ("aSide_TestEntityA") REFERENCES teiv_data."o-ran-smo-teiv-ran_TestEntityA" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_TESTENTITYA_PROVIDES_TESTENTITYB',
 'FK_TESTENTITYA_PROVIDES_TESTENTITYB_bSide_TestEntityB',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_TESTENTITYA_PROVIDES_TESTENTITYB" ADD CONSTRAINT "FK_TESTENTITYA_PROVIDES_TESTENTITYB_bSide_TestEntityB" FOREIGN KEY ("bSide_TestEntityB") REFERENCES teiv_data."o-ran-smo-teiv-ran_TestEntityB" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_TestEntityB',
 'FK_TestEntityB_REL_FK_used-by-testEntityA',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_TestEntityB" ADD CONSTRAINT "FK_TestEntityB_REL_FK_used-by-testEntityA" FOREIGN KEY ("REL_FK_used-by-testEntityA") REFERENCES teiv_data."o-ran-smo-teiv-ran_TestEntityA" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_TestEntityB',
 'UNIQUE_TestEntityB_REL_ID_TESTENTITYA_USES_TESTENTITYB',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_TestEntityB" ADD CONSTRAINT "UNIQUE_TestEntityB_REL_ID_TESTENTITYA_USES_TESTENTITYB" UNIQUE ("REL_ID_TESTENTITYA_USES_TESTENTITYB");'
);

COMMIT;