#!/bin/sh

for i in spark-base spark-master spark-slave spark-historyserver; do
    echo Building $i
    ( cd $i && ./build.sh)
done
