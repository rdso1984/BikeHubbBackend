# Deploy alternativo usando Docker direto
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "    DEPLOY ALTERNATIVO - DOCKER" -ForegroundColor Cyan  
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# 1. Compilar projeto
Write-Host "1. Compilando projeto..." -ForegroundColor Yellow
try {
    & .\mvnw.cmd clean package -DskipTests "-Dspring.profiles.active=production"
    if ($LASTEXITCODE -ne 0) {
        throw "Erro na compilação"
    }
    Write-Host "Compilação concluída!" -ForegroundColor Green
} catch {
    Write-Host "Erro na compilação!" -ForegroundColor Red
    exit 1
}

# 2. Construir imagem Docker
Write-Host ""
Write-Host "2. Construindo imagem Docker..." -ForegroundColor Yellow
try {
    docker build -t bikehubb:latest .
    if ($LASTEXITCODE -ne 0) {
        throw "Erro no build Docker"
    }
    Write-Host "Imagem Docker criada!" -ForegroundColor Green
} catch {
    Write-Host "Erro no build Docker!" -ForegroundColor Red
    exit 1
}

# 3. Testar imagem localmente
Write-Host ""
Write-Host "3. Testando imagem localmente..." -ForegroundColor Yellow
Write-Host "Comandos para testar:" -ForegroundColor Cyan
Write-Host "docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=local bikehubb:latest" -ForegroundColor White
Write-Host ""

# 4. Instruções para deploy
Write-Host "4. Próximos passos para deploy:" -ForegroundColor Yellow
Write-Host ""
Write-Host "OPÇÃO 1 - Render.com (Recomendado):" -ForegroundColor Cyan
Write-Host "1. Crie conta em https://render.com" -ForegroundColor White
Write-Host "2. Conecte seu repositório GitHub" -ForegroundColor White
Write-Host "3. Configure as variáveis de ambiente:" -ForegroundColor White
Write-Host "   SUPABASE_PASSWORD=Weboito8@Websete7@" -ForegroundColor White
Write-Host "   SPRING_PROFILES_ACTIVE=production" -ForegroundColor White
Write-Host ""

Write-Host "OPÇÃO 2 - Railway.app:" -ForegroundColor Cyan
Write-Host "1. Crie conta em https://railway.app" -ForegroundColor White
Write-Host "2. Conecte seu repositório GitHub" -ForegroundColor White
Write-Host "3. Configure as mesmas variáveis de ambiente" -ForegroundColor White
Write-Host ""

Write-Host "OPÇÃO 3 - Heroku (se preferir):" -ForegroundColor Cyan
Write-Host "1. heroku create bikehubb" -ForegroundColor White
Write-Host "2. heroku config:set SUPABASE_PASSWORD=Weboito8@Websete7@" -ForegroundColor White
Write-Host "3. heroku config:set SPRING_PROFILES_ACTIVE=production" -ForegroundColor White
Write-Host "4. git push heroku main" -ForegroundColor White
Write-Host ""

Write-Host "============================================" -ForegroundColor Green
Write-Host "PROCESSO CONCLUÍDO!" -ForegroundColor Green
Write-Host "Aplicação compilada e imagem Docker criada." -ForegroundColor Green
Write-Host "Escolha uma das opções acima para o deploy." -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green
