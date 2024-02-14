#!/usr/bin/env bash
source local/config.txt || exit 1
mkdir -p build target
echo "PREPARE"
cp -r app/* build
