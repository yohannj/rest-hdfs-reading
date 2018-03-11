#!/bin/sh

docker image rm spark-slave --force && docker build -t spark-slave .
