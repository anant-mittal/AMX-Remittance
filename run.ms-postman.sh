#!/bin/sh

source config/setvars.sh

#bash setup.sh

#mvn clean

# short command
mvn -T 4 package -pl ms-postman -am -DskipTests

java -jar ms-postman/target/ms-postman-0.0.1-SNAPSHOT.jar -Dserver.port=8082
