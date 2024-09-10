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
    openjdk-17-jdk-headless \
    graphviz && \
    rm -rf /var/lib/apt/lists/*

ARG USER_ID=40514
ARG USER_NAME="pgsql-schema-generator"
RUN useradd -u $USER_ID -m -s /bin/false $USER_NAME

RUN mkdir -p /opt/app/pgsql-schema-generator/target && \
    chown -R $USER_ID:$USER_ID /opt/app/pgsql-schema-generator

ARG JAR
WORKDIR /opt/app/pgsql-schema-generator
ADD src/main/resources/application.yaml /opt/app/pgsql-schema-generator/config/application.yaml
ADD ./copySqlSchemaFromPgsqlGenerator.sh /opt/app/pgsql-schema-generator/copySqlSchema.sh
ADD target/${JAR} /opt/app/pgsql-schema-generator/pgsql-schema-generator-app.jar

USER $USER_ID

CMD ["/bin/sh", "-c", "java ${JAVA_OPTS} -jar pgsql-schema-generator-app.jar && bash copySqlSchema.sh"]

ARG COMMIT
ARG BUILD_DATE
ARG APP_VERSION
LABEL \
    org.opencontainers.image.title=pgsql-schema-generator-jsb \
    org.opencontainers.image.created=$BUILD_DATE \
    org.opencontainers.image.revision=$COMMIT \
    org.opencontainers.image.vendor=ORAN \
    org.opencontainers.image.version=$APP_VERSION
