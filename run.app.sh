#!/bin/sh

source config/setvars.sh
#mvn clean
# short command
mvn package -pl $1 -am -DskipTests

java -jar $1/target/$1-0.0.1-SNAPSHOT.jar -Dserver.port=8080
