#!/bin/sh

source config/setvars.sh

#bash setup.sh

#mvn clean

# short command
mvn -T 4 package -pl server-scheduler -am -DskipTests

sshpass -p $JAX_PASS scp server-scheduler/target/server-scheduler-0.0.1-SNAPSHOT.jar $JAX_USER@$JAX_HOST:~/jax/server-scheduler

sshpass -p $JAX_PASS ssh -o StrictHostKeyChecking=no $JAX_USER@$JAX_HOST 'kill -9 $(cat /var/run/server-scheduler/server-scheduler.pid)'
sshpass -p $JAX_PASS ssh -o StrictHostKeyChecking=no $JAX_USER@$JAX_HOST '/etc/init.d/server-scheduler restart &'
