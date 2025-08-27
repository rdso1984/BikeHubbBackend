#!/bin/bash

# Script de build para Render
echo "Iniciando build para Render..."

# Instalar dependências e compilar
./mvnw clean package -DskipTests

echo "Build concluído!"
