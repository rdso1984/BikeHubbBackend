@echo off
echo 🚀 Iniciando BikeHubb em modo DEBUG...
echo.
echo 📡 Aguarde o debugger se conectar na porta 5005
echo 🔍 Use "Attach to Running App" no VS Code para conectar
echo.
.\mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005 -Dspring.profiles.active=dev"
