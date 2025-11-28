FROM maven:3.10.1-eclipse-temurin-25 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENV DB_HOST=db
ENV DB_PORT=5432
ENV DB_NAME=cars_db
ENV DB_USER=postgres
ENV DB_PASSWORD=postgres

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
