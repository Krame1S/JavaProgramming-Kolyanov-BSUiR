FROM openjdk:17-jdk-alpine

# Install bash
RUN apk add --no-cache bash

# Copy your application's JAR file
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Copy the db-config.json file into the container
COPY db-config.json /config/db-config.json

# Start the application
CMD java -jar /app.jar
