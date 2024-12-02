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

CREATE OR REPLACE FUNCTION ties_data.create_enum_type(
    schema_name TEXT, type_name TEXT, enum_values TEXT[]
) RETURNS VOID AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type t JOIN pg_namespace n ON n.oid = t.typnamespace WHERE t.typname = type_name AND n.nspname = schema_name) THEN
        EXECUTE format('CREATE TYPE %I.%I AS ENUM (%s)',schema_name, type_name, array_to_string(ARRAY(SELECT quote_literal(value) FROM unnest(enum_values) AS value), ', '));
    END IF;
END;
$$ language 'plpgsql';

SELECT ties_data.create_enum_type('ties_data', 'Reliability', ARRAY['OK', 'RESTORED', 'ADVISED']);

CREATE TABLE IF NOT EXISTS ties_data."responsible_adapter" (
	"id"			TEXT,
	"hashed_id"			BYTEA
);

SELECT ties_data.create_constraint_if_not_exists(
	'responsible_adapter',
 'PK_responsible_adapter_id',
 'ALTER TABLE ties_data."responsible_adapter" ADD CONSTRAINT "PK_responsible_adapter_id" PRIMARY KEY ("id");'
);

SELECT ties_data.create_constraint_if_not_exists(
	'responsible_adapter',
 'UNIQUE_responsible_adapter_hashed_id',
 'ALTER TABLE ties_data."responsible_adapter" ADD CONSTRAINT "UNIQUE_responsible_adapter_hashed_id" UNIQUE ("hashed_id");'
);
