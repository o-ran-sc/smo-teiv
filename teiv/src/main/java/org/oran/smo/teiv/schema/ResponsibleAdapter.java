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

import org.oran.smo.teiv.exception.TiesException;
import lombok.Value;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.oran.smo.teiv.schema.BidiDbNameMapper.getDbName;
import static org.oran.smo.teiv.utils.TiesConstants.ID_COLUMN_NAME;
import static org.oran.smo.teiv.utils.TiesConstants.RESPONSIBLE_ADAPTER;
import static org.oran.smo.teiv.utils.TiesConstants.TIES_DATA;

@Value
public class ResponsibleAdapter {

    private static final String RESPONSIBLE_ADAPTER_TABLE_NAME = String.format(TIES_DATA, getDbName(RESPONSIBLE_ADAPTER));

    String id;
    byte[] hashedId;

    public ResponsibleAdapter(String name) {
        this.id = name;
        this.hashedId = this.generateHashId();
    }

    public String getTableName() {
        return RESPONSIBLE_ADAPTER_TABLE_NAME;
    }

    public String getIdColumnName() {
        return ID_COLUMN_NAME;
    }

    public String getHashIdsColumnName() {
        return getDbName("hashed_id");
    }

    private static MessageDigest getDigest() {
        try {
            return MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException exception) {
            throw TiesException.serverException("Invalid Hashing algorithm", "Error while hashing alias", exception);
        }
    }

    private byte[] generateHashId() {
        return getDigest().digest(this.id.getBytes(StandardCharsets.UTF_8));
    }
}
