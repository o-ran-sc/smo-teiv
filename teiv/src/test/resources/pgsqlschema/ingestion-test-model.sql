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

COPY ties_model.hash_info("name", "hashedValue", "type") FROM stdin;
FK_o-ran-smo-teiv-equipment_ANTENNAMODULEEEEEEEEEEEE_DEPLOYED_ON_ANTENNAMODULEEEEEEEEEEEEEEE_aSide_AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee	FK_2E4BCE7E3E19C851F14B87EF1BB00F7C918B5DCF	CONSTRAINT
FK_o-ran-smo-teiv-equipment_ANTENNAMODULEEEEEEEEEEEE_DEPLOYED_ON_ANTENNAMODULEEEEEEEEEEEEEEE_bSide_AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee	FK_7762EA35111FA62ED7E064A70380C8E12F050F28	CONSTRAINT
FK_o-ran-smo-teiv-equipment_ANTENNAMODULEEEEEEEEEEEE_REALISED_BY_ANTENNAMODULEEEEEEEEEEEEEEE_aSide_AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee	FK_EAB359DF1FC9806007FD44BF435DE5711915589A	CONSTRAINT
FK_o-ran-smo-teiv-equipment_ANTENNAMODULEEEEEEEEEEEE_REALISED_BY_ANTENNAMODULEEEEEEEEEEEEEEE_bSide_AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee	FK_99559DB06CF1125A1EB4364780EB8B2A8A6C4BCA	CONSTRAINT
FK_o-ran-smo-teiv-oam_ManagedElementtttttttttttttttttttttttttttttttttttttttttttttttttt_REL_FK_used-nrCellDu	FK_6CAC17243CDF76F4F070B2D097A169FBD41588AB	CONSTRAINT
FK_o-ran-smo-teiv-ran_ODUFunctionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn_REL_FK_managed-by-managedElement	FK_059D7C49C9FF74C65CE378DB6DA019FE030027EB	CONSTRAINT
FK_o-ran-smo-teiv-ran_LTESectorCarrierrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr_REL_FK_used-antennaCapability	FK_6292B05AE4EFCFFAE3343B626AA0E854181DC8E0	CONSTRAINT
FK_o-ran-smo-teiv-ran_NRCellDUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU_REL_FK_provided-by-oduFunction	FK_3B5DED591A6C8D4A14706E7E9B712BB23E4B01CF	CONSTRAINT
FK_o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULEEEEEEEEEEEEEEEEEEEE_SERVES_ANTENNACAPABILITYYYYYYYYYYYYYYYYYY_aSide_AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee	FK_B44561B5980FFD16E1386AEA3192A2612C96D738	CONSTRAINT
FK_o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULEEEEEEEEEEEEEEEEEEEE_SERVES_ANTENNACAPABILITYYYYYYYYYYYYYYYYYY_bSide_AntennaCapabilityyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy	FK_5DD6B84381C61E614924591CA5F01C4169700280	CONSTRAINT
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-equipment_ANTENNAMODULEEEEEEEEEEEE_DEPLOYED_ON_ANTENNAMODULEEEEEEEEEEEEEEE_CD_classifiers	IDX_65CAD728629B6E4501E261A44FA4551A3C29FF7E	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-equipment_ANTENNAMODULEEEEEEEEEEEE_DEPLOYED_ON_ANTENNAMODULEEEEEEEEEEEEEEE_CD_sourceIds	IDX_A431A290E4A5C18874FE31117537BB03F86A4BE1	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-equipment_ANTENNAMODULEEEEEEEEEEEE_REALISED_BY_ANTENNAMODULEEEEEEEEEEEEEEE_CD_classifiers	IDX_2B2F5C1C9635A1AF418B85A486601087C420E8F9	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-equipment_ANTENNAMODULEEEEEEEEEEEE_REALISED_BY_ANTENNAMODULEEEEEEEEEEEEEEE_CD_sourceIds	IDX_629B2C2E73E2B5BF5BB31BB816EE0597484360CD	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-equipment_AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee_CD_classifiers	IDX_FF1E0F629EA6972551FCE6D99834A31FE5A7FB10	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-equipment_AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee_CD_sourceIds	IDX_822F9AB2F47067FDB88F9C322BC6A2B3B6C947B9	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-equipment_AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee_antennaBeamWidth	IDX_9375E42087BDE5DF649B8F30DA3DEE96D07C863C	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-oam_ManagedElementtttttttttttttttttttttttttttttttttttttttttttttttttt_CD_classifiers	IDX_FC784BD0FA3BEBD9F5C8333C558B7A0BE874E9B4	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-oam_ManagedElementtttttttttttttttttttttttttttttttttttttttttttttttttt_CD_sourceIds	IDX_DF65ACAC403E061AF151ECC6E1A0B5251A57663A	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-oam_ManagedElementtttttttttttttttttttttttttttttttttttttttttttttttttt_REL_CD_classifiers_ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU	IDX_411D5CAD37663D02499EB24445A21693A234FFDE	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-oam_ManagedElementtttttttttttttttttttttttttttttttttttttttttttttttttt_REL_CD_sourceIds_ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU	IDX_A784A72DDC0B6161C3C40CC809908B28CBA3586C	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-ran_AntennaCapabilityyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy_CD_classifiers	IDX_DD78610CF631FE9F7AEB4068C0A4D8A40234DB31	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-ran_AntennaCapabilityyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy_CD_sourceIds	IDX_337B9980AA0401C715A372CF8615E73F826B9DAE	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-ran_AntennaCapabilityyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy_eUtranFqBands	IDX_7DACD641D4ED26579A7D8F94C74447B19D067ECD	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-ran_AntennaCapabilityyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy_geranFqBands	IDX_BC63EFD4857126FFC64E88F6136B85CA31E2A0EB	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-ran_AntennaCapabilityyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy_nRFqBands	IDX_E754CEE6CB27BA7BE1ECB90F717B2C241381B00F	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-ran_ODUFunctionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn_CD_classifiers	IDX_AA040BB82CC5AD00E57E39F729D6AB3DF6CCA9F8	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-ran_ODUFunctionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn_CD_sourceIds	IDX_8F11B2E5857B19A3F9FB339F4DFB139751097100	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-ran_ODUFunctionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn_REL_CD_classifiers_MANAGEDELEMENTTTTTTTTTTTTTTT_MANAGES_ODUFUNCTIONNNNNNNNNNNNNNN	IDX_FF9E656D3D0CB257F97FDDCB728E7637783C889B	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-ran_ODUFunctionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn_REL_CD_sourceIds_MANAGEDELEMENTTTTTTTTTTTTTTT_MANAGES_ODUFUNCTIONNNNNNNNNNNNNNN	IDX_0648B3E68F701DC7FDC41A87DE0D79FB167AFB78	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-ran_LTESectorCarrierrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr_CD_classifiers	IDX_2888BA890DB44CD93287CAAF0695047CF39B736B	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-ran_LTESectorCarrierrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr_CD_sourceIds	IDX_389F565ED970F5EB2FAC8A94766B2A60E5DEB022	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-ran_LTESectorCarrierrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr_REL_CD_classifiers_LTESECTORCARRIERRRRRRRRRRRRRRRRRRRRR_USES_ANTENNACAPABILITYYYYYYYYYYYYYYY	IDX_A3CD76C492D00CAAE49571191E3270D53FA818EE	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-ran_LTESectorCarrierrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr_REL_CD_sourceIds_LTESECTORCARRIERRRRRRRRRRRRRRRRRRRRR_USES_ANTENNACAPABILITYYYYYYYYYYYYYYY	IDX_839D67DA1D3875780CA8C2EC7DFD39C4F0A7D666	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-ran_NRCellDUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU_CD_classifiers	IDX_941E47F8B0E1F353F356F09A7D4EED3670560681	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-ran_NRCellDUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU_CD_sourceIds	IDX_5AEF72C5C2C83452A1BC8535E3EFF6E2AC9F4E02	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-ran_NRCellDUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU_REL_CD_classifiers_ODUFUNCTIONNNNNNNNNNNNNNNNN_PROVIDES_NRCELLDUUUUUUUUUUUUUUUUUU	IDX_8A9B70E4E83E34700456A8395E18BE2F7E793F20	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-ran_NRCellDUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU_REL_CD_sourceIds_ODUFUNCTIONNNNNNNNNNNNNNNNN_PROVIDES_NRCELLDUUUUUUUUUUUUUUUUUU	IDX_BF805D12F8B66196DC7513960E3872DCFCCCA31E	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULEEEEEEEEEEEEEEEEEEEE_SERVES_ANTENNACAPABILITYYYYYYYYYYYYYYYYYY_CD_classifiers	IDX_AF0E12037BDEE1B45F3F902B29E0823D713DBB06	INDEX
IDX_GIN_TRGM_OPS_ON_LIST_AS_JSONB_o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULEEEEEEEEEEEEEEEEEEEE_SERVES_ANTENNACAPABILITYYYYYYYYYYYYYYYYYY_CD_sourceIds	IDX_E61FF1756D360B904A01B032904F380F9D4A8650	INDEX
IDX_GIN_o-ran-smo-teiv-equipment_ANTENNAMODULEEEEEEEEEEEE_DEPLOYED_ON_ANTENNAMODULEEEEEEEEEEEEEEE_CD_decorators	IDX_B38A3DE6633390FD5D88720054D2659A52D9E5D9	INDEX
IDX_GIN_o-ran-smo-teiv-equipment_ANTENNAMODULEEEEEEEEEEEE_REALISED_BY_ANTENNAMODULEEEEEEEEEEEEEEE_CD_decorators	IDX_0A8C3B8A6C6FC1C4C60C47C3996F65D200F2CDAC	INDEX
IDX_GIN_o-ran-smo-teiv-equipment_AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee_CD_decorators	IDX_734E3F8FAD3A1B010D7E2095EA1357D4CEABD249	INDEX
IDX_GIN_o-ran-smo-teiv-oam_ManagedElementtttttttttttttttttttttttttttttttttttttttttttttttttt_CD_decorators	IDX_9B2B02F454ADC81B20DF50A78854FDDAE1E07781	INDEX
IDX_GIN_o-ran-smo-teiv-oam_ManagedElementtttttttttttttttttttttttttttttttttttttttttttttttttt_REL_CD_decorators_ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU	IDX_B0FFEC36366A5E403AADCA0B9DCEB13F511B226F	INDEX
IDX_GIN_o-ran-smo-teiv-ran_AntennaCapabilityyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy_CD_decorators	IDX_572A048D2B70E8CE7B919179CE285264951E6057	INDEX
IDX_GIN_o-ran-smo-teiv-ran_ODUFunctionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn_CD_decorators	IDX_98DD6CE7BB866A4EA737CE96FF985936134D39C5	INDEX
IDX_GIN_o-ran-smo-teiv-ran_ODUFunctionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn_REL_CD_decorators_MANAGEDELEMENTTTTTTTTTTTTTTT_MANAGES_ODUFUNCTIONNNNNNNNNNNNNNN	IDX_DED8DFAF8F597276CAA4680444C6A069A0570510	INDEX
IDX_GIN_o-ran-smo-teiv-ran_ODUFunctionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn_dUpLMNId	IDX_E1ACC551BE0D4D70B82C930C793C0C3751006444	INDEX
IDX_GIN_o-ran-smo-teiv-ran_LTESectorCarrierrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr_CD_decorators	IDX_C65D145DDF1B1222AB4DFF08B0F504605F3E8468	INDEX
IDX_GIN_o-ran-smo-teiv-ran_LTESectorCarrierrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr_REL_CD_decorators_LTESECTORCARRIERRRRRRRRRRRRRRRRRRRRR_USES_ANTENNACAPABILITYYYYYYYYYYYYYYY	IDX_8D9A8B3F7A4B33D0D985B1F13D210F71277AACBC	INDEX
IDX_GIN_o-ran-smo-teiv-ran_NRCellDUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU_CD_decorators	IDX_C6135512BCBC5F91EAD1D324759ADD1B88901FE9	INDEX
IDX_GIN_o-ran-smo-teiv-ran_NRCellDUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU_REL_CD_decorators_ODUFUNCTIONNNNNNNNNNNNNNNNN_PROVIDES_NRCELLDUUUUUUUUUUUUUUUUUU	IDX_6BEF4595F68DF48B662E68003C784CD6D944087B	INDEX
IDX_GIN_o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULEEEEEEEEEEEEEEEEEEEE_SERVES_ANTENNACAPABILITYYYYYYYYYYYYYYYYYY_CD_decorators	IDX_A2E437FF2DD6096051A873525763FD9EF7709350	INDEX
PK_o-ran-smo-teiv-equipment_ANTENNAMODULEEEEEEEEEEEE_DEPLOYED_ON_ANTENNAMODULEEEEEEEEEEEEEEE_id	PK_E38D71419D2D129A6834B4E3283938D44105C199	CONSTRAINT
PK_o-ran-smo-teiv-equipment_ANTENNAMODULEEEEEEEEEEEE_REALISED_BY_ANTENNAMODULEEEEEEEEEEEEEEE_id	PK_054AA52D66B07A7CCFFE992ED59634B506322187	CONSTRAINT
PK_o-ran-smo-teiv-equipment_AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee_id	PK_D70EE78F6AD699CEB48CB9531574C1979C2D8ADA	CONSTRAINT
PK_o-ran-smo-teiv-oam_ManagedElementtttttttttttttttttttttttttttttttttttttttttttttttttt_id	PK_F702AF3A5CAAC6B868FEBA4CE6464E2243E8FA62	CONSTRAINT
PK_o-ran-smo-teiv-ran_AntennaCapabilityyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy_id	PK_8D02617AC88C132BFCBC9D632334DCA8D415BAAF	CONSTRAINT
PK_o-ran-smo-teiv-ran_ODUFunctionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn_id	PK_DC5BAFEC8AD0A864ADD4A21F6AEDE50F25D28F65	CONSTRAINT
PK_o-ran-smo-teiv-ran_LTESectorCarrierrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr_id	PK_00966063B84AE30FE4B3DEF804C06532734473E9	CONSTRAINT
PK_o-ran-smo-teiv-ran_NRCellDUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU_id	PK_444AE2C6A8809EBDD1B911623638A5F6558FC5B5	CONSTRAINT
PK_o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULEEEEEEEEEEEEEEEEEEEE_SERVES_ANTENNACAPABILITYYYYYYYYYYYYYYYYYY_id	PK_4307C17B59528C77107E7C39986C0D18277F61FD	CONSTRAINT
REL_CD_classifiers_ODUFUNCTIONNNNNNNNNNNNNNNNN_PROVIDES_NRCELLDUUUUUUUUUUUUUUUUUU	REL_CD_B614AAA814176BC7128CE9D72C0950C4D6DE8052	COLUMN
REL_metadata_ODUFUNCTIONNNNNNNNNNNNNNNNN_PROVIDES_NRCELLDUUUUUUUUUUUUUUUUUU	REL_metadata_8B4FA813345B7F25ACF2E9635EAF6E6DE0FBBE8A	COLUMN
REL_CD_classifiers_LTESECTORCARRIERRRRRRRRRRRRRRRRRRRRR_USES_ANTENNACAPABILITYYYYYYYYYYYYYYY	REL_CD_68C234330FD6388836D0DAF9DFD0A40DE66DD8C5	COLUMN
REL_metadata_LTESECTORCARRIERRRRRRRRRRRRRRRRRRRRR_USES_ANTENNACAPABILITYYYYYYYYYYYYYYY	REL_metadata_F723867781098568079DD82E5D5E529374F97E98	COLUMN
REL_CD_classifiers_MANAGEDELEMENTTTTTTTTTTTTTTT_MANAGES_ODUFUNCTIONNNNNNNNNNNNNNN	REL_CD_7E9F11EFBF8974D7C7DAB02E181A0BE4148091C6	COLUMN
REL_metadata_MANAGEDELEMENTTTTTTTTTTTTTTT_MANAGES_ODUFUNCTIONNNNNNNNNNNNNNN	REL_metadata_7E9F11EFBF8974D7C7DAB02E181A0BE4148091DE	COLUMN
REL_CD_classifiers_ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU	REL_CD_E388983F3E6BFAD67CA100F0AFCF8CD3E9B89ADD	COLUMN
REL_metadata_ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU	REL_metadata_780D8ABEAC14A0B2ET73FC4EB093C446F7CD3E56	COLUMN
REL_CD_decorators_ODUFUNCTIONNNNNNNNNNNNNNNNN_PROVIDES_NRCELLDUUUUUUUUUUUUUUUUUU	REL_CD_0AC0D382E4274681870EC7319460192F4F603001	COLUMN
REL_CD_decorators_LTESECTORCARRIERRRRRRRRRRRRRRRRRRRRR_USES_ANTENNACAPABILITYYYYYYYYYYYYYYY	REL_CD_A14923FFF9D13FEB18087CE2A9C0BD264C572CFC	COLUMN
REL_CD_decorators_MANAGEDELEMENTTTTTTTTTTTTTTT_MANAGES_ODUFUNCTIONNNNNNNNNNNNNNN	REL_CD_FFF7E036187A7605BFC714483D2E60FD2FF6560B	COLUMN
REL_CD_decorators_ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU	REL_CD_EF3979E9DAF31B7949C883654633E633B9D35B92	COLUMN
REL_CD_sourceIds_ODUFUNCTIONNNNNNNNNNNNNNNNN_PROVIDES_NRCELLDUUUUUUUUUUUUUUUUUU	REL_CD_32FFD9EF85AB2342E652FA493C3BF34D9BAAF054	COLUMN
REL_CD_sourceIds_LTESECTORCARRIERRRRRRRRRRRRRRRRRRRRR_USES_ANTENNACAPABILITYYYYYYYYYYYYYYY	REL_CD_8D8BBB53FB18E8E9A5D241B66DA18CB5DDE9C1A5	COLUMN
REL_CD_sourceIds_MANAGEDELEMENTTTTTTTTTTTTTTT_MANAGES_ODUFUNCTIONNNNNNNNNNNNNNN	REL_CD_45E8E8693B1B8928376EAA8995D08AA7B1E483BD	COLUMN
REL_CD_sourceIds_ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU	REL_CD_1F61FA6DDAECE90540F9880F2A98037B1530A5A4	COLUMN
REL_FK_used-nrCellDu	REL_FK_used-nrCellDu	COLUMN
REL_ID_ODUFUNCTIONNNNNNNNNNNNNNNNN_PROVIDES_NRCELLDUUUUUUUUUUUUUUUUUU	REL_ID_7899092EC8FBC674398C53965ADEFF940D17481F	COLUMN
REL_ID_LTESECTORCARRIERRRRRRRRRRRRRRRRRRRRR_USES_ANTENNACAPABILITYYYYYYYYYYYYYYY	REL_ID_1C61FC18067350DB393AFDB5270E9DE1F5151C64	COLUMN
REL_ID_MANAGEDELEMENTTTTTTTTTTTTTTT_MANAGES_ODUFUNCTIONNNNNNNNNNNNNNN	REL_ID_BDE0B6C74D14AC109D29A08D80E92D4D0DCAEB0B	COLUMN
REL_ID_ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU	REL_ID_ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU	COLUMN
UNIQUE_o-ran-smo-teiv-oam_ManagedElementtttttttttttttttttttttttttttttttttttttttttttttttttt_REL_ID_ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU	UNIQUE_734E3D6BB96CF74951FC24E93EDCC920A48F970C	CONSTRAINT
UNIQUE_o-ran-smo-teiv-ran_ODUFunctionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn_REL_ID_MANAGEDELEMENTTTTTTTTTTTTTTT_MANAGES_ODUFUNCTIONNNNNNNNNNNNNNN	UNIQUE_A105D998A021EF05E41D7EFCA7EFCFE4DEA9049A	CONSTRAINT
UNIQUE_o-ran-smo-teiv-ran_LTESectorCarrierrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr_REL_ID_LTESECTORCARRIERRRRRRRRRRRRRRRRRRRRR_USES_ANTENNACAPABILITYYYYYYYYYYYYYYY	UNIQUE_8BA54615B1C74692F55B5335A230309881FF9BF8	CONSTRAINT
UNIQUE_o-ran-smo-teiv-ran_NRCellDUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU_REL_ID_ODUFUNCTIONNNNNNNNNNNNNNNNN_PROVIDES_NRCELLDUUUUUUUUUUUUUUUUUU	UNIQUE_15A52B744DF4DBB44E6CA285D0A05203897B7BE3	CONSTRAINT
aSide_AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee	aSide_2A2D3374BF907674FA1905478E30ACB8882DC03C	COLUMN
bSide_AntennaCapabilityyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy	bSide_E8A97B8133D74D3BE65119E868FAC0BE63C09FFC	COLUMN
bSide_AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee	bSide_EE6DD4A2CFD743779BBCBFC18FC296EF6D72EB1E	COLUMN
cellLocalIdddddddddddddddddddddddddddddddddddddddddddddddddddddd	020335B0F627C169E24167748C38FE756FB34AE2	COLUMN
o-ran-smo-teiv-equipment_ANTENNAMODULEEEEEEEEEEEE_DEPLOYED_ON_ANTENNAMODULEEEEEEEEEEEEEEE	235E984E81B76ADCCFA68BDE09D3BBE49A355919	TABLE
o-ran-smo-teiv-equipment_ANTENNAMODULEEEEEEEEEEEE_REALISED_BY_ANTENNAMODULEEEEEEEEEEEEEEE	53089669D370B15C78B7E8376D434921D1C94240	TABLE
o-ran-smo-teiv-equipment_AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee	53017288F3FE983848689A3DD21D48D298CCD23E	TABLE
o-ran-smo-teiv-oam_ManagedElementtttttttttttttttttttttttttttttttttttttttttttttttttt	28C9A375E800E82308EBE7DA2932EF2C0AF13C38	TABLE
o-ran-smo-teiv-ran_AntennaCapabilityyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy	83C555BD948277E4A7C99698690BF800604C0603	TABLE
o-ran-smo-teiv-ran_ODUFunctionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn	9759D655D5978786B3284FE8057D912192AEB550	TABLE
o-ran-smo-teiv-ran_LTESectorCarrierrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr	C278F34A021D7A9AD13BE860608751874F21C00C	TABLE
o-ran-smo-teiv-ran_NRCellDUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU	84E676149362F50C55FE1E004B98D4891916BBF3	TABLE
o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULEEEEEEEEEEEEEEEEEEEE_SERVES_ANTENNACAPABILITYYYYYYYYYYYYYYYYYY	CE18CE5B725879B4016F5C281D80D37A95EA7E28	TABLE
\.

