# Build stage
FROM maven:3.8-openjdk-17-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the source code
COPY . .

# Build the application (this will run your tests as well)
RUN mvn clean install -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the app will run on
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
