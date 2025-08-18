package com.legacycorp.bikehubb.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.events.Event;

@Service
public class StripeWebhookService {

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    public Event constructEvent(String payload, String sigHeader) throws SignatureVerificationException {
        return Webhook.constructEvent(payload, sigHeader, webhookSecret);
    }

    public void handleEvent(Event event) {
        switch (event.getType()) {
            case "payment_intent.succeeded":
                handlePaymentIntentSucceeded(event);
                break;

            case "payment_intent.payment_failed":
                handlePaymentIntentFailed(event);
                break;

            default:
                //Logar eventos nao tratados
                break;
        }
    }

    private void handlePaymentIntentSucceeded(Event event) {
        String paymentIntentId = event.getData().getObject().getId();
        // Lógica para atualizar o status do anúncio para publicado
        stripeService.handlePaymentSuccess(paymentIntentId);
    }

    private void handlePaymentIntentFailed(Event event) {
        String paymentIntentId = event.getData().getObject().getId();
        // Lógica para atualizar o status do anúncio para falhado
        stripeService.handlePaymentFailure(paymentIntentId);
    }

}
