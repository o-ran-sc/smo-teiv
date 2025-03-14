--
-- ============LICENSE_START=======================================================
-- Modifications Copyright (C) 2025 OpenInfra Foundation Europe
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

INSERT INTO teiv_data."responsible_adapter" (id, hashed_id) VALUES(:'adapterId', decode(:'adapterHashedId', 'hex')) ON CONFLICT DO NOTHING ;

UPDATE teiv_data."o-ran-smo-teiv-ran_ODUFunction" SET "metadata"='{"reliabilityIndicator":"OK"}' WHERE "metadata" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_ODUFunction" SET "REL_metadata_MANAGEDELEMENT_MANAGES_ODUFUNCTION"='{"reliabilityIndicator":"OK"}' WHERE "REL_metadata_MANAGEDELEMENT_MANAGES_ODUFUNCTION" IS NULL AND "REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" SET "metadata"='{"reliabilityIndicator":"OK"}' WHERE "metadata" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" SET "REL_metadata_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION"='{"reliabilityIndicator":"OK"}' WHERE "REL_metadata_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION" IS NULL AND "REL_ID_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-equipment_Site" SET "metadata"='{"reliabilityIndicator":"OK"}' WHERE "metadata" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_AntennaCapability" SET "metadata"='{"reliabilityIndicator":"OK"}' WHERE "metadata" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" SET "metadata"='{"reliabilityIndicator":"OK"}' WHERE "metadata" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" SET "REL_metadata_ANTENNAMODULE_INSTALLED_AT_SITE"='{"reliabilityIndicator":"OK"}' WHERE "REL_metadata_ANTENNAMODULE_INSTALLED_AT_SITE" IS NULL AND "REL_ID_ANTENNAMODULE_INSTALLED_AT_SITE" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" SET "REL_metadata_SECTOR_GROUPS_ANTENNAMODULE"='{"reliabilityIndicator":"OK"}' WHERE "REL_metadata_SECTOR_GROUPS_ANTENNAMODULE" IS NULL AND "REL_ID_SECTOR_GROUPS_ANTENNAMODULE" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_NRCellCU" SET "metadata"='{"reliabilityIndicator":"OK"}' WHERE "metadata" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_NRCellCU" SET "REL_metadata_OCUCPFUNCTION_PROVIDES_NRCELLCU"='{"reliabilityIndicator":"OK"}' WHERE "REL_metadata_OCUCPFUNCTION_PROVIDES_NRCELLCU" IS NULL AND "REL_ID_OCUCPFUNCTION_PROVIDES_NRCELLCU" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-oam_ManagedElement" SET "metadata"='{"reliabilityIndicator":"OK"}' WHERE "metadata" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_NRCellDU" SET "metadata"='{"reliabilityIndicator":"OK"}' WHERE "metadata" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_NRCellDU" SET "REL_metadata_ODUFUNCTION_PROVIDES_NRCELLDU"='{"reliabilityIndicator":"OK"}' WHERE "REL_metadata_ODUFUNCTION_PROVIDES_NRCELLDU" IS NULL AND "REL_ID_ODUFUNCTION_PROVIDES_NRCELLDU" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_NRCellDU" SET "REL_metadata_SECTOR_GROUPS_NRCELLDU"='{"reliabilityIndicator":"OK"}' WHERE "REL_metadata_SECTOR_GROUPS_NRCELLDU" IS NULL AND "REL_ID_SECTOR_GROUPS_NRCELLDU" IS NOT NULL;

