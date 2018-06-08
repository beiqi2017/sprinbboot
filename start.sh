#!/usr/bin/env bash

CURRENT=$(cd $(dirname $0);pwd)

nohup java -jar $CURRENT/demo-*.jar &>/dev/null &