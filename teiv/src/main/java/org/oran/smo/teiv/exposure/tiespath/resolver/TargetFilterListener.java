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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.antlr.v4.runtime.RuleContext;
import org.oran.smo.teiv.antlr4.tiesPathBaseListener;
import org.oran.smo.teiv.antlr4.tiesPathParser;
import org.oran.smo.teiv.exception.TiesException;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.ContainerType;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.TargetObject;
import org.oran.smo.teiv.utils.query.exception.TiesPathException;

import static org.oran.smo.teiv.exposure.tiespath.resolver.ResolverUtil.getContainerType;
import static org.oran.smo.teiv.exposure.tiespath.resolver.ResolverUtil.getTopologyObject;

public class TargetFilterListener extends tiesPathBaseListener {
    private final String rootObject;
    private final List<String> containerNames = new ArrayList<>();
    private List<String> attributeNames = new ArrayList<>();

    public TargetFilterListener(String rootObject) {
        this.rootObject = rootObject;
    }

    public TargetObject getTargetObject() {
        if (containerNames.isEmpty()) {
            //invalid scenario
            throw TiesException.serverException("Server unknown exception", "Requested query could not be processed", null);
        }
        return Optional.ofNullable(getContainerType(this.containerNames)).map(containerType -> {
            validateIfParamsApplicable(containerType);
            return TargetObject.builder(getTopologyObject(rootObject, containerNames)).container(containerType).params(
                    this.attributeNames).build();
        }).orElseGet(() -> checkAndProcessIfSingleContainerAndValidTopologyObject(rootObject, this.containerNames,
                this.attributeNames));
    }

    private void validateIfParamsApplicable(ContainerType containerType) {
        if (!containerType.isParamAllowedInTargetFilter() && !this.attributeNames.isEmpty()) {
            throw TiesPathException.grammarError(String.format("Parameters are not supported for %s in target filter",
                    containerType.getValue()));
        }
    }

    @Override
    public void exitContainerName(final tiesPathParser.ContainerNameContext ctx) {
        final String containerName = ctx.getText();
        containerNames.add(containerName);
    }

    @Override
    public void exitContainsFunctionCondition(final tiesPathParser.ContainsFunctionConditionContext ctx) {
        throw TiesPathException.grammarError("");
    }

    @Override
    public void exitLeafCondition(final tiesPathParser.LeafConditionContext ctx) {
        throw TiesPathException.grammarError("Parameter condition is not supported in target filter");
    }

    @Override
    public void exitBooleanOperators(final tiesPathParser.BooleanOperatorsContext ctx) {
        throw TiesPathException.grammarError("");
    }

    @Override
    public void exitFieldLeaf(final tiesPathParser.FieldLeafContext ctx) {
        attributeNames = ctx.leafName().stream().map(RuleContext::getText).toList();
    }

    private TargetObject checkAndProcessIfSingleContainerAndValidTopologyObject(String rootObject,
            List<String> containerNames, List<String> attributeNames) {
        final int noOfContainers = containerNames.size();
        if (noOfContainers == 1 && (null == rootObject || rootObject.equals(containerNames.get(0)))) {
            assertAttributesApplicableForContainer(attributeNames);
            return TargetObject.builder(containerNames.get(0)).build();
        }
        throw TiesPathException.grammarError(
                "Invalid Container name or Root Object name does not match to the path parameter");
    }

    private void assertAttributesApplicableForContainer(List<String> attrNames) {
        if (!attrNames.isEmpty()) {
            throw TiesPathException.grammarError("Attributes cannot be associated at this level");
        }
    }
}
