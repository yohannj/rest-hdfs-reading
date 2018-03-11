#!/bin/bash

/etc/init.d/ssh start

sleep 20
$DRILL_HOME/bin/drillbit.sh start
sleep 20
curl -X POST -H "Content-Type: application/json" -d '{"name":"hdfs", "config": {"type": "file", "enabled": true, "connection": "hdfs://namenode:9000/", "workspaces": { "root": { "location": "/", "writable": false, "defaultInputFormat": null}}, "formats": null}}' http://drill:8047/storage/hdfs.json

tail -f /dev/null
