@echo off
echo ============================================
echo    VERIFICACAO DA INSTALACAO JAVA 21
echo ============================================
echo.

echo 1. Verificando versao do Java...
java -version
echo.

echo 2. Verificando JAVA_HOME...
echo JAVA_HOME: %JAVA_HOME%
echo.

echo 3. Verificando Maven com nova versao...
echo Testando compilacao do projeto...
.\mvnw.cmd clean compile -q
echo.

if %ERRORLEVEL%==0 (
    echo ✅ SUCESSO: Java 21 configurado corretamente!
    echo ✅ Projeto compilado com sucesso!
    echo.
    echo Agora você pode executar:
    echo .\mvnw.cmd spring-boot:run
) else (
    echo ❌ ERRO: Problemas na compilacao
    echo Verifique se Java 21 esta instalado corretamente
)

echo.
echo ============================================
pause
