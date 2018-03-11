#!/bin/sh

docker image rm hadoop-nodemanager --force && docker build -t hadoop-nodemanager .
