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

package org.oran.smo.teiv.utils;

import lombok.experimental.UtilityClass;
import org.oran.smo.teiv.service.models.OperationResult;

import java.util.List;

@UtilityClass
public class OperationResultParser {

    public static List<String> fromOperationResults(List<OperationResult> operationResults) {
        return operationResults.stream().filter(op -> !op.isInferred()).map(op -> String.format(
                "{\"%s:%s\":[{\"id\":\"%s\"}]}", op.getModule(), op.getType(), op.getId())).toList();
    }
}
