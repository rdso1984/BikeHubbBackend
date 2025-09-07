package com.legacycorp.bikehubb.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.legacycorp.bikehubb.exception.StripeException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

@Service
public class StripeWebhookService {

    // private final StripeService stripeService;

    // @Value("${stripe.webhook.secret}")
    // private String webhookSecret;

    // StripeWebhookService(StripeService stripeService) {
    //     this.stripeService = stripeService;
    // }

    // public Event constructEvent(String payload, String sigHeader) throws SignatureVerificationException {
    //     return Webhook.constructEvent(payload, sigHeader, webhookSecret);
    // }

    // public void handleEvent(Event event) throws com.stripe.exception.StripeException {
    //     switch (event.getType()) {
    //         case "payment_intent.succeeded":
    //             handlePaymentIntentSucceeded(event);
    //             break;
    //         case "payment_intent.payment_failed":
    //             handlePaymentIntentFailed(event);
    //             break;
    //         case "payment_intent.canceled":
    //             handlePaymentIntentCanceled(event);
    //             break;
    //         default:
    //             //Logar eventos nao tratados
    //             break;
    //     }
    // }

    // private void handlePaymentIntentSucceeded(Event event) throws StripeException, com.stripe.exception.StripeException {

    //     PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject()
    //         .orElseThrow(
    //             () -> new IllegalArgumentException("Objeto PaymentIntent não encontrado no evento"));

    //     stripeService.handlePaymentSuccess(paymentIntent.getId());
    
    //     // String paymentIntentId = event.getData().getObject().getId();
    //     // Lógica para atualizar o status do anúncio para publicado


    // }

    // private void handlePaymentIntentFailed(Event event) throws StripeException, com.stripe.exception.StripeException {
    //     PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject()
    //         .orElseThrow(
    //             () -> new IllegalArgumentException("Objeto PaymentIntent não encontrado no evento"));

    //     stripeService.handlePaymentFailure(paymentIntent.getId());
        
    //     // Aqui você pode adicionar notificações ou lógica adicional
    // }

    // private void handlePaymentIntentCanceled(Event event) throws StripeException, com.stripe.exception.StripeException {
    //     PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElseThrow(
    //             () -> new IllegalArgumentException("Objeto PaymentIntent não encontrado no evento"));
        
    //     // Trata cancelamento similar à falha (ou pode ter lógica diferente)
    //     stripeService.handlePaymentFailure(paymentIntent.getId());
    // }

}
