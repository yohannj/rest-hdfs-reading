#!/bin/sh

docker image rm ignite --force && docker build -t ignite .
