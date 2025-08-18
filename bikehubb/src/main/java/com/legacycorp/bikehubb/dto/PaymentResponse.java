package com.legacycorp.bikehubb.dto;

import lombok.Data;

@Data
public class PaymentResponse {

    private String clientSecret;
    private String paymentIntentId;
}
