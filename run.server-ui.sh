#!/bin/sh

source config/setvars.sh

#bash setup.sh

#mvn clean

# short command
mvn -T 4 package -pl server-ui -am -DskipTests

java -jar server-ui/target/server-ui-0.0.1-SNAPSHOT.jar -Dserver.port=8080
