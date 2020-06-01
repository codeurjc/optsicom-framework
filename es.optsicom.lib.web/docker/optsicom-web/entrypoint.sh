#!/bin/bash

function mysqlDB() {
cat>optsicom.properties<<EOF
db = mysql_local
mysql_local = mysql
mysql_local.host = ${OPTSICOM_DB_HOST}
mysql_local.port = ${OPTSICOM_DB_PORT}
mysql_local.schema = ${OPTSICOM_DB_SCHEMA}
mysql_local.user = ${OPTSICOM_DB_USER}
mysql_local.password = ${OPTSICOM_WEB_SECRET}
EOF
}

function derbyDB() {
cat>optsicom.properties<<EOF
db = derby_local
derby_local = derby
derby_local.workspace = derby_exp_repo
derby_local.dataDir = derby_data
derby_local.schema = optsicom
EOF
}

# Database Variables
[ -z "${OPTSICOM_DB_MODE}" ] && OPTSICOM_DB_MODE="derby" 
[ -z "${OPTSICOM_DB_HOST}" ] && OPTSICOM_DB_HOST="localhost"
[ -z "${OPTSICOM_DB_PORT}" ] && OPTSICOM_DB_PORT=3306
[ -z "${OPTSICOM_DB_SCHEMA}" ] && OPTSICOM_DB_SCHEMA=optsicom
[ -z "${OPTSICOM_DB_USER}" ] && OPTSICOM_DB_USER=root
[ -z "${OPTSCIOM_DB_SECRET}" ] && OPTSCIOM_DB_SECRET="0pts1c0m"

# API Variables
[ -z "${OPTSICOM_API_USER}" ] && OPTSICOM_API_USER="researcher"
[ -z "${OPTSICOM_API_SECRET}" ] && OPTSICOM_API_SECRET="0pts1c0m"

printf "\n  ======================================="
printf "\n  =          INPUT VARIABLES            ="
printf "\n  ======================================="
printf "\n"

printf "\n  Config Database:"
printf "\n    - Database Mode: %s" "${OPTSICOM_DB_MODE}"
printf "\n    - Database Host: %s" "${OPTSICOM_DB_HOST}"
printf "\n    - Database Port: %s" "${OPTSICOM_DB_PORT}"
printf "\n    - Database Schema: %s" "${OPTSICOM_DB_SCHEMA}"
printf "\n    - Database User: %s" "${OPTSICOM_DB_USER}"
printf "\n"
printf "\n  Config Web:"
printf "\n    - API User: %s" "${OPTSICOM_API_USER}"

case "${OPTSICOM_DB_MODE}" in
  mysql)
    mysqlDB
    ;;

  *)
    derbyDB
    ;;
esac

printf "\n"
printf "\n  ======================================="
printf "\n  =            OPTSICOM WEB             ="
printf "\n  ======================================="
printf "\n"

if [ "${OPTSICOM_DB_MODE}" == "mysql" ]; then
    printf "\n  Waiting database in '%s' host and '%s' port..." "${OPTSICOM_DB_HOST}" "${OPTSICOM_DB_PORT}"

    while ! nc -z ${OPTSICOM_DB_HOST} ${OPTSICOM_DB_PORT}; do
        printf "\n  Waiting database in '%s' host and '%s' port..." "${OPTSICOM_DB_HOST}" "${OPTSICOM_DB_PORT}"
        sleep 2
    done
fi

java -jar optsicom-web.jar
