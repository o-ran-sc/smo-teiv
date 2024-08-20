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
package org.oran.smo.teiv.exposure.spi.mapper;

import static org.oran.smo.teiv.exposure.tiespath.refiner.AliasMapper.getOriginalAlias;
import static org.oran.smo.teiv.utils.TiesConstants.ID_COLUMN_NAME;
import static org.oran.smo.teiv.utils.TiesConstants.PROPERTY_A_SIDE;
import static org.oran.smo.teiv.utils.TiesConstants.PROPERTY_B_SIDE;
import static org.oran.smo.teiv.utils.TiesConstants.QUOTED_STRING;
import static org.oran.smo.teiv.utils.TiesConstants.SOURCE_IDS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.exception.DataTypeException;

import org.oran.smo.teiv.schema.RelationType;
import lombok.extern.slf4j.Slf4j;
import org.oran.smo.teiv.utils.TiesConstants;

@Slf4j
public abstract class ResponseMapper {
    private static final String COUNT_FIELD = "count";

    protected Map<String, Object> createProperties(final Record record, final RelationType relationType) {
        final Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(ID_COLUMN_NAME, String.valueOf(record.get(relationType.getTableName() + "." + String.format(
                QUOTED_STRING, relationType.getIdColumnName()))));
        dataMap.put(PROPERTY_A_SIDE, String.valueOf(record.get(relationType.getTableName() + "." + String.format(
                QUOTED_STRING, relationType.aSideColumnName()))));
        dataMap.put(PROPERTY_B_SIDE, String.valueOf(record.get(relationType.getTableName() + "." + String.format(
                QUOTED_STRING, relationType.bSideColumnName()))));

        Field<?> sourceIds = record.field(relationType.getTableName() + "." + String.format(QUOTED_STRING, relationType
                .getSourceIdsColumnName()));
        if (sourceIds != null) {
            dataMap.put(SOURCE_IDS, mapField(record, sourceIds));
        }

        return dataMap;
    }

    protected Object mapField(final Record record, org.jooq.Field<?> field) {
        try {
            return !field.getType().equals(JSONB.class) ? record.getValue(field) : record.get(field, Map.class);
        } catch (DataTypeException e) {
            log.trace("Mapped as list", e);
            return record.get(field, List.class);
        }
    }

    public Pair<List<Object>, Integer> getItemsWithTotalCount(final Result<Record> results) {
        List<Object> items = new ArrayList<>();
        int totalCount = 0;
        for (Record result : results) {
            String managedObject = null;
            for (Field field : result.fields()) {
                final String alias = getOriginalAlias(field.getName());
                String[] nameTokens = alias.split("\\.");
                if (isCountField(nameTokens)) {
                    final Object count = result.get(COUNT_FIELD);
                    if (null != count) {
                        totalCount = (int) count;
                    }
                    break;
                }

                if (isIdFieldNotNull(result, field, nameTokens)) {
                    managedObject = nameTokens[0];
                }
            }

            if (managedObject != null) {
                items.add(getItem(result, managedObject));
            }
        }
        return Pair.of(items, totalCount);
    }

    private boolean isIdFieldNotNull(final Record result, final Field field, final String[] nameTokens) {
        return nameTokens[1].equals(ID_COLUMN_NAME) && result.get(field) != null;
    }

    private boolean isCountField(final String[] nameTokens) {
        return nameTokens.length < 2 && nameTokens[0].equals(COUNT_FIELD);
    }

    private Map<String, Object> getItem(final Record result, final String managedObject) {
        Map<String, Object> item = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> attributes = new HashMap<>();
        for (Field field : result.fields()) {
            final String alias = getOriginalAlias(field.getName());
            String[] nameTokens = alias.split("\\.");
            if (nameTokens[0].equals(managedObject)) {
                if (nameTokens[1].equals("attr")) {
                    attributes.put(nameTokens[2], mapField(result, field));
                } else {
                    data.put(nameTokens[1], mapField(result, field));
                }
            }
        }
        if (!attributes.isEmpty()) {
            data.put(TiesConstants.ATTRIBUTES, attributes);
        }
        item.put(managedObject, List.of(data));
        return item;
    }
}
