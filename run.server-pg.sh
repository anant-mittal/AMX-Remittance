#!/bin/sh

source config/setvars.sh

bash payment-service/setup.sh

#mvn clean

# short command
mvn -T 4 package -pl payment-service -am -DskipTests

java -jar payment-service/target/payment-service-0.0.1-SNAPSHOT.jar -Dserver.port=8083

