@echo off
echo ==============================================
echo   TESTE DE CONECTIVIDADE BACKEND/FRONTEND
echo ==============================================
echo.

set BACKEND_URL=https://seu-app.onrender.com

if "%1" NEQ "" (
    set BACKEND_URL=%1
)

echo Backend URL: %BACKEND_URL%
echo.

echo 1. Testando Health Check...
curl -s "%BACKEND_URL%/api/health" || echo ERRO: Health check falhou

echo.
echo.

echo 2. Testando CORS...
curl -s -H "Origin: https://example.netlify.app" "%BACKEND_URL%/api/test-cors" || echo ERRO: CORS test falhou

echo.
echo.

echo 3. Testando POST...
curl -s -X POST -H "Content-Type: application/json" -H "Origin: https://example.netlify.app" -d "{\"test\":\"data\"}" "%BACKEND_URL%/api/test-post" || echo ERRO: POST test falhou

echo.
echo.

echo 4. Testando OPTIONS (Preflight)...
curl -s -X OPTIONS -H "Origin: https://example.netlify.app" -H "Access-Control-Request-Method: POST" "%BACKEND_URL%/api/test-post" -v

echo.
echo ==============================================
echo   TESTE CONCLUIDO
echo ==============================================

pause
