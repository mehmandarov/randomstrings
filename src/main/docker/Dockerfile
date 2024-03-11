FROM eclipse-temurin:19

COPY target/randomstrings*.jar /randomstrings.jar

# Run the web service on container startup.
EXPOSE 9080
CMD ["java", "-jar", "/randomstrings.jar"]