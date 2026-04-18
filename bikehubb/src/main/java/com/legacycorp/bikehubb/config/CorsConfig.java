package com.legacycorp.bikehubb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // TEMPORÁRIO: Permitindo todas as origens para testes com Flutter Web
                // TODO: Configurar domínios específicos após definir URL do Flutter
                .allowedOriginPatterns("*")
                /* Domínios específicos (comentados temporariamente):
                .allowedOrigins(
                    "http://localhost:3000", 
                    "http://localhost:3001", 
                    "http://localhost:4200",
                    "http://localhost:4201",
                    "https://bikehubb.netlify.app",
                    "https://bikehubbfrontend.netlify.app",
                    "https://bikehubbbackend.onrender.com",
                    "http://localhost:52711"  // Adicione aqui a URL do Flutter Web
                )
                */
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .exposedHeaders("Authorization", "Content-Type") 
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // TEMPORÁRIO: Permitindo todas as origens para testes com Flutter Web
        // TODO: Configurar domínios específicos após definir URL do Flutter
        configuration.addAllowedOriginPattern("*");
        
        /* Domínios específicos (comentados temporariamente):
        configuration.addAllowedOriginPattern("https://bikehubb.netlify.app");
        configuration.addAllowedOriginPattern("https://bikehubbfrontend.netlify.app");
        configuration.addAllowedOriginPattern("https://bikehubbbackend.onrender.com");
        configuration.addAllowedOriginPattern("http://localhost:3000");
        configuration.addAllowedOriginPattern("http://localhost:3001");
        configuration.addAllowedOriginPattern("http://localhost:4200");
        configuration.addAllowedOriginPattern("http://localhost:4201");
        // Adicione aqui: configuration.addAllowedOriginPattern("http://localhost:52711");
        */
        
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
