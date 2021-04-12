# select parent image
FROM maven:3.6-openjdk-15

# copy the source tree and the pom.xml to our new container
COPY ./ ./

# package our application code
RUN mvn clean package -DskipTests

#EXPOSE 8080
# set the startup command to execute the jar
CMD ["java", "-jar", "target/app-0.0.1-SNAPSHOT.jar"]
