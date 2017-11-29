#!/bin/sh

source config/setvars.sh

bash setup.sh

mvn clean

mvn package -DskipTests

cd server-ui

mv target/server-ui-0.0.1-SNAPSHOT.war target/app.war

echo $TOMCAT_USER
echo $TOMCAT_PASSWORD
echo $TOMCAT_HOST

curl -T "target/app.war" "http://$TOMCAT_USER:$TOMCAT_PASSWORD@$TOMCAT_HOST/manager/text/deploy?path=/&update=true"
#curl -T "target/app.war" "http://tomcat:tomcat@localhost:8080/manager/text/deploy?path=/app&update=true"

cd ../
scp ms-jax/target/ms-jax-0.0.1-SNAPSHOT.jar devenvironment@10.6.42.95:~/jax

ssh devenvironment@10.6.42.95  '/usr/lib/jvm/java-8-oracle/bin/java -Djax.logging-dir=/home/devenvironment/jax/ -Dserver-port=8081 -jar /home/devenvironment/jax/ms-jax-0.0.1-SNAPSHOT.jar'
