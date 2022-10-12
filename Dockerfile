FROM openjdk:17-jdk-slim

# copy the packaged jar file into our docker image
COPY target/backend-0.0.1-SNAPSHOT.jar /backend.jar
# set the startup command to execute the jar
CMD ["java", "-jar", "/backend.jar"]