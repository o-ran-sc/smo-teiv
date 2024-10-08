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


COPY ties_data."163276fa439cdfccabb80f7acacb6fa638e8d314" ("id", "name", "REL_FK_deployed-managedElement" )  FROM stdin;
\.

COPY ties_data."ef701af8e1445ed5d377664ba1d3d1c645e31639" ("id", "name", "REL_FK_C2F5EC33C0760F653CE7263A49C0B697FCA2D542" )  FROM stdin;
C4E311A55666726FD9FE25CA572AFAF9	Example Cloud Native System/1	\N
\.

COPY ties_data."o-ran-smo-teiv-ran-oam_ManagedElement" ("id", "cmId", "fdn", "REL_FK_deployed-as-cloudNativeSystem", "REL_ID_MANAGEDELEMENT_DEPLOYED_AS_CLOUDNATIVESYSTEM" )  FROM stdin;
DA1039E77700A9EEFFA280049ECE9227	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/ManagedElement=9	\N	\N
6F02817AFE4D53237DB235EBE5378613	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/ManagedElement=10	\N	\N
27500EB447000209EE6E3CA1B31FBA92	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/ManagedElement=13	\N	\N
06222D277EE209CD8DCA1FE61CE752E6	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/ManagedElement=14	\N	\N
DC86CA7724113F4C0DF42BFEAA17FD53	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/ManagedElement=16	\N	\N
8D51EFC759166044DACBCA63C4EDFC51	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/ManagedElement=19	\N	\N
E64371CD4D12ED0CED200DD3A7591784	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/ManagedElement=28	\N	\N
\.

COPY ties_data."fa2dceaf53e045c136ce9db9bc5faae65a29fa4d" ("id", "cmId", "fdn", "REL_FK_26958E3A529C4C8B68A29FDA906F8CD290F66078", "REL_ID_B7945BFD83380F3E12CF99F2B0F838F364027F92" )  FROM stdin;
45EF31D8A1FD624D7276390A1215BFC3	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/ManagedElement=1	C4E311A55666726FD9FE25CA572AFAF9	urn:base64:TWFuYWdlZEVsZW1lbnQ6NDVFRjMxRDhBMUZENjI0RDcyNzYzOTBBMTIxNUJGQzM6REVQTE9ZRURfQVM6Q2xvdWROYXRpdmVTeXN0ZW06QzRFMzExQTU1NjY2NzI2RkQ5RkUyNUNBNTcyQUZBRjk=
DA1039E77700A9EEFFA280049ECE9227	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/ManagedElement=9	\N	\N
6F02817AFE4D53237DB235EBE5378613	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/ManagedElement=10	\N	\N
27500EB447000209EE6E3CA1B31FBA92	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/ManagedElement=13	\N	\N
06222D277EE209CD8DCA1FE61CE752E6	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/ManagedElement=14	\N	\N
DC86CA7724113F4C0DF42BFEAA17FD53	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/ManagedElement=16	\N	\N
8D51EFC759166044DACBCA63C4EDFC51	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/ManagedElement=19	\N	\N
E64371CD4D12ED0CED200DD3A7591784	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/ManagedElement=28	\N	\N
\.

COPY ties_data."o-ran-smo-teiv-ran-cloud_CloudSite" ("id", "geo-location", "name" )  FROM stdin;
16EE17AE89DF11B69E94B3F6827C2C0E	POINT(59.4019881 17.9419888)	ORAN
\.

COPY ties_data."o-ran-smo-teiv-ran-cloud_NodeCluster" ("id", "name", "REL_FK_located-at-cloudSite", "REL_ID_NODECLUSTER_LOCATED_AT_CLOUDSITE" )  FROM stdin;
015C2DDBD7AC722B34ED6A20EDEEB9C3	Example NodeCluster/45	16EE17AE89DF11B69E94B3F6827C2C0E	urn:base64:Tm9kZUNsdXN0ZXI6MDE1QzJEREJEN0FDNzIyQjM0RUQ2QTIwRURFRUI5QzM6TE9DQVRFRF9BVDpDbG91ZFNpdGU6MTZFRTE3QUU4OURGMTFCNjlFOTRCM0Y2ODI3QzJDMEU=
\.

COPY ties_data."o-ran-smo-teiv-ran-cloud_Namespace" ("id", "name", "REL_FK_deployed-on-nodeCluster", "REL_ID_NAMESPACE_DEPLOYED_ON_NODECLUSTER" )  FROM stdin;
1C02E96B2AAE036C7AE404BC38C308E0	Example Namespace/1	\N	\N
\.

COPY ties_data."bc563e4310b0538e6bdd35f52684160af5b23671" ("id", "name" )  FROM stdin;
1C02E96B2AAE036C7AE404BC38C308E0	Example Namespace/1
\.

