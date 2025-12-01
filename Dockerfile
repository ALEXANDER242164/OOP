## Multi-stage Dockerfile para construir y ejecutar la aplicación Spring Boot

FROM maven:3.8.8-eclipse-temurin-17 AS builder
WORKDIR /build

# Copiamos pom y código fuente
COPY pom.xml .
COPY src ./src

# Empaquetamos la aplicación (sin tests para acelerar)
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiamos el JAR construido desde la etapa anterior. Usamos patrón para que no importe la versión
COPY --from=builder /build/target/*.jar app.jar

# Railway asigna el puerto dinámicamente via variable de entorno PORT
EXPOSE ${PORT:-8081}

# La aplicación usa la variable PORT de Railway, con fallback a 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]
