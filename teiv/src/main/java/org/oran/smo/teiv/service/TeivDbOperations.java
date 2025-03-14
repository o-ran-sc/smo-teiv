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
package org.oran.smo.teiv.service;

import static org.jooq.impl.DSL.jsonbExists;
import static org.oran.smo.teiv.schema.BidiDbNameMapper.getDbName;
import static org.oran.smo.teiv.schema.RelationshipDataLocation.B_SIDE;
import static org.oran.smo.teiv.schema.RelationshipDataLocation.RELATION;
import static org.oran.smo.teiv.service.TeivMetadataResolver.APPEND_QUERY;
import static org.oran.smo.teiv.service.TeivMetadataResolver.getMetadataForInferredEntity;
import static org.oran.smo.teiv.service.models.OperationResult.ENTITY_CATEGORY;
import static org.oran.smo.teiv.service.models.OperationResult.RELATIONSHIP_CATEGORY;
import static org.oran.smo.teiv.utils.JooqTypeConverter.toJsonb;
import static org.oran.smo.teiv.utils.TeivConstants.FOREIGN_KEY_VIOLATION_ERROR_CODE;
import static org.oran.smo.teiv.utils.TeivConstants.ID_COLUMN_NAME;
import static org.oran.smo.teiv.utils.TeivConstants.QUOTED_STRING;
import static org.oran.smo.teiv.utils.TeivConstants.UNIQUE_CONSTRAINT_VIOLATION_CODE;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.SQLDataType.OTHER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.jooq.Field;
import org.jooq.JSONB;
import org.oran.smo.teiv.exception.IllegalManyToManyRelationshipUpdateException;
import org.oran.smo.teiv.exception.IllegalOneToManyRelationshipUpdateException;
import org.jooq.Configuration;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.schema.Persistable;
import org.oran.smo.teiv.schema.ResponsibleAdapter;
import org.oran.smo.teiv.utils.JooqTypeConverter;
import org.springframework.stereotype.Component;

import org.jooq.Record;

import org.oran.smo.teiv.exception.InvalidFieldInYangDataException;
import org.oran.smo.teiv.exception.RelationshipIdCollisionException;
import org.oran.smo.teiv.exception.UniqueRelationshipIdConstraintException;
import org.oran.smo.teiv.ingestion.validation.IngestionOperationValidatorFactory;
import org.oran.smo.teiv.ingestion.validation.MaximumCardinalityViolationException;
import org.oran.smo.teiv.service.models.OperationResult;
import org.oran.smo.teiv.schema.DataType;
import org.oran.smo.teiv.schema.EntityType;
import org.oran.smo.teiv.schema.RelationType;
import org.oran.smo.teiv.schema.RelationshipDataLocation;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.service.cloudevent.data.Entity;
import org.oran.smo.teiv.service.cloudevent.data.ParsedCloudEventData;
import org.oran.smo.teiv.service.cloudevent.data.Relationship;

import org.oran.smo.teiv.utils.TeivConstants;
import org.oran.smo.teiv.utils.schema.Geography;

@Component
@RequiredArgsConstructor
public class TeivDbOperations {

    private final TeivDbService teivDbService;

    private final IngestionOperationValidatorFactory ingestionOperationValidatorFactory;

    private final RelationshipMergeValidator relationshipMergeValidator;

    private final TeivMetadataResolver teivMetadataResolver;

    /**
     * Insert or update a row in the given table.
     * <p>
     * Note: Free version of Jooq doesn't support spatial types like Geography. Instead of that the OTHER type is used
     * </p>
     *
     * @param context
     *     The context where the database operation should be executed
     * @param objectType
     *     The type of object being ingested.
     * @param values
     *     A map of column name and value pairs. The value is converted to the corresponding Postgres type based on the
     *     dynamic type of the value. For
     *     example: <"column1", 5L> means that the value 5 should be inserted to the column1 as a BIGINT.
     * @return Xmax value, if 0 it was a insert, if greater than 0 it was an update, if -1 that means we didn't get the xmax
     *     value back after the operation.
     */