COPY ties_model.entity_info("storedAt", "name", "moduleReferenceName") FROM stdin;
o-ran-smo-teiv-equipment_AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee	AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee	o-ran-smo-teiv-equipment
o-ran-smo-teiv-oam_ManagedElementtttttttttttttttttttttttttttttttttttttttttttttttttt	ManagedElementtttttttttttttttttttttttttttttttttttttttttttttttttt	o-ran-smo-teiv-oam
o-ran-smo-teiv-ran_AntennaCapabilityyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy	AntennaCapabilityyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy	o-ran-smo-teiv-ran
o-ran-smo-teiv-ran_ODUFunctionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn	ODUFunctionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn	o-ran-smo-teiv-ran
o-ran-smo-teiv-ran_LTESectorCarrierrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr	LTESectorCarrierrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr	o-ran-smo-teiv-ran
o-ran-smo-teiv-ran_NRCellDUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU	NRCellDUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU	o-ran-smo-teiv-ran
\.

COPY ties_model.relationship_info("name", "aSideAssociationName", "aSideMOType", "aSideModule", "aSideMinCardinality", "aSideMaxCardinality", "bSideAssociationName", "bSideMOType", "bSideModule", "bSideMinCardinality", "bSideMaxCardinality", "associationKind", "connectSameEntity", "relationshipDataLocation", "storedAt", "moduleReferenceName") FROM stdin;
ANTENNAMODULEEEEEEEEEEEEEEEEEEEE_SERVES_ANTENNACAPABILITYYYYYYYYYYYYYYYYYY	serviced-antennaCapability	AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee	o-ran-smo-teiv-equipment	0	9223372036854775807	serving-antennaModule	AntennaCapabilityyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy	o-ran-smo-teiv-ran	0	9223372036854775807	BI_DIRECTIONAL	false	RELATION	o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULEEEEEEEEEEEEEEEEEEEE_SERVES_ANTENNACAPABILITYYYYYYYYYYYYYYYYYY	o-ran-smo-teiv-rel-equipment-ran
ANTENNAMODULEEEEEEEEEEEE_DEPLOYED_ON_ANTENNAMODULEEEEEEEEEEEEEEE	deployed-on-antennaModule	AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee	o-ran-smo-teiv-equipment	0	1	deployed-antennaModule	AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee	o-ran-smo-teiv-equipment	0	9223372036854775807	BI_DIRECTIONAL	true	RELATION	o-ran-smo-teiv-equipment_ANTENNAMODULEEEEEEEEEEEE_DEPLOYED_ON_ANTENNAMODULEEEEEEEEEEEEEEE	o-ran-smo-teiv-equipment
ANTENNAMODULEEEEEEEEEEEE_REALISED_BY_ANTENNAMODULEEEEEEEEEEEEEEE	realised-by-antennaModule	AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee	o-ran-smo-teiv-equipment	0	1	realised-antennaModule	AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee	o-ran-smo-teiv-equipment	0	1	BI_DIRECTIONAL	true	RELATION	o-ran-smo-teiv-equipment_ANTENNAMODULEEEEEEEEEEEE_REALISED_BY_ANTENNAMODULEEEEEEEEEEEEEEE	o-ran-smo-teiv-equipment
ODUFUNCTIONNNNNNNNNNNNNNNNN_PROVIDES_NRCELLDUUUUUUUUUUUUUUUUUU	provided-nrCellDu	ODUFunctionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn	o-ran-smo-teiv-ran	1	1	provided-by-oduFunction	NRCellDUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU	o-ran-smo-teiv-ran	0	9223372036854775807	BI_DIRECTIONAL	false	B_SIDE	o-ran-smo-teiv-ran_NRCellDUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU	o-ran-smo-teiv-ran
LTESECTORCARRIERRRRRRRRRRRRRRRRRRRRR_USES_ANTENNACAPABILITYYYYYYYYYYYYYYY	used-antennaCapability	LTESectorCarrierrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr	o-ran-smo-teiv-ran	0	9223372036854775807	used-by-lteSectorCarrier	AntennaCapabilityyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy	o-ran-smo-teiv-ran	0	1	BI_DIRECTIONAL	false	A_SIDE	o-ran-smo-teiv-ran_LTESectorCarrierrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr	o-ran-smo-teiv-ran
MANAGEDELEMENTTTTTTTTTTTTTTT_MANAGES_ODUFUNCTIONNNNNNNNNNNNNNN	managed-oduFunction	ManagedElementtttttttttttttttttttttttttttttttttttttttttttttttttt	o-ran-smo-teiv-oam	1	1	managed-by-managedElement	ODUFunctionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn	o-ran-smo-teiv-ran	0	9223372036854775807	BI_DIRECTIONAL	false	B_SIDE	o-ran-smo-teiv-ran_ODUFunctionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn	o-ran-smo-teiv-rel-oam-ran
ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU	used-nrCellDu	ManagedElementtttttttttttttttttttttttttttttttttttttttttttttttttt	o-ran-smo-teiv-oam	1	1	used-by-managedElement	NRCellDUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU	o-ran-smo-teiv-ran	0	1	BI_DIRECTIONAL	false	A_SIDE	o-ran-smo-teiv-oam_ManagedElementtttttttttttttttttttttttttttttttttttttttttttttttttt	o-ran-smo-teiv-rel-oam-ran
\.

;

COMMIT;