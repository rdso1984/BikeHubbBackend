package com.legacycorp.bikehubb.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import com.legacycorp.bikehubb.createAdvertisement.dto.PaymentRequest;
import com.legacycorp.bikehubb.createAdvertisement.dto.PaymentResponse;
import com.legacycorp.bikehubb.createAdvertisement.repository.AdvertisementRepository;
import com.legacycorp.bikehubb.exception.StripePaymentException;
import com.legacycorp.bikehubb.exception.StripeWebhookException;
import com.legacycorp.bikehubb.model.Advertisement;
import com.legacycorp.bikehubb.model.User;
import com.legacycorp.bikehubb.model.Advertisement.AdvertisementStatus;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodAttachParams;

@Service
public class StripeService {

    private final AdvertisementRepository advertisementRepository;
        // Defina o webhookSecret (idealmente via configuração)
        private final String webhookSecret = "SUA_CHAVE_WEBHOOK_AQUI"; // Substitua pelo valor real

    public StripeService(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

    public PaymentResponse createPaymentIntent (PaymentRequest paymentRequest) throws StripePaymentException { 

        try {
            //Validar e buscar o anuncio
            Advertisement advertisement = advertisementRepository.findById(paymentRequest.getAdvertisementId())
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));

            // Criar parâmetros para o PaymentIntent
                Map<String, String> metadata = new HashMap<>();
                metadata.put("advertisement_id", advertisement.getId().toString());
                // Corrigir para pegar o id do User corretamente
                if (advertisement.getOwner() != null && advertisement.getOwner().getId() != null) {
                    metadata.put("user_id", advertisement.getOwner().getId().toString());
                }

                PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(convertToCents(paymentRequest.getAmount()))
                    .setCurrency(paymentRequest.getCurrency().toLowerCase())
                    .putAllMetadata(metadata)
                    .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                            .setEnabled(true)
                            .build()
                    )
                    .build();

                PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Atualizar status do anúncio
            advertisement.setStatus(AdvertisementStatus.PENDING_PAYMENT);
            advertisement.setPaymentIntentId(paymentIntent.getId());
            advertisementRepository.save(advertisement);

            // Retornar resposta
            return new PaymentResponse(
                paymentIntent.getClientSecret(),
                paymentIntent.getId()
            );

        } catch (StripeException e) {
            String userMessage = "Erro ao processar pagamento. Por favor, tente novamente.";
        
            // Mapeia códigos de erro específicos para mensagens amigáveis
            switch (e.getCode()) {
                case "card_declined":
                    userMessage = "Seu cartão foi recusado. Por favor, use outro método de pagamento.";
                    break;
                case "insufficient_funds":
                    userMessage = "Fundos insuficientes no cartão.";
                    break;
                // Adicione outros casos conforme necessário
            }
        
            throw new StripePaymentException(
                "Erro no Stripe: " + e.getMessage(),
                e.getCode(),
                userMessage,
                e
            );
        } catch (Exception e) {
            throw new StripePaymentException(
                "Erro interno ao criar PaymentIntent",
                "internal_error",
                "Erro ao processar pagamento. Por favor, tente novamente mais tarde.",
                e
            );
        }
    }

    public void handlePaymentSuccess(String paymentIntentId) throws StripeException {
        
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        String advertisementId = paymentIntent.getMetadata().get("advertisement_id");
        Advertisement advertisement = advertisementRepository.findById(Long.parseLong(advertisementId))
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));

                advertisement.setStatus(AdvertisementStatus.PUBLISHED);
                advertisement.setPublishedAt(LocalDateTime.now());
                advertisement.setPaymentIntentId(paymentIntentId);
                advertisement.setPaymentDate(LocalDateTime.now());
                advertisementRepository.save(advertisement);
    }

    private long convertToCents(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(100)).longValue();
    }

    public void handleWebhookEvent(String payload, String sigHeader) throws StripeWebhookException {
    try {
        Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        // Processar evento...
        } catch (SignatureVerificationException e) {
            throw new StripeWebhookException(
                "Assinatura do webhook inválida",
                "invalid_signature",
                "Falha na verificação do webhook",
                e
            );
        } catch (Exception e) {
            throw new StripeWebhookException(
                "Erro ao processar webhook",
                "webhook_processing_error",
                "Falha no processamento do webhook",
                e
            );
        }
    }

    // Adicione esses métodos ao StripeService
    public String createStripeCustomer(User user) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("email", user.getEmail());
        params.put("name", user.getName());
        params.put("phone", user.getPhone());
        
        Customer customer = Customer.create(params);
        return customer.getId();
    }

    public void attachPaymentMethodToCustomer(String customerId, String paymentMethodId) throws StripeException {
        PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
        paymentMethod.attach(new PaymentMethodAttachParams.Builder()
                .setCustomer(customerId)
                .build());
    }

    public void handlePaymentFailure(String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        
        String advertisementId = paymentIntent.getMetadata().get("advertisement_id");
        Advertisement advertisement = advertisementRepository.findById(Long.parseLong(advertisementId))
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));
        
        // Mantém como PENDING_PAYMENT para permitir nova tentativa
        // ou pode mudar para FAILED conforme sua regra de negócio
        advertisement.setStatus(AdvertisementStatus.PENDING_PAYMENT);
        advertisement.setPaymentIntentId(null); // Remove o payment intent falho
        advertisementRepository.save(advertisement);
        
        // Aqui você pode adicionar lógica para:
        // - Notificar o usuário sobre a falha
        // - Registrar a tentativa falha
        // - Enviar alertas para a equipe
    }

    /**
     * Método para verificar o status de um pagamento
     */
    public PaymentIntent checkPaymentStatus(String paymentIntentId) throws StripeException {
        return PaymentIntent.retrieve(paymentIntentId);
    }

    /**
     * Método para cancelar um pagamento pendente
     */
    public void cancelPendingPayment(String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        
        // Só cancela se ainda estiver em estado cancelável
        if ("requires_payment_method".equals(paymentIntent.getStatus())) {
            PaymentIntent canceledIntent = paymentIntent.cancel();
            
            String advertisementId = canceledIntent.getMetadata().get("advertisement_id");
            Advertisement advertisement = advertisementRepository.findById(Long.parseLong(advertisementId))
                    .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));
            
            advertisement.setStatus(AdvertisementStatus.DRAFT);
            advertisement.setPaymentIntentId(null);
            advertisementRepository.save(advertisement);
        }
    }

}
