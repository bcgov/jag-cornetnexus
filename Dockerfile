FROM openjdk:11-jre-slim

COPY ./jag-cornetnexus-application/target/jag-cornetnexus-application.jar jag-cornetnexus-application.jar

ENTRYPOINT ["java","-jar","/jag-cornetnexus-application.jar"]
