package com.legacycorp.bikehubb.createAdvertisement.model;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "bicycles")
@Data
public class Bicycle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

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
    @Column(name = "is_active")
    private boolean isActive = true;
    
    @Column(name = "is_paid")
    private boolean isPaid = false;

    @Enumerated(EnumType.STRING)
    private AdvertisementStatus status = AdvertisementStatus.DRAFT;

    @Column(name = "user_id", nullable = true) // Temporariamente nullable para resolver problemas de migração
    private Long owner;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "published_at", nullable = true)
    private LocalDateTime publishedAt;
    
    @Column(name = "payment_intent_id", nullable = true)
    private String paymentIntentId;
    
    @Column(name = "payment_date", nullable = true)
    private LocalDateTime paymentDate;
    
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    // Relacionamento com imagens
    @OneToMany(mappedBy = "bicycle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BikeImage> images = new ArrayList<>();

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

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        // Definir data de expiração para 60 dias a partir da criação se não foi definida
        if (expiresAt == null) {
            expiresAt = LocalDateTime.now().plusDays(60);
        }
    }

    // Método auxiliar para definir o owner por ID
    public void setOwnerId(Long userId) {
        this.owner = userId;
    }
    
    // Método auxiliar para definir o owner diretamente
    public void setOwner(Long ownerId) {
        this.owner = ownerId;
    }

    // Método auxiliar para obter o ID do owner
    public String getOwnerId() {
        return this.owner != null ? this.owner.toString() : null;
    }

    // Método auxiliar para obter UUID do anúncio como String
    public String getIdAsString() {
        return this.id != null ? this.id.toString() : null;
    }

    // Método auxiliar para verificar se o anúncio está expirado
    public boolean isExpired() {
        return this.expiresAt != null && LocalDateTime.now().isAfter(this.expiresAt);
    }

    // Método auxiliar para renovar a expiração por mais 60 dias
    public void renewExpiration() {
        this.expiresAt = LocalDateTime.now().plusDays(60);
    }

    // Método auxiliar para renovar a expiração por um número específico de dias
    public void renewExpiration(int days) {
        this.expiresAt = LocalDateTime.now().plusDays(days);
    }
}
