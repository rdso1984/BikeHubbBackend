package com.legacycorp.bikehubb.security;

import java.util.Base64;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtUtil {

    private ObjectMapper objectMapper = new ObjectMapper();

    // Extrair user_id do token JWT (versão simplificada)
    public Long extractUserId(String token) {
        try {
            // Remove "Bearer " se presente
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // JWT tem 3 partes separadas por ponto: header.payload.signature
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new RuntimeException("Token JWT inválido");
            }

            // Decodificar o payload (segunda parte)
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            
            // Parse do JSON
            JsonNode payloadNode = objectMapper.readTree(payload);
            
            // Debug: mostrar o conteúdo do payload
            System.out.println("Payload do token: " + payload);
            
            // Tentar extrair user_id de diferentes campos possíveis
            if (payloadNode.has("user_id")) {
                return parseUserId(payloadNode.get("user_id").asText());
            } else if (payloadNode.has("userId")) {
                return parseUserId(payloadNode.get("userId").asText());
            } else if (payloadNode.has("id")) {
                return parseUserId(payloadNode.get("id").asText());
            } else if (payloadNode.has("sub")) {
                String sub = payloadNode.get("sub").asText();
                System.out.println("Campo 'sub' encontrado: " + sub);
                return parseUserId(sub);
            }
            
            throw new RuntimeException("user_id não encontrado no token. Campos disponíveis: " + 
                payloadNode.fieldNames().toString());
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar token JWT: " + e.getMessage());
        }
    }
    
    // Método auxiliar para converter string para Long (tratando UUIDs e números)
    private Long parseUserId(String userIdString) {
        if (userIdString == null || userIdString.trim().isEmpty()) {
            throw new RuntimeException("user_id está vazio");
        }
        
        try {
            // Tentar converter diretamente para Long
            return Long.valueOf(userIdString);
        } catch (NumberFormatException e) {
            // Se não for um número, pode ser um UUID
            if (userIdString.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")) {
                // É um UUID - você pode:
                // 1. Buscar o usuário pelo UUID na base de dados
                // 2. Ou mapear o UUID para um Long
                // Por enquanto, vamos gerar um hash do UUID para usar como Long
                return (long) userIdString.hashCode();
            } else {
                throw new RuntimeException("user_id não é um número válido nem um UUID: " + userIdString);
            }
        }
    }

    // Extrair username/email do token
    public String extractUsername(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new RuntimeException("Token JWT inválido");
            }

            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            JsonNode payloadNode = objectMapper.readTree(payload);
            
            if (payloadNode.has("email")) {
                return payloadNode.get("email").asText();
            } else if (payloadNode.has("sub")) {
                return payloadNode.get("sub").asText();
            }
            
            throw new RuntimeException("Username não encontrado no token");
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar token JWT: " + e.getMessage());
        }
    }

    // Verificar se o token expirou
    public boolean isTokenExpired(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            String[] parts = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            JsonNode payloadNode = objectMapper.readTree(payload);
            
            if (payloadNode.has("exp")) {
                long exp = payloadNode.get("exp").asLong();
                return new Date(exp * 1000).before(new Date());
            }
            
            return false; // Se não tem expiração, considera como não expirado
            
        } catch (Exception e) {
            return true; // Em caso de erro, considera como expirado
        }
    }

    // Extrair user_id como String (para UUIDs)
    public String extractUserIdAsString(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new RuntimeException("Token JWT inválido");
            }

            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            JsonNode payloadNode = objectMapper.readTree(payload);
            
            if (payloadNode.has("user_id")) {
                return payloadNode.get("user_id").asText();
            } else if (payloadNode.has("userId")) {
                return payloadNode.get("userId").asText();
            } else if (payloadNode.has("id")) {
                return payloadNode.get("id").asText();
            } else if (payloadNode.has("sub")) {
                return payloadNode.get("sub").asText();
            }
            
            throw new RuntimeException("user_id não encontrado no token");
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar token JWT: " + e.getMessage());
        }
    }

    // Validar token
    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}
