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
package org.oran.smo.teiv.exposure.teivpath.innerlanguage;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Table;

import static org.jooq.impl.DSL.noCondition;

public class EmptyLogicalBlock extends LogicalBlock {
    private static EmptyLogicalBlock emptyLogicalBlock = null;

    private EmptyLogicalBlock() {
    }

    public static EmptyLogicalBlock getInstance() {
        if (emptyLogicalBlock == null) {
            emptyLogicalBlock = new EmptyLogicalBlock();
        }
        return emptyLogicalBlock;
    }

    @Override
    public Condition getCondition() {
        return noCondition();
    }

    @Override
    public Set<Table> getTables() {
        return Collections.emptySet();
    }

    public Set<Pair<String, Field>> getWhereExistsConditions() {
        return Collections.emptySet();
    }
}
