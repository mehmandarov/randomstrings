#!/usr/bin/env bash

set -e

echo "=> Using CRaC enabled JDK."

echo "==> Starting application container build"
docker build -t randomstrings-quarkus-crac:builder -f src/main/docker/quarkus/Dockerfile-CRaC.jvm .
echo "> Done building application"

echo "==> Attempting to start the application container."
docker run -d --privileged --rm -i --name=randomstrings-quarkus-crac --ulimit nofile=1024 -p 8080:8080 -v "$(pwd)"/crac-files:/opt/crac-files randomstrings-quarkus-crac:builder
echo "> Application startup was successful"

echo "==> Creating checkpoint. Please wait..."
sleep 20

# Check if the container exists before taking the snapshot
CONTAINER_ID=$(docker ps -qf 'name=randomstrings-quarkus-crac')

if [ -n "$CONTAINER_ID" ]; then
    echo "> Container found. Taking snapshot..."
    docker commit --change='ENTRYPOINT ["/opt/app/entrypoint.sh"]' "$CONTAINER_ID" randomstrings-quarkus-crac:checkpoint
    docker kill "$CONTAINER_ID"
    echo "> Done creating a container image with checkpoint."
else
    echo "> No running container found. Skipping snapshot. Remove contents of crac-files/ folder to take a new snapshot."
fi