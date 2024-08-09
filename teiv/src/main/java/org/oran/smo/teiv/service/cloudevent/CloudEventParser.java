/*
 *  ============LICENSE_START=======================================================
 *  Copyright (C) 2024 Ericsson
 *  Modifications Copyright (C) 2024 OpenInfra Foundation Europe
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
package org.oran.smo.teiv.service.cloudevent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import io.cloudevents.CloudEvent;
import io.cloudevents.CloudEventData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oran.smo.teiv.exception.InvalidRelationshipException;
import org.oran.smo.teiv.exception.YangModelException;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.utils.TiesConstants;
import org.oran.smo.yangtools.parser.data.dom.YangDataDomNode;
import org.springframework.stereotype.Component;
import org.oran.smo.yangtools.parser.data.dom.YangDataDomDocumentRoot;
import org.oran.smo.teiv.service.cloudevent.data.Entity;
import org.oran.smo.teiv.service.cloudevent.data.ParsedCloudEventData;
import org.oran.smo.teiv.service.cloudevent.data.Relationship;
import org.oran.smo.teiv.utils.YangParser;

import static org.oran.smo.teiv.utils.CloudEventUtil.hasInvalidCharacter;

@Slf4j
@Component
@RequiredArgsConstructor
public class CloudEventParser {
    private static final String ENTITIES = "entities";
    private static final String RELATIONSHIPS = "relationships";
    private static final String ILLEGAL_CHARACTER_FOUND = "Illegal character found in relationship %s: %s";
    private final ObjectMapper objectMapper;

    public ParsedCloudEventData getCloudEventData(CloudEvent cloudEvent) {
        final CloudEventData cloudEventData = Objects.requireNonNull(cloudEvent.getData());
        JsonNode eventPayload;
        try {
            eventPayload = objectMapper.readValue(cloudEventData.toBytes(), JsonNode.class);
        } catch (IOException e) {
            log.error("Cannot parse CloudEvent data: ", e);
            return null;
        }

        boolean areSidesMandatory = !cloudEvent.getType().split("\\.")[1].equals(
                TiesConstants.CLOUD_EVENT_WITH_TYPE_DELETE);

        final List<Entity> entities = new ArrayList<>();
        Boolean parsedEntities = processEntities(eventPayload, entities);
        if (parsedEntities.equals(Boolean.FALSE)) {
            return null;
        }

        final List<Relationship> relationships = new ArrayList<>();
        Boolean parsedRelationship = processRelationships(eventPayload, relationships, areSidesMandatory);
        if (parsedRelationship.equals(Boolean.FALSE)) {
            return null;
        }

        return new ParsedCloudEventData(entities, relationships);
    }

    private Boolean processEntities(JsonNode eventPayload, List<Entity> entities) {
        JsonNode entitiesJsonNode = eventPayload.get(ENTITIES);
        if (entitiesJsonNode != null && (entitiesJsonNode.getNodeType() == JsonNodeType.ARRAY)) {
            for (JsonNode entityNode : entitiesJsonNode) {
                if (!parseEntities(entities, entitiesJsonNode, entityNode)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean parseEntities(List<Entity> entities, JsonNode entitiesJsonNode, JsonNode entityNode) {
        try {
            parseEntities(entityNode, entities);
        } catch (IOException | YangModelException e) {
            log.error("Cannot parse entity: " + entitiesJsonNode, e);
            return false;
        }
        return true;
    }

    private Boolean processRelationships(JsonNode eventPayload, List<Relationship> relationships,
            boolean areSidesMandatory) {
        JsonNode relationshipJsonNode = eventPayload.get(RELATIONSHIPS);
        if (relationshipJsonNode != null && (relationshipJsonNode.getNodeType() == JsonNodeType.ARRAY)) {
            return processRelationshipsArray(relationshipJsonNode, relationships, areSidesMandatory);
        }
        return true;
    }

    private boolean processRelationshipsArray(JsonNode relationshipJsonNode, List<Relationship> relationships,
            boolean areSidesMandatory) {
        for (JsonNode relationshipNode : relationshipJsonNode) {
            try {
                parseRelationships(relationshipNode, relationships, areSidesMandatory);
            } catch (IOException e) {
                log.error("Cannot parse relationship: " + relationshipJsonNode, e);
                return false;
            } catch (InvalidRelationshipException e) {
                log.error("Invalid relationship: " + e);
                return false;
            }
        }
        return true;
    }

    private boolean processRelationshipsObject(JsonNode relationshipJsonNode, List<Relationship> relationships,
            boolean areSidesMandatory) {
        try {
            parseRelationships(relationshipJsonNode, relationships, areSidesMandatory);
        } catch (IOException e) {
            log.error("Cannot parse relationship: " + relationshipJsonNode, e);
            return false;
        } catch (InvalidRelationshipException e) {
            log.error("Invalid relationship: " + e);
            return false;
        }
        return true;
    }

    public void parseEntities(JsonNode entitiesJsonNode, List<Entity> entities) throws IOException, YangModelException {
        YangDataDomDocumentRoot entityDom = YangParser.getYangDataDomDocumentRoot(entitiesJsonNode);
        entityDom.getChildren().forEach(child -> {
            Entity entity = new Entity();
            entity.parseObject(child);
            entities.add(entity);
        });
    }

    private static void validateCharactersInId(Entity entity) throws IOException {
        if (hasInvalidCharacter(entity.getId())) {
            throw new IOException("Illegal character found in entity id:" + entity.getId());
        }
    }

    private void parseRelationships(JsonNode relationshipNode, List<Relationship> relationships, boolean areSidesMandatory)
            throws IOException, InvalidRelationshipException {
        YangDataDomDocumentRoot relDom = YangParser.getYangDataDomDocumentRoot(relationshipNode);

        for (YangDataDomNode child : relDom.getChildren()) {
            Relationship relationship = new Relationship();
            relationship.parseObject(child);
            if (relationship.getId() == null) {
                throw new InvalidRelationshipException("Relationship is missing id! " + objectMapper.writeValueAsString(
                        relationship));
            }
            if (areSidesMandatory && (relationship.getASide() == null || relationship.getBSide() == null)) {
                throw new InvalidRelationshipException("Relationship is missing a side! " + relationship.getId());
            }
            if (SchemaRegistry.getRelationTypeByName(relationship.getType()) == null) {
                throw new InvalidRelationshipException("Invalid relationship type! " + relationship.getId());
            }
            if (!SchemaRegistry.getModuleRegistry().containsKey(relationship.getModule())) {
                throw new InvalidRelationshipException("Invalid relationship module! " + relationship.getId());
            }
            String moduleName = SchemaRegistry.getRelationTypeByName(relationship.getType()).getModule().getName();
            if (!moduleName.equals(relationship.getModule())) {
                log.error("Type: {} corresponding module: {}", relationship.getType(), moduleName);
                throw new InvalidRelationshipException("Invalid relationship module-type pair! " + relationship.getId());
            }
            validateCharactersInId(relationship);
            relationships.add(relationship);
        }
    }

    private static void validateCharactersInId(Relationship relationship) throws IOException {
        if (hasInvalidCharacter(relationship.getId())) {
            throw new IOException(String.format(ILLEGAL_CHARACTER_FOUND, "id", relationship.getId()));
        }
        if (hasInvalidCharacter(relationship.getASide())) {
            throw new IOException(String.format(ILLEGAL_CHARACTER_FOUND, "aSide", relationship.getASide()));
        }
        if (hasInvalidCharacter(relationship.getBSide())) {
            throw new IOException(String.format(ILLEGAL_CHARACTER_FOUND, "bSide", relationship.getBSide()));
        }
    }
}
