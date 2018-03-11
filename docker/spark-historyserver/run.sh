#!/bin/bash

/etc/init.d/ssh start

sleep 20
$SPARK_HOME/sbin/start-history-server.sh

tail -f /dev/null
