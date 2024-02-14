#!/bin/bash
base=$(cat local/config.txt)
name="$base-java"
manager="$base-manager"
[ "$1" != "" ] && { name=$1; }

curl -s --netrc-file $HOME/.netrc "https://informatik.hs-bremerhaven.de/$manager/
text/undeploy?path/=$name"
