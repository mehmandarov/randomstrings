#!/usr/bin/env bash

set -e

echo "Using CRaC enabled JDK."

cd ../../../
docker build -t randomstrings-quarkus-crac:builder -f src/main/docker/quarkus/Dockerfile.crac-jvm .
echo "==> Done building, attempting to RUN"
docker run -d --privileged --rm -i --name=randomstrings-quarkus-crac --ulimit nofile=1024 -p 8080:8080 -v $(pwd)/../../../target:/opt/mnt -e FLAG=$1 randomstrings-quarkus-crac:builder
echo "==> RUN successful"
#siege -c 1 -r 150000 -b http://localhost:8080/api/rnd
echo "Please wait during checkpoint creation..."
sleep 20
docker commit --change='ENTRYPOINT ["/opt/app/entrypoint.sh"]' $(docker ps -qf "name=randomstrings-quarkus-crac") randomstrings-quarkus-crac:checkpoint
docker kill $(docker ps -qf "name=randomstrings-quarkus-crac")
echo "Done creating an image with checkpoint."