COPY ties_data."e01fcb87ad2c34ce66c34420255e25aaca270e5e" ("id", "name", "REL_FK_realised-managedElement", "REL_ID_MANAGEDELEMENT_REALISED_BY_CLOUDNATIVEAPPLICATION", "REL_FK_comprised-by-cloudNativeSystem", "REL_ID_CLOUDNATIVESYSTEM_COMPRISES_CLOUDNATIVEAPPLICATION", "REL_FK_deployed-on-namespace", "REL_ID_CLOUDNATIVEAPPLICATION_DEPLOYED_ON_NAMESPACE" )  FROM stdin;
AD42D90497E93D276215DF6D3B899E17	Cloud Native CUUPApp/8	\N	\N	\N	\N	\N	\N
719BD5C7CD8A939D76A83DA95DA45C01	Example Cloud App/9	\N	\N	\N	\N	1C02E96B2AAE036C7AE404BC38C308E0	urn:base64:Q2xvdWROYXRpdmVBcHBsaWNhdGlvbjo3MTlCRDVDN0NEOEE5MzlENzZBODNEQTk1REE0NUMwMTpERVBMT1lFRF9PTjpOYW1lc3BhY2U6MUMwMkU5NkIyQUFFMDM2QzdBRTQwNEJDMzhDMzA4RTA=
416F31E6EB09055326621F4919D35BFF	Example Cloud App/10	\N	\N	\N	\N	\N	\N
C549905CF3CC890CE5746C5E10ACF00D	Example Cloud App/19	\N	\N	\N	\N	\N	\N
\.

COPY ties_data."5f8facd51b2bbddc90d9dee9be697907441892d0" ("id", "name", "REL_FK_AB87B417CCD05C332DDD0C60F0C6AB41D38B05E5", "REL_ID_FC2F6A5A12917357B548C83F4B0C1AD58FA61413", "REL_FK_8C98B70070BBD11F90F192DDA3ECF6302390E956", "REL_ID_AFBF10D23507AD3B6408947D2A9AF8465BA7B08C" )  FROM stdin;
AD42D90497E93D276215DF6D3B899E17	Cloud Native CUUPApp/8	\N	\N	\N	\N
719BD5C7CD8A939D76A83DA95DA45C01	Example Cloud App/9	\N	\N	1C02E96B2AAE036C7AE404BC38C308E0	urn:base64:Q2xvdWROYXRpdmVBcHBsaWNhdGlvbjo3MTlCRDVDN0NEOEE5MzlENzZBODNEQTk1REE0NUMwMTpERVBMT1lFRF9PTjpOYW1lc3BhY2U6MUMwMkU5NkIyQUFFMDM2QzdBRTQwNEJDMzhDMzA4RTA=
416F31E6EB09055326621F4919D35BFF	Example Cloud App/10	6F02817AFE4D53237DB235EBE5378613	urn:base64:TWFuYWdlZEVsZW1lbnQ6NkYwMjgxN0FGRTRENTMyMzdEQjIzNUVCRTUzNzg2MTM6UkVBTElTRURfQlk6Q2xvdWROYXRpdmVBcHBsaWNhdGlvbjo0MTZGMzFFNkVCMDkwNTUzMjY2MjFGNDkxOUQzNUJGRg==	\N	\N
C549905CF3CC890CE5746C5E10ACF00D	Example Cloud App/19	\N	\N	\N	\N
\.

COPY ties_data."22174a23af5d5a96143c83ddfa78654df0acb697" ("id", "geo-location", "sectorId", "azimuth" )  FROM stdin;
2F445AA5744FA3D230FD6838531F1407	POINT(59.4019881 17.9419888)	1	1
F5128C172A70C4FCD4739650B06DE9E2	POINT(59.4019881 17.9419888)	2	2
ADB1BAAC878C0BEEFE3175C60F44BB1D	POINT(59.4019881 17.9419888)	4	3
\.

COPY ties_data."c4a425179d3089b5288fdf059079d0ea26977f0f" ("id", "pLMNId", "cmId", "fdn", "gNBCUName", "gNBIdLength", "gNBId", "REL_FK_managed-by-managedElement", "REL_ID_MANAGEDELEMENT_MANAGES_GNBCUCPFUNCTION" )  FROM stdin;
\.

COPY ties_data."8fe520ccd54b8f72ef7b2d3061264ddc2481e4eb" ("id", "cmId", "fdn", "gNBIdLength", "gNBId", "REL_FK_managed-by-managedElement", "REL_ID_MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION", "REL_CD_classifiers_MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION", "REL_CD_decorators_MANAGEDELEMENT_MANAGES_GNBCUUPFUNCTION" )  FROM stdin;
BFEEAC2CE60273CB0A78319CC201A7FE	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/GNBCUUPFunction=8	1	8	E64371CD4D12ED0CED200DD3A7591784	urn:base64:TWFuYWdlZEVsZW1lbnQ6RTY0MzcxQ0Q0RDEyRUQwQ0VEMjAwREQzQTc1OTE3ODQ6TUFOQUdFUzpHTkJDVVVQRnVuY3Rpb246QkZFRUFDMkNFNjAyNzNDQjBBNzgzMTlDQzIwMUE3RkU=	["gnbcucp-gnbcuup-model:Weekend", "gnbcucp-gnbcuup-model:Weekday"]	{"gnbcucp-gnbcuup-model:metadata": "value1", "gnbcucp-gnbcuup-model:meta": "value2"}
\.

