#!/usr/bin/env bash

[ -z "$1" ] && name_image="codeurjc/optsicom-web:latest" || name_image="$1"

# Go to father project folder
cd ../../..
docker build -t "${name_image}" -f es.optsicom.lib.web/docker/optsicom-web/Dockerfile .
