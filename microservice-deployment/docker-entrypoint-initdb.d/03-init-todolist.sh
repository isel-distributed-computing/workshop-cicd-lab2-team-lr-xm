#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	CREATE DATABASE todolist;
	\c todolist;
	CREATE TABLE to_dos (
   		id SERIAL PRIMARY KEY,
   		description varchar(255),
   		username varchar(255)
	);
EOSQL