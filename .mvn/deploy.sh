#!/bin/bash

export GPG_TTY=$(tty)
mvn clean deploy -Dmaven.test.skip=false -P $1
unset GPG_TTY
