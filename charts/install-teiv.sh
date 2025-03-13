#!/bin/bash
# ============LICENSE_START=======================================================
# Copyright (C) 2024-2025 OpenInfra Foundation Europe. All rights reserved.
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
CM_VERSION="v0.16.2"
CM_PORT="8879"
HELM_LOCAL_REPO="$ROOT_DIR/chartstorage"

echo "Installing ChartMuseum binary..."
pushd /tmp
wget https://get.helm.sh/chartmuseum-$CM_VERSION-linux-amd64.tar.gz
tar xvfz chartmuseum-$CM_VERSION-linux-amd64.tar.gz
sudo mv /tmp/linux-amd64/chartmuseum /usr/local/bin/chartmuseum

echo "Starting ChartMuseum on port $CM_PORT..."
nohup chartmuseum --port=$CM_PORT --storage="local" --storage-local-rootdir=$HELM_LOCAL_REPO >/dev/null 2>&1 &
echo $! > $ROOT_DIR/CM_PID.txt

wget https://get.helm.sh/helm-v3.12.3-linux-amd64.tar.gz
tar xvfz /tmp/helm-v3.12.3-linux-amd64.tar.gz
sudo mv linux-amd64/helm /usr/local/bin/helm
popd

TAR_VERSION=v0.10.3
echo "Downloading and installing helm-push ${TAR_VERSION} ..."
TAR_FILE=helm-push-${TAR_VERSION}.tar.gz
HELM_PLUGINS=$(helm env HELM_PLUGINS)
mkdir -p $HELM_PLUGINS/helm-push
pushd $HELM_PLUGINS/helm-push
wget https://nexus.o-ran-sc.org/content/repositories/thirdparty/chartmuseum/helm-push/$TAR_VERSION/$TAR_FILE
tar zxvf $TAR_FILE >/dev/null
rm $TAR_FILE
helm repo remove local
helm repo add local http://localhost:8879
popd

sudo apt-get install make -y
(cd smo && make all)

# Creating namespace
kubectl create ns smo

# Installing helm charts
helm install --debug oran-smo local/smo --namespace smo

# Show the installed pods
kubectl get po -n smo