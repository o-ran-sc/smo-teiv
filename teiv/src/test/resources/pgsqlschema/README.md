<!--
  ============LICENSE_START=======================================================
  Copyright (C) 2024 Ericsson
  Modifications Copyright (C) 2025 OpenInfra Foundation Europe
  ================================================================================
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.

  SPDX-License-Identifier: Apache-2.0
  ============LICENSE_END=========================================================
-->

# PGSQL schema generator
During the `generate-resources` phase of the Maven build pgsql-schema-generator will run (see `exec-maven-plugin` in the
`teiv` POM), v1 of the SQL files in this directory are **automatically overwritten** if there are changes to the YANG
models.

**NOTE**: After regenerating SQL files, ensure the new v1 SQL files are checked in to version control. This ensures
that the generated SQL files remain in sync with the resource SQL files.
