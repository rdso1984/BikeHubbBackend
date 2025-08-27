# Script para fazer commit das configurações do Render.com
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "    PREPARANDO PARA RENDER.COM" -ForegroundColor Cyan  
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "1. Fazendo commit das configuracoes..." -ForegroundColor Yellow

# Verificar se está em um repositório git
if (-not (Test-Path ".git")) {
    Write-Host "Inicializando repositorio Git..." -ForegroundColor Blue
    git init
    git branch -M main
}

# Adicionar arquivos
Write-Host "Adicionando arquivos..." -ForegroundColor Blue
git add .
git status

Write-Host ""
Write-Host "2. Fazendo commit..." -ForegroundColor Yellow
git commit -m "feat: Configuracao para deploy no Render.com

- Adicionado render.yaml para deploy automatico
- Criado application-render.properties otimizado
- Configurado porta dinamica e headers para Render.com
- Pool de conexao ajustado para plano gratuito"

Write-Host ""
Write-Host "3. Verificando repositorio remoto..." -ForegroundColor Yellow

# Verificar se tem remote origin
$remoteExists = git remote get-url origin 2>$null
if ($LASTEXITCODE -ne 0) {
    Write-Host "Configure o repositorio remoto GitHub:" -ForegroundColor Red
    Write-Host "git remote add origin https://github.com/rdso1984/BikeHubbBackend.git" -ForegroundColor Cyan
    Write-Host "git push -u origin main" -ForegroundColor Cyan
    Write-Host ""
} else {
    Write-Host "Fazendo push para GitHub..." -ForegroundColor Blue
    git push origin main
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Push realizado com sucesso!" -ForegroundColor Green
    }
}

Write-Host ""
Write-Host "============================================" -ForegroundColor Green
Write-Host "PROJETO PREPARADO PARA RENDER.COM!" -ForegroundColor Green
Write-Host ""
Write-Host "PROXIMOS PASSOS:" -ForegroundColor Yellow
Write-Host "1. Acesse https://render.com" -ForegroundColor White
Write-Host "2. Clique em 'New +' > 'Web Service'" -ForegroundColor White
Write-Host "3. Conecte seu repositorio GitHub" -ForegroundColor White
Write-Host "4. Selecione 'BikeHubbBackend'" -ForegroundColor White
Write-Host "5. O Render.com detectara o render.yaml automaticamente!" -ForegroundColor Green
Write-Host ""
Write-Host "OU configure manualmente:" -ForegroundColor Yellow
Write-Host "- Build Command: ./mvnw clean package -DskipTests" -ForegroundColor Cyan
Write-Host "- Start Command: java -jar target/bikehubb-0.0.1-SNAPSHOT.jar" -ForegroundColor Cyan
Write-Host "- Environment: SPRING_PROFILES_ACTIVE=render" -ForegroundColor Cyan
Write-Host "- Environment: SUPABASE_PASSWORD=Weboito8@Websete7@" -ForegroundColor Cyan
Write-Host ""
Write-Host "============================================" -ForegroundColor Green
