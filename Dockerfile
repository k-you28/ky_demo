# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /workspace/job-application-tracker

COPY job-application-tracker/ ./

RUN chmod +x gradlew
RUN ./gradlew --no-daemon clean bootJar -x test

FROM eclipse-temurin:17-jre-jammy AS runtime
WORKDIR /app

RUN groupadd --system spring && useradd --system --gid spring spring
RUN mkdir -p /app/data && chown -R spring:spring /app

COPY --from=build /workspace/job-application-tracker/build/libs/*.jar /app/app.jar

USER spring

EXPOSE 8081
VOLUME ["/app/data"]

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
