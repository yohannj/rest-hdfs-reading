#!/bin/sh

docker image rm drill --force && docker build -t drill .
