FROM maven:3.8.5-jdk-8 AS builder
WORKDIR /app/
COPY pom.xml .
RUN mvn -e -B dependency:resolve
COPY src ./src
RUN mvn package -DskipTests

FROM tomcat:9-jdk8
WORKDIR /usr/local/tomcat/webapps/
COPY --from=builder /app/target/*.war ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]