#!/usr/bin/env bash

set -e

# Default runtime
RUNTIME="helidon"

# Parse command-line arguments
while [[ "$#" -gt 0 ]]; do
  case "$1" in
    --runtime)
      if [[ "$2" == "helidon" || "$2" == "quarkus" ]]; then
        RUNTIME="$2"
        shift 2
      else
        echo "Invalid runtime. Allowed values: helidon, quarkus"
        exit 1
      fi
      ;;
    *)
      echo "Unknown option: $1"
      exit 1
      ;;
  esac
done

# Get the current timestamp in milliseconds (workaround using python...)
START_COMMAND_TIMESTAMP_MS=$(($(date +'%s * 1000 + %-N / 1000000')))


echo "=> Using CRaC enabled JDK with runtime: $RUNTIME."

echo "==> Starting application container build"
docker build -t randomstrings-${RUNTIME}-crac:builder -f src/main/docker/${RUNTIME}/Dockerfile-CRaC.jvm .
echo "> Done building application"

echo "==> Attempting to start the application container."
docker run -d --privileged --rm -i --name=randomstrings-${RUNTIME}-crac --ulimit nofile=1024 -p 8080:8080 \
  -v "$(pwd)"/crac-files:/opt/crac-files -e START_COMMAND_TIMESTAMP_MS=${START_COMMAND_TIMESTAMP_MS} randomstrings-${RUNTIME}-crac:builder
echo "> Application startup was successful"

echo "==> Creating checkpoint. Please wait..."
sleep 20

# Check if the container exists before taking the snapshot
CONTAINER_ID=$(docker ps -qf "name=randomstrings-${RUNTIME}-crac")

if [ -n "$CONTAINER_ID" ]; then
    echo "> Container found. Taking snapshot..."
    docker commit --change='ENTRYPOINT ["/opt/app/entrypoint.sh"]' "$CONTAINER_ID" randomstrings-${RUNTIME}-crac:checkpoint
    docker kill "$CONTAINER_ID"
    echo "> Done creating a container image with checkpoint."
else
    echo "> No running container found. Skipping snapshot. Remove contents of crac-files/ folder to take a new snapshot."
fi
