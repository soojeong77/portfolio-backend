# 1. Build Stage
FROM gradle:7.6-jdk17 AS build
WORKDIR /home/gradle/project

# Gradle 빌드 파일 복사 (gradle.properties 없이)
COPY build.gradle settings.gradle ./
COPY src ./src

# 빌드
RUN gradle build -x test --no-daemon --stacktrace

# 2. Run Stage
FROM eclipse-temurin:17-jre
WORKDIR /app

# 빌드된 JAR 복사
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
