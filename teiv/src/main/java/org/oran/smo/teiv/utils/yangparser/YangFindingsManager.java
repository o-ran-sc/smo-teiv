/*
 *  ============LICENSE_START=======================================================
 *  Copyright (C) 2024 Ericsson
 *  Modifications Copyright (C) 2024-2025 OpenInfra Foundation Europe
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
package org.oran.smo.teiv.utils.yangparser;

import static org.oran.smo.teiv.utils.TeivConstants.INVALID_SCHEMA;
import static org.oran.smo.teiv.utils.TeivConstants.SCHEMA_ALREADY_EXISTS;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.oran.smo.yangtools.parser.findings.Finding;
import org.oran.smo.yangtools.parser.findings.FindingSeverity;
import org.oran.smo.yangtools.parser.findings.FindingSeverityCalculator;
import org.oran.smo.yangtools.parser.findings.FindingsManager;
import org.oran.smo.teiv.CustomMetrics;
import org.oran.smo.teiv.exception.TeivException;
import org.oran.smo.teiv.exception.YangValidationException;

@Slf4j
class YangFindingsManager {
    private static final String SCHEMA_NODE_NOT_FOUND_CODE = "P075_CORRESPONDING_SCHEMA_NODE_NOT_FOUND";
    private static final String NULL_VALUE_CODE = "P080_NULL_VALUE";
    private static final String WRONG_ENCODING_CODE = "D710_WRONG_ENCODING";
    private static final String CARDINALITY_VIOLATION_CODE = "D712_CARDINALITY_VIOLATION";

    private static final String UNSPECIFIED_ERROR = "UNSPECIFIED_ERROR";

    private static final String ATTRIBUTES_MSG = "/attributes/";
    private static final String SOURCE_IDS_MSG = "/sourceIds'";
    private static final String SOURCE_IDS_IN_DELETE_MSG = "'sourceIds'";
    private static final String NUMERIC_TYPE_ENCODING_REGEX = "(int64|uint64|decimal64) values must be encoded as strings in JSON\\.";

    private YangFindingsManager() {
    }

    private static FindingSeverity calculateSeverity(FindingSeverityCalculator severityCalculator, final Finding finding,
            final boolean areSidesAndSourceIdsMandatory) {
        String findingType = finding.getFindingType();
        String findingMsg = finding.getMessage();
        FindingSeverity severity = severityCalculator.calculateSeverity(findingType);
        switch (findingType) {
            case SCHEMA_NODE_NOT_FOUND_CODE:
                if (findingMsg.contains(SOURCE_IDS_MSG)) {
                    severity = FindingSeverity.SUPPRESS;
                } else if (findingMsg.contains(ATTRIBUTES_MSG)) {
                    severity = FindingSeverity.WARNING;
                }
                break;
            case NULL_VALUE_CODE:
                if (findingMsg.contains(ATTRIBUTES_MSG)) {
                    severity = FindingSeverity.WARNING;
                }
                break;
            case WRONG_ENCODING_CODE:
                if (findingMsg.matches(".*" + NUMERIC_TYPE_ENCODING_REGEX + ".*")) {
                    severity = FindingSeverity.SUPPRESS;
                }
                break;
            case CARDINALITY_VIOLATION_CODE:
                if (!areSidesAndSourceIdsMandatory && findingMsg.contains(SOURCE_IDS_IN_DELETE_MSG)) {
                    severity = FindingSeverity.SUPPRESS;
                }
                break;
            default:
                break;
        }
        return severity;
    }

    protected static void handleExposureFindings(FindingsManager findingsManager) {
        Set<Finding> findings = findingsManager.getAllFindings();
        if (findings.isEmpty()) {
            return;
        }
        FindingSeverityCalculator severityCalculator = findingsManager.getFindingSeverityCalculator();
        findings.stream().sorted(Comparator.comparing(Finding::getMessage)).filter(finding -> isErrorFinding(
                severityCalculator, finding)).findFirst().ifPresent(YangFindingsManager::handleFindingsAsError);
    }

    private static void handleFindingsAsError(Finding finding) {
        if (isInvalidSchemaFinding(finding)) {
            throw TeivException.invalidFileInput(INVALID_SCHEMA);
        } else if (finding.getFindingType().contains("P044_SAME_MODULE_IMPLEMENTS_AND_IMPORTS")) {
            throw TeivException.invalidFileInput(SCHEMA_ALREADY_EXISTS);
        } else {
            throw TeivException.invalidFileInput(finding.getMessage());
        }
    }

    protected static void handleIngestionFindings(FindingsManager findingsManager,
            final boolean areSidesAndSourceIdsMandatory, CustomMetrics customMetrics) throws YangValidationException {
        Set<Finding> findings = new HashSet<>();
        for (Finding finding : findingsManager.getAllFindings()) {
            switch (calculateSeverity(findingsManager.getFindingSeverityCalculator(), finding,
                    areSidesAndSourceIdsMandatory)) {
                case ERROR:
                    findings.add(finding);
                    break;
                case WARNING:
                    if (isIgnorableAttributeFinding(finding) && customMetrics != null) {
                        customMetrics.incrementNumIgnoredAttributes();
                    }
                    log.warn("Warning: {}", finding.getMessage());
                    break;
                case INFO:
                    log.info(finding.getMessage());
                    break;
                default:
                    break;
            }
        }

        if (!findings.isEmpty()) {
            throw YangValidationException.validationFailed(findings);
        }
    }

    private static boolean isErrorFinding(FindingSeverityCalculator severityCalculator, final Finding finding) {
        return severityCalculator.calculateSeverity(finding.getFindingType()).equals(FindingSeverity.ERROR);
    }

    private static boolean isIgnorableAttributeFinding(final Finding finding) {
        return finding.getFindingType().equals(SCHEMA_NODE_NOT_FOUND_CODE) && finding.getMessage().contains(ATTRIBUTES_MSG);
    }

    private static boolean isInvalidSchemaFinding(final Finding finding) {
        return finding.getMessage().contains("exception") || finding.getFindingType().contains(UNSPECIFIED_ERROR);
    }
}
