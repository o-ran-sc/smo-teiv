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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AliasMapperTest {

    @Test
    void testAliasMapper() {
        final String LONG_ALIAS = "this-is-a-very-long-alias-which-exceeds-the-sixty-three-characters";
        //hashed alias is same as alias if less than 63 chars
        final String hashedAlias1 = AliasMapper.hashAlias("alias");
        assertEquals("alias", hashedAlias1);
        //hashed alias is SHA-1 hash of alias
        final String hashedAlias2 = AliasMapper.hashAlias(LONG_ALIAS);
        assertEquals(40, hashedAlias2.length());
        assertEquals("8CE1DE1724594D06F010A083831CA2B0A345F730", hashedAlias2);

        //alias is same as hashed alias if less than 63 chars
        assertEquals("alias", AliasMapper.getOriginalAlias("alias"));
        //verify the retrieval of original alias from hashed alias
        assertEquals(LONG_ALIAS, AliasMapper.getOriginalAlias("8CE1DE1724594D06F010A083831CA2B0A345F730"));
    }

}
