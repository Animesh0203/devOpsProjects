# Stage 1: Build with Maven
FROM openjdk:17-jdk-slim AS builder

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set the working directory
WORKDIR /app

# Copy the source code and pom.xml to the container
COPY ./src ./src
COPY ./pom.xml .

# Build the project using Maven
RUN mvn clean package -DskipTests

# Stage 2: Create the final image
FROM openjdk:17-jdk-slim

# Copy the built JAR file from the builder stage
ARG JAR_FILE=/app/target/app.jar
COPY --from=builder ${JAR_FILE} app.jar

# Expose the application port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app.jar"]
