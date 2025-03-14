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
package org.oran.smo.teiv.exposure.utils;

import lombok.experimental.UtilityClass;
import org.oran.smo.teiv.api.model.OranTeivHref;
import org.oran.smo.teiv.exception.TeivException;

/**
 * Generates the pagination link as href.
 */
@UtilityClass
public class PaginationUtil {

    /**
     * Validates whether the offset is greater than the totalCount and throws exception.
     */
    public static void validateOffset(int offset, int totalCount) {
        if (totalCount > 0 && totalCount <= offset) {
            throw TeivException.invalidValueException("Offset", totalCount - 1, true);
        }
    }

    /**
     * Gets the viable offset based on given offset, limit and totalCount.
     *
     * @param offset
     *     - offset from the request
     * @param totalCount
     *     - total count of the records
     * @throws TeivException
     *     - if offset is greater than totalCount
     */
    public static int getViableLimit(final int offset, final int limit, final int totalCount) {
        validateOffset(offset, totalCount);

        return (totalCount == 0) ? totalCount : Math.min(limit, totalCount - offset);
    }

    /**
     * Gets the first page href.
     *
     * @param requestDetails
     *     - request details
     * @return first page href as {@link OranTeivHref}
     */
    public static OranTeivHref firstHref(RequestDetails requestDetails) {
        return getHref(requestDetails, 0);
    }

    /**
     * Gets the prev page href.
     *
     * @param requestDetails
     *     - request details
     * @param totalCount
     *     - total items count
     * @return prev page href as {@link OranTeivHref}
     */
    public static OranTeivHref prevHref(RequestDetails requestDetails, int totalCount) {
        if (requestDetails.getOffset() > 0 && totalCount > 0) {
            final int prevOffset = Math.max(requestDetails.getOffset() - requestDetails.getLimit(), 0);
            return getHref(requestDetails, prevOffset);
        }
        return selfHref(requestDetails);
    }

    /**
     * Gets the self page href.
     *
     * @param requestDetails
     *     - request details
     * @return self page href as {@link OranTeivHref}
     */
    public static OranTeivHref selfHref(RequestDetails requestDetails) {
        return getHref(requestDetails, requestDetails.getOffset());
    }

    /**
     * Gets the next page href.
     *
     * @param requestDetails
     *     - request details
     * @param totalCount
     *     - total items count
     * @return next page href as {@link OranTeivHref}
     */
    public static OranTeivHref nextHref(RequestDetails requestDetails, int totalCount) {
        if (hasNextPage(requestDetails, totalCount)) {
            final int nextOffset = Math.min(requestDetails.getOffset() + requestDetails.getLimit(), totalCount);
            return getHref(requestDetails, nextOffset);
        }

        return selfHref(requestDetails);
    }

    /**
     * Gets the last page href.
     *
     * @param requestDetails
     *     - request details
     * @param totalCount
     *     - total items count
     * @return last page href as {@link OranTeivHref}
     */
    public static OranTeivHref lastHref(RequestDetails requestDetails, int totalCount) {
        if (hasNextPage(requestDetails, totalCount)) {
            final int diff = totalCount - requestDetails.getOffset();
            final int lastOffset;
            if (diff % requestDetails.getLimit() == 0) {
                lastOffset = (diff / requestDetails.getLimit() - 1) * requestDetails.getLimit() + requestDetails
                        .getOffset();
            } else {
                lastOffset = (diff / requestDetails.getLimit()) * requestDetails.getLimit() + requestDetails.getOffset();
            }
            return getHref(requestDetails, lastOffset);
        }

        return selfHref(requestDetails);
    }

    private static OranTeivHref getHref(RequestDetails requestDetails, int pageOffset) {
        StringBuilder hrefString = new StringBuilder(requestDetails.getBasePath()).append(String.format(
                "?offset=%s&limit=%s", pageOffset, requestDetails.getLimit()));

        requestDetails.getQueryParams().forEach((key, value) -> {
            if (value != null) {
                hrefString.append("&").append(key).append("=").append(value);
            }
        });
        return OranTeivHref.builder().href(hrefString.toString()).build();
    }

    private static boolean hasNextPage(final RequestDetails requestDetails, int totalCount) {
        if (totalCount > 0) {
            return (requestDetails.getOffset() + requestDetails.getLimit()) < totalCount;
        }

        return false;
    }
}
