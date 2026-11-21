# BUILD
FROM eclipse-temurin:25-jdk-alpine AS build
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
FROM eclipse-temurin:25-jre-alpine
COPY --from=build /workspace/idr/target/api-0.1.jar api.jar
ENTRYPOINT ["java", "-jar", "api.jar"]