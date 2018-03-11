#!/bin/sh

for i in hadoop-base hadoop-datanode hadoop-historyserver hadoop-namenode hadoop-nodemanager hadoop-resourcemanager; do
    echo Building $i
    ( cd $i && ./build.sh)
done
