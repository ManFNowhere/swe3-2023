#!/usr/bin/env bash

YOURUSER="tmahdi"
DEPLOYPASSWORD="BlFbFqmXVM73CH5Wrp8t"

mkdir -p local
echo "user=$YOURUSER
webapp=docker-$YOURUSER-java
manager=docker-$YOURUSER-manager
baseurl=https://informatik.hs-bremerhaven.de" > local/config.txt

echo "service_password=$DEPLOYPASSWORD" > local/server.properties

echo "machine informatik.hs-bremerhaven.de login manager password $DEPLOYPASSWORD
machine localhost login manager password $DEPLOYPASSWORD" >local/netrc

echo "config.txt und  server.properties angelegt ($YOURUSER)"


