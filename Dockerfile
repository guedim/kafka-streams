FROM openjdk:8-jdk-alpine
RUN apk update && apk add libc6-compat
VOLUME /tmp
ADD target/kafka-streams-0.0.1-SNAPSHOT.jar  app.jar
ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]