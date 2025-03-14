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

COPY teiv_consumer_data."module_reference"("name", "namespace", "revision", "content", "ownerAppId", "status") FROM stdin;
test-app-module	urn:o-ran:test-app-module	2024-05-24	bW9kdWxlIHRlc3QtYXBwLW1vZHVsZSB7CgogICAgeWFuZy12ZXJzaW9uIDEuMTsKICAgIG5hbWVzcGFjZSAidXJuOnRlc3QtYXBwLW1vZHVsZSI7CiAgICBwcmVmaXggbW9kdWxlOwoKICAgIGltcG9ydCBvLXJhbi1zbW8tdGVpdi1jb21tb24teWFuZy10eXBlcyB7IHByZWZpeCB0ZXN0OyB9CiAgICBpbXBvcnQgby1yYW4tc21vLXRlaXYtY29tbW9uLXlhbmctZXh0ZW5zaW9ucyB7cHJlZml4IG9yLXRlaXYteWV4dDsgfQoKICAgIHJldmlzaW9uICIyMDI0LTA2LTEwIiB7CiAgICAgICAgZGVzY3JpcHRpb24KICAgICAgICBJbml0aWFsIHJldmlzaW9uLjsKICAgICAgICBvci10ZWl2LXlleHQ6bGFiZWwgMC4zLjA7CiAgICB9CgogICAgYXVnbWVudCAvdGVzdDpkZWNvcmF0b3JzIHsKICAgICAgICBsZWFmIGxvY2F0aW9uIHsKICAgICAgICAgICAgdHlwZSBzdHJpbmc7CiAgICAgICAgfQogICAgICAgIGxlYWYgdmVuZG9yIHsKICAgICAgICAgICAgdHlwZSBzdHJpbmc7CiAgICAgICAgfQogICAgfQoKICAgIGlkZW50aXR5IE91dGRvb3IgewogICAgICAgIGJhc2UgdGVzdDpjbGFzc2lmaWVyczsKICAgIH0KCiAgICBpZGVudGl0eSBSdXJhbCB7CiAgICAgICAgYmFzZSB0ZXN0OmNsYXNzaWZpZXJzOwogICAgfQoKICAgIGlkZW50aXR5IFdlZWtlbmQgewogICAgICAgIGJhc2UgdGVzdDpjbGFzc2lmaWVyczsKICAgIH0KCn0K	APP	IN_USAGE
test-app-for-deletion-module	urn:o-ran:test-app-for-deletion-module	2024-05-24	bW9kdWxlIHRlc3QtYXBwLW1vZHVsZSB7CgogICAgeWFuZy12ZXJzaW9uIDEuMTsKICAgIG5hbWVzcGFjZSAidXJuOnRlc3QtYXBwLW1vZHVsZSI7CiAgICBwcmVmaXggbW9kdWxlOwoKICAgIGltcG9ydCBvLXJhbi1zbW8tdGVpdi1jb21tb24teWFuZy10eXBlcyB7IHByZWZpeCB0ZXN0OyB9CiAgICBpbXBvcnQgby1yYW4tc21vLXRlaXYtY29tbW9uLXlhbmctZXh0ZW5zaW9ucyB7cHJlZml4IG9yLXRlaXYteWV4dDsgfQoKICAgIHJldmlzaW9uICIyMDI0LTA2LTEwIiB7CiAgICAgICAgZGVzY3JpcHRpb24KICAgICAgICBJbml0aWFsIHJldmlzaW9uLjsKICAgICAgICBvci10ZWl2LXlleHQ6bGFiZWwgMC4zLjA7CiAgICB9CgogICAgYXVnbWVudCAvdGVzdDpkZWNvcmF0b3JzIHsKICAgICAgICBsZWFmIGxvY2F0aW9uIHsKICAgICAgICAgICAgdHlwZSBzdHJpbmc7CiAgICAgICAgfQogICAgICAgIGxlYWYgdmVuZG9yIHsKICAgICAgICAgICAgdHlwZSBzdHJpbmc7CiAgICAgICAgfQogICAgfQoKICAgIGlkZW50aXR5IE91dGRvb3IgewogICAgICAgIGJhc2UgdGVzdDpjbGFzc2lmaWVyczsKICAgIH0KCiAgICBpZGVudGl0eSBSdXJhbCB7CiAgICAgICAgYmFzZSB0ZXN0OmNsYXNzaWZpZXJzOwogICAgfQoKICAgIGlkZW50aXR5IFdlZWtlbmQgewogICAgICAgIGJhc2UgdGVzdDpjbGFzc2lmaWVyczsKICAgIH0KCn0K	APP	IN_USAGE
test-app-in-deleting-status	urn:o-ran:test-app-in-deleting-status	2024-10-14	bW9kdWxlIHRlc3QtYXBwLW1vZHVsZSB7CgogICAgeWFuZy12ZXJzaW9uIDEuMTsKICAgIG5hbWVzcGFjZSAidXJuOnRlc3QtYXBwLW1vZHVsZSI7CiAgICBwcmVmaXggbW9kdWxlOwoKICAgIGltcG9ydCBvLXJhbi1zbW8tdGVpdi1jb21tb24teWFuZy10eXBlcyB7IHByZWZpeCB0ZXN0OyB9CiAgICBpbXBvcnQgby1yYW4tc21vLXRlaXYtY29tbW9uLXlhbmctZXh0ZW5zaW9ucyB7cHJlZml4IG9yLXRlaXYteWV4dDsgfQoKICAgIHJldmlzaW9uICIyMDI0LTA2LTEwIiB7CiAgICAgICAgZGVzY3JpcHRpb24KICAgICAgICBJbml0aWFsIHJldmlzaW9uLjsKICAgICAgICBvci10ZWl2LXlleHQ6bGFiZWwgMC4zLjA7CiAgICB9CgogICAgYXVnbWVudCAvdGVzdDpkZWNvcmF0b3JzIHsKICAgICAgICBsZWFmIGxvY2F0aW9uIHsKICAgICAgICAgICAgdHlwZSBzdHJpbmc7CiAgICAgICAgfQogICAgICAgIGxlYWYgdmVuZG9yIHsKICAgICAgICAgICAgdHlwZSBzdHJpbmc7CiAgICAgICAgfQogICAgfQoKICAgIGlkZW50aXR5IE91dGRvb3IgewogICAgICAgIGJhc2UgdGVzdDpjbGFzc2lmaWVyczsKICAgIH0KCiAgICBpZGVudGl0eSBSdXJhbCB7CiAgICAgICAgYmFzZSB0ZXN0OmNsYXNzaWZpZXJzOwogICAgfQoKICAgIGlkZW50aXR5IFdlZWtlbmQgewogICAgICAgIGJhc2UgdGVzdDpjbGFzc2lmaWVyczsKICAgIH0KCn0K	APP	DELETING
\.

COPY teiv_consumer_data."decorators" ("name", "dataType", "moduleReferenceName" )  FROM stdin;
test-app-module:textdata	TEXT	test-app-module
test-app-module:intdata	INT	test-app-module
\.

COPY teiv_consumer_data."classifiers" ("name", "moduleReferenceName" )  FROM stdin;
test-app-module:Indoor	test-app-module
test-app-module:Outdoor	test-app-module
test-app-module:Rural	test-app-module
test-app-module:Weekday	test-app-module
test-app-module:Weekend	test-app-module
\.

COMMIT;