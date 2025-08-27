@echo off
echo ============================================
echo    TESTE MULTIPLAS CONFIGURACOES SUPABASE
echo ============================================
echo.

cd /d "c:\Rafael\DesenvolvimentoTI\BikeHubb\Backend\bikehubb"

echo 1. Tentativa 1: IPv4 porta 5432 (PostgreSQL direta)
echo Testando conectividade IPv4...
powershell -Command "Test-NetConnection -ComputerName 'db.krlhnihkslmmihprkwqm.supabase.co' -Port 5432"
echo.

echo 2. Tentativa 2: IPv4 porta 6543 (Connection Pooler)  
echo Testando conectividade IPv4...
powershell -Command "Test-NetConnection -ComputerName 'db.krlhnihkslmmihprkwqm.supabase.co' -Port 6543"
echo.

echo 3. Tentativa 3: Usando IP IPv6 direto
echo Testando IP direto: 2600:1f1e:75b:4b0d:8f2b:ee20:ca61:428a
powershell -Command "Test-NetConnection -ComputerName '2600:1f1e:75b:4b0d:8f2b:ee20:ca61:428a' -Port 6543"
echo.

echo 4. Verificando se o projeto Supabase esta ativo...
echo Abra: https://supabase.com/dashboard
echo Verifique se o projeto krlhnihkslmmihprkwqm esta ATIVO (nao pausado)
echo.

echo ============================================
echo PROXIMOS PASSOS:
echo 1. Se NENHUMA porta conectar: Projeto Supabase pausado
echo 2. Se alguma porta conectar: Use essa configuracao
echo 3. Considere configurar um banco PostgreSQL local
echo ============================================
pause
