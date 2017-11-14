FROM maven:3.5.0-jdk-8 AS time.api.jar
WORKDIR /usr/src/time
COPY pom.xml .
RUN mvn -B -f pom.xml -s /usr/share/maven/ref/settings-docker.xml dependency:resolve
COPY . .
RUN mvn -B -s /usr/share/maven/ref/settings-docker.xml package -DskipTests

FROM openjdk:8-jre-alpine
COPY --from=time.api.jar /usr/src/trees/target/time.api-1.0-SNAPSHOT.jar time.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","trees.jar"]