#! /bin/bash
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

set -e

SOURCE_DIR="./target"
TARGET_DIR="./sql_scripts"
PLACEHOLDER1=":pguser"
PLACEHOLDER2=":'pguser'"
REPLACEMENT_VALUE="${1:-topology_exposure_user}"
REPLACEMENT2="'$REPLACEMENT_VALUE'"

declare -A FILES
FILES=( ["00_init-oran-smo-teiv-data.sql"]="01_init-teiv-exposure-data.sql"
        ["01_init-oran-smo-teiv-model.sql"]="00_init-teiv-exposure-model.sql"
        ["02_init-oran-smo-teiv-consumer-data.sql"]="02_init-teiv-exposure-consumer-data.sql"
        ["03_init-oran-smo-teiv-groups.sql"]="03_init-teiv-exposure-groups.sql")

mkdir -p "$TARGET_DIR"

for OLD_NAME in "${!FILES[@]}"; do
    NEW_NAME="${FILES[$OLD_NAME]}"
    if [ -f "$SOURCE_DIR/$OLD_NAME" ]; then
        sed "s/$PLACEHOLDER1/$REPLACEMENT_VALUE/g; s/$PLACEHOLDER2/$REPLACEMENT2/g" "$SOURCE_DIR/$OLD_NAME" > "${SOURCE_DIR}/${OLD_NAME}.tmp" && \
        cp "${SOURCE_DIR}/${OLD_NAME}.tmp" "$TARGET_DIR/$NEW_NAME" && \
        echo "Replaced pguser in $OLD_NAME, copied and renamed $OLD_NAME to $NEW_NAME in $TARGET_DIR."
    else
        echo "File $OLD_NAME does not exist in $SOURCE_DIR."
    fi
done

echo "All tasks completed."