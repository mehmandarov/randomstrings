#!/usr/bin/env bash

set -e

#docker run --cap-add CHECKPOINT_RESTORE --cap-add SYS_ADMIN --rm -p 8080:8080 --name quotes-crac randomstrings-crac:checkpoint
docker run --cap-add CHECKPOINT_RESTORE --cap-add SETPCAP --rm -p 8080:8080 --name quotes-crac randomstrings-crac:checkpoint