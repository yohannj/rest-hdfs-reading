#!/bin/bash

/etc/init.d/ssh start

sleep 20
$SPARK_HOME/sbin/start-slave.sh spark://spark-master:7077

tail -f /dev/null
