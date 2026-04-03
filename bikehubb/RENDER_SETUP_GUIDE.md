# 🚀 Guia de Configuração do Render

## ✅ Correções Realizadas

1. **render.yaml** - Corrigido para usar o profile `render` em vez de `prod`
2. **application-render.properties** - Adicionado log do DatabaseConnectionChecker

## 📋 Variáveis de Ambiente Necessárias no Render

Você precisa configurar estas variáveis de ambiente no painel do Render:

### 1. DATABASE_URL
```
jdbc:postgresql://aws-0-sa-east-1.pooler.supabase.com:6543/postgres?user=postgres.krlhnihkslmmihprkwqm&sslmode=require&preparedStatementCacheQueries=0&rewriteBatchedInserts=true&connectTimeout=30&socketTimeout=60&tcpKeepAlive=true
```

### 2. DB_USERNAME
```
postgres.krlhnihkslmmihprkwqm
```

### 3. DB_PASSWORD
```
dfyEf5Io8SQTyT2k
```

### 4. JWT_SECRET (Opcional - já tem valor padrão)
```
Gl7w8l5EO0z8lfnxssXKTeObnJGSUnxD3rBhmDzcs62V9O8CXoj/pLBAjP646Tq+Uo2WaOAgCn+8oyEE0q17Jw==
```

### 5. SPRING_PROFILES_ACTIVE (Já configurado no render.yaml)
```
render
```

### 6. PORT (Já configurado no render.yaml)
```
8080
```

## 🔧 Como Adicionar Variáveis de Ambiente no Render

1. Acesse o dashboard do Render: https://dashboard.render.com
2. Selecione seu serviço **bikehubb-api**
3. Vá em **Environment** no menu lateral
4. Clique em **Add Environment Variable**
5. Adicione cada variável (nome e valor)
6. Clique em **Save Changes**

## 📝 Ordem de Prioridade das Variáveis

O Render irá:
1. Primeiro procurar por variáveis de ambiente (DATABASE_URL, etc.)
2. Se não encontrar, usar os valores padrão especificados após `:` 
3. Se nenhum existir, a aplicação falhará na inicialização

## 🔍 Verificação de Conexão com Banco

Quando a aplicação iniciar, você verá nos logs do Render:

```
========================================
INICIANDO VERIFICAÇÃO DA CONEXÃO COM O BANCO DE DADOS
========================================
✓ CONEXÃO COM O BANCO DE DADOS ESTABELECIDA COM SUCESSO!
  → Database: PostgreSQL
  → Versão: ...
  → Driver: PostgreSQL JDBC Driver
  → Listando tabelas no banco de dados:
     • users
     • advertisements
  → Total de tabelas encontradas: X
========================================
BANCO DE DADOS PRONTO PARA USO!
========================================
```

Se houver erro, você verá mensagens detalhadas com o problema.

## 🐛 Comandos de Build/Deploy

O Render executará automaticamente:

**Build:**
```bash
./mvnw clean package -DskipTests
```

**Start:**
```bash
java -jar target/bikehubb-*.jar --spring.profiles.active=render
```

## ⚠️ Troubleshooting

### Erro: "Could not resolve placeholder"
- **Causa:** Variável de ambiente não configurada
- **Solução:** Adicione a variável faltante no painel do Render

### Erro: "Connection refused" ou "Timeout"
- **Causa:** Banco de dados inacessível ou credenciais incorretas
- **Solução:** Verifique as credenciais do Supabase e se o IP do Render está permitido

### Erro: "No suitable driver found"
- **Causa:** Driver PostgreSQL não está no classpath
- **Solução:** Verifique se a dependência está no pom.xml:
  ```xml
  <dependency>
      <groupId>org.postgresql</groupId>
      <artifactId>postgresql</artifactId>
      <scope>runtime</scope>
  </dependency>
  ```

## 📊 Logs do Render

Para ver os logs da aplicação no Render:
1. Acesse seu serviço no dashboard
2. Clique na aba **Logs**
3. Procure pelos logs do DatabaseConnectionChecker

## ✅ Checklist Final

- [ ] Variáveis de ambiente configuradas no Render
- [ ] Profile `render` configurado no render.yaml
- [ ] Arquivo application-render.properties com configurações corretas
- [ ] Banco de dados Supabase acessível
- [ ] Build executado com sucesso
- [ ] Logs mostram conexão bem-sucedida com o banco

## 🎯 Próximos Passos

Após configurar as variáveis de ambiente:
1. Faça commit das alterações nos arquivos
2. Faça push para o repositório Git
3. O Render detectará automaticamente as mudanças
4. Aguarde o build e deploy
5. Verifique os logs para confirmar a conexão com o banco
