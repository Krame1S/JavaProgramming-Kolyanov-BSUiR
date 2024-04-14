FROM openjdk:17-jdk-alpine

# Copy the wait-for-it script into the container
COPY wait-for-it.sh /usr/local/bin/wait-for-it.sh
RUN chmod +x /usr/local/bin/wait-for-it.sh

# Copy your application's JAR file
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Use the wait-for-it script to wait for the database
CMD /usr/local/bin/wait-for-it.sh db:3306 --timeout=60 --strict -- echo "Database is ready" && java -jar /app.jar
