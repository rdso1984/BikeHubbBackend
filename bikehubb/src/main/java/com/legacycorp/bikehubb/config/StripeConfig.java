package com.legacycorp.bikehubb.config;

import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

@Configuration
public class StripeConfig {
    
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @PostConstruct
    public void initStripe(){
        Stripe.apiKey = stripeSecretKey;
    }
}
