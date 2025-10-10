# 📝 INSTRUÇÕES PARA REABILITAR FUNCIONALIDADE DO STRIPE

## 🚫 Funcionalidades Desabilitadas Temporariamente

Para resolver o erro de build no Render relacionado ao `STRIPE_SECRET_KEY`, as seguintes funcionalidades foram **temporariamente desabilitadas**:

### Arquivos Desabilitados/Renomeados:
- `StripeService.java` → `StripeService.java.disabled`
- `PaymentController.java` → `PaymentController.java.disabled`
- `stripe-temp/StripeConfig.java` → comentado inteiramente

### Configurações Comentadas:
- `application-render.properties`: 
  - `# stripe.secret.key=${STRIPE_SECRET_KEY}`
  - `# stripe.publishable.key=${STRIPE_PUBLISHABLE_KEY}`
  - `# stripe.webhook.secret=${STRIPE_WEBHOOK_SECRET}`

## ✅ Para Reabilitar o Stripe no Futuro:

### 1. Configurar Variáveis de Ambiente no Render:
```bash
STRIPE_SECRET_KEY=sk_live_... ou sk_test_...
STRIPE_PUBLISHABLE_KEY=pk_live_... ou pk_test_...
STRIPE_WEBHOOK_SECRET=whsec_...
```

### 2. Descomentar as Propriedades:
Em `application-render.properties`, descomente:
```properties
stripe.secret.key=${STRIPE_SECRET_KEY}
stripe.publishable.key=${STRIPE_PUBLISHABLE_KEY}
stripe.webhook.secret=${STRIPE_WEBHOOK_SECRET}
```

### 3. Reabilitar os Arquivos:
```bash
# Renomear de volta
mv StripeService.java.disabled StripeService.java
mv PaymentController.java.disabled PaymentController.java

# Descomentar o conteúdo das classes
```

### 4. Descomentar StripeConfig.java:
No arquivo `stripe-temp/StripeConfig.java`, descomentar toda a classe.

## 💡 Status Atual da Aplicação:
- ✅ Anúncios podem ser criados **SEM COBRANÇA**
- ✅ Todas as funcionalidades principais funcionam
- ✅ Build no Render deve funcionar normalmente
- ❌ Endpoint `/api/create-checkout-session` temporariamente desabilitado

## 📋 Funcionalidades Principais Mantidas:
- ✅ Criação de anúncios de bicicleta
- ✅ Upload de imagens
- ✅ Listagem e busca de anúncios
- ✅ Autenticação JWT
- ✅ Gerenciamento de usuários
- ✅ Endpoints /api/advertisements/list, /search, /user

---
**Data de Desabilitação:** 10/10/2025
**Motivo:** Erro de build no Render - `Could not resolve placeholder 'STRIPE_SECRET_KEY'`