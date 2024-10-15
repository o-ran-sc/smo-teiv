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
package org.oran.smo.teiv.pgsqlgenerator.graphgenerator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HelperFunctions {

    @Value("${graphs.relationship-entities-bg-colour}")
    private boolean bgColour;

    public String getNodeFillColour(String input) {
        if (bgColour) {
            int hash = input.hashCode();

            int r = (hash >> 16) & 0xFF;
            int g = (hash >> 8) & 0xFF;
            int b = hash & 0xFF;

            return String.format("#%02X%02X%02X%02X", r, g, b, 65);
        }
        return "#FFFFFFFF";
    }

    public String getCardinality(long minCardinality, long maxCardinality) {
        return formatCardinality(minCardinality) + ".." + formatCardinality(maxCardinality);
    }

    public String formatCardinality(long cardinality) {
        return cardinality == Long.MAX_VALUE ? "*" : String.valueOf(cardinality);
    }

    public String escapeHtml(String text) {
        return StringUtils.replaceEach(text, new String[] { "<", ">" }, new String[] { "&lt;", "&gt;" });
    }
}
