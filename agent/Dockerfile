FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/agent-*.jar app.jar
COPY src/main/resources/config/prompt.txt /app/config/prompt.txt
ENTRYPOINT ["java", "-jar", "app.jar"]