COPY ties_data."o-ran-smo-teiv-ran-logical_GNBDUFunction" ("id", "gNBDUId", "cmId", "fdn", "dUpLMNId", "gNBIdLength", "gNBId", "REL_FK_managed-by-managedElement", "REL_ID_MANAGEDELEMENT_MANAGES_GNBDUFUNCTION", "CD_sourceIds", "CD_classifiers", "CD_decorators", "REL_CD_classifiers_MANAGEDELEMENT_MANAGES_GNBDUFUNCTION", "REL_CD_decorators_MANAGEDELEMENT_MANAGES_GNBDUFUNCTION" )  FROM stdin;
D3215E08570BE58339C7463626B50E37	\N	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/GNBDUFunction=9	{"mcc":{"mcca":"123","mccb":{"mccba":123}},"mcb":["a123","b123","c012"],"mnc":"82"}	1	9	DA1039E77700A9EEFFA280049ECE9227	urn:base64:TWFuYWdlZEVsZW1lbnQ6REExMDM5RTc3NzAwQTlFRUZGQTI4MDA0OUVDRTkyMjc6TUFOQUdFUzpHTkJEVUZ1bmN0aW9uOkQzMjE1RTA4NTcwQkU1ODMzOUM3NDYzNjI2QjUwRTM3	[]	[]	{}	[]	{}
1050570EBB1315E1AE7A9FD5E1400A00	\N	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/GNBDUFunction=10	{"mcc":{"mcca":"234","mccb":{"mccba":234}},"mcb":["a123","b123","c012"],"mnc":"82"}	2	10	6F02817AFE4D53237DB235EBE5378613	urn:base64:TWFuYWdlZEVsZW1lbnQ6NkYwMjgxN0FGRTRENTMyMzdEQjIzNUVCRTUzNzg2MTM6TUFOQUdFUzpHTkJEVUZ1bmN0aW9uOjEwNTA1NzBFQkIxMzE1RTFBRTdBOUZENUUxNDAwQTAw	[]	[]	{}	[]	{}
25E690E22BDA90B9C4FEE1F083CBA597	\N	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/GNBDUFunction=13	{"mcc":{"mcca":"345","mccb":{"mccba":345}},"mcb":["a123","b123","c012"],"mnc":"82"}	2	13	27500EB447000209EE6E3CA1B31FBA92	urn:base64:TWFuYWdlZEVsZW1lbnQ6Mjc1MDBFQjQ0NzAwMDIwOUVFNkUzQ0ExQjMxRkJBOTI6TUFOQUdFUzpHTkJEVUZ1bmN0aW9uOjI1RTY5MEUyMkJEQTkwQjlDNEZFRTFGMDgzQ0JBNTk3	[]	[]	{}	[]	{}
5A3085C3400C3096E2ED2321452766B1	\N	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/GNBDUFunction=14	{"mcc":{"mcca":"456","mccb":{"mccba":456}},"mcb":["a123","b123","c012"],"mnc":"82"}	2	14	06222D277EE209CD8DCA1FE61CE752E6	urn:base64:TWFuYWdlZEVsZW1lbnQ6MDYyMjJEMjc3RUUyMDlDRDhEQ0ExRkU2MUNFNzUyRTY6TUFOQUdFUzpHTkJEVUZ1bmN0aW9uOjVBMzA4NUMzNDAwQzMwOTZFMkVEMjMyMTQ1Mjc2NkIx	[]	[]	{}	[]	{}
5A548EA9D166341776CA0695837E55D8	\N	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/GNBDUFunction=16	{"mcc":{"mcca":"567","mccb":{"mccba":567}},"mcb":["a123","b123","c012"],"mnc":"82"}	2	16	DC86CA7724113F4C0DF42BFEAA17FD53	urn:base64:TWFuYWdlZEVsZW1lbnQ6REM4NkNBNzcyNDExM0Y0QzBERjQyQkZFQUExN0ZENTM6TUFOQUdFUzpHTkJEVUZ1bmN0aW9uOjVBNTQ4RUE5RDE2NjM0MTc3NkNBMDY5NTgzN0U1NUQ4	["urn:3gpp:dn:/SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/GNBDUFunction=16","urn:cmHandle:/395221E080CCF0FD1924103B15873814"]	["test-app-module:Indoor","test-app-module:Rural","test-app-module:Weekend"]	{"test-app-module:textdata":"Stockholm", "test-app-module:data":"test"}	["test-app-module:Indoor","test-app-module:Rural","test-app-module:Weekend"]	{"test-app-module:textdata":"Stockholm", "test-app-module:data":"test"}
4CFF136200A2DE36205A13559C55DB2A	\N	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/GNBDUFunction=19	{"mcc":{"mcca":"678","mccb":{"mccba":678}},"mcb":["a123","b123","c012"],"mnc":"82"}	2	19	8D51EFC759166044DACBCA63C4EDFC51	urn:base64:TWFuYWdlZEVsZW1lbnQ6OEQ1MUVGQzc1OTE2NjA0NERBQ0JDQTYzQzRFREZDNTE6TUFOQUdFUzpHTkJEVUZ1bmN0aW9uOjRDRkYxMzYyMDBBMkRFMzYyMDVBMTM1NTlDNTVEQjJB	[]	[]	{}	[]	{}
\.

