# Script de Deploy para Fly.io - BikeHubb
# Versão corrigida

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "    DEPLOY AUTOMATIZADO - FLY.IO" -ForegroundColor Cyan  
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# Função para verificar se comando existe
function Test-Command($commandName) {
    try {
        Get-Command $commandName -ErrorAction Stop
        return $true
    } catch {
        return $false
    }
}

# 1. Verificar/Instalar Fly CLI
Write-Host "1. Verificando Fly CLI..." -ForegroundColor Yellow

$flyctlFound = $false
$flyctlPath = "flyctl"

# Tentar encontrar flyctl no PATH
if (Test-Command "flyctl") {
    $flyctlFound = $true
    Write-Host "✅ Fly CLI encontrado no PATH!" -ForegroundColor Green
} else {
    # Verificar locais de instalação padrão
    $possiblePaths = @(
        "$env:USERPROFILE\.fly\bin\flyctl.exe",
        "$env:LOCALAPPDATA\fly\flyctl.exe", 
        "$env:PROGRAMFILES\fly\flyctl.exe"
    )
    
    foreach ($path in $possiblePaths) {
        if (Test-Path $path) {
            $flyctlPath = $path
            $flyctlFound = $true
            Write-Host "✅ Fly CLI encontrado em: $path" -ForegroundColor Green
            break
        }
    }
}

if (-not $flyctlFound) {
    Write-Host "❌ Fly CLI não encontrado! Instalando..." -ForegroundColor Red
    
    try {
        iwr https://fly.io/install.ps1 -useb | iex
        
        # Verificar se foi instalado
        $flyctlPath = "$env:USERPROFILE\.fly\bin\flyctl.exe"
        if (Test-Path $flyctlPath) {
            Write-Host "✅ Fly CLI instalado com sucesso!" -ForegroundColor Green
        } else {
            throw "Instalação falhou"
        }
    } catch {
        Write-Host "❌ Erro na instalação automática!" -ForegroundColor Red
        Write-Host "Por favor, instale manualmente:" -ForegroundColor Yellow
        Write-Host "1. Abra PowerShell como Administrador" -ForegroundColor Yellow
        Write-Host "2. Execute: iwr https://fly.io/install.ps1 -useb | iex" -ForegroundColor Yellow
        Read-Host "Pressione Enter para sair"
        exit 1
    }
}

# 2. Login
Write-Host ""
Write-Host "2. Fazendo login no Fly.io..." -ForegroundColor Yellow
try {
    & $flyctlPath auth login
    if ($LASTEXITCODE -ne 0) {
        throw "Erro no login"
    }
} catch {
    Write-Host "❌ Erro no login!" -ForegroundColor Red
    Read-Host "Pressione Enter para sair"
    exit 1
}

# 3. Verificar/Criar aplicação
Write-Host ""
Write-Host "3. Verificando aplicação BikeHubb..." -ForegroundColor Yellow

try {
    $appExists = $false
    $appsList = & $flyctlPath apps list 2>$null
    if ($appsList -match "bikehubb") {
        $appExists = $true
    }
    
    if (-not $appExists) {
        Write-Host "📱 Criando nova aplicação..." -ForegroundColor Blue
        & $flyctlPath apps create bikehubb --org personal
        if ($LASTEXITCODE -ne 0) {
            throw "Erro ao criar aplicação"
        }
    } else {
        Write-Host "✅ Aplicação bikehubb já existe!" -ForegroundColor Green
    }
} catch {
    Write-Host "❌ Erro ao verificar/criar aplicação!" -ForegroundColor Red
    Read-Host "Pressione Enter para sair"
    exit 1
}

# 4. Configurar variáveis de ambiente
Write-Host ""
Write-Host "4. Configurando variáveis de ambiente..." -ForegroundColor Yellow
try {
    & $flyctlPath secrets set SUPABASE_PASSWORD="Weboito8@Websete7@" -a bikehubb
    & $flyctlPath secrets set SPRING_PROFILES_ACTIVE="production" -a bikehubb
} catch {
    Write-Host "❌ Erro ao configurar variáveis!" -ForegroundColor Red
}

# 5. Compilar projeto
Write-Host ""
Write-Host "5. Compilando projeto..." -ForegroundColor Yellow
try {
    & .\mvnw.cmd clean package -DskipTests -Dspring.profiles.active=production
    if ($LASTEXITCODE -ne 0) {
        throw "Erro na compilação"
    }
} catch {
    Write-Host "❌ Erro na compilação!" -ForegroundColor Red
    Read-Host "Pressione Enter para sair"
    exit 1
}

# 6. Deploy
Write-Host ""
Write-Host "6. Fazendo deploy..." -ForegroundColor Yellow
try {
    & $flyctlPath deploy -a bikehubb
    if ($LASTEXITCODE -ne 0) {
        throw "Erro no deploy"
    }
} catch {
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
Write-Host "   $flyctlPath logs -a bikehubb" -ForegroundColor White
Write-Host "   $flyctlPath status -a bikehubb" -ForegroundColor White
Write-Host "   $flyctlPath ssh console -a bikehubb" -ForegroundColor White
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
    Write-Host "   Teste manualmente: https://bikehubb.fly.dev" -ForegroundColor Cyan
}

Write-Host ""
Write-Host "============================================" -ForegroundColor Green
Read-Host "Pressione Enter para finalizar"
