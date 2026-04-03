# Multi-stage build para Render - Java 21
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

# Copiar wrapper do Maven e configurações
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

# Dar permissão de execução ao mvnw
RUN chmod +x mvnw

# Download das dependências (cache layer)
RUN ./mvnw dependency:go-offline -B

# Copiar código fonte
COPY src src

# Build da aplicação
RUN ./mvnw clean package -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar JAR do builder
COPY --from=builder /build/target/*.jar app.jar

# Criar usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring
RUN chown spring:spring app.jar
USER spring:spring

# Configurar JVM para ambiente de produção
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Expor porta
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Comando de inicialização
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.profiles.active=render"]
