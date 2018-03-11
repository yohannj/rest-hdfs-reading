#!/bin/sh

docker image rm hadoop-historyserver --force && docker build -t hadoop-historyserver .
