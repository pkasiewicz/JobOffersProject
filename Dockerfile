FROM eclipse-temurin:17-jre-alpine
COPY /target/joboffers.jar /joboffers.jar
ENTRYPOINT ["java","-jar","/joboffers.jar"]