# Script de verificación para deployment en Railway (Windows PowerShell)
# Ejecuta este script antes de hacer push para verificar que todo está listo

Write-Host "🔍 Verificando configuración para Railway..." -ForegroundColor Cyan
Write-Host ""

# Verificar que existan los archivos clave
Write-Host "✓ Verificando archivos de configuración..." -ForegroundColor Yellow
$filesToCheck = @(
    "Dockerfile",
    "Procfile",
    "pom.xml",
    "src\main\resources\application.properties",
    "src\main\resources\static\index.html",
    "src\main\resources\static\diseno.css"
)

$allFilesExist = $true
foreach ($file in $filesToCheck) {
    if (Test-Path $file) {
        Write-Host "  ✅ $file existe" -ForegroundColor Green
    } else {
        Write-Host "  ❌ $file NO ENCONTRADO" -ForegroundColor Red
        $allFilesExist = $false
    }
}

if (-not $allFilesExist) {
    Write-Host ""
    Write-Host "❌ Faltan archivos requeridos" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "✓ Verificando build del proyecto..." -ForegroundColor Yellow
& .\mvnw.cmd clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Build falló" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "✓ Verificando contenido del JAR..." -ForegroundColor Yellow

$jarContent = jar -tf target\springboot-0.0.1-SNAPSHOT.jar

if ($jarContent -match "BOOT-INF/classes/static/index.html") {
    Write-Host "  ✅ index.html está en el JAR" -ForegroundColor Green
} else {
    Write-Host "  ❌ index.html NO está en el JAR" -ForegroundColor Red
    exit 1
}

if ($jarContent -match "BOOT-INF/classes/static/diseno.css") {
    Write-Host "  ✅ diseno.css está en el JAR" -ForegroundColor Green
} else {
    Write-Host "  ❌ diseno.css NO está en el JAR" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "✓ Verificando configuración de puerto..." -ForegroundColor Yellow
$appProps = Get-Content src\main\resources\application.properties -Raw

if ($appProps -match "server\.port=\`${PORT:") {
    Write-Host "  ✅ Puerto configurado con variable de entorno" -ForegroundColor Green
} else {
    Write-Host "  ⚠️  Puerto no usa variable de entorno PORT" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "✓ Verificando configuración de recursos estáticos..." -ForegroundColor Yellow

if ($appProps -match "spring\.web\.resources\.static-locations") {
    Write-Host "  ✅ Recursos estáticos configurados" -ForegroundColor Green
} else {
    Write-Host "  ⚠️  Configuración de recursos estáticos no encontrada" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "==========================================" -ForegroundColor Green
Write-Host "✅ Todas las verificaciones pasaron!" -ForegroundColor Green
Write-Host "==========================================" -ForegroundColor Green
Write-Host ""
Write-Host "📦 Archivos listos para Railway:" -ForegroundColor Cyan
Write-Host "   - Frontend: src\main\resources\static\" -ForegroundColor White
Write-Host "   - JAR: target\springboot-0.0.1-SNAPSHOT.jar" -ForegroundColor White
Write-Host ""
Write-Host "🚀 Próximos pasos:" -ForegroundColor Cyan
Write-Host "   1. git add ." -ForegroundColor White
Write-Host "   2. git commit -m 'Configuración para Railway'" -ForegroundColor White
Write-Host "   3. git push origin alexVersion" -ForegroundColor White
Write-Host "   4. Deploy en Railway desde tu repositorio GitHub" -ForegroundColor White
Write-Host ""
