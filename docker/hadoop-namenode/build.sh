#!/bin/sh

docker image rm hadoop-namenode --force && docker build -t hadoop-namenode .
