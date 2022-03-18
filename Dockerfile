FROM openjdk:11-jre-slim

COPY ./jag-cornetnexus-application/target/cornetnexus-application.jar cornetnexus-application.jar

ENTRYPOINT ["java","-jar","/cornetnexus-application.jar"]
