FROM openjdk:17-jdk-alpine
COPY target/medical-history-0.0.1-SNAPSHOT.jar xchel-medical-history-app.jar
ENTRYPOINT [ "java", "-jar", "xchel-medical-history-app.jar" ]