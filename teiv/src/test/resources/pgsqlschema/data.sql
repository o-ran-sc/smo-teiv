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
COPY teiv_data."responsible_adapter" ("id", "hashed_id") FROM stdin;
orannamespace-ran-topology-adapter	\\xff10444ea1540885f3e34b3670be8bbfd6a780c6
namespace-ran-topology-adapter	\\x5f270b1ee231080eb5ffd2e04077cdcfe60e018a
test-namespace-test-adapter-id	\\x41131feea09d9620d911657a710803ea92414059
ran-topology-adapter	\\x5b35d8c6c5f8e8acb48912b8f328ebc9b6924e03
\.

COPY teiv_data."o-ran-smo-teiv-oam_ManagedElement" ("id", "CD_sourceIds", "CD_classifiers", "CD_decorators", "metadata") FROM stdin;
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9", "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"]	[]	{}	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10", "urn:cmHandle:72FDA73D085F138FECC974CB91F1450E"]	[]	{}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13", "urn:cmHandle:E5196035D0B49A65B00EAA392B4EE155"]	[]	{}	{"reliabilityIndicator":"ADVISED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14", "urn:cmHandle:D67C0BD04FA613BBFD176B24B68FD6A4"]	[]	{}	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16", "urn:cmHandle:453431CC154F900606657D584700827A"]	[]	{}	{"reliabilityIndicator":"ADVISED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19", "urn:cmHandle:03661FA2E41EF3D12CAAD5954CD985AC"]	[]	{}	{"reliabilityIndicator":"ADVISED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=28	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=28", "urn:cmHandle:30C68865AF2F353F202056CB1921D418"]	[]	{}	{"reliabilityIndicator":"ADVISED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
\.

COPY teiv_data."o-ran-smo-teiv-ran_Sector" ("id", "geo-location", "sectorId", "azimuth", "CD_sourceIds", "CD_classifiers", "CD_decorators", "metadata") FROM stdin;
urn:Sector=1	POINT(59.4019881 17.9419888)	1	1.1	["source1", "source2"]	[]	{}	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:Sector=2	POINT(60.4019881 18.9419888)	2	2.2	["source1", "source2"]	[]	{}	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:Sector=3	POINT(61.4019881 19.9419888)	3	3.3	["source1", "source2"]	[]	{}	{"reliabilityIndicator":"ADVISED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
\.

COPY teiv_data."o-ran-smo-teiv-ran_AntennaCapability" ("id", "eUtranFqBands", "geranFqBands", "nRFqBands", "CD_sourceIds", "CD_classifiers", "CD_decorators", "metadata") FROM stdin;
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,NodeSupport=1,SectorEquipmentFunction=1	["123","456","789"]	["123","4564","789"]	["123","456","789"]	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,NodeSupport=1,SectorEquipmentFunction=1", "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"]	[]	{}	{"reliabilityIndicator":"INVALID","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,NodeSupport=1,SectorEquipmentFunction=1	["123","456","789"]	["123","4564","789"]	["123","456","789"]	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,NodeSupport=1,SectorEquipmentFunction=1", "urn:cmHandle:03661FA2E41EF3D12CAAD5954CD985AC"]	[]	{}	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
\.

COPY teiv_data."o-ran-smo-teiv-equipment_AntennaModule" ("id", "antennaBeamWidth", "antennaModelNumber", "electricalAntennaTilt", "geo-location", "mechanicalAntennaBearing", "mechanicalAntennaTilt", "positionWithinSector", "totalTilt", "CD_sourceIds", "CD_classifiers", "CD_decorators", "REL_FK_installed-at-site", "REL_ID_ANTENNAMODULE_INSTALLED_AT_SITE", "REL_CD_sourceIds_ANTENNAMODULE_INSTALLED_AT_SITE", "REL_CD_classifiers_ANTENNAMODULE_INSTALLED_AT_SITE", "REL_CD_decorators_ANTENNAMODULE_INSTALLED_AT_SITE", "REL_FK_grouped-by-sector", "REL_ID_SECTOR_GROUPS_ANTENNAMODULE", "REL_CD_sourceIds_SECTOR_GROUPS_ANTENNAMODULE", "REL_CD_classifiers_SECTOR_GROUPS_ANTENNAMODULE", "REL_CD_decorators_SECTOR_GROUPS_ANTENNAMODULE", "REL_metadata_ANTENNAMODULE_INSTALLED_AT_SITE", "REL_metadata_SECTOR_GROUPS_ANTENNAMODULE", "metadata" ) FROM stdin;
urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A	\N	5	11	POINT(39.4019881 67.9419888)	6	0	7	-900	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=1,RetSubUnit=1", "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"]	[]	{}	\N	\N	[]	[]	{}	urn:Sector=2	urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_ANTENNAMODULE=44F4F4FC906E9A7525065E4565246F7469CBD11FC7752C61EA6D74776845900AFF472DCAACA1F66443490B6CE0DD9AC9A5E1467022118599F6B4C6EC63400512	[]	[]	{}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7	\N	5	11	POINT(49.4019881 68.9419888)	6	0	7	-900	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=1,RetSubUnit=1", "urn:cmHandle:03661FA2E41EF3D12CAAD5954CD985AC"]	[]	{}	\N	\N	[]	[]	{}	urn:Sector=2	urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_ANTENNAMODULE=CEEC51BE136D671D2101C09FEDD8A1D95E1E177A4818E9FC0D6E63E610BC8FE26FC9C729A1E58AD43D70472F4CD54403E25CB1E5D2BBA66966625C21435C4A78	[]	[]	{}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	{"reliabilityIndicator":"ADVISED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:o-ran:smo:teiv:sha512:AntennaModule=72AC3D5E2A5F1C47BD09258A9F7B48E0123E9AD752AC54F7E8D8F9D3A6BC487A89A762A5D12CB9D148BB9E5D53A4F3F981345ACDF7B4CB55D67BC12A13FD5B7A	\N	5	11	POINT Z (47.497913 19.040236 111.1)	4	2	5	-850	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=1,RetSubUnit=1", "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"]	[]	{}	\N	\N	[]	[]	{}	urn:Sector=3	urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_ANTENNAMODULE=67C8D3C4E7B9A2F4C0A7D1B4E3F8D7C9E6F8C3A9D4F2E5B1C7D2F5C8B1D2F6C7A5E7B3C4A1D2E3F7B5A9C7D2F5B8C1D2F7C8A9D4E3F8D7A1E3B5C7	[]	[]	{}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	{}	{"reliabilityIndicator":"ADVISED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:o-ran:smo:teiv:sha512:AntennaModule=84A3E5D7C916F4B2390DC45F178BE6A9235FD80CB41972E3456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123	\N	5	11	POINT(-49.4019881 -68.9419888)	6	0	5	-900	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=1,RetSubUnit=1", "urn:cmHandle:03661FA2E41EF3D12CAAD5954CD985AC"]	[]	{}	\N	\N	[]	[]	{}	urn:Sector=2	urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_ANTENNAMODULE=DAFC42AF147B582C3212D1AEFEEB92EA6F2F288B5929FAED1E7F74F721CD9FF37FDAD83AB2F69BE54E81583F5DE65514F36DC2F6E3CCB77A77736D32546D5B89	[]	[]	{}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	{}	{"reliabilityIndicator":"ADVISED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:o-ran:smo:teiv:sha512:AntennaModule=B7F52C914E8D36A0185BC9D47EF230A95C681D7B4F923E0A5D8C741F6B9203E85A4D967B312C8F405E9B7831A6D2C5904F8B3E167A9D204C5B8371F9E6A02D45	\N	5	11	POINT Z (-47.497913 -19.040236 -111.1)	4	2	5	-850	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=1,RetSubUnit=1", "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"]	[]	{}	\N	\N	[]	[]	{}	urn:Sector=3	urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_ANTENNAMODULE=BFFB53CF258C693D4323E2BFFFFC93FB7F3F399C6939FBFE2F8F85F832DE9FF48FEAE94BC3F79CF65F92694F6EF76625F47ED3F7F4DDC88B88847E43657E6C9A	[]	[]	{}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	{}	{"reliabilityIndicator":"ADVISED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
\.

COPY teiv_data."o-ran-smo-teiv-ran_OCUCPFunction" ("id", "gNBCUName", "gNBId", "gNBIdLength", "pLMNId", "CD_sourceIds", "CD_classifiers", "CD_decorators", "REL_FK_managed-by-managedElement", "REL_ID_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION", "REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION", "REL_CD_classifiers_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION", "REL_CD_decorators_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION", "REL_metadata_MANAGEDELEMENT_MANAGES_OCUCPFUNCTION") FROM stdin;
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,OCUCPFunction=9	ocucp-9	9	1	{"mcc":"01","mnc":"234"}	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,OCUCPFunction=9", "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9	urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_OCUCPFUNCTION=0F7F3F3CEC7B3CDA05A7B7D2874D1EF39EBDAA18AD7D6F43CF219C087510114C59C6B78EC21F8E9C6F19B5F1999FBBA2DF8C3DDF76F416C874508303F0DA4AB4	[]	[]	{}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,OCUCPFunction=10	ocucp-10	10	2	{"mcc":"01","mnc":"234"}	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,OCUCPFunction=10", "urn:cmHandle:72FDA73D085F138FECC974CB91F1450E"]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10	urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_OCUCPFUNCTION=7053BF6EEB9769084BB91850C356BF20E3C9D6AD8F0D7212911DC827AD1B4D42AEDA0C43FD5715C94E14334EF49FA09405A976451B777B442BBF397DE89528A4	[]	[]	{}	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,OCUCPFunction=13	ocucp-13	13	2	{"mcc":"01","mnc":"234"}	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,OCUCPFunction=13", "urn:cmHandle:E5196035D0B49A65B00EAA392B4EE155"]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13	urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_OCUCPFUNCTION=C88ACE9CD7BA7E56FD059C027DA8E4D0ED0A3E13F9E358D5F4A66EE004FC3767A9D20B0512661B6D2F5F82F106725C04C5DC8826D990DECB4D5AD571BE402BE0	[]	[]	{}	{"reliabilityIndicator":"ADVISED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,OCUCPFunction=14	ocucp-14	14	2	{"mcc":"01","mnc":"234"}	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,OCUCPFunction=14", "urn:cmHandle:D67C0BD04FA613BBFD176B24B68FD6A4"]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14	urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_OCUCPFUNCTION=F23FADEDC45AE38DBC567C57FC4DD5D61D239B0BEF3C93DD54DF85545E6D0C8B9B26A1D3911B56A3F8C2EB148A4F276D1EBAF7EE2D2E35C8B37F008F572DF7B6	[]	[]	{}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,OCUCPFunction=16	ocucp-16	16	2	{"mcc":"01","mnc":"234"}	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,OCUCPFunction=16", "urn:cmHandle:453431CC154F900606657D584700827A"]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16	urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_OCUCPFUNCTION=5ECE0941DDBC9B4DEE8492333129A0AB80720CB6005A80B54C4D9247029A41FA42DB6D2C709F71D7ED5D82F5EA90CE2C0B553AC1BD860D8A6DFA218E2E790F1C	[]	[]	{}	{"reliabilityIndicator":"ADVISED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,OCUCPFunction=19	ocucp-19	19	2	{"mcc":"01","mnc":"234"}	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,OCUCPFunction=19", "urn:cmHandle:03661FA2E41EF3D12CAAD5954CD985AC"]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19	urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_OCUCPFUNCTION=63EA5A9F77FE5DED61CF6DC30233AB17A57D6E04283365D7C1976FF646961FAAF1889BBA618029EA10DA8761F8DAA643B707B602D4E61898A2B5259AA0118887	[]	[]	{}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=28,OCUCPFunction=28	ocucp-28	28	2	{"mcc":"01","mnc":"234"}	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=28,OCUCPFunction=28", "urn:cmHandle:30C68865AF2F353F202056CB1921D418"]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=28	urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_OCUCPFUNCTION=CD564BF182FD6DE77816BA69F25B664374AD02D642DF05FF8991E1009E7E09C50E92F6858A1E1B025CFDA933AB769B8C68FBAE0DBBAA140AE321DC55AED6C2A3	[]	[]	{}	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
\.

