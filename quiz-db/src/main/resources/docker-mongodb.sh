#!/usr/bin/env bash

# Script that setups and runs a mongo database inside a docker container
# Running the script for the first time pulls a mongo image from docker repository.
# If a container with the same name already exists, running this script will start/restart the container.
# Mongo default configuration is specified by the parameters below.
# Docker is required for this script to work

# parameters #
CONTAINER_NAME=quiz_mongo
DB_PORT=27017
DB_NAME=quizme
DB_USER=beby
DB_PASSWORD=beby

if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
    if [ ! "$(docker ps -aq -f name=$CONTAINER_NAME -f status=exited)" ]; then
        echo "Stopping mongo container"
		docker stop $CONTAINER_NAME
	fi
	echo "Starting mongo container"
	docker start $CONTAINER_NAME
else
	echo "Creating & starting postgres container"
    docker run -d \
         --name $CONTAINER_NAME \
         -p $DB_PORT:$DB_PORT \
         -e MONGO_INITDB_DATABASE=$DB_NAME \
         -e MONGO_INITDB_ROOT_USERNAME=$DB_USER \
         -e MONGO_INITDB_ROOT_PASSWORD=$DB_PASSWORD \
         mongo:3.4
fi

