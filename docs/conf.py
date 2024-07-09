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
    'sphinxcontrib.redoc'
]

redoc = [
            {
                'name': 'Topology Exposure & Inventory Service API',
                'page': 'offeredapis/topology-exposure-inventory-openapi',
                'spec': './offeredapis/topology-exposure-inventory-openapi.yaml',
                'embed': True,
            }
]
html_extra_path = [
]

linkcheck_ignore = [
  r'http://localhost:\d+/',
  './offeredapis/topology-exposure-inventory-openapi.html',  #Generated file that doesn't exist at link check.
  './offeredapis/index.html',  #Generated file that doesn't exist at link check.
  './offeredapis/html/index.html'  #Generated file that doesn't exist at link check.
]