COPY ties_data."3fdb9cd7557edf559a0e4de88df220e7545884b5" ("id", "gNBDUId", "cmId", "3786A6CA64C9422F9E7FC35B7B039F345BBDDA65", "dUpLMNId", "gNBIdLength", "gNBId", "REL_FK_48B14FA5B787C6398AD1DE5EE670AD0D2A2CB36F", "REL_ID_BDE0B6C74D14AC109D29A08D80E92D4D0DCAEB0B" )  FROM stdin;
D3215E08570BE58339C7463626B50E37	\N	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/GNBDUFunction=9	{"mcc":"456","mnc":"82"}	1	9	DA1039E77700A9EEFFA280049ECE9227	urn:base64:TWFuYWdlZEVsZW1lbnQ6REExMDM5RTc3NzAwQTlFRUZGQTI4MDA0OUVDRTkyMjc6TUFOQUdFUzpHTkJEVUZ1bmN0aW9uOkQzMjE1RTA4NTcwQkU1ODMzOUM3NDYzNjI2QjUwRTM3
1050570EBB1315E1AE7A9FD5E1400A00	\N	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/GNBDUFunction=10	{"mcc":"456","mnc":"82"}	2	10	6F02817AFE4D53237DB235EBE5378613	urn:base64:TWFuYWdlZEVsZW1lbnQ6NkYwMjgxN0FGRTRENTMyMzdEQjIzNUVCRTUzNzg2MTM6TUFOQUdFUzpHTkJEVUZ1bmN0aW9uOjEwNTA1NzBFQkIxMzE1RTFBRTdBOUZENUUxNDAwQTAw
25E690E22BDA90B9C4FEE1F083CBA597	\N	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/GNBDUFunction=13	{"mcc":"456","mnc":"82"}	2	13	27500EB447000209EE6E3CA1B31FBA92	urn:base64:TWFuYWdlZEVsZW1lbnQ6Mjc1MDBFQjQ0NzAwMDIwOUVFNkUzQ0ExQjMxRkJBOTI6TUFOQUdFUzpHTkJEVUZ1bmN0aW9uOjI1RTY5MEUyMkJEQTkwQjlDNEZFRTFGMDgzQ0JBNTk3
5A3085C3400C3096E2ED2321452766B1	\N	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/GNBDUFunction=14	{"mcc":"456","mnc":"82"}	2	14	06222D277EE209CD8DCA1FE61CE752E6	urn:base64:TWFuYWdlZEVsZW1lbnQ6MDYyMjJEMjc3RUUyMDlDRDhEQ0ExRkU2MUNFNzUyRTY6TUFOQUdFUzpHTkJEVUZ1bmN0aW9uOjVBMzA4NUMzNDAwQzMwOTZFMkVEMjMyMTQ1Mjc2NkIx
5A548EA9D166341776CA0695837E55D8	\N	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/GNBDUFunction=16	{"mcc":"456","mnc":"82"}	2	16	DC86CA7724113F4C0DF42BFEAA17FD53	urn:base64:TWFuYWdlZEVsZW1lbnQ6REM4NkNBNzcyNDExM0Y0QzBERjQyQkZFQUExN0ZENTM6TUFOQUdFUzpHTkJEVUZ1bmN0aW9uOjVBNTQ4RUE5RDE2NjM0MTc3NkNBMDY5NTgzN0U1NUD4
4CFF136200A2DE36205A13559C55DB2A	\N	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/GNBDUFunction=19	{"mcc":"456","mnc":"82"}	2	19	8D51EFC759166044DACBCA63C4EDFC51	urn:base64:TWFuYWdlZEVsZW1lbnQ6OEQ1MUVGQzc1OTE2NjA0NERBQ0JDQTYzQzRFREZDNTE6TUFOQUdFUzpHTkJEVUZ1bmN0aW9uOjRDRkYxMzYyMDBBMkRFMzYyMDVBMTM1NTlDNTVEQjJB
\.

COPY ties_data."88f1bd76c7a935fb505cf235e6819f46c55ec98a" ("id", "nRFqBands", "eUtranFqBands", "cmId", "fdn", "geranFqBands", "REL_FK_used-by-lteSectorCarrier" )  FROM stdin;
5835F77BE9D4E102316BD59195F6370B	["123","456","789"]	["123","4564","789"]	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/AntennaCapability=1	["123","456","789"]	\N
A77B237A541B2D3225B4B61D3098E4AA	["123","456","789"]	["123","4564","789"]	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/AntennaCapability=3	["123","456","789"]	\N
\.

