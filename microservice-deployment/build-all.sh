#!/bin/bash

cd ../authusers
mvn clean package -Dmaven.test.skip=true
docker build -t authuser .

cd ../todolist
mvn clean package -Dmaven.test.skip=true
docker build -t todolist .

cd ../notifications
mvn clean package -Dmaven.test.skip=true
docker build -t notifications .