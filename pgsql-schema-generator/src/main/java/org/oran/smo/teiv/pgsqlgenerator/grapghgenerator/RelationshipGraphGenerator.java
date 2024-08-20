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
package org.oran.smo.teiv.pgsqlgenerator.grapghgenerator;

import guru.nidi.graphviz.attribute.Arrow;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.EndLabel;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import lombok.extern.slf4j.Slf4j;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import org.oran.smo.teiv.pgsqlgenerator.Entity;
import org.oran.smo.teiv.pgsqlgenerator.Relationship;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class RelationshipGraphGenerator {

    @Value("${graphs.generate}")
    private boolean generateRelationshipGraph;

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
        MutableGraph g = prepareGraph(relationships, entities);
        File outputFile = new File(graphOutput, name + "-rel");
        Graphviz.fromGraph(g).render(Format.SVG).toFile(outputFile);
        Graphviz.fromGraph(g).render(Format.DOT).toFile(outputFile);
        log.info("Graph rendered to: {}", outputFile.getAbsolutePath());
    }

    private MutableGraph prepareGraph(List<Relationship> moduleRelationships, List<Entity> moduleEntities) {
        MutableGraph g = Factory.mutGraph("moduleName").setDirected(true).linkAttrs().add(Color.DARKSLATEGRAY4).nodeAttrs()
                .add(Shape.BOX);
        for (Entity moduleEntity : moduleEntities) {
            MutableNode node = Factory.mutNode(moduleEntity.getEntityName());
            g.add(node);
        }
        for (Relationship moduleRelationship : moduleRelationships) {
            MutableNode nodeA = Factory.mutNode(moduleRelationship.getASideMOType());
            g.add(nodeA);
            MutableNode nodeB = Factory.mutNode(moduleRelationship.getBSideMOType());
            g.add(nodeB);

            String label = moduleRelationship.getName().split("_")[1];
            Label aSideCardinality = Label.of(getCardinality(moduleRelationship.getASideMinCardinality(), moduleRelationship
                    .getASideMaxCardinality()));
            Label bSideCardinality = Label.of(getCardinality(moduleRelationship.getBSideMinCardinality(), moduleRelationship
                    .getBSideMaxCardinality()));

            g.add(nodeA.addLink(Factory.to(nodeB).with(Label.of(label), EndLabel.head(aSideCardinality, null, null),
                    EndLabel.tail(bSideCardinality, null, null), Arrow.VEE)));
        }
        return g;
    }

    private String getCardinality(long minCardinality, long maxCardinality) {
        return formatCardinality(minCardinality) + ".." + formatCardinality(maxCardinality);
    }

    private String formatCardinality(long cardinality) {
        if (cardinality == Long.MAX_VALUE) {
            return "*";
        } else {
            return String.valueOf(cardinality);
        }
    }
}
