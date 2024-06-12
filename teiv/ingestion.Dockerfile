#
# ============LICENSE_START=======================================================
# Copyright (C) 2024 Ericsson
# Modifications Copyright (C) 2024 OpenInfra Foundation Europe
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
#

FROM debian:bullseye-slim

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    openjdk-17-jdk-headless && \
    rm -rf /var/lib/apt/lists/*

ARG USER_ID=40514
RUN echo "$USER_ID:!::0:::::" >>/etc/shadow

ARG USER_NAME="topology-exposure-inventory"
RUN echo "$USER_ID:x:$USER_ID:0:An Identity for $USER_NAME:/nonexistent:/bin/false" >>/etc/passwd

ARG JAR
WORKDIR /opt/app/teiv

ENV SPRING_PROFILES_ACTIVE=ingestion

ADD src/main/resources/application.yaml /opt/app/teiv/config/application.yaml
ADD target/${JAR} /opt/app/teiv/topology-exposure-inventory-app.jar

USER $USER_ID

CMD ["/bin/sh", "-c", "java ${JAVA_OPTS} -jar topology-exposure-inventory-app.jar"]

ARG COMMIT
ARG BUILD_DATE
ARG APP_VERSION
LABEL \
    org.opencontainers.image.title=topology-ingestion-jsb \
    org.opencontainers.image.created=$BUILD_DATE \
    org.opencontainers.image.revision=$COMMIT \
    org.opencontainers.image.vendor=ORAN \
    org.opencontainers.image.version=$APP_VERSION
