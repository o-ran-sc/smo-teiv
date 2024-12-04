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

import java.util.List;
import java.util.Optional;

import lombok.experimental.UtilityClass;

import jakarta.annotation.Nullable;
import org.oran.smo.teiv.exposure.tiespath.innerlanguage.ContainerType;
import org.oran.smo.teiv.schema.SchemaRegistry;
import org.oran.smo.teiv.utils.query.exception.TiesPathException;

import static org.oran.smo.teiv.utils.TiesConstants.ATTRIBUTES;
import static org.oran.smo.teiv.utils.TiesConstants.WILDCARD;

@UtilityClass
public class ResolverUtil {

    public static String getTopologyObject(final String rootObject, final List<String> containerNames) {
        final int noOfContainers = containerNames.size();
        final String firstContainer = containerNames.get(0);
        if (isContainingAssociation(containerNames)) {
            return Optional.ofNullable(rootObject).orElse(WILDCARD);
        }
        if (isComplexAttribute(containerNames)) {
            return getTopologyObjectOnComplexAttributeCondition(rootObject, firstContainer);
        } else if (noOfContainers > 2) {
            throw TiesPathException.grammarError("More than two level deep path is not allowed");
        } else if (noOfContainers == 2) {
            return getTopologyObjectWhenTwoContainers(rootObject, firstContainer, containerNames.get(1));
        }

        return Optional.ofNullable(rootObject).orElse(WILDCARD);
    }

    public static boolean isComplexAttribute(final List<String> containerNames) {
        int index = containerNames.indexOf(ATTRIBUTES);
        return index != -1 && containerNames.size() - 1 > index;
    }

    public static boolean isContainingAssociation(final List<String> containerNames) {
        return !containerNames.isEmpty() && containerNames.stream().anyMatch(SchemaRegistry::isValidAssociation);
    }

    @Nullable
    public static ContainerType getContainerType(final List<String> containerNames) {
        if (isContainingAssociation(containerNames)) {
            return null;
        }
        return ContainerType.fromValue(containerNames.get(containerNames.size() - 1));
    }

    private static String getTopologyObjectWhenTwoContainers(final String rootObject, final String firstContainer,
            final String secondContainer) {
        if (null == rootObject || firstContainer.equals(rootObject) || secondContainer.equals(rootObject)) {
            return firstContainer;
        } else {
            throw TiesPathException.grammarError(
                    "Target/Scope filter can only contain Root Object types mentioned in the path parameter");
        }
    }

    private static String getTopologyObjectOnComplexAttributeCondition(String rootObject, String firstContainer) {
        final ContainerType containerType = ContainerType.fromValue(firstContainer);
        if (rootObject == null) {
            if (containerType != null) {
                return WILDCARD;
            }
            return firstContainer;
        } else {
            if (rootObject.equals(firstContainer) || containerType != null) {
                return rootObject;
            }
            throw TiesPathException.grammarError(
                    "Target/Scope filter can only contain Root Object types mentioned in the path parameter");
        }
    }
}
