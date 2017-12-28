#!/bin/sh

source config/setvars.sh

#bash setup.sh

mvn clean

# short command
mvn -T 4 package -pl ms-postman -am -DskipTests


sshpass -p $JAX_PASS scp ms-postman/target/ms-postman-0.0.1-SNAPSHOT.jar $JAX_USER@$JAX_HOST:~/jax/ms-postman

sshpass -p $JAX_PASS ssh -o StrictHostKeyChecking=no $JAX_USER@$JAX_HOST 'kill -9 $(cat /var/run/ms-postman/ms-postman.pid)'
sshpass -p $JAX_PASS ssh -o StrictHostKeyChecking=no $JAX_USER@$JAX_HOST '/etc/init.d/ms-postman restart &'

#curl -T "target/app.war" "http://$TOMCAT_USER:$TOMCAT_PASSWORD@$TOMCAT_HOST/manager/text/deploy?path=/&update=true"
#curl -T "target/app.war" "http://tomcat:tomcat@localhost:8080/manager/text/deploy?path=/app&update=true"
