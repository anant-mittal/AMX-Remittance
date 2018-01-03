#!/bin/sh

cd ext-jars
#cd payment-service/ext-jars
mvn install:install-file -Dfile=kent_bah.jar -DgroupId=com.knet.bah -DartifactId=knet_bah -Dversion=1.0 -Dpackaging=jar
#mvn install:install-file -Dfile=KNET_WinEPTS_API.jar -DgroupId=com.knet -DartifactId=KNET_WinEPTS_API -Dversion=1.0 -Dpackaging=jar
#mvn install:install-file -Dfile=knet.jar -DgroupId=com.knet -DartifactId=knet -Dversion=1.0 -Dpackaging=jar
#mvn install:install-file -Dfile=e24Pipe.jar -DgroupId=com.e24Pipe -DartifactId=e24Pipe -Dversion=1.0 -Dpackaging=jar
#mvn install:install-file -Dfile=knet_test.jar -DgroupId=com.knet.test -DartifactId=knet.test -Dversion=1.0 -Dpackaging=jar
#mvn install:install-file -Dfile=bouncycastle-1.20.jar -DgroupId=bouncycastle-1.20 -DartifactId=bouncycastle-1.20 -Dversion=1.0 -Dpackaging=jar
#mvn install:install-file -Dfile=cryptix32.jar -DgroupId=cryptix32 -DartifactId=cryptix32 -Dversion=1.0 -Dpackaging=jar
#mvn install:install-file -Dfile=bcprov-jdk15-145.jar -DgroupId=bcprov-jdk15-145 -DartifactId=bcprov-jdk15-145 -Dversion=1.0 -Dpackaging=jar
