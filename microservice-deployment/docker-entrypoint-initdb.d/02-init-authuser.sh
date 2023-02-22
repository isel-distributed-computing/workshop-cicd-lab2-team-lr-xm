#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	CREATE DATABASE users;
	\c users;
	CREATE TABLE users (
    	id SERIAL PRIMARY KEY,
    	username VARCHAR(255),
    	salted_password VARCHAR(255)
	);
EOSQL