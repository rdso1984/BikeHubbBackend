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

@Service
public class AdvertisementService {

    @Autowired
    private AdvertisementRepository advertisementRepository;
    
    @Autowired
    private UserRepository userRepository;

    public Bicycle createAdvertisement(AdvertisementRequest request, String userId) {
        // Buscar o usuário pelo UUID externo primeiro
        User user = userRepository.findByExternalId(userId)
            .orElseGet(() -> {
                // Se não encontrar pelo externalId, tentar pelo ID UUID como fallback
                try {
                    UUID userIdUUID = UUID.fromString(userId);
                    return userRepository.findById(userIdUUID)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Usuário não encontrado para ID: " + userId);
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
        bicycle.setFrameSize(request.getFrameSize());
        bicycle.setColor(request.getColor());
        
        // Localização
        bicycle.setCity(request.getCity());
        bicycle.setState(request.getState());
        bicycle.setNeighborhood(request.getNeighborhood());
        
        // Status
        bicycle.setActive(request.isActive());
        bicycle.setPaid(request.isPaid());
        bicycle.setStatus(AdvertisementStatus.DRAFT);
        UUID userIdUUID = UUID.fromString(userId);
        bicycle.setOwner(userIdUUID);
        bicycle.setCreatedAt(LocalDateTime.now());

        // Salvar no banco
        try {
            Bicycle savedAdvertisement = advertisementRepository.save(bicycle);
            System.out.println("Anúncio salvo com sucesso. ID: " + savedAdvertisement.getId() + ", Owner ID: " + savedAdvertisement.getOwnerId());
            
            // Processar imagens se existirem
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
        return advertisementRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));
    }
}
