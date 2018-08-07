#!/bin/sh

source config/setvars.sh

bash server-payg/setup.sh

#mvn clean

# short command
mvn -T 4 package -pl server-payg -am -DskipTests

java -jar server-payg/target/server-payg-0.0.1-SNAPSHOT.jar -Dserver.port=8083

