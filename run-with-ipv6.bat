@echo off
echo ============================================
echo    EXECUTANDO BIKEHUBB COM SUPORTE IPv6
echo ============================================
echo.

cd /d "c:\Rafael\DesenvolvimentoTI\BikeHubb\Backend\bikehubb"

echo 1. Verificando status do Supabase...
echo    Testando conectividade IPv6...
nslookup -query=AAAA db.krlhnihkslmmihprkwqm.supabase.co
echo.

echo 2. Configurando Java para IPv6...
set MAVEN_OPTS=-Djava.net.preferIPv6Addresses=true -Djava.net.preferIPv4Stack=false -Dfile.encoding=UTF-8
set JAVA_OPTS=-Djava.net.preferIPv6Addresses=true -Djava.net.preferIPv4Stack=false

echo 3. Configurações de rede ativas:
echo    - IPv6 preferido: true
echo    - IPv4 stack: false
echo    - SSL mode: require
echo    - Connection Pooler: porta 6543
echo.

echo 4. Executando aplicacao Spring Boot...
echo    URL do banco: jdbc:postgresql://db.krlhnihkslmmihprkwqm.supabase.co:6543/postgres
echo.

.\mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Djava.net.preferIPv6Addresses=true -Djava.net.preferIPv4Stack=false -Dfile.encoding=UTF-8 -Xmx512m"

echo.
echo ============================================
echo Se o erro persistir:
echo 1. Verifique se o projeto Supabase esta ATIVO em https://supabase.com/dashboard
echo 2. Confirme a URL do banco no painel do Supabase
echo 3. Considere usar um banco local para desenvolvimento
echo ============================================
pause
