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

DROP SCHEMA IF EXISTS ties_model cascade;
CREATE SCHEMA IF NOT EXISTS ties_model;
ALTER SCHEMA ties_model OWNER TO :pguser;
SET default_tablespace = '';
SET default_table_access_method = heap;

SET ROLE :'pguser';

CREATE TABLE IF NOT EXISTS ties_model.execution_status (
    "schema" VARCHAR(127) PRIMARY KEY,
    "status" VARCHAR(127)
);

CREATE TABLE IF NOT EXISTS ties_model.hash_info (
    "name"        TEXT PRIMARY KEY,
    "hashedValue" VARCHAR(63) NOT NULL,
    "type"        VARCHAR(511)
);

CREATE TABLE IF NOT EXISTS ties_model.module_reference (
    "name"            TEXT PRIMARY KEY,
    "namespace"       TEXT,
    "domain"          TEXT,
    "includedModules" jsonb DEFAULT '[]'::jsonb,
    "revision"        TEXT NOT NULL,
    "content"         TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS ties_model.entity_info (
    "storedAt"            TEXT PRIMARY KEY,
    "name"                TEXT NOT NULL,
    "moduleReferenceName" TEXT NOT NULL,
    FOREIGN KEY ("moduleReferenceName") REFERENCES ties_model.module_reference ("name") ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ties_model.relationship_info (
    "name"                     TEXT NOT NULL,
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
    "storedAt"                 TEXT NOT NULL,
    "connectSameEntity"        BOOLEAN NOT NULL,
    "moduleReferenceName"      TEXT NOT NULL,
    PRIMARY KEY ("name", "moduleReferenceName"),
    FOREIGN KEY ("aSideModule") REFERENCES ties_model.module_reference ("name") ON DELETE CASCADE,
    FOREIGN KEY ("bSideModule") REFERENCES ties_model.module_reference ("name") ON DELETE CASCADE,
    FOREIGN KEY ("moduleReferenceName") REFERENCES ties_model.module_reference ("name") ON DELETE CASCADE
);

-- Update model schema exec status
INSERT INTO ties_model.execution_status("schema", "status") VALUES ('ties_model', 'success');

