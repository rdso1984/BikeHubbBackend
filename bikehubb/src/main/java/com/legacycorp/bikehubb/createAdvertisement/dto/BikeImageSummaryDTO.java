package com.legacycorp.bikehubb.createAdvertisement.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class BikeImageSummaryDTO {
    
    private UUID id;
    private String originalFilename;
    private String contentType;
    private Long fileSize;
    private boolean isPrimary;
    private LocalDateTime createdAt;
    private String imageUrl; // URL para acessar a imagem

    // Construtor padrão
    public BikeImageSummaryDTO() {}

    // Construtor com todos os campos
    public BikeImageSummaryDTO(UUID id, String originalFilename, String contentType, 
                               Long fileSize, boolean isPrimary, LocalDateTime createdAt, String imageUrl) {
        this.id = id;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.isPrimary = isPrimary;
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;
    }

    // Método estático para converter de BikeImage para DTO
    public static BikeImageSummaryDTO fromBikeImage(com.legacycorp.bikehubb.createAdvertisement.model.BikeImage bikeImage) {
        String imageUrl = "/api/images/" + bikeImage.getId();
        
        return new BikeImageSummaryDTO(
            bikeImage.getId(),
            bikeImage.getOriginalFilename(),
            bikeImage.getContentType(),
            bikeImage.getFileSize(),
            bikeImage.isPrimary(),
            bikeImage.getCreatedAt(),
            imageUrl
        );
    }

    // Método auxiliar para obter tamanho formatado
    public String getFormattedSize() {
        if (fileSize == null) return "0 B";
        
        long bytes = fileSize;
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }
}
