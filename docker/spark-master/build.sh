#!/bin/sh

docker image rm spark-master --force && docker build -t spark-master .
