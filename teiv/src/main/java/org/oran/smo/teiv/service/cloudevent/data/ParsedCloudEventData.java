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
package org.oran.smo.teiv.service.cloudevent.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Comparator;
import java.util.List;

@Data
@AllArgsConstructor
public class ParsedCloudEventData {

    private List<Entity> entities;
    private List<Relationship> relationships;

    /**
     * Sorts the entity and relationship list by type and id.
     * The goal is to decrease the possibility of deadlocks in the database by executing the database operations in the same
     * order in every pod's every consumer.
     * For example: 2 transactions are trying to update both entity1 and entity2. If
     * they both start with entity1, then one transaction gets the row level lock on entity1 and can go ahead while the
     * other is blocked.
     * If it's not sorted, then the first transaction can get the lock for entity1, the other gets the lock for entity2, and
     * it's a deadlock.
     */
    public void sort() {
        if (entities != null) {
            entities = getEntities().stream().sorted(Comparator.comparing(Entity::getType).thenComparing(Entity::getId))
                    .toList();
        }
        if (relationships != null) {
            relationships = getRelationships().stream().sorted(Comparator.comparing(Relationship::getType).thenComparing(
                    Relationship::getStoringTablePrimaryKey)).toList();
        }
    }
}
