package com.legacycorp.bikehubb.createAdvertisement.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PaymentRequest {

    private Long advertisementId;
    private String currency;
    private BigDecimal amount;
}
