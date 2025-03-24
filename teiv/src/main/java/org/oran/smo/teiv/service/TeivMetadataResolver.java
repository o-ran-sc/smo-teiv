/*
 *  ============LICENSE_START=======================================================
 *  Modifications Copyright (C) 2025 OpenInfra Foundation Europe
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
package org.oran.smo.teiv.service;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.jsonbGetAttributeAsText;
import static org.jooq.impl.DSL.quotedName;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;
import static org.oran.smo.teiv.utils.JooqTypeConverter.toJsonb;
import static org.oran.smo.teiv.utils.TeivConstants.RELIABILITY_INDICATOR;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Record;
import org.oran.smo.teiv.schema.EntityType;
import org.oran.smo.teiv.schema.RelationType;
import org.oran.smo.teiv.schema.RelationshipDataLocation;
import org.oran.smo.teiv.schema.Reliability;
import org.oran.smo.teiv.service.cloudevent.data.Entity;
import org.oran.smo.teiv.service.cloudevent.data.Relationship;
import org.springframework.stereotype.Component;

@Component
public class TeivMetadataResolver {
    static final String FIRST_DISCOVERED = "firstDiscovered";
    static final String LAST_MODIFIED = "lastModified";
    public static final String APPEND_QUERY = "%s.\"%s\" || '%s'";

    /**
     * Determines what changes are needed to metadata of provided entity with respect to reliabilityIndicator,
     * firstDiscovered and lastModified. These changes are put in a metadata map, and this metadata map is put in the
     * provided
     * DB map with the metadata column name as its key.
     * In other words, the following entry will be put in the provided DB map:
     * metadata -> [reliabilityIndicator -> OK,
     * firstDiscovered -> <timestamp String value>, lastModified -> <timestamp String value>]
     * In addition, this metadata change map will be returned by this method.
     * Note: reliabilityIndicator and firstDiscovered will only be included in the metadata map IF NEEDED. The map will
     * always include lastModified at least.
     *
     * @param dslContext
     *     The DSL context
     * @param entityType
     *     The type of entity being ingested
     * @param entity
     *     The entity being ingested
     * @param dbMap
     *     The map of DB column names to values to be merged
     * @return The metadata change.
     */
    public Map<String, Object> setEntityMetadataChangeInDbMap(DSLContext dslContext, EntityType entityType, Entity entity,
            Map<String, Object> dbMap) {
        final List<String> attribReliabilityColumnsToGet = new ArrayList<>();
        final List<String> attributesNotIngested = new ArrayList<>(entityType.getAttributeNames());
        attributesNotIngested.removeIf(attribute -> entity.getAttributes().containsKey(attribute));
        final Record record = selectEntityDataForMetadataResolution(entityType, entity, attribReliabilityColumnsToGet,
                dslContext);
        final HashMap<String, Object> metadataChange = new HashMap<>();
        final String utcTime = getUtcTimeNow();
        final boolean isNewEntity = isNewDbObject(record);
        if (isNewEntity) {
            metadataChange.put(RELIABILITY_INDICATOR, Reliability.OK);
            metadataChange.put(FIRST_DISCOVERED, utcTime);
            metadataChange.put(LAST_MODIFIED, utcTime);
        } else {
            if (isEntityReliabilityToBeSetOk(record, attribReliabilityColumnsToGet)) {
                metadataChange.put(RELIABILITY_INDICATOR, Reliability.OK);
            }
            metadataChange.put(LAST_MODIFIED, utcTime);
        }
        dbMap.put(entityType.getMetadataColumnName(), metadataChange);
        return metadataChange;
    }

    /**
     * Determines what changes are needed to metadata of provided relationship with respect to reliabilityIndicator,
     * firstDiscovered and lastModified. These changes are put in a metadata map, and this metadata map is put in the
     * provided
     * DB map with the metadata column name as its key.
     * In other words, the following entry will be put in the provided DB map:
     * metadata -> [reliabilityIndicator -> OK,
     * firstDiscovered -> <timestamp String value>, lastModified -> <timestamp String value>]
     * In addition, this metadata change map will be returned by this method.
     * Note: reliabilityIndicator and firstDiscovered will only be included in the metadata map IF NEEDED. The map will
     * always include lastModified at least.
     *
     * @param dslContext
     *     The DSL context
     * @param relationType
     *     The type of relationship being ingested
     * @param relationship
     *     The relationship being ingested
     * @param dbMap
     *     The map of DB column names to values to be merged
     * @return The metadata change.
     */
    public Map<String, Object> setRelationMetadataChangeInDbMap(DSLContext dslContext, RelationType relationType,
            Relationship relationship, Map<String, Object> dbMap) {
        final HashMap<String, Object> metadataChange = new HashMap<>();
        final String tableName = relationType.getTableName();
        final String metadataColumnName = relationType.getMetadataColumnName();
        Record record;
        if (relationType.getRelationshipStorageLocation() == RelationshipDataLocation.RELATION) {
            record = selectRelationshipDataForMetadataResolution(tableName, relationType.getIdColumnName(), relationship
                    .getId(), metadataColumnName, dslContext);
        } else {
            record = selectRelationshipDataForMetadataResolution(tableName, relationType.getStoringSideEntityType()
                    .getIdColumnName(), relationship.getStoringSideEntityId(), metadataColumnName, dslContext);
        }
        final boolean isNewRelationship = isNewDbObject(record);
        if (isNewRelationship) {
            metadataChange.put(RELIABILITY_INDICATOR, Reliability.OK);
            metadataChange.put(FIRST_DISCOVERED, getUtcTimeNow());
            metadataChange.put(LAST_MODIFIED, getUtcTimeNow());
        } else {
            if (isRelationReliabilityToBeSetOk(record)) {
                metadataChange.put(RELIABILITY_INDICATOR, Reliability.OK);
            }
            metadataChange.put(LAST_MODIFIED, getUtcTimeNow());
        }
        dbMap.put(metadataColumnName, isNewRelationship ?
                toJsonb(metadataChange) :
                toSqlConcatinationJsonbField(toJsonb(metadataChange), tableName, metadataColumnName));
        return metadataChange;
    }

    /**
     * Get metadata for an inferred entity. It will include all the keys required for an inferred entity - when a
     * relationship
     * is ingested and an entity that has not been ingested.
     *
     * @return map for metadata of an inferred entity
     */
    public static Map<String, Object> getMetadataForInferredEntity() {
        return Map.of(RELIABILITY_INDICATOR, Reliability.ADVISED, FIRST_DISCOVERED, getUtcTimeNow(), LAST_MODIFIED,
                getUtcTimeNow());
    }

    private Record selectEntityDataForMetadataResolution(EntityType entityType, Entity entity, List<String> columnsToGet,
            DSLContext dslContext) {
        return selectColumnsAndMetadataKeysByIdForUpdate(columnsToGet, entityType.getTableName(), entityType
                .getIdColumnName(), entity.getId(), entityType.getMetadataColumnName(), dslContext);
    }

    private Record selectRelationshipDataForMetadataResolution(String tableName, String relIdFieldName, String relId,
            String metadataColumnName, DSLContext dslContext) {
        return dslContext.select(jsonbGetAttributeAsText(field(quotedName(metadataColumnName), JSONB.class),
                RELIABILITY_INDICATOR).as(RELIABILITY_INDICATOR)).select(jsonbGetAttributeAsText(field(quotedName(
                        metadataColumnName), JSONB.class), FIRST_DISCOVERED).as(FIRST_DISCOVERED)).from(table(tableName))
                .where(field(relIdFieldName).eq(relId)).fetchOne();
    }

    private static boolean isNewDbObject(Record record) {
        return record == null || record.get(FIRST_DISCOVERED) == null;
    }

    private Object toSqlConcatinationJsonbField(JSONB jsonb, String tableName, String metadataColumnName) {
        final String sqlConcatJsonb = String.format(APPEND_QUERY, tableName, metadataColumnName, jsonb);
        return field(sqlConcatJsonb, JSONB.class);
    }

    private boolean isEntityReliabilityToBeSetOk(Record record, List<String> attribReliabilityColumnsToGet) {
        if (record == null) {
            return true;//Entity not found in DB
        } else if (isReliabilityOkInDb(record)) {
            return false;
        } else {
            return isAllAttribReliabilitiesOkOrNull(record, attribReliabilityColumnsToGet);
        }
    }

    private boolean isRelationReliabilityToBeSetOk(Record record) {
        if (record == null) {
            return true;//Relationship not found in DB
        } else {
            return !isReliabilityOkInDb(record);
        }
    }

    private static String getUtcTimeNow() {
        return OffsetDateTime.now(ZoneOffset.UTC).toString();
    }

    private Record selectColumnsAndMetadataKeysByIdForUpdate(List<String> columnNames, String tableName, String idFieldName,
            String id, String metadataColumnName, DSLContext dslContext) {
        List<Field<Object>> fields = columnNames.stream().map(n -> field(name(n))).toList();
        return dslContext.select(fields).select(jsonbGetAttributeAsText(field(quotedName(metadataColumnName), JSONB.class),
                RELIABILITY_INDICATOR).as(RELIABILITY_INDICATOR)).select(jsonbGetAttributeAsText(field(quotedName(
                        metadataColumnName), JSONB.class), FIRST_DISCOVERED).as(FIRST_DISCOVERED)).from(table(tableName))
                .where(field(idFieldName).eq(id)).forUpdate().fetchOne();
    }

    private boolean isReliabilityOkInDb(Record currentReliabilitiesInDb) {
        if (currentReliabilitiesInDb.get(RELIABILITY_INDICATOR) == null) {
            return false;//Can happen when entity is being created and merged to in very close succession
        } else {
            return currentReliabilitiesInDb.get(RELIABILITY_INDICATOR).equals(Reliability.OK.name());
        }
    }

    private boolean isAllAttribReliabilitiesOkOrNull(Record record, List<String> attribReliabilitiesToCheck) {
        return attribReliabilitiesToCheck.stream().allMatch(ri -> record.get(ri) == null || record.get(ri).equals(
                Reliability.OK.name()));
    }
}
