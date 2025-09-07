# 🛠️ Configuração do JDK no VS Code - Guia Completo

## ✅ **Status Atual do Java**

**Java instalado e configurado corretamente:**
- ✅ **Versão**: OpenJDK 21.0.8+9 (Temurin)
- ✅ **JAVA_HOME**: `C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot`
- ✅ **Compatível**: Spring Boot 3.5.4 requer Java 17+

## 🔧 **Configurações Aplicadas no VS Code**

### **Arquivo `.vscode/settings.json` atualizado:**

```json
{
    "java.configuration.updateBuildConfiguration": "interactive",
    "java.jdt.ls.java.home": "C:\\Program Files\\Eclipse Adoptium\\jdk-21.0.8.9-hotspot",
    "java.jdt.ls.vmargs": "-XX:+UseParallelGC -XX:GCTimeRatio=4 -XX:AdaptiveSizePolicyWeight=90 -Dsun.zip.disableMemoryMapping=true -Xmx2G -Xms100m",
    "java.compile.nullAnalysis.mode": "automatic",
    "java.debug.settings.enableRunDebugCodeLens": true,
    "java.debug.settings.forceBuildBeforeLaunch": true,
    "java.configuration.runtimes": [
        {
            "name": "JavaSE-21",
            "path": "C:\\Program Files\\Eclipse Adoptium\\jdk-21.0.8.9-hotspot",
            "default": true
        }
    ]
}
```

## 🎯 **Como Resolver Problemas de JDK**

### **Método 1: Via Command Palette**

1. **Pressione `Ctrl+Shift+P`**
2. **Digite**: `Java: Configure Java Runtime`
3. **Verifique se aparece**:
   - ✅ **Java 21** em **Installed JDKs**
   - ✅ **JavaSE-21** como padrão
4. **Se não aparecer**, clique em **"+Add"** e aponte para:
   ```
   C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot
   ```

### **Método 2: Reload do Projeto**

1. **`Ctrl+Shift+P`** → **"Java: Reload Projects"**
2. **`Ctrl+Shift+P`** → **"Java: Rebuild Workspace"**
3. **Aguardar** o VS Code reindexar o projeto

### **Método 3: Verificação Manual**

**Abrir terminal e verificar:**
```powershell
# Verificar Java
java -version

# Verificar JAVA_HOME
echo $env:JAVA_HOME

# Verificar Maven wrapper
.\mvnw.cmd -version
```

## 🚨 **Problemas Comuns e Soluções**

### **❌ Problema 1: "No Java Runtime Found"**

**Solução:**
```json
// Adicionar em settings.json
"java.jdt.ls.java.home": "C:\\Program Files\\Eclipse Adoptium\\jdk-21.0.8.9-hotspot"
```

### **❌ Problema 2: "Wrong Java Version"**

**Solução:**
1. Verificar em **Java: Configure Java Runtime**
2. Definir **JavaSE-21** como padrão
3. Recarregar workspace

### **❌ Problema 3: "ClassNotFoundException"**

**Solução:**
1. **Clean + Rebuild**:
   ```powershell
   .\mvnw.cmd clean compile
   ```
2. **Reload VS Code Java**:
   - `Ctrl+Shift+P` → `Java: Reload Projects`

### **❌ Problema 4: "Extension Host Terminated"**

**Solução:**
1. **Aumentar memória da extensão Java**:
   ```json
   "java.jdt.ls.vmargs": "-Xmx2G -Xms100m"
   ```
2. **Reiniciar VS Code**

## 🔄 **Passos para Reinicializar o Ambiente**

### **1. Limpar Cache Completo**
```powershell
# Limpar projeto Maven
.\mvnw.cmd clean

# Fechar VS Code
# Reabrir VS Code
```

### **2. Reconfigurar Java**
```
Ctrl+Shift+P → "Java: Configure Java Runtime"
Ctrl+Shift+P → "Java: Reload Projects"
Ctrl+Shift+P → "Java: Rebuild Workspace"
```

### **3. Testar Funcionamento**
```powershell
# Teste 1: Compilação
.\mvnw.cmd compile

# Teste 2: Execução
.\mvnw.cmd spring-boot:run

# Teste 3: Debug no VS Code
# F5 → Selecionar "Debug Spring Boot App"
```

## 📋 **Configurações Globais vs Projeto**

### **Configuração Global (recomendada)**
**File** → **Preferences** → **Settings** → **Extensions** → **Java**
- Aplicar configurações para todos os projetos

### **Configuração por Projeto (atual)**
**`.vscode/settings.json`** - Apenas para este projeto

## ⚡ **Verificação Rápida**

**Execute estes comandos para verificar se tudo está funcionando:**

```powershell
# 1. Java está instalado?
java -version
# Esperado: OpenJDK 21.0.8+9

# 2. JAVA_HOME configurado?
echo $env:JAVA_HOME
# Esperado: C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot

# 3. Maven funciona?
.\mvnw.cmd -version
# Esperado: Apache Maven X.X.X (usando Java 21)

# 4. Projeto compila?
.\mvnw.cmd compile
# Esperado: BUILD SUCCESS
```

## 🎯 **Próximos Passos**

1. **Reinicie o VS Code** para aplicar as configurações
2. **Execute `Ctrl+Shift+P` → "Java: Reload Projects"`**
3. **Teste o debug** com `F5`
4. **Se persistir o problema**, use Maven:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

## 📌 **Importante**

- ✅ **Java 21 está instalado e funcionando**
- ✅ **Configurações do VS Code foram atualizadas**
- ✅ **JAVA_HOME está corretamente configurado**
- ✅ **Maven funciona normalmente**

**Se ainda tiver problemas, a aplicação funciona perfeitamente via Maven!**
