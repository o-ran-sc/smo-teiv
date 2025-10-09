/*
 *  ============LICENSE_START=======================================================
 *  Modifications Copyright (C) 2025 OpenInfra Foundation Europe
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
package org.oran.smo.teiv.adapters.common.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final String ORAN_SMO_TEIV_URN_PREFIX = "urn:oran:smo:teiv";
    public static final String SMO_TEIV_RAN_PREFIX = "o-ran-smo-teiv-ran";
    public static final String SMO_TEIV_REL_OAM_RAN_PREFIX = "o-ran-smo-teiv-rel-oam-ran";
    public static final String _3GPP_GNBDUFUNTION_PREFIX = "_3gpp-nr-nrm-gnbdufunction";
    public static final String _3GPP_GNBCUCPFUNTION_PREFIX = "_3gpp-nr-nrm-gnbcucpfunction";
    public static final String SMO_TEIV_OAM_PREFIX = "o-ran-smo-teiv-oam";

    //relationshipType
    public static final String MANAGES = "MANAGES";
    public static final String O1LINK = "O1LINK";
    public static final String PROVIDES = "PROVIDES";

    //relationshipTypeNames (NCMP)
    public static final String OCUCPFUNCTION_PROVIDES_NRCELLCU = "OCUCPFUNCTION_PROVIDES_NRCELLCU";
    public static final String ODUFUNCTION_PROVIDES_NRCELLDU = "ODUFUNCTION_PROVIDES_NRCELLDU";
    public static final String ODUFUNCTION_O1LINK_SMO = "ODUFUNCTION_O1LINK_SMO";
    public static final String OCUCPFUNCTION_O1LINK_SMO = "OCUCPFUNCTION_O1LINK_SMO";
    public static final String MANAGEDELEMENT_MANAGES_OCUCPFUNCTION = "MANAGEDELEMENT_MANAGES_OCUCPFUNCTION";
    public static final String MANAGEDELEMENT_MANAGES_ODUFUNCTION = "MANAGEDELEMENT_MANAGES_ODUFUNCTION";
}
