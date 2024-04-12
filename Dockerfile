FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
FROM openjdk:17-jdk-alpine

# Install dumb-init
RUN apk add --no-cache dumb-init

# Define the JAR file argument
ARG JAR_FILE=target/*.jar

# Copy the JAR file into the container
COPY ${JAR_FILE} app.jar

# Use dumb-init as the entrypoint
ENTRYPOINT ["dumb-init", "--"]

# Specify the command to run your application
CMD ["java","-jar","/app.jar"]
