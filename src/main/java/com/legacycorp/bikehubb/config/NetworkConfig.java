package com.legacycorp.bikehubb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import jakarta.annotation.PostConstruct;

/**
 * Configuração para forçar uso de IPv6 no banco de dados
 * Especialmente útil para conexões com Supabase que só suportam IPv6
 */
@Configuration
@Profile("!test") // Não aplicar em testes
public class NetworkConfig {

    @PostConstruct
    public void configureIPv6() {
        // Forçar uso de IPv6
        System.setProperty("java.net.preferIPv6Addresses", "true");
        System.setProperty("java.net.preferIPv4Stack", "false");
        
        // Configurações adicionais de rede
        System.setProperty("java.net.useSystemProxies", "true");
        
        System.out.println("🌐 Configuração de rede IPv6 ativada");
        System.out.println("   ✅ java.net.preferIPv6Addresses=true");
        System.out.println("   ✅ java.net.preferIPv4Stack=false");
    }
}
