# Configuração do Supabase - Transaction Pooler

## 🔗 **String de Conexão Original**
```
jdbc:postgresql://aws-0-sa-east-1.pooler.supabase.com:6543/postgres?user=postgres.krlhnihkslmmihprkwqm&password=[YOUR-PASSWORD]
```

## ⚙️ **Como Configurar**

### **1. Obter a Senha do Projeto Supabase**

1. Acesse o **Supabase Dashboard**: https://supabase.com/dashboard
2. Selecione seu projeto
3. Vá em **Settings** → **Database**
4. Na seção **Connection String**, copie a senha que aparece no campo `password=`

### **2. Configurar nos Arquivos**

**Para Desenvolvimento (`application-dev.properties`):**
```properties
spring.datasource.password=SUA_SENHA_AQUI
```

**Para Produção (`application-prod.properties`):**
```properties
spring.datasource.password=SUA_SENHA_AQUI
```

### **3. Exemplo de Configuração Completa**

```properties
# Configuração do banco de dados Supabase - Transaction Pooler
spring.datasource.url=jdbc:postgresql://aws-0-sa-east-1.pooler.supabase.com:6543/postgres?user=postgres.krlhnihkslmmihprkwqm&sslmode=require&preparedStatementCacheQueries=0&rewriteBatchedInserts=true&connectTimeout=30&socketTimeout=60&tcpKeepAlive=true
spring.datasource.username=postgres.krlhnihkslmmihprkwqm
spring.datasource.password=SUA_SENHA_REAL_AQUI
spring.datasource.driver-class-name=org.postgresql.Driver
```

## 🔧 **Parâmetros da Conexão Explicados**

| Parâmetro | Descrição |
|-----------|-----------|
| `user=postgres.krlhnihkslmmihprkwqm` | Usuário específico do projeto |
| `sslmode=require` | SSL obrigatório para segurança |
| `preparedStatementCacheQueries=0` | Otimização para Transaction Pooler |
| `rewriteBatchedInserts=true` | Melhora performance de inserções em lote |
| `connectTimeout=30` | Timeout de conexão (30 segundos) |
| `socketTimeout=60` | Timeout de socket (60 segundos) |
| `tcpKeepAlive=true` | Mantém conexão ativa |

## 🔒 **Segurança**

### **Para Desenvolvimento Local:**
- Coloque a senha diretamente no arquivo `application-dev.properties`

### **Para Produção:**
- **NÃO** comite a senha no Git
- Use variáveis de ambiente:
  ```properties
  spring.datasource.password=${SUPABASE_PASSWORD:default_value}
  ```

## 🚀 **Testando a Conexão**

1. Substitua `[YOUR-PASSWORD]` pela senha real
2. Execute a aplicação
3. Verifique os logs para confirmar conexão com Supabase
4. Teste o endpoint: `GET /api/bicycles/user`

## 📋 **Status Atual**

- ✅ **URL de conexão**: Configurada corretamente
- ✅ **Usuário**: postgres.krlhnihkslmmihprkwqm  
- ⚠️ **Senha**: Aguardando configuração (substitua [YOUR-PASSWORD])
- ✅ **Driver**: PostgreSQL driver configurado
- ✅ **Pool de conexão**: HikariCP otimizado para Transaction Pooler

## 🆘 **Troubleshooting**

### **Erro de Autenticação:**
```
org.postgresql.util.PSQLException: FATAL: password authentication failed
```
**Solução**: Verificar se a senha está correta no Supabase Dashboard

### **Erro de Conexão:**
```
java.net.ConnectException: Connection refused
```
**Solução**: Verificar se o Transaction Pooler está ativo no Supabase

### **Timeout de Conexão:**
```
org.postgresql.util.PSQLException: Connection attempt timed out
```
**Solução**: Verificar configurações de firewall e conectividade de rede