UPDATE teiv_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" SET "metadata"='{"reliabilityIndicator":"OK"}' WHERE "metadata" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" SET "metadata"='{"reliabilityIndicator":"OK"}' WHERE "metadata" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" SET "REL_metadata_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION"='{"reliabilityIndicator":"OK"}' WHERE "REL_metadata_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION" IS NULL AND "REL_ID_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU" SET "metadata"='{"reliabilityIndicator":"OK"}' WHERE "metadata" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" SET "metadata"='{"reliabilityIndicator":"OK"}' WHERE "metadata" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" SET "REL_metadata_ODUFUNCTION_PROVIDES_NRSECTORCARRIER"='{"reliabilityIndicator":"OK"}' WHERE "REL_metadata_ODUFUNCTION_PROVIDES_NRSECTORCARRIER" IS NULL AND "REL_ID_ODUFUNCTION_PROVIDES_NRSECTORCARRIER" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" SET "REL_metadata_NRCELLDU_USES_NRSECTORCARRIER"='{"reliabilityIndicator":"OK"}' WHERE "REL_metadata_NRCELLDU_USES_NRSECTORCARRIER" IS NULL AND "REL_ID_NRCELLDU_USES_NRSECTORCARRIER" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" SET "REL_metadata_NRSECTORCARRIER_USES_ANTENNACAPABILITY"='{"reliabilityIndicator":"OK"}' WHERE "REL_metadata_NRSECTORCARRIER_USES_ANTENNACAPABILITY" IS NULL AND "REL_ID_NRSECTORCARRIER_USES_ANTENNACAPABILITY" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_Sector" SET "metadata"='{"reliabilityIndicator":"OK"}' WHERE "metadata" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."1D71FC3BAE50E72552EAAC17B3B0C959B403E822" SET "metadata"='{"reliabilityIndicator":"OK"}' WHERE "metadata" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_ODUFunction" SET "RESP_id"= decode(:'adapterHashedId', 'hex') WHERE "RESP_id" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_ODUFunction" SET "REL_RESP_id_MANAGEDELEMENT_MANAGES_ODUFUNCTION"= decode(:'adapterHashedId', 'hex') WHERE "REL_RESP_id_MANAGEDELEMENT_MANAGES_ODUFUNCTION" IS NULL AND "REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" SET "RESP_id"= decode(:'adapterHashedId', 'hex') WHERE "RESP_id" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" SET "REL_RESP_id_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION"= decode(:'adapterHashedId', 'hex') WHERE "REL_RESP_id_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION" IS NULL AND "REL_ID_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-equipment_Site" SET "RESP_id"= decode(:'adapterHashedId', 'hex') WHERE "RESP_id" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_AntennaCapability" SET "RESP_id"= decode(:'adapterHashedId', 'hex') WHERE "RESP_id" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" SET "RESP_id"= decode(:'adapterHashedId', 'hex') WHERE "RESP_id" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" SET "REL_RESP_id_ANTENNAMODULE_INSTALLED_AT_SITE"= decode(:'adapterHashedId', 'hex') WHERE "REL_RESP_id_ANTENNAMODULE_INSTALLED_AT_SITE" IS NULL AND "REL_ID_ANTENNAMODULE_INSTALLED_AT_SITE" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" SET "REL_RESP_id_SECTOR_GROUPS_ANTENNAMODULE"= decode(:'adapterHashedId', 'hex') WHERE "REL_RESP_id_SECTOR_GROUPS_ANTENNAMODULE" IS NULL AND "REL_ID_SECTOR_GROUPS_ANTENNAMODULE" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_NRCellCU" SET "RESP_id"= decode(:'adapterHashedId', 'hex') WHERE "RESP_id" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_NRCellCU" SET "REL_RESP_id_OCUCPFUNCTION_PROVIDES_NRCELLCU"= decode(:'adapterHashedId', 'hex') WHERE "REL_RESP_id_OCUCPFUNCTION_PROVIDES_NRCELLCU" IS NULL AND "REL_ID_OCUCPFUNCTION_PROVIDES_NRCELLCU" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-oam_ManagedElement" SET "RESP_id"= decode(:'adapterHashedId', 'hex') WHERE "RESP_id" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_NRCellDU" SET "RESP_id"= decode(:'adapterHashedId', 'hex') WHERE "RESP_id" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_NRCellDU" SET "REL_RESP_id_ODUFUNCTION_PROVIDES_NRCELLDU"= decode(:'adapterHashedId', 'hex') WHERE "REL_RESP_id_ODUFUNCTION_PROVIDES_NRCELLDU" IS NULL AND "REL_ID_ODUFUNCTION_PROVIDES_NRCELLDU" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_NRCellDU" SET "REL_RESP_id_SECTOR_GROUPS_NRCELLDU"= decode(:'adapterHashedId', 'hex') WHERE "REL_RESP_id_SECTOR_GROUPS_NRCELLDU" IS NULL AND "REL_ID_SECTOR_GROUPS_NRCELLDU" IS NOT NULL;

