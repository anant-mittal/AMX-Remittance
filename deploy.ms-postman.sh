#!/bin/sh

source config/setvars.sh

#bash setup.sh

mvn clean

# short command
mvn -T 4 package -pl ms-postman -am -DskipTests

sshpass -p $PM_PASS scp ms-postman/target/ms-postman-0.0.1-SNAPSHOT.jar $PM_USER@$PM_HOST:~/jax/ms-postman

sshpass -p $PM_PASS ssh -o StrictHostKeyChecking=no $PM_USER@$PM_HOST 'kill -9 $(cat /var/run/ms-postman/ms-postman.pid)'
sshpass -p $PM_PASS ssh -o StrictHostKeyChecking=no $PM_USER@$PM_HOST '/etc/init.d/ms-postman restart &'
