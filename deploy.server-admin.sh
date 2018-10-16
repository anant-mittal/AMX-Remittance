#!/bin/sh

source config/setvars.sh

#bash setup.sh

#mvn clean

# short command
mvn -T 4 package -pl server-admin -am -DskipTests

sshpass -p $ADMIN_PASS scp server-admin/target/server-admin-0.0.1-SNAPSHOT.jar $ADMIN_USER@$ADMIN_HOST:~/jax/server-admin

sshpass -p $ADMIN_PASS ssh -o StrictHostKeyChecking=no $ADMIN_USER@$ADMIN_HOST 'kill -9 $(cat /var/run/server-admin/server-admin.pid)'
sshpass -p $ADMIN_PASS ssh -o StrictHostKeyChecking=no $ADMIN_USER@$ADMIN_HOST '/etc/init.d/server-admin restart &'