COPY teiv_data."o-ran-smo-teiv-ran_OCUUPFunction" ("id", "gNBId", "gNBIdLength", "CD_sourceIds", "CD_classifiers", "CD_decorators", "REL_FK_managed-by-managedElement", "REL_ID_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION", "REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION", "REL_CD_classifiers_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION", "REL_CD_decorators_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION", "REL_metadata_MANAGEDELEMENT_MANAGES_OCUUPFUNCTION", "metadata") FROM stdin;
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,OCUUPFunction=9	9	1	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,OCUUPFunction=9", "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9	urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_OCUUPFUNCTION=5255F37093F8EB3763CE5F017DFC1E162B44FC9DF6E13744C04DC1832C5E754AB7BE440DBE1187EE8EEE42FD04E652BB8148655C6F977B1FFDDA54FE87C6411A	[]	["ocucp-ocuup-model:Weekend"]	{"ocucp-ocuup-model:metadata" : "value1"}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,OCUUPFunction=10	10	2	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,OCUUPFunction=10", "urn:cmHandle:72FDA73D085F138FECC974CB91F1450E"]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10	urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_OCUUPFUNCTION=80D3B4C44B4F0BEFC7660AF0A4E91F89C8108DA814B09728F848C0C5C10E2D956A73FBC85EB2AE0A7EA4D95308A606856603B53C8C2669A50BCB58B9FC87D7F2	[]	[]	{}	{}	{"reliabilityIndicator":"ADVISED","foo":"bar","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,OCUUPFunction=13	13	2	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,OCUUPFunction=13", "urn:cmHandle:E5196035D0B49A65B00EAA392B4EE155"]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13	urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_OCUUPFUNCTION=29DE1D3B8DA9C9FA1201C36F74FEBE14493F7C674E47E1FFCB6AADEED8EABB0460770EC21E7AC8EEBBB057ABC0E31269AB5C92D941E9BA53877164918C6EFB30	[]	[]	{}	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	{"reliabilityIndicator":"RESTORED","foo":"bar","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,OCUUPFunction=14	14	2	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,OCUUPFunction=14", "urn:cmHandle:D67C0BD04FA613BBFD176B24B68FD6A4"]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14	urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_OCUUPFUNCTION=4C571BDD5DF9B297C1B249D0242EB9DDC77052BB0A33E62DB851809A075259EE3A0C354FA3978276BD5EE0BBB8CFDBF19F7C3C7017F828B9A2EBAD020E7FDF98	[]	[]	{}	{"reliabilityIndicator":"RESTORED","foo":"bar","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	{"reliabilityIndicator":"ADVISED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,OCUUPFunction=16	16	2	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,OCUUPFunction=16", "urn:cmHandle:453431CC154F900606657D584700827A"]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16	urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_OCUUPFUNCTION=DD5E12CB8D14C89E1C199BDABBCC71908F76AB04FE8FD7F7334A9A403B5BE0D8BB5DA69B4258C7AF4834BF2D3E00B6D5C263AD38A83A22E7EC673ACE1CAF0CF2	[]	[]	{}	{"reliabilityIndicator":"RESTORED","foo":"bar","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,OCUUPFunction=19	19	2	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,OCUUPFunction=19", "urn:cmHandle:03661FA2E41EF3D12CAAD5954CD985AC"]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19	urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_OCUUPFUNCTION=2AF48E294918A4EE0E3E7C783A70841E1DEEF72CDD43FE3CD1DF6531CCA06203B574BE02553254220A637632AECB719951DAE2A3D0487D24EA1A090843563603	[]	[]	{}	{"reliabilityIndicator":"ADVISED","foo":"bar","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	{"foo":"bar","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=28,OCUUPFunction=28	28	2	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=28,OCUUPFunction=28", "urn:cmHandle:30C68865AF2F353F202056CB1921D418"]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=28	urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_OCUUPFUNCTION=140E8C8C56B1E717CD992D3FF0C61C04C1633E1FEF17ABB92A9A17C53E18CFAF5D72C4C71415E0026C99671657FB3EC1BD394174DD306261ADA8A6CDF4D42748	[]	[]	{}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	{"reliabilityIndicator":"ADVISED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
\.

COPY teiv_data."o-ran-smo-teiv-ran_ODUFunction" ("id", "dUpLMNId", "gNBDUId", "gNBId", "gNBIdLength", "CD_sourceIds", "CD_classifiers", "CD_decorators", "REL_FK_managed-by-managedElement", "REL_ID_MANAGEDELEMENT_MANAGES_ODUFUNCTION", "REL_CD_sourceIds_MANAGEDELEMENT_MANAGES_ODUFUNCTION", "REL_CD_classifiers_MANAGEDELEMENT_MANAGES_ODUFUNCTION", "REL_CD_decorators_MANAGEDELEMENT_MANAGES_ODUFUNCTION", "metadata", "REL_metadata_MANAGEDELEMENT_MANAGES_ODUFUNCTION") FROM stdin;
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9	{"mcc":"123","mnc":"82"}	\N	9	1	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9", "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"]	["test-app-module:Indoor","test-app-module:Weekend"]	{"test-app-module:textdata":"ORAN"}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9	urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=9243B48F7D6A6CF471226915C74CF5FE4BDA6FA3CF7D897473007B46DF7FC50230BD6B8B4256116A6AFBF4D822CF9379EB56DE9490C1C0B54238263F2574B426	[]	["test-app-module:Indoor","test-app-module:Weekend"]	{"test-app-module:textdata":"ORAN"}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-09T10:40:36.461565Z","lastModified":"2025-01-09T10:40:36.461565Z"}	{"reliabilityIndicator":"ADVISED","firstDiscovered":"2025-01-09T10:40:36.461565Z","lastModified":"2025-01-09T10:40:36.461565Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10	{"mcc":"456","mnc":"83"}	\N	10	2	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10", "urn:cmHandle:72FDA73D085F138FECC974CB91F1450E"]	[]	{"test-app-module:textdata":"Budapest","test-app-module:intdata":123}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10	urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=661A89AD3C2702233CD9E96E97E738C05C35EC5FDF32DC78D149B773726350067315B72448D004C938BCD0263F0C4BCCC8A5F9CDD145B9B740983D1523664328	[]	["test-app-module:Rural","test-app-module:Weekend"]	{"test-app-module:textdata":"Budapest","test-app-module:intdata":123}	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-09T11:40:36.461565Z","lastModified":"2025-01-09T11:40:36.461565Z"}	{"reliabilityIndicator":"ADVISED","firstDiscovered":"2025-01-09T11:40:36.461565Z","lastModified":"2025-01-09T11:40:36.461565Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,ODUFunction=13	{"mcc":"789","mnc":"84"}	\N	13	2	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13,ODUFunction=13", "urn:cmHandle:E5196035D0B49A65B00EAA392B4EE155"]	["test-app-module:Indoor"]	{"test-app-module:textdata":"Stockholm","test-app-module:intdata":456}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=13	urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=DD9259A1B57FF2BB9DEC77C29DBFA4A5C49960D80622F603809ACA47E786DDD5C7ABD267D554A7C796477A9B2E02E072A8E682E4ED38F331BFB6DC3827CE4DB7	[]	[]	{"test-app-module:textdata":"Stockholm","test-app-module:intdata":456}	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-11T23:40:36.461565Z","lastModified":"2025-01-11T23:40:36.461565Z"}	{"reliabilityIndicator":"ADVISED","firstDiscovered":"2025-01-11T23:40:36.461565Z","lastModified":"2025-01-11T23:40:36.461565Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,ODUFunction=14	{"mcc":"123","mnc":"85"}	\N	14	2	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14,ODUFunction=14", "urn:cmHandle:D67C0BD04FA613BBFD176B24B68FD6A4"]	["test-app-module:Rural","test-app-module:Weekend"]	{"test-app-module:textdata":"Stockholm","test-app-module:intdata":123}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=14	urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=6F7BFBD3DA2A9A592084C75242210A33C9DCF10CFCD53B761A6ACCD385132921679EC3C16394A4DEEE5883712C9719511388230151BA84FBF209DFCFB639E2EA	[]	[]	{"test-app-module:textdata":"Stockholm","test-app-module:intdata":123}	{"reliabilityIndicator":"ADVISED","firstDiscovered":"2025-01-11T09:40:36.461565Z","lastModified":"2025-01-11T09:40:36.461565Z"}	{"reliabilityIndicator":"ADVISED","firstDiscovered":"2025-01-11T09:40:36.461565Z","lastModified":"2025-01-11T09:40:36.461565Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,ODUFunction=16	{"mcc":"456","mnc":"86"}	16	16	2	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16,ODUFunction=16", "urn:cmHandle:453431CC154F900606657D584700827A"]	["test-app-module:Indoor","test-app-module:Rural","test-app-module:Weekend"]	{"test-app-module:textdata":"Stockholm","test-app-module:intdata":123}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=16	urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=D67357F682531C7B068486313B0FDAC3E719A166229520196FB9CE917E0236754226A5BCBF7BB7240E516D7ED3FEA852855EC3F121DD4BAFEC5646F2A37F57EE	[]	["test-app-module:Indoor","test-app-module:Rural","test-app-module:Weekend"]	{"test-app-module:textdata":"Stockholm","test-app-module:intdata":123}	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-12T21:40:36.461565Z","lastModified":"2025-01-12T21:40:36.461565Z"}	{}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19	{"mcc":"789","mnc":"87"}	\N	19	2	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19", "urn:cmHandle:03661FA2E41EF3D12CAAD5954CD985AC"]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19	urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=6BD25E5C8FB7842F69010736253CC47F43535D7238E9E9A03E8092E8C019C83270DE47C96EF1049C40B83A130F9F129AE93B9C8538B6B004AE89BD0A098E48DD	[]	[]	{}	{}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-13T16:40:36.461565Z","lastModified":"2025-01-13T16:40:36.461565Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=28,ODUFunction=28	{"mcc":"123","mnc":"88"}	\N	28	2	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=28,ODUFunction=28", "urn:cmHandle:30C68865AF2F353F202056CB1921D418"]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=28	urn:o-ran:smo:teiv:sha512:MANAGEDELEMENT_MANAGES_ODUFUNCTION=ADD4A82DFBAF0409FA9D3C929A09314088627B447C733429D4EE7AAE2FFAEE4894F90826B6814B63431EC07140783C7861E463C5AF8330E29469D704675EAB43	[]	[]	{}	{}	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-14T12:40:36.461565Z","lastModified":"2025-01-14T12:40:36.461565Z"}
\.

