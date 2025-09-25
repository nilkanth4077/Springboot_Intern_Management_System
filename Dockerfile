FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/spring_internship_management_system-0.0.1-SNAPSHOT.jar ims.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "ims.jar"]