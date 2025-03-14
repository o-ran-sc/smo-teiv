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
package org.oran.smo.teiv.exposure.teivpath.resolver;

import org.oran.smo.teiv.antlr4.teivPathBaseListener;
import org.oran.smo.teiv.antlr4.teivPathParser;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.AndLogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.AndOrLogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.ContainerType;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.LogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.OrLogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.QueryFunction;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.ScopeLogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.ScopeObject;
import org.oran.smo.teiv.utils.query.exception.TeivPathException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.oran.smo.teiv.exposure.teivpath.resolver.ResolverUtil.getContainerType;
import static org.oran.smo.teiv.exposure.teivpath.resolver.ResolverUtil.getTopologyObjectForScope;
import static org.oran.smo.teiv.exposure.teivpath.resolver.ResolverUtil.isComplexAttribute;
import static org.oran.smo.teiv.utils.TeivConstants.ATTRIBUTES;
import static org.oran.smo.teiv.utils.TeivConstants.CLASSIFIERS;
import static org.oran.smo.teiv.utils.TeivConstants.DECORATORS;
import static org.oran.smo.teiv.utils.TeivConstants.METADATA;
import static org.oran.smo.teiv.utils.TeivConstants.SOURCE_IDS;
import static org.oran.smo.teiv.utils.TeivConstants.WILDCARD;

public class ScopeFilterListener extends teivPathBaseListener {
    private final String rootObject;
    private final List<String> containerNames = new ArrayList<>();

    public LogicalBlock getLogicalBlock() {
        return Optional.ofNullable(logicalBlock).orElseGet(this::generateLBWithContainers);
    }

    private LogicalBlock generateLBWithContainers() {
        checkIfContainerIsCompatibleWithQueryFunction(QueryFunction.NOT_NULL);
        final ScopeObject.ScopeObjectBuilder scopeObjectBuilder = ScopeObject.builder(getTopologyObjectForScope(
                this.rootObject, this.containerNames)).queryFunction(QueryFunction.NOT_NULL).resolverDataType(
                        ResolverDataType.NOT_NULL);
        addScopeLogicalBlock(scopeObjectBuilder);
        return this.logicalBlock;
    }

    private void checkIfContainerIsCompatibleWithQueryFunction(QueryFunction queryFunction) {
        List<String> containerTypesThatShouldNotBePairedWithNullQueryFunction = List.of(ATTRIBUTES, CLASSIFIERS, DECORATORS,
                SOURCE_IDS, METADATA);
        if (queryFunction.equals(QueryFunction.NOT_NULL) && containerNames.stream().anyMatch(
                containerTypesThatShouldNotBePairedWithNullQueryFunction::contains)) {
            throw TeivPathException.grammarError("Invalid data in scopeFilter");
        }
    }

    private LogicalBlock logicalBlock;

    public ScopeFilterListener(String rootObject) {
        this.rootObject = rootObject;
    }

    @Override
    public void exitContainerName(final teivPathParser.ContainerNameContext ctx) {
        final String containerName = ctx.getText();
        containerNames.add(containerName);
    }

    @Override
    public void exitTextFunctionCondition(teivPathParser.TextFunctionConditionContext ctx) {
        if (getContainerType(this.containerNames) == ContainerType.ID) {
            final ScopeObject.ScopeObjectBuilder scopeObjectBuilder = getScopeObjectBuilder(null, QueryFunction.EQ);
            scopeObjectBuilder.parameter(removeQuotes(ctx.StringLiteral().getText())).resolverDataType(
                    ResolverDataType.STRING);
            addScopeLogicalBlock(scopeObjectBuilder);
        } else {
            throw TeivPathException.grammarError("text() is supported for ID only");
        }
    }

    @Override
    public void exitContainsTextFunctionCondition(teivPathParser.ContainsTextFunctionConditionContext ctx) {
        if (getContainerType(this.containerNames) == ContainerType.ID) {
            final ScopeObject.ScopeObjectBuilder scopeObjectBuilder = getScopeObjectBuilder(null, QueryFunction.CONTAINS);
            scopeObjectBuilder.parameter(removeQuotes(ctx.StringLiteral().getText())).resolverDataType(
                    ResolverDataType.STRING);
            addScopeLogicalBlock(scopeObjectBuilder);
        } else {
            throw TeivPathException.grammarError("text() is supported for ID only");
        }
    }

    @Override
    public void exitContainsFunctionCondition(final teivPathParser.ContainsFunctionConditionContext ctx) {
        final ScopeObject.ScopeObjectBuilder scopeObjectBuilder = getScopeObjectBuilder(ctx.leafName().getText(),
                QueryFunction.CONTAINS);
        scopeObjectBuilder.parameter(removeQuotes(ctx.StringLiteral().getText())).resolverDataType(ResolverDataType.STRING);
        addScopeLogicalBlock(scopeObjectBuilder);
    }

