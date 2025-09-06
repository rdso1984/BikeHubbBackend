package com.legacycorp.bikehubb.createAdvertisement.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BicycleResponseDto {
    private String id;
    private String title;
    private String description;
    private BigDecimal price;
    private String category;
    private String brand;
    private String model;
    private Integer year;
    private String condition;
    private String frameSize;
    
    // Localização
    private String state;
    private String city;
    private String neighborhood;
    
    // Status e controle
    private Boolean is_active;
    private Boolean is_paid;
    private LocalDateTime expires_at;
    
    // Imagens
    private List<String> images;
    
    // Metadados
    private String user_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
