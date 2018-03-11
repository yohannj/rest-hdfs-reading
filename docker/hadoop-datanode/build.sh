#!/bin/sh

docker image rm hadoop-datanode --force && docker build -t hadoop-datanode .
