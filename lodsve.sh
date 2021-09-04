#!/bin/bash
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#


# Help info function
help(){
  echo "--------------------------------------------------------------------------"
  echo ""
  echo "usage: ./lodsve.sh [versions | deployOss | deployGithub | deployThirdParty]"
  echo ""
  echo "versions          Update lodsve-framework versions."
  echo "deployOss         Deploy lodsve-framework to maven repository"
  echo "deployGithub      Deploy lodsve-framework to github repository"
  echo "deployThirdParty  Deploy lodsve-framework to Third-Party repository"
  echo ""
  echo "--------------------------------------------------------------------------"
}


# Start
./.mvn/logo.sh
case "$1" in
  'versions')
    ./.mvn/versions.sh $2
	;;
  'deployOss')
    ./.mvn/deploy.sh release-oss $2
	;;
  'deployGithub')
    ./.mvn/deploy.sh release-github $2
	;;
  'deployThirdParty')
    ./.mvn/deploy.sh release-third-party $2
	;;
  *)
    help
esac
