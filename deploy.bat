@echo off
echo ============================================
echo    DEPLOY AUTOMATIZADO - BIKEHUBB
echo ============================================
echo.

echo Escolha a plataforma de deploy:
echo.
echo 1. Heroku (Recomendado - IPv6 nativo)
echo 2. Railway (Facil e rapido)
echo 3. Render (Gratuito com limitacoes)
echo 4. Docker Build (para VPS proprio)
echo 5. Gerar JAR para servidor proprio
echo.
set /p choice=Digite sua escolha (1-5): 

if "%choice%"=="1" goto heroku
if "%choice%"=="2" goto railway
if "%choice%"=="3" goto render
if "%choice%"=="4" goto docker
if "%choice%"=="5" goto jar
goto invalid

:heroku
echo.
echo ============================================
echo    DEPLOY HEROKU
echo ============================================
echo.
echo 1. Instalando Heroku CLI (se necessario)...
where heroku >nul 2>nul
if %errorlevel% neq 0 (
    echo Baixe Heroku CLI de: https://devcenter.heroku.com/articles/heroku-cli
    pause
    goto end
)

echo 2. Fazendo login no Heroku...
heroku login

echo 3. Criando aplicacao...
heroku create bikehubb-app-unique

echo 4. Configurando variaveis de ambiente...
heroku config:set SPRING_PROFILES_ACTIVE=production
heroku config:set SUPABASE_PASSWORD=Weboito8@Websete7@

echo 5. Fazendo deploy...
git add .
git commit -m "Deploy to Heroku"
git push heroku main

echo.
echo ✅ Deploy concluido! Acesse: https://bikehubb-app-unique.herokuapp.com
goto end

:railway
echo.
echo ============================================
echo    DEPLOY RAILWAY
echo ============================================
echo.
echo 1. Acesse: https://railway.app
echo 2. Conecte seu repositorio GitHub
echo 3. O railway.json ja esta configurado
echo 4. As variaveis de ambiente serao configuradas automaticamente
echo.
echo ✅ Deploy sera automatico apos conectar o repositorio!
goto end

:render
echo.
echo ============================================
echo    DEPLOY RENDER
echo ============================================
echo.
echo 1. Acesse: https://render.com
echo 2. Conecte seu repositorio GitHub
echo 3. Use estas configuracoes:
echo    - Build Command: ./mvnw clean package -DskipTests
echo    - Start Command: java -Djava.net.preferIPv6Addresses=true -jar target/*.jar --spring.profiles.active=production
echo    - Environment: SPRING_PROFILES_ACTIVE=production
echo.
echo ✅ Configure manualmente no painel do Render
goto end

:docker
echo.
echo ============================================
echo    BUILD DOCKER
echo ============================================
echo.
echo 1. Construindo imagem Docker...
docker build -t bikehubb-app .

echo 2. Executando localmente para teste...
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=production bikehubb-app

echo.
echo ✅ Para deploy em VPS:
echo docker save bikehubb-app | gzip > bikehubb-app.tar.gz
echo # Copie para servidor e execute:
echo # docker load < bikehubb-app.tar.gz
echo # docker run -d -p 8080:8080 --name bikehubb bikehubb-app
goto end

:jar
echo.
echo ============================================
echo    GERAR JAR PARA PRODUCAO
echo ============================================
echo.
echo 1. Compilando para producao...
.\mvnw.cmd clean package -DskipTests -Dspring.profiles.active=production

echo 2. JAR gerado em: target\bikehubb-0.0.1-SNAPSHOT.jar

echo 3. Para executar em servidor:
echo java -Djava.net.preferIPv6Addresses=true -Djava.net.preferIPv4Stack=false -jar bikehubb-0.0.1-SNAPSHOT.jar --spring.profiles.active=production

echo.
echo ✅ Copie o JAR e execute no servidor com IPv6!
goto end

:invalid
echo Opcao invalida!
goto end

:end
echo.
echo ============================================
pause
