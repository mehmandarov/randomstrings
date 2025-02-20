#!/usr/bin/env bash

set -e

echo "=> Using CRaC enabled JDK."

echo "==> Starting application build"
cd ../../../
docker build -t randomstrings-quarkus-crac:builder -f src/main/docker/quarkus/Dockerfile-CRaC.jvm .
echo "===> Done building application"

echo "==> Attempting to start the container and warm-up the app."
# Check if the container exists and remove it if it does
if docker ps -a --format '{{.Names}}' | grep -q "^randomstrings-quarkus-crac$"; then
    echo "===> Removing existing container: randomstrings-quarkus-crac"
    docker rm -f randomstrings-quarkus-crac
fi
docker run -d --privileged --rm -i --name=randomstrings-quarkus-crac --ulimit nofile=1024 -p 8080:8080 -v "$(pwd)"/../../../target:/opt/mnt -e FLAG=$1 randomstrings-quarkus-crac:builder
echo "===> Application startup was successful"

echo "==> JVM and application warmup"
# Explanation of the one-liner below:
# * seq 1 20000: Generates numbers from 1 to 20,000.
# * xargs -I{} -P 10: Runs 10 parallel requests at a time (-P 10).
# * wget -qO- http://localhost:8080/api/rnd > /dev/null: Fetches the endpoint without outputting anything.
# seq 1 20000 | xargs -I{} -P 10 curl -s http://localhost:8080/api/rnd > /dev/null
seq 1 10 | xargs -I{} -P 10 curl -s http://localhost:8080/api/rnd
echo "===> Done running JVM and application warmup"

echo "==> Creating checkpoint. Please wait..."
sleep 20
docker commit --change='ENTRYPOINT ["/opt/app/entrypoint.sh"]' "$(docker ps -qf 'name=randomstrings-quarkus-crac')" randomstrings-quarkus-crac:checkpoint
docker kill "$(docker ps -qf 'name=randomstrings-quarkus-crac')"
echo "===> Done creating a container image with checkpoint."