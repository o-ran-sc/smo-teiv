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
package org.oran.smo.teiv.exposure.tiespath.innerlanguage;

import java.util.List;

import org.jooq.Condition;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class AndLogicalBlock extends AndOrLogicalBlock {
    public static AndLogicalBlock fromLogicalBlockList(List<LogicalBlock> children) {
        AndLogicalBlock andLogicalBlock = new AndLogicalBlock();
        andLogicalBlock.addAllChild(children);
        return andLogicalBlock;
    }

    @Override
    public Condition getCondition() {
        Condition combinedCondition = children.get(0).getCondition();
        for (int i = 1; i < children.size(); i++) {
            combinedCondition = combinedCondition.and(children.get(i).getCondition());
        }
        return combinedCondition;
    }
}
