# 베이스 이미지 설정
FROM openjdk:17-jdk-slim


ARG JAR_FILE=build/libs/*.jar

ARG PROFILES
ARG ENV
COPY ${JAR_FILE} app.jar


ENTRYPOINT ["java","-Dspring.profiles.active=${PROFILES}","-Dspring.profiles.env=${ENV}","-jar","/app.jar"]
