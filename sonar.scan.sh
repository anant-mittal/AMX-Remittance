mvn clean verify sonar:sonar -DskipTests

# In some situation you may want to run sonar:sonar goal as a dedicated step. Be sure to use install as first step for multi-module projects
mvn clean install -DskipTests
mvn sonar:sonar  -DskipTests -Dmaven.test.failure.ignore=true

# Specify the version of sonar-maven-plugin instead of using the latest. See also 'How to Fix Version of Maven Plugin' below.
mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.3.0.603:sonar
