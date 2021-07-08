#!/usr/bin/env bash
set -ex

[ -z "$1" ] && exit 1 || VERSION="$1"

#Maven deploy

cd ./es.optsicom.lib.parent

mvn -B versions:set -DgenerateBackupPoms=false -DnewVersion="$VERSION"
mvn -B -DperformRelease=true clean package
mvn -B -DperformRelease=true clean deploy

# Github commit

cd ../

git config --global user.email "optsicom@gmail.com"
git config --global user.name "Optsicom"
git add . || true
git commit -m "Update version to $VERSION" || true
git push || true