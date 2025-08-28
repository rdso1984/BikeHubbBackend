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
import com.legacycorp.bikehubb.createAdvertisement.model.Advertisement;
import com.legacycorp.bikehubb.createAdvertisement.model.Advertisement.AdvertisementStatus;
import com.legacycorp.bikehubb.model.User;
import com.legacycorp.bikehubb.createAdvertisement.repository.AdvertisementRepository;
import com.legacycorp.bikehubb.createAdvertisement.repository.UserRepository;

@Service
public class AdvertisementService {

    @Autowired
    private AdvertisementRepository advertisementRepository;
    
    @Autowired
    private UserRepository userRepository;

    public Advertisement createAdvertisement(AdvertisementRequest request, String userId) {
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
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle(request.getTitle());
        advertisement.setDescription(request.getDescription());
        advertisement.setPrice(BigDecimal.valueOf(request.getPrice()));
        advertisement.setCategory(request.getCategory());
        
        // Campos específicos da bicicleta
        advertisement.setBrand(request.getBrand());
        advertisement.setModel(request.getModel());
        advertisement.setYear(request.getYear());
        advertisement.setCondition(request.getCondition());
        advertisement.setFrameSize(request.getFrameSize());
        advertisement.setColor(request.getColor());
        
        // Localização
        advertisement.setCity(request.getCity());
        advertisement.setState(request.getState());
        advertisement.setNeighborhood(request.getNeighborhood());
        
        // Status
        advertisement.setActive(request.isActive());
        advertisement.setPaid(request.isPaid());
        advertisement.setStatus(AdvertisementStatus.DRAFT);
        UUID userIdUUID = UUID.fromString(userId);
        advertisement.setOwner(userIdUUID);
        advertisement.setCreatedAt(LocalDateTime.now());

        // Salvar no banco
        try {
            Advertisement savedAdvertisement = advertisementRepository.save(advertisement);
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

    public List<Advertisement> getAllAdvertisements() {
        return advertisementRepository.findAll();
    }

    public Advertisement getAdvertisementById(UUID id) {
        return advertisementRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));
    }
}
