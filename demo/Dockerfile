FROM openjdk:11
VOLUME /tmp
EXPOSE 8282
ADD ./target/app-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]