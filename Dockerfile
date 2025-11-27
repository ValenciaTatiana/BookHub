# Build stage
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY .mvn .mvn
COPY mvnw mvnw
COPY pom.xml pom.xml
# Asegura permisos de ejecución del wrapper
RUN chmod +x mvnw
RUN ./mvnw -q -B -DskipTests dependency:go-offline
COPY src src
RUN ./mvnw -q -B -DskipTests clean package

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app
ENV PORT=8080
COPY --from=build /app/target/bookhub-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
