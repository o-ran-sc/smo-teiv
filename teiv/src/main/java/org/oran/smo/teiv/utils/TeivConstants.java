/*
 *  ============LICENSE_START=======================================================
 *  Copyright (C) 2024 Ericsson
 *  Modifications Copyright (C) 2024-2025 OpenInfra Foundation Europe
 *  ================================================================================
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  SPDX-License-Identifier: Apache-2.0
 *  ============LICENSE_END=========================================================
 */
package org.oran.smo.teiv.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TeivConstants {
    public static final String ENTITIES = "entities";
    public static final String RELATIONSHIPS = "relationships";
    public static final String API_VERSION = "v1";
    public static final String REQUEST_MAPPING = "/topology-inventory/" + API_VERSION;
    public static final String REQUEST_MAPPING_AUDIT = "topology-inventory-audit/" + API_VERSION;

    public static final String TEIV_DOMAIN = "TEIV";
    public static final String TEIV_DATA_SCHEMA = "teiv_data";
    public static final String TEIV_MODEL_SCHEMA = "teiv_model";
    public static final String TEIV_CONSUMER_DATA_SCHEMA = "teiv_consumer_data";
    public static final String TEIV_DATA = TEIV_DATA_SCHEMA + ".\"%s\"";
    public static final String TEIV_MODEL = "teiv_model.%s";
    public static final String TEIV_CONSUMER_DATA = "teiv_consumer_data.%s";
    public static final String ST_TO_STRING = "ST_AsText(%s)";
    public static final String ST_TO_STRING_COLUMN_WITH_TABLE_NAME = "ST_AsText(%s)";
    public static final String QUOTED_STRING = "\"%s\"";
    public static final String ID_COLUMN_NAME = "id";
    public static final String HASHED_ID_COLUMN_NAME = "hashed_id";
    public static final String CONSUMER_DATA_PREFIX = "CD_";
    public static final String SOURCE_IDS = "sourceIds";
    public static final String CLASSIFIERS = "classifiers";
    public static final String DECORATORS = "decorators";
    public static final String USER_DEFINED_SCHEMAS = "user-defined-schemas";
    public static final String METADATA = "metadata";
    public static final String RESPONSIBLE_ADAPTER = "responsible_adapter";
    public static final String REL_PREFIX = "REL_";
    public static final String PROPERTY_A_SIDE = "aSide";
    public static final String PROPERTY_B_SIDE = "bSide";
    public static final String ITEM = "item";
    public static final String ATTRIBUTES = "attributes";
    public static final String ATTRIBUTES_ABBREVIATION = ".attr.";
    public static final String TARGET_FILTER = "targetFilter";
    public static final String SCOPE_FILTER = "scopeFilter";
    public static final String QUERY = "query";
    public static final String CLOUD_EVENT_WITH_TYPE_CREATE = "create";
    public static final String CLOUD_EVENT_WITH_TYPE_MERGE = "merge";
    public static final String CLOUD_EVENT_WITH_TYPE_DELETE = "delete";
    public static final String CLOUD_EVENT_WITH_TYPE_SOURCE_ENTITY_DELETE = "source-entity-delete";
    public static final long INFINITE_MAXIMUM_CARDINALITY = 9223372036854775807L;
    public static final String FOREIGN_KEY_VIOLATION_ERROR_CODE = "23503";
    public static final String UNIQUE_CONSTRAINT_VIOLATION_CODE = "23505";
    public static final String MODULE_REFERENCE = "module_reference";

    public static final String REL_FK = "REL_FK_";
    public static final String IN_USAGE = "IN_USAGE";
    public static final String MODULE_REFERENCE_NAME = "moduleReferenceName";
    public static final String NAME = "name";
    public static final String SCHEMA_ALREADY_EXISTS = "Schema already exists";
    public static final String SEMICOLON_SEPARATION = "%s:%s";
    public static final String DOT_SEPARATION = "%s.%s";
    public static final String WILDCARD = "*";
    public static final String INVALID_SCHEMA = "Invalid schema";
    public static final String RELIABILITY_INDICATOR = "reliabilityIndicator";

    public static final String TOPOLOGY_INVENTORY_AUDIT_SCHEMA = "topology-inventory:audit:1.0.0";
    public static final String TOPOLOGY_INVENTORY_AUDIT_SOURCE = "topology-inventory:audit";
    public static final String TOPOLOGY_INVENTORY_AUDIT_TYPE = "topology-inventory-audit.initiate";

    public static final String URN_PREFIX = "urn:";
}
