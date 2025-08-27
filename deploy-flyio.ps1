# Deploy automatizado para Fly.io usando PowerShell
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "    DEPLOY AUTOMATIZADO - FLY.IO" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# 1. Verificar/Instalar Fly CLI
Write-Host "1. Verificando Fly CLI..." -ForegroundColor Yellow

$flyctlPath = $null
$possiblePaths = @(
    "$env:USERPROFILE\.fly\bin\flyctl.exe",
    "$env:LOCALAPPDATA\fly\flyctl.exe",
    "$env:PROGRAMFILES\fly\flyctl.exe"
)

# Tentar encontrar flyctl
foreach ($path in $possiblePaths) {
    if (Test-Path $path) {
        $flyctlPath = $path
        break
    }
}

# Tentar no PATH
if (-not $flyctlPath) {
    try {
        $null = Get-Command flyctl -ErrorAction Stop
        $flyctlPath = "flyctl"
    } catch {
        # Não encontrado
    }
}

if (-not $flyctlPath) {
    Write-Host "❌ Fly CLI não encontrado! Instalando..." -ForegroundColor Red
    
    try {
        iwr https://fly.io/install.ps1 -useb | iex
        
        # Atualizar PATH
        $env:PATH = [System.Environment]::GetEnvironmentVariable("PATH","Machine") + ";" + [System.Environment]::GetEnvironmentVariable("PATH","User")
        
        # Tentar encontrar novamente
        $flyctlPath = "$env:USERPROFILE\.fly\bin\flyctl.exe"
        if (-not (Test-Path $flyctlPath)) {
            throw "Instalação falhou"
        }
        
        Write-Host "✅ Fly CLI instalado com sucesso!" -ForegroundColor Green
    } catch {
        Write-Host "❌ Erro na instalação!" -ForegroundColor Red
        Write-Host "Instale manualmente: https://fly.io/docs/hands-on/install-flyctl/" -ForegroundColor Yellow
        Read-Host "Pressione Enter para sair"
        exit 1
    }
} else {
    Write-Host "✅ Fly CLI encontrado!" -ForegroundColor Green
}

# 2. Login
Write-Host ""
Write-Host "2. Fazendo login no Fly.io..." -ForegroundColor Yellow
& $flyctlPath auth login
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Erro no login!" -ForegroundColor Red
    Read-Host "Pressione Enter para sair"
    exit 1
}

# 3. Verificar/Criar aplicação
Write-Host ""
Write-Host "3. Verificando aplicação BikeHubb..." -ForegroundColor Yellow
$apps = & $flyctlPath apps list --json | ConvertFrom-Json
$existingApp = $apps | Where-Object { $_.Name -eq "bikehubb" }

if (-not $existingApp) {
    Write-Host "📱 Criando nova aplicação..." -ForegroundColor Blue
    & $flyctlPath apps create bikehubb --org personal
} else {
    Write-Host "✅ Aplicação bikehubb já existe!" -ForegroundColor Green
}

# 4. Configurar variáveis de ambiente
Write-Host ""
Write-Host "4. Configurando variáveis de ambiente..." -ForegroundColor Yellow
& $flyctlPath secrets set SUPABASE_PASSWORD="Weboito8@Websete7@" -a bikehubb
& $flyctlPath secrets set SPRING_PROFILES_ACTIVE="production" -a bikehubb

# 5. Compilar projeto
Write-Host ""
Write-Host "5. Compilando projeto..." -ForegroundColor Yellow
& .\mvnw.cmd clean package -DskipTests -Dspring.profiles.active=production
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Erro na compilação!" -ForegroundColor Red
    Read-Host "Pressione Enter para sair"
    exit 1
}

# 6. Deploy
Write-Host ""
Write-Host "6. Fazendo deploy..." -ForegroundColor Yellow
& $flyctlPath deploy -a bikehubb
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Erro no deploy!" -ForegroundColor Red
    Read-Host "Pressione Enter para sair"
    exit 1
}

# 7. Sucesso
Write-Host ""
Write-Host "============================================" -ForegroundColor Green
Write-Host "✅ DEPLOY CONCLUÍDO COM SUCESSO!" -ForegroundColor Green
Write-Host ""
Write-Host "🌐 URL da aplicação: https://bikehubb.fly.dev" -ForegroundColor Cyan
Write-Host "📊 Painel Fly.io: https://fly.io/apps/bikehubb" -ForegroundColor Cyan
Write-Host ""
Write-Host "📋 Comandos úteis:" -ForegroundColor Yellow
Write-Host "   $flyctlPath logs -a bikehubb                 # Ver logs"
Write-Host "   $flyctlPath status -a bikehubb               # Status da app"
Write-Host "   $flyctlPath ssh console -a bikehubb          # Acesso SSH"
Write-Host "   $flyctlPath scale count 1 -a bikehubb        # Escalar"
Write-Host ""
Write-Host "🎯 Testando conectividade em 10 segundos..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

try {
    $response = Invoke-WebRequest -Uri "https://bikehubb.fly.dev/actuator/health" -TimeoutSec 10
    if ($response.StatusCode -eq 200) {
        Write-Host "✅ Aplicação está online!" -ForegroundColor Green
    }
} catch {
    Write-Host "⚠️ Aplicação pode estar iniciando..." -ForegroundColor Yellow
    Write-Host "   Aguarde alguns minutos e teste: https://bikehubb.fly.dev" -ForegroundColor Cyan
}

Write-Host "============================================" -ForegroundColor Green
Read-Host "Pressione Enter para finalizar"
