package com.legacycorp.bikehubb.createAdvertisement.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentResponse {

    // Campos originais para Payment Intent (mantidos para compatibilidade)
    private String clientSecret;
    private String paymentIntentId;

    // Novos campos para Checkout Session
    private String sessionId;
    private String checkoutUrl;
    private UUID advertisementId;
    private BigDecimal amount;
    private String status;

    // Construtor padrão
    public PaymentResponse() {}

    // Construtor original para compatibilidade
    public PaymentResponse(String clientSecret, String paymentIntentId) {
        this.clientSecret = clientSecret;
        this.paymentIntentId = paymentIntentId;
    }

    // Construtor para Checkout Session
    public PaymentResponse(String sessionId, String checkoutUrl, UUID advertisementId, BigDecimal amount, String status) {
        this.sessionId = sessionId;
        this.checkoutUrl = checkoutUrl;
        this.advertisementId = advertisementId;
        this.amount = amount;
        this.status = status;
    }

    // Getters e Setters
    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getPaymentIntentId() {
        return paymentIntentId;
    }

    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    public void setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
    }

    public UUID getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(UUID advertisementId) {
        this.advertisementId = advertisementId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
