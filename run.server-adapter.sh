
mvn clean package -pl sw-adapter -am -Dtnt=kwt -Denv=appd -Dvzn=0-master -DskipTests -Dmaven.test.skip
mvn clean package -pl sw-updater -am -Dtnt=kwt -Denv=appd -Dvzn=0-master -DskipTests -Dmaven.test.skip
rsync -a  sw-updater/dist-sw-adapter/ sw-adapter/dist-sw-adapter/
java -jar sw-adapter-appd-kwt-java8.jar
