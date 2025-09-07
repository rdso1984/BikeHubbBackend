# 🔧 Solução para ClassNotFoundException: kotlin.Metadata

## 🔍 **Diagnóstico do Problema**

### **✅ O que funciona:**
- ✅ **Execução via Maven**: `./mvnw.cmd spring-boot:run` **FUNCIONA PERFEITAMENTE**
- ✅ **Conexão com Supabase**: Database conectou com sucesso
- ✅ **Compilação**: Zero erros de compilação
- ✅ **Dependências**: Nenhuma dependência Kotlin no projeto

### **❌ O que não funciona:**
- ❌ **Debug do VS Code**: `ClassNotFoundException: kotlin.Metadata`

### **🎯 Conclusão:**
O problema é **específico do modo debug do VS Code**, não da aplicação em si.

## 🛠️ **Soluções Possíveis**

### **🥇 Solução 1: Usar as Configurações de Debug Criadas**

Foram criadas configurações específicas em `.vscode/launch.json`:

1. **"Debug Spring Boot App"** - Configuração otimizada
2. **"Spring Boot-BikehubbApplication<bikehubb>"** - Configuração padrão

**Como usar:**
1. Pressione `F5` ou vá em **Run and Debug**
2. Selecione **"Debug Spring Boot App"**
3. Clique em **Start Debugging**

### **🥈 Solução 2: Limpar Cache do VS Code**

```powershell
# 1. Fechar VS Code completamente
# 2. Limpar cache do projeto
.\mvnw.cmd clean

# 3. Limpar cache do VS Code Java
# Pressione Ctrl+Shift+P e execute:
# "Java: Reload Projects"
# "Java: Rebuild Workspace"
```

### **🥉 Solução 3: Reconfigurar Extensão Java**

1. **Ctrl+Shift+P** → **"Java: Configure Java Runtime"**
2. Verificar se está usando o **JDK 21** correto
3. **Ctrl+Shift+P** → **"Java: Reload Projects"**

### **🔄 Solução 4: Usar Terminal Integrado**

Como alternativa, você pode executar diretamente no terminal:

```powershell
# No terminal integrado do VS Code
.\mvnw.cmd spring-boot:run
```

### **🔧 Solução 5: Verificar Configurações do Projeto**

Adicione ao `settings.json` do VS Code:

```json
{
    "java.configuration.updateBuildConfiguration": "interactive",
    "java.compile.nullAnalysis.mode": "automatic",
    "java.debug.settings.enableRunDebugCodeLens": true,
    "java.debug.settings.forceBuildBeforeLaunch": true
}
```

## 🔍 **Por que acontece este erro?**

### **Possíveis Causas:**

1. **Cache Corrompido**: VS Code pode ter cache incorreto do classpath
2. **Conflito de ClassLoader**: Debug mode usa ClassLoader diferente
3. **Extensão Java**: Problema com a extensão Java do VS Code
4. **Dependência Fantasma**: Alguma dependência transitiva não detectada

### **Por que Maven funciona e Debug não:**

- **Maven**: Usa seu próprio ClassLoader e resolução de dependências
- **VS Code Debug**: Usa extensão Java que pode ter configuração diferente

## ✅ **Status Atual**

- ✅ **Aplicação**: Funcionando perfeitamente via Maven
- ✅ **Database**: Conectando com Supabase com sucesso  
- ✅ **Endpoint**: `/api/bicycles/user` pronto para teste
- ✅ **JWT**: Refatoração completa e segura implementada
- ⚠️ **Debug**: Problema específico do VS Code (mas aplicação funciona)

## 🚀 **Recomendações**

### **Para Desenvolvimento Imediato:**
1. Use `.\mvnw.cmd spring-boot:run` para testar a aplicação
2. Use as configurações de debug criadas como alternativa
3. A aplicação está **100% funcional**

### **Para Resolver o Debug:**
1. Tente a **Solução 1** primeiro (configurações criadas)
2. Se não resolver, use **Solução 2** (limpar cache)
3. Como último recurso, **Solução 3** (reconfigurar Java)

### **Teste Imediato:**
```bash
# 1. Iniciar aplicação
.\mvnw.cmd spring-boot:run

# 2. Testar endpoint (em outro terminal)
curl -H "Authorization: Bearer SEU_JWT_TOKEN" http://localhost:8080/api/bicycles/user
```

## 📋 **Arquivos Criados/Modificados**

- ✅ `.vscode/launch.json` - Configurações de debug otimizadas
- ✅ `KOTLIN_METADATA_SOLUTION.md` - Este documento
- ✅ Aplicação **totalmente funcional** via Maven

**⚡ IMPORTANTE: O problema não afeta a funcionalidade da aplicação - apenas o modo debug do VS Code!**
