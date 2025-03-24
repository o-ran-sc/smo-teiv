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

DROP SCHEMA IF EXISTS teiv_model cascade;
CREATE SCHEMA IF NOT EXISTS teiv_model;
ALTER SCHEMA teiv_model OWNER TO :pguser;
SET default_tablespace = '';
SET default_table_access_method = heap;

SET ROLE :'pguser';

CREATE TABLE IF NOT EXISTS teiv_model.execution_status (
    "schema"                 VARCHAR(127) PRIMARY KEY,
    "status"          VARCHAR(127)
);

CREATE TABLE IF NOT EXISTS teiv_model.hash_info (
    "name"                 VARCHAR(511) PRIMARY KEY,
    "hashedValue"          VARCHAR(511),
    "type"                 VARCHAR(511)
);

CREATE TABLE IF NOT EXISTS teiv_model.module_reference (
    "name"                   VARCHAR(511) PRIMARY KEY,
    "namespace"              VARCHAR(511),
    "domain"             VARCHAR(511),
    "includedModules"        jsonb,
    "revision"       VARCHAR(511),
    "content"               TEXT
);

CREATE TABLE IF NOT EXISTS teiv_model.entity_info (
    "storedAt"            VARCHAR(511) PRIMARY KEY,
    "name"                VARCHAR(511) NOT NULL,
    "moduleReferenceName" VARCHAR(511) NOT NULL,
    "attributeNames"      jsonb DEFAULT '[]'::jsonb,
    FOREIGN KEY ("moduleReferenceName") REFERENCES teiv_model.module_reference ("name") ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS teiv_model.relationship_info (
    "name"                     VARCHAR(511) NOT NULL,
    "aSideAssociationName"     TEXT NOT NULL,
    "aSideMOType"              TEXT NOT NULL,
    "aSideModule"              TEXT NOT NULL,
    "aSideMinCardinality"      BIGINT NOT NULL,
    "aSideMaxCardinality"      BIGINT NOT NULL,
    "bSideAssociationName"     TEXT NOT NULL,
    "bSideMOType"              TEXT NOT NULL,
    "bSideModule"              TEXT NOT NULL,
    "bSideMinCardinality"      BIGINT NOT NULL,
    "bSideMaxCardinality"      BIGINT NOT NULL,
    "associationKind"          TEXT NOT NULL,
    "relationshipDataLocation" TEXT NOT NULL,
    "connectSameEntity"        BOOLEAN NOT NULL,
    "storedAt"                 VARCHAR(511) NOT NULL,
    "moduleReferenceName"      TEXT NOT NULL,
    PRIMARY KEY ("name", "moduleReferenceName"),
    FOREIGN KEY ("moduleReferenceName") REFERENCES teiv_model.module_reference ("name") ON DELETE CASCADE
);

-- Update model schema exec status
INSERT INTO teiv_model.execution_status("schema", "status") VALUES ('teiv_model', 'success');

COPY teiv_model.hash_info("name", "hashedValue", "type") FROM stdin;
\.

COPY teiv_model.module_reference("name", "namespace", "domain", "includedModules", "revision", "content") FROM stdin;
\.

COPY teiv_model.entity_info("storedAt", "name", "moduleReferenceName", "attributeNames") FROM stdin;
\.

COPY teiv_model.relationship_info("name", "aSideAssociationName", "aSideMOType", "aSideModule", "aSideMinCardinality", "aSideMaxCardinality", "bSideAssociationName", "bSideMOType", "bSideModule", "bSideMinCardinality", "bSideMaxCardinality", "associationKind", "connectSameEntity", "relationshipDataLocation", "storedAt", "moduleReferenceName") FROM stdin;
ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER	provided-lteSectorCarrier	ENodeBFunction	o-ran-smo-teiv-ran	1	1	provided-by-enodebFunction	LTESectorCarrier	o-ran-smo-teiv-ran	0	100	BI_DIRECTIONAL	false	B_SIDE	o-ran-smo-teiv-ran:LTESectorCarrier	o-ran-smo-teiv-ran
LTESECTORCARRIER_USES_ANTENNACAPABILITY	used-antennaCapability	LTESectorCarrier	o-ran-smo-teiv-ran	0	9223372036854775807	used-by-lteSectorCarrier	AntennaCapability	o-ran-smo-teiv-ran	0	1	BI_DIRECTIONAL	false	A_SIDE	o-ran-smo-teiv-ran:LTESectorCarrier	o-ran-smo-teiv-ran
\.

;

COMMIT;