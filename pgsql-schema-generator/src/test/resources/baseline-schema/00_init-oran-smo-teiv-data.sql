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

CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS postgis_topology;
CREATE EXTENSION IF NOT EXISTS pg_trgm;

GRANT USAGE ON SCHEMA topology to :pguser;
GRANT SELECT ON ALL SEQUENCES IN SCHEMA topology TO :pguser;
GRANT SELECT ON ALL TABLES IN SCHEMA topology TO :pguser;

CREATE SCHEMA IF NOT EXISTS ties_data;
ALTER SCHEMA ties_data OWNER TO :pguser;
SET default_tablespace = '';
SET default_table_access_method = heap;

SET ROLE :'pguser';

-- Function to create CONSTRAINT only if it does not exists
CREATE OR REPLACE FUNCTION ties_data.create_constraint_if_not_exists (
	t_name TEXT, c_name TEXT, constraint_sql TEXT
)
RETURNS void AS
$$
BEGIN
	IF NOT EXISTS (SELECT constraint_name FROM information_schema.table_constraints WHERE table_name = t_name AND constraint_name = c_name) THEN
		EXECUTE constraint_sql;
	END IF;
END;
$$ language 'plpgsql';

-- Update data schema exec status
INSERT INTO ties_model.execution_status("schema", "status") VALUES ('ties_data', 'success');

--missing consumer data columns, their default values and index
--missing index for antennaBeamWidth
CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-equipment_AntennaModule" (
	"id"			TEXT,
	"positionWithinSector"			TEXT,
	"electricalAntennaTilt"			INTEGER,
	"mechanicalAntennaBearing"			INTEGER,
	"antennaBeamWidth"			jsonb,
	"mechanicalAntennaTilt"			INTEGER,
	"antennaModelNumber"			TEXT,
	"totalTilt"			INTEGER,
	"geo-location"	geography
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'PK_o-ran-smo-teiv-equipment_AntennaModule_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "PK_o-ran-smo-teiv-equipment_AntennaModule_id" PRIMARY KEY ("id");'
);

--missing eNodeBPlmnId, classifiers and decorator columns
--missing default value for sourceIds column
CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-ran_ENodeBFunction" (
	"id"			TEXT,
	"eNBId"			INTEGER,
	"CD_sourceIds"			jsonb
);

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_ENodeBFunction" ALTER COLUMN "eNBId" SET DEFAULT '11';

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_ENodeBFunction',
 'PK_o-ran-smo-teiv-ran_ENodeBFunction_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_ENodeBFunction" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_ENodeBFunction_id" PRIMARY KEY ("id");'
);

CREATE INDEX IF NOT EXISTS "IDX_3F7D14B4CF2CA74F28BA1600606E82C6E8C447C0" ON ties_data."o-ran-smo-teiv-ran_ENodeBFunction" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

--missing index on JSONB columns
CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-ran_AntennaCapability" (
	"id"			TEXT,
	"geranFqBands"			jsonb,
	"nRFqBands"			jsonb,
	"eUtranFqBands"			jsonb,
	"CD_sourceIds"			jsonb,
	"CD_decorators"			jsonb
	"CD_classifiers"			jsonb
);

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_AntennaCapability" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_AntennaCapability" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_AntennaCapability" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_AntennaCapability',
 'PK_o-ran-smo-teiv-ran_AntennaCapability_id',
 'ALTER TABLE ties_data."AntennaCapability" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_AntennaCapability_id" PRIMARY KEY ("id");'
);

--missing "ANTENNACAPABILITY_USED_BY_LTESECTORCARRIER" relationship
CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" (
	"id"			TEXT,
	"sectorCarrierType"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"    jsonb,
	"CD_decorators"     jsonb,
	"REL_FK_provided-by-enodebFunction"			TEXT,
	"REL_ID_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER"			TEXT,
	"REL_CD_sourceIds_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER"			jsonb,
	"REL_CD_classifiers_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER"			jsonb,
	"REL_CD_decorators_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER"			jsonb,
);

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "REL_CD_sourceIds_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "REL_CD_classifiers_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "REL_CD_decorators_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER" SET DEFAULT '{}';

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_LTESectorCarrier',
 'PK_o-ran-smo-teiv-ran_LTESectorCarrier_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_LTESectorCarrier_id" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_LTESectorCarrier',
 'FK_D0868FBC0BBE2754F4B765C4898C1A1700E2BEFD',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD CONSTRAINT "FK_D0868FBC0BBE2754F4B765C4898C1A1700E2BEFD" FOREIGN KEY ("REL_FK_provided-by-enodebFunction") REFERENCES ties_data."o-ran-smo-teiv-ran_ENodeBFunction" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_LTESectorCarrier',
 'UNIQUE_FD943EE596337B11E0C640E1176CADF9CD69E19A',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD CONSTRAINT "UNIQUE_FD943EE596337B11E0C640E1176CADF9CD69E19A" UNIQUE ("REL_ID_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER");'
);

CREATE INDEX IF NOT EXISTS "IDX_6EC539C61EA7078DBA264C9877B87FC263605D42" ON ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_E754EB8AD825DB3111B07B9E5DA3B30C38DB406B" ON ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_LTESectorCarrier_CD_decorators" ON ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_1EBC7271CEA658156DE25286404CBC4593340F8E" ON ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN (("REL_CD_sourceIds_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_846B7740E8AA756B8C1409CD909D2DF73A47ED4C" ON ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN (("REL_CD_classifiers_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_44075E1D464599B61924196C20F2B88332520CD8" ON ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN ("REL_CD_decorators_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER");

COMMIT;
