package com.legacycorp.bikehubb.createAdvertisement.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class UserAdvertisementResponseDTO {
    
    private UUID id;
    private String user_id; // Campo que o frontend espera
    private String title;
    private BigDecimal price;
    private boolean is_active; // Campo que o frontend espera
    private boolean is_paid; // Campo que o frontend espera
    private LocalDateTime expires_at; // Campo que o frontend espera
    private List<BikeImageSummaryDTO> images;
    
    // Campos adicionais
    private String description;
    private String category;
    private String brand;
    private String model;
    private Integer year;
    private String condition;
    private String color;
    private String frameSize;
    private String city;
    private String state;
    private String neighborhood;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;
    private LocalDateTime paymentDate;
    private String paymentIntentId;

    // Construtor padrão
    public UserAdvertisementResponseDTO() {}

    // Construtor completo
    public UserAdvertisementResponseDTO(UUID id, String user_id, String title, BigDecimal price, 
                                       boolean is_active, boolean is_paid, LocalDateTime expires_at,
                                       List<BikeImageSummaryDTO> images, String description, String category,
                                       String brand, String model, Integer year, String condition, 
                                       String color, String frameSize, String city, String state,
                                       String neighborhood, String status, LocalDateTime createdAt,
                                       LocalDateTime publishedAt, LocalDateTime paymentDate, 
                                       String paymentIntentId) {
        this.id = id;
        this.user_id = user_id;
        this.title = title;
        this.price = price;
        this.is_active = is_active;
        this.is_paid = is_paid;
        this.expires_at = expires_at;
        this.images = images;
        this.description = description;
        this.category = category;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.condition = condition;
        this.color = color;
        this.frameSize = frameSize;
        this.city = city;
        this.state = state;
        this.neighborhood = neighborhood;
        this.status = status;
        this.createdAt = createdAt;
        this.publishedAt = publishedAt;
        this.paymentDate = paymentDate;
        this.paymentIntentId = paymentIntentId;
    }

    // Método estático para converter de Bicycle para DTO otimizado
    public static UserAdvertisementResponseDTO fromBicycleOptimized(
            com.legacycorp.bikehubb.createAdvertisement.model.Bicycle bicycle,
            String userExternalId,
            java.util.List<BikeImageSummaryDTO> imageSummaries) {
            
        return new UserAdvertisementResponseDTO(
            bicycle.getId(),
            userExternalId, // user_id que o frontend espera
            bicycle.getTitle(),
            bicycle.getPrice(),
            bicycle.isActive(), // is_active
            bicycle.isPaid(), // is_paid
            bicycle.getExpiresAt(), // expires_at
            imageSummaries,
            bicycle.getDescription(),
            bicycle.getCategory(),
            bicycle.getBrand(),
            bicycle.getModel(),
            bicycle.getYear(),
            bicycle.getCondition(),
            bicycle.getColor(),
            bicycle.getFrameSize(),
            bicycle.getCity(),
            bicycle.getState(),
            bicycle.getNeighborhood(),
            bicycle.getStatus() != null ? bicycle.getStatus().toString() : null,
            bicycle.getCreatedAt(),
            bicycle.getPublishedAt(),
            bicycle.getPaymentDate(),
            bicycle.getPaymentIntentId()
        );
    }
}
