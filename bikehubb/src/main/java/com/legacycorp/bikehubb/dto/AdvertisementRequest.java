package com.legacycorp.bikehubb.dto;

import java.util.Date;

import lombok.Data;

@Data
public class AdvertisementRequest {

    private Long id;
    private Long userId;
    private String title;
    private String description;
    private Double price;
    private String brand;
    private String model;
    private Long year;
    private String condition;
    private String category;
    private String color;
    private String city;
    private String state;
    private String neighborhood;
    private String images[];
    private boolean isActive;
    private boolean isPaid;
    private Date expiresAt;
    private Date createdAt;
    private Date updatedAt;
}
