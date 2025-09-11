package com.legacycorp.bikehubb.createAdvertisement.controller;

import com.legacycorp.bikehubb.createAdvertisement.dto.AdvertisementRequest;
import com.legacycorp.bikehubb.createAdvertisement.dto.BicycleListResponseDTO;
import com.legacycorp.bikehubb.createAdvertisement.dto.BikeImageSummaryDTO;
import com.legacycorp.bikehubb.createAdvertisement.dto.UserAdvertisementResponseDTO;
import com.legacycorp.bikehubb.createAdvertisement.service.AdvertisementService;
import com.legacycorp.bikehubb.createAdvertisement.model.Bicycle;
import com.legacycorp.bikehubb.createAdvertisement.repository.BikeImageRepository;
import com.legacycorp.bikehubb.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/advertisements")
public class AdvertisementController {

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private BikeImageRepository bikeImageRepository;

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
    @Transactional(readOnly = true) // Manter sessão aberta durante a leitura
    public ResponseEntity<List<BicycleListResponseDTO>> getAllAdvertisements() {
        try {
            System.out.println("=== INICIANDO BUSCA OTIMIZADA DE ANÚNCIOS ===");
            
            // Buscar anúncios SEM carregar imagens (mais rápido)
            List<Bicycle> advertisements = advertisementService.getAllAdvertisements();
            System.out.println("Encontrados " + advertisements.size() + " anúncios");
            
            // Converter para DTO de forma otimizada
            List<BicycleListResponseDTO> response = advertisements.stream()
                .map(bicycle -> {
                    // Buscar apenas metadados das imagens (SEM imageData)
                    List<BikeImageSummaryDTO> imageSummaries = 
                        bikeImageRepository.findImageSummariesByBicycleId(bicycle.getId());
                    
                    return BicycleListResponseDTO.fromBicycleOptimized(bicycle, imageSummaries);
                })
                .collect(Collectors.toList());
            
            System.out.println("Conversão otimizada para DTO concluída");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Erro ao buscar anúncios: " + e.getMessage());
            e.printStackTrace(); // Stack trace completo
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/search")
    @Transactional(readOnly = true) // Manter sessão aberta durante a leitura
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
            System.out.println("=== BUSCA OTIMIZADA DE ANÚNCIOS ===");
            System.out.println("State: " + state);
            System.out.println("City: " + city);
            System.out.println("Neighborhood: " + neighborhood);
            System.out.println("Min Price: " + minPrice);
            System.out.println("Max Price: " + maxPrice);
            System.out.println("Condition: " + condition);
            System.out.println("Category: " + category);
            System.out.println("Brand: " + brand);
            System.out.println("Sort: " + sort);
            System.out.println("====================================");
            
            // Buscar anúncios com os filtros aplicados
            List<Bicycle> advertisements = advertisementService.searchAdvertisements(
                state, city, neighborhood, minPrice, maxPrice, 
                condition, category, brand, sort);
            
            System.out.println("Encontrados " + advertisements.size() + " anúncios filtrados");
            
            // Converter para DTO de forma otimizada (SEM carregar dados binários das imagens)
            List<BicycleListResponseDTO> response = advertisements.stream()
                .map(bicycle -> {
                    // Buscar apenas metadados das imagens (SEM imageData)
                    List<BikeImageSummaryDTO> imageSummaries = 
                        bikeImageRepository.findImageSummariesByBicycleId(bicycle.getId());
                    
                    return BicycleListResponseDTO.fromBicycleOptimized(bicycle, imageSummaries);
                })
                .collect(Collectors.toList());
            
            System.out.println("Conversão otimizada para DTO concluída");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Erro ao buscar anúncios: " + e.getMessage());
            e.printStackTrace(); // Stack trace completo
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user")
    @Transactional(readOnly = true)
    public ResponseEntity<List<UserAdvertisementResponseDTO>> getUserAdvertisements(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            System.out.println("=== INICIANDO BUSCA DE ANÚNCIOS DO USUÁRIO ===");
            
            // Verificar se o header Authorization foi enviado
            if (authHeader == null || authHeader.trim().isEmpty()) {
                return ResponseEntity.status(401).body(null);
            }
            
            // Validar token
            if (!jwtUtil.validateToken(authHeader)) {
                return ResponseEntity.status(401).body(null);
            }
            
            // Extrair userId do token JWT como String (UUID)
            String externalId = jwtUtil.extractUserIdAsString(authHeader);
            System.out.println("ExternalId extraído do token: " + externalId);
            
            // Buscar anúncios do usuário
            List<Bicycle> userAdvertisements = advertisementService.getUserAdvertisements(externalId);
            System.out.println("Encontrados " + userAdvertisements.size() + " anúncios do usuário");
            
            // Converter para DTO específico do usuário com formato correto
            List<UserAdvertisementResponseDTO> response = userAdvertisements.stream()
                .map(bicycle -> {
                    // Buscar apenas metadados das imagens (SEM imageData)
                    List<BikeImageSummaryDTO> imageSummaries = 
                        bikeImageRepository.findImageSummariesByBicycleId(bicycle.getId());
                    
                    return UserAdvertisementResponseDTO.fromBicycleOptimized(bicycle, externalId, imageSummaries);
                })
                .collect(Collectors.toList());
            
            System.out.println("Conversão otimizada para DTO do usuário concluída");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Erro ao buscar anúncios do usuário: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Exclui um anúncio do usuário autenticado
     * @param advertisementId ID do anúncio a ser excluído
     * @param authHeader Token JWT de autenticação
     * @return Resposta de sucesso ou erro
     */
    @DeleteMapping("/exclude-advertisement")
    @Transactional
    public ResponseEntity<Map<String, String>> deleteUserAdvertisement(
            @RequestParam("id") String advertisementId,
            @RequestHeader(value = "Authorization", required = true) String authHeader) {
        
        try {
            System.out.println("=== INICIANDO EXCLUSÃO DE ANÚNCIO ===");
            System.out.println("Advertisement ID recebido: " + advertisementId);
            
            // Verificar se o header Authorization foi enviado
            if (authHeader == null || authHeader.trim().isEmpty()) {
                return ResponseEntity.status(401).body(Map.of("error", "Token de autenticação é obrigatório"));
            }
            
            // Validar token
            if (!jwtUtil.validateToken(authHeader)) {
                return ResponseEntity.status(401).body(Map.of("error", "Token inválido ou expirado"));
            }
            
            // Extrair userId do token JWT como String (UUID)
            String externalId = jwtUtil.extractUserIdAsString(authHeader);
            System.out.println("ExternalId extraído do token: " + externalId);
            
            // Converter string para UUID
            UUID advertisementUuid;
            try {
                advertisementUuid = UUID.fromString(advertisementId);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Map.of("error", "ID do anúncio inválido"));
            }
            
            // Excluir o anúncio
            boolean deleted = advertisementService.deleteUserAdvertisement(advertisementUuid, externalId);
            
            if (deleted) {
                return ResponseEntity.ok(Map.of(
                    "message", "Anúncio excluído com sucesso!",
                    "advertisementId", advertisementId
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Erro ao excluir anúncio"));
            }
            
        } catch (RuntimeException e) {
            System.err.println("Erro de negócio ao excluir anúncio: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            System.err.println("Erro inesperado ao excluir anúncio: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Erro interno do servidor"));
        }
    }
    
}
