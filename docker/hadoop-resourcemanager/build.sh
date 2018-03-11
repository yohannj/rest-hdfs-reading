#!/bin/sh

docker image rm hadoop-resourcemanager --force && docker build -t hadoop-resourcemanager .
