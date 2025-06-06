#!/bin/bash

CRAC_FILES_DIR=$(eval echo "${CRAC_FILES_DIR}")
mkdir -p "$CRAC_FILES_DIR"

# This is a workaround for resolving PID conflicts.
# More info: https://docs.azul.com/core/crac/crac-debugging.html#resolving-pid-conflicts
echo 128 > /proc/sys/kernel/ns_last_pid;

if [ -z "$(ls -A "$CRAC_FILES_DIR")" ]; then
  echo "=> Hello from the container >> CHECKPOINT: No checkpoint found. Creating one."
  java -XX:CRaCEngine=warp -XX:CRaCCheckpointTo="$CRAC_FILES_DIR" -jar randomstrings.jar&
  sleep 10
  jcmd randomstrings.jar JDK.checkpoint
  sleep infinity
else
  echo "=> Hello from the container >> CHECKPOINT: Restoring from checkpoint."
  java -XX:CRaCEngine=warp -XX:CRaCRestoreFrom="$CRAC_FILES_DIR" -XX:+UnlockExperimentalVMOptions&
  PID=$!
  trap 'kill "$PID"' SIGINT SIGTERM
  wait $PID
fi
