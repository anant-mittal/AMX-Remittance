#!/bin/sh

cd ext-jars
mvn install:install-file -Dfile=ojdbc6.jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.4 -Dpackaging=jar
