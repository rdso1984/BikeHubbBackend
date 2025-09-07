# 🚴‍♂️ BikeHubb - Configuração de Profiles

Este documento explica como usar os diferentes perfis de configuração do BikeHubb.

## 📁 Estrutura de Configuração

A aplicação possui **3 arquivos de configuração** organizados por ambiente:

```
src/main/resources/
├── application.properties          # Configuração principal
├── application-dev.properties      # Ambiente de desenvolvimento
└── application-prod.properties     # Ambiente de produção
```

## 🔧 Profiles Disponíveis

### 🛠️ Profile DEV (Desenvolvimento) 
- **Banco:** H2 em memória
- **Console H2:** Habilitado em `/h2-console`
- **Logs:** Detalhados para debug
- **Recriação de tabelas:** A cada execução

### 🚀 Profile PROD (Produção)
- **Banco:** PostgreSQL Supabase
- **Pool de conexões:** Otimizado para produção
- **Logs:** Reduzidos
- **Tabelas:** Atualizadas incrementalmente

## 🎮 Como Executar

### 🏃‍♂️ Executar em Desenvolvimento (Padrão)
```bash
# Maven
./mvnw spring-boot:run

# O profile 'dev' está configurado como padrão
```

### 🏃‍♂️ Executar em Produção
```bash
# Maven com profile específico
./mvnw spring-boot:run -Dspring.profiles.active=prod

# Ou definindo a variável de ambiente
set SPRING_PROFILES_ACTIVE=prod
./mvnw spring-boot:run
```

### 🏃‍♂️ Executar JAR
```bash
# Desenvolvimento
java -jar target/bikehubb-0.0.1-SNAPSHOT.jar

# Produção
java -jar target/bikehubb-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## 🗄️ Configurações de Banco

### 💾 H2 (Desenvolvimento)
- **URL:** `jdbc:h2:mem:testdb`
- **Console:** http://localhost:8080/h2-console
- **Usuário:** `sa`
- **Senha:** (vazia)

### 🐘 PostgreSQL Supabase (Produção)
- **Host:** `aws-0-sa-east-1.pooler.supabase.com:6543`
- **Banco:** `postgres`
- **SSL:** Obrigatório
- **Pool:** Configurado para Transaction Pooler

## 🔍 Como Verificar Profile Ativo

Ao iniciar a aplicação, procure por esta linha no log:
```
The following 1 profile is active: "dev"
```

## 📝 Endpoints Disponíveis

### 🚲 API Principal
- **GET** `/api/bicycles/user` - Lista bicicletas do usuário autenticado

### 🔧 Utilitários (apenas dev)
- **GET** `/h2-console` - Console do banco H2

## 🛡️ Autenticação

Todos os endpoints da API requerem JWT Token no header:
```
Authorization: Bearer <seu-jwt-token>
```

## 🚨 Troubleshooting

### ❌ Problema: Erro de conexão com Supabase
**Solução:** Verifique as credenciais no `application-prod.properties`

### ❌ Problema: Tabelas não criadas no H2
**Solução:** O H2 recria as tabelas automaticamente. Verifique o console em `/h2-console`

### ❌ Problema: Profile não está ativo
**Solução:** Verifique se definiu corretamente: `-Dspring.profiles.active=prod`

## 📊 Monitoramento

### 📈 Logs Importantes
- **Conexão DB:** Procure por "HikariPool" nos logs
- **Profile Ativo:** Procure por "profile is active"
- **Servidor:** Procure por "Tomcat started on port 8080"

---

**🔗 Projeto:** BikeHubb Backend  
**📅 Atualizado:** Setembro 2025  
**👨‍💻 Desenvolvedor:** Rafael DSO
