#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	CREATE DATABASE notification;
	\c notification;
	CREATE TABLE eventlog
	(
    id SERIAL PRIMARY KEY,
    username VARCHAR(255),
    "action" VARCHAR(255)
	);
EOSQL

#psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" -c "$(cat /docker-entrypoint-initdb.d/notifications/01-createdb.sql)"

#psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "notification" -c "$(cat /docker-entrypoint-initdb.d/notifications/02-createtable.sql)"
