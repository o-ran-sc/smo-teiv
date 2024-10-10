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

import org.oran.smo.teiv.pgsqlgenerator.Entity;
import org.oran.smo.teiv.pgsqlgenerator.Relationship;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RelationshipGraphGeneratorUml {

    @Value("${graphs.generate}")
    private boolean generateRelationshipGraph;

    @Value("${graphs.relationship-entities-bg-colour}")
    private boolean bgColour;

    @Value("${graphs.output}")
    private String graphOutput;

    public void generate(List<Relationship> relationships, List<Entity> entities) throws IOException {
        if (generateRelationshipGraph) {
            List<String> moduleNames = relationships.stream().map(Relationship::getModuleReferenceName).distinct().toList();
            for (String moduleName : moduleNames) {
                List<Relationship> moduleRelationships = relationships.stream().filter(relationship -> moduleName.equals(
                        relationship.getModuleReferenceName())).toList();
                List<Entity> moduleEntities = entities.stream().filter(entity -> moduleName.equals(entity
                        .getModuleReferenceName())).toList();
                generateGraph(moduleName, moduleRelationships, moduleEntities);
            }
            generateGraph("overall", relationships, entities);
        } else {
            log.info("graphs.generate set to false");
        }
    }

    private void generateGraph(String name, List<Relationship> relationships, List<Entity> entities) throws IOException {
        String plantUmlSource = prepareGraph(relationships, entities);
        File outputFile = new File(graphOutput, name + "-rel.puml");
        try (PrintWriter writer = new PrintWriter(outputFile)) {
            writer.write(plantUmlSource);
        }
        log.info("PUML generated at: {}", outputFile.getAbsolutePath());
    }

    private String prepareGraph(List<Relationship> relationships, List<Entity> entities) {
        StringBuilder sb = new StringBuilder();
        sb.append("@startuml\n");
        sb.append("skinparam componentStyle rectangle\n");
        for (Entity entity : entities) {
            sb.append(String.format("class %s %s {\n", entity.getEntityName(), getNodeFillColour(entity
                    .getModuleReferenceName())));
            sb.append("}\n");
        }
        for (Relationship relationship : relationships) {
            String label = relationship.getName().split("_")[1];
            String aSideCardinality = getCardinality(relationship.getASideMinCardinality(), relationship
                    .getASideMaxCardinality());
            String bSideCardinality = getCardinality(relationship.getBSideMinCardinality(), relationship
                    .getBSideMaxCardinality());

            sb.append(String.format("%s \"%s\" --> \"%s\" %s : %s\n", relationship.getASideMOType(), aSideCardinality,
                    bSideCardinality, relationship.getBSideMOType(), label));
        }
        sb.append("@enduml\n");
        return sb.toString();
    }

    private String getNodeFillColour(String input) {
        if (bgColour) {
            int hash = input.hashCode();

            int r = (hash >> 16) & 0xFF;
            int g = (hash >> 8) & 0xFF;
            int b = hash & 0xFF;

            return String.format("#%02X%02X%02X%02X", r, g, b, 65);
        }
        return "#FFFFFF";
    }

    private String getCardinality(long minCardinality, long maxCardinality) {
        return formatCardinality(minCardinality) + ".." + formatCardinality(maxCardinality);
    }

    private String formatCardinality(long cardinality) {
        return cardinality == Long.MAX_VALUE ? "*" : String.valueOf(cardinality);
    }
}
