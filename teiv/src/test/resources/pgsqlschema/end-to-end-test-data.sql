--  ============LICENSE_START=======================================================
--  Modifications Copyright (C) 2025 OpenInfra Foundation Europe
--  ================================================================================
--  Licensed under the Apache License, Version 2.0 (the "License");
--  you may not use this file except in compliance with the License.
--  You may obtain a copy of the License at
--
--        http://www.apache.org/licenses/LICENSE-2.0
--
--  Unless required by applicable law or agreed to in writing, software
--  distributed under the License is distributed on an "AS IS" BASIS,
--  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--  See the License for the specific language governing permissions and
--  limitations under the License.
--
--  SPDX-License-Identifier: Apache-2.0
--  ============LICENSE_END=========================================================

BEGIN;

CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS postgis_topology;
CREATE EXTENSION IF NOT EXISTS pg_trgm;

GRANT USAGE ON SCHEMA topology to :pguser;
GRANT SELECT ON ALL SEQUENCES IN SCHEMA topology TO :pguser;
GRANT SELECT ON ALL TABLES IN SCHEMA topology TO :pguser;

CREATE SCHEMA IF NOT EXISTS teiv_data;
ALTER SCHEMA teiv_data OWNER TO :pguser;
SET default_tablespace = '';
SET default_table_access_method = heap;

SET ROLE :'pguser';

