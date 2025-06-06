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
package org.oran.smo.teiv.pgsqlgenerator;

import static org.oran.smo.teiv.pgsqlgenerator.Constants.A_SIDE;
import static org.oran.smo.teiv.pgsqlgenerator.Constants.BIGINT;
import static org.oran.smo.teiv.pgsqlgenerator.Constants.B_SIDE;
import static org.oran.smo.teiv.pgsqlgenerator.Constants.DECIMAL;
import static org.oran.smo.teiv.pgsqlgenerator.Constants.INT;
import static org.oran.smo.teiv.pgsqlgenerator.Constants.JSONB;
import static org.oran.smo.teiv.pgsqlgenerator.Constants.RELATION;
import static org.oran.smo.teiv.pgsqlgenerator.Constants.TEXT;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.oran.smo.yangtools.parser.model.ModuleIdentity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.oran.smo.yangtools.parser.ParserExecutionContext;
import org.oran.smo.yangtools.parser.YangDeviceModel;
import org.oran.smo.yangtools.parser.findings.FindingsManager;
import org.oran.smo.yangtools.parser.findings.ModifyableFindingSeverityCalculator;
import org.oran.smo.yangtools.parser.findings.ModuleAndFindingTypeAndSchemaNodePathFilterPredicate;
import org.oran.smo.yangtools.parser.input.FileBasedYangInputResolver;
import org.oran.smo.yangtools.parser.model.ConformanceType;
import org.oran.smo.yangtools.parser.model.YangModel;
import org.oran.smo.yangtools.parser.model.statements.StatementModuleAndName;
import org.oran.smo.yangtools.parser.model.statements.ietf.IetfExtensionsClassSupplier;
import org.oran.smo.yangtools.parser.model.statements.oran.OranExtensionsClassSupplier;
import org.oran.smo.yangtools.parser.model.statements.oran.YOranSmoTeivASide;
import org.oran.smo.yangtools.parser.model.statements.oran.YOranSmoTeivBSide;
import org.oran.smo.yangtools.parser.model.statements.oran.YOranSmoTeivBiDirectionalTopologyRelationship;
import org.oran.smo.yangtools.parser.model.statements.threegpp.ThreeGppExtensionsClassSupplier;
import org.oran.smo.yangtools.parser.model.statements.yang.YModule;
import org.oran.smo.yangtools.parser.model.yangdom.YangDomElement;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class YangModelProcessor {
    private final HashMap<String, String> dataTypeMapping;
    @Value("${exclusions.model-names}")
    private List<String> excludedModelNames;
    private final YangDeviceModel yangDeviceModel;
    private final ModifyableFindingSeverityCalculator severityCalculator;
    private final FindingsManager findingsManager;
    private final ParserExecutionContext context;
    private final ThreeGppExtensionsClassSupplier threeGppStatementFactory;
    private final IetfExtensionsClassSupplier ietfStatementFactory;
    private final OranExtensionsClassSupplier oranStatementFactory;

    public YangModelProcessor() {
        dataTypeMapping = createDataTypeMapping();
        yangDeviceModel = new YangDeviceModel("Yang Parser JAR Test Device Model");
        severityCalculator = new ModifyableFindingSeverityCalculator();
        findingsManager = new FindingsManager(severityCalculator);
        findingsManager.addFilterPredicate(ModuleAndFindingTypeAndSchemaNodePathFilterPredicate.fromString(
                "ietf*,iana*;*;*"));

        threeGppStatementFactory = new ThreeGppExtensionsClassSupplier();
        ietfStatementFactory = new IetfExtensionsClassSupplier();
        oranStatementFactory = new OranExtensionsClassSupplier();
        context = new ParserExecutionContext(findingsManager, Arrays.asList(threeGppStatementFactory, oranStatementFactory,
                ietfStatementFactory));
        context.setFailFast(false);
        context.setSuppressFindingsOnUnusedSchemaNodes(true);
    }

    private HashMap<String, String> createDataTypeMapping() {
        HashMap<String, String> map = new HashMap<>();
        map.put("string", TEXT);
        map.put("uint32", BIGINT);
        map.put("int32", INT);
        map.put("or-teiv-types:_3GPP_FDN_Type", TEXT);
        map.put("enumeration", TEXT);
        map.put("types3gpp:PLMNId", JSONB);
        map.put("[]", JSONB);
        map.put(JSONB, JSONB);
        map.put("[uses types3gpp:PLMNId]", JSONB);
        map.put("geo:geo-location", "geography");
        map.put("uint64", BIGINT);
        map.put("int64", BIGINT);
        map.put("decimal64", DECIMAL);
        map.put("[uses or-teiv-types:CM_ID]", JSONB);
        return map;
    }

    public List<Entity> getEntitiesAndAttributesFromYang(List<File> pathToImplementing) {
        List<Entity> entities = new ArrayList<>();

        File rootFolder = pathToImplementing.get(0);
        final FileBasedYangInputResolver resolver = new FileBasedYangInputResolver(List.of(rootFolder));
        List<YangModel> yangModels = resolver.getResolvedYangInput().stream().map(yangInput -> new YangModel(yangInput,
                ConformanceType.IMPLEMENT)).toList();

        yangDeviceModel.parseIntoYangModels(context, yangModels);

        yangModels.stream().forEach(yangModel -> {
            YModule yModule = yangModel.getYangModelRoot().getModule();
            StringBuilder modelParserLog = new StringBuilder();
            modelParserLog.append("Module Name: ").append(yModule.getModuleName());

            yModule.getLists().stream().forEach(yList -> {
                final String tableName = getTableName(yModule.getModuleName(), yList.getListName());
                modelParserLog.append(String.format("%n\tEntity Name: %s %n", yList.getListName()));
                List<Attribute> attributes = new ArrayList<>();
                List<Object> constraint = List.of(PrimaryKeyConstraint.builder().constraintName("PK_" + yList
                        .getListName() + "_id").tableName(yList.getListName()).columnToAddConstraintTo("id").build());

                attributes.add(Attribute.builder().name("id").yangDataType("string").dataType(TEXT).constraints(constraint)
                        .build());
                yList.getContainers().forEach(yContainer -> {
                    modelParserLog.append(String.format("\t\tContainer Name: %s %n", yContainer.getContainerName()));
                    if (yContainer.getContainerName().equals("attributes")) {
                        String leafNameFormat = "\t\t\tLeaf Name: %s %n";
                        String leafTypeFormat = "\t\t\t\tLeaf Type: %s %n";
                        String dataTypeFormat = "\t\t\t\tData Type: %s %n";
                        yContainer.getLeafs().forEach(yLeaf -> {

                            modelParserLog.append(String.format(leafNameFormat, yLeaf.getLeafName()));
                            modelParserLog.append(String.format(leafTypeFormat, yLeaf.getType().getDataType()));
                            modelParserLog.append(String.format(dataTypeFormat, dataTypeMapping.get(yLeaf.getType()
                                    .getDataType())));

                            if (yLeaf.getDefault() != null) {

                                attributes.add(Attribute.builder().name(yLeaf.getLeafName()).yangDataType(yLeaf.getType()
                                        .getDataType()).dataType(dataTypeMapping.get(yLeaf.getType().getDataType()))
                                        .defaultValue(yLeaf.getDefault().getValue()).constraints(new ArrayList<>())
                                        .build());
                            } else {
                                attributes.add(Attribute.builder().name(yLeaf.getLeafName()).yangDataType(yLeaf.getType()
                                        .getDataType()).dataType(dataTypeMapping.get(yLeaf.getType().getDataType()))
                                        .constraints(new ArrayList<>()).build());
                            }
                        });
                        yContainer.getLeafLists().forEach(yLeafList -> {

                            modelParserLog.append(String.format(leafNameFormat, yLeafList.getLeafListName()));
                            modelParserLog.append(String.format(leafTypeFormat, yLeafList.getType().getDataType()));
                            modelParserLog.append(String.format(dataTypeFormat, dataTypeMapping.get(yLeafList.getType()
                                    .getDataType())));

                            attributes.add(Attribute.builder().name(yLeafList.getLeafListName()).yangDataType(yLeafList
                                    .getType().getDataType()).dataType(JSONB).indexType(
                                            IndexType.GIN_TRGM_OPS_ON_LIST_AS_JSONB).constraints(new ArrayList<>())
                                    .build());
                        });
                        yContainer.getLists().forEach(yListAttr -> {

                            modelParserLog.append(String.format(leafNameFormat, yListAttr.getListName()));
                            modelParserLog.append(String.format(leafTypeFormat, yListAttr.getUses()));
                            modelParserLog.append(String.format(dataTypeFormat, dataTypeMapping.get(yListAttr.getUses()
                                    .toString())));

                            attributes.add(Attribute.builder().name(yListAttr.getListName()).yangDataType(yListAttr
                                    .getUses().toString()).dataType(JSONB).indexType(
                                            IndexType.GIN_TRGM_OPS_ON_LIST_AS_JSONB).constraints(new ArrayList<>())
                                    .build());
                        });
                        yContainer.getContainers().forEach(container -> {

                            modelParserLog.append(String.format("\t\t\tContainer Name: %s %n", container
                                    .getContainerName()));
                            modelParserLog.append(String.format("\t\t\t\tContainer Type: %s %n", container.getUses()));
                            modelParserLog.append(String.format(dataTypeFormat, dataTypeMapping.get(container.getUses()
                                    .toString())));

                            String dataType = dataTypeMapping.get(container.getUses().toString());
                            if (container.getContainerName().equals("geo-location")) {
                                dataType = dataTypeMapping.get("geo:geo-location");
                            }
                            Attribute.AttributeBuilder attributeBuilder = Attribute.builder().name(container
                                    .getContainerName()).yangDataType("<< Refer to Module >>").dataType(dataType)
                                    .constraints(new ArrayList<>());
                            if (dataType.equals(JSONB)) {
                                attributeBuilder.indexType(IndexType.GIN);
                            }
                            attributes.add(attributeBuilder.build());
                        });
                        yContainer.getUses().forEach(uses -> {

                            modelParserLog.append(String.format("\t\t\tUses Name: %s %n", uses.getDomElement().getValue()));

                            attributes.add(Attribute.builder().name(uses.getDomElement().getValue().substring(uses
                                    .getDomElement().getValue().indexOf(':') + 1, uses.getDomElement().getValue().length()))
                                    .yangDataType(uses.getDomElement().getValue()).dataType(dataTypeMapping.get(uses
                                            .getDomElement().getValue())).constraints(new ArrayList<>()).build());
                        });
                    }
                });
                attributes.sort(Comparator.comparing(Attribute::getName));
                entities.add(Entity.builder().entityName(yList.getListName()).storedAt(tableName).moduleReferenceName(
                        yangModel.getYangModelRoot().getModule().getModuleName()).attributes(attributes).build());
            });
            log.info(modelParserLog.toString());
        });
        entities.sort(Comparator.comparing(Entity::getStoredAt));
        return entities;
    }

    public List<Relationship> getRelationshipsFromYang(List<File> pathToImplementing) {
        List<Relationship> relationships = new ArrayList<>();

        File rootFolder = pathToImplementing.get(0);
        final FileBasedYangInputResolver resolver = new FileBasedYangInputResolver(List.of(rootFolder));
        List<YangModel> yangModels = resolver.getResolvedYangInput().stream().map(yangInput -> new YangModel(yangInput,
                ConformanceType.IMPLEMENT)).toList();

        yangDeviceModel.parseIntoYangModels(context, yangModels);

        yangModels.stream().forEach(yangModel -> {
            YModule yModule = yangModel.getYangModelRoot().getModule();
            StringBuilder modelParserLog = new StringBuilder();
            modelParserLog.append(String.format("Module Name: %s ", yModule.getModuleName()));

            String oranSmoTeivCommonYangExt = "o-ran-smo-teiv-common-yang-extensions";

            StatementModuleAndName biDirectionalTopologyRelationship = new StatementModuleAndName(oranSmoTeivCommonYangExt,
                    "biDirectionalTopologyRelationship");
            StatementModuleAndName biDirectionalTopologyRelationshipAside = new StatementModuleAndName(
                    oranSmoTeivCommonYangExt, "aSide");
            StatementModuleAndName biDirectionalTopologyRelationshipBside = new StatementModuleAndName(
                    oranSmoTeivCommonYangExt, "bSide");

            yModule.getChildren(biDirectionalTopologyRelationship).stream().map(
                    YOranSmoTeivBiDirectionalTopologyRelationship.class::cast).forEach(
                            yOranSmoTeivBiDirectionalTopologyRelationship -> {
                                modelParserLog.append(String.format("\tRelationship Name: %s %n",
                                        yOranSmoTeivBiDirectionalTopologyRelationship.getRelationshipName()));
                                YOranSmoTeivASide aSide = yOranSmoTeivBiDirectionalTopologyRelationship.getChildStatements()
                                        .stream().filter(abstractStatement -> abstractStatement.getChild(
                                                biDirectionalTopologyRelationshipAside) != null).toList().get(0).getChild(
                                                        biDirectionalTopologyRelationshipAside);

                                YOranSmoTeivBSide bSide = yOranSmoTeivBiDirectionalTopologyRelationship.getChildStatements()
                                        .stream().filter(abstractStatement -> abstractStatement.getChild(
                                                biDirectionalTopologyRelationshipBside) != null).toList().get(0).getChild(
                                                        biDirectionalTopologyRelationshipBside);

                                modelParserLog.append(String.format("\t\tA Side:%n\t\t\t Name: %s %n\t\t\t Type: %s %n",
                                        aSide.getParentStatement().getStatementIdentifier(), aSide.getTeivTypeName()));
                                modelParserLog.append(String.format("\t\tB Side:%n\t\t\t Name %s %n\t\t\t Type %s %n", bSide
                                        .getParentStatement().getStatementIdentifier(), bSide.getTeivTypeName()));

                                long aSideMinCardinality = 0;
                                long aSideMaxCardinality = 0;
                                long bSideMinCardinality = 0;
                                long bSideMaxCardinality = 0;
                                Optional<YangDomElement> bSideMandatory = yOranSmoTeivBiDirectionalTopologyRelationship
                                        .getChildStatements().stream().filter(abstractStatement -> abstractStatement
                                                .getChild(biDirectionalTopologyRelationshipBside) != null).toList().get(0)
                                        .getDomElement().getChildren().stream().filter(name -> name.getNameValue().contains(
                                                "mandatory true")).findAny();
                                Optional<YangDomElement> aSideMandatory = yOranSmoTeivBiDirectionalTopologyRelationship
                                        .getChildStatements().stream().filter(abstractStatement -> abstractStatement
                                                .getChild(biDirectionalTopologyRelationshipAside) != null).toList().get(0)
                                        .getDomElement().getChildren().stream().filter(name -> name.getNameValue().contains(
                                                "mandatory true")).findAny();
                                Optional<YangDomElement> bSideMinElement = yOranSmoTeivBiDirectionalTopologyRelationship
                                        .getChildStatements().stream().filter(abstractStatement -> abstractStatement
                                                .getChild(biDirectionalTopologyRelationshipBside) != null).toList().get(0)
                                        .getDomElement().getChildren().stream().filter(name -> name.getNameValue().contains(
                                                "min-elements 1")).findAny();
                                Optional<YangDomElement> aSideMinElement = yOranSmoTeivBiDirectionalTopologyRelationship
                                        .getChildStatements().stream().filter(abstractStatement -> abstractStatement
                                                .getChild(biDirectionalTopologyRelationshipAside) != null).toList().get(0)
                                        .getDomElement().getChildren().stream().filter(name -> name.getNameValue().contains(
                                                "min-elements 1")).findAny();

                                if (aSide.getParentStatement().getDomElement().getName().equals("leaf")) {
                                    if (aSideMandatory.isPresent() || aSideMinElement.isPresent()) {
                                        bSideMinCardinality = 1;
                                    } else {
                                        bSideMinCardinality = 0;
                                    }
                                    bSideMaxCardinality = 1;

                                }
                                if (aSide.getParentStatement().getDomElement().getName().equals("leaf-list")) {
                                    if (aSideMandatory.isPresent() || aSideMinElement.isPresent()) {
                                        bSideMinCardinality = 1;
                                    } else {
                                        bSideMinCardinality = 0;
                                    }
                                    bSideMaxCardinality = Long.MAX_VALUE;

                                }
                                if (bSide.getParentStatement().getDomElement().getName().equals("leaf")) {
                                    if (bSideMandatory.isPresent() || bSideMinElement.isPresent()) {
                                        aSideMinCardinality = 1;
                                    } else {
                                        aSideMinCardinality = 0;
                                    }
                                    aSideMaxCardinality = 1;
                                }
                                if (bSide.getParentStatement().getDomElement().getName().equals("leaf-list")) {
                                    if (bSideMandatory.isPresent() || bSideMinElement.isPresent()) {
                                        aSideMinCardinality = 1;
                                    } else {
                                        aSideMinCardinality = 0;
                                    }
                                    aSideMaxCardinality = Long.MAX_VALUE;
                                }

                                String relDataLocation = getRelationshipDataLocation(aSideMaxCardinality,
                                        bSideMaxCardinality, aSide.getValue(), bSide.getValue(),
                                        yOranSmoTeivBiDirectionalTopologyRelationship.getRelationshipName());

                                String aSideMoType = aSide.getValue().substring(aSide.getValue().indexOf(':') + 1, aSide
                                        .getValue().length());
                                String bSideMoType = bSide.getValue().substring(bSide.getValue().indexOf(':') + 1, bSide
                                        .getValue().length());

                                boolean connectSameEntity;
                                if (aSideMoType.equals(bSideMoType)) {
                                    connectSameEntity = true;
                                } else {
                                    connectSameEntity = false;
                                }

                                String aSidePrefix = getPrefix(aSide.getValue());
                                String aSideModuleName = getSideModuleName(yangModels, yangModel, yModule, aSideMoType,
                                        aSidePrefix);

                                String bSidePrefix = getPrefix(bSide.getValue());
                                String bSideModuleName = getSideModuleName(yangModels, yangModel, yModule, bSideMoType,
                                        bSidePrefix);

                                String storedAt = "";
                                switch (relDataLocation) {
                                    case A_SIDE -> storedAt = getTableName(aSideModuleName, aSideMoType);
                                    case B_SIDE -> storedAt = getTableName(bSideModuleName, bSideMoType);
                                    case RELATION -> storedAt = getTableName(yModule.getDomElement().getValue(),
                                            yOranSmoTeivBiDirectionalTopologyRelationship.getRelationshipName());
                                }

                                Relationship relationship = Relationship.builder().name(
                                        yOranSmoTeivBiDirectionalTopologyRelationship.getRelationshipName())
                                        .aSideAssociationName(aSide.getParentStatement().getStatementIdentifier())
                                        .aSideMOType(aSideMoType).aSideModule(aSideModuleName).aSideMinCardinality(
                                                aSideMinCardinality).aSideMaxCardinality(aSideMaxCardinality)
                                        .bSideAssociationName(bSide.getParentStatement().getStatementIdentifier())
                                        .bSideMOType(bSideMoType).bSideModule(bSideModuleName).bSideMinCardinality(
                                                bSideMinCardinality).bSideMaxCardinality(bSideMaxCardinality)
                                        .relationshipDataLocation(relDataLocation).moduleReferenceName(yModule
                                                .getModuleName()).associationKind(("BI_DIRECTIONAL")).connectSameEntity(
                                                        connectSameEntity).storedAt(storedAt).aSideStoredAt(getTableName(
                                                                aSideModuleName, aSideMoType)).bSideStoredAt(getTableName(
                                                                        bSideModuleName, bSideMoType)).build();     // Hard coded for now
                                relationships.add(relationship);
                            });
            log.info(modelParserLog.toString());
        });
        relationships.sort(Comparator.comparing(Relationship::getName));
        return relationships;
    }

    private String getSideModuleName(List<YangModel> yangModels, String aSideMoType) {
        return yangModels.stream().flatMap(ym -> ym.getYangDomDocumentRoot().getChildren().stream()).flatMap(
                children -> children.getChildren().stream()).filter(child -> child.getValue().equals(aSideMoType)).map(
                        child -> child.getParentElement().getValue()).findFirst().orElseThrow(
                                () -> new NoSuchElementException("No module name found for type: " + aSideMoType));
    }

    private String getSideModuleName(List<YangModel> yangModels, YangModel yangModel, YModule yModule, String sideMoType,
            String sidePrefix) {
        if (sidePrefix.isEmpty()) {
            return yModule.getModuleName();
        } else {
            ModuleIdentity moduleIdentity = yangModel.getPrefixResolver().getModuleForPrefix(sidePrefix);
            if (moduleIdentity == null) {
                return getSideModuleName(yangModels, sideMoType);
            }
            return moduleIdentity.getModuleName();
        }
    }

    private String getPrefix(String side) {
        String[] parts = side.split(":", 2);
        return parts.length > 1 ? parts[0] : "";
    }

    private String getTableName(String moduleName, String name) {
        return moduleName + "_" + name;
    }

    /**
     * Identify the type of table in which relationship should be stored.
     */
    private String getRelationshipDataLocation(long aSideMaxCardinality, long bSideMaxCardinality, String aSideMO,
            String bSideMO, String relName) {
        if (aSideMO.equals(bSideMO) || (aSideMaxCardinality > 1 && bSideMaxCardinality > 1)) {
            return RELATION;
        } else if ((aSideMaxCardinality == 1 && bSideMaxCardinality == 1) || (aSideMaxCardinality > 1 && bSideMaxCardinality == 1)) {
            return A_SIDE;
        } else if (aSideMaxCardinality == 1 && bSideMaxCardinality > 1) {
            return B_SIDE;
        } else {
            throw PgSchemaGeneratorException.assignRelationshipDataLocation(relName, new UnsupportedOperationException());
        }
    }
}
