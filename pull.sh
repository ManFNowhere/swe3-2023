#!/bin/bash


#pull git
git pull

#give permission to exec bin
chmod 777 bin/*.sh

#undeploy project
./bin/undeploy.sh
#run project
./bin/build.sh
# ./bin/clean.sh