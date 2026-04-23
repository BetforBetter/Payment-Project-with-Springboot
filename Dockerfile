FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src
RUN ./mvnw -q -DskipTests package
CMD ["java", "-jar", "target/payment-service-1.0.0.jar"]
