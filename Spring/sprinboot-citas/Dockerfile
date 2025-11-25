# stage 1: build
FROM maven:3.9.2-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
# si tienes wrapper y .mvn puedes copiarlo también:
COPY .mvn .mvn
RUN mvn -B -f pom.xml -ntp dependency:go-offline

COPY src ./src
RUN mvn -B -f pom.xml -ntp package -DskipTests

# stage 2: runtime
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
