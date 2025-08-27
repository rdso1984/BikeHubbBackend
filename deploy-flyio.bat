@echo off
echo ============================================
echo    DEPLOY AUTOMATIZADO - FLY.IO
echo ============================================
echo.

@echo off
echo ============================================
echo    DEPLOY AUTOMATIZADO - FLY.IO
echo ============================================
echo.

echo 1. Verificando se Fly CLI esta instalado...

REM Verificar localizações possíveis do flyctl
set "FLYCTL_PATH="
if exist "%USERPROFILE%\.fly\bin\flyctl.exe" set "FLYCTL_PATH=%USERPROFILE%\.fly\bin\flyctl.exe"
if exist "%LOCALAPPDATA%\fly\flyctl.exe" set "FLYCTL_PATH=%LOCALAPPDATA%\fly\flyctl.exe"
if exist "%PROGRAMFILES%\fly\flyctl.exe" set "FLYCTL_PATH=%PROGRAMFILES%\fly\flyctl.exe"

REM Tentar encontrar flyctl no PATH
flyctl version >nul 2>nul
if %errorlevel% equ 0 (
    set "FLYCTL_PATH=flyctl"
    goto flyctl_found
)

REM Se não encontrou, tentar com caminhos específicos
if not "%FLYCTL_PATH%"=="" (
    "%FLYCTL_PATH%" version >nul 2>nul
    if %errorlevel% equ 0 goto flyctl_found
)

REM Fly CLI não encontrado - instalar
echo ❌ Fly CLI nao encontrado!
echo.
echo 📥 Instalando Fly CLI automaticamente...
echo    Aguarde...

REM Instalar usando PowerShell
powershell -Command "& {iwr https://fly.io/install.ps1 -useb | iex}"
if %errorlevel% neq 0 (
    echo ❌ Erro na instalacao automatica!
    echo.
    echo 📥 Instale manualmente:
    echo   1. Abra PowerShell como Administrador
    echo   2. Execute: iwr https://fly.io/install.ps1 -useb ^| iex
    echo   3. Feche e abra um novo terminal
    echo   4. Execute este script novamente
    echo.
    pause
    goto end
)

REM Atualizar PATH e tentar novamente
call refreshenv
set "FLYCTL_PATH=%USERPROFILE%\.fly\bin\flyctl.exe"
if not exist "%FLYCTL_PATH%" (
    echo ❌ Instalacao nao completou corretamente!
    echo.
    echo 📥 Tente instalacao manual:
    echo   PowerShell: iwr https://fly.io/install.ps1 -useb ^| iex
    echo   Ou baixe de: https://fly.io/docs/hands-on/install-flyctl/
    echo.
    pause
    goto end
)

:flyctl_found
echo ✅ Fly CLI encontrado!
echo.

echo 2. Fazendo login no Fly.io...
"%FLYCTL_PATH%" auth login
if %errorlevel% neq 0 (
    echo ❌ Erro no login!
    pause
    goto end
)

echo.
echo 3. Verificando/Criando aplicacao BikeHubb...
"%FLYCTL_PATH%" apps list | findstr "bikehubb" >nul
if %errorlevel% neq 0 (
    echo 📱 Criando nova aplicacao...
    "%FLYCTL_PATH%" apps create bikehubb --org personal
) else (
    echo ✅ Aplicacao bikehubb ja existe!
)

echo.
echo 4. Configurando variaveis de ambiente...
"%FLYCTL_PATH%" secrets set SUPABASE_PASSWORD="Weboito8@Websete7@" -a bikehubb
"%FLYCTL_PATH%" secrets set SPRING_PROFILES_ACTIVE="production" -a bikehubb

echo.
echo 5. Compilando projeto...
.\mvnw.cmd clean package -DskipTests -Dspring.profiles.active=production
if %errorlevel% neq 0 (
    echo ❌ Erro na compilacao!
    pause
    goto end
)

echo.
echo 6. Fazendo deploy...
"%FLYCTL_PATH%" deploy -a bikehubb
if %errorlevel% neq 0 (
    echo ❌ Erro no deploy!
    pause
    goto end
)

echo.
echo ============================================
echo ✅ DEPLOY CONCLUIDO COM SUCESSO!
echo.
echo 🌐 URL da aplicacao: https://bikehubb.fly.dev
echo 📊 Painel Fly.io: https://fly.io/apps/bikehubb
echo.
echo 📋 Comandos uteis:
echo   "%FLYCTL_PATH%" logs -a bikehubb              (ver logs)
echo   "%FLYCTL_PATH%" status -a bikehubb            (status da app)
echo   "%FLYCTL_PATH%" ssh console -a bikehubb       (acesso SSH)
echo   "%FLYCTL_PATH%" scale count 1 -a bikehubb     (escalar)
echo.
echo 🎯 Testando conectividade...
timeout /t 10 /nobreak >nul
curl -f https://bikehubb.fly.dev/actuator/health
if %errorlevel% equ 0 (
    echo ✅ Aplicacao esta online!
) else (
    echo ⚠️  Aplicacao pode estar iniciando...
    echo    Aguarde alguns minutos e teste: https://bikehubb.fly.dev
)
echo ============================================

:end
pause
