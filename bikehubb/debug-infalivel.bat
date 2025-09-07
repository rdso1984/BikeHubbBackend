@echo off
echo 🔥 SOLUÇÃO INFALÍVEL - DEBUG GARANTIDO
echo.
echo 1️⃣ Matando processos Java...
taskkill /f /im java.exe 2>nul
timeout /t 2 /nobreak >nul

echo 2️⃣ Compilando projeto...
call .\mvnw.cmd clean compile -q

echo 3️⃣ Iniciando em DEBUG na porta 5005...
echo ⚠️  Aplicação vai PAUSAR aguardando debugger
echo 🔌 Use Ctrl+Shift+P → "Java: Attach Debugger" → localhost:5005
echo.

call .\mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005" -Dspring-boot.run.profiles=dev

pause
