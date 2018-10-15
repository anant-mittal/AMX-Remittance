#!/bin/sh

source config/setvars.sh
#bash setup.sh

echo "-p ${SD_PASS} ${SD_USER}@${SD_HOST}"

mvn clean package -pl ms-worker -am -DskipTests
sshpass -p $SD_PASS scp ms-worker/target/ms-worker-0.0.1-SNAPSHOT.jar $SD_USER@$SD_HOST:~/jax/ms-worker
sshpass -p $SD_PASS scp ms-worker/target/ms-worker-0.0.1-SNAPSHOT.jar $SD_USER@$SD_HOST:~/jax/ms-worker2


sshpass -p $SD_PASS ssh -o StrictHostKeyChecking=no $SD_USER@$SD_HOST 'kill -9 $(cat /var/run/ms-worker/ms-worker.pid)'
sshpass -p $SD_PASS ssh -o StrictHostKeyChecking=no $SD_USER@$SD_HOST '/etc/init.d/ms-worker restart &'

sshpass -p $SD_PASS ssh -o StrictHostKeyChecking=no $SD_USER@$SD_HOST 'kill -9 $(cat /var/run/ms-worker2/ms-worker2.pid)'
sshpass -p $SD_PASS ssh -o StrictHostKeyChecking=no $SD_USER@$SD_HOST '/etc/init.d/ms-worker2 restart &'
