#!/usr/bin/env bash

# Go to father project folder
cd ../../..
docker build -t codeurjc/optsicom-web -f es.optsicom.lib.web/docker/optsicom-web/Dockerfile .
