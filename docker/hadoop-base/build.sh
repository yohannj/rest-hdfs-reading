#!/bin/sh

docker image rm hadoop-base --force && docker build -t hadoop-base .
