FROM maven:3.8.5-jdk-8 AS builder
WORKDIR /app/
COPY pom.xml .
RUN mvn -e -B dependency:resolve
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:8-jre-alpine
COPY --from=builder /app/target/*.jar /root.jar
EXPOSE 8090 
CMD ["java", "-jar", "/root.jar"]