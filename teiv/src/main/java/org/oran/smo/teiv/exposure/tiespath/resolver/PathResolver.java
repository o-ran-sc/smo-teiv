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

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.oran.smo.teiv.antlr4.tiesPathParser;
import org.oran.smo.teiv.utils.path.StrictErrorStrategy;
import org.oran.smo.teiv.utils.path.TiesPathErrorListener;
import org.oran.smo.teiv.utils.path.TiesPathLexer;
import org.oran.smo.teiv.utils.path.TiesPathParser;

public interface PathResolver<T> {

    /**
     * Resolves the supplied filter.
     *
     * @param rootObject
     *     - root
     * @param filter
     *     - filter for resolving
     * @return T
     */
    default T resolve(String rootObject, String filter) {
        if (filter == null) {
            return onEmptyFilter(rootObject);
        }
        preCheck(rootObject, filter);
        return process(rootObject, filter);
    }

    T onEmptyFilter(String rootObject);

    void preCheck(String rootObject, String filter);

    T process(String rootObject, String filter);

    default tiesPathParser getTiesPathParser(String filter) {
        final CharStream inputStream = CharStreams.fromString(filter);
        final TiesPathLexer tiesPathLexer = new TiesPathLexer(inputStream);
        final tiesPathParser tiesPathParser = new TiesPathParser(new CommonTokenStream(tiesPathLexer));
        tiesPathParser.removeErrorListeners();
        tiesPathParser.addErrorListener(new TiesPathErrorListener());
        tiesPathParser.setErrorHandler(new StrictErrorStrategy());

        return tiesPathParser;
    }
}
