### BUILD image
FROM maven:3-jdk-11 as builder
# create app folder for sources
RUN mkdir -p /build
WORKDIR /build
#COPY settings.xml /root/.m2/
COPY pom.xml /build
#Download all required dependencies into one layer
RUN mvn dependency:go-offline -B -q
#Copy source code
COPY src /build/src
# Build application
RUN mvn package -q

FROM openjdk:11-slim as runtime
EXPOSE 8888
#Set app home folder
ENV APP_HOME /app
#Possibility to set JVM options (https://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html)
ENV JAVA_OPTS="-Dspring.profiles.active=prod"
#Create base app folder
RUN mkdir $APP_HOME
#Create folder to save configuration files
RUN mkdir $APP_HOME/config
#Create folder with application logs
RUN mkdir $APP_HOME/log
VOLUME $APP_HOME/log
VOLUME $APP_HOME/config
WORKDIR $APP_HOME
#Copy executable jar file from the builder image
COPY --from=builder /build/target/*.jar app.jar
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]
#Second option using shell form:
#ENTRYPOINT exec java $JAVA_OPTS -jar app.jar $0 $@