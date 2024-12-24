FROM maven:3.8.1-openjdk-17-slim AS build
WORKDIR /customer-api
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests && ls -l target/

FROM openjdk:17-jdk-slim
WORKDIR /customer-api
COPY --from=build /customer-api/target/customer-api-0.0.1-SNAPSHOT.jar customer-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "customer-api.jar"]