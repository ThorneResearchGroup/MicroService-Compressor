FROM alpine
RUN apk update
RUN apk add openjdk17-jre
EXPOSE 80
EXPOSE 443
ADD target/compressor-1.0-SNAPSHOT.jar server.jar
ENTRYPOINT ["java", "-jar", "server.jar"]