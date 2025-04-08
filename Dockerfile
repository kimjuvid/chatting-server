FROM gradle:8.4-jdk17 AS builder
WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY src src

RUN gradle build -x test --no-daemon

FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
