package com.legacycorp.bikehubb.createAdvertisement.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.legacycorp.bikehubb.createAdvertisement.dto.AdvertisementRequest;
import com.legacycorp.bikehubb.createAdvertisement.model.Bicycle;
import com.legacycorp.bikehubb.createAdvertisement.model.Bicycle.AdvertisementStatus;
import com.legacycorp.bikehubb.model.User;
import com.legacycorp.bikehubb.createAdvertisement.repository.AdvertisementRepository;
import com.legacycorp.bikehubb.createAdvertisement.repository.UserRepository;
import com.legacycorp.bikehubb.security.JwtUtil;

@Service
public class AdvertisementService {

    @Autowired
    private AdvertisementRepository advertisementRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;

    public Bicycle createAdvertisement(AdvertisementRequest request, String externalId, String authToken) {
        
        User user = userRepository.findByExternalId(externalId)
            .orElseGet(() -> {
                // Usuário não existe, vamos criá-lo automaticamente
                System.out.println("Usuário não encontrado para ExternalId: " + externalId + ". Criando novo usuário...");
                
                try {
                    // Extrair informações do token JWT
                    String email = jwtUtil.extractUsername(authToken);
                    
                    // Criar novo usuário
                    User newUser = new User();
                    newUser.setExternalId(externalId);
                    newUser.setEmail(email);
                    newUser.setNome(email.split("@")[0]); // Usar parte do email como nome temporário
                    
                    // Salvar o usuário no banco de dados
                    User savedUser = userRepository.save(newUser);
                    System.out.println("Novo usuário criado: " + savedUser.getNome() + " (ID: " + savedUser.getId() + ", Email: " + savedUser.getEmail() + ")");
                    
                    return savedUser;
                } catch (Exception e) {
                    System.err.println("Erro ao criar usuário: " + e.getMessage());
                    throw new RuntimeException("Erro ao criar usuário: " + e.getMessage());
                }
            });

        System.out.println("Usuário encontrado: " + user.getNome() + " (ID: " + user.getId() + ", ExternalId: " + user.getExternalId() + ")");

        // Criar novo anúncio
        Bicycle bicycle = new Bicycle();
        bicycle.setTitle(request.getTitle());
        bicycle.setDescription(request.getDescription());
        bicycle.setPrice(BigDecimal.valueOf(request.getPrice()));
        bicycle.setCategory(request.getCategory());
        
        // Campos específicos da bicicleta
        bicycle.setBrand(request.getBrand());
        bicycle.setModel(request.getModel());
        bicycle.setYear(request.getYear());
        bicycle.setCondition(request.getCondition());
        bicycle.setFrameSize(request.getFrameSize()); //VINDO NULL
        bicycle.setColor(request.getColor());
        
        // Localização
        bicycle.setCity(request.getCity());
        bicycle.setState(request.getState());
        bicycle.setNeighborhood(request.getNeighborhood());
        
        // Status
        bicycle.setActive(request.isActive());
        bicycle.setPaid(request.isPaid());
        bicycle.setStatus(AdvertisementStatus.DRAFT);
        // Usar o ID do User encontrado (long), não o externalId
        bicycle.setOwnerId(user.getId());
        bicycle.setCreatedAt(LocalDateTime.now());
        bicycle.setExpiresAt(LocalDateTime.now().plusDays(60));

        // Salvar no banco
        try {
            Bicycle savedAdvertisement = advertisementRepository.save(bicycle);
            System.out.println("Anúncio salvo com sucesso. ID: " + savedAdvertisement.getId() + ", Owner ID: " + savedAdvertisement.getOwnerId());

            // Processar imagens se existirem -> TODO: Implementar upload das imagens
            if (request.getImages() != null && request.getImages().length > 0) {
                processImages(request.getImages(), savedAdvertisement.getId());
            }
            
            return savedAdvertisement;
        } catch (Exception e) {
            System.err.println("Erro ao salvar anúncio: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar anúncio: " + e.getMessage(), e);
        }
    }

    private void processImages(MultipartFile[] images, UUID advertisementId) {
        // TODO: Implementar upload das imagens
        // Aqui você pode:
        // 1. Salvar as imagens no sistema de arquivos ou cloud storage
        // 2. Salvar os caminhos das imagens em uma tabela separada no banco
        
        System.out.println("Processando " + images.length + " imagens para o anúncio " + advertisementId);
        
        Arrays.stream(images)
            .filter(image -> !image.isEmpty())
            .forEach(image -> {
                System.out.println("Processando imagem: " + image.getOriginalFilename());
                // Implementar lógica de upload
            });
    }

    public List<Bicycle> getAllAdvertisements() {
        return advertisementRepository.findAll();
    }

    public Bicycle getAdvertisementById(UUID id) {
        Bicycle bicycle = advertisementRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));
        
        // Verificar se o anúncio está expirado e atualizar status se necessário
        if (bicycle.isExpired() && bicycle.getStatus() != AdvertisementStatus.EXPIRED) {
            bicycle.setStatus(AdvertisementStatus.EXPIRED);
            advertisementRepository.save(bicycle);
        }
        
        return bicycle;
    }

    /**
     * Verifica e atualiza automaticamente anúncios expirados
     * @return número de anúncios atualizados
     */
    public int updateExpiredAdvertisements() {
        List<Bicycle> expiredAdvertisements = advertisementRepository.findExpiredAdvertisements(LocalDateTime.now());
        int updatedCount = 0;
        
        for (Bicycle bicycle : expiredAdvertisements) {
            bicycle.setStatus(AdvertisementStatus.EXPIRED);
            advertisementRepository.save(bicycle);
            updatedCount++;
            System.out.println("Anúncio expirado atualizado: " + bicycle.getId());
        }
        
        if (updatedCount > 0) {
            System.out.println("Total de anúncios expirados atualizados: " + updatedCount);
        }
        
        return updatedCount;
    }

    /**
     * Busca anúncios que expiram nos próximos X dias
     * @param days número de dias para verificar
     * @return lista de anúncios que expiram em breve
     */
    public List<Bicycle> getAdvertisementsExpiringInDays(int days) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime futureTime = currentTime.plusDays(days);
        return advertisementRepository.findAdvertisementsExpiringInDays(currentTime, futureTime);
    }

    /**
     * Renova a data de expiração de um anúncio
     * @param advertisementId ID do anúncio
     * @param days número de dias para adicionar (padrão 60)
     * @return anúncio atualizado
     */
    public Bicycle renewAdvertisementExpiration(UUID advertisementId, int days) {
        Bicycle bicycle = advertisementRepository.findById(advertisementId)
            .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));
        
        bicycle.renewExpiration(days);
        
        // Se estava expirado e foi renovado, voltar para o status anterior ou PUBLISHED
        if (bicycle.getStatus() == AdvertisementStatus.EXPIRED) {
            bicycle.setStatus(AdvertisementStatus.PUBLISHED);
        }
        
        Bicycle updated = advertisementRepository.save(bicycle);
        System.out.println("Expiração do anúncio renovada: " + advertisementId + " por " + days + " dias");
        
        return updated;
    }

    /**
     * Renova a data de expiração de um anúncio (padrão 60 dias)
     * @param advertisementId ID do anúncio
     * @return anúncio atualizado
     */
    public Bicycle renewAdvertisementExpiration(UUID advertisementId) {
        return renewAdvertisementExpiration(advertisementId, 60);
    }
}
