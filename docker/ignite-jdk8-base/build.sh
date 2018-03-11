#!/bin/sh

docker image rm ignite-jdk8-base --force && docker build -t ignite-jdk8-base .
