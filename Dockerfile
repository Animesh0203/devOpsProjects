# Use OpenJDK base image
FROM eclipse-temurin:17-jdk-alpine

# Copy the JAR file from the Maven build
COPY target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
