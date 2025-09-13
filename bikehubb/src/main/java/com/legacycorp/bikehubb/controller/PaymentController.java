package com.legacycorp.bikehubb.controller;

import com.legacycorp.bikehubb.createAdvertisement.dto.PaymentRequest;
import com.legacycorp.bikehubb.security.JwtUtil;
import com.legacycorp.bikehubb.createAdvertisement.model.Bicycle;
import com.legacycorp.bikehubb.createAdvertisement.repository.AdvertisementRepository;
import com.legacycorp.bikehubb.createAdvertisement.repository.UserRepository;
import com.legacycorp.bikehubb.model.User;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Autowired
    private AdvertisementRepository advertisementRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Cria uma sessão de checkout do Stripe para pagamento de anúncio
     * @param request Dados do pagamento (anuncio ID, etc.)
     * @param authHeader Token JWT de autenticação
     * @return URL de checkout do Stripe ou erro
     */
    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(
            @RequestBody PaymentRequest request,
            @RequestHeader(value = "Authorization", required = true) String authHeader) {
        
        try {
            System.out.println("=== INICIANDO CRIAÇÃO DE SESSÃO DE CHECKOUT ===");
            System.out.println("Request recebido para Advertisement ID: " + request.getAdvertisementId());
            
            // Verificar se o header Authorization foi enviado
            if (authHeader == null || authHeader.trim().isEmpty()) {
                System.err.println("❌ Header Authorization não fornecido");
                return ResponseEntity.status(401).body(Map.of("error", "Token de autenticação é obrigatório"));
            }
            
            // Validar token JWT
            if (!jwtUtil.validateToken(authHeader)) {
                System.err.println("❌ Token JWT inválido");
                return ResponseEntity.status(401).body(Map.of("error", "Token inválido ou expirado"));
            }
            
            // Extrair userId do token JWT
            String externalId = jwtUtil.extractUserIdAsString(authHeader);
            System.out.println("✅ ExternalId extraído do token: " + externalId);
            
            // Validar dados da requisição
            if (request.getAdvertisementId() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "ID do anúncio é obrigatório"));
            }
            
            // Configurar chave secreta do Stripe
            Stripe.apiKey = stripeSecretKey;
            
            // Validar usuário
            Optional<User> userOpt = userRepository.findByExternalId(externalId);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Usuário não encontrado"));
            }
            User user = userOpt.get();
            
            // Validar anúncio
            UUID advertisementId = request.getAdvertisementId();
            Optional<Bicycle> advertisementOpt = advertisementRepository.findById(advertisementId);
            if (advertisementOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Anúncio não encontrado"));
            }
            
            Bicycle advertisement = advertisementOpt.get();
            System.out.println("✅ Anúncio encontrado: " + advertisement.getTitle());
            
            // Verificar se o anúncio pertence ao usuário
            if (!advertisement.getOwner().equals(user.getId())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Usuário não é o proprietário deste anúncio"));
            }
            
            // Definir valores do pagamento - VALOR FIXO de R$ 3,00 para publicação
            BigDecimal amount = new BigDecimal("3.00"); // Valor fixo para publicação de anúncio
            long amountInCents = amount.multiply(new BigDecimal("100")).longValue(); // Converter para centavos (300 centavos)
            
            System.out.println("💰 Valor do pagamento: R$ " + amount + " (" + amountInCents + " centavos)");
            System.out.println("📋 Preço original da bicicleta: R$ " + advertisement.getPrice() + " (apenas para referência)");
            
            // Criar sessão de checkout do Stripe
            SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://bikehubbfrontend.netlify.app/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("https://bikehubbfrontend.netlify.app/cancel")
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("brl")
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("BikeHubb - Publicação de Anúncio")
                                        .setDescription("Publicação do anúncio: " + advertisement.getTitle())
                                        .build()
                                )
                                .setUnitAmount(amountInCents)
                                .build()
                        )
                        .setQuantity(1L)
                        .build()
                )
                .putMetadata("advertisement_id", advertisementId.toString())
                .putMetadata("user_external_id", externalId)
                .putMetadata("user_email", user.getEmail())
                .build();

            Session session = Session.create(params);
            
            System.out.println("✅ Sessão Stripe criada com ID: " + session.getId());
            System.out.println("🔗 URL de checkout: " + session.getUrl());
            
            // Criar resposta
            Map<String, Object> response = new HashMap<>();
            response.put("sessionId", session.getId());
            response.put("checkoutUrl", session.getUrl());
            response.put("advertisementId", advertisementId);
            response.put("amount", amount);
            response.put("status", "created");
            
            return ResponseEntity.ok(response);
            
        } catch (StripeException e) {
            System.err.println("❌ Erro do Stripe: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao criar sessão de pagamento: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Erro de validação: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Erro interno do servidor"));
        }
    }
}