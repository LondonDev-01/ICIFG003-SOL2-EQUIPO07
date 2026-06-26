FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/reservas-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENV DB_HOST=localhost DB_PORT=3306 DB_NAME=reservas_db DB_USER=root DB_PASSWORD=1234
ENTRYPOINT ["java", "-jar", "app.jar"]
