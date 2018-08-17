#!/bin/sh

source config/setvars.sh

#bash setup.sh

mvn clean

# short command
mvn -T 4 package -pl ms-logger -am -DskipTests


sshpass -p $LOGGER_PASS scp ms-logger/target/ms-logger-0.0.1-SNAPSHOT.jar $LOGGER_USER@$LOGGER_HOST:~/jax/ms-logger

sshpass -p $LOGGER_PASS ssh -o StrictHostKeyChecking=no $LOGGER_USER@$LOGGER_HOST 'kill -9 $(cat /var/run/ms-logger/ms-logger.pid)'
sshpass -p $LOGGER_PASS ssh -o StrictHostKeyChecking=no $LOGGER_USER@$LOGGER_HOST '/etc/init.d/ms-logger restart &'

#curl -T "target/app.war" "http://$TOMCAT_USER:$TOMCAT_PASSWORD@$TOMCAT_HOST/manager/text/deploy?path=/&update=true"
#curl -T "target/app.war" "http://tomcat:tomcat@localhost:8080/manager/text/deploy?path=/app&update=true"