-- Function to create CONSTRAINT only if it does not exists
CREATE OR REPLACE FUNCTION teiv_data.create_constraint_if_not_exists (
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

CREATE TABLE IF NOT EXISTS teiv_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" (
	"id"			TEXT,
	"aSide_AntennaModule"			TEXT,
	"bSide_AntennaCapability"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY teiv_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."o-ran-smo-teiv-equipment_AntennaModule" (
	"id"			TEXT,
	"antennaBeamWidth"			jsonb,
	"antennaModelNumber"			TEXT,
	"electricalAntennaTilt"			INTEGER,
	"geo-location"			geography,
	"mechanicalAntennaBearing"			INTEGER,
	"mechanicalAntennaTilt"			INTEGER,
	"positionWithinSector"			TEXT,
	"totalTilt"			INTEGER,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_installed-at-site"			TEXT,
	"REL_ID_ANTENNAMODULE_INSTALLED_AT_SITE"			TEXT,
	"REL_CD_sourceIds_ANTENNAMODULE_INSTALLED_AT_SITE"			jsonb,
	"REL_CD_classifiers_ANTENNAMODULE_INSTALLED_AT_SITE"			jsonb,
	"REL_CD_decorators_ANTENNAMODULE_INSTALLED_AT_SITE"			jsonb,
	"REL_FK_grouped-by-sector"			TEXT,
	"REL_ID_SECTOR_GROUPS_ANTENNAMODULE"			TEXT,
	"REL_CD_sourceIds_SECTOR_GROUPS_ANTENNAMODULE"			jsonb,
	"REL_CD_classifiers_SECTOR_GROUPS_ANTENNAMODULE"			jsonb,
	"REL_CD_decorators_SECTOR_GROUPS_ANTENNAMODULE"			jsonb
);

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ALTER COLUMN "REL_CD_sourceIds_ANTENNAMODULE_INSTALLED_AT_SITE" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ALTER COLUMN "REL_CD_classifiers_ANTENNAMODULE_INSTALLED_AT_SITE" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ALTER COLUMN "REL_CD_decorators_ANTENNAMODULE_INSTALLED_AT_SITE" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ALTER COLUMN "REL_CD_sourceIds_SECTOR_GROUPS_ANTENNAMODULE" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ALTER COLUMN "REL_CD_classifiers_SECTOR_GROUPS_ANTENNAMODULE" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ALTER COLUMN "REL_CD_decorators_SECTOR_GROUPS_ANTENNAMODULE" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."o-ran-smo-teiv-equipment_Site" (
	"id"			TEXT,
	"geo-location"			geography,
	"name"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-equipment_Site" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-equipment_Site" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-equipment_Site" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."o-ran-smo-teiv-oam_ManagedElement" (
	"id"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-oam_ManagedElement" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-oam_ManagedElement" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-oam_ManagedElement" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."o-ran-smo-teiv-ran_AntennaCapability" (
	"id"			TEXT,
	"eUtranFqBands"			jsonb,
	"geranFqBands"			jsonb,
	"nRFqBands"			jsonb,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_AntennaCapability" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_AntennaCapability" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_AntennaCapability" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" (
	"id"			TEXT,
	"eNBId"			INTEGER,
	"eNodeBPlmnId"			jsonb,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_managed-by-managedElement"			TEXT,
	"REL_ID_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION"			TEXT,
	"REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION"			jsonb,
	"REL_CD_classifiers_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION"			jsonb,
	"REL_CD_decorators_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION"			jsonb
);

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" ALTER COLUMN "REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" ALTER COLUMN "REL_CD_classifiers_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" ALTER COLUMN "REL_CD_decorators_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."o-ran-smo-teiv-ran_EUtranCell" (
	"id"			TEXT,
	"cellId"			INTEGER,
	"channelBandwidth"			INTEGER,
	"dlChannelBandwidth"			INTEGER,
	"duplexType"			TEXT,
	"earfcn"			INTEGER,
	"earfcndl"			INTEGER,
	"earfcnul"			INTEGER,
	"tac"			INTEGER,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_provided-by-enodebFunction"			TEXT,
	"REL_ID_ENODEBFUNCTION_PROVIDES_EUTRANCELL"			TEXT,
	"REL_CD_sourceIds_ENODEBFUNCTION_PROVIDES_EUTRANCELL"			jsonb,
	"REL_CD_classifiers_ENODEBFUNCTION_PROVIDES_EUTRANCELL"			jsonb,
	"REL_CD_decorators_ENODEBFUNCTION_PROVIDES_EUTRANCELL"			jsonb,
	"REL_FK_grouped-by-sector"			TEXT,
	"REL_ID_SECTOR_GROUPS_EUTRANCELL"			TEXT,
	"REL_CD_sourceIds_SECTOR_GROUPS_EUTRANCELL"			jsonb,
	"REL_CD_classifiers_SECTOR_GROUPS_EUTRANCELL"			jsonb,
	"REL_CD_decorators_SECTOR_GROUPS_EUTRANCELL"			jsonb
);

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_EUtranCell" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_EUtranCell" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_EUtranCell" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_EUtranCell" ALTER COLUMN "REL_CD_sourceIds_ENODEBFUNCTION_PROVIDES_EUTRANCELL" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_EUtranCell" ALTER COLUMN "REL_CD_classifiers_ENODEBFUNCTION_PROVIDES_EUTRANCELL" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_EUtranCell" ALTER COLUMN "REL_CD_decorators_ENODEBFUNCTION_PROVIDES_EUTRANCELL" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_EUtranCell" ALTER COLUMN "REL_CD_sourceIds_SECTOR_GROUPS_EUTRANCELL" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_EUtranCell" ALTER COLUMN "REL_CD_classifiers_SECTOR_GROUPS_EUTRANCELL" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_EUtranCell" ALTER COLUMN "REL_CD_decorators_SECTOR_GROUPS_EUTRANCELL" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" (
	"id"			TEXT,
	"gNBCUName"			TEXT,
	"gNBId"			BIGINT,
	"gNBIdLength"			INTEGER,
	"pLMNId"			jsonb,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_managed-by-managedElement"			TEXT,
	"REL_ID_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION"			TEXT,
	"REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION"			jsonb,
	"REL_CD_classifiers_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION"			jsonb,
	"REL_CD_decorators_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION"			jsonb
);

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ALTER COLUMN "REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ALTER COLUMN "REL_CD_classifiers_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ALTER COLUMN "REL_CD_decorators_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" (
	"id"			TEXT,
	"gNBId"			BIGINT,
	"gNBIdLength"			INTEGER,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_managed-by-managedElement"			TEXT,
	"REL_ID_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION"			TEXT,
	"REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION"			jsonb,
	"REL_CD_classifiers_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION"			jsonb,
	"REL_CD_decorators_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION"			jsonb
);

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" ALTER COLUMN "REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" ALTER COLUMN "REL_CD_classifiers_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" ALTER COLUMN "REL_CD_decorators_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."o-ran-smo-teiv-ran_ODUFunction" (
	"id"			TEXT,
	"dUpLMNId"			jsonb,
	"gNBDUId"			BIGINT,
	"gNBId"			BIGINT,
	"gNBIdLength"			INTEGER,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_managed-by-managedElement"			TEXT,
	"REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION"			TEXT,
	"REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ODUFUNCTION"			jsonb,
	"REL_CD_classifiers_MANAGEDELEMENT_MANAGES_ODUFUNCTION"			jsonb,
	"REL_CD_decorators_MANAGEDELEMENT_MANAGES_ODUFUNCTION"			jsonb
);

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_ODUFunction" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_ODUFunction" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_ODUFunction" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_ODUFunction" ALTER COLUMN "REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ODUFUNCTION" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_ODUFunction" ALTER COLUMN "REL_CD_classifiers_MANAGEDELEMENT_MANAGES_ODUFUNCTION" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_ODUFunction" ALTER COLUMN "REL_CD_decorators_MANAGEDELEMENT_MANAGES_ODUFUNCTION" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" (
	"id"			TEXT,
	"sectorCarrierType"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_provided-by-enodebFunction"			TEXT,
	"REL_ID_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER"			TEXT,
	"REL_CD_sourceIds_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER"			jsonb,
	"REL_CD_classifiers_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER"			jsonb,
	"REL_CD_decorators_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER"			jsonb,
	"REL_FK_used-by-euTranCell"			TEXT,
	"REL_ID_EUTRANCELL_USES_LTESECTORCARRIER"			TEXT,
	"REL_CD_sourceIds_EUTRANCELL_USES_LTESECTORCARRIER"			jsonb,
	"REL_CD_classifiers_EUTRANCELL_USES_LTESECTORCARRIER"			jsonb,
	"REL_CD_decorators_EUTRANCELL_USES_LTESECTORCARRIER"			jsonb,
	"REL_FK_used-antennaCapability"			TEXT,
	"REL_ID_LTESECTORCARRIER_USES_ANTENNACAPABILITY"			TEXT,
	"REL_CD_sourceIds_LTESECTORCARRIER_USES_ANTENNACAPABILITY"			jsonb,
	"REL_CD_classifiers_LTESECTORCARRIER_USES_ANTENNACAPABILITY"			jsonb,
	"REL_CD_decorators_LTESECTORCARRIER_USES_ANTENNACAPABILITY"			jsonb
);

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "REL_CD_sourceIds_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "REL_CD_classifiers_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "REL_CD_decorators_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "REL_CD_sourceIds_EUTRANCELL_USES_LTESECTORCARRIER" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "REL_CD_classifiers_EUTRANCELL_USES_LTESECTORCARRIER" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "REL_CD_decorators_EUTRANCELL_USES_LTESECTORCARRIER" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "REL_CD_sourceIds_LTESECTORCARRIER_USES_ANTENNACAPABILITY" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "REL_CD_classifiers_LTESECTORCARRIER_USES_ANTENNACAPABILITY" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "REL_CD_decorators_LTESECTORCARRIER_USES_ANTENNACAPABILITY" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."o-ran-smo-teiv-ran_NRCellCU" (
	"id"			TEXT,
	"cellLocalId"			INTEGER,
	"nCI"			BIGINT,
	"nRTAC"			INTEGER,
	"plmnId"			jsonb,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_provided-by-ocucpFunction"			TEXT,
	"REL_ID_OCUCPFUNCTION_PROVIDES_NRCELLCU"			TEXT,
	"REL_CD_sourceIds_OCUCPFUNCTION_PROVIDES_NRCELLCU"			jsonb,
	"REL_CD_classifiers_OCUCPFUNCTION_PROVIDES_NRCELLCU"			jsonb,
	"REL_CD_decorators_OCUCPFUNCTION_PROVIDES_NRCELLCU"			jsonb
);

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRCellCU" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRCellCU" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRCellCU" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRCellCU" ALTER COLUMN "REL_CD_sourceIds_OCUCPFUNCTION_PROVIDES_NRCELLCU" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRCellCU" ALTER COLUMN "REL_CD_classifiers_OCUCPFUNCTION_PROVIDES_NRCELLCU" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRCellCU" ALTER COLUMN "REL_CD_decorators_OCUCPFUNCTION_PROVIDES_NRCELLCU" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."o-ran-smo-teiv-ran_NRCellDU" (
	"id"			TEXT,
	"cellLocalId"			INTEGER,
	"nCI"			BIGINT,
	"nRPCI"			INTEGER,
	"nRTAC"			INTEGER,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_provided-by-oduFunction"			TEXT,
	"REL_ID_ODUFUNCTION_PROVIDES_NRCELLDU"			TEXT,
	"REL_CD_sourceIds_ODUFUNCTION_PROVIDES_NRCELLDU"			jsonb,
	"REL_CD_classifiers_ODUFUNCTION_PROVIDES_NRCELLDU"			jsonb,
	"REL_CD_decorators_ODUFUNCTION_PROVIDES_NRCELLDU"			jsonb,
	"REL_FK_grouped-by-sector"			TEXT,
	"REL_ID_SECTOR_GROUPS_NRCELLDU"			TEXT,
	"REL_CD_sourceIds_SECTOR_GROUPS_NRCELLDU"			jsonb,
	"REL_CD_classifiers_SECTOR_GROUPS_NRCELLDU"			jsonb,
	"REL_CD_decorators_SECTOR_GROUPS_NRCELLDU"			jsonb
);

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRCellDU" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRCellDU" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRCellDU" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRCellDU" ALTER COLUMN "REL_CD_sourceIds_ODUFUNCTION_PROVIDES_NRCELLDU" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRCellDU" ALTER COLUMN "REL_CD_classifiers_ODUFUNCTION_PROVIDES_NRCELLDU" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRCellDU" ALTER COLUMN "REL_CD_decorators_ODUFUNCTION_PROVIDES_NRCELLDU" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRCellDU" ALTER COLUMN "REL_CD_sourceIds_SECTOR_GROUPS_NRCELLDU" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRCellDU" ALTER COLUMN "REL_CD_classifiers_SECTOR_GROUPS_NRCELLDU" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRCellDU" ALTER COLUMN "REL_CD_decorators_SECTOR_GROUPS_NRCELLDU" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" (
	"id"			TEXT,
	"arfcnDL"			INTEGER,
	"arfcnUL"			INTEGER,
	"bSChannelBwDL"			INTEGER,
	"frequencyDL"			INTEGER,
	"frequencyUL"			INTEGER,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_provided-by-oduFunction"			TEXT,
	"REL_ID_ODUFUNCTION_PROVIDES_NRSECTORCARRIER"			TEXT,
	"REL_CD_sourceIds_ODUFUNCTION_PROVIDES_NRSECTORCARRIER"			jsonb,
	"REL_CD_classifiers_ODUFUNCTION_PROVIDES_NRSECTORCARRIER"			jsonb,
	"REL_CD_decorators_ODUFUNCTION_PROVIDES_NRSECTORCARRIER"			jsonb,
	"REL_FK_used-by-nrCellDu"			TEXT,
	"REL_ID_NRCELLDU_USES_NRSECTORCARRIER"			TEXT,
	"REL_CD_sourceIds_NRCELLDU_USES_NRSECTORCARRIER"			jsonb,
	"REL_CD_classifiers_NRCELLDU_USES_NRSECTORCARRIER"			jsonb,
	"REL_CD_decorators_NRCELLDU_USES_NRSECTORCARRIER"			jsonb,
	"REL_FK_used-antennaCapability"			TEXT,
	"REL_ID_NRSECTORCARRIER_USES_ANTENNACAPABILITY"			TEXT,
	"REL_CD_sourceIds_NRSECTORCARRIER_USES_ANTENNACAPABILITY"			jsonb,
	"REL_CD_classifiers_NRSECTORCARRIER_USES_ANTENNACAPABILITY"			jsonb,
	"REL_CD_decorators_NRSECTORCARRIER_USES_ANTENNACAPABILITY"			jsonb
);

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "REL_CD_sourceIds_ODUFUNCTION_PROVIDES_NRSECTORCARRIER" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "REL_CD_classifiers_ODUFUNCTION_PROVIDES_NRSECTORCARRIER" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "REL_CD_decorators_ODUFUNCTION_PROVIDES_NRSECTORCARRIER" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "REL_CD_sourceIds_NRCELLDU_USES_NRSECTORCARRIER" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "REL_CD_classifiers_NRCELLDU_USES_NRSECTORCARRIER" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "REL_CD_decorators_NRCELLDU_USES_NRSECTORCARRIER" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "REL_CD_sourceIds_NRSECTORCARRIER_USES_ANTENNACAPABILITY" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "REL_CD_classifiers_NRSECTORCARRIER_USES_ANTENNACAPABILITY" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "REL_CD_decorators_NRSECTORCARRIER_USES_ANTENNACAPABILITY" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."o-ran-smo-teiv-ran_Sector" (
	"id"			TEXT,
	"azimuth"			DECIMAL,
	"geo-location"			geography,
	"sectorId"			BIGINT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_Sector" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_Sector" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-ran_Sector" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

SELECT teiv_data.create_constraint_if_not_exists(
	'CFC235E0404703D1E4454647DF8AAE2C193DB402',
 'PK_63E61CB6802F21FE7A04A80A095F6AF8ABF067CE',
 'ALTER TABLE teiv_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" ADD CONSTRAINT "PK_63E61CB6802F21FE7A04A80A095F6AF8ABF067CE" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'PK_o-ran-smo-teiv-equipment_AntennaModule_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "PK_o-ran-smo-teiv-equipment_AntennaModule_id" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_Site',
 'PK_o-ran-smo-teiv-equipment_Site_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_Site" ADD CONSTRAINT "PK_o-ran-smo-teiv-equipment_Site_id" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-oam_ManagedElement',
 'PK_o-ran-smo-teiv-oam_ManagedElement_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-oam_ManagedElement" ADD CONSTRAINT "PK_o-ran-smo-teiv-oam_ManagedElement_id" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_AntennaCapability',
 'PK_o-ran-smo-teiv-ran_AntennaCapability_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_AntennaCapability" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_AntennaCapability_id" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_ENodeBFunction',
 'PK_o-ran-smo-teiv-ran_ENodeBFunction_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_ENodeBFunction_id" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_EUtranCell',
 'PK_o-ran-smo-teiv-ran_EUtranCell_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_EUtranCell_id" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_OCUCPFunction',
 'PK_o-ran-smo-teiv-ran_OCUCPFunction_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_OCUCPFunction_id" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_OCUUPFunction',
 'PK_o-ran-smo-teiv-ran_OCUUPFunction_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_OCUUPFunction_id" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_ODUFunction',
 'PK_o-ran-smo-teiv-ran_ODUFunction_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_ODUFunction_id" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_LTESectorCarrier',
 'PK_o-ran-smo-teiv-ran_LTESectorCarrier_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_LTESectorCarrier_id" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellCU',
 'PK_o-ran-smo-teiv-ran_NRCellCU_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_NRCellCU_id" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellDU',
 'PK_o-ran-smo-teiv-ran_NRCellDU_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_NRCellDU_id" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'PK_o-ran-smo-teiv-ran_NRSectorCarrier_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_NRSectorCarrier_id" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_Sector',
 'PK_o-ran-smo-teiv-ran_Sector_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_Sector" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_Sector_id" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'CFC235E0404703D1E4454647DF8AAE2C193DB402',
 'FK_D80D1E6B26DF620B4DE659C600A3B7F709A41960',
 'ALTER TABLE teiv_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" ADD CONSTRAINT "FK_D80D1E6B26DF620B4DE659C600A3B7F709A41960" FOREIGN KEY ("aSide_AntennaModule") REFERENCES teiv_data."o-ran-smo-teiv-equipment_AntennaModule" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'CFC235E0404703D1E4454647DF8AAE2C193DB402',
 'FK_7148BEED02C0617DE1DEEB6639F34A9FA9251B06',
 'ALTER TABLE teiv_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" ADD CONSTRAINT "FK_7148BEED02C0617DE1DEEB6639F34A9FA9251B06" FOREIGN KEY ("bSide_AntennaCapability") REFERENCES teiv_data."o-ran-smo-teiv-ran_AntennaCapability" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'FK_E3BAEF04443354C0FC1837CF7964E05BEF9FD6CC',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "FK_E3BAEF04443354C0FC1837CF7964E05BEF9FD6CC" FOREIGN KEY ("REL_FK_installed-at-site") REFERENCES teiv_data."o-ran-smo-teiv-equipment_Site" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'UNIQUE_9DF414C2F0CD7FA8BFCB3E9BF851784AC4BC49B1',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "UNIQUE_9DF414C2F0CD7FA8BFCB3E9BF851784AC4BC49B1" UNIQUE ("REL_ID_ANTENNAMODULE_INSTALLED_AT_SITE");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'FK_078764B2F3D613D44CC6E3586F564C83164D2481',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "FK_078764B2F3D613D44CC6E3586F564C83164D2481" FOREIGN KEY ("REL_FK_grouped-by-sector") REFERENCES teiv_data."o-ran-smo-teiv-ran_Sector" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'UNIQUE_78B1D3DCD903AFFB1965D440D87B2D194CA028A0',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "UNIQUE_78B1D3DCD903AFFB1965D440D87B2D194CA028A0" UNIQUE ("REL_ID_SECTOR_GROUPS_ANTENNAMODULE");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_ENodeBFunction',
 'FK_6C99B14BF3C9BC6DE2D69AD55DF323ADCB174167',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" ADD CONSTRAINT "FK_6C99B14BF3C9BC6DE2D69AD55DF323ADCB174167" FOREIGN KEY ("REL_FK_managed-by-managedElement") REFERENCES teiv_data."o-ran-smo-teiv-oam_ManagedElement" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_ENodeBFunction',
 'UNIQUE_A30444B7D036FA579730F0D2853E52FD08DEDCF0',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" ADD CONSTRAINT "UNIQUE_A30444B7D036FA579730F0D2853E52FD08DEDCF0" UNIQUE ("REL_ID_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_EUtranCell',
 'FK_2D1FA89480BF856AB865D58FAFB6AC0B476015EB',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD CONSTRAINT "FK_2D1FA89480BF856AB865D58FAFB6AC0B476015EB" FOREIGN KEY ("REL_FK_provided-by-enodebFunction") REFERENCES teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_EUtranCell',
 'UNIQUE_CA88C7E60C1A332FA7561FC965ED41DD4125CDED',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD CONSTRAINT "UNIQUE_CA88C7E60C1A332FA7561FC965ED41DD4125CDED" UNIQUE ("REL_ID_ENODEBFUNCTION_PROVIDES_EUTRANCELL");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_EUtranCell',
 'FK_o-ran-smo-teiv-ran_EUtranCell_REL_FK_grouped-by-sector',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_EUtranCell_REL_FK_grouped-by-sector" FOREIGN KEY ("REL_FK_grouped-by-sector") REFERENCES teiv_data."o-ran-smo-teiv-ran_Sector" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_EUtranCell',
 'UNIQUE_0513FE4A675A02C31E5EDD6BCB3728911FBDA2FA',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD CONSTRAINT "UNIQUE_0513FE4A675A02C31E5EDD6BCB3728911FBDA2FA" UNIQUE ("REL_ID_SECTOR_GROUPS_EUTRANCELL");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_OCUCPFunction',
 'FK_F1FB8F88851067901B66D53EE1420D2ECCEC98A3',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ADD CONSTRAINT "FK_F1FB8F88851067901B66D53EE1420D2ECCEC98A3" FOREIGN KEY ("REL_FK_managed-by-managedElement") REFERENCES teiv_data."o-ran-smo-teiv-oam_ManagedElement" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_OCUCPFunction',
 'UNIQUE_50E9E4A87D93AC833B1D1AC05E3B58805909E20E',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ADD CONSTRAINT "UNIQUE_50E9E4A87D93AC833B1D1AC05E3B58805909E20E" UNIQUE ("REL_ID_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_OCUUPFunction',
 'FK_34D6E2537E8EE1D395CAF5BF9B2182A4696A1EAA',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" ADD CONSTRAINT "FK_34D6E2537E8EE1D395CAF5BF9B2182A4696A1EAA" FOREIGN KEY ("REL_FK_managed-by-managedElement") REFERENCES teiv_data."o-ran-smo-teiv-oam_ManagedElement" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_OCUUPFunction',
 'UNIQUE_0CA05800AC7D277BDCB5CF0097DC35978E9311F4',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" ADD CONSTRAINT "UNIQUE_0CA05800AC7D277BDCB5CF0097DC35978E9311F4" UNIQUE ("REL_ID_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_ODUFunction',
 'FK_F67FAF9D3E82B97104E2392DA0AC8A86DF2407CC',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" ADD CONSTRAINT "FK_F67FAF9D3E82B97104E2392DA0AC8A86DF2407CC" FOREIGN KEY ("REL_FK_managed-by-managedElement") REFERENCES teiv_data."o-ran-smo-teiv-oam_ManagedElement" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_ODUFunction',
 'UNIQUE_5BD09ED226520A0BE27904AEAF0557416808E7E2',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" ADD CONSTRAINT "UNIQUE_5BD09ED226520A0BE27904AEAF0557416808E7E2" UNIQUE ("REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_LTESectorCarrier',
 'FK_D0868FBC0BBE2754F4B765C4898C1A1700E2BEFD',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD CONSTRAINT "FK_D0868FBC0BBE2754F4B765C4898C1A1700E2BEFD" FOREIGN KEY ("REL_FK_provided-by-enodebFunction") REFERENCES teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_LTESectorCarrier',
 'UNIQUE_FD943EE596337B11E0C640E1176CADF9CD69E19A',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD CONSTRAINT "UNIQUE_FD943EE596337B11E0C640E1176CADF9CD69E19A" UNIQUE ("REL_ID_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_LTESectorCarrier',
 'FK_96E6D4983CFFDF30FCA20423B5913DEE486E42D0',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD CONSTRAINT "FK_96E6D4983CFFDF30FCA20423B5913DEE486E42D0" FOREIGN KEY ("REL_FK_used-by-euTranCell") REFERENCES teiv_data."o-ran-smo-teiv-ran_EUtranCell" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_LTESectorCarrier',
 'UNIQUE_0A76398FBBC8E01A2D3BA602AB47835794E997E5',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD CONSTRAINT "UNIQUE_0A76398FBBC8E01A2D3BA602AB47835794E997E5" UNIQUE ("REL_ID_EUTRANCELL_USES_LTESECTORCARRIER");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_LTESectorCarrier',
 'FK_3D8DF3FBD9C042A888CEB382688C1E8F39D85AFE',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD CONSTRAINT "FK_3D8DF3FBD9C042A888CEB382688C1E8F39D85AFE" FOREIGN KEY ("REL_FK_used-antennaCapability") REFERENCES teiv_data."o-ran-smo-teiv-ran_AntennaCapability" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_LTESectorCarrier',
 'UNIQUE_EA18F1D278EAFE834B8A80BCF8A7D8355CD013DF',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD CONSTRAINT "UNIQUE_EA18F1D278EAFE834B8A80BCF8A7D8355CD013DF" UNIQUE ("REL_ID_LTESECTORCARRIER_USES_ANTENNACAPABILITY");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellCU',
 'FK_F2CDD1E84C7F07BF8065F99A5F3488E91E3BB7B2',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" ADD CONSTRAINT "FK_F2CDD1E84C7F07BF8065F99A5F3488E91E3BB7B2" FOREIGN KEY ("REL_FK_provided-by-ocucpFunction") REFERENCES teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellCU',
 'UNIQUE_EA2A6F5BA36ABB0DA357542E05AA2D07415E127A',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" ADD CONSTRAINT "UNIQUE_EA2A6F5BA36ABB0DA357542E05AA2D07415E127A" UNIQUE ("REL_ID_OCUCPFUNCTION_PROVIDES_NRCELLCU");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellDU',
 'FK_o-ran-smo-teiv-ran_NRCellDU_REL_FK_provided-by-oduFunction',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_NRCellDU_REL_FK_provided-by-oduFunction" FOREIGN KEY ("REL_FK_provided-by-oduFunction") REFERENCES teiv_data."o-ran-smo-teiv-ran_ODUFunction" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellDU',
 'UNIQUE_C3D8E5331EC71D46D4B8CED29FE5F6CEB1D8E67A',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD CONSTRAINT "UNIQUE_C3D8E5331EC71D46D4B8CED29FE5F6CEB1D8E67A" UNIQUE ("REL_ID_ODUFUNCTION_PROVIDES_NRCELLDU");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellDU',
 'FK_o-ran-smo-teiv-ran_NRCellDU_REL_FK_grouped-by-sector',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_NRCellDU_REL_FK_grouped-by-sector" FOREIGN KEY ("REL_FK_grouped-by-sector") REFERENCES teiv_data."o-ran-smo-teiv-ran_Sector" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellDU',
 'UNIQUE_AC1C114ABED77D6DEC3F3AE3F9EBE8231924AEF4',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD CONSTRAINT "UNIQUE_AC1C114ABED77D6DEC3F3AE3F9EBE8231924AEF4" UNIQUE ("REL_ID_SECTOR_GROUPS_NRCELLDU");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'FK_F7978366174C82E41F0A6ABF29005FF01603858F',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "FK_F7978366174C82E41F0A6ABF29005FF01603858F" FOREIGN KEY ("REL_FK_provided-by-oduFunction") REFERENCES teiv_data."o-ran-smo-teiv-ran_ODUFunction" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'UNIQUE_0AC16A840F6ACDC50136E71EC6D4F3D4E04B8198',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "UNIQUE_0AC16A840F6ACDC50136E71EC6D4F3D4E04B8198" UNIQUE ("REL_ID_ODUFUNCTION_PROVIDES_NRSECTORCARRIER");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'FK_o-ran-smo-teiv-ran_NRSectorCarrier_REL_FK_used-by-nrCellDu',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_NRSectorCarrier_REL_FK_used-by-nrCellDu" FOREIGN KEY ("REL_FK_used-by-nrCellDu") REFERENCES teiv_data."o-ran-smo-teiv-ran_NRCellDU" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'UNIQUE_1AB577E5AC207ED4C99A9A96BA1C9C35544AFD25',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "UNIQUE_1AB577E5AC207ED4C99A9A96BA1C9C35544AFD25" UNIQUE ("REL_ID_NRCELLDU_USES_NRSECTORCARRIER");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'FK_65D538D54EB33081C808540235FEB28823428E64',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "FK_65D538D54EB33081C808540235FEB28823428E64" FOREIGN KEY ("REL_FK_used-antennaCapability") REFERENCES teiv_data."o-ran-smo-teiv-ran_AntennaCapability" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'UNIQUE_A799EC9DA6624651081E1DA21B5F0C2D38F6A192',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "UNIQUE_A799EC9DA6624651081E1DA21B5F0C2D38F6A192" UNIQUE ("REL_ID_NRSECTORCARRIER_USES_ANTENNACAPABILITY");'
);

CREATE INDEX IF NOT EXISTS "IDX_E896A9EB22A3F9F96CE75A271475316A98B629C8" ON teiv_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_DD0D676834B12CA2F7E8219310998376A08D7F5F" ON teiv_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_7BF09D0227840279556AD27ACECB068705893D28" ON teiv_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_21B0F1FE632B6CB185C49BA6F00224068F443215" ON teiv_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN (("antennaBeamWidth"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_905011128A2C218B5352C19ED1FE9851F43EB911" ON teiv_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_1C0CAFD80FDD6444044E3F76C7C0A7BDC35F9BC8" ON teiv_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-equipment_AntennaModule_CD_decorators" ON teiv_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_F497DEC01DA066CB09DA2AA7EDE3F4410078491B" ON teiv_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN (("REL_CD_sourceIds_ANTENNAMODULE_INSTALLED_AT_SITE"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_17E417F7EF56809674BE1D5F5154DCCE01E00A96" ON teiv_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN (("REL_CD_classifiers_ANTENNAMODULE_INSTALLED_AT_SITE"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_2321BFA482AD2700F41E2BA359F6EB00F47601B9" ON teiv_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN ("REL_CD_decorators_ANTENNAMODULE_INSTALLED_AT_SITE");

CREATE INDEX IF NOT EXISTS "IDX_5ABDB19E55A6BDEF33855F14CB1B3B8CF457912C" ON teiv_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN (("REL_CD_sourceIds_SECTOR_GROUPS_ANTENNAMODULE"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_83B6347C0C0A005D5E3D856D973D3322DFEDEA35" ON teiv_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN (("REL_CD_classifiers_SECTOR_GROUPS_ANTENNAMODULE"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_6C6FBD69F47F41970595A8775DC99CA0F5E894A1" ON teiv_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN ("REL_CD_decorators_SECTOR_GROUPS_ANTENNAMODULE");

CREATE INDEX IF NOT EXISTS "IDX_102A50584376DE25B6BBD7157594C607A5C957F2" ON teiv_data."o-ran-smo-teiv-equipment_Site" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_EEBF1BC3344E97988232825777AB13FAB6C4F3F0" ON teiv_data."o-ran-smo-teiv-equipment_Site" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-equipment_Site_CD_decorators" ON teiv_data."o-ran-smo-teiv-equipment_Site" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_DDD73D6F4004BF3A96AA118281EE3E565A922B47" ON teiv_data."o-ran-smo-teiv-oam_ManagedElement" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_98AC4232BC02323E03416954215889CEE874A1E9" ON teiv_data."o-ran-smo-teiv-oam_ManagedElement" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-oam_ManagedElement_CD_decorators" ON teiv_data."o-ran-smo-teiv-oam_ManagedElement" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_5FB80647AE3E5C0443A792618D65B9090EE2A3FC" ON teiv_data."o-ran-smo-teiv-ran_AntennaCapability" USING GIN (("eUtranFqBands"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_A94722FF7B95D8974B494793908B57B4E1A9743B" ON teiv_data."o-ran-smo-teiv-ran_AntennaCapability" USING GIN (("geranFqBands"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_441B5C05448D63552C6414BD59C13641D8A4408D" ON teiv_data."o-ran-smo-teiv-ran_AntennaCapability" USING GIN (("nRFqBands"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_CC3E208A4EE51D3B505416A599F36F3C99F466C8" ON teiv_data."o-ran-smo-teiv-ran_AntennaCapability" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_E7FFE8F4A166AA9A382A0659762FFEC313A9EB5C" ON teiv_data."o-ran-smo-teiv-ran_AntennaCapability" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_AntennaCapability_CD_decorators" ON teiv_data."o-ran-smo-teiv-ran_AntennaCapability" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_ENodeBFunction_eNodeBPlmnId" ON teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" USING GIN ("eNodeBPlmnId");

CREATE INDEX IF NOT EXISTS "IDX_3F7D14B4CF2CA74F28BA1600606E82C6E8C447C0" ON teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_B598B74193845587BA03553CEDBA058D33956847" ON teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_ENodeBFunction_CD_decorators" ON teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_252AF4814C67384A7B05EA116316E83AFF9EB6AE" ON teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" USING GIN (("REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_61CDCD3F69CF67EE740358D2C76FA796CFDA19BF" ON teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" USING GIN (("REL_CD_classifiers_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_35C17C8A9BA3EF3AEADA72C21F8090C38F575BAF" ON teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" USING GIN ("REL_CD_decorators_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION");

CREATE INDEX IF NOT EXISTS "IDX_84E36DC53519D3E334C60B5B02C1AB27130CFA24" ON teiv_data."o-ran-smo-teiv-ran_EUtranCell" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_C9C19F8F83F50C130F2EB6502ABB7B2833F1F783" ON teiv_data."o-ran-smo-teiv-ran_EUtranCell" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_EUtranCell_CD_decorators" ON teiv_data."o-ran-smo-teiv-ran_EUtranCell" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_976F6A0F8991F64592B6F9E716EFEECBD5400FDA" ON teiv_data."o-ran-smo-teiv-ran_EUtranCell" USING GIN (("REL_CD_sourceIds_ENODEBFUNCTION_PROVIDES_EUTRANCELL"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_4C77E3A51BFAB2FCD30425E4EB21CC7636438299" ON teiv_data."o-ran-smo-teiv-ran_EUtranCell" USING GIN (("REL_CD_classifiers_ENODEBFUNCTION_PROVIDES_EUTRANCELL"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_FC7D79187227E0BFA69149048CC10E39AE540B8A" ON teiv_data."o-ran-smo-teiv-ran_EUtranCell" USING GIN ("REL_CD_decorators_ENODEBFUNCTION_PROVIDES_EUTRANCELL");

CREATE INDEX IF NOT EXISTS "IDX_173887418DD4FD6FD592F6404EA784150B1822C0" ON teiv_data."o-ran-smo-teiv-ran_EUtranCell" USING GIN (("REL_CD_sourceIds_SECTOR_GROUPS_EUTRANCELL"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_8A15E61498725DA9D8C78FC4B99053C06E88DCEC" ON teiv_data."o-ran-smo-teiv-ran_EUtranCell" USING GIN (("REL_CD_classifiers_SECTOR_GROUPS_EUTRANCELL"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_E4EF3C904939ED4C0996EAB7CDFE1895CDF34BFB" ON teiv_data."o-ran-smo-teiv-ran_EUtranCell" USING GIN ("REL_CD_decorators_SECTOR_GROUPS_EUTRANCELL");

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_OCUCPFunction_pLMNId" ON teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" USING GIN ("pLMNId");

CREATE INDEX IF NOT EXISTS "IDX_BE4B476041D559760931630000D3F4A6DFF42707" ON teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_588840BAE32C7FF8CF0553F631DAAF8BB6E8E7C1" ON teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_OCUCPFunction_CD_decorators" ON teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_E01081465B87F46E1CC7A22FE406C7B41C817E8C" ON teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" USING GIN (("REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_1324070754C1EBF8EA78EF40743AFC1713733BA8" ON teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" USING GIN (("REL_CD_classifiers_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_13E734DE57346378DA4F21FC4EA030290A7E532F" ON teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" USING GIN ("REL_CD_decorators_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION");

CREATE INDEX IF NOT EXISTS "IDX_C6D2419F8DC299FBC98342AA00BE92308C7566A7" ON teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_4C2B68358221A7FF0E68012DEDD3CBA2C4ED669F" ON teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_OCUUPFunction_CD_decorators" ON teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_883506CAA3E742D82EEFCEE8C8F29927983B73B1" ON teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" USING GIN (("REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_EB07ADD66F6CF51B9330403DE4500D05CA067647" ON teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" USING GIN (("REL_CD_classifiers_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_C3141DD7D2695EF74B13981AB378A58390D203D6" ON teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" USING GIN ("REL_CD_decorators_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION");

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_ODUFunction_dUpLMNId" ON teiv_data."o-ran-smo-teiv-ran_ODUFunction" USING GIN ("dUpLMNId");

CREATE INDEX IF NOT EXISTS "IDX_2BEF269CED354C2454AC2B2EABB134AC267A0C62" ON teiv_data."o-ran-smo-teiv-ran_ODUFunction" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_601A4514FFACA8985471531013AFC8F760361F09" ON teiv_data."o-ran-smo-teiv-ran_ODUFunction" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_ODUFunction_CD_decorators" ON teiv_data."o-ran-smo-teiv-ran_ODUFunction" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_3065F7FB78C5AA9FF17972F825F89AED127A6324" ON teiv_data."o-ran-smo-teiv-ran_ODUFunction" USING GIN (("REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ODUFUNCTION"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_04C614FDE6A4AE2AA106A1233D1DF95803FC122D" ON teiv_data."o-ran-smo-teiv-ran_ODUFunction" USING GIN (("REL_CD_classifiers_MANAGEDELEMENT_MANAGES_ODUFUNCTION"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_298FCD184347DEC995B06FED2B1AE61F12BF766A" ON teiv_data."o-ran-smo-teiv-ran_ODUFunction" USING GIN ("REL_CD_decorators_MANAGEDELEMENT_MANAGES_ODUFUNCTION");

CREATE INDEX IF NOT EXISTS "IDX_6EC539C61EA7078DBA264C9877B87FC263605D42" ON teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_E754EB8AD825DB3111B07B9E5DA3B30C38DB406B" ON teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_LTESectorCarrier_CD_decorators" ON teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_1EBC7271CEA658156DE25286404CBC4593340F8E" ON teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN (("REL_CD_sourceIds_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_846B7740E8AA756B8C1409CD909D2DF73A47ED4C" ON teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN (("REL_CD_classifiers_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_44075E1D464599B61924196C20F2B88332520CD8" ON teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN ("REL_CD_decorators_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER");

CREATE INDEX IF NOT EXISTS "IDX_F2D46817C2D618D8C33945F282299BF9EB49465E" ON teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN (("REL_CD_sourceIds_EUTRANCELL_USES_LTESECTORCARRIER"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_B291D7EFCAD3BF06A2C11F8C0429ABABEEF8308B" ON teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN (("REL_CD_classifiers_EUTRANCELL_USES_LTESECTORCARRIER"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_EAE482189F45D63CD1A88B0DD5F76EEE163D9E53" ON teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN ("REL_CD_decorators_EUTRANCELL_USES_LTESECTORCARRIER");

CREATE INDEX IF NOT EXISTS "IDX_7D01A5D21C990ACCBE65035C062C7D881A05F1EE" ON teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN (("REL_CD_sourceIds_LTESECTORCARRIER_USES_ANTENNACAPABILITY"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_3D3EFECFB917DAC074F56334224B19F8FD6BF8A5" ON teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN (("REL_CD_classifiers_LTESECTORCARRIER_USES_ANTENNACAPABILITY"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_4EE2AA643311DFCC13B6ED832EBE2FAB4CFDF494" ON teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN ("REL_CD_decorators_LTESECTORCARRIER_USES_ANTENNACAPABILITY");

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_NRCellCU_plmnId" ON teiv_data."o-ran-smo-teiv-ran_NRCellCU" USING GIN ("plmnId");

CREATE INDEX IF NOT EXISTS "IDX_0C443A16285D233F16966C2F0314CDC9D0F6D0B8" ON teiv_data."o-ran-smo-teiv-ran_NRCellCU" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_E5930226819982DC0CFC1FA64FB3600647222435" ON teiv_data."o-ran-smo-teiv-ran_NRCellCU" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_NRCellCU_CD_decorators" ON teiv_data."o-ran-smo-teiv-ran_NRCellCU" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_36A671754CD510FFBDC2713FD142303DCA75DD65" ON teiv_data."o-ran-smo-teiv-ran_NRCellCU" USING GIN (("REL_CD_sourceIds_OCUCPFUNCTION_PROVIDES_NRCELLCU"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_04BE1EB39848069422B97C28EE3C8ED18BCC6D33" ON teiv_data."o-ran-smo-teiv-ran_NRCellCU" USING GIN (("REL_CD_classifiers_OCUCPFUNCTION_PROVIDES_NRCELLCU"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_229957181BBC9D7B4535807BB397E8AA1378ED85" ON teiv_data."o-ran-smo-teiv-ran_NRCellCU" USING GIN ("REL_CD_decorators_OCUCPFUNCTION_PROVIDES_NRCELLCU");

CREATE INDEX IF NOT EXISTS "IDX_FFD60DD99D80C276F402E66546F5DACB2D81EE26" ON teiv_data."o-ran-smo-teiv-ran_NRCellDU" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_C437D39632DC79BAB6AC4F0880826A05425F9C32" ON teiv_data."o-ran-smo-teiv-ran_NRCellDU" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_NRCellDU_CD_decorators" ON teiv_data."o-ran-smo-teiv-ran_NRCellDU" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_1F6708B1E34FC908473DD7A7E5641E650B359BEF" ON teiv_data."o-ran-smo-teiv-ran_NRCellDU" USING GIN (("REL_CD_sourceIds_ODUFUNCTION_PROVIDES_NRCELLDU"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_2F4C43ED084968FDAF9943DB96741885C145FE1D" ON teiv_data."o-ran-smo-teiv-ran_NRCellDU" USING GIN (("REL_CD_classifiers_ODUFUNCTION_PROVIDES_NRCELLDU"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_0E63D6B76B229961CD45D998C63175B569DDECD1" ON teiv_data."o-ran-smo-teiv-ran_NRCellDU" USING GIN ("REL_CD_decorators_ODUFUNCTION_PROVIDES_NRCELLDU");

CREATE INDEX IF NOT EXISTS "IDX_6325926B4D2FDD1FBBB34250DABEA5E7229FF9F5" ON teiv_data."o-ran-smo-teiv-ran_NRCellDU" USING GIN (("REL_CD_sourceIds_SECTOR_GROUPS_NRCELLDU"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_7CB4A7724F68D1CB2D12E8DE779BA9103F7DBE0A" ON teiv_data."o-ran-smo-teiv-ran_NRCellDU" USING GIN (("REL_CD_classifiers_SECTOR_GROUPS_NRCELLDU"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_0A03C47C13AD3B5C84D3D8081493D670E9CBDCD1" ON teiv_data."o-ran-smo-teiv-ran_NRCellDU" USING GIN ("REL_CD_decorators_SECTOR_GROUPS_NRCELLDU");

CREATE INDEX IF NOT EXISTS "IDX_8E34EC0B1DE7DDCE3B32ADD85B11E15F95C5644E" ON teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_050A80BEEF775E4D3CE216F282F23DB99DA2D798" ON teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_NRSectorCarrier_CD_decorators" ON teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_CD293AD1111E344D150340A13BD299924D29A9DA" ON teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN (("REL_CD_sourceIds_ODUFUNCTION_PROVIDES_NRSECTORCARRIER"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_273D43FDDD1C4643ECF8BBE51B6B369C657F0861" ON teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN (("REL_CD_classifiers_ODUFUNCTION_PROVIDES_NRSECTORCARRIER"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_EDE8B88F488F9380DB49CB2C141318FB33C2CCEC" ON teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN ("REL_CD_decorators_ODUFUNCTION_PROVIDES_NRSECTORCARRIER");

CREATE INDEX IF NOT EXISTS "IDX_7BFD17A71AB1B7765FE6431DA4E66C2EDE88AC3B" ON teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN (("REL_CD_sourceIds_NRCELLDU_USES_NRSECTORCARRIER"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_ED50A5139F1449DBAD8DA10D45F5A5BF819EACBA" ON teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN (("REL_CD_classifiers_NRCELLDU_USES_NRSECTORCARRIER"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_2ADB5C6DCAEE8811FB1CA8FD9EB53381F35FCB70" ON teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN ("REL_CD_decorators_NRCELLDU_USES_NRSECTORCARRIER");

CREATE INDEX IF NOT EXISTS "IDX_1F27C515A028616FAC422A02ABBEC402D5DBB2E5" ON teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN (("REL_CD_sourceIds_NRSECTORCARRIER_USES_ANTENNACAPABILITY"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_B975D24291849007D4AA6686C5D3983885D5C884" ON teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN (("REL_CD_classifiers_NRSECTORCARRIER_USES_ANTENNACAPABILITY"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_902B73F741160B9D4FBF62406D3D9ABBECAD8BE7" ON teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN ("REL_CD_decorators_NRSECTORCARRIER_USES_ANTENNACAPABILITY");

CREATE INDEX IF NOT EXISTS "IDX_E234B43A7CD7843672F08F2197AB46A2A50BECB0" ON teiv_data."o-ran-smo-teiv-ran_Sector" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_19C19556F9714850389595E0A16218FA229205FE" ON teiv_data."o-ran-smo-teiv-ran_Sector" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_Sector_CD_decorators" ON teiv_data."o-ran-smo-teiv-ran_Sector" USING GIN ("CD_decorators");

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "azimuth" DECIMAL;

CREATE TABLE IF NOT EXISTS teiv_data."1D71FC3BAE50E72552EAAC17B3B0C959B403E822" (
	"id"			TEXT,
	"aSide_AntennaModule"			TEXT,
	"bSide_EUtranCell"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY teiv_data."1D71FC3BAE50E72552EAAC17B3B0C959B403E822" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."1D71FC3BAE50E72552EAAC17B3B0C959B403E822" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."1D71FC3BAE50E72552EAAC17B3B0C959B403E822" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU" (
	"id"			TEXT,
	"aSide_AntennaModule"			TEXT,
	"bSide_NRCellDU"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

SELECT teiv_data.create_constraint_if_not_exists(
	'1D71FC3BAE50E72552EAAC17B3B0C959B403E822',
 'PK_F3CAF5F2DDEBEEBA48316C708AD6E37BA12A6632',
 'ALTER TABLE teiv_data."1D71FC3BAE50E72552EAAC17B3B0C959B403E822" ADD CONSTRAINT "PK_F3CAF5F2DDEBEEBA48316C708AD6E37BA12A6632" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU',
 'PK_F41873285F3BD831F63C6041B4356A063403406D',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU" ADD CONSTRAINT "PK_F41873285F3BD831F63C6041B4356A063403406D" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'1D71FC3BAE50E72552EAAC17B3B0C959B403E822',
 'FK_6AF9FA81BF3F1D3374C021E5554012492FA32B72',
 'ALTER TABLE teiv_data."1D71FC3BAE50E72552EAAC17B3B0C959B403E822" ADD CONSTRAINT "FK_6AF9FA81BF3F1D3374C021E5554012492FA32B72" FOREIGN KEY ("aSide_AntennaModule") REFERENCES teiv_data."o-ran-smo-teiv-equipment_AntennaModule" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'1D71FC3BAE50E72552EAAC17B3B0C959B403E822',
 'FK_D71E365C050B4B749155D43A16DCC01C12BFA055',
 'ALTER TABLE teiv_data."1D71FC3BAE50E72552EAAC17B3B0C959B403E822" ADD CONSTRAINT "FK_D71E365C050B4B749155D43A16DCC01C12BFA055" FOREIGN KEY ("bSide_EUtranCell") REFERENCES teiv_data."o-ran-smo-teiv-ran_EUtranCell" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU',
 'FK_1AB1E0CC29DA2E122D43A6616EC60A3F73E68649',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU" ADD CONSTRAINT "FK_1AB1E0CC29DA2E122D43A6616EC60A3F73E68649" FOREIGN KEY ("aSide_AntennaModule") REFERENCES teiv_data."o-ran-smo-teiv-equipment_AntennaModule" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU',
 'FK_8605800A4923C52258A8CE3989E18A7C93D22E8C',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU" ADD CONSTRAINT "FK_8605800A4923C52258A8CE3989E18A7C93D22E8C" FOREIGN KEY ("bSide_NRCellDU") REFERENCES teiv_data."o-ran-smo-teiv-ran_NRCellDU" (id) ON DELETE CASCADE;'
);

CREATE INDEX IF NOT EXISTS "IDX_570AAD5BDC2B6A7DCCB840639DE232EE08D72534" ON teiv_data."1D71FC3BAE50E72552EAAC17B3B0C959B403E822" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_6657C8783456D4F7FDD411B3B7FD1764E580AB80" ON teiv_data."1D71FC3BAE50E72552EAAC17B3B0C959B403E822" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_0B76D3F42FB84D2D1E6B28774B52150174780BE4" ON teiv_data."1D71FC3BAE50E72552EAAC17B3B0C959B403E822" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_0E1BE8724BEBB21C5AE3986BE150BEC8F8CD903E" ON teiv_data."o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_F93AD0AE5C6940EE73D0B661A2E2E5BB10B3772C" ON teiv_data."o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_319FDFF6C9E6BC1D922F0A2AFEAAC294E520F753" ON teiv_data."o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU" USING GIN ("CD_decorators");

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "horizontalBeamWidth" DECIMAL;

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "verticalBeamWidth" DECIMAL;

CREATE OR REPLACE FUNCTION teiv_data.create_enum_type(
    schema_name TEXT, type_name TEXT, enum_values TEXT[]
) RETURNS VOID AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type t JOIN pg_namespace n ON n.oid = t.typnamespace WHERE t.typname = type_name AND n.nspname = schema_name) THEN
        EXECUTE format('CREATE TYPE %I.%I AS ENUM (%s)',schema_name, type_name, array_to_string(ARRAY(SELECT quote_literal(value) FROM unnest(enum_values) AS value), ', '));
    END IF;
END;
$$ language 'plpgsql';

SELECT teiv_data.create_enum_type('teiv_data', 'Reliability', ARRAY['OK', 'RESTORED', 'ADVISED']);

CREATE TABLE IF NOT EXISTS teiv_data."responsible_adapter" (
	"id"			TEXT,
	"hashed_id"			BYTEA
);

SELECT teiv_data.create_constraint_if_not_exists(
	'responsible_adapter',
 'PK_responsible_adapter_id',
 'ALTER TABLE teiv_data."responsible_adapter" ADD CONSTRAINT "PK_responsible_adapter_id" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'responsible_adapter',
 'UNIQUE_responsible_adapter_hashed_id',
 'ALTER TABLE teiv_data."responsible_adapter" ADD CONSTRAINT "UNIQUE_responsible_adapter_hashed_id" UNIQUE ("hashed_id");'
);

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" ADD COLUMN IF NOT EXISTS "REL_metadata_MANAGEDELEMENT_MANAGES_ODUFUNCTION" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ADD COLUMN IF NOT EXISTS "REL_metadata_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_Site" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_AntennaCapability" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "REL_metadata_ANTENNAMODULE_INSTALLED_AT_SITE" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "REL_metadata_SECTOR_GROUPS_ANTENNAMODULE" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD COLUMN IF NOT EXISTS "REL_metadata_ENODEBFUNCTION_PROVIDES_EUTRANCELL" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD COLUMN IF NOT EXISTS "REL_metadata_SECTOR_GROUPS_EUTRANCELL" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD COLUMN IF NOT EXISTS "REL_metadata_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD COLUMN IF NOT EXISTS "REL_metadata_EUTRANCELL_USES_LTESECTORCARRIER" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD COLUMN IF NOT EXISTS "REL_metadata_LTESECTORCARRIER_USES_ANTENNACAPABILITY" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" ADD COLUMN IF NOT EXISTS "REL_metadata_OCUCPFUNCTION_PROVIDES_NRCELLCU" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-oam_ManagedElement" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD COLUMN IF NOT EXISTS "REL_metadata_ODUFUNCTION_PROVIDES_NRCELLDU" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD COLUMN IF NOT EXISTS "REL_metadata_SECTOR_GROUPS_NRCELLDU" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE teiv_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" ADD COLUMN IF NOT EXISTS "REL_metadata_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD COLUMN IF NOT EXISTS "REL_metadata_ODUFUNCTION_PROVIDES_NRSECTORCARRIER" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD COLUMN IF NOT EXISTS "REL_metadata_NRCELLDU_USES_NRSECTORCARRIER" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD COLUMN IF NOT EXISTS "REL_metadata_NRSECTORCARRIER_USES_ANTENNACAPABILITY" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" ADD COLUMN IF NOT EXISTS "REL_metadata_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_Sector" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE teiv_data."1D71FC3BAE50E72552EAAC17B3B0C959B403E822" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" ADD COLUMN IF NOT EXISTS "REL_RESP_id_MANAGEDELEMENT_MANAGES_ODUFUNCTION" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" ADD COLUMN IF NOT EXISTS "RESP_id" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ADD COLUMN IF NOT EXISTS "REL_RESP_id_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ADD COLUMN IF NOT EXISTS "RESP_id" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_Site" ADD COLUMN IF NOT EXISTS "RESP_id" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_AntennaCapability" ADD COLUMN IF NOT EXISTS "RESP_id" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "REL_RESP_id_ANTENNAMODULE_INSTALLED_AT_SITE" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "REL_RESP_id_SECTOR_GROUPS_ANTENNAMODULE" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RESP_id" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD COLUMN IF NOT EXISTS "REL_RESP_id_ENODEBFUNCTION_PROVIDES_EUTRANCELL" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD COLUMN IF NOT EXISTS "REL_RESP_id_SECTOR_GROUPS_EUTRANCELL" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD COLUMN IF NOT EXISTS "RESP_id" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD COLUMN IF NOT EXISTS "REL_RESP_id_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD COLUMN IF NOT EXISTS "REL_RESP_id_EUTRANCELL_USES_LTESECTORCARRIER" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD COLUMN IF NOT EXISTS "REL_RESP_id_LTESECTORCARRIER_USES_ANTENNACAPABILITY" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD COLUMN IF NOT EXISTS "RESP_id" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" ADD COLUMN IF NOT EXISTS "REL_RESP_id_OCUCPFUNCTION_PROVIDES_NRCELLCU" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" ADD COLUMN IF NOT EXISTS "RESP_id" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-oam_ManagedElement" ADD COLUMN IF NOT EXISTS "RESP_id" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD COLUMN IF NOT EXISTS "REL_RESP_id_ODUFUNCTION_PROVIDES_NRCELLDU" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD COLUMN IF NOT EXISTS "REL_RESP_id_SECTOR_GROUPS_NRCELLDU" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD COLUMN IF NOT EXISTS "RESP_id" BYTEA;

ALTER TABLE teiv_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" ADD COLUMN IF NOT EXISTS "RESP_id" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" ADD COLUMN IF NOT EXISTS "REL_RESP_id_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" ADD COLUMN IF NOT EXISTS "RESP_id" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU" ADD COLUMN IF NOT EXISTS "RESP_id" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD COLUMN IF NOT EXISTS "REL_RESP_id_ODUFUNCTION_PROVIDES_NRSECTORCARRIER" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD COLUMN IF NOT EXISTS "REL_RESP_id_NRCELLDU_USES_NRSECTORCARRIER" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD COLUMN IF NOT EXISTS "REL_RESP_id_NRSECTORCARRIER_USES_ANTENNACAPABILITY" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD COLUMN IF NOT EXISTS "RESP_id" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" ADD COLUMN IF NOT EXISTS "REL_RESP_id_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" ADD COLUMN IF NOT EXISTS "RESP_id" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_Sector" ADD COLUMN IF NOT EXISTS "RESP_id" BYTEA;

ALTER TABLE teiv_data."1D71FC3BAE50E72552EAAC17B3B0C959B403E822" ADD COLUMN IF NOT EXISTS "RESP_id" BYTEA;

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_ODUFunction',
 'FK_7B930D529570AED19AABDFA950EED794D4EB53CE',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" ADD CONSTRAINT "FK_7B930D529570AED19AABDFA950EED794D4EB53CE" FOREIGN KEY ("REL_RESP_id_MANAGEDELEMENT_MANAGES_ODUFUNCTION") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_ODUFunction',
 'FK_o-ran-smo-teiv-ran_ODUFunction_RESP_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_ODUFunction_RESP_id" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_OCUCPFunction',
 'FK_2A809BABCCE0A947D420653EF4EBD4707DA73B9F',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ADD CONSTRAINT "FK_2A809BABCCE0A947D420653EF4EBD4707DA73B9F" FOREIGN KEY ("REL_RESP_id_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_OCUCPFunction',
 'FK_o-ran-smo-teiv-ran_OCUCPFunction_RESP_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_OCUCPFunction_RESP_id" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_Site',
 'FK_o-ran-smo-teiv-equipment_Site_RESP_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_Site" ADD CONSTRAINT "FK_o-ran-smo-teiv-equipment_Site_RESP_id" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_AntennaCapability',
 'FK_o-ran-smo-teiv-ran_AntennaCapability_RESP_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_AntennaCapability" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_AntennaCapability_RESP_id" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'FK_F4EE618C98A1EB18212D9607E870E87B59690448',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "FK_F4EE618C98A1EB18212D9607E870E87B59690448" FOREIGN KEY ("REL_RESP_id_ANTENNAMODULE_INSTALLED_AT_SITE") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'FK_E2C830AB84CE31F71BBD0F91BBEFF66069A7E6B8',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "FK_E2C830AB84CE31F71BBD0F91BBEFF66069A7E6B8" FOREIGN KEY ("REL_RESP_id_SECTOR_GROUPS_ANTENNAMODULE") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'FK_o-ran-smo-teiv-equipment_AntennaModule_RESP_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "FK_o-ran-smo-teiv-equipment_AntennaModule_RESP_id" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_EUtranCell',
 'FK_A7A12166DB3EC34E10F93FDD942A5DA916CBCFA0',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD CONSTRAINT "FK_A7A12166DB3EC34E10F93FDD942A5DA916CBCFA0" FOREIGN KEY ("REL_RESP_id_ENODEBFUNCTION_PROVIDES_EUTRANCELL") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_EUtranCell',
 'FK_6F467D3CDA985248AEB010A0EDB1343A9173593E',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD CONSTRAINT "FK_6F467D3CDA985248AEB010A0EDB1343A9173593E" FOREIGN KEY ("REL_RESP_id_SECTOR_GROUPS_EUTRANCELL") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_EUtranCell',
 'FK_o-ran-smo-teiv-ran_EUtranCell_RESP_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_EUtranCell_RESP_id" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_LTESectorCarrier',
 'FK_2C1B8D1ECA6B4E645EF162522E6A5169CE7D7E9C',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD CONSTRAINT "FK_2C1B8D1ECA6B4E645EF162522E6A5169CE7D7E9C" FOREIGN KEY ("REL_RESP_id_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_LTESectorCarrier',
 'FK_B1CBA7F54C85E77DC96CD6531DB073B0EDCFF6BC',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD CONSTRAINT "FK_B1CBA7F54C85E77DC96CD6531DB073B0EDCFF6BC" FOREIGN KEY ("REL_RESP_id_EUTRANCELL_USES_LTESECTORCARRIER") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_LTESectorCarrier',
 'FK_B8F9FB1ADEBB9B35B6245684DE64775DE98442B4',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD CONSTRAINT "FK_B8F9FB1ADEBB9B35B6245684DE64775DE98442B4" FOREIGN KEY ("REL_RESP_id_LTESECTORCARRIER_USES_ANTENNACAPABILITY") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_LTESectorCarrier',
 'FK_o-ran-smo-teiv-ran_LTESectorCarrier_RESP_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_LTESectorCarrier_RESP_id" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellCU',
 'FK_323384A3617DC94F73FB225A3562EB93A05E976D',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" ADD CONSTRAINT "FK_323384A3617DC94F73FB225A3562EB93A05E976D" FOREIGN KEY ("REL_RESP_id_OCUCPFUNCTION_PROVIDES_NRCELLCU") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellCU',
 'FK_o-ran-smo-teiv-ran_NRCellCU_RESP_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_NRCellCU_RESP_id" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-oam_ManagedElement',
 'FK_o-ran-smo-teiv-oam_ManagedElement_RESP_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-oam_ManagedElement" ADD CONSTRAINT "FK_o-ran-smo-teiv-oam_ManagedElement_RESP_id" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellDU',
 'FK_3F79604542DA97B9338CF6A5610ABC05E17153B4',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD CONSTRAINT "FK_3F79604542DA97B9338CF6A5610ABC05E17153B4" FOREIGN KEY ("REL_RESP_id_ODUFUNCTION_PROVIDES_NRCELLDU") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellDU',
 'FK_01FDDC88AFC28623B49266BFA3FE0D2078988E88',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD CONSTRAINT "FK_01FDDC88AFC28623B49266BFA3FE0D2078988E88" FOREIGN KEY ("REL_RESP_id_SECTOR_GROUPS_NRCELLDU") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellDU',
 'FK_o-ran-smo-teiv-ran_NRCellDU_RESP_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_NRCellDU_RESP_id" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'CFC235E0404703D1E4454647DF8AAE2C193DB402',
 'FK_1D23F428960B9279D29AC35A946D0C60CAFE638E',
 'ALTER TABLE teiv_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" ADD CONSTRAINT "FK_1D23F428960B9279D29AC35A946D0C60CAFE638E" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_OCUUPFunction',
 'FK_2356BAEED80FD88699DCCD85993162818C9BD0A1',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" ADD CONSTRAINT "FK_2356BAEED80FD88699DCCD85993162818C9BD0A1" FOREIGN KEY ("REL_RESP_id_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_OCUUPFunction',
 'FK_o-ran-smo-teiv-ran_OCUUPFunction_RESP_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_OCUUPFunction_RESP_id" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU',
 'FK_09C96C0397884B6E28EEBE17565CE2DF7535E380',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU" ADD CONSTRAINT "FK_09C96C0397884B6E28EEBE17565CE2DF7535E380" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'FK_229725A1E5A342C3E7579273F54AD9B209CE0770',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "FK_229725A1E5A342C3E7579273F54AD9B209CE0770" FOREIGN KEY ("REL_RESP_id_ODUFUNCTION_PROVIDES_NRSECTORCARRIER") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'FK_CD383C61EDAA5A8E84DCB423E1378E3173D585E7',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "FK_CD383C61EDAA5A8E84DCB423E1378E3173D585E7" FOREIGN KEY ("REL_RESP_id_NRCELLDU_USES_NRSECTORCARRIER") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'FK_EEF72E2F5B0421B04A5129F44B6740CE35EB7D7B',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "FK_EEF72E2F5B0421B04A5129F44B6740CE35EB7D7B" FOREIGN KEY ("REL_RESP_id_NRSECTORCARRIER_USES_ANTENNACAPABILITY") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'FK_o-ran-smo-teiv-ran_NRSectorCarrier_RESP_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_NRSectorCarrier_RESP_id" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_ENodeBFunction',
 'FK_8B083C6C2AF2BA35E20AE16A4D0702373C5B5D4D',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" ADD CONSTRAINT "FK_8B083C6C2AF2BA35E20AE16A4D0702373C5B5D4D" FOREIGN KEY ("REL_RESP_id_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_ENodeBFunction',
 'FK_o-ran-smo-teiv-ran_ENodeBFunction_RESP_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_ENodeBFunction_RESP_id" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_Sector',
 'FK_o-ran-smo-teiv-ran_Sector_RESP_id',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_Sector" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_Sector_RESP_id" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'1D71FC3BAE50E72552EAAC17B3B0C959B403E822',
 'FK_3EB2B0A0A4405454550AED663948D4DD77AD3B77',
 'ALTER TABLE teiv_data."1D71FC3BAE50E72552EAAC17B3B0C959B403E822" ADD CONSTRAINT "FK_3EB2B0A0A4405454550AED663948D4DD77AD3B77" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" (hashed_id);'
);

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" ADD COLUMN IF NOT EXISTS "RESP_dUpLMNId" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" ADD COLUMN IF NOT EXISTS "RESP_gNBDUId" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" ADD COLUMN IF NOT EXISTS "RESP_gNBId" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" ADD COLUMN IF NOT EXISTS "RESP_gNBIdLength" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" ADD COLUMN IF NOT EXISTS "RI_dUpLMNId" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" ADD COLUMN IF NOT EXISTS "RI_gNBDUId" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" ADD COLUMN IF NOT EXISTS "RI_gNBId" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" ADD COLUMN IF NOT EXISTS "RI_gNBIdLength" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ADD COLUMN IF NOT EXISTS "RESP_gNBCUName" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ADD COLUMN IF NOT EXISTS "RESP_gNBId" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ADD COLUMN IF NOT EXISTS "RESP_gNBIdLength" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ADD COLUMN IF NOT EXISTS "RESP_pLMNId" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ADD COLUMN IF NOT EXISTS "RI_gNBCUName" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ADD COLUMN IF NOT EXISTS "RI_gNBId" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ADD COLUMN IF NOT EXISTS "RI_gNBIdLength" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ADD COLUMN IF NOT EXISTS "RI_pLMNId" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_Site" ADD COLUMN IF NOT EXISTS "RESP_geo-location" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_Site" ADD COLUMN IF NOT EXISTS "RESP_name" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_Site" ADD COLUMN IF NOT EXISTS "RI_geo-location" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_Site" ADD COLUMN IF NOT EXISTS "RI_name" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_AntennaCapability" ADD COLUMN IF NOT EXISTS "RESP_eUtranFqBands" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_AntennaCapability" ADD COLUMN IF NOT EXISTS "RESP_geranFqBands" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_AntennaCapability" ADD COLUMN IF NOT EXISTS "RESP_nRFqBands" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_AntennaCapability" ADD COLUMN IF NOT EXISTS "RI_eUtranFqBands" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_AntennaCapability" ADD COLUMN IF NOT EXISTS "RI_geranFqBands" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_AntennaCapability" ADD COLUMN IF NOT EXISTS "RI_nRFqBands" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RESP_antennaBeamWidth" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RESP_antennaModelNumber" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RESP_azimuth" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RESP_electricalAntennaTilt" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RESP_geo-location" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RESP_horizontalBeamWidth" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RESP_mechanicalAntennaBearing" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RESP_mechanicalAntennaTilt" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RESP_positionWithinSector" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RESP_totalTilt" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RESP_verticalBeamWidth" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RI_antennaBeamWidth" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RI_antennaModelNumber" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RI_azimuth" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RI_electricalAntennaTilt" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RI_geo-location" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RI_horizontalBeamWidth" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RI_mechanicalAntennaBearing" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RI_mechanicalAntennaTilt" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RI_positionWithinSector" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RI_totalTilt" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD COLUMN IF NOT EXISTS "RI_verticalBeamWidth" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD COLUMN IF NOT EXISTS "RESP_cellId" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD COLUMN IF NOT EXISTS "RESP_channelBandwidth" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD COLUMN IF NOT EXISTS "RESP_dlChannelBandwidth" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD COLUMN IF NOT EXISTS "RESP_duplexType" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD COLUMN IF NOT EXISTS "RESP_earfcn" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD COLUMN IF NOT EXISTS "RESP_earfcndl" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD COLUMN IF NOT EXISTS "RESP_earfcnul" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD COLUMN IF NOT EXISTS "RESP_tac" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD COLUMN IF NOT EXISTS "RI_cellId" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD COLUMN IF NOT EXISTS "RI_channelBandwidth" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD COLUMN IF NOT EXISTS "RI_dlChannelBandwidth" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD COLUMN IF NOT EXISTS "RI_duplexType" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD COLUMN IF NOT EXISTS "RI_earfcn" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD COLUMN IF NOT EXISTS "RI_earfcndl" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD COLUMN IF NOT EXISTS "RI_earfcnul" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD COLUMN IF NOT EXISTS "RI_tac" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD COLUMN IF NOT EXISTS "RESP_sectorCarrierType" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD COLUMN IF NOT EXISTS "RI_sectorCarrierType" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" ADD COLUMN IF NOT EXISTS "RESP_cellLocalId" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" ADD COLUMN IF NOT EXISTS "RESP_nCI" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" ADD COLUMN IF NOT EXISTS "RESP_nRTAC" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" ADD COLUMN IF NOT EXISTS "RESP_plmnId" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" ADD COLUMN IF NOT EXISTS "RI_cellLocalId" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" ADD COLUMN IF NOT EXISTS "RI_nCI" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" ADD COLUMN IF NOT EXISTS "RI_nRTAC" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" ADD COLUMN IF NOT EXISTS "RI_plmnId" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD COLUMN IF NOT EXISTS "RESP_cellLocalId" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD COLUMN IF NOT EXISTS "RESP_nCI" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD COLUMN IF NOT EXISTS "RESP_nRPCI" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD COLUMN IF NOT EXISTS "RESP_nRTAC" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD COLUMN IF NOT EXISTS "RI_cellLocalId" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD COLUMN IF NOT EXISTS "RI_nCI" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD COLUMN IF NOT EXISTS "RI_nRPCI" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD COLUMN IF NOT EXISTS "RI_nRTAC" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" ADD COLUMN IF NOT EXISTS "RESP_gNBId" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" ADD COLUMN IF NOT EXISTS "RESP_gNBIdLength" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" ADD COLUMN IF NOT EXISTS "RI_gNBId" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" ADD COLUMN IF NOT EXISTS "RI_gNBIdLength" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD COLUMN IF NOT EXISTS "RESP_arfcnDL" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD COLUMN IF NOT EXISTS "RESP_arfcnUL" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD COLUMN IF NOT EXISTS "RESP_bSChannelBwDL" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD COLUMN IF NOT EXISTS "RESP_frequencyDL" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD COLUMN IF NOT EXISTS "RESP_frequencyUL" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD COLUMN IF NOT EXISTS "RI_arfcnDL" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD COLUMN IF NOT EXISTS "RI_arfcnUL" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD COLUMN IF NOT EXISTS "RI_bSChannelBwDL" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD COLUMN IF NOT EXISTS "RI_frequencyDL" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD COLUMN IF NOT EXISTS "RI_frequencyUL" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" ADD COLUMN IF NOT EXISTS "RESP_eNBId" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" ADD COLUMN IF NOT EXISTS "RESP_eNodeBPlmnId" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" ADD COLUMN IF NOT EXISTS "RI_eNBId" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" ADD COLUMN IF NOT EXISTS "RI_eNodeBPlmnId" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_Sector" ADD COLUMN IF NOT EXISTS "RESP_azimuth" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_Sector" ADD COLUMN IF NOT EXISTS "RESP_geo-location" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_Sector" ADD COLUMN IF NOT EXISTS "RESP_sectorId" BYTEA;

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_Sector" ADD COLUMN IF NOT EXISTS "RI_azimuth" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_Sector" ADD COLUMN IF NOT EXISTS "RI_geo-location" teiv_data."Reliability";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_Sector" ADD COLUMN IF NOT EXISTS "RI_sectorId" teiv_data."Reliability";

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_ODUFunction',
 'FK_o-ran-smo-teiv-ran_ODUFunction_RESP_dUpLMNId',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_ODUFunction_RESP_dUpLMNId" FOREIGN KEY ("RESP_dUpLMNId") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_ODUFunction',
 'FK_o-ran-smo-teiv-ran_ODUFunction_RESP_gNBDUId',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_ODUFunction_RESP_gNBDUId" FOREIGN KEY ("RESP_gNBDUId") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_ODUFunction',
 'FK_o-ran-smo-teiv-ran_ODUFunction_RESP_gNBId',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_ODUFunction_RESP_gNBId" FOREIGN KEY ("RESP_gNBId") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_ODUFunction',
 'FK_o-ran-smo-teiv-ran_ODUFunction_RESP_gNBIdLength',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_ODUFunction_RESP_gNBIdLength" FOREIGN KEY ("RESP_gNBIdLength") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_OCUCPFunction',
 'FK_o-ran-smo-teiv-ran_OCUCPFunction_RESP_gNBCUName',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_OCUCPFunction_RESP_gNBCUName" FOREIGN KEY ("RESP_gNBCUName") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_OCUCPFunction',
 'FK_o-ran-smo-teiv-ran_OCUCPFunction_RESP_gNBId',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_OCUCPFunction_RESP_gNBId" FOREIGN KEY ("RESP_gNBId") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_OCUCPFunction',
 'FK_o-ran-smo-teiv-ran_OCUCPFunction_RESP_gNBIdLength',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_OCUCPFunction_RESP_gNBIdLength" FOREIGN KEY ("RESP_gNBIdLength") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_OCUCPFunction',
 'FK_o-ran-smo-teiv-ran_OCUCPFunction_RESP_pLMNId',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_OCUCPFunction_RESP_pLMNId" FOREIGN KEY ("RESP_pLMNId") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_Site',
 'FK_o-ran-smo-teiv-equipment_Site_RESP_geo-location',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_Site" ADD CONSTRAINT "FK_o-ran-smo-teiv-equipment_Site_RESP_geo-location" FOREIGN KEY ("RESP_geo-location") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_Site',
 'FK_o-ran-smo-teiv-equipment_Site_RESP_name',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_Site" ADD CONSTRAINT "FK_o-ran-smo-teiv-equipment_Site_RESP_name" FOREIGN KEY ("RESP_name") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_AntennaCapability',
 'FK_o-ran-smo-teiv-ran_AntennaCapability_RESP_eUtranFqBands',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_AntennaCapability" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_AntennaCapability_RESP_eUtranFqBands" FOREIGN KEY ("RESP_eUtranFqBands") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_AntennaCapability',
 'FK_o-ran-smo-teiv-ran_AntennaCapability_RESP_geranFqBands',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_AntennaCapability" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_AntennaCapability_RESP_geranFqBands" FOREIGN KEY ("RESP_geranFqBands") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_AntennaCapability',
 'FK_o-ran-smo-teiv-ran_AntennaCapability_RESP_nRFqBands',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_AntennaCapability" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_AntennaCapability_RESP_nRFqBands" FOREIGN KEY ("RESP_nRFqBands") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'FK_o-ran-smo-teiv-equipment_AntennaModule_RESP_antennaBeamWidth',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "FK_o-ran-smo-teiv-equipment_AntennaModule_RESP_antennaBeamWidth" FOREIGN KEY ("RESP_antennaBeamWidth") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'FK_9F8C83A98E46570C846E83D0F220A05357E2007B',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "FK_9F8C83A98E46570C846E83D0F220A05357E2007B" FOREIGN KEY ("RESP_antennaModelNumber") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'FK_o-ran-smo-teiv-equipment_AntennaModule_RESP_azimuth',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "FK_o-ran-smo-teiv-equipment_AntennaModule_RESP_azimuth" FOREIGN KEY ("RESP_azimuth") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'FK_4A737D290E8E8B656C1794065E9B16D0E536E1C8',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "FK_4A737D290E8E8B656C1794065E9B16D0E536E1C8" FOREIGN KEY ("RESP_electricalAntennaTilt") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'FK_o-ran-smo-teiv-equipment_AntennaModule_RESP_geo-location',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "FK_o-ran-smo-teiv-equipment_AntennaModule_RESP_geo-location" FOREIGN KEY ("RESP_geo-location") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'FK_C4A0358503DD39D9CB99E3632B2E1E1C337A37CB',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "FK_C4A0358503DD39D9CB99E3632B2E1E1C337A37CB" FOREIGN KEY ("RESP_horizontalBeamWidth") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'FK_1DC2D3CF0DE253BABBF39A6E95711C96AA6EF1DC',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "FK_1DC2D3CF0DE253BABBF39A6E95711C96AA6EF1DC" FOREIGN KEY ("RESP_mechanicalAntennaBearing") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'FK_2B6558634445CBA8724FFE8D3E651761E260C91A',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "FK_2B6558634445CBA8724FFE8D3E651761E260C91A" FOREIGN KEY ("RESP_mechanicalAntennaTilt") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'FK_9E7F6B8B5829210351BD81048A305F97BF3E71E6',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "FK_9E7F6B8B5829210351BD81048A305F97BF3E71E6" FOREIGN KEY ("RESP_positionWithinSector") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'FK_o-ran-smo-teiv-equipment_AntennaModule_RESP_totalTilt',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "FK_o-ran-smo-teiv-equipment_AntennaModule_RESP_totalTilt" FOREIGN KEY ("RESP_totalTilt") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'FK_DF761BBEA829AC0658C2D49D290E500CB71546B7',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "FK_DF761BBEA829AC0658C2D49D290E500CB71546B7" FOREIGN KEY ("RESP_verticalBeamWidth") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_EUtranCell',
 'FK_o-ran-smo-teiv-ran_EUtranCell_RESP_cellId',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_EUtranCell_RESP_cellId" FOREIGN KEY ("RESP_cellId") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_EUtranCell',
 'FK_o-ran-smo-teiv-ran_EUtranCell_RESP_channelBandwidth',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_EUtranCell_RESP_channelBandwidth" FOREIGN KEY ("RESP_channelBandwidth") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_EUtranCell',
 'FK_o-ran-smo-teiv-ran_EUtranCell_RESP_dlChannelBandwidth',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_EUtranCell_RESP_dlChannelBandwidth" FOREIGN KEY ("RESP_dlChannelBandwidth") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_EUtranCell',
 'FK_o-ran-smo-teiv-ran_EUtranCell_RESP_duplexType',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_EUtranCell_RESP_duplexType" FOREIGN KEY ("RESP_duplexType") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_EUtranCell',
 'FK_o-ran-smo-teiv-ran_EUtranCell_RESP_earfcn',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_EUtranCell_RESP_earfcn" FOREIGN KEY ("RESP_earfcn") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_EUtranCell',
 'FK_o-ran-smo-teiv-ran_EUtranCell_RESP_earfcndl',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_EUtranCell_RESP_earfcndl" FOREIGN KEY ("RESP_earfcndl") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_EUtranCell',
 'FK_o-ran-smo-teiv-ran_EUtranCell_RESP_earfcnul',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_EUtranCell_RESP_earfcnul" FOREIGN KEY ("RESP_earfcnul") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_EUtranCell',
 'FK_o-ran-smo-teiv-ran_EUtranCell_RESP_tac',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_EUtranCell_RESP_tac" FOREIGN KEY ("RESP_tac") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_LTESectorCarrier',
 'FK_o-ran-smo-teiv-ran_LTESectorCarrier_RESP_sectorCarrierType',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_LTESectorCarrier_RESP_sectorCarrierType" FOREIGN KEY ("RESP_sectorCarrierType") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellCU',
 'FK_o-ran-smo-teiv-ran_NRCellCU_RESP_cellLocalId',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_NRCellCU_RESP_cellLocalId" FOREIGN KEY ("RESP_cellLocalId") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellCU',
 'FK_o-ran-smo-teiv-ran_NRCellCU_RESP_nCI',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_NRCellCU_RESP_nCI" FOREIGN KEY ("RESP_nCI") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellCU',
 'FK_o-ran-smo-teiv-ran_NRCellCU_RESP_nRTAC',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_NRCellCU_RESP_nRTAC" FOREIGN KEY ("RESP_nRTAC") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellCU',
 'FK_o-ran-smo-teiv-ran_NRCellCU_RESP_plmnId',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_NRCellCU_RESP_plmnId" FOREIGN KEY ("RESP_plmnId") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellDU',
 'FK_o-ran-smo-teiv-ran_NRCellDU_RESP_cellLocalId',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_NRCellDU_RESP_cellLocalId" FOREIGN KEY ("RESP_cellLocalId") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellDU',
 'FK_o-ran-smo-teiv-ran_NRCellDU_RESP_nCI',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_NRCellDU_RESP_nCI" FOREIGN KEY ("RESP_nCI") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellDU',
 'FK_o-ran-smo-teiv-ran_NRCellDU_RESP_nRPCI',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_NRCellDU_RESP_nRPCI" FOREIGN KEY ("RESP_nRPCI") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellDU',
 'FK_o-ran-smo-teiv-ran_NRCellDU_RESP_nRTAC',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_NRCellDU_RESP_nRTAC" FOREIGN KEY ("RESP_nRTAC") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_OCUUPFunction',
 'FK_o-ran-smo-teiv-ran_OCUUPFunction_RESP_gNBId',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_OCUUPFunction_RESP_gNBId" FOREIGN KEY ("RESP_gNBId") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_OCUUPFunction',
 'FK_o-ran-smo-teiv-ran_OCUUPFunction_RESP_gNBIdLength',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_OCUUPFunction_RESP_gNBIdLength" FOREIGN KEY ("RESP_gNBIdLength") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'FK_o-ran-smo-teiv-ran_NRSectorCarrier_RESP_arfcnDL',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_NRSectorCarrier_RESP_arfcnDL" FOREIGN KEY ("RESP_arfcnDL") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'FK_o-ran-smo-teiv-ran_NRSectorCarrier_RESP_arfcnUL',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_NRSectorCarrier_RESP_arfcnUL" FOREIGN KEY ("RESP_arfcnUL") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'FK_o-ran-smo-teiv-ran_NRSectorCarrier_RESP_bSChannelBwDL',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_NRSectorCarrier_RESP_bSChannelBwDL" FOREIGN KEY ("RESP_bSChannelBwDL") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'FK_o-ran-smo-teiv-ran_NRSectorCarrier_RESP_frequencyDL',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_NRSectorCarrier_RESP_frequencyDL" FOREIGN KEY ("RESP_frequencyDL") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'FK_o-ran-smo-teiv-ran_NRSectorCarrier_RESP_frequencyUL',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_NRSectorCarrier_RESP_frequencyUL" FOREIGN KEY ("RESP_frequencyUL") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_ENodeBFunction',
 'FK_o-ran-smo-teiv-ran_ENodeBFunction_RESP_eNBId',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_ENodeBFunction_RESP_eNBId" FOREIGN KEY ("RESP_eNBId") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_ENodeBFunction',
 'FK_o-ran-smo-teiv-ran_ENodeBFunction_RESP_eNodeBPlmnId',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_ENodeBFunction_RESP_eNodeBPlmnId" FOREIGN KEY ("RESP_eNodeBPlmnId") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_Sector',
 'FK_o-ran-smo-teiv-ran_Sector_RESP_azimuth',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_Sector" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_Sector_RESP_azimuth" FOREIGN KEY ("RESP_azimuth") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_Sector',
 'FK_o-ran-smo-teiv-ran_Sector_RESP_geo-location',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_Sector" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_Sector_RESP_geo-location" FOREIGN KEY ("RESP_geo-location") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_Sector',
 'FK_o-ran-smo-teiv-ran_Sector_RESP_sectorId',
 'ALTER TABLE teiv_data."o-ran-smo-teiv-ran_Sector" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_Sector_RESP_sectorId" FOREIGN KEY ("RESP_sectorId") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

CREATE INDEX IF NOT EXISTS "IDX_A5A6D0F976F10334CBD2DEDBDB6894BF8720157B" ON teiv_data."o-ran-smo-teiv-ran_ODUFunction" USING GIN ("REL_metadata_MANAGEDELEMENT_MANAGES_ODUFUNCTION");

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_ODUFunction_metadata" ON teiv_data."o-ran-smo-teiv-ran_ODUFunction" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_E4D4598CF15FD8AF9AE91F3647A8D943E1A51E8C" ON teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" USING GIN ("REL_metadata_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION");

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_OCUCPFunction_metadata" ON teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-equipment_Site_metadata" ON teiv_data."o-ran-smo-teiv-equipment_Site" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_AntennaCapability_metadata" ON teiv_data."o-ran-smo-teiv-ran_AntennaCapability" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_B309F56A2389CE1A1CD82B4982F98F7F3B50570E" ON teiv_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN ("REL_metadata_ANTENNAMODULE_INSTALLED_AT_SITE");

CREATE INDEX IF NOT EXISTS "IDX_0B4648D289B22EAA16B21BE4505F49850AC018C9" ON teiv_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN ("REL_metadata_SECTOR_GROUPS_ANTENNAMODULE");

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-equipment_AntennaModule_metadata" ON teiv_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_82B09F9D6370B2F07855F70042BA1B0ED782167D" ON teiv_data."o-ran-smo-teiv-ran_EUtranCell" USING GIN ("REL_metadata_ENODEBFUNCTION_PROVIDES_EUTRANCELL");

CREATE INDEX IF NOT EXISTS "IDX_C4728AE1A440E5C3670B4B691213C163D805CD84" ON teiv_data."o-ran-smo-teiv-ran_EUtranCell" USING GIN ("REL_metadata_SECTOR_GROUPS_EUTRANCELL");

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_EUtranCell_metadata" ON teiv_data."o-ran-smo-teiv-ran_EUtranCell" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_9966F4C45B258EBA701CE9C1564152885FE3251A" ON teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN ("REL_metadata_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER");

CREATE INDEX IF NOT EXISTS "IDX_19ABF252C4E09016A6FA24AA27724311226C554D" ON teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN ("REL_metadata_EUTRANCELL_USES_LTESECTORCARRIER");

CREATE INDEX IF NOT EXISTS "IDX_5D51C73E247073756AE9185C219303E004250431" ON teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN ("REL_metadata_LTESECTORCARRIER_USES_ANTENNACAPABILITY");

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_LTESectorCarrier_metadata" ON teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_9F6548916831A3F9771008367D58936D21C30423" ON teiv_data."o-ran-smo-teiv-ran_NRCellCU" USING GIN ("REL_metadata_OCUCPFUNCTION_PROVIDES_NRCELLCU");

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_NRCellCU_metadata" ON teiv_data."o-ran-smo-teiv-ran_NRCellCU" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-oam_ManagedElement_metadata" ON teiv_data."o-ran-smo-teiv-oam_ManagedElement" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_C731749DB013DA392D5010E566ACA0961D9A6535" ON teiv_data."o-ran-smo-teiv-ran_NRCellDU" USING GIN ("REL_metadata_ODUFUNCTION_PROVIDES_NRCELLDU");

CREATE INDEX IF NOT EXISTS "IDX_BE5B953B9861DD77A9096198E375074251030653" ON teiv_data."o-ran-smo-teiv-ran_NRCellDU" USING GIN ("REL_metadata_SECTOR_GROUPS_NRCELLDU");

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_NRCellDU_metadata" ON teiv_data."o-ran-smo-teiv-ran_NRCellDU" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_FE8D81BC4BD1C54D860A934A4484326AAFF02726" ON teiv_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_4E84A0C18434550C0096F1EE2695DF5952826EA4" ON teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" USING GIN ("REL_metadata_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION");

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_OCUUPFunction_metadata" ON teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_E1F96D2B0519812654B5A3D95B8F0E90C0FBDA55" ON teiv_data."o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_5B3F8D2AA421A4F7084B428387F4BFABE236A936" ON teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN ("REL_metadata_ODUFUNCTION_PROVIDES_NRSECTORCARRIER");

CREATE INDEX IF NOT EXISTS "IDX_21B2E5B5220DC3FDF12D6E481F0E672B5B034BCA" ON teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN ("REL_metadata_NRCELLDU_USES_NRSECTORCARRIER");

CREATE INDEX IF NOT EXISTS "IDX_20C83B7E7A4AFBA974AFBF99A0ED1A398F5C11D3" ON teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN ("REL_metadata_NRSECTORCARRIER_USES_ANTENNACAPABILITY");

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_NRSectorCarrier_metadata" ON teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_943E4F35504A6F925ADA7C609CE639875C09C949" ON teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" USING GIN ("REL_metadata_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION");

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_ENodeBFunction_metadata" ON teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_Sector_metadata" ON teiv_data."o-ran-smo-teiv-ran_Sector" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_24F125BEB2D7C718FA6F2634178AA92F7F71D95A" ON teiv_data."1D71FC3BAE50E72552EAAC17B3B0C959B403E822" USING GIN ("metadata");

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" DROP COLUMN IF EXISTS "updated_time";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ODUFunction" DROP COLUMN IF EXISTS "REL_updated_time_MANAGEDELEMENT_MANAGES_ODUFUNCTION";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" DROP COLUMN IF EXISTS "updated_time";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" DROP COLUMN IF EXISTS "REL_updated_time_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION";

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_Site" DROP COLUMN IF EXISTS "updated_time";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_AntennaCapability" DROP COLUMN IF EXISTS "updated_time";

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" DROP COLUMN IF EXISTS "updated_time";

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" DROP COLUMN IF EXISTS "REL_updated_time_ANTENNAMODULE_INSTALLED_AT_SITE";

ALTER TABLE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" DROP COLUMN IF EXISTS "REL_updated_time_SECTOR_GROUPS_ANTENNAMODULE";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" DROP COLUMN IF EXISTS "updated_time";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" DROP COLUMN IF EXISTS "REL_updated_time_ENODEBFUNCTION_PROVIDES_EUTRANCELL";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_EUtranCell" DROP COLUMN IF EXISTS "REL_updated_time_SECTOR_GROUPS_EUTRANCELL";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" DROP COLUMN IF EXISTS "updated_time";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" DROP COLUMN IF EXISTS "REL_updated_time_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" DROP COLUMN IF EXISTS "REL_updated_time_EUTRANCELL_USES_LTESECTORCARRIER";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier" DROP COLUMN IF EXISTS "REL_updated_time_LTESECTORCARRIER_USES_ANTENNACAPABILITY";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" DROP COLUMN IF EXISTS "updated_time";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellCU" DROP COLUMN IF EXISTS "REL_updated_time_OCUCPFUNCTION_PROVIDES_NRCELLCU";

ALTER TABLE teiv_data."o-ran-smo-teiv-oam_ManagedElement" DROP COLUMN IF EXISTS "updated_time";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" DROP COLUMN IF EXISTS "updated_time";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" DROP COLUMN IF EXISTS "REL_updated_time_ODUFUNCTION_PROVIDES_NRCELLDU";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRCellDU" DROP COLUMN IF EXISTS "REL_updated_time_SECTOR_GROUPS_NRCELLDU";

ALTER TABLE teiv_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" DROP COLUMN IF EXISTS "updated_time";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" DROP COLUMN IF EXISTS "updated_time";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" DROP COLUMN IF EXISTS "REL_updated_time_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION";

ALTER TABLE teiv_data."o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU" DROP COLUMN IF EXISTS "updated_time";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" DROP COLUMN IF EXISTS "updated_time";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" DROP COLUMN IF EXISTS "REL_updated_time_ODUFUNCTION_PROVIDES_NRSECTORCARRIER";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" DROP COLUMN IF EXISTS "REL_updated_time_NRCELLDU_USES_NRSECTORCARRIER";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" DROP COLUMN IF EXISTS "REL_updated_time_NRSECTORCARRIER_USES_ANTENNACAPABILITY";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" DROP COLUMN IF EXISTS "updated_time";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_ENodeBFunction" DROP COLUMN IF EXISTS "REL_updated_time_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION";

ALTER TABLE teiv_data."o-ran-smo-teiv-ran_Sector" DROP COLUMN IF EXISTS "updated_time";

ALTER TABLE teiv_data."1D71FC3BAE50E72552EAAC17B3B0C959B403E822" DROP COLUMN IF EXISTS "updated_time";

CREATE TABLE IF NOT EXISTS teiv_data."09D47DF128202AB535BC2056D7C0952F6EE7AF2D" (
	"id"			TEXT,
	"aSide_2A2D3374BF907674FA1905478E30ACB8882DC03C"			TEXT,
	"bSide_EE6DD4A2CFD743779BBCBFC18FC296EF6D72EB1E"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"metadata"			jsonb,
	"RESP_id"			BYTEA
);

ALTER TABLE ONLY teiv_data."09D47DF128202AB535BC2056D7C0952F6EE7AF2D" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."09D47DF128202AB535BC2056D7C0952F6EE7AF2D" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."09D47DF128202AB535BC2056D7C0952F6EE7AF2D" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."8F1E5B0125750A5230DB73DB7816ECCFFC2D918B" (
	"id"			TEXT,
	"sectorCarrierType"			TEXT,
	"RI_sectorCarrierType"			teiv_data."Reliability",
	"RESP_sectorCarrierType"			BYTEA,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"metadata"			jsonb,
	"RESP_id"			BYTEA,
	"REL_FK_used-antennaCapability"			TEXT,
	"REL_ID_1C61FC18067350DB393AFDB5270E9DE1F5151C64"			TEXT,
	"REL_CD_8D8BBB53FB18E8E9A5D241B66DA18CB5DDE9C1A5"			jsonb,
	"REL_CD_68C234330FD6388836D0DAF9DFD0A40DE66DD8C5"			jsonb,
	"REL_CD_A14923FFF9D13FEB18087CE2A9C0BD264C572CFC"			jsonb,
	"REL_metadata_F723867781098568079DD82E5D5E529374F97E98"			jsonb,
	"REL_RESP_id_927D105F3F1EA229D51FB9D2C90C6D5727FB3F7B"			BYTEA
);

ALTER TABLE ONLY teiv_data."8F1E5B0125750A5230DB73DB7816ECCFFC2D918B" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."8F1E5B0125750A5230DB73DB7816ECCFFC2D918B" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."8F1E5B0125750A5230DB73DB7816ECCFFC2D918B" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."8F1E5B0125750A5230DB73DB7816ECCFFC2D918B" ALTER COLUMN "REL_CD_8D8BBB53FB18E8E9A5D241B66DA18CB5DDE9C1A5" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."8F1E5B0125750A5230DB73DB7816ECCFFC2D918B" ALTER COLUMN "REL_CD_68C234330FD6388836D0DAF9DFD0A40DE66DD8C5" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."8F1E5B0125750A5230DB73DB7816ECCFFC2D918B" ALTER COLUMN "REL_CD_A14923FFF9D13FEB18087CE2A9C0BD264C572CFC" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."98D95275440120DC7A5FEF7E870FB6649F275AEC" (
	"id"			TEXT,
	"aSide_2A2D3374BF907674FA1905478E30ACB8882DC03C"			TEXT,
	"bSide_EE6DD4A2CFD743779BBCBFC18FC296EF6D72EB1E"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"metadata"			jsonb,
	"RESP_id"			BYTEA
);

ALTER TABLE ONLY teiv_data."98D95275440120DC7A5FEF7E870FB6649F275AEC" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."98D95275440120DC7A5FEF7E870FB6649F275AEC" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."98D95275440120DC7A5FEF7E870FB6649F275AEC" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" (
	"id"			TEXT,
	"antennaBeamWidth"			jsonb,
	"antennaModelNumber"			TEXT,
	"azimuth"			DECIMAL,
	"electricalAntennaTilt"			INTEGER,
	"geo-location"			geography,
	"horizontalBeamWidth"			DECIMAL,
	"mechanicalAntennaBearing"			INTEGER,
	"mechanicalAntennaTilt"			INTEGER,
	"positionWithinSector"			TEXT,
	"totalTilt"			INTEGER,
	"verticalBeamWidth"			DECIMAL,
	"RI_antennaBeamWidth"			teiv_data."Reliability",
	"RESP_antennaBeamWidth"			BYTEA,
	"RI_antennaModelNumber"			teiv_data."Reliability",
	"RESP_antennaModelNumber"			BYTEA,
	"RI_azimuth"			teiv_data."Reliability",
	"RESP_azimuth"			BYTEA,
	"RI_electricalAntennaTilt"			teiv_data."Reliability",
	"RESP_electricalAntennaTilt"			BYTEA,
	"RI_geo-location"			teiv_data."Reliability",
	"RESP_geo-location"			BYTEA,
	"RI_horizontalBeamWidth"			teiv_data."Reliability",
	"RESP_horizontalBeamWidth"			BYTEA,
	"RI_mechanicalAntennaBearing"			teiv_data."Reliability",
	"RESP_mechanicalAntennaBearing"			BYTEA,
	"RI_mechanicalAntennaTilt"			teiv_data."Reliability",
	"RESP_mechanicalAntennaTilt"			BYTEA,
	"RI_positionWithinSector"			teiv_data."Reliability",
	"RESP_positionWithinSector"			BYTEA,
	"RI_totalTilt"			teiv_data."Reliability",
	"RESP_totalTilt"			BYTEA,
	"RI_verticalBeamWidth"			teiv_data."Reliability",
	"RESP_verticalBeamWidth"			BYTEA,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"metadata"			jsonb,
	"RESP_id"			BYTEA
);

ALTER TABLE ONLY teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."AA68071F5A253FC68706DC4244B2EEEDCFA4F477" (
	"id"			TEXT,
	"eUtranFqBands"			jsonb,
	"geranFqBands"			jsonb,
	"nRFqBands"			jsonb,
	"RI_eUtranFqBands"			teiv_data."Reliability",
	"RESP_eUtranFqBands"			BYTEA,
	"RI_geranFqBands"			teiv_data."Reliability",
	"RESP_geranFqBands"			BYTEA,
	"RI_nRFqBands"			teiv_data."Reliability",
	"RESP_nRFqBands"			BYTEA,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"metadata"			jsonb,
	"RESP_id"			BYTEA
);

ALTER TABLE ONLY teiv_data."AA68071F5A253FC68706DC4244B2EEEDCFA4F477" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."AA68071F5A253FC68706DC4244B2EEEDCFA4F477" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."AA68071F5A253FC68706DC4244B2EEEDCFA4F477" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" (
	"id"			TEXT,
	"dUpLMNId"			jsonb,
	"gNBDUId"			BIGINT,
	"gNBId"			BIGINT,
	"gNBIdLength"			INTEGER,
	"RI_dUpLMNId"			teiv_data."Reliability",
	"RESP_dUpLMNId"			BYTEA,
	"RI_gNBDUId"			teiv_data."Reliability",
	"RESP_gNBDUId"			BYTEA,
	"RI_gNBId"			teiv_data."Reliability",
	"RESP_gNBId"			BYTEA,
	"RI_gNBIdLength"			teiv_data."Reliability",
	"RESP_gNBIdLength"			BYTEA,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"metadata"			jsonb,
	"RESP_id"			BYTEA,
	"REL_FK_managed-by-managedElement"			TEXT,
	"REL_ID_BDE0B6C74D14AC109D29A08D80E92D4D0DCAEB0B"			TEXT,
	"REL_CD_45E8E8693B1B8928376EAA8995D08AA7B1E483BD"			jsonb,
	"REL_CD_7E9F11EFBF8974D7C7DAB02E181A0BE4148091C6"			jsonb,
	"REL_CD_FFF7E036187A7605BFC714483D2E60FD2FF6560B"			jsonb,
	"REL_metadata_72494257260B57223C09828D7C3B31D203B050B4"			jsonb,
	"REL_RESP_id_B327AD97E459B9904E24439FF4F9A442165DD1C0"			BYTEA
);

ALTER TABLE ONLY teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" ALTER COLUMN "REL_CD_45E8E8693B1B8928376EAA8995D08AA7B1E483BD" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" ALTER COLUMN "REL_CD_7E9F11EFBF8974D7C7DAB02E181A0BE4148091C6" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" ALTER COLUMN "REL_CD_FFF7E036187A7605BFC714483D2E60FD2FF6560B" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."C9475FE40C02BEB41F720B4284A856F2CF49E1F3" (
	"id"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"metadata"			jsonb,
	"RESP_id"			BYTEA,
	"REL_FK_used-nrCellDu"			TEXT,
	"REL_ID_ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU"			TEXT,
	"REL_CD_1F61FA6DDAECE90540F9880F2A98037B1530A5A4"			jsonb,
	"REL_CD_E388983F3E6BFAD67CA100F0AFCF8CD3E9B89ADD"			jsonb,
	"REL_CD_EF3979E9DAF31B7949C883654633E633B9D35B92"			jsonb,
	"REL_metadata_C9E09FBCCBA1F6A3252B71B869B269EF28AACDFB"			jsonb,
	"REL_RESP_id_16BDFF7861A21C39988A1CE8BF89B1816C8754FC"			BYTEA
);

ALTER TABLE ONLY teiv_data."C9475FE40C02BEB41F720B4284A856F2CF49E1F3" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."C9475FE40C02BEB41F720B4284A856F2CF49E1F3" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."C9475FE40C02BEB41F720B4284A856F2CF49E1F3" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."C9475FE40C02BEB41F720B4284A856F2CF49E1F3" ALTER COLUMN "REL_CD_1F61FA6DDAECE90540F9880F2A98037B1530A5A4" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."C9475FE40C02BEB41F720B4284A856F2CF49E1F3" ALTER COLUMN "REL_CD_E388983F3E6BFAD67CA100F0AFCF8CD3E9B89ADD" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."C9475FE40C02BEB41F720B4284A856F2CF49E1F3" ALTER COLUMN "REL_CD_EF3979E9DAF31B7949C883654633E633B9D35B92" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."E2D2B450D65F1FF241A983CBB43B54C040969908" (
	"id"			TEXT,
	"aSide_2A2D3374BF907674FA1905478E30ACB8882DC03C"			TEXT,
	"bSide_E8A97B8133D74D3BE65119E868FAC0BE63C09FFC"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"metadata"			jsonb,
	"RESP_id"			BYTEA
);

ALTER TABLE ONLY teiv_data."E2D2B450D65F1FF241A983CBB43B54C040969908" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."E2D2B450D65F1FF241A983CBB43B54C040969908" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."E2D2B450D65F1FF241A983CBB43B54C040969908" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" (
	"id"			TEXT,
	"020335B0F627C169E24167748C38FE756FB34AE2"			INTEGER,
	"nCI"			BIGINT,
	"nRPCI"			INTEGER,
	"nRTAC"			INTEGER,
	"RI_6022A4C8771F6FCB03972F8966C1AD11DB9AD215"			teiv_data."Reliability",
	"RESP_958686CF1CBC168C2ED063F6645F06556C76DC3B"			BYTEA,
	"RI_nCI"			teiv_data."Reliability",
	"RESP_nCI"			BYTEA,
	"RI_nRPCI"			teiv_data."Reliability",
	"RESP_nRPCI"			BYTEA,
	"RI_nRTAC"			teiv_data."Reliability",
	"RESP_nRTAC"			BYTEA,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"metadata"			jsonb,
	"RESP_id"			BYTEA,
	"REL_FK_provided-by-oduFunction"			TEXT,
	"REL_ID_7899092EC8FBC674398C53965ADEFF940D17481F"			TEXT,
	"REL_CD_32FFD9EF85AB2342E652FA493C3BF34D9BAAF054"			jsonb,
	"REL_CD_B614AAA814176BC7128CE9D72C0950C4D6DE8052"			jsonb,
	"REL_CD_0AC0D382E4274681870EC7319460192F4F603001"			jsonb,
	"REL_metadata_8B4FA813345B7F25ACF2E9635EAF6E6DE0FBBE8A"			jsonb,
	"REL_RESP_id_3E560656C6590F0E2C4BAD9653CE371CF29D2D08"			BYTEA
);

ALTER TABLE ONLY teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" ALTER COLUMN "REL_CD_32FFD9EF85AB2342E652FA493C3BF34D9BAAF054" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" ALTER COLUMN "REL_CD_B614AAA814176BC7128CE9D72C0950C4D6DE8052" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" ALTER COLUMN "REL_CD_0AC0D382E4274681870EC7319460192F4F603001" SET DEFAULT '{}';

SELECT teiv_data.create_constraint_if_not_exists(
	'09D47DF128202AB535BC2056D7C0952F6EE7AF2D',
 'PK_8123C74A8D9A873F330C37E370DB951212243844',
 'ALTER TABLE teiv_data."09D47DF128202AB535BC2056D7C0952F6EE7AF2D" ADD CONSTRAINT "PK_8123C74A8D9A873F330C37E370DB951212243844" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'8F1E5B0125750A5230DB73DB7816ECCFFC2D918B',
 'PK_61C7969B1B7C806E01CE3D9471CFF52FA37864C3',
 'ALTER TABLE teiv_data."8F1E5B0125750A5230DB73DB7816ECCFFC2D918B" ADD CONSTRAINT "PK_61C7969B1B7C806E01CE3D9471CFF52FA37864C3" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'98D95275440120DC7A5FEF7E870FB6649F275AEC',
 'PK_48BAB21388EA2B1BE83EF5D3FC143807776A882D',
 'ALTER TABLE teiv_data."98D95275440120DC7A5FEF7E870FB6649F275AEC" ADD CONSTRAINT "PK_48BAB21388EA2B1BE83EF5D3FC143807776A882D" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974',
 'PK_8EB1724D91D554E7771C8C5AE5AA752A604E025C',
 'ALTER TABLE teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" ADD CONSTRAINT "PK_8EB1724D91D554E7771C8C5AE5AA752A604E025C" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'AA68071F5A253FC68706DC4244B2EEEDCFA4F477',
 'PK_AE71D2F6E7DABC3C066CEB9E1F30286DE8AF12D0',
 'ALTER TABLE teiv_data."AA68071F5A253FC68706DC4244B2EEEDCFA4F477" ADD CONSTRAINT "PK_AE71D2F6E7DABC3C066CEB9E1F30286DE8AF12D0" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'AD6AA1B5E225BBC96B792246D095340747056399',
 'PK_BEBBD904815CB84D3D7D326B13A71BC786E8C2B6',
 'ALTER TABLE teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" ADD CONSTRAINT "PK_BEBBD904815CB84D3D7D326B13A71BC786E8C2B6" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'C9475FE40C02BEB41F720B4284A856F2CF49E1F3',
 'PK_AD1799D4203C08DE10524497B4997C2DCB4F6A92',
 'ALTER TABLE teiv_data."C9475FE40C02BEB41F720B4284A856F2CF49E1F3" ADD CONSTRAINT "PK_AD1799D4203C08DE10524497B4997C2DCB4F6A92" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'E2D2B450D65F1FF241A983CBB43B54C040969908',
 'PK_E078962EDFAEDBC93784222B0B7BDFC3929D4D79',
 'ALTER TABLE teiv_data."E2D2B450D65F1FF241A983CBB43B54C040969908" ADD CONSTRAINT "PK_E078962EDFAEDBC93784222B0B7BDFC3929D4D79" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'F93C2CA075353668A76B4718E07B741ACCD83641',
 'PK_94DB80CFD01964A5D56BC22F433BBAF282F5AB0E',
 'ALTER TABLE teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" ADD CONSTRAINT "PK_94DB80CFD01964A5D56BC22F433BBAF282F5AB0E" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'09D47DF128202AB535BC2056D7C0952F6EE7AF2D',
 'FK_1A5303EF43A4407B0A47C80B9EF3DCA81FEA484A',
 'ALTER TABLE teiv_data."09D47DF128202AB535BC2056D7C0952F6EE7AF2D" ADD CONSTRAINT "FK_1A5303EF43A4407B0A47C80B9EF3DCA81FEA484A" FOREIGN KEY ("aSide_2A2D3374BF907674FA1905478E30ACB8882DC03C") REFERENCES teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'09D47DF128202AB535BC2056D7C0952F6EE7AF2D',
 'FK_AAB4A7CE5DD41A0CAEFA20B7538F2B851529E327',
 'ALTER TABLE teiv_data."09D47DF128202AB535BC2056D7C0952F6EE7AF2D" ADD CONSTRAINT "FK_AAB4A7CE5DD41A0CAEFA20B7538F2B851529E327" FOREIGN KEY ("bSide_EE6DD4A2CFD743779BBCBFC18FC296EF6D72EB1E") REFERENCES teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'09D47DF128202AB535BC2056D7C0952F6EE7AF2D',
 'FK_A5E880C59CF224776E30855EBDF62B6E98C5D127',
 'ALTER TABLE teiv_data."09D47DF128202AB535BC2056D7C0952F6EE7AF2D" ADD CONSTRAINT "FK_A5E880C59CF224776E30855EBDF62B6E98C5D127" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'8F1E5B0125750A5230DB73DB7816ECCFFC2D918B',
 'FK_53502FA0D6ADEA6C33D6DB99A53D6F3100E9E288',
 'ALTER TABLE teiv_data."8F1E5B0125750A5230DB73DB7816ECCFFC2D918B" ADD CONSTRAINT "FK_53502FA0D6ADEA6C33D6DB99A53D6F3100E9E288" FOREIGN KEY ("RESP_sectorCarrierType") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'8F1E5B0125750A5230DB73DB7816ECCFFC2D918B',
 'FK_100298449AA5ACC1F08DB20BC65868C337E9C333',
 'ALTER TABLE teiv_data."8F1E5B0125750A5230DB73DB7816ECCFFC2D918B" ADD CONSTRAINT "FK_100298449AA5ACC1F08DB20BC65868C337E9C333" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'8F1E5B0125750A5230DB73DB7816ECCFFC2D918B',
 'FK_778741F2AA29B57611EB06A903D58099245BB537',
 'ALTER TABLE teiv_data."8F1E5B0125750A5230DB73DB7816ECCFFC2D918B" ADD CONSTRAINT "FK_778741F2AA29B57611EB06A903D58099245BB537" FOREIGN KEY ("REL_FK_used-antennaCapability") REFERENCES teiv_data."AA68071F5A253FC68706DC4244B2EEEDCFA4F477" ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'8F1E5B0125750A5230DB73DB7816ECCFFC2D918B',
 'UNIQUE_523F8C2098EA2DE66650F9CCA7713124C14D63DF',
 'ALTER TABLE teiv_data."8F1E5B0125750A5230DB73DB7816ECCFFC2D918B" ADD CONSTRAINT "UNIQUE_523F8C2098EA2DE66650F9CCA7713124C14D63DF" UNIQUE ("REL_ID_1C61FC18067350DB393AFDB5270E9DE1F5151C64");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'8F1E5B0125750A5230DB73DB7816ECCFFC2D918B',
 'FK_CD80909E7C7B2A0958E7D96DD4AE1EEA9B85205C',
 'ALTER TABLE teiv_data."8F1E5B0125750A5230DB73DB7816ECCFFC2D918B" ADD CONSTRAINT "FK_CD80909E7C7B2A0958E7D96DD4AE1EEA9B85205C" FOREIGN KEY ("REL_RESP_id_927D105F3F1EA229D51FB9D2C90C6D5727FB3F7B") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'98D95275440120DC7A5FEF7E870FB6649F275AEC',
 'FK_CF5ED6B6F3D98A44BCF9209A20D9C901F5C3C23E',
 'ALTER TABLE teiv_data."98D95275440120DC7A5FEF7E870FB6649F275AEC" ADD CONSTRAINT "FK_CF5ED6B6F3D98A44BCF9209A20D9C901F5C3C23E" FOREIGN KEY ("aSide_2A2D3374BF907674FA1905478E30ACB8882DC03C") REFERENCES teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'98D95275440120DC7A5FEF7E870FB6649F275AEC',
 'FK_FA290CAE2E1C24893F90877F0EDF7304CD6D4563',
 'ALTER TABLE teiv_data."98D95275440120DC7A5FEF7E870FB6649F275AEC" ADD CONSTRAINT "FK_FA290CAE2E1C24893F90877F0EDF7304CD6D4563" FOREIGN KEY ("bSide_EE6DD4A2CFD743779BBCBFC18FC296EF6D72EB1E") REFERENCES teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'98D95275440120DC7A5FEF7E870FB6649F275AEC',
 'FK_58D8B5F4AF4FC229F23B545470E43B8E7C69B75F',
 'ALTER TABLE teiv_data."98D95275440120DC7A5FEF7E870FB6649F275AEC" ADD CONSTRAINT "FK_58D8B5F4AF4FC229F23B545470E43B8E7C69B75F" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974',
 'FK_01962D77F25A6FD76223BE654784268BF60CD712',
 'ALTER TABLE teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" ADD CONSTRAINT "FK_01962D77F25A6FD76223BE654784268BF60CD712" FOREIGN KEY ("RESP_antennaBeamWidth") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974',
 'FK_F7844BAF6629C2103757E04A38C12A2F20500A15',
 'ALTER TABLE teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" ADD CONSTRAINT "FK_F7844BAF6629C2103757E04A38C12A2F20500A15" FOREIGN KEY ("RESP_antennaModelNumber") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974',
 'FK_5EB4306AB1BB07F1902717B5C845B689F479B0C8',
 'ALTER TABLE teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" ADD CONSTRAINT "FK_5EB4306AB1BB07F1902717B5C845B689F479B0C8" FOREIGN KEY ("RESP_azimuth") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974',
 'FK_7D30EF0E91E5821108E28C244D8F200A9F6EF171',
 'ALTER TABLE teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" ADD CONSTRAINT "FK_7D30EF0E91E5821108E28C244D8F200A9F6EF171" FOREIGN KEY ("RESP_electricalAntennaTilt") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974',
 'FK_7BB08E9AF9BC6AA4AA3709504D5D9D859446E7EC',
 'ALTER TABLE teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" ADD CONSTRAINT "FK_7BB08E9AF9BC6AA4AA3709504D5D9D859446E7EC" FOREIGN KEY ("RESP_geo-location") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974',
 'FK_AA75893038D9D2DFC5276DD64C5B89545B59A85D',
 'ALTER TABLE teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" ADD CONSTRAINT "FK_AA75893038D9D2DFC5276DD64C5B89545B59A85D" FOREIGN KEY ("RESP_horizontalBeamWidth") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974',
 'FK_F4A7E5AE0D0197F17A3BF498BFF708E04EA702FE',
 'ALTER TABLE teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" ADD CONSTRAINT "FK_F4A7E5AE0D0197F17A3BF498BFF708E04EA702FE" FOREIGN KEY ("RESP_mechanicalAntennaBearing") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974',
 'FK_1BBB2773C56AC17AF4F81E8CE86181C61F4E28D0',
 'ALTER TABLE teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" ADD CONSTRAINT "FK_1BBB2773C56AC17AF4F81E8CE86181C61F4E28D0" FOREIGN KEY ("RESP_mechanicalAntennaTilt") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974',
 'FK_166C6D1792E0FCE19E3F6AA5705219094678406F',
 'ALTER TABLE teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" ADD CONSTRAINT "FK_166C6D1792E0FCE19E3F6AA5705219094678406F" FOREIGN KEY ("RESP_positionWithinSector") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974',
 'FK_E891C64DBCC7E495419938DBD17B967635EA9921',
 'ALTER TABLE teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" ADD CONSTRAINT "FK_E891C64DBCC7E495419938DBD17B967635EA9921" FOREIGN KEY ("RESP_totalTilt") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974',
 'FK_CC36D660526D1C767F26A3544B6B473417B0EE94',
 'ALTER TABLE teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" ADD CONSTRAINT "FK_CC36D660526D1C767F26A3544B6B473417B0EE94" FOREIGN KEY ("RESP_verticalBeamWidth") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974',
 'FK_2B3186DA8B7B260F570B6482C815A75147876AAC',
 'ALTER TABLE teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" ADD CONSTRAINT "FK_2B3186DA8B7B260F570B6482C815A75147876AAC" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'AA68071F5A253FC68706DC4244B2EEEDCFA4F477',
 'FK_369392B0C4A062C140E858C9400C0E744F0AC68B',
 'ALTER TABLE teiv_data."AA68071F5A253FC68706DC4244B2EEEDCFA4F477" ADD CONSTRAINT "FK_369392B0C4A062C140E858C9400C0E744F0AC68B" FOREIGN KEY ("RESP_eUtranFqBands") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'AA68071F5A253FC68706DC4244B2EEEDCFA4F477',
 'FK_BDB6F82FFAC7E705C7D5D88643B77E36B2B9C4A7',
 'ALTER TABLE teiv_data."AA68071F5A253FC68706DC4244B2EEEDCFA4F477" ADD CONSTRAINT "FK_BDB6F82FFAC7E705C7D5D88643B77E36B2B9C4A7" FOREIGN KEY ("RESP_geranFqBands") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'AA68071F5A253FC68706DC4244B2EEEDCFA4F477',
 'FK_5ED96C3949F44E73C1F4EB9E8808FF79627A4E9D',
 'ALTER TABLE teiv_data."AA68071F5A253FC68706DC4244B2EEEDCFA4F477" ADD CONSTRAINT "FK_5ED96C3949F44E73C1F4EB9E8808FF79627A4E9D" FOREIGN KEY ("RESP_nRFqBands") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'AA68071F5A253FC68706DC4244B2EEEDCFA4F477',
 'FK_DD48083834253B9A506780AB5EC36078B571553C',
 'ALTER TABLE teiv_data."AA68071F5A253FC68706DC4244B2EEEDCFA4F477" ADD CONSTRAINT "FK_DD48083834253B9A506780AB5EC36078B571553C" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'AD6AA1B5E225BBC96B792246D095340747056399',
 'FK_7186222B8E58F2BA404053317F6B750832B7AEBB',
 'ALTER TABLE teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" ADD CONSTRAINT "FK_7186222B8E58F2BA404053317F6B750832B7AEBB" FOREIGN KEY ("RESP_dUpLMNId") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'AD6AA1B5E225BBC96B792246D095340747056399',
 'FK_F52167D7632035FBC133091B80F9DEE9E37ABD69',
 'ALTER TABLE teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" ADD CONSTRAINT "FK_F52167D7632035FBC133091B80F9DEE9E37ABD69" FOREIGN KEY ("RESP_gNBDUId") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'AD6AA1B5E225BBC96B792246D095340747056399',
 'FK_970FEC4332A2D5BBA9EB1398FE0F5FAD455D7EE3',
 'ALTER TABLE teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" ADD CONSTRAINT "FK_970FEC4332A2D5BBA9EB1398FE0F5FAD455D7EE3" FOREIGN KEY ("RESP_gNBId") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'AD6AA1B5E225BBC96B792246D095340747056399',
 'FK_0AB6F974D9D18A47E4FDCEB6C4260AC2A62218BD',
 'ALTER TABLE teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" ADD CONSTRAINT "FK_0AB6F974D9D18A47E4FDCEB6C4260AC2A62218BD" FOREIGN KEY ("RESP_gNBIdLength") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'AD6AA1B5E225BBC96B792246D095340747056399',
 'FK_71C5B9A055FF4C3CB37CB920A7442FC5411F7BCE',
 'ALTER TABLE teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" ADD CONSTRAINT "FK_71C5B9A055FF4C3CB37CB920A7442FC5411F7BCE" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'AD6AA1B5E225BBC96B792246D095340747056399',
 'FK_4ADA30B48EB5107E0F730EB9684B5545524ADB42',
 'ALTER TABLE teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" ADD CONSTRAINT "FK_4ADA30B48EB5107E0F730EB9684B5545524ADB42" FOREIGN KEY ("REL_FK_managed-by-managedElement") REFERENCES teiv_data."C9475FE40C02BEB41F720B4284A856F2CF49E1F3" ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'AD6AA1B5E225BBC96B792246D095340747056399',
 'UNIQUE_EFB77EDE9661E3C6826E575AB125FD4F8FCD3BE1',
 'ALTER TABLE teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" ADD CONSTRAINT "UNIQUE_EFB77EDE9661E3C6826E575AB125FD4F8FCD3BE1" UNIQUE ("REL_ID_BDE0B6C74D14AC109D29A08D80E92D4D0DCAEB0B");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'AD6AA1B5E225BBC96B792246D095340747056399',
 'FK_419715A9297507DFE5165617FB4D8827AEBF3A9A',
 'ALTER TABLE teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" ADD CONSTRAINT "FK_419715A9297507DFE5165617FB4D8827AEBF3A9A" FOREIGN KEY ("REL_RESP_id_B327AD97E459B9904E24439FF4F9A442165DD1C0") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'C9475FE40C02BEB41F720B4284A856F2CF49E1F3',
 'FK_743B8A7D8779674BDBA042C099462FFE7BFFBDBB',
 'ALTER TABLE teiv_data."C9475FE40C02BEB41F720B4284A856F2CF49E1F3" ADD CONSTRAINT "FK_743B8A7D8779674BDBA042C099462FFE7BFFBDBB" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'C9475FE40C02BEB41F720B4284A856F2CF49E1F3',
 'FK_15CBD840591824744326C05E2FC38C9ABE9A197E',
 'ALTER TABLE teiv_data."C9475FE40C02BEB41F720B4284A856F2CF49E1F3" ADD CONSTRAINT "FK_15CBD840591824744326C05E2FC38C9ABE9A197E" FOREIGN KEY ("REL_FK_used-nrCellDu") REFERENCES teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'C9475FE40C02BEB41F720B4284A856F2CF49E1F3',
 'UNIQUE_C40364D44BC6DFCB468EEE1A8FB42BDB71D94114',
 'ALTER TABLE teiv_data."C9475FE40C02BEB41F720B4284A856F2CF49E1F3" ADD CONSTRAINT "UNIQUE_C40364D44BC6DFCB468EEE1A8FB42BDB71D94114" UNIQUE ("REL_ID_ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'C9475FE40C02BEB41F720B4284A856F2CF49E1F3',
 'FK_B2748BFFCFCEE7C9AE54A3BACFF9052A7BE3E626',
 'ALTER TABLE teiv_data."C9475FE40C02BEB41F720B4284A856F2CF49E1F3" ADD CONSTRAINT "FK_B2748BFFCFCEE7C9AE54A3BACFF9052A7BE3E626" FOREIGN KEY ("REL_RESP_id_16BDFF7861A21C39988A1CE8BF89B1816C8754FC") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'E2D2B450D65F1FF241A983CBB43B54C040969908',
 'FK_8ABF058C19072591C1000BDDD52FF17F488DCF24',
 'ALTER TABLE teiv_data."E2D2B450D65F1FF241A983CBB43B54C040969908" ADD CONSTRAINT "FK_8ABF058C19072591C1000BDDD52FF17F488DCF24" FOREIGN KEY ("aSide_2A2D3374BF907674FA1905478E30ACB8882DC03C") REFERENCES teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'E2D2B450D65F1FF241A983CBB43B54C040969908',
 'FK_37802898775B4197154F9DA54F8B7946A3C735BA',
 'ALTER TABLE teiv_data."E2D2B450D65F1FF241A983CBB43B54C040969908" ADD CONSTRAINT "FK_37802898775B4197154F9DA54F8B7946A3C735BA" FOREIGN KEY ("bSide_E8A97B8133D74D3BE65119E868FAC0BE63C09FFC") REFERENCES teiv_data."AA68071F5A253FC68706DC4244B2EEEDCFA4F477" ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'E2D2B450D65F1FF241A983CBB43B54C040969908',
 'FK_A52435BCC8CAF93A5852E912203F3A76ACE9FA35',
 'ALTER TABLE teiv_data."E2D2B450D65F1FF241A983CBB43B54C040969908" ADD CONSTRAINT "FK_A52435BCC8CAF93A5852E912203F3A76ACE9FA35" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'F93C2CA075353668A76B4718E07B741ACCD83641',
 'FK_E958432C9E761168561ACE1E78B21A7AF92580C6',
 'ALTER TABLE teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" ADD CONSTRAINT "FK_E958432C9E761168561ACE1E78B21A7AF92580C6" FOREIGN KEY ("RESP_958686CF1CBC168C2ED063F6645F06556C76DC3B") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'F93C2CA075353668A76B4718E07B741ACCD83641',
 'FK_8CB88A1C0F652B51FD9845C4F564A272A4F58422',
 'ALTER TABLE teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" ADD CONSTRAINT "FK_8CB88A1C0F652B51FD9845C4F564A272A4F58422" FOREIGN KEY ("RESP_nCI") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'F93C2CA075353668A76B4718E07B741ACCD83641',
 'FK_3C034AD8F98A5E741F962CFC406F48EECD096DB8',
 'ALTER TABLE teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" ADD CONSTRAINT "FK_3C034AD8F98A5E741F962CFC406F48EECD096DB8" FOREIGN KEY ("RESP_nRPCI") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'F93C2CA075353668A76B4718E07B741ACCD83641',
 'FK_74E499FB900CBFFBCB389A24AC99CD5CE92A5144',
 'ALTER TABLE teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" ADD CONSTRAINT "FK_74E499FB900CBFFBCB389A24AC99CD5CE92A5144" FOREIGN KEY ("RESP_nRTAC") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'F93C2CA075353668A76B4718E07B741ACCD83641',
 'FK_FA12E9B91FE138393C518269329D9262104DAD7A',
 'ALTER TABLE teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" ADD CONSTRAINT "FK_FA12E9B91FE138393C518269329D9262104DAD7A" FOREIGN KEY ("RESP_id") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'F93C2CA075353668A76B4718E07B741ACCD83641',
 'FK_500A088A217D3F8B505A8F408076BB5AD265AF63',
 'ALTER TABLE teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" ADD CONSTRAINT "FK_500A088A217D3F8B505A8F408076BB5AD265AF63" FOREIGN KEY ("REL_FK_provided-by-oduFunction") REFERENCES teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'F93C2CA075353668A76B4718E07B741ACCD83641',
 'UNIQUE_6C7164EC5E639D7E0D833D3C4E0CAB85062EE418',
 'ALTER TABLE teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" ADD CONSTRAINT "UNIQUE_6C7164EC5E639D7E0D833D3C4E0CAB85062EE418" UNIQUE ("REL_ID_7899092EC8FBC674398C53965ADEFF940D17481F");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'F93C2CA075353668A76B4718E07B741ACCD83641',
 'FK_0C3F721FF39EBC5118835710D0C088C8E3328FF1',
 'ALTER TABLE teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" ADD CONSTRAINT "FK_0C3F721FF39EBC5118835710D0C088C8E3328FF1" FOREIGN KEY ("REL_RESP_id_3E560656C6590F0E2C4BAD9653CE371CF29D2D08") REFERENCES teiv_data."responsible_adapter" ("hashed_id");'
);

CREATE INDEX IF NOT EXISTS "IDX_8A2A0ABA5E6ACAA8EF991E5B983551524A679509" ON teiv_data."09D47DF128202AB535BC2056D7C0952F6EE7AF2D" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_07C2D9F67E32FD07A927D1DF5F06734D7F89DBA0" ON teiv_data."09D47DF128202AB535BC2056D7C0952F6EE7AF2D" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_58D33C1960633F6CAA5F7E0A9880FF85D5923BF7" ON teiv_data."09D47DF128202AB535BC2056D7C0952F6EE7AF2D" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_EA883FE1AB208DC033513D76B1414014FE4D1397" ON teiv_data."09D47DF128202AB535BC2056D7C0952F6EE7AF2D" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_53A525F2FE3F291B4F2B234FE4EC799C778A8687" ON teiv_data."8F1E5B0125750A5230DB73DB7816ECCFFC2D918B" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_8F5A022153912D07D92FCE34FA9E754E0163F818" ON teiv_data."8F1E5B0125750A5230DB73DB7816ECCFFC2D918B" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_83EEB2F630BDBBF7BE7E8BBFA452D7F2AB69EB69" ON teiv_data."8F1E5B0125750A5230DB73DB7816ECCFFC2D918B" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_77A2303E82E9B9B4A9D61BE7C4F1B6088C40A04E" ON teiv_data."8F1E5B0125750A5230DB73DB7816ECCFFC2D918B" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_DBBF1D30DCD311E5D9D17524730BBF38DEB8BBF8" ON teiv_data."8F1E5B0125750A5230DB73DB7816ECCFFC2D918B" USING GIN (("REL_CD_8D8BBB53FB18E8E9A5D241B66DA18CB5DDE9C1A5"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_D8626C46F2134489869EC9F70A2F3960D37BA656" ON teiv_data."8F1E5B0125750A5230DB73DB7816ECCFFC2D918B" USING GIN (("REL_CD_68C234330FD6388836D0DAF9DFD0A40DE66DD8C5"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_97C1311E85CBA88A2048A17CC3A91406A13A162A" ON teiv_data."8F1E5B0125750A5230DB73DB7816ECCFFC2D918B" USING GIN ("REL_CD_A14923FFF9D13FEB18087CE2A9C0BD264C572CFC");

CREATE INDEX IF NOT EXISTS "IDX_52840EAF2D41B89AC3CE825BC9B0D9AC35AEFC5C" ON teiv_data."8F1E5B0125750A5230DB73DB7816ECCFFC2D918B" USING GIN ("REL_metadata_F723867781098568079DD82E5D5E529374F97E98");

CREATE INDEX IF NOT EXISTS "IDX_13E530F00B0D405F58CDA235146715762EF6DF91" ON teiv_data."98D95275440120DC7A5FEF7E870FB6649F275AEC" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_B6911D7B07CE9E7CF692209760002ABD15DA8CD9" ON teiv_data."98D95275440120DC7A5FEF7E870FB6649F275AEC" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_4D253E190E8502C2DE0DC0E2A7A758EA8A5E5888" ON teiv_data."98D95275440120DC7A5FEF7E870FB6649F275AEC" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_A04886514310E5B31E206923891CD213D9A5CEE6" ON teiv_data."98D95275440120DC7A5FEF7E870FB6649F275AEC" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_452AE5B87383D94C9B188FBA8BBDA09B8A7E73B4" ON teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" USING GIN (("antennaBeamWidth"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_52B1FEA89EC725EA6195DBD272ED9983E98B99CB" ON teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_35E57E345ABB0D6A9CD6D8FBDBC620646C046669" ON teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_8B5018C20A65AC6FFF6E9B4C099B8F18058B3533" ON teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_54DC74991A70957B0CEBAA38200F3059ECCABDEC" ON teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_C9652A1BD1F6441ECBDB38922226AD08D7DDD60D" ON teiv_data."AA68071F5A253FC68706DC4244B2EEEDCFA4F477" USING GIN (("eUtranFqBands"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_41A2CB8C648BE9B006CB04631C440F71DA6A61C3" ON teiv_data."AA68071F5A253FC68706DC4244B2EEEDCFA4F477" USING GIN (("geranFqBands"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_49AB1B81ED859FE4A0F148617CB3E11454E05EAA" ON teiv_data."AA68071F5A253FC68706DC4244B2EEEDCFA4F477" USING GIN (("nRFqBands"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_CD2038615C829FA3EC5520DEF04DEBAD87DB7B57" ON teiv_data."AA68071F5A253FC68706DC4244B2EEEDCFA4F477" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_7470F8E4355FEDE440D3CEB831E05AF350B8144B" ON teiv_data."AA68071F5A253FC68706DC4244B2EEEDCFA4F477" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_3D44AB1602F5AA87FAF39E6F596888419F723F47" ON teiv_data."AA68071F5A253FC68706DC4244B2EEEDCFA4F477" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_9532CB2D7B71297C7279D2E824DBE655331B5220" ON teiv_data."AA68071F5A253FC68706DC4244B2EEEDCFA4F477" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_00624177EC65536001DDF0477A8F698B871C142D" ON teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" USING GIN ("dUpLMNId");

CREATE INDEX IF NOT EXISTS "IDX_241D90708F29A60824E2A63C86D9AB84E611E505" ON teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_98C227150239A7692514F1FDF5E3F49FC4439357" ON teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_F308FB8D16B912769597B9678466ADB19BAA27B2" ON teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_9EC8D13EBFB1E513FD4F18C284AD66FA6751E2BC" ON teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_0B9A243313790591EFA74660F1863924EE8CCB6C" ON teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" USING GIN (("REL_CD_45E8E8693B1B8928376EAA8995D08AA7B1E483BD"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_3FF5E0D439B273B25E65C4EC6E4682E5F8AE9D79" ON teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" USING GIN (("REL_CD_7E9F11EFBF8974D7C7DAB02E181A0BE4148091C6"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_DC5DA06087B85C01A975848202AC386694598D78" ON teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" USING GIN ("REL_CD_FFF7E036187A7605BFC714483D2E60FD2FF6560B");

CREATE INDEX IF NOT EXISTS "IDX_40D54F003F8A66E82DFC0BE377ADEFF1A8EAE78D" ON teiv_data."AD6AA1B5E225BBC96B792246D095340747056399" USING GIN ("REL_metadata_72494257260B57223C09828D7C3B31D203B050B4");

CREATE INDEX IF NOT EXISTS "IDX_36D40805D5D41247C46C23AF06A399E7CDC46EDD" ON teiv_data."C9475FE40C02BEB41F720B4284A856F2CF49E1F3" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_18D66A31B8B88551ADA98E4BC6362B71A504CE1B" ON teiv_data."C9475FE40C02BEB41F720B4284A856F2CF49E1F3" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_245803DDE5BD5AB2780BB277649AE40CA8F4E145" ON teiv_data."C9475FE40C02BEB41F720B4284A856F2CF49E1F3" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_CDD068E13765C799B6503561CB0FAC5638B22C70" ON teiv_data."C9475FE40C02BEB41F720B4284A856F2CF49E1F3" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_3EEEE6049314C502989CA49A6050FC40898889D1" ON teiv_data."C9475FE40C02BEB41F720B4284A856F2CF49E1F3" USING GIN (("REL_CD_1F61FA6DDAECE90540F9880F2A98037B1530A5A4"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_40A0F4A77264E4DD55EBA83EB2270B21E79F48FF" ON teiv_data."C9475FE40C02BEB41F720B4284A856F2CF49E1F3" USING GIN (("REL_CD_E388983F3E6BFAD67CA100F0AFCF8CD3E9B89ADD"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_4520B8DC811185D63D0E415F4E0AED4DC9967219" ON teiv_data."C9475FE40C02BEB41F720B4284A856F2CF49E1F3" USING GIN ("REL_CD_EF3979E9DAF31B7949C883654633E633B9D35B92");

CREATE INDEX IF NOT EXISTS "IDX_6E7F02AC7C81800C688BB46D2639CBABFB376239" ON teiv_data."C9475FE40C02BEB41F720B4284A856F2CF49E1F3" USING GIN ("REL_metadata_C9E09FBCCBA1F6A3252B71B869B269EF28AACDFB");

CREATE INDEX IF NOT EXISTS "IDX_C4B686BC6CAF76EADC031DCCC15CBA7C3F81427C" ON teiv_data."E2D2B450D65F1FF241A983CBB43B54C040969908" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_44B101C83E01F881D34B98CFB68E980DF4AF9A96" ON teiv_data."E2D2B450D65F1FF241A983CBB43B54C040969908" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_7C35D9F162CB5D2DA8BF96C1F076C9C55EC7CA96" ON teiv_data."E2D2B450D65F1FF241A983CBB43B54C040969908" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_2905D52A1A6E49FD9E984F4AD978144B6309C86F" ON teiv_data."E2D2B450D65F1FF241A983CBB43B54C040969908" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_E909484E1251C6944EE877CB737E07475884A1AE" ON teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_14454BB7AC41220815C5885AD1B3DB72F8089315" ON teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_3C1344FC931EF89522B673948D541FD73E375C4F" ON teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_BBF662FB069D121A65DCE5A46DAC3185B9A6D9A4" ON teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" USING GIN ("metadata");

CREATE INDEX IF NOT EXISTS "IDX_1161D2AF3E3B8B7503469AFF6E3DA87B4D66F852" ON teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" USING GIN (("REL_CD_32FFD9EF85AB2342E652FA493C3BF34D9BAAF054"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_4F93EF555F8640ADA220F02F1434AF29A96F85F2" ON teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" USING GIN (("REL_CD_B614AAA814176BC7128CE9D72C0950C4D6DE8052"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_17F56568BFFACD4F9F2E6372407CE7F8FA06AF17" ON teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" USING GIN ("REL_CD_0AC0D382E4274681870EC7319460192F4F603001");

CREATE INDEX IF NOT EXISTS "IDX_6DDF32177944AAEDACB2123D05680F3B3B9BD4C7" ON teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641" USING GIN ("REL_metadata_8B4FA813345B7F25ACF2E9635EAF6E6DE0FBBE8A");

ANALYZE teiv_data."o-ran-smo-teiv-ran_ODUFunction";

ANALYZE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction";

ANALYZE teiv_data."o-ran-smo-teiv-equipment_Site";

ANALYZE teiv_data."C9475FE40C02BEB41F720B4284A856F2CF49E1F3";

ANALYZE teiv_data."98D95275440120DC7A5FEF7E870FB6649F275AEC";

ANALYZE teiv_data."9AB5ED164AE73FC3AB14965C0B1F3E90A04CF974";

ANALYZE teiv_data."o-ran-smo-teiv-oam_ManagedElement";

ANALYZE teiv_data."o-ran-smo-teiv-ran_NRCellDU";

ANALYZE teiv_data."AD6AA1B5E225BBC96B792246D095340747056399";

ANALYZE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction";

ANALYZE teiv_data."o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU";

ANALYZE teiv_data."8F1E5B0125750A5230DB73DB7816ECCFFC2D918B";

ANALYZE teiv_data."o-ran-smo-teiv-ran_AntennaCapability";

ANALYZE teiv_data."o-ran-smo-teiv-equipment_AntennaModule";

ANALYZE teiv_data."09D47DF128202AB535BC2056D7C0952F6EE7AF2D";

ANALYZE teiv_data."o-ran-smo-teiv-ran_EUtranCell";

ANALYZE teiv_data."o-ran-smo-teiv-ran_LTESectorCarrier";

ANALYZE teiv_data."o-ran-smo-teiv-ran_NRCellCU";

ANALYZE teiv_data."F93C2CA075353668A76B4718E07B741ACCD83641";

ANALYZE teiv_data."CFC235E0404703D1E4454647DF8AAE2C193DB402";

ANALYZE teiv_data."AA68071F5A253FC68706DC4244B2EEEDCFA4F477";

ANALYZE teiv_data."E2D2B450D65F1FF241A983CBB43B54C040969908";

ANALYZE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier";

ANALYZE teiv_data."o-ran-smo-teiv-ran_ENodeBFunction";

ANALYZE teiv_data."o-ran-smo-teiv-ran_Sector";

ANALYZE teiv_data."1D71FC3BAE50E72552EAAC17B3B0C959B403E822";

COMMIT;
