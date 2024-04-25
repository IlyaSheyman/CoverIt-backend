FROM openjdk:17-jdk
COPY target/*.jar image-server.jar
ENTRYPOINT ["java", "-jar", "/image-server.jar"]