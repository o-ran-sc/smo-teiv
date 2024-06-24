#!/bin/bash
# ============LICENSE_START=======================================================
# Copyright (C) 2024 OpenInfra Foundation Europe. All rights reserved.
# ================================================================================
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# ============LICENSE_END============================================
#
ROOT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"

helm uninstall oran-smo -n smo
kubectl delete namespace smo
kubectl get pv | grep Released | awk '$1 {print$1}' | while read vol; do kubectl delete pv/${vol}; done

# Cleanup ChartMuseum
CM_PID_FILE="$ROOT_DIR/CM_PID.txt"
if [ -f $CM_PID_FILE ]; then
  echo "Cleaning up ChartMuseum..."
  PID=$(cat "$CM_PID_FILE")
  echo "Killing ChartMuseum with PID $PID"
  kill $PID
  rm $CM_PID_FILE
  echo "ChartMuseum cleanup completed"
fi

rm -rf "$ROOT_DIR/chartstorage"