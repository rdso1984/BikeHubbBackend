@echo off
echo 🛑 Parando processos Java existentes...
taskkill /f /im java.exe 2>nul
timeout /t 2 /nobreak >nul

echo 🚀 Iniciando BikeHubb em modo DEBUG (porta 5005)...
echo ⚠️  AGUARDE a mensagem "Listening for transport dt_socket at address: 5005"
echo 🔌 Depois conecte o VS Code usando "Attach to Debugger"
echo.

.\mvnw.cmd clean compile -q
.\mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005" -Dspring-boot.run.profiles=dev
