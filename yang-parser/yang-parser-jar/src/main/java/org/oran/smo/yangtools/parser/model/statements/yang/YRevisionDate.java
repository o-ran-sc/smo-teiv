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
package org.oran.smo.yangtools.parser.model.statements.yang;

import org.oran.smo.yangtools.parser.ParserExecutionContext;
import org.oran.smo.yangtools.parser.findings.Finding;
import org.oran.smo.yangtools.parser.findings.ParserFindingType;
import org.oran.smo.yangtools.parser.model.statements.AbstractStatement;
import org.oran.smo.yangtools.parser.model.statements.SimpleStatement;
import org.oran.smo.yangtools.parser.model.statements.StatementModuleAndName;
import org.oran.smo.yangtools.parser.model.yangdom.YangDomElement;

/**
 * Type-safe Yang core statement.
 *
 * @author Mark Hollmann
 */
public class YRevisionDate extends SimpleStatement {

    public YRevisionDate(final AbstractStatement parentStatement, final YangDomElement domNode) {
        super(parentStatement, domNode);
    }

    @Override
    public StatementArgumentType getArgumentType() {
        return StatementArgumentType.DATE;
    }

    @Override
    public StatementModuleAndName getStatementModuleAndName() {
        return CY.STMT_REVISION_DATE;
    }

    public String getRevisionDateValue() {
        return getValue();
    }

    protected void validate(final ParserExecutionContext context) {
        if (!validateArgumentNotNullNotEmpty(context)) {
            /* no point trying to perform more validation */
            return;
        }

        final String value = getRevisionDateValue();
        if (value.length() != 10) {
            context.addFinding(new Finding(this, ParserFindingType.P053_INVALID_VALUE,
                    "value '" + value + "' not valid for revision date (must be YYYY-MM-DD)."));
        } else {
            if (!Character.isDigit(value.charAt(0)) || !Character.isDigit(value.charAt(1)) || !Character.isDigit(value
                    .charAt(2)) || !Character.isDigit(value.charAt(3)) || value.charAt(4) != '-' || !Character.isDigit(value
                            .charAt(5)) || !Character.isDigit(value.charAt(6)) || value.charAt(7) != '-' || !Character
                                    .isDigit(value.charAt(8)) || !Character.isDigit(value.charAt(9))) {
                context.addFinding(new Finding(this, ParserFindingType.P053_INVALID_VALUE,
                        "value '" + value + "' not valid for revision date (must be YYYY-MM-DD)."));
            }
        }
    }
}
