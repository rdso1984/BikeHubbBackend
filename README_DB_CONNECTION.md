# Problemas de Conexão com o Banco de Dados Supabase

## Problema Identificado
A aplicação está falhando ao tentar conectar com o banco PostgreSQL do Supabase com o erro:
```
A tentativa de conexão falhou.
```

## Diagnósticos Realizados

### 1. Teste de Conectividade
- ❌ **Falha na resolução DNS**: O hostname `db.krlhnihkslmmihprkwqm.supabase.co` não está sendo resolvido
- ❌ **Porta 5432 inacessível**: Não conseguimos conectar na porta PostgreSQL

### 2. Possíveis Causas

#### A. URL do Banco Incorreta
- Verifique no painel do Supabase se a URL está correta
- A URL deve estar no formato: `db.[PROJECT_ID].supabase.co`

#### B. Projeto Supabase Pausado
- Projetos gratuitos do Supabase são pausados após inatividade
- Acesse o painel do Supabase e verifique se o projeto está ativo

#### C. Configurações de Firewall
- O firewall corporativo pode estar bloqueando a conexão
- Portas 5432 (PostgreSQL) podem estar bloqueadas

#### D. Configurações de Pool de Conexão
- O Supabase pode estar limitando conexões diretas
- Considere usar connection pooling

## Soluções Recomendadas

### Solução 1: Verificar Status do Projeto Supabase
1. Acesse https://supabase.com/dashboard
2. Verifique se o projeto está ativo (não pausado)
3. Se pausado, clique em "Resume" para reativar

### Solução 2: Obter URL Correta do Banco
1. No painel do Supabase, vá em Settings > Database
2. Copie a Connection String correta
3. Atualize o `application.properties`

### Solução 3: Usar Connection Pooling (Recomendado)
Substitua a URL no `application.properties` por:
```properties
# Usar Supabase Connection Pooler (porta 6543)
spring.datasource.url=jdbc:postgresql://db.krlhnihkslmmihprkwqm.supabase.co:6543/postgres?sslmode=require
```

### Solução 4: Configuração de SSL
Adicione parâmetros SSL obrigatórios:
```properties
spring.datasource.url=jdbc:postgresql://db.krlhnihkslmmihprkwqm.supabase.co:5432/postgres?sslmode=require&sslcert=client-cert.pem&sslkey=client-key.pem&sslrootcert=server-ca.pem
```

### Solução 5: Configuração Local para Testes
Para desenvolvimento local, considere usar um banco PostgreSQL local:
```properties
# Configuração para PostgreSQL local
spring.datasource.url=jdbc:postgresql://localhost:5432/bikehubb
spring.datasource.username=postgres
spring.datasource.password=sua_senha_local
```

## Próximos Passos
1. ✅ Verificar status do projeto no Supabase
2. ✅ Obter a URL correta de conexão
3. ✅ Testar com connection pooling (porta 6543)
4. ✅ Configurar SSL se necessário
5. ✅ Considerar ambiente local para desenvolvimento