    public int merge(DSLContext context, Persistable objectType, Map<String, Object> values) {
        final String tableName = objectType.getTableName();
        Map<String, Object> valuesToInsert = new HashMap<>(values.size());
        values.forEach((key, value) -> {
            if ((value instanceof Geography)) {
                valuesToInsert.put(key, field("'" + value + "'", OTHER));
            } else if (value instanceof Map<?, ?> mapValue) {
                valuesToInsert.put(key, toJsonb(mapValue));
            } else {
                valuesToInsert.put(key, value);
            }
        });
        Map<String, Object> valuesToUpdate = new HashMap<>(valuesToInsert);
        valuesToUpdate.remove(ID_COLUMN_NAME);

        if (valuesToUpdate.isEmpty()) {//If there is no data in this event then only do a DB insert, no DB update.
            return context.insertInto(table(tableName)).set(valuesToInsert).onConflict(field(ID_COLUMN_NAME)).doNothing()
                    .execute() > 0 ? 0 : -1;
        }
        if (values.containsKey(objectType.getMetadataColumnName())) {
            final String sqlConcatJsonb = String.format(APPEND_QUERY, tableName, objectType.getMetadataColumnName(),
                    toJsonb(values.get(objectType.getMetadataColumnName())));
            valuesToUpdate.put(objectType.getMetadataColumnName(), field(sqlConcatJsonb, JSONB.class));
        }
        //Do an insert or update depending on conflict
        Record fetch = context.insertInto(table(tableName)).set(valuesToInsert).onConflict(field(ID_COLUMN_NAME)).doUpdate()
                .set(valuesToUpdate).returning(field("xmax", int.class)).fetchOne();
        if (fetch != null) {
            return fetch.getValue("xmax", int.class);
        }
        return -1;
    }

    public List<OperationResult> deleteEntity(DSLContext context, EntityType entityType, String entityId) {
        List<OperationResult> result = new ArrayList<>();
        result.addAll(clearRelationshipsOnEntityDelete(context, entityType, entityId));

        int affectedRows = context.delete(table(entityType.getTableName())).where(field(ID_COLUMN_NAME).eq(entityId))
                .execute();
        if (affectedRows > 0) {
            result.add(OperationResult.builder().id(entityId).type(entityType.getName()).category(ENTITY_CATEGORY).build());
        }
        return result;
    }

    public List<OperationResult> deleteRelationshipByManySideEntityId(DSLContext context, String manySideEntityId,
            String manySideEntityIdColumn, RelationType relationType) {
        String oneSideEntityIdColumn = relationType.getRelationshipStorageLocation().equals(B_SIDE) ?
                relationType.aSideColumnName() :
                relationType.bSideColumnName();

        List<OperationResult> relationshipList = context.select(field(String.format(QUOTED_STRING, relationType
                .getIdColumnName()), String.class)).from(table(relationType.getTableName())).where(field(String.format(
                        QUOTED_STRING, manySideEntityIdColumn)).eq(manySideEntityId)).forUpdate().fetchInto(String.class)
                .stream().filter(Objects::nonNull).map(id -> OperationResult.builder().id(id).type(relationType.getName())
                        .category(RELATIONSHIP_CATEGORY).build()).collect(Collectors.toList());
        if (relationshipList.isEmpty()) {
            return relationshipList;
        } else {
            int updateResult = context.update(table(relationType.getTableName())).setNull(field(String.format(QUOTED_STRING,
                    relationType.getIdColumnName()))).setNull(field(String.format(QUOTED_STRING, oneSideEntityIdColumn)))
                    .set(field(String.format(QUOTED_STRING, relationType.getSourceIdsColumnName())), toJsonb(List.of()))
                    .setNull(field(String.format(QUOTED_STRING, relationType.getMetadataColumnName()))).where(field(String
                            .format(QUOTED_STRING, manySideEntityIdColumn)).eq(manySideEntityId)).execute();
            return updateResult > 0 ? relationshipList : List.of();
        }

    }

