#!/bin/sh

docker image rm spark-base --force && docker build -t spark-base .
