package com.legacycorp.bikehubb.controller;

import java.security.Signature;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.legacycorp.bikehubb.dto.PaymentRequest;
import com.legacycorp.bikehubb.dto.PaymentResponse;
import com.legacycorp.bikehubb.service.StripeService;
import com.legacycorp.bikehubb.service.StripeWebhookService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final StripeService stripeService;
    private final StripeWebhookService stripeWebhookService;

    public PaymentController(StripeService stripeService, StripeWebhookService stripeWebhookService) {
        this.stripeService = stripeService;
        this.stripeWebhookService = stripeWebhookService;
    }

    @PostMapping("/create-payment-intent")
    public PaymentResponse createPaymentIntent(@RequestBody PaymentRequest paymentRequest) throws StripeException {
        return stripeService.createPaymentIntent(paymentRequest);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook (@RequestBody String payload, @RequestHeader("Stripe-signature") String sigHeader) {
        // Implementar lógica para lidar com o webhook do Stripe
        // Isso geralmente envolve verificar o tipo de evento e atualizar o status do anúncio
        try {
            // Verificar a assinatura do webhook
            Event event = stripeWebhookService.constructEvent(payload, sigHeader);
            stripeWebhookService.handleEvent(event);
            return ResponseEntity.ok().build();
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("Assinatura Inválida");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao processar webhook");
        }
    }

    private String parseEventFromPayload(String payload) {
        // Implementar parsing do payload do webhook
        // Isso é simplificado - na prática use a biblioteca do Stripe para verificar a assinatura
        return "payment_intent_id_simulado";
    }
}
