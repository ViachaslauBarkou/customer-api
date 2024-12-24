FROM maven:3.8.1-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /customer-api
COPY target/customer-api-0.0.1-SNAPSHOT.jar customer-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "customer-api.jar"]