#!/bin/sh

source config/setvars.sh

bash server-payg/setup.sh

#mvn clean

# short command
mvn -T 4 package -pl server-payg -am -DskipTests -P dev
mvn -T 4 package -pl server-payg -am -DskipTests -P prod

sshpass -p $PG_PASS scp server-payg/target/server-payg-0.0.1-SNAPSHOT-dev.jar $PG_USER@$PG_HOST:~/jax/server-payg
sshpass -p $PG_PASS scp server-payg/target/server-payg-0.0.1-SNAPSHOT-prod.jar $PG_USER@$PG_HOST:~/jax/server-payg

sshpass -p $PG_PASS ssh -o StrictHostKeyChecking=no $PG_USER@$PG_HOST 'kill -9 $(cat /var/run/server-payg/server-payg.pid)'
sshpass -p $PG_PASS ssh -o StrictHostKeyChecking=no $PG_USER@$PG_HOST '/etc/init.d/server-payg restart &'
