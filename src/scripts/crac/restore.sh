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

# Get the current timestamp in milliseconds
START_COMMAND_TIMESTAMP_MS=$(python3 -c "from time import time; print(int(round(time()*1000)))")

echo "=> Restoring from Checkpoint: New container. Who dis?"

docker run --privileged --cap-add CHECKPOINT_RESTORE --cap-add SETPCAP --cap-add SYS_RESOURCE --rm -p 8080:8080 \
  -v "$(pwd)"/crac-files:/opt/crac-files --name randomstrings-${RUNTIME}-crac \
  -e START_COMMAND_TIMESTAMP_MS=${START_COMMAND_TIMESTAMP_MS} randomstrings-${RUNTIME}-crac:checkpoint