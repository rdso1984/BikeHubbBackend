package com.legacycorp.bikehubb.createAdvertisement.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class BicycleListResponseDTO {
    
    private UUID id;
    private String title;
    private String description;
    private BigDecimal price;
    private String category;
    private String brand;
    private String model;
    private Integer year;
    private String condition;
    private String frameSize;
    private String color;
    private String city;
    private String state;
    private String neighborhood;
    private List<BikeImageSummaryDTO> images;

    // Construtor padrão
    public BicycleListResponseDTO() {}

    // Construtor com todos os campos
    public BicycleListResponseDTO(UUID id, String title, String description, BigDecimal price, 
                                  String category, String brand, String model, Integer year, 
                                  String condition, String frameSize, String color, String city, 
                                  String state, String neighborhood, List<BikeImageSummaryDTO> images) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.condition = condition;
        this.frameSize = frameSize;
        this.color = color;
        this.city = city;
        this.state = state;
        this.neighborhood = neighborhood;
        this.images = images;
    }

    // Método estático para converter de Bicycle para DTO de forma otimizada
    public static BicycleListResponseDTO fromBicycleOptimized(
            com.legacycorp.bikehubb.createAdvertisement.model.Bicycle bicycle,
            java.util.List<BikeImageSummaryDTO> imageSummaries) {
            
        return new BicycleListResponseDTO(
            bicycle.getId(),
            bicycle.getTitle(),
            bicycle.getDescription(),
            bicycle.getPrice(),
            bicycle.getCategory(),
            bicycle.getBrand(),
            bicycle.getModel(),
            bicycle.getYear(),
            bicycle.getCondition(),
            bicycle.getFrameSize(),
            bicycle.getColor(),
            bicycle.getCity(),
            bicycle.getState(),
            bicycle.getNeighborhood(),
            imageSummaries
        );
    }

    // Método estático para converter de Bicycle para DTO (mantido para compatibilidade)
    public static BicycleListResponseDTO fromBicycle(com.legacycorp.bikehubb.createAdvertisement.model.Bicycle bicycle) {
        // Converter as imagens para BikeImageSummaryDTO
        List<BikeImageSummaryDTO> imageSummaries = bicycle.getImages().stream()
            .map(BikeImageSummaryDTO::fromBikeImage)
            .collect(Collectors.toList());
            
        return new BicycleListResponseDTO(
            bicycle.getId(),
            bicycle.getTitle(),
            bicycle.getDescription(),
            bicycle.getPrice(),
            bicycle.getCategory(),
            bicycle.getBrand(),
            bicycle.getModel(),
            bicycle.getYear(),
            bicycle.getCondition(),
            bicycle.getFrameSize(),
            bicycle.getColor(),
            bicycle.getCity(),
            bicycle.getState(),
            bicycle.getNeighborhood(),
            imageSummaries
        );
    }
}
