#!/bin/sh

source config/setvars.sh

echo $JAX_PASS

bash setup.sh

mvn clean

#mvn package -DskipTests

#mvn -pl amx-lib clean install -DskipTests
#mvn -pl core clean install -DskipTests
#mvn -pl ms-meta clean install -DskipTests
#mvn -pl ms-user clean install -DskipTests
#mvn -pl ms-exchangerate clean install -DskipTests
#mvn -pl ms-jax clean install -DskipTests
#mvn -pl jax-client clean install -DskipTests

mvn -T 4 package -pl ms-jax -am -DskipTests
mvn -T 4 package -pl jax-client -am -DskipTests

sshpass -p $JAX_PASS scp ms-jax/target/ms-jax-0.0.1-SNAPSHOT.jar $JAX_USER@$JAX_HOST:~/jax

sshpass -p $JAX_PASS ssh -o StrictHostKeyChecking=no $JAX_USER@$JAX_HOST kill -9 $(cat /var/run/jaxapp/jaxapp.pid)
sshpass -p $JAX_PASS ssh -o StrictHostKeyChecking=no $JAX_USER@$JAX_HOST /etc/init.d/jaxapp restart &

# use this command
# nohup java -jar /web/server.jar &
