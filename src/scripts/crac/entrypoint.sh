#!/bin/bash

CRAC_FILES_DIR=`eval echo ${CRAC_FILES_DIR}`
mkdir -p $CRAC_FILES_DIR

if [ -z "$(ls -A $CRAC_FILES_DIR)" ]; then
  echo "==> CHECKPOINT: No checkpoint found. Creating one."
  if [ "$FLAG" = "-r" ]; then
    java -XX:CRaCCheckpointTo=$CRAC_FILES_DIR -jar /opt/app/quarkus-run.jar
  else
    java -XX:CRaCCheckpointTo=$CRAC_FILES_DIR -jar /opt/app/quarkus-run.jar&
    sleep 5
    jcmd /opt/app/quarkus-run.jar JDK.checkpoint
  fi
  sleep infinity
else
  echo "==> CHECKPOINT: Restoring from checkpoint."
  echo "==> CHECKPOINT: DEBUG: \n $(ls -a $CRAC_FILES_DIR)"
  #java -XX:CRaCRestoreFrom=$CRAC_FILES_DIR&
  java -XX:CRaCRestoreFrom=$CRAC_FILES_DIR -XX:+UnlockExperimentalVMOptions -XX:+IgnoreCPUFeatures&
  PID=$!
  trap "kill $PID" SIGINT SIGTERM
  wait $PID
fi
