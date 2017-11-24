#!/bin/sh

bash setup.sh

mvn clean

mvn package -DskipTests

scp ms-jax/target/ms-jax-0.0.1-SNAPSHOT.jar devenvironment@10.6.42.95:~/jax

sshpass -p "amx@123" ssh -o StrictHostKeyChecking=no devenvironment@10.6.42.95 '/usr/lib/jvm/java-8-oracle/bin/java -Djax.logging-dir=/home/devenvironment/jax/ -Dserver-port=8081 -jar /home/devenvironment/jax/ms-jax-0.0.1-SNAPSHOT.jar'