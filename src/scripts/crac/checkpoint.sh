#!/usr/bin/env bash

set -e

case $(uname -m) in
    arm64)   url="https://cdn.azul.com/zulu/bin/zulu21.28.89-ca-crac-jdk21.0.0-linux_aarch64.tar.gz" ;;
    *)       url="https://cdn.azul.com/zulu/bin/zulu21.28.89-ca-crac-jdk21.0.0-linux_x64.tar.gz" ;;
esac

echo "Using CRaC enabled JDK"

cd ../../../
#mvn -f pom-quarkus.xml clean package
docker build -t randomstrings-crac:builder -f src/main/docker/quarkus/Dockerfile.crac-jvm --build-arg CRAC_JDK_URL=$url .
echo "==> Done building, attempting to RUN"
docker run -d --privileged --rm -i --name=randomstrings-crac --ulimit nofile=1024 -p 8080:8080 -v $(pwd)/../../../target:/opt/mnt -e FLAG=$1 randomstrings-crac:builder
echo "==> RUN successful"
#siege -c 1 -r 100000 -b http://localhost:8080/api/rnd
echo "Please wait during checkpoint creation..."
sleep 10
docker commit --change='ENTRYPOINT ["/opt/app/entrypoint.sh"]' $(docker ps -qf "name=randomstrings-crac") randomstrings-crac:checkpoint
docker kill $(docker ps -qf "name=randomstrings-crac")