#!/bin/bash

# Help info function
help(){
  echo "--------------------------------------------------------------------------"
  echo ""
  echo "usage: ./lodsve.sh [versions | deployOss | deployGithub]"
  echo ""
  echo "versions          Update lodsve-framework versions."
  echo "deployOss         Deploy lodsve-framework to maven repository"
  echo "deployGithub      Deploy lodsve-framework to github repository"
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
  *)
    help
esac