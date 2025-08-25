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

import static org.oran.smo.teiv.exposure.utils.PaginationUtil.firstHref;
import static org.oran.smo.teiv.exposure.utils.PaginationUtil.validateOffset;
import static org.oran.smo.teiv.exposure.utils.PaginationUtil.prevHref;
import static org.oran.smo.teiv.exposure.utils.PaginationUtil.lastHref;
import static org.oran.smo.teiv.exposure.utils.PaginationUtil.selfHref;
import static org.oran.smo.teiv.exposure.utils.PaginationUtil.nextHref;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.Record;
import org.jooq.Result;

import org.oran.smo.teiv.api.model.OranTeivEntities;
import org.oran.smo.teiv.exposure.utils.RequestDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EntityMapper extends ResponseMapper {
    public OranTeivEntities mapEntities(final Result<Record> results, final RequestDetails requestDetails) {
        //Pair<items, totalCount>
        final Pair<List<Object>, Integer> pair = getItemsWithTotalCount(results);
        int totalCount = pair.getRight();
        validateOffset(requestDetails.getOffset(), totalCount);
        return OranTeivEntities.builder().items(pair.getLeft()).first(firstHref(requestDetails)).prev(prevHref(
                requestDetails, totalCount)).self(selfHref(requestDetails)).next(nextHref(requestDetails, totalCount)).last(
                        lastHref(requestDetails, totalCount)).totalCount(totalCount).build();
    }
}
