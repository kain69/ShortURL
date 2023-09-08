# Build stage
FROM gradle:latest AS BUILD
WORKDIR /usr/app/
COPY . .
RUN gradle build -x test

# Package stage
FROM openjdk:17-jdk-alpine
ENV JAR_NAME=ShortURL-0.0.1-SNAPSHOT.jar
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME .
EXPOSE 8080
ENTRYPOINT exec java -jar $APP_HOME/build/libs/$JAR_NAME