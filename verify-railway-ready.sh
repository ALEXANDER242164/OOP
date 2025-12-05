#!/bin/bash

# Script de verificación para deployment en Railway
# Ejecuta este script antes de hacer push para verificar que todo está listo

echo "🔍 Verificando configuración para Railway..."
echo ""

# Verificar que existan los archivos clave
echo "✓ Verificando archivos de configuración..."
files_to_check=(
    "Dockerfile"
    "Procfile"
    "pom.xml"
    "src/main/resources/application.properties"
    "src/main/resources/static/index.html"
    "src/main/resources/static/diseno.css"
)

for file in "${files_to_check[@]}"; do
    if [ -f "$file" ]; then
        echo "  ✅ $file existe"
    else
        echo "  ❌ $file NO ENCONTRADO"
        exit 1
    fi
done

echo ""
echo "✓ Verificando build del proyecto..."
./mvnw clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Build falló"
    exit 1
fi

echo ""
echo "✓ Verificando contenido del JAR..."
jar -tf target/*.jar | grep "BOOT-INF/classes/static/index.html" > /dev/null

if [ $? -eq 0 ]; then
    echo "  ✅ index.html está en el JAR"
else
    echo "  ❌ index.html NO está en el JAR"
    exit 1
fi

jar -tf target/*.jar | grep "BOOT-INF/classes/static/diseno.css" > /dev/null

if [ $? -eq 0 ]; then
    echo "  ✅ diseno.css está en el JAR"
else
    echo "  ❌ diseno.css NO está en el JAR"
    exit 1
fi

echo ""
echo "✓ Verificando configuración de puerto..."
grep "server.port=\${PORT:" src/main/resources/application.properties > /dev/null

if [ $? -eq 0 ]; then
    echo "  ✅ Puerto configurado con variable de entorno"
else
    echo "  ⚠️  Puerto no usa variable de entorno PORT"
fi

echo ""
echo "✓ Verificando configuración de recursos estáticos..."
grep "spring.web.resources.static-locations" src/main/resources/application.properties > /dev/null

if [ $? -eq 0 ]; then
    echo "  ✅ Recursos estáticos configurados"
else
    echo "  ⚠️  Configuración de recursos estáticos no encontrada"
fi

echo ""
echo "=========================================="
echo "✅ Todas las verificaciones pasaron!"
echo "=========================================="
echo ""
echo "📦 Archivos listos para Railway:"
echo "   - Frontend: src/main/resources/static/"
echo "   - JAR: target/springboot-0.0.1-SNAPSHOT.jar"
echo ""
echo "🚀 Próximos pasos:"
echo "   1. git add ."
echo "   2. git commit -m 'Configuración para Railway'"
echo "   3. git push origin alexVersion"
echo "   4. Deploy en Railway desde tu repositorio GitHub"
echo ""
