# ============LICENSE_START=======================================================
# Modifications Copyright (C) 2025 OpenInfra Foundation Europe
# ================================================================================
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# SPDX-License-Identifier: Apache-2.0
# ============LICENSE_END=========================================================

FROM debian:bullseye-slim

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    openjdk-17-jdk-headless && \
    rm -rf /var/lib/apt/lists/* 

ARG JAR
WORKDIR /opt/app/teiv

ADD src/main/resources/application.yaml /opt/app/teiv/config/application.yaml
ADD target/${JAR} /opt/app/teiv/ncmp-to-teiv-adapter.jar

CMD ["/bin/sh", "-c", "java -jar ncmp-to-teiv-adapter.jar"]
