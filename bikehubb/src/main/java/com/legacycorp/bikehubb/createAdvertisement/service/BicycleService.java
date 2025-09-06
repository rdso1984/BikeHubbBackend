package com.legacycorp.bikehubb.createAdvertisement.service;

import com.legacycorp.bikehubb.createAdvertisement.dto.BicycleResponseDto;
import com.legacycorp.bikehubb.createAdvertisement.model.Bicycle;
import com.legacycorp.bikehubb.createAdvertisement.model.BikeImage;
import com.legacycorp.bikehubb.createAdvertisement.repository.AdvertisementRepository;
import com.legacycorp.bikehubb.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BicycleService {

    private final AdvertisementRepository advertisementRepository;
    private final JwtUtil jwtUtil;

    public List<BicycleResponseDto> getBicyclesByUser(String token) {
        try {
            // Extrair user_id do token JWT
            String userIdAsString = jwtUtil.extractUserIdAsString(token);
            log.info("Extraído userIdAsString do token: {}", userIdAsString);
            
            // Buscar bicicletas do usuário pelo ID  
            Long userId = Long.parseLong(userIdAsString);
            List<Bicycle> bicycles = advertisementRepository.findByOwner(userId);
            log.info("Encontradas {} bicicletas para o usuário", bicycles.size());
            
            // Converter para DTO
            return bicycles.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            log.error("Erro ao buscar bicicletas do usuário: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar bicicletas do usuário", e);
        }
    }
    
    private BicycleResponseDto convertToDto(Bicycle bicycle) {
        // Converter imagens para URLs
        List<String> imageUrls = bicycle.getImages() != null ? 
            bicycle.getImages().stream()
                .map(this::convertImageToUrl)
                .filter(url -> url != null)
                .collect(Collectors.toList()) : 
            List.of();
        
        return BicycleResponseDto.builder()
                .id(bicycle.getId().toString())
                .title(bicycle.getTitle())
                .description(bicycle.getDescription())
                .price(bicycle.getPrice())
                .category(bicycle.getCategory())
                .brand(bicycle.getBrand())
                .model(bicycle.getModel())
                .year(bicycle.getYear())
                .condition(bicycle.getCondition())
                .frameSize(bicycle.getFrameSize())
                .state(bicycle.getState())
                .city(bicycle.getCity())
                .neighborhood(bicycle.getNeighborhood())
                .is_active(bicycle.isActive())
                .is_paid(bicycle.isPaid())
                .expires_at(bicycle.getExpiresAt())
                .images(imageUrls)
                .user_id(bicycle.getOwner() != null ? bicycle.getOwner().toString() : null)
                .created_at(bicycle.getCreatedAt())
                .updated_at(bicycle.getCreatedAt()) // Por enquanto usando created_at como updated_at
                .build();
    }
    
    private String convertImageToUrl(BikeImage image) {
        // Converter imagem para base64 data URL
        if (image.getImageData() != null && image.getContentType() != null) {
            String base64Data = java.util.Base64.getEncoder().encodeToString(image.getImageData());
            return "data:" + image.getContentType() + ";base64," + base64Data;
        }
        return null;
    }
}
