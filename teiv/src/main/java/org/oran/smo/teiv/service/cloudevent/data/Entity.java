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
package org.oran.smo.teiv.service.cloudevent.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Getter;

import lombok.extern.slf4j.Slf4j;
import org.oran.smo.teiv.exception.IllegalCharacterException;
import org.oran.smo.teiv.schema.EntityType;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.schema.SchemaRegistryException;
import org.oran.smo.yangtools.parser.data.dom.YangDataDomNode;
import org.oran.smo.yangtools.parser.data.instance.AbstractContentInstance;
import org.oran.smo.yangtools.parser.data.instance.AbstractStructureInstance;
import org.oran.smo.yangtools.parser.model.statements.StatementModuleAndName;
import org.oran.smo.yangtools.parser.util.NamespaceModuleIdentifier;

import static org.oran.smo.teiv.utils.CloudEventUtil.hasInvalidCharacter;
import static org.oran.smo.teiv.utils.TeivConstants.QUOTED_STRING;

@Slf4j
public class Entity extends ModuleObject {

    @Getter
    private final Map<String, Object> attributes;

    private static final ObjectWriter objectWriter = new ObjectMapper().writer();

    public Entity(String module, String type, String id, Map<String, Object> attributes, List<String> sourceIds) {
        super(module, type, id, sourceIds);
        if (attributes == null) {
            this.attributes = new HashMap<>();
        } else {
            this.attributes = new HashMap<>(attributes);
        }
    }

    public Entity() {
        this.attributes = new HashMap<>();
    }

    public static Entity fromAbstractStructureInstance(final AbstractStructureInstance instance)
            throws IllegalCharacterException, JsonProcessingException {
        Entity entity = new Entity();
        entity.parseObjectFromValidatedInstance(instance);
        validateEntityIdentifierCharacters(entity);
        return entity;
    }

    public static Entity fromYangDataDom(final YangDataDomNode node) {
        Entity entity = new Entity();
        entity.parseObject(node);
        return entity;
    }

    @Override
    public void parseObject(final YangDataDomNode node) {
        final String name = node.getName();
        if ("attributes".equals(name)) {
            setAttributes(node);
        } else {
            super.parseObject(node);
            node.getChildren().forEach(this::parseObject);
        }
    }

    private void setAttributes(final YangDataDomNode node) {
        node.getChildren().forEach(this::handleJsonObjects);
        attributes.keySet().forEach(name -> {
            final Object v = attributes.get(name);
            if (v instanceof List) {
                attributes.put(name, v.toString());
            }
        });
    }

    private boolean isJsonObject(final YangDataDomNode node) {
        return !node.getChildren().isEmpty();
    }

    private void handleJsonObjects(final YangDataDomNode node) {
        final Object object = isJsonObject(node) ? jsonToString(node) : node.getValue();
        final String name = node.getName();
        if (attributes.containsKey(name)) {
            Object v = attributes.get(name);
            if (v instanceof List) {
                ((List) v).add(object);
            } else {
                List<Object> l = new ArrayList<>();
                l.add(v);
                l.add(object);
                attributes.put(name, l);
            }
        } else {
            attributes.put(name, object);
        }
    }

