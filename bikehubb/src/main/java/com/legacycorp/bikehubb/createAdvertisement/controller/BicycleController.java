package com.legacycorp.bikehubb.createAdvertisement.controller;

import com.legacycorp.bikehubb.createAdvertisement.dto.BicycleResponseDto;
import com.legacycorp.bikehubb.createAdvertisement.service.BicycleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bicycles")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {
    "https://bikehubb.netlify.app",
    "https://bikehubbbackend.onrender.com",
    "http://localhost:3000",
    "http://localhost:3001",
    "http://localhost:4200",
    "http://localhost:4201"
}, allowCredentials = "true")
public class BicycleController {

    private final BicycleService bicycleService;

    @GetMapping("/user")
    public ResponseEntity<List<BicycleResponseDto>> getUserBicycles(
            @RequestHeader("Authorization") String authorizationHeader) {
        
        log.info("Recebida requisição para buscar bicicletas do usuário");
        
        try {
            // Extrair token do header Authorization
            String token = extractTokenFromHeader(authorizationHeader);
            
            // Buscar bicicletas do usuário
            List<BicycleResponseDto> userBicycles = bicycleService.getBicyclesByUser(token);
            
            log.info("Encontradas {} bicicletas para o usuário", userBicycles.size());
            
            return ResponseEntity.ok(userBicycles);
            
        } catch (Exception e) {
            log.error("Erro ao buscar bicicletas do usuário: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new IllegalArgumentException("Token JWT inválido");
    }
}
