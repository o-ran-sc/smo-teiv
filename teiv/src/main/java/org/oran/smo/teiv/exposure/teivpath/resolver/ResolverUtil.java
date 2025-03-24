/*
 *  ============LICENSE_START=======================================================
 *  Modifications Copyright (C) 2025 OpenInfra Foundation Europe
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.experimental.UtilityClass;

import jakarta.annotation.Nullable;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.ContainerType;
import org.oran.smo.teiv.utils.query.exception.TeivPathException;

import static org.oran.smo.teiv.utils.TeivConstants.ATTRIBUTES;
import static org.oran.smo.teiv.utils.TeivConstants.WILDCARD;

@UtilityClass
public class ResolverUtil {
    public static String getTopologyObject(final String rootObject, final List<String> containerNames) {
        final int noOfContainers = containerNames.size();
        final String firstContainer = containerNames.get(0);

        if (noOfContainers > 2) {
            throw TeivPathException.grammarError("More than two level deep path is not allowed");
        } else if (noOfContainers == 2) {
            return getTopologyObjectWhenTwoContainers(rootObject, firstContainer);
        }
        return Optional.ofNullable(rootObject).orElse(WILDCARD);
    }

    public static boolean isComplexAttribute(final List<String> containerNames) {
        int index = containerNames.indexOf(ATTRIBUTES);
        return index != -1 && containerNames.size() - 1 > index;
    }

    @Nullable
    public static ContainerType getContainerType(final List<String> containerNames) {
        List<String> validContainerNames = containerNames.stream().filter(container -> ContainerType.fromValue(
                container) != null).toList();
        if (validContainerNames.size() > 1) {
            throw TeivPathException.grammarError(String.format("More than one valid containerType appeared in path: %s",
                    String.join(", ", validContainerNames)));
        }
        return ContainerType.fromValue(containerNames.get(containerNames.size() - 1));
    }

    private static String getTopologyObjectWhenTwoContainers(final String rootObject, final String firstContainer) {
        if (null == rootObject || firstContainer.equals(rootObject)) {
            return firstContainer;
        } else {
            throw TeivPathException.grammarError(
                    "Target/Scope filter can only contain Root Object types mentioned in the path parameter");
        }
    }

    public static String getTopologyObjectForScope(String rootObject, List<String> containerNames) {
        ContainerType containerType = identifyContainerType(containerNames);

        List<String> containerNamesPositionedBeforeContainerType = collectContainerNamesPositionedBeforeContainerType(
                containerNames, containerType);

        if (containerNamesPositionedBeforeContainerType.size() > 2) {
            throw TeivPathException.grammarError(containerType != null ?
                    String.format("Too many containers before %s", containerType.getValue()) :
                    String.format("Too many containers: %s", String.join(", ",
                            containerNamesPositionedBeforeContainerType)));
        }

        return assembleTopologyObject(rootObject, containerNamesPositionedBeforeContainerType);
    }

    private static String assembleTopologyObject(String rootObject,
            List<String> containerNamesPositionedBeforeContainerType) {
        if (rootObject == null) {
            if (containerNamesPositionedBeforeContainerType.isEmpty()) {
                return WILDCARD;
            } else if (containerNamesPositionedBeforeContainerType.size() == 1) {
                return WILDCARD + "/" + containerNamesPositionedBeforeContainerType.get(0);
            }
            return String.join("/", containerNamesPositionedBeforeContainerType);
        } else {
            if (containerNamesPositionedBeforeContainerType.size() == 2) {
                if (containerNamesPositionedBeforeContainerType.get(0).equals(rootObject)) {
                    return String.join("/", containerNamesPositionedBeforeContainerType);
                }
                throw TeivPathException.grammarError(
                        "Target/Scope filter can only contain Root Object types mentioned in the path parameter");
            } else if (containerNamesPositionedBeforeContainerType.size() == 1) {
                if (containerNamesPositionedBeforeContainerType.get(0).equals(rootObject)) {
                    return rootObject;
                } else {
                    return String.format("%s/%s", rootObject, containerNamesPositionedBeforeContainerType.get(0));
                }
            }
            return rootObject;
        }
    }

    private static List<String> collectContainerNamesPositionedBeforeContainerType(List<String> containerNames,
            ContainerType containerType) {
        List<String> containerNamesPositionedBeforeContainerType = new ArrayList<>();
        if (containerType != null) {
            containerNamesPositionedBeforeContainerType.addAll(containerNames.subList(0, containerNames.indexOf(
                    containerType.getValue())));
        } else {
            containerNamesPositionedBeforeContainerType.addAll(containerNames);
        }
        return containerNamesPositionedBeforeContainerType;
    }

    private static ContainerType identifyContainerType(List<String> containerNames) {
        ContainerType containerType = null;
        for (String containerName : containerNames) {
            if (ContainerType.fromValue(containerName) != null) {
                containerType = ContainerType.fromValue(containerName);
                break;
            }
        }
        return containerType;
    }
}