    private String jsonToString(final YangDataDomNode node) {
        if (node.getChildren().isEmpty()) {
            final Object value = node.getValue();
            if (value instanceof String) {
                return getQuotedString(value.toString());
            }
            return String.valueOf(value);
        } else {
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Iterator<YangDataDomNode> it = node.getChildren().iterator(); it.hasNext(); first = false) {
                final YangDataDomNode actual = it.next();
                if (!first) {
                    sb.append(",");
                }
                sb.append(getQuotedString(actual.getName())).append(":").append(jsonToString(actual));
            }
            return sb.append("}").toString();
        }
    }

    private String getQuotedString(final String str) {
        return String.format(QUOTED_STRING, str);
    }

    public void parseObjectFromValidatedInstance(AbstractStructureInstance instance) throws JsonProcessingException {
        type = instance.getName();
        module = instance.getDataDomNode().getModuleName();
        for (AbstractStructureInstance structureInstance : instance.getStructureChildren()) {
            if ("attributes".equals(structureInstance.getName())) {
                try {
                    parseAttributes(structureInstance);
                } catch (SchemaRegistryException e) {
                    log.error("Error parsing entity attributes for module {} and entity type name {}", module, type, e);
                }
            }
        }
        for (AbstractContentInstance contentInstance : instance.getContentChildren()) {
            if ("id".equals(contentInstance.getName())) {
                id = contentInstance.getValue().toString();
            }
        }
        instance.getContentChildren().get(0).getParent().getDataDomNode().getChildren().stream().filter(child -> "sourceIds"
                .equals(child.getName())).forEach(domNode -> addSourceIds(domNode.getStringValue()));
    }

    private void parseAttributes(AbstractStructureInstance structureInstance) throws JsonProcessingException,
            SchemaRegistryException {
        attributes.putAll(getMapFromContentChildren(structureInstance.getContentChildren()));
        EntityType entityTypeByModuleAndName = SchemaRegistry.getEntityTypeByModuleAndName(module, type);
        for (String attributeName : entityTypeByModuleAndName.getAttributeNames()) {
            if (!attributes.containsKey(attributeName) && Objects.nonNull(structureInstance.getDataDomNode())) {
                structureInstance.getDataDomNode().getChildren().stream().filter(attribute -> attribute.getName().equals(
                        attributeName) && Objects.isNull(attribute.getValue()) && attribute.getChildren().isEmpty())
                        .forEach(attribute -> attributes.put(attributeName, attribute.getValue()));
            }
        }
        for (AbstractStructureInstance complexAttribute : structureInstance.getStructureChildren()) {
            if (complexAttribute.getDataDomNode() != null && !complexAttribute.getDataDomNode().getChildren().isEmpty()) {
                attributes.put(complexAttribute.getName(), parseComplexAttributeIntoJsonString(complexAttribute));
            }
        }
        for (NamespaceModuleIdentifier emptyListIdentifier : structureInstance.getEmptyChildLeafListIdentifiers()) {
            attributes.put(emptyListIdentifier.getIdentifier(), "[]");
        }
    }

    private static String parseComplexAttributeIntoJsonString(AbstractStructureInstance complexAttribute)
            throws JsonProcessingException {
        Map<String, Object> map = getMapFromStructureInstance(complexAttribute);
        return objectWriter.writeValueAsString(map);
    }

    private static Map<String, Object> getMapFromStructureInstance(AbstractStructureInstance structureInstance)
            throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.putAll(getMapFromContentChildren(structureInstance.getContentChildren()));
        for (AbstractStructureInstance structureChild : structureInstance.getStructureChildren()) {
            map.put(structureChild.getName(), getMapFromStructureInstance(structureChild));
        }
        return map;
    }

    private static Map<String, Object> getMapFromContentChildren(List<AbstractContentInstance> contentChildren)
            throws JsonProcessingException {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, List<Object>> attributesWithTypeOfArray = new HashMap<>();
        for (AbstractContentInstance contentInstance : contentChildren) {
            if (isContentInstancePartOfArray(contentInstance)) {
                final String key = contentInstance.getName();
                attributesWithTypeOfArray.computeIfAbsent(key, k -> new ArrayList<>());
                attributesWithTypeOfArray.get(key).add(contentInstance.getValue());
            } else {
                returnMap.put(contentInstance.getName(), getAttributeValueAsJavaType(contentInstance));
            }
        }
        for (var entry : attributesWithTypeOfArray.entrySet()) {
            returnMap.put(entry.getKey(), objectWriter.writeValueAsString(entry.getValue()));
        }
        return returnMap;
    }

    private static boolean isContentInstancePartOfArray(AbstractContentInstance contentInstance) {
        return "leaf-list".equals(contentInstance.getSchemaNode().getDomElement().getName());
    }

    private static Object getAttributeValueAsJavaType(AbstractContentInstance contentInstance) {
        final String typeFromYangSchema = contentInstance.getSchemaNode().getChild(new StatementModuleAndName("YANG CORE",
                "type")).getDomElement().getValue();
        return switch (typeFromYangSchema) {
            case "int64" -> Long.valueOf(contentInstance.getValue().toString());
            case "uint64" -> Long.parseUnsignedLong(contentInstance.getValue().toString());
            case "decimal64" -> new BigDecimal(contentInstance.getValue().toString());
            default -> contentInstance.getValue();
        };
    }

    private static void validateEntityIdentifierCharacters(Entity entity) throws IllegalCharacterException {
        if (hasInvalidCharacter(entity.getId())) {
            throw IllegalCharacterException.inEntity("id", entity.getId());
        }
    }
}