COPY ties_data."o-ran-smo-teiv-ran-logical_NRCellDU" ("id", "nRPCI", "cmId", "cellLocalId", "fdn", "nCI", "nRTAC", "REL_FK_grouped-by-sector", "REL_ID_SECTOR_GROUPS_NRCELLDU", "REL_FK_provided-by-gnbduFunction", "REL_ID_GNBDUFUNCTION_PROVIDES_NRCELLDU" )  FROM stdin;
98C3A4591A37718E1330F0294E23B62A	789	\N	1	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/NRCellDU=1	1	456	F5128C172A70C4FCD4739650B06DE9E2	urn:base64:U2VjdG9yOkY1MTI4QzE3MkE3MEM0RkNENDczOTY1MEIwNkRFOUUyOkdST1VQUzpOUkNlbGxEVTo5OEMzQTQ1OTFBMzc3MThFMTMzMEYwMjk0RTIzQjYyQQ==	D3215E08570BE58339C7463626B50E37	urn:base64:R05CRFVGdW5jdGlvbjpEMzIxNUUwODU3MEJFNTgzMzlDNzQ2MzYyNkI1MEUzNzpQUk9WSURFUzpOUkNlbGxEVTo5OEMzQTQ1OTFBMzc3MThFMTMzMEYwMjk0RTIzQjYyQQ==
F9546E82313AC1D5E690DCD7BE55606F	789	\N	2	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/NRCellDU=2	2	456	F5128C172A70C4FCD4739650B06DE9E2	urn:base64:U2VjdG9yOkY1MTI4QzE3MkE3MEM0RkNENDczOTY1MEIwNkRFOUUyOkdST1VQUzpOUkNlbGxEVTpGOTU0NkU4MjMxM0FDMUQ1RTY5MERDRDdCRTU1NjA2Rg==	D3215E08570BE58339C7463626B50E37	urn:base64:R05CRFVGdW5jdGlvbjpEMzIxNUUwODU3MEJFNTgzMzlDNzQ2MzYyNkI1MEUzNzpQUk9WSURFUzpOUkNlbGxEVTpGOTU0NkU4MjMxM0FDMUQ1RTY5MERDRDdCRTU1NjA2Rg==
B480427E8A0C0B8D994E437784BB382F	789	\N	3	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/NRCellDU=3	3	456	F5128C172A70C4FCD4739650B06DE9E2	urn:base64:U2VjdG9yOkY1MTI4QzE3MkE3MEM0RkNENDczOTY1MEIwNkRFOUUyOkdST1VQUzpOUkNlbGxEVTpCNDgwNDI3RThBMEMwQjhEOTk0RTQzNzc4NEJCMzgyRg==	D3215E08570BE58339C7463626B50E37	urn:base64:R05CRFVGdW5jdGlvbjpEMzIxNUUwODU3MEJFNTgzMzlDNzQ2MzYyNkI1MEUzNzpQUk9WSURFUzpOUkNlbGxEVTpCNDgwNDI3RThBMEMwQjhEOTk0RTQzNzc4NEJCMzgyRg==
76E9F605D4F37330BF0B505E94F64F11	789	\N	91	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/NRCellDU=91	91	456	\N	\N	4CFF136200A2DE36205A13559C55DB2A	urn:base64:R05CRFVGdW5jdGlvbjo0Q0ZGMTM2MjAwQTJERTM2MjA1QTEzNTU5QzU1REIyQTpQUk9WSURFUzpOUkNlbGxEVTo3NkU5RjYwNUQ0RjM3MzMwQkYwQjUwNUU5NEY2NEYxMQ==
67A1BDA10B5AF43028D07C7BE5CB1AE2	789	\N	92	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/NRCellDU=92	92	456	\N	\N	4CFF136200A2DE36205A13559C55DB2A	urn:base64:R05CRFVGdW5jdGlvbjo0Q0ZGMTM2MjAwQTJERTM2MjA1QTEzNTU5QzU1REIyQTpQUk9WSURFUzpOUkNlbGxEVTo2N0ExQkRBMTBCNUFGNDMwMjhEMDdDN0JFNUNCMUFFMg==
B3B0A1939EFCA654A37005B6A7F24BD7	789	\N	93	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/NRCellDU=93	93	456	\N	\N	4CFF136200A2DE36205A13559C55DB2A	urn:base64:R05CRFVGdW5jdGlvbjo0Q0ZGMTM2MjAwQTJERTM2MjA1QTEzNTU5QzU1REIyQTpQUk9WSURFUzpOUkNlbGxEVTpCM0IwQTE5MzlFRkNBNjU0QTM3MDA1QjZBN0YyNEJENw==
\.

