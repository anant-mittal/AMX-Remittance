#!/bin/sh

source config/setvars.sh

bash setup.sh

mvn clean

# short command
mvn -T 4 package -pl payment-service -am -DskipTests

sshpass -p $PG_PASS scp payment-service/target/payment-service-0.0.1-SNAPSHOT.jar $PG_USER@$PG_HOST:~/jax/payment-service

sshpass -p $PG_PASS ssh -o StrictHostKeyChecking=no $PG_USER@$PG_HOST 'kill -9 $(cat /var/run/payment-service/payment-service.pid)'
sshpass -p $PG_PASS ssh -o StrictHostKeyChecking=no $PG_USER@$PG_HOST '/etc/init.d/payment-service restart &'
