#!/bin/bash
# Script para configurar servidor de produção com IPv6

echo "============================================"
echo "    CONFIGURANDO SERVIDOR PARA BIKEHUBB"
echo "============================================"

# 1. Atualizar sistema
echo "1. Atualizando sistema..."
sudo apt update && sudo apt upgrade -y

# 2. Instalar Java 21
echo "2. Instalando Java 21..."
sudo apt install -y openjdk-21-jdk

# 3. Configurar IPv6
echo "3. Configurando IPv6..."
sudo sysctl -w net.ipv6.conf.all.disable_ipv6=0
sudo sysctl -w net.ipv6.conf.default.disable_ipv6=0
echo 'net.ipv6.conf.all.disable_ipv6 = 0' | sudo tee -a /etc/sysctl.conf
echo 'net.ipv6.conf.default.disable_ipv6 = 0' | sudo tee -a /etc/sysctl.conf

# 4. Testar conectividade IPv6 com Supabase
echo "4. Testando conectividade com Supabase..."
ping6 -c 3 db.krlhnihkslmmihprkwqm.supabase.co

# 5. Criar diretório da aplicação
echo "5. Preparando diretório..."
sudo mkdir -p /opt/bikehubb
sudo chown $USER:$USER /opt/bikehubb

# 6. Criar serviço systemd
echo "6. Criando serviço systemd..."
sudo tee /etc/systemd/system/bikehubb.service > /dev/null <<EOF
[Unit]
Description=BikeHubb Spring Boot Application
After=network.target

[Service]
Type=simple
User=$USER
WorkingDirectory=/opt/bikehubb
ExecStart=/usr/bin/java -Djava.net.preferIPv6Addresses=true -Djava.net.preferIPv4Stack=false -Xmx512m -jar /opt/bikehubb/bikehubb.jar --spring.profiles.active=production
Restart=always
RestartSec=10
Environment=JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64

[Install]
WantedBy=multi-user.target
EOF

# 7. Configurar firewall
echo "7. Configurando firewall..."
sudo ufw allow 8080/tcp
sudo ufw allow ssh
sudo ufw --force enable

echo "============================================"
echo "CONFIGURAÇÃO CONCLUÍDA!"
echo ""
echo "Para fazer deploy:"
echo "1. Copie o JAR: scp target/*.jar user@servidor:/opt/bikehubb/bikehubb.jar"
echo "2. Inicie o serviço: sudo systemctl start bikehubb"
echo "3. Ative na inicialização: sudo systemctl enable bikehubb"
echo "4. Verifique logs: sudo journalctl -u bikehubb -f"
echo "============================================"
