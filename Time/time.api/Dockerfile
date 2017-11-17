#FROM openjdk:8-jre-alpine
FROM hypriot/rpi-java

EXPOSE 8080
ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/time/time.api.jar"]

ADD target/lib           /usr/share/time/lib
ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/time/time.api.jar