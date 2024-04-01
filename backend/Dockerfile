### Stage 1: Build the Spring Boot application with Maven ###
FROM maven:3.8.4-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build the application
COPY src ./src
RUN mvn package -DskipTests

### Stage 2: Create a smaller image with the built JAR file ###
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the previous stage to the current image
COPY --from=build /app/target/crm-api-0.0.1-SNAPSHOT.jar ./crm-api.jar

# Expose the port your Spring Boot application is running on (change 8080 to your desired port)
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "crm-api.jar"]
