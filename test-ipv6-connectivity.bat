@echo off
echo ============================================
echo    TESTE DE CONECTIVIDADE IPv6 SUPABASE
echo ============================================
echo.

echo 1. Testando resolução DNS IPv6...
nslookup -query=AAAA db.krlhnihkslmmihprkwqm.supabase.co
echo.

echo 2. Testando conectividade IPv6 na porta 6543...
powershell -Command "Test-NetConnection -ComputerName 'db.krlhnihkslmmihprkwqm.supabase.co' -Port 6543"
echo.

echo 3. Verificando configuração de rede local...
ipconfig | findstr "IPv6"
echo.

echo 4. Testando ping IPv6...
ping -6 db.krlhnihkslmmihprkwqm.supabase.co
echo.

echo ============================================
echo RESULTADO:
echo - Se DNS IPv6 funcionar: Configuração correta
echo - Se conexão falhar: Projeto Supabase pode estar pausado
echo - Se ping falhar: Problema de rede IPv6 local
echo ============================================
pause
