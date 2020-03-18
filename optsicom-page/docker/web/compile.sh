#!/bin/bash
[ -z "$1" ] && name_image="optsicomweb" || name_image="$1"

echo Building front...
cd ../../frontend
npx ng build --prod --output-path ../es.optsicom.lib.web/src/main/resources/static

echo Building backend...
cd ../../../es.optsicom.lib.parent
mvn --batch-mode package -DskipTests
cd ../../optsicom-page/es.optsicom.lib.web

echo Building Docker with name: $name_image
cp target/es.optsicom.lib.web-0.0.1-SNAPSHOT.jar ../../docker/optsicomweb.jar
cd ../../docker

docker build -t $name_image .