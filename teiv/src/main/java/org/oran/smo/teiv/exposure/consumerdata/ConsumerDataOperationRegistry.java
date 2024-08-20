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
package org.oran.smo.teiv.exposure.consumerdata;

import lombok.RequiredArgsConstructor;
import org.oran.smo.teiv.api.model.OranTeivClassifier;
import org.oran.smo.teiv.api.model.OranTeivDecorator;
import org.oran.smo.teiv.exposure.consumerdata.operation.ClassifiersOperation;
import org.oran.smo.teiv.exposure.consumerdata.operation.DecoratorsOperation;
import org.oran.smo.teiv.exposure.consumerdata.operation.DeleteClassifiersOperation;
import org.oran.smo.teiv.exposure.consumerdata.operation.DeleteDecoratorsOperation;
import org.oran.smo.teiv.exposure.consumerdata.operation.MergeClassifiersOperation;
import org.oran.smo.teiv.exposure.consumerdata.operation.MergeDecoratorsOperation;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsumerDataOperationRegistry {
    private final MergeClassifiersOperation mergeClassifiersOperation;
    private final DeleteClassifiersOperation deleteClassifiersOperation;
    private final MergeDecoratorsOperation mergeDecoratorsOperation;
    private final DeleteDecoratorsOperation deleteDecoratorsOperation;

    /**
     * Returns the concrete operation according to the parameter for classifiers.
     *
     * @param operation
     *     coming from the request
     * @return classifiers operation for execution
     */
    public ClassifiersOperation getClassifiersOperation(final OranTeivClassifier.OperationEnum operation) {
        return switch (operation) {
            case MERGE -> mergeClassifiersOperation;
            case DELETE -> deleteClassifiersOperation;
        };
    }

    /**
     * Returns the concrete operation according to the parameter for decorators.
     *
     * @param operation
     *     coming from the request
     * @return decorators operation for execution
     */
    public DecoratorsOperation getDecoratorsOperation(final OranTeivDecorator.OperationEnum operation) {
        return switch (operation) {
            case MERGE -> mergeDecoratorsOperation;
            case DELETE -> deleteDecoratorsOperation;
        };
    }
}
