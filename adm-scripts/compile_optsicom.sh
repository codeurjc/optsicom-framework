#!/bin/bash

printf "Building Optsicom"
cd es.optsicom.lib.parent || exit 1
mvn --batch-mode package -DskipTests || exit 1
