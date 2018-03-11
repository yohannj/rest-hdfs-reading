#!/bin/bash

/etc/init.d/ssh start

$HADOOP_HOME/bin/yarn --config $HADOOP_CONF_DIR historyserver

tail -f /dev/null
