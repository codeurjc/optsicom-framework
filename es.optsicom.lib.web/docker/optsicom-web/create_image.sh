#!/bin/bash

cp ../../target/optsicom-web-*.jar ./optsicom-web.jar

docker build -t codeurjc/optsicom-web .

rm ./optsicom-wb.jar
