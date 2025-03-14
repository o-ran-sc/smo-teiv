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
package org.oran.smo.teiv.exposure.teivpath.refiner;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import jakarta.xml.bind.DatatypeConverter;
import org.oran.smo.teiv.exception.TeivException;

@Slf4j
@UtilityClass
public class AliasMapper {

    private static final String SHA_1 = "SHA-1";
    private static final int POSTGRES_MAX_IDENTIFIER_LENGTH = 63;
    private static final Map<String, String> originalToHashed = new ConcurrentHashMap<>();
    private static final Map<String, String> hashedToOriginal = new ConcurrentHashMap<>();

    /**
     * Hashes the original alias if it is more than 63 characters and stores in the originalToHashed & hashedToOriginal
     * maps.
     *
     * @param originalAlias
     *     - original alias
     * @return hashed alias if more than 63 characters otherwise original alias
     */
    public static String hashAlias(final String originalAlias) {
        if (originalAlias.length() <= POSTGRES_MAX_IDENTIFIER_LENGTH) {
            return originalAlias;
        }
        if (originalToHashed.containsKey(originalAlias)) {
            return originalToHashed.get(originalAlias);
        }
        final String hashedAlias = generateSha1Hash(originalAlias);
        originalToHashed.put(originalAlias, hashedAlias);
        hashedToOriginal.put(hashedAlias, originalAlias);
        return hashedAlias;
    }

    /**
     * Gets the original alias from the hashed alias.
     *
     * @param hashedAlias
     *     - hashed alias
     * @return original alias
     */
    public static String getOriginalAlias(String hashedAlias) {
        return hashedToOriginal.getOrDefault(hashedAlias, hashedAlias);
    }

    private static String generateSha1Hash(final String stringToHash) {
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA_1);
            byte[] hashBytes = digest.digest(stringToHash.getBytes(StandardCharsets.UTF_8));
            return DatatypeConverter.printHexBinary(hashBytes).toUpperCase(Locale.US);
        } catch (NoSuchAlgorithmException exception) {
            log.warn("Not a valid algorithm", exception);
            throw TeivException.serverException("Invalid Hashing algorithm", "Error while hashing alias", exception);
        }
    }
}
