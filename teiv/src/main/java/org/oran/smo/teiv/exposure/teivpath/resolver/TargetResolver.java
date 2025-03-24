/*
 *  ============LICENSE_START=======================================================
 *  Copyright (C) 2024 Ericsson
 *  Modifications Copyright (C) 2024-2025 OpenInfra Foundation Europe
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.oran.smo.teiv.antlr4.teivPathParser;
import org.springframework.stereotype.Component;

import org.oran.smo.teiv.exposure.teivpath.innerlanguage.TargetObject;
import org.oran.smo.teiv.utils.path.exception.PathParsingException;
import org.oran.smo.teiv.utils.query.exception.TeivPathException;
import lombok.extern.slf4j.Slf4j;

import static org.oran.smo.teiv.utils.TeivConstants.WILDCARD;

@Slf4j
@Component
public class TargetResolver implements PathResolver<List<TargetObject>> {

    @Override
    public List<TargetObject> onEmptyFilter(String rootObject) {
        final String topologyObject = Optional.ofNullable(rootObject).orElse(WILDCARD);
        final TargetObject targetObject = TargetObject.builder(topologyObject).build();
        final ArrayList<TargetObject> objects = new ArrayList<>();
        objects.add(targetObject);
        return objects;
    }

    @Override
    public void preCheck(String rootObject, String filter) {
        if (filter.contains("|")) {
            throw TeivPathException.grammarError("OR (|) is not supported for target filter");
        }
    }

    /**
     * Process filter and root objectType . Populates List of Target Object
     *
     * @param rootObject
     *     rootObject in the path parameter
     * @param filter
     *     filter in the query parameter
     * @return Immutable list of filter objects
     */
    @Override
    public List<TargetObject> process(String rootObject, String filter) {
        List<TargetObject> targetObjects = new ArrayList<>();
        Arrays.stream(filter.split(";")).forEach(targetToken -> {
            try {
                teivPathParser pathParser = getTeivPathParser(targetToken);
                final TargetFilterListener targetFilterListener = new TargetFilterListener(rootObject);
                pathParser.addParseListener(targetFilterListener);
                pathParser.teivPath();

                targetObjects.add(targetFilterListener.getTargetObject());
            } catch (ParseCancellationException | PathParsingException e) {
                log.error("Parsing error on target {} :", targetToken, e);
                throw TeivPathException.grammarError(e.getMessage());
            }
        });
        return targetObjects;
    }
}
