#!/bin/bash

# Script de start para Render
echo "Iniciando aplicação BikeHubb..."

# Executar a aplicação com o perfil render
java -jar target/bikehubb-*.jar --spring.profiles.active=render
