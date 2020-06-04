#!/bin/bash

[ "$1" == "install_dependencies" ] && DEPENDENCIES=true || DEPENDENCIES=false

printf "Building Optsicom Web Front"
cd ../es.optsicom.lib.web/src/web || exit 1
[ ${DEPENDENCIES} == true ] && (npm install || exit 1)
npm run build-prod || exit 1
cd ../../.. || exit 1

printf "Building Optsicom Web Backend"
cd es.optsicom.lib.parent || exit 1
mvn --batch-mode package -DskipTests || exit 1
