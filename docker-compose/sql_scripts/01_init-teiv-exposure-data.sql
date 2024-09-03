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

GRANT USAGE ON SCHEMA topology to topology_exposure_user;
GRANT SELECT ON ALL SEQUENCES IN SCHEMA topology TO topology_exposure_user;
GRANT SELECT ON ALL TABLES IN SCHEMA topology TO topology_exposure_user;

CREATE SCHEMA IF NOT EXISTS ties_data;
ALTER SCHEMA ties_data OWNER TO topology_exposure_user;
SET default_tablespace = '';
SET default_table_access_method = heap;

SET ROLE 'topology_exposure_user';

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

CREATE TABLE IF NOT EXISTS ties_data."53983B8B2DDA2A5B8CA9EDBDA547333872024418" (
	"id"			TEXT,
	"aSide_NFDeployment"			TEXT,
	"bSide_GNBCUUPFunction"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY ties_data."53983B8B2DDA2A5B8CA9EDBDA547333872024418" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."53983B8B2DDA2A5B8CA9EDBDA547333872024418" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."53983B8B2DDA2A5B8CA9EDBDA547333872024418" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."93A133BC1B3BA1FA430DFB98A4AB1141328425FD" (
	"id"			TEXT,
	"aSide_NFDeployment"			TEXT,
	"bSide_GNBCUCPFunction"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY ties_data."93A133BC1B3BA1FA430DFB98A4AB1141328425FD" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."93A133BC1B3BA1FA430DFB98A4AB1141328425FD" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."93A133BC1B3BA1FA430DFB98A4AB1141328425FD" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" (
	"id"			TEXT,
	"aSide_AntennaModule"			TEXT,
	"bSide_AntennaCapability"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY ties_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-cloud_CloudNamespace" (
	"id"			TEXT,
	"name"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_deployed-on-nodeCluster"			TEXT,
	"REL_ID_CLOUDNAMESPACE_DEPLOYED_ON_NODECLUSTER"			TEXT,
	"REL_CD_sourceIds_CLOUDNAMESPACE_DEPLOYED_ON_NODECLUSTER"			jsonb,
	"REL_CD_classifiers_CLOUDNAMESPACE_DEPLOYED_ON_NODECLUSTER"			jsonb,
	"REL_CD_decorators_CLOUDNAMESPACE_DEPLOYED_ON_NODECLUSTER"			jsonb
);

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_CloudNamespace" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_CloudNamespace" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_CloudNamespace" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_CloudNamespace" ALTER COLUMN "REL_CD_sourceIds_CLOUDNAMESPACE_DEPLOYED_ON_NODECLUSTER" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_CloudNamespace" ALTER COLUMN "REL_CD_classifiers_CLOUDNAMESPACE_DEPLOYED_ON_NODECLUSTER" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_CloudNamespace" ALTER COLUMN "REL_CD_decorators_CLOUDNAMESPACE_DEPLOYED_ON_NODECLUSTER" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-cloud_CloudSite" (
	"id"			TEXT,
	"geo-location"			jsonb,
	"name"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_CloudSite" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_CloudSite" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_CloudSite" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-cloud_CloudifiedNF" (
	"id"			TEXT,
	"name"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_CloudifiedNF" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_CloudifiedNF" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_CloudifiedNF" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-cloud_NFDEPLOYMENT_DEPLOYED_ON_CLOUDNAMESPACE" (
	"id"			TEXT,
	"aSide_NFDeployment"			TEXT,
	"bSide_CloudNamespace"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_NFDEPLOYMENT_DEPLOYED_ON_CLOUDNAMESPACE" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_NFDEPLOYMENT_DEPLOYED_ON_CLOUDNAMESPACE" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_NFDEPLOYMENT_DEPLOYED_ON_CLOUDNAMESPACE" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-cloud_NFDeployment" (
	"id"			TEXT,
	"name"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_comprised-by-cloudifiedNF"			TEXT,
	"REL_ID_CLOUDIFIEDNF_COMPRISES_NFDEPLOYMENT"			TEXT,
	"REL_CD_sourceIds_CLOUDIFIEDNF_COMPRISES_NFDEPLOYMENT"			jsonb,
	"REL_CD_classifiers_CLOUDIFIEDNF_COMPRISES_NFDEPLOYMENT"			jsonb,
	"REL_CD_decorators_CLOUDIFIEDNF_COMPRISES_NFDEPLOYMENT"			jsonb,
	"REL_FK_serviced-managedElement"			TEXT,
	"REL_ID_NFDEPLOYMENT_SERVES_MANAGEDELEMENT"			TEXT,
	"REL_CD_sourceIds_NFDEPLOYMENT_SERVES_MANAGEDELEMENT"			jsonb,
	"REL_CD_classifiers_NFDEPLOYMENT_SERVES_MANAGEDELEMENT"			jsonb,
	"REL_CD_decorators_NFDEPLOYMENT_SERVES_MANAGEDELEMENT"			jsonb
);

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_NFDeployment" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_NFDeployment" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_NFDeployment" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_NFDeployment" ALTER COLUMN "REL_CD_sourceIds_CLOUDIFIEDNF_COMPRISES_NFDEPLOYMENT" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_NFDeployment" ALTER COLUMN "REL_CD_classifiers_CLOUDIFIEDNF_COMPRISES_NFDEPLOYMENT" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_NFDeployment" ALTER COLUMN "REL_CD_decorators_CLOUDIFIEDNF_COMPRISES_NFDEPLOYMENT" SET DEFAULT '{}';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_NFDeployment" ALTER COLUMN "REL_CD_sourceIds_NFDEPLOYMENT_SERVES_MANAGEDELEMENT" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_NFDeployment" ALTER COLUMN "REL_CD_classifiers_NFDEPLOYMENT_SERVES_MANAGEDELEMENT" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_NFDeployment" ALTER COLUMN "REL_CD_decorators_NFDEPLOYMENT_SERVES_MANAGEDELEMENT" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-cloud_NODECLUSTER_LOCATED_AT_CLOUDSITE" (
	"id"			TEXT,
	"aSide_NodeCluster"			TEXT,
	"bSide_CloudSite"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_NODECLUSTER_LOCATED_AT_CLOUDSITE" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_NODECLUSTER_LOCATED_AT_CLOUDSITE" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_NODECLUSTER_LOCATED_AT_CLOUDSITE" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-cloud_NodeCluster" (
	"id"			TEXT,
	"name"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_NodeCluster" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_NodeCluster" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-cloud_NodeCluster" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-equipment_AntennaModule" (
	"id"			TEXT,
	"antennaBeamWidth"			jsonb,
	"antennaModelNumber"			TEXT,
	"electricalAntennaTilt"			INTEGER,
	"geo-location"			jsonb,
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

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-equipment_AntennaModule" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-equipment_AntennaModule" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-equipment_AntennaModule" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-equipment_AntennaModule" ALTER COLUMN "REL_CD_sourceIds_ANTENNAMODULE_INSTALLED_AT_SITE" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-equipment_AntennaModule" ALTER COLUMN "REL_CD_classifiers_ANTENNAMODULE_INSTALLED_AT_SITE" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-equipment_AntennaModule" ALTER COLUMN "REL_CD_decorators_ANTENNAMODULE_INSTALLED_AT_SITE" SET DEFAULT '{}';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-equipment_AntennaModule" ALTER COLUMN "REL_CD_sourceIds_SECTOR_GROUPS_ANTENNAMODULE" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-equipment_AntennaModule" ALTER COLUMN "REL_CD_classifiers_SECTOR_GROUPS_ANTENNAMODULE" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-equipment_AntennaModule" ALTER COLUMN "REL_CD_decorators_SECTOR_GROUPS_ANTENNAMODULE" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-equipment_Site" (
	"id"			TEXT,
	"geo-location"			jsonb,
	"name"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-equipment_Site" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-equipment_Site" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-equipment_Site" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-oam_ManagedElement" (
	"id"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_deployed-as-cloudifiedNF"			TEXT,
	"REL_ID_MANAGEDELEMENT_DEPLOYED_AS_CLOUDIFIEDNF"			TEXT,
	"REL_CD_sourceIds_MANAGEDELEMENT_DEPLOYED_AS_CLOUDIFIEDNF"			jsonb,
	"REL_CD_classifiers_MANAGEDELEMENT_DEPLOYED_AS_CLOUDIFIEDNF"			jsonb,
	"REL_CD_decorators_MANAGEDELEMENT_DEPLOYED_AS_CLOUDIFIEDNF"			jsonb
);

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-oam_ManagedElement" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-oam_ManagedElement" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-oam_ManagedElement" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-oam_ManagedElement" ALTER COLUMN "REL_CD_sourceIds_MANAGEDELEMENT_DEPLOYED_AS_CLOUDIFIEDNF" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-oam_ManagedElement" ALTER COLUMN "REL_CD_classifiers_MANAGEDELEMENT_DEPLOYED_AS_CLOUDIFIEDNF" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-oam_ManagedElement" ALTER COLUMN "REL_CD_decorators_MANAGEDELEMENT_DEPLOYED_AS_CLOUDIFIEDNF" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-ran_AntennaCapability" (
	"id"			TEXT,
	"eUtranFqBands"			jsonb,
	"geranFqBands"			jsonb,
	"nRFqBands"			jsonb,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_AntennaCapability" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_AntennaCapability" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_AntennaCapability" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-ran_ENodeBFunction" (
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

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_ENodeBFunction" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_ENodeBFunction" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_ENodeBFunction" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_ENodeBFunction" ALTER COLUMN "REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_ENodeBFunction" ALTER COLUMN "REL_CD_classifiers_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_ENodeBFunction" ALTER COLUMN "REL_CD_decorators_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-ran_EUtranCell" (
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

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_EUtranCell" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_EUtranCell" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_EUtranCell" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_EUtranCell" ALTER COLUMN "REL_CD_sourceIds_ENODEBFUNCTION_PROVIDES_EUTRANCELL" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_EUtranCell" ALTER COLUMN "REL_CD_classifiers_ENODEBFUNCTION_PROVIDES_EUTRANCELL" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_EUtranCell" ALTER COLUMN "REL_CD_decorators_ENODEBFUNCTION_PROVIDES_EUTRANCELL" SET DEFAULT '{}';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_EUtranCell" ALTER COLUMN "REL_CD_sourceIds_SECTOR_GROUPS_EUTRANCELL" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_EUtranCell" ALTER COLUMN "REL_CD_classifiers_SECTOR_GROUPS_EUTRANCELL" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_EUtranCell" ALTER COLUMN "REL_CD_decorators_SECTOR_GROUPS_EUTRANCELL" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-ran_GNBCUCPFunction" (
	"id"			TEXT,
	"gNBCUName"			TEXT,
	"gNBId"			BIGINT,
	"gNBIdLength"			INTEGER,
	"pLMNId"			jsonb,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_managed-by-managedElement"			TEXT,
	"REL_ID_MANAGEDELEMENT_MANAGES_GNBCUCPFUNCTION"			TEXT,
	"REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_GNBCUCPFUNCTION"			jsonb,
	"REL_CD_classifiers_MANAGEDELEMENT_MANAGES_GNBCUCPFUNCTION"			jsonb,
	"REL_CD_decorators_MANAGEDELEMENT_MANAGES_GNBCUCPFUNCTION"			jsonb
);

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_GNBCUCPFunction" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_GNBCUCPFunction" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_GNBCUCPFunction" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_GNBCUCPFunction" ALTER COLUMN "REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_GNBCUCPFUNCTION" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_GNBCUCPFunction" ALTER COLUMN "REL_CD_classifiers_MANAGEDELEMENT_MANAGES_GNBCUCPFUNCTION" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_GNBCUCPFunction" ALTER COLUMN "REL_CD_decorators_MANAGEDELEMENT_MANAGES_GNBCUCPFUNCTION" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-ran_GNBCUUPFunction" (
	"id"			TEXT,
	"gNBId"			BIGINT,
	"gNBIdLength"			INTEGER,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_managed-by-managedElement"			TEXT,
	"REL_ID_MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION"			TEXT,
	"REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION"			jsonb,
	"REL_CD_classifiers_MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION"			jsonb,
	"REL_CD_decorators_MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION"			jsonb
);

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_GNBCUUPFunction" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_GNBCUUPFunction" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_GNBCUUPFunction" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_GNBCUUPFunction" ALTER COLUMN "REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_GNBCUUPFunction" ALTER COLUMN "REL_CD_classifiers_MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_GNBCUUPFunction" ALTER COLUMN "REL_CD_decorators_MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-ran_GNBDUFunction" (
	"id"			TEXT,
	"dUpLMNId"			jsonb,
	"gNBDUId"			BIGINT,
	"gNBId"			BIGINT,
	"gNBIdLength"			INTEGER,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_managed-by-managedElement"			TEXT,
	"REL_ID_MANAGEDELEMENT_MANAGES_GNBDUFUNCTION"			TEXT,
	"REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_GNBDUFUNCTION"			jsonb,
	"REL_CD_classifiers_MANAGEDELEMENT_MANAGES_GNBDUFUNCTION"			jsonb,
	"REL_CD_decorators_MANAGEDELEMENT_MANAGES_GNBDUFUNCTION"			jsonb
);

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_GNBDUFunction" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_GNBDUFunction" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_GNBDUFunction" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_GNBDUFunction" ALTER COLUMN "REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_GNBDUFUNCTION" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_GNBDUFunction" ALTER COLUMN "REL_CD_classifiers_MANAGEDELEMENT_MANAGES_GNBDUFUNCTION" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_GNBDUFunction" ALTER COLUMN "REL_CD_decorators_MANAGEDELEMENT_MANAGES_GNBDUFUNCTION" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" (
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

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "REL_CD_sourceIds_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "REL_CD_classifiers_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "REL_CD_decorators_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER" SET DEFAULT '{}';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "REL_CD_sourceIds_EUTRANCELL_USES_LTESECTORCARRIER" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "REL_CD_classifiers_EUTRANCELL_USES_LTESECTORCARRIER" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "REL_CD_decorators_EUTRANCELL_USES_LTESECTORCARRIER" SET DEFAULT '{}';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "REL_CD_sourceIds_LTESECTORCARRIER_USES_ANTENNACAPABILITY" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "REL_CD_classifiers_LTESECTORCARRIER_USES_ANTENNACAPABILITY" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ALTER COLUMN "REL_CD_decorators_LTESECTORCARRIER_USES_ANTENNACAPABILITY" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-ran_NRCellCU" (
	"id"			TEXT,
	"cellLocalId"			INTEGER,
	"nCI"			BIGINT,
	"nRTAC"			INTEGER,
	"plmnId"			jsonb,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_provided-by-gnbcucpFunction"			TEXT,
	"REL_ID_GNBCUCPFUNCTION_PROVIDES_NRCELLCU"			TEXT,
	"REL_CD_sourceIds_GNBCUCPFUNCTION_PROVIDES_NRCELLCU"			jsonb,
	"REL_CD_classifiers_GNBCUCPFUNCTION_PROVIDES_NRCELLCU"			jsonb,
	"REL_CD_decorators_GNBCUCPFUNCTION_PROVIDES_NRCELLCU"			jsonb
);

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRCellCU" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRCellCU" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRCellCU" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRCellCU" ALTER COLUMN "REL_CD_sourceIds_GNBCUCPFUNCTION_PROVIDES_NRCELLCU" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRCellCU" ALTER COLUMN "REL_CD_classifiers_GNBCUCPFUNCTION_PROVIDES_NRCELLCU" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRCellCU" ALTER COLUMN "REL_CD_decorators_GNBCUCPFUNCTION_PROVIDES_NRCELLCU" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-ran_NRCellDU" (
	"id"			TEXT,
	"cellLocalId"			INTEGER,
	"nCI"			BIGINT,
	"nRPCI"			INTEGER,
	"nRTAC"			INTEGER,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_provided-by-gnbduFunction"			TEXT,
	"REL_ID_GNBDUFUNCTION_PROVIDES_NRCELLDU"			TEXT,
	"REL_CD_sourceIds_GNBDUFUNCTION_PROVIDES_NRCELLDU"			jsonb,
	"REL_CD_classifiers_GNBDUFUNCTION_PROVIDES_NRCELLDU"			jsonb,
	"REL_CD_decorators_GNBDUFUNCTION_PROVIDES_NRCELLDU"			jsonb,
	"REL_FK_grouped-by-sector"			TEXT,
	"REL_ID_SECTOR_GROUPS_NRCELLDU"			TEXT,
	"REL_CD_sourceIds_SECTOR_GROUPS_NRCELLDU"			jsonb,
	"REL_CD_classifiers_SECTOR_GROUPS_NRCELLDU"			jsonb,
	"REL_CD_decorators_SECTOR_GROUPS_NRCELLDU"			jsonb
);

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRCellDU" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRCellDU" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRCellDU" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRCellDU" ALTER COLUMN "REL_CD_sourceIds_GNBDUFUNCTION_PROVIDES_NRCELLDU" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRCellDU" ALTER COLUMN "REL_CD_classifiers_GNBDUFUNCTION_PROVIDES_NRCELLDU" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRCellDU" ALTER COLUMN "REL_CD_decorators_GNBDUFUNCTION_PROVIDES_NRCELLDU" SET DEFAULT '{}';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRCellDU" ALTER COLUMN "REL_CD_sourceIds_SECTOR_GROUPS_NRCELLDU" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRCellDU" ALTER COLUMN "REL_CD_classifiers_SECTOR_GROUPS_NRCELLDU" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRCellDU" ALTER COLUMN "REL_CD_decorators_SECTOR_GROUPS_NRCELLDU" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" (
	"id"			TEXT,
	"arfcnDL"			INTEGER,
	"arfcnUL"			INTEGER,
	"bSChannelBwDL"			INTEGER,
	"frequencyDL"			INTEGER,
	"frequencyUL"			INTEGER,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_provided-by-gnbduFunction"			TEXT,
	"REL_ID_GNBDUFUNCTION_PROVIDES_NRSECTORCARRIER"			TEXT,
	"REL_CD_sourceIds_GNBDUFUNCTION_PROVIDES_NRSECTORCARRIER"			jsonb,
	"REL_CD_classifiers_GNBDUFUNCTION_PROVIDES_NRSECTORCARRIER"			jsonb,
	"REL_CD_decorators_GNBDUFUNCTION_PROVIDES_NRSECTORCARRIER"			jsonb,
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

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "REL_CD_sourceIds_GNBDUFUNCTION_PROVIDES_NRSECTORCARRIER" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "REL_CD_classifiers_GNBDUFUNCTION_PROVIDES_NRSECTORCARRIER" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "REL_CD_decorators_GNBDUFUNCTION_PROVIDES_NRSECTORCARRIER" SET DEFAULT '{}';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "REL_CD_sourceIds_NRCELLDU_USES_NRSECTORCARRIER" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "REL_CD_classifiers_NRCELLDU_USES_NRSECTORCARRIER" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "REL_CD_decorators_NRCELLDU_USES_NRSECTORCARRIER" SET DEFAULT '{}';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "REL_CD_sourceIds_NRSECTORCARRIER_USES_ANTENNACAPABILITY" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "REL_CD_classifiers_NRSECTORCARRIER_USES_ANTENNACAPABILITY" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" ALTER COLUMN "REL_CD_decorators_NRSECTORCARRIER_USES_ANTENNACAPABILITY" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-ran_Sector" (
	"id"			TEXT,
	"azimuth"			DECIMAL,
	"geo-location"			jsonb,
	"sectorId"			BIGINT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_Sector" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_Sector" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-ran_Sector" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS ties_data."o-ran-smo-teiv-rel-cloud-ran_NFDEPLOYMENT_SERVES_GNBDUFUNCTION" (
	"id"			TEXT,
	"aSide_NFDeployment"			TEXT,
	"bSide_GNBDUFunction"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-rel-cloud-ran_NFDEPLOYMENT_SERVES_GNBDUFUNCTION" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-rel-cloud-ran_NFDEPLOYMENT_SERVES_GNBDUFUNCTION" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY ties_data."o-ran-smo-teiv-rel-cloud-ran_NFDEPLOYMENT_SERVES_GNBDUFUNCTION" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

SELECT ties_data.create_constraint_if_not_exists(
	'53983B8B2DDA2A5B8CA9EDBDA547333872024418',
 'PK_05C01244A393BDC3392EDBDB231586E6E98CB7E2',
 'ALTER TABLE ties_data."53983B8B2DDA2A5B8CA9EDBDA547333872024418" ADD CONSTRAINT "PK_05C01244A393BDC3392EDBDB231586E6E98CB7E2" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'93A133BC1B3BA1FA430DFB98A4AB1141328425FD',
 'PK_C2321B22B1E252C3EBABC1EBF29DEEFF787BE934',
 'ALTER TABLE ties_data."93A133BC1B3BA1FA430DFB98A4AB1141328425FD" ADD CONSTRAINT "PK_C2321B22B1E252C3EBABC1EBF29DEEFF787BE934" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'CFC235E0404703D1E4454647DF8AAE2C193DB402',
 'PK_63E61CB6802F21FE7A04A80A095F6AF8ABF067CE',
 'ALTER TABLE ties_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" ADD CONSTRAINT "PK_63E61CB6802F21FE7A04A80A095F6AF8ABF067CE" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-cloud_CloudNamespace',
 'PK_o-ran-smo-teiv-cloud_CloudNamespace_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-cloud_CloudNamespace" ADD CONSTRAINT "PK_o-ran-smo-teiv-cloud_CloudNamespace_id" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-cloud_CloudSite',
 'PK_o-ran-smo-teiv-cloud_CloudSite_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-cloud_CloudSite" ADD CONSTRAINT "PK_o-ran-smo-teiv-cloud_CloudSite_id" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-cloud_CloudifiedNF',
 'PK_o-ran-smo-teiv-cloud_CloudifiedNF_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-cloud_CloudifiedNF" ADD CONSTRAINT "PK_o-ran-smo-teiv-cloud_CloudifiedNF_id" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-cloud_NFDEPLOYMENT_DEPLOYED_ON_CLOUDNAMESPACE',
 'PK_7D474DCD64679ECB0278511BA40418F4A8809986',
 'ALTER TABLE ties_data."o-ran-smo-teiv-cloud_NFDEPLOYMENT_DEPLOYED_ON_CLOUDNAMESPACE" ADD CONSTRAINT "PK_7D474DCD64679ECB0278511BA40418F4A8809986" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-cloud_NFDeployment',
 'PK_o-ran-smo-teiv-cloud_NFDeployment_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-cloud_NFDeployment" ADD CONSTRAINT "PK_o-ran-smo-teiv-cloud_NFDeployment_id" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-cloud_NODECLUSTER_LOCATED_AT_CLOUDSITE',
 'PK_o-ran-smo-teiv-cloud_NODECLUSTER_LOCATED_AT_CLOUDSITE_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-cloud_NODECLUSTER_LOCATED_AT_CLOUDSITE" ADD CONSTRAINT "PK_o-ran-smo-teiv-cloud_NODECLUSTER_LOCATED_AT_CLOUDSITE_id" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-cloud_NodeCluster',
 'PK_o-ran-smo-teiv-cloud_NodeCluster_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-cloud_NodeCluster" ADD CONSTRAINT "PK_o-ran-smo-teiv-cloud_NodeCluster_id" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'PK_o-ran-smo-teiv-equipment_AntennaModule_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "PK_o-ran-smo-teiv-equipment_AntennaModule_id" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_Site',
 'PK_o-ran-smo-teiv-equipment_Site_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-equipment_Site" ADD CONSTRAINT "PK_o-ran-smo-teiv-equipment_Site_id" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-oam_ManagedElement',
 'PK_o-ran-smo-teiv-oam_ManagedElement_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-oam_ManagedElement" ADD CONSTRAINT "PK_o-ran-smo-teiv-oam_ManagedElement_id" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_AntennaCapability',
 'PK_o-ran-smo-teiv-ran_AntennaCapability_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_AntennaCapability" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_AntennaCapability_id" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_ENodeBFunction',
 'PK_o-ran-smo-teiv-ran_ENodeBFunction_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_ENodeBFunction" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_ENodeBFunction_id" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_EUtranCell',
 'PK_o-ran-smo-teiv-ran_EUtranCell_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_EUtranCell" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_EUtranCell_id" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_GNBCUCPFunction',
 'PK_o-ran-smo-teiv-ran_GNBCUCPFunction_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_GNBCUCPFunction" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_GNBCUCPFunction_id" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_GNBCUUPFunction',
 'PK_o-ran-smo-teiv-ran_GNBCUUPFunction_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_GNBCUUPFunction" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_GNBCUUPFunction_id" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_GNBDUFunction',
 'PK_o-ran-smo-teiv-ran_GNBDUFunction_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_GNBDUFunction" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_GNBDUFunction_id" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_LTESectorCarrier',
 'PK_o-ran-smo-teiv-ran_LTESectorCarrier_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_LTESectorCarrier_id" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellCU',
 'PK_o-ran-smo-teiv-ran_NRCellCU_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_NRCellCU" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_NRCellCU_id" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellDU',
 'PK_o-ran-smo-teiv-ran_NRCellDU_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_NRCellDU" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_NRCellDU_id" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'PK_o-ran-smo-teiv-ran_NRSectorCarrier_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_NRSectorCarrier_id" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_Sector',
 'PK_o-ran-smo-teiv-ran_Sector_id',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_Sector" ADD CONSTRAINT "PK_o-ran-smo-teiv-ran_Sector_id" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-rel-cloud-ran_NFDEPLOYMENT_SERVES_GNBDUFUNCTION',
 'PK_D659DD37E438F4710181BCC2756AB31D0C21CE54',
 'ALTER TABLE ties_data."o-ran-smo-teiv-rel-cloud-ran_NFDEPLOYMENT_SERVES_GNBDUFUNCTION" ADD CONSTRAINT "PK_D659DD37E438F4710181BCC2756AB31D0C21CE54" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'53983B8B2DDA2A5B8CA9EDBDA547333872024418',
 'FK_C73A4A2E6E78F60F4ACE8EE81F4DCA7C3E2B91DD',
 'ALTER TABLE ties_data."53983B8B2DDA2A5B8CA9EDBDA547333872024418" ADD CONSTRAINT "FK_C73A4A2E6E78F60F4ACE8EE81F4DCA7C3E2B91DD" FOREIGN KEY ("aSide_NFDeployment") REFERENCES ties_data."o-ran-smo-teiv-cloud_NFDeployment" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'53983B8B2DDA2A5B8CA9EDBDA547333872024418',
 'FK_AB8702C7CF7B3B39F3946271F1808220ABC71461',
 'ALTER TABLE ties_data."53983B8B2DDA2A5B8CA9EDBDA547333872024418" ADD CONSTRAINT "FK_AB8702C7CF7B3B39F3946271F1808220ABC71461" FOREIGN KEY ("bSide_GNBCUUPFunction") REFERENCES ties_data."o-ran-smo-teiv-ran_GNBCUUPFunction" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'93A133BC1B3BA1FA430DFB98A4AB1141328425FD',
 'FK_6D0E3426A51B665F58683CD51B16309A4906C069',
 'ALTER TABLE ties_data."93A133BC1B3BA1FA430DFB98A4AB1141328425FD" ADD CONSTRAINT "FK_6D0E3426A51B665F58683CD51B16309A4906C069" FOREIGN KEY ("aSide_NFDeployment") REFERENCES ties_data."o-ran-smo-teiv-cloud_NFDeployment" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'93A133BC1B3BA1FA430DFB98A4AB1141328425FD',
 'FK_D3F50F479BFE3C0EA46395541E5A8319D4C93C28',
 'ALTER TABLE ties_data."93A133BC1B3BA1FA430DFB98A4AB1141328425FD" ADD CONSTRAINT "FK_D3F50F479BFE3C0EA46395541E5A8319D4C93C28" FOREIGN KEY ("bSide_GNBCUCPFunction") REFERENCES ties_data."o-ran-smo-teiv-ran_GNBCUCPFunction" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'CFC235E0404703D1E4454647DF8AAE2C193DB402',
 'FK_D80D1E6B26DF620B4DE659C600A3B7F709A41960',
 'ALTER TABLE ties_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" ADD CONSTRAINT "FK_D80D1E6B26DF620B4DE659C600A3B7F709A41960" FOREIGN KEY ("aSide_AntennaModule") REFERENCES ties_data."o-ran-smo-teiv-equipment_AntennaModule" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'CFC235E0404703D1E4454647DF8AAE2C193DB402',
 'FK_7148BEED02C0617DE1DEEB6639F34A9FA9251B06',
 'ALTER TABLE ties_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" ADD CONSTRAINT "FK_7148BEED02C0617DE1DEEB6639F34A9FA9251B06" FOREIGN KEY ("bSide_AntennaCapability") REFERENCES ties_data."o-ran-smo-teiv-ran_AntennaCapability" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-cloud_CloudNamespace',
 'FK_578C2E899F50A133E194733D63BBEE81D9E0D8F4',
 'ALTER TABLE ties_data."o-ran-smo-teiv-cloud_CloudNamespace" ADD CONSTRAINT "FK_578C2E899F50A133E194733D63BBEE81D9E0D8F4" FOREIGN KEY ("REL_FK_deployed-on-nodeCluster") REFERENCES ties_data."o-ran-smo-teiv-cloud_NodeCluster" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-cloud_CloudNamespace',
 'UNIQUE_1DB2EA3AF6DE7BEEABF5F500FFC397AFD7C74C1C',
 'ALTER TABLE ties_data."o-ran-smo-teiv-cloud_CloudNamespace" ADD CONSTRAINT "UNIQUE_1DB2EA3AF6DE7BEEABF5F500FFC397AFD7C74C1C" UNIQUE ("REL_ID_CLOUDNAMESPACE_DEPLOYED_ON_NODECLUSTER");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-cloud_NFDEPLOYMENT_DEPLOYED_ON_CLOUDNAMESPACE',
 'FK_B747BD330BCD18563818EEBBED49D5CD29D2787B',
 'ALTER TABLE ties_data."o-ran-smo-teiv-cloud_NFDEPLOYMENT_DEPLOYED_ON_CLOUDNAMESPACE" ADD CONSTRAINT "FK_B747BD330BCD18563818EEBBED49D5CD29D2787B" FOREIGN KEY ("aSide_NFDeployment") REFERENCES ties_data."o-ran-smo-teiv-cloud_NFDeployment" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-cloud_NFDEPLOYMENT_DEPLOYED_ON_CLOUDNAMESPACE',
 'FK_C94E3F3ACD4ADCB4153521A6D8EC9D38CCAF7AAA',
 'ALTER TABLE ties_data."o-ran-smo-teiv-cloud_NFDEPLOYMENT_DEPLOYED_ON_CLOUDNAMESPACE" ADD CONSTRAINT "FK_C94E3F3ACD4ADCB4153521A6D8EC9D38CCAF7AAA" FOREIGN KEY ("bSide_CloudNamespace") REFERENCES ties_data."o-ran-smo-teiv-cloud_CloudNamespace" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-cloud_NFDeployment',
 'FK_127C21CB9B8871C3BCACA05A5400BE6B8E7FCAC0',
 'ALTER TABLE ties_data."o-ran-smo-teiv-cloud_NFDeployment" ADD CONSTRAINT "FK_127C21CB9B8871C3BCACA05A5400BE6B8E7FCAC0" FOREIGN KEY ("REL_FK_comprised-by-cloudifiedNF") REFERENCES ties_data."o-ran-smo-teiv-cloud_CloudifiedNF" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-cloud_NFDeployment',
 'UNIQUE_A5A8418B6BE911F281E6E2AA640D7D9F777471DC',
 'ALTER TABLE ties_data."o-ran-smo-teiv-cloud_NFDeployment" ADD CONSTRAINT "UNIQUE_A5A8418B6BE911F281E6E2AA640D7D9F777471DC" UNIQUE ("REL_ID_CLOUDIFIEDNF_COMPRISES_NFDEPLOYMENT");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-cloud_NFDeployment',
 'FK_AC1348E208C2E64F2EB1DECE2CCA5DB10B89CBD9',
 'ALTER TABLE ties_data."o-ran-smo-teiv-cloud_NFDeployment" ADD CONSTRAINT "FK_AC1348E208C2E64F2EB1DECE2CCA5DB10B89CBD9" FOREIGN KEY ("REL_FK_serviced-managedElement") REFERENCES ties_data."o-ran-smo-teiv-oam_ManagedElement" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-cloud_NFDeployment',
 'UNIQUE_8AD46969905BEEB89F63D3F37FD82B14F34FDCBC',
 'ALTER TABLE ties_data."o-ran-smo-teiv-cloud_NFDeployment" ADD CONSTRAINT "UNIQUE_8AD46969905BEEB89F63D3F37FD82B14F34FDCBC" UNIQUE ("REL_ID_NFDEPLOYMENT_SERVES_MANAGEDELEMENT");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-cloud_NODECLUSTER_LOCATED_AT_CLOUDSITE',
 'FK_6BFD8A633C553F178F2730403371E590D669A4E4',
 'ALTER TABLE ties_data."o-ran-smo-teiv-cloud_NODECLUSTER_LOCATED_AT_CLOUDSITE" ADD CONSTRAINT "FK_6BFD8A633C553F178F2730403371E590D669A4E4" FOREIGN KEY ("aSide_NodeCluster") REFERENCES ties_data."o-ran-smo-teiv-cloud_NodeCluster" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-cloud_NODECLUSTER_LOCATED_AT_CLOUDSITE',
 'FK_A80C81E7F1C8CBF32326F589E7582BF533474CB8',
 'ALTER TABLE ties_data."o-ran-smo-teiv-cloud_NODECLUSTER_LOCATED_AT_CLOUDSITE" ADD CONSTRAINT "FK_A80C81E7F1C8CBF32326F589E7582BF533474CB8" FOREIGN KEY ("bSide_CloudSite") REFERENCES ties_data."o-ran-smo-teiv-cloud_CloudSite" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'FK_E3BAEF04443354C0FC1837CF7964E05BEF9FD6CC',
 'ALTER TABLE ties_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "FK_E3BAEF04443354C0FC1837CF7964E05BEF9FD6CC" FOREIGN KEY ("REL_FK_installed-at-site") REFERENCES ties_data."o-ran-smo-teiv-equipment_Site" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'UNIQUE_9DF414C2F0CD7FA8BFCB3E9BF851784AC4BC49B1',
 'ALTER TABLE ties_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "UNIQUE_9DF414C2F0CD7FA8BFCB3E9BF851784AC4BC49B1" UNIQUE ("REL_ID_ANTENNAMODULE_INSTALLED_AT_SITE");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'FK_078764B2F3D613D44CC6E3586F564C83164D2481',
 'ALTER TABLE ties_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "FK_078764B2F3D613D44CC6E3586F564C83164D2481" FOREIGN KEY ("REL_FK_grouped-by-sector") REFERENCES ties_data."o-ran-smo-teiv-ran_Sector" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-equipment_AntennaModule',
 'UNIQUE_78B1D3DCD903AFFB1965D440D87B2D194CA028A0',
 'ALTER TABLE ties_data."o-ran-smo-teiv-equipment_AntennaModule" ADD CONSTRAINT "UNIQUE_78B1D3DCD903AFFB1965D440D87B2D194CA028A0" UNIQUE ("REL_ID_SECTOR_GROUPS_ANTENNAMODULE");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-oam_ManagedElement',
 'FK_899B8130A861D1450FC49D3159D8B29C0628A717',
 'ALTER TABLE ties_data."o-ran-smo-teiv-oam_ManagedElement" ADD CONSTRAINT "FK_899B8130A861D1450FC49D3159D8B29C0628A717" FOREIGN KEY ("REL_FK_deployed-as-cloudifiedNF") REFERENCES ties_data."o-ran-smo-teiv-cloud_CloudifiedNF" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-oam_ManagedElement',
 'UNIQUE_EC9B35192A31C6491E6566602720D1C26E3CB708',
 'ALTER TABLE ties_data."o-ran-smo-teiv-oam_ManagedElement" ADD CONSTRAINT "UNIQUE_EC9B35192A31C6491E6566602720D1C26E3CB708" UNIQUE ("REL_ID_MANAGEDELEMENT_DEPLOYED_AS_CLOUDIFIEDNF");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_ENodeBFunction',
 'FK_6C99B14BF3C9BC6DE2D69AD55DF323ADCB174167',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_ENodeBFunction" ADD CONSTRAINT "FK_6C99B14BF3C9BC6DE2D69AD55DF323ADCB174167" FOREIGN KEY ("REL_FK_managed-by-managedElement") REFERENCES ties_data."o-ran-smo-teiv-oam_ManagedElement" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_ENodeBFunction',
 'UNIQUE_A30444B7D036FA579730F0D2853E52FD08DEDCF0',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_ENodeBFunction" ADD CONSTRAINT "UNIQUE_A30444B7D036FA579730F0D2853E52FD08DEDCF0" UNIQUE ("REL_ID_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_EUtranCell',
 'FK_2D1FA89480BF856AB865D58FAFB6AC0B476015EB',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_EUtranCell" ADD CONSTRAINT "FK_2D1FA89480BF856AB865D58FAFB6AC0B476015EB" FOREIGN KEY ("REL_FK_provided-by-enodebFunction") REFERENCES ties_data."o-ran-smo-teiv-ran_ENodeBFunction" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_EUtranCell',
 'UNIQUE_CA88C7E60C1A332FA7561FC965ED41DD4125CDED',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_EUtranCell" ADD CONSTRAINT "UNIQUE_CA88C7E60C1A332FA7561FC965ED41DD4125CDED" UNIQUE ("REL_ID_ENODEBFUNCTION_PROVIDES_EUTRANCELL");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_EUtranCell',
 'FK_o-ran-smo-teiv-ran_EUtranCell_REL_FK_grouped-by-sector',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_EUtranCell" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_EUtranCell_REL_FK_grouped-by-sector" FOREIGN KEY ("REL_FK_grouped-by-sector") REFERENCES ties_data."o-ran-smo-teiv-ran_Sector" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_EUtranCell',
 'UNIQUE_0513FE4A675A02C31E5EDD6BCB3728911FBDA2FA',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_EUtranCell" ADD CONSTRAINT "UNIQUE_0513FE4A675A02C31E5EDD6BCB3728911FBDA2FA" UNIQUE ("REL_ID_SECTOR_GROUPS_EUTRANCELL");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_GNBCUCPFunction',
 'FK_F1FB8F88851067901B66D53EE1420D2ECCEC98A3',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_GNBCUCPFunction" ADD CONSTRAINT "FK_F1FB8F88851067901B66D53EE1420D2ECCEC98A3" FOREIGN KEY ("REL_FK_managed-by-managedElement") REFERENCES ties_data."o-ran-smo-teiv-oam_ManagedElement" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_GNBCUCPFunction',
 'UNIQUE_50E9E4A87D93AC833B1D1AC05E3B58805909E20E',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_GNBCUCPFunction" ADD CONSTRAINT "UNIQUE_50E9E4A87D93AC833B1D1AC05E3B58805909E20E" UNIQUE ("REL_ID_MANAGEDELEMENT_MANAGES_GNBCUCPFUNCTION");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_GNBCUUPFunction',
 'FK_34D6E2537E8EE1D395CAF5BF9B2182A4696A1EAA',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_GNBCUUPFunction" ADD CONSTRAINT "FK_34D6E2537E8EE1D395CAF5BF9B2182A4696A1EAA" FOREIGN KEY ("REL_FK_managed-by-managedElement") REFERENCES ties_data."o-ran-smo-teiv-oam_ManagedElement" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_GNBCUUPFunction',
 'UNIQUE_0CA05800AC7D277BDCB5CF0097DC35978E9311F4',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_GNBCUUPFunction" ADD CONSTRAINT "UNIQUE_0CA05800AC7D277BDCB5CF0097DC35978E9311F4" UNIQUE ("REL_ID_MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_GNBDUFunction',
 'FK_F67FAF9D3E82B97104E2392DA0AC8A86DF2407CC',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_GNBDUFunction" ADD CONSTRAINT "FK_F67FAF9D3E82B97104E2392DA0AC8A86DF2407CC" FOREIGN KEY ("REL_FK_managed-by-managedElement") REFERENCES ties_data."o-ran-smo-teiv-oam_ManagedElement" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_GNBDUFunction',
 'UNIQUE_5BD09ED226520A0BE27904AEAF0557416808E7E2',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_GNBDUFunction" ADD CONSTRAINT "UNIQUE_5BD09ED226520A0BE27904AEAF0557416808E7E2" UNIQUE ("REL_ID_MANAGEDELEMENT_MANAGES_GNBDUFUNCTION");'
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

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_LTESectorCarrier',
 'FK_96E6D4983CFFDF30FCA20423B5913DEE486E42D0',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD CONSTRAINT "FK_96E6D4983CFFDF30FCA20423B5913DEE486E42D0" FOREIGN KEY ("REL_FK_used-by-euTranCell") REFERENCES ties_data."o-ran-smo-teiv-ran_EUtranCell" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_LTESectorCarrier',
 'UNIQUE_0A76398FBBC8E01A2D3BA602AB47835794E997E5',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD CONSTRAINT "UNIQUE_0A76398FBBC8E01A2D3BA602AB47835794E997E5" UNIQUE ("REL_ID_EUTRANCELL_USES_LTESECTORCARRIER");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_LTESectorCarrier',
 'FK_3D8DF3FBD9C042A888CEB382688C1E8F39D85AFE',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD CONSTRAINT "FK_3D8DF3FBD9C042A888CEB382688C1E8F39D85AFE" FOREIGN KEY ("REL_FK_used-antennaCapability") REFERENCES ties_data."o-ran-smo-teiv-ran_AntennaCapability" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_LTESectorCarrier',
 'UNIQUE_EA18F1D278EAFE834B8A80BCF8A7D8355CD013DF',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" ADD CONSTRAINT "UNIQUE_EA18F1D278EAFE834B8A80BCF8A7D8355CD013DF" UNIQUE ("REL_ID_LTESECTORCARRIER_USES_ANTENNACAPABILITY");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellCU',
 'FK_F2CDD1E84C7F07BF8065F99A5F3488E91E3BB7B2',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_NRCellCU" ADD CONSTRAINT "FK_F2CDD1E84C7F07BF8065F99A5F3488E91E3BB7B2" FOREIGN KEY ("REL_FK_provided-by-gnbcucpFunction") REFERENCES ties_data."o-ran-smo-teiv-ran_GNBCUCPFunction" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellCU',
 'UNIQUE_EA2A6F5BA36ABB0DA357542E05AA2D07415E127A',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_NRCellCU" ADD CONSTRAINT "UNIQUE_EA2A6F5BA36ABB0DA357542E05AA2D07415E127A" UNIQUE ("REL_ID_GNBCUCPFUNCTION_PROVIDES_NRCELLCU");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellDU',
 'FK_o-ran-smo-teiv-ran_NRCellDU_REL_FK_provided-by-gnbduFunction',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_NRCellDU" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_NRCellDU_REL_FK_provided-by-gnbduFunction" FOREIGN KEY ("REL_FK_provided-by-gnbduFunction") REFERENCES ties_data."o-ran-smo-teiv-ran_GNBDUFunction" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellDU',
 'UNIQUE_C3D8E5331EC71D46D4B8CED29FE5F6CEB1D8E67A',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_NRCellDU" ADD CONSTRAINT "UNIQUE_C3D8E5331EC71D46D4B8CED29FE5F6CEB1D8E67A" UNIQUE ("REL_ID_GNBDUFUNCTION_PROVIDES_NRCELLDU");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellDU',
 'FK_o-ran-smo-teiv-ran_NRCellDU_REL_FK_grouped-by-sector',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_NRCellDU" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_NRCellDU_REL_FK_grouped-by-sector" FOREIGN KEY ("REL_FK_grouped-by-sector") REFERENCES ties_data."o-ran-smo-teiv-ran_Sector" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRCellDU',
 'UNIQUE_AC1C114ABED77D6DEC3F3AE3F9EBE8231924AEF4',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_NRCellDU" ADD CONSTRAINT "UNIQUE_AC1C114ABED77D6DEC3F3AE3F9EBE8231924AEF4" UNIQUE ("REL_ID_SECTOR_GROUPS_NRCELLDU");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'FK_F7978366174C82E41F0A6ABF29005FF01603858F',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "FK_F7978366174C82E41F0A6ABF29005FF01603858F" FOREIGN KEY ("REL_FK_provided-by-gnbduFunction") REFERENCES ties_data."o-ran-smo-teiv-ran_GNBDUFunction" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'UNIQUE_0AC16A840F6ACDC50136E71EC6D4F3D4E04B8198',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "UNIQUE_0AC16A840F6ACDC50136E71EC6D4F3D4E04B8198" UNIQUE ("REL_ID_GNBDUFUNCTION_PROVIDES_NRSECTORCARRIER");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'FK_o-ran-smo-teiv-ran_NRSectorCarrier_REL_FK_used-by-nrCellDu',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "FK_o-ran-smo-teiv-ran_NRSectorCarrier_REL_FK_used-by-nrCellDu" FOREIGN KEY ("REL_FK_used-by-nrCellDu") REFERENCES ties_data."o-ran-smo-teiv-ran_NRCellDU" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'UNIQUE_1AB577E5AC207ED4C99A9A96BA1C9C35544AFD25',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "UNIQUE_1AB577E5AC207ED4C99A9A96BA1C9C35544AFD25" UNIQUE ("REL_ID_NRCELLDU_USES_NRSECTORCARRIER");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'FK_65D538D54EB33081C808540235FEB28823428E64',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "FK_65D538D54EB33081C808540235FEB28823428E64" FOREIGN KEY ("REL_FK_used-antennaCapability") REFERENCES ties_data."o-ran-smo-teiv-ran_AntennaCapability" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-ran_NRSectorCarrier',
 'UNIQUE_A799EC9DA6624651081E1DA21B5F0C2D38F6A192',
 'ALTER TABLE ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" ADD CONSTRAINT "UNIQUE_A799EC9DA6624651081E1DA21B5F0C2D38F6A192" UNIQUE ("REL_ID_NRSECTORCARRIER_USES_ANTENNACAPABILITY");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-rel-cloud-ran_NFDEPLOYMENT_SERVES_GNBDUFUNCTION',
 'FK_E6A6C36D4FF5FCFF75D64D84CD124FD88252F735',
 'ALTER TABLE ties_data."o-ran-smo-teiv-rel-cloud-ran_NFDEPLOYMENT_SERVES_GNBDUFUNCTION" ADD CONSTRAINT "FK_E6A6C36D4FF5FCFF75D64D84CD124FD88252F735" FOREIGN KEY ("aSide_NFDeployment") REFERENCES ties_data."o-ran-smo-teiv-cloud_NFDeployment" (id) ON DELETE CASCADE;'
);

SELECT ties_data.create_constraint_if_not_exists(
	'o-ran-smo-teiv-rel-cloud-ran_NFDEPLOYMENT_SERVES_GNBDUFUNCTION',
 'FK_AC409CAE557306A752577462405473B8CD3D93F0',
 'ALTER TABLE ties_data."o-ran-smo-teiv-rel-cloud-ran_NFDEPLOYMENT_SERVES_GNBDUFUNCTION" ADD CONSTRAINT "FK_AC409CAE557306A752577462405473B8CD3D93F0" FOREIGN KEY ("bSide_GNBDUFunction") REFERENCES ties_data."o-ran-smo-teiv-ran_GNBDUFunction" (id) ON DELETE CASCADE;'
);

CREATE INDEX IF NOT EXISTS "IDX_34B07874400148A5C6BEF92D4507DFD69BE31186" ON ties_data."53983B8B2DDA2A5B8CA9EDBDA547333872024418" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_C82B7EDB24DB63FB28B03A886E88225F345FE3D8" ON ties_data."53983B8B2DDA2A5B8CA9EDBDA547333872024418" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_253071B9FA8525A8B2B2D3DBA1A74EBAEF27CE4A" ON ties_data."53983B8B2DDA2A5B8CA9EDBDA547333872024418" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_85054B275ED4BE6CD5B21D0DF728D39CD8F975C2" ON ties_data."93A133BC1B3BA1FA430DFB98A4AB1141328425FD" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_1D6BFC1696A588F59F12697E08471B9EB8A4073D" ON ties_data."93A133BC1B3BA1FA430DFB98A4AB1141328425FD" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_CACDA1786832798355FA450C79FB90001A9DE725" ON ties_data."93A133BC1B3BA1FA430DFB98A4AB1141328425FD" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_E896A9EB22A3F9F96CE75A271475316A98B629C8" ON ties_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_DD0D676834B12CA2F7E8219310998376A08D7F5F" ON ties_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_7BF09D0227840279556AD27ACECB068705893D28" ON ties_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_68E2CF0D41D96AFA209F4FB807E873F942E06160" ON ties_data."o-ran-smo-teiv-cloud_CloudNamespace" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_BAB9B6E93250360ADF354D2A478A9AE0F0CDBA74" ON ties_data."o-ran-smo-teiv-cloud_CloudNamespace" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-cloud_CloudNamespace_CD_decorators" ON ties_data."o-ran-smo-teiv-cloud_CloudNamespace" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_6391A575D92408DA8CBC24C7E1D57B45A1F680AA" ON ties_data."o-ran-smo-teiv-cloud_CloudNamespace" USING GIN (("REL_CD_sourceIds_CLOUDNAMESPACE_DEPLOYED_ON_NODECLUSTER"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_0955C27501E74E6433BB2E0CA6C31CE919A96474" ON ties_data."o-ran-smo-teiv-cloud_CloudNamespace" USING GIN (("REL_CD_classifiers_CLOUDNAMESPACE_DEPLOYED_ON_NODECLUSTER"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_2DD943E7AA20F192D80193C9A05807EA227E4AF5" ON ties_data."o-ran-smo-teiv-cloud_CloudNamespace" USING GIN ("REL_CD_decorators_CLOUDNAMESPACE_DEPLOYED_ON_NODECLUSTER");

CREATE INDEX IF NOT EXISTS "IDX_0A6F89256B698212512EF3AC971B4FFAD26C4598" ON ties_data."o-ran-smo-teiv-cloud_CloudSite" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_AB2EA49D463C5A3F9D9AD3FBCBCBF44416031BED" ON ties_data."o-ran-smo-teiv-cloud_CloudSite" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-cloud_CloudSite_CD_decorators" ON ties_data."o-ran-smo-teiv-cloud_CloudSite" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_9EDB5C47201FC82A4565BFED9EF369D6C6529B19" ON ties_data."o-ran-smo-teiv-cloud_CloudifiedNF" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_BD96130868B69147B2F87B0D15F5829690DEF454" ON ties_data."o-ran-smo-teiv-cloud_CloudifiedNF" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-cloud_CloudifiedNF_CD_decorators" ON ties_data."o-ran-smo-teiv-cloud_CloudifiedNF" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_CA2F2186104EA61BD13152A7BC3B56DB2903483B" ON ties_data."o-ran-smo-teiv-cloud_NFDEPLOYMENT_DEPLOYED_ON_CLOUDNAMESPACE" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_947619E28616D6BC26387291E47CC65C010CE9A4" ON ties_data."o-ran-smo-teiv-cloud_NFDEPLOYMENT_DEPLOYED_ON_CLOUDNAMESPACE" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_A82FEF2ABF913EB8696131AFE99C0267C5DC3CA6" ON ties_data."o-ran-smo-teiv-cloud_NFDEPLOYMENT_DEPLOYED_ON_CLOUDNAMESPACE" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_6433B9B7D69E51E828BDCFCAF59729EDCD10DA60" ON ties_data."o-ran-smo-teiv-cloud_NFDeployment" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_BED5B5FAA75FEE133E27581EAA611B89D20F24E1" ON ties_data."o-ran-smo-teiv-cloud_NFDeployment" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-cloud_NFDeployment_CD_decorators" ON ties_data."o-ran-smo-teiv-cloud_NFDeployment" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_072EB0C094138AB2D90F9CFBDA765B3B464EE86F" ON ties_data."o-ran-smo-teiv-cloud_NFDeployment" USING GIN (("REL_CD_sourceIds_CLOUDIFIEDNF_COMPRISES_NFDEPLOYMENT"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_31F185F0F700C0AE11C5A9B8D28DBF6E37538635" ON ties_data."o-ran-smo-teiv-cloud_NFDeployment" USING GIN (("REL_CD_classifiers_CLOUDIFIEDNF_COMPRISES_NFDEPLOYMENT"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_46CDB369134F042EC021F7496DF721B49A9D43C0" ON ties_data."o-ran-smo-teiv-cloud_NFDeployment" USING GIN ("REL_CD_decorators_CLOUDIFIEDNF_COMPRISES_NFDEPLOYMENT");

CREATE INDEX IF NOT EXISTS "IDX_4DD95BAED8503502101FEB9ECA25DDA8F371816C" ON ties_data."o-ran-smo-teiv-cloud_NFDeployment" USING GIN (("REL_CD_sourceIds_NFDEPLOYMENT_SERVES_MANAGEDELEMENT"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_1EE98ACCAE5537752BD51A3D5F6429585CC543F6" ON ties_data."o-ran-smo-teiv-cloud_NFDeployment" USING GIN (("REL_CD_classifiers_NFDEPLOYMENT_SERVES_MANAGEDELEMENT"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_39A292C3C42B34C2AD7C2A0FD087739C253B06FC" ON ties_data."o-ran-smo-teiv-cloud_NFDeployment" USING GIN ("REL_CD_decorators_NFDEPLOYMENT_SERVES_MANAGEDELEMENT");

CREATE INDEX IF NOT EXISTS "IDX_5040461E772F4F4E71BB2BE590930AC2971C16D1" ON ties_data."o-ran-smo-teiv-cloud_NODECLUSTER_LOCATED_AT_CLOUDSITE" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_B32012D9ED6683AA0AEDF8C0894A5CD8C44B950B" ON ties_data."o-ran-smo-teiv-cloud_NODECLUSTER_LOCATED_AT_CLOUDSITE" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_7310FAE21221EDCDCC254C5CD1BE0D74F6FD9A1F" ON ties_data."o-ran-smo-teiv-cloud_NODECLUSTER_LOCATED_AT_CLOUDSITE" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_DC1829E4241BA7C9B3E5281AC0DF00A766F9452E" ON ties_data."o-ran-smo-teiv-cloud_NodeCluster" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_CB29E8DDA990051B2A3DF193D8E4912F25D5FA0D" ON ties_data."o-ran-smo-teiv-cloud_NodeCluster" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-cloud_NodeCluster_CD_decorators" ON ties_data."o-ran-smo-teiv-cloud_NodeCluster" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_21B0F1FE632B6CB185C49BA6F00224068F443215" ON ties_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN (("antennaBeamWidth"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_905011128A2C218B5352C19ED1FE9851F43EB911" ON ties_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_1C0CAFD80FDD6444044E3F76C7C0A7BDC35F9BC8" ON ties_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-equipment_AntennaModule_CD_decorators" ON ties_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_F497DEC01DA066CB09DA2AA7EDE3F4410078491B" ON ties_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN (("REL_CD_sourceIds_ANTENNAMODULE_INSTALLED_AT_SITE"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_17E417F7EF56809674BE1D5F5154DCCE01E00A96" ON ties_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN (("REL_CD_classifiers_ANTENNAMODULE_INSTALLED_AT_SITE"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_2321BFA482AD2700F41E2BA359F6EB00F47601B9" ON ties_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN ("REL_CD_decorators_ANTENNAMODULE_INSTALLED_AT_SITE");

CREATE INDEX IF NOT EXISTS "IDX_5ABDB19E55A6BDEF33855F14CB1B3B8CF457912C" ON ties_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN (("REL_CD_sourceIds_SECTOR_GROUPS_ANTENNAMODULE"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_83B6347C0C0A005D5E3D856D973D3322DFEDEA35" ON ties_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN (("REL_CD_classifiers_SECTOR_GROUPS_ANTENNAMODULE"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_6C6FBD69F47F41970595A8775DC99CA0F5E894A1" ON ties_data."o-ran-smo-teiv-equipment_AntennaModule" USING GIN ("REL_CD_decorators_SECTOR_GROUPS_ANTENNAMODULE");

CREATE INDEX IF NOT EXISTS "IDX_102A50584376DE25B6BBD7157594C607A5C957F2" ON ties_data."o-ran-smo-teiv-equipment_Site" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_EEBF1BC3344E97988232825777AB13FAB6C4F3F0" ON ties_data."o-ran-smo-teiv-equipment_Site" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-equipment_Site_CD_decorators" ON ties_data."o-ran-smo-teiv-equipment_Site" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_DDD73D6F4004BF3A96AA118281EE3E565A922B47" ON ties_data."o-ran-smo-teiv-oam_ManagedElement" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_98AC4232BC02323E03416954215889CEE874A1E9" ON ties_data."o-ran-smo-teiv-oam_ManagedElement" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-oam_ManagedElement_CD_decorators" ON ties_data."o-ran-smo-teiv-oam_ManagedElement" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_8065626F3F48D4E5A4285654739D3B26499E4C4E" ON ties_data."o-ran-smo-teiv-oam_ManagedElement" USING GIN (("REL_CD_sourceIds_MANAGEDELEMENT_DEPLOYED_AS_CLOUDIFIEDNF"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_634619CF7333EBC0AFDE990900B79220FC626EBA" ON ties_data."o-ran-smo-teiv-oam_ManagedElement" USING GIN (("REL_CD_classifiers_MANAGEDELEMENT_DEPLOYED_AS_CLOUDIFIEDNF"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_F15A070FC83B2E49223B4232E0BEB8931C2B7A4C" ON ties_data."o-ran-smo-teiv-oam_ManagedElement" USING GIN ("REL_CD_decorators_MANAGEDELEMENT_DEPLOYED_AS_CLOUDIFIEDNF");

CREATE INDEX IF NOT EXISTS "IDX_5FB80647AE3E5C0443A792618D65B9090EE2A3FC" ON ties_data."o-ran-smo-teiv-ran_AntennaCapability" USING GIN (("eUtranFqBands"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_A94722FF7B95D8974B494793908B57B4E1A9743B" ON ties_data."o-ran-smo-teiv-ran_AntennaCapability" USING GIN (("geranFqBands"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_441B5C05448D63552C6414BD59C13641D8A4408D" ON ties_data."o-ran-smo-teiv-ran_AntennaCapability" USING GIN (("nRFqBands"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_CC3E208A4EE51D3B505416A599F36F3C99F466C8" ON ties_data."o-ran-smo-teiv-ran_AntennaCapability" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_E7FFE8F4A166AA9A382A0659762FFEC313A9EB5C" ON ties_data."o-ran-smo-teiv-ran_AntennaCapability" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_AntennaCapability_CD_decorators" ON ties_data."o-ran-smo-teiv-ran_AntennaCapability" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_ENodeBFunction_eNodeBPlmnId" ON ties_data."o-ran-smo-teiv-ran_ENodeBFunction" USING GIN ("eNodeBPlmnId");

CREATE INDEX IF NOT EXISTS "IDX_3F7D14B4CF2CA74F28BA1600606E82C6E8C447C0" ON ties_data."o-ran-smo-teiv-ran_ENodeBFunction" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_B598B74193845587BA03553CEDBA058D33956847" ON ties_data."o-ran-smo-teiv-ran_ENodeBFunction" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_ENodeBFunction_CD_decorators" ON ties_data."o-ran-smo-teiv-ran_ENodeBFunction" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_252AF4814C67384A7B05EA116316E83AFF9EB6AE" ON ties_data."o-ran-smo-teiv-ran_ENodeBFunction" USING GIN (("REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_61CDCD3F69CF67EE740358D2C76FA796CFDA19BF" ON ties_data."o-ran-smo-teiv-ran_ENodeBFunction" USING GIN (("REL_CD_classifiers_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_35C17C8A9BA3EF3AEADA72C21F8090C38F575BAF" ON ties_data."o-ran-smo-teiv-ran_ENodeBFunction" USING GIN ("REL_CD_decorators_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION");

CREATE INDEX IF NOT EXISTS "IDX_84E36DC53519D3E334C60B5B02C1AB27130CFA24" ON ties_data."o-ran-smo-teiv-ran_EUtranCell" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_C9C19F8F83F50C130F2EB6502ABB7B2833F1F783" ON ties_data."o-ran-smo-teiv-ran_EUtranCell" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_EUtranCell_CD_decorators" ON ties_data."o-ran-smo-teiv-ran_EUtranCell" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_976F6A0F8991F64592B6F9E716EFEECBD5400FDA" ON ties_data."o-ran-smo-teiv-ran_EUtranCell" USING GIN (("REL_CD_sourceIds_ENODEBFUNCTION_PROVIDES_EUTRANCELL"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_4C77E3A51BFAB2FCD30425E4EB21CC7636438299" ON ties_data."o-ran-smo-teiv-ran_EUtranCell" USING GIN (("REL_CD_classifiers_ENODEBFUNCTION_PROVIDES_EUTRANCELL"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_FC7D79187227E0BFA69149048CC10E39AE540B8A" ON ties_data."o-ran-smo-teiv-ran_EUtranCell" USING GIN ("REL_CD_decorators_ENODEBFUNCTION_PROVIDES_EUTRANCELL");

CREATE INDEX IF NOT EXISTS "IDX_173887418DD4FD6FD592F6404EA784150B1822C0" ON ties_data."o-ran-smo-teiv-ran_EUtranCell" USING GIN (("REL_CD_sourceIds_SECTOR_GROUPS_EUTRANCELL"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_8A15E61498725DA9D8C78FC4B99053C06E88DCEC" ON ties_data."o-ran-smo-teiv-ran_EUtranCell" USING GIN (("REL_CD_classifiers_SECTOR_GROUPS_EUTRANCELL"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_E4EF3C904939ED4C0996EAB7CDFE1895CDF34BFB" ON ties_data."o-ran-smo-teiv-ran_EUtranCell" USING GIN ("REL_CD_decorators_SECTOR_GROUPS_EUTRANCELL");

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_GNBCUCPFunction_pLMNId" ON ties_data."o-ran-smo-teiv-ran_GNBCUCPFunction" USING GIN ("pLMNId");

CREATE INDEX IF NOT EXISTS "IDX_BE4B476041D559760931630000D3F4A6DFF42707" ON ties_data."o-ran-smo-teiv-ran_GNBCUCPFunction" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_588840BAE32C7FF8CF0553F631DAAF8BB6E8E7C1" ON ties_data."o-ran-smo-teiv-ran_GNBCUCPFunction" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_GNBCUCPFunction_CD_decorators" ON ties_data."o-ran-smo-teiv-ran_GNBCUCPFunction" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_E01081465B87F46E1CC7A22FE406C7B41C817E8C" ON ties_data."o-ran-smo-teiv-ran_GNBCUCPFunction" USING GIN (("REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_GNBCUCPFUNCTION"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_1324070754C1EBF8EA78EF40743AFC1713733BA8" ON ties_data."o-ran-smo-teiv-ran_GNBCUCPFunction" USING GIN (("REL_CD_classifiers_MANAGEDELEMENT_MANAGES_GNBCUCPFUNCTION"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_13E734DE57346378DA4F21FC4EA030290A7E532F" ON ties_data."o-ran-smo-teiv-ran_GNBCUCPFunction" USING GIN ("REL_CD_decorators_MANAGEDELEMENT_MANAGES_GNBCUCPFUNCTION");

CREATE INDEX IF NOT EXISTS "IDX_C6D2419F8DC299FBC98342AA00BE92308C7566A7" ON ties_data."o-ran-smo-teiv-ran_GNBCUUPFunction" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_4C2B68358221A7FF0E68012DEDD3CBA2C4ED669F" ON ties_data."o-ran-smo-teiv-ran_GNBCUUPFunction" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_GNBCUUPFunction_CD_decorators" ON ties_data."o-ran-smo-teiv-ran_GNBCUUPFunction" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_883506CAA3E742D82EEFCEE8C8F29927983B73B1" ON ties_data."o-ran-smo-teiv-ran_GNBCUUPFunction" USING GIN (("REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_EB07ADD66F6CF51B9330403DE4500D05CA067647" ON ties_data."o-ran-smo-teiv-ran_GNBCUUPFunction" USING GIN (("REL_CD_classifiers_MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_C3141DD7D2695EF74B13981AB378A58390D203D6" ON ties_data."o-ran-smo-teiv-ran_GNBCUUPFunction" USING GIN ("REL_CD_decorators_MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION");

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_GNBDUFunction_dUpLMNId" ON ties_data."o-ran-smo-teiv-ran_GNBDUFunction" USING GIN ("dUpLMNId");

CREATE INDEX IF NOT EXISTS "IDX_2BEF269CED354C2454AC2B2EABB134AC267A0C62" ON ties_data."o-ran-smo-teiv-ran_GNBDUFunction" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_601A4514FFACA8985471531013AFC8F760361F09" ON ties_data."o-ran-smo-teiv-ran_GNBDUFunction" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_GNBDUFunction_CD_decorators" ON ties_data."o-ran-smo-teiv-ran_GNBDUFunction" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_3065F7FB78C5AA9FF17972F825F89AED127A6324" ON ties_data."o-ran-smo-teiv-ran_GNBDUFunction" USING GIN (("REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_GNBDUFUNCTION"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_04C614FDE6A4AE2AA106A1233D1DF95803FC122D" ON ties_data."o-ran-smo-teiv-ran_GNBDUFunction" USING GIN (("REL_CD_classifiers_MANAGEDELEMENT_MANAGES_GNBDUFUNCTION"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_298FCD184347DEC995B06FED2B1AE61F12BF766A" ON ties_data."o-ran-smo-teiv-ran_GNBDUFunction" USING GIN ("REL_CD_decorators_MANAGEDELEMENT_MANAGES_GNBDUFUNCTION");

CREATE INDEX IF NOT EXISTS "IDX_6EC539C61EA7078DBA264C9877B87FC263605D42" ON ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_E754EB8AD825DB3111B07B9E5DA3B30C38DB406B" ON ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_LTESectorCarrier_CD_decorators" ON ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_1EBC7271CEA658156DE25286404CBC4593340F8E" ON ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN (("REL_CD_sourceIds_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_846B7740E8AA756B8C1409CD909D2DF73A47ED4C" ON ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN (("REL_CD_classifiers_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_44075E1D464599B61924196C20F2B88332520CD8" ON ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN ("REL_CD_decorators_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER");

CREATE INDEX IF NOT EXISTS "IDX_F2D46817C2D618D8C33945F282299BF9EB49465E" ON ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN (("REL_CD_sourceIds_EUTRANCELL_USES_LTESECTORCARRIER"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_B291D7EFCAD3BF06A2C11F8C0429ABABEEF8308B" ON ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN (("REL_CD_classifiers_EUTRANCELL_USES_LTESECTORCARRIER"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_EAE482189F45D63CD1A88B0DD5F76EEE163D9E53" ON ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN ("REL_CD_decorators_EUTRANCELL_USES_LTESECTORCARRIER");

CREATE INDEX IF NOT EXISTS "IDX_7D01A5D21C990ACCBE65035C062C7D881A05F1EE" ON ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN (("REL_CD_sourceIds_LTESECTORCARRIER_USES_ANTENNACAPABILITY"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_3D3EFECFB917DAC074F56334224B19F8FD6BF8A5" ON ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN (("REL_CD_classifiers_LTESECTORCARRIER_USES_ANTENNACAPABILITY"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_4EE2AA643311DFCC13B6ED832EBE2FAB4CFDF494" ON ties_data."o-ran-smo-teiv-ran_LTESectorCarrier" USING GIN ("REL_CD_decorators_LTESECTORCARRIER_USES_ANTENNACAPABILITY");

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_NRCellCU_plmnId" ON ties_data."o-ran-smo-teiv-ran_NRCellCU" USING GIN ("plmnId");

CREATE INDEX IF NOT EXISTS "IDX_0C443A16285D233F16966C2F0314CDC9D0F6D0B8" ON ties_data."o-ran-smo-teiv-ran_NRCellCU" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_E5930226819982DC0CFC1FA64FB3600647222435" ON ties_data."o-ran-smo-teiv-ran_NRCellCU" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_NRCellCU_CD_decorators" ON ties_data."o-ran-smo-teiv-ran_NRCellCU" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_36A671754CD510FFBDC2713FD142303DCA75DD65" ON ties_data."o-ran-smo-teiv-ran_NRCellCU" USING GIN (("REL_CD_sourceIds_GNBCUCPFUNCTION_PROVIDES_NRCELLCU"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_04BE1EB39848069422B97C28EE3C8ED18BCC6D33" ON ties_data."o-ran-smo-teiv-ran_NRCellCU" USING GIN (("REL_CD_classifiers_GNBCUCPFUNCTION_PROVIDES_NRCELLCU"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_229957181BBC9D7B4535807BB397E8AA1378ED85" ON ties_data."o-ran-smo-teiv-ran_NRCellCU" USING GIN ("REL_CD_decorators_GNBCUCPFUNCTION_PROVIDES_NRCELLCU");

CREATE INDEX IF NOT EXISTS "IDX_FFD60DD99D80C276F402E66546F5DACB2D81EE26" ON ties_data."o-ran-smo-teiv-ran_NRCellDU" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_C437D39632DC79BAB6AC4F0880826A05425F9C32" ON ties_data."o-ran-smo-teiv-ran_NRCellDU" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_NRCellDU_CD_decorators" ON ties_data."o-ran-smo-teiv-ran_NRCellDU" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_1F6708B1E34FC908473DD7A7E5641E650B359BEF" ON ties_data."o-ran-smo-teiv-ran_NRCellDU" USING GIN (("REL_CD_sourceIds_GNBDUFUNCTION_PROVIDES_NRCELLDU"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_2F4C43ED084968FDAF9943DB96741885C145FE1D" ON ties_data."o-ran-smo-teiv-ran_NRCellDU" USING GIN (("REL_CD_classifiers_GNBDUFUNCTION_PROVIDES_NRCELLDU"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_0E63D6B76B229961CD45D998C63175B569DDECD1" ON ties_data."o-ran-smo-teiv-ran_NRCellDU" USING GIN ("REL_CD_decorators_GNBDUFUNCTION_PROVIDES_NRCELLDU");

CREATE INDEX IF NOT EXISTS "IDX_6325926B4D2FDD1FBBB34250DABEA5E7229FF9F5" ON ties_data."o-ran-smo-teiv-ran_NRCellDU" USING GIN (("REL_CD_sourceIds_SECTOR_GROUPS_NRCELLDU"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_7CB4A7724F68D1CB2D12E8DE779BA9103F7DBE0A" ON ties_data."o-ran-smo-teiv-ran_NRCellDU" USING GIN (("REL_CD_classifiers_SECTOR_GROUPS_NRCELLDU"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_0A03C47C13AD3B5C84D3D8081493D670E9CBDCD1" ON ties_data."o-ran-smo-teiv-ran_NRCellDU" USING GIN ("REL_CD_decorators_SECTOR_GROUPS_NRCELLDU");

CREATE INDEX IF NOT EXISTS "IDX_8E34EC0B1DE7DDCE3B32ADD85B11E15F95C5644E" ON ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_050A80BEEF775E4D3CE216F282F23DB99DA2D798" ON ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_NRSectorCarrier_CD_decorators" ON ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_CD293AD1111E344D150340A13BD299924D29A9DA" ON ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN (("REL_CD_sourceIds_GNBDUFUNCTION_PROVIDES_NRSECTORCARRIER"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_273D43FDDD1C4643ECF8BBE51B6B369C657F0861" ON ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN (("REL_CD_classifiers_GNBDUFUNCTION_PROVIDES_NRSECTORCARRIER"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_EDE8B88F488F9380DB49CB2C141318FB33C2CCEC" ON ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN ("REL_CD_decorators_GNBDUFUNCTION_PROVIDES_NRSECTORCARRIER");

CREATE INDEX IF NOT EXISTS "IDX_7BFD17A71AB1B7765FE6431DA4E66C2EDE88AC3B" ON ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN (("REL_CD_sourceIds_NRCELLDU_USES_NRSECTORCARRIER"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_ED50A5139F1449DBAD8DA10D45F5A5BF819EACBA" ON ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN (("REL_CD_classifiers_NRCELLDU_USES_NRSECTORCARRIER"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_2ADB5C6DCAEE8811FB1CA8FD9EB53381F35FCB70" ON ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN ("REL_CD_decorators_NRCELLDU_USES_NRSECTORCARRIER");

CREATE INDEX IF NOT EXISTS "IDX_1F27C515A028616FAC422A02ABBEC402D5DBB2E5" ON ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN (("REL_CD_sourceIds_NRSECTORCARRIER_USES_ANTENNACAPABILITY"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_B975D24291849007D4AA6686C5D3983885D5C884" ON ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN (("REL_CD_classifiers_NRSECTORCARRIER_USES_ANTENNACAPABILITY"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_902B73F741160B9D4FBF62406D3D9ABBECAD8BE7" ON ties_data."o-ran-smo-teiv-ran_NRSectorCarrier" USING GIN ("REL_CD_decorators_NRSECTORCARRIER_USES_ANTENNACAPABILITY");

CREATE INDEX IF NOT EXISTS "IDX_E234B43A7CD7843672F08F2197AB46A2A50BECB0" ON ties_data."o-ran-smo-teiv-ran_Sector" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_19C19556F9714850389595E0A16218FA229205FE" ON ties_data."o-ran-smo-teiv-ran_Sector" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_GIN_o-ran-smo-teiv-ran_Sector_CD_decorators" ON ties_data."o-ran-smo-teiv-ran_Sector" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_5A45CC516788E441D6D4C5CCC6A1EB50C36F0C5C" ON ties_data."o-ran-smo-teiv-rel-cloud-ran_NFDEPLOYMENT_SERVES_GNBDUFUNCTION" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_0129685EC2D748D2AD9EC0AF4FA2AB6242CD3CBF" ON ties_data."o-ran-smo-teiv-rel-cloud-ran_NFDEPLOYMENT_SERVES_GNBDUFUNCTION" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_5741692EDE13E86FD8BFFAD47847257CC0925F6C" ON ties_data."o-ran-smo-teiv-rel-cloud-ran_NFDEPLOYMENT_SERVES_GNBDUFUNCTION" USING GIN ("CD_decorators");

ANALYZE ties_data."o-ran-smo-teiv-ran_GNBDUFunction";

ANALYZE ties_data."o-ran-smo-teiv-ran_GNBCUCPFunction";

ANALYZE ties_data."o-ran-smo-teiv-equipment_Site";

ANALYZE ties_data."o-ran-smo-teiv-cloud_NodeCluster";

ANALYZE ties_data."o-ran-smo-teiv-cloud_NODECLUSTER_LOCATED_AT_CLOUDSITE";

ANALYZE ties_data."o-ran-smo-teiv-cloud_CloudNamespace";

ANALYZE ties_data."o-ran-smo-teiv-oam_ManagedElement";

ANALYZE ties_data."o-ran-smo-teiv-ran_NRCellDU";

ANALYZE ties_data."o-ran-smo-teiv-cloud_NFDEPLOYMENT_DEPLOYED_ON_CLOUDNAMESPACE";

ANALYZE ties_data."o-ran-smo-teiv-ran_GNBCUUPFunction";

ANALYZE ties_data."o-ran-smo-teiv-cloud_CloudifiedNF";

ANALYZE ties_data."o-ran-smo-teiv-cloud_CloudSite";

ANALYZE ties_data."o-ran-smo-teiv-cloud_NFDeployment";

ANALYZE ties_data."93A133BC1B3BA1FA430DFB98A4AB1141328425FD";

ANALYZE ties_data."o-ran-smo-teiv-ran_AntennaCapability";

ANALYZE ties_data."53983B8B2DDA2A5B8CA9EDBDA547333872024418";

ANALYZE ties_data."o-ran-smo-teiv-equipment_AntennaModule";

ANALYZE ties_data."o-ran-smo-teiv-ran_EUtranCell";

ANALYZE ties_data."o-ran-smo-teiv-ran_LTESectorCarrier";

ANALYZE ties_data."o-ran-smo-teiv-ran_NRCellCU";

ANALYZE ties_data."CFC235E0404703D1E4454647DF8AAE2C193DB402";

ANALYZE ties_data."o-ran-smo-teiv-ran_NRSectorCarrier";

ANALYZE ties_data."o-ran-smo-teiv-ran_ENodeBFunction";

ANALYZE ties_data."o-ran-smo-teiv-ran_Sector";

ANALYZE ties_data."o-ran-smo-teiv-rel-cloud-ran_NFDEPLOYMENT_SERVES_GNBDUFUNCTION";

COMMIT;
