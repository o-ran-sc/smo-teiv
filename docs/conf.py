#  ============LICENSE_START=======================================================
#  Copyright (C) 2024 Ericsson
#  Modifications Copyright (C) 2024 OpenInfra Foundation Europe
#  ================================================================================
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#        http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#
#  SPDX-License-Identifier: Apache-2.0
#  ============LICENSE_END=========================================================

from docs_conf.conf import *

project = "teiv"
release = "master"
version = "master"

author = "ORAN"

extensions = [
    'sphinx.ext.autosectionlabel',
    'sphinxcontrib.openapi',
]

# sphinxcontrib-openapi synthesises HTTP request/response examples
# whose bodies (e.g. enum literal values, free-form 'string'
# placeholders) Pygments' http lexer rejects. The extension already
# falls back to relaxed-mode lexing, but the warnings are promoted to
# errors under sphinx-build -W. Suppress them since the rendered
# output is correct.
suppress_warnings = ['misc.highlighting_failure']

html_extra_path = [
]

linkcheck_ignore = [
  r'http://localhost:\d+/',
]
