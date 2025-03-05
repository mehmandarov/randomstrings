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

docker build -t randomstrings-${RUNTIME}:regular -f src/main/docker/${RUNTIME}/Dockerfile.jvm .

echo "=> Starting WITHOUT a restore: New container. Who dis?"
# Get the current timestamp in milliseconds
START_COMMAND_TIMESTAMP_MS=$(($(date +'%s * 1000 + %-N / 1000000')))
docker run --rm -p 8080:8080 --name randomstrings-${RUNTIME}-crac \
  -e START_COMMAND_TIMESTAMP_MS=${START_COMMAND_TIMESTAMP_MS} randomstrings-${RUNTIME}:regular