#!/bin/bash

/etc/init.d/ssh start

if [ "`ls -A /hadoop/dfs/name`" == "" ]; then
  echo "Formatting namenode name directory: /hadoop/dfs/name"
  $HADOOP_HOME/bin/hdfs --config $HADOOP_CONF_DIR namenode -format rest-hdfs-reading
fi

$HADOOP_HOME/bin/hdfs --config $HADOOP_CONF_DIR namenode

tail -f /dev/null
