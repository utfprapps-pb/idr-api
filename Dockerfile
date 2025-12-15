# BUILD
FROM eclipse-temurin:23-jdk-alpine as build
WORKDIR /workspace/idr

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# clean up the file
RUN sed -i 's/\r$//' mvnw
# create package
RUN /bin/sh mvnw package -DskipTests

# DELIVERY
FROM openjdk:23-ea-jdk
COPY --from=build /workspace/idr/target/api-0.1.jar api.jar
ENTRYPOINT ["java", "-jar", "api.jar"]