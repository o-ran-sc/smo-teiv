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

COPY teiv_data."o-ran-smo-teiv-ran_NRCellDU" ("id", "metadata", "cellLocalId", "nCI", "nRPCI", "nRTAC") FROM stdin;
myReliabilityCell_1	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	51	61	\N	\N
myReliabilityCell_2	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	51	61	701	\N
myReliabilityCell_3	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	51	61	701	401
myReliabilityCell_4	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	51	61	701	401
myReliabilityCell_5	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	51	61	701	401
myReliabilityCell_6	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	\N	\N	\N	\N
myReliabilityCell_7	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	51	61	701	401
myReliabilityCell_8	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	51	61	701	401
\.

CREATE TABLE IF NOT EXISTS teiv_data."235E984E81B76ADCCFA68BDE09D3BBE49A355919" (
	"id"			TEXT,
	"aSide_2A2D3374BF907674FA1905478E30ACB8882DC03C"			TEXT,
	"bSide_EE6DD4A2CFD743779BBCBFC18FC296EF6D72EB1E"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY teiv_data."235E984E81B76ADCCFA68BDE09D3BBE49A355919" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE ONLY teiv_data."235E984E81B76ADCCFA68BDE09D3BBE49A355919" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."235E984E81B76ADCCFA68BDE09D3BBE49A355919" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."235E984E81B76ADCCFA68BDE09D3BBE49A355919" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."28C9A375E800E82308EBE7DA2932EF2C0AF13C38" (
	"id"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_used-nrCellDu"			TEXT,
	"REL_ID_ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU"			TEXT,
	"REL_CD_1F61FA6DDAECE90540F9880F2A98037B1530A5A4"			jsonb,
	"REL_CD_E388983F3E6BFAD67CA100F0AFCF8CD3E9B89ADD"			jsonb,
	"REL_CD_EF3979E9DAF31B7949C883654633E633B9D35B92"			jsonb
);

ALTER TABLE ONLY teiv_data."28C9A375E800E82308EBE7DA2932EF2C0AF13C38" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE ONLY teiv_data."28C9A375E800E82308EBE7DA2932EF2C0AF13C38" ADD COLUMN IF NOT EXISTS "REL_metadata_C9E09FBCCBA1F6A3252B71B869B269EF28AACDFB" jsonb;

ALTER TABLE ONLY teiv_data."28C9A375E800E82308EBE7DA2932EF2C0AF13C38" ALTER COLUMN "metadata" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."28C9A375E800E82308EBE7DA2932EF2C0AF13C38" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."28C9A375E800E82308EBE7DA2932EF2C0AF13C38" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."28C9A375E800E82308EBE7DA2932EF2C0AF13C38" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."28C9A375E800E82308EBE7DA2932EF2C0AF13C38" ALTER COLUMN "REL_CD_1F61FA6DDAECE90540F9880F2A98037B1530A5A4" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."28C9A375E800E82308EBE7DA2932EF2C0AF13C38" ALTER COLUMN "REL_CD_E388983F3E6BFAD67CA100F0AFCF8CD3E9B89ADD" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."28C9A375E800E82308EBE7DA2932EF2C0AF13C38" ALTER COLUMN "REL_CD_EF3979E9DAF31B7949C883654633E633B9D35B92" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."53017288F3FE983848689A3DD21D48D298CCD23E" (
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
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY teiv_data."53017288F3FE983848689A3DD21D48D298CCD23E" ADD COLUMN IF NOT EXISTS "metadata" jsonb;


ALTER TABLE ONLY teiv_data."53017288F3FE983848689A3DD21D48D298CCD23E" ALTER COLUMN "metadata" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."53017288F3FE983848689A3DD21D48D298CCD23E" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."53017288F3FE983848689A3DD21D48D298CCD23E" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."53017288F3FE983848689A3DD21D48D298CCD23E" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."53089669D370B15C78B7E8376D434921D1C94240" (
	"id"			TEXT,
	"aSide_2A2D3374BF907674FA1905478E30ACB8882DC03C"			TEXT,
	"bSide_EE6DD4A2CFD743779BBCBFC18FC296EF6D72EB1E"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY teiv_data."53089669D370B15C78B7E8376D434921D1C94240" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE ONLY teiv_data."53089669D370B15C78B7E8376D434921D1C94240" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."53089669D370B15C78B7E8376D434921D1C94240" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."53089669D370B15C78B7E8376D434921D1C94240" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."83C555BD948277E4A7C99698690BF800604C0603" (
	"id"			TEXT,
	"eUtranFqBands"			jsonb,
	"geranFqBands"			jsonb,
	"nRFqBands"			jsonb,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY teiv_data."83C555BD948277E4A7C99698690BF800604C0603" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE ONLY teiv_data."83C555BD948277E4A7C99698690BF800604C0603" ALTER COLUMN "metadata" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."83C555BD948277E4A7C99698690BF800604C0603" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."83C555BD948277E4A7C99698690BF800604C0603" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."83C555BD948277E4A7C99698690BF800604C0603" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."84E676149362F50C55FE1E004B98D4891916BBF3" (
	"id"			TEXT,
	"020335B0F627C169E24167748C38FE756FB34AE2"			INTEGER,
	"nCI"			BIGINT,
	"nRPCI"			INTEGER,
	"nRTAC"			INTEGER,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_provided-by-oduFunction"			TEXT,
	"REL_ID_7899092EC8FBC674398C53965ADEFF940D17481F"			TEXT,
	"REL_CD_32FFD9EF85AB2342E652FA493C3BF34D9BAAF054"			jsonb,
	"REL_CD_B614AAA814176BC7128CE9D72C0950C4D6DE8052"			jsonb,
	"REL_CD_0AC0D382E4274681870EC7319460192F4F603001"			jsonb
);

ALTER TABLE ONLY teiv_data."84E676149362F50C55FE1E004B98D4891916BBF3" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE ONLY teiv_data."84E676149362F50C55FE1E004B98D4891916BBF3" ADD COLUMN IF NOT EXISTS "REL_metadata_8B4FA813345B7F25ACF2E9635EAF6E6DE0FBBE8A" jsonb;

ALTER TABLE ONLY teiv_data."84E676149362F50C55FE1E004B98D4891916BBF3" ALTER COLUMN "metadata" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."84E676149362F50C55FE1E004B98D4891916BBF3" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."84E676149362F50C55FE1E004B98D4891916BBF3" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."84E676149362F50C55FE1E004B98D4891916BBF3" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."84E676149362F50C55FE1E004B98D4891916BBF3" ALTER COLUMN "REL_CD_32FFD9EF85AB2342E652FA493C3BF34D9BAAF054" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."84E676149362F50C55FE1E004B98D4891916BBF3" ALTER COLUMN "REL_CD_B614AAA814176BC7128CE9D72C0950C4D6DE8052" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."84E676149362F50C55FE1E004B98D4891916BBF3" ALTER COLUMN "REL_CD_0AC0D382E4274681870EC7319460192F4F603001" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."9759D655D5978786B3284FE8057D912192AEB550" (
	"id"			TEXT,
	"dUpLMNId"			jsonb,
	"gNBDUId"			BIGINT,
	"gNBId"			BIGINT,
	"gNBIdLength"			INTEGER,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_provided-by-oduFunction"			TEXT,
	"REL_ID_7899092EC8FBC674398C53965ADEFF940D17481F"			TEXT,
	"REL_FK_managed-by-managedElement"			TEXT,
	"REL_ID_BDE0B6C74D14AC109D29A08D80E92D4D0DCAEB0B"			TEXT,
	"REL_CD_45E8E8693B1B8928376EAA8995D08AA7B1E483BD"			jsonb,
	"REL_CD_7E9F11EFBF8974D7C7DAB02E181A0BE4148091C6"			jsonb,
	"REL_CD_FFF7E036187A7605BFC714483D2E60FD2FF6560B"			jsonb
);

ALTER TABLE ONLY teiv_data."9759D655D5978786B3284FE8057D912192AEB550" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE ONLY teiv_data."9759D655D5978786B3284FE8057D912192AEB550" ADD COLUMN IF NOT EXISTS "REL_metadata_7E9F11EFBF8974D7C7DAB02E181A0BE4148091DE" jsonb;

ALTER TABLE ONLY teiv_data."9759D655D5978786B3284FE8057D912192AEB550" ALTER COLUMN "metadata" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."9759D655D5978786B3284FE8057D912192AEB550" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."9759D655D5978786B3284FE8057D912192AEB550" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."9759D655D5978786B3284FE8057D912192AEB550" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."9759D655D5978786B3284FE8057D912192AEB550" ALTER COLUMN "REL_CD_45E8E8693B1B8928376EAA8995D08AA7B1E483BD" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."9759D655D5978786B3284FE8057D912192AEB550" ALTER COLUMN "REL_CD_7E9F11EFBF8974D7C7DAB02E181A0BE4148091C6" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."9759D655D5978786B3284FE8057D912192AEB550" ALTER COLUMN "REL_CD_FFF7E036187A7605BFC714483D2E60FD2FF6560B" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."C278F34A021D7A9AD13BE860608751874F21C00C" (
	"id"			TEXT,
	"sectorCarrierType"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_used-antennaCapability"			TEXT,
	"REL_ID_1C61FC18067350DB393AFDB5270E9DE1F5151C64"			TEXT,
	"REL_CD_8D8BBB53FB18E8E9A5D241B66DA18CB5DDE9C1A5"			jsonb,
	"REL_CD_68C234330FD6388836D0DAF9DFD0A40DE66DD8C5"			jsonb,
	"REL_CD_A14923FFF9D13FEB18087CE2A9C0BD264C572CFC"			jsonb
);

ALTER TABLE ONLY teiv_data."C278F34A021D7A9AD13BE860608751874F21C00C" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE ONLY teiv_data."C278F34A021D7A9AD13BE860608751874F21C00C" ADD COLUMN IF NOT EXISTS "REL_metadata_F723867781098568079DD82E5D5E529374F97E98" jsonb;

ALTER TABLE ONLY teiv_data."C278F34A021D7A9AD13BE860608751874F21C00C" ALTER COLUMN "metadata" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."C278F34A021D7A9AD13BE860608751874F21C00C" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."C278F34A021D7A9AD13BE860608751874F21C00C" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."C278F34A021D7A9AD13BE860608751874F21C00C" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."C278F34A021D7A9AD13BE860608751874F21C00C" ALTER COLUMN "REL_CD_8D8BBB53FB18E8E9A5D241B66DA18CB5DDE9C1A5" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."C278F34A021D7A9AD13BE860608751874F21C00C" ALTER COLUMN "REL_CD_68C234330FD6388836D0DAF9DFD0A40DE66DD8C5" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."C278F34A021D7A9AD13BE860608751874F21C00C" ALTER COLUMN "REL_CD_A14923FFF9D13FEB18087CE2A9C0BD264C572CFC" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."CE18CE5B725879B4016F5C281D80D37A95EA7E28" (
	"id"			TEXT,
	"aSide_2A2D3374BF907674FA1905478E30ACB8882DC03C"			TEXT,
	"bSide_E8A97B8133D74D3BE65119E868FAC0BE63C09FFC"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY teiv_data."CE18CE5B725879B4016F5C281D80D37A95EA7E28" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE ONLY teiv_data."CE18CE5B725879B4016F5C281D80D37A95EA7E28" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."CE18CE5B725879B4016F5C281D80D37A95EA7E28" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."CE18CE5B725879B4016F5C281D80D37A95EA7E28" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."CE18CE5B725879B4016F5C281D80D37A95EA7E28" ALTER COLUMN "metadata" SET DEFAULT '{}';

SELECT teiv_data.create_constraint_if_not_exists(
	'235E984E81B76ADCCFA68BDE09D3BBE49A355919',
 'PK_E38D71419D2D129A6834B4E3283938D44105C199',
 'ALTER TABLE teiv_data."235E984E81B76ADCCFA68BDE09D3BBE49A355919" ADD CONSTRAINT "PK_E38D71419D2D129A6834B4E3283938D44105C199" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'28C9A375E800E82308EBE7DA2932EF2C0AF13C38',
 'PK_F702AF3A5CAAC6B868FEBA4CE6464E2243E8FA62',
 'ALTER TABLE teiv_data."28C9A375E800E82308EBE7DA2932EF2C0AF13C38" ADD CONSTRAINT "PK_F702AF3A5CAAC6B868FEBA4CE6464E2243E8FA62" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'53017288F3FE983848689A3DD21D48D298CCD23E',
 'PK_D70EE78F6AD699CEB48CB9531574C1979C2D8ADA',
 'ALTER TABLE teiv_data."53017288F3FE983848689A3DD21D48D298CCD23E" ADD CONSTRAINT "PK_D70EE78F6AD699CEB48CB9531574C1979C2D8ADA" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'53089669D370B15C78B7E8376D434921D1C94240',
 'PK_054AA52D66B07A7CCFFE992ED59634B506322187',
 'ALTER TABLE teiv_data."53089669D370B15C78B7E8376D434921D1C94240" ADD CONSTRAINT "PK_054AA52D66B07A7CCFFE992ED59634B506322187" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'83C555BD948277E4A7C99698690BF800604C0603',
 'PK_8D02617AC88C132BFCBC9D632334DCA8D415BAAF',
 'ALTER TABLE teiv_data."83C555BD948277E4A7C99698690BF800604C0603" ADD CONSTRAINT "PK_8D02617AC88C132BFCBC9D632334DCA8D415BAAF" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'84E676149362F50C55FE1E004B98D4891916BBF3',
 'PK_444AE2C6A8809EBDD1B911623638A5F6558FC5B5',
 'ALTER TABLE teiv_data."84E676149362F50C55FE1E004B98D4891916BBF3" ADD CONSTRAINT "PK_444AE2C6A8809EBDD1B911623638A5F6558FC5B5" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'9759D655D5978786B3284FE8057D912192AEB550',
 'PK_DC5BAFEC8AD0A864ADD4A21F6AEDE50F25D28F65',
 'ALTER TABLE teiv_data."9759D655D5978786B3284FE8057D912192AEB550" ADD CONSTRAINT "PK_DC5BAFEC8AD0A864ADD4A21F6AEDE50F25D28F65" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'C278F34A021D7A9AD13BE860608751874F21C00C',
 'PK_00966063B84AE30FE4B3DEF804C06532734473E9',
 'ALTER TABLE teiv_data."C278F34A021D7A9AD13BE860608751874F21C00C" ADD CONSTRAINT "PK_00966063B84AE30FE4B3DEF804C06532734473E9" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'CE18CE5B725879B4016F5C281D80D37A95EA7E28',
 'PK_4307C17B59528C77107E7C39986C0D18277F61FD',
 'ALTER TABLE teiv_data."CE18CE5B725879B4016F5C281D80D37A95EA7E28" ADD CONSTRAINT "PK_4307C17B59528C77107E7C39986C0D18277F61FD" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'235E984E81B76ADCCFA68BDE09D3BBE49A355919',
 'FK_2E4BCE7E3E19C851F14B87EF1BB00F7C918B5DCF',
 'ALTER TABLE teiv_data."235E984E81B76ADCCFA68BDE09D3BBE49A355919" ADD CONSTRAINT "FK_2E4BCE7E3E19C851F14B87EF1BB00F7C918B5DCF" FOREIGN KEY ("aSide_2A2D3374BF907674FA1905478E30ACB8882DC03C") REFERENCES teiv_data."53017288F3FE983848689A3DD21D48D298CCD23E" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'235E984E81B76ADCCFA68BDE09D3BBE49A355919',
 'FK_7762EA35111FA62ED7E064A70380C8E12F050F28',
 'ALTER TABLE teiv_data."235E984E81B76ADCCFA68BDE09D3BBE49A355919" ADD CONSTRAINT "FK_7762EA35111FA62ED7E064A70380C8E12F050F28" FOREIGN KEY ("bSide_EE6DD4A2CFD743779BBCBFC18FC296EF6D72EB1E") REFERENCES teiv_data."53017288F3FE983848689A3DD21D48D298CCD23E" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'28C9A375E800E82308EBE7DA2932EF2C0AF13C38',
 'FK_6CAC17243CDF76F4F070B2D097A169FBD41588AB',
 'ALTER TABLE teiv_data."28C9A375E800E82308EBE7DA2932EF2C0AF13C38" ADD CONSTRAINT "FK_6CAC17243CDF76F4F070B2D097A169FBD41588AB" FOREIGN KEY ("REL_FK_used-nrCellDu") REFERENCES teiv_data."84E676149362F50C55FE1E004B98D4891916BBF3" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'28C9A375E800E82308EBE7DA2932EF2C0AF13C38',
 'UNIQUE_734E3D6BB96CF74951FC24E93EDCC920A48F970C',
 'ALTER TABLE teiv_data."28C9A375E800E82308EBE7DA2932EF2C0AF13C38" ADD CONSTRAINT "UNIQUE_734E3D6BB96CF74951FC24E93EDCC920A48F970C" UNIQUE ("REL_ID_ManagedElementttttttttttttttttt_USES_NRCellDUUUUUUUUUUUU");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'53089669D370B15C78B7E8376D434921D1C94240',
 'FK_EAB359DF1FC9806007FD44BF435DE5711915589A',
 'ALTER TABLE teiv_data."53089669D370B15C78B7E8376D434921D1C94240" ADD CONSTRAINT "FK_EAB359DF1FC9806007FD44BF435DE5711915589A" FOREIGN KEY ("aSide_2A2D3374BF907674FA1905478E30ACB8882DC03C") REFERENCES teiv_data."53017288F3FE983848689A3DD21D48D298CCD23E" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'53089669D370B15C78B7E8376D434921D1C94240',
 'FK_99559DB06CF1125A1EB4364780EB8B2A8A6C4BCA',
 'ALTER TABLE teiv_data."53089669D370B15C78B7E8376D434921D1C94240" ADD CONSTRAINT "FK_99559DB06CF1125A1EB4364780EB8B2A8A6C4BCA" FOREIGN KEY ("bSide_EE6DD4A2CFD743779BBCBFC18FC296EF6D72EB1E") REFERENCES teiv_data."53017288F3FE983848689A3DD21D48D298CCD23E" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'84E676149362F50C55FE1E004B98D4891916BBF3',
 'FK_3B5DED591A6C8D4A14706E7E9B712BB23E4B01CF',
 'ALTER TABLE teiv_data."84E676149362F50C55FE1E004B98D4891916BBF3" ADD CONSTRAINT "FK_3B5DED591A6C8D4A14706E7E9B712BB23E4B01CF" FOREIGN KEY ("REL_FK_provided-by-oduFunction") REFERENCES teiv_data."9759D655D5978786B3284FE8057D912192AEB550" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'84E676149362F50C55FE1E004B98D4891916BBF3',
 'UNIQUE_15A52B744DF4DBB44E6CA285D0A05203897B7BE3',
 'ALTER TABLE teiv_data."84E676149362F50C55FE1E004B98D4891916BBF3" ADD CONSTRAINT "UNIQUE_15A52B744DF4DBB44E6CA285D0A05203897B7BE3" UNIQUE ("REL_ID_7899092EC8FBC674398C53965ADEFF940D17481F");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'9759D655D5978786B3284FE8057D912192AEB550',
 'FK_059D7C49C9FF74C65CE378DB6DA019FE030027EB',
 'ALTER TABLE teiv_data."9759D655D5978786B3284FE8057D912192AEB550" ADD CONSTRAINT "FK_059D7C49C9FF74C65CE378DB6DA019FE030027EB" FOREIGN KEY ("REL_FK_managed-by-managedElement") REFERENCES teiv_data."28C9A375E800E82308EBE7DA2932EF2C0AF13C38" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'9759D655D5978786B3284FE8057D912192AEB550',
 'UNIQUE_A105D998A021EF05E41D7EFCA7EFCFE4DEA9049A',
 'ALTER TABLE teiv_data."9759D655D5978786B3284FE8057D912192AEB550" ADD CONSTRAINT "UNIQUE_A105D998A021EF05E41D7EFCA7EFCFE4DEA9049A" UNIQUE ("REL_ID_BDE0B6C74D14AC109D29A08D80E92D4D0DCAEB0B");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'C278F34A021D7A9AD13BE860608751874F21C00C',
 'FK_6292B05AE4EFCFFAE3343B626AA0E854181DC8E0',
 'ALTER TABLE teiv_data."C278F34A021D7A9AD13BE860608751874F21C00C" ADD CONSTRAINT "FK_6292B05AE4EFCFFAE3343B626AA0E854181DC8E0" FOREIGN KEY ("REL_FK_used-antennaCapability") REFERENCES teiv_data."83C555BD948277E4A7C99698690BF800604C0603" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'C278F34A021D7A9AD13BE860608751874F21C00C',
 'UNIQUE_8BA54615B1C74692F55B5335A230309881FF9BF8',
 'ALTER TABLE teiv_data."C278F34A021D7A9AD13BE860608751874F21C00C" ADD CONSTRAINT "UNIQUE_8BA54615B1C74692F55B5335A230309881FF9BF8" UNIQUE ("REL_ID_1C61FC18067350DB393AFDB5270E9DE1F5151C64");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'CE18CE5B725879B4016F5C281D80D37A95EA7E28',
 'FK_B44561B5980FFD16E1386AEA3192A2612C96D738',
 'ALTER TABLE teiv_data."CE18CE5B725879B4016F5C281D80D37A95EA7E28" ADD CONSTRAINT "FK_B44561B5980FFD16E1386AEA3192A2612C96D738" FOREIGN KEY ("aSide_2A2D3374BF907674FA1905478E30ACB8882DC03C") REFERENCES teiv_data."53017288F3FE983848689A3DD21D48D298CCD23E" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'CE18CE5B725879B4016F5C281D80D37A95EA7E28',
 'FK_5DD6B84381C61E614924591CA5F01C4169700280',
 'ALTER TABLE teiv_data."CE18CE5B725879B4016F5C281D80D37A95EA7E28" ADD CONSTRAINT "FK_5DD6B84381C61E614924591CA5F01C4169700280" FOREIGN KEY ("bSide_E8A97B8133D74D3BE65119E868FAC0BE63C09FFC") REFERENCES teiv_data."83C555BD948277E4A7C99698690BF800604C0603" (id) ON DELETE CASCADE;'
);

CREATE INDEX IF NOT EXISTS "IDX_A431A290E4A5C18874FE31117537BB03F86A4BE1" ON teiv_data."235E984E81B76ADCCFA68BDE09D3BBE49A355919" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_65CAD728629B6E4501E261A44FA4551A3C29FF7E" ON teiv_data."235E984E81B76ADCCFA68BDE09D3BBE49A355919" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_B38A3DE6633390FD5D88720054D2659A52D9E5D9" ON teiv_data."235E984E81B76ADCCFA68BDE09D3BBE49A355919" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_DF65ACAC403E061AF151ECC6E1A0B5251A57663A" ON teiv_data."28C9A375E800E82308EBE7DA2932EF2C0AF13C38" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_FC784BD0FA3BEBD9F5C8333C558B7A0BE874E9B4" ON teiv_data."28C9A375E800E82308EBE7DA2932EF2C0AF13C38" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_9B2B02F454ADC81B20DF50A78854FDDAE1E07781" ON teiv_data."28C9A375E800E82308EBE7DA2932EF2C0AF13C38" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_A784A72DDC0B6161C3C40CC809908B28CBA3586C" ON teiv_data."28C9A375E800E82308EBE7DA2932EF2C0AF13C38" USING GIN (("REL_CD_1F61FA6DDAECE90540F9880F2A98037B1530A5A4"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_411D5CAD37663D02499EB24445A21693A234FFDE" ON teiv_data."28C9A375E800E82308EBE7DA2932EF2C0AF13C38" USING GIN (("REL_CD_E388983F3E6BFAD67CA100F0AFCF8CD3E9B89ADD"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_B0FFEC36366A5E403AADCA0B9DCEB13F511B226F" ON teiv_data."28C9A375E800E82308EBE7DA2932EF2C0AF13C38" USING GIN ("REL_CD_EF3979E9DAF31B7949C883654633E633B9D35B92");

CREATE INDEX IF NOT EXISTS "IDX_9375E42087BDE5DF649B8F30DA3DEE96D07C863C" ON teiv_data."53017288F3FE983848689A3DD21D48D298CCD23E" USING GIN (("antennaBeamWidth"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_822F9AB2F47067FDB88F9C322BC6A2B3B6C947B9" ON teiv_data."53017288F3FE983848689A3DD21D48D298CCD23E" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_FF1E0F629EA6972551FCE6D99834A31FE5A7FB10" ON teiv_data."53017288F3FE983848689A3DD21D48D298CCD23E" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_734E3F8FAD3A1B010D7E2095EA1357D4CEABD249" ON teiv_data."53017288F3FE983848689A3DD21D48D298CCD23E" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_629B2C2E73E2B5BF5BB31BB816EE0597484360CD" ON teiv_data."53089669D370B15C78B7E8376D434921D1C94240" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_2B2F5C1C9635A1AF418B85A486601087C420E8F9" ON teiv_data."53089669D370B15C78B7E8376D434921D1C94240" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_0A8C3B8A6C6FC1C4C60C47C3996F65D200F2CDAC" ON teiv_data."53089669D370B15C78B7E8376D434921D1C94240" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_7DACD641D4ED26579A7D8F94C74447B19D067ECD" ON teiv_data."83C555BD948277E4A7C99698690BF800604C0603" USING GIN (("eUtranFqBands"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_BC63EFD4857126FFC64E88F6136B85CA31E2A0EB" ON teiv_data."83C555BD948277E4A7C99698690BF800604C0603" USING GIN (("geranFqBands"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_E754CEE6CB27BA7BE1ECB90F717B2C241381B00F" ON teiv_data."83C555BD948277E4A7C99698690BF800604C0603" USING GIN (("nRFqBands"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_337B9980AA0401C715A372CF8615E73F826B9DAE" ON teiv_data."83C555BD948277E4A7C99698690BF800604C0603" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_DD78610CF631FE9F7AEB4068C0A4D8A40234DB31" ON teiv_data."83C555BD948277E4A7C99698690BF800604C0603" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_572A048D2B70E8CE7B919179CE285264951E6057" ON teiv_data."83C555BD948277E4A7C99698690BF800604C0603" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_5AEF72C5C2C83452A1BC8535E3EFF6E2AC9F4E02" ON teiv_data."84E676149362F50C55FE1E004B98D4891916BBF3" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_941E47F8B0E1F353F356F09A7D4EED3670560681" ON teiv_data."84E676149362F50C55FE1E004B98D4891916BBF3" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_C6135512BCBC5F91EAD1D324759ADD1B88901FE9" ON teiv_data."84E676149362F50C55FE1E004B98D4891916BBF3" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_BF805D12F8B66196DC7513960E3872DCFCCCA31E" ON teiv_data."84E676149362F50C55FE1E004B98D4891916BBF3" USING GIN (("REL_CD_32FFD9EF85AB2342E652FA493C3BF34D9BAAF054"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_8A9B70E4E83E34700456A8395E18BE2F7E793F20" ON teiv_data."84E676149362F50C55FE1E004B98D4891916BBF3" USING GIN (("REL_CD_B614AAA814176BC7128CE9D72C0950C4D6DE8052"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_6BEF4595F68DF48B662E68003C784CD6D944087B" ON teiv_data."84E676149362F50C55FE1E004B98D4891916BBF3" USING GIN ("REL_CD_0AC0D382E4274681870EC7319460192F4F603001");

CREATE INDEX IF NOT EXISTS "IDX_E1ACC551BE0D4D70B82C930C793C0C3751006444" ON teiv_data."9759D655D5978786B3284FE8057D912192AEB550" USING GIN ("dUpLMNId");

CREATE INDEX IF NOT EXISTS "IDX_8F11B2E5857B19A3F9FB339F4DFB139751097100" ON teiv_data."9759D655D5978786B3284FE8057D912192AEB550" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_AA040BB82CC5AD00E57E39F729D6AB3DF6CCA9F8" ON teiv_data."9759D655D5978786B3284FE8057D912192AEB550" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_98DD6CE7BB866A4EA737CE96FF985936134D39C5" ON teiv_data."9759D655D5978786B3284FE8057D912192AEB550" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_0648B3E68F701DC7FDC41A87DE0D79FB167AFB78" ON teiv_data."9759D655D5978786B3284FE8057D912192AEB550" USING GIN (("REL_CD_45E8E8693B1B8928376EAA8995D08AA7B1E483BD"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_FF9E656D3D0CB257F97FDDCB728E7637783C889B" ON teiv_data."9759D655D5978786B3284FE8057D912192AEB550" USING GIN (("REL_CD_7E9F11EFBF8974D7C7DAB02E181A0BE4148091C6"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_DED8DFAF8F597276CAA4680444C6A069A0570510" ON teiv_data."9759D655D5978786B3284FE8057D912192AEB550" USING GIN ("REL_CD_FFF7E036187A7605BFC714483D2E60FD2FF6560B");

CREATE INDEX IF NOT EXISTS "IDX_389F565ED970F5EB2FAC8A94766B2A60E5DEB022" ON teiv_data."C278F34A021D7A9AD13BE860608751874F21C00C" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_2888BA890DB44CD93287CAAF0695047CF39B736B" ON teiv_data."C278F34A021D7A9AD13BE860608751874F21C00C" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_C65D145DDF1B1222AB4DFF08B0F504605F3E8468" ON teiv_data."C278F34A021D7A9AD13BE860608751874F21C00C" USING GIN ("CD_decorators");

CREATE INDEX IF NOT EXISTS "IDX_839D67DA1D3875780CA8C2EC7DFD39C4F0A7D666" ON teiv_data."C278F34A021D7A9AD13BE860608751874F21C00C" USING GIN (("REL_CD_8D8BBB53FB18E8E9A5D241B66DA18CB5DDE9C1A5"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_A3CD76C492D00CAAE49571191E3270D53FA818EE" ON teiv_data."C278F34A021D7A9AD13BE860608751874F21C00C" USING GIN (("REL_CD_68C234330FD6388836D0DAF9DFD0A40DE66DD8C5"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_8D9A8B3F7A4B33D0D985B1F13D210F71277AACBC" ON teiv_data."C278F34A021D7A9AD13BE860608751874F21C00C" USING GIN ("REL_CD_A14923FFF9D13FEB18087CE2A9C0BD264C572CFC");

CREATE INDEX IF NOT EXISTS "IDX_E61FF1756D360B904A01B032904F380F9D4A8650" ON teiv_data."CE18CE5B725879B4016F5C281D80D37A95EA7E28" USING GIN (("CD_sourceIds"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_AF0E12037BDEE1B45F3F902B29E0823D713DBB06" ON teiv_data."CE18CE5B725879B4016F5C281D80D37A95EA7E28" USING GIN (("CD_classifiers"::TEXT) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS "IDX_A2E437FF2DD6096051A873525763FD9EF7709350" ON teiv_data."CE18CE5B725879B4016F5C281D80D37A95EA7E28" USING GIN ("CD_decorators");

ANALYZE teiv_data."CE18CE5B725879B4016F5C281D80D37A95EA7E28";

ANALYZE teiv_data."9759D655D5978786B3284FE8057D912192AEB550";

ANALYZE teiv_data."53089669D370B15C78B7E8376D434921D1C94240";

ANALYZE teiv_data."84E676149362F50C55FE1E004B98D4891916BBF3";

ANALYZE teiv_data."C278F34A021D7A9AD13BE860608751874F21C00C";

ANALYZE teiv_data."28C9A375E800E82308EBE7DA2932EF2C0AF13C38";

ANALYZE teiv_data."83C555BD948277E4A7C99698690BF800604C0603";

ANALYZE teiv_data."235E984E81B76ADCCFA68BDE09D3BBE49A355919";

ANALYZE teiv_data."53017288F3FE983848689A3DD21D48D298CCD23E";

COMMIT;