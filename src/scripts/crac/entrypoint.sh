#!/bin/bash

CRAC_FILES_DIR=`eval echo ${CRAC_FILES_DIR}`
mkdir -p $CRAC_FILES_DIR

if [ -z "$(ls -A $CRAC_FILES_DIR)" ]; then
  if [ "$FLAG" = "-r" ]; then
    echo 128 > /proc/sys/kernel/ns_last_pid; java -XX:CRaCCheckpointTo=$CRAC_FILES_DIR -jar /opt/app/quarkus-run.jar
  else
    echo 128 > /proc/sys/kernel/ns_last_pid; java -XX:CRaCCheckpointTo=$CRAC_FILES_DIR -jar /opt/app/quarkus-run.jar&
    sleep 5
    jcmd /opt/app/quarkus-run.jar JDK.checkpoint
  fi
  sleep infinity
else
  java -XX:CRaCRestoreFrom=$CRAC_FILES_DIR -XX:+UnlockExperimentalVMOptions -XX:+IgnoreCPUFeatures&
  PID=$!
  trap "kill $PID" SIGINT SIGTERM
  wait $PID
fi
