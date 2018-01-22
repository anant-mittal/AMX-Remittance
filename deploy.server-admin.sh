#!/bin/sh

source config/setvars.sh

#bash setup.sh

#mvn clean

# short command
mvn -T 4 package -pl server-admin -am -DskipTests

sshpass -p $JAX_PASS scp server-admin/target/server-admin-0.0.1-SNAPSHOT.jar $JAX_USER@$JAX_HOST:~/jax/server-admin

sshpass -p $JAX_PASS ssh -o StrictHostKeyChecking=no $JAX_USER@$JAX_HOST 'kill -9 $(cat /var/run/server-admin/server-admin.pid)'
sshpass -p $JAX_PASS ssh -o StrictHostKeyChecking=no $JAX_USER@$JAX_HOST '/etc/init.d/server-admin restart &'
