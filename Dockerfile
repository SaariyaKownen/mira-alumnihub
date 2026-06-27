FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:resolve
COPY src ./src
RUN ./mvnw clean package -DskipTests
EXPOSE 10000
ENTRYPOINT ["sh", "-c", "java -jar target/portal-0.0.1-SNAPSHOT.jar --server.port=${PORT:-10000}"]