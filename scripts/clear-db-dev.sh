#!/bin/bash
docker stop idr-db
docker rm idr-db
docker volume ls
./run-dev.sh