    @Override
    public void exitWithinMetersFunctionCondition(final teivPathParser.WithinMetersFunctionConditionContext ctx) {
        final ScopeObject.ScopeObjectBuilder scopeObjectBuilder = getScopeObjectBuilder(ctx.leafName().getText(),
                QueryFunction.WITHIN_METERS);
        String parameter = ctx.StringLiteral().getText() + ", ";
        if (ctx.IntegerLiteral() != null) {
            parameter += ctx.IntegerLiteral().getText();
        } else if (ctx.DoubleLiteral() != null) {
            parameter += ctx.DoubleLiteral().getText();
        } else if (ctx.DecimalLiteral() != null) {
            parameter += ctx.DecimalLiteral().getText();
        } else {
            throw TeivPathException.grammarError("Integer, decimal or double meters parameter expected!");
        }
        scopeObjectBuilder.parameter(parameter).resolverDataType(ResolverDataType.STRING);
        addScopeLogicalBlock(scopeObjectBuilder);
    }

    @Override
    public void exitCoveredByFunctionCondition(final teivPathParser.CoveredByFunctionConditionContext ctx) {
        final ScopeObject.ScopeObjectBuilder scopeObjectBuilder = getScopeObjectBuilder(ctx.leafName().getText(),
                QueryFunction.COVERED_BY);
        scopeObjectBuilder.parameter(removeQuotes(ctx.StringLiteral().getText())).resolverDataType(ResolverDataType.STRING);
        addScopeLogicalBlock(scopeObjectBuilder);
    }

    @Override
    public void exitLeafCondition(final teivPathParser.LeafConditionContext ctx) {
        final ScopeObject.ScopeObjectBuilder scopeObjectBuilder = getScopeObjectBuilder(ctx.leafName().getText(),
                QueryFunction.fromValue(ctx.comparativeOperators().getText()));
        if (ctx.IntegerLiteral() != null) {
            scopeObjectBuilder.parameter(ctx.IntegerLiteral().getText()).resolverDataType(ResolverDataType.INTEGER);
        } else if (ctx.StringLiteral() != null) {
            scopeObjectBuilder.parameter(removeQuotes(ctx.StringLiteral().getText())).resolverDataType(
                    ResolverDataType.STRING);
        } else {
            throw TeivPathException.grammarError("Unsupported comparison value encountered in expression" + ctx.getText());
        }
        addScopeLogicalBlock(scopeObjectBuilder);
    }

    private String removeQuotes(String text) {
        // Remove first occurrence of single or double quotes at the start and end of the string
        return text.replaceAll("^['\"]+", "").replaceAll("['\"]+$", "");
    }

    @Override
    public void exitBooleanOperators(final teivPathParser.BooleanOperatorsContext ctx) {
        final AndOrLogicalBlock andOrLogicalBlock;
        if (ctx.getText().equals("and")) {
            andOrLogicalBlock = new AndLogicalBlock();
            andOrLogicalBlock.addChild(this.logicalBlock);
        } else {
            andOrLogicalBlock = new OrLogicalBlock();
            andOrLogicalBlock.addChild(this.logicalBlock);
        }
        this.logicalBlock = andOrLogicalBlock;
    }

    @Override
    public void exitFieldLeaf(final teivPathParser.FieldLeafContext ctx) {
        throw TeivPathException.grammarError("Parameter without any condition is not supported in scope filter");
    }

    private ScopeObject.ScopeObjectBuilder getScopeObjectBuilder(final String leafName, final QueryFunction queryFunction) {
        final String topologyObject = getTopologyObjectForScope(this.rootObject, this.containerNames);
        final ScopeObject.ScopeObjectBuilder scopeObjectBuilder = ScopeObject.builder(topologyObject).leaf(leafName)
                .queryFunction(queryFunction);
        Optional.ofNullable(getContainerType(this.containerNames)).ifPresentOrElse(scopeObjectBuilder::container, () -> {
            if (!isComplexAttribute(containerNames)) {
                final String container = this.containerNames.get(containerNames.size() - 1);
                if (topologyObject.equals(container)) {
                    Optional.ofNullable(getContainerType(List.of(leafName))).ifPresentOrElse(
                            containerType -> scopeObjectBuilder.container(containerType).leaf(null), () -> {
                                throw TeivPathException.grammarError(String.format(
                                        "%s is not a valid leaf for topology object: %s", leafName, rootObject));
                            });
                } else {
                    scopeObjectBuilder.topologyObject(Objects.equals(topologyObject, WILDCARD) ? null : topologyObject);
                }
            } else {
                scopeObjectBuilder.topologyObject(topologyObject.equals(WILDCARD) ? null : topologyObject).container(
                        ContainerType.ATTRIBUTES).innerContainer(Collections.unmodifiableList(containerNames.subList(
                                containerNames.indexOf(ATTRIBUTES) + 1, containerNames.size())));
            }
        });

        return scopeObjectBuilder;
    }

    private void addScopeLogicalBlock(ScopeObject.ScopeObjectBuilder scopeObjectBuilder) {
        if (this.logicalBlock == null || this.logicalBlock instanceof ScopeLogicalBlock) {
            this.logicalBlock = new ScopeLogicalBlock(scopeObjectBuilder.build());
        } else if (this.logicalBlock instanceof AndOrLogicalBlock andOrLogicalBlock) {
            andOrLogicalBlock.addChild(new ScopeLogicalBlock(scopeObjectBuilder.build()));
        }
    }
}
