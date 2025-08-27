# Multi-stage build otimizado para Render
FROM eclipse-temurin:21-jdk-alpine as builder

# Instalar Maven
RUN apk add --no-cache maven

WORKDIR /app
COPY . .

# Build da aplicação usando Maven diretamente
RUN mvn clean package -DskipTests -Dspring.profiles.active=render

FROM eclipse-temurin:21-jre-alpine

# Instalar ferramentas essenciais
RUN apk add --no-cache curl

WORKDIR /app

# Copiar JAR otimizado
COPY --from=builder /app/target/*.jar app.jar

# Criar usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring
RUN chown spring:spring app.jar
USER spring:spring

# Configurar JVM para Render
ENV JAVA_OPTS="-Xmx400m -Xms200m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Expor porta
EXPOSE 8080

# Comando otimizado para Render
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.profiles.active=render"]
