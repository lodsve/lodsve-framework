#!/bin/bash

mvn clean deploy -Dmaven.test.skip=false -P release -Dgpg.passphrase=$1