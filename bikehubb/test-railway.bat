@echo off
echo ==============================================
echo   TESTE COM RAILWAY POSTGRESQL
echo ==============================================
echo.
echo 1. Crie conta em: https://railway.app
echo 2. New Project ^> Provision PostgreSQL
echo 3. Copie a URL de conexao (Connect tab)
echo 4. Execute este script com a URL:
echo.
echo Exemplo:
echo   test-railway.bat "postgresql://postgres:senha@host:port/database"
echo.

if "%1"=="" (
    echo ERRO: URL de conexao nao fornecida!
    echo.
    echo Uso: test-railway.bat "postgresql://user:pass@host:port/db"
    pause
    exit /b 1
)

echo Testando conexao com Railway...
echo URL: %1
echo.

set DATABASE_URL=%1
set JAVA_HOME=E:\JAVA\jdk-21_windows-x64_bin\jdk-21.0.6

echo Compilando projeto...
call mvn clean package -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo ERRO na compilacao!
    pause
    exit /b 1
)

echo.
echo Iniciando aplicacao com Railway PostgreSQL...
echo Profile: railway
echo.

java -jar target\*.jar --spring.profiles.active=railway --spring.datasource.url="%DATABASE_URL%"
