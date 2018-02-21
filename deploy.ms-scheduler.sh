#!/bin/sh

source config/setvars.sh

#bash setup.sh

#mvn clean

# short command
mvn -T 4 package -pl ms-scheduler -am -DskipTests

sshpass -p $JAX_PASS scp ms-scheduler/target/ms-scheduler-0.0.1-SNAPSHOT.jar $JAX_USER@$JAX_HOST:~/jax/ms-scheduler

sshpass -p $JAX_PASS ssh -o StrictHostKeyChecking=no $JAX_USER@$JAX_HOST 'kill -9 $(cat /var/run/ms-scheduler/ms-scheduler.pid)'
sshpass -p $JAX_PASS ssh -o StrictHostKeyChecking=no $JAX_USER@$JAX_HOST '/etc/init.d/ms-scheduler restart &'