    public Optional<OperationResult> deleteRelationFromEntityTableByRelationId(DSLContext context, String relationshipId,
            RelationType relationType) {
        String oneSideEntityIdColumn = relationType.getRelationshipStorageLocation().equals(B_SIDE) ?
                relationType.aSideColumnName() :
                relationType.bSideColumnName();
        int affectedRows = context.update(table(relationType.getTableName())).setNull(field(String.format(QUOTED_STRING,
                relationType.getIdColumnName()))).setNull(field(String.format(QUOTED_STRING, oneSideEntityIdColumn))).set(
                        field(String.format(QUOTED_STRING, relationType.getSourceIdsColumnName())), toJsonb(List.of()))
                .setNull(field(String.format(QUOTED_STRING, relationType.getMetadataColumnName()))).where(field(String
                        .format(QUOTED_STRING, relationType.getIdColumnName())).eq(relationshipId)).execute();
        return affectedRows > 0 ?
                Optional.of(OperationResult.builder().id(relationshipId).type(relationType.getName()).category(
                        RELATIONSHIP_CATEGORY).build()) :
                Optional.empty();
    }

    public List<OperationResult> deleteManyToManyRelationByEntityId(DSLContext context, RelationType relationType,
            String entityId, String aSideColumnName, String bSideColumnName) {
        List<String> deletedIds = context.delete(table((relationType.getTableName()))).where(field(String.format(
                QUOTED_STRING, aSideColumnName)).eq(entityId).or(field(String.format(QUOTED_STRING, bSideColumnName)).eq(
                        entityId))).returning(field(TeivConstants.ID_COLUMN_NAME)).fetch().getValues(field(
                                TeivConstants.ID_COLUMN_NAME), String.class);

        return deletedIds.stream().map(id -> OperationResult.builder().id(id).type(relationType.getName()).category(
                RELATIONSHIP_CATEGORY).build()).collect(Collectors.toList());
    }

    public Optional<OperationResult> deleteManyToManyRelationByRelationId(DSLContext context, RelationType relationType,
            String relationshipId) {
        int affectedRows = context.delete(table(relationType.getTableName())).where(field(ID_COLUMN_NAME).eq(
                relationshipId)).execute();
        return affectedRows > 0 ?
                Optional.of(OperationResult.builder().id(relationshipId).type(relationType.getName()).category(
                        RELATIONSHIP_CATEGORY).build()) :
                Optional.empty();
    }

    public List<OperationResult> executeEntityAndRelationshipMergeOperations(ParsedCloudEventData parsedCloudEventData,
            String sourceAdapter) throws InvalidFieldInYangDataException, MaximumCardinalityViolationException {
        List<Consumer<DSLContext>> dbOperations = new ArrayList<>();
        List<OperationResult> results = new ArrayList<>();
        ResponsibleAdapter responsibleAdapter = new ResponsibleAdapter(sourceAdapter);
        byte[] respAdapterHash = responsibleAdapter.getHashedId();
        dbOperations.add(getResponsibleAdapterOperation(responsibleAdapter));
        for (Entity entity : parsedCloudEventData.getEntities()) {
            dbOperations.add(getEntityOperation(entity, results, respAdapterHash));
        }
        for (Relationship relationship : parsedCloudEventData.getRelationships()) {
            dbOperations.addAll(getRelationshipOperations(relationship, results, respAdapterHash));
        }
        dbOperations.add(dslContext -> ingestionOperationValidatorFactory.createValidator(dslContext).validate(
                parsedCloudEventData));
        teivDbService.execute(dbOperations);
        return results;
    }

    public Consumer<DSLContext> getResponsibleAdapterOperation(ResponsibleAdapter responsibleAdapter) throws TeivException {

        Map<String, Object> dbMap = new HashMap<>();
        dbMap.put(responsibleAdapter.getIdColumnName(), responsibleAdapter.getId());
        dbMap.put(responsibleAdapter.getHashIdsColumnName(), responsibleAdapter.getHashedId());

        return dslContext -> dslContext.insertInto(table(responsibleAdapter.getTableName())).set(dbMap).onConflict(field(
                responsibleAdapter.getIdColumnName())).doNothing().execute();
    }