UPDATE teiv_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" SET "RESP_id"= decode(:'adapterHashedId', 'hex') WHERE "RESP_id" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" SET "RESP_id"= decode(:'adapterHashedId', 'hex') WHERE "RESP_id" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" SET "REL_RESP_id_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION"= decode(:'adapterHashedId', 'hex') WHERE "REL_RESP_id_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION" IS NULL AND "REL_ID_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU" SET "RESP_id"= decode(:'adapterHashedId', 'hex') WHERE "RESP_id" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" SET "RESP_id"= decode(:'adapterHashedId', 'hex') WHERE "RESP_id" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" SET "REL_RESP_id_ODUFUNCTION_PROVIDES_NRSECTORCARRIER"= decode(:'adapterHashedId', 'hex') WHERE "REL_RESP_id_ODUFUNCTION_PROVIDES_NRSECTORCARRIER" IS NULL AND "REL_ID_ODUFUNCTION_PROVIDES_NRSECTORCARRIER" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" SET "REL_RESP_id_NRCELLDU_USES_NRSECTORCARRIER"= decode(:'adapterHashedId', 'hex') WHERE "REL_RESP_id_NRCELLDU_USES_NRSECTORCARRIER" IS NULL AND "REL_ID_NRCELLDU_USES_NRSECTORCARRIER" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" SET "REL_RESP_id_NRSECTORCARRIER_USES_ANTENNACAPABILITY"= decode(:'adapterHashedId', 'hex') WHERE "REL_RESP_id_NRSECTORCARRIER_USES_ANTENNACAPABILITY" IS NULL AND "REL_ID_NRSECTORCARRIER_USES_ANTENNACAPABILITY" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_Sector" SET "RESP_id"= decode(:'adapterHashedId', 'hex') WHERE "RESP_id" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."1D71FC3BAE50E72552EAAC17B3B0C959B403E822" SET "RESP_id"= decode(:'adapterHashedId', 'hex') WHERE "RESP_id" IS NULL AND "id" IS NOT NULL;

