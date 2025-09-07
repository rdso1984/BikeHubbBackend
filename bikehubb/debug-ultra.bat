@echo off
setlocal

echo 🚨 SOLUÇÃO ULTRA-DEFINITIVA - MAVEN DEBUG
echo ==========================================

REM Definir JAVA_HOME explicitamente
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

REM Matar processos conflitantes
echo 🛑 Parando processos Java...
taskkill /f /im java.exe /t 2>nul
timeout /t 3 /nobreak >nul

REM Limpar arquivos problemáticos
echo 🧹 Limpando cache problemático...
if exist "%USERPROFILE%\.java.policy" del "%USERPROFILE%\.java.policy" 2>nul
if exist "%USERPROFILE%\.keystore" del "%USERPROFILE%\.keystore" 2>nul

REM Configurar MAVEN_OPTS para evitar problemas de segurança
set MAVEN_OPTS=-Djava.security.manager= -Djava.security.policy=all.policy -Xms512m -Xmx1024m

echo 🔧 Compilando projeto...
call .\mvnw.cmd clean compile -q

echo 🐛 Iniciando DEBUG na porta 5005...
echo.
echo ⚠️  IMPORTANTE: Aplicação vai PAUSAR aguardando conexão do debugger
echo 🔌 Use VS Code ou IntelliJ para conectar em localhost:5005
echo.
echo 📋 Passos no VS Code:
echo    1. Ctrl+Shift+P
echo    2. Digite: "Java: Attach Debugger"  
echo    3. Host: localhost, Port: 5005
echo.

REM Iniciar com parâmetros de debug mais compatíveis
call .\mvnw.cmd spring-boot:run ^
  -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005 -Djava.security.manager= -Djava.security.policy=all.policy -Dfile.encoding=UTF-8" ^
  -Dspring-boot.run.profiles=dev

pause