    public List<String> selectByCmHandleFormSourceIds(DSLContext context, String tableName, String cmHandle) {
        String path = String.format("$[*] ? (@ == \"urn:cmHandle:%s\")", cmHandle);
        return context.select(field(String.format(QUOTED_STRING, "id"), String.class)).from(tableName).where(jsonbExists(
                field(String.format(QUOTED_STRING, "CD_sourceIds"), JSONB.class), path)).orderBy(field(String.format(
                        QUOTED_STRING, "id"))).fetch().getValues(field(String.format(QUOTED_STRING, "id"), String.class));
    }

    private Consumer<DSLContext> getEntityOperation(Entity entity, List<OperationResult> results, byte[] respAdapterHash)
            throws InvalidFieldInYangDataException {
        EntityType entityType = SchemaRegistry.getEntityTypeByName(entity.getType());
        Map<String, DataType> fieldsFromModel = entityType.getFields();
        Map<String, Object> dbMap = new HashMap<>();
        List<String> resultExclusion = new ArrayList<>();
        for (Map.Entry<String, Object> entry : entity.getAttributes().entrySet()) {
            final String attributeName = entry.getKey();
            final String dbName = getDbName(attributeName);
            DataType dataType = fieldsFromModel.get(attributeName);
            if (dataType == null) {
                throw new InvalidFieldInYangDataException(String.format(
                        "Received field: %s isn't a valid field of entity type: %s", attributeName, entity.getType()));
            }
            switch (dataType) {
                case GEOGRAPHIC -> dbMap.put(dbName, JooqTypeConverter.toGeography(entry.getValue()));
                case CONTAINER -> dbMap.put(dbName, toJsonb(entry.getValue()));
                case PRIMITIVE, DECIMAL, INTEGER, BIGINT, BYTEA -> dbMap.put(dbName, entry.getValue());
            }
        }
        dbMap.put(ID_COLUMN_NAME, entity.getId());
        if (entity.getSourceIds() != null) {
            dbMap.put(entityType.getSourceIdsColumnName(), toJsonb(entity.getSourceIds()));
        }
        return dslContext -> {
            final Map<String, Object> metadata = teivMetadataResolver.setEntityMetadataChangeInDbMap(dslContext, entityType,
                    entity, dbMap);
            int xmax = merge(dslContext, entityType, dbMap);
            if (xmax >= 0) {
                boolean isUpdatedInDb = xmax != 0;
                dbMap.remove(ID_COLUMN_NAME);
                dbMap.remove(entityType.getSourceIdsColumnName());
                dbMap.remove(entityType.getMetadataColumnName());
                resultExclusion.forEach(dbMap::remove);
                results.add(OperationResult.builder().id(entity.getId()).type(entity.getType()).category(ENTITY_CATEGORY)
                        .attributes(dbMap).sourceIds(entity.getSourceIds()).isUpdatedInDb(isUpdatedInDb).metadata(metadata)
                        .build());

            }
        };
    }

    private List<Consumer<DSLContext>> getRelationshipOperations(Relationship relationship, List<OperationResult> results,
            byte[] respAdapterByteArray) {
        List<Consumer<DSLContext>> relationshipOperations = new ArrayList<>();
        RelationType relationType = SchemaRegistry.getRelationTypeByName(relationship.getType());
        RelationshipDataLocation relationshipDataLocation = relationType.getRelationshipStorageLocation();

        Map<String, Object> dbMap = new HashMap<>();
        dbMap.put(relationType.getIdColumnName(), relationship.getId());
        dbMap.put(relationType.aSideColumnName(), relationship.getASide());
        dbMap.put(relationType.bSideColumnName(), relationship.getBSide());
        if (relationship.getSourceIds() != null) {
            dbMap.put(relationType.getSourceIdsColumnName(), toJsonb(relationship.getSourceIds()));
        }
        if (relationshipDataLocation == RELATION) {
            relationshipOperations.add(outer -> {
                try {
                    outer.dsl().transaction((Configuration nested) -> mergeManyToManyRelationship(nested.dsl(),
                            relationship, results, relationType, dbMap));
                } catch (DataAccessException e) {
                    if (e.sqlState().equals(FOREIGN_KEY_VIOLATION_ERROR_CODE)) {
                        createMissingEntities(outer, relationship, relationType, results, respAdapterByteArray);
                        mergeManyToManyRelationship(outer, relationship, results, relationType, dbMap);
                    } else {
                        throw e;
                    }
                }
            });
        } else {
            relationshipOperations.add(dslContext -> mergeOneToManyOrOneToOneRelationship(dslContext, relationship, results,
                    relationType, dbMap, respAdapterByteArray));
        }

        return relationshipOperations;
    }

