#!/usr/bin/env bash

set -e

echo "New phone. Who dis?"

# SETPCAP => Modify process capabilities.
# https://docs.docker.com/engine/reference/run/#runtime-privilege-and-linux-capabilities
docker run --privileged --cap-add CHECKPOINT_RESTORE --cap-add SETPCAP --cap-add SYS_RESOURCE --rm -p 8080:8080 --name randomstrings-quarkus-crac randomstrings-quarkus-crac:checkpoint