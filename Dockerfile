# Step 1: Build Stage
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -X

# Step 2: Runtime Stage
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/producer-0.0.1-SNAPSHOT.jar ./producer-app.jar

# Step 3: Specify the default command
ENTRYPOINT ["java", "-jar", "producer-app.jar"]