    private void mergeOneToManyOrOneToOneRelationship(DSLContext dslContext, Relationship relationship,
            List<OperationResult> results, RelationType relationType, Map<String, Object> dbMap,
            byte[] respAdapterByteArray) {
        AtomicBoolean isManySideEntityMissingAtTheBeginning = new AtomicBoolean(false);
        final Map<String, Object> metadataMissingEntity = getMetadataForInferredEntity();
        try {
            dslContext.dsl().transaction((Configuration nested) -> updateRelationshipInEntityTable(nested.dsl(),
                    relationship, relationType, dbMap).ifPresentOrElse(results::add, () -> {
                        Record manySideRow = selectColumnsByIdForUpdate(List.of(relationType.aSideColumnName(), relationType
                                .bSideColumnName(), relationType.getIdColumnName()), relationType.getTableName(),
                                ID_COLUMN_NAME, relationship.getStoringSideEntityId(), dslContext);
                        if (manySideRow == null) {
                            isManySideEntityMissingAtTheBeginning.set(true);
                            createMissingStoringSideEntity(dslContext, relationship, relationType, respAdapterByteArray,
                                    metadataMissingEntity);
                            addEntityToOperationResults(results, relationship.getStoringSideEntityId(),
                                    metadataMissingEntity, relationType.getStoringSideEntityType().getName());
                            updateRelationshipInEntityTable(dslContext, relationship, relationType, dbMap).ifPresentOrElse(
                                    results::add, () -> {
                                        throw new IllegalOneToManyRelationshipUpdateException(relationship, true);
                                    });
                        } else {
                            handleOneToManyRelationshipFaults(manySideRow, relationship, relationType);
                        }
                    }));
        } catch (DataAccessException e) {
            if (e.sqlState().equals(UNIQUE_CONSTRAINT_VIOLATION_CODE)) {
                throw new UniqueRelationshipIdConstraintException(relationship);
            } else if (e.sqlState().equals(FOREIGN_KEY_VIOLATION_ERROR_CODE)) {
                if (isManySideEntityMissingAtTheBeginning.get()) {
                    createMissingStoringSideEntity(dslContext, relationship, relationType, respAdapterByteArray,
                            metadataMissingEntity);
                    addEntityToOperationResults(results, relationship.getStoringSideEntityId(), metadataMissingEntity,
                            relationType.getStoringSideEntityType().getName());
                }
                createMissingNotStoringSideEntity(dslContext, relationship, relationType, respAdapterByteArray,
                        metadataMissingEntity);
                addEntityToOperationResults(results, relationship.getNotStoringSideEntityId(), metadataMissingEntity,
                        relationType.getNotStoringSideEntityType().getName());
                updateRelationshipInEntityTable(dslContext, relationship, relationType, dbMap).ifPresentOrElse(results::add,
                        () -> {
                            throw new IllegalOneToManyRelationshipUpdateException(relationship, false);
                        });
            } else {
                throw e;
            }
        }
    }

