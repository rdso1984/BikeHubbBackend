# Multi-stage build otimizado para Fly.io
FROM openjdk:21-jdk-slim as builder

WORKDIR /app
COPY . .
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests -Dspring.profiles.active=production

FROM openjdk:21-jre-slim

# Instalar ferramentas essenciais e configurar IPv6
RUN apt-get update && apt-get install -y \
    curl \
    iputils-ping \
    dnsutils \
    && rm -rf /var/lib/apt/lists/*

# Habilitar IPv6 
RUN echo 'net.ipv6.conf.all.disable_ipv6 = 0' >> /etc/sysctl.conf
RUN echo 'net.ipv6.conf.default.disable_ipv6 = 0' >> /etc/sysctl.conf

WORKDIR /app

# Copiar JAR otimizado
COPY --from=builder /app/target/*.jar app.jar

# Criar usuário não-root para segurança
RUN addgroup --system spring && adduser --system spring --ingroup spring
RUN chown spring:spring app.jar
USER spring:spring

# Configurar JVM para Fly.io com IPv6
ENV JAVA_OPTS="-Djava.net.preferIPv6Addresses=true -Djava.net.preferIPv4Stack=false -Xmx400m -Xms200m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Expor porta
EXPOSE 8080

# Comando otimizado para Fly.io
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.profiles.active=production"]
