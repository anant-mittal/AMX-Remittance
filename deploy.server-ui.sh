#!/bin/sh

source config/setvars.sh

# short command
mvn -T 4 package -pl server-ui -am -DskipTests

sshpass -p $UIS_PASS scp server-ui/target/server-ui-0.0.1-SNAPSHOT.jar $UIS_USER@$UIS_HOST:~/jax/server-ui

sshpass -p $UIS_PASS ssh -o StrictHostKeyChecking=no $UIS_USER@$UIS_HOST 'kill -9 $(cat /var/run/server-ui/server-ui.pid)'
sshpass -p $UIS_PASS ssh -o StrictHostKeyChecking=no $UIS_USER@$UIS_HOST '/etc/init.d/server-ui restart &'