    private Optional<OperationResult> updateRelationshipInEntityTable(DSLContext dslContext, Relationship relationship,
            RelationType relationType, Map<String, Object> values) {
        final Map<String, Object> metadata = teivMetadataResolver.setRelationMetadataChangeInDbMap(dslContext, relationType,
                relationship, values);
        Condition condition = field(ID_COLUMN_NAME).eq(relationship.getStoringSideEntityId()).and(field(name(relationType
                .getIdColumnName())).isNull().or(field(name(relationType.getIdColumnName())).eq(relationship.getId()).and(
                        field(name(relationType.getNotStoringSideEntityIdColumnNameInStoringSideTable())).eq(relationship
                                .getNotStoringSideEntityId()))));
        Map<String, Object> valuesToUpdate = new HashMap<>(values);
        valuesToUpdate.remove(ID_COLUMN_NAME);
        Record fetch = dslContext.update(table(relationType.getTableName())).set(valuesToUpdate).where(condition).returning(
                field("xmax", int.class)).fetchOne();

        if (fetch != null) {
            boolean isUpdatedInDb = fetch.getValue("xmax", int.class) != 0;
            return Optional.of(OperationResult.builder().id(relationship.getId()).type(relationship.getType()).category(
                    RELATIONSHIP_CATEGORY).aSide(relationship.getASide()).bSide(relationship.getBSide()).sourceIds(
                            relationship.getSourceIds()).isUpdatedInDb(isUpdatedInDb).metadata(metadata).build());
        }
        return Optional.empty();
    }

    private void handleOneToManyRelationshipFaults(Record manySideRow, Relationship relationship,
            RelationType relationType) {
        if (relationshipMergeValidator.anotherRelationshipAlreadyExistsOnStoringSideEntity(manySideRow, relationType,
                relationship)) {
            String manySideEntityType = relationType.getStoringSideEntityType().getName();
            String exceptionMessage = String.format(
                    "Another relationship with id %s of type %s already exists on entity with id %s of type %s, can't override it with new relationship with id %s",
                    manySideRow.get(relationType.getIdColumnName()), relationType.getName(), relationship
                            .getStoringSideEntityId(), manySideEntityType, relationship.getId());
            throw new MaximumCardinalityViolationException(exceptionMessage);
        } else if (relationshipMergeValidator.relationshipAlreadyExistsWithDifferentNotStoringSideEntity(manySideRow,
                relationship, relationType)) {
            throw new RelationshipIdCollisionException(relationship);
        }
    }

    private void mergeManyToManyRelationship(DSLContext dslContext, Relationship relationship,
            List<OperationResult> results, RelationType relationType, Map<String, Object> valuesToInsert) {
        final Map<String, Object> valuesToUpdate = new HashMap<>(valuesToInsert);
        final Map<String, Object> metadata = teivMetadataResolver.setRelationMetadataChangeInDbMap(dslContext, relationType,
                relationship, valuesToUpdate);
        valuesToInsert.put(relationType.getMetadataColumnName(), toJsonb(metadata));
        String primaryKeyColumn = relationType.getIdColumnName();
        valuesToUpdate.remove(primaryKeyColumn);
        Record fetch = dslContext.insertInto(table(relationType.getTableName())).set(valuesToInsert).onConflict(field(
                primaryKeyColumn)).doUpdate().set(valuesToUpdate).where(field(relationType.getTableName() + "." + name(
                        relationType.aSideColumnName())).eq(relationship.getASide()).and(field(relationType
                                .getTableName() + "." + name(relationType.bSideColumnName())).eq(relationship.getBSide())))
                .returning(field("xmax", int.class)).fetchOne();
        if (fetch != null) {
            boolean isUpdatedInDb = fetch.getValue("xmax", int.class) != 0;
            results.add(OperationResult.builder().id(relationship.getId()).type(relationship.getType()).category(
                    RELATIONSHIP_CATEGORY).aSide(relationship.getASide()).bSide(relationship.getBSide()).sourceIds(
                            relationship.getSourceIds()).isUpdatedInDb(isUpdatedInDb).metadata(metadata).build());
        } else {
            throw new IllegalManyToManyRelationshipUpdateException(relationship);
        }
    }

