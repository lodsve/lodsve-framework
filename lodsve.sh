#!/bin/bash

# Help info function
help(){
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
./docs/logo.sh
case "$1" in
  'versions')
    shell/shell/versions.sh $2
	;;
  'deploy')
    shell/shell/deploy.sh $2
	;;
  *)
    help
esac