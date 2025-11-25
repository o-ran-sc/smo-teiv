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
    openjdk-17-jdk-headless

WORKDIR /opt/app/teiv

COPY src/main/resources/application.yaml config/application.yaml
COPY target/focom-to-teiv-adapter-0.3.0-SNAPSHOT.jar /opt/app/teiv/focom-to-teiv-adapter.jar

CMD ["/bin/sh", "-c", "java -jar focom-to-teiv-adapter.jar"]