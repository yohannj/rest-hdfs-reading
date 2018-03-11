#!/bin/bash

/etc/init.d/ssh start

$SPARK_HOME/sbin/start-master.sh

tail -f /dev/null
