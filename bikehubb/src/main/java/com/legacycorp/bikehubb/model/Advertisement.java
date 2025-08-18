package com.legacycorp.bikehubb.model;

import java.time.LocalDateTime;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.cglib.core.Local;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import java.math.BigDecimal;

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

    @Enumerated(EnumType.STRING)
    private AdvertisementStatus status;

    @ManyToOne
    private User owner;

    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;

    public enum AdvertisementStatus {
        DRAFT,
        PENDING_PAYMENT,
        PUBLISHED,
        REJECTED,
        EXPIRED
    }
}
