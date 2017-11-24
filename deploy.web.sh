#!/bin/sh

mvn clean

mvn package -DskipTests

cd server-ui

mv target/server-ui-0.0.1-SNAPSHOT.war target/app.war

curl -T "target/app.war" "http://amxtomcatuser:amxtomcatpass@10.6.42.95:8080/manager/text/deploy?path=/&update=true"
#curl -T "target/app.war" "http://tomcat:tomcat@localhost:8080/manager/text/deploy?path=/app&update=true"