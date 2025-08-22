package com.legacycorp.bikehubb.createAdvertisement.model;

import java.time.LocalDateTime;
import java.math.BigDecimal;

import com.legacycorp.bikehubb.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private BigDecimal price;
    private String category;
    
    // Campos adicionais para bicicletas
    private String brand;
    private String model;
    private Integer year;
    private String condition;
    private String frameSize;
    private String color;
    
    // Campos de localização
    private String city;
    private String state;
    private String neighborhood;
    
    // Campos de status
    private boolean isActive = true;
    private boolean isPaid = false;

    @Enumerated(EnumType.STRING)
    private AdvertisementStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;
    private String paymentIntentId;
    private LocalDateTime paymentDate;

    public enum AdvertisementStatus {
        DRAFT,
        PENDING_PAYMENT,
        PUBLISHED,
        REJECTED,
        EXPIRED
    }

    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
}