UPDATE teiv_data."o-ran-smo-teiv-ran_ODUFunction" SET "metadata" = COALESCE("metadata", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "id" IS NOT NULL AND NOT "metadata" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."o-ran-smo-teiv-ran_ODUFunction" SET "REL_metadata_MANAGEDELEMENT_MANAGES_ODUFUNCTION" = COALESCE("REL_metadata_MANAGEDELEMENT_MANAGES_ODUFUNCTION", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION" IS NOT NULL AND NOT "REL_metadata_MANAGEDELEMENT_MANAGES_ODUFUNCTION" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" SET "metadata" = COALESCE("metadata", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "id" IS NOT NULL AND NOT "metadata" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" SET "REL_metadata_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION" = COALESCE("REL_metadata_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "REL_ID_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION" IS NOT NULL AND NOT "REL_metadata_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."o-ran-smo-teiv-equipment_Site" SET "metadata" = COALESCE("metadata", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "id" IS NOT NULL AND NOT "metadata" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."o-ran-smo-teiv-ran_AntennaCapability" SET "metadata" = COALESCE("metadata", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "id" IS NOT NULL AND NOT "metadata" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" SET "metadata" = COALESCE("metadata", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "id" IS NOT NULL AND NOT "metadata" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" SET "REL_metadata_ANTENNAMODULE_INSTALLED_AT_SITE" = COALESCE("REL_metadata_ANTENNAMODULE_INSTALLED_AT_SITE", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "REL_ID_ANTENNAMODULE_INSTALLED_AT_SITE" IS NOT NULL AND NOT "REL_metadata_ANTENNAMODULE_INSTALLED_AT_SITE" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."o-ran-smo-teiv-equipment_AntennaModule" SET "REL_metadata_SECTOR_GROUPS_ANTENNAMODULE" = COALESCE("REL_metadata_SECTOR_GROUPS_ANTENNAMODULE", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "REL_ID_SECTOR_GROUPS_ANTENNAMODULE" IS NOT NULL AND NOT "REL_metadata_SECTOR_GROUPS_ANTENNAMODULE" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."o-ran-smo-teiv-ran_NRCellCU" SET "metadata" = COALESCE("metadata", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "id" IS NOT NULL AND NOT "metadata" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."o-ran-smo-teiv-ran_NRCellCU" SET "REL_metadata_OCUCPFUNCTION_PROVIDES_NRCELLCU" = COALESCE("REL_metadata_OCUCPFUNCTION_PROVIDES_NRCELLCU", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "REL_ID_OCUCPFUNCTION_PROVIDES_NRCELLCU" IS NOT NULL AND NOT "REL_metadata_OCUCPFUNCTION_PROVIDES_NRCELLCU" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."o-ran-smo-teiv-oam_ManagedElement" SET "metadata" = COALESCE("metadata", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "id" IS NOT NULL AND NOT "metadata" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."o-ran-smo-teiv-ran_NRCellDU" SET "metadata" = COALESCE("metadata", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "id" IS NOT NULL AND NOT "metadata" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."o-ran-smo-teiv-ran_NRCellDU" SET "REL_metadata_ODUFUNCTION_PROVIDES_NRCELLDU" = COALESCE("REL_metadata_ODUFUNCTION_PROVIDES_NRCELLDU", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "REL_ID_ODUFUNCTION_PROVIDES_NRCELLDU" IS NOT NULL AND NOT "REL_metadata_ODUFUNCTION_PROVIDES_NRCELLDU" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."o-ran-smo-teiv-ran_NRCellDU" SET "REL_metadata_SECTOR_GROUPS_NRCELLDU" = COALESCE("REL_metadata_SECTOR_GROUPS_NRCELLDU", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "REL_ID_SECTOR_GROUPS_NRCELLDU" IS NOT NULL AND NOT "REL_metadata_SECTOR_GROUPS_NRCELLDU" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" SET "metadata" = COALESCE("metadata", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "id" IS NOT NULL AND NOT "metadata" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" SET "metadata" = COALESCE("metadata", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "id" IS NOT NULL AND NOT "metadata" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" SET "REL_metadata_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION" = COALESCE("REL_metadata_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "REL_ID_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION" IS NOT NULL AND NOT "REL_metadata_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU" SET "metadata" = COALESCE("metadata", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "id" IS NOT NULL AND NOT "metadata" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" SET "metadata" = COALESCE("metadata", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "id" IS NOT NULL AND NOT "metadata" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" SET "REL_metadata_ODUFUNCTION_PROVIDES_NRSECTORCARRIER" = COALESCE("REL_metadata_ODUFUNCTION_PROVIDES_NRSECTORCARRIER", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "REL_ID_ODUFUNCTION_PROVIDES_NRSECTORCARRIER" IS NOT NULL AND NOT "REL_metadata_ODUFUNCTION_PROVIDES_NRSECTORCARRIER" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" SET "REL_metadata_NRCELLDU_USES_NRSECTORCARRIER" = COALESCE("REL_metadata_NRCELLDU_USES_NRSECTORCARRIER", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "REL_ID_NRCELLDU_USES_NRSECTORCARRIER" IS NOT NULL AND NOT "REL_metadata_NRCELLDU_USES_NRSECTORCARRIER" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" SET "REL_metadata_NRSECTORCARRIER_USES_ANTENNACAPABILITY" = COALESCE("REL_metadata_NRSECTORCARRIER_USES_ANTENNACAPABILITY", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "REL_ID_NRSECTORCARRIER_USES_ANTENNACAPABILITY" IS NOT NULL AND NOT "REL_metadata_NRSECTORCARRIER_USES_ANTENNACAPABILITY" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."o-ran-smo-teiv-ran_Sector" SET "metadata" = COALESCE("metadata", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "id" IS NOT NULL AND NOT "metadata" ?& array['firstDiscovered','lastModified'];

UPDATE teiv_data."1D71FC3BAE50E72552EAAC17B3B0C959B403E822" SET "metadata" = COALESCE("metadata", '{}') || ('{"firstDiscovered": "' || :'upgradeTime' || '"}')::jsonb || ('{"lastModified": "' || :'upgradeTime' || '"}')::jsonb WHERE "id" IS NOT NULL AND NOT "metadata" ?& array['firstDiscovered','lastModified'];

COMMIT;