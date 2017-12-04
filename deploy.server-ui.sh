#!/bin/sh

source config/setvars.sh

bash setup.sh

echo "VARIABLES"

mvn clean

mvn -pl boot-utils clean install
mvn -pl amx-lib clean install -DskipTests
mvn -pl jax-client clean install -DskipTests
mvn -pl server-ui clean install -DskipTests

#mvn package -DskipTests

cd server-ui

mv target/server-ui-0.0.1-SNAPSHOT.war target/app.war

echo "VARIABLES"
echo $TOMCAT_USER
echo $TOMCAT_PASSWORD
echo $TOMCAT_HOST

curl -T "target/app.war" "http://$TOMCAT_USER:$TOMCAT_PASSWORD@$TOMCAT_HOST/manager/text/deploy?path=/&update=true"
#curl -T "target/app.war" "http://tomcat:tomcat@localhost:8080/manager/text/deploy?path=/app&update=true"
