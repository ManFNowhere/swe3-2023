#!/usr/bin/env bash
source local/config.txt || exit 1

echo "ASSEMBLE: target/webapp.war" 
cp local/server.properties build/WEB-INF/classes/ 
date > build/assembled.txt

jar -cf "target/webapp.war" -C build . && 
	echo "ASSEMBLE: success" || { echo "ASSEMBLE: failure"; exit 1; }

