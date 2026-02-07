FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean package -DskipTests
EXPOSE 8080
CMD ["java", "-jar", "target/taskbuddy_backend_monolithic_jwt-0.0.1.jar"]
