FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar image-server.jar
ENTRYPOINT ["java","-jar","stats-server.jar"]