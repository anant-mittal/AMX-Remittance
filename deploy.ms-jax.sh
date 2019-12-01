#!/bin/sh

source config/setvars.sh

mvn package -pl ms-jax -am -DskipTests -DcreateChecksum=true

sshpass -p $UIS_PASS scp ms-jax/target/ms-jax-0.0.1-SNAPSHOT.jar $UIS_USER@$UIS_HOST:~/jax/ms-jax

sshpass -p $UIS_PASS ssh -o StrictHostKeyChecking=no $UIS_USER@$UIS_HOST 'kill -9 $(cat /var/run/ms-jax/ms-jax.pid)'
sshpass -p $UIS_PASS ssh -o StrictHostKeyChecking=no $UIS_USER@$UIS_HOST '/etc/init.d/ms-jax restart &'
