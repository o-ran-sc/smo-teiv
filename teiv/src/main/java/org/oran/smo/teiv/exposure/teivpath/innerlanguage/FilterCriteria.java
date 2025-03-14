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
package org.oran.smo.teiv.exposure.teivpath.innerlanguage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jooq.SelectField;
import org.oran.smo.teiv.schema.DataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder(builderMethodName = "hiddenBuilder")
@Slf4j
public class FilterCriteria {
    @NonNull
    private final String domain;
    private final ResolvingTopologyObjectType resolvingTopologyObjectType;
    private List<InnerFilterCriteria> filterCriteriaList;

    public static FilterCriteriaBuilder builder(final String domain) {
        return hiddenBuilder().domain(domain);
    }

    public Map<SelectField, Map<SelectField, DataType>> getSelects() {
        Map<SelectField, Map<SelectField, DataType>> selectFields = new HashMap<>();

        filterCriteriaList.forEach(t -> {
            Map<SelectField, Map<SelectField, DataType>> selects = t.getSelects();
            for (Map.Entry<SelectField, Map<SelectField, DataType>> select : selects.entrySet()) {
                if (!selectFields.containsKey(select.getKey())) {
                    selectFields.put(select.getKey(), new HashMap<>());
                }
                selectFields.get(select.getKey()).putAll(select.getValue());
            }
        });

        return selectFields;
    }

    @Getter
    @AllArgsConstructor
    public enum ResolvingTopologyObjectType {
        RELATIONSHIP(true, false),
        ENTITY(false, true),
        ALL(true, true);

        private final boolean resolveRelationships;
        private final boolean resolveEntities;
    }
}
