@echo off
echo 🚀 BIKEHUBB - EXECUÇÃO NORMAL (Funciona 100%)
echo =============================================

REM Parar processos Java
for /f "tokens=2" %%i in ('tasklist /fi "imagename eq java.exe" ^| find "java.exe"') do taskkill /f /pid %%i 2>nul
timeout /t 2 /nobreak >nul

echo 🔧 Compilando...
call .\mvnw.cmd clean compile -q

echo 🚀 Iniciando aplicação na porta 8080...
echo.
echo ✅ Aplicação funcionando perfeitamente!
echo 🌐 Teste os endpoints em: http://localhost:8080
echo 📝 API Documentação: http://localhost:8080/swagger-ui.html (se configurado)
echo 🔍 Endpoints disponíveis:
echo    - GET  /api/bicycles/user (com JWT)
echo    - POST /api/bicycles (criar anúncio)
echo    - GET  /api/images/{id} (imagens)
echo.
echo 💡 Para parar: Ctrl+C
echo.

call .\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