COPY ties_data."300672c8a708d6af0b9af2f9accdff5e6e005721" ("id", "nRPCI", "cmId", "020335B0F627C169E24167748C38FE756FB34AE2", "fdn", "nCI", "nRTAC", "REL_FK_609963BFEE15FF824280FBE201313C3CDACDDDCE", "REL_ID_040FC8B06B420BA5708AF4798102D1E65FB4DC61" )  FROM stdin;
98C3A4591A37718E1330F0294E23B62A	789	\N	1	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/NRCellDU=1	1	456	D3215E08570BE58339C7463626B50E37	urn:base64:R05CRFVGdW5jdGlvbjpEMzIxNUUwODU3MEJFNTgzMzlDNzQ2MzYyNkI1MEUzNzpQUk9WSURFUzpOUkNlbGxEVTo5OEMzQTQ1OTFBMzc3MThFMTMzMEYwMjk0RTIzQjYyQQ==
F9546E82313AC1D5E690DCD7BE55606F	789	\N	2	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/NRCellDU=2	2	456	D3215E08570BE58339C7463626B50E37	urn:base64:R05CRFVGdW5jdGlvbjpEMzIxNUUwODU3MEJFNTgzMzlDNzQ2MzYyNkI1MEUzNzpQUk9WSURFUzpOUkNlbGxEVTpGOTU0NkU4MjMxM0FDMUQ1RTY5MERDRDdCRTU1NjA2Rg==
B480427E8A0C0B8D994E437784BB382F	789	\N	3	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/NRCellDU=3	3	456	D3215E08570BE58339C7463626B50E37	urn:base64:R05CRFVGdW5jdGlvbjpEMzIxNUUwODU3MEJFNTgzMzlDNzQ2MzYyNkI1MEUzNzpQUk9WSURFUzpOUkNlbGxEVTpCNDgwNDI3RThBMEMwQjhEOTk0RTQzNzc4NEJCMzgyRg==
76E9F605D4F37330BF0B505E94F64F11	789	\N	91	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/NRCellDU=91	91	456	4CFF136200A2DE36205A13559C55DB2A	urn:base64:R05CRFVGdW5jdGlvbjo0Q0ZGMTM2MjAwQTJERTM2MjA1QTEzNTU5QzU1REIyQTpQUk9WSURFUzpOUkNlbGxEVTo3NkU5RjYwNUQ0RjM3MzMwQkYwQjUwNUU5NEY2NEYxMQ==
67A1BDA10B5AF43028D07C7BE5CB1AE2	789	\N	92	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/NRCellDU=92	92	456	4CFF136200A2DE36205A13559C55DB2A	urn:base64:R05CRFVGdW5jdGlvbjo0Q0ZGMTM2MjAwQTJERTM2MjA1QTEzNTU5QzU1REIyQTpQUk9WSURFUzpOUkNlbGxEVTo2N0ExQkRBMTBCNUFGNDMwMjhEMDdDN0JFNUNCMUFFMg==
B3B0A1939EFCA654A37005B6A7F24BD7	789	\N	93	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/NRCellDU=93	93	456	4CFF136200A2DE36205A13559C55DB2A	urn:base64:R05CRFVGdW5jdGlvbjo0Q0ZGMTM2MjAwQTJERTM2MjA1QTEzNTU5QzU1REIyQTpQUk9WSURFUzpOUkNlbGxEVTpCM0IwQTE5MzlFRkNBNjU0QTM3MDA1QjZBN0YyNEJENw==
F26F279E91D0941DB4F646E707EA403A	789	\N	94	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/NRCellDU=94	94	456	4CFF136200A2DE36205A13559C55DB2A	urn:base64:R05CRFVGdW5jdGlvbjo0Q0ZGMTM2MjAwQTJERTM2MjA1QTEzNTU5QzU1REIyQTpQUk9WSURFUzpOUkNlbGxEVTpGMjZGMjc5RTkxRDA5NDFEQjRGNjQ2RTcwN0VBNDAzQQ==
\.

COPY ties_data."39a335dca201ef99ae06f4ffd1908b534f8c6c39" ("id", "fdn", "frequencyUL", "cmId", "essScLocalId", "arfcnUL", "frequencyDL", "arfcnDL", "REL_FK_used-by-nrCellDu", "REL_ID_NRCELLDU_USES_NRSECTORCARRIER", "REL_FK_provided-by-gnbduFunction", "REL_ID_GNBDUFUNCTION_PROVIDES_NRSECTORCARRIER", "REL_FK_used-antennaCapability", "REL_ID_NRSECTORCARRIER_USES_ANTENNACAPABILITY", "CD_classifiers", "CD_decorators" )  FROM stdin;
E49D942C16E0364E1E0788138916D70C	SubNetwork=SolarSyst3em/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/NRSectorCarrier=3	20	\N	20	3	20	20	B480427E8A0C0B8D994E437784BB382F	urn:base64:TlJDZWxsRFU6QjQ4MDQyN0U4QTBDMEI4RDk5NEU0Mzc3ODRCQjM4MkY6VVNFUzpOUlNlY3RvckNhcnJpZXI6RTQ5RDk0MkMxNkUwMzY0RTFFMDc4ODEzODkxNkQ3MEM=	D3215E08570BE58339C7463626B50E37	urn:base64:R05CRFVGdW5jdGlvbjpEMzIxNUUwODU3MEJFNTgzMzlDNzQ2MzYyNkI1MEUzNzpQUk9WSURFUzpOUlNlY3RvckNhcnJpZXI6RTQ5RDk0MkMxNkUwMzY0RTFFMDc4ODEzODkxNkQ3MEM=	A77B237A541B2D3225B4B61D3098E4AA	urn:base64:TlJTZWN0b3JDYXJyaWVyOkU0OUQ5NDJDMTZFMDM2NEUxRTA3ODgxMzg5MTZENzBDOlVTRVM6QW50ZW5uYUNhcGFiaWxpdHk6QTc3QjIzN0E1NDFCMkQzMjI1QjRCNjFEMzA5OEU0QUE=	["gnbcucp-gnbcuup-model:Weekend", "gnbcucp-gnbcuup-model:Weekday"]	{"gnbcucp-gnbcuup-model:metadata": "value1", "gnbcucp-gnbcuup-model:meta": "value2"}
\.

