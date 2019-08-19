#!/bin/bash

# Help info function
help() {
  echo "--------------------------------------------------------------------------"
  echo ""
  echo "usage: ./lodsve.sh [versions | deploy]"
  echo ""
  echo "-versions    Update lodsve-framework versions."
  echo "-deploy      Deploy lodsve-framework to maven repository"
  echo ""
  echo "--------------------------------------------------------------------------"
}

# Start
./.mvn/logo.sh
case "$1" in
'versions')
  ./.mvn/versions.sh $2
  ;;
'deploy')
  ./.mvn/deploy.sh $2
  ;;
*)
  help
  ;;
esac
