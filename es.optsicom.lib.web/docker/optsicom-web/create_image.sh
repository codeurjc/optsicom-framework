#!/bin/bash

cp ../../target/es.optsicom.lib.web-*SNAPSHOT.jar ./optsicom-web.jar

docker build -t codeurjc/optsicom-web .

rm ./optsicom-web.jar
