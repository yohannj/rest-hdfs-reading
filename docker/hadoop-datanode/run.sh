#!/bin/bash

/etc/init.d/ssh start

sleep 20
$HADOOP_HOME/bin/hdfs --config $HADOOP_CONF_DIR datanode

tail -f /dev/null