COPY teiv_data."o-ran-smo-teiv-ran_NRCellDU" ("id", "cellLocalId", "nCI", "nRPCI", "nRTAC", "CD_sourceIds", "CD_classifiers", "CD_decorators", "REL_FK_provided-by-oduFunction", "REL_ID_ODUFUNCTION_PROVIDES_NRCELLDU", "REL_CD_sourceIds_ODUFUNCTION_PROVIDES_NRCELLDU", "REL_CD_classifiers_ODUFUNCTION_PROVIDES_NRCELLDU", "REL_CD_decorators_ODUFUNCTION_PROVIDES_NRCELLDU", "REL_FK_grouped-by-sector", "REL_ID_SECTOR_GROUPS_NRCELLDU", "REL_CD_sourceIds_SECTOR_GROUPS_NRCELLDU", "REL_CD_classifiers_SECTOR_GROUPS_NRCELLDU", "REL_CD_decorators_SECTOR_GROUPS_NRCELLDU", "REL_metadata_ODUFUNCTION_PROVIDES_NRCELLDU", "REL_metadata_SECTOR_GROUPS_NRCELLDU") FROM stdin;
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1	1	1	789	456	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1", "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9	urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=4E40BE000AFEA418CE1B9ED9E21D38DA51772175BD498BE825D9EA362F9B7393C36AB72F6FDEE702439143D578268A2E84719A9352C8EA70F847B7B7664E047C	[]	[]	{}	urn:Sector=2	urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_NRCELLDU=4BBE73F685A98EF799968ACFE76F376D795F4CC3B56A6B867642048CDF4C1B8E323430EA7C6C38E4031FB891158763CC5459A8704E1A9FBFBD53CE8AD23BF463	[]	[]	{}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2	2	2	789	456	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2", "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9	urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=78ECC09D4832328976EF0F9C19699EE05D98E3837368D386AE39AD027543494AC620086BD2A7403DACFAA7B474B3DEBD313E0906F1EDE7FA2B584E16542A706A	[]	[]	{}	urn:Sector=2	urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_NRCELLDU=46AAB2CC5388BECD7B2180C89EEFA64B9A3BC197B614B57FD4BD9ADACE2475A89E16BA04291DE1674FAF2925483E23B8EDCAD4EE98759A9C08E2677CD88F4C43	[]	[]	{}	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=3	3	3	789	456	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=3", "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9	urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=714C1B73945C298CAA03FE0D800053CDD1C571BBF375DC647B9F23FDA861CEB369832A3593BB1AA4B8A7245AD187ED24ADDF6FB147130827CDC17BA8370C4838	[]	[]	{}	urn:Sector=2	urn:o-ran:smo:teiv:sha512:SECTOR_GROUPS_NRCELLDU=F892571703F0E20A37F3950818DEFA9991ACF35828EEEBD3E43404218F947E1F522258A1F31F4C82A53E7E60D9E1A7AC7AC4219A0D9DD0D8FD192BC73BBB5101	[]	[]	{}	{"reliabilityIndicator":"OK","foo":"bar","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	{"reliabilityIndicator":"OK","foo":"bar","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=91	91	91	789	456	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=91", "urn:cmHandle:03661FA2E41EF3D12CAAD5954CD985AC"]	[]	{}	\N	\N	[]	[]	{}	\N	\N	[]	[]	{}	{"reliabilityIndicator":"RESTORED","foo":"bar","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	{"reliabilityIndicator":"RESTORED","foo":"bar","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=92	92	92	789	456	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=92", "urn:cmHandle:03661FA2E41EF3D12CAAD5954CD985AC"]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19	urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=DDECCEFB8831FA4EB21B121BA35EAB07ED8D841B5A38580C5F3AD11E66FE73D2FC42E823C6C73288860C7562B610C3D07B6C39FD386171A3BE622096F4B3D006	[]	[]	{}	\N	\N	[]	[]	{}	{"reliabilityIndicator":"ADVISED","foo":"bar","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	{"reliabilityIndicator":"ABC","foo":"bar","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=93	93	93	789	456	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19,NRCellDU=93", "urn:cmHandle:03661FA2E41EF3D12CAAD5954CD985AC"]	["test-app-module:Rural","test-app-module:Weekend"]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,ODUFunction=19	urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRCELLDU=7723E5D5B3332E0890EAA620C77A6A47065E15A2EA28AD83F3B3CFEA5A7E3BB5965AE78890F1BF000EAA89BF8DE209E506192BF5EA6871426603ED76CBFAF088	[]	[]	{}	\N	\N	[]	[]	{}	{"reliabilityIndicator":"RESTORED","foo":"bar","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}	{"reliabilityIndicator":"RESTORED","foo":"bar","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
\.

COPY teiv_data."o-ran-smo-teiv-ran_NRSectorCarrier" ("id", "arfcnDL", "arfcnUL", "bSChannelBwDL", "frequencyDL", "frequencyUL", "CD_sourceIds", "CD_classifiers", "CD_decorators", "REL_FK_provided-by-oduFunction", "REL_ID_ODUFUNCTION_PROVIDES_NRSECTORCARRIER", "REL_CD_sourceIds_ODUFUNCTION_PROVIDES_NRSECTORCARRIER", "REL_CD_classifiers_ODUFUNCTION_PROVIDES_NRSECTORCARRIER", "REL_CD_decorators_ODUFUNCTION_PROVIDES_NRSECTORCARRIER", "REL_FK_used-by-nrCellDu", "REL_ID_NRCELLDU_USES_NRSECTORCARRIER", "REL_CD_sourceIds_NRCELLDU_USES_NRSECTORCARRIER", "REL_CD_classifiers_NRCELLDU_USES_NRSECTORCARRIER", "REL_CD_decorators_NRCELLDU_USES_NRSECTORCARRIER", "REL_FK_used-antennaCapability", "REL_ID_NRSECTORCARRIER_USES_ANTENNACAPABILITY", "REL_CD_sourceIds_NRSECTORCARRIER_USES_ANTENNACAPABILITY", "REL_CD_classifiers_NRSECTORCARRIER_USES_ANTENNACAPABILITY", "REL_CD_decorators_NRSECTORCARRIER_USES_ANTENNACAPABILITY", "REL_metadata_ODUFUNCTION_PROVIDES_NRSECTORCARRIER", "REL_metadata_NRCELLDU_USES_NRSECTORCARRIER", "REL_metadata_NRSECTORCARRIER_USES_ANTENNACAPABILITY", "metadata") FROM stdin;
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRSectorCarrier=1	507000	507000	10000	2535000	2535000	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRSectorCarrier=1", "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"]	["ocucp-ocuup-model:Weekend"]	{"ocucp-ocuup-model:metadata":"value1"}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9	urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRSECTORCARRIER=BE61426CAEA457C85D8B551A1A75BFA157BBCE6F143110661C4C9D406A7AAF22D8522515CE924CFB3A9E54E68588A45D3A51065BD24ADBA62CC0FDA761AEE2FC	[]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1	urn:o-ran:smo:teiv:sha512:NRCELLDU_USES_NRSECTORCARRIER=7B9425BBD6977FEA6C180F6078CFBAEBE65400223B29E0EFA4F38424FAD66C690806778909177ECF1457CAC18E5BCF6FA4F24E3ECE524C89DE68108708D6D876	[]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,NodeSupport=1,SectorEquipmentFunction=1	urn:o-ran:smo:teiv:sha512:NRSECTORCARRIER_USES_ANTENNACAPABILITY=11EDFC31E2BE240D3CB15DB1A3FA3B78C828524BC8FCA3365A615129A61A627C21DA8EBF6DD788CDBDEC668344D1F79A371749083D6AE04DDDD57CB4FA8C3ECB	[]	[]	{}	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-13T16:40:36.461565Z","lastModified":"2025-01-13T16:40:36.461565Z"}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-13T16:40:36.461565Z","lastModified":"2025-01-13T16:40:36.461565Z"}	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-13T16:40:36.461565Z","lastModified":"2025-01-13T16:40:36.461565Z"}	{"reliabilityIndicator":"ADVISED","firstDiscovered":"2025-01-13T16:40:36.461565Z","lastModified":"2025-01-13T16:40:36.461565Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRSectorCarrier=2	507000	507000	10000	2535000	2535000	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRSectorCarrier=2", "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9	urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRSECTORCARRIER=C0E73797DB4599AB9ECACFC1FFE3543C92926070ECFBE77E7C15BA99DAFBB1D69352533D3DE5EB2D3D3CC84DAD51B242CB0FC594FF9E8B73C3B42106B0F9AF46	[]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2	urn:o-ran:smo:teiv:sha512:NRCELLDU_USES_NRSECTORCARRIER=2F87CE31F38D38B993786E3D75D253984DA2842F71504958AAF052D0728B309C73BB3132D6BEA011748BB0B94F489725DB5765AB5366702B812D5C76A772BD9C	[]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,NodeSupport=1,SectorEquipmentFunction=1	urn:o-ran:smo:teiv:sha512:NRSECTORCARRIER_USES_ANTENNACAPABILITY=27DF07D016FE349EC565DE2FB09303EE7D8700346624046F79D8DAC176E7FA221E918E3030758B51931C430919E14FD7D16720460F6E1585000C72874A1641DA	[]	[]	{}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-13T16:40:36.461565Z","lastModified":"2025-01-13T16:40:36.461565Z"}	{"reliabilityIndicator":"ADVISED","firstDiscovered":"2025-01-13T16:40:36.461565Z","lastModified":"2025-01-13T16:40:36.461565Z"}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-13T16:40:36.461565Z","lastModified":"2025-01-13T16:40:36.461565Z"}	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-13T16:40:36.461565Z","lastModified":"2025-01-13T16:40:36.461565Z"}
urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRSectorCarrier=3	507000	507000	10000	2535000	2535000	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRSectorCarrier=3", "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9	urn:o-ran:smo:teiv:sha512:ODUFUNCTION_PROVIDES_NRSECTORCARRIER=BD87BC547A6731B2905A989EBA493810C74258337C49BBB288F4F55734D28B4E40D9C719EC3564348253905BD93EC78EB7C88F2297FF20778911635E94800F74	[]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=3	urn:o-ran:smo:teiv:sha512:NRCELLDU_USES_NRSECTORCARRIER=950ED4540349F9859CEA9E47884A28CD567BDD2505A3C5335C8851A7AADF2AF65542157BB42D607EE3847E4223D76DE88B90762D0590E48693822FD6DCAE60CD	[]	[]	{}	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,NodeSupport=1,SectorEquipmentFunction=1	urn:o-ran:smo:teiv:sha512:NRSECTORCARRIER_USES_ANTENNACAPABILITY=1B891FCC4F5479BC71127ED2EB43EA26AC3452F8C47792786373442C10BBC408FE5B779BF1CF732C81220803342F4FB969E348F9C5CEEDEC78F9764E186C633F	[]	[]	{}	{"reliabilityIndicator":"ADVISED","foo":"bar","firstDiscovered":"2025-01-13T16:40:36.461565Z","lastModified":"2025-01-13T16:40:36.461565Z"}	{"reliabilityIndicator":"ADVISED","firstDiscovered":"2025-01-13T16:40:36.461565Z","lastModified":"2025-01-13T16:40:36.461565Z"}	{"reliabilityIndicator":"OK","foo":"bar","firstDiscovered":"2025-01-13T16:40:36.461565Z","lastModified":"2025-01-13T16:40:36.461565Z"}	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-13T16:40:36.461565Z","lastModified":"2025-01-13T16:40:36.461565Z"}
\.

