FROM openjdk:17-jdk-slim

VOLUME /tmp

ARG JAR_FILE=target/casino-0.1.jar

ADD ${JAR_FILE} app.jar


ENTRYPOINT ["java","-jar","/app.jar"]