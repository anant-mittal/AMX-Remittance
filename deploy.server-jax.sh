#!/bin/sh

source config/setvars.sh

echo $JAX_PASS

bash setup.sh

mvn clean

mvn package -DskipTests

sshpass -p $JAX_PASS scp ms-jax/target/ms-jax-0.0.1-SNAPSHOT.jar $JAX_USER@$JAX_HOST:~/jax

# sshpass -p "$JAX_PASS" ssh -o StrictHostKeyChecking=no $JAX_USER@$JAX_HOST 'nohup /usr/lib/jvm/java-8-oracle/bin/java -Djax.logging-dir=/home/devenvironment/jax/ -Dserver-port=$JAX_PORT -jar /home/devenvironment/jax/ms-jax-0.0.1-SNAPSHOT.jar &'
sshpass -p $JAX_PASS ssh -o StrictHostKeyChecking=no $JAX_USER@$JAX_HOST '/etc/init.d/jaxapp restart &'


# use this command
# nohup java -jar /web/server.jar &
