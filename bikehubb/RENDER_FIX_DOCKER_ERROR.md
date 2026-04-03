# 🔧 Corrigir Erro de Docker no Render

## ❌ Erro Atual
```
[ERROR] The goal you specified requires a project to execute but there is no POM in this directory (/app)
```

## 🎯 Causa do Problema

O **Render está tentando fazer build usando Docker** mesmo após renomear os Dockerfiles. Isso acontece porque o serviço foi configurado inicialmente como "Docker" no dashboard do Render.

## ✅ SOLUÇÃO: Recriar o Serviço no Render

### Opção 1: Deletar e Recriar o Serviço (RECOMENDADO)

1. **Acesse o Dashboard do Render:**
   - Vá para https://dashboard.render.com

2. **Delete o serviço atual:**
   - Clique no serviço `bikehubb-api`
   - Vá em **Settings** (no menu lateral)
   - Role até o final da página
   - Clique em **Delete Web Service**
   - Confirme digitando o nome do serviço

3. **Crie um novo serviço:**
   - Clique em **New +** > **Web Service**
   - Conecte seu repositório Git
   - O Render detectará automaticamente o `render.yaml`
   - Clique em **Apply**

4. **Configure as Variáveis de Ambiente:**
   - Vá em **Environment** no menu lateral
   - Adicione:
     - `DATABASE_URL` = `jdbc:postgresql://aws-0-sa-east-1.pooler.supabase.com:6543/postgres?user=postgres.krlhnihkslmmihprkwqm&sslmode=require&preparedStatementCacheQueries=0&rewriteBatchedInserts=true&connectTimeout=30&socketTimeout=60&tcpKeepAlive=true`
     - `DB_USERNAME` = `postgres.krlhnihkslmmihprkwqm`
     - `DB_PASSWORD` = `dfyEf5Io8SQTyT2k`
   - Clique em **Save Changes**

5. **Faça o primeiro deploy:**
   - Commit e push das alterações:
     ```bash
     git add .
     git commit -m "fix: configurar Render para usar buildpack Java nativo"
     git push
     ```
   - O Render iniciará o build automaticamente

### Opção 2: Forçar Build Nativo Manualmente

Se não quiser deletar o serviço:

1. **Acesse as Settings do serviço:**
   - Dashboard > `bikehubb-api` > **Settings**

2. **Verifique a seção "Build & Deploy":**
   - **Build Command:** `./mvnw clean package -DskipTests`
   - **Start Command:** `java -jar target/bikehubb-*.jar --spring.profiles.active=render`
   - **NÃO deve ter Dockerfile configurado**

3. **Adicione variável de ambiente para forçar buildpack:**
   - Vá em **Environment**
   - Adicione: `DISABLE_DOCKER_BUILD` = `true`
   - Salve

4. **Force Clear Build Cache:**
   - Vá em **Settings** > **Build & Deploy**
   - Clique em **Manual Deploy** > **Clear build cache & deploy**

## 🔍 Verificar se Funcionou

Após o deploy, nos **Logs** você deve ver:

✅ **Correto (usando buildpack Java):**
```
==> Building from source...
==> Installing OpenJDK
==> Installing Maven
==> Executing buildCommand: ./mvnw clean package -DskipTests
```

❌ **Errado (ainda usando Docker):**
```
==> Building with Dockerfile
#14 ERROR: process "/bin/sh -c mvn clean package..."
```

## 📋 Checklist de Verificação

Antes de fazer commit, confirme:

- [ ] `Dockerfile` foi renomeado para `Dockerfile.bak`
- [ ] `Dockerfile.root` foi renomeado para `Dockerfile.root.bak`
- [ ] Nenhum arquivo chamado exatamente `Dockerfile` existe no projeto
- [ ] `render.yaml` está configurado com `env: java` e `runtime: java`
- [ ] Variáveis de ambiente configuradas no Render

## 🚀 Comandos Git para Deploy

```bash
# Verificar status
git status

# Adicionar alterações
git add .

# Commit
git commit -m "fix: remover Dockerfiles e configurar buildpack Java nativo para Render"

# Push para trigger deploy automático
git push origin main  # ou master, dependendo do seu branch
```

## ⚠️ Troubleshooting

### Se AINDA aparecer erro de Docker:

1. **Verifique se há Dockerfile no repositório remoto:**
   ```bash
   git ls-files | grep -i dockerfile
   ```
   
2. **Se aparecer algum arquivo, delete do Git:**
   ```bash
   git rm Dockerfile
   git rm Dockerfile.root
   git commit -m "remove: deletar Dockerfiles do repositório"
   git push
   ```

3. **Limpe o cache do Render:**
   - Dashboard > Settings > Manual Deploy > Clear build cache & deploy

### Se o build passar mas a aplicação não iniciar:

Verifique nos logs se o erro é de conexão com banco de dados:
- Confirme que as variáveis `DATABASE_URL`, `DB_USERNAME`, `DB_PASSWORD` estão configuradas
- Verifique se o banco Supabase está acessível
- Procure logs do `DatabaseConnectionChecker`

## 📚 Referências

- [Render Docs - Native Environments](https://render.com/docs/native-environments)
- [Render Docs - Java Buildpacks](https://render.com/docs/deploy-java)
- [RENDER_SETUP_GUIDE.md](./RENDER_SETUP_GUIDE.md) - Guia completo de configuração