COPY teiv_data."o-ran-smo-teiv-physical_PhysicalAppliance" ("id", "vendorName", "modelName", "CD_sourceIds", "CD_classifiers", "CD_decorators", "metadata") FROM stdin;
urn:o-ran:smo:teiv:PhysicalAppliance=135	"ORAN"	"ORAN-135"	["urn:o-ran:smo:teiv:PhysicalAppliance=135", "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"]	[]	{}	{}
urn:o-ran:smo:teiv:PhysicalAppliance=246	"ORAN"	"ORAN-246"	["urn:o-ran:smo:teiv:PhysicalAppliance=246", "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"]	[]	{}	{}
\.

COPY teiv_data."5A1D611A68E4A8B2F007A89876701DB3FA88346E" ("id", "aSide_PhysicalAppliance", "bSide_ODUFunction", "CD_sourceIds", "CD_classifiers", "CD_decorators", "metadata") FROM stdin;
urn:o-ran:smo:teiv:PHYSICALAPPLIANCE_SERVES_ODUFUNCTION=135	urn:o-ran:smo:teiv:PhysicalAppliance=135	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9	["urn:o-ran:smo:teiv:PhysicalAppliance=135", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9"]	[]	{}	{}
urn:o-ran:smo:teiv:PHYSICALAPPLIANCE_SERVES_ODUFUNCTION=246	urn:o-ran:smo:teiv:PhysicalAppliance=246	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10	["urn:o-ran:smo:teiv:PhysicalAppliance=246", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=10,ODUFunction=10"]	[]	{}	{}
\.


COPY teiv_data."CFC235E0404703D1E4454647DF8AAE2C193DB402" ("id", "aSide_AntennaModule", "bSide_AntennaCapability", "CD_sourceIds", "CD_classifiers", "CD_decorators", "metadata") FROM stdin;
urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_SERVES_ANTENNACAPABILITY=ABD52B030DF1169F9F41C898913EF30F7BB5741F53352F482310B280C90AC569B7D31D52A2BB41F1F0099AE1EDD56CACF0B285D145A5584D376DD45DED1E2D65	urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,NodeSupport=1,SectorEquipmentFunction=1	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=1,RetSubUnit=1", "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,NodeSupport=1,SectorEquipmentFunction=1", "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"]	["ocucp-ocuup-model:Weekend"]	{"ocucp-ocuup-model:metadata":"value1"}	{"reliabilityIndicator":"RESTORED","firstDiscovered":"2025-01-13T16:40:36.461565Z","lastModified":"2025-01-13T16:40:36.461565Z"}
urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_SERVES_ANTENNACAPABILITY=8940999E2069725B463052BC35572FDB888C7B734459EE78A01B9F91E2607D87356425BC8EFF0B1C9057D852A4D3F9E1B09479D32FEE68C65EF2821B65F7BD80	urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,NodeSupport=1,SectorEquipmentFunction=1	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=1,RetSubUnit=1", "urn:cmHandle:03661FA2E41EF3D12CAAD5954CD985AC", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,NodeSupport=1,SectorEquipmentFunction=1", "urn:cmHandle:03661FA2E41EF3D12CAAD5954CD985AC"]	[]	{}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-13T16:40:36.461565Z","lastModified":"2025-01-13T16:40:36.461565Z"}
\.

COPY teiv_data."o-ran-smo-teiv-rel-equipment-ran_ANTENNAMODULE_SERVES_NRCELLDU" ("id", "aSide_AntennaModule", "bSide_NRCellDU", "CD_sourceIds", "CD_classifiers", "CD_decorators") FROM stdin;
urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_SERVES_NRCELLDU=ABD52B030DF1169F9F41C898913EF30F7BB5741F53352F482310B280C90AC569B7D31D52A2BB41F1F0099AE1EDD56CACF0B285D145A5584D376DD45DED1E2D65	urn:o-ran:smo:teiv:sha512:AntennaModule=308D6602D2FE1C923DF176A0F30688B1810DFA7BC4AD5B8050BF9E27361ECA86E86B47B8582DC28E8CE92EB81822DE248845E87094557A953FD9F15BA508B03A	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=1	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=1,RetSubUnit=1", "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,NodeSupport=1,SectorEquipmentFunction=1", "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"]	["gnbcucp-gnbcuup-model:Weekend"]	{"gnbcucp-gnbcuup-model:metadata":"value1"}
urn:o-ran:smo:teiv:sha512:ANTENNAMODULE_SERVES_NRCELLDU=8940999E2069725B463052BC35572FDB888C7B734459EE78A01B9F91E2607D87356425BC8EFF0B1C9057D852A4D3F9E1B09479D32FEE68C65EF2821B65F7BD80	urn:o-ran:smo:teiv:sha512:AntennaModule=971FCD28D02B78DDD982611639A0957140339C5522EAAF3FBACA1B8308CF7B0A870CFA80AE04E259805B2A2CB95E263261309883B4D4BF50183FA17AFBA47EA7	urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=9,ODUFunction=9,NRCellDU=2	["urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,Equipment=1,AntennaUnitGroup=1,AntennaUnit=1,AntennaSubunit=1", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,Equipment=1,AntennaUnitGroup=1,AntennaNearUnit=1,RetSubUnit=1", "urn:cmHandle:03661FA2E41EF3D12CAAD5954CD985AC", "urn:3gpp:dn:SubNetwork=Europe,SubNetwork=Hungary,MeContext=1,ManagedElement=19,NodeSupport=1,SectorEquipmentFunction=1", "urn:cmHandle:03661FA2E41EF3D12CAAD5954CD985AC"]	[]	{}
\.

COPY teiv_data."o-ran-smo-teiv-ran_NRCellCU" ("id", "cellLocalId", "nCI", "nRTAC", "plmnId", "CD_sourceIds", "CD_classifiers", "CD_decorators", "REL_FK_provided-by-ocucpFunction", "REL_ID_OCUCPFUNCTION_PROVIDES_NRCELLCU", "REL_CD_sourceIds_OCUCPFUNCTION_PROVIDES_NRCELLCU", "REL_CD_classifiers_OCUCPFUNCTION_PROVIDES_NRCELLCU", "REL_CD_decorators_OCUCPFUNCTION_PROVIDES_NRCELLCU", "metadata", "REL_metadata_OCUCPFUNCTION_PROVIDES_NRCELLCU") FROM stdin;
\.

COPY teiv_data."o-ran-smo-teiv-equipment_Site" ("id", "geo-location", "name", "CD_sourceIds", "CD_classifiers", "CD_decorators", "metadata") FROM stdin;
\.

-- Test specific entries --

CREATE TABLE IF NOT EXISTS teiv_data."10B9F515756871D3EF6558FAF1F112BAE207945D" (
	"id"			TEXT,
	"6446B2D4BE5E367FB0396383C4BDEF42D51CF74F"			TEXT,
	"F03B534AFF0872651FED60C54AB56BEDADAB94B5"			BIGINT,
	"333177AA699C0DE6399503171DCF48FB396322B0"			INTEGER,
	"027B1A8019C6DEF04558B90D9D8B52253B82FEC6"			BIGINT,
	"478D043D81678134EF1C8BFB073A70F882C4AF12"			DECIMAL,
	"8252D18D44F633831557076D827993C45278024D"			jsonb,
	"68C48305AB6C3A30DD927F5D38562379374A4B31"			jsonb,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_provided-by-entityTypeA"			TEXT,
	"REL_ID_F64052A4F8BB3CC533EC15BBFB5E224F600735B0"			TEXT,
	"REL_CD_F26C39EC1F710F3096BE0588F6783A03A378516A"			jsonb,
	"REL_CD_E2C3D598A06EA38133E23C1756ED58A66FE21386"			jsonb,
	"REL_CD_92559ED73C761B860682582A040E745ECEC194D5"			jsonb
);

ALTER TABLE ONLY teiv_data."10B9F515756871D3EF6558FAF1F112BAE207945D" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."10B9F515756871D3EF6558FAF1F112BAE207945D" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."10B9F515756871D3EF6558FAF1F112BAE207945D" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."10B9F515756871D3EF6558FAF1F112BAE207945D" ALTER COLUMN "REL_CD_F26C39EC1F710F3096BE0588F6783A03A378516A" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."10B9F515756871D3EF6558FAF1F112BAE207945D" ALTER COLUMN "REL_CD_E2C3D598A06EA38133E23C1756ED58A66FE21386" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."10B9F515756871D3EF6558FAF1F112BAE207945D" ALTER COLUMN "REL_CD_92559ED73C761B860682582A040E745ECEC194D5" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."10B9F515756871D3EF6558FAF1F112BAE207945D" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE ONLY teiv_data."10B9F515756871D3EF6558FAF1F112BAE207945D" ADD COLUMN IF NOT EXISTS "REL_metadata_F53C39EC1F710F3096BE0588F6783A03A378516A" jsonb;

CREATE TABLE IF NOT EXISTS teiv_data."54110F8D085BBBA7BB6DE5CE71B511562090F7EE" (
	"id"			TEXT,
	"aSide_EntityTypeA"			TEXT,
	"bSide_A85CE100A012A71EF2ABA306BABE484AC2AAE515"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY teiv_data."54110F8D085BBBA7BB6DE5CE71B511562090F7EE" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE ONLY teiv_data."54110F8D085BBBA7BB6DE5CE71B511562090F7EE" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."54110F8D085BBBA7BB6DE5CE71B511562090F7EE" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."54110F8D085BBBA7BB6DE5CE71B511562090F7EE" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

CREATE TABLE IF NOT EXISTS teiv_data."FB1E124031A12CD85D3335194B39B193723A0490" (
	"id"			TEXT,
	"aSide_C812C285BEFA4EC42026AB075D9C65200A00F815"			TEXT,
	"bSide_A85CE100A012A71EF2ABA306BABE484AC2AAE515"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb
);

ALTER TABLE ONLY teiv_data."FB1E124031A12CD85D3335194B39B193723A0490" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."FB1E124031A12CD85D3335194B39B193723A0490" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."FB1E124031A12CD85D3335194B39B193723A0490" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."FB1E124031A12CD85D3335194B39B193723A0490" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

CREATE TABLE IF NOT EXISTS teiv_data."test-built-in-module_EntityTypeA" (
	"id"			TEXT,
	"CD_sourceIds"			jsonb,
	"CD_classifiers"			jsonb,
	"CD_decorators"			jsonb,
	"REL_FK_B7E43411C5C5079D49856E74A9FA63BD20C522C5"			TEXT,
	"REL_ID_31A5B55158140557F09AE15589A8B8038416689B"			TEXT,
	"REL_CD_AB6BDADE3F6C750C9FDB6CAD6059C4CBCE67236C"			jsonb,
	"REL_CD_75B161E740A96ADBAE6F08D4F85684ECC29049B9"			jsonb,
	"REL_CD_6F7211CAF505AECF9A565BC7A4AF56E7032CCC54"			jsonb,
	"REL_FK_A86937FEBD025CFDF6EE5BC386B4C569EB2652DA"			TEXT,
	"REL_ID_A974AD6DD8C4CA281D45693D3A61AE98FEE82845"			TEXT,
	"REL_CD_3B43F80D423BF8F96A2906643B7B4712604FC28B"			jsonb,
	"REL_CD_74A44B167FDF37D6C8E79B5033FEF8BC384C881A"			jsonb,
	"REL_CD_F5B24D9A7273119D4D1519473D9EC88CB407E5CA"			jsonb
);

ALTER TABLE ONLY teiv_data."test-built-in-module_EntityTypeA" ALTER COLUMN "CD_sourceIds" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."test-built-in-module_EntityTypeA" ALTER COLUMN "CD_classifiers" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."test-built-in-module_EntityTypeA" ALTER COLUMN "CD_decorators" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."test-built-in-module_EntityTypeA" ALTER COLUMN "REL_CD_AB6BDADE3F6C750C9FDB6CAD6059C4CBCE67236C" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."test-built-in-module_EntityTypeA" ALTER COLUMN "REL_CD_75B161E740A96ADBAE6F08D4F85684ECC29049B9" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."test-built-in-module_EntityTypeA" ALTER COLUMN "REL_CD_6F7211CAF505AECF9A565BC7A4AF56E7032CCC54" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."test-built-in-module_EntityTypeA" ALTER COLUMN "REL_CD_3B43F80D423BF8F96A2906643B7B4712604FC28B" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."test-built-in-module_EntityTypeA" ALTER COLUMN "REL_CD_74A44B167FDF37D6C8E79B5033FEF8BC384C881A" SET DEFAULT '[]';

ALTER TABLE ONLY teiv_data."test-built-in-module_EntityTypeA" ALTER COLUMN "REL_CD_F5B24D9A7273119D4D1519473D9EC88CB407E5CA" SET DEFAULT '{}';

ALTER TABLE ONLY teiv_data."test-built-in-module_EntityTypeA" ADD COLUMN IF NOT EXISTS "metadata" jsonb;

ALTER TABLE ONLY teiv_data."test-built-in-module_EntityTypeA" ADD COLUMN IF NOT EXISTS "REL_metadata_3B43F80D423BF8F96A2906643B7B4712604FC28B" jsonb;

ALTER TABLE ONLY teiv_data."test-built-in-module_EntityTypeA" ADD COLUMN IF NOT EXISTS "REL_metadata_3H23F80D423BF8F96A2906643B7B4712604FC45F" jsonb;

SELECT teiv_data.create_constraint_if_not_exists(
	'10B9F515756871D3EF6558FAF1F112BAE207945D',
 'PK_7A421D526B36AA9EEF17964BC27011A12FF80DBB',
 'ALTER TABLE teiv_data."10B9F515756871D3EF6558FAF1F112BAE207945D" ADD CONSTRAINT "PK_7A421D526B36AA9EEF17964BC27011A12FF80DBB" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'54110F8D085BBBA7BB6DE5CE71B511562090F7EE',
 'PK_4C48AAFA2160D74F9D13364AA2BE4FDB8A60689D',
 'ALTER TABLE teiv_data."54110F8D085BBBA7BB6DE5CE71B511562090F7EE" ADD CONSTRAINT "PK_4C48AAFA2160D74F9D13364AA2BE4FDB8A60689D" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'FB1E124031A12CD85D3335194B39B193723A0490',
 'PK_020B03AED5787D1B43ABBD9F2C26B494ADDBC7CD',
 'ALTER TABLE teiv_data."FB1E124031A12CD85D3335194B39B193723A0490" ADD CONSTRAINT "PK_020B03AED5787D1B43ABBD9F2C26B494ADDBC7CD" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'test-built-in-module_EntityTypeA',
 'PK_test-built-in-module_EntityTypeA_id',
 'ALTER TABLE teiv_data."test-built-in-module_EntityTypeA" ADD CONSTRAINT "PK_test-built-in-module_EntityTypeA_id" PRIMARY KEY ("id");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'10B9F515756871D3EF6558FAF1F112BAE207945D',
 'FK_B0923C0CCED6CF47CFF759FFE1B810D6CA10D228',
 'ALTER TABLE teiv_data."10B9F515756871D3EF6558FAF1F112BAE207945D" ADD CONSTRAINT "FK_B0923C0CCED6CF47CFF759FFE1B810D6CA10D228" FOREIGN KEY ("REL_FK_provided-by-entityTypeA") REFERENCES teiv_data."test-built-in-module_EntityTypeA" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'10B9F515756871D3EF6558FAF1F112BAE207945D',
 'UNIQUE_B1C2FC9A96300B2BE45785DE60E152D8E85FBE14',
 'ALTER TABLE teiv_data."10B9F515756871D3EF6558FAF1F112BAE207945D" ADD CONSTRAINT "UNIQUE_B1C2FC9A96300B2BE45785DE60E152D8E85FBE14" UNIQUE ("REL_ID_F64052A4F8BB3CC533EC15BBFB5E224F600735B0");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'54110F8D085BBBA7BB6DE5CE71B511562090F7EE',
 'FK_2839E6FFDF7F3DF2687DAC3E57082AD6B22E9B30',
 'ALTER TABLE teiv_data."54110F8D085BBBA7BB6DE5CE71B511562090F7EE" ADD CONSTRAINT "FK_2839E6FFDF7F3DF2687DAC3E57082AD6B22E9B30" FOREIGN KEY ("aSide_EntityTypeA") REFERENCES teiv_data."test-built-in-module_EntityTypeA" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'54110F8D085BBBA7BB6DE5CE71B511562090F7EE',
 'FK_33B5669A341584011D9A73FB491FF2242A158057',
 'ALTER TABLE teiv_data."54110F8D085BBBA7BB6DE5CE71B511562090F7EE" ADD CONSTRAINT "FK_33B5669A341584011D9A73FB491FF2242A158057" FOREIGN KEY ("bSide_A85CE100A012A71EF2ABA306BABE484AC2AAE515") REFERENCES teiv_data."10B9F515756871D3EF6558FAF1F112BAE207945D" (id) ON DELETE CASCADE;'
);


SELECT teiv_data.create_constraint_if_not_exists(
	'FB1E124031A12CD85D3335194B39B193723A0490',
 'FK_2A5C84A2226EE0FCAAA513CC5AF4CD78DDDAF49F',
 'ALTER TABLE teiv_data."FB1E124031A12CD85D3335194B39B193723A0490" ADD CONSTRAINT "FK_2A5C84A2226EE0FCAAA513CC5AF4CD78DDDAF49F" FOREIGN KEY ("aSide_C812C285BEFA4EC42026AB075D9C65200A00F815") REFERENCES teiv_data."10B9F515756871D3EF6558FAF1F112BAE207945D" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'FB1E124031A12CD85D3335194B39B193723A0490',
 'FK_FBFE10B6F165A8EC2086B8DEAFA238E0DD6643F5',
 'ALTER TABLE teiv_data."FB1E124031A12CD85D3335194B39B193723A0490" ADD CONSTRAINT "FK_FBFE10B6F165A8EC2086B8DEAFA238E0DD6643F5" FOREIGN KEY ("bSide_A85CE100A012A71EF2ABA306BABE484AC2AAE515") REFERENCES teiv_data."10B9F515756871D3EF6558FAF1F112BAE207945D" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'test-built-in-module_EntityTypeA',
 'FK_02592FFA6AFD7EAC7AFAD936E3CD50708E4533E0',
 'ALTER TABLE teiv_data."test-built-in-module_EntityTypeA" ADD CONSTRAINT "FK_02592FFA6AFD7EAC7AFAD936E3CD50708E4533E0" FOREIGN KEY ("REL_FK_B7E43411C5C5079D49856E74A9FA63BD20C522C5") REFERENCES teiv_data."10B9F515756871D3EF6558FAF1F112BAE207945D" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'test-built-in-module_EntityTypeA',
 'UNIQUE_7715FF94E14F99CE4994ABBD8C2583CBA9EAE5BD',
 'ALTER TABLE teiv_data."test-built-in-module_EntityTypeA" ADD CONSTRAINT "UNIQUE_7715FF94E14F99CE4994ABBD8C2583CBA9EAE5BD" UNIQUE ("REL_ID_31A5B55158140557F09AE15589A8B8038416689B");'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'test-built-in-module_EntityTypeA',
 'FK_5CD9BCFA08278DA0BF902BAFBCFCDFCE4FF25FEF',
 'ALTER TABLE teiv_data."test-built-in-module_EntityTypeA" ADD CONSTRAINT "FK_5CD9BCFA08278DA0BF902BAFBCFCDFCE4FF25FEF" FOREIGN KEY ("REL_FK_A86937FEBD025CFDF6EE5BC386B4C569EB2652DA") REFERENCES teiv_data."10B9F515756871D3EF6558FAF1F112BAE207945D" (id) ON DELETE CASCADE;'
);

SELECT teiv_data.create_constraint_if_not_exists(
	'test-built-in-module_EntityTypeA',
 'UNIQUE_67DB5E4BC34AB83BDC069A5CAF73B57967D5C2D9',
 'ALTER TABLE teiv_data."test-built-in-module_EntityTypeA" ADD CONSTRAINT "UNIQUE_67DB5E4BC34AB83BDC069A5CAF73B57967D5C2D9" UNIQUE ("REL_ID_A974AD6DD8C4CA281D45693D3A61AE98FEE82845");'
);

COPY teiv_data."10B9F515756871D3EF6558FAF1F112BAE207945D" ("id", "6446B2D4BE5E367FB0396383C4BDEF42D51CF74F", "F03B534AFF0872651FED60C54AB56BEDADAB94B5", "333177AA699C0DE6399503171DCF48FB396322B0", "027B1A8019C6DEF04558B90D9D8B52253B82FEC6", "478D043D81678134EF1C8BFB073A70F882C4AF12", "8252D18D44F633831557076D827993C45278024D", "68C48305AB6C3A30DD927F5D38562379374A4B31", "CD_sourceIds", "CD_classifiers", "CD_decorators", "metadata") FROM stdin;
urn:LongEntityType1	someStringValue	9223372036854775807	2147483647	-9223372036854775807	1.1	["1000", "2000"]	{"mcc":"01","mnc":"234"}	["urn:LongEntityType1"]	[]	{}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
\.

COPY teiv_data."test-built-in-module_EntityTypeA" ("id", "CD_sourceIds", "CD_classifiers", "CD_decorators", "REL_FK_B7E43411C5C5079D49856E74A9FA63BD20C522C5", "REL_ID_31A5B55158140557F09AE15589A8B8038416689B", "REL_CD_AB6BDADE3F6C750C9FDB6CAD6059C4CBCE67236C", "REL_CD_75B161E740A96ADBAE6F08D4F85684ECC29049B9", "REL_CD_6F7211CAF505AECF9A565BC7A4AF56E7032CCC54", "REL_FK_A86937FEBD025CFDF6EE5BC386B4C569EB2652DA", "REL_ID_A974AD6DD8C4CA281D45693D3A61AE98FEE82845", "REL_CD_3B43F80D423BF8F96A2906643B7B4712604FC28B", "REL_CD_74A44B167FDF37D6C8E79B5033FEF8BC384C881A", "REL_CD_F5B24D9A7273119D4D1519473D9EC88CB407E5CA", "metadata") FROM stdin;
urn:EntityType1	[]	[]	{}	\N	\N	[]	[]	{}	urn:LongEntityType1	urn:RelId_OneToOne_EntityType1_LongEntityType1	["urn:EntityType1"]	[]	{}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:EntityType2	[]	[]	{}	urn:LongEntityType1	urn:Rel_ManyToOne_EntityType2_LongEntityType1	[]	[]	{}	\N	\N	["urn:EntityType2"]	[]	{}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
\.

COPY teiv_data."10B9F515756871D3EF6558FAF1F112BAE207945D" ("id", "6446B2D4BE5E367FB0396383C4BDEF42D51CF74F", "F03B534AFF0872651FED60C54AB56BEDADAB94B5", "333177AA699C0DE6399503171DCF48FB396322B0", "027B1A8019C6DEF04558B90D9D8B52253B82FEC6", "478D043D81678134EF1C8BFB073A70F882C4AF12", "8252D18D44F633831557076D827993C45278024D", "68C48305AB6C3A30DD927F5D38562379374A4B31", "CD_sourceIds", "CD_classifiers", "CD_decorators", "REL_FK_provided-by-entityTypeA", "REL_ID_F64052A4F8BB3CC533EC15BBFB5E224F600735B0", "REL_CD_F26C39EC1F710F3096BE0588F6783A03A378516A", "REL_CD_E2C3D598A06EA38133E23C1756ED58A66FE21386", "REL_CD_92559ED73C761B860682582A040E745ECEC194D5", "metadata") FROM stdin;
urn:LongEntityType2	\N	\N	\N	\N	\N	\N	\N	[]	[]	{}	urn:EntityType2	urn:Rel_OneToMany_EntityType2_LongEntityType2	["urn:LongEntityType2"]	[]	{}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:LongEntityType3	\N	\N	\N	\N	\N	\N	\N	[]	[]	{}	\N	\N	["urn:LongEntityType3"]	[]	{}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
\.

COPY teiv_data."54110F8D085BBBA7BB6DE5CE71B511562090F7EE" ("id", "aSide_EntityTypeA", "bSide_A85CE100A012A71EF2ABA306BABE484AC2AAE515", "CD_sourceIds", "CD_classifiers", "CD_decorators", "metadata") FROM stdin;
urn:Rel_ManyToMany_EntityType1_LongEntityType1	urn:EntityType1	urn:LongEntityType1	["urn:EntityType1", "urn:LongEntityType1"]	[]	{}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
\.

COPY teiv_data."FB1E124031A12CD85D3335194B39B193723A0490" ("id", "aSide_C812C285BEFA4EC42026AB075D9C65200A00F815", "bSide_A85CE100A012A71EF2ABA306BABE484AC2AAE515", "CD_sourceIds", "CD_classifiers", "CD_decorators", "metadata") FROM stdin;
urn:Rel_OneToOne_SameEntity_LongEntityType1_LongEntityType2	urn:LongEntityType1	urn:LongEntityType2	["urn:LongEntityType1", "urn:LongEntityType2", "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8616"]	["test-app-module:Weekday"]	{"test-app-module:textdata":"ORAN","test-app-module:intdata":123}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
urn:Rel_OneToOne_SameEntity_LongEntityType2_LongEntityType3	urn:LongEntityType2	urn:LongEntityType3	["urn:LongEntityType2", "urn:LongEntityType3", "urn:cmHandle:C4388D6BB970EC663F88B46CC14F8617"]	["test-app-module:Weekend"]	{"test-app-module:textdata":"ORAN","test-app-module:intdata":234}	{"reliabilityIndicator":"OK","firstDiscovered":"2025-01-08T10:40:36.46156500Z","lastModified":"2025-01-08T10:40:36.46156500Z"}
\.

COMMIT;

