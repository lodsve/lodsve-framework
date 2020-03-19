#!/bin/bash

mvn clean deploy -Dmaven.test.skip=false -P $1 -Dgpg.passphrase=$2