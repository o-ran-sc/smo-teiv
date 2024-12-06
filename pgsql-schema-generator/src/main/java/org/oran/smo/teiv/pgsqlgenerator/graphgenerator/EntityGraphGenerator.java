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

import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import lombok.extern.slf4j.Slf4j;
import org.oran.smo.teiv.pgsqlgenerator.Attribute;
import org.oran.smo.teiv.pgsqlgenerator.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static guru.nidi.graphviz.attribute.Rank.RankDir;

@Component
@Slf4j
public class EntityGraphGenerator {

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
        MutableGraph g = prepareGraph(entities, name);
        File outputFile = new File(graphOutput, name);
        Graphviz.fromGraph(g).render(Format.SVG).toFile(outputFile);
        Graphviz.fromGraph(g).render(Format.DOT).toFile(outputFile);
        log.info("Graph rendered to: {}", outputFile.getAbsolutePath());
    }

    private MutableGraph prepareGraph(List<Entity> moduleEntities, String moduleName) {
        Color fillColour = Color.rgba(helperFunctions.getNodeFillColour(moduleName)).fill();
        MutableGraph g = Factory.mutGraph(moduleName).setDirected(true).graphAttrs().add(Rank.dir(RankDir.LEFT_TO_RIGHT))
                .nodeAttrs().add(Shape.RECT, Style.BOLD, Color.BLACK, Style.FILLED, fillColour, Font.name("Arial"));
        MutableNode moduleNameNode = Factory.mutNode(moduleName).attrs().add(Color.LIGHTBLUE.fill());
        g.add(moduleNameNode);
        for (Entity moduleEntity : moduleEntities) {
            MutableNode moduleNode = Factory.mutNode(moduleEntity.getEntityName());
            g.add(moduleNode);
            g.add(moduleNameNode.addLink(Factory.to(moduleNode)));
            List<Attribute> attributes = moduleEntity.getAttributes();
            if (!attributes.isEmpty()) {
                addAttributeNodeToGraph(g, attributes, moduleEntity, moduleNode);
            }
        }
        return g;
    }

    private void addAttributeNodeToGraph(MutableGraph graph, List<Attribute> attributes, Entity moduleEntity,
            MutableNode moduleNode) {
        String label = "<TABLE border='0' cellborder='0' cellspacing='0' cellpadding='0'>";
        Optional<Attribute> optionalId = attributes.stream().filter(att -> "id".equals(att.getName())).findFirst();
        if (optionalId.isPresent()) {
            Attribute id = optionalId.get();
            label = label.concat("<TR> <TD align='left'>" + id.getName() + "</TD> <TD align='right'>" + helperFunctions
                    .escapeHtml(id.getYangDataType()) + "</TD> </TR>");
        }

        label += "<TR> <TD align='left'>sourceIds</TD> <TD align='right' >&lt;&lt; Refer to Module &gt;&gt;</TD> </TR>";
        label = label.concat("<TR> <TD colspan='2' align='left'> attributes: </TD> </TR>");
        for (Attribute attribute : attributes) {
            if (!"id".equals(attribute.getName())) {
                label = label.concat("<TR> <TD align='left'>        " + attribute
                        .getName() + "</TD> <TD align='right'>" + helperFunctions.escapeHtml(attribute
                                .getYangDataType()) + "</TD> </TR>");
            }
        }
        label = label.concat("</TABLE>");
        MutableNode attributeNode = Factory.mutNode(moduleEntity.getEntityName() + "-attributes").attrs().add(Label.html(
                label));
        graph.add(attributeNode);
        graph.add(moduleNode.addLink(attributeNode));
    }
}
