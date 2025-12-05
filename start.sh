#!/usr/bin/env bash
set -e

# Script de construcción y arranque para Railway (fallback si no usa Dockerfile)
echo "Building application..."
mvn -B -DskipTests package

JAR_FILE=$(ls target/*.jar | head -n 1)
if [ -z "$JAR_FILE" ]; then
  echo "Jar not found in target/ - build failed"
  exit 1
fi

echo "Starting application from $JAR_FILE"
exec java -jar "$JAR_FILE"