COPY ties_data."f8caf5ebe876c3001d67efe06e4d83abf0babe31" ("id", "mechanicalAntennaTilt", "antennaBeamWidth", "geo-location", "fdn", "cmId", "antennaModelNumber", "totalTilt", "mechanicalAntennaBearing", "positionWithinSector", "electricalAntennaTilt", "REL_FK_grouped-by-sector", "REL_ID_SECTOR_GROUPS_ANTENNAMODULE", "REL_FK_installed-at-site", "REL_ID_ANTENNAMODULE_INSTALLED_AT_SITE" )  FROM stdin;
278A05C67D47D117C2DC5BDF5E00AE70	123	\N	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/AntennaModule=1	\N	['123-abc']	45	123	['123', '456', '789']	1	ADB1BAAC878C0BEEFE3175C60F44BB1D	urn:base64:U2VjdG9yOkFEQjFCQUFDODc4QzBCRUVGRTMxNzVDNjBGNDRCQjFEOkdST1VQUzpBbnRlbm5hTW9kdWxlOjI3OEEwNUM2N0Q0N0QxMTdDMkRDNUJERjVFMDBBRTcw	\N	\N
279A05C67D47D117C2DC5BDF5E00AE70	456	\N	\N	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/AntennaModule=2	\N	['123-xyz']	45	123	['123', '456', '789']	1	\N	\N	\N	\N
\.

COPY ties_data."341622dca4e0350289717b52df5883edc3fa0280" ("id", "fdn" )  FROM stdin;
378A05C67D47D117C2DC5BDF5E00AE70	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee=11
379A05C67D47D117C2DC5BDF5E00AE70	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee=12
478A05C67D47D117C2DC5BDF5E00AE70	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee=21
479A05C67D47D117C2DC5BDF5E00AE70	SubNetwork=SolarSystem/SubNetwork=Earth/SubNetwork=Europe/SubNetwork=Hungary/AntennaModuleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee=22
\.

COPY ties_data."o-ran-smo-teiv-ran-logical_ENodeBFunction" ("id", "eNBId", "cmId", "eNodeBPlmnId", "fdn", "REL_FK_managed-by-managedElement", "REL_ID_MANAGEDELEMENT_MANAGES_ENODEBFUNCTION", "REL_FK_realised-by-physicalNetworkAppliance", "REL_ID_ENODEBFUNCTION_REALISED_BY_PHYSICALNETWORKAPPLIANCE" )  FROM stdin;
\.

COPY ties_data."o-ran-smo-teiv-ran-logical_EUtranCell" ("id", "cellId", "cmId", "earfcn", "duplexType", "tac", "fdn", "earfcndl", "earfcnul", "REL_FK_grouped-by-sector", "REL_ID_SECTOR_GROUPS_EUTRANCELL", "REL_FK_provided-by-enodebFunction", "REL_ID_ENODEBFUNCTION_PROVIDES_EUTRANCELL" )  FROM stdin;
\.

COPY ties_data."c88307168935f02fdecc084ea5040bb9db16c701" ("id", "fdn", "cmId", "sectorCarrierType", "essScLocalId", "REL_FK_provided-by-enodebFunction", "REL_ID_ENODEBFUNCTION_PROVIDES_LTESECTORCARRIER", "REL_FK_used-antennaCapability", "REL_ID_LTESECTORCARRIER_USES_ANTENNACAPABILITY", "REL_FK_used-by-euTranCell", "REL_ID_EUTRANCELL_USES_LTESECTORCARRIER" )  FROM stdin;
\.

COPY ties_data."o-ran-smo-teiv-ran-logical_NRCellCU" ("id", "fdn", "cmId", "plmnId", "nCI", "cellLocalId", "nRTAC", "REL_FK_provided-by-gnbcucpFunction", "REL_ID_GNBCUCPFUNCTION_PROVIDES_NRCELLCU" )  FROM stdin;
\.

COPY ties_data."57a20807ab3f39c86b0b5bf9a819e0881353fa1e" ("id", "geo-location", "cmId", "name", "type", "REL_FK_installed-at-site", "REL_ID_PHYSICALNETWORKAPPLIANCE_INSTALLED_AT_SITE" )  FROM stdin;
\.

COPY ties_data."o-ran-smo-teiv-ran-equipment_Site" ("id", "geo-location", "cmId", "name" )  FROM stdin;
\.

COPY ties_data."7cd7062ea24531b2f48e4f2fdc51eaf0b82f88c7" ("id", "aSide_GNBCUCPFunction", "bSide_CloudNativeApplication" )  FROM stdin;
\.

COPY ties_data."70a4a84bca01ea022ab24d8cb82422c572922675" ("id", "aSide_GNBCUUPFunction", "bSide_CloudNativeApplication", "CD_classifiers", "CD_decorators" )  FROM stdin;
urn:base64:R05CQ1VVUEZ1bmN0aW9uOkJGRUVBQzJDRTYwMjczQ0IwQTc4MzE5Q0MyMDFBN0ZFOlJFQUxJU0VEX0JZOkNsb3VkTmF0aXZlQXBwbGljYXRpb246QUQ0MkQ5MDQ5N0U5M0QyNzYyMTVERjZEM0I4OTlFMTc=	BFEEAC2CE60273CB0A78319CC201A7FE	AD42D90497E93D276215DF6D3B899E17	["gnbcucp-gnbcuup-model:Weekend", "gnbcucp-gnbcuup-model:Weekday"]	{"gnbcucp-gnbcuup-model:metadata": "value1", "gnbcucp-gnbcuup-model:meta": "value2"}
\.

