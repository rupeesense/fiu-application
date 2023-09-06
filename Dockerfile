# Use an official Java runtime as a parent image
FROM openjdk:11-jre-slim
# Set the working directory in the container to /app
WORKDIR /app

# Copy the JAR file from the local fiu-api/target directory to the working directory in the Docker image
COPY fiu-api/target/fiu-api-*.jar /app/fiu-application.jar
COPY scripts/start.sh /app/start.sh
RUN chmod +x /app/start.sh
# Define the command to run the application
CMD ["sh", "/app/start.sh"]
