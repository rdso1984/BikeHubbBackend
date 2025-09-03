package com.legacycorp.bikehubb.createAdvertisement.controller;

import com.legacycorp.bikehubb.createAdvertisement.dto.AdvertisementRequest;
import com.legacycorp.bikehubb.createAdvertisement.dto.BicycleListResponseDTO;
import com.legacycorp.bikehubb.createAdvertisement.service.AdvertisementService;
import com.legacycorp.bikehubb.createAdvertisement.model.Bicycle;
import com.legacycorp.bikehubb.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api/advertisements")
public class AdvertisementController {

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/create-advertisement")
    public ResponseEntity<Map<String, String>> createAdvertisement(
        @ModelAttribute AdvertisementRequest request,
        @RequestHeader(value = "Authorization", required = false) String authHeader,
        HttpServletRequest httpRequest) {
        
        try {
            // Debug: Listar todos os headers recebidos
            System.out.println("=== DEBUG HEADERS ===");
            httpRequest.getHeaderNames().asIterator().forEachRemaining(headerName -> {
                System.out.println(headerName + ": " + httpRequest.getHeader(headerName));
            });
            System.out.println("Authorization header: " + authHeader);
            System.out.println("====================");
            
            // Verificar se o header Authorization foi enviado
            if (authHeader == null || authHeader.trim().isEmpty()) {
                return ResponseEntity.status(401).body(Map.of("error", "Token de autenticação é obrigatório"));
            }
            
            // Extrair userId do token JWT como String (UUID)
            String externalId = jwtUtil.extractUserIdAsString(authHeader);
            
            System.out.println("Chegou na API - Título: " + request.getTitle());
            System.out.println("Preço: " + request.getPrice());
            System.out.println("Categoria: " + request.getCategory());
            System.out.println("ExternalId extraído do token: " + externalId);
            
            if (request.getImages() != null) {
                System.out.println("Número de imagens: " + request.getImages().length);
            }
            
            // Validar token
            if (!jwtUtil.validateToken(authHeader)) {
                return ResponseEntity.status(401).body(Map.of("error", "Token inválido ou expirado"));
            }
            
            Bicycle advertisement = advertisementService.createAdvertisement(request, externalId, authHeader);
            
            // Processar imagens se fornecidas
            if (request.getImages() != null && request.getImages().length > 0) {
                advertisementService.processAdvertisementImages(request.getImages(), advertisement.getId());
            }
            
            return ResponseEntity.ok(Map.of("message", "Anúncio criado com sucesso!", "id", advertisement.getId().toString()));
            
        } catch (Exception e) {
            System.err.println("Erro ao criar anúncio: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", "Erro ao criar anúncio: " + e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<BicycleListResponseDTO>> getAllAdvertisements() {
        try {
            System.out.println("=== INICIANDO BUSCA DE ANÚNCIOS ===");
            List<Bicycle> advertisements = advertisementService.getAllAdvertisements();
            System.out.println("Encontrados " + advertisements.size() + " anúncios");
            
            // Converter para DTO
            List<BicycleListResponseDTO> response = advertisements.stream()
                .map(BicycleListResponseDTO::fromBicycle)
                .collect(Collectors.toList());
            
            System.out.println("Conversão para DTO concluída");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Erro ao buscar anúncios: " + e.getMessage());
            e.printStackTrace(); // Stack trace completo
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<BicycleListResponseDTO>> searchAdvertisements(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "neighborhood", required = false) String neighborhood,
            @RequestParam(value = "min_price", required = false) String minPrice,
            @RequestParam(value = "max_price", required = false) String maxPrice,
            @RequestParam(value = "condition", required = false) String condition,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "sort", defaultValue = "newest") String sort) {
        try {
            
            // Debug: Log dos parâmetros recebidos
            System.out.println("=== BUSCA DE ANÚNCIOS ===");
            System.out.println("State: " + state);
            System.out.println("City: " + city);
            System.out.println("Neighborhood: " + neighborhood);
            System.out.println("Min Price: " + minPrice);
            System.out.println("Max Price: " + maxPrice);
            System.out.println("Condition: " + condition);
            System.out.println("Category: " + category);
            System.out.println("Brand: " + brand);
            System.out.println("Sort: " + sort);
            System.out.println("========================");
            
            // Buscar anúncios com os filtros aplicados
            List<Bicycle> advertisements = advertisementService.searchAdvertisements(
                state, city, neighborhood, minPrice, maxPrice, 
                condition, category, brand, sort);
            
            // Converter para DTO
            List<BicycleListResponseDTO> response = advertisements.stream()
                .map(BicycleListResponseDTO::fromBicycle)
                .collect(Collectors.toList());
                
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Erro ao buscar anúncios: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
}
