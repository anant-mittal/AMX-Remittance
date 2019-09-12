#!/bin/sh

source config/setvars.sh

mvn package -pl ms-jax -am -DskipTests -DcreateChecksum=true

sshpass -p $UIS_PASS scp ms-jax/target/ms-jax-0.0.1-SNAPSHOT.jar $UIS_USER@$UIS_HOST:~/jax/ms-jax2

sshpass -p $UIS_PASS ssh -o StrictHostKeyChecking=no $UIS_USER@$UIS_HOST 'kill -9 $(cat /var/run/ms-jax2/ms-jax2.pid)'
sshpass -p $UIS_PASS ssh -o StrictHostKeyChecking=no $UIS_USER@$UIS_HOST ‘/etc/init.d/ms-jax2 restart &'
