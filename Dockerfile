FROM maven:3.9.6-eclipse-temurin-21 as build
WORKDIR /usr/app
ADD . /usr/app
RUN --mount=type=cache,target=/root/.m2 mvn -f /usr/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
COPY --from=build /usr/app/target/atmosphere-service-products.jar /app/atmosphere-service-products.jar
ENTRYPOINT java -jar /app/atmosphere-service-products.jar
