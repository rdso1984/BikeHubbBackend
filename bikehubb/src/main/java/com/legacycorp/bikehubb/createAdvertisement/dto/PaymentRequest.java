package com.legacycorp.bikehubb.createAdvertisement.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;

@Data
public class PaymentRequest {

    private UUID advertisementId;
    private String currency;
    private BigDecimal amount;
}
