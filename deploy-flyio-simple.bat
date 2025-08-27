@echo off
echo ============================================
echo    EXECUTANDO DEPLOY FLY.IO
echo ============================================
echo.

echo Executando script PowerShell...
echo.

REM Executar script PowerShell com política de execução temporária
powershell -ExecutionPolicy Bypass -File "deploy-flyio.ps1"

echo.
echo Deploy finalizado!
pause
