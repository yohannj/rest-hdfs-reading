#!/bin/bash

/etc/init.d/ssh start

sleep 20
$IGNITE_HOME/bin/ignite.sh

tail -f /dev/null
