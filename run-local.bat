@echo off
echo ============================================
echo    EXECUTANDO COM BANCO LOCAL H2
echo ============================================
echo.

cd /d "c:\Rafael\DesenvolvimentoTI\BikeHubb\Backend\bikehubb"

echo 1. Configurando perfil LOCAL...
echo    - Banco H2 em memoria (funciona 100%%)
echo    - Console H2: http://localhost:8080/h2-console
echo    - Usuario: sa (sem senha)
echo    - Todas as tabelas serao criadas automaticamente
echo.

echo 2. Compilando projeto...
.\mvnw.cmd clean compile -q

echo 3. Executando aplicacao...
echo    Aguarde a mensagem: "Started BikehubbApplication"
echo.

.\mvnw.cmd spring-boot:run -Dspring.profiles.active=local

echo.
echo ============================================
echo SUCESSO! Aplicacao rodando:
echo.
echo 🌐 API: http://localhost:8080
echo 🗃️  H2 Console: http://localhost:8080/h2-console
echo.
echo Configuracoes H2:
echo   JDBC URL: jdbc:h2:mem:bikehubb
echo   Username: sa
echo   Password: (deixe vazio)
echo.
echo 📝 Para testar: Use Postman ou curl para testar os endpoints
echo ============================================
pause
