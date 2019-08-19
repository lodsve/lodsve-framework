#!/bin/bash

mvn clean deploy -Dmaven.test.skip=false -P release-oss -Dgpg.passphrase=$1