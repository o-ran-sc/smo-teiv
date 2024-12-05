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
package org.oran.smo.teiv.pgsqlgenerator.graphgenerator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import org.oran.smo.teiv.pgsqlgenerator.Attribute;
import org.oran.smo.teiv.pgsqlgenerator.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EntityGraphGeneratorUml {

    @Value("${graphs.generate}")
    private boolean generateEntityGraph;

    @Value("${graphs.output}")
    private String graphOutput;

    @Autowired
    private final HelperFunctions helperFunctions = new HelperFunctions();

    public void generate(List<Entity> entities) throws IOException {
        if (generateEntityGraph) {
            List<String> moduleNames = entities.stream().map(Entity::getModuleReferenceName).distinct().toList();
            for (String moduleName : moduleNames) {
                List<Entity> moduleEntities = entities.stream().filter(entity -> moduleName.equals(entity
                        .getModuleReferenceName())).toList();
                generateGraph(moduleName, moduleEntities);
            }
        } else {
            log.info("graphs.generate set to false");
        }
    }

    private void generateGraph(String name, List<Entity> entities) throws IOException {
        String plantUmlSource = prepareGraphSource(name, entities);
        File outputFile = new File(graphOutput, name + ".puml");
        try (PrintWriter writer = new PrintWriter(outputFile)) {
            writer.write(plantUmlSource);
        }
        log.info("PUML generated at: {}", outputFile.getAbsolutePath());
    }

    private String prepareGraphSource(String moduleName, List<Entity> entities) {
        StringBuilder sb = new StringBuilder();
        sb.append("@startuml\n");
        sb.append("skinparam class {\n");
        sb.append("    BackgroundColor<<Entity>> ").append(helperFunctions.getNodeFillColour(moduleName)).append("\n");
        sb.append("    BackgroundColor<<Module>> LightBlue\n");
        sb.append("}\n");
        sb.append(String.format("class %s <<Module>> {%n}%n", moduleName));

        for (Entity entity : entities) {
            sb.append(String.format("class %s <<Entity>> {%n", entity.getEntityName()));
            List<Attribute> attributes = entity.getAttributes();
            Optional<Attribute> optionalId = attributes.stream().filter(att -> "id".equals(att.getName())).findFirst();
            if (optionalId.isPresent()) {
                Attribute id = optionalId.get();
                sb.append(String.format("    %s : %s%n", id.getName(), id.getYangDataType()));
            }
            sb.append("    sourceIds : << Refer to Module >>\n");
            sb.append("    attributes:\n");
            for (Attribute attribute : attributes) {
                if (!"id".equals(attribute.getName())) {
                    sb.append(String.format("        %s : %s%n", attribute.getName(), attribute.getYangDataType()));
                }
            }
            sb.append("}\n");
            sb.append(String.format("\"%s\" --> %s\n", moduleName, entity.getEntityName()));
        }
        sb.append("@enduml\n");
        return sb.toString();
    }
}
