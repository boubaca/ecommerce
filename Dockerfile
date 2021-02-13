FROM openjdk:8-jdk-alpine
LABEL maintainer="barry.boubacar@b-hitech.com"
COPY /target/auth-course-0.0.1-SNAPSHOT.jar /home/auth-course-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","/home/auth-course-0.0.1-SNAPSHOT.jar"]