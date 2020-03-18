#!/bin/bash

function mysqlDB() {
cat>optsicom.properties<<EOF
db = mysql_local
mysql_local = mysql
mysql_local.host = ${DB_HOST:-localhost}
mysql_local.port = ${DB_PORT:-3306}
mysql_local.schema = ${DB_SCHEMA:-optsicom}
mysql_local.user = ${DB_USER:-root}
mysql_local.password = ${DB_PASS:-0pts1c0m}
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

if [ "$DB_HOST" ]; then
    mysqlDB
else
    derbyDB
fi

exec "$@"