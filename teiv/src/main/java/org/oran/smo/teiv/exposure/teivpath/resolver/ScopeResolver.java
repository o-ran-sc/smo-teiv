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

import java.util.Optional;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.oran.smo.teiv.antlr4.teivPathParser;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.AndLogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.EmptyLogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.LogicalBlock;
import org.oran.smo.teiv.exposure.teivpath.innerlanguage.OrLogicalBlock;
import org.oran.smo.teiv.utils.path.exception.PathParsingException;
import org.oran.smo.teiv.utils.query.exception.TeivPathException;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ScopeResolver implements PathResolver<LogicalBlock> {

    @Override
    public LogicalBlock onEmptyFilter(String rootObject) {
        return EmptyLogicalBlock.getInstance();
    }

    @Override
    public void preCheck(String rootObject, String filter) {
        //DO NOTHING
    }

    @Override
    public LogicalBlock process(String rootObject, String filter) {
        final int lastIndexOf = getLastIndexOf(filter);
        if (lastIndexOf != -1) {
            final LogicalBlock logicalBlock = getLogicalBlock(rootObject, filter.substring(lastIndexOf + 1));
            final char c = filter.charAt(lastIndexOf);
            if (c == ';') {
                AndLogicalBlock andLogicalBlock = new AndLogicalBlock();
                andLogicalBlock.addChild(logicalBlock);
                andLogicalBlock.addChild(resolve(rootObject, filter.substring(0, lastIndexOf)));
                return andLogicalBlock;
            } else if (c == '|') {
                OrLogicalBlock orLogicalBlock = new OrLogicalBlock();
                orLogicalBlock.addChild(logicalBlock);
                orLogicalBlock.addChild(resolve(rootObject, filter.substring(0, lastIndexOf)));
                return orLogicalBlock;
            }
        }
        return getLogicalBlock(rootObject, filter);
    }

    private int getLastIndexOf(String string) {
        final boolean containsAnd = string.contains(";");
        final boolean containsOr = string.contains("|");
        if (containsAnd && containsOr) {
            return Math.max(string.lastIndexOf(";"), string.lastIndexOf("|"));
        } else if (containsAnd) {
            return string.lastIndexOf(";");
        } else {
            return string.lastIndexOf("|");
        }
    }

    private LogicalBlock getLogicalBlock(final String rootObject, final String filter) {
        try {
            final teivPathParser teivPathParser = getTeivPathParser(filter);
            final ScopeFilterListener scopeFilterListener = new ScopeFilterListener(rootObject);
            teivPathParser.addParseListener(scopeFilterListener);
            teivPathParser.teivPath();
            LogicalBlock logicalBlock = scopeFilterListener.getLogicalBlock();
            return Optional.ofNullable(logicalBlock).orElseThrow(() -> TeivPathException.grammarError(String.format(
                    "Unsupported filter token: %s", filter)));
        } catch (ParseCancellationException | PathParsingException e) {
            log.error("Parsing error on target {} :", filter, e);
            throw TeivPathException.grammarError(e.getMessage());
        }
    }

}
