#!/usr/bin/env bash

set -e

echo "=> Restoring from Checkpoint: New container. Who dis?"

# SETPCAP => Modify process capabilities.
# https://docs.docker.com/engine/reference/run/#runtime-privilege-and-linux-capabilities
docker run --privileged --cap-add CHECKPOINT_RESTORE --cap-add SETPCAP --cap-add SYS_RESOURCE --rm -p 8080:8080 -v "$(pwd)"/crac-files:/opt/crac-files --name randomstrings-quarkus-crac randomstrings-quarkus-crac:checkpoint