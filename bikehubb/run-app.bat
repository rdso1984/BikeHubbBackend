@echo off
echo Executando BikeHubb Application com configuracoes otimizadas...
echo.

cd /d "c:\Rafael\DesenvolvimentoTI\BikeHubb\Backend\bikehubb"

echo Configurando variaveis de ambiente para IPv4...
set JAVA_OPTS=-Djava.net.preferIPv4Stack=true -Djava.net.preferIPv6Addresses=false

echo Executando aplicacao Spring Boot...
.\mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Djava.net.preferIPv4Stack=true -Djava.net.preferIPv6Addresses=false"

pause
