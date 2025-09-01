@echo off
echo ==============================================
echo   TESTE DE CONECTIVIDADE BIKEHUBB
echo ==============================================
echo.

set BACKEND_URL=https://bikehubbbackend.onrender.com
set FRONTEND_URL=https://bikehubb.netlify.app

echo Backend URL: %BACKEND_URL%
echo Frontend URL: %FRONTEND_URL%
echo.

echo 1. Testando Health Check...
curl -s "%BACKEND_URL%/api/health" || echo ERRO: Health check falhou

echo.
echo.

echo 2. Testando Info Endpoint...
curl -s "%BACKEND_URL%/api/info" || echo ERRO: Info endpoint falhou

echo.
echo.

echo 3. Testando Config Endpoint...
curl -s "%BACKEND_URL%/api/config" || echo ERRO: Config endpoint falhou

echo.
echo.

echo 4. Testando CORS do Netlify...
curl -s -H "Origin: %FRONTEND_URL%" "%BACKEND_URL%/api/test-cors" || echo ERRO: CORS test falhou

echo.
echo.

echo 5. Testando POST com CORS...
curl -s -X POST -H "Content-Type: application/json" -H "Origin: %FRONTEND_URL%" -d "{\"test\":\"data\"}" "%BACKEND_URL%/api/test-post" || echo ERRO: POST test falhou

echo.
echo.

echo 6. Testando OPTIONS (Preflight)...
curl -s -X OPTIONS -H "Origin: %FRONTEND_URL%" -H "Access-Control-Request-Method: POST" "%BACKEND_URL%/api/advertisements/create-advertisement" -v

echo.
echo ==============================================
echo   TESTE CONCLUIDO
echo ==============================================

pause
