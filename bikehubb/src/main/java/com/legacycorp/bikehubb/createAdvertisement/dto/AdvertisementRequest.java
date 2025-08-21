package com.legacycorp.bikehubb.createAdvertisement.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AdvertisementRequest {

    // private Long id;
    // private Long userId;
    private String title;
    private String description;
    private Double price;
    private String brand;
    private String model;
    private Integer year;
    private String condition;
    private String category;
    private String frameSize;
    private String color;
    private String city;
    private String state;
    private String neighborhood;
    private MultipartFile images[];
    private boolean isActive;
    private boolean isPaid;
}
