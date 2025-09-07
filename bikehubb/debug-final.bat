@echo off
setlocal

echo 🚀 SOLUÇÃO FINAL - SEM SECURITY MANAGER
echo =====================================

REM Definir JAVA_HOME
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

REM Parar processos Java
echo 🛑 Parando processos Java...
for /f "tokens=2" %%i in ('tasklist /fi "imagename eq java.exe" ^| find "java.exe"') do taskkill /f /pid %%i 2>nul
timeout /t 2 /nobreak >nul

REM MAVEN_OPTS limpo (SEM Security Manager)
set MAVEN_OPTS=-Xms512m -Xmx1024m -Dfile.encoding=UTF-8

echo 🔧 Compilando...
call .\mvnw.cmd clean compile -q

echo 🐛 Iniciando DEBUG - Porta 5005...
echo.
echo ⚠️  AGUARDE: "Listening for transport dt_socket at address: 5005"
echo 🔌 DEPOIS conecte VS Code: Ctrl+Shift+P → "Java: Attach Debugger"
echo    Host: localhost, Port: 5005
echo.

REM DEBUG SEM Security Manager
call .\mvnw.cmd spring-boot:run ^
  -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005 -Dfile.encoding=UTF-8" ^
  -Dspring-boot.run.profiles=dev

pause
