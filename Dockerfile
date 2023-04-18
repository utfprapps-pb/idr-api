# BUILD
FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/idr

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
COPY report report

# clean up the file
RUN sed -i 's/\r$//' mvnw
# create package
RUN /bin/sh mvnw package -DskipTests

# DELIVERY
FROM openjdk:17
COPY --from=build /workspace/idr/target/api-0.1.jar api.jar
ENTRYPOINT ["java", "-jar", "api.jar"]