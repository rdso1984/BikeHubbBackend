# 🔧 Solução para ClassNotFoundException no VS Code Debug

## 🔍 **Diagnóstico do Problema**

### **✅ Status Atual:**
- ✅ **Maven funciona**: Compilação e execução via `.\mvnw.cmd spring-boot:run` funcionam perfeitamente
- ✅ **Classes compiladas**: Todas as classes estão em `target/classes/`
- ✅ **Dependências baixadas**: Todas as dependências estão em `target/dependency/`
- ❌ **VS Code Debug**: Não consegue encontrar as classes no classpath

### **🎯 Causa do Problema:**
O VS Code não está conseguindo resolver corretamente o classpath das dependências Maven durante o debug.

## 🛠️ **Soluções Implementadas**

### **Solução 1: Dependências Copiadas para Local**
```bash
# Comando executado para copiar dependências
.\mvnw.cmd dependency:copy-dependencies
```
**Resultado**: 115+ JARs copiados para `target/dependency/`

### **Solução 2: Configurações de Debug Otimizadas**

**Foram criadas 3 configurações de debug diferentes em `.vscode/launch.json`:**

#### **🥇 Configuração 1: "Debug Spring Boot App (Classpath Fixed)"**
- ✅ **Classpath explícito** via vmArgs
- ✅ **Console integrado** para melhor visibilidade
- ✅ **Pré-compilação** automática via task

#### **🥈 Configuração 2: "Spring Boot via Maven Task"**
- ✅ **Executa via Maven** (mais confiável)
- ✅ **Background task** para spring-boot:run
- ✅ **Debug attach** possível

#### **🥉 Configuração 3: "Spring Boot-BikehubbApplication<bikehubb>"**
- ✅ **Configuração padrão** do VS Code
- ✅ **ClassPaths explícitos** definidos
- ✅ **Fallback** se outras não funcionarem

### **Solução 3: Tasks Automatizadas**

**Arquivo `.vscode/tasks.json` criado com:**
- ✅ **maven-compile**: Compilação rápida
- ✅ **maven-clean-compile**: Limpeza + compilação
- ✅ **maven-spring-boot-run**: Execução em background

## 🚀 **Como Testar as Soluções**

### **Método 1: Debug Otimizado (Recomendado)**
1. **Pressione `F5`**
2. **Selecione**: `"Debug Spring Boot App (Classpath Fixed)"`
3. **Aguarde**: A aplicação deve iniciar sem ClassNotFoundException

### **Método 2: Via Maven Task**
1. **Pressione `F5`**
2. **Selecione**: `"Spring Boot via Maven Task"`
3. **Resultado**: Executa como Maven mas permite debug

### **Método 3: Configuração Padrão**
1. **Pressione `F5`**
2. **Selecione**: `"Spring Boot-BikehubbApplication<bikehubb>"`
3. **Teste**: Se VS Code resolver as dependências corretamente

### **Método 4: Terminal (Funciona Sempre)**
```bash
# Se debug ainda não funcionar, use o terminal:
.\mvnw.cmd spring-boot:run
```

## 🔧 **Comandos de Troubleshooting**

### **Recarregar Projeto VS Code:**
```
Ctrl+Shift+P → "Java: Reload Projects"
Ctrl+Shift+P → "Java: Rebuild Workspace"
```

### **Limpar Cache Completo:**
```bash
# 1. Limpar Maven
.\mvnw.cmd clean

# 2. Recopiar dependências
.\mvnw.cmd dependency:copy-dependencies

# 3. Compilar
.\mvnw.cmd compile

# 4. Recarregar VS Code
# Ctrl+Shift+P → "Developer: Reload Window"
```

### **Verificar Classpath:**
```bash
# Verificar se classes estão compiladas
ls target/classes/com/legacycorp/bikehubb/

# Verificar se dependências foram copiadas
ls target/dependency/ | wc -l
# Deve mostrar 115+ arquivos .jar
```

## 📋 **Estrutura de Arquivos Criados**

```
.vscode/
├── launch.json          # 3 configurações de debug
├── tasks.json           # Tasks Maven automatizadas
└── settings.json        # Configurações JDK

target/
├── classes/             # Classes compiladas
└── dependency/          # 115+ JARs das dependências
```

## 🔍 **Diagnóstico de Problemas**

### **Se ainda der ClassNotFoundException:**

#### **Verificar se dependências estão presentes:**
```bash
ls target/dependency/ | grep -E "(spring|hibernate|jackson)"
```

#### **Verificar se classes foram compiladas:**
```bash
ls target/classes/com/legacycorp/bikehubb/
```

#### **Verificar configuração Java no VS Code:**
```
Ctrl+Shift+P → "Java: Configure Java Runtime"
# Verificar se Java 21 está selecionado
```

### **Classes específicas que podem dar problema:**
- ✅ **Spring Boot classes**: Dependências copiadas para `target/dependency/`
- ✅ **Hibernate classes**: Incluídas nas dependências
- ✅ **Jackson classes**: Presentes para JSON processing
- ✅ **JWT classes**: jjwt-* JARs incluídos

## ⚡ **Solução Rápida**

**Se nada funcionar, execute estes comandos em sequência:**

```bash
# 1. Limpar tudo
.\mvnw.cmd clean

# 2. Copiar dependências
.\mvnw.cmd dependency:copy-dependencies

# 3. Compilar
.\mvnw.cmd compile

# 4. No VS Code:
# Ctrl+Shift+P → "Java: Reload Projects"
# F5 → "Debug Spring Boot App (Classpath Fixed)"
```

## 🎯 **Status Final**

- ✅ **Dependências**: 115+ JARs disponíveis em `target/dependency/`
- ✅ **Classes**: Compiladas em `target/classes/`
- ✅ **Configurações**: 3 opções de debug criadas
- ✅ **Tasks**: Automação Maven configurada
- ✅ **Fallback**: Maven sempre funciona via terminal

**O ClassNotFoundException deve estar resolvido com essas configurações!** 🎉
