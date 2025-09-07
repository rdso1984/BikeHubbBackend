package com.legacycorp.bikehubb.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Chave secreta para validação de assinatura
    // Em produção, isso deve vir de variável de ambiente ou ser configurado externamente
    @Value("${jwt.secret:mySecretKey}")
    private String secretKey;

    // Método para obter a chave secreta decodificada
    private SecretKey getSigningKey() {
        // Se a chave é menor que 256 bits, usar o algoritmo HMAC para expandir
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extrair todas as claims do token
    private Claims extractAllClaims(String token) {
        try {
            // Remove "Bearer " se presente
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new RuntimeException("Token JWT inválido ou malformado: " + e.getMessage());
        }
    }

    // Extrair uma claim específica
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extrair user_id do token JWT usando JJWT
    public Long extractUserId(String token) {
        try {
            Claims claims = extractAllClaims(token);
            
            // Debug: mostrar o conteúdo das claims
            System.out.println("Claims do token: " + claims);
            
            // Tentar extrair user_id de diferentes campos possíveis
            if (claims.containsKey("user_id")) {
                return parseUserId(claims.get("user_id").toString());
            } else if (claims.containsKey("userId")) {
                return parseUserId(claims.get("userId").toString());
            } else if (claims.containsKey("id")) {
                return parseUserId(claims.get("id").toString());
            } else if (claims.getSubject() != null) {
                String sub = claims.getSubject();
                System.out.println("Campo 'sub' encontrado: " + sub);
                return parseUserId(sub);
            }
            
            throw new RuntimeException("user_id não encontrado no token. Claims disponíveis: " + 
                claims.keySet().toString());
            
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
                return (long) Math.abs(userIdString.hashCode());
            } else {
                throw new RuntimeException("user_id não é um número válido nem um UUID: " + userIdString);
            }
        }
    }

    // Extrair username/email do token usando JJWT
    public String extractUsername(String token) {
        try {
            Claims claims = extractAllClaims(token);
            
            if (claims.containsKey("email")) {
                return claims.get("email").toString();
            } else if (claims.getSubject() != null) {
                return claims.getSubject();
            }
            
            throw new RuntimeException("Username não encontrado no token");
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar token JWT: " + e.getMessage());
        }
    }

    // Verificar se o token expirou usando JJWT
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = extractClaim(token, Claims::getExpiration);
            return expiration != null && expiration.before(new Date());
        } catch (Exception e) {
            return true; // Em caso de erro, considera como expirado
        }
    }

    // Extrair user_id como String (para UUIDs) usando JJWT
    public String extractUserIdAsString(String token) {
        try {
            Claims claims = extractAllClaims(token);
            
            if (claims.containsKey("user_id")) {
                return claims.get("user_id").toString();
            } else if (claims.containsKey("userId")) {
                return claims.get("userId").toString();
            } else if (claims.containsKey("id")) {
                return claims.get("id").toString();
            } else if (claims.getSubject() != null) {
                return claims.getSubject();
            }
            
            throw new RuntimeException("user_id não encontrado no token");
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar token JWT: " + e.getMessage());
        }
    }

    // Validar token com verificação de assinatura e expiração
    public boolean validateToken(String token) {
        try {
            // Tentar extrair as claims - isso já valida a assinatura
            extractAllClaims(token);
            // Se chegou até aqui, o token é válido em termos de assinatura
            // Agora verificar se não expirou
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}
