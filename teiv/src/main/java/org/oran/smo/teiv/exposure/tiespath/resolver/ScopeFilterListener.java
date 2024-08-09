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
package org.oran.smo.teiv.exposure.tiespath.resolver;

import org.oran.smo.teiv.antlr4.tiesPathBaseListener;
import org.oran.smo.teiv.antlr4.tiesPathParser;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.AndLogicalBlock;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.AndOrLogicalBlock;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.ContainerType;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.LogicalBlock;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.OrLogicalBlock;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.QueryFunction;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.ScopeLogicalBlock;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.ScopeObject;
import org.oran.smo.teiv.utils.query.exception.TiesPathException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.oran.smo.teiv.exposure.tiespath.resolver.ResolverUtil.getContainerType;
import static org.oran.smo.teiv.exposure.tiespath.resolver.ResolverUtil.getTopologyObject;
import static org.oran.smo.teiv.exposure.tiespath.resolver.ResolverUtil.isComplexAttribute;
import static org.oran.smo.teiv.utils.TiesConstants.ATTRIBUTES;
import static org.oran.smo.teiv.utils.TiesConstants.WILDCARD;

public class ScopeFilterListener extends tiesPathBaseListener {
    private final String rootObject;
    private final List<String> containerNames = new ArrayList<>();

    public LogicalBlock getLogicalBlock() {
        return Optional.ofNullable(logicalBlock).orElseGet(this::generateLBWithContainers);
    }

    private LogicalBlock generateLBWithContainers() {
        final ScopeObject.ScopeObjectBuilder scopeObjectBuilder = ScopeObject.builder(getTopologyObject(this.rootObject,
                this.containerNames)).queryFunction(QueryFunction.NOT_NULL).innerContainer(new ArrayList<>(Arrays.asList(
                        this.containerNames.get(containerNames.size() - 1)))).resolverDataType(ResolverDataType.NOT_NULL);
        addScopeLogicalBlock(scopeObjectBuilder);
        return this.logicalBlock;
    }

    private LogicalBlock logicalBlock;

    public ScopeFilterListener(String rootObject) {
        this.rootObject = rootObject;
    }

    @Override
    public void exitContainerName(final tiesPathParser.ContainerNameContext ctx) {
        final String containerName = ctx.getText();
        containerNames.add(containerName);
    }

    @Override
    public void exitTextFunctionCondition(tiesPathParser.TextFunctionConditionContext ctx) {
        if (getContainerType(this.containerNames) == ContainerType.ID) {
            final ScopeObject.ScopeObjectBuilder scopeObjectBuilder = getScopeObjectBuilder(null, QueryFunction.EQ);
            scopeObjectBuilder.parameter(removeQuotes(ctx.StringLiteral().getText())).resolverDataType(
                    ResolverDataType.STRING);
            addScopeLogicalBlock(scopeObjectBuilder);
        } else {
            throw TiesPathException.grammarError("text() is supported for ID only");
        }
    }

    @Override
    public void exitContainsTextFunctionCondition(tiesPathParser.ContainsTextFunctionConditionContext ctx) {
        if (getContainerType(this.containerNames) == ContainerType.ID) {
            final ScopeObject.ScopeObjectBuilder scopeObjectBuilder = getScopeObjectBuilder(null, QueryFunction.CONTAINS);
            scopeObjectBuilder.parameter(removeQuotes(ctx.StringLiteral().getText())).resolverDataType(
                    ResolverDataType.STRING);
            addScopeLogicalBlock(scopeObjectBuilder);
        } else {
            throw TiesPathException.grammarError("text() is supported for ID only");
        }

    }

    @Override
    public void exitContainsFunctionCondition(final tiesPathParser.ContainsFunctionConditionContext ctx) {
        final ScopeObject.ScopeObjectBuilder scopeObjectBuilder = getScopeObjectBuilder(ctx.leafName().getText(),
                QueryFunction.CONTAINS);
        scopeObjectBuilder.parameter(removeQuotes(ctx.StringLiteral().getText())).resolverDataType(ResolverDataType.STRING);
        addScopeLogicalBlock(scopeObjectBuilder);
    }

    @Override
    public void exitLeafCondition(final tiesPathParser.LeafConditionContext ctx) {
        final ScopeObject.ScopeObjectBuilder scopeObjectBuilder = getScopeObjectBuilder(ctx.leafName().getText(),
                QueryFunction.fromValue(ctx.comparativeOperators().getText()));

        if (ctx.IntegerLiteral() != null) {
            scopeObjectBuilder.parameter(ctx.IntegerLiteral().getText()).resolverDataType(ResolverDataType.INTEGER);
        } else if (ctx.StringLiteral() != null) {
            scopeObjectBuilder.parameter(removeQuotes(ctx.StringLiteral().getText())).resolverDataType(
                    ResolverDataType.STRING);
        } else {
            throw TiesPathException.grammarError("Unsupported comparison value encountered in expression" + ctx.getText());
        }

        addScopeLogicalBlock(scopeObjectBuilder);
    }

    private String removeQuotes(String text) {
        // Remove first occurrence of single or double quotes at the start and end of the string
        return text.replaceAll("^['\"]+", "").replaceAll("['\"]+$", "");
    }

    @Override
    public void exitBooleanOperators(final tiesPathParser.BooleanOperatorsContext ctx) {
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
    public void exitFieldLeaf(final tiesPathParser.FieldLeafContext ctx) {
        throw TiesPathException.grammarError("Parameter without any condition is not supported in scope filter");
    }

    private ScopeObject.ScopeObjectBuilder getScopeObjectBuilder(final String leafName, final QueryFunction queryFunction) {
        final String topologyObject = getTopologyObject(this.rootObject, this.containerNames);
        final ScopeObject.ScopeObjectBuilder scopeObjectBuilder = ScopeObject.builder(topologyObject).leaf(leafName)
                .queryFunction(queryFunction);

        Optional.ofNullable(getContainerType(this.containerNames)).ifPresentOrElse(scopeObjectBuilder::container, () -> {
            if (!isComplexAttribute(containerNames)) {
                final String container = this.containerNames.get(containerNames.size() - 1);
                if (topologyObject.equals(container)) {
                    Optional.ofNullable(getContainerType(List.of(leafName))).ifPresentOrElse(
                            containerType -> scopeObjectBuilder.container(containerType).leaf(null), () -> {
                                throw TiesPathException.grammarError(String.format(
                                        "%s is not a valid leaf for topology object: %s", leafName, rootObject));
                            });
                } else {
                    scopeObjectBuilder.topologyObject(Objects.equals(topologyObject, WILDCARD) ? null : topologyObject)
                            .innerContainer(new ArrayList<>(Arrays.asList(container)));
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
