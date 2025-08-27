@echo off
echo ============================================
echo    CONFIGURANDO PROXY IPv6 PARA SUPABASE
echo ============================================
echo.

cd /d "c:\Rafael\DesenvolvimentoTI\BikeHubb\Backend\bikehubb"

echo 1. Habilitando IPv6 no Windows...
netsh interface ipv6 set global randomizeidentifiers=disabled
netsh interface ipv6 set privacy state=disabled

echo 2. Configurando DNS publico (Cloudflare e Google)...
netsh interface ip set dns "Wi-Fi" static 1.1.1.1 primary
netsh interface ip add dns "Wi-Fi" 8.8.8.8 index=2

echo 3. Tentando configurar IPv6...
netsh interface ipv6 set global randomizeidentifiers=disabled

echo 4. Executando aplicacao com configuracoes especiais...
set JAVA_OPTS=-Djava.net.preferIPv6Addresses=false -Djava.net.preferIPv4Stack=true -Djava.net.useSystemProxies=true

.\mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Djava.net.preferIPv6Addresses=false -Djava.net.preferIPv4Stack=true -Djava.net.useSystemProxies=true -Dfile.encoding=UTF-8"

echo.
echo ============================================
echo IMPORTANTE: Execute como ADMINISTRADOR
echo Se nao funcionar, use: .\run-local.bat
echo ============================================
pause