    private void createMissingEntities(DSLContext dslContext, Relationship relationship, RelationType relationType,
            List<OperationResult> results, byte[] respAdapterByteArray) {
        String aSideTableName = relationType.getASide().getTableName();
        String aSideId = relationship.getASide();
        String bSideTableName = relationType.getBSide().getTableName();
        String bSideId = relationship.getBSide();
        String relationshipId = relationship.getId();
        final Map<String, Object> metadata = TeivMetadataResolver.getMetadataForInferredEntity();

        if (createMissingEntity(aSideTableName, aSideId, relationshipId, dslContext, relationType.getASide(),
                respAdapterByteArray, metadata) == 1) {
            results.add(OperationResult.builder().id(aSideId).type(relationType.getASide().getName()).category(
                    ENTITY_CATEGORY).sourceIds(List.of(relationshipId)).metadata(metadata).build());
        }
        if (createMissingEntity(bSideTableName, bSideId, relationshipId, dslContext, relationType.getBSide(),
                respAdapterByteArray, metadata) == 1) {
            results.add(OperationResult.builder().id(bSideId).type(relationType.getBSide().getName()).category(
                    ENTITY_CATEGORY).sourceIds(List.of(relationshipId)).metadata(metadata).build());
        }
    }

    private List<OperationResult> clearRelationshipsOnEntityDelete(DSLContext dslContext, EntityType entityType,
            String entityId) {
        List<OperationResult> deletedIds = new ArrayList<>();
        List<RelationType> relationTypes = SchemaRegistry.getRelationTypesByEntityName(entityType.getName());

        for (RelationType relationType : relationTypes) {
            String columnNameForWhereCondition = (relationType.getASide().getName().equals(entityType.getName())) ?
                    relationType.aSideColumnName() :
                    relationType.bSideColumnName();
            if (relationType.getRelationshipStorageLocation() == RELATION) {
                deletedIds.addAll(deleteManyToManyRelationByEntityId(dslContext, relationType, entityId, relationType
                        .aSideColumnName(), relationType.bSideColumnName()));

            } else {
                deletedIds.addAll(deleteRelationshipByManySideEntityId(dslContext, entityId, columnNameForWhereCondition,
                        relationType));
            }
        }
        return deletedIds;
    }

    private int createMissingEntity(String entityTableName, String entityId, String relationshipId, DSLContext dslContext,
            EntityType entityType, byte[] respAdapterByteArray, Map<String, Object> metadata) {
        final JSONB sourceIds = toJsonb(relationshipId);
        return dslContext.insertInto(table(entityTableName)).set(field(name(ID_COLUMN_NAME)), entityId).set(field(name(
                entityType.getSourceIdsColumnName())), sourceIds).set(field(name(entityType.getMetadataColumnName())),
                        toJsonb(metadata)).onConflictDoNothing().execute();
    }

    private void createMissingNotStoringSideEntity(DSLContext dslContext, Relationship relationship,
            RelationType relationType, byte[] respAdapterByteArray, Map<String, Object> metadata) {
        createMissingEntity(relationType.getNotStoringSideTableName(), relationship.getNotStoringSideEntityId(),
                relationship.getId(), dslContext, relationType.getNotStoringSideEntityType(), respAdapterByteArray,
                metadata);
    }

    private void createMissingStoringSideEntity(DSLContext dslContext, Relationship relationship, RelationType relationType,
            byte[] respAdapterByteArray, Map<String, Object> metadata) {
        createMissingEntity(relationType.getTableName(), relationship.getStoringSideEntityId(), relationship.getId(),
                dslContext, relationType.getStoringSideEntityType(), respAdapterByteArray, metadata);
    }

    private Record selectColumnsByIdForUpdate(List<String> columnNames, String tableName, String idFieldName,
            String manySideEntityId, DSLContext dslContext) {
        List<Field<Object>> fields = columnNames.stream().map(n -> field(name(n))).toList();
        return dslContext.select(fields).from(table(tableName)).where(field(idFieldName).eq(manySideEntityId)).forUpdate()
                .fetchOne();
    }

    private void addEntityToOperationResults(List<OperationResult> results, String entityId, Map<String, Object> metadata,
            String entityType) {
        OperationResult result = OperationResult.builder().id(entityId).type(entityType).category(ENTITY_CATEGORY).metadata(
                metadata).build();
        if (!results.contains(result)) {
            results.add(result);
        }
    }

}
