# 1. Build Stage
FROM gradle:7.6-jdk17 AS build
WORKDIR /home/gradle/project

# Gradle 빌드 파일과 src만 복사
COPY build.gradle settings.gradle gradle.properties ./ 
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