COPY ties_data."10484f157f490eb5b27e40dbfaf4d5f2be17c57c" ("id", "aSide_GNBDUFunction", "bSide_CloudNativeApplication" )  FROM stdin;
urn:base64:R05CRFVGdW5jdGlvbjo0Q0ZGMTM2MjAwQTJERTM2MjA1QTEzNTU5QzU1REIyQTpSRUFMSVNFRF9CWTpDbG91ZE5hdGl2ZUFwcGxpY2F0aW9uOkM1NDk5MDVDRjNDQzg5MENFNTc0NkM1RTEwQUNGMDBE	4CFF136200A2DE36205A13559C55DB2A	C549905CF3CC890CE5746C5E10ACF00D
\.

COPY ties_data."f86057a8762a50b1c7fb07af9d5c001bffaefd15" ("id", "aSide_B0FD0521695A211BFF76F413A31F28CBA32E57ED", "bSide_84EF1134719BB6FCF33A94FF770311FC722BCF41" )  FROM stdin;
urn:base64:R05CRFVGdW5jdGlvbjo0Q0ZGMTM2MjAwQTJERTM2MjA1QTEzNTU5QzU1REIyQTpSRUFMSVNFRF9CWTpDbG91ZE5hdGl2ZUFwcGxpY2F0aW9uOkM1NDk5MDVDRjNDQzg5MENFNTc0NkM1RTEwQUNGMDBE	4CFF136200A2DE36205A13559C55DB2A	C549905CF3CC890CE5746C5E10ACF00D
\.

COPY ties_data."5b8a47d4a8297a0a1d31e091af06e26d25ef6caf" ("id", "aSide_AntennaCapability", "bSide_AntennaModule" )  FROM stdin;
\.

COPY ties_data."5123d0adfb7b4a04e7f2e5e1783f476ed5cf76f6" ("id", "aSide_AntennaModule", "bSide_AntennaModule" )  FROM stdin;
urn:base64:QW50ZW5uYU1vZHVsZToyNzhBMDVDNjdENDdEMTE3QzJEQzVCREY1RTAwQUU3MDpSRUFMSVNFRF9CWTpBbnRlbm5hTW9kdWxlOjI3OEEwNUM2N0Q0N0QxMTdDMkRDNUJERjVFMDBBRTcwCg==	278A05C67D47D117C2DC5BDF5E00AE70	279A05C67D47D117C2DC5BDF5E00AE70
\.

COPY ties_data."cd39d44beed963d50df42cd301e63d288f911c97" ("id", "aSide_2A2D3374BF907674FA1905478E30ACB8882DC03C", "bSide_EE6DD4A2CFD743779BBCBFC18FC296EF6D72EB1E" )  FROM stdin;
urn:base64:QW50ZW5uYU1vZHVsZTozNzhBMDVDNjdENDdEMTE3QzJEQzVCREY1RTAwQUU3MDpSRUFMSVNFRF9CWTpBbnRlbm5hTW9kdWxlOjM3OUEwNUM2N0Q0N0QxMTdDMkRDNUJERjVFMDBBRTcwCg==	378A05C67D47D117C2DC5BDF5E00AE70	379A05C67D47D117C2DC5BDF5E00AE70
\.

COPY ties_data."c6f3a3396e9165e886da928c5fe1382fb20dc850" ("id", "aSide_2A2D3374BF907674FA1905478E30ACB8882DC03C", "bSide_EE6DD4A2CFD743779BBCBFC18FC296EF6D72EB1E" )  FROM stdin;
urn:base64:QW50ZW5uYU1vZHVsZTo0NzhBMDVDNjdENDdEMTE3QzJEQzVCREY1RTAwQUU3MDpERVBMT1lFRF9CWTpBbnRlbm5hTW9kdWxlOjQ3OUEwNUM2N0Q0N0QxMTdDMkRDNUJERjVFMDBBRTcwCg==	478A05C67D47D117C2DC5BDF5E00AE70	479A05C67D47D117C2DC5BDF5E00AE70
\.

COPY ties_model.module_reference("name", "namespace", "domain", "includedModules", "revision", "content") FROM stdin;
gnbdu-function-model	\N	\N	[]	2024-05-02	Z25kYnUtZnVuY3Rpb24tbW9kZWwgeWFuZyBtb2RlbCBmaWxlCg==
gnbcucp-gnbcuup-model	\N	\N	[]	2024-05-02	Z25iY3VjcC1nbmJjdXVwLW1vZGVsIHlhbmcgbW9kZWwgZmlsZQo=
gnbcucp-gnbcuup-old-model	\N	\N	[]	2024-05-02	Z25iY3VjcC1nbmJjdXVwLW1vZGVsIHlhbmcgbW9kZWwgZmlsZQo=
module-rapp-module	\N	\N	[]	2024-05-01	dGVzdE1vZHVsZQo=
\.

;