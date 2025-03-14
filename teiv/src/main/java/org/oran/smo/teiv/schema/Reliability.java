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
package org.oran.smo.teiv.schema;

import static org.oran.smo.teiv.utils.TeivConstants.TEIV_DATA_SCHEMA;

import org.jooq.EnumType;
import org.jooq.Schema;
import org.jooq.impl.SchemaImpl;
import jakarta.validation.constraints.NotNull;

public enum Reliability implements EnumType {
    OK,
    RESTORED,
    ADVISED;

    @Override
    public @NotNull String getLiteral() {
        return this.name();
    }

    @Override
    public Schema getSchema() {
        return new SchemaImpl(TEIV_DATA_SCHEMA);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
