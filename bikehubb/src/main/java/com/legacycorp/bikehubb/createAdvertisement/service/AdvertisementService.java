package com.legacycorp.bikehubb.createAdvertisement.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.legacycorp.bikehubb.createAdvertisement.dto.AdvertisementRequest;
import com.legacycorp.bikehubb.createAdvertisement.model.Bicycle;
import com.legacycorp.bikehubb.createAdvertisement.model.BikeImage;
import com.legacycorp.bikehubb.createAdvertisement.model.Bicycle.AdvertisementStatus;
import com.legacycorp.bikehubb.model.User;
import com.legacycorp.bikehubb.createAdvertisement.repository.AdvertisementRepository;
import com.legacycorp.bikehubb.createAdvertisement.repository.BikeImageRepository;
import com.legacycorp.bikehubb.createAdvertisement.repository.UserRepository;
import com.legacycorp.bikehubb.security.JwtUtil;

@Service
public class AdvertisementService {

    @Autowired
    private AdvertisementRepository advertisementRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BikeImageRepository bikeImageRepository;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public Bicycle createAdvertisement(AdvertisementRequest request, String externalId, String authToken) {
        
        User user = userRepository.findByExternalId(externalId)
            .orElseGet(() -> {
                // Usuário não existe, vamos criá-lo automaticamente
                System.out.println("Usuário não encontrado para ExternalId: " + externalId + ". Criando novo usuário...");
                
                try {
                    // Extrair informações do token JWT
                    String email = jwtUtil.extractUsername(authToken);
                    
                    // Verificar se já existe usuário com o mesmo email
                    if (userRepository.findByEmail(email).isPresent()) {
                        throw new RuntimeException("Já existe um usuário com o email: " + email);
                    }
                    
                    // Criar novo usuário com validações
                    User newUser = new User();
                    newUser.setExternalId(externalId);
                    newUser.setEmail(email);
                    
                    // Validar email antes de prosseguir
                    if (email == null || email.trim().isEmpty() || !email.contains("@")) {
                        throw new RuntimeException("Email inválido extraído do token: " + email);
                    }
                    
                    // Usar parte do email como nome temporário ou usar um padrão
                    String nome = email.split("@")[0];
                    if (nome.trim().isEmpty()) {
                        nome = "Usuario_" + System.currentTimeMillis();
                    }
                    newUser.setNome(nome);
                    
                    // Salvar o usuário no banco de dados
                    User savedUser = userRepository.save(newUser);
                    System.out.println("Novo usuário criado: " + savedUser.getNome() + " (ID: " + savedUser.getId() + ", Email: " + savedUser.getEmail() + ")");
                    
                    return savedUser;
                } catch (Exception e) {
                    System.err.println("Erro detalhado ao criar usuário: " + e.getClass().getSimpleName() + " - " + e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException("Erro ao criar usuário: " + e.getMessage(), e);
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
        
        // Definir valores padrão para campos que podem estar causando problemas
        bicycle.setPaymentIntentId(null); // Explicitamente null
        bicycle.setPaymentDate(null); // Explicitamente null  
        // Para status DRAFT, não definir published_at (deixar null)
        bicycle.setPublishedAt(null); // Explicitamente null

        // Log detalhado dos valores antes de salvar
        System.out.println("=== DADOS DA BICICLETA ANTES DE SALVAR ===");
        System.out.println("ID: " + bicycle.getId());
        System.out.println("Title: " + bicycle.getTitle());
        System.out.println("Description: " + bicycle.getDescription());
        System.out.println("Price: " + bicycle.getPrice());
        System.out.println("Category: " + bicycle.getCategory());
        System.out.println("Brand: " + bicycle.getBrand());
        System.out.println("Model: " + bicycle.getModel());
        System.out.println("Year: " + bicycle.getYear());
        System.out.println("Condition: " + bicycle.getCondition());
        System.out.println("FrameSize: " + bicycle.getFrameSize());
        System.out.println("Color: " + bicycle.getColor());
        System.out.println("City: " + bicycle.getCity());
        System.out.println("State: " + bicycle.getState());
        System.out.println("Neighborhood: " + bicycle.getNeighborhood());
        System.out.println("IsActive: " + bicycle.isActive());
        System.out.println("IsPaid: " + bicycle.isPaid());
        System.out.println("Status: " + bicycle.getStatus());
        System.out.println("Owner ID: " + bicycle.getOwner());
        System.out.println("Created At: " + bicycle.getCreatedAt());
        System.out.println("Expires At: " + bicycle.getExpiresAt());
        System.out.println("========================================");

        // Salvar no banco
        try {
            System.out.println("Iniciando salvamento da bicicleta...");
            Bicycle savedAdvertisement = advertisementRepository.save(bicycle);
            System.out.println("Anúncio salvo com sucesso. ID: " + savedAdvertisement.getId() + ", Owner ID: " + savedAdvertisement.getOwnerId());

            // Flush explícito para forçar o commit imediato
            advertisementRepository.flush();
            System.out.println("Flush executado com sucesso.");

            return savedAdvertisement;
        } catch (Exception e) {
            System.err.println("Erro detalhado ao salvar anúncio: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            
            // Tentar identificar a causa específica
            Throwable cause = e.getCause();
            while (cause != null) {
                System.err.println("Causa: " + cause.getClass().getSimpleName() + " - " + cause.getMessage());
                cause = cause.getCause();
            }
            
            throw new RuntimeException("Erro ao salvar anúncio: " + e.getMessage(), e);
        }
    }
    
    /**
     * Processa as imagens de um anúncio em uma transação separada
     * @param images Array de imagens para processar
     * @param advertisementId ID do anúncio
     */
    public void processAdvertisementImages(MultipartFile[] images, UUID advertisementId) {
        try {
            if (images != null && images.length > 0) {
                processImages(images, advertisementId);
            }
        } catch (Exception e) {
            System.err.println("Erro ao processar imagens para anúncio " + advertisementId + ": " + e.getMessage());
            // Não rethrow para não afetar a criação do anúncio
        }
    }

    private void processImages(MultipartFile[] images, UUID advertisementId) {
        try {
            System.out.println("Processando " + images.length + " imagens para o anúncio " + advertisementId);
            
            // Buscar a bicicleta para associar as imagens
            Bicycle bicycle = advertisementRepository.findById(advertisementId)
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado: " + advertisementId));
            
            // Processar cada imagem
            for (int i = 0; i < images.length; i++) {
                MultipartFile image = images[i];
                
                if (image.isEmpty()) {
                    System.out.println("Imagem vazia ignorada: " + image.getOriginalFilename());
                    continue;
                }
                
                // Validar tipo de arquivo
                String contentType = image.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    System.err.println("Tipo de arquivo inválido: " + contentType + " para " + image.getOriginalFilename());
                    continue;
                }
                
                // Validar tamanho (máximo 5MB)
                if (image.getSize() > 5 * 1024 * 1024) {
                    System.err.println("Arquivo muito grande: " + image.getSize() + " bytes para " + image.getOriginalFilename());
                    continue;
                }
                
                try {
                    // Criar entidade BikeImage
                    BikeImage bikeImage = new BikeImage();
                    bikeImage.setBicycle(bicycle);
                    bikeImage.setOriginalFilename(image.getOriginalFilename());
                    bikeImage.setContentType(contentType);
                    bikeImage.setFileSize(image.getSize());
                    bikeImage.setImageData(image.getBytes());
                    bikeImage.setPrimary(i == 0); // Primeira imagem é a principal
                    
                    // Salvar no banco
                    BikeImage savedImage = bikeImageRepository.save(bikeImage);
                    
                    System.out.println("Imagem salva com sucesso: " + savedImage.getOriginalFilename() + 
                                     " (ID: " + savedImage.getId() + ", Tamanho: " + savedImage.getFormattedSize() + ")");
                                     
                } catch (Exception e) {
                    System.err.println("Erro ao salvar imagem " + image.getOriginalFilename() + ": " + e.getMessage());
                }
            }
            
            System.out.println("Processamento de imagens concluído para anúncio " + advertisementId);
            
        } catch (Exception e) {
            System.err.println("Erro geral ao processar imagens para anúncio " + advertisementId + ": " + e.getMessage());
            throw new RuntimeException("Erro ao processar imagens", e);
        }
    }

    public List<Bicycle> getAllAdvertisements() {
        return advertisementRepository.findAll();
    }
    
    /**
     * Busca anúncios com filtros aplicados
     * @param state Estado
     * @param city Cidade  
     * @param neighborhood Bairro
     * @param minPrice Preço mínimo
     * @param maxPrice Preço máximo
     * @param condition Condição da bicicleta
     * @param category Categoria
     * @param brand Marca
     * @param sort Critério de ordenação
     * @return Lista de anúncios filtrados
     */
    public List<Bicycle> searchAdvertisements(String state, String city, String neighborhood, 
                                            String minPrice, String maxPrice, String condition, 
                                            String category, String brand, String sort) {
        
        // Primeiro, vamos verificar se há dados no banco
        List<Bicycle> allBicycles = advertisementRepository.findAllForTesting();
        System.out.println("=== DADOS NO BANCO ===");
        System.out.println("Total de bicicletas no banco: " + allBicycles.size());
        
        if (!allBicycles.isEmpty()) {
            Bicycle firstBicycle = allBicycles.get(0);
            System.out.println("Primeira bicicleta:");
            System.out.println("  ID: " + firstBicycle.getId());
            System.out.println("  Title: " + firstBicycle.getTitle());
            System.out.println("  Status: " + firstBicycle.getStatus());
            System.out.println("  State: " + firstBicycle.getState());
            System.out.println("  City: " + firstBicycle.getCity());
            System.out.println("  Brand: " + firstBicycle.getBrand());
            System.out.println("  Category: " + firstBicycle.getCategory());
            System.out.println("  Condition: " + firstBicycle.getCondition());
            System.out.println("  Price: " + firstBicycle.getPrice());
        }
        System.out.println("======================");
        
        // Converter strings de preço para BigDecimal se fornecidas
        BigDecimal minPriceDecimal = null;
        BigDecimal maxPriceDecimal = null;
        
        try {
            if (minPrice != null && !minPrice.trim().isEmpty()) {
                minPriceDecimal = new BigDecimal(minPrice);
            }
            if (maxPrice != null && !maxPrice.trim().isEmpty()) {
                maxPriceDecimal = new BigDecimal(maxPrice);
            }
        } catch (NumberFormatException e) {
            System.err.println("Erro ao converter preços: " + e.getMessage());
            // Continuar com valores null se conversão falhar
        }
        
        // Log dos filtros que serão aplicados
        System.out.println("=== FILTROS DE BUSCA APLICADOS ===");
        System.out.println("State: " + state);
        System.out.println("City: " + city);
        System.out.println("Neighborhood: " + neighborhood);
        System.out.println("Min Price: " + minPriceDecimal);
        System.out.println("Max Price: " + maxPriceDecimal);
        System.out.println("Condition: " + condition);
        System.out.println("Category: " + category);
        System.out.println("Brand: " + brand);
        System.out.println("Sort: " + sort);
        System.out.println("==================================");
        
        // Usar o método do repository com todos os filtros
        try {
            List<Bicycle> results = advertisementRepository.findAdvertisementsWithFilters(
                state, city, neighborhood, minPriceDecimal, maxPriceDecimal, 
                condition, category, brand, sort);
            
            System.out.println("Busca retornou " + results.size() + " resultados");
            
            // Carregar as imagens para cada bicicleta encontrada (se necessário)
            for (Bicycle bicycle : results) {
                // As imagens serão carregadas automaticamente devido ao relacionamento @OneToMany
                // Mas vamos forçar a inicialização da lista para evitar lazy loading issues
                if (bicycle.getImages() != null) {
                    bicycle.getImages().size(); // Force initialization
                }
            }
            
            // Verificar se as imagens foram carregadas
            if (!results.isEmpty()) {
                Bicycle firstResult = results.get(0);
                System.out.println("=== VERIFICAÇÃO DE IMAGENS ===");
                System.out.println("Primeira bicicleta do resultado: " + firstResult.getTitle());
                System.out.println("Número de imagens carregadas: " + firstResult.getImages().size());
                if (!firstResult.getImages().isEmpty()) {
                    System.out.println("Primeira imagem: " + firstResult.getImages().get(0).getOriginalFilename());
                    System.out.println("É primária: " + firstResult.getImages().get(0).isPrimary());
                }
                System.out.println("=============================");
            }
            
            return results;
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar anúncios: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar anúncios: " + e.getMessage(), e);
        }
    }

    public Bicycle getAdvertisementById(UUID id) {
        Bicycle bicycle = advertisementRepository.findByIdWithImages(id)
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
