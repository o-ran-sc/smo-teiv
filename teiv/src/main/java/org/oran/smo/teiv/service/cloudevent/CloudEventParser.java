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
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import io.cloudevents.CloudEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oran.smo.teiv.CustomMetrics;
import org.oran.smo.teiv.exception.CloudEventParserException;
import org.oran.smo.teiv.exception.YangParsingException;
import org.oran.smo.teiv.exception.YangValidationException;
import org.oran.smo.teiv.schema.SchemaRegistryException;
import org.oran.smo.teiv.utils.CloudEventUtil;
import org.oran.smo.teiv.utils.TiesConstants;
import org.oran.smo.teiv.utils.yangparser.IngestionYangParser;
import org.oran.smo.yangtools.parser.data.dom.YangDataDomNode;
import org.oran.smo.yangtools.parser.data.instance.AbstractStructureInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.oran.smo.teiv.service.cloudevent.data.Entity;
import org.oran.smo.teiv.service.cloudevent.data.ParsedCloudEventData;
import org.oran.smo.teiv.service.cloudevent.data.Relationship;

import static org.oran.smo.teiv.utils.TiesConstants.ENTITIES;
import static org.oran.smo.teiv.utils.TiesConstants.RELATIONSHIPS;

@Slf4j
@Component
@RequiredArgsConstructor
public class CloudEventParser {
    private final CustomMetrics customMetrics;
    private final ObjectMapper objectMapper;

    @Value("${yang-data-validation.ingestion-events}")
    private boolean isYangValidationEnabled;

    public ParsedCloudEventData getCloudEventData(final CloudEvent cloudEvent) {
        try {
            JsonNode eventPayload = processEventPayload(cloudEvent);
            boolean areSidesMandatory = !cloudEvent.getType().split("\\.")[1].equals(
                    TiesConstants.CLOUD_EVENT_WITH_TYPE_DELETE);
            List<Entity> entities = processEntities(eventPayload, areSidesMandatory);
            List<Relationship> relationships = processRelationships(eventPayload, areSidesMandatory);
            if (entities.isEmpty() && relationships.isEmpty()) {
                log.warn("CloudEvent data contains no entities and no relationships. \nEvent id: {}", cloudEvent.getId());
            }
            return new ParsedCloudEventData(entities, relationships);
        } catch (CloudEventParserException e) {
            log.error("{}", e.getMessage());
            return null;
        }
    }

    private JsonNode processEventPayload(final CloudEvent cloudEvent) throws CloudEventParserException {
        try {
            if (cloudEvent == null || cloudEvent.getData() == null) {
                throw CloudEventParserException.noEventData();
            }
            return objectMapper.readValue(Objects.requireNonNull(cloudEvent.getData()).toBytes(), JsonNode.class);
        } catch (IOException e) {
            throw CloudEventParserException.eventDataReading(CloudEventUtil.cloudEventToPrettyString(cloudEvent), e);
        }
    }

    private List<Entity> processEntities(final JsonNode eventPayload, final boolean areSidesMandatory)
            throws CloudEventParserException {
        JsonNode entitiesJsonNode = eventPayload.get(ENTITIES);

        if (entitiesJsonNode == null) {
            return Collections.emptyList();
        } else if (entitiesJsonNode.getNodeType() == JsonNodeType.ARRAY) {
            List<Entity> entities = new ArrayList<>();
            for (JsonNode entityNode : entitiesJsonNode) {
                entities.addAll(parseEntities(entityNode, areSidesMandatory));
            }
            return entities;
        } else {
            throw CloudEventParserException.invalidStructure(entitiesJsonNode.toString());
        }
    }

    private List<Entity> parseEntities(final JsonNode entityNode, final boolean areSidesMandatory)
            throws CloudEventParserException {
        List<Entity> entities = new ArrayList<>();
        try {
            if (isYangValidationEnabled) {
                List<AbstractStructureInstance> abstractInstance = IngestionYangParser.parseData(entityNode.toString(),
                        areSidesMandatory, customMetrics).getStructureChildren().stream().filter(instance -> instance
                                .getDataDomNode() != null).toList();
                for (AbstractStructureInstance structureInstance : abstractInstance) {
                    entities.add(Entity.fromAbstractStructureInstance(structureInstance));
                }
            } else {
                IngestionYangParser.parseData(entityNode.toString()).getChildren().forEach(child -> entities.add(Entity
                        .fromYangDataDom(child)));
            }
        } catch (YangParsingException e) {
            throw CloudEventParserException.entityParsing(entityNode.toString(), e);
        } catch (YangValidationException e) {
            throw CloudEventParserException.entityValidating(entityNode.toString(), e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return entities;
    }

    private List<Relationship> processRelationships(final JsonNode eventPayload, final boolean areSidesMandatory)
            throws CloudEventParserException {
        JsonNode relationshipJsonNode = eventPayload.get(RELATIONSHIPS);

        if (relationshipJsonNode == null) {
            return Collections.emptyList();
        } else if (relationshipJsonNode.getNodeType() == JsonNodeType.ARRAY) {
            List<Relationship> relationships = new ArrayList<>();
            for (JsonNode relationshipNode : relationshipJsonNode) {
                relationships.addAll(parseRelationships(relationshipNode, areSidesMandatory));
            }
            return relationships;
        } else {
            throw CloudEventParserException.invalidStructure(relationshipJsonNode.toString());
        }
    }

    private List<Relationship> parseRelationships(final JsonNode relationshipNode, final boolean areSidesMandatory)
            throws CloudEventParserException {
        List<Relationship> relationships = new ArrayList<>();
        try {
            List<YangDataDomNode> relationshipNodes = IngestionYangParser.parseData(relationshipNode.toString())
                    .getChildren();
            for (YangDataDomNode singleNode : relationshipNodes) {
                relationships.add(Relationship.fromYangDataDom(singleNode, areSidesMandatory));
            }
        } catch (SchemaRegistryException | YangParsingException e) {
            throw CloudEventParserException.relationshipParsing(relationshipNode.toString(), e);
        } catch (YangValidationException e) {
            throw CloudEventParserException.relationshipValidating(relationshipNode.toString(), e);
        }

        return relationships;
    }
}
