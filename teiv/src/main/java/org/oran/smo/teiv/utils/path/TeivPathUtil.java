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
package org.oran.smo.teiv.utils.path;

import org.oran.smo.teiv.antlr4.teivPathParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeivPathUtil {
    /**
     * @deprecated (this method is deprecated)
     */
    @Deprecated
    public static TeivPathQuery getTeivPathQuery(final String TeivPathSource) {
        final CharStream inputStream = CharStreams.fromString(TeivPathSource);
        final TeivPathLexer teivPathLexer = new TeivPathLexer(inputStream);
        final teivPathParser teivPathParser = new TeivPathParser(new CommonTokenStream(teivPathLexer));
        teivPathParser.removeErrorListeners();
        teivPathParser.addErrorListener(new TeivPathErrorListener());
        teivPathParser.setErrorHandler(new StrictErrorStrategy());
        final TeivPathBuilder teivPathBuilder = new TeivPathBuilder();
        teivPathParser.addParseListener(teivPathBuilder);
        teivPathParser.teivPath();

        return teivPathBuilder.build();
    }

}
