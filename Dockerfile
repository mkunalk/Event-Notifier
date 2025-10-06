FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./
RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw clean install -DskipTests

FROM openjdk:21-slim

WORKDIR /app
#add firebase configuration file in the root folder and rename the file in next line
ADD firebaseConfig.json ./
COPY --from=build /app/target/notification-0.0.1-SNAPSHOT.jar ./

ENTRYPOINT ["java","-jar","notification-0.0.1-SNAPSHOT.jar"]
