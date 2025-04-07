# 1단계: 빌드용 이미지
FROM gradle:8.4-jdk17 AS builder
WORKDIR /app

# 프로젝트 파일 복사 (필요한 것만)
COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY src src

# 의존성 캐시 먼저
RUN gradle build -x test --no-daemon

# 2단계: 실행용 이미지
FROM eclipse-temurin:17-jdk
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 포트 오픈 (Spring 기본 8080)
EXPOSE 8080

# 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]
