#!/bin/sh

docker image rm spark-historyserver --force && docker build -t spark-historyserver .
